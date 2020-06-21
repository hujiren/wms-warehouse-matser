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
 * 网店铺 查询参数
 * </p>
 *
 * @author arran
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="网店铺 查询参数", description="网店铺 查询参数")
public class StoreKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(name = "storeStatus", value = "状态  1: 可用 2：停用", required = true)
    @NotNull(message = "状态不能为空")
    @Range(min = 1, max = 2, message = "状态值不合法")
    private Integer storeStatus;

    @ApiModelProperty(name = "electricCode", value = "电商平台代码")
    private String electricCode;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;


    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }

    public String getElectricCode() {
        if (electricCode != null && electricCode.trim().equals(""))
            electricCode = null;

        return keyword;
    }
}
