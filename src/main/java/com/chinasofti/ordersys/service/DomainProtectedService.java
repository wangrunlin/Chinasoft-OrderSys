/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service;

import javax.servlet.http.HttpServletRequest;

import com.chinasofti.web.common.httpequest.HttpRequestContext;

/**
 * <p>
 * Title: DomainProtectedService
 * </p>
 * <p>
 * Description: 防止盗链、防止外站提交数据的服务对象
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
public class DomainProtectedService {

	/**
	 * 判定是否盗链或是否外站提交数据的方法
	 * 
	 * @return 判定结果，false表示本站合法请求，true表示外站请求
	 * */
	public boolean isFromSameDomain() {
		// 获取本次的请求对象
		HttpServletRequest request = HttpRequestContext.getRequest();
		// 获取本站的context root
		String path = request.getContextPath();
		// 获取本站截至到context root的域名信息
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		// 获取上一个页面的地址
		String fromUrl = request.getHeader("referer");
		// 判定是否外站请求并返回结果
		return fromUrl != null && fromUrl.startsWith(basePath) ? true : false;

	}

}
