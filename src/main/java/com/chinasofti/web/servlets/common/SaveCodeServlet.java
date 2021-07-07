/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.web.servlets.common;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chinasofti.web.common.service.SaveCodeService;

/**
 * <p>
 * Title: SaveCodeServlet
 * </p>
 * <p>
 * Description: 输出验证码的Servlet
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
public class SaveCodeServlet extends HttpServlet {

	/**
	 * 验证码字符串在会话中的属性名
	 * */
	public static final String CODE_SESSION_ATTR_NAME = "web_app_savecode_value";

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

		// 图片不需要缓存的响应头
		response.setHeader("Pragma", "No-cache");
		// 图片不需要缓存的响应头
		response.setHeader("Cache-Control", "no-cache");
		// 图片不需要缓存的响应头
		response.setDateHeader("Expires", 0);
		// 设置响应MIME类型为JPEG图片
		response.setContentType("image/jpeg");
		// 创建验证码服务对象
		SaveCodeService codeService = new SaveCodeService(
				"abcdefghijklmnopqrstuvwxyz123456789".toUpperCase()
						.toCharArray(), 100, 25, 6);
		// 创建验证码图片
		codeService.createSaveCodeImage();
		// 获取验证码图片
		BufferedImage img = codeService.getImage();
		// 获取验证码字符串
		String codeString = codeService.getCodeString();
		// 获取会话对象
		HttpSession session = request.getSession();
		// 将验证码字符串存入会话
		session.setAttribute(CODE_SESSION_ATTR_NAME, codeString);
		try {
			// 将缓存图片编码为物理图片数据并从响应输出流中输出到客户端
			ImageIO.write(img, "JPEG", response.getOutputStream());
			// 捕获异常
		} catch (Exception e) {

			// TODO: handle exception
		}
	}

}
