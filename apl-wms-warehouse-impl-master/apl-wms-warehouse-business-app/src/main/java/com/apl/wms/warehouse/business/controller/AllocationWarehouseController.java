package com.apl.wms.warehouse.business.controller;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.service.AllocationWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author hjr start
 * @date 2020/7/6 - 15:11
 */
@RestController
@Api(value = "分配仓库", tags = "分配仓库")
@Slf4j
@RequestMapping("/allocation-warehouse")
@Validated
public class AllocationWarehouseController {

    @Autowired
    private AllocationWarehouseService allocationWarehouseService;

    @RequestMapping(value = "/allocation-stock-by-orders")
    @ApiOperation(value =  "分配订单" , notes = "分配订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderIds" , value = "订单ids",paramType = "query")
    })
    public ResultUtil<Boolean> AllocationStockByOrders(@NotNull(message = "ids不能为空") String orderIds) throws Exception {
         return allocationWarehouseService.AllocationStockByOrders(orderIds);
    }
}
