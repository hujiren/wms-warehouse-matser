package com.apl.wms.warehouse.lib.feign;

import com.apl.cache.AplCacheUtil;
import com.apl.db.abatis.AbatisExecutor;
import com.apl.db.adb.AdbContext;
import com.apl.db.adb.AdbPersistent;
import com.apl.db.adb.AdbQuery;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.pojo.vo.PackagingMaterialsInfoVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjr start
 * @date 2020/8/1 - 10:07
 */
@Component
public class PackMaterialsFeign {

    @Autowired
    AplCacheUtil aplCacheUtil;

    public AdbContext connectDb(){

        // 创建数据库上下文
        AdbContext adbContext = new AdbContext("wms_wh", aplCacheUtil);

        return adbContext;
    }

    public ResultUtil<PackagingMaterialsInfoVo> getPackMaterials(AdbContext adbContext, String sku) throws Exception {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("sku", sku);
        //查询包装材料信息
        PackagingMaterialsInfoVo packagingMaterialsInfoVo = AdbQuery.queryObj(adbContext, "select id, sku, commodity_id, commodity_name, commodity_name_en," +
                        "spec_name, spec_name_en, unit_code, img_url, texture_name, texture_name_en, color_name, " +
                        "color_name_en, use_way, use_way_en, capacity, remark from packaging_materials where sku =:sku",
                paramMap, PackagingMaterialsInfoVo.class);


        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS.getCode() , CommonStatusCode.SAVE_SUCCESS.getMsg() , packagingMaterialsInfoVo);
    }
}
