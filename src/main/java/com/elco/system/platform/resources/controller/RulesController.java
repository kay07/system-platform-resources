package com.elco.system.platform.resources.controller;

import com.elco.platform.util.CommonResult;
import com.elco.system.platform.resources.entity.Rules;
import com.elco.system.platform.resources.entity.vo.RulesVo;
import com.elco.system.platform.resources.service.RulesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kay
 * @date 2021/9/2
 */
@Api(tags = "节点监控规则")
@RestController
@RequestMapping(value = "/rules")
public class RulesController {
    @Resource
    private RulesService rulesService;

    @ApiOperation(value = "节点监控规则-查看")
    @RequestMapping(value = "/show",method = RequestMethod.POST)
    public CommonResult<List<RulesVo>> show(){
        List<RulesVo> rules = rulesService.showRules();
        return CommonResult.success(rules);
    }


    @ApiOperation(value = "节点监控规则-修改")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public CommonResult<Rules> show(@RequestBody Rules rules){
        rulesService.updateRules(rules);
        return CommonResult.success(rules);
    }
}
