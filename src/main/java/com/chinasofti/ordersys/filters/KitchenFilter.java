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

import com.chinasofti.ordersys.vo.UserInfo;

/**
 * <p>
 * Title: KitchenFilter
 * </p>
 * <p>
 * Description: 后厨权限判定过滤器，判定当前是否以后厨人员登录
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
public class KitchenFilter implements Filter {
	/**
	 * 过滤器销毁时执行的回调方法
	 * */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * 核心过滤方法，用于判定当前是否以后厨人员身份登录
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
		// 判定当前用户是否登录且用户身份是否为后厨人员
		if (session.getAttribute("USER_INFO") != null
				&& ((UserInfo) session.getAttribute("USER_INFO")).getRoleId() == 2) {
			// 如果登录身份符合要求，则允许执行请求
			arg2.doFilter(arg0, arg1);
			// 如果未登录或登录身份不符合要求
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
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
