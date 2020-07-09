package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.outstorage.order.lib.pojo.bo.AllocationWarehouseOutOrderBo;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author hjr start
 * @date 2020/7/6 - 15:14
 */
public interface AllocationWarehouseForOrderService extends IService<CommodityBrandPo> {

    // 手动分配仓库
    ResultUtil<Boolean> allocationManual(Long outOrderId);

    // 消息队列分配
    Boolean allocationByQueue(AllocationWarehouseOutOrderBo outOrderBo);
}
