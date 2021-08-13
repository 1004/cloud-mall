package com.xky.mall.common.exception;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/20 5:42 下午
 * 通用异常
 */
public class MallException extends RuntimeException {
    private int code;
    private String msg;

    public MallException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 关联到通用枚举
     *
     * @param exceptionEnum
     */
    public MallException(MallExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
