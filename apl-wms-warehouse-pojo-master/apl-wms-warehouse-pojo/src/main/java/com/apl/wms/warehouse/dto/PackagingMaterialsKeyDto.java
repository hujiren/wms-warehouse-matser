package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 包装材料 查询参数
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="包装材料 查询参数", description="包装材料 查询参数")
public class PackagingMaterialsKeyDto implements Serializable {

    private static final long serialVersionUID=1L;


    @ApiModelProperty(name = "saleStatus" , value = "销售状态 0:全部 1:上架   2：下架", required = true)
    @NotNull(message = "销售状态不能为空")
    @Range(min = 0, max = 2, message = "销售状态值不合法")
    private Integer saleStatus;


    @ApiModelProperty(name = "reviewStatus" , value = "审核状态 0:全部 1：已审核   2：未审核")
    @Range(min = 0, max = 2 , message = "审核状态值不合法")
    private Integer reviewStatus;


    @ApiModelProperty(name = "keyword", value = "关键词")
    @Length(max = 50, message = "关键词长度不能超过50")
    private String keyword;


    public String getKeyword() {
        if (keyword == null|| keyword.trim().equals("")){
            return null;
        }
        return keyword;
    }

}
