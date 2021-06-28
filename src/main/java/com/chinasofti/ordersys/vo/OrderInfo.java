package com.chinasofti.ordersys.vo;

import lombok.Data;

import java.util.Date;

/**
 * 订单信息
 *
 * @author leo
 * @date 2021/06/28 10:04
 **/
@Data
public class OrderInfo {

    /**
     * 订单编号 #
     * 自动增长
     */
    private int id;

    /**
     * 订单开始时间
     */
    private Date orderBeginDate;

    /**
     * 订单结束时间
     */
    private Date orderEndDate;

    /**
     * 订单的点餐员ID $
     * 外键 user_info.id
     */
    private int waiterId;

    /**
     * 订单状态
     * 0-正在用餐
     * 1-准备结帐
     * 2-已经结帐
     * 3-免单订单
     */
    private int orderStatus;

    /**
     * 订单的桌号
     */
    private int tableId;

}
