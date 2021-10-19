package com.elco.system.platform.resources.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/9/17
 */
@Data
@ApiModel(value = "节点监控-入参")
public class MonitorDto implements Serializable {
    private static final long serialVersionUID = 3375516506915532569L;
    @ApiModelProperty(value = "页码")
    private int page;
    @ApiModelProperty(value = "节点名称")
    private String name;
    @ApiModelProperty(value = "节点类型")
    private String type;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
