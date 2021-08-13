package com.xky.mall.cartorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/12 8:49 下午
 */
@SpringBootApplication
@EnableSwagger2
@EnableFeignClients //服务通信
@EnableRedisHttpSession //session 存入 redis
@MapperScan(basePackages = "com.xky.mall.cartorder.model.dao") //mapper 扫描
public class MallCartOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallCartOrderApplication.class, args);
    }
}
