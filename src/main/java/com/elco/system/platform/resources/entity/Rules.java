package com.elco.system.platform.resources.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/9/2
 */
@Data
@TableName(value = "server_rules")
public class Rules implements Serializable {

    private static final long serialVersionUID = -3842817004929780553L;

    private int id;
    private int low;
    private int high;
}
