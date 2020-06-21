package com.apl.wms.warehouse.business.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.dto.WhOperatorKeyDto;
import com.apl.wms.warehouse.po.WhOperatorPo;
import com.apl.wms.warehouse.service.WhOperatorService;
import com.apl.wms.warehouse.vo.WhOperatorInfoVo;
import com.apl.wms.warehouse.vo.WhOperatorListVo;
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
 *
 * @author CY
 * @since 2019-12-09
 */
@RestController
@RequestMapping("/wh-operator")
@Validated
@Api(value = "仓库操作员",tags = "仓库操作员")
@Slf4j
public class WhOperatorController {

    @Resource
    public WhOperatorService whOperatorService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    public ResultUtils<Integer> add(@Validated WhOperatorPo whOperator) {

        return whOperatorService.add(whOperator);
    }



    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "long")
    public ResultUtils<Boolean> delById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return whOperatorService.delById(id);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    public ResultUtils<Boolean> updById(@Validated WhOperatorPo whOperatorPo) {

        return whOperatorService.updById(whOperatorPo);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "long")
    public ResultUtils<WhOperatorInfoVo> getById(@Min(value = 1 , message = "id不能小于 1") Long id) {

            return whOperatorService.selectById(id);
    }

    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<WhOperatorListVo>> getList(PageDto pageDto, @Validated WhOperatorKeyDto keyDto) {


        return whOperatorService.getList(pageDto , keyDto);
    }


}
