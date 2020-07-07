package com.apl.wms.warehouse.business.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.StorageCommodityKeyDto;
import com.apl.wms.warehouse.lib.pojo.bo.PullBatchOrderItemBo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.service.StorageLocalStocksService;
import com.apl.wms.warehouse.vo.StorageLocalStocksListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 *
 * @author CY
 * @since 2019-12-09
 */
@RestController
@RequestMapping("/storage-local-stocks")
@Validated
@Api(value = "库位储存商品数量",tags = "库位储存商品数量")
@Slf4j
public class StorageLocalStocksController {

    @Autowired
    public StorageLocalStocksService storageCommodityService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    public ResultUtil<Boolean> add(@Validated StorageLocalStocksPo storageCommodity) {

        return storageCommodityService.add(storageCommodity);
    }



    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "long")
    public ResultUtil<Boolean> delById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return storageCommodityService.deleteById(id);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    public ResultUtil<Boolean> updById(@Validated StorageLocalStocksPo storageLocalStocksPo) {

        return storageCommodityService.updById(storageLocalStocksPo);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "long")
    public ResultUtil<StorageLocalStocksPo> getById(@Min(value = 1 , message = "id不能小于 1") Long id) {

            return storageCommodityService.selectById(id);
    }

    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtil<Page<StorageLocalStocksListVo>> getList(PageDto pageDto, @Validated StorageCommodityKeyDto keyDto) {


        return storageCommodityService.getList(pageDto , keyDto);
    }

    @PostMapping("/storage/lock")
    @ApiOperation(value =  "锁定库位库存数据" , notes = "锁定库位库存数据，给每个订单商品分配不同的库位")
    @ApiIgnore
    public ResultUtil<Map<String, List<PullBatchOrderItemBo>>> lockStorageLocal(@RequestBody List<PullBatchOrderItemBo> pullBatchOrderItems) throws Exception {

        return storageCommodityService.storageLocalLock(pullBatchOrderItems);
    }

}
