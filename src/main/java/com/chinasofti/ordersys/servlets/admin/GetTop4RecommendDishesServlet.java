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

import com.chinasofti.ordersys.service.admin.DishesService;
import com.chinasofti.ordersys.vo.DishesInfo;

/**
 * <p>
 * Title: GetTop4RecommendDishesServlet
 * </p>
 * <p>
 * Description: 获取管理员主界面中需要的头4个推荐菜品信息的Servlet
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
public class GetTop4RecommendDishesServlet extends HttpServlet {

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
		// 创建菜品管理服务对象
		DishesService service = new DishesService();
		// 获取头4条推荐菜品信息列表
		ArrayList<DishesInfo> list = service.getTop4RecommendDishes();
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("disheses");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的菜品信息
			for (DishesInfo info : list) {
				// 每一个菜品构建一个dishes标签节点
				Element dishes = doc.createElement("dishes");
				// 创建菜品ID标签
				Element dishesId = doc.createElement("dishesId");
				// 设置菜品ID标签的文本内容
				dishesId.setTextContent(info.getDishesId() + "");
				// 将菜品ID标签设置为菜品标签子标签
				dishes.appendChild(dishesId);
				// 创建菜品名标签
				Element dishesName = doc.createElement("dishesName");
				// 设置菜品名标签的文本内容
				dishesName.setTextContent(info.getDishesName());
				// 将菜品名标签设置为菜品标签的子标签
				dishes.appendChild(dishesName);
				// 创建菜品描述标签
				Element dishesDiscript = doc.createElement("dishesDiscript");
				// 设置菜品描述标签文本内容
				dishesDiscript.setTextContent(info.getDishesDiscript());
				// 将菜品描述标签设置为菜品标签子标签
				dishes.appendChild(dishesDiscript);
				// 创建菜品图片标签
				Element dishesImg = doc.createElement("dishesImg");
				// 设置菜品图片标签的文本内容
				dishesImg.setTextContent(info.getDishesImg());
				// 将菜品图片标签设置为菜品标签的子标签
				dishes.appendChild(dishesImg);
				// 将菜品标签设置为根标签的子标签
				root.appendChild(dishes);

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

}
