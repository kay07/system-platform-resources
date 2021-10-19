package com.elco.system.platform.resources.entity.vo;

import com.elco.system.platform.resources.config.AllStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/9/3
 */
@Data
@ApiModel(value = "数量统计-出参")
public class AmountVo implements Serializable {
    private static final long serialVersionUID = 8362817102488023156L;
    @ApiModelProperty(value = "报警")
    private int status1;
    @ApiModelProperty(value = "预警")
    private int status2;
    @ApiModelProperty(value = "正常")
    private int status3;

}
