/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: ExchangeRatePublishServlet
 * </p>
 * <p>
 * Description: 发布实时汇率信息的服务器端Servlet
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */
public class ExchangeRatePublishServlet extends HttpServlet {

	/**
	 * 处理发布实时汇率信息的Get HTTP请求方法，，本Servlet要求不同的请求返回相同的结果，因此调用doPost方法统一处理
	 * 
	 * @param request
	 *            Http请求对象
	 * @param response
	 *            Http响应对象
	 * @throws ServletException
	 *             如果有servlet访问方面的异常，则抛出异常信息
	 * @throws IOException
	 *             如果网络访问出错则抛出异常信息
	 * */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 调用doPost处理请求
		doPost(request, response);
	}

	/**
	 * 处理Post请求的方法
	 * 
	 * @param request
	 *            http请求对象
	 * @param response
	 *            http响应对象
	 * @throws ServletException
	 *             如果有servlet访问方面的异常，则抛出异常信息
	 * @throws IOException
	 *             如果网络访问出错则抛出异常信息
	 * */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 请求中数据的字符集编码是"utf-8",正确设置后防止获取到的数据变成乱码
		request.setCharacterEncoding("utf-8");
		// 获取客户端发送过来的汇率数据
		String message = request.getParameter("inputRate");
		// 创建服务器推送数据的消息生产者
		MessageProducer producer = new MessageProducer();
		// 遍历所有的客户端
		for (int i = 0; i < GetExchangeRateServlet.clients.size(); i++) {
			//向每一个客户端生产一个汇率消息
			producer.sendMessage(GetExchangeRateServlet.clients.get(i)
					.toString(), "rtrate", "1$ = " + message + "RMB");
		}		
	}
}
