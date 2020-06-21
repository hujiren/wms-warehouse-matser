package com.apl.wms.warehouse.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 品名库
 * </p>
 *
 * @author apl
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommodityNameLibListVo implements Serializable {


private static final long serialVersionUID=1L;

    private Long id;

    // 品名
    private String commodityName;

    // 英文名
    private String commodityNameEn;

    // sku
    private String sku;

    // 客户id
    private Long customerId;

    //客户名
    private String customerName;

    // 品类id
    private Integer categoryId;

    //品类名称
    private String categoryName;

}
