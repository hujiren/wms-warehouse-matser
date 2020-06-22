package com.apl.wms.warehouse.lib.cache;

import lombok.Data;

@Data
public class StoreCacheBo {

    private String cacheKey;

    private Long storeId;

    private String electricCode;

    private String storeName;

    private String storeNameEn;

}
