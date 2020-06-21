package com.apl.wms.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author CY
 * @since 2019-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CommodityKeyDto对象", description="商品")
public class CommodityKeyDto implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(name = "customerId" , value = "客户id")
    private Long customerId;

    @ApiModelProperty(name = "brandId" , value = "品牌id")
    private Integer brandId;

    @ApiModelProperty(name = "categoryId" , value = "品类id")
    private Integer categoryId;


    @ApiModelProperty(name = "isCorrespondence" , value = "是否含电 1:含电 0：不含电")
    private Integer isCorrespondence;


    @ApiModelProperty(name = "saleStatus" , value = "销售状态 1:上架   2：下架", required = true)
    @NotNull(message = "销售状态不能为空")
    @Range(min = 0, max = 2, message = "销售状态值不合法")
    private Integer saleStatus;


    @ApiModelProperty(name = "reviewStatus" , value = "审核状态 1：已审核   2：未审核")
    @Range(min = 0, max = 2 , message = "审核状态值不合法")
    private Integer reviewStatus;



    @ApiModelProperty(name = "keyword" , value = "keyword关键字")
    private String keyword;

    public Integer getIsCorrespondence() {
        if (isCorrespondence == null|| isCorrespondence < 0){
            return null;
        }
        return isCorrespondence;
    }

    public Integer getSaleStatus() {
        if (saleStatus == null|| saleStatus < 0){
            return null;
        }
        return saleStatus;
    }

    public Integer getReviewStatus() {
        if (reviewStatus == null|| reviewStatus < 0){
            return null;
        }
        return reviewStatus;
    }

    public String getKeyword() {
        if (keyword == null|| keyword.trim().equals("")){
            return null;
        }

        return keyword;
    }
}
