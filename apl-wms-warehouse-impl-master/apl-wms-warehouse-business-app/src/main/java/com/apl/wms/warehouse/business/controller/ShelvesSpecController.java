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
import com.apl.wms.warehouse.service.ShelvesSpecService;
import com.apl.wms.warehouse.po.ShelvesSpecPo;
import com.apl.wms.warehouse.vo.ShelvesSpecListVo;
import com.apl.wms.warehouse.vo.ShelvesSpecInfoVo;
import com.apl.wms.warehouse.dto.ShelvesSpecKeyDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.validate.ApiParamValidate;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author cy
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/shelves-spec")
@Validated
@Api(value = "货架规格",tags = "货架规格")
@Slf4j
public class ShelvesSpecController {

    @Resource
    public ShelvesSpecService shelvesSpecService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加", notes ="SPEC_NO_EXIST -> specNo已经存在")
    public ResultUtils<Integer> add(ShelvesSpecPo shelvesSpecPo) {
        ApiParamValidate.validate(shelvesSpecPo);

        return shelvesSpecService.add(shelvesSpecPo);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新",  notes ="SPEC_NO_EXIST -> specNo已经存在")
    public ResultUtils<Boolean> updById(ShelvesSpecPo shelvesSpecPo) {
        ApiParamValidate.notEmpty("id", shelvesSpecPo.getId());
        ApiParamValidate.validate(shelvesSpecPo);

        return shelvesSpecService.updById(shelvesSpecPo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtils<Boolean> delById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return shelvesSpecService.delById(id);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtils<ShelvesSpecInfoVo> selectById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return shelvesSpecService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<ShelvesSpecListVo>> getList(PageDto pageDto, @Validated ShelvesSpecKeyDto keyDto) {

        return shelvesSpecService.getList(pageDto , keyDto);
    }

}
