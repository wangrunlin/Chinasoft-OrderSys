/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.bean;

/**
 * <p>
 * Title: FillBeanException
 * </p>
 * <p>
 * Description: 自定义的业务异常类，描述填充JavaBean属性时出现异常状况
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
public class FillBeanException extends RuntimeException {
	/**
	 * 构造方法，构建异常信息
	 * 
	 * @param beanClassName
	 *            出现异常时正在填充的JavaBean的类名
	 * @param propertyName
	 *            出现异常时正在填充的属性名称
	 * */
	public FillBeanException(String beanClassName, String propertyName) {
		super("填充Bean类" + beanClassName + "的属性" + propertyName + "时发生异常");
	}
}
