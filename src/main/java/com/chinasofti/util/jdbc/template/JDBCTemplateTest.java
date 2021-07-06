/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.template;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title: JDBCTemplateTest
 * </p>
 * <p>
 * Description: 测试JDBC模板模式数据操作工具
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
public class JDBCTemplateTest {

	/**
	 * 测试的入口
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 测试直接new工具对象
		// JDBCTemplate helper = new JDBCTemplate("com.mysql.jdbc.Driver",
		// "jdbc:mysql://127.0.0.1:3306/chinasofti", "root", "root");

		// 测试工厂方法获取工具对象
		// JDBCTemplate helper = JDBCTemplateFactory.getJDBCHelper();

		// 测试普通语句对象数据更新操作
		// helper.executeUpdate("insert into t_stuinfo(stuname,stupass) values('icssuser1','icsspass1')");

		// 测试预编译语句对象数据更新操作
		// helper.executePreparedUpdate(
		// "insert into t_stuinfo(stuname,stupass) values(?,?)",
		// new Object[] { "icss用户2", "icss用户3" });

		// 测试普通语句对象批处理
		// String[] sqls = new String[20];
		// for (int i = 0; i < sqls.length; i++) {
		// sqls[i] = "insert into t_stuinfo(stuname,stupass) values('icssuser"
		// + i + "','icsspass" + i + "')";
		// }
		// helper.executeBatch(sqls);

		// 测试预编译语句对象批处理
		// List<Object[]> data = new ArrayList<Object[]>();
		// for (int i = 0; i < 20; i++) {
		// Object[] arg = new Object[2];
		// arg[0] = "icss用户" + i;
		// arg[1] = "icss密码" + i;
		// data.add(arg);
		// }
		// helper.executePreparedBatch(
		// "insert into t_stuinfo(stuname,stupass) values(?,?)", data, 6);

		// 测试预混合批处理
		// String[] sqls = new String[20];
		// for (int i = 0; i < sqls.length; i++) {
		// sqls[i] = "insert into t_stuinfo(stuname,stupass) values('icssuser"
		// + i + "','icsspass" + i + "')";
		// }
		// List<Object[]> data = new ArrayList<Object[]>();
		// for (int i = 0; i < 20; i++) {
		// Object[] arg = new Object[2];
		// arg[0] = "icss用户" + i;
		// arg[1] = "icss密码" + i;
		// data.add(arg);
		// }
		// helper.executeMixedBatch(
		// "insert into t_stuinfo(stuname,stupass) values(?,?)", data, 8,
		// sqls);

		// 测试非预编译语句对象查询
		// try {
		// ArrayList<Student> list = helper.queryForList(
		// "select * from t_stuinfo where stuid>5", Student.class);
		// for (Student stu : list) {
		// System.out.println(stu.getStuid() + "\t" + stu.getStuname()
		// + "\t" + stu.getStupass());
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// 测试预编译语句对象查询
		// try {
		//
		// ArrayList<Student> list = helper.preparedQueryForList(
		// "select * from t_stuinfo where stuid>?",
		// new Object[] { new Integer(1) }, Student.class);
		// for (Student stu : list) {
		// System.out.println(stu.getStuid() + "\t" + stu.getStuname()
		// + "\t" + stu.getStupass());
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		System.out
				.println(helper
						.preparedInsertForGeneratedKeys(
								"insert into userinfo(username,userpass,faceimg) values(?,?,?)",
								new Object[] { "bb", "cc", "dd" })[0]);

	}

}
