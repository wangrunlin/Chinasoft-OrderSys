/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.upload;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Title: GetProgress
 * </p>
 * <p>
 * Description: 利用Ajax获取文件上传进度信息的服务器端Servlet
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
public class GetProgress extends HttpServlet {

	/**
	 * 处理Ajax进度信息Get HTTP请求的方法
	 * 
	 * @param request
	 *            Http请求对象
	 * @param response
	 *            Http响应对象
	 * @throws ServletException
	 *             如果有servlet访问方面的异常，则抛出异常信息
	 * @throws IOException
	 *             如果网络访问出错则抛出异常信息
	 * */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 设置本次请求返回响应的MIME类型
		response.setContentType("text/html");
		// 获取针对客户端的字符输出流
		PrintWriter out = response.getWriter();
		// 获取当前回话里的进度信息后利用输出流输出给客户端
		out.print(MultipartRequestParser.getUploadProgress(request));

	}

}
