package com.xky.mall.cartorder.service.impl;

import com.xky.mall.cartorder.feign.ProductFeignClient;
import com.xky.mall.cartorder.feign.UserFeignClient;
import com.xky.mall.cartorder.model.dao.CartMapper;
import com.xky.mall.cartorder.model.pojo.Cart;
import com.xky.mall.cartorder.model.vo.CartVO;
import com.xky.mall.cartorder.service.CartService;
import com.xky.mall.catp.model.pojo.Product;
import com.xky.mall.common.common.Constants;
import com.xky.mall.common.exception.MallException;
import com.xky.mall.common.exception.MallExceptionEnum;
import com.xky.mall.user.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/23 6:18 下午
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public List<CartVO> add(Integer productId, Integer count) {
        //校验
        validProduct(productId, count);
        User user = userFeignClient.getUserByFeign();
        //是否已经在购物车了
        Cart cart = cartMapper.selectByUserIdAndProId(productId, user.getId());
        if (cart == null) {
            //不在购物车， 新增当前记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setQuantity(count);
            cart.setUserId(user.getId());
            cart.setSelected(Constants.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        } else {
            //在购物车，只需要数量+1
            Cart updateCart = new Cart();
            updateCart.setId(cart.getId());
            updateCart.setQuantity(cart.getQuantity() + 1);
            updateCart.setSelected(Constants.Cart.CHECKED);
            updateCart.setProductId(productId);
            updateCart.setUserId(user.getId());
            cartMapper.updateByPrimaryKeySelective(updateCart);
        }
        return list();
    }

    /**
     * 校验商品
     *
     * @param productId
     */
    private void validProduct(Integer productId, Integer count) {
//        ①：查询商品
        Product product = productFeignClient.proDetailByFeign(productId);
        if (product == null) {
            throw new MallException(MallExceptionEnum.CART_PRO_NOT_EXIST);
        }
        if (product.getStatus() == Constants.SaleStatus.NOT_SALE) {
            throw new MallException(MallExceptionEnum.CART_PRO_HAS_SALE);
        }
        //库存不足
        if (product.getStock() < count) {
            throw new MallException(MallExceptionEnum.CART_PRO_NO_ENOUGH);
        }
    }

    /**
     * 查询
     *
     * @return
     */
    @Override
    public List<CartVO> list() {
        User user = userFeignClient.getUserByFeign();
        List<CartVO> cartVOS = cartMapper.selectList(user.getId());
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getQuantity() * cartVO.getProductPrice());
        }
        return cartVOS;
    }

    @Override
    public List<CartVO> update(Integer productId, Integer count) {
        //校验
        validProduct(productId, count);
        User user = userFeignClient.getUserByFeign();
        //是否已经在购物车了
        Cart cart = cartMapper.selectByUserIdAndProId(productId, user.getId());
        if (cart == null) {
            //不在购物车， 抛出异常
            throw new MallException(MallExceptionEnum.CART_PRO_NOT_EXIST);
        } else {
            //在购物车，只需要数量+1
            Cart updateCart = new Cart();
            updateCart.setId(cart.getId());
            updateCart.setQuantity(count);
            updateCart.setSelected(Constants.Cart.CHECKED);
            updateCart.setProductId(productId);
            updateCart.setUserId(user.getId());
            cartMapper.updateByPrimaryKeySelective(updateCart);
        }
        return list();
    }


    @Override
    public List<CartVO> delete(Integer productId) {
        User user = userFeignClient.getUserByFeign();
        //是否已经在购物车了
        Cart cart = cartMapper.selectByUserIdAndProId(productId, user.getId());
        if (cart == null) {
            throw new MallException(MallExceptionEnum.CART_DELETE_F);
        }
        int count = cartMapper.deleteByPrimaryKey(cart.getId());
        if (count == 0) {
            throw new MallException(MallExceptionEnum.CART_DELETE_F);
        }
        return list();
    }

    @Override
    public List<CartVO> check(Integer productId, Integer checked){
        if (checked>0){
            checked = Constants.Cart.CHECKED;
        }
        User user = userFeignClient.getUserByFeign();
        //是否已经在购物车了
        Cart cart = cartMapper.selectByUserIdAndProId(productId, user.getId());
        if (cart == null) {
            throw new MallException(MallExceptionEnum.CART_DELETE_F);
        }
        cartMapper.updataCheck(productId,user.getId(),checked);
        return list();
    }

    @Override
    public List<CartVO> checkAll(Integer checked){
        if (checked>0){
            checked = Constants.Cart.CHECKED;
        }
        User user = userFeignClient.getUserByFeign();
        cartMapper.updataCheck(null,user.getId(),checked);
        return list();
    }

}
