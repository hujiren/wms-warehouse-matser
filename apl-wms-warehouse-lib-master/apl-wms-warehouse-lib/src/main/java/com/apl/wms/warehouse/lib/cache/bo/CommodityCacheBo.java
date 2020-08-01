package com.apl.wms.warehouse.lib.cache.bo;

import lombok.Data;

@Data
public class CommodityCacheBo {

    private String cacheKey;

    private Long id;

    //客户id
    private Long customerId;

    //商品sku
    private String sku;

    //商品名称
    private String commodityName;

    //商品英文名
    private String commodityNameEn;

    //品牌名称
    private String brandName;

    //品牌英文名
    private String brandNameEn;

    //规格
    private String specName;

    //规格英文名
    private String specNameEn;

    //颜色
    private String colorName;

    //颜色英文名
    private String colorNameEn;

    //商品图片
    private String imgUrl;

    //是否含电 1:含电 0：不含电
    private Integer isCorrespondence;

    //海关编码
    private String hsCode;
}
