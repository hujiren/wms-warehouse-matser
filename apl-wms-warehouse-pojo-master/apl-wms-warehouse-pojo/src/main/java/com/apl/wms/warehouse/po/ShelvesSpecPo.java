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
 * 货架规格 持久化对象
 * </p>
 *
 * @author cy
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("shelves_spec")
@ApiModel(value = "货架规格 持久化对象", description = "货架规格 持久化对象")
public class ShelvesSpecPo extends Model<ShelvesSpecPo> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "specNo", value = "规格编号", required = true)
    @NotEmpty(message = "规格编号不能为空")
    private String specNo;

    @ApiModelProperty(name = "singleLayersLength", value = "单层-长", required = true)
    @NotNull(message = "单层-长不能为空")
    @Min(value = 0, message = "单层-长不合法")
    private BigDecimal singleLayersLength;

    @ApiModelProperty(name = "singleLayersWidth", value = "单层-宽", required = true)
    @NotNull(message = "单层-宽不能为空")
    @Min(value = 0, message = "单层-宽不合法")
    private BigDecimal singleLayersWidth;

    @ApiModelProperty(name = "singleLayersHeight", value = "单层-高", required = true)
    @NotNull(message = "单层-高不能为空")
    @Min(value = 0, message = "单层-高不合法")
    private BigDecimal singleLayersHeight;

    @ApiModelProperty(name = "layersCount", value = "层数", required = true)
    @NotNull(message = "层数不能为空")
    @Min(value = 0, message = "层数不合法")
    private Integer layersCount;

    @ApiModelProperty(name = "singleLayerSupportWeight", value = "每层承受重量", required = true)
    private Double singleLayerSupportWeight;

    @ApiModelProperty(name = "remark", value = "描述")//required = true
//    @NotEmpty(message = "描述不能为空")
    private String remark;

    @ApiModelProperty(name = "imgUrl", value = "货架规格图片url")// required = true
//    @NotEmpty(message = "货架规格图片url不能为空")
    private String imgUrl;


    private static final long serialVersionUID = 1L;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
