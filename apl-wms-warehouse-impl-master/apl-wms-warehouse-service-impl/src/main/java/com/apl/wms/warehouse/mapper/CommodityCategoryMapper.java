package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.po.CommodityCategoryPo;
import com.apl.wms.warehouse.vo.CommodityCategoryInfoVo;
import com.apl.wms.warehouse.vo.CommodityCategoryListVo;
import com.apl.wms.warehouse.dto.CommodityCategoryKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 商品种类 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-10
 */
public interface CommodityCategoryMapper extends BaseMapper<CommodityCategoryPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-10
     */
    public CommodityCategoryInfoVo getById(@Param("id") Long id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-10
     */
    List<CommodityCategoryListVo> getList(Page page , @Param("kd") CommodityCategoryKeyDto keyDto);


    /**
     * @Desc: 根据分类名称， 查找分类信息
     * @Author: CY
     * @Date: 2019/12/11 17:38
     */
    CommodityCategoryInfoVo getCommodityByName(@Param("categoryName") String categoryName,@Param("categoryEnName") String categoryEnName);

    /**
     * @Desc: 根据分类名称， 查找分类信息
     * @Author: CY
     * @Date: 2019/12/11 17:38
     */
    List<CommodityCategoryInfoVo> exists(@Param("id") Long id, @Param("categoryName") String categoryName,@Param("categoryEnName") String categoryEnName);

    /**
     * @Desc: 根据 父分类 ，查找子分类
     * @Author: CY
     * @Date: 2019/12/13 19:09
     */
    List<CommodityCategoryInfoVo> getChildCategory(@Param("id") Long id);


    /**
     * @Desc: 获取分类字符串
     * @Author: CY
     * @Date: 2019/12/14 11:50
     */
    String getCategoryNames(@Param("categoryId") Long categoryId);

    /**
     * @Desc: 获取子分类的父级id 列表
     * @Author: CY
     * @Date: 2019/12/14 12:16
     */
    CommodityCategoryInfoVo getCategoryPid(@Param("categoryId") Long categoryId);
}
