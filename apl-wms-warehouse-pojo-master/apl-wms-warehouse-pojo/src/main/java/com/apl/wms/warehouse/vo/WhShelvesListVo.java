package com.apl.wms.warehouse.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 货架
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WhShelvesListVo implements Serializable {


    private static final long serialVersionUID = 1L;

    // 货架id
    private Long id;

    // 货架编号
    private String shelvesNo;

    // 货架规格id
    private Long shelvesSpecId;

    // 仓库区域id
    private Long whZoneId;

    // 备注
    private String remark;

    private String specNo;

    private String singleLayersLength;

    private BigDecimal singleLayersWidth;

    private BigDecimal singleLayersHeight;

    private Integer layersCount;

    private Double singleLayerSupportWeight;

    private String imgUrl;

}
