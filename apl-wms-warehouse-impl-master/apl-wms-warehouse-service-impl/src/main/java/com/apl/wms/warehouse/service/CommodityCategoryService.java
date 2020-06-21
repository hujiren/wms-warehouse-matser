package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtils;

import com.apl.wms.warehouse.po.CommodityCategoryPo;
import com.apl.wms.warehouse.vo.CommodityCategoryInfoVo;
import com.apl.wms.warehouse.vo.CommodityCategoryListVo;
import com.apl.wms.warehouse.dto.CommodityCategoryKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 * 商品种类 服务类
 * </p>
 *
 * @author cy
 * @since 2019-12-10
 */
public interface CommodityCategoryService extends IService<CommodityCategoryPo> {

        /**
         * @Desc: 添加一个CommodityCategoryPo实体
         * @author cy
         * @since 2019-12-10
         */
        ResultUtils<Integer> add(Long parentId , String categoryName , String categoryEnName);


        /**
         * @Desc: 根据id 更新一个CommodityCategoryPo 实体
         * @author cy
         * @since 2019-12-10
         */
        ResultUtils<Boolean> updById(Long id , String categoryName , String categoryEnName);


        /**
         * @Desc: 根据id 查找一个CommodityCategoryPo 实体
         * @author cy
         * @since 2019-12-10
         */
        ResultUtils<Boolean> delById(Long id);


        /**
         * @Desc: 根据id 查找一个 CommodityCategoryPo 实体
         * @author cy
         * @since 2019-12-10
         */
        ResultUtils<CommodityCategoryInfoVo> selectById(Long id);


        /**
         * @Desc: 分页查找 CommodityCategoryPo 列表
         * @author cy
         * @since 2019-12-10
         */
        ResultUtils<Page<CommodityCategoryListVo>>getList(PageDto pageDto, CommodityCategoryKeyDto keyDto);


        /**
         * @Desc: 获取 分类字符串
         * @Author: CY
         * @Date: 2019/12/14 11:48
         */
        String getCategoryNames(Long categoryId);

        /**
         * @Desc: 获取子分类的父id 列表
         * @Author: CY
         * @Date: 2019/12/14 12:13
         */
        String getCategoryPid(Long categoryId);
}
