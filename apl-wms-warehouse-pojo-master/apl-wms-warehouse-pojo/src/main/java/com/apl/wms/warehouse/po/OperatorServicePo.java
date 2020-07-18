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

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 附加服务操作名称 持久化对象
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("operator_service")
@ApiModel(value="附加服务操作名称 持久化对象", description="附加服务操作名称 持久化对象")
public class OperatorServicePo extends Model<OperatorServicePo> {


    @TableId(value = "id", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(name = "serviceName" , value = "服务名称" , required = true)
    @NotEmpty(message = "服务名称不能为空")
    private String serviceName;

    @ApiModelProperty(name = "serviceNameEn" , value = "服务英文名称" , required = true)
    @NotEmpty(message = "服务英文名称不能为空")
    private String serviceNameEn;


    private static final long serialVersionUID=1L;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
