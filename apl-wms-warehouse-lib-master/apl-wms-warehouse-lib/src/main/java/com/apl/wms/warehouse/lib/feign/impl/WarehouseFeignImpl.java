package com.apl.wms.warehouse.lib.feign.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.lib.pojo.bo.PlatformOutOrderStockBo;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;
import com.apl.wms.warehouse.lib.pojo.bo.PullBatchOrderItemBo;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalInfoVo;
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
    public ResultUtils<Boolean> addWarehouseCache(@RequestParam("keys") String keys,
                                                  @RequestParam("minKey") Long minKey,
                                                  @RequestParam("maxKey") Long maxKey) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }


    @Override
    public ResultUtils<Boolean> addCommodityCacheById( String keys,  Long minKey,  Long maxKey) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils<Boolean> addCommodityCacheBySku(String skus, Long customerId) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils<Boolean> addOperatorServiceCache(String keys, Long minKey, Long maxKey) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }
    @Override
    public ResultUtils<Boolean> addOperatorCache(String keys, Long minKey, Long maxKey) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }
    @Override
    public ResultUtils<Boolean> addStoreCache(String keys, Long minKey, Long maxKey) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }
    @Override
    public ResultUtils<Boolean> addStorageLocalCache(String keys, Long minKey, Long maxKey) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils<List<StorageLocalInfoVo>> allocationStorageLocal(Long commodityId , Integer count , Long whId , String storageLocal){
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils<StorageLocalInfoVo> manualAllotLocal(Long commodityId , String storageLocalSn){
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils<Boolean> changeStorageLocalStatus(String lockIds , String unLockIds){
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils<String> getStoreApiConfigStrVal(@RequestParam("id") Long id ){
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils<Boolean> checkStockCount(PlatformOutOrderStockBo platformOutOrderStockBo) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils<Map<String, List<PullBatchOrderItemBo>>> lockStorageLocal(List<PullBatchOrderItemBo> pullBatchOrderItems) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

    @Override
    public ResultUtils getCommodityStockMsg(Long whId , String commodityIds) {
        return ResultUtils.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL.getCode() , CommonStatusCode.SERVER_INVOKE_FAIL.getMsg() , null);
    }

}
