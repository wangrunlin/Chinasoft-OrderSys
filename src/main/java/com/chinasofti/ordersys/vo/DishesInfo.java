/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.vo;

/**
 * <p>
 * Title:DishesInfo
 * </p>
 * <p>
 * Description: 菜品信息VO
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
public class DishesInfo {
	/**
	 * 菜品ID
	 * */
	private int dishesId;
	/**
	 * 菜品名称
	 * */
	private String dishesName;
	/**
	 * 菜品描述
	 * */
	private String dishesDiscript;
	/**
	 * 菜品图片
	 * */
	private String dishesImg;
	/**
	 * 菜品详细描述文本
	 * */
	private String dishesTxt;
	/**
	 * 是否推荐菜品标识
	 * */
	private int recommend;
	/**
	 * 菜品单价
	 * */
	private float dishesPrice;

	public int getDishesId() {
		return dishesId;
	}

	public void setDishesId(int dishesId) {
		this.dishesId = dishesId;
	}

	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public String getDishesDiscript() {
		return dishesDiscript;
	}

	public void setDishesDiscript(String dishesDiscript) {
		this.dishesDiscript = dishesDiscript;
	}

	public String getDishesImg() {
		return dishesImg;
	}

	public void setDishesImg(String dishesImg) {
		this.dishesImg = dishesImg;
	}

	public String getDishesTxt() {
		return dishesTxt;
	}

	public void setDishesTxt(String dishesTxt) {
		this.dishesTxt = dishesTxt;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public float getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(float dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

}
