package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Desc: 货架添加
 * @Author: CY
 * @Date: 2019/12/17 16:51
 */
@Data
public class StorageLocationBatchUpdDto {

    @ApiModelProperty(name = "idList" , value = "id列表 ，用逗号隔开" , required = true)
    @NotNull(message = "id列表")
    private String idList;

    @ApiModelProperty(name = "sizeLength" , value = "长" , required = true)
    @Min(value = 0 , message = "长不能小于0")
    @NotNull(message = "长不能为空")
    private BigDecimal sizeLength;

    @ApiModelProperty(name = "sizeWidth" , value = "宽" , required = true)
    @Min(value = 0 , message = "宽不能小于0")
    @NotNull(message = "宽不能为空")
    private BigDecimal sizeWidth;

    @ApiModelProperty(name = "sizeHeight" , value = "高" , required = true)
    @Min(value = 0 , message = "高不能小于0")
    @NotNull(message = "高不能为空")
    private BigDecimal sizeHeight;

    @ApiModelProperty(name = "supportWeight" , value = "承量" , required = true)
    @Min(value = 0 , message = "承量不能小于0")
    @NotNull(message = "承量不能为空")
    private Double supportWeight;

    @ApiModelProperty(name = "remark" , value = "库位描述" , required = true)
    @NotEmpty(message = "库位描述不能为空")
    private String remark;

}
