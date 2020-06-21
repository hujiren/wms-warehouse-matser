package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.po.CommodityNameLibPo;
import com.apl.wms.warehouse.vo.CommodityNameLibListVo;
import com.apl.wms.warehouse.vo.CommodityNameLibInfoVo;
import com.apl.wms.warehouse.dto.CommodityNameLibKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 品名库 Mapper 接口
 * </p>
 *
 * @author apl
 * @since 2019-12-20
 */
public interface CommodityNameLibMapper extends BaseMapper<CommodityNameLibPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-20
     */
    public CommodityNameLibInfoVo getById(@Param("id") Integer id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-20
     */
    List<CommodityNameLibListVo> getList(Page page, @Param("kd") CommodityNameLibKeyDto keyDto);

    /**
     * @Desc: 更新品类
     * @Author: arran
     * @Date: 2019-12-20
     */
    int updCategory( @Param("id") String id,  @Param("categoryId") Integer categoryId);


    /**
     * @Desc: 更新品类
     * @Author: arran
     * @Date: 2019-12-20
     */
    int del( @Param("id") String id);

}
