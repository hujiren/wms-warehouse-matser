package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.dto.CommodityBrandKeyDto;
import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.service.impl.AllocationWarehouseServiceImpl;
import com.apl.wms.warehouse.vo.CommodityBrandInfoVo;
import com.apl.wms.warehouse.vo.CommodityBrandListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品品牌 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
public interface CommodityBrandMapper extends BaseMapper<CommodityBrandPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-11
     */
    public CommodityBrandInfoVo getById(@Param("id") Long id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-11
     */
    List<CommodityBrandListVo> getList(Page page, @Param("kd") CommodityBrandKeyDto keyDto);

    /**
     * @Desc: 根据商标名称 ，查找商标是否存在
     * @Author: CY
     * @Date: 2019/12/11 17:45
     */
    List<CommodityBrandInfoVo> exist(@Param("id")Long id , @Param("brandName")String brandName, @Param("brandNameEn")String brandNameEn);


    @MapKey("commodityKey")
    Map<String, StocksPo> queryTotalStock(@Param("whId") Long whId, @Param("ids") String ids, @Param("minId") Long minId, @Param("maxId") Long maxId);


    List<StorageLocalStocksPo> queryStorageLocalStock(@Param("whId") Long whId, @Param("ids") String ids, @Param("minId") Long minId, @Param("maxId") Long maxId);

    /**
     * 更新总库存
     * @param newStocksPos
     * @return
     */
    Integer updateStock(@Param("stock") List<StocksPo> newStocksPos);




    /**
     * 更新库位库存
     */
    Integer updateStorageLocalStock(@Param("storage") List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos);
}
