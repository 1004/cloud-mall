package com.xky.mall.catp.controller;

import com.github.pagehelper.PageInfo;
import com.xky.mall.catp.model.pojo.Category;
import com.xky.mall.catp.model.request.AddCategoryReq;
import com.xky.mall.catp.model.request.UpdataCategoryReq;
import com.xky.mall.catp.model.vo.CategoryVo;
import com.xky.mall.catp.service.CategoryService;
import com.xky.mall.common.common.CommonResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/21 5:10 下午
 * 分类处理
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 保证必须登录
     * json 请求
     *
     * @param category
     * @param session
     * @return
     */
    @ApiOperation("后台添加目录")
    @PostMapping("/admin/add")
    public CommonResponse<Object> addCatetory(@ApiParam("数据") @Valid @RequestBody AddCategoryReq category, HttpSession session) {
        //登录逻辑校验
        //用户角色登录
        categoryService.addCategory(category);
        return CommonResponse.success();
    }

    @ApiOperation("后台修改目录")
    @PostMapping("/admin/updata")
    public CommonResponse<Object> updataCategory(@Valid @RequestBody UpdataCategoryReq categoryReq, HttpSession session) {
        //用户角色登录
        Category category = new Category();
        BeanUtils.copyProperties(categoryReq, category);
        categoryService.updataCategory(category);
        return CommonResponse.success();
    }

    @ApiOperation("后台删除分类")
    @GetMapping("/admin/delete")
    public CommonResponse<Object> deleteCategory(Integer id) {
        categoryService.deleteCategory(id);
        return CommonResponse.success();
    }

    @ApiOperation("后台查询分类")
    @GetMapping("/admin/query")
    public CommonResponse<Object> adminQuery(@RequestParam Integer page, @RequestParam Integer pageSize) {
        PageInfo<Category> pageInfo = categoryService.queryCategoryByAdmin(page, pageSize);
        return CommonResponse.success(pageInfo);
    }


    @GetMapping("/query")
    public CommonResponse customerQuery() {
        List<CategoryVo> categoryVos = categoryService.queryCategoryByCustomer(0);
        return CommonResponse.success(categoryVos);
    }

}
