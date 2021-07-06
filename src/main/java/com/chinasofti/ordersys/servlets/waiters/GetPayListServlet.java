/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.waiters;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

import com.chinasofti.ordersys.service.waiters.OrderService;
import com.chinasofti.ordersys.vo.OrderInfo;

/**
 * <p>
 * Title: GetPayListServlet
 * </p>
 * <p>
 * Description: 获取需要买单订单列表的Servlet
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
public class GetPayListServlet extends HttpServlet {

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
		// 创建订单管理服务对象
		OrderService service = new OrderService();
		// 定义查询数据页码变量
		int page = 1;
		// 如果请求中包含页码信息
		if (request.getParameter("page") != null) {
			// 获取请求中的页面信息
			page = Integer.parseInt(request.getParameter("page"));
		}
		// 获取最大页码数
		int maxPage = service.getMaxPage(10, 0);
		// 对当前的页码数进行纠错，如果小于1，则直接显示第一页的内容
		page = page < 1 ? 1 : page;
		// 对当前的页码数进行纠错，如果大于最大页码，则直接显示最后一页的内容
		page = page > maxPage ? maxPage : page;
		// 根据页码信息查询需要买单的订单信息
		ArrayList<OrderInfo> list = service.getNeedPayOrdersByPage(page, 10, 0);
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("orderes");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历每一个订单信息
			for (OrderInfo info : list) {
				// 每一个订单创建一个订单标签
				Element order = doc.createElement("order");
				// 创建订单Id标签
				Element orderId = doc.createElement("orderId");
				// 设置订单ID标签的文本内容
				orderId.setTextContent(info.getOrderId() + "");
				// 将订单ID标签设置为订单标签子标签
				order.appendChild(orderId);
				// 创建桌号标签
				Element tableId = doc.createElement("tableId");
				// 设置桌号标签文本内容
				tableId.setTextContent(info.getTableId() + "");
				// 将桌号标签设置为订单标签子标签
				order.appendChild(tableId);
				// 创建点餐员帐号标签
				Element userAccount = doc.createElement("userAccount");
				// 设置点餐员帐号标签文本内容
				userAccount.setTextContent(info.getUserAccount());
				// 将点餐员帐号标签设置为订单标签子标签
				order.appendChild(userAccount);
				// 创建订单开始时间标签
				Element orderBeginDate = doc.createElement("orderBeginDate");
				// 创建日期时间格式化工具
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				// 设置订单开始时间标签文本内容
				orderBeginDate.setTextContent(sdf.format(info
						.getOrderBeginDate()));
				// 将订单开始时间标签设置为订单标签子标签
				order.appendChild(orderBeginDate);
				// 将订单标签设置为根标签子标签
				root.appendChild(order);

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

	}

}
