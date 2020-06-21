package com.apl.wms.warehouse.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 商品图片
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PicItemDto implements Serializable {


    private String imgUrl;

    private Integer imgSort;

}
