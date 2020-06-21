package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.utils.ResultUtils;
import com.apl.wms.warehouse.vo.WarehouseInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.apl.wms.warehouse.mapper.WarehouseMapper;
import com.apl.wms.warehouse.service.WarehouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.po.WarehousePo;
import com.apl.wms.warehouse.vo.WarehouseListVo;
import com.apl.wms.warehouse.dto.WarehouseKeyDto;

import java.util.List;

import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;


/**
 * <p>
 * 仓库 服务实现类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Service
@Slf4j
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, WarehousePo> implements WarehouseService {


    public enum WarehouseServiceImplStatusCode {

        //WH_CODE_CAN_NOT_REPEAT("WH_CODE_CAN_NOT_REPEAT", "仓库代码不能重复")
        ;
        private String code;
        private String msg;

        WarehouseServiceImplStatusCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }


    private void exist(Long id, String whCode, String whName, String whNameEn) {

        List<WarehousePo> list = baseMapper.exist( id,  whCode,   whName,   whNameEn);
        if (!CollectionUtils.isEmpty(list)) {
            for(WarehousePo warehousePo : list) {
                if(warehousePo.getWhCode().equals(whCode))
                    throw new AplException("WH_CODE_CAN_NOT_REPEAT", "仓库代码不能重复");
                if(warehousePo.getWhName().equals(whName))
                    throw new AplException("WH_NAME_CAN_NOT_REPEAT", "仓库中文名不能重复");
                if(warehousePo.getWhNameEn().equals(whNameEn))
                    throw new AplException("WH_NAME_EN_CAN_NOT_REPEAT", "仓库英文名不能重复");
            }
        }
    }

    @Override
    public ResultUtils add(WarehousePo warehouse) {

        this.exist( 0l,  warehouse.getWhCode(),  warehouse.getWhName(),  warehouse.getWhNameEn());

        Integer flag = baseMapper.insert(warehouse);
        if (flag.equals(1)) {
            return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS, warehouse.getId());
        }

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL, null);
    }

    @Override
    public ResultUtils<Boolean> deleteById(Long id) {

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS , removeById(id));
    }

    @Override
    public ResultUtils<Boolean> updById(WarehousePo warehouse) {
        this.exist(  warehouse.getId(),  warehouse.getWhCode(), warehouse.getWhName(),  warehouse.getWhNameEn());

        Integer flag = baseMapper.updateById(warehouse);
        if (flag.equals(1)) {
            return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS, true);
        }
        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL, false);
    }

    @Override
    public ResultUtils<WarehouseInfoVo> selectById(Long id) {

        WarehouseInfoVo warehouseInfoVo = baseMapper.getById(id);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, warehouseInfoVo);
    }


    @Override
    public ResultUtils<Page<WarehouseListVo>> getList(PageDto pageDto, WarehouseKeyDto keyDto) {
        Page<WarehouseListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<WarehouseListVo> list = baseMapper.getList(page, keyDto);
        page.setRecords(list);
        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS , page);
    }

    @Override
    public ResultUtils<List<WarehouseListVo>> getBind() {

        WarehouseKeyDto keyDto = new WarehouseKeyDto();
        keyDto.setWhStatus(1);

        List<WarehouseListVo> list = baseMapper.getList(null, keyDto);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS , list);
    }

}
