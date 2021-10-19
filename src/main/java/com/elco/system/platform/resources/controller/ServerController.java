package com.elco.system.platform.resources.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.elco.platform.util.CommonResult;
import com.elco.platform.util.PageResult;
import com.elco.system.platform.resources.entity.Server;
import com.elco.system.platform.resources.entity.dto.MonitorDto;
import com.elco.system.platform.resources.entity.dto.ServerDelDto;
import com.elco.system.platform.resources.entity.dto.ServerInsertDto;
import com.elco.system.platform.resources.service.ServerService;
import com.elco.system.platform.resources.service.impl.ImageServiceImpl;
import com.elco.system.platform.resources.service.impl.ServerServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kay
 * @date 2021/8/31
 */
@Api(tags = "节点管理")
@RestController
@RequestMapping(value = "/server")
public class ServerController {
    @Resource
    private ServerService serverService;

    @ApiOperation(value = "节点管理-添加")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public CommonResult<String> insertServer(@RequestBody ServerInsertDto serverInsertDto){
        serverService.addServer(serverInsertDto);
        return CommonResult.success(serverInsertDto.getServerName());
    }
    @ApiOperation(value = "节点管理-列表")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public CommonResult<PageResult<List<Server>>> listServer(@RequestBody MonitorDto serverDto){
        PageResult<List<Server>> listPageResult = serverService.listServer(serverDto);
        return CommonResult.success(listPageResult);
    }
    @ApiOperation(value = "节点管理-修改")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public CommonResult<String> updateServer(@RequestBody ServerDelDto serverDelDto){
        serverService.updateServer(serverDelDto.getId(),serverDelDto.getName(),serverDelDto.getDetails());
        return CommonResult.success(serverDelDto.getName());
    }
    @ApiOperation(value = "节点管理-删除")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public CommonResult<String> deleteServer(@RequestBody ServerDelDto serverDelDto){
        serverService.deleteServer(serverDelDto.getId());
        return CommonResult.success(serverDelDto.getId()+"");
    }

}
