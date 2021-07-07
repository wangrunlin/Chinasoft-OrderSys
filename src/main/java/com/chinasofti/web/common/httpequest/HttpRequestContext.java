/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.web.common.httpequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: HttpRequestContext
 * </p>
 * <p>
 * Description: 请求、响应、ServletContext对象的包装对象
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
public class HttpRequestContext {

	/**
	 * 请求对象
	 * */
	private HttpServletRequest request;
	/**
	 * 响应对象
	 * */
	private HttpServletResponse response;
	/**
	 * ServletContext对象
	 * */
	private ServletContext servletContext;

	/**
	 * 构造器，构建包装对象
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param servletContext
	 *            ServletContext对象
	 * */
	private HttpRequestContext(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext) {
		// 初始化请求对象
		this.request = request;
		// 初始化响应对象
		this.response = response;
		// 初始化ServletContext对象
		this.servletContext = servletContext;
	}

	/**
	 * ThreadLocal对象，用于在单一线程中共享数据
	 * */
	private static ThreadLocal<HttpRequestContext> currentContext = new ThreadLocal<HttpRequestContext>();

	/**
	 * 设置共享数据的方法
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param servletContext
	 *            ServletContext对象
	 * */
	public static void setHttpRequestContext(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext) {
		// 构建包装对象
		HttpRequestContext context = new HttpRequestContext(request, response,
				servletContext);
		// 在ThreadLocal中存放包装对象
		currentContext.set(context);
	}

	/**
	 * 获取请求对象的方法
	 * 
	 * @return 请求对象
	 * */
	public static HttpServletRequest getRequest() {
		// 返回请求对象
		return currentContext.get() == null ? null
				: currentContext.get().request;
	}

	/**
	 * 获取响应对象的方法
	 * 
	 * @return 响应对象
	 * */
	public static HttpServletResponse getResponse() {
		// 返回响应对象
		return currentContext.get() == null ? null
				: currentContext.get().response;
	}

	/**
	 * 获取ServletContext对象的方法
	 * 
	 * @return ServletContext对象
	 * */
	public static ServletContext getServletContext() {
		// 返回servletContext对象
		return currentContext.get() == null ? null
				: currentContext.get().servletContext;
	}

}
