package com.apl.wms.warehouse.service.impl;

import com.apl.cache.AplCacheHelper;
import com.apl.cache.AplCacheHelper;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.StringUtil;

import com.apl.wms.warehouse.lib.cache.bo.*;
import com.apl.wms.warehouse.lib.pojo.bo.CommodityCategoryCacheVo;
import com.apl.wms.warehouse.mapper.CacheMapper;
import com.apl.wms.warehouse.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description : 跨库缓存客户缓存
 * @Param ： * @param null
 * @Return ：
 * @Author : arran
 * @Date :
 */

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    //状态code枚举
    enum CacheServiceServiceCode {
        SKU_AND_CUSTOMERID_IS_NULL("SKUS_AND_CUSTOMERID_IS_NULL" ,"SKU和CUSTOMERID不能同时为空"),
        ;

        private String code;
        private String msg;

        CacheServiceServiceCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    @Autowired
    AplCacheHelper aplCacheHelper;

    @Autowired
    CacheMapper cacheMapper;


    //添加仓库缓存
    public ResultUtil<Boolean> addWarehouseCache(String keys, Long minKey, Long maxKey) throws IOException {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, WarehouseCacheBo> maps = cacheMapper.addWarehouseCache(keys, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            //String cacheKey = "JOIN_CACHE:"+securityUser.getInnerOrgId().toString()+"_customer";
            //aplCacheHelper.opsForHash().putAll(cacheKey, maps);
            aplCacheHelper.opsForValue("wareHouse").set(maps);
            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }


    //添加仓库服务名称缓存
    @Override
    public ResultUtil<Boolean> addOperatorServiceCache(String keys, Long minKey, Long maxKey) throws IOException {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, OperatorServiceBo> maps = cacheMapper.addOperatorServiceCache(keys, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);
            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }


    //添加仓库操作员缓存
    @Override
    public ResultUtil<Boolean> addOperatorCache(String keys, Long minKey, Long maxKey) throws IOException {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, OperatorServiceBo> maps = cacheMapper.addOperatorCache(keys, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);
            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }


    //添加商品缓存(根据id)
    public ResultUtil<Boolean> addCommodityCacheById(String ids, Long minKey, Long maxKey) throws IOException {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, CommodityCacheBo> maps = cacheMapper.addCommodityCacheById(ids, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);

            String key;
            Map<String, String> skuMaps = new HashMap<>();
            for (CommodityCacheBo commodityCacheBo : maps.values()) {
                key = "JOIN_CACHE:commodity_sku_" +securityUser.getInnerOrgId().toString()+"_"+ commodityCacheBo.getSku();
                skuMaps.put(key, commodityCacheBo.getId().toString());
            }
            aplCacheHelper.opsForValue("wareHouse").set(maps);

            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }

    //添加商品缓存(根据sku)
    public ResultUtil<Boolean> addCommodityCacheBySku(String skus, Long customerId) throws IOException {
        if(StringUtil.isEmpty(skus) && (customerId==null || customerId<1)){
            ResultUtil.APPRESULT(CacheServiceServiceCode.SKU_AND_CUSTOMERID_IS_NULL.code, CacheServiceServiceCode.SKU_AND_CUSTOMERID_IS_NULL.msg, false);
        }

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, CommodityCacheBo> maps = cacheMapper.addCommodityCacheBySku(skus,  customerId, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);

            String key;
            Map<String, String> skuMaps = new HashMap<>();
            for (CommodityCacheBo commodityCacheBo : maps.values()) {
                key = "JOIN_CACHE:commodity_sku_" +securityUser.getInnerOrgId().toString()+"_"+ commodityCacheBo.getSku();
                skuMaps.put(key, commodityCacheBo.getId().toString());
            }
            aplCacheHelper.opsForValue("wareHouse").set(maps);

            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }


    //添加商品品类缓存
    public ResultUtil<Boolean> addCommodityCategoryCache(String keys, Long minKey, Long maxKey) throws IOException {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, CommodityCategoryCacheVo> maps = cacheMapper.addCommodityCategoryCache(keys, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);
            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }

    @Override
    public ResultUtil<Boolean> addStoreCache(String keys, Long minKey, Long maxKey) throws IOException {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, StoreCacheBo> maps = cacheMapper.addStoreCache(keys, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);
            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }

    @Override
    public ResultUtil<Boolean> addStorageLocalCache(String keys, Long minKey, Long maxKey) throws IOException {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, StorageLocalCacheBo> maps = cacheMapper.addStorageLocalCache(keys, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);
            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }


    @Override
    public ResultUtil<Boolean> addPackMaterialsCache(String keys, Long minKey, Long maxKey) throws IOException {
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, PackingMaterialsCacheBo> maps = cacheMapper.addPackMaterialsCache(keys, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);
            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }

    @Override
    public ResultUtil<Boolean> addCommodityBrandCache(String keys, Long minKey, Long maxKey) throws IOException {
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Map<String, CommodityBrandCacheBo> maps = cacheMapper.addCommodityBrandCache(keys, minKey, maxKey, securityUser.getInnerOrgId());
        if(null != maps && maps.size()>0) {
            aplCacheHelper.opsForValue("wareHouse").set(maps);
            return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, false);
    }
}
