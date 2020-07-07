package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dto.CommodityPicAddDto;
import com.apl.wms.warehouse.dto.PicItemDto;
import com.apl.wms.warehouse.mapper.CommodityPicMapper;
import com.apl.wms.warehouse.po.CommodityPicPo;
import com.apl.wms.warehouse.service.CommodityPicService;
import com.apl.wms.warehouse.service.CommodityService;
import com.apl.wms.warehouse.vo.CommodityPicListVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * 商品图片 服务实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Service
@Slf4j
public class CommodityPicServiceImpl extends ServiceImpl<CommodityPicMapper, CommodityPicPo> implements CommodityPicService {

    //状态code枚举
    enum CommodityPicServiceCode {

        COMMODITY_IS_NOT_EXIST("COMMODITY_IS_NOT_EXIST","商品不存在");

        ;

        private String code;
        private String msg;

        CommodityPicServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }


    @Autowired
    CommodityService commodityService;


    @Override
    public ResultUtil<Integer> add(CommodityPicAddDto commodityPicAddDto){

        Long communityId = commodityPicAddDto.getCommodityId();

        //ResultUtil<CommodityInfoVo> ResultUtil = commodityService.selectById(communityId);
        //商品不存在
        //if(ResultUtil.getData() == null){
        //    return ResultUtil.APPRESULT(CommodityPicServiceCode.COMMODITY_IS_NOT_EXIST.code , CommodityPicServiceCode.COMMODITY_IS_NOT_EXIST.msg , null);
        //}

        List<PicItemDto> picItemDtos = commodityPicAddDto.getPicItemDtos();
        if(!CollectionUtils.isEmpty(picItemDtos)){
            Integer imgSort= baseMapper.getMaxSoft(communityId);
            if(imgSort==null)
                imgSort=1;
            else
                imgSort++;

            List<CommodityPicPo> commodityPicPos = new ArrayList<>();
            for (PicItemDto picItemDto : picItemDtos) {
                CommodityPicPo commodityPicPo = new CommodityPicPo();
                commodityPicPo.setCommodityId(communityId);
                commodityPicPo.setImgUrl(picItemDto.getImgUrl());
                commodityPicPo.setImgSort(imgSort);
                commodityPicPos.add(commodityPicPo);
                imgSort++;
            }
            boolean flag = this.saveBatch(commodityPicPos);

            if(flag){
                return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS);
            }
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtil<Boolean> updById(CommodityPicPo commodityPic){
        commodityPic.setCommodityId(null);
        Integer flag = baseMapper.updateById(commodityPic);

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
    public ResultUtil<List<CommodityPicListVo>> getList(Long commodityId){

        List<CommodityPicListVo> list = baseMapper.getList(commodityId);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, list);
    }


    public List<CommodityPicListVo> getByCommodityId(Long commodityId) {

        List<CommodityPicListVo> commodityPicInfoVos = null;
        commodityPicInfoVos.sort(new Comparator<CommodityPicListVo>() {
            @Override
            public int compare(CommodityPicListVo o1, CommodityPicListVo o2) {
                return o1.getImgSort().compareTo(o2.getImgSort());
            }
        });

        return commodityPicInfoVos;
    }


}
