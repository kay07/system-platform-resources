package com.elco.system.platform.resources.service;

import com.elco.system.platform.resources.entity.Image;
import com.elco.system.platform.resources.entity.Item;
import com.elco.system.platform.resources.entity.dto.ImageDelDto;
import com.elco.system.platform.resources.entity.dto.ImageDto;
import com.elco.system.platform.resources.entity.dto.ItemDto;
import com.elco.platform.util.PageResult;

/**
 * @author kay
 * @date 2021/8/23
 */
public interface ImageService {
    boolean createItem(String name);
    boolean deleteItem(String projectId);
    PageResult<Item> list(ItemDto itemDto);
    boolean deleteImage(ImageDelDto imageDelDto);
    PageResult<Image> listImg(ImageDto imageDto);
}
