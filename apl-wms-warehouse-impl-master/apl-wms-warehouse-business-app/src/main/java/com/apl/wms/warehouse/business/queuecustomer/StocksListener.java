package com.apl.wms.warehouse.business.queuecustomer;

import com.apl.cache.AplCacheUtil;
import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.StringUtil;
import com.apl.sys.lib.feign.OuterFeign;
import com.apl.tenant.AplTenantConfig;
import com.apl.wms.warehouse.bo.StockUpdBo;
import com.apl.wms.warehouse.bo.StockUpdListBo;
import com.apl.wms.warehouse.lib.pojo.bo.PlatformOutOrderStockBo;
import com.apl.wms.warehouse.lib.pojo.dto.CountDto;
import com.apl.wms.warehouse.service.StocksService;
import com.apl.wms.warehouse.service.StorageLocalStocksService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component
public class StocksListener {

    @Autowired
    StocksService stocksService;

    @Autowired
    StorageLocalStocksService storageLocalStocksService;

    @Autowired
    AplCacheUtil redisTemplate;

    @Autowired
    OuterFeign outerFeign;

    //@RabbitHandler
    //@RabbitListener(queues = "putAwayQueue")
    public void putAwayQueueOnMessage(Message message, Channel channel) throws Exception {

         try {

             StockUpdListBo stockUpdListBo = (StockUpdListBo) StringUtil.getObjectFromBytes(message.getBody());

             List<StockUpdBo> stockUpdBo = stockUpdListBo.getStockUpdBo();
             //为空 直接终止执行
            if(CollectionUtils.isEmpty(stockUpdBo)){
               return ;
            }
             SecurityUser securityUser  = stockUpdListBo.getSecurityUser();

             //创建临时token，并把securityUser放入redis中，供微服务调用
             String token = CommonContextHolder.setSecurityUser(redisTemplate, securityUser);

             //把临时token放入线程安全变量中, feign会用到
             CommonContextHolder.tokenContextHolder.set(token);

             //多数据源切换
             //DataSourceContextHolder.set(securityUser.getTenantGroup(), securityUser.getInnerOrgCode(), securityUser.getInnerOrgId());

             // 多租户ID值
             AplTenantConfig.tenantIdContextHolder.set(securityUser.getInnerOrgId());

             //加库存
            stocksService.updateStocks(stockUpdBo);
            //手工ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error(this.getClass().getName()+", ERROR：{}", e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    //@RabbitHandler
    //@RabbitListener(queues = "reduceQueue")
    public void reduceExchangeOnMessage(Message message, Channel channel) throws Exception {

        try {

           /*List<OutOrderCommodityItemUpdDto> outOrderCommodityItemUpdDtos = (List<OutOrderCommodityItemUpdDto>) StringUtil.getObjectFromBytes(message.getBody());

            if(CollectionUtils.isEmpty(outOrderCommodityItemUpdDtos)){
                log.error("传入集合为空 ，减库存失败");
                //集合为空 ， 直接返回
                return ;
            }

            String token = outOrderCommodityItemUpdDtos.get(0).getToken();
            if(StringUtil.isEmpty(token)){
                log.error("token 不存在");
                //token 为空 ， 直接返回
                return ;
            }
            DataSourceContextHolder.set(token);

            //减库存
            Boolean flag = stocksService.reduceStocks(outOrderCommodityItemUpdDtos);
            if(!flag){
                log.error("减库存失败");
                throw new AplException(CommonStatusCode.SAVE_FAIL);
            }*/

            //手工ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error(this.getClass().getName()+", ERROR：{}", e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    //@RabbitHandler
    //@RabbitListener(queues = "outOrderReduceStockQueue")
    public void outOrderReduceStockQueueOnMessage(Message message, Channel channel) throws Exception {

        try {

            List<CountDto> countDtos = (List<CountDto>) StringUtil.getObjectFromBytes(message.getBody());

            if(CollectionUtils.isEmpty(countDtos)){
                log.error("传入集合为空 ，减库存失败");
                //集合为空 ， 直接返回
                return ;
            }

            //手工ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error(this.getClass().getName()+", ERROR：{}", e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }



    //@RabbitHandler
    //@RabbitListener(queues = "outStorageOrderCreateCountLockQueue")
    public void outOrderCreateStockUpdate(Message message, Channel channel) throws Exception {

        try {

            PlatformOutOrderStockBo platformOutOrderStockBo = (PlatformOutOrderStockBo) StringUtil.getObjectFromBytes(message.getBody());

            //切换数据库
            changeDb(platformOutOrderStockBo.getSecurityUser());

            //进行库存 锁定
            stocksService.outStorageOrderStockLock(platformOutOrderStockBo);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error(this.getClass().getName()+", ERROR：{}", e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }


    //@RabbitHandler
    //@RabbitListener(queues = "pullBatchSubmitStockReduceQueue")
    public void pullBatchSubmitStockReduceUpdate(Message message, Channel channel) throws Exception {

        try {

            PlatformOutOrderStockBo platformOutOrderStockBo = (PlatformOutOrderStockBo) StringUtil.getObjectFromBytes(message.getBody());

            //切换数据库
            changeDb(platformOutOrderStockBo.getSecurityUser());

            //进行库存 减扣
            stocksService.pullBatchSubmitStockReduce(platformOutOrderStockBo);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error(this.getClass().getName()+", ERROR：{}", e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    private void changeDb(SecurityUser securityUser) {

        //创建临时token，并把securityUser放入redis中，供微服务调用
        String token = CommonContextHolder.setSecurityUser(redisTemplate, securityUser);

        //把临时token放入线程安全变量中, feign会用到
        CommonContextHolder.tokenContextHolder.set(token);

        //多租户切换数据源
        //DataSourceContextHolder.set(securityUser.getTenantGroup(), securityUser.getInnerOrgCode(), securityUser.getInnerOrgId());
    }
}
