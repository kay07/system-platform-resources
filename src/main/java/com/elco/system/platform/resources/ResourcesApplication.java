package com.elco.system.platform.resources;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author kay
 * @date 2021/8/20
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value = "com.elco.system.platform.resources.mapper")
public class ResourcesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourcesApplication.class,args);
    }
}
