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
 * 货架 持久化对象
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wh_shelves")
@ApiModel(value="货架 持久化对象", description="货架 持久化对象")
public class WhShelvesPo extends Model<WhShelvesPo> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "shelvesNo" , value = "货架编号" , required = true)
    @NotEmpty(message = "货架编号不能为空")
    private String shelvesNo;

    @ApiModelProperty(name = "shelvesSpecId" , value = "货架规格id" , required = true)
    @NotNull(message = "货架规格id不能为空")
    @Min(value = 0 , message = "货架规格id不合法")
    private Long shelvesSpecId;

    @ApiModelProperty(name = "whZoneId" , value = "仓库区域id" , required = true)
    @NotNull(message = "仓库区域id不能为空")
    @Min(value = 0 , message = "仓库区域id不合法")
    private Long whZoneId;

    @ApiModelProperty(name = "remark" , value = "备注" , required = true)
    private String remark;


    private static final long serialVersionUID=1L;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
