package com.apl.wms.warehouse.service.impl;

import com.apl.lib.exception.AplException;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.po.WhZonePo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.apl.lib.constants.CommonStatusCode;

import com.apl.wms.warehouse.mapper.WhZoneMapper;
import com.apl.wms.warehouse.service.WhZoneService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.vo.WhZoneListVo;
import com.apl.wms.warehouse.vo.WhZoneInfoVo;
import com.apl.wms.warehouse.dto.WhZoneKeyDto;

import java.util.List;
import java.util.Objects;

import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;


/**
 * <p>
 * 仓库分区 service实现类
 * </p>
 *
 * @author apl
 * @since 2019-12-16
 */
@Service
@Slf4j
public class WhZoneServiceImpl extends ServiceImpl<WhZoneMapper, WhZonePo> implements WhZoneService {

    //状态code枚举
    /*enum WhZoneServiceCode {

        ;

        private String code;
        private String msg;

        WhZoneServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }*/


    @Override
    public ResultUtil<Integer> add(WhZonePo whZone) {

        this.exists(0L, whZone.getWhId(), whZone.getZoneCode(), whZone.getZoneName(), whZone.getZoneNameEn());

        Integer flag = baseMapper.insert(whZone);
        if (flag.equals(1)) {
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, whZone.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL, null);
    }


    @Override
    public ResultUtil<Boolean> updById(WhZonePo whZone) {

        this.exists(whZone.getId(), whZone.getWhId(), whZone.getZoneCode(), whZone.getZoneName(), whZone.getZoneNameEn());

        Integer flag = baseMapper.updateById(whZone);
        if (flag.equals(1)) {
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL, false);
    }


    @Override
    public ResultUtil<Boolean> delById(Integer id) {

        boolean flag = removeById(id);
        if (flag) {
            return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS, true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.DEL_FAIL, false);
    }


    @Override
    public ResultUtil<WhZoneInfoVo> getById(Integer id) {

        WhZoneInfoVo whZoneInfoVo = baseMapper.getById(id);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, whZoneInfoVo);
    }


    @Override
    public ResultUtil<Page<WhZoneListVo>> getList(PageDto pageDto, WhZoneKeyDto keyDto) {

        Page<WhZoneListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<WhZoneListVo> list = baseMapper.getList(page, keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , page);
    }


    void exists(Long id, Long whId, String zoneCode, String zoneName, String zoneNameEn) {

        List<WhZoneInfoVo> list = baseMapper.exists(id, whId, zoneCode, zoneName, zoneNameEn);
        if (!CollectionUtils.isEmpty(list)) {
            for (WhZoneInfoVo whZoneInfoVo : list) {
                if (id == 0 || !Objects.equals(whZoneInfoVo.getId(), id)) {
                    if (whZoneInfoVo.getZoneCode().equals(zoneCode))
                        throw new AplException("ZONE_CODE_EXIST", "zoneCode已经存在");
                    if (whZoneInfoVo.getZoneName().equals(zoneName))
                        throw new AplException("ZONE_NAME_EXIST", "zoneName已经存在");
                    if (whZoneInfoVo.getZoneNameEn().equals(zoneNameEn))
                        throw new AplException("ZONE_NAME_EN_EXIST", "zoneNameEn已经存在");
                }
            }
        }
    }

//    void exists(Long id, String zoneCode, String zoneName, String zoneNameEn) {
//        exists(id, zoneCode, zoneName, zoneNameEn);
//    }
}
