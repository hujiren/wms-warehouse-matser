package com.apl.wms.warehouse.dao;

import com.apl.wms.warehouse.po.ShelvesSpecPo;
import com.apl.wms.warehouse.vo.ShelvesSpecListVo;
import com.apl.wms.warehouse.vo.ShelvesSpecInfoVo;
import com.apl.wms.warehouse.dto.ShelvesSpecKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 货架规格 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-19
 */
public interface ShelvesSpecMapper extends BaseMapper<ShelvesSpecPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-19
     */
    public ShelvesSpecInfoVo getById(@Param("id") Long id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-19
     */
    List<ShelvesSpecListVo> getList(Page page, @Param("kd") ShelvesSpecKeyDto keyDto);


    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-19
     */
    List<ShelvesSpecInfoVo> exists(@Param("id") Long id, @Param("specNo") String specNo);

}
