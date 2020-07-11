package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.join.JoinUtil;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.bo.StockUpdBo;
import com.apl.wms.warehouse.lib.constants.WmsWarehouseAplConstants;
import com.apl.wms.warehouse.lib.pojo.bo.CompareStorageLocalStocksBo;
import com.apl.wms.warehouse.lib.pojo.bo.PlatformOutOrderStockBo;
import com.apl.wms.warehouse.lib.pojo.bo.PullBatchOrderItemBo;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalStock;
import com.apl.wms.warehouse.dao.StorageLocalStocksMapper;
import com.apl.wms.warehouse.dto.StorageCommodityKeyDto;
import com.apl.wms.warehouse.po.CommodityPo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import com.apl.wms.warehouse.po.StorageLocalStocksPo;
import com.apl.wms.warehouse.service.CommodityService;
import com.apl.wms.warehouse.service.StorageLocalService;
import com.apl.wms.warehouse.service.StorageLocalStocksService;
import com.apl.wms.warehouse.vo.StorageLocalStocksListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;


/**
 * <p>
 * 库位储存商品数量 服务实现类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Service
@Slf4j
public class StorageLocalStocksServiceImpl extends ServiceImpl<StorageLocalStocksMapper, StorageLocalStocksPo> implements StorageLocalStocksService {


    //状态code枚举
    enum StorageLocalStocksServiceCode {


        STOCK_IS_NOT_ENOUGH("STOCK_IS_NOT_ENOUGH", "库存不足"),
        STOCK_UPDATE_FAIL("STOCK_UPDATE_FAIL", "库位更新失败"),
        STORAGE_STATUS_UPDATE_FAIL("STORAGE_STATUS_UPDATE_FAIL", "库位状态更新失败");

        private String code;
        private String msg;


        StorageLocalStocksServiceCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }


    @Autowired
    StorageLocalService storageLocalService;

    @Autowired
    CommodityService commodityService;


    @Override
    public List<StorageLocalStock> getCommodityStorageLocalStockList(Long whId, List<Long> commodityIdList) {

        return baseMapper.getCommodityStorageLocalStockList(whId , commodityIdList);
    }

    @Override
    public ResultUtil<Boolean> add(StorageLocalStocksPo storageCommodity) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, save(storageCommodity));
    }

    @Override
    public ResultUtil<Boolean> deleteById(Long id) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, removeById(id));
    }

    @Override
    public ResultUtil<Boolean> updById(StorageLocalStocksPo storageCommodity) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, updateById(storageCommodity));
    }

    @Override
    public ResultUtil<StorageLocalStocksPo> selectById(Long id) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, getById(id));
    }


    @Override
    public ResultUtil<Page<StorageLocalStocksListVo>> getList(PageDto pageDto, StorageCommodityKeyDto keyDto) {

        Page<StorageLocalStocksListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<StorageLocalStocksListVo> list = baseMapper.getList(page, keyDto);
        page.setRecords(list);
        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }

    @Override
    public Long judgeStorageLocationIsNull(Long storageId) {

        return baseMapper.judgeStorageLocationIsNull(storageId);
    }

    @Override
    public StorageLocalStocksPo getStorageLocalStocks(Long storageLocalId, Long commodityId) {

        return baseMapper.getStorageLocalStocks(storageLocalId, commodityId);
    }

    @Override
    public void updateLocalStocks(List<StockUpdBo> stocksPos) {

        Boolean result = true;

        //剩余库存
        Integer residue = 0;

        for (StockUpdBo stockUpdBo : stocksPos) {
            StorageLocalStocksPo storageLocalStocksPo = getStorageLocalStocks(stockUpdBo.getStorageLocalId(), stockUpdBo.getCommodityId());

            //不存在，第一次添加
            if (storageLocalStocksPo == null) {
                storageLocalStocksPo = new StorageLocalStocksPo();
                storageLocalStocksPo.setStorageLocalId(stockUpdBo.getStorageLocalId());
                storageLocalStocksPo.setCommodityId(stockUpdBo.getCommodityId());
                storageLocalStocksPo.setAvailableCount(stockUpdBo.getQty());
                result = result && this.save(storageLocalStocksPo);
            } else {
                //存在 ，进行更新 ， 计算剩余库存
                residue = storageLocalStocksPo.getAvailableCount() + stockUpdBo.getQty();

                //库存不足
                if (residue < 0) {
                    throw new AplException(StorageLocalStocksServiceCode.STOCK_IS_NOT_ENOUGH.code, StorageLocalStocksServiceCode.STOCK_IS_NOT_ENOUGH.msg);
                } else {
                    storageLocalStocksPo.setAvailableCount(residue);
                    result = result && this.updateById(storageLocalStocksPo);

                }
            }

            if (!result) {
                throw new AplException(StorageLocalStocksServiceCode.STOCK_UPDATE_FAIL.code, StorageLocalStocksServiceCode.STOCK_UPDATE_FAIL.msg);
            }

            //获取商品信息 ，计算体积
            CommodityPo commodityPo = commodityService.getById(stockUpdBo.getCommodityId());

            //获取库位信息 ， 更新库位状态
            StorageLocalPo storageLocalPo = storageLocalService.getById(stockUpdBo.getStorageLocalId());

            //商品体积
            BigDecimal commodityVolume = commodityPo.getSizeWidth().multiply(commodityPo.getSizeWidth()).multiply(commodityPo.getSizeHeight());

            BigDecimal storeVolume = commodityVolume.multiply(new BigDecimal(residue));

            Integer storageLocalAllVolume = storageLocalPo.getVolume().multiply(new BigDecimal(8.0)).intValue();

            //判断库位状态是否已满
            if (storageLocalAllVolume - storeVolume.intValue() > 0) {
                //未满状态
                storageLocalPo.setStorageStatus(WmsWarehouseAplConstants.STORAGE_UN_FULL_STATUS);
            } else {//已满
                storageLocalPo.setStorageStatus(WmsWarehouseAplConstants.STORAGE_FULL_STATUS);
            }

            //更新库位状态
            storageLocalService.updateById(storageLocalPo);
        }

    }


    @Override
    public ResultUtil<Map<String, List<PullBatchOrderItemBo>>> storageLocalLock(List<PullBatchOrderItemBo> pullBatchOrderItems) throws Exception {

        //根据商品id 分组 商品id -- 》 订单列表
        Map<String, List<PullBatchOrderItemBo>> commodityOrders = JoinUtil.listGrouping(pullBatchOrderItems, "commodityId");

        for (Map.Entry<String, List<PullBatchOrderItemBo>> entry : commodityOrders.entrySet()) {
            //查找商品对应的 库存列表
            List<StorageLocalStocksPo> storageLocalStocks = baseMapper.getStorageLocalByCommodityId(Long.parseLong(entry.getKey()));
            Integer commodityStockSize = storageLocalStocks.size();
            Integer index = 0;

            // 商品对应的订单列表
            List<PullBatchOrderItemBo> orderItemBos = entry.getValue();

                //循环商品对应的订单列表
                for (PullBatchOrderItemBo orderItemBo : orderItemBos) {
                    //商品对应的订单对应的库位列表
                    List<PullBatchOrderItemBo.StorageCount> storageCounts = new ArrayList<>();
                    orderItemBo.setStorageCounts(storageCounts);
                    //循环遍历商品对应的库存列表，比对订单是否足够锁定，不够锁定，使用多个库位，一个商品可能对应多个库位
                    for ( ; index < commodityStockSize ; index ++) {
                        StorageLocalStocksPo changeStocksInfo = storageLocalStocks.get(index);
                        //遍历 商品 库存

                        PullBatchOrderItemBo.StorageCount storageCount = new PullBatchOrderItemBo.StorageCount();
                        storageCounts.add(storageCount);

                        storageCount.setStorageId(changeStocksInfo.getStorageLocalId());

                        //比对商品库存 与 订单数量
                        Integer residueCount = changeStocksInfo.getAvailableCount() - orderItemBo.getOrderQty();
                        //商品库存足够锁定对应的订单商品
                        if(residueCount >= 0){
                            storageCount.setCount(orderItemBo.getOrderQty());
                            changeStocksInfo.setAvailableCount(residueCount);
                            changeStocksInfo.setFreezeCount(changeStocksInfo.getFreezeCount() + orderItemBo.getOrderQty());
                            baseMapper.updateById(changeStocksInfo);
                            break ;
                        }else{
                            //商品库存不能完全锁定，需要继续循环遍历第二个商品库存
                            storageCount.setCount(changeStocksInfo.getAvailableCount());
                            changeStocksInfo.setAvailableCount(0);
                            orderItemBo.setOrderQty(orderItemBo.getOrderQty() - changeStocksInfo.getAvailableCount());
                            changeStocksInfo.setFreezeCount(changeStocksInfo.getFreezeCount() + changeStocksInfo.getAvailableCount());
                            baseMapper.updateById(changeStocksInfo);

                        }



                    }

            }

        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , commodityOrders);
    }

    @Override
    public Map<Long , Integer> storageLocalStocksReduce(List<PlatformOutOrderStockBo.PlatformOutOrderStock> platformOutOrderStocks) throws Exception {

        Map<Long , Integer> commodityCount = new HashMap<>();

        Map<String, List<PlatformOutOrderStockBo.PlatformOutOrderStock>> commodityStock = JoinUtil.listGrouping(platformOutOrderStocks, "commodityId");

        for (Map.Entry<String, List<PlatformOutOrderStockBo.PlatformOutOrderStock>> commodityStockEntry : commodityStock.entrySet()) {

            Integer totalCount = 0;

            for (PlatformOutOrderStockBo.PlatformOutOrderStock platformOutOrderStock : commodityStockEntry.getValue()) {
                StorageLocalStocksPo storageLocalStocks = getStorageLocalStocks(platformOutOrderStock.getStorageLocalId(), platformOutOrderStock.getCommodityId());
                storageLocalStocks.setFreezeCount(storageLocalStocks.getFreezeCount() - platformOutOrderStock.getChangeCount());
                storageLocalStocks.setRealityCount(storageLocalStocks.getRealityCount() - platformOutOrderStock.getChangeCount());
                updateById(storageLocalStocks);
                totalCount = totalCount + platformOutOrderStock.getChangeCount();
            }

            commodityCount.put(Long.parseLong(commodityStockEntry.getKey()) , totalCount);
        }


        return commodityCount;

    }


    public static void main(String[] args) throws Exception {

        List<TempBo> tempBos = new ArrayList<>();
        TempBo tempBo1 = new TempBo();
        tempBo1.setCommodityId(2);
        tempBo1.setCount(5);

        TempBo tempBo2 = new TempBo();
        tempBo2.setCommodityId(1);
        tempBo2.setCount(5);

        TempBo tempBo3 = new TempBo();
        tempBo3.setCommodityId(1);
        tempBo3.setCount(5);

        TempBo tempBo4 = new TempBo();
        tempBo4.setCommodityId(3);
        tempBo4.setCount(5);

        tempBos.add(tempBo1);
        tempBos.add(tempBo2);
        tempBos.add(tempBo3);
        tempBos.add(tempBo4);

        LinkedHashMap<String, List<TempBo>> commodityId = JoinUtil.listGrouping(tempBos, "commodityId");

        Map<Integer , Integer> totalCount = new HashMap<>();
        for (Map.Entry<String, List<TempBo>> countEntry : commodityId.entrySet()) {

            Integer total = 0;
            for (TempBo tempBo : countEntry.getValue()) {
                total = total + tempBo.getCount();

            }
            System.out.println("total hash is : " + total.hashCode());
            totalCount.put(Integer.parseInt(countEntry.getKey()) , total);
        }
        System.out.println(totalCount);

    }


@Data
static
public class TempBo{

        private Integer commodityId;

        private Integer count;

}

    /**
     * 更新库位库存
     * @param compareStorageLocalStocksBos
     * @return
     */
    @Override
    public Integer updateStorageLocalStock(List<CompareStorageLocalStocksBo> compareStorageLocalStocksBos) {
        Integer integer = baseMapper.updateStorageLocalStock(compareStorageLocalStocksBos);
        return integer;
    }
}
