package com.apl.wms.warehouse.lib.feign;
import com.apl.db.adb.AdbHelper;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.pojo.vo.PackagingMaterialsInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hjr start
 * @date 2020/8/1 - 10:07
 */
@Component
public class PackMaterialsFeign {

    @Autowired
    AdbHelper adbHelper;

    public ResultUtil<PackagingMaterialsInfoVo> getPackMaterials(String sku) throws Exception {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("sku", sku);
        //查询包装材料信息
        PackagingMaterialsInfoVo packagingMaterialsInfoVo = adbHelper.queryObj("select id, sku, commodity_id, commodity_name, commodity_name_en," +
                        "spec_name, spec_name_en, unit_code, img_url, texture_name, texture_name_en, color_name, " +
                        "color_name_en, use_way, use_way_en, capacity, remark from packaging_materials where sku =:sku",
                paramMap, PackagingMaterialsInfoVo.class);


        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS.getCode() , CommonStatusCode.SAVE_SUCCESS.getMsg() , packagingMaterialsInfoVo);
    }
}
