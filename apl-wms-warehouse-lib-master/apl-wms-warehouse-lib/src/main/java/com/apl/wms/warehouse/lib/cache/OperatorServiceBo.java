package com.apl.wms.warehouse.lib.cache;

import lombok.Data;

@Data
public class OperatorServiceBo {

    private String cacheKey;

    private Long id;

    private String serviceName;

    private String serviceNameEn;
}
