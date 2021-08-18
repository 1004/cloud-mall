package com.xky.mall.cartorder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/21 8:03 下午
 * 接口文档配置
 */
@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 路径匹配的意思， 请求地址-->映射到本地静态资源
         */
        registry.addResourceHandler("/image/**").addResourceLocations("file:"+ OrderUploadFileConstants.FILE_UPLOAD_DIR);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
}
