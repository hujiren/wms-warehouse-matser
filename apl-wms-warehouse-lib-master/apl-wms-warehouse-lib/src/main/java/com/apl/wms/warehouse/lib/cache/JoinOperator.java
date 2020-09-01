package com.apl.wms.warehouse.lib.cache;


import com.apl.lib.cachebase.BaseCacheUtil;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.join.JoinBase;
import com.apl.lib.utils.ResultUtil;
import com.apl.tenant.AplTenantConfig;
import com.apl.wms.warehouse.lib.cache.bo.OperatorCacheBo;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;



/**
 * @Description  缓存操作员
 * @Param
 * @Return
 * @Author  arran
 * @Date  2020/1/9
 */
public class JoinOperator extends JoinBase<OperatorCacheBo> {

    public WarehouseFeign warehouseFeign;

    public JoinOperator(int joinStyle, WarehouseFeign warehouseFeign, BaseCacheUtil cacheUtil){
        this.warehouseFeign = warehouseFeign;
        this.cacheUtil = cacheUtil;
        this.tabName = "operator";
        this.joinStyle = joinStyle;

        this.innerOrgId = AplTenantConfig.tenantIdContextHolder.get();
        this.cacheKeyNamePrefix = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_";
    }


    @Override
    public Boolean addCache(String keys, Long minKey, Long maxKey){

        ResultUtil<Boolean> result = warehouseFeign.addOperatorCache(keys, minKey, maxKey);
        if(result.getCode().equals(CommonStatusCode.SYSTEM_SUCCESS.code))
            return true;



        return false;
    }

}
