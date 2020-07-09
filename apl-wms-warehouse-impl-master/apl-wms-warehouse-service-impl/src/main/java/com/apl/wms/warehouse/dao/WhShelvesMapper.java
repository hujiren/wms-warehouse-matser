package com.apl.wms.warehouse.dao;

import com.apl.wms.warehouse.po.WhShelvesPo;
import com.apl.wms.warehouse.vo.WhShelvesListVo;
import com.apl.wms.warehouse.vo.WhShelvesInfoVo;
import com.apl.wms.warehouse.dto.WhShelvesKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 货架 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
public interface WhShelvesMapper extends BaseMapper<WhShelvesPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-21
     */
    public WhShelvesInfoVo getById(@Param("id") Long id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-21
     */
    List<WhShelvesListVo> getList(Page page, @Param("kd") WhShelvesKeyDto keyDto);


    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-21
     */
    List<WhShelvesInfoVo> exists(@Param("id") Long id, @Param("shelvesNo") String shelvesNo);

}
