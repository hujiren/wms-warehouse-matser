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
 * 仓库操作员 查询参数
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "仓库操作员 查询参数", description = "仓库操作员 查询参数")
public class WhOperatorKeyDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    @ApiModelProperty(name = "whId", value = "所属的仓库id", required = true)
    @NotNull(message = "所属的仓库id不能为空")
    @Min(value = 0, message = "所属的仓库id不合法")
    private Long whId;

    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }

    public Long getWhId() {
        return whId;
    }
}
