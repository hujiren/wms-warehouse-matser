package com.apl.wms.warehouse.business.queueconfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc: 出库订单创建 库存锁定
 * @Author: CY
 * @Date: 2020/6/11 10:36
 */
@Component
public class OutStorageOrderCreateCountLockQueueConfig {

    private String outStorageOrderCreateCountLockQueue = "outStorageOrderCreateCountLockQueue";

    private String outStorageOrderCreateCountLockExchange = "outStorageOrderCreateCountLockExchange";

    @Bean
    public Queue outStorageOrderCreateCountLockQueue(){

        Map<String, Object> args = new HashMap<>();
        return new Queue(outStorageOrderCreateCountLockQueue,false,false,false,args);
    }


    @Bean
    public Exchange outStorageOrderCreateCountLockExchange(){

        return new FanoutExchange(outStorageOrderCreateCountLockExchange);
    }


    @Bean
    public Binding outStorageOrderCreateCountLockBinding(Queue outStorageOrderCreateCountLockQueue, Exchange outStorageOrderCreateCountLockExchange){

        return BindingBuilder.bind(outStorageOrderCreateCountLockQueue).to(outStorageOrderCreateCountLockExchange).with("putAwayQueue").noargs();
    }
}
