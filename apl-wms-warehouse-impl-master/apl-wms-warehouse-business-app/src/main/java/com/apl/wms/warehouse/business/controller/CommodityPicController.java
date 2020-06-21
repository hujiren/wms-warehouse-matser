package com.apl.wms.warehouse.business.controller;

import com.apl.lib.utils.ResultUtils;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.dto.CommodityPicAddDto;
import com.apl.wms.warehouse.po.CommodityPicPo;
import com.apl.wms.warehouse.service.CommodityPicService;
import com.apl.wms.warehouse.vo.CommodityPicListVo;
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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * @author cy
 * @since 2019-12-12
 */
@RestController
@RequestMapping("/commodity-pic")
@Validated
@Api(value = "商品图片",tags = "商品图片")
@Slf4j
public class CommodityPicController {

    @Autowired
    public CommodityPicService commodityPicService;

    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    public ResultUtils<Integer> add(@RequestBody CommodityPicAddDto commodityPicAddDto) {

        return commodityPicService.add(commodityPicAddDto);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    public ResultUtils<Boolean> updById(@Validated CommodityPicPo commodityPicPo) {

        ApiParamValidate.notEmpty("id", commodityPicPo.getId());

        return commodityPicService.updById(commodityPicPo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtils<Boolean> delById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于 1") Long id) {

        return commodityPicService.delById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "查找" , notes = "查找")
    public ResultUtils<List<CommodityPicListVo>> getList(@NotNull(message = "commodityId不能为空") @Min(value = 1, message = "commodityId不能小于1") Long commodityId) {

        return commodityPicService.getList(commodityId);
    }

}
