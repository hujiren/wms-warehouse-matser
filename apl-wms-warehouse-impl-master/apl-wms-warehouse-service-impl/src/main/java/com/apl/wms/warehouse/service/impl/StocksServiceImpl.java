package com.apl.wms.warehouse.service.impl;

import com.apl.cache.AplCacheUtil;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.join.JoinUtil;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.RedisLock;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.StringUtil;
import com.apl.wms.outstorage.order.lib.feign.OutstorageOrderBusinessFeign;
import com.apl.wms.warehouse.bo.StockUpdBo;
import com.apl.wms.warehouse.lib.pojo.bo.PlatformOutOrderStockBo;
import com.apl.wms.warehouse.lib.pojo.vo.CheckOrderStockDetailsVo;
import com.apl.wms.warehouse.lib.pojo.vo.StocksVo;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalStock;
import com.apl.wms.warehouse.mapper.StocksMapper;
import com.apl.wms.warehouse.service.StocksService;
import com.apl.wms.warehouse.service.StorageLocalStocksService;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.vo.StocksListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 库存 服务实现类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Service
@Slf4j
public class StocksServiceImpl extends ServiceImpl<StocksMapper, StocksPo> implements StocksService {


    //状态code枚举
    enum StocksServiceCode {


        STOCK_IS_NOT_ENOUGH("STOCK_IS_NOT_ENOUGH", "库存不足"),
        STOCK_UPDATE_FAIL("STOCK_UPDATE_FAIL", "库存更新失败");

        private String code;
        private String msg;




        StocksServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }

    @Autowired
    StorageLocalStocksService storageLocalStocksService;


    @Autowired
    CommodityServiceImpl commodityService;

    @Autowired
    AplCacheUtil redisTemplate;

    @Autowired
    OutstorageOrderBusinessFeign outstorageOrderBusinessFeign;

    @Override
    public ResultUtil<List<CheckOrderStockDetailsVo>> getCommodityStockMsg(Long whId , String commodityIds) throws Exception {

        List<Long> commodityIdList = StringUtil.stringToLongList(commodityIds);
        //获取仓库下商品总库存
        List<CheckOrderStockDetailsVo> checkOrderStockDetailsVos = baseMapper.getWareHouseCommodityCountList(whId , commodityIdList);

        List<StorageLocalStock> storageLocalStocks =  storageLocalStocksService.getCommodityStorageLocalStockList(whId , commodityIdList);

        //获取商品列表 对应的库位库存列表
        Map<String, List<StorageLocalStock >> commodityStorageLocalStocks = JoinUtil.listGrouping(storageLocalStocks, "commodityId");

        for (CheckOrderStockDetailsVo checkOrderStockDetailsVo : checkOrderStockDetailsVos) {
            //组装商品对应的库位列表
            List<StorageLocalStock > commodityStorageLocalStockList = commodityStorageLocalStocks.get(checkOrderStockDetailsVo.getCommodityId().toString());
            checkOrderStockDetailsVo.setStorageLocalStocks(commodityStorageLocalStockList);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , checkOrderStockDetailsVos);
    }

