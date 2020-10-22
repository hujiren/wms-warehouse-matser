package com.apl.wms.warehouse.service.impl;
import com.apl.lib.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.apl.lib.constants.CommonStatusCode;

import com.apl.wms.warehouse.mapper.GoodsBasketMapper;
import com.apl.wms.warehouse.service.GoodsBasketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.po.GoodsBasketPo;
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
 * @author hjr
 *  * @since 2020-09-26
 */
@Service
@Slf4j
public class GoodsBasketServiceImpl extends ServiceImpl<GoodsBasketMapper, GoodsBasketPo> implements GoodsBasketService {

    //状态code枚举
    enum GoodsBasketServiceCode {

        ID_IS_NOT_EXIST("ID_IS_NOT_EXIST", "id不存在"),
        NO_CORRESPONDING_DATA("NO_CORRESPONDING_DATA", "没有对应数据"),
        BASKET_NUMBER_IS_ALREADY_EXISTS("BASKET_NUMBER_IS_ALREADY_EXISTS", "货篮编号已经存在"),
        ;

        private String code;
        private String msg;

        GoodsBasketServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }



    @Override
    public ResultUtil<Integer> add(GoodsBasketPo goodsBasket){
        Boolean exists = this.exists(0L, goodsBasket.getBasketSn());
        goodsBasket.setId(null);
        if(exists){
            return ResultUtil.APPRESULT(GoodsBasketServiceCode.BASKET_NUMBER_IS_ALREADY_EXISTS.code,
                    GoodsBasketServiceCode.BASKET_NUMBER_IS_ALREADY_EXISTS.msg, 0);
        }
        Integer flag = baseMapper.insert(goodsBasket);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , goodsBasket.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtil<Boolean> updById(GoodsBasketPo goodsBasket){

        Boolean exists = this.exists(goodsBasket.getId(), goodsBasket.getBasketSn());
        if(exists){
            return ResultUtil.APPRESULT(GoodsBasketServiceCode.BASKET_NUMBER_IS_ALREADY_EXISTS.code,
                    GoodsBasketServiceCode.BASKET_NUMBER_IS_ALREADY_EXISTS.msg, false);
        }
        Integer flag = baseMapper.updateById(goodsBasket);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(GoodsBasketServiceCode.ID_IS_NOT_EXIST.code,
                GoodsBasketServiceCode.ID_IS_NOT_EXIST.msg, false);
    }


    @Override
    public ResultUtil<Boolean> delById(Long id){

        baseMapper.deleteById(id);
        return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS , true);

    }

    /**
     * 查询详细
     * @param id
     * @return
     */
    @Override
    public ResultUtil<GoodsBasketInfoVo> selectById(Long id){

        GoodsBasketInfoVo goodsBasketInfoVo = baseMapper.getById(id);
        if(null == goodsBasketInfoVo)
            return ResultUtil.APPRESULT(GoodsBasketServiceCode.NO_CORRESPONDING_DATA.code,
                    GoodsBasketServiceCode.NO_CORRESPONDING_DATA.msg, null);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, goodsBasketInfoVo);
    }

    /**
     * 查询列表
     * @param pageDto
     * @param keyDto
     * @return
     */
    @Override
    public ResultUtil<Page<GoodsBasketInfoVo>> getList(PageDto pageDto, GoodsBasketKeyDto keyDto){

        Page<GoodsBasketInfoVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<GoodsBasketInfoVo> list = baseMapper.getList(page , keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    Boolean exists(Long id,  String basketSn) {
        Boolean exists = false;
        List<GoodsBasketInfoVo> list = baseMapper.exists(id, basketSn);
        if (!CollectionUtils.isEmpty(list)) {
           for(GoodsBasketInfoVo  goodsBasketInfoVo : list) {
              if(goodsBasketInfoVo.getBasketSn().equals(basketSn))
                  exists = true;
           }
        }
        return exists;
    }
}
