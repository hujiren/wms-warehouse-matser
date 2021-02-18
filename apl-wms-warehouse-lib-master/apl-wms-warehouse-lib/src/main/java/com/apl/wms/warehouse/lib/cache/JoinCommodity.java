package com.apl.wms.warehouse.lib.cache;

import com.apl.cache.AplCacheHelper;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.join.JoinBase;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.StringUtil;
import com.apl.tenant.AplTenantConfig;
import com.apl.wms.warehouse.lib.cache.bo.CommodityCacheBo;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;

import java.io.IOException;

/**
   * @Description : 缓存商品
   * @Author : arran
   * @Date : 2019-12-16
*/

public class JoinCommodity extends JoinBase<CommodityCacheBo> {

    private WarehouseFeign warehouseFeign;

    private Long customerId;

    private Integer keyType=1;  //  1:产品id  2:SKU

    public JoinCommodity(int joinStyle, WarehouseFeign warehouseFeign, AplCacheHelper cacheUtil){
        this.warehouseFeign = warehouseFeign;
        this.cacheUtil = cacheUtil;
        this.tabName = "commodity";
        this.joinStyle = joinStyle;

        this.innerOrgId = AplTenantConfig.tenantIdContextHolder.get();
        this.cacheKeyNamePrefix = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_";
    }


    @Override
    public Boolean addCache(String keys, Long minKey, Long maxKey){

        ResultUtil<Boolean> result = null;

        if(keyType==1)
            result = warehouseFeign.addCommodityCacheById(keys, minKey, maxKey);
        else if(keyType==2)
            result = warehouseFeign.addCommodityCacheBySku(keys, customerId);
        if(result!=null && result.getCode().equals(CommonStatusCode.SYSTEM_SUCCESS.code))
           return true;

        return false;
    }

    //根据sku获取商品对象
    public CommodityCacheBo getEntityBySku(String sku) throws IOException {
        String cacheKey = "JOIN_CACHE:"+this.tabName+"_sku_"+this.innerOrgId.toString()+"_" + sku;
        String strVal =  (String)this.cacheUtil.opsForValue("wareHouse").get(cacheKey);
        if(StringUtil.isEmpty(strVal))
            return  null;

        Long commodityId = Long.parseLong(strVal);
        if (commodityId == null) {
            this.addCache(sku, 0l, 0l);
            commodityId = (Long)this.cacheUtil.opsForValue("wareHouse").get(cacheKey);
        }
        if(commodityId==null)
            return null;

        cacheKey = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_" + commodityId.toString();
        CommodityCacheBo entity = (CommodityCacheBo)this.cacheUtil.opsForValue("wareHouse").get(cacheKey);
        if (entity == null) {
            this.addCache(commodityId.toString(), commodityId, commodityId);
            entity = (CommodityCacheBo)this.cacheUtil.opsForValue("wareHouse").get(cacheKey);
        }

        return entity;
    }

}
