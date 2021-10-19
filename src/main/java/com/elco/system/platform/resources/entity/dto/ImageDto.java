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
@ApiModel("镜像列表-入参")
public class ImageDto implements Serializable {

    private static final long serialVersionUID = 281657718760006769L;
    @ApiModelProperty(value = "页码")
    private int page;
    @ApiModelProperty(value = "项目名称")
    private String itemName;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "镜像名称")
    private String name;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
