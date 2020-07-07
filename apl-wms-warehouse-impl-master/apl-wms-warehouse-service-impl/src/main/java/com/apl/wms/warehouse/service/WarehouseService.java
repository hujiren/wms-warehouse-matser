package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtil;

import com.apl.wms.warehouse.po.WarehousePo;
import com.apl.wms.warehouse.vo.WarehouseInfoVo;
import com.apl.wms.warehouse.vo.WarehouseListVo;
import com.apl.wms.warehouse.dto.WarehouseKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 仓库 服务类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
public interface WarehouseService extends IService<WarehousePo> {

        /**
         * @Desc: 添加一个WarehousePo实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> add(WarehousePo warehouse);

        /**
         * @Desc: 根据id 查找一个WarehousePo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> deleteById(Long id);

        /**
         * @Desc: 根据id 更新一个WarehousePo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> updById(WarehousePo warehouse);

        /**
         * @Desc: 根据id 查找一个 WarehousePo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<WarehouseInfoVo> selectById(Long id);

        /**
         * @Desc: 分页查找 WarehousePo 列表
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Page<WarehouseListVo>>getList(PageDto pageDto, WarehouseKeyDto keyDto);

        /**
           * @Description : 
           * @Param ： * @param null
           * @Return ：
           * @Author : arran
           * @Date : 
        */
        ResultUtil<List<WarehouseListVo>> getBind();

}
