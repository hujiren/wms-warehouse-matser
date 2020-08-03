package com.apl.wms.warehouse.lib.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author hjr start
 * @date 2020/8/1 - 13:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StocksVo implements Serializable {


    private static final long serialVersionUID = -6044949101567714516L;
    @ApiModelProperty(name = "id" , value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(name = "whId" , value = "仓库id")
    private Long whId;

    @ApiModelProperty(name = "commodityId" , value = "商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commodityId;

    @ApiModelProperty(name = "availableCount" , value = "可售库存")
    private Integer availableCount;

    @ApiModelProperty(name = "realityCount" , value = "实际库存")
    private Integer realityCount;
}
