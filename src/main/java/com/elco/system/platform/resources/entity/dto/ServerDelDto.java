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
@ApiModel(value = "节点管理-修改和删除-入参")
public class ServerDelDto implements Serializable {
    private static final long serialVersionUID = 3475091628429061008L;
    @ApiModelProperty(value = "节点删除与修改都需要")
    private int  id;
    @ApiModelProperty(value = "节点名称，仅修改需要")
    private String name;
    @ApiModelProperty(value = "节点详情，仅修改需要")
    private String details;
}
