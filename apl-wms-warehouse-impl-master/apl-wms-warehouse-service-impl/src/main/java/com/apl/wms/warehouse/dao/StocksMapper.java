package com.apl.wms.warehouse.dao;


import com.apl.wms.warehouse.bo.StockUpdBo;
import com.apl.wms.warehouse.lib.pojo.vo.CheckOrderStockDetailsVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.vo.StocksListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 库存 Mapper 接口
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
public interface StocksMapper extends BaseMapper<StocksPo> {

    /**
     * @Desc: 获取仓库下商品总库存
     * @Author: CY
     * @Date: 2020/6/19 16:31
     */
    List<CheckOrderStockDetailsVo> getWareHouseCommodityCountList(@Param("whId") Long whId, @Param("commodityIds") List<Long> commodityIds);


    /**
     * @Desc: 根据id 查找详情
     * @Author: CY
     * @Date: 2019-12-09
     */
    StocksPo getById(@Param("id") Long id);


    /**
     * @Desc: 查找商品 库存
     * @Author: CY
     * @Date: 2020/1/6 16:39
     */
    StocksPo getCommodityStock(@Param("whId") Long whId, @Param("commodityId") Long commodityId);

    /**
     * @Desc: 获取库存列表 ， 准备进行中转，运输到其他仓库
     * @Author: CY
     * @Date: 2020/1/7 10:46
     */
    List<StocksListVo> listStocks(Page page , @Param("whId")Long whId, @Param("customerId")Long customerId, @Param("isCorrespondence")Integer isCorrespondence, @Param("keyword")String keyword);

    /**
     * @Desc: 插入库存记录
     * @Author: CY
     * @Date: 2020/3/24 17:10
     */
    Boolean addStock(@Param("stockUpdBo") StockUpdBo stockUpdBo);


}
