package com.apl.wms.warehouse.business.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.apl.lib.pojo.dto.PageDto;
import org.springframework.web.bind.annotation.*;
import com.apl.wms.warehouse.service.GoodsBasketService;
import com.apl.wms.warehouse.po.GoodsBasketPo;
import com.apl.wms.warehouse.vo.GoodsBasketListVo;
import com.apl.wms.warehouse.vo.GoodsBasketInfoVo;
import com.apl.wms.warehouse.dto.GoodsBasketKeyDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.validate.ApiParamValidate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author cy
 * @since 2019-12-21
 */
@RestController
@RequestMapping("/goods-basket")
@Validated
@Api(value = "货蓝",tags = "货蓝")
@Slf4j
public class GoodsBasketController {

    @Autowired
    public GoodsBasketService goodsBasketService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加", notes ="BASKET_SN_EXIST -> basketSn已经存在")
    public ResultUtils<Integer> add(GoodsBasketPo goodsBasketPo) {
        ApiParamValidate.validate(goodsBasketPo);

        return goodsBasketService.add(goodsBasketPo);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新",  notes ="BASKET_SN_EXIST -> basketSn已经存在")
    public ResultUtils<Boolean> updById(GoodsBasketPo goodsBasketPo) {
        ApiParamValidate.notEmpty("id", goodsBasketPo.getId());
        ApiParamValidate.validate(goodsBasketPo);

        return goodsBasketService.updById(goodsBasketPo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtils<Boolean> delById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return goodsBasketService.delById(id);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtils<GoodsBasketInfoVo> selectById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return goodsBasketService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<GoodsBasketListVo>> getList(PageDto pageDto, @Validated GoodsBasketKeyDto keyDto) {

        return goodsBasketService.getList(pageDto , keyDto);
    }

}
