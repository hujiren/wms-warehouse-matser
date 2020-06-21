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
public class StocksInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 库存id
    private Long id;

    // 仓库id
    private Long whId;

    // 商品id
    private Long commodityId;

    // 可用库存
    private Integer availableStockCount;

    // 冻结库存
    private Integer freezeStockCount;

    // 总库存
    private Integer allStockCount;


}
