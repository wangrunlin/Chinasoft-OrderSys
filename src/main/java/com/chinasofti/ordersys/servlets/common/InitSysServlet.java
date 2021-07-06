/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: InitSysServlet
 * </p>
 * <p>
 * Description: 初始化系统信息的Servlet
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
public class InitSysServlet extends HttpServlet {

	/**
	 * 当以GET方式请求Servlet时由service方法回调
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * 当以POST方式请求Servlet时由service方法回调
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * 初始化本Servlet的方法，本案例中用于获取项目的名称
	 */
	public void init() throws ServletException {
		// Put your code here
		// 定义本项目名称
		String sysName = "中软国际-餐厅到店点餐系统";
		// 尝试从配置文件中读取自定义项目名称
		try {
			// 读取自定义项目名称
			sysName = getServletConfig().getInitParameter("sysname") != null ? getServletConfig()
					.getInitParameter("sysname") : "中软国际-餐厅到店点餐系统";
			// 忽略异常信息
		} catch (Exception ex) {

		}
		// 将项目名信息设置到ServletContext作用域中
		getServletContext().setAttribute("ORDER_SYS_NAME", sysName);
	}

}
