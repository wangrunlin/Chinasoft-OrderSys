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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.chinasofti.ordersys.listeners.OrderSysListener;
import com.chinasofti.ordersys.vo.UserInfo;

/**
 * <p>
 * Title: GetOnlineWaitersServlet
 * </p>
 * <p>
 * Description: 获取当前在线餐厅服务员列表的Servlet
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
public class GetOnlineWaitersServlet extends HttpServlet {

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
	 * 当以Post方式请求Servlet时由service方法回调,为了考虑以后的平台兼容性， 本响应返回xml结果
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 设置返回的MIME类型为xml
		response.setContentType("text/xml");
		// 从监听器中获取在线的服务员列表
		ArrayList<UserInfo> waiters = OrderSysListener.getOnlineWaiters();
		// 获取所有的会话数目
		int sessions = OrderSysListener.onlineSessions;
		// 尝试将在线服务员列表结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("users");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的管理员信息
			for (UserInfo info : waiters) {
				// 每一个员工构建一个用户标签节点
				Element user = doc.createElement("user");
				// 创建用户id节点标签
				Element userId = doc.createElement("userId");
				// 设置用户id标签文本内容
				userId.setTextContent(info.getUserId() + "");
				// 将用户id标签设置为用户标签子标签
				user.appendChild(userId);
				// 创建用户名标签
				Element userAccount = doc.createElement("userAccount");
				// 设置用户名标签文本内容
				userAccount.setTextContent(info.getUserAccount());
				// 将用户名标签设置为用户标签子标签
				user.appendChild(userAccount);
				// 创建角色id标签
				Element roleId = doc.createElement("roleId");
				// 设置角色id标签文本内容
				roleId.setTextContent(info.getRoleId() + "");
				// 将角色id标签设置为用户标签的子标签
				user.appendChild(roleId);
				// 创建角色名标签
				Element roleName = doc.createElement("roleName");
				// 设置角色名标签文本内容
				roleName.setTextContent(info.getRoleName());
				// 将角色名标签设置为用户标签的子标签
				user.appendChild(roleName);
				// 创建用户锁定状态标签
				Element locked = doc.createElement("locked");
				// 设置用户锁定状态标签文本内容
				locked.setTextContent(info.getLocked() + "");
				// 将用户锁定状态标签设置为用户标签子标签
				user.appendChild(locked);
				// 创建角色头像标签
				Element faceimg = doc.createElement("faceimg");
				// 设置角色头像标签文本内容
				faceimg.setTextContent(info.getFaceimg() + "");
				// 将角色头像标签设置为用户标签子标签
				user.appendChild(faceimg);
				// 将角色标签设置为根标签子节点
				root.appendChild(user);

			}
			// 创建会话数标签
			Element sessionNum = doc.createElement("sessionNum");
			// 设置会话数标签文本内容
			sessionNum.setTextContent(sessions + "");
			// 将会话数标签设置为根标签的子标签
			root.appendChild(sessionNum);
			// 创建服务员数标签
			Element waitersNum = doc.createElement("waitersNum");
			// 设置服务员数标签文本内容
			waitersNum.setTextContent(waiters.size() + "");
			// 将服务员数标签设置为根标签的子标签
			root.appendChild(waitersNum);
			// 将完整的DOM树转换为XML文档结构字符串输出到客户端
			TransformerFactory
					.newInstance()
					.newTransformer()
					.transform(new DOMSource(doc),
							new StreamResult(response.getOutputStream()));
			// 捕获查询、转换过程中的异常信息
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
		}
	}

}
