package com.elco.system.platform.resources.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elco.system.platform.resources.entity.Server;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kay
 * @date 2021/9/3
 */
public interface MonitorMapper extends BaseMapper<Server> {
//    List<Server> byName(@Param("name") String name);
}
