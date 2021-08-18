package com.xky.mall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/12 11:19 上午
 */
@SpringBootApplication(scanBasePackages = "com.xky.mall.*")
@EnableSwagger2
@EnableRedisHttpSession
@MapperScan(basePackages = "com.xky.mall.user.model.dao")

public class MallUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallUserApplication.class,args);
    }
}
