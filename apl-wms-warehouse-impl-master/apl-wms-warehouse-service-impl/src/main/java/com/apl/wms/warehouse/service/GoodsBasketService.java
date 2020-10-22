package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtil;

import com.apl.wms.warehouse.po.GoodsBasketPo;
import com.apl.wms.warehouse.vo.GoodsBasketListVo;
import com.apl.wms.warehouse.vo.GoodsBasketInfoVo;
import com.apl.wms.warehouse.dto.GoodsBasketKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 * 货蓝 service接口
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
public interface GoodsBasketService extends IService<GoodsBasketPo> {

        /**
         * @Desc: 添加一个GoodsBasketPo实体
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<Integer> add(GoodsBasketPo goodsBasket);


        /**
         * @Desc: 根据id 更新一个GoodsBasketPo 实体
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<Boolean> updById(GoodsBasketPo goodsBasket);


        /**
         * @Desc: 根据id 查找一个GoodsBasketPo 实体
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<Boolean> delById(Long id);


        /**
         * @Desc: 根据id 查找一个 GoodsBasketPo 实体
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<GoodsBasketInfoVo> selectById(Long id);


        /**
         * @Desc: 分页查找 GoodsBasketPo 列表
         * @author cy
         * @since 2019-12-21
         */
        ResultUtil<Page<GoodsBasketInfoVo>> getList(PageDto pageDto, GoodsBasketKeyDto keyDto);

}
