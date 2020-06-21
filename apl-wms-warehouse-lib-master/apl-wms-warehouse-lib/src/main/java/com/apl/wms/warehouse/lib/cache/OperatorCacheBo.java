package com.apl.wms.warehouse.lib.cache;

import lombok.Data;

@Data
public class OperatorCacheBo {

    private String cacheKey;

    private Long id;

    //成员id
    private Long memberId;

    //成员名称
    private String memberName;

    //成员职位
    //private String memberPosition;

    //成员职位英文名
    //private String memberPositionEn;

    //仓库 id
    private Long whId;

    private Long innerOrgId;


}
