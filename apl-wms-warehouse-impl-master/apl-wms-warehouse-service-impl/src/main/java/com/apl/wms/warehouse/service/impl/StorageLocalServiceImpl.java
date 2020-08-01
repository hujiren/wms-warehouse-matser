package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.join.JoinUtil;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.StringUtil;

import com.apl.wms.warehouse.lib.constants.WmsWarehouseAplConstants;
import com.apl.wms.warehouse.lib.pojo.bo.StorageLocationBo;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalInfoVo;
import com.apl.wms.warehouse.dao.StorageLocalMapper;
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
    public ResultUtil<Map> add(StorageLocationAddDto storageLocationAddDto) {

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
            String storageLocalSn = prefix + String.format("%03d", num);
            Boolean result = this.exists(0l, storageLocalSn);
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
                storageLocationPo.setStorageLocalSn((storageLocalSn));
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

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, resultMapVo);

    }


    @Override
    public ResultUtil<Boolean> updById(StorageLocalPo storageLocation) {

        Boolean result = this.exists(storageLocation.getId(), storageLocation.getStorageLocalSn());
        if (!result) {
            Integer flag = baseMapper.updateById(storageLocation);
            if (flag > 0) {
                return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, true);
            }
        }
        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL, false);
    }


    @Override
    public ResultUtil<Boolean> delById(Long id) {

        //查找库位是否有商品
        Long storageCommodityId = storageCommodityService.judgeStorageLocationIsNull(id);
        //库位存在商品，不可以进行删除
        if (storageCommodityId != null) {
            return ResultUtil.APPRESULT(StorageLocationServiceCode.STORAGE_EXIST_COMMODITY.code, StorageLocationServiceCode.STORAGE_EXIST_COMMODITY.msg, null);
        }

        boolean flag = removeById(id);
        if (flag) {
            return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.DEL_FAIL, false);
    }


    @Override
    public ResultUtil<StorageLocalInfoVo> getStorageLocalBySn(String storageLocalSn) {
        StorageLocalInfoVo storageLocationInfoVo = baseMapper.getStorageLocalBySn(storageLocalSn);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, storageLocationInfoVo);
    }

    @Override
    public ResultUtil<Page<StorageLocalListVo>> getList(PageDto pageDto, StorageLocationKeyDto keyDto) {

        Page<StorageLocalListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<StorageLocalListVo> list = baseMapper.getList(page, keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    @Override
    @Transactional
    public ResultUtil<Boolean> batchUpdate(StorageLocationBatchUpdDto storageLocationBatchUpdDto) {

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
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL, null);
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
    public ResultUtil<List<StorageLocalInfoVo>> allocationStorageLocal(Long commodityId , Integer count , String storageLocalName)throws Exception {

        List<StorageLocalInfoVo> results = new ArrayList<>();


        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , results);
    }



    //分配库位锁定
    @Override
    @Transactional
    public ResultUtil<Boolean> changeStorageLocalStatus(String lockIds, String unLockIds) {

        Boolean result = baseMapper.changeStorageLocalStatus(unLockIds, WmsWarehouseAplConstants.UN_LOCK);

        //解锁库位id 不等于 零 且 更新失败 ，直接抛异常
        if (!result && !unLockIds.equals("0")) {
            //锁定库位失败
            throw new AplException(StorageLocationServiceCode.LOCK_STORAGE_LOCAL_FAIL.code, StorageLocationServiceCode.LOCK_STORAGE_LOCAL_FAIL.msg);
        }
        result = baseMapper.changeStorageLocalStatus(lockIds, WmsWarehouseAplConstants.LOCK);
        //锁定库位id 不等于 零 且 更新失败 ，直接抛异常
        if (!result && !lockIds.equals("0")) {
            //锁定库位失败
            throw new AplException(StorageLocationServiceCode.LOCK_STORAGE_LOCAL_FAIL.code, StorageLocationServiceCode.LOCK_STORAGE_LOCAL_FAIL.msg);
        }


        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, true);
    }

    @Override
    public ResultUtil<StorageLocalInfoVo> allocationOneStorageLocal(Long commodityId , String storageLocalSn) {

        //查找商品体积
        Double commodityVolume = countCommodityVolume(commodityId);
        //查找库位信息
        StorageLocalInfoVo storageLocalInfoVo = baseMapper.getStorageLocalBySn(storageLocalSn);

        //库位不存在
        if(storageLocalInfoVo == null){
            throw new AplException(StorageLocationServiceCode.STORAGE_LOCAL_IS_NOT_EXIST.code , StorageLocationServiceCode.STORAGE_LOCAL_IS_NOT_EXIST.msg);
        }

        //库位存储 商品数量
        //BigDecimal storageCount = storageLocalInfoVo.getVolume().divideAndRemainder(commodityVolume)[0];
        //storageLocalInfoVo.setStockCount(storageCount.intValue());
        //分配一个库位
        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , storageLocalInfoVo);
    }


    Boolean exists(Long id, String storageLocalSn) {

        List<StorageLocalInfoVo> list = baseMapper.exists(id, storageLocalSn);
        if (!CollectionUtils.isEmpty(list) && list.size() > 0) {
            return true;
           /*for(StorageLocalInfoVo storageLocationInfoVo : list) {

              if(storageLocationInfoVo.getStorageLocalSn().equals(storageLocalSn));
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
    private Double countCommodityVolume(Long commodityId) {

        CommodityPo find = commodityService.getById(commodityId);

        if(find == null){
            throw new AplException(StorageLocationServiceCode.COMMODITY_IS_NOT_EXIST.code , StorageLocationServiceCode.COMMODITY_IS_NOT_EXIST.msg);
        }

        //每件商品 的体积
        return find.getSizeLength() * find.getSizeWidth() * find.getSizeHeight();
    }

}
