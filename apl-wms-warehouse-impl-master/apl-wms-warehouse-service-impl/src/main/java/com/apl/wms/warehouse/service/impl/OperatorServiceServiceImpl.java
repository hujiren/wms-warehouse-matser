package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.mapper.OperatorServiceMapper;
import com.apl.wms.warehouse.dto.OperatorServiceKeyDto;
import com.apl.wms.warehouse.po.OperatorServicePo;
import com.apl.wms.warehouse.service.OperatorServiceService;
import com.apl.wms.warehouse.vo.OperatorServiceInfoVo;
import com.apl.wms.warehouse.vo.OperatorServiceListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * <p>
 * 附加服务操作名称 service实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Service
@Slf4j
public class OperatorServiceServiceImpl extends ServiceImpl<OperatorServiceMapper, OperatorServicePo> implements OperatorServiceService {

    //状态code枚举
    /*enum OperatorServiceServiceCode {

        ;

        private String code;
        private String msg;

        OperatorServiceServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }*/



    @Override
    public ResultUtil<Integer> add(OperatorServicePo operatorService){

        this.exists(0L, operatorService.getServiceName(),  operatorService.getServiceNameEn() );

        Integer flag = baseMapper.insert(operatorService);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , operatorService.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtil<Boolean> updById(OperatorServicePo operatorService){

        this.exists(operatorService.getId(), operatorService.getServiceName(),  operatorService.getServiceNameEn() );

        Integer flag = baseMapper.updateById(operatorService);
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
    public ResultUtil<OperatorServiceInfoVo> selectById(Long id){

        OperatorServiceInfoVo operatorServiceInfoVo = baseMapper.getById(id);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, operatorServiceInfoVo);
    }


    @Override
    public ResultUtil<Page<OperatorServiceListVo>> getList(PageDto pageDto, OperatorServiceKeyDto keyDto){

        Page<OperatorServiceListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<OperatorServiceListVo> list = baseMapper.getList(page , keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    void exists(Long id,  String name,   String nameEn ) {

        List<OperatorServiceInfoVo> list = baseMapper.exists(id, name,  nameEn );
        if (!CollectionUtils.isEmpty(list)) {
           for(OperatorServiceInfoVo operatorServiceInfoVo : list) {

              if(operatorServiceInfoVo.getServiceName().equals(name))
                 throw new AplException("NAME_EXIST", "name已经存在");
              if(operatorServiceInfoVo.getServiceNameEn().equals(nameEn))
                 throw new AplException("NAME_EN_EXIST", "nameEn已经存在");
           }
        }
    }
}
