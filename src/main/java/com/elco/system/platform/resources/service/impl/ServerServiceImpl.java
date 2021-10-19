package com.elco.system.platform.resources.service.impl;

import com.elco.platform.util.ListPage;
import com.elco.platform.util.PageResult;
import com.elco.platform.util.SysCodeEnum;
import com.elco.platform.util.SysCodeException;
import com.elco.system.platform.resources.config.Command;
import com.elco.system.platform.resources.entity.Server;
import com.elco.system.platform.resources.entity.dto.MonitorDto;
import com.elco.system.platform.resources.entity.dto.ServerInsertDto;
import com.elco.system.platform.resources.mapper.ServerMapper;
import com.elco.system.platform.resources.service.ServerService;
import com.elco.system.platform.resources.utils.MyAESUtil;
import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kay
 * @date 2021/8/31
 */
@Service
public class ServerServiceImpl implements ServerService {

    private Session session;
    @Resource
    private ServerMapper serverMapper;
    @Resource
    private MyAESUtil myAESUtil;
    @Override
    public boolean addServer(ServerInsertDto serverInsertDto) {
        if (serverInsertDto.getServerName().equals("") || serverInsertDto.getIp().equals("") || serverInsertDto.getUserName().equals("") || serverInsertDto.getPassWord().equals("") || serverInsertDto.getPort().equals("")) {
            throw new SysCodeException(SysCodeEnum.NULL_ERROR);
        }
        //判断该ip是否已经在数据库了，已经存在则直接退出
        Map<String, Object> map1=new HashMap<>();
        map1.put("ip",serverInsertDto.getIp());
        List<Server> servers = serverMapper.selectByMap(map1);
        if(servers.size()!=0){
            throw new RuntimeException("该服务信息已存在");
        }
        //按照账号密码等信息登录服务器获取信息
        String[] commands={Command.CPU_GHZ,Command.MEMORY_ALL,Command.DISK_ALL};
        Map<String, String> map = runShell(commands, serverInsertDto.getUserName(), serverInsertDto.getPassWord(), serverInsertDto.getIp(), Integer.parseInt(serverInsertDto.getPort()));
        String cpu=map.get(Command.CPU_GHZ);
        String mem=map.get(Command.MEMORY_ALL);
        mem=mem.replaceAll("\r|\n","");
        cpu=cpu.replaceAll("\r|\n","");
        //类型转换
        BigDecimal b1=new BigDecimal(mem);
        BigDecimal b2=new BigDecimal("1024");
        mem=b1.divide(b2,1,BigDecimal.ROUND_HALF_UP).divide(b2,1,BigDecimal.ROUND_HALF_UP).toString();
        mem=mem+"G";
        String dk=map.get(Command.DISK_ALL);
        String disk=shellSplitDiskAll(dk)+"G";
        if(cpu.equals("")||mem.equals("")||disk.equals("")){
            throw new SysCodeException(SysCodeEnum.NO_DATA);
        }
        //获取信息成功，可以入库了
        Server s=new Server();
        s.setServerName(serverInsertDto.getServerName());
        s.setDetails(serverInsertDto.getDetails());
        s.setIp(serverInsertDto.getIp());
        s.setPort(serverInsertDto.getPort());
        s.setUserName(serverInsertDto.getUserName());
        //密码加密处理
        String encrypt = myAESUtil.Encrypt(serverInsertDto.getPassWord());
        s.setPassword(encrypt);
        s.setCreator(serverInsertDto.getCreator());
        s.setCpu(cpu);
        s.setMemory(mem);
        s.setDisk(disk);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = simpleDateFormat.format(new Date());
        s.setCreateDate(format);
        int insert = serverMapper.insert(s);
        if(insert!=1){
            throw new SysCodeException(SysCodeEnum.TIME_OUT);
        }
        return true;
    }

