package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.po.WarehousePo;
import com.apl.wms.warehouse.vo.WarehouseInfoVo;
import com.apl.wms.warehouse.vo.WarehouseListVo;
import com.apl.wms.warehouse.dto.WarehouseKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 仓库 Mapper 接口
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
public interface WarehouseMapper extends BaseMapper<WarehousePo> {


    /**
     * @Desc: 保存或者修改时检查不能重复的字段
     * @Author: Juner
     * @Date: 2019-12-09
     */
    List<WarehousePo> exist(
            @Param("id") Long id,
            @Param("whCode") String whCode,
            @Param("whName") String whName,
            @Param("whNameEn") String whNameEn
    );


    /**
     * @Desc: 根据id 查找详情
     * @Author: CY
     * @Date: 2019-12-09
     */
    WarehouseInfoVo getById(@Param("id") Long id);

    /**
     * @Desc: 查找列表
     * @Author: CY
     * @Date: 2019-12-09
     */
    List<WarehouseListVo> getList(Page page, @Param("kd") WarehouseKeyDto keyDto);

}
