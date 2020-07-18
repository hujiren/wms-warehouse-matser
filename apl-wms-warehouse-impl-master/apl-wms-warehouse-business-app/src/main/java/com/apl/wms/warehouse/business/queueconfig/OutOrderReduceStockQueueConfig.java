package com.apl.wms.warehouse.business.queueconfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class OutOrderReduceStockQueueConfig {

    private String outOrderReduceStockQueue = "outOrderReduceStockQueue";

    private String outOrderReduceStockExchange = "outOrderReduceStockExchange";

    @Bean
    public Queue outOrderReduceStockQueue(){

        Map<String, Object> args = new HashMap<>();
        return new Queue(outOrderReduceStockQueue,false,false,false,args);
    }


    @Bean
    public Exchange outOrderReduceStockExchange(){

        return new FanoutExchange(outOrderReduceStockExchange);
    }


    @Bean
    public Binding outOrderReduceStockBinding(Queue outOrderReduceStockQueue, Exchange outOrderReduceStockExchange){

        return BindingBuilder.bind(outOrderReduceStockQueue).to(outOrderReduceStockExchange).with("outOrderReduceStockQueue").noargs();
    }

}
