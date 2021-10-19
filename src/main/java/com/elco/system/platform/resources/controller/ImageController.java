package com.elco.system.platform.resources.controller;

import com.elco.platform.util.CommonResult;
import com.elco.platform.util.PageResult;
import com.elco.system.platform.resources.entity.Image;
import com.elco.system.platform.resources.entity.Item;
import com.elco.system.platform.resources.entity.dto.ImageDelDto;
import com.elco.system.platform.resources.entity.dto.ImageDto;
import com.elco.system.platform.resources.entity.dto.ItemDelDto;
import com.elco.system.platform.resources.entity.dto.ItemDto;
import com.elco.system.platform.resources.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author kay
 * @date 2021/8/23
 */
@Api(tags = "镜像管理")
@RestController
@RequestMapping(value = "/img")
public class ImageController {
    @Resource
    private ImageService imageService;
    @ApiOperation(value = "项目列表")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public CommonResult<PageResult<Item>> list(@RequestBody ItemDto itemDto){
        PageResult<Item> list = imageService.list(itemDto);
        return CommonResult.success(list);
    }
    @ApiOperation(value = "项目删除")
    @RequestMapping(value = "/delitem",method = RequestMethod.POST)
    public CommonResult<String > delItem(@RequestBody ItemDelDto item){
        imageService.deleteItem(item.getProjectId());
        return CommonResult.success(item.getProjectId());
    }
    @ApiOperation(value = "项目添加")
    @RequestMapping(value = "/createitem",method = RequestMethod.POST)
    public CommonResult<String> createItem(@RequestBody ItemDelDto item){
        imageService.createItem(item.getName());
        return CommonResult.success(item.getName());
    }
    @ApiOperation(value = "镜像列表")
    @RequestMapping(value = "/listimg",method = RequestMethod.POST)
    public CommonResult<PageResult<Image>> listImg(@RequestBody ImageDto imageDto){
        PageResult<Image> imagePageResult = imageService.listImg(imageDto);
        return CommonResult.success(imagePageResult);
    }
    @ApiOperation(value = "镜像删除")
    @RequestMapping(value = "/delimg",method = RequestMethod.POST)
    public CommonResult<ImageDelDto> delImg(@RequestBody ImageDelDto imageDelDto){
            imageService.deleteImage(imageDelDto);
            return CommonResult.success(imageDelDto);
    }
}

