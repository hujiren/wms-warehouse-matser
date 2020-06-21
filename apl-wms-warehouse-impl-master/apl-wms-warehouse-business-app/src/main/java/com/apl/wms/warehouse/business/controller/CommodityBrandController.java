package com.apl.wms.warehouse.business.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.dto.CommodityBrandKeyDto;
import com.apl.wms.warehouse.service.CommodityBrandService;
import com.apl.wms.warehouse.vo.CommodityBrandInfoVo;
import com.apl.wms.warehouse.vo.CommodityBrandListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author cy
 * @since 2019-12-11
 */
@RestController
@RequestMapping("/commodity-brand")
@Validated
@Api(value = "商品品牌",tags = "商品品牌")
@Slf4j
public class CommodityBrandController {

    @Resource
    public CommodityBrandService commodityBrandService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "brandName" , value = "品牌名称" ,paramType = "query"),
            @ApiImplicitParam(name = "brandNameEn" , value = "品牌英文名称",paramType = "query")
    })
    public ResultUtils<Integer> add(@NotEmpty(message = "brandName 不能为空")String brandName ,
                                    @NotEmpty(message = "brandNameEn 不能为空") String brandNameEn) {

        return commodityBrandService.add(brandName ,brandNameEn);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "brandId" , value = "brandId" , required = true , paramType = "query"),
            @ApiImplicitParam(name = "brandName" , value = "品牌名称",paramType = "query"),
            @ApiImplicitParam(name = "brandNameEn" , value = "品牌英文名称",paramType = "query")
    })
    public ResultUtils<Boolean> updById(@NotNull(message = "brandId 不能为空") Long brandId ,
                                        @NotEmpty(message = "brandName 不能为空")String brandName ,
                                        @NotEmpty(message = "brandNameEn 不能为空") String brandNameEn) {

        return commodityBrandService.updById(brandId ,brandName , brandNameEn);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除  如果品牌有多个，用逗号隔开")
    @ApiImplicitParam(name = "brandIdList",value = " id 列表",required = true  , paramType = "query")
    public ResultUtils<Boolean> delById(@NotNull(message = "brandIdList 不能为空") String brandIdList) {

        return commodityBrandService.delById(brandIdList);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtils<CommodityBrandInfoVo> getById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return commodityBrandService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<CommodityBrandListVo>> getList(PageDto pageDto, @Validated CommodityBrandKeyDto keyDto) {

        return commodityBrandService.getList(pageDto , keyDto);
    }

}
