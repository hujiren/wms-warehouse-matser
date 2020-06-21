package com.apl.wms.warehouse.vo;

import java.math.BigDecimal;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 货架规格 详细实体
 * </p>
 *
 * @author cy
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ShelvesSpecInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 货架规格id
    private Long id;

    // 规格编号
    private String specNo;

    // 单层-长
    private BigDecimal singleLayersLength;

    // 单层-宽
    private BigDecimal singleLayersWidth;

    // 单层-高
    private BigDecimal singleLayersHeight;

    // 层数
    private Integer layersCount;

    // 每层承受重量
    private Double singleLayerSupportWeight;

    // 描述
    private String remark;

    // 货架规格图片url
    private String imgUrl;

}
