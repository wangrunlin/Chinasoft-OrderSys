/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinasofti.ordersys.service.admin.DishesService;
import com.chinasofti.ordersys.vo.DishesInfo;
import com.chinasofti.util.web.upload.MultipartRequestParser;
import com.chinasofti.web.common.taglib.TokenTag;

/**
 * <p>
 * Title: ModifyDishesServlet
 * </p>
 * <p>
 * Description: 修改菜品信息的Servlet
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
public class ModifyDishesServlet extends HttpServlet {

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
		// 判定是否存在表单提交令牌
		if (TokenTag.isTokenValid()) {
			// 创建菜品管理服务对象
			DishesService service = new DishesService();
			// 创建表单请求解析器工具
			MultipartRequestParser parser = new MultipartRequestParser();
			// 解析获取DishesInfo菜品信息对象
			DishesInfo info = (DishesInfo) parser.parse(request,
					DishesInfo.class);
			// 执行菜品信息修改工作
			service.modifyDishes(info);
			// 释放表单提交令牌
			TokenTag.releaseToken();
		}
		// 跳转到菜品管理界面
		response.sendRedirect("/OrderSys/todishesadmin.order");
	}



}
