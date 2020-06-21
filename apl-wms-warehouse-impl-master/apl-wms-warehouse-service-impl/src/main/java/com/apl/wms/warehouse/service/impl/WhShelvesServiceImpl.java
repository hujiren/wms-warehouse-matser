package com.apl.wms.warehouse.service.impl;
import com.apl.lib.exception.AplException;
import com.apl.lib.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.apl.lib.constants.CommonStatusCode;

import com.apl.wms.warehouse.mapper.WhShelvesMapper;
import com.apl.wms.warehouse.service.WhShelvesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.po.WhShelvesPo;
import com.apl.wms.warehouse.vo.WhShelvesListVo;
import com.apl.wms.warehouse.vo.WhShelvesInfoVo;
import com.apl.wms.warehouse.dto.WhShelvesKeyDto;

import java.util.List;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;


/**
 * <p>
 * 货架 service实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Service
@Slf4j
public class WhShelvesServiceImpl extends ServiceImpl<WhShelvesMapper, WhShelvesPo> implements WhShelvesService {

    //状态code枚举
    /*enum WhShelvesServiceCode {

        ;

        private String code;
        private String msg;

        WhShelvesServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }*/



    @Override
    public ResultUtils<Integer> add(WhShelvesPo whShelves){

        this.exists(0L, whShelves.getShelvesNo() );

        Integer flag = baseMapper.insert(whShelves);
        if(flag.equals(1)){
            return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS , whShelves.getId());
        }

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtils<Boolean> updById(WhShelvesPo whShelves){

        this.exists(whShelves.getId(), whShelves.getShelvesNo() );

        Integer flag = baseMapper.updateById(whShelves);
        if(flag.equals(1)){
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
    public ResultUtils<WhShelvesInfoVo> selectById(Long id){

        WhShelvesInfoVo whShelvesInfoVo = baseMapper.getById(id);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, whShelvesInfoVo);
    }


    @Override
    public ResultUtils<Page<WhShelvesListVo>> getList(PageDto pageDto, WhShelvesKeyDto keyDto){

        Page<WhShelvesListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<WhShelvesListVo> list = baseMapper.getList(page , keyDto);
        page.setRecords(list);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    void exists(Long id,  String shelvesNo ) {

        List<WhShelvesInfoVo> list = baseMapper.exists(id, shelvesNo );
        if (!CollectionUtils.isEmpty(list)) {
           for(WhShelvesInfoVo  whShelvesInfoVo : list) {

              if(whShelvesInfoVo.getShelvesNo().equals(shelvesNo))
                 throw new AplException("SHELVES_NO_EXIST", "shelvesNo已经存在");
           }
        }
    }
}
