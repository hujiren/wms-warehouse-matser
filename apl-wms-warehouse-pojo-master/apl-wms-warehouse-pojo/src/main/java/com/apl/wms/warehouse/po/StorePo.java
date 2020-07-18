package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

/**
 * <p>
 * 网店铺 持久化对象
 * </p>
 *
 * @author arran
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("store")
@ApiModel(value="网店铺 持久化对象", description="网店铺 持久化对象")
public class StorePo extends Model<StorePo> {

    @TableId(value = "id", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(name = "storeCode" , value = "编码code" , required = true)
    @NotEmpty(message = "编码code不能为空")
    private String storeCode;

    @ApiModelProperty(name = "storeName" , value = "名称" , required = true)
    @NotEmpty(message = "名称不能为空")
    private String storeName;

    @ApiModelProperty(name = "storeNameEn" , value = "英文名称" , required = true)
    @NotEmpty(message = "英文名称不能为空")
    private String storeNameEn;

    @ApiModelProperty(name = "customerId" , value = "客户id" , hidden = true)
    //@NotNull(message = "客户id不能为空")
    //@Min(value = 0 , message = "客户id不合法")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    @ApiModelProperty(name = "electricCode" , value = "电商平台代码" , required = true)
    @NotEmpty(message = "电商平台代码不能为空")
    private String electricCode;

    @ApiModelProperty(name = "storeStatus" , value = "状态 1可用  2停用" , required = true)
    @NotNull( message = "状态不能为空")
    @Range(min = 1 , max = 2, message = "状态值不合法")
    private Integer storeStatus;

    @ApiModelProperty(name = "remark" , value = "备注")
    private String remark;


    private static final long serialVersionUID=1L;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
