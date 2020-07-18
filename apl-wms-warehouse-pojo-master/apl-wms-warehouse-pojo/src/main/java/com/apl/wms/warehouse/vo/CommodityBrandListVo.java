package com.apl.wms.warehouse.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 商品品牌
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommodityBrandListVo implements Serializable {


private static final long serialVersionUID=1L;


    // 商品品牌id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;



    // 品牌名称
    private String brandName;



    // 品牌的英文名称
    private String brandNameEn;



    // 品牌所属的客户id
    private Long customerId;






}
