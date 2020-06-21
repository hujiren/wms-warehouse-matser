package com.apl.wms.warehouse.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 包装材料 详细实体
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PackagingMaterialsInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 包装材料id
    private Long id;

    // sku
    private String sku;

    // 包装材料名称
    private String commodityName;

    // 包装材料英文名称
    private String commodityNameEn;

    // 单位名称
    private String unitCode;

    // 长
    private BigDecimal sizeLength;

    // 宽
    private BigDecimal sizeWidth;

    // 高
    private BigDecimal sizeHeight;

    // 重量
    private BigDecimal weight;

    // 商品url链接
    private String netUrl;

    // 商品的主图
    private String imgUrl;

    // 材质
    private String textureName;

    // 材质英文名称
    private String textureNameEn;

    // 颜色名称
    private String colorName;

    // 颜色英文名称
    private String colorNameEn;

    // 规格
    private String specName;

    // 规格英文名称
    private String specNameEn;

    // 用途
    private String useWay;

    // 用途英文名称
    private String useWayEn;

    //包装材料 对应的商品容量
    private Integer capacity;

    //包装材料数量
    private Integer count;


}
