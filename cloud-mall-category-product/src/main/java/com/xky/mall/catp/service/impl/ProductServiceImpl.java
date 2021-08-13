package com.xky.mall.catp.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xky.mall.catp.model.dao.ProductMapper;
import com.xky.mall.catp.model.pojo.Product;
import com.xky.mall.catp.model.query.ProductListQuery;
import com.xky.mall.catp.model.request.AddProductReq;
import com.xky.mall.catp.model.request.SelectProductReq;
import com.xky.mall.catp.model.vo.CategoryVo;
import com.xky.mall.catp.service.CategoryService;
import com.xky.mall.catp.service.ProductService;
import com.xky.mall.common.common.Constants;
import com.xky.mall.common.exception.MallException;
import com.xky.mall.common.exception.MallExceptionEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/22 7:41 下午
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void addProduct(AddProductReq productReq) {
        Product oldProduct = productMapper.selectByName(productReq.getName());
        if (oldProduct != null) {
            throw new MallException(MallExceptionEnum.PRODUCT_NAME_REPEAT);
        }
        Product product = new Product();
        BeanUtils.copyProperties(productReq, product);
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.PRODUCT_ADD_F);
        }
    }

    @Override
    public void updateProduct(Product product) {
        if (StringUtils.hasLength(product.getName())) {
            Product oldProduct = productMapper.selectByName(product.getName());
            if (oldProduct != null && !oldProduct.getId().equals(product.getId())) {
                throw new MallException(MallExceptionEnum.PRODUCT_NAME_REPEAT);
            }
        }
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.PRODUCT_UPDATE_F);
        }
    }

    @Override
    public void updataStack(Integer productId,Integer stock){
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            throw new MallException(MallExceptionEnum.PRODUCT_NOT_EXIST);
        }
        product.setStock(stock);
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.PRODUCT_UPDATE_F);
        }
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new MallException(MallExceptionEnum.PRODUCT_NOT_EXIST);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.PRODUCT_DELETE_F);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    @Override
    public PageInfo listForAdmin(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Product> products = productMapper.listForAdmin();
        return PageInfo.of(products);
    }

    @Override
    public Product selectDetail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    /**
     * 前台查询
     *
     * @param productReq
     * @return
     */
    @Override
    public PageInfo listProduct(SelectProductReq productReq) {
        ProductListQuery listQuery = new ProductListQuery();
        //搜索
        if (StringUtils.hasLength(productReq.getKeyword())) {
            String k = new StringBuffer().append("%").append(productReq.getKeyword()).append("%").toString();
            listQuery.setKeyword(k);
        }
        //子分类平铺
        if (productReq.getCategoryId() != null) {
            List<Integer> allChildCategoryIds = new ArrayList<>();
            allChildCategoryIds.add(productReq.getCategoryId());
            //先查询category所有的子分类id
            List<CategoryVo> categoryVos = categoryService.queryCategoryByCustomer(productReq.getCategoryId());
            fullChildCategoryId(allChildCategoryIds, categoryVos);
            listQuery.setCategoryIds(allChildCategoryIds);
        }
        //排序
        if (Constants.ProductListOrderBy.PRICE_ASC_DESC.contains(productReq.getOrderBy())) {
            PageHelper.startPage(productReq.getPage(), productReq.getPageSize(), productReq.getOrderBy());
        } else {
            PageHelper.startPage(productReq.getPage(), productReq.getPageSize());
        }
        List<Product> products = productMapper.selectList(listQuery);
        return PageInfo.of(products);
    }

    private void fullChildCategoryId(List<Integer> container, List<CategoryVo> categoryVos) {
        if (!CollectionUtils.isEmpty(categoryVos)) {
            for (int i = 0; i < categoryVos.size(); i++) {
                CategoryVo categoryVo = categoryVos.get(i);
                if (categoryVo != null) {
                    container.add(categoryVo.getId());
                    fullChildCategoryId(container, categoryVo.getChildCategory());
                }
            }
        }
    }


}
