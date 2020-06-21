package com.apl.wms.warehouse.service;

import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.lib.bo.PackagingMaterialsCountBo;
import com.apl.wms.warehouse.dto.PackagingMaterialsKeyDto;
import com.apl.wms.warehouse.po.PackagingMaterialsPo;
import com.apl.wms.warehouse.vo.CommodityInfoVo;
import com.apl.wms.warehouse.vo.PackagingMaterialsListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 包装材料 service接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface PackagingMaterialsService extends IService<PackagingMaterialsPo> {

        /**
         * @Desc: 获取商品的包装材料信息，根据传入的商品数量，返回对应数量的包装材料
         * @Author: CY
         * @Date: 2020/6/13 10:06
         */
        ResultUtils<Map<String , List<PackagingMaterialsCountBo>>> getCommodityPackMaterials(Long orderId) throws Exception;

        /**
         * @Desc: 添加一个CommodityPo实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtils<Long> add(PackagingMaterialsPo packagingMaterialsPo);


        /**
         * @Desc: 根据id 更新一个CommodityPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtils<Boolean> updById(PackagingMaterialsPo packagingMaterialsPo);


        /**
         * @Desc: 根据id 查找一个CommodityPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtils<Boolean> delById(Long id);


        /**
         * @Desc: 根据id 查找一个 CommodityPo 实体
         * @author cy
         * @since 2019-12-11
         */
        ResultUtils<CommodityInfoVo> selectById(Long id);


        /**
         * @Desc: 分页查找 CommodityPo 列表
         * @author cy
         * @since 2019-12-11
         */
        ResultUtils<Page<PackagingMaterialsListVo>>getList(PageDto pageDto, PackagingMaterialsKeyDto packagingMaterialsKeyDto)  throws Exception;


        /**
         * @Description : 打印
         * @Param ： * @param null
         * @Return ：
         * @Author : arran
         * @Date :
         */
        void print(String commodityIds)  throws Exception;


}
