package com.elco.system.platform.resources.controller;

import com.elco.platform.util.CommonResult;
import com.elco.platform.util.PageResult;
import com.elco.system.platform.resources.config.AllStatus;
import com.elco.system.platform.resources.entity.dto.MonitorDto;
import com.elco.system.platform.resources.entity.dto.NodeDto;
import com.elco.system.platform.resources.entity.vo.AmountVo;
import com.elco.system.platform.resources.entity.vo.MonitorVo;
import com.elco.system.platform.resources.entity.vo.StatusVo;
import com.elco.system.platform.resources.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kay
 * @date 2021/9/3
 */
@Api(tags = "节点监控")
@RequestMapping(value = "/monitor")
@RestController
public class MonitorController {

    @Resource
    private MonitorService monitorService;
    @ApiOperation(value = "节点信息-查找")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public CommonResult<PageResult<StatusVo>> list(@RequestBody MonitorDto monitorDto){
        PageResult<StatusVo> list = monitorService.list(monitorDto);
        return CommonResult.success(list);
    }

}
