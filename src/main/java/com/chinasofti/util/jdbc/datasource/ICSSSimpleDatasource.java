/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.datasource;

import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * <p>
 * Title: ICSSSimpleDatasource
 * </p>
 * <p>
 * Description:
 * 数据库连接池对象，连接池使用代理模式而不是用包装模式的原因是Connection接口在不同的JDK版本中会发生细微的变化，而代理模式则不关心这种变化
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
public class ICSSSimpleDatasource implements DataSource {

	/**
	 * 保存连接的向量集合
	 * */
	private Vector<DSConnectionContext> dbPool = new Vector<DSConnectionContext>();

	/**
	 * 连接池的最大容量
	 * */
	private int maxConnections = 100;

	/**
	 * 不能获取连接时的等待时间，单位：毫秒
	 * */
	private int waitTimeOut = 300;

	/**
	 * 当连接数目不足时，自动创建新的连接的数量
	 * */
	private int incrementalConnections = 5;

	/**
	 * 初始化时的链接数量,必须在能够自动调用init()方法进行初始化时才能够生效
	 * */
	private int initConnections = 5;

	/***
	 * 尝试获取连接的次数
	 */
	private int waitTimes = 5;

	/**
	 * JDBC连接驱动字符串
	 * */
	private String driverString = "";

	/**
	 * JDBC连接字符串
	 * */
	private String conString = "";// 连接字符串

	/**
	 * 数据库连接用户名
	 * */
	private String dbUser = "";// 连接数据库的用户名
	/**
	 * 数据库连接密码
	 * */
	private String dbPass = "";// 连接数据库的密码

	/**
	 * 获取当前的整个连接池
	 * 
	 * @return 返回存放连接池所有连接的集合向量
	 */
	public Vector<DSConnectionContext> getDbPool() {
		return dbPool;
	}

	/**
	 * 设置当前数据库连接池的最大连接数
	 * 
	 * @param maxConnections
	 *            希望连接池变更的容量
	 * */
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	/**
	 * 设置每次用户获取数据库连接时的超时时间
	 * 
	 * @param waitTimeOut
	 *            用户等待超时时间，单位为毫秒
	 * */
	public void setWaitTimeOut(int waitTimeOut) {
		this.waitTimeOut = waitTimeOut;
	}

	/**
	 * 设置当无法获取空闲连接时尝试增长的链接数目
	 * 
	 * @param incrementalConnections
	 *            自动扩展连接的数目，连接数不能扩展超过连接池的最大容量
	 * */
	public void setIncrementalConnections(int incrementalConnections) {
		this.incrementalConnections = incrementalConnections;
	}

	/**
	 * 设置创建连接池时的初始化连接数
	 * 
	 * @param initConnections
	 *            创建连接池时的初始化连接数，只有在工厂容器能够调用init初始化方法的时候生效
	 * */
	public void setInitConnections(int initConnections) {
		this.initConnections = initConnections;
	}

	/**
	 * 设置用户尝试获取连接的等待次数
	 * 
	 * @param waitTimes
	 *            用户可以尝试获取连接的次数
	 * */
	public void setWaitTimes(int waitTimes) {
		this.waitTimes = waitTimes;
	}

	/**
	 * 设置连接池创建JDBC连接时使用的驱动字符串
	 * 
	 * @param driverString
	 *            JDBC驱动字符串
	 * */
	public void setDriverString(String driverString) {
		this.driverString = driverString;
	}

	/**
	 * 设置连接池创建JDBC连接时使用的连接字符串
	 * 
	 * @param conString
	 *            JDBC连接字符串
	 * */
	public void setConString(String conString) {
		this.conString = conString;
	}

	/**
	 * 设置创建连接时需要提供的数据库用户名
	 * 
	 * @param dbUser
	 *            数据库用户名
	 * */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	/**
	 * 设置创建连接时需要提供的数据库密码
	 * 
	 * @param dbPass
	 *            数据库用户密码
	 * */
	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	/**
	 * 初始化连接池的方法，需要工厂容器在创建连接池对象并设置相关属性后自动调用
	 * */
	public void init() {
		// 创建新的连接池容器
		dbPool = new Vector();
		// 根据初始化的连接池容量大小创建响应数目的连接
		createConnections(initConnections);
	}

	/**
	 * 创建特定数量的数据库连接的方法
	 * 
	 * @param incremental
	 *            需要增长额连接数目
	 * */
	private void createConnections(int incremental) {
		// 每次循环创建一个JDBC连接
		for (int i = 0; i < incremental; i++) {
			// 判断是否到达连接池规定的最大连接数
			if (dbPool.size() >= maxConnections) {
				// 如果已经到达最大连接数，则放弃创建操作
				break;
			}

			// 创建JDBC连接并进行代理后构建连接上下文对象
			DSConnectionContext conT = createConnection();
			if (conT != null) {
				// 如果构建成功，则将本次获取到的连接上下文对象存入连接池容器中
				dbPool.addElement(conT);
			}
		}
	}

