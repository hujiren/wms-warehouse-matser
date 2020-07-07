package com.apl.wms.warehouse.business.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.dto.WarehouseKeyDto;
import com.apl.wms.warehouse.po.WarehousePo;
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

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加", notes =
            "WH_CODE_CAN_NOT_REPEAT->仓库代码不能重复\n" +
                    "WH_NAME_CAN_NOT_REPEAT->仓库中文名不能重复\n" +
                    "WH_NAME_EN_CAN_NOT_REPEAT->仓库英文名不能重复"
    )
    public ResultUtil<Boolean> add(@Validated WarehousePo warehouse) {

        return warehouseService.add(warehouse);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value = "更新基本信息", notes =
            "WH_CODE_CAN_NOT_REPEAT->仓库代码不能重复\n" +
                    "WH_NAME_CAN_NOT_REPEAT->仓库中文名不能重复\n" +
                    "WH_NAME_EN_CAN_NOT_REPEAT->仓库英文名不能重复")
    public ResultUtil<Boolean> updById(@Validated WarehousePo warehouse) {
        ApiParamValidate.notEmpty("id", warehouse.getId());
        return warehouseService.updById(warehouse);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value = "获取基本信息", notes = "获取基本信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query")
    public ResultUtil<WarehouseInfoVo> getById(@Min(value = 1, message = "id不能小于 1") Long id) {

        return warehouseService.selectById(id);
    }

    @PostMapping("/get-list")
    @ApiOperation(value = "分页查找", notes = "分页查找")
    public ResultUtil<Page<WarehouseListVo>> getList(PageDto pageDto, @Validated WarehouseKeyDto keyDto) {

        return warehouseService.getList(pageDto, keyDto);
    }

    /*@PostMapping("/get-bind-wh")
    @ApiOperation(value = "获取绑定数据-仓库", notes = "")
    public ResultUtil<List<WarehouseListVo>> getBindWh() {

        return warehouseService.getBind();
    }*/

    @PostMapping(value = "/upd-details")
    @ApiOperation(value = "更新仓库详细", notes = "更新仓库详细")
    public ResultUtil<Boolean> updateByWhId(@Validated WhDetailsPo whDetailsPo) {
        System.out.println(whDetailsPo);

        return whDetailsService.updByWhId(whDetailsPo);
    }


    @PostMapping(value = "/get-details")
    @ApiOperation(value = "获取详细", notes = "获取详细")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "long")
    public ResultUtil<WhDetailsPo> getByWhId(@Min(value = 1, message = "id不能小于 1") Long id) {

        return whDetailsService.selectById(id);
    }

}
