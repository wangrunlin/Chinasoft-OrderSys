/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

/**
 * <p>
 * Title: Message
 * </p>
 * <p>
 * Description: 用于保存服务器推送消息内容的类
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
public class Message {

	/**
	 * 服务器推送的消息内容字符串
	 * */
	String msg;

	/**
	 * 提供给消息的消费者获取当前消息内容的方法
	 * 
	 * @return 返回消息生产者生产的消息内容
	 * */
	public String getMsg() {
		return msg;
	}

	/**
	 * 提供给消息的生产者设置当前消息内容的方法
	 * 
	 * @param msg
	 *            生产者生产后希望发送给消费者的消息内容
	 * */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 将对象转变为字符串形式的方法，本类对象的字符串形式即消息内容
	 * 
	 * @return 当前的消息内容
	 * */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return msg;
	}

}
