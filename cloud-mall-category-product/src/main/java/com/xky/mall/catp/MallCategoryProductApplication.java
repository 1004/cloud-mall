package com.xky.mall.catp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/12 3:49 下午
 */
@SpringBootApplication
@EnableSwagger2
@EnableRedisHttpSession
@MapperScan(basePackages = "com.xky.mall.catp.model.dao")
@EnableCaching
public class MallCategoryProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallCategoryProductApplication.class,args);
    }
}
