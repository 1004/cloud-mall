package com.xky.mall.catp.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xiekongying
 * @version 1.0
 * @date 2021/7/21 5:12 下午
 * 添加分类的实体
 */
public class AddCategoryReq {

    @NotNull(message = "分类名称不能为空")
    @Size(min = 2, max = 8)
    private String name;

    @NotNull(message = "分类层级不能为空")
    @Max(3)
    private Integer type;

    @NotNull(message = "父分类id不能为空")
    private Integer parentId;

    @NotNull(message = "排序不能为空")
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
