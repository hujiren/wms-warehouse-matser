package com.apl.wms.warehouse.service.impl;

import com.apl.cache.AplCacheUtil;
import com.apl.db.adb.AdbContext;
import com.apl.db.adb.AdbTransactional;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.join.JoinKeyValues;
import com.apl.lib.join.JoinUtil;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.StringUtil;
import com.apl.wms.outstorage.order.lib.feign.OutStorageOrderOperatorFeign;
import com.apl.wms.outstorage.order.lib.pojo.bo.AllocationWarehouseOrderCommodityBo;
import com.apl.wms.outstorage.order.lib.pojo.bo.AllocationWarehouseOutOrderBo;
import com.apl.wms.warehouse.bo.StocksBo;
import com.apl.wms.warehouse.dao.CancelAllocStockOrderMapper;
import com.apl.wms.warehouse.lib.feign.StocksHistoryFeign;
import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.lib.pojo.po.StocksHistoryPo;
import com.apl.wms.warehouse.lib.pojo.po.StorageLocalStocksHistoryPo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.service.CancelAllocStockOrderService;
import com.apl.wms.warehouse.service.StocksService;
import com.apl.wms.warehouse.service.StorageLocalStocksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjr start
 * @date 2020/7/14 - 9:05
 */

@Service
@Slf4j
public class CancelAllocStockAllocStockOrderServiceImpl extends ServiceImpl<CancelAllocStockOrderMapper, StocksPo> implements CancelAllocStockOrderService {


    //状态code枚举
    enum CancelAllocationWarehouseServiceCode {

        THE_ORDER_QTY_IS_WRONG("THE_ORDER_QTY_IS_WRONG", "分配数量不正确"),
        REDIS_DOES_NOT_HAS_KEY("REDIS_GET_KEY_FAILED", "远程调用redis key 不存在"),
        CANCEL_ALLOCATION_STOCK_SUCCESS("CANCEL_ALLOCATION_STOCK_SUCCESS", "取消分配成功 ");
        ;

        private String code;
        private String msg;

        CancelAllocationWarehouseServiceCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    @Autowired
    private StocksService stocksService;

    @Autowired
    private StorageLocalStocksService storageLocalStocksService;

    @Autowired
    private OutStorageOrderOperatorFeign outStorageOrderOperatorFeign;

    @Autowired
    AplCacheUtil redisTemplate;

    @Autowired
    StocksHistoryFeign stocksHistoryFeign;


    /**
     * 手动取消分配
     *
     * @param outOrderId
     * @return
     */
    @Override
    @Transactional
    public ResultUtil<Boolean> cancelAllocationManual(Long outOrderId) throws Exception {

        //  1.通过feign远程调用获取包含商品信息集合在内的订单信息对象返回集
        ResultUtil<AllocationWarehouseOutOrderBo> outOrderBoResult =
                outStorageOrderOperatorFeign.getOrderForCancelAllocationWarehouseManual(outOrderId);

        //如果没有拿到相应的数据, 则返回相应的状态信息
        if (!outOrderBoResult.getCode().equals("GET_SUCCESS")) {

            log.info("method cancelAllocationManual in class CancelAllocStockAllocStockOrderServiceImpl query order fail!! outOrderId:"
                    + outOrderId.toString());
            return ResultUtil.APPRESULT(outOrderBoResult.getCode(), outOrderBoResult.getMsg(), false);

        }

        // 2.从返回集中将订单信息对象提取出来
        AllocationWarehouseOutOrderBo outOrderBo = outOrderBoResult.getData();

        ResultUtil<Boolean> ResultBoolean = cancelAllocationStockByOrder(outOrderBo);

        return ResultBoolean;
    }


    /**
     * 队列取消分配
     *
     * @param outOrderBo
     * @return
     */
    @Override
    @Transactional
    public Boolean cancelAllocationByQueue(AllocationWarehouseOutOrderBo outOrderBo) throws Exception {

        // 1.为从消息队列中取到的对象分配库存, 总库存, 库位库存, 返回值结果为false或true
        ResultUtil<Boolean> result = cancelAllocationStockByOrder(outOrderBo);
        Boolean data = result.getData();
        return data;
    }


