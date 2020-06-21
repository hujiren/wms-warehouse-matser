package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
public class StorageLocationAddDto {

    @ApiModelProperty(name = "shelvesId" , value = "货架id" , required = true)
    @NotNull(message = "货架id不能为空")
    @Min(value = 1 , message = "货架id不能小于1")
    private Long shelvesId;

    @ApiModelProperty(name = "storageLayer" , value = "所在层数" , required = true)
    @NotNull(message = "所在层数不能为空")
    @Min(value = 1 , message = "所在层数不能小于1")
    private Integer storageLayer;

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


    @ApiModelProperty(name = "firstSn" , value = "起始编号" , required = true)
    @NotNull(message = "起始编号不能为空")
    @Length(min=6, max=30,  message = "起始编号长度必须是6-30位，且后3位必须为纯数字")
    private String firstSn;

    @ApiModelProperty(name = "count" , value = "数量" , required = true)
    @NotNull(message = "数量不能为空")
    @Min(value = 1 , message = "数量不能小于1")
    private Integer count;

    @ApiModelProperty(name = "remark" , value = "库位描述" , required = true)
    @NotEmpty(message = "库位描述不能为空")
    private String remark;

}
