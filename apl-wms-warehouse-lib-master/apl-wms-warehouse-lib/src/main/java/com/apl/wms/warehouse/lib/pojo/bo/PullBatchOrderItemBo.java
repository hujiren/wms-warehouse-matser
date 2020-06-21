package com.apl.wms.warehouse.lib.pojo.bo;

import lombok.Data;

import java.util.List;

@Data
public class PullBatchOrderItemBo {

    private Long orderId;

    private Long commodityId;

    private Integer orderQty;

    private List<StorageCount> storageCounts;

    @Data
    public static class StorageCount {

        private Long storageId;

        private Integer count;

    }

}
