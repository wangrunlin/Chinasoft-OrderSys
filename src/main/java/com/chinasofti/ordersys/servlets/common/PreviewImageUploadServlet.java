/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.servlets.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chinasofti.util.web.serverpush.MessageProducer;
import com.chinasofti.util.web.upload.FormFile;
import com.chinasofti.util.web.upload.MultipartRequestParser;

/**
 * <p>
 * Title:PreviewImageUploadServlet
 * </p>
 * <p>
 * Description: 上传图片文件并进行成功后实时预览的Servlet
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
public class PreviewImageUploadServlet extends HttpServlet {

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
	 * 当以Post方式请求Servlet时由service方法回调
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 定义默认的图片存放路径
		String savepath = "/img/faces";
		// 判定请求参数中是否存在自定义存放路径
		if (request.getParameter("path") != null) {
			// 设置自定义图片存放路径
			savepath = request.getParameter("path");
		}
		// 获取表单请求解析器
		MultipartRequestParser parser = new MultipartRequestParser();
		// 解析数据并获取对应vo
		PreviewImageInfo info = (PreviewImageInfo) parser.parse(request,
				"com.chinasofti.ordersys.servlets.common.PreviewImageInfo");
		// 获取上传文件对象
		FormFile img = info.getUploadFile();
		// 获取存放图像的物理路径
		String path = getServletContext().getRealPath(savepath);
		// 将上传的图片存放到物理路径中
		img.saveToFileSystem(request, path + "/" + img.getFileName());

		// 请求中数据的字符集编码是"utf-8",正确设置后防止获取到的数据变成乱码
		request.setCharacterEncoding("utf-8");

		// 创建服务器推送数据的消息生产者
		MessageProducer producer = new MessageProducer();

		// 向本会话生产一个文件上传成功的实时推送消息
		producer.sendMessage(request.getSession().getId().toString(),
				"upstate", img.getFileName());

	}

}
