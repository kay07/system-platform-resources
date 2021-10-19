package com.elco.system.platform.resources.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/9/13
 */
@Data
@ApiModel(value = "节点信息-入参")
public class NodeDto implements Serializable {
    private static final long serialVersionUID = 2824551570924408675L;
    @ApiModelProperty(value = "节点名称")
        private String name;
    @ApiModelProperty(value = "节点类型，包括正常，预警，报警三种")
        private String type;
}
