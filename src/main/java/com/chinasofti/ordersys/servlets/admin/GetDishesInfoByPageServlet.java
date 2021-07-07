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
 * Title: GetDishesInfoByPageServlet
 * </p>
 * <p>
 * Description: 以分页形式获取菜品信息的Servlet
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
public class GetDishesInfoByPageServlet extends HttpServlet {

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
		// 创建菜品管理服务对象
		DishesService service = new DishesService();
		// 获取最大页码数
		int maxPage = service.getMaxPage(8);
		// 对当前的页码数进行纠错，如果小于1，则直接显示第一页的内容
		page = page < 1 ? 1 : page;
		// 对当前的页码数进行纠错，如果大于最大页码，则直接显示最后一页的内容
		page = page > maxPage ? maxPage : page;
		// 进行分页数据查询
		ArrayList<DishesInfo> list = service.getDishesInfoByPage(page, 8);
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
				// 创建菜品详细文本标签
				Element dishesTxt = doc.createElement("dishesTxt");
				// 获取菜品详细文字描述
				String txt = info.getDishesTxt();
				// 将空格替换为特殊分隔符
				txt = txt.replaceAll(" ", "ordersysspace");
				// 将\r替换为空字符串
				txt = txt.replaceAll("\r", "");
				// 将换行替换为特殊分隔符
				txt = txt.replaceAll("\n", "ordersysbreak");
				// 将双引号替换为转移字符
				txt = txt.replaceAll("\"", "\\\\\"");
				// 将单引号替换为转移字符
				txt = txt.replaceAll("\'", "\\\\\'");
				// 设置菜品详细文本标签的文本内容
				dishesTxt.setTextContent(txt);
				// 将菜品详细文本标签设置为菜品标签的子标签
				dishes.appendChild(dishesTxt);
				// 创建是否推荐子标签
				Element recommend = doc.createElement("recommend");
				// 设置是否推荐菜品标签文本内容
				recommend.setTextContent(info.getRecommend() + "");
				// 将是否推荐菜品标签设置为菜品标签的子标签
				dishes.appendChild(recommend);
				// 创建菜品价格标签
				Element dishesPrice = doc.createElement("dishesPrice");
				// 设置菜品价格标签文本内容
				dishesPrice.setTextContent(info.getDishesPrice() + "");
				// 将菜品价格标签设置为菜品标签子标签
				dishes.appendChild(dishesPrice);
				// 将菜品标签设置为根标签的子标签
				root.appendChild(dishes);

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
