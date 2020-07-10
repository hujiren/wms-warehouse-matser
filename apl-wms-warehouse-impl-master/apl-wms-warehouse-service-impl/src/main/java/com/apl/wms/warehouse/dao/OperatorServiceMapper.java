package com.apl.wms.warehouse.dao;

import com.apl.wms.warehouse.dto.OperatorServiceKeyDto;
import com.apl.wms.warehouse.po.OperatorServicePo;
import com.apl.wms.warehouse.vo.OperatorServiceInfoVo;
import com.apl.wms.warehouse.vo.OperatorServiceListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 附加服务操作名称 Mapper 接口
 * </p>
 *
 * @author cy
 * @since 2019-12-17
 */
public interface OperatorServiceMapper extends BaseMapper<OperatorServicePo> {

    /**
     * @Desc: 根据id 查找详情
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    public OperatorServiceInfoVo getById(@Param("id" ) Long id);

    /**
     * @Desc: 查找列表
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<OperatorServiceListVo> getList(Page page, @Param("kd" ) OperatorServiceKeyDto keyDto);


    /**
     * @Desc: 检测记录是否重复
     * @Author: ${cfg.author}
     * @Date: 2019-12-17
     */
    List<OperatorServiceInfoVo> exists(@Param("id" ) Long id, @Param("name" ) String name, @Param("nameEn" ) String nameEn);

}
