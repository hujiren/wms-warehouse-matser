package com.apl.wms.warehouse.service;

import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.OperatorServiceKeyDto;
import com.apl.wms.warehouse.po.OperatorServicePo;
import com.apl.wms.warehouse.vo.OperatorServiceInfoVo;
import com.apl.wms.warehouse.vo.OperatorServiceListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 附加服务操作名称 service接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface OperatorServiceService extends IService<OperatorServicePo> {

        /**
         * @Desc: 添加一个OperatorServicePo实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Integer> add(OperatorServicePo operatorService);


        /**
         * @Desc: 根据id 更新一个OperatorServicePo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Boolean> updById(OperatorServicePo operatorService);


        /**
         * @Desc: 根据id 查找一个OperatorServicePo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Boolean> delById(Long id);


        /**
         * @Desc: 根据id 查找一个 OperatorServicePo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<OperatorServiceInfoVo> selectById(Long id);


        /**
         * @Desc: 分页查找 OperatorServicePo 列表
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Page<OperatorServiceListVo>>getList(PageDto pageDto, OperatorServiceKeyDto keyDto);

}
