package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 商品种类
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CommodityCategoryKeyDto对象", description="商品种类")
public class CommodityCategoryKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "parentId", value = "父类id", required = true)
    @NotNull(message = "parentId不能为空")
    private Integer parentId;


    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }
}
