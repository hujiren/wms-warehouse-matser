package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtils;

import com.apl.wms.warehouse.po.WhZonePo;
import com.apl.wms.warehouse.vo.WhZoneListVo;
import com.apl.wms.warehouse.vo.WhZoneInfoVo;
import com.apl.wms.warehouse.dto.WhZoneKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 仓库分区 service接口
 * </p>
 *
 * @author apl
 * @since 2019-12-16
 */
public interface WhZoneService extends IService<WhZonePo> {

        /**
         * @Desc: 添加一个WhZonePo实体
         * @author apl
         * @since 2019-12-16
         */
        ResultUtils<Integer> add(WhZonePo whZone);


        /**
         * @Desc: 根据id 更新一个WhZonePo 实体
         * @author apl
         * @since 2019-12-16
         */
        ResultUtils<Boolean> updById(WhZonePo whZone);


        /**
         * @Desc: 根据id 查找一个WhZonePo 实体
         * @author apl
         * @since 2019-12-16
         */
        ResultUtils<Boolean> delById(Integer id);


        /**
         * @Desc: 根据id 查找一个 WhZonePo 实体
         * @author apl
         * @since 2019-12-16
         */
        ResultUtils<WhZoneInfoVo> getById(Integer id);


        /**
         * @Desc: 分页查找 WhZonePo 列表
         * @author apl
         * @since 2019-12-16
         */
        ResultUtils<Page<WhZoneListVo>>getList(PageDto pageDto, WhZoneKeyDto keyDto);

}
