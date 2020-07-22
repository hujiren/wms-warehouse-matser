package com.apl.wms.warehouse.lib.cache;

import lombok.Data;

@Data
public class StorageLocalCacheBo {

    private String cacheKey;

    private Long storageLocalId;

    //库位编号
    private String storageLocalSn;

    // 库位全称
    private String storageLocalName;


}
