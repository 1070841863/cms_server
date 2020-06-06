package com.xuecheng.manager_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author study
 * @create 2020-03-31 18:04
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.xuecheng.framework.domain.cms")//扫描实体类
@ComponentScan(basePackages = "com.xuecheng.api")//扫描接口
@ComponentScan(basePackages = "com.xuecheng.framework")//扫描commons
@ComponentScan(basePackages = "com.xuecheng.manager_cms")//扫描本项目的所有类，可以不写。写了方便维护代码。方便阅读
public class ManagerCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerCmsApplication.class,args);
    }
}
