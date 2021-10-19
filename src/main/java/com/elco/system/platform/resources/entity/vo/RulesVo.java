package com.elco.system.platform.resources.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/9/2
 */
@Data
@ApiModel(value = "规则列表-出参")
public class RulesVo implements Serializable {
    private static final long serialVersionUID = 6627384130772006158L;
    @ApiModelProperty(value = "预警等级-3,2,1")
    private String level;
    @ApiModelProperty(value = "预警状态-正常,预警,报警")
    private String status;
    @ApiModelProperty(value = "预警范围-开始")
    private String low;
    @ApiModelProperty(value = "预警范围-结束")
    private String high;
}
