package com.apl.wms.warehouse.service.impl;

import com.apl.cache.AplCacheUtil;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.join.JoinBase;
import com.apl.lib.join.JoinFieldInfo;
import com.apl.lib.join.JoinUtil;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtil;
import com.apl.lib.utils.StringUtil;
import com.apl.sys.lib.cache.CustomerCacheBo;
import com.apl.sys.lib.cache.JoinCustomer;
import com.apl.sys.lib.feign.InnerFeign;
import com.apl.sys.lib.feign.OuterFeign;
import com.apl.wms.warehouse.dao.CommodityMapper;
import com.apl.wms.warehouse.dao.CommodityPicMapper;
import com.apl.wms.warehouse.service.CacheService;
import com.apl.wms.warehouse.service.CommodityCategoryService;
import com.apl.wms.warehouse.service.CommodityService;
import com.apl.wms.warehouse.utils.JoinLocalCommodityCategory;
import com.apl.wms.warehouse.bo.CommodityReportBo;
import com.apl.wms.warehouse.dto.CommodityKeyDto;
import com.apl.wms.warehouse.po.CommodityCategoryPo;
import com.apl.wms.warehouse.po.CommodityPo;
import com.apl.wms.warehouse.utils.JasperHelper;
import com.apl.wms.warehouse.vo.CommodityInfoVo;
import com.apl.wms.warehouse.vo.CommodityListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-11
 */
