package com.apl.wms.warehouse.business.queueconfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc: 上架队列 进行库存变更
 * @Author: CY
 * @Date: 2020/1/8 9:48
 */
@Component
public class PutAwayQueueConfig {

    private String putAwayQueue = "putAwayQueue";

    private String putAwayExchange = "putAwayExchange";

    @Bean
    public Queue putAwayQueue(){

        Map<String, Object> args = new HashMap<>();
        return new Queue(putAwayQueue,false,false,false,args);
    }


    @Bean
    public Exchange putAwayExchange(){

        return new FanoutExchange(putAwayExchange);
    }


    @Bean
    public Binding putAwayBinding(Queue putAwayQueue, Exchange putAwayExchange){

        return BindingBuilder.bind(putAwayQueue).to(putAwayExchange).with("putAwayQueue").noargs();
    }
}