	/**
	 * 创建单个连接的方法
	 * 
	 * @return 创建成功的连接上下文
	 * */
	private DSConnectionContext createConnection() {
		try {
			// 加载JDBC驱动程序
			Class.forName(driverString);
			// 利用给定的连接字符串、用户名及密码创建JDBC连接
			Connection con = DriverManager.getConnection(conString, dbUser,
					dbPass);
			// 判定创建的是否是连接池中的第一个连接
			if (dbPool.size() == 0) {
				// 如果是创建的第一个连接，则获取数据库的元数据
				DatabaseMetaData metaData = con.getMetaData();
				// 利用数据库元数据获取数据库允许的最大连接数
				int driverMaxConnection = metaData.getMaxConnections();
				// 如果数据库对最大的连接数有限制并且该数目小于给定的数据库连接池容量，则将数据库限定的连接数设置为当前连接池的容量
				if (driverMaxConnection > 0
						&& maxConnections > driverMaxConnection) {
					setMaxConnections(driverMaxConnection);
				}
			}
			// 创建数据库连接上下文
			DSConnectionContext conT = new DSConnectionContext(con);
			// 创建连接的代理处理器对象
			DSConnectionInvocationHandler conHandle = new DSConnectionInvocationHandler(
					conT);
			// 创建代理以后的连接对象
			Connection proxyCon = (Connection) Proxy.newProxyInstance(con
					.getClass().getClassLoader(),
					new Class[] { Connection.class }, conHandle);
			// 将代理以后的连接对象传递给连接上下文
			conT.proxyConnection = proxyCon;
			// 返回获取到的连接上下文
			return conT;
		} catch (Exception e) {
			// 如果出现异常，则输出异常信息
			e.printStackTrace();
			// 在出现异常的情况下返回null
			return null;
		}
	}

	/**
	 * 当客户端尝试获取连接不成功时等待
	 * 
	 * @param mSeconds
	 *            单次等待的时间，单位为毫秒
	 * */
	private void clientWait(int mSeconds) {

		try {
			// 尝试让本线程休眠一段时间，休眠的时长由参数mSeconds确定
			Thread.sleep(mSeconds);
		} catch (InterruptedException e) {
			// 出现异常则输出异常信息
			e.printStackTrace();

		}
	}

	/**
	 * 数据库连接池获取连接的核心方法
	 * 
	 * @return 返回从连接池中获取到的空闲连接对象，该对象已被代理，调用close方法时不会关闭连接而是直接将该连接的状态重置为空闲
	 * */
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		// 如果连接池本身为空，则返回null
		if (dbPool == null) {
			return null;
		}
		// 尝试从连接池容器中查找空闲的连接对象
		Connection con = getFreeConnection();
		// 定义客户端等待次数
		int wTimes = 0;
		// 如果没有直接获取到空闲连接，并且当前等待次数还没有到达规定的最大等待次数，则延迟一段时间后重新尝试
		while (con == null && wTimes < waitTimes) {
			// 等待次数累加
			wTimes++;
			// 等待延迟
			clientWait(waitTimeOut);
			// 再次尝试获取连接
			con = getFreeConnection();
		}
		// 返回获取到的连接对象
		return con;
	}

	/**
	 * 试图查找空闲连接，如果现在连接池目前不存在空闲连接，则试图扩展连接池容量后再次查找
	 * 
	 * @return 查找到的空闲连接，如果无法得到空闲连接，则返回null
	 * */
	private Connection getFreeConnection() {
		// 尝试直接获取当前容器中的空闲连接
		Connection con = findFreeConnection();
		// 如果当前容器中没有空闲连接，则尝试扩展连接池容量
		if (con == null) {
			// 创建一定数量的连接
			createConnections(incrementalConnections);
			// 再次尝试获取连接池中的空闲连接
			con = findFreeConnection();
			// 如果仍然无法获取到空闲连接说明容量已经到达规定数目且所有的连接都已经被占用，返回null
			if (con == null) {
				return null;
			}
		}
		// 返回获取到的连接对象
		return con;
	}

	/**
	 * 查找现有连接池中空闲连接的方法
	 * 
	 * @return 查找到的空闲连接，如果当前连接池中没有空闲连接，则返回null
	 * */
	private Connection findFreeConnection() {

		// 创建返回连接上下文对象
		DSConnectionContext conT = null;

		try {
			// 遍历整个连接池容器
			for (int i = 0; i < dbPool.size(); i++) {
				// 获取到单一的连接上下文对象
				conT = dbPool.elementAt(i);
				// 判定当前连接是否被占用，如果状态为空闲，则直接占用当前连接
				if (!conT.busyFlag) {
					// 占用当前连接
					conT.busyFlag = true;
					// 输出调试信息
					System.out.println("使用第" + i + "个连接处理数据");
					// 返回代理以后的连接对象
					return conT.proxyConnection;
				}
			}
			//如果所有的连接都已经被占用，则返回null
			return null;
		} catch (Exception ex) {
			//发生异常情况时输出异常信息
			ex.printStackTrace();
			//在发生异常时返回null
			return null;
		}
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
