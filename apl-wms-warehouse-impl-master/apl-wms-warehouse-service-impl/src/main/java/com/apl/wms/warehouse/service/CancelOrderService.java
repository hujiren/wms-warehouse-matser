package com.apl.wms.warehouse.service;

import com.apl.lib.utils.ResultUtil;
import com.apl.wms.outstorage.order.lib.pojo.bo.AllocationWarehouseOutOrderBo;
import com.apl.wms.warehouse.po.StocksPo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author hjr start
 * @date 2020/7/13 - 21:53
 */
public interface CancelOrderService extends IService<StocksPo> {

    /**
     * 取消订单
     * @param outOrderListBo
     * @return
     */
    ResultUtil<Boolean> cancelAllocation(List<AllocationWarehouseOutOrderBo> outOrderListBo) throws Exception;
}
