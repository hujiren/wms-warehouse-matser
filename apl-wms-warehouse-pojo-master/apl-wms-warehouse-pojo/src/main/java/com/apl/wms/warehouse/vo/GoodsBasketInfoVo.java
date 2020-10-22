package com.apl.wms.warehouse.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 货蓝 详细实体
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Data
@ApiModel(value = "货篮-返回对象", description = "货篮-返回对象")
public class GoodsBasketInfoVo implements Serializable {


private static final long serialVersionUID=1L;

    // 货蓝id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "id", value = "货篮id")
    private Long id;

    // 篮子编号
    @ApiModelProperty(name = "basketSn", value = "货篮编号")
    private String basketSn;

    // 描述
    @ApiModelProperty(name = "remark", value = "备注")
    private String remark;

}
