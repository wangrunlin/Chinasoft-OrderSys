/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.bean.convertor;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * <p>
 * Title: SimpleDateConvertor
 * </p>
 * <p>
 * Description: 将字符串数据转换为java.util.Date类型数据的工具
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
public class SimpleDateConvertor implements TypeConvertor {
	/**
	 * 执行类型转换的方法
	 * 
	 * @param srcString
	 *            原始数据值
	 * @return 转换后的结果数据
	 * */
	@Override
	public Object convertToObject(Object srcString) {
		// 创建日期模式
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			// 将字符串按照日期模式转化为Date对象
			Date date=sdf.parse(srcString.toString());
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
		
	}

}
