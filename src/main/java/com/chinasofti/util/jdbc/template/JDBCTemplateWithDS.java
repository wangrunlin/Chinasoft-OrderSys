/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.template;

import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.chinasofti.util.jdbc.datasource.ICSSSimpleDatasource;

/**
 * <p>
 * Title: JDBCTemplate
 * </p>
 * <p>
 * Description: 使用自实现轻量级低侵入性数据库连接池的JDBC模板模式封装主类
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
 * @see JDBCTemplate
 */
public class JDBCTemplateWithDS extends JDBCTemplate {
	/**
	 * 本工具实例对象，用于工厂方法返回
	 * */
	private static JDBCTemplateWithDS instance;
	/**
	 * 简易数据库连接池工具
	 * */
	ICSSSimpleDatasource dS;

	/**
	 * 构造器，用于构建封装工具对象，和父类构造器不同，本类构造器还需要初始化简易数据库连接池的JDBC参数
	 * */
	JDBCTemplateWithDS(String driverName, String connectionString,
			String dbmsUserName, String dbmsPassword) {
		// 调用父类构造器
		super(driverName, connectionString, dbmsUserName, dbmsPassword);
		// 构造建议数据库连接池对象
		dS = new ICSSSimpleDatasource();
		// 设置数据库连接池中的JDBC连接字符串
		dS.setConString(connectionString);
		// 设置数据库连接池中的驱动名
		dS.setDriverString(driverName);
		// 设置数据库连接池中的用户名
		dS.setDbUser(dbmsUserName);
		// 设置数据库连接池中的用户密码
		dS.setDbPass(dbmsPassword);
		// 初始化数据库连接池对象，主要功能是预先创建特定数量的连接
		dS.init();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 返回数据库连接对象的工具
	 * 
	 * @return 返回获取到的连接对象
	 * */
	@Override
	public Connection getConnection() {
		// TODO Auto-generated method stub
		try {
			// 从数据库连接池中返回可供使用的空闲连接
			return dS.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// 如果有异常的情况则输出异常信息
			e.printStackTrace();
			// 返回空连接对象
			return null;
		}
	}

	/**
	 * 获取JDBC模板模式封装工具类单例实例的静态工厂方法
	 * 
	 * @param configFilePath
	 *            JDBC连接参数配置文件路径
	 * @return 返回JDBC工具类单例实例
	 * */
	public static JDBCTemplateWithDS getJDBCHelper(String configFilePath) {
		// 如果当前的工具实例为空，则需要读取配置文件后创建一个新的实例
		if (instance == null) {
			try {
				// 保存配置文件中存放的驱动名字
				String driverName = "";
				// 保存配置文件中存放的连接字符串
				String connectionString = "";
				// 保存配置文件存放的数据库登陆用户名
				String dbmsUserName = "";
				// 保存配置文件中存放的数据库登陆密码
				String dbmsPassword = "";

				// 创建DocumentBuilder对象，该对象将XML解析成为树状结构存放于内存
				DocumentBuilder db = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				// 解析configFilePath路径指向的XML文件
				Document doc = db.parse(configFilePath);
				// 在XML DOM中找到第一个jdbc标签（根标签）
				Node jdbcNode = doc.getElementsByTagName("jdbc").item(0);
				// 获取jdbc标签的子标签
				NodeList args = jdbcNode.getChildNodes();
				// 遍历jdbc标签的子标签
				for (int i = 0; i < args.getLength(); i++) {
					// 得到jdbc标签的一个子标签
					Node arg = args.item(i);
					// 判定子标签的标签名，如果是驱动名则将标签体的文本内容赋值给标签名变量
					if ("drivername".equals(arg.getNodeName())) {
						driverName = arg.getTextContent().trim();
						// 判定子标签的标签名，如果是连接字符串则将标签体的文本内容赋值给连接字符串变量
					} else if ("connectionstring".equals(arg.getNodeName())) {
						connectionString = arg.getTextContent().trim();
						// 判定子标签的标签名，如果是数据库用户名则将标签体的文本内容赋值给数据库用户名名变量
					} else if ("dbmsusername".equals(arg.getNodeName())) {
						dbmsUserName = arg.getTextContent().trim();
						// 判定子标签的标签名，如果是数据库密码则将标签体的文本内容赋值给数据库密码变量
					} else if ("dbmspassword".equals(arg.getNodeName())) {
						dbmsPassword = arg.getTextContent().trim();
					}

				}

				// 根据XML配置文件解析的结果创建新的JDBC工具实例
				instance = new JDBCTemplateWithDS(driverName, connectionString,
						dbmsUserName, dbmsPassword);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 返回实例对象
		return instance;
	}

	/**
	 * 利用默认配置文件获取JDBC模板模式封装工具类单例实例的静态工厂方法
	 * 
	 * @return 返回JDBC工具类单例实例
	 * */
	public static JDBCTemplateWithDS getJDBCHelper() {
		// 利用默认配置文件创建JDBC工具实例并返回
		return getJDBCHelper(Thread.currentThread().getContextClassLoader()
				.getResource("jdbc.xml").getPath());
	}

}
