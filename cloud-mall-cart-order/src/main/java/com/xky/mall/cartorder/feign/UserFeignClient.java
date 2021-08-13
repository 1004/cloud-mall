package com.xky.mall.cartorder.feign;

import com.xky.mall.user.model.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "mall-user-server")
public interface UserFeignClient {
    @GetMapping("/user/getUserByFeign")
    public User getUserByFeign();
}
