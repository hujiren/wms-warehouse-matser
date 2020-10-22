package com.apl.wms.warehouse.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 库存
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@ApiModel(value = "库存分页-返回对象", description = "库存分页-返回对象")
public class StocksListVo implements Serializable {

private static final long serialVersionUID=1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(name = "id",value = "库存id")
    private Long id;

    @ApiModelProperty(name = "sku", value = "商品品名 ")
    private String sku;

    @ApiModelProperty(name = "commodityId", value = "商品Id")
    private String commodityId;

    @ApiModelProperty(name = "commodityName", value = "商品名称")
    private String commodityName;

    @ApiModelProperty(name = "commodityNameEn", value = "商品英文名")
    private String commodityNameEn;

    @ApiModelProperty(name = "specName", value = "规格")
    private String specName;

    @ApiModelProperty(name = "imgUrl", value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(name = "isCorrespondence", value = "是否带电")
    private Integer isCorrespondence;

    @ApiModelProperty(name = "catName", value = "")
    private String catName;

    @ApiModelProperty(name = "categoryName", value = "品类名称")
    private String categoryName;

    @ApiModelProperty(name = "categoryNameEn", value = "品类英文名")
    private String categoryNameEn;

    @ApiModelProperty(name = "whId", value = "仓库id")
    private Long whId;

    @ApiModelProperty(name = "availableCount", value = "可售库存 ")
    private Integer availableCount;

    @ApiModelProperty(name = "realityCount", value = "实际库存")
    private Integer realityCount;

}
