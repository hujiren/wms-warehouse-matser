package com.apl.wms.warehouse.lib.feign;

import com.apl.db.adb.AdbHelper;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalInfoVo;
import com.apl.wms.warehouse.po.StocksPo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjr start
 * @date 2020/7/31 - 19:52
 */
@Component
public class StocksOperatorFeign {

    @Autowired
    AdbHelper adbHelper;

    //批量更新库位实际库存和总库存实际库存
    public ResultUtil<Integer> batchUpdateStorageLocal(List<StorageLocalPo> newStorageLocalList,
                                                    List<StocksPo> newStocksPoList) throws Exception {

        //批量更新库位和总库存
        int[] intArray = adbHelper.updateBatch(newStorageLocalList, "storage_local", "id");

        int[] intArray2 = adbHelper.updateBatch(newStocksPoList, "stocks", "commodity_id");

        int sum = 0;
        for (int i : intArray) {
            sum += i;
        }

        int sum2 = 0;
        for (int i : intArray2) {
            sum2 += i;
        }
        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS.getCode() , CommonStatusCode.SAVE_SUCCESS.getMsg() , sum + sum2);

    }
    
    //根据库位ids查询库位对应实际库存
    public ResultUtil<List<StorageLocalInfoVo>> getStorageLocalMap(String ids) throws Exception{

        Map<String, Object> paramMaps = new HashMap();
        paramMaps.put("ids", ids);
        List<StorageLocalInfoVo> storageLocalList = adbHelper.queryList(
                "select id, reality_count from storage_local where id in (:ids)",
                paramMaps,
                StorageLocalInfoVo.class);

        return ResultUtil.APPRESULT(CommonStatusCode.GET_SUCCESS.code, CommonStatusCode.GET_SUCCESS.msg, storageLocalList);

    }


}

