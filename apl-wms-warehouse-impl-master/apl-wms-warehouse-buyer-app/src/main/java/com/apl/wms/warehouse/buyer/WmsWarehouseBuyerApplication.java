package com.apl.wms.warehouse.buyer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.apl.wms.warehouse", "com.apl.lib", "com.apl.lib.handler"}, exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.apl.wms.warehouse.lib.feign","com.apl.sys.lib.feign"})
@MapperScan("com.apl.wms.warehouse.mapper")
@EnableDiscoveryClient
@EnableSwagger2
public class WmsWarehouseBuyerApplication {

    public static void main(String[] args) {

        SpringApplication.run(WmsWarehouseBuyerApplication.class , args);
    }
}
