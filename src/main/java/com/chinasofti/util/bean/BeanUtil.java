/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.bean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Hashtable;

import com.chinasofti.util.bean.convertor.BooleanConvertor;
import com.chinasofti.util.bean.convertor.ByteConvertor;
import com.chinasofti.util.bean.convertor.CharConvertor;
import com.chinasofti.util.bean.convertor.DoubleConvertor;
import com.chinasofti.util.bean.convertor.FloatConvertor;
import com.chinasofti.util.bean.convertor.IntConvertor;
import com.chinasofti.util.bean.convertor.LongConvertor;
import com.chinasofti.util.bean.convertor.ShortConvertor;
import com.chinasofti.util.bean.convertor.StringConvertor;
import com.chinasofti.util.bean.convertor.TypeConvertor;
/**
 * <p>
 * Title: BeanUtil
 * </p>
 * <p>
 * Description: 利用反射和内省填充JAVA Bean属性的工具类
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
public class BeanUtil {

	/**
	 * 获取当前工具中使用的类型转换器集合
	 * @return 当前工具正在使用的类型转换器集合
	 * */
	public Hashtable<String, TypeConvertor> getConvertors() {
		return convertors;
	}

	/**
	 * 设置当前工具中使用的类型转换器集合
	 * @param convertors 希望工具使用的新的类型转换器集合
	 * */
	public void setConvertors(Hashtable<String, TypeConvertor> convertors) {
		this.convertors = convertors;
	}

	/**
	 * 用于在内存中保存对应类型的转换器，转换器支持字符串-特定类型，对象-特定类型，流-特定类型的转换
	 * */ 
	private Hashtable<String, TypeConvertor> convertors = new Hashtable<String, TypeConvertor>();

	/**
	 * 初始化工具，加载默认的类型转换器，默认类型转换器主要提供字符串到基本数据类型之间的转换
	 * */ 
	public BeanUtil() {

		// 将特定类型的数据转换器加入类型转换器列表
		convertors.put("java.lang.String", new StringConvertor());
		convertors.put("int", new IntConvertor());
		convertors.put("java.lang.Integer", new IntConvertor());
		convertors.put("byte", new ByteConvertor());
		convertors.put("java.lang.Byte", new ByteConvertor());
		convertors.put("short", new ShortConvertor());
		convertors.put("java.lang.Short", new ShortConvertor());
		convertors.put("long", new LongConvertor());
		convertors.put("java.lang.Long", new LongConvertor());
		convertors.put("float", new FloatConvertor());
		convertors.put("java.lang.Float", new FloatConvertor());
		convertors.put("double", new DoubleConvertor());
		convertors.put("java.lang.Double", new DoubleConvertor());
		convertors.put("boolean", new BooleanConvertor());
		convertors.put("java.lang.Boolean", new BooleanConvertor());
		convertors.put("char", new CharConvertor());
		convertors.put("java.lang.Character", new CharConvertor());
//		convertors.put("java.util.Date", new SimpleDateConvertor());
//		convertors.put("java.sql.Date", new SimpleDateConvertor());
	}

	/**
	 * 设置某个特定类型数据所需要使用的类型转换器，提供自定义转换器的设置操作，可以新增类型转换器，也可以覆盖默认转换器
	 * 
	 * @param targetType
	 *            需要转换的目标数据类型
	 * @param convertor
	 *            目标数据需要使用的数据类型转换器
	 * */
	public void setConvertor(String targetType, TypeConvertor convertor) {
		convertors.put(targetType, convertor);
	}

	
	/**
	 * 移除某个特定类型数据所需要使用的类型转换器的方法
	 * 
	 * @param targetType
	 *            需要转换的目标数据类型
	 * */
	public void removeConvertor(String targetType) {
		convertors.remove(targetType);
	}

	/**
	 * 清除所有类型数据所需要使用的类型转换器的方法
	 * */
	public void clearConvertor() {
		convertors.clear();
	}

	/**
	 * 利用字符串和对应转换器填充Bean
	 * 
	 * @param bean
	 *            要填充的对象
	 * @param propertyName
	 *            要填充的属性名
	 * @param propertyValue
	 *            要填充的属性值
	 * @param IgnoreCase
	 *            查找Bean中对应属性时是否忽略大小写
	 * */
	public void setBeanProperty(Object bean, String propertyName,
			String propertyValue, boolean IgnoreCase) {
		// 利用内省获取指定Java Bean的属性描述信息集合
		PropertyDescriptor[] pds = getBeanPropertyDescriptor(bean);
		// 遍历Bean的所有属性
		for (PropertyDescriptor pd : pds) {
			// 判定给出的属性名字是否存在于当前Bean中
			if (((!IgnoreCase) && pd.getName().equals(propertyName))
					|| (IgnoreCase && pd.getName().equalsIgnoreCase(
							propertyName))) {
				// 如果在Bean中找到了相应的属性，则获取属性对应的Setter方法
				Method setter = pd.getWriteMethod();
				// 获取Setter方法第一个参数的类型说明符（即该属性的类型说明符）
				String typeString = setter.getParameterTypes()[0]
						.getCanonicalName();				
				Object value = propertyValue;
				// 查找属性数据类型对应的类型转换器
				if (convertors.get(typeString) != null) {
					// 如果存在该类型对应的类型转换器，则利用类型转换器将原数据值转换为目标类型
					value = convertors.get(typeString).convertToObject(
							propertyValue);
				}

				try {
					// 利用反射调用属性的Setter方法为属性设值
					setter.invoke(bean, new Object[] { value });
				} catch (Exception ex) {
					// 如果执行不成功，则抛出FillBeanException的自定义业务异常
					throw new FillBeanException(bean.getClass()
							.getCanonicalName(), propertyName);
				}

			}

		}

	}

	/**
	 * 利用对象和对应的转换器填充Bean
	 * 
	 * @param bean
	 *            要填充的对象
	 * @param propertyName
	 *            要填充的属性名
	 * @param propertyValue
	 *            要填充的属性值
	 * @param IgnoreCase
	 *            查找Bean中对应属性时是否忽略大小写
	 * */
	public void setBeanProperty(Object bean, String propertyName,
			Object propertyValue, boolean IgnoreCase) {
		// 利用内省获取指定Java Bean的属性描述信息集合
		PropertyDescriptor[] pds = getBeanPropertyDescriptor(bean);
		// 遍历Bean的所有属性
		for (PropertyDescriptor pd : pds) {
			// 判定给出的属性名字是否存在于当前Bean中
			if (((!IgnoreCase) && pd.getName().equals(propertyName))
					|| (IgnoreCase && pd.getName().equalsIgnoreCase(
							propertyName))) {
				// 如果在Bean中找到了相应的属性，则获取属性对应的Setter方法
				Method setter = pd.getWriteMethod();
				// 获取Setter方法第一个参数的类型说明符（即该属性的类型说明符）
				String typeString = setter.getParameterTypes()[0]
						.getCanonicalName();
				try {
					// 查找属性数据类型对应的类型转换器
					if (convertors.get(typeString) != null) {
						// 如果存在该类型对应的类型转换器，则利用类型转换器将原数据值转换为目标类型
						propertyValue = convertors.get(typeString)
								.convertToObject(propertyValue);
					}
					// 利用反射调用属性的Setter方法为属性设值
					setter.invoke(bean, new Object[] { propertyValue });
				} catch (Exception ex) {
					// 如果执行不成功，则抛出FillBeanException的自定义业务异常
					throw new FillBeanException(bean.getClass()
							.getCanonicalName(), propertyName);
				}
			}
		}
	}

	/**
	 * 利用内省获取JavaBean属性描述的方法
	 * @param bean 要内省的JavaBean对象
	 * @return 返回JavaBean对象的所有属性描述集合
	 * */
	private PropertyDescriptor[] getBeanPropertyDescriptor(Object bean) {
		try {
			// 对实例化参数Bean的类进行内省
			BeanInfo binfo = Introspector.getBeanInfo(bean.getClass());
			// 获取属性描述信息
			PropertyDescriptor[] pds = binfo.getPropertyDescriptors();
			// 返回属性描述信息
			return pds;
		} catch (Exception e) {
			// 如果存在异常，则输出异常信息
			e.printStackTrace();
			// 在发生异常的情况下，返回null
			return null;
			// TODO: handle exception
		}

	}

}
