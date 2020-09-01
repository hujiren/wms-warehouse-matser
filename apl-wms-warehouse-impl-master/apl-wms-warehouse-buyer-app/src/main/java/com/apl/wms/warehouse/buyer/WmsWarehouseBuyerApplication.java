package com.apl.wms.warehouse.buyer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(
        scanBasePackages = {
                "com.apl.lib", //APL基本工具类
                //"com.apl.tenant", //多租户
                //"com.apl.abatis", // sqlSession封装
                "com.apl.db.adb", // adb数据库操作助手
                //"com.apl.db.dynamicdb", //动态数据源
                "com.apl.cache", // redis代理
                //"com.apl.amqp", //消息队列代理
                "com.apl.sys.lib",
                "com.apl.wms.warehouse"},
        exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(
        basePackages = {
                "com.apl.wms.warehouse.lib.feign",
                "com.apl.sys.lib.feign",
                "com.apl.wms.outstorage.order.lib.feign"})
@MapperScan("com.apl.wms.warehouse.dao")
@EnableDiscoveryClient
@EnableSwagger2
public class WmsWarehouseBuyerApplication {

    public static void main(String[] args) {
        //com.apl.lib.utils.CommonContextHolder

        SpringApplication.run(WmsWarehouseBuyerApplication.class , args);
    }
}
