package com.xky.mall.cartorder.model.request;

import javax.validation.constraints.NotNull;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/26 3:43 下午
 */
public class CreateOrderReq {

    @NotNull(message = "收货人姓名不能为空")
    private String receiverName;
    @NotNull(message = "收货人电话不能为空")
    private String receiverMobile;
    @NotNull(message = "收货人地址不能为空")
    private String receiverAddress;

    private Integer postage = 0; //运费

    private Integer paymentType; //支付类型

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
}
