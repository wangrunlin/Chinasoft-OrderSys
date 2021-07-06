/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.web.common.taglib;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.chinasofti.web.common.httpequest.HttpRequestContext;

/**
 * <p>
 * Title: TokenTag
 * </p>
 * <p>
 * Description: 防止表单重复提交的标签
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
public class TokenTag extends TagSupport {

	/**
	 * 令牌属性在SESSION中的属性名
	 * */
	public static final String TOKEN_SESSION_ATTR_NAME = "SUBMIT_TOKEN_ATTR_NAME_SESSION";
	/**
	 * 令牌属性在REQUEST中的属性名
	 * */
	public static final String TOKEN_REQUEST_ATTR_NAME = "SUBMIT_TOKEN_ATTR_NAME_REQUEST";

	/**
	 * 判定当前请求中是否包含合法令牌值的方法
	 * 
	 * @return true-请求中包含合法令牌值,false-请求中不包含合法令牌值
	 * */
	public static boolean isTokenValid() {
		// 获取当前请求中的令牌值
		String requestToken = HttpRequestContext.getRequest().getParameter(
				TOKEN_REQUEST_ATTR_NAME);
		// 获取会话中的令牌值
		Object sessionToken = HttpRequestContext.getRequest().getSession()
				.getAttribute(TOKEN_SESSION_ATTR_NAME);

		// System.out.println(requestToken+"            "+sessionToken);
		// 判定令牌是否合法
		return sessionToken != null
				&& sessionToken.toString().equals(requestToken);
	}

	/**
	 * 释放令牌的方法
	 * */
	public static void releaseToken() {
		// 释放会话令牌值
		HttpRequestContext.getRequest().getSession()
				.setAttribute(TOKEN_SESSION_ATTR_NAME, "");

	}

	/**
	 * 结束标签回调
	 * 
	 * @return 回调结束后的执行标识
	 * */
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		// 利用UUID获取唯一的令牌值
		String token = UUID.randomUUID().toString();
		// 在会话中保存令牌值
		pageContext.getSession().setAttribute(TOKEN_SESSION_ATTR_NAME, token);
		// 创建表单令牌域HTML字符串
		String tokenTag = "<input type=\"hidden\" name=\"SUBMIT_TOKEN_ATTR_NAME_REQUEST\" value=\""
				+ token + "\"/>";
		try {
			// 在页面中输出令牌域字符串
			pageContext.getOut().print(tokenTag);
			// 捕获异常
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// 输出异常信息
			e.printStackTrace();
		}
		// 本标签结束后继续执行页面的其他代码
		return EVAL_PAGE;
	}

	/**
	 * 开始标签回调
	 * 
	 * @return 回调结束后的执行标识
	 * */
	@Override
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		// 跳过标签体
		return SKIP_BODY;
	}

}
