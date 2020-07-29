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
import com.apl.wms.warehouse.dao.AllocationStockOrderMapper;
import com.apl.wms.warehouse.lib.feign.StocksHistoryFeign;
import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.lib.pojo.po.StocksHistoryPo;
import com.apl.wms.warehouse.lib.pojo.po.StorageLocalStocksHistoryPo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.service.AllocationStockOrderService;
import com.apl.wms.warehouse.service.StocksService;
import com.apl.wms.warehouse.service.StorageLocalStocksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjr start
 * @date 2020/7/6 - 15:15
 */
@Service
@Slf4j
public class AllocationStockOrderServiceImpl extends ServiceImpl<AllocationStockOrderMapper, StocksPo> implements AllocationStockOrderService {

    //状态code枚举
    enum AllocationWarehouseServiceCode {
         INSERT_PULL_FAIL("INSERT_PULL_FAIL" ,"插入分配库存对象失败"),
         UNDER_STOCK("UNDER_STOCK", "库存不足"),
         ALLOCATION_STOCKS_SUCCESS("ALLOCATION_STOCKS_SUCCESS", "库存分配成功");


        private String code;
        private String msg;

        AllocationWarehouseServiceCode(String code, String msg) {
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

    static final ThreadLocal<AllocationWarehouseContextInfo> contextHolder = new ThreadLocal();

    public class AllocationWarehouseContextInfo{
        //仓库id
        public Long whId;

        // 库存记录
        public List<StocksHistoryPo> stocksHistoryPos;
    }

    @Autowired
    AplCacheUtil redisTemplate;

    @Autowired
    StocksHistoryFeign stocksHistoryFeign;


    /**
     * 根据订单<手动>分配仓库
     * 手动分配, 队列分配都是取单个订单进行分配
     * @//
     *      1.通过feign远程调用获取包含商品信息集合在内的订单信息对象返回集
     *      2.从返回集中将订单信息对象提取出来
     */
    @Override
    @Transactional
    public ResultUtil<Boolean> allocationManual(Long outOrderId)  {

        //  1.通过feign远程调用获取包含商品信息集合在内的订单信息对象返回集
        ResultUtil<AllocationWarehouseOutOrderBo> outOrderBoResult =
                outStorageOrderOperatorFeign.getOrderByAllocationWarehouseManual(outOrderId);

        //如果没有拿到相应的数据, 则返回相应的状态信息
        if (!outOrderBoResult.getCode().equals(CommonStatusCode.GET_SUCCESS.code)) {
            log.info("method allocationManual in class AllocationStockOrderServiceImpl query order fail!! outOrderId:" + outOrderId.toString());
            return ResultUtil.APPRESULT(outOrderBoResult.getCode(), outOrderBoResult.getMsg(), false);
        }

        // 2.从返回集中将订单信息对象提取出来
        AllocationWarehouseOutOrderBo outOrderBo = outOrderBoResult.getData();

        ResultUtil<Boolean> ResultBoolean = allocationStockByOrder(outOrderBo);

        return ResultBoolean;
    }




    /**
     * 给<消息队列>中取出的订单对象进行分配仓库(消息队列是将对象集合进行遍历, 逐个对象进行发送)
     * 通过订单的下单数量和商品id查询对应的总库存并完成减库存
     * 消息队列是不走controller的, 直接调用service层的allocationByQueue方法 ↓
     * @//
     *      1.为从消息队列中取到的对象分配库存, 总库存, 库位库存
     */
    @Override
    @Transactional
    public Boolean allocationByQueue(AllocationWarehouseOutOrderBo outOrderBo) {

        // 1.为从消息队列中取到的对象分配库存, 总库存, 库位库存, 返回值结果为false或true
        ResultUtil<Boolean> result = allocationStockByOrder(outOrderBo);
        Boolean data = result.getData();
        return data;
    }





    /**
     * 统一为手动分配或队列分配的对象分配 总库存 , 库位库存
     * @param outOrderBo
     * @//
     *      1.通过切换数据源保存库存记录
     *      2.遍历, 取出所有商品信息列表对象, 按商品id排序
     *      3.调用对比总库存方法, 将循环对比总库存
     *      4.如果总库存充足, 则进行对比库位库存
     */
    public ResultUtil<Boolean> allocationStockByOrder(AllocationWarehouseOutOrderBo outOrderBo)  {

        //  1.通过切换数据源保存库存记录
        AdbContext adbContext = stocksHistoryFeign.connectDb();

        try {

            JoinKeyValues commodityIdJoinKeyValues = JoinUtil.getKeys(
                    outOrderBo.getAllocationWarehouseOrderCommodityBoList(),
                    "commodityId",
                    Long.class);

            //新建总库存历史记录列表
            List<StocksHistoryPo> stocksHistoryPos = new ArrayList<>();

            //新建库位库存历史记录
            List<StorageLocalStocksHistoryPo> storageLocalStocksHistoryPos = new ArrayList<>();

            // 3.调用对比总库存方法, 将循环对比总库存
            List<StocksPo> newStocksPos = checkTotalStock(outOrderBo, commodityIdJoinKeyValues, stocksHistoryPos);

            // 4.如果总库存充足, 则进行对比库位库存
            if (null != newStocksPos && newStocksPos.size() > 0) {
                // whId > 0 表示总库存足够, 进行库位库存比较
                Long whId = newStocksPos.get(0).getWhId();//所有的库存对象仓库id都相同, 所有的商品都在一个库
                outOrderBo.setWhId(whId);

                //为订单set好仓库id, 进行对比库位库存
                List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos = checkStorageLocalStockByOrder(outOrderBo, commodityIdJoinKeyValues, storageLocalStocksHistoryPos);

                // 更新库位库存
                Integer updateStorageLocalStockResult = storageLocalStocksService.updateStorageLocalStock(compareStorageLocalStocksBos);

                //更新总库存
                Integer updateTotalStockResult = stocksService.updateTotalStock(newStocksPos);

                //如果更新总库存和更新库位库存执行成功则保存此次出库记录, 并更改状态为已分配仓库
                if(updateStorageLocalStockResult > 0 && updateTotalStockResult > 0) {
                    //切换数据源,开启事务
                    AdbTransactional.beginTrans(adbContext);

                    //保存总库存历史记录列表和库位库存历史记录列表
                    stocksHistoryFeign.saveStocksHistoryPos(adbContext, stocksHistoryPos, storageLocalStocksHistoryPos);

                    AllocaOutOrderStockCallBack(outOrderBo.getOrderId(), 3, compareStorageLocalStocksBos);
                }

                // 库存历史记录事务提交
                AdbTransactional.commit(adbContext);
            }else{

                //库存不足, 恢复订单拣货状态为1(未分配库存)
                List<CompareStorageLocalStocksBo> listEmpty = new ArrayList<>();
                AllocaOutOrderStockCallBack(outOrderBo.getOrderId(), 1 ,listEmpty);

                return ResultUtil.APPRESULT(AllocationWarehouseServiceCode.UNDER_STOCK.code, AllocationWarehouseServiceCode.UNDER_STOCK.msg, false);
            }
        }
        catch (Exception e){
            log.error(MessageFormat.format("{0},  outOrderId: {1}", e.getMessage(), outOrderBo.getOrderId()));

            AdbTransactional.rollback(adbContext);
            List<CompareStorageLocalStocksBo> listEmpty = new ArrayList<>();
            AllocaOutOrderStockCallBack(outOrderBo.getOrderId(), 0, listEmpty);

            throw new AplException(CommonStatusCode.SAVE_FAIL.code, CommonStatusCode.SAVE_FAIL.msg);
        }

        return ResultUtil.APPRESULT(AllocationWarehouseServiceCode.ALLOCATION_STOCKS_SUCCESS.code,
                AllocationWarehouseServiceCode.ALLOCATION_STOCKS_SUCCESS.msg,  true);
    }



    /**
     * 对比总库存
     * @param outOrderBo 单个订单信息对象
     * @param commodityIdJoinKeyValues 调用JoinUtil.getKeys方法遍历商品信息集合并排序后的对象, 内含商品信息集合
     * @param stocksHistoryPos 库存历史记录对象集合, 每一次库存减少都要生成一个对象插入数据库做记录
     * @return
     */
    private List<StocksPo> checkTotalStock(AllocationWarehouseOutOrderBo outOrderBo,
                                           JoinKeyValues commodityIdJoinKeyValues,
                                           List<StocksHistoryPo> stocksHistoryPos)  {
        //通过订单对象取出商品信息对象列表集合
        List<AllocationWarehouseOrderCommodityBo> commodityBoList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();

        Long whId = outOrderBo.getWhId();

        //查询该订单中多个商品的总库存  keys:whId_commodityId(10_16) 使用Map可以指定key方便取出指定的数据
        Map<String , StocksBo> stocksPoMap = baseMapper.queryTotalStock(whId,
                commodityIdJoinKeyValues.getSbKeys().toString(),
                commodityIdJoinKeyValues.getMinKey(),
                commodityIdJoinKeyValues.getMaxKey());

        if(null==stocksPoMap || stocksPoMap.size()==0){
            // 没有库存返回空
            return null;
        }

        if(whId==0) {//如果仓库id等于0, 表示未指定仓库
            for (Map.Entry<String, StocksBo> entry : stocksPoMap.entrySet()) {
                //随机取一个此订单中商品的仓库id, 一个订单中的商品不可能出现在两个仓库, 只有一个仓库id
                // todo 此算法有不足, 可能会取到库存不足的仓库id, 后面补充改进
                whId = entry.getValue().getWhId();
                break;
            }
        }

        //需要更新到数据库的总库存对象集合
        List<StocksPo> newStocksPos = new ArrayList<>();

        //比对多个商品与总库存
        String key;
        for (AllocationWarehouseOrderCommodityBo orderCommodityBo : commodityBoList) {
            //通过拼接一个key, 可获得从数据库中查询到的总库存的对象
            key = whId.toString()+"_"+orderCommodityBo.getCommodityId().toString();

            //获取指定商品对应的总库存
            StocksBo stocksBo = stocksPoMap.get(key);//如果没有库存中没有该商品, key将不存在, stockBo也为空
            if(stocksBo == null){
                //其中一个商品, 可售库存为空
                return null;
            }
            Integer orderQty = orderCommodityBo.getOrderQty();

            //将指定的商品的出库数量与对应的实际的库存做比较
            if(stocksBo.getAvailableCount() < orderQty){
                //其中一个商品, 可售库存不足
                return null;
            }

            // 构建总库存对象, 每出库一件商品将更新一次总库存
            Integer newAvailableCount = stocksBo.getAvailableCount() - orderQty;//新的可售库存
            StocksPo newStocksPo = new StocksPo();
            newStocksPo.setId(stocksBo.getId());
            newStocksPo.setWhId(whId);
            newStocksPo.setAvailableCount(newAvailableCount);
            newStocksPos.add(newStocksPo);

            //构建总库存记录对象
            StocksHistoryPo shp = new StocksHistoryPo();
            shp.setOrderType(2);
            shp.setOrderId(orderCommodityBo.getOrderId());
            shp.setInQty(0);
            shp.setOutQty(orderCommodityBo.getOrderQty());
            shp.setWhId(whId);
            shp.setStocksQty(newAvailableCount);
            shp.setOrderSn(outOrderBo.getOrderSn());
            shp.setOperatorTime(new Timestamp(System.currentTimeMillis()));
            shp.setCommodityId(orderCommodityBo.getCommodityId());
            shp.setStocksType(1);
            stocksHistoryPos.add(shp);
        }

        //所有商品库存都够, 返回总库存对象集合, 在上一层方法中统一做更新操作
        return newStocksPos;
    }


    //按订单, 检查库位库存
    private List<CompareStorageLocalStocksBo> checkStorageLocalStockByOrder(
                                                                       AllocationWarehouseOutOrderBo outOrderBo,
                                                                       JoinKeyValues commodityIdJoinKeyValues,
                                                                       List<StorageLocalStocksHistoryPo> storageLocalStocksHistoryPos) throws Exception {


        //通过订单对象取出商品对象列表集合
        List<AllocationWarehouseOrderCommodityBo> commodityBoList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();


        //通过商品id查询出所有对应的库位id和可售库存
        List<StorageLocalStocksPo> storageStocksPos = baseMapper.queryStorageLocalStock(outOrderBo.getWhId(),
                commodityIdJoinKeyValues.getSbKeys().toString(),
                commodityIdJoinKeyValues.getMinKey(),
                commodityIdJoinKeyValues.getMaxKey());

        if(null == storageStocksPos || storageStocksPos.size() == 0){

            throw new AplException(CommonStatusCode.GET_FAIL.code, CommonStatusCode.GET_FAIL.msg);

        }


        //根据商品Id进行分组    每个商品Id对应一个或多个库位库存对象
        LinkedHashMap<String, List<StorageLocalStocksPo>> storageStocksMaps =  JoinUtil.listGrouping(storageStocksPos, "commodityId");

        // 循环多个商品
        List<CompareStorageLocalStocksBo> compareAllCommodityStocksList = new ArrayList<>();

        for (AllocationWarehouseOrderCommodityBo orderCommodityBo : commodityBoList) {
            // 找到单个商品的所有对应的库位库存对象                                每次遍历商品信息集合都获取一个商品id
            List<StorageLocalStocksPo> storageStocksList = storageStocksMaps.get(orderCommodityBo.getCommodityId().toString());

            //传入单个商品和其对应的一个或多个库位库存对象进行剩余库存的比较
            List<CompareStorageLocalStocksBo> commodityStocksList = checkStorageStockByCommodity(outOrderBo.getOrderSn(),
                    outOrderBo.getWhId(), orderCommodityBo, storageStocksList, storageLocalStocksHistoryPos);
            if(commodityStocksList.size()>0){
                compareAllCommodityStocksList.addAll(commodityStocksList);
            }
        }

        return compareAllCommodityStocksList;
    }


    //比较单个商品库位库存  pull_item  batch_id
    private List<CompareStorageLocalStocksBo> checkStorageStockByCommodity(
            String orderSn,
            Long whId,
            AllocationWarehouseOrderCommodityBo orderCommodityBo,
            List<StorageLocalStocksPo> storageStocksList,
            List<StorageLocalStocksHistoryPo> storageLocalStocksHistoryPos) {

        //单个商品的出库数量
        Integer compareQty = orderCommodityBo.getOrderQty();
        List<CompareStorageLocalStocksBo> compareStocksList = new ArrayList<>();

        for (StorageLocalStocksPo storageLocalStocksPo : storageStocksList) {

            Integer newAvailableCount =  storageLocalStocksPo.getAvailableCount() - compareQty;//新的可售库存

            CompareStorageLocalStocksBo compareStocksBo = new CompareStorageLocalStocksBo();
            compareStocksList.add(compareStocksBo);
            compareStocksBo.setId(storageLocalStocksPo.getId());
            compareStocksBo.setCommodityId(orderCommodityBo.getCommodityId());
            compareStocksBo.setStorageLocalId(storageLocalStocksPo.getStorageLocalId());
            compareStocksBo.setAvailableCount(newAvailableCount); //新的可用库位库存

            //构建库位库存记录对象
            StorageLocalStocksHistoryPo shp = new StorageLocalStocksHistoryPo();
            shp.setOrderType(2);
            shp.setCommodityId(orderCommodityBo.getCommodityId());
            shp.setOutQty(orderCommodityBo.getOrderQty());
            shp.setOperatorTime(new Timestamp(System.currentTimeMillis()));
            shp.setOrderSn(orderSn);
            shp.setWhId(whId);
            shp.setInQty(0);
            shp.setOrderId(orderCommodityBo.getOrderId());
            shp.setStocksType(1);
            shp.setStorageLocalId(storageLocalStocksPo.getStorageLocalId());


            if (storageLocalStocksPo.getAvailableCount() >= compareQty) {
                //如果本库位可售库存足够分配
                compareStocksBo.allocationQty = compareQty;
                shp.setStocksQty(newAvailableCount);
                return compareStocksList;

            }else{
                //如果本库位库存不够分配, 把本库位全部分配完
                compareStocksBo.allocationQty = storageLocalStocksPo.getAvailableCount();
                shp.setStocksQty(0);
            }
            storageLocalStocksHistoryPos.add(shp);

            //剩余未分配商品数量, 继续在下一个库位分配
            compareQty -= storageLocalStocksPo.getAvailableCount();
        }

        return compareStocksList;
    }


    //跨项目更新拣货明细
    private  Integer AllocaOutOrderStockCallBack(Long outOrderId, Integer pullStatus, List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos){

        //生成一个唯一的事务id , 用来校验远程调用是否成功
        String tranId ="tranId:"+ StringUtil.generateUuid();
        ResultUtil<Integer> result = outStorageOrderOperatorFeign.AllocOutOrderStockCallBack(tranId, outOrderId, pullStatus,compareStorageLocalStocksBos);

        if(!redisTemplate.hasKey(tranId)){
            //如果远程调用失败, redis中key(事务id)将为空
            throw new AplException(AllocationWarehouseServiceCode.INSERT_PULL_FAIL.code, AllocationWarehouseServiceCode.INSERT_PULL_FAIL.msg);
        }

        redisTemplate.delete(tranId);
        return  result.getData();
    }
}



