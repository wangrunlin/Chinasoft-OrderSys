/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.template.specialsqloperation;

import java.sql.PreparedStatement;

/**
 * <p>
 * Title: SpecialSQLOperation
 * </p>
 * <p>
 * Description: 特殊数据库特定操作抽象
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
public abstract class SpecialSQLOperation {

	/**
	 * 获取特定TopN操作SQL语句的方法
	 * 
	 * @param initialSQL
	 *            初始化SQL语句
	 * @param hasOffset
	 *            是否支持offSet操作
	 * @return 获取到的特定TopN操作语句
	 * */
	public abstract String getTopNSQL(String initialSQL, boolean hasOffset);

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
	public abstract PreparedStatement setTopNQueryParameter(
			PreparedStatement topNStatement, Object[] args, int offset, int size);
}
