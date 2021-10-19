package com.elco.system.platform.resources.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/9/13
 */
@ApiModel(value = "项目删除与添加-入参")
@Data
public class ItemDelDto implements Serializable {
    private static final long serialVersionUID = -4521284396110225965L;
    @ApiModelProperty(value = "项目id，删除时使用")
    private String projectId;
    @ApiModelProperty(value = "项目名称，添加时使用")
    private String name;
}
