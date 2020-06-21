package com.apl.wms.warehouse.business.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.dto.OperatorServiceKeyDto;
import com.apl.wms.warehouse.po.OperatorServicePo;
import com.apl.wms.warehouse.service.OperatorServiceService;
import com.apl.wms.warehouse.vo.OperatorServiceInfoVo;
import com.apl.wms.warehouse.vo.OperatorServiceListVo;
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
 * @author cy
 * @since 2019-12-17
 */
@RestController
@RequestMapping("/operator-service")
@Validated
@Api(value = "附加服务操作名称",tags = "附加服务操作名称")
@Slf4j
public class OperatorServiceController {

    @Autowired
    public OperatorServiceService operatorServiceService;


    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加", notes ="")
    public ResultUtils<Integer> add(OperatorServicePo operatorServicePo) {
        ApiParamValidate.validate(operatorServicePo);

        return operatorServiceService.add(operatorServicePo);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新",  notes ="")
    public ResultUtils<Boolean> updById(OperatorServicePo operatorServicePo) {
        ApiParamValidate.notEmpty("id", operatorServicePo.getId());
        ApiParamValidate.validate(operatorServicePo);

        return operatorServiceService.updById(operatorServicePo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtils<Boolean> delById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return operatorServiceService.delById(id);
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtils<OperatorServiceInfoVo> selectById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return operatorServiceService.selectById(id);
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<OperatorServiceListVo>> getList(PageDto pageDto, @Validated OperatorServiceKeyDto keyDto) {

        return operatorServiceService.getList(pageDto , keyDto);
    }

}
