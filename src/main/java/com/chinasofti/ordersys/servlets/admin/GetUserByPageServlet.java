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

import com.chinasofti.ordersys.service.admin.UserService;
import com.chinasofti.ordersys.vo.UserInfo;

/**
 * <p>
 * Title: GetUserByPageServlet
 * </p>
 * <p>
 * Description: 以分页的方式获取用户信息的Servlet
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
public class GetUserByPageServlet extends HttpServlet {

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
		// 获取希望显示的页码数
		int page = Integer.parseInt(request.getParameter("page"));
		// 创建用户管理服务对象
		UserService service = new UserService();
		// 获取最大页码数
		int maxPage = service.getMaxPage(10);
		// 对当前的页码数进行纠错，如果小于1，则直接显示第一页的内容
		page = page < 1 ? 1 : page;
		// 对当前的页码数进行纠错，如果大于最大页码，则直接显示最后一页的内容
		page = page > maxPage ? maxPage : page;
		// 进行分页数据查询
		ArrayList<UserInfo> list = service.getByPage(page, 10);
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("users");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的用户信息
			for (UserInfo info : list) {
				// 每一个用户创建一个用户标签
				Element user = doc.createElement("user");
				// 创建用户ID标签
				Element userId = doc.createElement("userId");
				// 设置用户ID标签文本内容
				userId.setTextContent(info.getUserId() + "");
				// 将用户ID标签设置为用户标签子标签
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
				// 将角色id标签设置为用户标签子标签
				user.appendChild(roleId);
				// 创建角色名标签
				Element roleName = doc.createElement("roleName");
				// 设置角色名标签文本内容
				roleName.setTextContent(info.getRoleName());
				// 将角色名标签设置为用户标签子标签
				user.appendChild(roleName);
				// 创建角色锁定信息标签
				Element locked = doc.createElement("locked");
				// 设置角色锁定信息标签文本内容
				locked.setTextContent(info.getLocked() + "");
				// 将角色锁定信息标签设置为用户标签子标签
				user.appendChild(locked);
				// 创建角色头像标签
				Element faceimg = doc.createElement("faceimg");
				// 设置角色头像标签文本内容
				faceimg.setTextContent(info.getFaceimg() + "");
				// 设置头像标签为用户标签子标签
				user.appendChild(faceimg);
				// 设置用户标签为根标签子标签
				root.appendChild(user);
			}
			// 创建当前页码数的标签
			Element pageNow = doc.createElement("page");
			// 设置当前页码数标签的文本内容
			pageNow.setTextContent(page + "");
			// 将当前页码数标签设置为根标签的子标签
			root.appendChild(pageNow);
			// 创建最大页码数的标签
			Element maxPageElement = doc.createElement("maxPage");
			// 设置最大页码数标签的文本内容
			maxPageElement.setTextContent(maxPage + "");
			// 将最大页码数标签设置为根标签的子标签
			root.appendChild(maxPageElement);
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
		// for (UserInfo info : list) {
		// System.out.println(info.getUserId() + "\t" + info.getUserAccount());
		// }
	}

}
