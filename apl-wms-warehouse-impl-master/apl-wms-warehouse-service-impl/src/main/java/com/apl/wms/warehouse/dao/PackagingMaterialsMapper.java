package com.apl.wms.warehouse.dao;

import com.apl.wms.warehouse.bo.CommodityReportBo;
import com.apl.wms.warehouse.dto.PackagingMaterialsKeyDto;
import com.apl.wms.warehouse.lib.pojo.bo.PackagingMaterialsCountBo;
import com.apl.wms.warehouse.po.PackagingMaterialsPo;
import com.apl.wms.warehouse.vo.PackagingMaterialsInfoVo;
import com.apl.wms.warehouse.vo.PackagingMaterialsListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 包装材料 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface PackagingMaterialsMapper extends BaseMapper<PackagingMaterialsPo> {

    /**
     * @Desc: 根据商品id 返回包装材料列表
     * @Author: CY
     * @Date: 2020/6/13 10:18
     */
    List<PackagingMaterialsCountBo> getPackMaterialsByCommodityId(@Param("commodityId") Long commodityId ,
                                                                  @Param("saleStatus") Integer saleStatus ,
                                                                  @Param("reviewStatus") Integer reviewStatus);

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    PackagingMaterialsInfoVo getById(@Param("id" ) Long id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<PackagingMaterialsListVo> getList(Page page, @Param("kd" ) PackagingMaterialsKeyDto keyDto);


    /**
     * @Desc: 查找打印商品列表
     * @Author: CY
     * @Date: 2019/12/20 17:27
     */
    List<CommodityReportBo> getCommodityReportBarcode(@Param("ids") List<Long> ids);


    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-19
     */
    List<PackagingMaterialsInfoVo> exists(@Param("id") Long id,  @Param("sku") String sku,  @Param("commodityName") String commodityName,   @Param("commodityNameEn") String commodityNameEn );



    /**
     * @Desc: 判断自己是否拥有此商品
     * @Author: CY
     * @Date: 2019/12/20 11:12
     */
    Long existsOwm(@Param("commodityId") Long commodityId);



}
