package com.apl.wms.warehouse.lib.feign;
import com.apl.cache.AplCacheUtil;
import com.apl.db.abatis.AbatisExecutor;
import com.apl.db.adb.AdbContext;
import com.apl.db.adb.AdbPersistent;
import com.apl.lib.constants.CommonStatusCode;
import com.apl.lib.utils.ResultUtil;
import com.apl.wms.warehouse.lib.pojo.po.StocksHistoryPo;
import com.apl.wms.warehouse.lib.pojo.po.StorageLocalStocksHistoryPo;
import org.apache.ibatis.session.SqlSession;
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

    public SqlSession connectDb2(){

        // 创建数据库上下文

        SqlSession sqlSession = AbatisExecutor.sqlSessionFactory.openSession("wms_stocks_history", aplCacheUtil);
        return sqlSession;
    }

    //批量保存库存记录
    public ResultUtil<Integer> saveStocksHistoryPos(AdbContext dbInfo, List<StocksHistoryPo> stocksHistoryPos, List<StorageLocalStocksHistoryPo> storageLocalStocksHistoryPos) throws Exception
    {
         AdbPersistent.insertBatch(dbInfo, stocksHistoryPos, "stocks_history");

         AdbPersistent.insertBatch(dbInfo, stocksHistoryPos, "storage_local_stocks_history");

         return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS.getCode() , CommonStatusCode.SAVE_SUCCESS.getMsg() , stocksHistoryPos.size());
    }


}
