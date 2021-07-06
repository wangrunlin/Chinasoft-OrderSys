/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.bean.cache;

import java.lang.ref.ReferenceQueue;
import java.util.Hashtable;

/**
 * <p>
 * Title: FactoryBeanCache
 * </p>
 * <p>
 * Description: 利用软引用实现内存安全缓存的工具
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
public class FactoryBeanCache {
	/**
	 * 用于保存缓存数据的hashtable,其键为JavaBean存放到缓存中的检索名，值为缓存对象的软引用
	 * */
	private Hashtable<String, ObjectFactoryCacheSoftReference> cache = new Hashtable<String, ObjectFactoryCacheSoftReference>();
	/**
	 * 用于通知缓存对象被清除的引用队列
	 * */
	ReferenceQueue queue = new ReferenceQueue();

	/**
	 * 刷新缓存集合，检索是否有缓存对象被垃圾回收机制回收，如果存在被回收的对象，则将其软引用从Hashtble中删除
	 * */
	private void refershCache() {
		// 定义软应用变量，用于获取引用队列中出队的软引用
		ObjectFactoryCacheSoftReference ref = null;
		// 循环出队（如果队列中存在软引用对象，则说明垃圾回收机制认为当前虚拟机内存存在不足的风险，将不常用的只拥有软引用的对象清除后将其软引用加入了队列）
		while ((ref = (ObjectFactoryCacheSoftReference) queue.poll()) != null) {
			//将出队的软引用从Hashtable中删除
			cache.remove(ref.key);
		}
	}

	/**
	 *将对象加入缓存 的方法
	 *@param bean 需要加入软引用缓存的对象
	 *@param key 将对象加入缓存后的检索键
	 * */
	public void putCacheBean(Object bean, String key) {
		//在执行缓存操作前先刷新缓存内容
		refershCache();
		//只有在缓存以前不存在给出的检索键的情况下才加入缓存对象
		if (!cache.containsKey(key)) {
			//创建缓存对象的软引用
			ObjectFactoryCacheSoftReference ref = new ObjectFactoryCacheSoftReference(
					key, bean, queue);			
			System.out.println("将bean：" + bean + "加入缓存，key值为" + key);
			//移除强引用
			bean = null;
			//使用给出的检索键将软引用存放入Hashtable
			cache.put(key, ref);

		}

	}

	/**
	 *将对象从缓存 中取出方法
	 *@param key 将对象加入缓存后的检索键
	 *@return 返回从缓存容器中获取到的JavaBean
	 * */
	public Object getCacheBean(String key) {
		//在执行缓存操作前先刷新缓存内容
		refershCache();
		//如果缓存中存在相应的检索键，则继续查找
		if (cache.containsKey(key)) {
			//从Hashtable中利用检索键获取软引用
			ObjectFactoryCacheSoftReference ref = cache.get(key);
			System.out.println("从缓存中获取名为" + key + "的bean");
			//返回软引用所引用的JavaBean对象
			return ref.get();

		} else {
			//如果缓存中不存在相应的检索键，则返回null
			return null;
		}

	}

}
