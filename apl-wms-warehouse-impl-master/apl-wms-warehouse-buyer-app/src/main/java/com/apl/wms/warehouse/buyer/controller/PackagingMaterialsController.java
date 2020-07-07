package com.apl.wms.warehouse.buyer.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.service.PackagingMaterialsService;
import com.apl.wms.warehouse.dto.PackagingMaterialsKeyDto;
import com.apl.wms.warehouse.po.PackagingMaterialsPo;
import com.apl.wms.warehouse.vo.CommodityInfoVo;
import com.apl.wms.warehouse.vo.PackagingMaterialsListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author cy
 * @since 2019-12-17
 */
@RestController
@RequestMapping("/packaging-materials")
@Validated
@Api(value = "包装材料",tags = "包装材料")
@Slf4j
public class PackagingMaterialsController {

    @Autowired
    public PackagingMaterialsService packagingMaterialsService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    public ResultUtil<Long> add(@Validated PackagingMaterialsPo packagingMaterialsPo) {

        return packagingMaterialsService.add(packagingMaterialsPo);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    public ResultUtil<Boolean> updById(@Validated PackagingMaterialsPo packagingMaterialsPo) {

        ApiParamValidate.notEmpty("id", packagingMaterialsPo.getId());

        return packagingMaterialsService.updById(packagingMaterialsPo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtil<Boolean> delById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return packagingMaterialsService.delById(id);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtil<CommodityInfoVo> getById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return packagingMaterialsService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtil<Page<PackagingMaterialsListVo>> getList(PageDto pageDto, @Validated PackagingMaterialsKeyDto keyDto)  throws Exception{

        return packagingMaterialsService.getList(pageDto , keyDto);
    }


    @PostMapping("/print")
    @ApiOperation(value =  "打印条形码" , notes = "")
    @ApiImplicitParam(name = "id",value = " 商品id列表，多个id用逗号隔开",required = true  , paramType = "query")
    public void print(@NotNull(message = "商品id 不能为空") String id) throws Exception {
        ApiParamValidate.notEmpty("id",id);

        packagingMaterialsService.print(id);
    }


}
