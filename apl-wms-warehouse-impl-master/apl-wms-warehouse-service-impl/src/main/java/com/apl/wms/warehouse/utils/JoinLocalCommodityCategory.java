package com.apl.wms.warehouse.utils;

import com.apl.lib.datasource.DataSourceContextHolder;
import com.apl.lib.join.JoinBase;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.lib.pojo.bo.CommodityCategoryCacheVo;
import com.apl.wms.warehouse.service.CacheService;
import org.springframework.data.redis.core.RedisTemplate;


/**
   * @Description : 缓存商品种类(本服务)
   * @Author : arran
   * @Date : 2019-12-16
*/

public class JoinLocalCommodityCategory extends JoinBase<CommodityCategoryCacheVo> {

    CacheService cacheService;

    public JoinLocalCommodityCategory(int joinStyle, CacheService cacheService, RedisTemplate redisTemplate){
        this.cacheService = cacheService;
        this.redisTemplate = redisTemplate;
        this.tabName = "commodityCategory";
        this.joinStyle = joinStyle;

        this.innerOrgId = DataSourceContextHolder.getInnerOrgId();
        this.cacheKeyNamePrefix = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_";
    }


    @Override
    public Boolean addCache(String keys, Long minKey, Long maxKey){

        ResultUtils<Boolean> result = cacheService.addCommodityCategoryCache(keys, minKey, maxKey);
        if(result.getData())
           return true;

        return false;
    }

}
