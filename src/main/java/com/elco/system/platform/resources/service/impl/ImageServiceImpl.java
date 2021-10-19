package com.elco.system.platform.resources.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.*;
import cn.hutool.log.Log;
import com.elco.platform.util.SysCodeEnum;
import com.elco.platform.util.SysCodeException;
import com.elco.system.platform.resources.entity.Image;
import com.elco.system.platform.resources.entity.Item;
import com.elco.system.platform.resources.entity.dto.ImageDelDto;
import com.elco.system.platform.resources.entity.dto.ImageDto;
import com.elco.system.platform.resources.entity.dto.ItemDto;
import com.elco.system.platform.resources.service.ImageService;
import com.elco.platform.util.ListPage;
import com.elco.platform.util.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kay
 * @date 2021/8/23
 */
@Service
//@CrossOrigin
public class ImageServiceImpl implements ImageService {
    @Value("${harbor.url}")
    private String url;
    @Value("${harbor.username}")
    private String username;
    @Value("${harbor.password}")
    private String password;

    private final static String version="/api/v2.0/projects/";
    @Override
    public boolean createItem(String name) {
        if(name==""){
            throw new SysCodeException(SysCodeEnum.NULL_ERROR);
        }
        String url=this.url+version;
        String authToken=loginToken(this.username,this.password);
        Map<String,String> map=new HashMap<>();
        map.put("Authorization","Basic "+authToken);
        map.put("Content-Type","application/json");
        String s = "{\"project_name\": "+'"'+name+'"'+",\"metadata\": {\"public\": \"true\"},\"storage_limit\": -1}";
        String body=HttpRequest.post(url).addHeaders(map).body(s).execute().body();
        if(!body.equals("")){
            throw new RuntimeException(body);
        }
        return true;
    }

    @Override
    public boolean deleteItem(String name) {
        if (name.equals("")){
            throw new SysCodeException(SysCodeEnum.NULL_ERROR);
        }
        String url=this.url+version+name;
        String authString=loginToken(this.username,this.password);
        String body = HttpRequest.delete(url).header("Authorization","Basic "+authString).execute().body();
        if(!body.equals("")){
            throw new RuntimeException(body);
        }
        return true;
    }

