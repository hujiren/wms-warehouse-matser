package com.apl.wms.warehouse.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 包装材料
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PackagingMaterialsListVo implements Serializable {


private static final long serialVersionUID=1L;

    // 商品id
    private Long id;

    // sku
    private String sku;

    private Long customerId;
    // 客户名称
    private String customerName;

    // 商品名称
    private String commodityName;

    // 商品英文名称
    private String commodityNameEn;

    // 规格名称
    private String specName;

    // 规格名称规格英文名称
    private String specNameEn;

    //颜色中文名称
    private String colorName;

    //颜色英文名称
    private String colorNameEn;

    //商品图片
    private String imgUrl;

    // 销售状态 1:上架 0：下架
    private Integer saleStatus;

    // 审核状态 1：审核中  0：审核不通过
    private Integer reviewStatus;

}
