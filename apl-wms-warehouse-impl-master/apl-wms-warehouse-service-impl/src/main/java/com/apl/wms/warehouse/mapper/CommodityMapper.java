package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.bo.CommodityReportBo;
import com.apl.wms.warehouse.dto.CommodityKeyDto;
import com.apl.wms.warehouse.po.CommodityPo;
import com.apl.wms.warehouse.vo.CommodityInfoVo;
import com.apl.wms.warehouse.vo.CommodityListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
public interface CommodityMapper extends BaseMapper<CommodityPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-11
     */
    CommodityInfoVo getById(@Param("id") Long id, @Param("customerId") Long customerId);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-11
     */
    List<CommodityListVo> getList(Page page, @Param("kd") CommodityKeyDto keyDto);

    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-19
     */
    List<CommodityInfoVo> exists(@Param("id") Long id,  @Param("sku") String sku,   @Param("commodityName") String commodityName,   @Param("commodityNameEn") String commodityNameEn );


    /**
     * @Desc: 判断自己是否拥有此商品
     * @Author: CY
     * @Date: 2019/12/20 11:12
     */
    Long existsOwm(@Param("commodityId") Long commodityId, @Param("customerId")  Long customerId);


    /**
     * @Desc: 查找打印商品列表
     * @Author: CY
     * @Date: 2019/12/20 17:27
     */
    List<CommodityReportBo> getCommodityReportBarcode(@Param("ids") List<Long> ids, @Param("customerId")  Long customerId);

}
