package com.apl.wms.warehouse.lib.pojo.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

/**
 * @author hjr start
 * @date 2020/7/7 - 10:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库订单分配库存", description="出库订单分配库存")
public class OutOrderAlloStocksBo implements Serializable {

    private Long outOrderId;

    private String tranId;

    //private List<CompareStorageLocalStocksBo> alloStocksBos;
}
