/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.admin;

import java.util.ArrayList;

import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.jdbc.template.JDBCTemplateWithDS;
import com.chinasofti.util.sec.Passport;

/**
 * <p>
 * Title: UserService
 * </p>
 * <p>
 * Description: 用户管理服务对象
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
public class UserService {

	/**
	 * 分页获取用户数据的方法
	 * 
	 * @param page
	 *            要获取数据的页号
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前页的用户数据列表
	 * */
	public ArrayList<UserInfo> getByPage(int page, int pageSize) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 通过查询语句获取对应页的数据
		ArrayList<UserInfo> list = helper
				.preparedForPageList(
						"select userId,userAccount,userPass,locked,roleId,roleName,faceimg from userinfo,roleinfo where userinfo.role=roleinfo.roleId order by userId",
						new Object[] {}, page, pageSize, UserInfo.class);
		// 返回结果
		return list;
	}

	/**
	 * 获取用户信息的最大页数
	 * 
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前数据库中数据的最大页数
	 * */
	public int getMaxPage(int pageSize) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 获取最大页数信息
		Long rows = (Long) helper.preparedQueryForObject(
				"select count(*) from userinfo", new Object[] {});
		// 返回最大页数
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * 添加用户的方法
	 * 
	 * @param info
	 *            需要添加的用户信息
	 * */
	public void addUser(UserInfo info) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 创建加密工具
		Passport passport = new Passport();
		// 执行用户信息插入操作
		helper.executePreparedUpdate(
				"insert into userinfo(userAccount,userPass,role,faceImg) values(?,?,?,?)",
				new Object[] { info.getUserAccount(),
						passport.md5(info.getUserPass()),
						new Integer(info.getRoleId()), info.getFaceimg() });
	}

	/**
	 * 删除用户的方法
	 * 
	 * @param userId
	 *            待删除用户的Id
	 * */
	public void deleteUser(Integer userId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 删除给定ID的用户信息
		helper.executePreparedUpdate("delete from userinfo where userId=?",
				new Object[] { userId });
	}

	/**
	 * 修改用户自身信息的方法
	 * 
	 * @param info
	 *            需要修改的用户信息，其中userId属性指明需要修改的用户ID，其他信息为目标值，本人修改信息只能修改密码和头像
	 * */
	public void modify(UserInfo info) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 创建加密工具
		Passport passport = new Passport();
		// 修改本人信息
		helper.executePreparedUpdate(
				"update userinfo set userPass=?,faceimg=? where userId=?",
				new Object[] { passport.md5(info.getUserPass()),
						info.getFaceimg(), new Integer(info.getUserId()) });

	}

	/**
	 * 管理员修改用户信息的方法
	 * 
	 * @param info
	 *            需要修改的用户信息，其中userId属性指明需要修改的用户ID，其他信息为目标值
	 * */
	public void adminModify(UserInfo info) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 创建加密工具
		Passport passport = new Passport();
		// 修改本人信息
		helper.executePreparedUpdate(
				"update userinfo set userPass=?,faceimg=?,role=? where userId=?",
				new Object[] { passport.md5(info.getUserPass()),
						info.getFaceimg(), new Integer(info.getRoleId()),
						new Integer(info.getUserId()) });

	}

	/**
	 * 根据ID获取用户详细信息的方法
	 * 
	 * @param userId
	 *            需要获取详细信息的用户ID
	 * @return 返回查询到的用户详细信息
	 * */
	public UserInfo getUserById(Integer userId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 按照用户ID条件进行查询操作
		ArrayList<UserInfo> list = helper
				.preparedQueryForList(
						"select userId,userAccount,userPass,locked,roleId,roleName,faceimg from userinfo,roleinfo where userinfo.role=roleinfo.roleId and userId=?",
						new Object[] { userId }, UserInfo.class);
		// 返回给定ID对应的用户信息
		return list.get(0);
	}

}
