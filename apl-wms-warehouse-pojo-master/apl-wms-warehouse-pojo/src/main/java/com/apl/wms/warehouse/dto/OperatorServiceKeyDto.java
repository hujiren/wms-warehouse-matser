package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 附加服务操作名称 查询参数
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="附加服务操作名称 查询参数", description="附加服务操作名称 查询参数")
public class OperatorServiceKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }
}
