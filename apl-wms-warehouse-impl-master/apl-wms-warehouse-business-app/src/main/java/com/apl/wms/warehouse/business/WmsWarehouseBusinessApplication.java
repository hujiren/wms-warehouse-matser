package com.apl.wms.warehouse.business;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@SpringBootApplication(scanBasePackages = {"com.apl.wms.warehouse", "com.apl.lib", "com.apl.lib.handler"}, exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"com.apl.wms.warehouse", "com.apl.lib", "com.apl.lib.handler"})
@EnableFeignClients(basePackages = {"com.apl.wms.warehouse.lib.feign","com.apl.sys.lib.feign"})
@MapperScan("com.apl.wms.warehouse.mapper")
@EnableDiscoveryClient
@EnableSwagger2
public class WmsWarehouseBusinessApplication {

    public static void main(String[] args) {

        //com.apl.lib.datasource.DataSourceConfig
        //com.apl.lib.datasource.DynamicDataSource

        SpringApplication.run(WmsWarehouseBusinessApplication.class , args);
    }

}
