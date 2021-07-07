/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.waiters;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.chinasofti.ordersys.servlets.admin.GetRTPayOrderServlet;
import com.chinasofti.ordersys.vo.OrderInfo;
import com.chinasofti.util.web.serverpush.MessageProducer;

/**
 * <p>
 * Title: RequestToPayServlet
 * </p>
 * <p>
 * Description: 向餐厅管理元推送买单信息的Servlet
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
public class RequestToPayServlet extends HttpServlet {

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
	 * 当以Post方式请求Servlet时由service方法回调，为了考虑以后的平台兼容性， 本Servlet推送xml结果
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取需要买单的订单ID
		Integer orderId = new Integer(request.getParameter("orderId"));
		// 创建订单管理服务对象
		OrderService service = new OrderService();
		// 修改数据库中的订单状态信息
		service.requestPay(orderId);
		// 获取订单详情
		OrderInfo info = service.getOrderById(orderId);
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("order");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 创建订单ID节点
			Element oid = doc.createElement("orderId");
			// 设置订单ID节点文本内容
			oid.setTextContent(info.getOrderId() + "");
			// 将订单ID节点设置为根节点子节点
			root.appendChild(oid);
			// 创建点餐员用户名标签
			Element userAccount = doc.createElement("userAccount");
			// 设置点餐员用户名标签文本内容
			userAccount.setTextContent(info.getUserAccount());
			// 将点餐员用户名标签设置为根节点子节点
			root.appendChild(userAccount);
			// 创建桌号标签
			Element tid = doc.createElement("tableId");
			// 设置桌号标签文本内容
			tid.setTextContent(info.getTableId() + "");
			// 将桌号标签设置为根标签子标签
			root.appendChild(tid);
			// 创建订单开始时间标签
			Element orderBeginDate = doc.createElement("orderBeginDate");
			// 创建日期时间格式化工具
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 设置订单开始时间标签文本内容
			orderBeginDate.setTextContent(sdf.format(info.getOrderBeginDate()));
			// 将订单开始时间标签设置为根标签子标签
			root.appendChild(orderBeginDate);
			// 创建结单时间标签
			Element orderEndDate = doc.createElement("orderEndDate");
			// 设置结单时间标签文本内容
			orderEndDate.setTextContent(sdf.format(info.getOrderEndDate()));
			// 将节点时间标签设置为根标签子标签
			root.appendChild(orderEndDate);
			// 获取订单总金额
			double sum = service.getSumPriceByOrderId(orderId);
			// 创建总金额标签
			Element sumPrice = doc.createElement("sumPrice");
			// 设置总金额标签文本内容
			sumPrice.setTextContent(sum + "");
			// 将总金额标签设置为根标签子标签
			root.appendChild(sumPrice);
			// 创建字符串输出流
			StringWriter writer = new StringWriter();
			// 创建格式化输出流
			PrintWriter pwriter = new PrintWriter(writer);
			// 将完整的DOM树转换为XML文档结构字符串输出到字符串
			TransformerFactory.newInstance().newTransformer()
					.transform(new DOMSource(doc), new StreamResult(pwriter));
			// 获取XML字符串
			String msg = writer.toString();
			// 格式化输出流关闭
			pwriter.close();
			// 字符串输出流关闭
			writer.close();
			// 获取餐厅管理员等待列表
			ArrayList<String> list = GetRTPayOrderServlet.pays;
			// 创建消息生产者
			MessageProducer producer = new MessageProducer();
			// 遍历所有的等待管理员
			for (int i = list.size() - 1; i >= 0; i--) {
				// 获取管理员sessionID
				String id = list.get(i);
				// 为管理员推送订单买单信息
				producer.sendMessage(id, "rtpay", msg);
				// 将本管理员从等待用户列表中删除
				list.remove(id);
			}

			// response.getWriter().write(msg);
			// 捕获异常信息
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
		}

	}

}
