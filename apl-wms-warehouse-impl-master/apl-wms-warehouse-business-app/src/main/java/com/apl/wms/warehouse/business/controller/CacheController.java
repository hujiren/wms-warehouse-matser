package com.apl.wms.warehouse.business.controller;

import com.apl.lib.utils.ResultUtil;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.lib.constants.WmsWarehouseUrlConstants;
import com.apl.wms.warehouse.service.CacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(WmsWarehouseUrlConstants.CACHE_PATH)
@Api(value = "缓存",tags = "缓存")
@Validated
public class CacheController {

    @Autowired
    CacheService cacheService;

    @PostMapping("//add-packMaterials-cache")
    @ApiOperation(value = "添加包装材料缓存")
    public ResultUtil<Boolean> addPackMaterialsCache(String keys, Long minKey, Long maxKey){

        return cacheService.addPackMaterialsCache(keys, minKey, maxKey);
    }

    @PostMapping("//add-commodity-brand-cache")
    @ApiOperation(value = "添加商品品牌缓存")
    public ResultUtil<Boolean> addCommodityBrandCache(String keys, Long minKey, Long maxKey){

        return cacheService.addCommodityBrandCache(keys, minKey, maxKey);
    }

    @PostMapping("/add-warehouse-cache")
    @ApiOperation(value = "添加仓库缓存")
    public ResultUtil<Boolean> addWarehouseCache(String keys, Long minKey, Long maxKey){

        return cacheService.addWarehouseCache(keys, minKey, maxKey);
    }

    @PostMapping("/add-commodity-cache-by-id")
    @ApiOperation(value = "添加商品缓存(根据id)")
    public ResultUtil<Boolean> addCommodityCacheById(String keys, Long minKey, Long maxKey){
        ApiParamValidate.notEmpty("keys", keys);

        return cacheService.addCommodityCacheById(keys, minKey, maxKey);
    }

    @PostMapping("/add-commodity-cache-by-sku")
    @ApiOperation(value = "添加商品缓存(根据sku)")
    public ResultUtil<Boolean> addCommodityCacheBySku(String skus, Long customerId){

        return cacheService.addCommodityCacheBySku(skus, customerId);
    }

    @PostMapping("/add-operator-service-cache")
    @ApiOperation(value = "添加操作服务缓存")
    public ResultUtil<Boolean> addOperatorServiceCache(String keys, Long minKey, Long maxKey){

        return cacheService.addOperatorServiceCache(keys, minKey, maxKey);
    }

    @PostMapping("/add-operator-cache")
    @ApiOperation(value = "添加操作员缓存")
    public ResultUtil<Boolean> addOperatorCache(String keys, Long minKey, Long maxKey){

        return cacheService.addOperatorCache(keys, minKey, maxKey);
    }


    @PostMapping("/add-store-cache")
    @ApiOperation(value = "添加店铺缓存")
    public ResultUtil<Boolean> addStoreCache(String keys, Long minKey, Long maxKey){

        return cacheService.addStoreCache(keys, minKey, maxKey);
    }

    @PostMapping("/add-storage-local-cache")
    @ApiOperation(value = "添加店铺缓存")
    public ResultUtil<Boolean> addStorageLocalCache(String keys, Long minKey, Long maxKey){

        return cacheService.addStorageLocalCache(keys, minKey, maxKey);
    }

}