    @Override
    public PageResult<Item> list(ItemDto itemDto) {
        String url = this.url + version;
        String execute = HttpRequest.get(url).execute().body();
        //new JSONArray(execute);
        JSONArray objects = JSONUtil.parseArray(execute);
        int size = objects.size();
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Item item = new Item();
            JSONObject o = (JSONObject) objects.get(i);
            //JSONObject parse = JSONUtil.parseObj(o);
            String creation_time = o.get("creation_time").toString();
            String owner_name = o.get("owner_name").toString();
            int project_id = (int)o.get("project_id");
            String repo_count ="0";
            if(o.containsKey("repo_count")){
                repo_count=o.getStr("repo_count");
            }
            String name = o.get("name").toString();
            //如果不包含该名称，直接跳出
            if (!name.contains(itemDto.getName())) {
                continue;
            }
            if(!containDate(creation_time,itemDto.getStartTime(),itemDto.getEndTime())){
                continue;
            }
            item.setId(i + 1);
            item.setName(name);
            item.setProjectId(project_id);
            item.setAmount(repo_count);
            item.setCreator(owner_name);
            creation_time=creation_time.replaceAll("T"," ");
            creation_time=creation_time.substring(0,creation_time.length()-5);
            //修改时区
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse=null;
            try {
                 parse = simpleDateFormat.parse(creation_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long time=parse.getTime();
            String format = simpleDateFormat.format(time + 28800000);
            item.setCreateTime(format);
            itemList.add(item);
        }

        //分页
        int everyPage = 10;
        int total = itemList.size();
        int totalPage = (total + everyPage - 1) / everyPage;
        if (itemDto.getPage() > 0 && itemDto.getPage() <= totalPage) {
            PageResult<Item> pageResult = new PageResult<>();
            pageResult.setTotalPage(totalPage);
            pageResult.setTotalCount(total);
            List<Item> list = new ListPage().listPage(itemList, itemDto.getPage(), everyPage);
            pageResult.setDataList(list);
            return pageResult;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteImage(ImageDelDto imageDelDto) {
        if(imageDelDto.getImageName().equals("")||imageDelDto.getImageTag().equals("")||imageDelDto.getItemName().equals("")){
           throw new SysCodeException(SysCodeEnum.NULL_ERROR);
        }
        String item=imageDelDto.getItemName();
        String image=imageDelDto.getImageName();
        String tag=imageDelDto.getImageTag();
        String url=this.url+version+item+"/repositories/"+image+"/artifacts/"+tag;
        String authString=loginToken(this.username,this.password);
        String body = HttpRequest.delete(url).header("Authorization","Basic "+authString).execute().body();
        if(!body.equals("")){
            throw new RuntimeException(body);
        }

        //如果该仓库下没有tag，则删除该空仓库
        //1,判断有没有tag
//        String urltag=this.url+version+item+"/repositories/"+image;
//        String body1 = HttpRequest.get(urltag).execute().body();
//        JSONObject jsonObject = JSONUtil.parseObj(body1);
//        boolean artifact_count = jsonObject.containsKey("artifact_count");
//        //2,删除
//        if(!artifact_count){
//            body = HttpRequest.delete(urltag).header("Authorization","Basic "+authString).execute().body();
//            if(!body.equals("")){
//                throw new RuntimeException(body);
//            }
//        }
        return true;
    }

    @Override
    public PageResult<Image> listImg(ImageDto imageDto) {
        if(imageDto.getItemName().equals("")||imageDto.getItemName()==null){
            throw new SysCodeException(SysCodeEnum.NULL_ERROR);
        }
        String urlNoPage=this.url+version+imageDto.getItemName()+"/repositories";
        String url=urlNoPage+"?page=1&page_size=100";

        String body = HttpRequest.get(url).execute().body();
        JSONArray objects = JSONUtil.parseArray(body);
        List<Image> imageList=new ArrayList<>();
        int id=1;
        //id用于记录编号
        String body1="";
        JSONArray objects1=null;
        for (int i = 0; i < objects.size(); i++) {
            JSONObject o = (JSONObject) objects.get(i);
            String  imageName = o.get("name").toString();
            //不包含当前镜像直接跳过当前循环
            if(!imageName.contains(imageDto.getName())){
                continue;
            }
            //获取所有镜像名称，需要遍历每一个镜像获取其tag
            String[] split = imageName.split("/");
            //获取只含名字的镜像，然后遍历
            String url1=urlNoPage+"/"+split[1]+"/artifacts?page=1&page_size=100";
            body1= HttpRequest.get(url1).execute().body();
            objects1 = JSONUtil.parseArray(body1);
            for (int i1 = 0; i1 < objects1.size(); i1++) {
                Image image=new Image();
                JSONObject o2 = (JSONObject) objects1.get(i1);

                String createTime=o2.get("push_time").toString();
                //不包含该时间段直接跳过当前循环
               if(!containDate(createTime,imageDto.getStartTime(),imageDto.getEndTime())){
                   continue;
               }
                if(o2.get("tags").toString().equals("null")){
                    //空标签的镜像直接忽略
                    continue;
                //    image.setImageTag("null");
                }else {
                    JSONArray tags = (JSONArray) o2.get("tags");
                    JSONObject o4 = (JSONObject) tags.get(0);
                    String imageTag=o4.get("name").toString();
                    image.setImageTag(imageTag);
                }
                String imageDetails = o2.get("labels").toString();
                if(imageDetails.equals("null")){
                    imageDetails="";
                }
                String imageSize=o2.get("size").toString();
                //类型转换
                BigDecimal b1=new BigDecimal(imageSize);
                BigDecimal b2=new BigDecimal("1024");
                imageSize = b1.divide(b2, 1, BigDecimal.ROUND_HALF_UP).divide(b2, 1, BigDecimal.ROUND_HALF_UP).toString();
                imageSize=imageSize+"M";
                JSONObject o3 = (JSONObject) o2.get("extra_attrs");
                String creator=o3.get("author").toString();

                image.setImageId(id++);
                image.setImageName(imageName);
                createTime=createTime.replaceAll("T"," ");
                createTime=createTime.substring(0,createTime.length()-5);
                //
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse=null;
                try {
                    parse = simpleDateFormat.parse(createTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long time=parse.getTime();
                String format = simpleDateFormat.format(time + 28800000);
                image.setImageCreateTime(format);
                image.setImageCreator(creator);
                image.setImageDetails(imageDetails);
                image.setImageSize(imageSize);
                imageList.add(image);
            }

        }

        //分页
        int imageDtoPage = imageDto.getPage();
        ListPage listPage=new ListPage();
        PageResult<Image> imagePageResult=new PageResult<>();
        int totalPage=(imageList.size()+imagePageResult.getPageSize()-1)/imagePageResult.getPageSize();
        if(imageDtoPage<=0||imageDtoPage>totalPage){
           return null;
        }
        List list = listPage.listPage(imageList, imageDtoPage, imagePageResult.getPageSize());
        imagePageResult.setDataList(list);
        imagePageResult.setTotalCount(imageList.size());
        imagePageResult.setTotalPage(totalPage);
        return imagePageResult;
    }


    //判断是否在当前时间段
    public static boolean containDate(String now,String start,String end){
        boolean bTime = (start != null) && (end != null) && (!"".equals(start)) && (!"".equals(end));
        if (bTime) {
            //时间段不为空
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = null;
            Date date2 = null;
            Date d = null;
            try {
                date1 = simpleDateFormat.parse(start);
                date2 = simpleDateFormat.parse(end);
                d = simpleDateFormat.parse(now);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //如果不包含该时间段，直接跳出
            if (d.compareTo(date1) == -1 || d.compareTo(date2) == 1) {
                return false;
            }else {
                return true;
            }
        }
        return true;
    }
    public static String loginToken(String username,String password){
        String auth=username+":"+password;
        String authString="";
        try {
            authString= Base64.encode(auth.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return authString;
    }
}
