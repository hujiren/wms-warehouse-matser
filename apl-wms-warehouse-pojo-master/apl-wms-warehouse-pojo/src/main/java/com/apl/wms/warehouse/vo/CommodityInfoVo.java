package com.apl.wms.warehouse.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class CommodityInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 商品id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // sku
    private String sku;

    // 原产地
    private String sourceArea;

    // 客户id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    //客户名称
    private String customerName;

    // 商品名称
    private String commodityName;

    // 商品英文名称
    private String commodityNameEn;

    // 品类一id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long category1Id;

    //分类名称
    private String categoryName;

    //分类英文名称
    private String categoryNameEn;

    // 品牌id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long brandId;

    //品牌中文名称
    private String brandName;

    //品牌英文名称
    private String brandNameEn;

    // 销售状态 1:上架 0：下架
    private Integer saleStatus;

    // 审核状态 1：审核中  0：审核不通过
    private Integer reviewStatus;

    // 单位名称
    private String unitCode;

    // 平台价格
    private BigDecimal platformPrice;

    // 平台币制
    private String platformCurrency;

    // 长
    private BigDecimal sizeLength;

    // 宽
    private BigDecimal sizeWidth;

    // 高
    private BigDecimal sizeHeight;

    // 重量
    private BigDecimal weight;

    // 商品url链接
    private String netUrl;

    // 申报价格
    private BigDecimal declarePrice;

    // 申报币制
    private String declareCurrency;

    // 商品的主图
    private String imgUrl;

    // 是否含电 1:含电 0：不含电
    private Integer isCorrespondence;

    // 材质
    private String textureName;

    // 材质英文名称
    private String textureNameEn;

    // 颜色名称
    private String colorName;

    // 颜色英文名称
    private String colorNameEn;

    // 规格
    private String specName;

    // 规格英文名称
    private String specNameEn;

    // 用途
    private String useWay;

    // 用途英文名称
    private String useWayEn;

    // 海关编码
    private String hsCode;



}
