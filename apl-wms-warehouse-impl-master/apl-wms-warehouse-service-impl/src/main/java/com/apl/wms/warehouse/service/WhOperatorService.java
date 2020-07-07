package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtil;

import com.apl.wms.warehouse.po.WhOperatorPo;
import com.apl.wms.warehouse.vo.WhOperatorListVo;
import com.apl.wms.warehouse.vo.WhOperatorInfoVo;
import com.apl.wms.warehouse.dto.WhOperatorKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 仓库操作员 service接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface WhOperatorService extends IService<WhOperatorPo> {

        /**
         * @Desc: 添加一个WhOperatorPo实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Integer> add(WhOperatorPo whOperator);


        /**
         * @Desc: 根据id 更新一个WhOperatorPo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Boolean> updById(WhOperatorPo whOperator);


        /**
         * @Desc: 根据id 查找一个WhOperatorPo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Boolean> delById(Long id);


        /**
         * @Desc: 根据id 查找一个 WhOperatorPo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<WhOperatorInfoVo> selectById(Long id);


        /**
         * @Desc: 分页查找 WhOperatorPo 列表
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Page<WhOperatorListVo>>getList(PageDto pageDto, WhOperatorKeyDto keyDto);

}
