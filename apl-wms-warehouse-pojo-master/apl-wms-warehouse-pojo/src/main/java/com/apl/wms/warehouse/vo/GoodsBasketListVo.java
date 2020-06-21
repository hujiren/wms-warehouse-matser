package com.apl.wms.warehouse.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 货蓝
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsBasketListVo implements Serializable {


private static final long serialVersionUID=1L;

    // 货蓝id
    private Long id;

    // 篮子编号
    private String basketSn;

    // 描述
    private String remark;

}
