package com.xky.zuul.feign;

import com.xky.mall.user.model.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/12 2:47 下午
 */
@FeignClient("mall-user-server")
public interface UserFeignClient {
    @PostMapping("/user/checkRole")
    @ResponseBody
    public boolean checkAdminRole(@RequestBody User user);
}
