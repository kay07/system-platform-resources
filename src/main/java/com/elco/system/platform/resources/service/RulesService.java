package com.elco.system.platform.resources.service;

import com.elco.system.platform.resources.entity.Rules;
import com.elco.system.platform.resources.entity.vo.RulesVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kay
 * @date 2021/9/2
 */
public interface RulesService {
    List<RulesVo> showRules();
    boolean updateRules(Rules rules);
}
