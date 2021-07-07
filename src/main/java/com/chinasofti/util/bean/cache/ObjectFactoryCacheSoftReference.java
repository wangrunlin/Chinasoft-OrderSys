/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.bean.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * <p>
 * Title: ObjectFactoryCacheSoftReference
 * </p>
 * <p>
 * Description: 缓存使用的软引用
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
public class ObjectFactoryCacheSoftReference extends SoftReference {

	/**
	 * 软引用所引用的对象在缓存中的检索键
	 * */
	String key = "";

	/**
	 * 构建软引用的构造方法
	 * 
	 * @param key
	 *            该软引用所引用的对象在缓存中的检索键
	 * @param referent
	 *            该软引用所引用的实际对象，即要加入到缓存中的JavaBean对象
	 * @param q
	 *            该软引用所绑定的引用队列，如该软引用所引用的对象被垃圾回收机制回收，则自动将该引用本身加入到本队列中
	 * */
	public ObjectFactoryCacheSoftReference(String key, Object referent,
			ReferenceQueue q) {
		// 调用父类的构造方法
		super(referent, q);
		// 初始化索引键值
		this.key = key;
	}	

}
