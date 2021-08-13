package com.xky.mall.catp.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xky.mall.catp.model.dao.CategoryMapper;
import com.xky.mall.catp.model.pojo.Category;
import com.xky.mall.catp.model.request.AddCategoryReq;
import com.xky.mall.catp.model.vo.CategoryVo;
import com.xky.mall.catp.service.CategoryService;
import com.xky.mall.common.exception.MallException;
import com.xky.mall.common.exception.MallExceptionEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/21 5:19 下午
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加分类
     *
     * @param addCategoryReq
     */
    @Override
    public void addCategory(AddCategoryReq addCategoryReq) {
        //先判断下是否重名
        Category category = categoryMapper.selectByName(addCategoryReq.getName());
        if (category != null) {
            throw new MallException(MallExceptionEnum.CATEGORY_NAME_REPEAT);
        }
        Category newCategory = new Category();
        BeanUtils.copyProperties(addCategoryReq, newCategory);
        int count = categoryMapper.insertSelective(newCategory);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.CATEGORY_ADD_F);
        }
    }

    @Override
    public void updataCategory(Category category) {
        //名称如果修改，不能重复
        if (StringUtils.hasLength(category.getName())) {
            Category oldCategory = categoryMapper.selectByName(category.getName());
            if (oldCategory != null && !category.getId().equals(oldCategory.getId())) {
                //查到记录，又不是一条，重复
                throw new MallException(MallExceptionEnum.CATEGORY_NAME_REPEAT);
            }
        }
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count == 0) {
            //更新失败
            throw new MallException(MallExceptionEnum.CATEGORY_UPDATA_F);
        }
    }

    /**
     * 删除分类
     *
     * @param id
     */
    @Override
    public void deleteCategory(Integer id) {
        Category oldCategory = categoryMapper.selectByPrimaryKey(id);
        if (oldCategory == null) {
            throw new MallException(MallExceptionEnum.CATEGORY_DELETE_NOT_EXIST);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.CATEGORY_DELETE_F);
        }
    }


    /**
     * 管理员平铺查询分类
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Category> queryCategoryByAdmin(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize, "type,order_num");
        List<Category> categories = categoryMapper.selectList();
        return PageInfo.of(categories);
    }

    @Override
    @Cacheable(value = "queryCategoryByCustomer")
    public List<CategoryVo> queryCategoryByCustomer(Integer parentId) {
        List<CategoryVo> temp = new ArrayList<>();
        recursiveFindCategories(temp,parentId);
        return temp;
    }

    private void recursiveFindCategories(List<CategoryVo> categoryVos, Integer parentId) {
        List<Category> categories = categoryMapper.selectListByParentId(parentId);
        if (!CollectionUtils.isEmpty(categories)) {
            for (int i=0 ;i<categories.size();i++){
                Category category = categories.get(i);
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(category,categoryVo);
                categoryVos.add(categoryVo);
                recursiveFindCategories(categoryVo.getChildCategory(),category.getId());
            }
        }
    }

}
