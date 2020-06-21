package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品图片
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CommodityPicKeyDto对象", description="商品图片")
public class CommodityPicAddDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "communityId" , value = "商品id" , required = true)
    @Min(value = 0 , message = "商品id不能小于0")
    private Long commodityId;

    @ApiModelProperty(name = "communityId" , value = "商品id" , required = true , allowableValues = "true")
    private List<PicItemDto> picItemDtos;
}
