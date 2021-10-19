package com.elco.system.platform.resources.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/8/31
 */
@Data
@ApiModel(value = "添加节点-入参")
public class ServerInsertDto implements Serializable {
    private static final long serialVersionUID = 4489169336060287872L;
    @ApiModelProperty(value = "服务器名称")
    private String serverName;
    @ApiModelProperty(value = "描述")
    private String details;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "服务器ip")
    private String ip;
    @ApiModelProperty(value = "服务器用户名")
    private String userName;
    @ApiModelProperty(value = "服务器密码")
    private String passWord;
    @ApiModelProperty(value = "服务器ssh端口")
    private String port;
}
