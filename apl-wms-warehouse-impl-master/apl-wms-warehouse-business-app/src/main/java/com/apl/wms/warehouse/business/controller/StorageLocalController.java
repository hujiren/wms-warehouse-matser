package com.apl.wms.warehouse.business.controller;


import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.validate.ApiParamValidate;
import com.apl.wms.warehouse.dto.StorageLocationAddDto;
import com.apl.wms.warehouse.dto.StorageLocationBatchUpdDto;
import com.apl.wms.warehouse.dto.StorageLocationKeyDto;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalInfoVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import com.apl.wms.warehouse.service.StorageLocalService;
import com.apl.wms.warehouse.vo.StorageLocalListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cy
 * @since 2019-12-17
 */
@RestController
@RequestMapping("/storage-local")
@Validated
@Api(value = "库位",tags = "库位")
@Slf4j
public class StorageLocalController {

    @Autowired
    public StorageLocalService storageLocalService;

    @PostMapping(value = "/add")
    @ApiOperation(value =  "批量添加库位添加", notes ="批量添加库位添加")
    public ResultUtil<Map> add(StorageLocationAddDto storageLocationAddDto) {
        ApiParamValidate.validate(storageLocationAddDto);

        return storageLocalService.add(storageLocationAddDto);
    }


    @PostMapping(value = "/upd")
    @ApiOperation(value =  "更新",  notes ="STORAGE_NO_EXIST -> 编号已经存在")
    public ResultUtil<Boolean> updById(StorageLocalPo storageLocalPo) {
        ApiParamValidate.notEmpty("id", storageLocalPo.getId());
        ApiParamValidate.validate(storageLocalPo);

        return storageLocalService.updById(storageLocalPo);
    }


    @PostMapping(value = "/batch-upd")
    @ApiOperation(value =  "批量更新",  notes ="STORAGE_NO_EXIST -> 编号已经存在")
    public ResultUtil<Boolean> batchUpdate(@Validated StorageLocationBatchUpdDto storageLocationBatchUpdDto) {

        return storageLocalService.batchUpdate(storageLocationBatchUpdDto);
    }


    @PostMapping(value = "/del")
    @ApiOperation(value =  "删除" , notes = "删除库位，如果库位存在商品 ，删除失败")
    @ApiImplicitParam(name = "id",value = " id",required = true  , paramType = "query")
    public ResultUtil<Boolean> delById(@NotNull(message = "id不能为空") @Min(value = 1 , message = "id不能小于1") Long id) {

        return storageLocalService.delById(id);
    }


    @PostMapping(value = "/get-storage-local-details")
    @ApiOperation(value =  "获取库位详情详细" , notes = "获取库位详情详细")
    @ApiImplicitParam(name = "storageLocalSn",value = "storageLocalSn",required = true  , paramType = "query")
    public ResultUtil<StorageLocalInfoVo> getStorageLocalBySn(@NotEmpty(message = "id不能为空") String storageLocalSn) {

        return storageLocalService.getStorageLocalBySn(storageLocalSn);
    }




    @PostMapping("/get-list")
    @ApiOperation(value =  "分页查找" , notes = "分页查找")
    public ResultUtil<Page<StorageLocalListVo>> getList(PageDto pageDto, @Validated StorageLocationKeyDto keyDto) {

        return storageLocalService.getList(pageDto , keyDto);
    }


    @PostMapping("/print")
    @ApiOperation(value =  "打印条形码" , notes = "")
    @ApiImplicitParam(name = "id",value = " 库位id列表，多个id用逗号隔开",required = true  , paramType = "query")
    public void print(@NotNull(message = "库位id不能为空") String id)  throws Exception{

        ApiParamValidate.notEmpty("id",id);

        storageLocalService.print(id);
    }

    @PostMapping("/allocation-storage")
    @ApiOperation(value =  "分配多个库位" , notes = "分配多个库位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodityId",value = "商品id",required = true  , paramType = "query"),
            @ApiImplicitParam(name = "count",value = "商品数量",required = true  , paramType = "query"),
            @ApiImplicitParam(name = "storageLocal",value = "库位名称",required = true  , paramType = "query")
    })
    public ResultUtil<List<StorageLocalInfoVo>> allocationStorageLocal(
                               @NotNull(message = "commodityId不能为空") @Min(value = 0 ,message = "id不能小于零") Long commodityId,
                               @NotNull(message = "commodityId不能为空") @Min(value = 0 ,message = "数量不能小于零") Integer count,
                               @NotEmpty(message = "storageLocal不能为空") String storageLocal)throws Exception{

        return storageLocalService.allocationStorageLocal(commodityId , count , storageLocal);
    }


    @PostMapping("/manual-allocation")
    @ApiOperation(value =  "分配单个库位" , notes = "分配单个库位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodityId",value = "商品id",required = true  , paramType = "query"),
            @ApiImplicitParam(name = "storageLocalSn",value = "库位编号",required = true  , paramType = "query")
    })

    public ResultUtil<StorageLocalInfoVo> allocationOneStorageLocal(@Min(value = 1 , message = "商品id不能小于零") @NotNull(message = "商品id不能为空") Long commodityId,
                                                                     @NotEmpty(message = "storageLocalSn") String storageLocalSn){

        return storageLocalService.allocationOneStorageLocal(commodityId , storageLocalSn);
    }


    @PostMapping("/change-status")
    @ApiOperation(value =  "修改库位状态" , notes = "修改库位状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lockIds",value = "lockIds 修改为锁定状态",required = true  , paramType = "query"),
            @ApiImplicitParam(name = "unLockIds",value = "unLockIds 修改为解锁状态",required = true  , paramType = "query")
    })
    public ResultUtil<Boolean> changeStorageLocalStatus(@NotEmpty(message = "lockIds不能为空")String lockIds ,
                                                         @NotEmpty(message = "ubLockIds不能为空") String unLockIds){

        return storageLocalService.changeStorageLocalStatus(lockIds , unLockIds);
    }

}
