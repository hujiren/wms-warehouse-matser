package com.apl.wms.warehouse.service.impl;

import com.apl.cache.AplCacheUtil;
import com.apl.lib.exception.AplException;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.cache.bo.OperatorCacheBo;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;
import com.apl.wms.warehouse.lib.utils.WmsWarehouseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apl.lib.constants.CommonStatusCode;

import com.apl.wms.warehouse.mapper.WhOperatorMapper;
import com.apl.wms.warehouse.service.WhOperatorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.po.WhOperatorPo;
import com.apl.wms.warehouse.vo.WhOperatorListVo;
import com.apl.wms.warehouse.vo.WhOperatorInfoVo;
import com.apl.wms.warehouse.dto.WhOperatorKeyDto;

import java.util.List;

import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;


/**
 * <p>
 * 仓库操作员 service实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Service
@Slf4j
public class WhOperatorServiceImpl extends ServiceImpl<WhOperatorMapper, WhOperatorPo> implements WhOperatorService {

    //状态code枚举
    /*enum WhOperatorServiceCode {

        ;

        private String code;
        private String msg;

        WhOperatorServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }*/

    @Autowired
    AplCacheUtil redisTemplate;

    @Autowired
    WarehouseFeign warehouseFeign;

    @Override
    public ResultUtil<Integer> add(WhOperatorPo whOperator) {

        this.exists(0L, whOperator.getWhId(), whOperator.getMemberId());

        Integer flag = baseMapper.insert(whOperator);
        if (flag.equals(1)) {
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, whOperator.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL, null);
    }


    @Override
    public ResultUtil<Boolean> updById(WhOperatorPo whOperator) {

        this.exists(whOperator.getId(),whOperator.getWhId(), whOperator.getMemberId());

        Integer flag = baseMapper.updateById(whOperator);
        if (flag.equals(1)) {
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL, false);
    }


    @Override
    public ResultUtil<Boolean> delById(Long id) {

        boolean flag = removeById(id);
        if (flag) {
            return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.DEL_FAIL, false);
    }


    @Override
    public ResultUtil<WhOperatorInfoVo> selectById(Long id) {

        WhOperatorInfoVo whOperatorInfoVo = baseMapper.getById(id);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, whOperatorInfoVo);
    }


    @Override
    public ResultUtil<Page<WhOperatorListVo>> getList(PageDto pageDto, WhOperatorKeyDto keyDto) {

        OperatorCacheBo operatorCacheBo = WmsWarehouseUtils.checkOperator(warehouseFeign, redisTemplate);
        keyDto.setWhId(operatorCacheBo.getWhId());

        Page<WhOperatorListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<WhOperatorListVo> list = baseMapper.getList(page, keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , page);
    }


    void exists(Long id, Long whId, Long memberId) {

        List<WhOperatorInfoVo> list = baseMapper.exists(id, whId, memberId);
        if (!CollectionUtils.isEmpty(list)) {
            for (WhOperatorInfoVo whOperatorInfoVo : list) {

                if (whOperatorInfoVo.getMemberId().equals(memberId))
                    throw new AplException("MEMBER_ID_EXIST", "memberId已经存在");
            }
        }
    }
}
