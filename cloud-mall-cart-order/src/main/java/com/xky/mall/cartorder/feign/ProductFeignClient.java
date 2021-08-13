package com.xky.mall.cartorder.feign;

import com.xky.mall.catp.model.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/8/13 10:29 上午
 */
@FeignClient(value = "mall-category-product")
public interface ProductFeignClient {

    @GetMapping("/product/feign/proDetail")
    public Product proDetailByFeign(@RequestParam Integer id);

    @PostMapping("/product/feign/update")
    public void updateByPrimaryKeySelectiveByFeign(@RequestParam Integer id,@RequestParam Integer stack);
}
