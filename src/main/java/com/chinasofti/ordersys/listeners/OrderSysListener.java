/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.listeners;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.chinasofti.ordersys.vo.UserInfo;

/**
 * <p>
 * Title: OrderSysListener
 * </p>
 * <p>
 * Description: 点餐系统监听器，主要监听会话的创建、销毁、登录信息会话变量的设置，用于实现在线用户数及在线后厨人员、在线服务员列表功能
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
public class OrderSysListener implements HttpSessionListener,
		HttpSessionAttributeListener {

	/**
	 * 存放所有已经登陆用户信息的Hashtable,键为用户sessionID，值为用户详细信息
	 * */
	public static Hashtable<String, UserInfo> sessions = new Hashtable<String, UserInfo>();
	/**
	 * 保存在线会话数的变量
	 * */
	public static int onlineSessions = 0;

	/**
	 * 获取在线服务员列表的方法
	 * 
	 * @return 当前在线的所有餐厅服务员列表信息
	 * */
	public static ArrayList<UserInfo> getOnlineWaiters() {
		// 返回当前在线的所有服务员列表
		return getOnlineUsersByRoleId(3);
	}

	/**
	 * 获取在线后厨人员列表的方法
	 * 
	 * @return 当前在线的所有餐厅后厨人员列表信息
	 * */
	public static ArrayList<UserInfo> getOnlineKitchens() {
		// 返回当前在线的所有后厨人员列表
		return getOnlineUsersByRoleId(2);
	}

	/**
	 * 获取特定角色在线人员列表的方法
	 * 
	 * @param roleId
	 *            要获取的人员角色ID，1-餐厅管理员，2-后厨人员，3-餐厅服务员
	 * @return 当前在线的所有符合角色的人员列表信息
	 * */
	private static ArrayList<UserInfo> getOnlineUsersByRoleId(int roleId) {
		// 获取所有在线用户的sessionID
		Set<String> sessionIds = sessions.keySet();
		// 获取在线用户ID的迭代器
		Iterator<String> sessionIdIt = sessionIds.iterator();
		// 创建结果集合
		ArrayList<UserInfo> list = new ArrayList<UserInfo>();
		// 迭代在线用户ID
		while (sessionIdIt.hasNext()) {
			// 获取该sessionID对应的用户信息
			UserInfo info = sessions.get(sessionIdIt.next());
			// 判定角色信息是否符合要求
			if (info.getRoleId() == roleId) {
				// 如果角色信息和给定信息一致，则将该用户加入结果列表
				list.add(info);
			}
		}
		// 返回结果列表
		return list;

	}

	/**
	 * 当session中添加属性时的监听器回调方法
	 * 
	 * @param arg0
	 *            监听器事件信息，可以获取添加属性的session\添加的属性名\添加的属性值等信息
	 * */
	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		// 如果添加的属性名是USER_INFO，则说明有一个用户登录了
		if (arg0.getName().equals("USER_INFO")) {
			// 将该用户信息添加到在线用户列表中
			sessions.put(arg0.getSession().getId(), (UserInfo) arg0.getValue());
		}
	}

	/**
	 * 当session中移出属性时的监听器回调方法
	 * 
	 * @param arg0
	 *            监听器事件信息，可以获取移出属性的session\移出的属性名\移出的属性值等信息
	 * */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		// 如果移出的属性名是USER_INFO，则说明有一个用户注销了
		if (arg0.getName().equals("USER_INFO")) {
			// 从在线用户列表中将该用户信息移出
			sessions.remove(arg0.getSession().getId());
		}

	}

	/**
	 * 当session中属性值被替换时的监听器回调方法
	 * 
	 * @param arg0
	 *            监听器事件信息，可以获取替换属性的session\替换的属性名\替换的属性值等信息
	 * */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		// 如果替换的属性名是USER_INFO，则说明有一个用户变更登录身份了
		if (arg0.getName().equals("USER_INFO")) {
			// 更新当前用户的登录信息
			sessions.put(arg0.getSession().getId(), (UserInfo) arg0.getValue());
		}

	}

	/**
	 * 当创建Session时的监听器回调方法
	 * 
	 * @param arg0
	 *            监听器事件信息，可以获取创建的session信息
	 * */
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		// 在线会话数增加
		onlineSessions++;
	}

	/**
	 * 当销毁Session时的监听器回调方法
	 * 
	 * @param arg0
	 *            监听器事件信息，可以获取销毁的session信息
	 * */
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		// 在线会话数减少
		onlineSessions--;
		// 如果被销毁的时候已经登录的用户
		if (arg0.getSession().getAttribute("USER_INFO") != null) {
			// 从在线用户列表中将该用户信息移出
			sessions.remove(arg0.getSession().getId());
		}
	}

}
