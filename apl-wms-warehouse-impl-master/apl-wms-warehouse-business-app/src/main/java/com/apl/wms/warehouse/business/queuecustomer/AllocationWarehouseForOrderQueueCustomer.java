package com.apl.wms.warehouse.business.queuecustomer;

import com.apl.cache.AplCacheHelper;
import com.apl.cache.AplCacheHelper;
import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.StringUtil;
import com.apl.tenant.AplTenantConfig;
import com.apl.wms.outstorage.order.lib.pojo.bo.AllocationWarehouseOutOrderBo;
import com.apl.wms.warehouse.service.AllocationStockOrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author hjr start
 * @date 2020/7/8 - 16:56
 */
@Slf4j
@Component
public class AllocationWarehouseForOrderQueueCustomer {

    @Autowired
    AllocationStockOrderService allocationStockOrderService;

    @Autowired
    AplCacheHelper aplCacheHelper;

    @RabbitHandler
    @RabbitListener(queues = "allocationWarehouseForOrderQueue")
    public void onMessage(Message message, Channel channel)  throws Exception{

        try {

            AllocationWarehouseOutOrderBo outOrderBo = (AllocationWarehouseOutOrderBo) StringUtil.getObjectFromBytes(message.getBody());

            SecurityUser securityUser  = outOrderBo.getSecurityUser();

            //创建临时token，并把securityUser放入redis中，供微服务调用
//            String token = CommonContextHolder.setSecurityUser(aplCacheHelper, securityUser);
            String token = UUID.randomUUID().toString().replaceAll("-", "").replaceAll("_", "");
            token = securityUser.getTenantGroup() + "_" + securityUser.getInnerOrgCode() + "_" + securityUser.getInnerOrgId() + "_" + token;
            aplCacheHelper.opsForValue("wareHouse").set(token, token);

            //把临时token放入线程安全变量中, feign会用到
            CommonContextHolder.tokenContextHolder.set(token);

            //多数据源切换
            //DataSourceContextHolder.set(securityUser.getTenantGroup(), securityUser.getInnerOrgCode(), securityUser.getInnerOrgId());

            // 多租户ID值
            AplTenantConfig.tenantIdContextHolder.set(securityUser.getInnerOrgId());

            //订单来源  1自动同步平台订单
            allocationStockOrderService.allocationByQueue(outOrderBo);

            //删除临时token
            aplCacheHelper.opsForKey("wareHouse").delByKey(token);

            CommonContextHolder.tokenContextHolder.remove();
            CommonContextHolder.securityUserContextHolder.remove();
            AplTenantConfig.tenantIdContextHolder.remove();

            //手工ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
