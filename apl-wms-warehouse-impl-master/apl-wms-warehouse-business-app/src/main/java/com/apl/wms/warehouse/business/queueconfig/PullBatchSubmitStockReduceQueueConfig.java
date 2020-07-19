package com.apl.wms.warehouse.business.queueconfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc: 批次提交 库存减扣
 * @Author: CY
 * @Date: 2020/6/11 10:36
 */
@Component
public class PullBatchSubmitStockReduceQueueConfig {

    private String pullBatchSubmitStockReduceQueue = "pullBatchSubmitStockReduceQueue";

    private String pullBatchSubmitStockReduceExchange = "pullBatchSubmitStockReduceExchange";

    @Bean
    public Queue pullBatchSubmitStockReduceQueue(){

        Map<String, Object> args = new HashMap<>();
        return new Queue(pullBatchSubmitStockReduceQueue,false,false,false,args);
    }


    @Bean
    public Exchange pullBatchSubmitStockReduceExchange(){

        return new FanoutExchange(pullBatchSubmitStockReduceExchange);
    }


    @Bean
    public Binding pullBatchSubmitStockReduceBinding(Queue pullBatchSubmitStockReduceQueue, Exchange pullBatchSubmitStockReduceExchange){

        return BindingBuilder.bind(pullBatchSubmitStockReduceQueue).to(pullBatchSubmitStockReduceExchange).with("putAwayQueue").noargs();
    }
}
