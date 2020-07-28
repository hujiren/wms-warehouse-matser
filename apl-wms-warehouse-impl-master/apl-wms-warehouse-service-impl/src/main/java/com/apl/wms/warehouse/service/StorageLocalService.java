package com.apl.wms.warehouse.service;

import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.StorageLocationAddDto;
import com.apl.wms.warehouse.dto.StorageLocationBatchUpdDto;
import com.apl.wms.warehouse.dto.StorageLocationKeyDto;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalInfoVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import com.apl.wms.warehouse.vo.StorageLocalListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库位 service接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface StorageLocalService extends IService<StorageLocalPo> {

        /**
         * @Desc: 添加一个StorageLocationPo实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Map> add(StorageLocationAddDto storageLocationAddDto);


        /**
         * @Desc: 根据id 更新一个StorageLocationPo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Boolean> updById(StorageLocalPo storageLocation);


        /**
         * @Desc: 根据id 查找一个StorageLocationPo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Boolean> delById(Long id);


        /**
         * @Desc: 根据id 查找一个 StorageLocalPo 实体
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<StorageLocalInfoVo> getStorageLocalBySn(String storageLocalSn);


        /**
         * @Desc: 分页查找 StorageLocalPo 列表
         * @author cy
         * @since 2019-12-17
         */
        ResultUtil<Page<StorageLocalListVo>>getList(PageDto pageDto, StorageLocationKeyDto keyDto);


        /**
         * @Desc: 批量修改 库位
         * @Author: CY
         * @Date: 2019/12/17 17:21
         */
        ResultUtil<Boolean> batchUpdate(StorageLocationBatchUpdDto storageLocationBatchUpdDto);


        /**
         * @Description : 打印
         * @Param ： * @param null
         * @Return ：
         * @Author : arran
         * @Date :
         */
        void print(String id)  throws Exception;

        /**
         * @Desc: 分配库位
         * @Author: CY
         * @Date: 2020/1/3 10:04
         */
        ResultUtil<List<StorageLocalInfoVo>> allocationStorageLocal(Long commodityId , Integer count , String storageLocal)  throws Exception;

        /**
         * @Description : 库位锁定
         * @Param ：
         * @Return ：
         * Created by arran on 2020/3/12
         */
        ResultUtil<Boolean> changeStorageLocalStatus(String lockIds , String unLockIds);

        /**
         * @Desc: 分配当个库位
         * @Author: CY
         * @Date: 2020/3/24 9:31
         */
        ResultUtil<StorageLocalInfoVo> allocationOneStorageLocal(Long commodityId , String storageLocalSn);


}
