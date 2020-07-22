package com.apl.wms.warehouse.lib.feign;
import com.alibaba.druid.pool.DruidDataSource;
import com.apl.cache.AplCacheUtil;
import com.apl.db.adb.AdbContext;
import com.apl.db.adb.AdbMySqlGenerate;
import com.apl.db.adb.AdbPersistent;
import com.apl.db.datasource.DataSourceConfig;
import com.apl.db.datasource.DynamicDataSource;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.security.SecurityUser;
import com.apl.lib.utils.CommonContextHolder;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.pojo.po.StocksHistoryPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author hjr start
 * @date 2020/7/9 - 10:11
 */
@Component
public class StocksHistoryFeign {


    @Autowired
    AplCacheUtil aplCacheUtil;

    public AdbContext connectDb(){

        // 创建数据库上下文
        AdbContext adbContext = new AdbContext("wms_stocks_history", aplCacheUtil);

        return adbContext;
    }

    //批量保存库存记录
    public ResultUtil<Integer> saveStocksHistoryPos(AdbContext dbInfo, List<StocksHistoryPo> stocksHistoryPos) throws Exception
    {
        AdbPersistent.insertBatch(dbInfo, stocksHistoryPos, "stocks_history");

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS.getCode() , CommonStatusCode.SAVE_SUCCESS.getMsg() , stocksHistoryPos.size());
    }



}
