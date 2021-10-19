package com.elco.system.platform.resources.config;

/**
 * @author kay
 * @date 2021/8/31
 */
public class Command {
    //cpu的核数
    public static final String CPU_CORE="cat /proc/cpuinfo | grep \"cpu cores\" | uniq |cut -d : -f 2";
    //cpu的个数
    public static final String CPU_AMOUNTS="cat /proc/cpuinfo | grep \"physical id\" | sort | uniq | wc -l";
    //cpu的主频
    public static final String CPU_GHZ="cat /proc/cpuinfo | grep GHz |uniq |cut -d @ -f 2";
    //总内存大小
    public static final String MEMORY_ALL="top -b -n 1 | grep \"KiB Mem\" |cut -d : -f 2|cut -d, -f 1|cut -d t -f 1";
    //总硬盘大小,因为每个服务器划分分区不同，所以只能代码解析
    public static final String DISK_ALL="/usr/sbin/fdisk -l";
    //cpu剩余率
    public static final String CPU_UNUSED="top -b -n 1 |grep \"%Cpu(s)\" |cut -d , -f 4 |cut -d i -f 1";
    //内存使用大小
    public static final String MEMORY_USED="top -b -n 1 |grep \"KiB Mem\" |cut -d, -f3 |cut -d u -f 1";
    //硬盘使用大小,因为每个服务器划分分区不同，所以只能代码解析
    public static final String DISK_USED="df -h | grep \"/dev/\" |grep -v tmpfs";

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
}
