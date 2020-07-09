package com.apl.wms.warehouse.service.impl;
import com.apl.lib.exception.AplException;
import com.apl.lib.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.apl.lib.constants.CommonStatusCode;

import com.apl.wms.warehouse.dao.GoodsBasketMapper;
import com.apl.wms.warehouse.service.GoodsBasketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.po.GoodsBasketPo;
import com.apl.wms.warehouse.vo.GoodsBasketListVo;
import com.apl.wms.warehouse.vo.GoodsBasketInfoVo;
import com.apl.wms.warehouse.dto.GoodsBasketKeyDto;

import java.util.List;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;


/**
 * <p>
 * 货蓝 service实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Service
@Slf4j
public class GoodsBasketServiceImpl extends ServiceImpl<GoodsBasketMapper, GoodsBasketPo> implements GoodsBasketService {

    //状态code枚举
    /*enum GoodsBasketServiceCode {

        ;

        private String code;
        private String msg;

        GoodsBasketServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }*/



    @Override
    public ResultUtil<Integer> add(GoodsBasketPo goodsBasket){

        this.exists(0L, goodsBasket.getBasketSn() );

        Integer flag = baseMapper.insert(goodsBasket);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , goodsBasket.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtil<Boolean> updById(GoodsBasketPo goodsBasket){

        this.exists(goodsBasket.getId(), goodsBasket.getBasketSn() );

        Integer flag = baseMapper.updateById(goodsBasket);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , false);
    }


    @Override
    public ResultUtil<Boolean> delById(Long id){

        boolean flag = removeById(id);
        if(flag){
            return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.DEL_FAIL , false);
    }


    @Override
    public ResultUtil<GoodsBasketInfoVo> selectById(Long id){

        GoodsBasketInfoVo goodsBasketInfoVo = baseMapper.getById(id);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, goodsBasketInfoVo);
    }


    @Override
    public ResultUtil<Page<GoodsBasketListVo>> getList(PageDto pageDto, GoodsBasketKeyDto keyDto){

        Page<GoodsBasketListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<GoodsBasketListVo> list = baseMapper.getList(page , keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    void exists(Long id,  String basketSn ) {

        List<GoodsBasketInfoVo> list = baseMapper.exists(id, basketSn );
        if (!CollectionUtils.isEmpty(list)) {
           for(GoodsBasketInfoVo  goodsBasketInfoVo : list) {

              if(goodsBasketInfoVo.getBasketSn().equals(basketSn))
                 throw new AplException("BASKET_SN_EXIST", "编号已经存在");
           }
        }
    }
}
