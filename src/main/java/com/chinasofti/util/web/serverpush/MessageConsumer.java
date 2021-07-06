/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

/**
 * <p>
 * Title: MessageConsumer
 * </p>
 * <p>
 * Description: 消息的消费者
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
public class MessageConsumer {

	/**
	 * 消费者尝试获取并处理消息的方法，本方法会导致线程阻塞，直到真的获取到需要的消息并执行消息处理器的处理代码后才会继续执行后续程序
	 * 
	 * @param sessionID
	 *            需要获取消息的会话sessionid
	 * @param messageTitle
	 *            需要获取的消息的标题
	 * @param handler
	 *            当实际获取到消息后的处理器对象
	 * */
	public void searchMessage(String sessionID, String messageTitle,
			MessageHandler handler) {
		// 创建消息对象
		Message msg = new Message();
		// 利用给出的客户sessionid和消息标题创建消息等待序列的键对象
		ServerPushKey key = new ServerPushKey(sessionID, messageTitle);
		// 如果消息序列中存在当前的key，说明之前同一个客户端试图获取同一个消息的请求不成功
		if (ServerPushMQ.waitQueue.get(key) != null) {
			// 获取历史消息对象
			Message old = ServerPushMQ.waitQueue.get(key);
			// 将消息内容设置为请求超时
			old.setMsg(BuildinMessage.MESSAGE_WAITE_TIMEOUT);
			// 将原消息对象从消息等待序列中删除
			ServerPushMQ.waitQueue.remove(key);
			// 获取原有消息对象的同步锁
			synchronized (old) {
				// 唤醒原有消息对象锁定的线程，该线程处理的是http请求，完成请求
				old.notifyAll();
			}
		}
		// 获取新建消息对象的同步锁
		synchronized (msg) {
			try {
				// 将消息序列键对象和新的消息对象加入到消息等待序列
				ServerPushMQ.waitQueue.put(key, msg);
				// 利用msg对象将当前试图获取消息的线程（处理Http请求的线程）锁定并阻塞
				msg.wait();
				// 当当前线程被唤醒后调用消息处理器对象的消息处理回调方法
				handler.handle(ServerPushMQ.waitQueue, key, msg);
				// 如果当前消息内容不是超时信息则将本消息对象从消息等待序列中移除
				if (!msg.getMsg().equals(BuildinMessage.MESSAGE_WAITE_TIMEOUT)) {
					ServerPushMQ.waitQueue.remove(key);
				}
			} catch (InterruptedException e) {
				// 如果发生异常，则输出异常信息
				e.printStackTrace();
			}
		}
	}
}
