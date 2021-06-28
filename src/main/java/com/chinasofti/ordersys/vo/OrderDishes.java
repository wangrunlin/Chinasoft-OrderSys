package com.chinasofti.ordersys.vo;

import lombok.Data;

/**
 * 订单菜品
 *
 * @author leo
 * @date 2021/06/28 09:59
 **/
@Data
public class OrderDishes {

    /**
     * 详单编号 #
     * 自动增长
     */
    private int id;

    /**
     * 对应订单编号 $
     * 外键 order_info.id
     */
    private int orderId;

    /**
     * 对应菜品编号 $
     * 外键 dishes_info.id
     */
    private int dishesId;

    /**
     * 菜品的数量
     */
    private int num;

}
