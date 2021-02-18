package com.apl.wms.warehouse.lib.cache.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperatorServiceBo implements Serializable {

    private String cacheKey;

    private Long id;

    private String serviceName;

    private String serviceNameEn;
}
