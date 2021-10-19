package com.elco.system.platform.resources.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/8/25
 */
@Data
@ApiModel(value = "删除镜像-入参")
public class ImageDelDto implements Serializable {
    private static final long serialVersionUID = 4823937230286590355L;
    @ApiModelProperty("项目名称")
    private String itemName;
    @ApiModelProperty("镜像名称")
    private String imageName;
    @ApiModelProperty("镜像标签")
    private String imageTag;
}
