package com.apl.wms.warehouse.lib.feign;

import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.constants.WmsWarehouseUrlConstants;
import com.apl.wms.warehouse.lib.pojo.bo.PlatformOutOrderStockBo;
import com.apl.wms.warehouse.lib.feign.impl.WarehouseFeignImpl;
import com.apl.wms.warehouse.lib.pojo.bo.PullBatchOrderItemBo;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalInfoVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author CY
 * @version 1.0.0
 * @ClassName InnerFeign.java
 * @createTime 2019年07月24日 17:51:00
 */
@Component
@FeignClient(value = "apl-wms-warehouse-business-app" , fallback = WarehouseFeignImpl.class)
public interface WarehouseFeign {


    /**
     * @Description : 添加仓库缓存
     * @Param ： * @param null
     * @Return ：
     * @Author : arran
     * @Date :
     */
    @PostMapping( WmsWarehouseUrlConstants.CACHE_PATH + "/add-warehouse-cache")
    ResultUtil<Boolean> addWarehouseCache(@RequestParam("keys") String keys,
                                           @RequestParam("minKey") Long minKey,
                                           @RequestParam("maxKey") Long maxKey);


    /**
     * @Description : 添加商品缓存
     * @Param ： * @param null
     * @Return ：
     * @Author : arran
     * @Date :
     */
    @PostMapping(WmsWarehouseUrlConstants.CACHE_PATH + "/add-commodity-cache-by-id")
    ResultUtil<Boolean> addCommodityCacheById(@RequestParam("keys") String keys,
                                               @RequestParam("minKey") Long minKey,
                                               @RequestParam("maxKey") Long maxKey);


    /**
     * @Description : 添加商品SKU缓存
     * @Param ： * @param null
     * @Return ：
     * @Author : arran
     * @Date :
     */
    @PostMapping(WmsWarehouseUrlConstants.CACHE_PATH + "/add-commodity-cache-by-sku")
    ResultUtil<Boolean> addCommodityCacheBySku(@RequestParam("skus") String skus,
                                                @RequestParam("customerId") Long customerId);


    /**
     * @Desc: 添加操作服务 缓存
     * @Author: CY
     * @Date: 2019/12/26 10:18
     */
    @PostMapping(WmsWarehouseUrlConstants.CACHE_PATH + "/add-operator-service-cache")
    ResultUtil<Boolean> addOperatorServiceCache(@RequestParam("keys") String keys,
                                                 @RequestParam("minKey") Long minKey,
                                                 @RequestParam("maxKey") Long maxKey);

    /**
     * @Desc: 添加操作服务 缓存
     * @Author: CY
     * @Date: 2019/12/26 10:18
     */
    @PostMapping(WmsWarehouseUrlConstants.CACHE_PATH + "/add-operator-cache")
    ResultUtil<Boolean> addOperatorCache(@RequestParam("keys") String keys,
                                          @RequestParam("minKey") Long minKey,
                                          @RequestParam("maxKey") Long maxKey);


    /**
     * @Desc: 添加店铺 缓存
     * @Author: CY
     * @Date: 2020/1/13 18:32
     */
    @PostMapping(WmsWarehouseUrlConstants.CACHE_PATH + "/add-store-cache")
    ResultUtil<Boolean> addStoreCache(@RequestParam("keys") String keys,
                                       @RequestParam("minKey") Long minKey,
                                       @RequestParam("maxKey") Long maxKey);


    /**
     * @Desc: 添加库位 缓存
     * @Author: CY
     * @Date: 2020/1/13 18:32
     */
    @PostMapping(WmsWarehouseUrlConstants.CACHE_PATH + "/add-storage-local-cache")
    ResultUtil<Boolean> addStorageLocalCache(@RequestParam("keys") String keys,
                                              @RequestParam("minKey") Long minKey,
                                              @RequestParam("maxKey") Long maxKey);


    /**
     * @Desc: 给上架订单 分配 库位
     * @Author: CY
     * @Date: 2020/1/3 9:43
     */
    @PostMapping("/storage-local/allocation-storage")
    ResultUtil<List<StorageLocalInfoVo>> allocationStorageLocal(
            @RequestParam("commodityId") Long commodityId ,
            @RequestParam("count") Integer count ,
            @RequestParam("whId") Long whId ,
            @RequestParam("storageLocal") String storageLocal);

    /**
     * @Desc: 给上架订单 分配 库位
     * @Author: CY
     * @Date: 2020/1/3 9:43
     */
    @PostMapping("/storage-local/manual-allocation")
    ResultUtil<StorageLocalInfoVo> manualAllotLocal(@RequestParam("commodityId")Long commodityId ,
                                                     @RequestParam("storageLocalSn") String storageLocalSn);


    /** @Description : 锁定库位 和 解锁库位
     *  @Param ：
     *  @Return ：
     *  Created by arran on 2020/3/12 */
    @PostMapping("/storage-local/change-status")
    ResultUtil<Boolean> changeStorageLocalStatus(@RequestParam("lockIds") String lockIds , @RequestParam("unLockIds") String unLockIds);

    /**
     * @Desc: 给上架订单 分配 库位
     * @Author: CY
     * @Date: 2020/1/3 9:43
     */
    @PostMapping("/store/feign/apiconfig-strval")
    ResultUtil<String> getStoreApiConfigStrVal(@RequestParam("id") Long id);

    /**
     * @Desc: 校验库存
     * @Author: CY
     * @Date: 2020/6/8 11:56
     */
    @PostMapping(value = "/stocks/check" , consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultUtil<Boolean> checkStockCount(PlatformOutOrderStockBo platformOutOrderStockBo);

    /**
     * @Desc: 锁定库位库存
     * @Author: CY
     * @Date: 2020/6/9 11:38
     */
    @PostMapping(value = "/storage-local-stocks/storage/lock" , consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultUtil<Map<String, List<PullBatchOrderItemBo>>> lockStorageLocal(List<PullBatchOrderItemBo> pullBatchOrderItems);

    /**
     * @Desc: 锁定库位库存
     * @Author: CY
     * @Date: 2020/6/9 11:38
     */
    @PostMapping(value = "/stocks/commodity/get")
    ResultUtil getCommodityStockMsg(@RequestParam("whId")Long whId ,@RequestParam("commodityIds") String commodityIds);


    @PostMapping(value = "/storage-local/get-storage-local-reality-count")
    ResultUtil<Map<Long, Integer>> getStorageLocalRealityCount(@RequestBody List<Long> StorageLocalIdList);


    @PostMapping(value = "/storage-local/get-storage-local-reality-count")
    ResultUtil<Map<Long, List<StorageLocalPo>>> getStorageLocalRealityCountByCommodityId2(@RequestBody List<Long> commodityIdList);


    @PostMapping(value = "/stocks/getStocks-reality-count")
    ResultUtil<Map<Long, Integer>> getStocksRealityCountByCommodityId(@RequestBody List<Long> commodityIdList);

    @PostMapping(value = "/stocks/getStocks-reality-count")
    ResultUtil<List<StocksPo>> getStocksRealityCountByCommodityId2(@RequestBody List<Long> commodityIdList);


}
