package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtils;

import com.apl.wms.warehouse.po.CommodityNameLibPo;
import com.apl.wms.warehouse.vo.CommodityNameLibListVo;
import com.apl.wms.warehouse.vo.CommodityNameLibInfoVo;
import com.apl.wms.warehouse.dto.CommodityNameLibKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 品名库 service接口
 * </p>
 *
 * @author apl
 * @since 2019-12-20
 */
public interface CommodityNameLibService extends IService<CommodityNameLibPo> {

        /**
         * @Desc: 添加一个CommodityNameLibPo实体
         * @author apl
         * @since 2019-12-20
         */
        ResultUtils<Integer> add(CommodityNameLibPo commodityNameLib);


        /**
         * @Desc: 根据id 更新一个CommodityNameLibPo 实体
         * @author apl
         * @since 2019-12-20
         */
        ResultUtils<Boolean> updById(CommodityNameLibPo commodityNameLib);


        /**
         * @Desc: 根据id 查找一个CommodityNameLibPo 实体
         * @author apl
         * @since 2019-12-20
         */
        ResultUtils<Boolean> delById(String id);


        /**
         * @Desc: 根据id 查找一个 CommodityNameLibPo 实体
         * @author apl
         * @since 2019-12-20
         */
        ResultUtils<CommodityNameLibInfoVo> selectById(Integer id);


        /**
         * @Desc: 分页查找 CommodityNameLibPo 列表
         * @author apl
         * @since 2019-12-20
         */
        ResultUtils<Page<CommodityNameLibListVo>>getList(PageDto pageDto, CommodityNameLibKeyDto keyDto)  throws Exception;

        /**
         * @Desc: 根据id 更新一个CommodityNameLibPo 实体
         * @author apl
         * @since 2019-12-20
         */
        ResultUtils<Boolean> updCategory(String id, Integer categoryId);

}
