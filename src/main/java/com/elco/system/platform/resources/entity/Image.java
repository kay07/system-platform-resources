package com.elco.system.platform.resources.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kay
 * @date 2021/8/23
 */
@Data
public class Image implements Serializable {

    private static final long serialVersionUID = 1903225013520633901L;
    private int imageId;
    private String imageName;
    private String imageTag;
    private String imageDetails;
    private String imageSize;
    private String imageCreator;
    private String imageCreateTime;
}
