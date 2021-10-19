package com.elco.system.platform.resources.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/8/23
 */
@Data
public class Item implements Serializable {

    private static final long serialVersionUID = -6322377433687515619L;
    private int id;
    private int projectId;
    private String name;
    private String amount;
    private String creator;
    private String createTime;
}
