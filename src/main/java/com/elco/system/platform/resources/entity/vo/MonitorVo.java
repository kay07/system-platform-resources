package com.elco.system.platform.resources.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/9/3
 */
@Data
@ApiModel(value = "节点监控-出参")
public class MonitorVo implements Serializable {
    private static final long serialVersionUID = 2471394339997092076L;
    @ApiModelProperty(value = "id")
    private int id;
    @ApiModelProperty(value = "节点名称")
    private String nodeName;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "创建时间")
    private String createDate;
    @ApiModelProperty(value = "描述")
    private String details;
    @ApiModelProperty(value = "cpu参数")
    private FourType cpu;
    @ApiModelProperty(value = "内存参数")
    private FourType memory;
    @ApiModelProperty(value = "硬盘参数")
    private FourType disk;
}
