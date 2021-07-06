/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Title: ExchangeRatePublishServlet
 * </p>
 * <p>
 * Description:
 * 功能组件中用于接收消息的核心Servlet，当用户需要获取推送消息时，直接请求该Servlet,并给Servlet传递表示需要接收消息标题的参数
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
public class GetExchangeRateServlet extends HttpServlet {
	/**
	 * 用于存放当前系统中的所有客户端，向量中的每一个元素都是单一客户端的sessionid
	 * */
	static Vector clients = new Vector();
	/**
	 * 希望获取推送消息时，提供给Servlet的消息名称的参数名
	 * */
	String messageTitleParameterName = "messageTitle";

	/**
	 * 处理获取实时汇率信息的Get HTTP请求方法，，本Servlet要求不同的请求返回相同的结果，因此调用doPost方法统一处理
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
		// 设置正确的请求字符集以防止出现乱码
		request.setCharacterEncoding("utf-8");
		// 设置到客户端输出流中输出数据的字符集
		response.setCharacterEncoding("utf-8");
		// 获取用户的session会话对象
		HttpSession session = request.getSession(true);
		// 判断当前会话的id值是否位于用户集合中
		if (!clients.contains(session.getId())) {
			// 如果当前回话的id值没有在用户集合中，说明出现了一个新的用户，则将新用户的sessionid值加入用户集合
			clients.add(session.getId());
		}

		// 获取用户希望抓取数据的名称
		String messageTitle = request.getParameter(messageTitleParameterName);
		// 创建服务器数据推送的消息消费者
		MessageConsumer mconsumer = new MessageConsumer();

		// 由于需要在匿名内部类对象中使用响应对象，因此定义一个final版本
		final HttpServletResponse rsp = response;
		// 构建消息的处理对象
		MessageHandler handler = new MessageHandler() {
			// 实现当获取消息后对消息进行实际处理的回调方法、
			// 回调方法的messageQueue参数描述了当前系统使用的消息等待序列
			// 回调方法的key参数保存了当前获取到的消息发送的目标sessionid和消息名称
			// 回调方法的msg参数保存了实际的消息数据
			@Override
			public void handle(Hashtable<ServerPushKey, Message> messageQueue,
					ServerPushKey key, Message msg) {

				try {
					// 获取针对客户端的字符输出流
					PrintWriter pw = rsp.getWriter();
					// 将消息字符串直接发送给客户端浏览器
					pw.print(msg.getMsg());
				} catch (Exception ex) {
					// 如果遇到异常则输出异常信息
					ex.printStackTrace();
				}
			}
		};
		// 利用消息消费者尝试获取消息数据
		mconsumer.searchMessage(session.getId(), messageTitle, handler);
	}

	/**
	 * 初始化Servlet的方法，主要作用是获取自定义的消息名称参数名
	 */
	public void init() throws ServletException {
		// 获取ServletConfig对象用于读取web.xml中针对本Servlet设置的初始化参数信息
		ServletConfig config = getServletConfig();
		// 如果存在名字叫做MessageTitleParameterName的初始化参数，则将该参数对应的参数值作为自定义的消息名称参数名
		if (config.getInitParameter("MessageTitleParameterName") != null) {
			messageTitleParameterName = config
					.getInitParameter("MessageTitleParameterName");
		}
	}
}
