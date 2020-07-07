package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.StringUtil;
import com.apl.sys.lib.constants.AplConstants;
import com.apl.wms.warehouse.mapper.CommodityBrandMapper;
import com.apl.wms.warehouse.dto.CommodityBrandKeyDto;
import com.apl.wms.warehouse.po.CommodityBrandPo;
import com.apl.wms.warehouse.service.CommodityBrandService;
import com.apl.wms.warehouse.vo.CommodityBrandInfoVo;
import com.apl.wms.warehouse.vo.CommodityBrandListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * <p>
 * 商品品牌 服务实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
@Service
@Slf4j
public class CommodityBrandServiceImpl extends ServiceImpl<CommodityBrandMapper, CommodityBrandPo> implements CommodityBrandService {

    //状态code枚举
    enum CommodityBrandServiceCode {

        BRAND_IS_ALREADY_EXIST("BRAND_IS_ALREADY_EXIST" ,"品牌已经存在"),
        ;

        private String code;
        private String msg;

        CommodityBrandServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }


    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public ResultUtil<Integer> add(String brandName , String brandNameEn){

        //检查添加是否重复
        checkBrand(null , brandName , brandNameEn);

        SecurityUser securityUser = CommonContextHolder.getSecurityUser(redisTemplate);

        CommodityBrandPo commodityBrand = new CommodityBrandPo();
        commodityBrand.setBrandName(brandName);
        commodityBrand.setBrandNameEn(brandNameEn);

        commodityBrand.setCustomerId(securityUser.getOuterOrgId());

        Integer flag = baseMapper.insert(commodityBrand);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , commodityBrand.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtil<Boolean> updById(Long brandId , String brandName , String brandNameEn){

        //判断 名称是否重复
        checkBrand(brandId , brandName , brandNameEn);

        CommodityBrandPo commodityBrand = new CommodityBrandPo();
        commodityBrand.setId(brandId);
        commodityBrand.setBrandName(brandName);
        commodityBrand.setBrandNameEn(brandNameEn);

        Integer flag = baseMapper.updateById(commodityBrand);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , false);
    }


    @Override
    public ResultUtil<Boolean> delById(String brandIdList){


        List<Long> brandIds = StringUtil.stringToLongList(brandIdList);

        Integer result = baseMapper.deleteBatchIds(brandIds);
        if(!result.equals(0)){
            return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.DEL_FAIL , false);
    }


    @Override
    public ResultUtil<CommodityBrandInfoVo> selectById(Long id){

        CommodityBrandInfoVo commodityBrandInfoVo = baseMapper.getById(id);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, commodityBrandInfoVo);
    }


    @Override
    public ResultUtil<Page<CommodityBrandListVo>> getList(PageDto pageDto, CommodityBrandKeyDto keyDto){

        Page<CommodityBrandListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<CommodityBrandListVo> list = baseMapper.getList(page , keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }

    /**
     * @Desc: 判断商标是否存在
     * @Author: CY
     * @Date: 2019/12/13 14:14
     */
    private void checkBrand(Long brandId , String brandName , String brandNameEn){

        List<CommodityBrandInfoVo> brands = baseMapper.exist(brandId ,brandName, brandNameEn);

        if (!CollectionUtils.isEmpty(brands)) {
            for (CommodityBrandInfoVo brand : brands) {
                if(brand.getBrandName().equals(brandName))
                    throw new AplException("BRAND_NAME_EXIST", "品牌名称已经存在");
                else if(brand.getBrandNameEn().equals(brandNameEn))
                    throw new AplException("BRAND_EN_NAME_EXIST", "品牌英文名称已经存在");
            }
        }
    }

}
