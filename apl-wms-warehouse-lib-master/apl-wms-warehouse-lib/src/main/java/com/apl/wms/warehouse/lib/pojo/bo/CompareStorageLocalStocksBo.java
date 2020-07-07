package com.apl.wms.warehouse.lib.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author hjr start
 * @date 2020/7/7 - 10:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
public class CompareStorageLocalStocksBo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "storageLocalId" , value = "库位id" , required = true)
    @Min(value = 0 , message = "库位id不能小于1")
    private Long storageLocalId;

    @ApiModelProperty(name = "commodityId" , value = "商品id" , required = true)
    @Min(value = 0 , message = "商品id不能小于1")
    private Long commodityId;

    @ApiModelProperty(name = "availableCount" , value = "可用库存" , required = true)
    @Min(value = 0 , message = "可用库存 不能小于0")
    private Integer availableCount;

    @ApiModelProperty(name = "freezeCount" , value = "冻结库存" , required = true)
    @Min(value = 0 , message = "冻结库存 不能小于0")
    private Integer freezeCount;

    @ApiModelProperty(name = "realityCount" , value = "实际库存" , required = true)
    @Min(value = 0 , message = "实际库存 不能小于0")
    private Integer realityCount;

    //分配出去的库位库存数量  pull_qty
    public Integer allocationQty;
}
