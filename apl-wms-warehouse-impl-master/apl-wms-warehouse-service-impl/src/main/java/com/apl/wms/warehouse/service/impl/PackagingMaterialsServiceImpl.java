package com.apl.wms.warehouse.service.impl;

import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.exception.AplException;
import com.apl.lib.join.JoinBase;
import com.apl.lib.join.JoinFieldInfo;
import com.apl.lib.join.JoinUtils;
import com.apl.lib.pojo.dto.PageDto;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtils;
import com.apl.lib.utils.SnowflakeIdWorker;
import com.apl.lib.utils.StringUtil;
import com.apl.sys.lib.cache.JoinCustomer;
import com.apl.sys.lib.feign.InnerFeign;
import com.apl.sys.lib.feign.OuterFeign;
import com.apl.wms.warehouse.lib.pojo.bo.PackagingMaterialsCountBo;
import com.apl.wms.warehouse.lib.pojo.vo.OrderCountVo;
import com.apl.wms.warehouse.mapper.CommodityPicMapper;
import com.apl.wms.warehouse.mapper.PackagingMaterialsMapper;
import com.apl.wms.warehouse.service.CommodityCategoryService;
import com.apl.wms.warehouse.service.PackagingMaterialsService;
import com.apl.wms.warehouse.utils.JasperHelper;
import com.apl.wms.warehouse.bo.CommodityReportBo;
import com.apl.wms.warehouse.dto.PackagingMaterialsKeyDto;
import com.apl.wms.warehouse.po.PackagingMaterialsPo;
import com.apl.wms.warehouse.vo.CommodityInfoVo;
import com.apl.wms.warehouse.vo.PackagingMaterialsInfoVo;
import com.apl.wms.warehouse.vo.PackagingMaterialsListVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;


