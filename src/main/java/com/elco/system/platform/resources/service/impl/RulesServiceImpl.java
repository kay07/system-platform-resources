package com.elco.system.platform.resources.service.impl;

import com.elco.platform.util.SysCodeEnum;
import com.elco.platform.util.SysCodeException;
import com.elco.system.platform.resources.config.AllStatus;
import com.elco.system.platform.resources.entity.Rules;
import com.elco.system.platform.resources.entity.vo.RulesVo;
import com.elco.system.platform.resources.mapper.RulesMapper;
import com.elco.system.platform.resources.service.RulesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kay
 * @date 2021/9/2
 */
@Service
public class RulesServiceImpl implements RulesService {
    @Resource
    private RulesMapper rulesMapper;
    public static final int ID=1;
    private static final String level1="1级预警";
    private static final String level2="2级预警";
    private static final String level3="3级预警";
    @Override
    public List<RulesVo> showRules() {
        Rules rules = rulesMapper.selectById(ID);
        if(rules==null){
            return null;
        }else {
            RulesVo r1=new RulesVo();
            RulesVo r2=new RulesVo();
            RulesVo r3=new RulesVo();
            r1.setLevel(level1);
            r1.setStatus(AllStatus.STATUS1.getMsg());
            r1.setHigh("100%");
            r1.setLow(rules.getHigh()+"%");
            r2.setLevel(level2);
            r2.setStatus(AllStatus.STATUS2.getMsg());
            r2.setHigh(rules.getHigh()+"%");
            r2.setLow(rules.getLow()+"%");
            r3.setLevel(level3);
            r3.setStatus(AllStatus.STATUS3.getMsg());
            r3.setHigh(rules.getLow()+"%");
            r3.setLow("0%");
            List<RulesVo> list=new ArrayList<>();
            list.add(r1);
            list.add(r2);
            list.add(r3);
            return list;
        }
    }

    @Override
    public boolean updateRules(Rules rules) {
        Rules rules1 = rulesMapper.selectById(ID);
        rules.setId(ID);
        if(rules.getHigh()<=rules.getLow()||rules.getLow()<=0||rules.getHigh()>=100){
            throw new SysCodeException(SysCodeEnum.ILLEGAL_PARAM);
        }
        if(rules1==null){
            //规则为空，新增
            rulesMapper.insert(rules);
        }else {
            //修改规则
            rulesMapper.updateById(rules);
        }
        return true;
    }
}
