package com.apl.wms.warehouse.lib;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author hjr start
 * @date 2020/7/31 - 22:20
 */
@Repository
@Mapper
public interface StorageLocalMapper {
    @MapKey("mapKey")
    Map<Long, Integer> getStorageLocalMap(@Param("ids") String ids);
}
