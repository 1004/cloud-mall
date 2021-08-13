package com.xky.mall.cartorder.service;


import com.xky.mall.cartorder.model.vo.CartVO;

import java.util.List;

public interface CartService {
    List<CartVO> add(Integer productId, Integer count);

    List<CartVO> list();

    List<CartVO> update(Integer productId, Integer count);

    List<CartVO> delete(Integer productId);

    List<CartVO> check(Integer productId, Integer checked);

    List<CartVO> checkAll(Integer checked);
}
