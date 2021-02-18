package com.apl.wms.warehouse.service.impl;

import com.apl.cache.AplCacheHelper;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.join.JoinBase;
import com.apl.lib.join.JoinUtil;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.StringUtil;
import com.apl.sys.lib.cache.JoinCustomer;
import com.apl.sys.lib.cache.bo.CustomerCacheBo;
import com.apl.sys.lib.feign.InnerFeign;
import com.apl.wms.warehouse.dto.CommodityNameLibKeyDto;
import com.apl.wms.warehouse.mapper.CommodityNameLibMapper;
import com.apl.wms.warehouse.po.CommodityNameLibPo;
import com.apl.wms.warehouse.service.CommodityNameLibService;
import com.apl.wms.warehouse.vo.CommodityNameLibInfoVo;
import com.apl.wms.warehouse.vo.CommodityNameLibListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 品名库 service实现类
 * </p>
 *
 * @author apl
 * @since 2019-12-20
 */
@Service
@Slf4j
public class CommodityNameLibServiceImpl extends ServiceImpl<CommodityNameLibMapper, CommodityNameLibPo> implements CommodityNameLibService {

    //状态code枚举
    enum CommodityNameLibServiceCode {
        //illegal
        ID_ILLEGAL("ID_ILLEGAL" ,"id不合法"),
        ;

        private String code;
        private String msg;

        CommodityNameLibServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }

    @Autowired
    AplCacheHelper aplCacheHelper;

    @Autowired
    InnerFeign innerFeign;

    @Autowired
    JoinUtil JoinUtil;

    @Override
    public ResultUtil<Integer> add(CommodityNameLibPo commodityNameLib){


        Integer flag = baseMapper.insert(commodityNameLib);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , commodityNameLib.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtil<Boolean> updById(CommodityNameLibPo commodityNameLib){

        Integer flag = baseMapper.updateById(commodityNameLib);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , false);
    }


    @Override
    public ResultUtil<Boolean> delById(String id){
        id = StringUtil.checkIds(id);
        if(StringUtil.isEmpty(id)){
            return ResultUtil.APPRESULT(CommodityNameLibServiceCode.ID_ILLEGAL.code, CommodityNameLibServiceCode.ID_ILLEGAL.msg , false);
        }
        int flag = baseMapper.del(id);
        if(flag>0){
            return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.DEL_FAIL , false);
    }


    @Override
    public ResultUtil<CommodityNameLibInfoVo> selectById(Integer id) throws IOException {

        CommodityNameLibInfoVo commodityNameLibInfoVo = baseMapper.getById(id);

        JoinCustomer joinCustomer = new JoinCustomer(1, innerFeign, aplCacheHelper);
        CustomerCacheBo customerCacheBo = joinCustomer.getEntity(commodityNameLibInfoVo.getCustomerId());
        if(customerCacheBo!=null)
            commodityNameLibInfoVo.setCustomerName(customerCacheBo.getCustomerName());

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, commodityNameLibInfoVo);
    }


    @Override
    public ResultUtil<Page<CommodityNameLibListVo>> getList(PageDto pageDto, CommodityNameLibKeyDto keyDto)  throws Exception{

        Page<CommodityNameLibListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());
        
        List<CommodityNameLibListVo> list = baseMapper.getList(page , keyDto);

        List<JoinBase> joinTabs = new ArrayList<>();
        JoinCustomer joinCustomer = new JoinCustomer(1, innerFeign, aplCacheHelper);
        joinCustomer.addField("customerId",  Long.class, "customerName",  String.class);
        joinTabs.add(joinCustomer);

        JoinUtil.join(list, joinTabs);

         page.setRecords(list);

        return  ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    @Override
    public ResultUtil<Boolean> updCategory(String id, Integer categoryId){

        id = StringUtil.checkIds(id);
        if(StringUtil.isEmpty(id)){
            return ResultUtil.APPRESULT(CommodityNameLibServiceCode.ID_ILLEGAL.code, CommodityNameLibServiceCode.ID_ILLEGAL.msg , false);
        }

        Integer flag = baseMapper.updCategory(id, categoryId);
        if(flag>0){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , false);
    }


}
