package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 货蓝 持久化对象
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("goods_basket")
@ApiModel(value="货蓝 持久化对象", description="货蓝 持久化对象")
public class GoodsBasketPo extends Model<GoodsBasketPo> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "basketSn" , value = "篮子编号" , required = true)
    @NotEmpty(message = "篮子编号不能为空")
    private String basketSn;

    @ApiModelProperty(name = "remark" , value = "描述" , required = true)
    @NotEmpty(message = "描述不能为空")
    private String remark;


    private static final long serialVersionUID=1L;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
