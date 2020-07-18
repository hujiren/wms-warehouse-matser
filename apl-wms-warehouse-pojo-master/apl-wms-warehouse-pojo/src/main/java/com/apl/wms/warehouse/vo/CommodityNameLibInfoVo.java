package com.apl.wms.warehouse.vo;

import java.math.BigDecimal;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 品名库 详细实体
 * </p>
 *
 * @author apl
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommodityNameLibInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    //
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 品名
    private String commodityName;

    // 英文名
    private String commodityNameEn;

    // sku
    private String sku;

    // 客户id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    //客户名
    private String customerName;

    // 品类id
    private Integer categoryId;

    //品类名称
    private String categoryName;

    // 申报价值
    private BigDecimal declarePrice;

    // 申报币制
    private String declareCurrency;

}
