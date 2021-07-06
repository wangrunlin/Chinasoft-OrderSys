/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Title: LoginFilter
 * </p>
 * <p>
 * Description: 登录权限判定过滤器，判定当前用户是否登录
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
public class LoginFilter implements Filter {

	/**
	 * 过滤器销毁时执行的回调方法
	 * */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * 核心过滤方法，用于判定当前用户是否登录
	 * 
	 * @param arg0
	 *            请求对象
	 * @param arg1
	 *            响应对象
	 * @param arg2
	 *            过滤器链对象
	 * */
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// 获取HttpServletReuqst请求
		HttpServletRequest request = (HttpServletRequest) arg0;
		// 获取HttpServletResponse响应
		HttpServletResponse response = (HttpServletResponse) arg1;
		// 获取HttpSession会话跟踪对象
		HttpSession session = request.getSession();
		
		System.out.println(session.getAttribute("USER_INFO"));
		
		// 判定当前用户是否登录
		if (session.getAttribute("USER_INFO") != null) {
			// 如果已经登录，则允许执行请求
			arg2.doFilter(arg0, arg1);
			// 如果未登录
		} else {
			// 跳转到登录界面
			response.sendRedirect("/OrderSys");
		}
	}

	/**
	 * 过滤器初始化时执行的回调方法
	 * 
	 * @param arg0
	 *            过滤器配置对象，可以读取配置文件中的特殊配置信息
	 * */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
