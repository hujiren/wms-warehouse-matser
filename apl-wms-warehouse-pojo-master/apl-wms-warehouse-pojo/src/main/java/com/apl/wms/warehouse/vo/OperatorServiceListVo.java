package com.apl.wms.warehouse.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 附加服务操作名称
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OperatorServiceListVo implements Serializable {


private static final long serialVersionUID=1L;


    // 
    private Long id;

    // 服务名称
    private String serviceName;

    // 服务英文名称
    private String serviceNameEn;

}
