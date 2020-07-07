package com.apl.wms.warehouse.lib.cache;

import com.apl.lib.config.MyBatisPlusConfig;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.join.JoinBase;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.feign.WarehouseFeign;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * @Description  关联附加服务
 * @Param
 * @Return
 * @Author  arran
 * @Date  2020/1/9
 */
public class JoinOperatorService extends JoinBase<OperatorServiceBo> {

    public WarehouseFeign warehouseFeign;

    public JoinOperatorService(int joinStyle, WarehouseFeign warehouseFeign, RedisTemplate redisTemplate){
        this.warehouseFeign = warehouseFeign;
        this.redisTemplate = redisTemplate;
        this.tabName = "operator_service";
        this.joinStyle = joinStyle;

        this.innerOrgId = MyBatisPlusConfig.tenantIdContextHolder.get();
        this.cacheKeyNamePrefix = "JOIN_CACHE:"+this.tabName+"_"+this.innerOrgId.toString()+"_";
    }


    @Override
    public Boolean addCache(String keys, Long minKey, Long maxKey){

        ResultUtil<Boolean> result = warehouseFeign.addOperatorServiceCache(keys, minKey, maxKey);
        if(result.getCode().equals(CommonStatusCode.SYSTEM_SUCCESS.code))
            return true;

        return false;
    }

}
