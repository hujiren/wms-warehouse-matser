package com.apl.wms.warehouse.lib.cache;

import com.apl.lib.cachebase.BaseCacheUtil;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.join.JoinBase;
import com.apl.lib.utils.ResultUtil;
import com.apl.tenant.AplTenantConfig;
import com.apl.wms.warehouse.lib.cache.bo.OperatorServiceBo;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;



/**
 * @Description  关联附加服务
 * @Param
 * @Return
 * @Author  arran
 * @Date  2020/1/9
 */
public class JoinOperatorService extends JoinBase<OperatorServiceBo> {

    public WarehouseFeign warehouseFeign;

    public JoinOperatorService(int joinStyle, WarehouseFeign warehouseFeign, BaseCacheUtil cacheUtil){
        this.warehouseFeign = warehouseFeign;
        this.cacheUtil = cacheUtil;
        this.tabName = "operator_service";
        this.joinStyle = joinStyle;


        this.innerOrgId = AplTenantConfig.tenantIdContextHolder.get();
        this.cacheKeyNamePrefix = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_";
    }


    @Override
    public Boolean addCache(String keys, Long minKey, Long maxKey){

        ResultUtil<Boolean> result = warehouseFeign.addOperatorServiceCache(keys, minKey, maxKey);
        if(result.getCode().equals(CommonStatusCode.SYSTEM_SUCCESS.code))
            return true;

        return false;
    }

}
