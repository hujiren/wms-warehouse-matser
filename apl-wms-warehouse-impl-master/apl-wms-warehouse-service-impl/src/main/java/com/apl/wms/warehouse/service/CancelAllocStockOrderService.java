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
public interface CancelAllocStockOrderService extends IService<StocksPo> {


    /**
     * 手动取消分配订单库位
     * @param outOrderId
     * @return
     */
    ResultUtil<Boolean> cancelAllocationManual(Long outOrderId) throws Exception;


    /**
     * 消息队列取消分配订单库位
     * @param outOrderBo
     * @return
     */
    Boolean cancelAllocationByQueue(AllocationWarehouseOutOrderBo outOrderBo) throws Exception;
}
