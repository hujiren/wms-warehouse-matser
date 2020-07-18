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
import java.io.Serializable;

/**
 * <p>
 * 商品图片
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("commodity_pic")
@ApiModel(value="CommodityPicPo对象", description="商品图片")
public class CommodityPicPo extends Model<CommodityPicPo> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(name = "commodityId" , value = "商品id" , required = true)
    @Min(value = 0 , message = "商品id不能小于0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commodityId;

    @ApiModelProperty(name = "imgUrl" , value = "图片地址" , required = true)
    @NotEmpty(message = "图片地址不能为空")
    private String imgUrl;

    @ApiModelProperty(name = "imgSort" , value = "排序")
    @Min(value = 0 , message = "排序不能小于0")
    private Integer imgSort;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
