package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * <p>
 * 品名库 查询参数
 * </p>
 *
 * @author apl
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="品名库 查询参数", description="品名库 查询参数")
public class CommodityNameLibKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "customerId", value = "客户id")
    @Range(min = 0, message = "客户id不能小于0")
    private String customerId;

    @ApiModelProperty(name = "categoryId", value = "品类id")
    @Range(min = 0, message = "客户id不能小于0")
    private Integer categoryId;

    @ApiModelProperty(name = "isCategory", value = "归类状态")
    @Range(min = 0, message = "归类状态不能小于0")
    private Integer isCategory;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }
}
