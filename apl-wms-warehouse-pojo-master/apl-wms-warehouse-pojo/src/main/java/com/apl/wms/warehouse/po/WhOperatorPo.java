package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 仓库操作员 持久化对象
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wh_operator")
@ApiModel(value="仓库操作员 持久化对象", description="仓库操作员 持久化对象")
public class WhOperatorPo extends Model<WhOperatorPo> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "memberId" , value = "操作员id" , required = true)
    @NotNull(message = "操作员id不能为空")
    @Min(value = 0 , message = "操作员id不合法")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long memberId;

    @ApiModelProperty(name = "memberName" , value = "操作员姓名" , required = true)
    @NotEmpty(message = "操作员姓名不能为空")
    private String memberName;

    @ApiModelProperty(name = "memberPosition" , value = "成员职位" , required = true)
    @NotEmpty(message = "成员职位不能为空")
    private String memberPosition;

    @ApiModelProperty(name = "memberPositionEn" , value = "成员职位英文职位" , required = true)
//    @NotEmpty(message = "成员职位英文职位不能为空")
    private String memberPositionEn;

    @ApiModelProperty(name = "whId" , value = "所属的仓库id" , required = true)
    @NotNull(message = "所属的仓库id不能为空")
    @Min(value = 0 , message = "所属的仓库id不合法")
    private Long whId;


    private static final long serialVersionUID=1L;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