@Service
@Slf4j
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, CommodityPo> implements CommodityService {

    //状态code枚举
    enum CommodityServiceCode {

        COMMODITY_IS_ALREADY_EXIST("COMMODITY_IS_ALREADY_EXIST","商品已经存在"),
        COMMODITY_IS_NOT_EXIST("COMMODITY_IS_NOT_EXIST","商品不存在"),
        CUSTOMER_IS_NOT_EXIST("CUSTOMER_IS_NOT_EXIST","客户不存在"),
        COMMODITY_CATEGORY_NOT_EXIST("COMMODITY_CATEGORY_NOT_EXIST","商品分类不存在"),
        COMMODITY_TYPE_NOT_TRUE("COMMODITY_TYPE_NOT_TRUE","商品类型错误"),
        ;

        private String code;
        private String msg;

        CommodityServiceCode(String code, String msg) {
             this.code = code;
             this.msg = msg;
        }
    }


    @Autowired
    OuterFeign outerFeign;


    @Autowired
    CommodityPicMapper commodityPicMapper;

    @Autowired
    CommodityCategoryService commodityCategoryService;

    @Autowired
    AplCacheUtil redisTemplate;

    @Autowired
    CacheService cacheService;

    @Autowired
    InnerFeign innerFeign;


    @Value("${apl.report.commodityReportPath}")
    private String commodityReportPath;


    @Override
    public ResultUtil<Long> add(CommodityPo commodity){

        commodity.setReviewStatus(2);// 审核状态 1已审核  2未审核
        //判断商品是否已经添加
        this.exists(0L, commodity.getSku(),  commodity.getCommodityName(),  commodity.getCommodityNameEn() );

        CommodityCategoryPo categoryPo = commodityCategoryService.getById(commodity.getCategory1Id());
        //判断商品分类是否存在
        if(categoryPo == null){
            return ResultUtil.APPRESULT(CommodityServiceCode.COMMODITY_CATEGORY_NOT_EXIST.code , CommodityServiceCode.COMMODITY_CATEGORY_NOT_EXIST.msg , null);
        }

        //查找分类的父id
        String pId = commodityCategoryService.getCategoryPid(commodity.getCategory1Id());
        //给商品分类 赋值
        setCategoryId(commodity , pId);


        ResultUtil<Integer> result = outerFeign.existOuterOrg(commodity.getCustomerId());

        if(result.code.equals(CommonStatusCode.SERVER_INVOKE_FAIL.code)){
            //服务调用失败
            return ResultUtil.APPRESULT(CommonStatusCode.SERVER_INVOKE_FAIL);
        }

        if(result.getData() == null){
            //外部组织  客户 不存在
            return ResultUtil.APPRESULT(CommodityServiceCode.CUSTOMER_IS_NOT_EXIST.code ,CommodityServiceCode.CUSTOMER_IS_NOT_EXIST.msg , null);
        }

        commodity.setUnitCode(commodity.getUnitCode().toUpperCase());

        Integer flag = baseMapper.insert(commodity);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , commodity.getId());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , null);
    }

    /**
     * @Desc: 给商品设置 分类id
     * @Author: CY
     * @Date: 2019/12/14 12:23
     */
    private void setCategoryId(CommodityPo commodity, String pId) {

        String ids = pId.substring(2);
        List<Long> longs = StringUtil.stringToLongList(ids);

        commodity.setCategory1Id(longs.get(0));
        if (longs.size() == 2) {
            commodity.setCategory2Id(longs.get(1));
        }
        if (longs.size() == 3) {
            commodity.setCategory2Id(longs.get(1));
            commodity.setCategory2Id(longs.get(2));
        }
        if (longs.size() == 4) {
            commodity.setCategory2Id(longs.get(1));
            commodity.setCategory2Id(longs.get(2));
            commodity.setCategory2Id(longs.get(3));
        }
        if (longs.size() == 5) {
            commodity.setCategory2Id(longs.get(1));
            commodity.setCategory2Id(longs.get(2));
            commodity.setCategory2Id(longs.get(3));
            commodity.setCategory2Id(longs.get(4));
        }
    }


    @Override
    public ResultUtil<Boolean> updById(CommodityPo commodity){
        if(baseMapper.existsOwm(commodity.getId(), commodity.getCustomerId()) == null){
            //检测此客户是否拥有此商品
            return ResultUtil.APPRESULT(CommodityServiceCode.COMMODITY_IS_NOT_EXIST.code , CommodityServiceCode.COMMODITY_IS_NOT_EXIST.msg , false);
        }

        this.exists(commodity.getId() , commodity.getSku(),  commodity.getCommodityName(),  commodity.getCommodityNameEn() );

        Integer flag = baseMapper.updateById(commodity);
        if(flag.equals(1)){
            return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_FAIL , false);
    }


    @Transactional
    @Override
    public ResultUtil<Boolean> delById(Long id, Long customerId){

        if(baseMapper.existsOwm(id, customerId) == null){
            //检测此客户是否拥有此商品
            return ResultUtil.APPRESULT(CommodityServiceCode.COMMODITY_IS_NOT_EXIST.code , CommodityServiceCode.COMMODITY_IS_NOT_EXIST.msg , false);
        }
        //删除图片
        commodityPicMapper.delByCommodityId(id);

        boolean flag = removeById(id);
        if(flag){
            return ResultUtil.APPRESULT(CommonStatusCode.DEL_SUCCESS , true);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.DEL_FAIL , false);
    }


    @Override
    public ResultUtil<CommodityInfoVo> selectById(Long id, Long customerId){

        //将商品赋值给 info
        CommodityInfoVo commodityInfoVo = baseMapper.getById(id, customerId);
        if(null!=commodityInfoVo) {
            JoinCustomer joinCustomer = new JoinCustomer(1, innerFeign, redisTemplate);
            CustomerCacheBo customerCacheBo = joinCustomer.getEntity(commodityInfoVo.getCustomerId());
            if (customerCacheBo != null)
                commodityInfoVo.setCustomerName(customerCacheBo.getCustomerName());
        }

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, commodityInfoVo);
    }


    static JoinFieldInfo joinCommodityFieldInfo = null; //缓存联客户表反射字段
    static JoinFieldInfo joinCommodityCategoryFieldInfo = null; //缓存商品种类反射字段
    @Override
    public ResultUtil<Page<CommodityListVo>> getList(PageDto pageDto, CommodityKeyDto keyDto)  throws Exception{

        Page<CommodityListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<CommodityListVo> list = baseMapper.getList(page , keyDto);

        List<JoinBase> joinTabs = new ArrayList<>();
        JoinCustomer joinCustomer = new JoinCustomer(1, innerFeign, redisTemplate);
        if(null!=joinCommodityFieldInfo) {
            //已经缓存客户反射字段
            joinCustomer.setJoinFieldInfo(joinCommodityFieldInfo);
        }
        else{
            //缓存客户反射字段，此代码只执行一次
            joinCustomer.addField("customerId",  Long.class, "customerName",  String.class);
            joinCommodityFieldInfo = joinCustomer.getJoinFieldInfo();
        }
        joinTabs.add(joinCustomer);

        JoinLocalCommodityCategory joinLocalCommodityCategory = new JoinLocalCommodityCategory(1, cacheService, redisTemplate);
        if(null!=joinCommodityCategoryFieldInfo) {
            //已经缓存品类反射字段
            joinLocalCommodityCategory.setJoinFieldInfo(joinCommodityCategoryFieldInfo);
        }
        else{
            //缓存品类反射字段，此代码只执行一次
            joinLocalCommodityCategory.addField("categoryId",  Long.class, "categoryName",  String.class);
            joinCommodityCategoryFieldInfo = joinLocalCommodityCategory.getJoinFieldInfo();
        }
        joinTabs.add(joinLocalCommodityCategory);

        JoinUtil.join(list, joinTabs);

        page.setRecords(list);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    static JoinFieldInfo joinCommodityReportBo = null;

    @Override
    public void print(String id, Long customerId)  throws Exception{

        List<Long> ids = StringUtil.stringToLongList(id);
        List<CommodityReportBo> list = baseMapper.getCommodityReportBarcode(ids, customerId);

        List<JoinBase> joinTabs = new ArrayList<>();
        JoinCustomer joinCustomer = new JoinCustomer(1, innerFeign, redisTemplate);
        if(null!=joinCommodityReportBo) {
            //已经缓存字段
            joinCustomer.setJoinFieldInfo(joinCommodityReportBo);
        }
        else{
            //缓存反射字段，此代码只执行一次
            joinCustomer.addField("customerId",  Long.class, "customerName",  String.class);
            joinCommodityReportBo = joinCustomer.getJoinFieldInfo();
        }
        joinTabs.add(joinCustomer);

        JoinUtil.join(list, joinTabs);

        String sysPath = System.getProperty("user.dir").replace("\\","/");
        System.out.println(sysPath + "/" + commodityReportPath);
        File reportFile = new File(sysPath + "/" + commodityReportPath);
        JasperHelper.export("pdf", "print.pdf", reportFile, CommonContextHolder.getRequest(), CommonContextHolder.httpServletResponse(), new HashMap(), list);
    }


    public void print2(String id, Long customerId)  throws Exception{

        List<Long> ids = StringUtil.stringToLongList(id);
        List<CommodityReportBo> list = baseMapper.getCommodityReportBarcode(ids, customerId);

        String sysPath = System.getProperty("user.dir").replace("\\","/");
        File reportFile = new File(sysPath + "/" + commodityReportPath);
        JasperHelper.export("pdf", "print.pdf", reportFile, CommonContextHolder.getRequest(), CommonContextHolder.httpServletResponse(), new HashMap(), list);
    }



    void exists(Long id,  String sku,   String commodityName,   String commodityNameEn ) {

        List<CommodityInfoVo> list = baseMapper.exists(id, sku,  commodityName,  commodityNameEn );
        if (!CollectionUtils.isEmpty(list)) {
            for(CommodityInfoVo  commodityInfoVo : list) {
                if(commodityInfoVo.getSku().equals(sku))
                    throw new AplException("SKU_EXIST", "sku已经存在");
                if(commodityInfoVo.getCommodityName().equals(commodityName))
                    throw new AplException("COMMODITY_NAME_EXIST", "commodityName已经存在");
                if(commodityInfoVo.getCommodityNameEn().equals(commodityNameEn))
                    throw new AplException("COMMODITY_NAME_EN_EXIST", "commodityNameEn已经存在");
            }
        }
    }
}
