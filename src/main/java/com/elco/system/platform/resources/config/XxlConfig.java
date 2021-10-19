package com.elco.system.platform.resources.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author kay
 * @date 2021/10/19
 */
@Component
public class XxlConfig {
    @Value("${xxl.job.accessToken}")
    private String accessToken;
    @Value("${xxl.job.admin.addresses}")
    private String address;
    @Value("${xxl.job.executor.appName}")
    private String appName;
    @Value("${xxl.job.executor.logpath}")
    private String path;
    @Value("${xxl.job.executor.logRetentionDays}")
    private int days;
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(){
        XxlJobSpringExecutor executor=new XxlJobSpringExecutor();
        executor.setAccessToken(accessToken);
        executor.setAdminAddresses(address);
        executor.setAppname(appName);
        executor.setLogPath(path);
        executor.setLogRetentionDays(days);
        return executor;
    }
}
