package com.elco.system.platform.resources.service;

import com.elco.platform.util.PageResult;
import com.elco.system.platform.resources.entity.dto.MonitorDto;
import com.elco.system.platform.resources.entity.vo.AmountVo;
import com.elco.system.platform.resources.entity.vo.MonitorVo;
import com.elco.system.platform.resources.entity.vo.StatusVo;

import java.util.List;

/**
 * @author kay
 * @date 2021/9/3
 */
public interface MonitorService {
    PageResult<StatusVo> list(MonitorDto monitorDto);
}
