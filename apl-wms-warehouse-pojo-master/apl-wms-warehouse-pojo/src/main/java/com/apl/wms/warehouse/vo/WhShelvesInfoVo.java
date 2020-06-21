package com.apl.wms.warehouse.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 货架 详细实体
 * </p>
 *
 * @author cy
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WhShelvesInfoVo implements Serializable {


private static final long serialVersionUID=1L;

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

}