    /**
     * 取消分配
     */
    public ResultUtil<Boolean> cancelAllocationStockByOrder(AllocationWarehouseOutOrderBo outOrderBo) {

        //  1.通过切换数据源保存库存记录
        AdbContext dbInfo = stocksHistoryFeign.connectDb();
        Long whId = outOrderBo.getWhId();

        try {
            JoinKeyValues commodityIdJoinKeyValues = JoinUtil.getKeys(
                    outOrderBo.getAllocationWarehouseOrderCommodityBoList(),
                    "commodityId",
                    Long.class);


            //创建总库存历史记录集合
            List<StocksHistoryPo> stocksHistoryPoList = new ArrayList<>();

            //创建库位库存历史记录列表
            List<StorageLocalStocksHistoryPo> storageLocalStocksHistoryPos = new ArrayList<>();

            //取消分配总库存
            List<StocksPo> newStocksPos = this.CancelTotalStock(commodityIdJoinKeyValues, outOrderBo, whId, stocksHistoryPoList);

            //取消分配库位库存
            List<CompareStorageLocalStocksBo> compareStorageLocalStocksBoList = this.cancelStorageLocalStock(commodityIdJoinKeyValues, outOrderBo, storageLocalStocksHistoryPos);

            //切换数据源,开启事务
            AdbTransactional.beginTrans(dbInfo);

            //更新总库存
            stocksService.updateTotalStock(newStocksPos);

            //更新库位库存
            storageLocalStocksService.updateStorageLocalStock(compareStorageLocalStocksBoList);

            //删除拣货明细
            deleteOrderAllocationItem(outOrderBo.getOrderId());

            //保存库存历史记录列表
            stocksHistoryFeign.saveStocksHistoryPos(dbInfo, stocksHistoryPoList, storageLocalStocksHistoryPos);


            // 库存历史记录事务提交
            AdbTransactional.commit(dbInfo);


        } catch (Exception e) {

            AdbTransactional.rollback(dbInfo);
            throw new AplException(CommonStatusCode.SAVE_FAIL.code, CommonStatusCode.SAVE_FAIL.msg);

        }

        return ResultUtil.APPRESULT(CancelAllocationWarehouseServiceCode.CANCEL_ALLOCATION_STOCK_SUCCESS.code,
                CancelAllocationWarehouseServiceCode.CANCEL_ALLOCATION_STOCK_SUCCESS.msg, true);
    }


    /**
     * 取消分配总库存
     *
     * @param joinKeyValues
     * @param outOrderBo
     * @param whId
     * @param stocksHistoryPoList
     * @return
     */
    private List<StocksPo> CancelTotalStock(JoinKeyValues joinKeyValues,
                                            AllocationWarehouseOutOrderBo outOrderBo,
                                            Long whId,
                                            List<StocksHistoryPo> stocksHistoryPoList) {

        List<AllocationWarehouseOrderCommodityBo> commodityBoList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();

        //查询该订单中多个商品的总库存  keys:whId_commodityId
        Map<String, StocksBo> StocksBoMap = baseMapper.queryTotalStock(whId,
                joinKeyValues.getSbKeys().toString(),
                joinKeyValues.getMinKey(),
                joinKeyValues.getMaxKey());

        //库存更新对象集合
        List<StocksPo> stocksPoList = new ArrayList<>();

        String key = null;

        for (AllocationWarehouseOrderCommodityBo commodityBo : commodityBoList) {

            key = whId.toString() + "_" + commodityBo.getCommodityId().toString();
            StocksBo stocksBo = null;
            //构建总库存更新对象
            StocksPo stocksPo = new StocksPo();

            //总库存对象
            stocksBo = StocksBoMap.get(key);

            if(commodityBo.getOrderQty() > 0) {

                stocksPo.setAvailableCount(stocksBo.getAvailableCount() + commodityBo.getOrderQty());
                stocksPo.setRealityCount(stocksBo.getRealityCount() - commodityBo.getOrderQty());
                stocksPo.setWhId(stocksBo.getWhId());
                stocksPo.setId(stocksBo.getId());
                stocksPo.setCommodityId(stocksBo.getCommodityId());

                stocksPoList.add(stocksPo);

            } else {

                throw new AplException(CancelAllocationWarehouseServiceCode.THE_ORDER_QTY_IS_WRONG.code, CancelAllocationWarehouseServiceCode.THE_ORDER_QTY_IS_WRONG.msg, null);

            }

            //构建库存历史记录对象
            StocksHistoryPo shp = new StocksHistoryPo();
            shp.setOrderType(2);
            shp.setOrderId(commodityBo.getOrderId());
            shp.setInQty(0);
            shp.setOutQty(-commodityBo.getOrderQty());
            shp.setWhId(0L);
            shp.setStocksQty(stocksBo.getAvailableCount() + commodityBo.getOrderQty());
            shp.setOrderSn(outOrderBo.getOrderSn());
            shp.setOperatorTime(new Timestamp(System.currentTimeMillis()));
            shp.setCommodityId(commodityBo.getCommodityId());
            shp.setStocksType(1);
            stocksHistoryPoList.add(shp);

        }

        return stocksPoList;

    }


