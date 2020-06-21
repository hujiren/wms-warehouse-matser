package com.apl.wms.warehouse.buyer.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.dto.CommodityCategoryKeyDto;
import com.apl.wms.warehouse.service.CommodityCategoryService;
import com.apl.wms.warehouse.vo.CommodityCategoryInfoVo;
import com.apl.wms.warehouse.vo.CommodityCategoryListVo;
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

/**
 *
 * @author cy
 * @since 2019-12-11
 */
@RestController
@RequestMapping("/commodity-category")
@Validated
@Api(value = "商品种类",tags = "商品种类")
@Slf4j
public class CommodityCategoryController {

    @Autowired
    public CommodityCategoryService commodityCategoryService;


    /*@PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId" , value = "分类 父id" , paramType = "query"),
            @ApiImplicitParam(name = "categoryName" , value = "分类名称", paramType = "query"),
            @ApiImplicitParam(name = "categoryEnName" , value = "分类英文名称", paramType = "query")
    })
    public ResultUtils<Integer> add(@RequestParam(value = "parentId" , required = false) Long parentId ,
                                    @NotEmpty(message = "categoryName 不能为空") String categoryName ,
                                    @NotEmpty(message = "categoryEnName 不能为空") String categoryEnName) {

        return commodityCategoryService.add(parentId , categoryName , categoryEnName);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "分类id" , required = true , paramType = "query"),
            @ApiImplicitParam(name = "categoryName" , value = "分类名称", paramType = "query"),
            @ApiImplicitParam(name = "categoryEnName" , value = "分类英文名称", paramType = "query")
    })
    public ResultUtils<Boolean> updById(@NotNull(message = "id 不能为空")Long id ,
                                        @NotEmpty(message = "categoryName 不能为空") String categoryName ,
                                        @NotEmpty(message = "categoryEnName 不能为空") String categoryEnName) {

        return commodityCategoryService.updById(id , categoryName ,categoryEnName);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtils<Boolean> delById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return commodityCategoryService.delById(id);
    }*/


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtils<CommodityCategoryInfoVo> getById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        commodityCategoryService.getById(id);
        return commodityCategoryService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "按层次显示品类" , notes = "按层次显示品类")
    public ResultUtils<Page<CommodityCategoryListVo>> getList(PageDto pageDto,  @Validated CommodityCategoryKeyDto keyDto) {

        return commodityCategoryService.getList(pageDto , keyDto);
    }

}
