package com.apl.wms.warehouse.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 仓库详细
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WhDetailsInfoVo implements Serializable {


private static final long serialVersionUID=1L;


    // 仓库id
    private Long whId;

    // 联系人
    private String contact;

    // 座机
    private String tel;

    // 手机
    private String phone;

    // 邮箱
    private String email;

    // 国家简码
    private String countryCode;

    // 州
    private String state;

    // 城市
    private String city;

    // 邮编
    private String zipCode;

    // 街道
    private String street;

    // 地址1
    private String address1;

    // 地址2
    private String address2;

    // 地址3
    private String address3;

    // 公司名称
    private String companyName;

}
