package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 仓库
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("warehouse")
@ApiModel(value = "WarehousePo对象", description = "仓库")
public class WarehousePo extends Model<WarehousePo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "whName", value = "仓库名称", required = true)
    @NotEmpty(message = "仓库名称 不能为空")
    private String whName;

    @ApiModelProperty(name = "whNameEn", value = "仓库英文名称", required = true)
    @NotEmpty(message = "仓库英文名称 不能为空")
    private String whNameEn;

    @ApiModelProperty(name = "whCode", value = "仓库代码", required = true)
    @NotEmpty(message = "仓库代码 不能为空")
    private String whCode;

    @ApiModelProperty(name = "whStatus", value = "仓库状态 1可用  2不可用", required = true)
    @Range(min = 1, max = 2, message = "仓库状态不合法")
    private Integer whStatus;


    @ApiModelProperty(name = "whType", value = "仓库类型 1：保税仓   2普通仓", required = true)
    @Range(min = 1, max = 2,message = "仓库类型 1：保税仓   2普通仓 不能小于0")
    private Integer whType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
