package com.apl.wms.warehouse.business.controller;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.outstorage.order.lib.pojo.bo.AllocationWarehouseOutOrderBo;
import com.apl.wms.warehouse.service.CancelOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author hjr start
 * @date 2020/7/13 - 22:37
 */
@RestController
@Api(value = "取消分配仓库", tags = "取消分配仓库")
@RequestMapping("/cancel")
@Slf4j
@Validated
public class CancelOrderController {

    @Autowired
    private CancelOrderService cancelOrderService;

    @PostMapping(value = "/cancel-order")
    @ApiOperation(value =  "取消订单" , notes = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outOrderListBo" , value = "订单信息对象",paramType = "query")
    })
    public ResultUtil<Boolean> cancelOrder(@RequestBody @NotNull(message = "订单信息不能为空") List<AllocationWarehouseOutOrderBo> outOrderListBo) throws Exception {

        return cancelOrderService.cancelAllocation(outOrderListBo);
    }
}
