package com.apl.wms.warehouse.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 库位储存商品数量
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StorageLocalStocksInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 库位储存商品数量id
    private Long id;

    // 库位id
    private Long storageId;

    // 商品id
    private Long commodityId;

    // 可用库存
    private Integer availableCount;

    // 冻结库存
    private Integer freezeCount;

    // 实际库存
    private Integer realityCount;

}
