package com.apl.wms.warehouse.buyer.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.dto.CommodityNameLibKeyDto;
import com.apl.wms.warehouse.po.CommodityNameLibPo;
import com.apl.wms.warehouse.service.CommodityNameLibService;
import com.apl.wms.warehouse.vo.CommodityNameLibInfoVo;
import com.apl.wms.warehouse.vo.CommodityNameLibListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author apl
 * @since 2019-12-20
 */
@RestController
@RequestMapping("/commodity-name-lib")
@Validated
@Api(value = "品名库",tags = "品名库")
@Slf4j
public class CommodityNameLibController {

    @Autowired
    public CommodityNameLibService commodityNameLibService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加", notes ="")
    public ResultUtils<Integer> add(CommodityNameLibPo commodityNameLibPo) {
        ApiParamValidate.validate(commodityNameLibPo);

        return commodityNameLibService.add(commodityNameLibPo);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新",  notes ="")
    public ResultUtils<Boolean> updById(CommodityNameLibPo commodityNameLibPo) {
        ApiParamValidate.notEmpty("id", commodityNameLibPo.getId());
        ApiParamValidate.validate(commodityNameLibPo);

        return commodityNameLibService.updById(commodityNameLibPo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "批量删除" , notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "id，用逗号隔开多个" , paramType = "query")
    })
    public ResultUtils<Boolean> delById(@NotEmpty(message = "id不能为空") String id) {

        return commodityNameLibService.delById(id);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtils<CommodityNameLibInfoVo> selectById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Integer id) {

        return commodityNameLibService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<CommodityNameLibListVo>> getList(PageDto pageDto, @Validated CommodityNameLibKeyDto keyDto)  throws Exception{

        return commodityNameLibService.getList(pageDto , keyDto);
    }

    @PostMapping(value = "/upd-category")
    @ApiOperation(value =  "更新品类",  notes ="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "id，用逗号隔开多个" , paramType = "query"),
            @ApiImplicitParam(name = "categoryId" , value = "品类id", paramType = "query")
    })
    public ResultUtils<Boolean> updCategory(String id, Integer categoryId) {
        ApiParamValidate.notEmpty("id", id);
        ApiParamValidate.notEmpty("categoryId", categoryId);

        return commodityNameLibService.updCategory(id, categoryId);
    }

}
