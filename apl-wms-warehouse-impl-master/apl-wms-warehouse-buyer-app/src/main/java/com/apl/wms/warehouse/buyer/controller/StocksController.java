package com.apl.wms.warehouse.buyer.controller;


import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.service.StocksService;
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
 * @author CY
 * @since 2019-12-09
 */
@RestController
@RequestMapping("/stocks")
@Validated
@Api(value = "库存",tags = "库存")
@Slf4j
public class StocksController {

    @Autowired
    public StocksService stocksService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    public ResultUtils<Boolean> add(@Validated StocksPo stocks) {

        return stocksService.add(stocks);
    }



    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "long")
    public ResultUtils<Boolean> delById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return stocksService.deleteById(id);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    public ResultUtils<Boolean> updById(@Validated StocksPo stocksPo) {

        return stocksService.updById(stocksPo);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "long")
    public ResultUtils<StocksPo> getById(@Min(value = 1 , message = "id不能小于 1") Long id) {

        return stocksService.selectById(id);
    }



}
