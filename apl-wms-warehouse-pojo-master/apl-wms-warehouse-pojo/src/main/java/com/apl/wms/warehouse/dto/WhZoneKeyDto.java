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
 * 仓库分区 查询参数
 * </p>
 *
 * @author apl
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="仓库分区 查询参数", description="仓库分区 查询参数")
public class WhZoneKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;

    @ApiModelProperty(name = "zoneType", value = "分区类型 1：产品区 2：入库区 3：退货区 4：中转区", required = true)
    @NotNull(message = "分区类型 1：产品区 2：入库区 3：退货区 4：中转区不能为空")
    @Min(value = -1, message = "分区类型 1：产品区 2：入库区 3：退货区 4：中转区不合法")
    private Integer zoneType;

    @ApiModelProperty(name = "whId", value = "仓库id", required = true)
    @NotNull(message = "仓库id不能为空")
    @Min(value = 1, message = "仓库id不合法")
    private Long whId;


    public String getKeyword() {
        if (keyword != null && keyword.trim().equals(""))
            keyword = null;

        return keyword;
    }
}
