package com.apl.wms.warehouse.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class CommodityReportBo {

    //客户id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;
    //客户名称
    private String customerName;
    //商品名称
    private String commodityName;
    //商品英文名称
    private String commodityNameEn;
    //品牌名称
    private String brandName;
    //品牌英文名称
    private String brandNameEn;
    //备注
    private String remark;
    //sku
    private String sku;
    //分类名称
    private String categoryName;
    //分类英文名称
    private String categoryNameEn;

}
