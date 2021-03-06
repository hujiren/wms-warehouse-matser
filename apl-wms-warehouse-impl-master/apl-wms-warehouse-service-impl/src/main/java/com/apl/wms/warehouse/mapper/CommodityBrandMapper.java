package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.dto.CommodityBrandKeyDto;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.apl.wms.warehouse.vo.CommodityBrandInfoVo;
import com.apl.wms.warehouse.vo.CommodityBrandListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;

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




}
