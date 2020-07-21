package com.apl.wms.warehouse.lib.feign;

import com.alibaba.druid.pool.DruidDataSource;
import com.apl.cache.AplCacheUtil;
import com.apl.db.adb.AdbContext;
import com.apl.db.adb.AdbMySqlGenerate;
import com.apl.db.adb.AdbPersistent;
import com.apl.db.adb.MySqlGenerate;
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
    AplCacheUtil redisTemplate;

    static String insertSql = null;

    public AdbContext connectDb(){
        // 创建数据库连接信息
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();

        DruidDataSource druidDataSource = DynamicDataSource.getDruidDataSource(securityUser.getTenantGroup(),
                DataSourceConfig.sysProduct,
                "wms_stocks_history",
                redisTemplate);
        AdbContext dbInfo = new AdbContext(druidDataSource);
        dbInfo.setTenantValue(securityUser.getInnerOrgId());
        AdbContext.tenantIdName = "inner_org_id";

        return dbInfo;
    }

    //批量保存库存记录
    public ResultUtil<Integer> saveStocksHistoryPos(AdbContext dbInfo, List<StocksHistoryPo> stocksHistoryPos) throws Exception
    {
        //if(null==insertSql)
        {
            // 首次调用生成插入SQL语句
            insertSql = AdbMySqlGenerate.creteInsertSql(dbInfo, stocksHistoryPos.get(0), "stocks_history");
        }

        for (StocksHistoryPo stocksHistoryPo : stocksHistoryPos) {
            AdbPersistent.insert(dbInfo, insertSql, stocksHistoryPo);
        }

        return ResultUtil.APPRESULT(CommonStatusCode.SAVE_SUCCESS.getCode() , CommonStatusCode.SAVE_SUCCESS.getMsg() , stocksHistoryPos.size());
    }



}
