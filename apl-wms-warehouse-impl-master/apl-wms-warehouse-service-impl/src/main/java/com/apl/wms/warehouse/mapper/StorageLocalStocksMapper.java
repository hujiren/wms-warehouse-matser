package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalStock;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.dto.StorageCommodityKeyDto;
import com.apl.wms.warehouse.vo.StorageLocalStocksListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 库位储存商品数量 Mapper 接口
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
public interface StorageLocalStocksMapper extends BaseMapper<StorageLocalStocksPo> {

    /**
     * @Desc: 获取商品库位库存列表
     * @Author: CY
     * @Date: 2020/6/19 16:54
     */
    List<StorageLocalStock> getCommodityStorageLocalStockList(@Param("whId") Long whId , @Param("commodityIds") List<Long> commodityIds);

    /**
     * @Desc: 根据id 查找详情
     * @Author: CY
     * @Date: 2019-12-09
     */
    StorageLocalStocksPo getById(@Param("id") Long id);

    /**
     * @Desc: 查找列表
     * @Author: CY
     * @Date: 2019-12-09
     */
    List<StorageLocalStocksListVo> getList(Page page, @Param("kd") StorageCommodityKeyDto keyDto);


    /**
     * @Desc: 根据库位id 查找库位是否存放商品
     * @Author: CY
     * @Date: 2019/12/17 16:00
     */
    Long judgeStorageLocationIsNull(@Param("storageId") Long storageId);

    /**
     * @Desc: 查找库位库存
     * @Author: CY
     * @Date: 2020/1/6 16:41
     */
    StorageLocalStocksPo getStorageLocalStocks(@Param("storageLocalId") Long storageLocalId, @Param("commodityId") Long commodityId);


    /**
     * @Desc: 获取商品对应的库位库存列表
     * @Author: CY
     * @Date: 2020/6/9 15:01
     */
    List<StorageLocalStocksPo> getStorageLocalByCommodityId(@Param("commodityId") Long commodityId);



    /**
     * 更新分配库位库存
     */
    Integer updateStorageLocalStock(@Param("storageLocalStocksBos") List<CompareStorageLocalStocksBo> storageLocalStocksBos);

}
