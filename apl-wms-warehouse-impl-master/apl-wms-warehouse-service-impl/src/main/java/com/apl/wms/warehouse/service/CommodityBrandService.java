package com.apl.wms.warehouse.service;

import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.CommodityBrandKeyDto;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.apl.wms.warehouse.vo.CommodityBrandInfoVo;
import com.apl.wms.warehouse.vo.CommodityBrandListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品品牌 服务类
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
public interface CommodityBrandService extends IService<CommodityBrandPo> {

        /**
         * @Desc: 添加一个CommodityBrandPo实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<Integer> add(String brandName , String brandNameEn);


        /**
         * @Desc: 根据id 更新一个CommodityBrandPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<Boolean> updById(Long brandId , String brandName , String brandNameEn);


        /**
         * @Desc: 根据id 查找一个CommodityBrandPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<Boolean> delById(String brandIdList);


        /**
         * @Desc: 根据id 查找一个 CommodityBrandPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<CommodityBrandInfoVo> selectById(Long id);


        /**
         * @Desc: 分页查找 CommodityBrandPo 列表
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<Page<CommodityBrandListVo>>getList(PageDto pageDto, CommodityBrandKeyDto keyDto);

}
