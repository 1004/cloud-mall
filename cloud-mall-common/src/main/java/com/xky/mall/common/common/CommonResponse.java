package com.xky.mall.common.common;


import com.xky.mall.common.exception.MallExceptionEnum;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/20 3:38 下午
 */
public class CommonResponse<T> {
    private static final int CODE_SUCCESS = 0;
    private static final String MSG_SUCCESS = "success";

    /**
     * 暴露几个工具方法
     */
    public static <T> CommonResponse<T> success() {
        return new CommonResponse<>();
    }

    public static <T> CommonResponse<T> success(T t) {
        return new CommonResponse<>(CODE_SUCCESS, MSG_SUCCESS, t);
    }

    public static <T> CommonResponse<T> error(int code, String msg) {
        return new CommonResponse<T>(code, msg);
    }

    public static <T> CommonResponse<T> error(MallExceptionEnum exceptionEnum) {
        return new CommonResponse<T>(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }


    public Integer code;
    public String msg;
    public T data;

    public CommonResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CommonResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResponse() {
        this(CODE_SUCCESS, MSG_SUCCESS);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
