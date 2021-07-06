/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.bean.convertor;

/**
 * <p>
 * Title: TypeConvertor
 * </p>
 * <p>
 * Description: 类型转换器接口，填充JavaBean时根据类型转换器将原始数据转换为目标类型
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
public interface TypeConvertor {
	/**
	 * 执行类型转换的方法
	 * 
	 * @param srcString
	 *            原始数据值
	 * @return 转换后的结果数据
	 * */
	public Object convertToObject(Object srcString);

}
