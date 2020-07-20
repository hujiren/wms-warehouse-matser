package com.apl.wms.warehouse.service;

import com.apl.lib.utils.ResultUtil;

import java.util.List;

public interface CacheService {

    ResultUtil<Boolean> addWarehouseCache(String keys, Long minKey, Long maxKey);

    ResultUtil<Boolean> addCommodityCacheById(String keys, Long minKey, Long maxKey);

    ResultUtil<Boolean> addCommodityCacheBySku(String skus, Long customerId);

    ResultUtil<Boolean> addOperatorServiceCache(String keys, Long minKey, Long maxKey);

    ResultUtil<Boolean> addOperatorCache(String keys, Long minKey, Long maxKey);

    ResultUtil<Boolean> addCommodityCategoryCache(String keys, Long minKey, Long maxKey);

    ResultUtil<Boolean> addStoreCache(String keys, Long minKey, Long maxKey);

    ResultUtil<Boolean> addStorageLocalCache(String keys, Long minKey, Long maxKey);

}
