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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 包装材料 持久化对象
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("packaging_materials")
@ApiModel(value="包装材料 持久化对象", description="包装材料 持久化对象")
public class PackagingMaterialsPo extends Model<PackagingMaterialsPo> {

    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(name = "id" , hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(name = "sku" , value = "sku" , required = true)
    @NotEmpty(message = "sku不能为空")
    private String sku;

    @ApiModelProperty(name = "commodityId" , value = "commodityId 商品id" , required = true)
    @NotNull(message = "commodityId 不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commodityId;

    @ApiModelProperty(name = "commodityName" , value = "商品名称" , required = true)
    @NotEmpty(message = "商品名称不能为空")
    private String commodityName;

    @ApiModelProperty(name = "commodityNameEn" , value = "商品英文名称" , required = true)
    @NotEmpty(message = "商品英文名称不能为空")
    private String commodityNameEn;

    @ApiModelProperty(name = "saleStatus" , value = "销售状态 1:上架  2下架" , hidden = true)
    private Integer saleStatus;

    @ApiModelProperty(name = "reviewStatus" , value = "审核状态 1已审核  2未审核", hidden = true)
    private Integer reviewStatus;

    @ApiModelProperty(name = "unitCode" , value = "单位code" , required = true)
    @NotEmpty(message = "单位code不能为空")
    private String unitCode;


    @ApiModelProperty(name = "platformPrice" , value = "平台价格" , hidden = true)
    private BigDecimal platformPrice;

    @ApiModelProperty(name = "platformCurrency" , value = "平台币制" , hidden = true)
    private String platformCurrency;

    @ApiModelProperty(name = "sizeLength" , value = "长" , required = true)
    @Min(value = 0 , message = "长不能小于0")
    private BigDecimal sizeLength;

    @ApiModelProperty(name = "sizeWidth" , value = "宽" , required = true)
    @Min(value = 0 , message = "宽不能小于0")
    private BigDecimal sizeWidth;

    @ApiModelProperty(name = "sizeHeight" , value = "高" , required = true)
    @Min(value = 0 , message = "高不能小于0")
    private BigDecimal sizeHeight;

    @ApiModelProperty(name = "weight" , value = "重量" , required = true)
    @Min(value = 0 , message = "重量不能小于0")
    private BigDecimal weight;

    @ApiModelProperty(name = "netUrl" , value = "商品url链接" , required = true)
    @NotEmpty(message = "商品url链接不能为空")
    private String netUrl;

    @ApiModelProperty(name = "declarePrice" , value = "申报价格" , hidden = true)
    private BigDecimal declarePrice;

    @ApiModelProperty(name = "declareCurrency" , value = "申报币制" , hidden = true)
    private String declareCurrency;

    @ApiModelProperty(name = "imgUrl" , value = "商品的主图" , required = true)
    @NotEmpty(message = "商品的主图不能为空")
    private String imgUrl;

    @ApiModelProperty(name = "textureName" , value = "材质" , required = true)
    @NotEmpty(message = "材质不能为空")
    private String textureName;

    @ApiModelProperty(name = "textureNameEn" , value = "材质英文名称" , required = true)
    @NotEmpty(message = "材质英文名称不能为空")
    private String textureNameEn;

    @ApiModelProperty(name = "colorName" , value = "颜色名称")
    //@NotEmpty(message = "颜色名称不能为空")
    private String colorName;

    @ApiModelProperty(name = "colorNameEn" , value = "颜色英文名称")
    //@NotEmpty(message = "颜色英文名称不能为空")
    private String colorNameEn;

    @ApiModelProperty(name = "specName" , value = "规格" , required = true)
    @NotEmpty(message = "规格不能为空")
    private String specName;

    @ApiModelProperty(name = "specNameEn" , value = "规格英文名称" , required = true)
    @NotEmpty(message = "规格英文名称不能为空")
    private String specNameEn;

    @ApiModelProperty(name = "useWay" , value = "用途" , required = true)
    @NotEmpty(message = "用途不能为空")
    private String useWay;


    @ApiModelProperty(name = "useWayEn" , value = "用途英文名称" , required = true)
    @NotEmpty(message = "用途英文名称不能为空")
    private String useWayEn;

    @ApiModelProperty(name = "capacity" , value = "包装材料容量", required = true)
    private Integer capacity;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
