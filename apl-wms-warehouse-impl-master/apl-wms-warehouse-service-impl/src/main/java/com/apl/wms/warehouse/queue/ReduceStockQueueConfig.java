package com.apl.wms.warehouse.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc: 减库存 队列
 * @Author: CY
 * @Date: 2020/1/8 9:56
 */
@Component
public class ReduceStockQueueConfig {

    private String reduceQueue = "reduceQueue";

    private String reduceExchange = "reduceExchange";

    @Bean
    public Queue reduceQueue(){

        Map<String, Object> args = new HashMap<>();
        return new Queue(reduceQueue,false,false,false,args);
    }


    @Bean
    public Exchange reduceExchange(){

        return new FanoutExchange(reduceExchange);
    }


    @Bean
    public Binding reduceBinding(Queue reduceQueue, Exchange reduceExchange){

        return BindingBuilder.bind(reduceQueue).to(reduceExchange).with("reduceQueue").noargs();
    }

}
