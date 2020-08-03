package com.apl.wms.warehouse.lib.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class StorageLocalInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 库位id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    // 库位编号（条形码）
    private String storageLocalSn;

    // 库位全称
    private String storageLocalName;

    //所在层数
    private Integer storageLayer;

    //商品长
    private Double sizeLength;

    //商品宽
    private Double sizeWidth;

    //商品高
    private Double sizeHeight;

    // 承受重量
    private Double supportWeight;

    //体积
    private Double volume;

    // 商品ID
    private Long commodityId;

    // 商品可售库存
    private Integer availableCount;

    // 商品实际库存
    private Integer realityCount;

    // 商品可存放数量
    private Integer thresholdCount;

    // 库位描述
    private String remark;

    // 货架id
    private Long shelvesId;

    //库位可以存放 的商品数量
    //private Integer stockCount;

    //库位状态
    //private Integer storageStatus;

    //是否锁定
    private Integer isLock;



}
