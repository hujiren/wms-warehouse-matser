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
import com.apl.wms.warehouse.lib.dao.StocksHistoryDao;
import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.lib.pojo.po.StocksHistoryPo;
import com.apl.wms.warehouse.dao.CommodityBrandMapper;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.service.AllocationWarehouseForOrderService;
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
 * @date 2020/7/6 - 15:15
 */
@Service
@Slf4j
public class AllocationWarehouseForOrderForOrderServiceImpl extends ServiceImpl<CommodityBrandMapper, CommodityBrandPo> implements AllocationWarehouseForOrderService {

    //状态code枚举
    enum AllocationWarehouseServiceCode {
         INSERT_PULL_FAIL("INSERT_PULL_FAIL" ,"插入分配库存对象失败"),
        ;

        private String code;
        private String msg;

        AllocationWarehouseServiceCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

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
    RedisTemplate redisTemplate;

    @Autowired
    StocksHistoryDao stocksHistoryDao;


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

        //如果没有返回相应的数据, 则返回相应的状态信息
        if (!outOrderBoResult.getCode().equals(CommonStatusCode.GET_SUCCESS.code)) {
            log.info("allocationStockByOrder query order fail!! outOrderId:" + outOrderId.toString());
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
     *      2.遍历, 取出所有商品信息列表集合, 按商品id排序
     *      3.调用对比总库存方法, 将循环对比总库存
     *      4.如果总库存充足, 则进行对比库位库存
     */
    public ResultUtil<Boolean> allocationStockByOrder(AllocationWarehouseOutOrderBo outOrderBo)  {

        //  1.通过切换数据源保存库存记录
        DBUtil.DBInfo dbinfo = stocksHistoryDao.createDBinfo();

        try {
            // 2.遍历, 取出所有商品信息列表集合, 按商品id排序
            JoinKeyValues commodityIdJoinKeyValues = JoinUtil.getKeys(
                    outOrderBo.getAllocationWarehouseOrderCommodityBoList(),
                    "commodityId",
                    Long.class);

            //新建库存历史记录列表
            List<StocksHistoryPo> stocksHistoryPos = new ArrayList<>();

            // 3.调用对比总库存方法, 将循环对比总库存
            List<StocksPo> newStocksPos = checkTotalStock(outOrderBo, commodityIdJoinKeyValues, stocksHistoryPos);

            // 4.如果总库存充足, 则进行对比库位库存
            if (null != newStocksPos && newStocksPos.size() > 0) {
                // whId > 0 表示总库存足够, 进行库位库存比较
                Long whId = newStocksPos.get(0).getWhId();
                outOrderBo.setWhId(whId);
                List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos = checkStorageStockByOrder(outOrderBo, commodityIdJoinKeyValues, stocksHistoryPos);

                // 更新库位库存
                updStorageLocalStock(compareStorageLocalStocksBos);

                //更新总库存
                updTotalLocalStockSql(newStocksPos);

                //切换数据源,保存库存历史记录列表
                dbinfo.dbUtil.beginTrans(dbinfo);
                stocksHistoryDao.saveStocksHistoryPos(dbinfo, stocksHistoryPos);
                dbinfo.dbUtil.commit(dbinfo);

                insertPullAllocationItem(outOrderBo.getOrderId(), compareStorageLocalStocksBos);
            }
        }
        catch (Exception e){
            dbinfo.dbUtil.rollback(dbinfo);
            log.info("allocationStockByOrder, {0},  \r\n outOrderId: {1}", e.getMessage(), outOrderBo.getOrderId());
            throw new AplException(CommonStatusCode.SAVE_FAIL.code, CommonStatusCode.SAVE_FAIL.msg);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, true);
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
        //获取商品信息对象??
        List<AllocationWarehouseOrderCommodityBo> commodityBoList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();


        Long whId = outOrderBo.getWhId();

        //查询该订单中多个商品的总库存
        Map<String , StocksBo> stocksPos = baseMapper.queryTotalStock(whId,
                commodityIdJoinKeyValues.getSbKeys().toString(),
                commodityIdJoinKeyValues.getMinKey(),
                commodityIdJoinKeyValues.getMaxKey());

        if(whId==0) {//如果仓库id等于0, 表示未指定仓库
            for (Map.Entry<String, StocksBo> entry : stocksPos.entrySet()) {
                //随机取一个此商品的仓库id
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

            //获取key(wh_id + commodity_id)对应的总库存对象
            StocksBo stocksBo = stocksPos.get(key);
            if(stocksBo == null){
                //此商品可用库存为空
                return null;
            }
            Integer orderQty = orderCommodityBo.getOrderQty();
            if(stocksBo.getAvailableStockCount() < orderQty){
                //此商品库存不足
                return null;
            }

            // 构建要更新的总库存对象
            Integer newAvailableStockCount = stocksBo.getAvailableStockCount() - orderQty;//新的可用库存
            Integer newFreezeStockCount = stocksBo.getFreezeStockCount() + orderQty;//新的冻结库存
            StocksPo newStocksPo = new StocksPo();
            newStocksPo.setId(stocksBo.getId());
            newStocksPo.setWhId(whId);
            newStocksPo.setAllStockCount(newAvailableStockCount);
            newStocksPo.setFreezeStockCount(newFreezeStockCount);
            newStocksPos.add(newStocksPo);

            //构建总库存记录对象
            StocksHistoryPo shp = new StocksHistoryPo();
            shp.setOrderType(2);
            shp.setOrderId(orderCommodityBo.getOrderId());
            shp.setInQty(0);
            shp.setOutQty(orderCommodityBo.getOrderQty());
            shp.setWhId(whId);
            shp.setStorageLocalId(0L);
            shp.setStocksQty(newAvailableStockCount);
            shp.setOrderSn(outOrderBo.getOrderSn());
            shp.setOperatorTime(LocalDateTime.now());
            shp.setCommodityId(orderCommodityBo.getCommodityId());
            stocksHistoryPos.add(shp);
        }

        //所有商品库存都够, 返回仓库id
        return newStocksPos;
    }


    //按订单, 检查库位库存
    private List<CompareStorageLocalStocksBo> checkStorageStockByOrder(
                                                                       AllocationWarehouseOutOrderBo outOrderBo,
                                                                       JoinKeyValues commodityIdJoinKeyValues,
                                                                       List<StocksHistoryPo> stocksHistoryPos) throws Exception {

        //通过商品id查询出所有对应的库位id和可用库存
        List<StorageLocalStocksPo> stocksPos = baseMapper.queryStorageLocalStock(outOrderBo.getWhId(),
                commodityIdJoinKeyValues.getSbKeys().toString(),
                commodityIdJoinKeyValues.getMinKey(),
                commodityIdJoinKeyValues.getMaxKey());

        //获取商品信息对象列表集合
        List<AllocationWarehouseOrderCommodityBo> commodityBoList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();

        //根据商品Id进行分组    每个商品Id对应一个或多个库位库存对象
        LinkedHashMap<String, List<StorageLocalStocksPo>> stocksMaps =  JoinUtil.listGrouping(stocksPos, "commodityId");

        // 循环多个商品
        List<CompareStorageLocalStocksBo> compareAllCommodityStocksList = new ArrayList<>();
        for (AllocationWarehouseOrderCommodityBo orderCommodityBo : commodityBoList) {
            // 找到单个商品的所有对应的库位库存对象                                每次遍历商品信息集合都获取一个商品id
            List<StorageLocalStocksPo> storageStocksList = stocksMaps.get(orderCommodityBo.getCommodityId().toString());

            //传入单个商品和其对应的一个或多个库位库存对象进行剩余库存的比较
            List<CompareStorageLocalStocksBo> commodityStocksList = checkStorageStockByCommodity(outOrderBo.getOrderSn(), outOrderBo.getWhId(), orderCommodityBo, storageStocksList, stocksHistoryPos);
            if(commodityStocksList.size()>0){
                compareAllCommodityStocksList.addAll(commodityStocksList);
            }
        }

        return compareAllCommodityStocksList;
    }


    //比较单个商品库位库存  pull_item  batch_id
    private List<CompareStorageLocalStocksBo> checkStorageStockByCommodity(String orderSn, Long whId,AllocationWarehouseOrderCommodityBo orderCommodityBo, List<StorageLocalStocksPo> storageStocksList, List<StocksHistoryPo> stocksHistoryPos) {

        //单个商品的出库数量
        Integer compareQty = orderCommodityBo.getOrderQty();
        List<CompareStorageLocalStocksBo> compareStocksList = new ArrayList<>();

        for (StorageLocalStocksPo storageLocalStocksPo : storageStocksList) {

            // 构建分配信息  availableCount  getFreezeStockCount
            Integer newAvailableCount =  storageLocalStocksPo.getAvailableCount() - compareQty;//新的可用库存
            Integer newFreezeCount = storageLocalStocksPo.getFreezeCount() + compareQty;//新的冻结库存

            CompareStorageLocalStocksBo compareStocksBo = new CompareStorageLocalStocksBo();
            compareStocksList.add(compareStocksBo);
            compareStocksBo.setId(storageLocalStocksPo.getId());
            compareStocksBo.setCommodityId(orderCommodityBo.getCommodityId());
            compareStocksBo.setStorageLocalId(storageLocalStocksPo.getStorageLocalId());
            compareStocksBo.setAvailableCount(newAvailableCount); //新的可用库位库存
            compareStocksBo.setFreezeCount(newFreezeCount);//新的冻结库位库存

            //构建库位库存记录对象
            StocksHistoryPo shp = new StocksHistoryPo();
            shp.setCommodityId(orderCommodityBo.getCommodityId());
            shp.setOutQty(orderCommodityBo.getOrderQty());
            shp.setOperatorTime(LocalDateTime.now());
            shp.setOrderSn(orderSn);
            shp.setOrderType(2);
            shp.setWhId(whId);
            shp.setInQty(0);
            shp.setOrderId(orderCommodityBo.getOrderId());
            shp.setStorageLocalId(storageLocalStocksPo.getStorageLocalId());

            if (storageLocalStocksPo.getAvailableCount() >= compareQty) {
                //如果本库位可用库存足够分配
                compareStocksBo.allocationQty = compareQty;
                shp.setStocksQty(newAvailableCount);
                return compareStocksList;

            }else{
                //如果本库位库存不够分配, 把本库位全部分配完
                compareStocksBo.allocationQty = storageLocalStocksPo.getAvailableCount();
                shp.setStocksQty(0);
            }
            stocksHistoryPos.add(shp);

            //剩余未分配商品数量, 继续在下一个库位分配
            compareQty -= storageLocalStocksPo.getAvailableCount();
        }

        return compareStocksList;
    }

    //更新总库存
    private Integer updTotalLocalStockSql(List<StocksPo> newStocksPos){
        Integer integer = baseMapper.updateStock(newStocksPos);
        return  integer;
    }

    //更新库位库存
    private Integer updStorageLocalStock(List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos){
        Integer integer = baseMapper.updateStorageLocalStock(compareStorageLocalStocksBos);

        return integer;
    }

    //跨项目更新拣货明细
    private  Integer insertPullAllocationItem(Long outOrderId, List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos){

        //生成一个唯一的事务id , 用来校验远程调用是否成功
        String tranId = "tranId:" + StringUtil.generateUuid();
        ResultUtil<Integer> result = outStorageOrderOperatorFeign.insertAllocationItem(tranId, outOrderId, compareStorageLocalStocksBos);

        if(!redisTemplate.hasKey(tranId)){
            //如果远程调用失败, redis中key(事务id)将为空
            throw new AplException(AllocationWarehouseServiceCode.INSERT_PULL_FAIL.code, AllocationWarehouseServiceCode.INSERT_PULL_FAIL.msg);
        }

        redisTemplate.delete(tranId);
        return  result.getData();
    }
}



