package com.apl.wms.warehouse.service.impl;

import com.apl.lib.exception.AplException;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.dao.ShelvesSpecMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.apl.lib.constants.CommonStatusCode;

import com.apl.wms.warehouse.service.ShelvesSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.po.ShelvesSpecPo;
import com.apl.wms.warehouse.vo.ShelvesSpecListVo;
import com.apl.wms.warehouse.vo.ShelvesSpecInfoVo;
import com.apl.wms.warehouse.dto.ShelvesSpecKeyDto;

import java.util.List;

import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;


/**
 * <p>
 * 货架规格 service实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-19
 */
@Service
@Slf4j
public class ShelvesSpecServiceImpl extends ServiceImpl<ShelvesSpecMapper, ShelvesSpecPo> implements ShelvesSpecService {

    //状态code枚举
    /*enum ShelvesSpecServiceCode {

        ;

        private String code;
        private String msg;

        ShelvesSpecServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }*/


    @Override
    public ResultUtil<Integer> add(ShelvesSpecPo shelvesSpec) {

        this.exists(0L, shelvesSpec.getSpecNo());
        System.out.println(shelvesSpec);
        Integer flag = baseMapper.insert(shelvesSpec);
        if (flag.equals(1)) {
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS, shelvesSpec.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL, null);
    }


    @Override
    public ResultUtil<Boolean> updById(ShelvesSpecPo shelvesSpec) {

        this.exists(shelvesSpec.getId(), shelvesSpec.getSpecNo());

        Integer flag = baseMapper.updateById(shelvesSpec);
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
    public ResultUtil<ShelvesSpecInfoVo> selectById(Long id) {

        ShelvesSpecInfoVo shelvesSpecInfoVo = baseMapper.getById(id);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, shelvesSpecInfoVo);
    }


    @Override
    public ResultUtil<Page<ShelvesSpecListVo>> getList(PageDto pageDto, ShelvesSpecKeyDto keyDto) {

        Page<ShelvesSpecListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<ShelvesSpecListVo> list = baseMapper.getList(page, keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    void exists(Long id, String specNo) {

        List<ShelvesSpecInfoVo> list = baseMapper.exists(id, specNo);
        if (!CollectionUtils.isEmpty(list)) {
            for (ShelvesSpecInfoVo shelvesSpecInfoVo : list) {

                if (shelvesSpecInfoVo.getSpecNo().equals(specNo))
                    throw new AplException("SPEC_NO_EXIST", "specNo已经存在");
            }
        }
    }
}
