package com.apl.wms.warehouse.business.controller;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.service.AllocationStockOrderService;
import com.apl.wms.warehouse.service.CancelAllocStockOrderService;
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
import javax.validation.constraints.NotNull;

/**
 * @author hjr start
 * @date 2020/7/6 - 15:11
 */
@RestController
@Api(value = "分配仓库", tags = "分配仓库")
@Slf4j
@RequestMapping("/allocation-stock-for-order")
@Validated
public class AllocationStockOrderController {

    @Autowired
    private AllocationStockOrderService allocationStockOrderService;

    @Autowired
    private CancelAllocStockOrderService cancelAllocStockOrderService;

    @PostMapping(value = "/allocation")
    @ApiOperation(value =  "分配订单库位" , notes = "分配订单库位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outOrderId" , value = "订单id",paramType = "query")
    })
    public ResultUtil<Boolean> allocationManual(@NotNull(message = "outOrderId不能为空") Long outOrderId) throws Exception {
         return allocationStockOrderService.allocationManual(outOrderId);
    }


    @PostMapping(value = "/cancel-allocation")
    @ApiOperation(value =  "取消订单分配库位" , notes = "取消订单分配库位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outOrderId" , value = "订单id",paramType = "query")
    })
    public ResultUtil<Boolean> cancelAllocationManual(@NotNull(message = "订单信息不能为空") Long outOrderId) throws Exception {

        return cancelAllocStockOrderService.cancelAllocationManual(outOrderId);
    }

}
