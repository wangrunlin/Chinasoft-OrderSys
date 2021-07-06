/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.serverpush;

import java.util.Hashtable;

/**
 * <p>
 * Title: MessageHandler
 * </p>
 * <p>
 * Description: ��Ϣ�������ص��ӿ�
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
public interface MessageHandler {
	/**
	 * ������Ϣ�Ļص�����
	 * 
	 * @param messageQueue
	 *            ����Ϣ���ڵ���Ϣ�ȴ�����
	 * @param key
	 *            ����Ϣ�ļ���������Ϣ��ԵĿͻ�sessionid����Ϣ�ı���
	 * @param msg
	 *            ��Ϣ����
	 * */
	public void handle(Hashtable<ServerPushKey, Message> messageQueue,
                       ServerPushKey key, Message msg);

}
