package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.dto.GoodsBasketKeyDto;
import com.apl.wms.warehouse.po.GoodsBasketPo;
import com.apl.wms.warehouse.vo.GoodsBasketInfoVo;
import com.apl.wms.warehouse.vo.GoodsBasketListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 货蓝 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface GoodsBasketMapper extends BaseMapper<GoodsBasketPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    GoodsBasketInfoVo getById(@Param("id" ) Long id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<GoodsBasketInfoVo> getList(Page page, @Param("kd") GoodsBasketKeyDto keyDto);


    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<GoodsBasketInfoVo> exists(@Param("id" ) Long id, @Param("basketNo" ) String basketNo);
}
