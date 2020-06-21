package com.apl.wms.warehouse.lib.pojo.bo;

import lombok.Data;

@Data
public class PackagingMaterialsCountBo {


    // 包装材料id
    private Long id;

    // 包装材料名称
    private String commodityName;

    // 包装材料英文名称
    private String commodityNameEn;

    // 单位名称
    private String unitCode;

    // 商品的主图
    private String imgUrl;

    //包装材料 对应的商品容量
    private Integer capacity;

    //包装材料数量
    private Integer count;

}
