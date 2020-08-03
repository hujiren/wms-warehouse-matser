package com.apl.wms.warehouse.business;
import com.apl.wms.warehouse.lib.pojo.po.StocksHistoryPo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(
        scanBasePackages = {
                "com.apl.wms.warehouse",
                "com.apl.db",
                "com.apl.cache",
                "com.apl.amqp",
                "com.apl.lib",
                "com.apl.sys.lib",
                "com.apl.wms.warehouse.lib"},
        exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(
        basePackages = {"com.apl.wms.warehouse.lib.feign",
                "com.apl.sys.lib.feign",
                "com.apl.wms.outstorage.order.lib.feign"})
@MapperScan("com.apl.wms.warehouse.dao")
@EnableDiscoveryClient
@EnableSwagger2
public class WmsWarehouseBusinessApplication {



    public static void main(String[] args) throws Exception {

        SpringApplication.run(WmsWarehouseBusinessApplication.class , args);

    }

}
