package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 仓库详细
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WhDetailsKeyDto对象", description="仓库详细")
public class WhDetailsKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }
}
