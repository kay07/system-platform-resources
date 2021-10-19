package com.elco.system.platform.resources.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.elco.platform.util.ListPage;
import com.elco.platform.util.PageResult;
import com.elco.system.platform.resources.config.AllStatus;
import com.elco.system.platform.resources.config.Command;
import com.elco.system.platform.resources.entity.Rules;
import com.elco.system.platform.resources.entity.Server;
import com.elco.system.platform.resources.entity.dto.MonitorDto;
import com.elco.system.platform.resources.entity.vo.FourType;
import com.elco.system.platform.resources.entity.vo.MonitorVo;
import com.elco.system.platform.resources.entity.vo.StatusVo;
import com.elco.system.platform.resources.mapper.MonitorMapper;
import com.elco.system.platform.resources.mapper.RulesMapper;
import com.elco.system.platform.resources.mapper.ServerMapper;
import com.elco.system.platform.resources.service.MonitorService;
import com.elco.system.platform.resources.service.ServerService;
import com.elco.system.platform.resources.utils.MyAESUtil;
import com.elco.system.platform.resources.utils.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.data.redis.core.ClusterOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author kay
 * @date 2021/9/3
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    private static final String REDIS_KEY="PLATFORM:KEY";
    private static final int TIME_ALL=600;
    @Resource
    private ServerMapper serverMapper;
    @Resource
    private RulesMapper rulesMapper;
    @Resource
    private MyAESUtil myAESUtil;
    @Resource
    private RedisTemplate redisTemplate;

    @XxlJob("serverHandler")
    public void serverHandler() throws Exception {
        intoRedis();
    }
    @Override
    public PageResult<StatusVo> list(MonitorDto monitorDto){
        ObjectMapper objectMapper=new ObjectMapper();
        Object obj =redisTemplate.opsForValue().get(REDIS_KEY);
        StatusVo o = objectMapper.convertValue(obj, StatusVo.class);
        if(o==null){
            o = intoRedis();
        }
        //根据输入的条件进行查询
        boolean bName=(monitorDto.getName()!=null)&&(monitorDto.getName()!="");
        boolean bType=(monitorDto.getType()!=null)&&(monitorDto.getType()!="");
        boolean bTime=(monitorDto.getStartTime()!=null)&&(monitorDto.getEndTime()!=null)&&(monitorDto.getStartTime()!="")&&(monitorDto.getEndTime()!="");
        List<MonitorVo> data=o.getMonitorVos();
        int s1=0;
        int s2=0;
        int s3=0;
        List<MonitorVo> newData=new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String nodeName = data.get(i).getNodeName();
            String now=data.get(i).getCreateDate();
            String status = data.get(i).getStatus();
            boolean sta1 = status.equals(AllStatus.STATUS1.getMsg());
            boolean sta2 = status.equals(AllStatus.STATUS2.getMsg());
            boolean sta3 = status.equals(AllStatus.STATUS3.getMsg());

            boolean eName=nodeName.contains(monitorDto.getName());
            boolean eType=status.equals(monitorDto.getType());
            boolean eTime=containDate(now,monitorDto.getStartTime(),monitorDto.getEndTime());
            if(bName){
                if(!eName){
                    continue;
                }
            }
            if(bType) {
                if (!eType) {
                    continue;
                }
            }
            if(bTime) {
                if (!eTime) {
                    continue;
                }
            }
            if(sta1){s1++;}
            if(sta2){s2++;}
            if(sta3){s3++;}
            newData.add(data.get(i));
//            if(bName&&bType){
//                if(eName&&eType){
//                    newData.add(data.get(i));
//                    //统计状态数量
//                    if(sta1){s1++;}
//                    if(sta2){s2++;}
//                    if(sta3){s3++;}
//                }
//            }else if(bName){
//                if(eName){
//                    newData.add(data.get(i));
//                    if(sta1){s1++;}
//                    if(sta2){s2++;}
//                    if(sta3){s3++;}
//                }
//            }else if (bType){
//                if(eType){
//                    newData.add(data.get(i));
//                    if(sta1){s1++;}
//                    if(sta2){s2++;}
//                    if(sta3){s3++;}
//                }
//            }else {
//                newData.add(data.get(i));
//                if(sta1){s1++;}
//                if(sta2){s2++;}
//                if(sta3){s3++;}
//            }
        }
        //StatusVo o = intoRedis(monitorDto);
        List<StatusVo> statusVoList=new ArrayList<>();
        StatusVo statusVo=new StatusVo();
        PageResult pageResult=new PageResult();
        pageResult.setDataList(statusVoList);
        int total=newData.size();
        pageResult.setTotalCount(total);
        int page=monitorDto.getPage();
        //每页设置为8
        pageResult.setPageSize(8);
        int pageSize=pageResult.getPageSize();
        int totalPage=(total+pageSize-1)/pageSize;
        pageResult.setTotalPage(totalPage);
        if(page<1||page>totalPage){
            return null;
        }else {
            List<MonitorVo> monitorVoList=newData;
            ListPage listPage=new ListPage();
            List list = listPage.listPage(monitorVoList, page, pageSize);
            statusVo.setMonitorVos(list);
            statusVo.setStatus1(s1);
            statusVo.setStatus2(s2);
            statusVo.setStatus3(s3);
            statusVo.setStatus(s1+s2+s3);
            statusVoList.add(statusVo);
            return pageResult;
        }
    }
    //获取所有数据并放入redis中
    public StatusVo intoRedis() {
        Rules rules = rulesMapper.selectById(RulesServiceImpl.ID);
        //规则为空时返回错误
        if(rules==null){
            rules.setHigh(66);
            rules.setLow(33);
//            throw new RuntimeException("请先设置规则!!!");
        }
        List<Server> servers = serverMapper.list();
        List<MonitorVo> monitorVoList = new ArrayList<>();
        int s1 = 0;
        int s2 = 0;
        int s3 = 0;
        //遍历所有节点，获取信息

        for (int i = 0; i < servers.size(); i++) {
            MonitorVo monitorVo = new MonitorVo();
            Server server = servers.get(i);
            //填充已知类型
            monitorVo.setId(server.getId());
            monitorVo.setNodeName(server.getServerName());
            monitorVo.setCreator(server.getCreator());
            monitorVo.setCreateDate(server.getCreateDate());
            monitorVo.setDetails(server.getDetails());

            //动态获取cpu，内存，硬盘使用率
            List<FourType> lf = getComputer(server,rules);
            FourType cpu = lf.get(0);
            FourType memory = lf.get(1);
            FourType disk = lf.get(2);
            monitorVo.setCpu(cpu);
            monitorVo.setMemory(memory);
            monitorVo.setDisk(disk);
            //判断服务器的状态

            String status = getStatus(rules, cpu.getRate(), memory.getRate(), disk.getRate());
            if(status.equals(AllStatus.STATUS1.getMsg())){
                s1++;
            }
            if(status.equals(AllStatus.STATUS2.getMsg())){
                s2++;
            }
            if(status.equals(AllStatus.STATUS3.getMsg())){
                s3++;
            }
            monitorVo.setStatus(status);
            monitorVoList.add(monitorVo);
//            String name =monitorDto.getName();
//            String type =monitorDto.getType();
//            boolean bName=(name!=null)&&(!"".equals(name));
//            boolean bType=(type!=null)&&(!"".equals(type));
//            boolean eType=status.equals(type);
//            if(bType){
//                if(eType){
//                    monitorVoList.add(monitorVo);
//                }
//            }else {
//                monitorVoList.add(monitorVo);
//            }
            //均不为空
/*            if(bName&&bType){
                boolean eName=server.getServerName().contains(name);
                if(eName&&eType){
                    //放入list中
                    monitorVoList.add(monitorVo);
                }
            }else if(bName){
                boolean eName=server.getServerName().contains(name);
                if(eName){
                    monitorVoList.add(monitorVo);
                }
            }else if(bType){
                if(eType){
                    monitorVoList.add(monitorVo);
                }
            }else {
                monitorVoList.add(monitorVo);
            }*/
        }
        StatusVo statusVo=new StatusVo();
        statusVo.setMonitorVos(monitorVoList);
        statusVo.setStatus1(s1);
        statusVo.setStatus2(s2);
        statusVo.setStatus3(s3);
        statusVo.setStatus(s1+s2+s3);
        //将数据存入redis
        //redisUtil.set(REDIS_KEY,statusVo,60);
        ValueOperations<String,StatusVo> ops = redisTemplate.opsForValue();
        ops.set(REDIS_KEY,statusVo);
        redisTemplate.expire(REDIS_KEY,TIME_ALL, TimeUnit.SECONDS);
        return statusVo;
    }

    public List<FourType> getComputer(Server server, Rules rules) {
        ServerServiceImpl serverServiceImpl = new ServerServiceImpl();
        String[] commands = {Command.CPU_UNUSED, Command.MEMORY_USED, Command.DISK_USED};
        String password = myAESUtil.Decrypt(server.getPassword());
        Map<String, String> map = serverServiceImpl.runShell(commands, server.getUserName(), password, server.getIp(), Integer.parseInt(server.getPort()));
        String cpu = map.get(Command.CPU_UNUSED);
        String mem = map.get(Command.MEMORY_USED);
        String disk = serverServiceImpl.shellSplitDiskUsed(map.get(Command.DISK_USED));
        cpu = cpu.replaceAll("\r|\n", "");
        mem = mem.replaceAll("\r|\n", "");

        List<FourType> list = new ArrayList<>();
        //cpu是未使用率，返回使用率
        BigDecimal c = new BigDecimal(cpu);
        BigDecimal c100 = new BigDecimal("100");
        cpu = c100.subtract(c).toString();
        //计算cpu已使用
        String cpuAll = server.getCpu().substring(0, server.getCpu().length() - 3);
        BigDecimal cpuUsed = (new BigDecimal(cpuAll)).multiply(c100.subtract(c)).divide(c100, 1, BigDecimal.ROUND_HALF_UP);
        //计算cpu未使用
        BigDecimal cpuUnUsed = (new BigDecimal(cpuAll)).subtract(cpuUsed);
        FourType fourTypeCpu = new FourType();
        fourTypeCpu.setAll(cpuAll + "GHz");
        fourTypeCpu.setRate(cpu + "%");
        fourTypeCpu.setUsed(cpuUsed.toString()+"GHz");
        fourTypeCpu.setUnUsed(cpuUnUsed.toString()+"GHz");
        fourTypeCpu.setStatus(getOneStatus(rules,cpu));
        list.add(fourTypeCpu);
        //内存是使用大小
        BigDecimal b1 = new BigDecimal(mem);
        BigDecimal b2 = new BigDecimal("1024");
        mem = b1.divide(b2, 1, BigDecimal.ROUND_HALF_UP).divide(b2, 1, BigDecimal.ROUND_HALF_UP).toString();
        //计算使用率
        String memAll = server.getMemory().substring(0, server.getMemory().length() - 1);
        BigDecimal memRate = new BigDecimal(mem).multiply(c100).divide(new BigDecimal(memAll), 1, BigDecimal.ROUND_HALF_UP);
        //计算未使用
        BigDecimal memUnUsed = new BigDecimal(memAll).subtract(new BigDecimal(mem));
        FourType fourTypeMem = new FourType();
        fourTypeMem.setUsed(mem+"G");
        fourTypeMem.setAll(memAll + "G");
        fourTypeMem.setRate(memRate.toString() + "%");
        fourTypeMem.setUnUsed(memUnUsed.toString()+"G");
        fourTypeMem.setStatus(getOneStatus(rules,memRate.toString()));
        list.add(fourTypeMem);
        //硬盘是使用大小
        String diskAll = server.getDisk().substring(0, server.getDisk().length() - 1);
        //计算使用率
        BigDecimal diskRate = new BigDecimal(disk).multiply(c100).divide(new BigDecimal(diskAll), 1, BigDecimal.ROUND_HALF_UP);
        //计算未使用
        BigDecimal diskUnUsed = new BigDecimal(diskAll).subtract(new BigDecimal(disk));
        FourType fourTypeDisk = new FourType();
        fourTypeDisk.setAll(diskAll + "G");
        fourTypeDisk.setUsed(disk+"G");
        fourTypeDisk.setRate(diskRate.toString() + "%");
        fourTypeDisk.setUnUsed(diskUnUsed.toString()+"G");
        fourTypeDisk.setStatus(getOneStatus(rules,diskRate.toString()));
        list.add(fourTypeDisk);
        return list;
    }

    public String getStatus(Rules rules, String cpu, String mem, String disk) {
        //取出cpu,mem,disk中最大的
        cpu = cpu.substring(0, cpu.length() - 1);
        mem = mem.substring(0, mem.length() - 1);
        disk = disk.substring(0, disk.length() - 1);
        double c1 = Double.parseDouble(cpu);
        double m1 = Double.parseDouble(mem);
        double d1 = Double.parseDouble(disk);
        Double max = 0.0;
        max = c1 > m1 ? c1 : m1;
        max = max > d1 ? max : d1;
        if (max <= rules.getLow()) {
            return AllStatus.STATUS3.getMsg();
        } else if (max <= rules.getHigh()) {
            return AllStatus.STATUS2.getMsg();
        } else {
            return AllStatus.STATUS1.getMsg();
        }
    }
    //根据规则判断状态
    public String getOneStatus(Rules rules,String rate){
//        rate = rate.substring(0, rate.length() - 1);
        double c1 = Double.parseDouble(rate);
        if (c1 <= rules.getLow()) {
            return AllStatus.STATUS3.getMsg();
        } else if (c1 <= rules.getHigh()) {
            return AllStatus.STATUS2.getMsg();
        } else {
            return AllStatus.STATUS1.getMsg();
        }
    }
    public boolean containDate(String now,String start,String end){
        boolean bTime = (start != null) && (end != null) && (!"".equals(start)) && (!"".equals(end));
        if (bTime) {
            //时间段不为空
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
}
