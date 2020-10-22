package com.apl.wms.warehouse.service;

import com.apl.lib.utils.ResultUtil;

import com.apl.wms.warehouse.po.ShelvesSpecPo;
import com.apl.wms.warehouse.vo.ShelvesSpecListVo;
import com.apl.wms.warehouse.vo.ShelvesSpecInfoVo;
import com.apl.wms.warehouse.dto.ShelvesSpecKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 货架规格 service接口
 * </p>
 *
 * @author cy
 * @since 2019-12-19
 */
public interface ShelvesSpecService extends IService<ShelvesSpecPo> {

    /**
     * @Desc: 添加一个ShelvesSpecPo实体
     * @author cy
     * @since 2019-12-19
     */
    ResultUtil<Integer> add(ShelvesSpecPo shelvesSpec);


    /**
     * @Desc: 根据id 更新一个ShelvesSpecPo 实体
     * @author cy
     * @since 2019-12-19
     */
    ResultUtil<Boolean> updById(ShelvesSpecPo shelvesSpec);


    /**
     * @Desc: 根据id 查找一个ShelvesSpecPo 实体
     * @author cy
     * @since 2019-12-19
     */
    ResultUtil<Boolean> delById(Long id);


    /**
     * @Desc: 根据id 查找一个 ShelvesSpecPo 实体
     * @author cy
     * @since 2019-12-19
     */
    ResultUtil<ShelvesSpecInfoVo> selectById(Long id);


    /**
     * @Desc: 分页查找 ShelvesSpecPo 列表
     * @author cy
     * @since 2019-12-19
     */
    ResultUtil<Page<ShelvesSpecListVo>> getList(PageDto pageDto, ShelvesSpecKeyDto keyDto);

}
