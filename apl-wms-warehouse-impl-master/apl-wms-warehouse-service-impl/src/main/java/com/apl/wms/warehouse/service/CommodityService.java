package com.apl.wms.warehouse.service;

import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.CommodityKeyDto;
import com.apl.wms.warehouse.po.CommodityPo;
import com.apl.wms.warehouse.vo.CommodityInfoVo;
import com.apl.wms.warehouse.vo.CommodityListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
public interface CommodityService extends IService<CommodityPo> {

        /**
         * @Desc: 添加一个CommodityPo实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<Long> add(CommodityPo commodity);


        /**
         * @Desc: 根据id 更新一个CommodityPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<Boolean> updById(CommodityPo commodity);


        /**
         * @Desc: 根据id 查找一个CommodityPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<Boolean> delById(Long id, Long customerId);


        /**
         * @Desc: 根据id 查找一个 CommodityPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<CommodityInfoVo> selectById(Long id, Long customerId) throws IOException;


        /**
         * @Desc: 分页查找 CommodityPo 列表
         * @author cy
         * @since 2019-12-11
         */
        ResultUtil<Page<CommodityListVo>>getList(PageDto pageDto, CommodityKeyDto keyDto)  throws Exception;


        /**
           * @Description : 打印
           * @Param ： * @param null
           * @Return ：
           * @Author : arran
           * @Date :
        */
        void print(String commodityIds, Long customerId) throws Exception ;
}
