package com.apl.wms.warehouse.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 仓库操作员 详细实体
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WhOperatorInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 仓库操作员id
    private Long id;



    // 操作员id
    private Long memberId;

    // 操作员姓名
    private String memberName;

    // 成员职位
    private String memberPosition;

    // 成员职位英文职位
    private String memberPositionEn;

    // 所属的仓库id
    private Long whId;


}
