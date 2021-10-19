package com.elco.system.platform.resources.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/8/23
 */
@Data
@ApiModel("项目名称列表-入参")
public class ItemDto implements Serializable {
    private static final long serialVersionUID = -3998565802617209991L;
    @ApiModelProperty(value = "页码")
    private int page;
    @ApiModelProperty(value = "项目名称")
    private String name;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
