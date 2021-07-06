/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.waiters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chinasofti.ordersys.vo.Cart;

/**
 * <p>
 * Title:AddCartServlet
 * </p>
 * <p>
 * Description: 加入购物车的Servlet
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
public class AddCartServlet extends HttpServlet {

	/**
	 * 当以GET方式请求Servlet时由service方法回调,本Servlet不同方法不要求不同的响应，因此在本Servlet中直接调用doPost
	 * ，以保证响应的一致性
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * 当以Post方式请求Servlet时由service方法回调
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取会话对象
		HttpSession session = request.getSession();
		// 创建购物车对象
		Cart cart = new Cart();
		// 如果会话中的存在购物车
		if (session.getAttribute("CART") != null) {
			// 直接获取会话中的购物车对象
			cart = (Cart) session.getAttribute("CART");
		}
		// 定义桌号变量
		Integer tableId = 1;
		// 如果会话中存在桌号信息
		if (session.getAttribute("TABLE_ID") != null) {
			// 直接获取桌号信息
			tableId = (Integer) session.getAttribute("TABLE_ID");
		}
		//设置购物车的桌号信息
		cart.setTableId(tableId.intValue());
		//获取本次加入购物车的菜品数量
		int num = Integer.parseInt(request.getParameter("num"));
		//获取本次加入购物车的菜品ID
		int dishesId = Integer.parseInt(request.getParameter("dishes"));
		//将菜品ID和菜品数量加入到购物车菜品详情中
		cart.getUnits().add(cart.createUnit(dishesId, num));
		//将购物车对象设置到会话中
		session.setAttribute("CART", cart);

//		for (Cart.CartUnit unit : cart.getUnits()) {
//
//			System.out.println(unit.getDishesId() + "\t" + unit.getNum());
//
//		}
	}



}
