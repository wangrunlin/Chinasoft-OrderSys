/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.template.specialsqloperation;

import java.sql.PreparedStatement;

/**
 * <p>
 * Title: OracleSpecialOperation
 * </p>
 * <p>
 * Description:Oracle数据库特定操作抽象
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
public class OracleSpecialOperation extends SpecialSQLOperation {
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
		// 移出初始化SQL语句前后的无用空白
		initialSQL = initialSQL.trim();
		// 初始化SQL语句中是否有for update子句
		boolean isForUpdate = false;
		// 判定初始化SQL语句中是否有for update子句
		if (initialSQL.toLowerCase().endsWith(" for update")) {
			// 删除for update子句
			initialSQL = initialSQL.substring(0, initialSQL.length() - 11);
			// 保存for update子句状态
			isForUpdate = true;
		}
		// 创建目标SQL语句字符串缓冲区
		StringBuffer pagingSelect = new StringBuffer(initialSQL.length() + 200);
		// 如果有offSet信息
		if (hasOffset) {
			// 添加rownum伪列信息
			pagingSelect
					.append("select * from ( select row_.*, rownum rownum_ from ( ");
			// 如果没有offset信息
		} else {
			// 直接查询目标表格
			pagingSelect.append("select * from ( ");
		}
		// 将初始化查询作为临时视图查询目标
		pagingSelect.append(initialSQL);
		// 如果有offset
		if (hasOffset) {
			// 添加offset条件
			pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
			// 如果没有offset
		} else {
			// 只规定查询的最大条目数
			pagingSelect.append(" ) where rownum <= ?");
		}
		// 如果存在for update子句
		if (isForUpdate) {
			// 补全for update子句
			pagingSelect.append(" for update");
		}
		// 返回结果SQL语句
		return pagingSelect.toString();
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
		// TODO Auto-generated method stub
		// 尝试设置参数
		try {
			// 遍历参数值
			for (int i = 0; i < args.length; i++) {
				// 为相应的占位符设置对应的值
				topNStatement.setObject(i + 1, args[i]);
			}
			// 设置每页最大条目数参数
			topNStatement.setObject(args.length + 1, size + size);
			// 设置offset参数
			topNStatement.setObject(args.length + 2, offset);
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