    @Override
    public ResultUtil<Boolean> add(StocksPo stocks) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, save(stocks));
    }


    @Override
    public ResultUtil<Boolean> deleteById(Long id) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, removeById(id));
    }

    @Override
    public ResultUtil<Boolean> updById(StocksPo stocks) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, updateById(stocks));
    }

    @Override
    public ResultUtil<StocksPo> selectById(Long id) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, getById(id));
    }


    @Override
    public ResultUtil<Page<StocksListVo>> listStocks(Long whId, Long customerId, Integer isCorrespondence, String keyword, PageDto pageDto) {

        Page<StocksListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());


        List<StocksListVo> result = baseMapper.listStocks(page, whId, customerId, isCorrespondence, keyword);
        page.setRecords(result);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }

    @Override
    @Transactional
    public void updateStocks(List<StockUpdBo> stocksPos) {

        //更新总库存

        this.incrStock(stocksPos);

        //更新库位库存
        storageLocalStocksService.updateLocalStocks(stocksPos);

    }

    @Override
    @Transactional
    public void outStorageOrderStockLock(PlatformOutOrderStockBo platformOutOrderStockBo){

        List<StocksPo> stocksUpdList = new ArrayList<>();
        Long whId = platformOutOrderStockBo.getWhId();
        try {
                //getCommodityStock
            for (PlatformOutOrderStockBo.PlatformOutOrderStock platformOutOrderStock : platformOutOrderStockBo.getPlatformOutOrderStocks()) {
                StocksPo commodityStock = baseMapper.getCommodityStock(whId, platformOutOrderStock.getCommodityId());
                commodityStock.setRealityCount(commodityStock.getRealityCount() + platformOutOrderStock.getChangeCount());
                commodityStock.setAvailableCount(commodityStock.getAvailableCount() - platformOutOrderStock.getChangeCount());

                stocksUpdList.add(commodityStock);
            }

            //批量更新库存信息
            updateBatchById(stocksUpdList);
        }catch (Exception e){

            throw e;
        }
        finally {
            String lockKey = "lock-stocks-" + platformOutOrderStockBo.getCustomerId();
            RedisLock.unlock(redisTemplate , lockKey);
        }


    }


    public Boolean checkStockCount(PlatformOutOrderStockBo platformOutOrderStockBo) {

        Long whId = platformOutOrderStockBo.getWhId();

        for (PlatformOutOrderStockBo.PlatformOutOrderStock platformOutOrderStock : platformOutOrderStockBo.getPlatformOutOrderStocks()) {
            StocksPo commodityStock = getStockByWhIdAndCommodity(whId , platformOutOrderStock.getCommodityId());
            if(commodityStock != null){
                Integer availableCount = commodityStock.getAvailableCount() - platformOutOrderStock.getChangeCount();
                if(availableCount < 0){
                    //库存不足异常
                    return false;
                }
                Integer freezeCount = commodityStock.getRealityCount() + platformOutOrderStock.getChangeCount();
                commodityStock.setAvailableCount(availableCount);
                commodityStock.setRealityCount(freezeCount);
            }else{
                //库存不存在异常
                return false;
            }

        }
        return true;
    }

    @Override
    @Transactional
    public void pullBatchSubmitStockReduce(PlatformOutOrderStockBo platformOutOrderStockBo) throws Exception {

        //库位库存减扣
        Map<Long, Integer> commodityCount = storageLocalStocksService.storageLocalStocksReduce(platformOutOrderStockBo.getPlatformOutOrderStocks());

        //仓库库存 减扣
        whStocksReduce(platformOutOrderStockBo.getWhId() , commodityCount);

    }

    private void incrStock(List<StockUpdBo> stocksPos) {

        //更新结果
        Boolean result = true;

        for (StockUpdBo stockUpdBo : stocksPos) {
            //更新库存
            StocksPo stocksPo = this.getStockByWhIdAndCommodity(stockUpdBo.getWhId(), stockUpdBo.getCommodityId());

            //库存不存在 ， 插入一条记录
            if (stocksPo == null) {
                result = result && baseMapper.addStock(stockUpdBo);
            } else {
                Integer residue = stocksPo.getAvailableCount() + stockUpdBo.getQty();
                if (residue < 0) {
                    //库存不合法
                    throw new AplException(StocksServiceCode.STOCK_IS_NOT_ENOUGH.code , StocksServiceCode.STOCK_IS_NOT_ENOUGH.msg);
                } else {
                    stocksPo.setAvailableCount(residue);
                    result = result && this.updateById(stocksPo);
                }

            }
            //如果更新失败 ，抛出异常 ，库存增加失败
            if(!result){
                throw new AplException(StocksServiceCode.STOCK_UPDATE_FAIL.code , StocksServiceCode.STOCK_UPDATE_FAIL.msg);
            }

        }
    }


    @Override
    public StocksPo getStockByWhIdAndCommodity(Long whId, Long commodityId) {

        return baseMapper.getCommodityStock(whId, commodityId);
    }


    /**
     * @Desc: 仓库库存减扣
     * @Author: CY
     * @Date: 2020/6/11 12:02
     */
    private void whStocksReduce(Long whId , Map<Long, Integer> commodityCount) {

        for (Map.Entry<Long, Integer> commodityCountEntry : commodityCount.entrySet()) {

            StocksPo stocksPo = getStockByWhIdAndCommodity(whId, commodityCountEntry.getKey());
            stocksPo.setRealityCount(stocksPo.getRealityCount() - commodityCountEntry.getValue());
            stocksPo.setAvailableCount(stocksPo.getAvailableCount() - commodityCountEntry.getValue());

            updateById(stocksPo);

        }

    }


    /**
     * 更新总库存
     * @param newStocksPos
     * @return
     */
    @Override
    public Integer updateTotalStock(List<StocksPo> newStocksPos) {
        Integer integer = baseMapper.updateTotalStock(newStocksPos);
        return  integer;
    }


    @Override
    public ResultUtil<List<StocksVo>> getStocksByCommodityId(List<Long> commodityIdList) {

        List<StocksVo> stocksRealityCountByCommodityId = baseMapper.getStocksByCommodityId(commodityIdList);
        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, stocksRealityCountByCommodityId);
    }

}
