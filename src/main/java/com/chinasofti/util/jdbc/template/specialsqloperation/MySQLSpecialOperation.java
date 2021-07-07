/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.template.specialsqloperation;

import java.sql.PreparedStatement;

/**
 * <p>
 * Title: MySQLSpecialOperation
 * </p>
 * <p>
 * Description: MySQL数据库特定操作抽象
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
public class MySQLSpecialOperation extends SpecialSQLOperation {
	/**
	 * 获取特定TopN操作SQL语句的方法
	 * 
	 * @param initialSQL
	 *            初始化SQL语句
	 * @param hasOffset
	 *            是否支持offSet操作
	 * @return 获取到的特定TopN操作语句
	 * */
	@Override
	public String getTopNSQL(String initialSQL, boolean hasOffset) {
		// TODO Auto-generated method stub
		// 利用limit生成特殊的TopN查询语句
		return new StringBuffer(initialSQL.length() + 50).append(initialSQL)
				.append(hasOffset ? " limit ?, ?" : " limit ?").toString();
	}

	/**
	 * 设置TopN查询操作特殊参数信息
	 * 
	 * @param topNStatement
	 *            预编译语句对象
	 * @param args
	 *            查询语句对应的参数
	 * @param offset
	 *            查询的offset值
	 * @param size
	 *            单次查询返回的最大条目值
	 * @return 设置参数后的预编译语句对象
	 * */
	@Override
	public PreparedStatement setTopNQueryParameter(
			PreparedStatement topNStatement, Object[] args, int offset, int size) {
		// 尝试设置参数
		try {
			// 遍历参数值
			for (int i = 0; i < args.length; i++) {
				// 为相应的占位符设置对应的值
				topNStatement.setObject(i + 1, args[i]);
			}
			// 设置offset参数
			topNStatement.setObject(args.length + 1, offset);
			// 设置每页最大条目数参数
			topNStatement.setObject(args.length + 2, size);
			// TODO Auto-generated method stub
			// 返回设置参数后的预编译语句对象
			return topNStatement;
			// 捕获异常
		} catch (Exception e) {
			// 输出异常信息
			e.printStackTrace();
			// 返回null
			return null;
			// TODO: handle exception
		}

	}

}
