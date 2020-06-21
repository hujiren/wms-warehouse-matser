package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 货架 查询参数
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="货架 查询参数", description="货架 查询参数")
public class WhShelvesKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "whZoneId" , value = "仓库区域id" , required = true)
    @NotNull(message = "仓库区域id不能为空")
    @Min(value = 1 , message = "仓库区域id不合法")
    private Long whZoneId;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }

    public Long getWhZoneId() {
        return whZoneId;
    }
}
