package com.apl.wms.warehouse.lib.pojo.bo;

import com.apl.lib.security.SecurityUser;
import lombok.Data;

import javax.servlet.AsyncContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class PlatformOutOrderStockBo implements Serializable {

    private SecurityUser securityUser;

    //客户id
    private Long customerId;

    //仓库id
    private Long whId;

    private List<PlatformOutOrderStock> platformOutOrderStocks = new ArrayList<>();

    @Data
    public static class PlatformOutOrderStock implements Serializable{

        //库位id
        private Long storageLocalId;

        //商品id
        private Long commodityId;

        //改变的数量
        private Integer changeCount;


    }


}

