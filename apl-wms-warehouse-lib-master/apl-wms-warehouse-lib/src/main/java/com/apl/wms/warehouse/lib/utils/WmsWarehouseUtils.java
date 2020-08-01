package com.apl.wms.warehouse.lib.utils;

import com.apl.cache.AplCacheUtil;
import com.apl.lib.exception.AplException;
import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.wms.warehouse.lib.cache.JoinOperator;
import com.apl.wms.warehouse.lib.cache.bo.OperatorCacheBo;
import com.apl.wms.warehouse.lib.constants.WmsWarehouseCommonStatusCode;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;



public class WmsWarehouseUtils {


    /**
     * @Desc: 检查操作员 是否存在
     * @Author: CY
     * @Date: 2020/3/18 16:39
     */
    public static OperatorCacheBo checkOperator(WarehouseFeign warehouseFeign, AplCacheUtil redisTemplate) {

        //获取仓库id
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();
        Long memberId = securityUser.getMemberId();

        JoinOperator joinOperator = new JoinOperator(1, warehouseFeign, redisTemplate);
        OperatorCacheBo operatorCacheBo = joinOperator.getEntity(memberId);
        if (null == operatorCacheBo) {
            throw new AplException(WmsWarehouseCommonStatusCode.YOURE_NOT_AN_WAREHOUSE_OPERATOR.getCode(), WmsWarehouseCommonStatusCode.YOURE_NOT_AN_WAREHOUSE_OPERATOR.getMsg());
        }

        return operatorCacheBo;
    }





}
