package com.elco.system.platform.resources.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author kay
 * @date 2021/9/3
 */
@Data
@ApiModel(value = "列表信息")
public class StatusVo implements Serializable {
    private static final long serialVersionUID = 3716645350569661674L;
    @ApiModelProperty(value = "详情")
    List<MonitorVo> monitorVos;
    @ApiModelProperty(value = "告警数量")
    int status1;
    @ApiModelProperty(value = "预警数量")
    int status2;
    @ApiModelProperty(value = "正常数量")
    int status3;
    @ApiModelProperty(value = "总计")
    private int status;
}
