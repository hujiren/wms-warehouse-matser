package com.apl.wms.warehouse.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 库位
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StorageLocalListVo implements Serializable {

private static final long serialVersionUID=1L;


    // 库位id
    private Long id;

    // 库位编号（条形码）
    private String storageLocalSn;

    //所在层数
    private Integer storageLayer;

    //商品长
    private BigDecimal sizeLength;

    //商品宽
    private BigDecimal sizeWidth;

    //商品高
    private BigDecimal sizeHeight;

    //体积
    private BigDecimal volume;

    // 承受重量
    private Double supportWeight;

    // 库位描述
    private String remark;

    // 货架id
    private Long shelvesId;

}
