package com.apl.wms.warehouse.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class StockUpdBo implements Serializable {

    @ApiModelProperty(name = "whId", value = "创建id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long whId;

    @ApiModelProperty(name = "commodityId", value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commodityId;

    @ApiModelProperty(name = "storageLocalId", value = "库位id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storageLocalId;

    @ApiModelProperty(name = "qty", value = "数量")
    private Integer qty;

    //private String token;

    //private List<CountDto> countDtos;

}
