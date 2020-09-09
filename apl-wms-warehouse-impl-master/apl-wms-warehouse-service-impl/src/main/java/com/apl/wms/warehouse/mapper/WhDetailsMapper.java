package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.po.WhDetailsPo;
import com.apl.wms.warehouse.vo.WhDetailsListVo;
import com.apl.wms.warehouse.dto.WhDetailsKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 仓库详细 Mapper 接口
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
public interface WhDetailsMapper extends BaseMapper<WhDetailsPo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: CY
     * @Date: 2019-12-09
     */
    public WhDetailsPo getByWhId(@Param("id") Long id);

    /**
     * @Desc: 查找列表
     * @Author: CY
     * @Date: 2019-12-09
     */
    List<WhDetailsListVo> getList(Page page, @Param("kd") WhDetailsKeyDto keyDto);

    public boolean updateByWhId( WhDetailsPo whDetailsPo);

}
