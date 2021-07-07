/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.jdbc.template.JDBCTemplateWithDS;

/**
 * <p>
 * Title:CheckAddUserServlet
 * </p>
 * <p>
 * Description: 利用Ajax检查用户是否已经存在的Servlet
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
public class CheckAddUserServlet extends HttpServlet {

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

		// 获取请求参数中的用户名信息
		String userAccount = request.getParameter("name");
		// 由于是ajax请求，因此需要转码
		userAccount = new String(userAccount.getBytes("iso8859-1"), "utf-8");
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 查询对应用户名的用户信息
		ArrayList<UserInfo> list = helper.preparedQueryForList(
				"select userAccount from userinfo where userAccount=?",
				new Object[] { userAccount }, UserInfo.class);
		// 获取针对客户端的文字输出流
		PrintWriter pw = response.getWriter();
		// 如果数据库中无数据
		if (list.size() == 0) {
			// 输出可以添加标识
			pw.print("OK");
			// 如果数据库中有数据
		} else {
			// 输出不能添加标识
			pw.print("FAIL");
		}
	}



}
