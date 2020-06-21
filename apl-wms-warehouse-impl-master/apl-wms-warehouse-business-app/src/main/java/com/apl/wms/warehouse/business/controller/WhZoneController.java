package com.apl.wms.warehouse.business.controller;


import com.apl.wms.warehouse.po.WhZonePo;
import com.apl.wms.warehouse.vo.WhZoneListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.apl.lib.pojo.dto.PageDto;
import org.springframework.web.bind.annotation.*;
import com.apl.wms.warehouse.service.WhZoneService;
import com.apl.wms.warehouse.vo.WhZoneInfoVo;
import com.apl.wms.warehouse.dto.WhZoneKeyDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.validate.ApiParamValidate;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author apl
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/wh-zone")
@Validated
@Api(value = "仓库分区",tags = "仓库分区")
@Slf4j
public class WhZoneController {

    @Resource
    public WhZoneService whZoneService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加", notes ="ZONE_CODE_EXIST -> zoneCode已经存在\n"+
                                         "ZONE_NAME_EXIST -> zoneName已经存在\n"+
                                         "ZONE_NAME_EN_EXIST -> zoneNameEn已经存在")
    public ResultUtils<Integer> add(WhZonePo whZone) {
        ApiParamValidate.validate(whZone);

        return whZoneService.add(whZone);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新",  notes ="ZONE_CODE_EXIST -> zoneCode已经存在\n"+
                                          "ZONE_NAME_EXIST -> zoneName已经存在\n"+
                                          "ZONE_NAME_EN_EXIST -> zoneNameEn已经存在")
    public ResultUtils<Boolean> updById(WhZonePo whZonePo) {
        ApiParamValidate.notEmpty("id", whZonePo.getId());
        ApiParamValidate.validate(whZonePo);

        return whZoneService.updById(whZonePo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtils<Boolean> delById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Integer id) {

        return whZoneService.delById(id);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtils<WhZoneInfoVo> getById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Integer id) {

        return whZoneService.getById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<WhZoneListVo>> getList(PageDto pageDto, @Validated WhZoneKeyDto keyDto) {

        return whZoneService.getList(pageDto , keyDto);
    }

}
