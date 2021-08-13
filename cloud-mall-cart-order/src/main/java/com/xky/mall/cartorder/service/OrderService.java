package com.xky.mall.cartorder.service;

import com.github.pagehelper.PageInfo;
import com.xky.mall.cartorder.model.request.CreateOrderReq;
import com.xky.mall.cartorder.model.vo.OrderVO;

public interface OrderService {
    String create(CreateOrderReq orderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer page, Integer pageSize);

    void cancelOrder(String orderNo);

    String qrcode(String orderNo);

    PageInfo listForAdmin(Integer page, Integer pageSize);

    void pay(String orderNo);

    void deliver(String orderNo);

    void finish(String orderNo);
}
