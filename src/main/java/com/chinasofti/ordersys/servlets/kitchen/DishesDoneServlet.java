/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.kitchen;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinasofti.util.web.serverpush.MessageProducer;

/**
 * <p>
 * Title:DishesDoneServlet
 * </p>
 * <p>
 * Description: 菜品完成推送传菜消息的Servlet
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
public class DishesDoneServlet extends HttpServlet {

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
		// 设置响应结果集
		response.setCharacterEncoding("utf-8");
		// 获取菜品对应的桌号
		String tableId = request.getParameter("tableId");
		// 获取菜品名
		String dishesName = request.getParameter("dishesName");
		// 由于使用ajax提交，因此需要转码
		dishesName = new String(dishesName.getBytes("iso8859-1"), "utf-8");
		// 创建消息生产者
		MessageProducer producer = new MessageProducer();
		// 获取服务员等待列表
		ArrayList<String> list = GetRTDishesServlet.disheses;
		// 遍历服务员等待列表
		for (int i = list.size() - 1; i >= 0; i--) {
			// 获取特定的服务员SessionID
			String id = list.get(i);
			// 对该服务员生产菜品完成等待传菜的消息
			producer.sendMessage(id, "rtdishes", "桌号[" + tableId + "]的菜品["
					+ dishesName + "]已经烹制完成，请传菜！");
			// 从等待列表中删除该服务员
			list.remove(id);
		}
	}

}
