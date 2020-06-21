package com.apl.wms.warehouse.service.impl;
import com.apl.lib.exception.AplException;
import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.mapper.StoreApiMapper;
import com.apl.wms.warehouse.mapper.StoreMapper;
import com.apl.wms.warehouse.service.StoreService;
import com.apl.wms.warehouse.po.StoreApiPo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apl.lib.constants.CommonStatusCode;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;

import com.apl.wms.warehouse.po.StorePo;
import com.apl.wms.warehouse.vo.StoreListVo;
import com.apl.wms.warehouse.vo.StoreInfoVo;
import com.apl.wms.warehouse.dto.StoreKeyDto;
import com.apl.lib.pojo.dto.PageDto;


/**
 * <p>
 * 网店铺 service实现类
 * </p>
 *
 * @author arran
 * @since 2019-12-21
 */
@Service
@Slf4j
public class StoreServiceImpl extends ServiceImpl<StoreMapper, StorePo> implements StoreService {

    //状态code枚举
    /*enum StoreServiceCode {

        ;

        private String code;
        private String msg;

        StoreServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }*/

    @Autowired
    StoreApiMapper storeApiMapper;


    @Override
    public ResultUtils<Integer> add(StorePo store){
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        this.exists(0L, securityUser.getOuterOrgId(),  store.getStoreCode(),  store.getStoreName(),  store.getStoreNameEn() );
        store.setCustomerId(securityUser.getOuterOrgId());

        Integer flag = baseMapper.insert(store);
        if(flag.equals(1)){
            return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS , store.getId());
        }

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtils<Boolean> updById(StorePo store){
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        this.exists(store.getId(), securityUser.getOuterOrgId(),  store.getStoreCode(),  store.getStoreName(),  store.getStoreNameEn() );
        //store.setCustomerId(securityUser.getOuterOrgId());

        QueryWrapper<StorePo> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id", securityUser.getOuterOrgId());
        wrapper.eq("id", store.getId());
        Integer flag =  baseMapper.update(store, wrapper);
        if(flag.equals(1)){
            return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL , false);
    }

    @Override
    public ResultUtils<Boolean> updConfig(StoreApiPo storeApiPo){

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();

        QueryWrapper<StoreApiPo> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id", securityUser.getOuterOrgId());
        wrapper.eq("id", storeApiPo.getId());
        Integer flag =  storeApiMapper.updateById(storeApiPo);
        if(flag==1){
            return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL , false);

    }


    @Override
    public ResultUtils<Boolean> delById(Long id){

        boolean flag = removeById(id);
        if(flag){
            return ResultUtils.APPRESULT(CommonStatusCode.DEL_SUCCESS , true);
        }

        return ResultUtils.APPRESULT(CommonStatusCode.DEL_FAIL , false);
    }


    @Override
    public ResultUtils<StoreInfoVo> selectById(Long id){

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();

        StoreInfoVo storeInfoVo = baseMapper.getById(securityUser.getOuterOrgId(), id);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, storeInfoVo);
    }


    @Override
    public ResultUtils<StoreApiPo> getApiConfig(Long id, Long customerId){

        StoreApiPo storeApiPo = baseMapper.getApiConfig(id, customerId);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, storeApiPo);
    }


    @Override
    public ResultUtils<String> getApiConfigStrVal(Long id, Long customerId){

        String val = baseMapper.getApiConfigStrVal(id, customerId);

        return ResultUtils.APPRESULT(CommonStatusCode.SYSTEM_SUCCESS, val);
    }


    @Override
    public ResultUtils<Page<StoreListVo>> getList(PageDto pageDto, StoreKeyDto keyDto){

        Page<StoreListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<StoreListVo> list = baseMapper.getList(page , keyDto);
        page.setRecords(list);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS , page);
    }


    void exists(Long id,  Long customerId,   String storeCode,   String storeName,   String storeNameEn ) {

        List<StoreInfoVo> list = baseMapper.exists(id, customerId,  storeCode,  storeName,  storeNameEn );
        if (!CollectionUtils.isEmpty(list)) {
           for(StoreInfoVo  storeInfoVo : list) {

              if(storeInfoVo.getStoreCode().equals(storeCode))
                 throw new AplException("STORE_CODE_EXIST", "店铺编号已经存在");
              if(storeInfoVo.getStoreName().equals(storeName))
                 throw new AplException("STORE_NAME_EXIST", "店铺名称已经存在");
              if(storeInfoVo.getStoreNameEn().equals(storeNameEn))
                 throw new AplException("STORE_NAME_EN_EXIST", "店铺英文名称已经存在");
           }
        }
    }
}
