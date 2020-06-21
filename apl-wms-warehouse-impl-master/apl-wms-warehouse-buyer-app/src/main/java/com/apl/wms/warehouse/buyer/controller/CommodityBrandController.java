package com.apl.wms.warehouse.buyer.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.dto.CommodityBrandKeyDto;
import com.apl.wms.warehouse.service.CommodityBrandService;
import com.apl.wms.warehouse.vo.CommodityBrandListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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



    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<CommodityBrandListVo>> getList(PageDto pageDto, @Validated CommodityBrandKeyDto keyDto) {

        return commodityBrandService.getList(pageDto , keyDto);
    }

}
