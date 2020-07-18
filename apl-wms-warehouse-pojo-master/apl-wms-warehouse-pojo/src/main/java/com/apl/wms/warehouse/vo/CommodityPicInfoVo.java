package com.apl.wms.warehouse.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class CommodityPicInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 图片id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 商品id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long commodityId;

    // 图片地址
    private String imgUrl;

    // 排序
    private Integer imgSort;

}
