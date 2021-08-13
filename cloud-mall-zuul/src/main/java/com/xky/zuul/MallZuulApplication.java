package com.xky.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/12 2:32 下午
 */
@SpringCloudApplication
@EnableZuulProxy
@EnableFeignClients
@EnableRedisHttpSession // session 存入redis 公共服务器
public class MallZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallZuulApplication.class, args);
    }
}
