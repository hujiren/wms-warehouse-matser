package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author hjr start
 * @date 2020/7/6 - 15:14
 */
public interface AllocationWarehouseService extends IService<CommodityBrandPo> {

    ResultUtils<Boolean> AllocationStockByOrders(String orderIds) throws Exception ;
}