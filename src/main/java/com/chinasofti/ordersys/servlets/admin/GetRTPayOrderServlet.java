/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.admin;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chinasofti.util.web.serverpush.BaseGetPushMsgServlet;
import com.chinasofti.util.web.serverpush.Message;
import com.chinasofti.util.web.serverpush.MessageHandler;
import com.chinasofti.util.web.serverpush.ServerPushKey;

/**
 * <p>
 * Title: GetRTPayOrderServlet
 * </p>
 * <p>
 * Description: 获取实时结账订单信息的Servlet
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
public class GetRTPayOrderServlet extends BaseGetPushMsgServlet {

	/**
	 * 获取实时结账订单信息的等待列表
	 * */
	public static ArrayList<String> pays = new ArrayList<String>();

	/**
	 * 获取实时消息处理器的回调
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @return 本Servlet使用的实时消息处理器
	 * */
	@Override
	public MessageHandler getHandler(HttpServletRequest request,
			HttpServletResponse response) {
		// 设置请求字符集
		response.setCharacterEncoding("utf-8");
		// TODO Auto-generated method stub
		// 尝试处理实时消息
		try {
			// 获取针对客户端的文本输出流
			final PrintWriter out = response.getWriter();
			// 创建消息处理器
			MessageHandler handler = new MessageHandler() {

				@Override
				public void handle(
						Hashtable<ServerPushKey, Message> messageQueue,
						ServerPushKey key, Message msg) {
					// 将消息的文本内容直接发送给客户端
					out.print(msg.getMsg());
					// TODO Auto-generated method stub

				}
			};
			// 返回创建好的消息处理器
			return handler;
			// 捕获创建消息处理器时产生的异常
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
			// 返回空的消息处理器
			return null;
		}
	}

	/**
	 * 初始化实时消息获取服务的方法
	 * 
	 * @param request
	 *            请求信息
	 * @param response
	 *            响应对象
	 * @param session
	 *            会话跟踪对象
	 * */
	@Override
	public void initService(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		// TODO Auto-generated method stub
		// 将当前会话加入到实时消息等待列表
		pays.add(session.getId());
	}

}
