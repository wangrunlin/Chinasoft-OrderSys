/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinasofti.ordersys.service.login.CheckUserPassService;
import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.web.upload.MultipartRequestParser;

/**
 * <p>
 * Title:CheckUserPassServlet
 * </p>
 * <p>
 * Description: 利用Ajax检查用户密码是否正确的Servlet
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
public class CheckUserPassServlet extends HttpServlet {

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
		// 创建表单请求数据解析工具对象
		MultipartRequestParser parser = new MultipartRequestParser();
		// 解析并获取UserInfo对象
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);
		// 创建判定用户密码是否正确的服务对象
		CheckUserPassService service = new CheckUserPassService();
		// 获取针对客户端的文本输出流
		PrintWriter pw = response.getWriter();
		// 如果密码正确
		if (service.checkPass(info)) {
			// 输出密码正确的标识
			pw.print("OK");
			// 密码错误
		} else {
			// 输出密码错误的标识
			pw.print("FAIL");
		}

	}

}
