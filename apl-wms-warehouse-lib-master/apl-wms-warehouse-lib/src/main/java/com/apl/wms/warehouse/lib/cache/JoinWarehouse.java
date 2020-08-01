package com.apl.wms.warehouse.lib.cache;

import com.apl.db.abatis.MyBatisPlusConfig;
import com.apl.lib.cachebase.BaseCacheUtil;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.join.JoinBase;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.cache.bo.WarehouseCacheBo;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;



/**
   * @Description : 关联仓库
   * @Author : arran
   * @Date : 2019-12-16
*/

public class JoinWarehouse extends JoinBase<WarehouseCacheBo> {

    public WarehouseFeign warehouseFeign;

    public JoinWarehouse(int joinStyle, WarehouseFeign warehouseFeign, BaseCacheUtil cacheUtil){
        this.warehouseFeign = warehouseFeign;
        this.cacheUtil = cacheUtil;
        this.tabName = "warehouse";
        this.joinStyle = joinStyle;

        this.innerOrgId = MyBatisPlusConfig.tenantIdContextHolder.get();
        this.cacheKeyNamePrefix = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_";
    }


    @Override
    public Boolean addCache(String keys, Long minKey, Long maxKey){

        ResultUtil<Boolean> result = warehouseFeign.addWarehouseCache(keys, minKey, maxKey);
        if(result.getCode().equals(CommonStatusCode.SYSTEM_SUCCESS.code))
           return true;

        return false;
    }

}
