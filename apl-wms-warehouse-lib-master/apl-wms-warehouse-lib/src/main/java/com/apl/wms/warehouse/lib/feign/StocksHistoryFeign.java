package com.apl.wms.warehouse.lib.feign;
import com.apl.db.adb.AdbHelper;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.pojo.po.StocksHistoryPo;
import com.apl.wms.warehouse.lib.pojo.po.StorageLocalStocksHistoryPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author hjr start
 * @date 2020/7/9 - 10:11
 */
@Component
public class StocksHistoryFeign {


    @Autowired
    AdbHelper adbHelper;

    //批量保存库存记录
    public ResultUtil<Integer> saveStocksHistoryPos(List<StocksHistoryPo> stocksHistoryPos, List<StorageLocalStocksHistoryPo> storageLocalStocksHistoryPos) throws Exception
    {
        adbHelper.insertBatch(stocksHistoryPos, "stocks_history");

        adbHelper.insertBatch(stocksHistoryPos, "storage_local_stocks_history");

         return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS.getCode() , CommonStatusCode.SAVE_SUCCESS.getMsg() , stocksHistoryPos.size());

    }


}
