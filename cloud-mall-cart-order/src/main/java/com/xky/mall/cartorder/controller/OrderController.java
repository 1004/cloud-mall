package com.xky.mall.cartorder.controller;

import com.xky.mall.cartorder.model.request.CreateOrderReq;
import com.xky.mall.cartorder.service.OrderService;
import com.xky.mall.common.common.CommonResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/26 3:22 下午
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    public CommonResponse create(@Valid @RequestBody CreateOrderReq orderReq) {
        return CommonResponse.success(orderService.create(orderReq));
    }

    @ApiOperation("订单详情")
    @GetMapping("/detail")
    public CommonResponse detail(@RequestParam String orderNo) {
        return CommonResponse.success(orderService.detail(orderNo));
    }

    @ApiOperation("前台订单列表")
    @GetMapping("/list")
    public CommonResponse listForCustomer(Integer page, Integer pageSize) {
        return CommonResponse.success(orderService.listForCustomer(page, pageSize));
    }

    @ApiOperation("取消订单")
    @GetMapping("/cancel")
    public CommonResponse cancel(String orderNo) {
        orderService.cancelOrder(orderNo);
        return CommonResponse.success();
    }

    @ApiOperation("生成订单支付二维码")
    @PostMapping("/qrcode")
    public CommonResponse qrcode(@RequestParam String orderNo) {
        return CommonResponse.success(orderService.qrcode(orderNo));
    }

    @ApiOperation("支付")
    @GetMapping("/pay")
    public CommonResponse pay(@RequestParam("orderNo") String orderNo) {
        orderService.pay(orderNo);
        return CommonResponse.success();
    }

    @ApiOperation("完结订单")
    @GetMapping("/finish")
    public CommonResponse finish(@RequestParam("orderNo") String orderNo) {
        orderService.finish(orderNo);
        return CommonResponse.success();
    }
}
