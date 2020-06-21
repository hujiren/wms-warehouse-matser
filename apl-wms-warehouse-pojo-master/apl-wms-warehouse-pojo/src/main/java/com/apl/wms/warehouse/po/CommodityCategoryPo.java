package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 商品种类
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("commodity_category")
@ApiModel(value="CommodityCategoryPo对象", description="商品种类")
public class CommodityCategoryPo extends Model<CommodityCategoryPo> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "categoryName" , value = "商品分类名称" , required = true)
    @NotEmpty(message = "商品分类名称不能为空")
    private String categoryName;

    @ApiModelProperty(name = "categoryNameEn" , value = "商品分类英文名称" , required = true)
    @NotEmpty(message = "商品分类英文名称不能为空")
    private String categoryNameEn;

    @ApiModelProperty(name = "parentId" , value = "商品分类的父级id" , required = true)
    @Min(value = 0 , message = "商品分类的父级id不能小于0")
    private Long parentId;

    @ApiModelProperty(name = "numberOfPlies" , value = "菜单层级数" , required = true)
    @Min(value = 0 , message = "菜单层级数不能小于0")
    private Integer numberOfPlies;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
