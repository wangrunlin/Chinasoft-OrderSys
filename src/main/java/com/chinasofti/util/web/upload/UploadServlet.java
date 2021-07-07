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
 * Title: UploadServlet
 * </p>
 * <p>
 * Description: 测试文件上传的服务器端servlet
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
public class UploadServlet extends HttpServlet {

	/**
	 * 处理Get请求的方法，本Servlet要求不同的请求返回相同的结果，因此调用doPost方法统一处理
	 * 
	 * @param request
	 *            http请求对象
	 * @param response
	 *            http响应对象
	 * @throws ServletException
	 *             如果有servlet访问方面的异常，则抛出异常信息
	 * @throws IOException
	 *             如果网络访问出错则抛出异常信息
	 * @see #doPost(HttpServletRequest, HttpServletResponse)
	 * */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 调用doPost处理请求
		doPost(request, response);
	}

	/**
	 * 处理Post请求的方法，正常情况下，如果有文件上传，则应该发起Post请求
	 * 
	 * @param request
	 *            http请求对象
	 * @param response
	 *            http响应对象
	 * @throws ServletException
	 *             如果有servlet访问方面的异常，则抛出异常信息
	 * @throws IOException
	 *             如果网络访问出错则抛出异常信息
	 * */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 创建Http请求解析工具
		MultipartRequestParser parser = new MultipartRequestParser();
		// 进行Http请求解析，将参数解析的结果存入com.chinasofti.util.web.upload.SubmitInformation类的对象中
		SubmitInformation info = (SubmitInformation) parser.parse(request,
				"com.chinasofti.util.web.upload.SubmitInformation");
		// 输出info对象耳朵ownerName属性值，以验证请求解析工具功能
		System.out.println(info.getOwnerName());
		// 输出info对象耳朵ownerAge属性值，以验证请求解析工具功能
		System.out.println(info.getOwnerAge());
		// 将info中的uploadFile代表的上传文件实际保存到目标路径中
		info.getUploadFile().saveToFileSystem(
				request,
				getServletContext().getRealPath("/upload") + "/"
						+ info.getUploadFile().getFileName());

	}

}
