package com.apl.wms.warehouse.lib.cache;
import com.apl.db.abatis.MyBatisPlusConfig;
import com.apl.lib.cachebase.BaseCacheUtil;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.join.JoinBase;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.cache.bo.CommodityBrandCacheBo;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;

/**
 * @author hjr start
 * @date 2020/8/1 - 10:44
 */

public class JoinCommodityBrand extends JoinBase<CommodityBrandCacheBo> {

    public WarehouseFeign warehouseFeign;

    public JoinCommodityBrand(int joinStyle, WarehouseFeign warehouseFeign, BaseCacheUtil cacheUtil){
        this.warehouseFeign = warehouseFeign;
        this.cacheUtil = cacheUtil;
        this.tabName = "commodity_brand";
        this.joinStyle = joinStyle;

        this.innerOrgId = MyBatisPlusConfig.tenantIdContextHolder.get();
        this.cacheKeyNamePrefix = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_";
    }


    @Override
    public Boolean addCache(String keys, Long minKey, Long maxKey){

        ResultUtil<Boolean> result = warehouseFeign.addCommodityBrandCache(keys, minKey, maxKey);
        if(result.getCode().equals(CommonStatusCode.SYSTEM_SUCCESS.code))
            return true;

        return false;
    }
}
