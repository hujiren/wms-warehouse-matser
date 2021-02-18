package com.apl.wms.warehouse.lib.cache.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StorageLocalCacheBo implements Serializable {

    private String cacheKey;

    private Long storageLocalId;

    //库位编号
    private String storageLocalSn;

    // 库位全称
    private String storageLocalName;


}
