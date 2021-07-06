/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

/**
 * <p>
 * Title: MessageProducer
 * </p>
 * <p>
 * Description: 服务器推送消息的消息生产者
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
public class MessageProducer {

	/**
	 * 生产消息的方法
	 * 
	 * @param sessionID
	 *            本次生产的消息针对的客户sessionid
	 * @param msg
	 *            生产的消息内容
	 * */
	public void sendMessage(String sessionID, String messageTitle, String msg) {
		// 利用给出消息目标客户sessionid和消息标题创建消息键
		ServerPushKey key = new ServerPushKey(sessionID, messageTitle);
		// 如果消息等待序列中存在本键才执行消息的生产操作
		if (ServerPushMQ.waitQueue.get(key) != null) {
			// 获取到消息等待序列中的消息对象
			Message message = ServerPushMQ.waitQueue.get(key);
			// 将消息内容填充如消息对象
			message.setMsg(msg);
			// 获取到消息对象的同步锁
			synchronized (message) {
				// 唤醒由消息对象锁定阻塞的线程，即由消息的消费者阻塞的http请求处理线程，消息的消费者将直接调用消息处理器的处理方法
				message.notifyAll();
			}
		}
	}
}
