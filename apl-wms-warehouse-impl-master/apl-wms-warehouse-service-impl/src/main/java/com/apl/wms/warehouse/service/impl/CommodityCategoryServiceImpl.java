package com.apl.wms.warehouse.service.impl;
import com.apl.cache.AplCacheUtil;
import com.apl.lib.exception.AplException;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.mapper.CommodityCategoryMapper;
import com.apl.wms.warehouse.vo.CommodityCategoryInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.apl.lib.constants.CommonStatusCode;

import com.apl.wms.warehouse.service.CommodityCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.apl.wms.warehouse.po.CommodityCategoryPo;
import com.apl.wms.warehouse.vo.CommodityCategoryListVo;
import com.apl.wms.warehouse.dto.CommodityCategoryKeyDto;

import java.util.List;
import com.apl.lib.pojo.dto.PageDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;


/**
 * <p>
 * 商品种类 服务实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-10
 */
@Service
@Slf4j
public class CommodityCategoryServiceImpl extends ServiceImpl<CommodityCategoryMapper, CommodityCategoryPo> implements CommodityCategoryService {

    //状态code枚举
    enum CommodityCategoryServiceCode {

        CATEGORY_IS_ALREADY_EXIST("CATEGORY_IS_ALREADY_EXIST" ,"商品分类已经存在"),
        PARENT_CATEGORY_IS_NOT_EXIST("PARENT_CATEGORY_IS_NOT_EXIST" ,"父级分类不存在"),
        CATEGORY_IS_OVER_LIMIT("PARENT_CATEGORY_IS_NOT_EXIST" ,"添加的分类 层级超出了限制"),
        EXIST_CHILD_CATEGORY("EXIST_CHILD_CATEGORY" ,"存在子分类 ，不能进行删除"),
        ;

        private String code;
        private String msg;

        CommodityCategoryServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }


    @Autowired
    AplCacheUtil redisTemplate;

    @Override
    public ResultUtil<Integer> add(Long parentId , String categoryName , String categoryEnName){

        Integer numberOfPlies = 0;

        //判断是否重复添加
        checkCategory(null  , categoryName , categoryEnName);

        if(!parentId.equals(0)){
            //查找 父级 分类
            CommodityCategoryInfoVo findCommodity = baseMapper.getById(parentId);
            //如果上级 分类不存在，则不可以进行添加
            if(findCommodity == null){

                return ResultUtil.APPRESULT(CommodityCategoryServiceCode.PARENT_CATEGORY_IS_NOT_EXIST.code , CommodityCategoryServiceCode.PARENT_CATEGORY_IS_NOT_EXIST.msg , null);
            }

            //如果层级数超过了5层，则不能进行添加
            if(findCommodity.getNumberOfPlies() == 5){
                log.info("添加的分类 层级超出了限制");
                return ResultUtil.APPRESULT(CommodityCategoryServiceCode.CATEGORY_IS_OVER_LIMIT.code , CommodityCategoryServiceCode.CATEGORY_IS_OVER_LIMIT.msg , null);

            }
            numberOfPlies = findCommodity.getNumberOfPlies();
        }
        CommodityCategoryPo commodityCategory = new CommodityCategoryPo();
        commodityCategory.setCategoryName(categoryName);
        commodityCategory.setCategoryNameEn(categoryEnName);
        commodityCategory.setParentId(parentId);
        //层数加一
        commodityCategory.setNumberOfPlies(numberOfPlies + 1);

        Integer flag = baseMapper.insert(commodityCategory);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , commodityCategory.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }


    @Override
    public ResultUtil<Boolean> updById(Long categoryId , String categoryName , String categoryEnName){

        //判断是否重复添加
        checkCategory(categoryId , categoryName , categoryEnName);

        CommodityCategoryPo commodityCategory = new CommodityCategoryPo();
        commodityCategory.setId(categoryId);
        commodityCategory.setCategoryName(categoryName);
        commodityCategory.setCategoryNameEn(categoryEnName);

        Integer flag = baseMapper.updateById(commodityCategory);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }
        
        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , false);
    }


    @Override
    public ResultUtil<Boolean> delById(Long id){

        List<CommodityCategoryInfoVo> lists = baseMapper.getChildCategory(id);

        if(!CollectionUtils.isEmpty(lists)){
            return ResultUtil.APPRESULT(CommodityCategoryServiceCode.EXIST_CHILD_CATEGORY.code , CommodityCategoryServiceCode.EXIST_CHILD_CATEGORY.msg , null);
        }
        boolean flag = removeById(id);
        if(flag){
            return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.DEL_FAIL , false);
    }


    @Override
    public ResultUtil<CommodityCategoryInfoVo> selectById(Long id){

        CommodityCategoryInfoVo commodityCategoryInfoVo = baseMapper.getById(id);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, commodityCategoryInfoVo);
    }


    @Override
    public ResultUtil<Page<CommodityCategoryListVo>> getList(PageDto pageDto, CommodityCategoryKeyDto keyDto){

        Page<CommodityCategoryListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<CommodityCategoryListVo> list = baseMapper.getList(page , keyDto);
        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }

    @Override
    public String getCategoryNames(Long categoryId) {

        return baseMapper.getCategoryNames(categoryId);
    }

    @Override
    public String getCategoryPid(Long categoryId) {
        return baseMapper.getCategoryPid(categoryId);
    }


    /**
     * @Desc: 判断分类是否存在
     * @Author: CY
     * @Date: 2019/12/13 14:13
     */
    private void checkCategory(Long categoryId , String categoryName , String categoryNameEn){

        List<CommodityCategoryInfoVo> categorys = baseMapper.exists(categoryId ,categoryName, categoryNameEn);
        if (!CollectionUtils.isEmpty(categorys)) {
            for (CommodityCategoryInfoVo category : categorys) {
                if(category.getCategoryName().equals(categoryName))
                    throw new AplException("CATEGORY_NAME_EXIST", "分类名称已经存在");
                else if(category.getCategoryNameEn().equals(categoryNameEn))
                    throw new AplException("CATEGORY_EN_NAME_EXIST", "分类英文名称已经存在");
            }

        }

    }




}
