package com.xky.mall.catp.service;

import com.github.pagehelper.PageInfo;
import com.xky.mall.catp.model.pojo.Product;
import com.xky.mall.catp.model.request.AddProductReq;
import com.xky.mall.catp.model.request.SelectProductReq;

public interface ProductService {
    void addProduct(AddProductReq productReq);

    void updateProduct(Product product);

    void updataStack(Integer productId, Integer stock);

    void deleteProduct(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer page, Integer pageSize);

    Product selectDetail(Integer id);

    PageInfo listProduct(SelectProductReq productReq);
}
