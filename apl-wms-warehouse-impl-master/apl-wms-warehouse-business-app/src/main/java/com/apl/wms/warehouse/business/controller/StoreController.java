package com.apl.wms.warehouse.business.controller;

import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.dto.StoreKeyDto;
import com.apl.wms.warehouse.service.StoreService;
import com.apl.wms.warehouse.vo.StoreListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<StoreListVo>> getList(PageDto pageDto, @Validated StoreKeyDto keyDto) {

        ApiParamValidate.thanZero("customerId", keyDto.getCustomerId());

        return storeService.getList(pageDto , keyDto);
    }

    @PostMapping(value = "/feign/apiconfig-strval")
    @ApiOperation(value =  "获取API参数值" , notes = "获取API参数")
    @ApiImplicitParam(name = "id",value = "id", required = true  , paramType = "query")
    public ResultUtils<String> getApiConfigStrVal(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return storeService.getApiConfigStrVal(id, 0l);
    }

}
