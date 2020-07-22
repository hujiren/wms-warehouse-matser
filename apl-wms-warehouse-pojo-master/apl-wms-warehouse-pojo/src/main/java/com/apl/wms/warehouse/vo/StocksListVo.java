package com.apl.wms.warehouse.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 库存
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StocksListVo implements Serializable {


private static final long serialVersionUID=1L;

    // 库存id
    private Long id;

    private String sku;

    private String commodityId;

    private String commodityName;

    private String commodityNameEn;

    private String specName;

    private String imgUrl;

    private String catName;

    private String categoryName;

    private String categoryNameEn;

    // 仓库id
    private Long whId;

    // 可售库存
    private Integer availableCount;

    // 实际库存
    private Integer realityCount;

    //是否带电
    private Integer isCorrespondence;


}