    @Override
    public boolean updateServer(int id,String name,String details) {
        if(name.equals("")||id==0){
            throw new SysCodeException(SysCodeEnum.NULL_ERROR);
        }
        Server entity=new Server();
        entity.setId(id);
        entity.setServerName(name);
        entity.setDetails(details);
        int i= serverMapper.updateById(entity);
        if (i!=1){
            throw new SysCodeException(SysCodeEnum.TIME_OUT);
        }
        return true;
    }

    @Override
    public PageResult<List<Server>> listServer(MonitorDto serverDto) {
        List<Server> servers = serverMapper.listServer(serverDto);
        PageResult pageResult=new PageResult();
        ListPage listPage=new ListPage();
        int page=serverDto.getPage();
        int totalPage=(pageResult.getPageSize()+servers.size()-1)/pageResult.getPageSize();
        if(page<=0||page>totalPage){
            return null;
        }else {
            servers=listPage.listPage(servers,serverDto.getPage(),10);
            pageResult.setDataList(servers);
            pageResult.setTotalPage(totalPage);
            pageResult.setTotalCount(servers.size());
            return pageResult;
        }
    }

    @Override
    public boolean deleteServer(int id) {
        int i=serverMapper.deleteById(id);
        if(i!=1){
            throw new SysCodeException(SysCodeEnum.TIME_OUT);
        }return true;
    }


    public Map<String,String> runShell(String[] commands,String user, String passwd, String host,int port){
        if (!connect(user, passwd, host,port)) {
            throw new SysCodeException(SysCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        Map<String,String> map=new HashMap<>();
        StringBuffer stringBuffer;
        BufferedReader reader=null;
        Channel channel=null;
        try {
        for (String command : commands) {
            stringBuffer=new StringBuffer();
                channel=session.openChannel("exec");
                ((ChannelExec)channel).setCommand(command);
                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(System.err);
                channel.connect();
                InputStream in=channel.getInputStream();
                reader=new BufferedReader(new InputStreamReader(in));
                String buf;
                while((buf=reader.readLine())!=null){
                    if(buf.contains("PID")){break;}
                    stringBuffer.append(buf.trim()).append(Command.LINE_SEPARATOR);
                }
                map.put(command,stringBuffer.toString());
            }
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (channel != null) {
                channel.disconnect();
            }
            session.disconnect();
        }
        return map;
        }
    public  String shellSplitDiskAll(String s){
        String[] split = s.split(Command.LINE_SEPARATOR);
        String line1=split[1];
        String[] split1 = line1.split(",");
        String[] s1 = split1[1].split(" ");
        String b= s1[1];
        String mb = b.substring(0, b.length() - 9);
        return mb;
    }
    public String shellSplitDiskUsed(String s){
        String[] split=s.split(Command.LINE_SEPARATOR);
        BigDecimal b=new BigDecimal("0");
        for (int i = 0; i < split.length; i++) {
            String str=split[i];
            String s1 = str.split("\\s+")[2];
            // String used=s1[8];
            b=b.add(getParse(s1));
        }
        return b.toString();
    }
    public  BigDecimal getParse(String s){
        String lastIndex=s.substring(s.length()-1).toUpperCase();
        String value=s.substring(0,s.length()-1);
        BigDecimal bigDecimal=new BigDecimal(value);
        BigDecimal b1024=new BigDecimal("1024");
        BigDecimal b0=new BigDecimal("0");
        if(lastIndex.equals("G")){
            b0=b0.add(bigDecimal);
        }else if(lastIndex.equals("T")){
            b0=bigDecimal.multiply(b1024);
        }else if(lastIndex.equals("M")){
            b0=bigDecimal.divide(b1024,1,BigDecimal.ROUND_HALF_UP);
        }else if(lastIndex.equals("K")){
            b0=bigDecimal.divide(b1024,1,BigDecimal.ROUND_HALF_UP).divide(b1024,1,BigDecimal.ROUND_HALF_UP);
        }
        return b0;
    }
    public  boolean connect(String user, String passwd, String host, int port) {
        JSch jSch = new JSch();
        try {
            session = jSch.getSession(user, host, port);
            session.setPassword(passwd);
            Properties config=new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(host+"服务器连接失败");
        }
        return true;
    }
}
