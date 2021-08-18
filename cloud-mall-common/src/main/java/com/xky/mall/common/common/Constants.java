package com.xky.mall.common.common;

import com.google.common.collect.Sets;
import com.xky.mall.common.exception.MallException;
import com.xky.mall.common.exception.MallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/20 7:42 下午
 */
@Component
public class Constants {
    public static final String SALT = "sdlkf#@]]12";
    /**
     * 登录缓存key
     */
    public static final String USER_LOGIN_CACHE_KEY = "user_login_cache_key";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    public interface SaleStatus {
        public Integer NOT_SALE = 0; //已下架
        public Integer SALE = 1; //上架
    }

    public interface Cart {
        public Integer CHECKED = 1; //已选择
        public Integer UNCHECKED = 0; //未选择
    }

    public enum OrderStatusEnum {
        CANCELED(0, "用户已取消"),
        NOT_PAY(10, "未付款"),
        PAID(20, "已付款"),
        SEND(30, "已发货"),
        FINISHED(40, "交易完成");
        private Integer code;
        private String value;

        OrderStatusEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static OrderStatusEnum codeOfValue(Integer code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.code == code) {
                    return orderStatusEnum;
                }
            }
            throw new MallException(MallExceptionEnum.ORDER_NOT_STATE);
        }

    }
}
