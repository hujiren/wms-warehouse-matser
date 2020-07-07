package com.apl.wms.warehouse.buyer.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.WarehouseKeyDto;
import com.apl.wms.warehouse.po.WhDetailsPo;
import com.apl.wms.warehouse.service.WarehouseService;
import com.apl.wms.warehouse.service.WhDetailsService;
import com.apl.wms.warehouse.vo.WarehouseInfoVo;
import com.apl.wms.warehouse.vo.WarehouseListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;

/**
 * @author CY
 * @since 2019-12-09
 */
@RestController
@RequestMapping("/warehouse")
@Validated
@Api(value = "仓库", tags = "仓库")
@Slf4j
public class WarehouseController {

    @Resource
    public WarehouseService warehouseService;

    @Resource
    public WhDetailsService whDetailsService;



    @PostMapping(value = "/get")
    @ApiOperation(value = "获取基本信息", notes = "获取基本信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query")
    public ResultUtil<WarehouseInfoVo> getById(@Min(value = 1, message = "id不能小于 1") Long id) {

        return warehouseService.selectById(id);
    }


    @PostMapping(value = "/get-details")
    @ApiOperation(value = "获取详细", notes = "获取详细")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "long")
    public ResultUtil<WhDetailsPo> getByWhId(@Min(value = 1, message = "id不能小于 1") Long id) {

        return whDetailsService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value = "分页查找", notes = "分页查找")
    public ResultUtil<Page<WarehouseListVo>> getList(PageDto pageDto, @Validated WarehouseKeyDto keyDto) {

        return warehouseService.getList(pageDto, keyDto);
    }

}
