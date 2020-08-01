/*
 * Copyright © 2018 BDHub Software
 * Shenzhen BDHub Software Co., Ltd.
 * http://www.bdhubware.com/
 * All rights reserved.
 */

package com.apl.wms.warehouse.dao;


import com.apl.wms.warehouse.lib.cache.bo.*;
import com.apl.wms.warehouse.lib.pojo.bo.CommodityCategoryCacheVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author APL
 * @since 2019-10-14
 */
@Repository
public interface CacheMapper extends BaseMapper {

    //添加仓库缓存
    @MapKey("cacheKey")
    Map<String, WarehouseCacheBo> addWarehouseCache(@Param("keys") String keys, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);

    //添加仓库服务名称缓存
    @MapKey("cacheKey")
    Map<String, OperatorServiceBo> addOperatorServiceCache(@Param("keys") String keys, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);


    //添加 操作员缓存
    @MapKey("cacheKey")
    Map<String, OperatorServiceBo> addOperatorCache(@Param("keys") String keys, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);


    //添加商品缓存(根据id)
    @MapKey("cacheKey")
    Map<String, CommodityCacheBo> addCommodityCacheById(@Param("ids") String ids, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);

    //添加商品缓存(根据id)
    @MapKey("cacheKey")
    Map<String, CommodityCacheBo> addCommodityCacheBySku(@Param("skus") String skus, @Param("customerId") Long customerId, @Param("innerOrgId") Long innerOrgId);


    //添加商品品类缓存
    @MapKey("cacheKey")
    Map<String, CommodityCategoryCacheVo> addCommodityCategoryCache(@Param("keys") String keys, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);

    /**
     * @Desc: 添加 店铺 缓存
     * @Author: CY
     * @Date: 2020/1/13 18:37
     */
    @MapKey("cacheKey")
    Map<String, StoreCacheBo> addStoreCache(@Param("keys") String keys, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);



    /**
     * @Desc: 添加 店铺 缓存
     * @Author: CY
     * @Date: 2020/1/13 18:37
     */
    @MapKey("cacheKey")
    Map<String, StorageLocalCacheBo> addStorageLocalCache(@Param("keys") String keys, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);

    /**
     * 添加包装材料缓存
     * @return
     */
    @MapKey("cacheKey")
    Map<String, PackingMaterialsCacheBo> addPackMaterialsCache(@Param("keys") String keys, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);

    /**
     * 添加商品品牌缓存
     * @return
     */
    @MapKey("cacheKey")
    Map<String, CommodityBrandCacheBo> addCommodityBrandCache(@Param("keys") String keys, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey, @Param("innerOrgId") Long innerOrgId);
}

