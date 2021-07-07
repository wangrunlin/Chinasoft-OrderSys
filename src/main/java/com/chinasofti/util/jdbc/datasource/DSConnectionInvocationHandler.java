/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 * <p>Title: DSConnectionInvocationHandler</p>
 * <p>Description: 连接对象的代理处理器</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: ChinaSoft International Ltd.</p>
 * @author etc
 * @version 1.0
 */
public class DSConnectionInvocationHandler implements InvocationHandler {
	/**
	 * 数据库连接上下文
	 * */
	DSConnectionContext conn;

	/**
	 * 利用数据库连接上下文构建处理器的构造方法
	 * @param conn 数据库连接上下文
	 * */
	public DSConnectionInvocationHandler(DSConnectionContext conn) {
		//初始化连接上下文
		this.conn = conn;
	}

	/**
	 * 代理处理器核心方法，自动代理Connection接口的close方法，不关闭连接而是将连接状态置为空闲，供其他用户使用
	 * @param proxy 代理以后的连接对象
	 * @param method 客户端试图调用的业务方法
	 * @param args 客户端调用业务方法时传递的实际参数
	 * @return 返回给客户端的实际返回值
	 * @see InvocationHandler#invoke(Object, Method, Object[])
	 * */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// TODO Auto-generated method stub
		//定义返回值对象
		Object resultObject = null;
		//判定是否调用关闭连接的close方法
		if ("close".equals(method.getName())) {
			//如果是close方法则不执行其原始内容而讲本连接状态重置为空闲
			conn.busyFlag = false;
		} else {
			//如果不是close方法则利用原始的连接对象执行对应的方法并获取返回值
			resultObject = method.invoke(conn.dbConnection, args);
		}
		//返回实际的返回值对象
		return resultObject;
	}

}
