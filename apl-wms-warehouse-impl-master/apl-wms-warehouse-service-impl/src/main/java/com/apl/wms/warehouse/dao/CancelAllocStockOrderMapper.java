package com.apl.wms.warehouse.dao;

import com.apl.wms.warehouse.bo.StocksBo;
import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author hjr start
 * @date 2020/7/13 - 22:34
 */
public interface CancelAllocStockOrderMapper extends BaseMapper<StocksPo> {

    Integer updateOrderStatusById(@Param("orderStatus") Integer orderStatus, @Param("orderId") Long orderId);

    @MapKey("commodityKey")
    Map<String, StocksBo> queryTotalStock(@Param("whId") Long whId, @Param("ids") String ids, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey);


    /**
     * 根据商品Id查询库位库存
     * @param
     * @param minKey
     * @param maxKey
     * @return
     */
    List<StorageLocalStocksPo> queryStorageLocalStock(@Param("ids") String ids, @Param("minKey") Long minKey, @Param("maxKey") Long maxKey);



}
