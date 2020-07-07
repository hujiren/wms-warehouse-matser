package com.apl.wms.warehouse.buyer.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.WhDetailsKeyDto;
import com.apl.wms.warehouse.po.WhDetailsPo;
import com.apl.wms.warehouse.service.WhDetailsService;
import com.apl.wms.warehouse.vo.WhDetailsListVo;
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
@RequestMapping("/wh-details")
@Validated
@Api(value = "仓库详细",tags = "仓库详细")
@Slf4j
public class WhDetailsController {

    @Resource
    public WhDetailsService whDetailsService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    public ResultUtil<Boolean> add(@Validated WhDetailsPo whDetails) {

        return whDetailsService.add(whDetails);
    }



    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "long")
    public ResultUtil<Boolean> delById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return whDetailsService.deleteById(id);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    public ResultUtil<Boolean> updById(@Validated WhDetailsPo whDetailsPo) {

        return whDetailsService.updByWhId(whDetailsPo);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "long")
    public ResultUtil<WhDetailsPo> getById(@Min(value = 1 , message = "id不能小于 1") Long id) {

            return whDetailsService.selectById(id);
    }

    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtil<Page<WhDetailsListVo>> getList(PageDto pageDto, @Validated WhDetailsKeyDto keyDto) {


        return whDetailsService.getList(pageDto , keyDto);
    }


}