/**
 * <p>
 * 包装材料 service实现类
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
@Service
@Slf4j
public class PackagingMaterialsServiceImpl extends ServiceImpl<PackagingMaterialsMapper, PackagingMaterialsPo> implements PackagingMaterialsService {

    //状态code枚举
    enum PackagingMaterialsServiceCode {

        COMMODITY_IS_ALREADY_EXIST("COMMODITY_IS_ALREADY_EXIST","商品已经存在"),
        COMMODITY_IS_NOT_EXIST("COMMODITY_IS_NOT_EXIST","包装材料不存在"),
        CUSTOMER_IS_NOT_EXIST("CUSTOMER_IS_NOT_EXIST","客户不存在"),
        COMMODITY_CATEGORY_NOT_EXIST("COMMODITY_CATEGORY_NOT_EXIST","商品分类不存在"),
        COMMODITY_TYPE_NOT_TRUE("COMMODITY_TYPE_NOT_TRUE","商品类型错误"),
        ;

        private String code;
        private String msg;

        PackagingMaterialsServiceCode(String code, String msg) {
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
    RedisTemplate redisTemplate;

    @Autowired
    InnerFeign innerFeign;

    @Autowired
    JoinUtils joinUtils;

    @Value("${apl.report.commodityReportPath}")
    private String commodityReportPath;


    @Override
    public ResultUtils<Map<String , List<PackagingMaterialsCountBo>>> getCommodityPackMaterials(Long orderId) throws Exception {

        OrderCountVo orderCountVo = (OrderCountVo) redisTemplate.opsForValue().get("packaging:" + orderId);

        Map<String , List<PackagingMaterialsCountBo>> commodityPackMap = new HashMap<>();
        if(orderCountVo != null){
            List<OrderCountVo.OrderItem> orderItems = orderCountVo.getOrderItems();

            Map<String, List<OrderCountVo.OrderItem>> commodityOrderCount = JoinUtils.listGrouping(orderItems, "commodityId");

            for (Map.Entry<String, List<OrderCountVo.OrderItem>> orderCountEntry : commodityOrderCount.entrySet()) {
                Integer commodityCount = 0;
                for (OrderCountVo.OrderItem orderItem : orderCountEntry.getValue()) {
                    commodityCount = commodityCount + orderItem.getOrderQty();
                }
                List<PackagingMaterialsCountBo> packagingMaterials = baseMapper.getPackMaterialsByCommodityId(Long.parseLong(orderCountEntry.getKey()) , 1 , 1);

                if(!CollectionUtils.isEmpty(packagingMaterials)){
                    for (PackagingMaterialsCountBo packagingMaterial : packagingMaterials) {
                        packagingMaterial.setCount(((int) Math.ceil(Double.parseDouble(String.valueOf(commodityCount)) / Double.parseDouble(String.valueOf(packagingMaterial.getCapacity())))));
                    }
                }

                commodityPackMap.put(orderCountEntry.getKey() , packagingMaterials);
            }
            redisTemplate.opsForValue().set("packaging:count:" + orderId , commodityPackMap);
        }

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS , commodityPackMap);
    }

    @Override
    public ResultUtils<Long> add(PackagingMaterialsPo packagingMaterialsPo){

        packagingMaterialsPo.setId(SnowflakeIdWorker.generateId());
        packagingMaterialsPo.setReviewStatus(2);// 审核状态 1已审核  2未审核
        packagingMaterialsPo.setSaleStatus(2);

        //判断商品是否已经添加
        this.exists(0L, packagingMaterialsPo.getSku(),  packagingMaterialsPo.getCommodityName(),  packagingMaterialsPo.getCommodityNameEn());

        packagingMaterialsPo.setUnitCode(packagingMaterialsPo.getUnitCode().toUpperCase());

        packagingMaterialsPo.setId(SnowflakeIdWorker.generateId());
        baseMapper.insert(packagingMaterialsPo);

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_SUCCESS , packagingMaterialsPo.getId());
    }

    @Override
    public ResultUtils<Boolean> updById(PackagingMaterialsPo packagingMaterialsPo){

        this.exists(packagingMaterialsPo.getId(), packagingMaterialsPo.getSku(),  packagingMaterialsPo.getCommodityName(),  packagingMaterialsPo.getCommodityNameEn() );

        baseMapper.updateById(packagingMaterialsPo);

        return ResultUtils.APPRESULT(CommonStatusCode.SAVE_FAIL , true);
    }


    @Transactional
    @Override
    public ResultUtils<Boolean> delById(Long id){

        if(baseMapper.existsOwm(id) == null){
            //检测此客户是否拥有此商品
            return ResultUtils.APPRESULT(PackagingMaterialsServiceCode.COMMODITY_IS_NOT_EXIST.code , PackagingMaterialsServiceCode.COMMODITY_IS_NOT_EXIST.msg , false);
        }
        //删除图片
        commodityPicMapper.delByCommodityId(id);

        boolean flag = removeById(id);
        if(flag){
            return ResultUtils.APPRESULT(CommonStatusCode.DEL_SUCCESS , true);
        }

        return ResultUtils.APPRESULT(CommonStatusCode.DEL_FAIL , false);
    }


    @Override
    public ResultUtils<CommodityInfoVo> selectById(Long id){

        //将商品赋值给 info
        PackagingMaterialsInfoVo packagingMaterialsInfoVo = baseMapper.getById(id);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, packagingMaterialsInfoVo);
    }

    static JoinFieldInfo joinCommodityFieldInfo = null; //缓存联客户表反射字段
    @Override
    public ResultUtils<Page<PackagingMaterialsListVo>> getList(PageDto pageDto, PackagingMaterialsKeyDto packagingMaterialsKeyDto)  throws Exception{

        Page<PackagingMaterialsListVo> page = new Page();
        page.setCurrent(pageDto.getPageIndex());
        page.setSize(pageDto.getPageSize());

        List<PackagingMaterialsListVo> list = baseMapper.getList(page , packagingMaterialsKeyDto);

        List<JoinBase> joinTabs = new ArrayList<>();
        JoinCustomer joinCustomer = new JoinCustomer(1, innerFeign, redisTemplate);
        if(null!=joinCommodityFieldInfo) {
            //已经缓存字段
            joinCustomer.setJoinFieldInfo(joinCommodityFieldInfo);
        }
        else{
            //缓存反射字段，此代码只执行一次
            joinCustomer.addField("customerId",  Long.class, "customerName",  String.class);
            joinCommodityFieldInfo = joinCustomer.getJoinFieldInfo();
        }

        joinTabs.add(joinCustomer);

        JoinUtils.join(list, joinTabs);

        page.setRecords(list);

        return ResultUtils.APPRESULT(CommonStatusCode.GET_SUCCESS, page);
    }


    static JoinFieldInfo joinCommodityReportBo = null;

    @Override
    public void print(String id)  throws Exception{

        List<Long> ids = StringUtil.stringToLongList(id);
        List<CommodityReportBo> list = baseMapper.getCommodityReportBarcode(ids);

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

        JoinUtils.join(list, joinTabs);

        String sysPath = System.getProperty("user.dir").replace("\\","/");
        System.out.println(sysPath + "/" + commodityReportPath);
        File reportFile = new File(sysPath + "/" + commodityReportPath);
        JasperHelper.export("pdf", "print.pdf", reportFile, CommonContextHolder.getRequest(), CommonContextHolder.httpServletResponse(), new HashMap(), list);
    }


    void exists(Long id,  String sku,   String commodityName,   String commodityNameEn ) {

        List<PackagingMaterialsInfoVo> list = baseMapper.exists(id, sku,  commodityName,  commodityNameEn );
        if (!CollectionUtils.isEmpty(list)) {
            for(PackagingMaterialsInfoVo  packagingMaterialsInfoVo : list) {

                if(packagingMaterialsInfoVo.getSku().equals(sku))
                    throw new AplException("SKU_EXIST", "sku已经存在");
                if(packagingMaterialsInfoVo.getCommodityName().equals(commodityName))
                    throw new AplException("COMMODITY_NAME_EXIST", "commodityName已经存在");
                if(packagingMaterialsInfoVo.getCommodityNameEn().equals(commodityNameEn))
                    throw new AplException("COMMODITY_NAME_EN_EXIST", "commodityNameEn已经存在");
            }
        }
    }

}