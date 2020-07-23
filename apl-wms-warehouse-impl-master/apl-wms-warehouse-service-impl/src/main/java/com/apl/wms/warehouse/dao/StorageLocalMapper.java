package com.apl.wms.warehouse.dao;

import com.apl.wms.warehouse.dto.StorageLocationKeyDto;
import com.apl.wms.warehouse.lib.pojo.bo.StorageLocationBo;
import com.apl.wms.warehouse.lib.pojo.vo.StorageLocalInfoVo;
import com.apl.wms.warehouse.po.StorageLocalPo;
import com.apl.wms.warehouse.vo.StorageLocalListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库位 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface StorageLocalMapper extends BaseMapper<StorageLocalPo> {

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<StorageLocalListVo> getList(Page page, @Param("kd" ) StorageLocationKeyDto keyDto);


    /**
     * @Desc: 获取商品对应的库位库存列表
     * @Author: CY
     * @Date: 2020/6/9 15:01
     */
    List<StorageLocalInfoVo> getStorageLocalByCommodityId(@Param("commodityId") Long commodityId);

    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<StorageLocalInfoVo> exists(@Param("id" ) Long id, @Param("storageLocalSn" ) String storageLocalSn);


    /**
     * @Desc: 获取打印的数据
     * @Author: CY
     * @Date: 2019/12/20 18:31
     */
    List<StorageLocationBo> getPrintData(@Param("ids") List<Long> ids);

    /**
     * @Desc: 获取 未满 的库位
     * @Author: CY
     * @Date: 2020/1/3 15:08
     */
    List<StorageLocalInfoVo> getUnFullStorageLocal(@Param("commodityId") Long commodityId);

    /**
     * @Desc: 获取十个 空的 库位
     * @Author: CY
     * @Date: 2020/1/3 15:20
     */
    List<StorageLocalInfoVo> getNullStorageLocal(@Param("count") Integer count);

    /**
     * @Desc: 更新库位 状态
     * @Author: CY
     * @Date: 2020/1/3 16:04
     */
    Boolean batchUpdateStorageStatus(@Param("storageIds") String storageIds, @Param("status") Integer status);


    /** @Description : 批量改变库位状态
     * @param ：
     * @Return ：
     * Created by arran on 2020/3/12
     */
    Boolean changeStorageLocalStatus(@Param("storageLocalIds") String storageLocalIds, @Param("localStatus") Integer localStatus);

    /**
     * @Desc: 根据库位 编号 ， 库位是否被占用 查找库位信息
     * @Author: CY
     * @Date: 2020/3/17 10:53
     */
    StorageLocalInfoVo getStorageLocalBySn(@Param("storageLocalSn") String storageLocalSn);

    /**
     * 根据商品id查询出多个库位信息
     * @param commodityIdList
     * @return
     */
    List<StorageLocalPo> getStorageLocalRealityCountByCommodityId(@Param("ids") List<Long> commodityIdList);
}
