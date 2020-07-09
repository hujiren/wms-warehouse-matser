package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dao.WarehouseMapper;
import com.apl.wms.warehouse.po.WarehousePo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.apl.wms.warehouse.dao.WhDetailsMapper;
import com.apl.wms.warehouse.service.WhDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.po.WhDetailsPo;
import com.apl.wms.warehouse.vo.WhDetailsListVo;
import com.apl.wms.warehouse.dto.WhDetailsKeyDto;

import java.util.List;

import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;


/**
 * <p>
 * 仓库详细 服务实现类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Service
@Slf4j
public class WhDetailsServiceImpl extends ServiceImpl<WhDetailsMapper, WhDetailsPo> implements WhDetailsService {

    @Resource
    private WarehouseMapper warehouseMapper;

    public enum WhDetailsServiceImplStatusCode {

        WH_ID_NOT_EXIST("WH_ID_NOT_EXIST", "仓库基本信息id不存在");


        private String code;
        private String msg;

        WhDetailsServiceImplStatusCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    @Override
    public ResultUtil<Boolean> add(WhDetailsPo whDetails) {

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , save(whDetails));
    }

    @Override
    public ResultUtil<Boolean> deleteById(Long id) {


        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , removeById(id));
    }

    @Override
    public ResultUtil<Boolean> updByWhId(WhDetailsPo whDetails) {
        if (whDetails.getWhId() != null && whDetails.getWhId() >= 1) {
            QueryWrapper<WarehousePo> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("id", whDetails.getWhId());
            List<WarehousePo> rows2 = warehouseMapper.selectList(wrapper2);
            if (rows2.size() >= 1) {
                boolean res;
                QueryWrapper<WhDetailsPo> wrapper = new QueryWrapper<>();
                wrapper.eq("wh_id", whDetails.getWhId());
                List<WhDetailsPo> rows = baseMapper.selectList(wrapper);
                if (rows.size() >= 1) {
                    res = baseMapper.updateByWhId(whDetails);
                } else {
                    res = save(whDetails);
                }
                if (res) return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, whDetails.getWhId());
            } else {
                return ResultUtil.APPRESULT(
                        WhDetailsServiceImplStatusCode.WH_ID_NOT_EXIST.code,
                        WhDetailsServiceImplStatusCode.WH_ID_NOT_EXIST.msg,
                        null);
            }
        }
        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL, null);
    }

    @Override
    public ResultUtil<WhDetailsPo> selectById(Long id) {
        WhDetailsPo whDetailsPo = baseMapper.getByWhId(id);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, whDetailsPo);
    }


    @Override
    public ResultUtil<Page<WhDetailsListVo>> getList(PageDto pageDto, WhDetailsKeyDto keyDto) {

        Page<WhDetailsListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<WhDetailsListVo> list = baseMapper.getList(page, keyDto);
        page.setRecords(list);
        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS , page);
    }

}
