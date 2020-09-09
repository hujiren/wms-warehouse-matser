package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.po.WhOperatorPo;
import com.apl.wms.warehouse.vo.WhOperatorListVo;
import com.apl.wms.warehouse.vo.WhOperatorInfoVo;
import com.apl.wms.warehouse.dto.WhOperatorKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 仓库操作员 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface WhOperatorMapper extends BaseMapper<WhOperatorPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    public WhOperatorInfoVo getById(@Param("id") Long id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<WhOperatorListVo> getList(Page page, @Param("kd") WhOperatorKeyDto keyDto);


    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<WhOperatorInfoVo> exists(@Param("id") Long id, @Param("whId") Long whId, @Param("memberId") Long memberId);

}
