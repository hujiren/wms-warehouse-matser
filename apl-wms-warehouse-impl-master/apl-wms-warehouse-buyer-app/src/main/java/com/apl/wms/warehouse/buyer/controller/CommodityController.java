package com.apl.wms.warehouse.buyer.controller;


import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.dto.CommodityKeyDto;
import com.apl.wms.warehouse.po.CommodityPo;
import com.apl.wms.warehouse.service.CommodityService;
import com.apl.wms.warehouse.vo.CommodityInfoVo;
import com.apl.wms.warehouse.vo.CommodityListVo;
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
 * @since 2019-12-11
 */
@RestController
@RequestMapping("/commodity")
@Validated
@Api(value = "商品",tags = "商品")
@Slf4j
public class CommodityController {

    @Autowired
    public CommodityService commodityService;

    @PostMapping(value = "/add")
    @ApiOperation(value =  "添加" , notes = "添加 ")
    public ResultUtils<Long> add(@Validated CommodityPo commodity) {

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        commodity.setCustomerId(securityUser.getOuterOrgId());

        return commodityService.add(commodity);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新" , notes = "更新")
    public ResultUtils<Boolean> updById(@Validated CommodityPo commodityPo) {

        ApiParamValidate.notEmpty("id", commodityPo.getId());
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        commodityPo.setCustomerId(securityUser.getOuterOrgId());

        return commodityService.updById(commodityPo);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtils<Boolean> delById(@Min(value = 1 , message = "id不能小于1") Long id) {
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();

        return commodityService.delById(id, securityUser.getOuterOrgId());
    }


    @PostMapping(value = "/get")
    @ApiOperation(value =  "获取详细" , notes = "获取详细")
    @ApiImplicitParam(name = "id",value = "id",required = true  , paramType = "query")
    public ResultUtils<CommodityInfoVo> getById(@Min(value = 1 , message = "id不能小于 1") Long id) {
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();

        return commodityService.selectById(id, securityUser.getOuterOrgId());
    }


    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtils<Page<CommodityListVo>> getList(PageDto pageDto, @Validated CommodityKeyDto keyDto)  throws Exception{

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        keyDto.setCustomerId(securityUser.getOuterOrgId());

        return commodityService.getList(pageDto , keyDto);
    }


    @PostMapping("/print")
    @ApiOperation(value =  "打印条形码" , notes = "")
    @ApiImplicitParam(name = "id",value = " 商品id列表，多个id用逗号隔开",required = true  , paramType = "query")
    public void print(@NotNull(message = "商品id 不能为空") String id)  throws Exception{
        ApiParamValidate.notEmpty("id",id);

        SecurityUser securityUser = CommonContextHolder.getSecurityUser();

        commodityService.print(id, securityUser.getOuterOrgId());
    }

}
