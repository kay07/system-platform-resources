package com.elco.system.platform.resources.service;

import com.elco.platform.util.PageResult;
import com.elco.system.platform.resources.entity.Server;
import com.elco.system.platform.resources.entity.dto.MonitorDto;
import com.elco.system.platform.resources.entity.dto.ServerInsertDto;

import java.util.List;

/**
 * @author kay
 * @date 2021/8/31
 */
public interface ServerService {
    boolean addServer(ServerInsertDto serverInsertDto);
    boolean updateServer(int id,String name,String details);
    PageResult<List<Server>> listServer(MonitorDto serverDto);
    boolean deleteServer(int id);
}
