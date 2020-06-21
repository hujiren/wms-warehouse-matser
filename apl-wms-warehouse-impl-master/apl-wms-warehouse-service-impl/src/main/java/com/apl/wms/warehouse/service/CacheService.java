package com.apl.wms.warehouse.service;

import com.apl.lib.utils.ResultUtils;

public interface CacheService {

    ResultUtils<Boolean> addWarehouseCache(String keys, Long minKey, Long maxKey);

    ResultUtils<Boolean> addCommodityCacheById(String keys, Long minKey, Long maxKey);

    ResultUtils<Boolean> addCommodityCacheBySku(String skus, Long customerId);

    ResultUtils<Boolean> addOperatorServiceCache(String keys, Long minKey, Long maxKey);

    ResultUtils<Boolean> addOperatorCache(String keys, Long minKey, Long maxKey);

    ResultUtils<Boolean> addCommodityCategoryCache(String keys, Long minKey, Long maxKey);

    ResultUtils<Boolean> addStoreCache(String keys, Long minKey, Long maxKey);

    ResultUtils<Boolean> addStorageLocalCache(String keys, Long minKey, Long maxKey);

}
