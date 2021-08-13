package com.xky.mall.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/11 5:43 下午
 */
@EnableEurekaServer //服务注册中心
@SpringBootApplication
public class MallEurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallEurekaServerApplication.class, args);
    }
}
