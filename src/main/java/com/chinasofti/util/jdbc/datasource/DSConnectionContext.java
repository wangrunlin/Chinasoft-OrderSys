/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.datasource;

import java.sql.Connection;
/**
 * <p>Title: DSConnectionContext</p>
 * <p>Description: 保存数据库连接池连接对象上下文</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: ChinaSoft International Ltd.</p>
 * @author etc
 * @version 1.0
 */
public class DSConnectionContext {

	/**
	 * 真实的JDBC连接对象
	 * */
	Connection dbConnection;
	/**
	 * 代理以后的JDBC连接对象
	 * */
	Connection proxyConnection;
	/**
	 * 确定当前连接是否被占用的标志位，true表示当前连接正在被占用，false表示当前连接为空闲状态
	 * */
	boolean busyFlag;

	/**
	 * 利用现有的JDBC连接对象创建上下文的构造方法
	 * @param dbConnection 创建好的普通JDBC连接对象
	 * */
	DSConnectionContext(Connection dbConnection) {
		//将当前连接设置为空闲状态
		busyFlag = false;
		//初始化连接对象
		this.dbConnection = dbConnection;
	}

}
