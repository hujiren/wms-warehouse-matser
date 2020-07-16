package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.join.JoinKeyValues;
import com.apl.lib.join.JoinUtil;
import com.apl.lib.utils.DBUtil;
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
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.service.CancelAllocStockOrderService;
import com.apl.wms.warehouse.service.StocksService;
import com.apl.wms.warehouse.service.StorageLocalStocksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        YOUR_ORDER_IS_ALLOCATING("YOUR_ORDER_IS_ALLOCATING", "您的订单正在分配, 此时无法取消, 请稍后再试"),
        YOUR_ORDER_CAN_NOT_CANCEL("YOUR_ORDER_CAN_NOT_CANCEL", "您的订单已经分配拣货员, 已无法取消"),
        THERE_IS_NOT_ENOUGH_FREE_SPACE("THERE_IS_NOT_ENOUGH_FREE_SPACE","当前库存可用空间不足"),
        ORDER_STATUS_UPDATE_FAILED("ORDER_STATUS_UPDATE_FAILED", "订单状态更新失败"),
        REDIS_DOES_NOT_HAS_KEY("REDIS_GET_KEY_FAILED", "远程调用redis key 不存在"),
        START_CANCELING_ORDERS("START_CANCELING_ORDERS", "订单状态无异常,开始取消订单");

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
    RedisTemplate redisTemplate;

    @Autowired
    StocksHistoryFeign stocksHistoryFeign;


    /**
     * 手动取消分配
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
        if (!outOrderBoResult.getCode().equals(CommonStatusCode.GET_SUCCESS.code)) {

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

    public ResultUtil<Boolean> cancelAllocationStockByOrder(AllocationWarehouseOutOrderBo outOrderBo) throws Exception {

        //  1.通过切换数据源保存库存记录
        DBUtil.DBInfo dbinfo = stocksHistoryFeign.createDBinfo();
        Long whId = outOrderBo.getWhId();

        try {
                JoinKeyValues commodityIdJoinKeyValues = JoinUtil.getKeys(
                        outOrderBo.getAllocationWarehouseOrderCommodityBoList(),
                        "commodityId",
                        Long.class);


                //创建库存历史记录集合
                List<StocksHistoryPo> stocksHistoryPoList = new ArrayList<>();

                //取消分配总库存
                List<StocksPo> newStocksPos = this.CancelTotalStock(commodityIdJoinKeyValues, outOrderBo, whId, stocksHistoryPoList);

                //取消分配库位库存
                List<CompareStorageLocalStocksBo> compareStorageLocalStocksBoList = this.cancelStorageLocalStock(commodityIdJoinKeyValues, outOrderBo, stocksHistoryPoList);

                //切换数据源,开启事务
                dbinfo.dbUtil.beginTrans(dbinfo);

                //更新库位库存
                storageLocalStocksService.updateStorageLocalStock(compareStorageLocalStocksBoList);

                //更新总库存
                stocksService.updateTotalStock(newStocksPos);

                //保存库存历史记录列表
                stocksHistoryFeign.saveStocksHistoryPos(dbinfo, stocksHistoryPoList);

                //删除拣货明细
                deleteOrderAllocationItem(outOrderBo.getOrderId());

                // 库存历史记录事务提交
                dbinfo.dbUtil.commit(dbinfo);


        } catch (Exception e) {

            dbinfo.dbUtil.rollback(dbinfo);
            throw new AplException(CommonStatusCode.SAVE_FAIL.code, CommonStatusCode.SAVE_FAIL.msg);

        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, CommonStatusCode.SYSTEM_SUCCESS);
    }



    /**
     * 取消分配总库存
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


        //库位库存更新对象集合
        List<StocksPo> stocksPoList = new ArrayList<>();

        String key = null;

        for (AllocationWarehouseOrderCommodityBo commodityBo : commodityBoList) {

            key = whId + "_" + commodityBo.getCommodityId();
            StocksBo stocksBo = null;
            //构建库位库存更新对象
            StocksPo stocksPo = new StocksPo();

                //总库存对象
                stocksBo = StocksBoMap.get(key);

                //如果冻结库存容量大于即将退回库存的数量
            if (stocksBo.getFreezeStockCount() > commodityBo.getOrderQty()) {

                stocksPo.setAvailableStockCount(stocksBo.getAvailableStockCount() + commodityBo.getOrderQty());
                stocksPo.setFreezeStockCount(stocksBo.getFreezeStockCount() - commodityBo.getOrderQty());
                stocksPo.setWhId(stocksBo.getWhId());
                stocksPo.setId(stocksBo.getId());
                stocksPo.setCommodityId(stocksBo.getCommodityId());

                stocksPoList.add(stocksPo);

            }else{

                throw new AplException(CancelAllocationWarehouseServiceCode.THERE_IS_NOT_ENOUGH_FREE_SPACE.code,
                        CancelAllocationWarehouseServiceCode.THERE_IS_NOT_ENOUGH_FREE_SPACE.msg);
            }


            //构建库存历史记录对象
            StocksHistoryPo shp = new StocksHistoryPo();
            shp.setOrderType(2);
            shp.setOrderId(commodityBo.getOrderId());
            shp.setInQty(0);
            shp.setOutQty(-commodityBo.getOrderQty());
            shp.setWhId(whId);
            shp.setStorageLocalId(0L);
            shp.setStocksQty(stocksBo.getAvailableStockCount() + commodityBo.getOrderQty());
            shp.setOrderSn(outOrderBo.getOrderSn());
            shp.setOperatorTime(LocalDateTime.now());
            shp.setCommodityId(commodityBo.getCommodityId());
            stocksHistoryPoList.add(shp);

        }

        return stocksPoList;

    }



    /**
     * 取消分配库位库存
     * @param joinKeyValues
     * @param outOrderBo
     * @param stocksHistoryPoList
     * @return
     * @throws Exception
     */
    public List<CompareStorageLocalStocksBo> cancelStorageLocalStock(JoinKeyValues joinKeyValues,
                                        AllocationWarehouseOutOrderBo outOrderBo,
                                        List<StocksHistoryPo> stocksHistoryPoList) throws Exception {

        //统一订单商品列表
        List<AllocationWarehouseOrderCommodityBo> commodityList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();

        //根据商品id查询库位库存对象集合
        List<StorageLocalStocksPo> storageStocksPos = baseMapper.queryStorageLocalStock(joinKeyValues.getSbKeys(), joinKeyValues.getMinKey(), joinKeyValues.getMaxKey());

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
                    stocksHistoryPoList,
                    compareStorageLocalStocksBos);
        }

        return compareStorageLocalStocksBos;
    }



    /**
     * 构建库位库存对象及库位出库记录对象
     * @param orderSn
     * @param whId
     * @param orderCommodityBo
     * @param storageLocalStockList
     * @param stocksHistoryPos
     * @param compareStorageLocalStocksBos
     * @return
     */
    private List<CompareStorageLocalStocksBo> buildFallbackData(String orderSn, Long whId,
                                                                           AllocationWarehouseOrderCommodityBo orderCommodityBo,
                                                                           List<StorageLocalStocksPo> storageLocalStockList,
                                                                           List<StocksHistoryPo> stocksHistoryPos,
                                                                           List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos) {
        //单个商品的出库数量
        Integer compareQty = orderCommodityBo.getOrderQty();

        for (StorageLocalStocksPo stocksPo : storageLocalStockList) {

            //构建库位库存更新对象
            CompareStorageLocalStocksBo compareStorageLocalStocksBo = new CompareStorageLocalStocksBo();
            //构建库位库存记录对象
            StocksHistoryPo shp = new StocksHistoryPo();

                //如果当前库位放得下回退的商品
                if(stocksPo.getFreezeCount() >= compareQty){

                    compareStorageLocalStocksBo.setAvailableCount(stocksPo.getAvailableCount() + compareQty);
                    compareStorageLocalStocksBo.setFreezeCount(stocksPo.getFreezeCount() - compareQty);
                    compareStorageLocalStocksBo.setAllocationQty(compareQty);
                    shp.setOutQty(-compareQty);


                }else{

                    //放不下则有多少放多少, 剩下的放下一个库位
                    compareQty -= stocksPo.getFreezeCount();
                    compareStorageLocalStocksBo.setAvailableCount(stocksPo.getAvailableCount() + stocksPo.getFreezeCount());
                    compareStorageLocalStocksBo.setAllocationQty(stocksPo.getFreezeCount());
                    shp.setOutQty(-stocksPo.getFreezeCount());
                    compareStorageLocalStocksBo.setFreezeCount(0);

                }

            compareStorageLocalStocksBo.setCommodityId(orderCommodityBo.getCommodityId());
            compareStorageLocalStocksBo.setId(stocksPo.getId());
            compareStorageLocalStocksBo.setStorageLocalId(stocksPo.getStorageLocalId());

            compareStorageLocalStocksBos.add(compareStorageLocalStocksBo);


            shp.setOrderType(2);
            shp.setCommodityId(orderCommodityBo.getCommodityId());
            shp.setOperatorTime(LocalDateTime.now());
            shp.setOrderSn(orderSn);
            shp.setWhId(whId);
            shp.setInQty(0);
            shp.setOrderId(orderCommodityBo.getOrderId());
            shp.setStorageLocalId(stocksPo.getStorageLocalId());

            stocksHistoryPos.add(shp);

        }

        return compareStorageLocalStocksBos;
    }


    /**
     * 跨项目删除拣货明细
     * @param outOrderId
     * @return
     */
    public Integer deleteOrderAllocationItem(Long outOrderId) {

        //生成一个唯一的事务id , 用来校验远程调用是否成功
        String tranId = "tranId:" + StringUtil.generateUuid();

        ResultUtil<Integer> result = outStorageOrderOperatorFeign.deleteOrderAllocationItem(outOrderId);

        if (!redisTemplate.hasKey(tranId)) {

            //如果远程调用失败, redis中key(事务id)将为空
            throw new AplException(CancelAllocationWarehouseServiceCode.REDIS_DOES_NOT_HAS_KEY.code,
                    CancelAllocationWarehouseServiceCode.REDIS_DOES_NOT_HAS_KEY.msg, null);

        }

        redisTemplate.delete(tranId);

        return result.getData();
    }
}





