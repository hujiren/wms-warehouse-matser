package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 网店铺 持久化对象
 * </p>
 *
 * @author arran
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("store")
@ApiModel(value="网店铺 持久化对象", description="网店铺 持久化对象")
public class StoreApiPo extends Model<StoreApiPo> {

    @TableId(value = "id", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(name = "customerId" , value = "客户id" , hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long customerId;

    @ApiModelProperty(name = "apiConfig" , value = "API参数值")
    private String apiConfig;

    @ApiModelProperty(name = "isAutoCrSyncOrder" , value = "是否自动创建同步订单任务  1是  2否")
    private Integer isAutoCrSyncOrder;

    @ApiModelProperty(name = "autoCrSyncTimeZone" , value = "创建同步订单任务时区")
    private Integer autoCrSyncTimeZone;

    @ApiModelProperty(name = "autoCrSyncOrderTime" , value = "自动同步订单任务创建时间")
    private String autoCrSyncOrderTime;

    @ApiModelProperty(name = "syncOrderStartTime" , value = "同步订单起始时间(不要日期只要时间, 字符型)")
    private String syncOrderStartTime;

    @ApiModelProperty(name = "syncOrderEndTime" , value = "同步订单截止时间(不要日期只要时间, 字符型)" )
    private String syncOrderEndTime;

    private static final long serialVersionUID=1L;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
