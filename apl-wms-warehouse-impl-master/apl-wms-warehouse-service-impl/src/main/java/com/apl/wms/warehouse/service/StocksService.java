package com.apl.wms.warehouse.service;

import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.bo.StockUpdBo;
import com.apl.wms.warehouse.lib.pojo.bo.PlatformOutOrderStockBo;
import com.apl.wms.warehouse.lib.pojo.vo.CheckOrderStockDetailsVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.vo.StocksListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 库存 服务类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
public interface StocksService extends IService<StocksPo> {


        /**
         * @Desc: 获取仓库库存信息
         * @Author: CY
         * @Date: 2020/6/19 9:38
         */
        ResultUtil<List<CheckOrderStockDetailsVo>> getCommodityStockMsg(Long whId , String commodityIds) throws Exception;

        /**
         * @Desc: 添加一个StocksPo实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> add(StocksPo stocks);

        /**
         * @Desc: 根据id 查找一个StocksPo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> deleteById(Long id);

        /**
         * @Desc: 根据id 更新一个StocksPo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Boolean> updById(StocksPo stocks);

        /**
         * @Desc: 根据id 查找一个 StocksPo 实体
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<StocksPo> selectById(Long id);

        /**
         * @Desc: 获取库存列表 ， 准备进行中转，运输到其他仓库
         * @author CY
         * @since 2019-12-09
         */
        ResultUtil<Page<StocksListVo>>listStocks(Long whId , Long customerId , Integer isCorrespondence , String keyword , PageDto pageDto);

        /**
         * @Desc: 更新库存
         * @Author: CY
         * @Date: 2020/1/6 16:28
         */
        void updateStocks(List<StockUpdBo> stocksPos);
        /**
         * @Desc: 更新库存
         * @Author: CY
         * @Date: 2020/1/6 16:28
         */
        void outStorageOrderStockLock(PlatformOutOrderStockBo platformOutOrderStockBo) throws Exception;

        /**
         * @Desc: 查找 仓库 对应商品的库存
         * @Author: CY
         * @Date: 2020/3/18 17:21
         */
        StocksPo getStockByWhIdAndCommodity(Long whId, Long commodityId);

        /**
         * @Desc: 检查库存是否够用
         * @Author: CY
         * @Date: 2020/6/8 11:43
         */
        Boolean checkStockCount(PlatformOutOrderStockBo platformOutOrderStockBo);

        /**
         * @Desc: 批次提交 库存减扣
         * @Author: CY
         * @Date: 2020/6/11 11:24
         */
        void pullBatchSubmitStockReduce(PlatformOutOrderStockBo platformOutOrderStockBo) throws Exception;


        //更新总库存
         Integer updateTotalStock(List<StocksPo> newStocksPos);


}
