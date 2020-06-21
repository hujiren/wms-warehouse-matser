package com.apl.wms.warehouse.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 品名库 持久化对象
 * </p>
 *
 * @author apl
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("commodity_name_lib")
@ApiModel(value="品名库 持久化对象", description="品名库 持久化对象")
public class CommodityNameLibPo extends Model<CommodityNameLibPo> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "commodityName" , value = "品名" , required = true)
    @NotEmpty(message = "品名不能为空")
    private String commodityName;

    @ApiModelProperty(name = "commodityNameEn" , value = "英文名" , required = true)
    @NotEmpty(message = "英文名不能为空")
    private String commodityNameEn;

    @ApiModelProperty(name = "sku" , value = "sku" )
    private String sku;

    @ApiModelProperty(name = "customerId" , value = "客户" , required = true)
    @NotNull(message = "客户不能为空")
    @Min(value = 0 , message = "客户id不合法")
    private Long customerId;

    @ApiModelProperty(name = "categoryId" , value = "品类id" , required = true)
    @NotNull(message = "品类id不能为空")
    @Min(value = 0 , message = "品类id不合法")
    private Integer categoryId;

    @ApiModelProperty(name = "declarePrice" , value = "申报价值")
    @Min(value = 0 , message = "申报价值不合法")
    private BigDecimal declarePrice;

    @ApiModelProperty(name = "declareCurrency" , value = "申报币制")
    private String declareCurrency;

    private static final long serialVersionUID=1L;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
