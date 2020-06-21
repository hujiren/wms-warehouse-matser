package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品品牌
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("commodity_brand")
@ApiModel(value="CommodityBrandPo对象", description="商品品牌")
public class CommodityBrandPo extends Model<CommodityBrandPo> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "brandName" , value = "品牌名称" , required = true)
    @NotEmpty(message = "品牌名称不能为空")
    private String brandName;

    @ApiModelProperty(name = "brandNameEn" , value = "品牌的英文名称" , required = true)
    @NotEmpty(message = "品牌的英文名称不能为空")
    private String brandNameEn;

    @ApiModelProperty(name = "customerId" , value = "客户id" , required = true)
    @Min(value = 0 , message = "品牌所属的外部组织id不能小于0")
    @NotNull
    private Long customerId;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
