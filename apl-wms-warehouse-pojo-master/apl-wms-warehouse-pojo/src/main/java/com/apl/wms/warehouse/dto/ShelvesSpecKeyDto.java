package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * <p>
 * 货架规格 查询参数
 * </p>
 *
 * @author cy
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="货架规格 查询参数", description="货架规格 查询参数")
public class ShelvesSpecKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }
}
