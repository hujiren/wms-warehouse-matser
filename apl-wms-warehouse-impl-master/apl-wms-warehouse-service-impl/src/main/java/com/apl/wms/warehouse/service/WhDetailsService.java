package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtil;

import com.apl.wms.warehouse.po.WhDetailsPo;
import com.apl.wms.warehouse.vo.WhDetailsListVo;
import com.apl.wms.warehouse.dto.WhDetailsKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 仓库详细 服务类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
public interface WhDetailsService extends IService<WhDetailsPo> {

        /**
         * @Desc: 添加一个WhDetailsPo实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> add(WhDetailsPo whDetails);

        /**
         * @Desc: 根据id 查找一个WhDetailsPo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> deleteById(Long id);

        /**
         * @Desc: 根据id 更新一个WhDetailsPo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> updByWhId(WhDetailsPo whDetails);

        /**
         * @Desc: 根据id 查找一个 WhDetailsPo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<WhDetailsPo> selectById(Long id);

        /**
         * @Desc: 分页查找 WhDetailsPo 列表
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Page<WhDetailsListVo>>getList(PageDto pageDto, WhDetailsKeyDto keyDto);

        }
