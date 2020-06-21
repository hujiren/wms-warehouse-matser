package com.apl.wms.warehouse.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 网店铺 详细实体
 * </p>
 *
 * @author arran
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StoreInfoVo implements Serializable {


private static final long serialVersionUID=1L;

    // 网店铺id
    private Long id;

    // 店铺编码code
    private String storeCode;

    // 店铺名称
    private String storeName;

    // 店铺英文名称
    private String storeNameEn;

    // 客户id
    //private Long customerId;


    // 电商平台代码
    private String electricCode;

    // 店铺状态 1: 可用 0：不可用
    private Integer storeStatus;

    // 备注
    private String remark;


}
