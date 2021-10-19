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
@ApiModel(value = "基本参数")
public class FourType implements Serializable {

    private static final long serialVersionUID = -5647102925070496884L;
    @ApiModelProperty(value = "总大小")
    private String all;
    @ApiModelProperty(value = "使用率")
    private String rate;
    @ApiModelProperty(value = "已使用")
    private String used;
    @ApiModelProperty(value = "未使用")
    private String unUsed;
    @ApiModelProperty(value = "状态")
    private String status;
}
