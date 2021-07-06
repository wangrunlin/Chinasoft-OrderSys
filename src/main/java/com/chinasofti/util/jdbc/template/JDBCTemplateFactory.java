/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.template;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Title: JDBCTemplateFactory
 * </p>
 * <p>
 * Description: 利用XML配置文件获取JDBC模板模式数据库访问工具的工厂类
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
public class JDBCTemplateFactory {

	/**
	 * 保存JDBC连接工具单例实例对象
	 * */
	private static JDBCTemplate instance;

	/**
	 * 私有的构造方法，本类无需构建实例
	 * */
	private JDBCTemplateFactory() {

	}

	/**
	 * 获取JDBC模板模式封装工具类单例实例的静态工厂方法
	 * @param configFilePath JDBC连接参数配置文件路径
	 * @return 返回JDBC工具类单例实例
	 * */
	public static JDBCTemplate getJDBCHelper(String configFilePath) {
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
				instance = new JDBCTemplate(driverName,
						connectionString, dbmsUserName, dbmsPassword);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 返回实例对象
		return instance;
	}

	/**
	 * 利用默认配置文件获取JDBC工具实例的方法
	 * @return 返回JDBC工具单例实例
	 * */
	public static JDBCTemplate getJDBCHelper() {
		// 利用默认配置文件创建JDBC工具实例
		return getJDBCHelper(Thread.currentThread().getContextClassLoader().getResource("jdbc.xml").getPath());
	}

}
