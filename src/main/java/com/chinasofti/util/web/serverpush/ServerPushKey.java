/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

/**
 * <p>
 * Title: ServerPushKey
 * </p>
 * <p>
 * Description: 消息等待序列键，包含了尝试获取消息的客户sessionid和试图获取的消息名称
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */
public class ServerPushKey {

	/**
	 * 试图获取本消息的客户sessionid
	 * */
	String sessionID = "";
	/**
	 * 试图获取的消息名称
	 * */
	String messageTitle = "";

	/**
	 * 创建消息键的构造方法
	 * 
	 * @param sessionID
	 *            尝试获取消息的客户sessionid
	 * @param messageTitle
	 *            客户尝试获取的消息名称
	 * */
	public ServerPushKey(String sessionID, String messageTitle) {
		super();
		this.sessionID = sessionID;
		this.messageTitle = messageTitle;
	}

	/**
	 * 判定两个消息键是否相同的方法，如果两个消息键包含的sessionid和消息标题都相同则认为连个键相同
	 * 
	 * @param arg0
	 *            用于对比的另一个消息对象
	 * */
	@Override
	public boolean equals(Object arg0) {
		// 定义描述两个消息键对象是否相同的变量
		boolean isEquals = false;
		// 只有对比对象也是消息键时才能做比较
		if (arg0 instanceof ServerPushKey) {
			// 获取对比消息键对象
			ServerPushKey key = (ServerPushKey) arg0;
			// 判定连个消息键的客户sessionid和消息标题是否相同
			if (key.sessionID.equals(sessionID)
					&& key.messageTitle.equals(messageTitle)) {
				isEquals = true;
			}
		}
		// 返回比对结果
		return isEquals;
	}

	/**
	 * 获取消息键对象的哈希值
	 * 
	 * @return 对象哈希值
	 * */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return sessionID.hashCode() + messageTitle.hashCode();
	}

}
