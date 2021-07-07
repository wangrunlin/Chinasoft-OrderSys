/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.admin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
 * Title: GetOperateDateServlet
 * </p>
 * <p>
 * Description: 获取运营数据的Servlet
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
public class GetOperateDateServlet extends HttpServlet {

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
		// 尝试创建运行数据结果XML
		try {
			// 创建订单管理服务对象
			OrderService service = new OrderService();
			// 创建日期格式化工具
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			// 获取开始时间
			Date begin = sdf.parse(request.getParameter("bt"));
			// 获取结束时间
			Date end = sdf.parse(request.getParameter("et"));
			// 查询结单时间在开始时间与结束时间之间的所有订单信息
			ArrayList<OrderInfo> list = service.getOrderInfoBetweenDate(begin,
					end);
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("orders");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的订单信息
			for (OrderInfo info : list) {
				// 获取每个订单的总价
				float sumPrice = service.getSumPriceByOrderId(new Integer(info
						.getOrderId()));
				// // 每一个订单构建一个订单标签节点
				Element order = doc.createElement("order");
				// 创建订单id标签
				Element orderId = doc.createElement("orderId");
				// 设置订单id标签文本内容
				orderId.setTextContent(info.getOrderId() + "");
				// 将订单id标签设置为订单标签的子标签
				order.appendChild(orderId);
				// 创建桌号标签
				Element tableId = doc.createElement("tableId");
				// 设置桌号标签文本内容
				tableId.setTextContent(info.getTableId() + "");
				// 将桌号标签设置为订单标签子标签
				order.appendChild(tableId);
				// 创建总价标签
				Element sumPriceElement = doc.createElement("sumPrice");
				// 设置总价标签文本内容
				sumPriceElement.setTextContent(sumPrice + "");
				// 将总价标签设置为订单标签子标签
				order.appendChild(sumPriceElement);
				// 创建点餐服务员用户名标签
				Element userAccount = doc.createElement("userAccount");
				// 设置点餐服务员用户名标签文本内容
				userAccount.setTextContent(info.getUserAccount());
				// 将点餐服务员用户名标签设置为订单标签子标签
				order.appendChild(userAccount);
				// 创建订单结单时间标签
				Element orderEndDate = doc.createElement("orderEndDate");
				// 创建时间、日期格式化工具
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 设置结单时间标签内容为格式化后的时间字符串
				orderEndDate.setTextContent(sdf.format(info.getOrderEndDate()));
				// 将结单时间标签设置为订单标签子标签
				order.appendChild(orderEndDate);
				// 将订单标签设置为根标签子标签
				root.appendChild(order);

			}
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

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
