package com.xky.mall.cartorder.controller;

import com.xky.mall.cartorder.model.vo.CartVO;
import com.xky.mall.cartorder.service.CartService;
import com.xky.mall.common.common.CommonResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/23 6:11 下午
 * 购物车
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public CommonResponse add(@RequestParam("productId") Integer productId, @RequestParam("count") Integer count) {
        List<CartVO> vos = cartService.add(productId, count);
        return CommonResponse.success(vos);
    }

    @GetMapping("/list")
    public CommonResponse list() {
        List<CartVO> list = cartService.list();
        return CommonResponse.success(list);
    }

    @ApiOperation("更新购物车")
    @PostMapping("/update")
    public CommonResponse update(@RequestParam("productId") Integer productId, @RequestParam("count") Integer count) {
        List<CartVO> update = cartService.update(productId, count);
        return CommonResponse.success(update);
    }

    @ApiOperation("删除购物车商品")
    @PostMapping("/delete")
    public CommonResponse delete(@RequestParam("productId") Integer productId) {
        List<CartVO> delete = cartService.delete(productId);
        return CommonResponse.success(delete);
    }

    @PostMapping("/check")
    public CommonResponse checkOrNot(@RequestParam("productId") Integer productId, @RequestParam("checked") Integer checked) {
        List<CartVO> check = cartService.check(productId, checked);
        return CommonResponse.success(check);
    }

    @PostMapping("/checkall")
    public CommonResponse checkAll(@RequestParam("checked") Integer checked) {
        List<CartVO> cartVOS = cartService.checkAll(checked);
        return CommonResponse.success(cartVOS);
    }
}
