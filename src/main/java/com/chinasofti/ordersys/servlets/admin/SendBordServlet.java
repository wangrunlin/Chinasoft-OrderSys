/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.admin;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinasofti.util.web.serverpush.MessageProducer;

/**
 * <p>
 * Title: SendBordServlet
 * </p>
 * <p>
 * Description: 发送实时公告的Servlet
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
public class SendBordServlet extends HttpServlet {

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
		// 设置响应字符集
		response.setCharacterEncoding("utf-8");
		// 获取公告信息
		String bord = request.getParameter("bord");
		// 创建实施消息生产者
		MessageProducer producer = new MessageProducer();
		// 获取实时公告信息等待列表
		ArrayList<String> list = GetRTBordServlet.bords;
		// 遍历等待列表
		for (int i = list.size() - 1; i >= 0; i--) {
			// 获取等待信息的用户sessionID
			String id = list.get(i);
			// 针对该sessionID和消息标题、内容生产消息
			producer.sendMessage(id, "rtbord", bord);
			// 将该sessionID从等待列表中删除
			list.remove(id);
		}
	}



}
