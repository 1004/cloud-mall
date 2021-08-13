package com.xky.mall.catp.service;

import com.github.pagehelper.PageInfo;
import com.xky.mall.catp.model.pojo.Category;
import com.xky.mall.catp.model.request.AddCategoryReq;
import com.xky.mall.catp.model.vo.CategoryVo;

import java.util.List;

public interface CategoryService {
    void addCategory(AddCategoryReq addCategoryReq);

    void updataCategory(Category category);

    void deleteCategory(Integer id);

    PageInfo<Category> queryCategoryByAdmin(Integer page, Integer pageSize);

    List<CategoryVo> queryCategoryByCustomer(Integer parentId);
}
