/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.admin;

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
 * Title: GetOrderDetailServlet
 * </p>
 * <p>
 * Description: 获取订单菜品详情的Servlet
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
public class GetOrderDetailServlet extends HttpServlet {

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
		// 获取界面传递的订单ID
		Integer orderId = new Integer(request.getParameter("orderId"));
		// 创建订单管理服务对象
		OrderService service = new OrderService();
		// 查询对应订单菜品详情
		ArrayList<OrderInfo> list = service.getOrderDetailById(orderId);
		// 查询订单基本信息
		OrderInfo info = service.getOrderById(orderId);
		// 尝试创建运行数据结果XML
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("order");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 创建订单Id标签
			Element oidElement = doc.createElement("orderId");
			// 设置订单ID标签文本内容
			oidElement.setTextContent(info.getOrderId() + "");
			// 将订单ID标签设置为根标签子标签
			root.appendChild(oidElement);
			// 创建订单点餐服务员用户名标签
			Element userAccountElement = doc.createElement("userAccount");
			// 设置订单点餐员用户名标签文本内容
			userAccountElement.setTextContent(info.getUserAccount());
			// 将订单点餐员用户名标签设置为根标签子标签
			root.appendChild(userAccountElement);
			// 创建桌号标签
			Element tid = doc.createElement("tableId");
			// 设置桌号标签文本内容
			tid.setTextContent(info.getTableId() + "");
			// 将桌号标签设置为根标签子标签
			root.appendChild(tid);
			// 创建开餐时间标签
			Element orderBeginDateElement = doc.createElement("orderBeginDate");
			// 创建时间日期格式转换工具
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 设置开餐时间标签文本内容
			orderBeginDateElement.setTextContent(sdf.format(info
					.getOrderBeginDate()));
			// 将开餐时间标签设置为根标签子标签
			root.appendChild(orderBeginDateElement);
			// 创建结单时间标签
			Element orderEndDateElement = doc.createElement("orderEndDate");
			// 设置结单时间标签文本内容
			orderEndDateElement.setTextContent(sdf.format(info
					.getOrderEndDate()));
			// 将结单时间标签设置为根标签子标签
			root.appendChild(orderEndDateElement);
			// 获取本订单总价
			double sum = service.getSumPriceByOrderId(orderId);
			// 创建总价标签
			Element sumPrice = doc.createElement("sumPrice");
			// 设置总价标签文本内容
			sumPrice.setTextContent(sum + "");
			// 将总价标签设置为根标签子标签
			root.appendChild(sumPrice);
			// 循环遍历订单详情列表
			for (OrderInfo oi : list) {
				// 每一个订单详情创建一个订单单元标签
				Element unit = doc.createElement("unit");
				// 将订单单元标签设置为根标签子标签
				root.appendChild(unit);
				// 创建菜品名标签
				Element dishesName = doc.createElement("dishesName");
				// 设置菜品名标签文本内容
				dishesName.setTextContent(oi.getDishesName());
				// 将菜品名标签设置为单元标签子标签
				unit.appendChild(dishesName);
				// 创建菜品价格标签
				Element dishesPrice = doc.createElement("dishesPrice");
				// 设置菜品价格标签文本内容
				dishesPrice.setTextContent(oi.getDishesPrice() + "");
				// 将菜品价格标签设置为单元标签子标签
				unit.appendChild(dishesPrice);
				// 创建菜品数量标签
				Element num = doc.createElement("num");
				// 设置菜品数量标签文本内容
				num.setTextContent(oi.getNum() + "");
				// 将菜品数量标签设置为单元标签子标签
				unit.appendChild(num);
			}
			// 将完整的DOM树转换为XML文档结构字符串输出到客户端
			TransformerFactory
					.newInstance()
					.newTransformer()
					.transform(new DOMSource(doc),
							new StreamResult(response.getOutputStream()));

			// response.getWriter().write(msg);
			// 捕获查询、转换过程中的异常信息
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
		}

	}

}
