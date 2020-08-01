package com.apl.wms.warehouse.business.controller;


import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.pojo.bo.PlatformOutOrderStockBo;
import com.apl.wms.warehouse.lib.pojo.vo.CheckOrderStockDetailsVo;
import com.apl.wms.warehouse.lib.pojo.vo.StocksVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.service.StocksService;
import com.apl.wms.warehouse.vo.StocksListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

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


    @PostMapping(value = "/commodity/get")
    @ApiOperation(value =  "获取商品库存信息" , notes = "获取商品库存信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "whId",value = "仓库id",required = true  , paramType = "query"),
            @ApiImplicitParam(name = "commodityIds",value = "商品ids",required = true  , paramType = "query")
    })
    public ResultUtil<List<CheckOrderStockDetailsVo>> getCommodityStockMsg(
                                                @NotNull(message = "仓库id不能为空")
                                                @Min(value = 0, message = "仓库id不能小于0") Long whId ,
                                                @NotEmpty(message = "商品id不能为空") String commodityIds) throws Exception {

        return stocksService.getCommodityStockMsg(whId , commodityIds);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    public ResultUtil<Boolean> add(@Validated StocksPo stocks) {

        return stocksService.add(stocks);
    }


    @PostMapping(value = "/incr")
    @ApiOperation(value =  "增加库存" , notes = "增加库存 ")
    public ResultUtil<Boolean> incrStock(@Validated StocksPo stocks) {

        return stocksService.add(stocks);
    }



    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "long")
    public ResultUtil<Boolean> delById(@Min(value = 1 , message = "id不能小于 1") @NotNull(message = "id不能为空") Long id) {

        return stocksService.deleteById(id);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    public ResultUtil<Boolean> updById(@Validated StocksPo stocksPo) {

        return stocksService.updById(stocksPo);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "long")
    public ResultUtil<StocksPo> getById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于 1") Long id) {

            return stocksService.selectById(id);
    }

    @PostMapping("/list-stocks")
    @ApiOperation(value =  "获取库存列表" , notes = "获取库存列表 ， 进行再分配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "whId",value = "仓库id",required = true  , paramType = "query"),
            @ApiImplicitParam(name = "customerId",value = "客户id",required = true  , paramType = "query"),
            @ApiImplicitParam(name = "isCorrespondence",value = "是否带电",required = true  , paramType = "query"),
            @ApiImplicitParam(name = "keyword",value = "关键字" , paramType = "query")
    })
    public ResultUtil<Page<StocksListVo>> listStocks(PageDto pageDto,
              @NotNull(message = "whId不能为空") @Min(value = 1 , message = "whId不能小于1")Long whId ,
              @NotNull(message = "customerId不能为空") @Min(value = 1 , message = "customerId不能小于1")Long customerId ,
              @NotNull(message = "是否带电不能为空") @Min(value = 0, message = "是否带电最小值为0") @Max(value = 2, message = "是否带电最大值为2") Integer isCorrespondence ,
              String keyword) {


        return stocksService.listStocks(whId , customerId , isCorrespondence , keyword , pageDto);
    }

    @PostMapping("/check")
    @ApiOperation(value =  "校验库存数量是否足够" , notes = "校验库存数量是否足够")
    @ApiIgnore
    public ResultUtil<Boolean> checkStockCount(@RequestBody PlatformOutOrderStockBo platformOutOrderStockBo) {

        Boolean result = stocksService.checkStockCount(platformOutOrderStockBo);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , result);
    }


    @PostMapping("/batch-query")
    @ApiOperation(value =  "查询总库存的实际库存" , notes = "查询总库存的实际库存")
    public ResultUtil<List<StocksVo>> getStocksByCommodityId(@RequestBody List<Long> commodityIdList) {

        ResultUtil<List<StocksVo>> result = stocksService.getStocksByCommodityId(commodityIdList);

        return result;
    }





}
