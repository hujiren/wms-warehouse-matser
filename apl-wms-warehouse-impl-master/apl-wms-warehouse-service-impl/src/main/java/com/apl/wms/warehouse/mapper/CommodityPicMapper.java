package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.po.CommodityPicPo;
import com.apl.wms.warehouse.vo.CommodityPicListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品图片 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Repository
@Mapper
public interface CommodityPicMapper extends BaseMapper<CommodityPicPo> {


    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-12
     */
    List<CommodityPicListVo> getList(@Param("commodityId") Long commodityId);


    /**
       * @Description : 获取最大排序的图片
       * @Param ：
       * @Return ：
       * @Author : arran
       * @Date :
    */
    Integer getMaxSoft(@Param("commodityId") Long commodityId);


    //根据商品id，删除这个商品的所有子图片
    Integer delByCommodityId(@Param("commodityId") Long commodityId);

}
