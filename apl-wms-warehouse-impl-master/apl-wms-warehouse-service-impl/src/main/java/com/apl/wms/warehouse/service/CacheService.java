package com.apl.wms.warehouse.service;

import com.apl.lib.utils.ResultUtil;

import java.io.IOException;
import java.util.List;

public interface CacheService {

    ResultUtil<Boolean> addWarehouseCache(String keys, Long minKey, Long maxKey) throws IOException;

    ResultUtil<Boolean> addCommodityCacheById(String keys, Long minKey, Long maxKey) throws IOException;

    ResultUtil<Boolean> addCommodityCacheBySku(String skus, Long customerId) throws IOException;

    ResultUtil<Boolean> addOperatorServiceCache(String keys, Long minKey, Long maxKey) throws IOException;

    ResultUtil<Boolean> addOperatorCache(String keys, Long minKey, Long maxKey) throws IOException;

    ResultUtil<Boolean> addCommodityCategoryCache(String keys, Long minKey, Long maxKey) throws IOException;

    ResultUtil<Boolean> addStoreCache(String keys, Long minKey, Long maxKey) throws IOException;

    ResultUtil<Boolean> addStorageLocalCache(String keys, Long minKey, Long maxKey) throws IOException;

    ResultUtil<Boolean> addPackMaterialsCache(String keys, Long minKey, Long maxKey) throws IOException;

    ResultUtil<Boolean> addCommodityBrandCache(String keys, Long minKey, Long maxKey) throws IOException;
}
