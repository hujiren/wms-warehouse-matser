package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.utils.StringUtil;
import com.apl.wms.lib.constants.AplConstants;
import com.apl.wms.lib.pojo.bo.PullBatchOrderItemBo;
import com.apl.wms.lib.vo.StorageLocalInfoVo;
import com.apl.wms.lib.bo.StorageLocationBo;
import com.apl.wms.warehouse.mapper.StorageLocalMapper;
import com.apl.wms.warehouse.dto.StorageLocationAddDto;
import com.apl.wms.warehouse.dto.StorageLocationBatchUpdDto;
import com.apl.wms.warehouse.dto.StorageLocationKeyDto;
import com.apl.wms.warehouse.po.CommodityPo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import com.apl.wms.warehouse.service.CommodityService;
import com.apl.wms.warehouse.service.StorageLocalService;
import com.apl.wms.warehouse.service.StorageLocalStocksService;
import com.apl.wms.warehouse.utils.JasperHelper;
import com.apl.wms.warehouse.vo.StorageLocalListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;


/**
 * <p>
 * 库位 service实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Service
@Slf4j
public class StorageLocalServiceImpl extends ServiceImpl<StorageLocalMapper, StorageLocalPo> implements StorageLocalService {

    //状态code枚举
    enum StorageLocationServiceCode {

        STORAGE_EXIST_COMMODITY("STORAGE_EXIST_COMMODITY", "库位存在商品，不能进行删除"),

        LAST_THREE_MUST_NUMBER("LAST_THREE_MUST_NUMBER", "编号后3位必须是纯数字"),
        COMMODITY_IS_NOT_EXIST("COMMODITY_IS_NOT_EXIST", "商品不存在"),
        STORAGE_LOCAL_IS_NOT_EXIST("STORAGE_LOCAL_IS_NOT_EXIST", "库位不存在"),

        LOCK_STORAGE_LOCAL_FAIL("LOCK_STORAGE_LOCAL_FAIL", "库位锁定失败");

        private String code;
        private String msg;

        StorageLocationServiceCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    @Autowired
    StorageLocalStocksService storageCommodityService;

    @Value("${apl.report.storageLocationReportPath}")
    private String storageLocationReportPath;

    @Autowired
    CommodityService commodityService;

    @Override
    public ResultUtils<Map> add(StorageLocationAddDto storageLocationAddDto) {

        String firstSn = storageLocationAddDto.getFirstSn();//起始编号
        String suffix = firstSn.substring(firstSn.length() - 3);//编号后缀
        Integer num = 0;
        try {
            num = Integer.parseInt(suffix);
        } catch (Exception e) {
        }
        if (num < 1) {
            throw new AplException(StorageLocationServiceCode.LAST_THREE_MUST_NUMBER.code, StorageLocationServiceCode.LAST_THREE_MUST_NUMBER.msg);
        }
        String prefix = firstSn.substring(0, firstSn.length() - 3); //编号前缀
        Integer count = storageLocationAddDto.getCount();//数量

        Integer addCount = 0, failCount = 0;
        //生成 预 插入对象列表
        for (Integer i = 0; i < count; i++) {
            String storageSn = prefix + String.format("%03d", num);
            Boolean result = this.exists(0l, storageSn);
            //库位不存在，进行生成库位
            if (!result) {
                //自增 生成code
                StorageLocalPo storageLocationPo = new StorageLocalPo();
                storageLocationPo.setStorageLayer(storageLocationAddDto.getStorageLayer());
                storageLocationPo.setSizeLength(storageLocationAddDto.getSizeLength());
                storageLocationPo.setSizeWidth(storageLocationAddDto.getSizeWidth());
                storageLocationPo.setSizeHeight(storageLocationAddDto.getSizeHeight());
                storageLocationPo.setRemark(storageLocationAddDto.getRemark());
                storageLocationPo.setShelvesId(storageLocationAddDto.getShelvesId());
                storageLocationPo.setStorageSn(storageSn);
                storageLocationPo.setStorageStatus(1);
                //体积
                storageLocationPo.setVolume(storageLocationAddDto.getSizeLength().multiply(storageLocationAddDto.getSizeWidth()).multiply(storageLocationAddDto.getSizeHeight()));
                storageLocationPo.setSupportWeight(storageLocationAddDto.getSupportWeight());
                save(storageLocationPo);

                addCount++;
            } else {
                failCount++;
            }
            num++;
        }

        Map resultMapVo = new HashMap<>();
        resultMapVo.put("addCount", addCount);
        resultMapVo.put("failCount", failCount);

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS, resultMapVo);

    }


    @Override
    public ResultUtils<Boolean> updById(StorageLocalPo storageLocation) {

        Boolean result = this.exists(storageLocation.getId(), storageLocation.getStorageSn());
        if (!result) {
            Integer flag = baseMapper.updateById(storageLocation);
            if (flag > 0) {
                return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS, true);
            }
        }
        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL, false);
    }


    @Override
    public ResultUtils<Boolean> delById(Long id) {

        //查找库位是否有商品
        Long storageCommodityId = storageCommodityService.judgeStorageLocationIsNull(id);
        //库位存在商品，不可以进行删除
        if (storageCommodityId != null) {
            return ResultUtils.APPRESULT(StorageLocationServiceCode.STORAGE_EXIST_COMMODITY.code, StorageLocationServiceCode.STORAGE_EXIST_COMMODITY.msg, null);
        }

        boolean flag = removeById(id);
        if (flag) {
            return ResultUtils.APPRESULT(CommonStatusCode.DEL_SUCCESS, true);
        }

        return ResultUtils.APPRESULT(CommonStatusCode.DEL_FAIL, false);
    }


    @Override
    public ResultUtils<StorageLocalInfoVo> getStorageLocalBySn(String storageLocalSn) {
        StorageLocalInfoVo storageLocationInfoVo = baseMapper.getStorageLocalBySn(storageLocalSn);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, storageLocationInfoVo);
    }

    @Override
    public ResultUtils<Page<StorageLocalListVo>> getList(PageDto pageDto, StorageLocationKeyDto keyDto) {

        Page<StorageLocalListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<StorageLocalListVo> list = baseMapper.getList(page, keyDto);
        page.setRecords(list);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    @Override
    @Transactional
    public ResultUtils<Boolean> batchUpdate(StorageLocationBatchUpdDto storageLocationBatchUpdDto) {

        List<StorageLocalPo> storageLocationPos = new ArrayList<>();
        List<Long> longs = StringUtil.stringToLongList(storageLocationBatchUpdDto.getIdList());
        for (Long aLong : longs) {
            StorageLocalPo storageLocationPo = new StorageLocalPo();
            storageLocationPo.setId(aLong);
            storageLocationPo.setSizeLength(storageLocationBatchUpdDto.getSizeLength());
            storageLocationPo.setSizeWidth(storageLocationBatchUpdDto.getSizeWidth());
            storageLocationPo.setSizeHeight(storageLocationBatchUpdDto.getSizeHeight());
            storageLocationPo.setSupportWeight(storageLocationBatchUpdDto.getSupportWeight());
            storageLocationPo.setRemark(storageLocationBatchUpdDto.getRemark());
            storageLocationPos.add(storageLocationPo);
        }

        boolean flag = this.updateBatchById(storageLocationPos);
        if (flag) {
            return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS);
        }

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL, null);
    }


    @Override
    public void print(String id)  throws Exception{

        List<Long> longs = StringUtil.stringToLongList(id);
        List<StorageLocationBo> list = baseMapper.getPrintData(longs);

        String sysPath = System.getProperty("user.dir").replace("\\", "/");
        File reportFile = new File(sysPath + "/" + storageLocationReportPath);

        JasperHelper.export("pdf", "print.pdf", reportFile, CommonContextHolder.getRequest(), CommonContextHolder.httpServletResponse(), new HashMap(), list);
    }

    //分配库位计算
    @Override
    public ResultUtils<List<StorageLocalInfoVo>> allocationStorageLocal(Long commodityId , Integer count , String storageLocalName)throws Exception {

        List<StorageLocalInfoVo> results = new ArrayList<>();

        //每件商品 的体积
        BigDecimal commodityVolume = countCommodityVolume(commodityId);

        //剩余商品数量
        Integer residueCount = count;
        //订单总体积  体积 * 数量，
        //BigDecimal orderVolume = singleCommodityVolume.multiply(new BigDecimal(count));

       try{
           //todo 获取未满 的库位 库位信息
           List<StorageLocalInfoVo> storageLocals = baseMapper.getUnFullStorageLocal(commodityId);

           //使用 未满 的 库位进行分配库位
               //循环 减 订单体积 ， 直到小于 零
               for (StorageLocalInfoVo storageLocalBo : storageLocals) {

                   //将库位存到返回值
                   results.add(storageLocalBo);

                   //总容量（单个库位）
                   BigDecimal allVolume = storageLocalBo.getVolume().multiply(new BigDecimal(0.8));

                   //已使用的容量（单个库位）
                   BigDecimal alreadyUseVolume = commodityVolume.multiply(BigDecimal.valueOf(storageLocalBo.getStockCount()));

                   //剩余的容量（单个库位）
                   BigDecimal residueVolume = allVolume.subtract(alreadyUseVolume);

                   //剩余可以装多少个商品（单个库位）
                   BigDecimal usableCount = residueVolume.divideAndRemainder(commodityVolume)[0];

                   //库位装不下一个商品
                   if(usableCount.intValue() == 0){
                       //直接找下一个库位
                       //break;
                       continue;
                   }
                   //可分配的库位数量 大于 剩余的商品数量 ， 说明可以分配完成，此库位足够装下，直接返回订单数量
                   if(usableCount.intValue() > residueCount){
                       storageLocalBo.setStockCount(residueCount);
                       break;
                   }
                   storageLocalBo.setStockCount(usableCount.intValue());

                   residueCount = residueCount - usableCount.intValue();

                   //数量小于 零 ，说明已经分配 完成了
                   if(residueCount < 0){
                       //直接退出 循环
                       break;
                   }
           }

           //使用 空的 库位进行分配库位如果50个 空库位 还未分配完成 ,继续增加分配库存的数量，在看看如何处理
           if(residueCount > 0){
               //获取50个 空的 库位 进行分配
               storageLocals = baseMapper.getNullStorageLocal(50);

                   for (StorageLocalInfoVo storageLocalBo : storageLocals) {

                       //获取库位 的容量
                       BigDecimal remain = storageLocalBo.getVolume().multiply(new BigDecimal(0.8));

                       //单个库位 可以装多少个 商品
                       BigDecimal storageCount = remain.divideAndRemainder(commodityVolume)[0];

                       //一般不会出现 一个库位装不下一个商品
                       if(storageCount.intValue() == 0){
                           continue ;
                       }
                       //如果一个库位足够装下 商品，直接返回订单数量
                       if(storageCount.intValue() > residueCount){
                           storageLocalBo.setStockCount(residueCount);
                           //单个库位可分配完，直接返回
                           results.add(storageLocalBo);
                           break ;
                       }
                       storageLocalBo.setStockCount(storageCount.intValue());

                       residueCount = residueCount - storageCount.intValue();

                       //数量小于 零 ，说明已经分配 完成了
                       if(residueCount < 0){
                           //直接退出 循环
                           break;
                       }

                       //将库位存到返回值
                       results.add(storageLocalBo);
                   }
           }

       }finally {
       }

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS , results);
    }



    //分配库位锁定
    @Override
    @Transactional
    public ResultUtils<Boolean> changeStorageLocalStatus(String lockIds, String unLockIds) {

        Boolean result = baseMapper.changeStorageLocalStatus(unLockIds, AplConstants.UN_LOCK);
        //解锁库位id 不等于 零 且 更新失败 ，直接抛异常
        if (!result && !unLockIds.equals("0")) {
            //锁定库位失败
            throw new AplException(StorageLocationServiceCode.LOCK_STORAGE_LOCAL_FAIL.code, StorageLocationServiceCode.LOCK_STORAGE_LOCAL_FAIL.msg);
        }
        result = baseMapper.changeStorageLocalStatus(lockIds, AplConstants.LOCK);
        //锁定库位id 不等于 零 且 更新失败 ，直接抛异常
        if (!result && !lockIds.equals("0")) {
            //锁定库位失败
            throw new AplException(StorageLocationServiceCode.LOCK_STORAGE_LOCAL_FAIL.code, StorageLocationServiceCode.LOCK_STORAGE_LOCAL_FAIL.msg);
        }


        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS, true);
    }

    @Override
    public ResultUtils<StorageLocalInfoVo> allocationOneStorageLocal(Long commodityId , String storageLocalSn) {

        //查找商品体积
        BigDecimal commodityVolume = countCommodityVolume(commodityId);
        //查找库位信息
        StorageLocalInfoVo storageLocalInfoVo = baseMapper.getStorageLocalBySn(storageLocalSn);

        //库位不存在
        if(storageLocalInfoVo == null){
            throw new AplException(StorageLocationServiceCode.STORAGE_LOCAL_IS_NOT_EXIST.code , StorageLocationServiceCode.STORAGE_LOCAL_IS_NOT_EXIST.msg);
        }

        //库位存储 商品数量
        BigDecimal storageCount = storageLocalInfoVo.getVolume().divideAndRemainder(commodityVolume)[0];
        storageLocalInfoVo.setStockCount(storageCount.intValue());
        //分配一个库位
        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS , storageLocalInfoVo);
    }


    Boolean exists(Long id, String storageSn) {

        List<StorageLocalInfoVo> list = baseMapper.exists(id, storageSn);
        if (!CollectionUtils.isEmpty(list) && list.size() > 0) {
            return true;
           /*for(StorageLocationInfoVo storageLocationInfoVo : list) {

              if(storageLocationInfoVo.getStorageSn().equals(storageSn));
                 return true;
           }*/
        }
        return false;
    }



    /**
     * @Desc: 计算商品体积
     * @Author: CY
     * @Date: 2020/3/24 9:45
     */
    private BigDecimal countCommodityVolume(Long commodityId) {

        CommodityPo find = commodityService.getById(commodityId);

        if(find == null){
            throw new AplException(StorageLocationServiceCode.COMMODITY_IS_NOT_EXIST.code , StorageLocationServiceCode.COMMODITY_IS_NOT_EXIST.msg);
        }

        //每件商品 的体积
        return find.getSizeLength().multiply(find.getSizeWidth()).multiply(find.getSizeHeight());
    }


}
