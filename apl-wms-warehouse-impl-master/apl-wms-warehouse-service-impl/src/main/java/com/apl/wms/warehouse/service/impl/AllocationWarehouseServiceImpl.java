package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.join.JoinKeyValues;
import com.apl.lib.join.JoinUtils;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.outstorage.order.lib.feign.OutStorageOrderOperatorFeign;
import com.apl.wms.outstorage.order.lib.pojo.bo.AllocationWarehouseOrderCommodityBo;
import com.apl.wms.outstorage.order.lib.pojo.bo.AllocationWarehouseOutOrderBo;
import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.mapper.AllocationWarehouseMapper;
import com.apl.wms.warehouse.mapper.CommodityBrandMapper;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.service.AllocationWarehouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class AllocationWarehouseServiceImpl extends ServiceImpl<CommodityBrandMapper, CommodityBrandPo> implements AllocationWarehouseService {

    @Autowired
    private AllocationWarehouseMapper allocationWarehouseMapper;

    @Autowired
    private OutStorageOrderOperatorFeign outStorageOrderOperatorFeign;

    static final ThreadLocal<AllocationWarehouseContextInfo> contextHolder = new ThreadLocal();

    public class AllocationWarehouseContextInfo{
        //仓库id
        public Long whId;
    }


    /**
     * 根据订单分配仓库
     * 通过订单的下单数量和商品id查询对应的总库存并完成减库存
     */
    @Override
    @Transactional
    public ResultUtil<Boolean> AllocationStockByOrders(String orderIds) throws Exception  {

        ResultUtil<List<AllocationWarehouseOutOrderBo>> ordersByAllocationWarehouse = outStorageOrderOperatorFeign.getOrdersByAllocationWarehouse(orderIds);
        if (!ordersByAllocationWarehouse.getCode().equals(CommonStatusCode.GET_SUCCESS)) {
            return ResultUtil.APPRESULT(ordersByAllocationWarehouse.getCode(), ordersByAllocationWarehouse.getMsg(), false);
        }

        //从远程调用接口获取到订单信息和商品信息对象的列表集合
        List<AllocationWarehouseOutOrderBo> ordersList = ordersByAllocationWarehouse.getData();

        for (AllocationWarehouseOutOrderBo outOrderBo : ordersList) {

            //遍历, 取出所有商品信息列表集合, 按商品id排序
            JoinKeyValues commodityIdJoinKeyValues = JoinUtils.getKeys(
                    outOrderBo.getAllocationWarehouseOrderCommodityBoList(),
                    "commodityId",
                    Long.class);
            //22,5,4455  5,4455

            //循环比对总库存
            List<StocksPo> newStocksPos  = checkTotalStock(outOrderBo, commodityIdJoinKeyValues);

            if(null!=newStocksPos && newStocksPos.size()>0) {
                // whId > 0 表示总库存足够, 进行库位库存比较
                Long whId = newStocksPos.get(0).getWhId();
                List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos = checkStorageStockByOrder(whId, outOrderBo, commodityIdJoinKeyValues);

                // 更新库位库存
                updStorageLocalStock(compareStorageLocalStocksBos);
                updTotalLocalStockSql(newStocksPos);

                insertPullAllocationItem(outOrderBo.getOrderId(), compareStorageLocalStocksBos);
            }
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
    }

    //检查总库存
    private List<StocksPo> checkTotalStock(AllocationWarehouseOutOrderBo outOrderBo, JoinKeyValues commodityIdJoinKeyValues)  {
        //获取商品信息对象
        List<AllocationWarehouseOrderCommodityBo> commodityBoList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();

        Long whId = outOrderBo.getWhId();

        //查询总库存
        Map<String ,StocksPo> stocksPos = baseMapper.queryTotalStock(whId,
                commodityIdJoinKeyValues.getSbKeys().toString(),
                commodityIdJoinKeyValues.getMinKey(),
                commodityIdJoinKeyValues.getMaxKey());

        if(whId==0) {//如果仓库id等于0, 表示未分配仓库
            for (Map.Entry<String, StocksPo> entry : stocksPos.entrySet()) {
                //随机取一个此商品的仓库id
                whId = entry.getValue().getWhId();
                break;
            }
        }


        //需要更新到数据库的总库存对象集合
        List<StocksPo> newStocksPos = new ArrayList<>();



        //比对多个商品
        String key;
        for (AllocationWarehouseOrderCommodityBo orderCommodityBo : commodityBoList) {
            //通过拼接一个key, 可获得从数据库中查询到的总库存的对象
            key = whId.toString()+"_"+orderCommodityBo.getCommodityId().toString();

            //获取key(wh_id + commodity_id)对应的总库存对象
            StocksPo stocksPo = stocksPos.get(key);
            if(stocksPo == null){
                //此商品可用库存为空
                return null;
            }
            Integer orderQty = orderCommodityBo.getOrderQty();
            if(stocksPo.getAvailableStockCount() < orderQty){
                //此商品库存不足
                return null;
            }

            // 构建要更新的总库存对象
            Integer newAvailableStockCount = stocksPo.getAvailableStockCount() - orderQty;//新的可用库存
            Integer newFreezeStockCount = stocksPo.getFreezeStockCount() + orderQty;//新的冻结库存
            StocksPo newStocksPo = new StocksPo();
            newStocksPo.setId(stocksPo.getId());
            newStocksPo.setWhId(whId);
            newStocksPo.setAllStockCount(newAvailableStockCount);
            newStocksPo.setFreezeStockCount(newFreezeStockCount);

            newStocksPos.add(newStocksPo);
        }

        //所有商品库存都够, 返回仓库id
        return newStocksPos;
    }


    //按订单, 检查库位库存
    private List<CompareStorageLocalStocksBo> checkStorageStockByOrder(Long whId, AllocationWarehouseOutOrderBo outOrderBo, JoinKeyValues commodityIdJoinKeyValues) throws Exception {

        //通过商品id查询出所有对应的库位id和可用库存
        List<StorageLocalStocksPo> stocksPos = baseMapper.queryStorageLocalStock(whId,
                commodityIdJoinKeyValues.getSbKeys().toString(),
                commodityIdJoinKeyValues.getMinKey(),
                commodityIdJoinKeyValues.getMaxKey());

        //获取商品信息对象列表集合
        List<AllocationWarehouseOrderCommodityBo> commodityBoList = outOrderBo.getAllocationWarehouseOrderCommodityBoList();

        //根据商品Id进行分组    每个商品Id对应一个或多个库位库存对象
        LinkedHashMap<String, List<StorageLocalStocksPo>> stocksMaps =  JoinUtils.listGrouping(stocksPos, "commodityId");

        // 循环多个商品
        List<CompareStorageLocalStocksBo> compareAllCommodityStocksList = new ArrayList<>();
        for (AllocationWarehouseOrderCommodityBo orderCommodityBo : commodityBoList) {
            // 找到单个商品的所有对应的库位库存对象                                每次遍历商品信息集合都获取一个商品id
            List<StorageLocalStocksPo> storageStocksList = stocksMaps.get(orderCommodityBo.getCommodityId().toString());

            //传入单个商品和其对应的一个或多个库位库存对象进行剩余库存的比较
            List<CompareStorageLocalStocksBo> commodityStocksList = checkStorageStockByCommodity(orderCommodityBo, storageStocksList);
            if(commodityStocksList.size()>0){
                compareAllCommodityStocksList.addAll(commodityStocksList);
            }
        }

        return compareAllCommodityStocksList;
    }


    //比较单个商品库位库存  pull_item  batch_id
    private List<CompareStorageLocalStocksBo> checkStorageStockByCommodity(AllocationWarehouseOrderCommodityBo orderCommodityBo, List<StorageLocalStocksPo> storageStocksList) {

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

            if (storageLocalStocksPo.getAvailableCount() >= compareQty) {
                //如果本库位可用库存足够分配
                compareStocksBo.allocationQty = compareQty;
                return compareStocksList;

            }else{
                //如果本库位库存不够分配, 把本库位全部分配完
                compareStocksBo.allocationQty = storageLocalStocksPo.getAvailableCount();
            }

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
        ResultUtil<Integer> integerResultUtil = outStorageOrderOperatorFeign.insertAllocationItem(outOrderId, compareStorageLocalStocksBos);

        return  integerResultUtil.getData();
    }
}



