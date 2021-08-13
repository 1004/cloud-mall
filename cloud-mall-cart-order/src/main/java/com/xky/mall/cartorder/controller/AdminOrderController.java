package com.xky.mall.cartorder.controller;

import com.xky.mall.cartorder.service.OrderService;
import com.xky.mall.common.common.CommonResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/27 2:34 下午
 */
@RestController
@RequestMapping("/order/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("管理员查询所有订单")
    @GetMapping("/list")
    public CommonResponse listForAll(Integer page, Integer pageSize) {
        return CommonResponse.success(orderService.listForAdmin(page, pageSize));
    }

    @ApiOperation("管理员发货")
    @GetMapping("/delivered")
    public CommonResponse delivered(@RequestParam("orderNo") String orderNo) {
        orderService.deliver(orderNo);
        return CommonResponse.success();
    }

    @ApiOperation("管理员完结订单")
    @GetMapping("/finish")
    public CommonResponse finish(@RequestParam("orderNo") String orderNo) {
        orderService.finish(orderNo);
        return CommonResponse.success();
    }
}
