package com.apl.wms.warehouse.bo;

import com.apl.wms.warehouse.po.StocksPo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * <p>
 * 库存
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stocks")
@ApiModel(value="StocksPo对象", description="库存")
public class StocksBo extends Model<StocksPo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "whId" , value = "仓库id" , required = true)
    @Min(value = 0 , message = "仓库id不能小于1")
    private Long whId;

    @ApiModelProperty(name = "commodityId" , value = "商品id" , required = true)
    @Min(value = 0 , message = "商品id不能小于1")
    private Long commodityId;

    @ApiModelProperty(name = "commodityKey" , value = "商品key" , required = true)
    @Min(value = 0 , message = "商品key不能小于1")
    private String commodityKey;

    @ApiModelProperty(name = "availableStockCount" , value = "可用库存" , required = true)
    @Min(value = 0 , message = "可用库存 不能小于0")
    private Integer availableStockCount;

    @ApiModelProperty(name = "freezeStockCount" , value = "冻结库存" , required = true)
    @Min(value = 0 , message = "冻结库存 不能小于0")
    private Integer freezeStockCount;

    @ApiModelProperty(name = "allStockCount" , value = "总库存" , required = true)
    @Min(value = 0 , message = "总库存 不能小于0")
    private Integer allStockCount;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
