package com.apl.wms.warehouse.lib.feign;


import com.apl.wms.warehouse.lib.feign.impl.OutstorageOrderBusinessFeignImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "apl-wms-order-outstorage-business-app" , fallback = OutstorageOrderBusinessFeignImpl.class)
@Component
public interface OutstorageOrderBusinessFeign {


    @PostMapping(value = "/out-order/create/call-back")
    void outStorageOrderCreateCallback(@RequestParam("orderId") Long orderId,
                                       @RequestParam("status") Integer status);



}
