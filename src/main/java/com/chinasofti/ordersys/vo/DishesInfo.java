package com.chinasofti.ordersys.vo;

import lombok.Data;

/**
 * 订单信息
 *
 * @author leo
 * @date 2021/06/25 15:27
 **/
@Data
public class DishesInfo {

    /**
     * 菜品编号 #
     * 自动增长
     */
    private int id;

    /**
     * 菜品名称
     */
    private String dishesName;

    /**
     * 菜品的简单介绍
     */
    private String dishesDescription;

    /**
     * 菜品图片文件名称
     */
    private String dishesImg = "default.jpg";

    /**
     * 菜品的简单介绍
     */
    private String dishesTxt;

    /**
     * 是否推荐菜品
     * 0-非推荐
     * 1-推荐菜品
     */
    private int recommend = 0;

    /**
     * 菜品单价
     */
    private float dishesPrice;

}
