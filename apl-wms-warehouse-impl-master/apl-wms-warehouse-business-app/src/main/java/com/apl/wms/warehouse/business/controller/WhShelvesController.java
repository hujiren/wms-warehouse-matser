package com.apl.wms.warehouse.business.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.apl.lib.pojo.dto.PageDto;
import org.springframework.web.bind.annotation.*;
import com.apl.wms.warehouse.service.WhShelvesService;
import com.apl.wms.warehouse.po.WhShelvesPo;
import com.apl.wms.warehouse.vo.WhShelvesListVo;
import com.apl.wms.warehouse.vo.WhShelvesInfoVo;
import com.apl.wms.warehouse.dto.WhShelvesKeyDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.validate.ApiParamValidate;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author cy
 * @since 2019-12-21
 */
@RestController
@RequestMapping("/wh-shelves")
@Validated
@Api(value = "货架",tags = "货架")
@Slf4j
public class WhShelvesController {

    @Resource
    public WhShelvesService whShelvesService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加", notes ="SHELVES_NO_EXIST -> shelvesNo已经存在")
    public ResultUtil<Integer> add(WhShelvesPo whShelvesPo) {
        ApiParamValidate.validate(whShelvesPo);

        return whShelvesService.add(whShelvesPo);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新",  notes ="SHELVES_NO_EXIST -> shelvesNo已经存在")
    public ResultUtil<Boolean> updById(WhShelvesPo whShelvesPo) {
        ApiParamValidate.notEmpty("id", whShelvesPo.getId());
        ApiParamValidate.validate(whShelvesPo);

        return whShelvesService.updById(whShelvesPo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtil<Boolean> delById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return whShelvesService.delById(id);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtil<WhShelvesInfoVo> selectById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return whShelvesService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtil<Page<WhShelvesListVo>> getList(PageDto pageDto, @Validated WhShelvesKeyDto keyDto) {

        return whShelvesService.getList(pageDto , keyDto);
    }

}
