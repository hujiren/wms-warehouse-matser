package com.apl.wms.warehouse.lib.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public  class CountDto implements Serializable {

    private Long id;

    private Long whId;

    private Integer count;

}
