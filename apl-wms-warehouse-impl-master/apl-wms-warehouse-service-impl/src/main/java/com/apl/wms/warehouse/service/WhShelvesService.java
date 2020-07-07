package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtil;

import com.apl.wms.warehouse.po.WhShelvesPo;
import com.apl.wms.warehouse.vo.WhShelvesListVo;
import com.apl.wms.warehouse.vo.WhShelvesInfoVo;
import com.apl.wms.warehouse.dto.WhShelvesKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 货架 service接口
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
public interface WhShelvesService extends IService<WhShelvesPo> {

        /**
         * @Desc: 添加一个WhShelvesPo实体
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<Integer> add(WhShelvesPo whShelves);


        /**
         * @Desc: 根据id 更新一个WhShelvesPo 实体
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<Boolean> updById(WhShelvesPo whShelves);


        /**
         * @Desc: 根据id 查找一个WhShelvesPo 实体
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<Boolean> delById(Long id);


        /**
         * @Desc: 根据id 查找一个 WhShelvesPo 实体
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<WhShelvesInfoVo> selectById(Long id);


        /**
         * @Desc: 分页查找 WhShelvesPo 列表
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<Page<WhShelvesListVo>>getList(PageDto pageDto, WhShelvesKeyDto keyDto);

}
