/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>
 * Title: ServerPushMQ
 * </p>
 * <p>
 * Description: 服务器消息推送机制的消息等待序列
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
public class ServerPushMQ {
	/**
	 * 用于存放消息等待序列的散列表，键中包含了需要获取数据的客户端sessionid以及期望获取的消息名称，
	 * 值对象为存放消息字符串的容器并作为阻塞客户端的锁对象存在
	 * */
	static Hashtable<ServerPushKey, Message> waitQueue = new Hashtable<ServerPushKey, Message>();


}
