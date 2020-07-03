package com.apl.wms.warehouse.mapper;

import com.apl.wms.warehouse.po.StoreApiPo;
import com.apl.wms.warehouse.po.StorePo;
import com.apl.wms.warehouse.vo.StoreListVo;
import com.apl.wms.warehouse.vo.StoreInfoVo;
import com.apl.wms.warehouse.dto.StoreKeyDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 网店铺 Mapper 接口
 * </p>
 *
 * @author arran
 * @since 2019-12-21
 */
public interface StoreMapper extends BaseMapper<StorePo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-21
     */
    StoreInfoVo getById( @Param("customerId") Long customerId,   @Param("id") Long id);
    
    
    
    /**
     * @Description  获取API参数
     * @Param   
     * @Return  
     * @Author  arran
     * @Date  2019/12/30
     */
    StoreApiPo getApiConfig(@Param("id") Long id, @Param("customerId") Long customerId);

    /**
     * @Description  获取API参数
     * @Param
     * @Return
     * @Author  arran
     * @Date  2019/12/30
     */
    String getApiConfigStrVal(@Param("id") Long id, @Param("customerId") Long customerId);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-21
     */
    List<StoreListVo> getList(Page page, @Param("kd") StoreKeyDto keyDto);


    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-21
     */
    List<StoreInfoVo> exists(@Param("id") Long id,  @Param("customerId") Long customerId,   @Param("storeCode") String storeCode,   @Param("storeName") String storeName,   @Param("storeNameEn") String storeNameEn );


    void seata2();


}
