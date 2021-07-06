/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.login;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.chinasofti.ordersys.listeners.OrderSysListener;
import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.jdbc.template.JDBCTemplateWithDS;

/**
 * <p>
 * Title: LoginService
 * </p>
 * <p>
 * Description: 判定用户是否登录成功的服务对象
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
public class LoginService {

	/**
	 * 用户名错误的标识
	 * */
	public static final int WRONG_USERNAME = 0;
	/**
	 * 密码错误的标识
	 * */
	public static final int WRONG_PASSWORD = 1;
	/**
	 * 用户已经在线不能重复登录的标识
	 * */
	public static final int USER_ALREADY_ONLINE = 2;
	/**
	 * 用户被锁定标识
	 * */
	public static final int WRONG_LOCKED = 3;
	/**
	 * 其他错误的标识
	 * */
	public static final int WRONG_OTHER = 4;
	/**
	 * 登录成功标识
	 * */
	public static final int LOGIN_OK = 5;

	/**
	 * 登录成功的用户信息
	 * */
	private UserInfo loginUser = null;

	/**
	 * 获取登录成功的用户信息的方法
	 * 
	 * @return 返回登录成功的用户信息
	 * */
	public UserInfo getLoginUser() {
		// 返回用户信息
		return loginUser;
	}

	/**
	 * 登录的判定方法
	 * 
	 * @param Info
	 *            用户输入的需判定登录用户信息
	 * @return 登录判定结果标识值
	 * */
	public int login(UserInfo info) {
		// 获取当前在线的用户信息，用于判定用户是否已经在线
		Hashtable<String, UserInfo> loginUserMap = OrderSysListener.sessions;
		// 获取在线所有已经登录用户对应的sessionId
		Set<String> loginIds = loginUserMap.keySet();
		// 获取sessionId迭代器
		Iterator<String> it = loginIds.iterator();
		// 迭代sessionId
		while (it.hasNext()) {
			// 获取特定的在线用户信息
			UserInfo user = loginUserMap.get(it.next());
			// 如果某个在线用户的用户名和用户登录的用户名相同，说明试图登录的用户已经在线
			if (user.getUserAccount().equals(info.getUserAccount())) {
				// 返回错误标识
				return USER_ALREADY_ONLINE;
			}
		}
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS dbHelper = JDBCTemplateWithDS.getJDBCHelper();
		// 根据给定用户名查询用户信息
		ArrayList<UserInfo> userList = dbHelper
				.preparedQueryForList(
						"select userId,userAccount,userPass,locked,roleId,roleName,faceimg from userinfo,roleinfo where userinfo.role=roleinfo.roleId and userinfo.userAccount=?",
						new Object[] { info.getUserAccount() }, UserInfo.class);
		// 判定是否查询到数据
		switch (userList.size()) {
		// 如果没有查询到数据，说明用户名不存在
		case 0:
			// 返回用户名错误的标识
			return WRONG_USERNAME;
			// 如果查询到数据
		case 1:
			// 获取数据库中的用户信息
			UserInfo dbUser = userList.get(0);

			// System.out.println(info.getLocked());
			// 如果用户已经被锁定
			if (dbUser.getLocked() == 1) {
				// 保存登录用户信息
				loginUser = dbUser;
				// 返回用户已经被锁定的标识
				return WRONG_LOCKED;
			}
			// 如果用户密码匹配
			if (info.getUserPass().equals(dbUser.getUserPass())) {
				// 保存登录用户信息
				loginUser = dbUser;
				// 返回登录成功标识
				return LOGIN_OK;
				// 如果密码不匹配
			} else {
				// 返回密码错误标识
				return WRONG_PASSWORD;
			}
			// 其他情况
		default:
			// 返回其他错误标识
			return WRONG_OTHER;

		}

	}
}
