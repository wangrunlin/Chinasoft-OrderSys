/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.vo;

import java.util.ArrayList;

/**
 * <p>
 * Title:Cart
 * </p>
 * <p>
 * Description: 点单购物车VO
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
public class Cart {

	/**
	 * 订单桌号
	 * */
	private int tableId;

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	/**
	 * 订单菜品详情列表
	 * */
	private ArrayList<CartUnit> units = new ArrayList<CartUnit>();

	public ArrayList<CartUnit> getUnits() {
		return units;
	}

	public void setUnits(ArrayList<CartUnit> units) {
		this.units = units;
	}

	/**
	 * 创建菜品详情对象方法
	 * 
	 * @param dishesId
	 *            菜品编号
	 * @param num
	 *            菜品数量
	 * @return 创建好的菜品详情对象
	 * */
	public CartUnit createUnit(int dishesId, int num) {
		return new CartUnit(dishesId, num);
	}

	/**
	 * <p>
	 * Title:CartUnit
	 * </p>
	 * <p>
	 * Description: 购物车菜品单元VO
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
	public class CartUnit {

		/**
		 * 菜品ID
		 * */
		private int dishesId;
		/**
		 * 菜品数量
		 * */
		private int num;

		private CartUnit(int dishesId, int num) {
			super();
			this.dishesId = dishesId;
			this.num = num;
		}

		public int getDishesId() {
			return dishesId;
		}

		public void setDishesId(int dishesId) {
			this.dishesId = dishesId;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

	}

}
