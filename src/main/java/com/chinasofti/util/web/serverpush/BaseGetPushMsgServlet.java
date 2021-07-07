/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Title: BaseGetPushMsgServlet
 * </p>
 * <p>
 * Description: 实现获取推送信息的基础Servlet
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
public abstract class BaseGetPushMsgServlet extends HttpServlet {

	/**
	 * 希望获取推送消息时，提供给Servlet的消息名称的参数名
	 * */
	String messageTitleParameterName = "messageTitle";

	/**
	 * Constructor of the object.
	 */
	public BaseGetPushMsgServlet() {
		super();
	}

	/**
	 * 用于设置消息处理对象的抽象回调，子类利用该回调确定获取推送消息后的处理方式
	 * 
	 * @param request
	 *            Servlet获取到的Http请求对象
	 * @param response
	 *            Servlet获取到的Http相应对象
	 * @return 创建好的消息处理对象，供Servlet使用
	 * */
	public abstract MessageHandler getHandler(final HttpServletRequest request,
			final HttpServletResponse response);

	public void initService(final HttpServletRequest request,
			final HttpServletResponse response, final HttpSession session) {

	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 设置正确的请求字符集以防止出现乱码
		request.setCharacterEncoding("utf-8");
		// 设置到客户端输出流中输出数据的字符集
		response.setCharacterEncoding("utf-8");
		// 获取用户的session会话对象

		HttpSession session = request.getSession(true);
		initService(request, response, session);

		// 获取用户希望抓取数据的名称
		String messageTitle = request.getParameter(messageTitleParameterName);
		// 创建服务器数据推送的消息消费者
		MessageConsumer mconsumer = new MessageConsumer();

		// 由于需要在匿名内部类对象中使用响应对象，因此定义一个final版本
		final HttpServletResponse rsp = response;

		// 调用子类中实现的setHandler方法构建消息的处理对象
		MessageHandler handler = getHandler(request, response);

		// 利用消息消费者尝试获取消息数据
		mconsumer.searchMessage(session.getId(), messageTitle, handler);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
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
