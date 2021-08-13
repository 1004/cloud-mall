package com.xky.mall.catp.model.query;

import java.util.List;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/23 2:57 下午
 * 对商品的查询条件进行组装
 */
public class ProductListQuery {

    private String keyword;//搜索
    private List<Integer> categoryIds; // 当前分类下的素有子分类

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
