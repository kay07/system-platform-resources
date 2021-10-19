package com.elco.system.platform.resources.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elco.system.platform.resources.entity.Server;
import com.elco.system.platform.resources.entity.dto.MonitorDto;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author kay
 * @date 2021/8/31
 */
public interface ServerMapper extends BaseMapper<Server> {
    List<Server> listServer(@Param(value = "serverDto") MonitorDto serverDto);
    List<Server> list();
    int countServer();
    // 0 10 第一页
    // 10 10 第二页
    // 20 10 第三页
    // 30 10 第四页
}
