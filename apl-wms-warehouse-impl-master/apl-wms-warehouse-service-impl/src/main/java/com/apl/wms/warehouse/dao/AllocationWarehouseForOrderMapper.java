package com.apl.wms.warehouse.dao;

import com.apl.wms.warehouse.bo.StocksBo;
import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author hjr start
 * @date 2020/7/10 - 11:46
 */
public interface AllocationWarehouseForOrderMapper extends BaseMapper<StocksPo> {


    @MapKey("commodityKey")
    Map<String, StocksBo> queryTotalStock(@Param("whId") Long whId, @Param("ids") String ids, @Param("minId") Long minId, @Param("maxId") Long maxId);


    List<StorageLocalStocksPo> queryStorageLocalStock(@Param("whId") Long whId, @Param("ids") String ids, @Param("minId") Long minId, @Param("maxId") Long maxId);

    /**
     * 更新总库存
     * @param newStocksPos
     * @return
     */
    Integer updateStock(@Param("stocksPos") List<StocksPo> newStocksPos);



    /**
     * 更新库位库存
     */
    Integer updateStorageLocalStock(@Param("storageLocalStocksBos") List<CompareStorageLocalStocksBo> storageLocalStocksBos);
}
