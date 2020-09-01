package com.apl.wms.warehouse.lib.cache.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author hjr start
 * @date 2020/8/1 - 10:46
 */

public class CommodityBrandCacheBo {

    // 商品品牌id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 品牌名称
    private String brandName;

    // 品牌的英文名称
    private String brandNameEn;

    // 品牌所属的客户id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;
}
