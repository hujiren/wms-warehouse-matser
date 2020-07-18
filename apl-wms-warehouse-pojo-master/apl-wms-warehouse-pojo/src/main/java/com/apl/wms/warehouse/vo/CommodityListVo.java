package com.apl.wms.warehouse.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommodityListVo implements Serializable {

private static final long serialVersionUID=1L;

    // 商品id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // sku
    private String sku;

    // 客户id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    // 客户名称
    private String customerName;

    // 商品名称
    private String commodityName;

    // 商品英文名称
    private String commodityNameEn;

    // 品类id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;

    // 品类分类名称
    private String categoryName;

    // 规格名称
    private String specName;

    //颜色中文名称
    private String colorName;

    //商品图片
    private String imgUrl;

    // 品牌名称
    private String brandName;

    // 销售状态 1:上架 0：下架
    private Integer saleStatus;

    // 审核状态 1：审核中  0：审核不通过
    private Integer reviewStatus;

    // 是否含电 1:含电 0：不含电
    private Integer isCorrespondence;

}
