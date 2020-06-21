package com.apl.wms.warehouse.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 仓库详细
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wh_details")
@ApiModel(value = "WhDetailsPo对象", description = "仓库详细")
public class WhDetailsPo extends Model<WhDetailsPo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "whId", value = "仓库id", required = true)
    @Min(value = 0, message = "仓库id 不能小于1")
    @TableId()
    private Long whId;

    @ApiModelProperty(name = "contact", value = "联系人", required = true)
    @NotEmpty(message = "联系人 不能为空")
    private String contact;

    @ApiModelProperty(name = "tel", value = "座机", required = true)
    @NotEmpty(message = "座机 不能为空")
    private String tel;

    @ApiModelProperty(name = "phone", value = "手机")
    private String phone;

    @ApiModelProperty(name = "email", value = "邮箱")
    private String email;

    @ApiModelProperty(name = "countryCode", value = "国家简码", required = true)
    @NotEmpty(message = "国家简码 不能为空")
    private String countryCode;

    @ApiModelProperty(name = "state", value = "州", required = true)
    @NotEmpty(message = "州 不能为空")
    private String state;

    @ApiModelProperty(name = "city", value = "城市", required = true)
    @NotEmpty(message = "城市 不能为空")
    private String city;

    @ApiModelProperty(name = "zipCode", value = "邮编")
    private String zipCode;

    @ApiModelProperty(name = "street", value = "街道", required = true)
    @NotEmpty(message = "街道 不能为空")
    private String street;

    @ApiModelProperty(name = "address1", value = "地址1", required = true)
    @NotEmpty(message = "地址1 不能为空")
    private String address1;

    @ApiModelProperty(name = "address2", value = "地址2")
    private String address2;

    @ApiModelProperty(name = "address3", value = "地址3")
    private String address3;

    @ApiModelProperty(name = "companyName", value = "公司名称", required = true)
    @NotEmpty(message = "公司名称 不能为空")
    private String companyName;

    @ApiModelProperty(name = "localTimeZone", value = "仓库所在时区", required = true)
    @NotNull(message = "仓库所在时区 不能为空")
    private Integer localTimeZone;
}
