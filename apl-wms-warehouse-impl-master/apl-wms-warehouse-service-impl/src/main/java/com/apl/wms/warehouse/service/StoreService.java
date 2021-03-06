package com.apl.wms.warehouse.service;
import com.apl.lib.utils.ResultUtil;

import com.apl.wms.warehouse.po.StoreApiPo;
import com.apl.wms.warehouse.po.StorePo;
import com.apl.wms.warehouse.vo.StoreListVo;
import com.apl.wms.warehouse.vo.StoreInfoVo;
import com.apl.wms.warehouse.dto.StoreKeyDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 网店铺 service接口
 * </p>
 *
 * @author arran
 * @since 2019-12-21
 */
public interface StoreService extends IService<StorePo> {

        /**
         * @Desc: 添加一个StorePo实体
         * @author arran
         * @since 2019-12-21
         */
        ResultUtil<Integer> add(StorePo store);


        /**
         * @Desc: 根据id 更新一个StorePo 实体
         * @author arran
         * @since 2019-12-21
         */
        ResultUtil<Boolean> updById(StorePo store);


        /**
         * @Description  保存参数
         * @Param
         * @Return
         * @Author  arran
         * @Date  2019/12/30
         */
        ResultUtil<Boolean> updConfig(StoreApiPo storeApiPo);


        /**
         * @Desc: 根据id 查找一个StorePo 实体
         * @author arran
         * @since 2019-12-21
         */
        ResultUtil<Boolean> delById(Long id);


        /**
         * @Desc: 根据id 查找一个 StorePo 实体
         * @author arran
         * @since 2019-12-21
         */
        ResultUtil<StoreInfoVo> selectById(Long id);


        ResultUtil<StoreApiPo> getApiConfig(Long id, Long customerId);


        ResultUtil<String> getApiConfigStrVal(Long id, Long customerId);


        /**
         * @Desc: 分页查找 StorePo 列表
         * @author arran
         * @since 2019-12-21
         */
        ResultUtil<Page<StoreListVo>>getList(PageDto pageDto, StoreKeyDto keyDto);



}
