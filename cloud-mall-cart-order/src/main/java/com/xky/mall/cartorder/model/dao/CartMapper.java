package com.xky.mall.cartorder.model.dao;

import com.xky.mall.cartorder.model.pojo.Cart;
import com.xky.mall.cartorder.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProId(@Param("productId") Integer productId, @Param("userId") Integer userId);

    List<CartVO> selectList(@Param("userId") Integer userId);

    void updataCheck(@Param("productId") Integer productId, @Param("userId") Integer userId, @Param("checked") Integer checked);
}