package com.apl.wms.warehouse.lib.constants;

/**
 * @author CY
 * @version 1.0.0
 * @ClassName PgsSystemConstants.java
 * @createTime 2019年07月24日 14:16:00
 */
public interface WmsWarehouseAplConstants {

    //锁定
    Integer LOCK = 1;
    //没有锁定
    Integer UN_LOCK = 0;

    //库位状态.  1空   2未满     3满   4占用中
    Integer STORAGE_EMTITY_STATUS = 1;
    Integer STORAGE_UN_FULL_STATUS = 2;
    Integer STORAGE_FULL_STATUS = 3;
    Integer STORAGE_USEING_STATUS = 4;

}
