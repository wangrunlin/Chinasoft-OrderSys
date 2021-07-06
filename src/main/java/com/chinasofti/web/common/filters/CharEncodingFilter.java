/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.web.common.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Title: CharEncodingFilter
 * </p>
 * <p>
 * Description: 设置项目中请求字符集的过滤器
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
public class CharEncodingFilter implements Filter {
	/**
	 * 字符集名称变量
	 * */
	String encoding = "utf-8";

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
		// 设置请求字符集
		((HttpServletRequest) request).setCharacterEncoding(encoding);
		// 继续执行请求
		chain.doFilter(request, response);

	}

	/**
	 * 过滤器初始化时执行的回调方法
	 * 
	 * @param arg0
	 *            过滤器配置对象，可以读取配置文件中的特殊配置信息
	 * */
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		// 尝试获取自定义字符集编码名称
		try {
			// 从配置文件读取字符集编码名称
			encoding = filterConfig.getInitParameter("encoding") != null ? filterConfig
					.getInitParameter("encoding") : "utf-8";
			// 捕获异常
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();

		}
	}
}
