package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * <p>
 * 库位储存商品数量
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("storage_local_stocks")
@ApiModel(value="StorageCommodityPo对象", description="库位储存商品数量")
public class StorageLocalStocksPo extends Model<StorageLocalStocksPo> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(name = "storageLocalId" , value = "库位id" , required = true)
    @Min(value = 0 , message = "库位id不能小于1")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storageLocalId;

    @ApiModelProperty(name = "commodityId" , value = "商品id" , required = true)
    @Min(value = 0 , message = "商品id不能小于1")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commodityId;

    @ApiModelProperty(name = "availableCount" , value = "可用库存" , required = true)
    @Min(value = 0 , message = "可用库存 不能小于0")
    private Integer availableCount;

    //@ApiModelProperty(name = "freezeCount" , value = "冻结库存" , required = true)
    //@Min(value = 0 , message = "冻结库存 不能小于0")
    //private Integer freezeCount;

    @ApiModelProperty(name = "realityCount" , value = "实际库存" , required = true)
    @Min(value = 0 , message = "实际库存 不能小于0")
    private Integer realityCount;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
