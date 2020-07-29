package com.apl.wms.warehouse.lib.feign.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.pojo.bo.PlatformOutOrderStockBo;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;
import com.apl.wms.warehouse.lib.pojo.bo.PullBatchOrderItemBo;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalInfoVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author CY
 * @version 1.0.0
 * @ClassName InnerFeignImpl.java
 * @createTime 2019年07月24日 17:57:00
 */
public class WarehouseFeignImpl implements WarehouseFeign {

    @Override
    public ResultUtil<Boolean> addWarehouseCache(@RequestParam("keys") String keys,
                                                  @RequestParam("minKey") Long minKey,
                                                  @RequestParam("maxKey") Long maxKey) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }


    @Override
    public ResultUtil<Boolean> addCommodityCacheById( String keys,  Long minKey,  Long maxKey) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil<Boolean> addCommodityCacheBySku(String skus, Long customerId) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil<Boolean> addOperatorServiceCache(String keys, Long minKey, Long maxKey) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }
    @Override
    public ResultUtil<Boolean> addOperatorCache(String keys, Long minKey, Long maxKey) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }
    @Override
    public ResultUtil<Boolean> addStoreCache(String keys, Long minKey, Long maxKey) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }
    @Override
    public ResultUtil<Boolean> addStorageLocalCache(String keys, Long minKey, Long maxKey) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil<List<StorageLocalInfoVo>> allocationStorageLocal(Long commodityId , Integer count , Long whId , String storageLocal){
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil<StorageLocalInfoVo> manualAllotLocal(Long commodityId , String storageLocalSn){
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil<Boolean> changeStorageLocalStatus(String lockIds , String unLockIds){
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil<String> getStoreApiConfigStrVal(@RequestParam("id") Long id ){
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil<Boolean> checkStockCount(PlatformOutOrderStockBo platformOutOrderStockBo) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil<Map<String, List<PullBatchOrderItemBo>>> lockStorageLocal(List<PullBatchOrderItemBo> pullBatchOrderItems) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil getCommodityStockMsg(Long whId , String commodityIds) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }


    @Override
    public ResultUtil getStocksByCommodityId(List<Long> commodityIdList) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtil getPackingMaterialsByCommodityIds(String tranId, List<Long> commodityIds) {
        return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }
}
