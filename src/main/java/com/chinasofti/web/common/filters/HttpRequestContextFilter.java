/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.web.common.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinasofti.web.common.httpequest.HttpRequestContext;

/**
 * <p>
 * Title: HttpRequestContextFilter
 * </p>
 * <p>
 * Description: 在处理每次请求前捕获请求、响应对象的过滤器
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
public class HttpRequestContextFilter implements Filter {
	/**
	 * ServletContext对象
	 * */
	private ServletContext context;

	/**
	 * 过滤器销毁时执行的回调方法
	 * */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * 核心过滤方法，用于判定当前是否以餐厅管理员身份登录
	 * 
	 * @param arg0
	 *            请求对象
	 * @param arg1
	 *            响应对象
	 * @param arg2
	 *            过滤器链对象
	 * */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// 在ThreadLocal中共享本次请求、响应对象
		HttpRequestContext.setHttpRequestContext((HttpServletRequest) request,
				(HttpServletResponse) response, context);
		// 继续执行请求
		chain.doFilter(request, response);

	}

	/**
	 * 过滤器初始化时执行的回调方法
	 * 
	 * @param arg0
	 *            过滤器配置对象，可以读取配置文件中的特殊配置信息
	 * */
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		// 获取ServletContext对象
		context = config.getServletContext();

	}

}
