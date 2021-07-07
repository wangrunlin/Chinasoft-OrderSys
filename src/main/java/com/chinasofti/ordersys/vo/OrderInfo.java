/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.vo;

import java.sql.Date;

/**
 * <p>
 * Title:OrderInfo
 * </p>
 * <p>
 * Description: 订单信息VO
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */
public class OrderInfo {
	/**
	 * 订单ID
	 * */
	private int orderId;
	/**
	 * 开单时间
	 * */
	private Date orderBeginDate;
	/**
	 * 结单时间
	 * */
	private Date orderEndDate;
	/**
	 * 点单服务员ID
	 * */
	private int waiterId;
	/**
	 * 订单桌号
	 * */
	private int tableId;
	/**
	 * 点单服务员帐号
	 * */
	private String userAccount;
	/**
	 * 菜品单价
	 * */
	private float dishesPrice;
	/**
	 * 菜品名
	 * */
	private String dishesName;

	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public float getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(float dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	/**
	 * 订单状态
	 * */
	private int orderState;
	/**
	 * 菜品ID
	 * */
	private int dishes;
	/**
	 * 菜品数量
	 * */
	private int num;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getOrderBeginDate() {

		return orderBeginDate;
	}

	public void setOrderBeginDate(Date orderBeginDate) {
		this.orderBeginDate = orderBeginDate;
	}

	public Date getOrderEndDate() {
		return orderEndDate;
	}

	public void setOrderEndDate(Date orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public int getWaiterId() {
		return waiterId;
	}

	public void setWaiterId(int waiterId) {
		this.waiterId = waiterId;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public int getDishes() {
		return dishes;
	}

	public void setDishes(int dishes) {
		this.dishes = dishes;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