    /**
     * 取消分配库位库存
     *
     * @param joinKeyValues
     * @param outOrderBo
     * @param
     * @return
     * @throws Exception
     */
    public List<CompareStorageLocalStocksBo> cancelStorageLocalStock(JoinKeyValues joinKeyValues,
                                                                     AllocationWarehouseOutOrderBo outOrderBo,
                                                                     List<StorageLocalStocksHistoryPo> storageLocalStocksHistoryPos) throws Exception {

        //统一订单商品列表
        List<AllocationWarehouseOrderCommodityBo> commodityList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();

        //根据商品id查询库位库存对象集合
        List<StorageLocalStocksPo> storageStocksPos = baseMapper.queryStorageLocalStock(joinKeyValues.getSbKeys().toString(), joinKeyValues.getMinKey(), joinKeyValues.getMaxKey());

        //将库位库存集合根据商品id进行分组
        LinkedHashMap<String, List<StorageLocalStocksPo>> storageLocalStockMap = JoinUtil.listGrouping(storageStocksPos, "commodityId");

        //构建库位库存集合对象
        List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos = new ArrayList<>();

        for (AllocationWarehouseOrderCommodityBo commodityBo : commodityList) {

            //通过商品id得到对应的库位库存对象集合
            List<StorageLocalStocksPo> storageLocalStockList = storageLocalStockMap.get(commodityBo.getCommodityId().toString());

            this.buildFallbackData(outOrderBo.getOrderSn(), outOrderBo.getWhId(),
                    commodityBo,
                    storageLocalStockList,
                    storageLocalStocksHistoryPos,
                    compareStorageLocalStocksBos);
        }

        return compareStorageLocalStocksBos;
    }


    /**
     * 构建库位库存对象及库位出库记录对象
     *
     * @param orderSn
     * @param whId
     * @param orderCommodityBo
     * @param storageLocalStockList
     * @param compareStorageLocalStocksBos
     * @return
     */
    private List<CompareStorageLocalStocksBo> buildFallbackData(String orderSn, Long whId,
                                                                AllocationWarehouseOrderCommodityBo orderCommodityBo,
                                                                List<StorageLocalStocksPo> storageLocalStockList,
                                                                List<StorageLocalStocksHistoryPo> storageLocalStocksHistoryPos,
                                                                List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos) {
        //单个商品的出库数量
        Integer compareQty = orderCommodityBo.getOrderQty();

        for (StorageLocalStocksPo stocksPo : storageLocalStockList) {

            //构建库位库存更新对象
            CompareStorageLocalStocksBo compareStorageLocalStocksBo = new CompareStorageLocalStocksBo();
            //构建库位库存记录对象
            StorageLocalStocksHistoryPo shp = new StorageLocalStocksHistoryPo();

            //如果当前库位放得下回退的商品

            compareStorageLocalStocksBo.setAvailableCount(stocksPo.getAvailableCount() + compareQty);
            compareStorageLocalStocksBo.setAllocationQty(compareQty);


            compareStorageLocalStocksBo.setCommodityId(orderCommodityBo.getCommodityId());
            compareStorageLocalStocksBo.setId(stocksPo.getId());
            compareStorageLocalStocksBo.setStorageLocalId(stocksPo.getStorageLocalId());

            compareStorageLocalStocksBos.add(compareStorageLocalStocksBo);

            shp.setOutQty(-compareQty);
            shp.setOrderType(2);
            shp.setCommodityId(orderCommodityBo.getCommodityId());
            shp.setOperatorTime(new Timestamp(System.currentTimeMillis()));
            shp.setOrderSn(orderSn);
            shp.setWhId(whId);
            shp.setInQty(0);
            shp.setOrderId(orderCommodityBo.getOrderId());
            shp.setStocksQty(compareStorageLocalStocksBo.getAvailableCount());
            shp.setStocksType(1);
            storageLocalStocksHistoryPos.add(shp);

        }

        return compareStorageLocalStocksBos;
    }


    /**
     * 跨项目删除拣货明细
     *
     * @param outOrderId
     * @return
     */
    public Integer deleteOrderAllocationItem(Long outOrderId) {

        //生成一个唯一的事务id , 用来校验远程调用是否成功
        String tranId = "tranId:" + StringUtil.generateUuid();

        ResultUtil<Integer> result = outStorageOrderOperatorFeign.deleteOrderAllocationItem(outOrderId, tranId);


        if (!redisTemplate.hasKey(tranId)) {

            //如果远程调用失败, redis中key(事务id)将为空
            throw new AplException(CancelAllocationWarehouseServiceCode.REDIS_DOES_NOT_HAS_KEY.code,
                    CancelAllocationWarehouseServiceCode.REDIS_DOES_NOT_HAS_KEY.msg, null);

        }

        redisTemplate.delete(tranId);

        return result.getData();
    }
}





