package com.elco.system.platform.resources.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/8/31
 */
@Data
@TableName(value = "server_msg")
public class Server implements Serializable {

    private static final long serialVersionUID = 3772521276022174966L;

    private int id;
    private String serverName;
    private String details;
    private String creator;
    private String ip;
    private String userName;
    private String password;
    private String port;
    private String cpu;
    private String memory;
    private String disk;
    private String createDate;
}
