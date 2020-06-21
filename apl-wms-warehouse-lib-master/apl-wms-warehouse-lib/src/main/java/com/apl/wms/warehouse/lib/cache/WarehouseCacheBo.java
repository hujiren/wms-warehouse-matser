package com.apl.wms.warehouse.lib.cache;

import lombok.Data;

@Data
public class WarehouseCacheBo {

    private String cacheKey;

    private Long id;

    //仓库代码
    private String whCode;

    //商品名称
    private String whName;

    //仓库英文名
    private String whNameEn;
}
