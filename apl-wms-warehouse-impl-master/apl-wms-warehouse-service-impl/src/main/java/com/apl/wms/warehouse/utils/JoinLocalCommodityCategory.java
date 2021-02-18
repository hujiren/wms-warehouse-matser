package com.apl.wms.warehouse.utils;

import com.apl.cache.AplCacheHelper;
import com.apl.lib.join.JoinBase;
import com.apl.lib.utils.ResultUtil;
import com.apl.tenant.AplTenantConfig;
import com.apl.wms.warehouse.lib.pojo.bo.CommodityCategoryCacheVo;
import com.apl.wms.warehouse.service.CacheService;
import lombok.SneakyThrows;

/**
   * @Description : 缓存商品种类(本服务)
   * @Author : arran
   * @Date : 2019-12-16
*/

public class JoinLocalCommodityCategory extends JoinBase<CommodityCategoryCacheVo> {

    CacheService cacheService;

    public JoinLocalCommodityCategory(int joinStyle, CacheService cacheService, AplCacheHelper cacheUtil){
        this.cacheService = cacheService;
        this.cacheUtil = cacheUtil;
        this.tabName = "commodityCategory";
        this.joinStyle = joinStyle;

        this.innerOrgId = AplTenantConfig.tenantIdContextHolder.get();
        this.cacheKeyNamePrefix = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_";
    }


    @SneakyThrows
    @Override
    public Boolean addCache(String keys, Long minKey, Long maxKey){

        ResultUtil<Boolean> result = cacheService.addCommodityCategoryCache(keys, minKey, maxKey);
        if(result.getData())
           return true;

        return false;
    }

}
