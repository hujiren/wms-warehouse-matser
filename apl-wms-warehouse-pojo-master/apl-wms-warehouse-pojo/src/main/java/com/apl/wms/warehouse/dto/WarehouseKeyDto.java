package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 仓库
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "WarehouseKeyDto对象", description = "仓库")
public class WarehouseKeyDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "whType", value = "类型")
    private Integer whType;

    @ApiModelProperty(name = "whStatus", value = "状态  1启用  2停用", required = true)
    @NotNull(message = "状态不能为空")
    @Range(min = 1, max = 2, message = "状态值不合法")
    private Integer whStatus;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }

    public Integer getWhType() {
        if (whType != null && whType == -1)
            whType = null;

        return whType;
    }

    public Integer getWhStatus() {
        if (whStatus != null && whStatus == -1)
            whStatus = null;

        return whStatus;
    }
}
