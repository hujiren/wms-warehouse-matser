package com.apl.wms.warehouse.po;

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
 * 仓库分区 持久化对象
 * </p>
 *
 * @author apl
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wh_zone")
@ApiModel(value = "仓库分区 持久化对象", description = "仓库分区 持久化对象")
public class WhZonePo extends Model<WhZonePo> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "whId", value = "仓库id", required = true)
    @NotNull(message = "仓库id不能为空")
    @Min(value = 1, message = "仓库id不合法")
    private Long whId;

    @ApiModelProperty(name = "zoneCode", value = "分区编号code", required = true)
    @NotEmpty(message = "分区编号code不能为空")
    private String zoneCode;

    @ApiModelProperty(name = "zoneName", value = "分区名称", required = true)
    @NotEmpty(message = "分区名称不能为空")
    private String zoneName;

    @ApiModelProperty(name = "zoneNameEn", value = "仓库英文名", required = true)
    @NotEmpty(message = "仓库英文名不能为空")
    private String zoneNameEn;

    @ApiModelProperty(name = "zoneType", value = "分区类型 1：产品区 2：入库区 3：退货区 4：中转区", required = true)
    @NotNull(message = "分区类型 1：产品区 2：入库区 3：退货区 4：中转区不能为空")
    @Min(value = 0, message = "分区类型 1：产品区 2：入库区 3：退货区 4：中转区不合法")
    private Integer zoneType;


    private static final long serialVersionUID = 1L;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
