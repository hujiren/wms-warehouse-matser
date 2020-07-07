package com.apl.wms.warehouse.buyer.controller;

import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.wms.warehouse.po.StoreApiPo;
import com.apl.wms.warehouse.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.apl.lib.pojo.dto.PageDto;
import org.springframework.web.bind.annotation.*;
import com.apl.wms.warehouse.po.StorePo;
import com.apl.wms.warehouse.vo.StoreListVo;
import com.apl.wms.warehouse.vo.StoreInfoVo;
import com.apl.wms.warehouse.dto.StoreKeyDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.validate.ApiParamValidate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author arran
 * @since 2019-12-21
 */
@RestController
@RequestMapping("/store")
@Validated
@Api(value = "网店铺",tags = "网店铺")
@Slf4j
public class StoreController {

    @Autowired
    public StoreService storeService;

    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加", notes ="STORE_CODE_EXIST -> 店铺代码已经存在\n"+
                                         "STORE_NAME_EXIST -> 店铺名称已经存在\n"+
                                         "STORE_NAME_EN_EXIST -> 店铺英文名称已经存在")
    public ResultUtil<Integer> add(StorePo storePo) {
        storePo.setStoreCode(storePo.getStoreCode().trim().toUpperCase());
        storePo.setElectricCode(storePo.getElectricCode().trim().toUpperCase());
        ApiParamValidate.validate(storePo);

        return storeService.add(storePo);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新",  notes ="STORE_CODE_EXIST -> 店铺代码已经存在\n"+
                                            "STORE_NAME_EXIST -> 店铺名称已经存在\n"+
                                            "STORE_NAME_EN_EXIST -> 店铺英文名称已经存在")
    public ResultUtil<Boolean> updById(StorePo storePo) {
        storePo.setStoreCode(storePo.getStoreCode().trim().toUpperCase());
        storePo.setElectricCode(storePo.getElectricCode().trim().toUpperCase());
        ApiParamValidate.notEmpty("id", storePo.getId());
        ApiParamValidate.validate(storePo);

        return storeService.updById(storePo);
    }


    @PostMapping(value = "/updConfig")
    @ApiOperation(value =  "保存API参数")
    public ResultUtil<Boolean> updConfig(StoreApiPo storeApiPo) {
        ApiParamValidate.notEmpty("id", storeApiPo.getId());
        ApiParamValidate.validate(storeApiPo);

        return storeService.updConfig(storeApiPo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtil<Boolean> delById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return storeService.delById(id);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtil<StoreInfoVo> selectById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return storeService.selectById(id);
    }


    @PostMapping(value = "/get-apiconfig")
    @ApiOperation(value =  "获取API参数值" , notes = "获取API参数")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtil<StoreApiPo> getApiConfig(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();

        return storeService.getApiConfig(id, securityUser.getOuterOrgId());
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtil<Page<StoreListVo>> getList(PageDto pageDto, @Validated StoreKeyDto keyDto) {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        keyDto.setCustomerId(securityUser.getOuterOrgId());

        return storeService.getList(pageDto , keyDto);
    }

}
