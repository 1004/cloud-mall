package com.xky.mall.common.exception;

public enum MallExceptionEnum {
    USER_NEED_NAME(1000, "用户名不能为空"),
    USER_NAME_REPEAT(1001,"用户名已经存在"),
    USER_REGIST_FAILED(1002,"用户注册失败"),
    USER_NEED_PWD(1003, "密码不能为空"),
    USER_PWD_LENGTH(1004, "密码最少8位"),
    USER_LOGIN_PWD(1005, "用户名或者密码不正确"),
    USER_LOGIN_NEED(1006, "用户未登录"),
    USER_UPDATA_SIG_F(1007, "更新签名失败"),
    USER_ADMIN_LOGIN_F(1008, "管理员登录失败"),

    CATEGORY_NAME_REPEAT(1009, "分类名称重名"),
    CATEGORY_ADD_F(1010, "分类添加失败"),
    CATEGORY_UPDATA_F(1011, "分类更新失败"),
    CATEGORY_DELETE_NOT_EXIST(1012, "删除分类失败-不存在分类"),
    CATEGORY_DELETE_F(1013, "删除分类失败"),


    PRODUCT_NAME_REPEAT(1014, "商品名字重复"),
    PRODUCT_ADD_F(1015, "商品新增失败"),
    PRODUCT_UPDATE_F(1016, "修改商品失败"),
    PRODUCT_NOT_EXIST(1017, "商品不存在"),
    PRODUCT_DELETE_F(1018, "商品删除失败"),


    CART_PRO_NOT_EXIST(1019, "商品不存在"),
    CART_PRO_HAS_SALE(1010, "商品已下架"),
    CART_PRO_NO_ENOUGH(1011, "商品库存不足"),
    CART_DELETE_F(1012, "商品删除失败"),

    ORDER_NO_SELECT(1013, "未选择商品,无法下单"),
    ORDER_NO_ENOUGH(1014, "库存不够,无法下单"),
    ORDER_NOT_STATE(1015, "订单状态异常"),
    ORDER_NO_EXIST(1016, "订单不存在"),
    ORDER_NO_PER(1017, "无权限操作该订单"),
    ORDER_NO_CANCEL(1018, "订单不能取消"),


    SYSTEM_ERROR(10000, "系统异常"),
    SYSTEM_UPLOAD_F(10002, "图片上传失败"),
    SYSTEM_PARAM_EXCEPTION(10001, "参数异常");

    private int code;
    private String msg;

    MallExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
