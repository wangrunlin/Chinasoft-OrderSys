/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */

package com.chinasofti.util.jdbc.template;

import java.sql.Statement;
/**
 * <p>Title: JDBCCallback</p>
 * <p>Description: JDBC模板回调接口</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: ChinaSoft International Ltd.</p>
 * @author etc
 * @version 1.0
 */
public interface JDBCCallback {
	
	/**
	 * 实际回调方法
	 * @param statement 模板容器获取的语句对象，用户可以直接利用该对象执行数据操作
	 * @return 根据业务的实现返回不同的信息，如数据更新操作涉及的行数
	 * */
	public Object doWithStatement(Statement statement);

}
