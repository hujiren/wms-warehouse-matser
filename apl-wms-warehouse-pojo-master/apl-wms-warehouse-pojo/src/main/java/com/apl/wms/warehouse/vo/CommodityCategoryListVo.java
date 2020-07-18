package com.apl.wms.warehouse.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 商品种类
 * </p>
 *
 * @author cy
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommodityCategoryListVo implements Serializable {


private static final long serialVersionUID=1L;


    // 商品种类id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;



    // 商品分类名称
    private String categoryName;



    // 商品分类英文名称
    private String categoryNameEn;



    // 商品分类的父级id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;



    // 菜单层级数
    private Integer numberOfPlies;






}
