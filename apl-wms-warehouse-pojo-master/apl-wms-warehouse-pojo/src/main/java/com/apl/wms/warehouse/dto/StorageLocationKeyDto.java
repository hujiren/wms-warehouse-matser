package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 库位
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="StorageLocationKeyDto对象", description="库位")
public class StorageLocationKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "shelvesId", value = "货架id", required = true)
    @NotNull(message = "货架id不能为空")
    Long shelvesId;

    @ApiModelProperty(name = "storageLayer", value = "所在层数")
    Integer storageLayer;

    @ApiModelProperty(name = "keyword", value = "关键词")
    private String keyword;


    public String getKeyword() {
        if (keyword == null || StringUtils.isEmpty(keyword.trim())){
            return  null;
        }

        return keyword;
    }
}
