package com.apl.wms.warehouse.lib.feign;

import com.alibaba.druid.pool.DruidDataSource;
import com.apl.cache.AplCacheUtil;
import com.apl.db.datasource.DataSourceConfig;
import com.apl.db.datasource.DynamicDataSource;
import com.apl.db.orm.AplDBInfo;
import com.apl.db.orm.AplDBPersistent;
import com.apl.db.orm.AplSqlGenerate;
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

    //状态code枚举
    enum CancelAllocationWarehouseServiceCode {

        CANCEL_ALLOCATION_SUCCESS("CANCEL_ALLOCATION_SUCCESS", "取消分配成功"),
        ;

        private String code;
        private String msg;

        CancelAllocationWarehouseServiceCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public AplDBInfo connectDb(){
        // 创建数据库连接信息
        SecurityUser securityUser = CommonContextHolder.getSecurityUser();

        DruidDataSource druidDataSource = DynamicDataSource.getDruidDataSource(securityUser.getTenantGroup(),
                DataSourceConfig.sysProduct,
                "wms_stocks_history",
                redisTemplate);
        AplDBInfo dbInfo = new AplDBInfo(druidDataSource);
        dbInfo.setTenantValue(securityUser.getInnerOrgId());
        dbInfo.tenantIdName = "inner_org_id";

        return dbInfo;
    }

    //批量保存库存记录
    public ResultUtil<Integer> saveStocksHistoryPos(AplDBInfo dbInfo, List<StocksHistoryPo> stocksHistoryPos) throws Exception
    {
        if(null==insertSql)
        {
             //首次调用生成插入SQL语句
            insertSql = AplSqlGenerate.creteInsertSql(dbInfo, stocksHistoryPos.get(0), "stocks_history");
        }

        for (StocksHistoryPo stocksHistoryPo : stocksHistoryPos) {
            AplDBPersistent.insert(dbInfo, insertSql, stocksHistoryPo);
        }
//        dbUtil.insertBatch(dbInfo, stocksHistoryPos, "stocks_history", "id");

        return ResultUtil.APPRESULT(CancelAllocationWarehouseServiceCode.CANCEL_ALLOCATION_SUCCESS.code,
                CancelAllocationWarehouseServiceCode.CANCEL_ALLOCATION_SUCCESS.msg, stocksHistoryPos.size());
    }



}
