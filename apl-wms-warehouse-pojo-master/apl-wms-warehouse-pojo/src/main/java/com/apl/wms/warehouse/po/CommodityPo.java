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
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("commodity")
@ApiModel(value="CommodityPo对象", description="商品")
public class CommodityPo extends Model<CommodityPo> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(name = "sku" , value = "sku" , required = true)
    @NotEmpty(message = "sku不能为空")
    private String sku;

    @ApiModelProperty(name = "sourceArea" , value = "原产地" , required = true)
    @NotEmpty(message = "原产地 不能为空")
    private String sourceArea;

    @ApiModelProperty(name = "commodityName" , value = "商品名称" , required = true)
    @NotEmpty(message = "商品名称不能为空")
    private String commodityName;

    @ApiModelProperty(name = "commodityNameEn" , value = "商品英文名称" , required = true)
    @NotEmpty(message = "商品英文名称不能为空")
    private String commodityNameEn;

    @ApiModelProperty(name = "customerId" , value = "客户id" , required = true)
    @Min(value = 0 , message = "客户id不能小于1")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    @ApiModelProperty(name = "category1Id" , value = "品类一id" , required = true)
    @Min(value = 0 , message = "品类一id不能小于0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long category1Id;

    @ApiModelProperty(name = "category2Id" , value = "品类二id" ,hidden = true)
    @Min(value = 0 , message = "品类二id不能小于0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long category2Id;

    @ApiModelProperty(name = "category3Id" , value = "品类三d" , hidden = true)
    @Min(value = 0 , message = "品类三d不能小于0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long category3Id;

    @ApiModelProperty(name = "category4Id" , value = "品类四id" , hidden = true)
    @Min(value = 0 , message = "品类四id不能小于0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long category4Id;

    @ApiModelProperty(name = "category5Id" , value = "品类五id" , hidden = true)
    @Min(value = 0 , message = "品类五id不能小于0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long category5Id;

    @ApiModelProperty(name = "brandId" , value = "品牌id" , required = true)
    @Min(value = 0 , message = "品牌id不能小于0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long brandId;

    @ApiModelProperty(name = "saleStatus" , value = "销售状态 1:上架  2下架" , required = true)
    @NotNull(message = "销售状态不能为空")
    @Range(min = 1, max = 2, message = "销售状态值不合法")
    private Integer saleStatus;

    @ApiModelProperty(name = "reviewStatus" , value = "审核状态 1已审核  2未审核", hidden = true)
    @Range(min = 1, max = 2, message = "审核状态值不合法")
    private Integer reviewStatus;

    @ApiModelProperty(name = "unitCode" , value = "单位code" , required = true)
    @NotEmpty(message = "单位code不能为空")
    private String unitCode;


    @ApiModelProperty(name = "platformPrice" , value = "平台价格" , required = true)
    @Min(value = 0 , message = "平台价格不能小于0")
    private Double platformPrice;

    @ApiModelProperty(name = "platformCurrency" , value = "平台币制" , required = true)
    @NotEmpty(message = "平台币制不能为空")
    private String platformCurrency;

    @ApiModelProperty(name = "sizeLength" , value = "长" , required = true)
    @Min(value = 0 , message = "长不能小于0")
    private Double sizeLength;

    @ApiModelProperty(name = "sizeWidth" , value = "宽" , required = true)
    @Min(value = 0 , message = "宽不能小于0")
    private Double sizeWidth;

    @ApiModelProperty(name = "sizeHeight" , value = "高" , required = true)
    @Min(value = 0 , message = "高不能小于0")
    private Double sizeHeight;

    @ApiModelProperty(name = "weight" , value = "重量" , required = true)
    @Min(value = 0 , message = "重量不能小于0")
    private Double weight;

    @ApiModelProperty(name = "netUrl" , value = "商品url链接" , required = true)
    @NotEmpty(message = "商品url链接不能为空")
    private String netUrl;

    @ApiModelProperty(name = "declarePrice" , value = "申报价格" , required = true)
    @Min(value = 0 , message = "申报价格不能小于0")
    private Double declarePrice;

    @ApiModelProperty(name = "declareCurrency" , value = "申报币制" , required = true)
    @NotEmpty(message = "申报币制不能为空")
    private String declareCurrency;

    @ApiModelProperty(name = "imgUrl" , value = "商品的主图" , required = true)
    @NotEmpty(message = "商品的主图不能为空")
    private String imgUrl;

    @ApiModelProperty(name = "isCorrespondence" , value = "是否含电 1:含电 0：不含电" , required = true)
    @Min(value = 0 , message = "是否含电 1:含电 0：不含电不能小于0")
    private Integer isCorrespondence;

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

    @ApiModelProperty(name = "hsCode" , value = "海关编码")
    private String hsCode;

    @ApiModelProperty(name = "remark" , value = "商品备注")
    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
