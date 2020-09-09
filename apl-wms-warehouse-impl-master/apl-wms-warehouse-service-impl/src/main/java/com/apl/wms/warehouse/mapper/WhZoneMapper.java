package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.po.WhZonePo;
import com.apl.wms.warehouse.vo.WhZoneListVo;
import com.apl.wms.warehouse.vo.WhZoneInfoVo;
import com.apl.wms.warehouse.dto.WhZoneKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 仓库分区 Mapper 接口
 * </p>
 *
 * @author apl
 * @since 2019-12-16
 */
public interface WhZoneMapper extends BaseMapper<WhZonePo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-16
     */
    public WhZoneInfoVo getById(@Param("id") Integer id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-16
     */
    List<WhZoneListVo> getList(Page page, @Param("kd") WhZoneKeyDto keyDto);


    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-16
     */
    List<WhZoneInfoVo> exists(@Param("id") Long id, @Param("zoneCode") String zoneCode, @Param("zoneName") String zoneName, @Param("zoneNameEn") String zoneNameEn);

    List<WhZoneInfoVo> exists(@Param("id") Long id, @Param("whId") Long whId, @Param("zoneCode") String zoneCode, @Param("zoneName") String zoneName, @Param("zoneNameEn") String zoneNameEn);

}
