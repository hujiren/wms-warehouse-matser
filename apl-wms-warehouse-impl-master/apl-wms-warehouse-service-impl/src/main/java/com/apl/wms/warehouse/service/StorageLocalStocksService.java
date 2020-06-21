package com.apl.wms.warehouse.service;

import com.apl.lib.utils.ResultUtils;
import com.apl.wms.lib.bo.PlatformOutOrderStockBo;
import com.apl.wms.lib.pojo.bo.PullBatchOrderItemBo;
import com.apl.wms.lib.vo.StorageLocalStock;
import com.apl.wms.order.bo.StockUpdBo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.dto.StorageCommodityKeyDto;
import com.apl.wms.warehouse.vo.StorageLocalStocksListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库位储存商品数量 服务类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
public interface StorageLocalStocksService extends IService<StorageLocalStocksPo> {

    /**
     * @Desc: 获取商品库位库存列表
     * @Author: CY
     * @Date: 2020/6/19 16:53
     */
    List<StorageLocalStock> getCommodityStorageLocalStockList(Long whId, List<Long> commodityIdList);

    /**
     * @Desc: 添加一个StorageCommodityPo实体
     * @author CY
     * @since 2019-12-09
     */
    ResultUtils<Boolean> add(StorageLocalStocksPo storageCommodity);

    /**
     * @Desc: 根据id 查找一个StorageCommodityPo 实体
     * @author CY
     * @since 2019-12-09
     */
    ResultUtils<Boolean> deleteById(Long id);

    /**
     * @Desc: 根据id 更新一个StorageCommodityPo 实体
     * @author CY
     * @since 2019-12-09
     */
    ResultUtils<Boolean> updById(StorageLocalStocksPo storageCommodity);

    /**
     * @Desc: 根据id 查找一个 StorageLocalStocksPo 实体
     * @author CY
     * @since 2019-12-09
     */
    ResultUtils<StorageLocalStocksPo> selectById(Long id);

    /**
     * @Desc: 分页查找 StorageLocalStocksPo 列表
     * @author CY
     * @since 2019-12-09
     */
    ResultUtils<Page<StorageLocalStocksListVo>> getList(PageDto pageDto, StorageCommodityKeyDto keyDto);

    /**
     * @Desc: 根据库位id 查找库位是否存放商品
     * @Author: CY
     * @Date: 2019/12/17 15:59
     */
    Long judgeStorageLocationIsNull(Long storageId);

    /**
     * @Desc: 查找库位库存
     * @Author: CY
     * @Date: 2020/1/6 16:40
     */
    StorageLocalStocksPo getStorageLocalStocks(Long storageLocalId, Long commodityId);


    /**
     * @Desc:
     * @Author: CY
     * @Date: 2020/3/25 17:36
     */
    void updateLocalStocks(List<StockUpdBo> stocksPos);


    /**
     * @Desc: 库位库存锁定
     * @Author: CY
     * @Date: 2020/6/9 11:53
     */
    ResultUtils<Map<String, List<PullBatchOrderItemBo>>> storageLocalLock(List<PullBatchOrderItemBo> pullBatchOrderItems) throws Exception;

    /**
     * @Desc: 库位库存减扣
     * @Author: CY
     * @Date: 2020/6/11 11:41
     */
    Map<Long , Integer> storageLocalStocksReduce(List<PlatformOutOrderStockBo.PlatformOutOrderStock> platformOutOrderStocks) throws Exception;


}
