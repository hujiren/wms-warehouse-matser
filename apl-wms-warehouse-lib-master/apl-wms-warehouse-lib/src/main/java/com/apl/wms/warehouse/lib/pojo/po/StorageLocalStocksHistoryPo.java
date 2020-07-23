package com.apl.wms.warehouse.lib.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 库存记录
 * </p>
 *
 * @author cy
 * @since 2020-07-08
 */
//@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StorageLocalStocksHistory对象", description="库位库存记录")
public class StorageLocalStocksHistoryPo implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单类型 1入库订单   2出库订单  3盘点订单")
    private Integer orderType;

    @ApiModelProperty(value = "库存类型 可售库存  2实际库存")
    private Integer stocksType;

    @ApiModelProperty(value = "订单号")
    private String orderSn;

    @ApiModelProperty(value = "仓库id")
    private Long whId;

    @ApiModelProperty(value = "库位id")
    private Long storageLocalId;

    @ApiModelProperty(value = "商品id")
    private Long commodityId;

    @ApiModelProperty(value = "商品入库数量")
    private Integer inQty;

    @ApiModelProperty(value = "商品出库数量")
    private Integer outQty;

    @ApiModelProperty(value = "库存剩余数量")
    private Integer stocksQty;

    @ApiModelProperty(value = "操作时间")
    private Timestamp operatorTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getStocksType() {
        return stocksType;
    }

    public void setStocksType(Integer stocksType) {
        this.stocksType = stocksType;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Long getWhId() {
        return whId;
    }

    public void setWhId(Long whId) {
        this.whId = whId;
    }

    public Long getStorageLocalId() {
        return storageLocalId;
    }

    public void setStorageLocalId(Long storageLocalId) {
        this.storageLocalId = storageLocalId;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getInQty() {
        return inQty;
    }

    public void setInQty(Integer inQty) {
        this.inQty = inQty;
    }

    public Integer getOutQty() {
        return outQty;
    }

    public void setOutQty(Integer outQty) {
        this.outQty = outQty;
    }

    public Integer getStocksQty() {
        return stocksQty;
    }

    public void setStocksQty(Integer stocksQty) {
        this.stocksQty = stocksQty;
    }

    public Timestamp getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Timestamp operatorTime) {
        this.operatorTime = operatorTime;
    }
}
