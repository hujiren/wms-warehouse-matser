package com.apl.wms.warehouse.lib.cache.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StoreCacheBo implements Serializable {

    private String cacheKey;

    private Long storeId;

    private String electricCode;

    private String storeName;

    private String storeNameEn;

}
