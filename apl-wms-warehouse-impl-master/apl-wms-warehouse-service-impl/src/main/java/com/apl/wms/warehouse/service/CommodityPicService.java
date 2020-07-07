package com.apl.wms.warehouse.service;

import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.CommodityPicAddDto;
import com.apl.wms.warehouse.po.CommodityPicPo;
import com.apl.wms.warehouse.vo.CommodityPicListVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
public interface CommodityPicService extends IService<CommodityPicPo> {

        /**
         * @Desc: 添加一个CommodityPicPo实体
         * @author cy
         * @since 2019-12-12
         */
        ResultUtil<Integer> add(CommodityPicAddDto commodityPicAddDto);


        /**
         * @Desc: 根据id 更新一个CommodityPicPo 实体
         * @author cy
         * @since 2019-12-12
         */
        ResultUtil<Boolean> updById(CommodityPicPo commodityPic);


        /**
         * @Desc: 根据id 查找一个CommodityPicPo 实体
         * @author cy
         * @since 2019-12-12
         */
        ResultUtil<Boolean> delById(Long id);


        /**
         * @Desc: 分页查找 CommodityPicPo 列表
         * @author cy
         * @since 2019-12-12
         */
        ResultUtil<List<CommodityPicListVo>>getList(Long commodityId);

        /**
         * @Desc: 根据商品id 查找商品图片列表 进行排序
         * @Author: CY
         * @Date: 2019/12/13 15:55
         */
        List<CommodityPicListVo> getByCommodityId(Long commodityId);


}
