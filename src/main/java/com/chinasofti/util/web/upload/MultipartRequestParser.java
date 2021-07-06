/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.upload;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.chinasofti.util.bean.BeanUtil;
import com.chinasofti.util.bean.convertor.TypeConvertor;

/**
 * <p>
 * Title: MultipartRequestParser
 * </p>
 * <p>
 * Description: 带有上传文件内容的Http请求体解析器
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
public class MultipartRequestParser {

	/**
	 * 设置Bean属性值得工具，在本类中的主要作用是获取常用数据类型的转换器
	 * */
	private BeanUtil beanUtil = new BeanUtil();
	/**
	 * 用于存放HTTP请求中的文本类型参数的散列表
	 * */
	private Hashtable<String, String[]> txtParameters = new Hashtable<String, String[]>();
	/**
	 * 用于存放HTTP请求中文件类型参数的散列表
	 * */
	private Hashtable<String, FormFile[]> fileParameters = new Hashtable<String, FormFile[]>();

	/**
	 * 请求解析方法
	 * 
	 * @param request
	 *            http请求对象
	 * @param bean
	 *            需要利用http请求中的参数自动填充的javabean,要求bean中的属性拥有对应的setter方法，
	 *            如果存在一个属性名对应多个属性值得情况，bean中应该定义数组
	 * @return 返回填充完毕的javaBean对象
	 * */
	public Object parse(HttpServletRequest request, Object bean) {

		try {
			publishProgress(request, 0);
			// 获取session回话对象
			HttpSession session = request.getSession(true);
			// 获取请求头和请求体中存在的"参数名=参数值"形式存在的参数名称集合
			Enumeration<String> parameterNames = request.getParameterNames();
			// 遍历参数名集合
			while (parameterNames.hasMoreElements()) {
				// 获取到其中一个参数名
				String parameterName = parameterNames.nextElement();
				// 获取该参数名对应的参数值集合
				String[] values = request.getParameterValues(parameterName);
				// 将参数名和对应的值集合存入散列表中
				txtParameters.put(parameterName, values);
			}
			// 获取http请求的mime类型
			String contentType = request.getContentType();
			// 利用http请求mime类型判定是否上传了文件
			if ("post".equalsIgnoreCase(request.getMethod())
					&& contentType.toLowerCase().startsWith(
							"multipart/form-data;")) {

				// 获取表单项分隔符的内容
				String boundary = contentType.substring(contentType
						.indexOf("boundary=") + 9);
				// 获取请求对应的输入流
				ServletInputStream sis = request.getInputStream();
				// 获取请求的长度，即所有表单项内容+描述信息的合计大小
				int length = request.getContentLength();
				// 在回话中存入表示开始上传的状态信息
				session.setAttribute(IUploadInfo.UPLOAD_STATE,
						IUploadInfo.UPLOAD_STATE_UPLOADING);
				// 创建读取数据的缓冲区
				byte[] buf = new byte[4096];
				// 读取字节计数器
				int c = 0;
				// 总共读取字节数的计数器
				int readCounter = 0;
				// 循环读取
				while ((c = sis.readLine(buf, 0, buf.length)) != -1) {
					// 累加读取字节数
					readCounter += c;
					// 将读取的数据转换为字符串
					String msg = new String(buf, 0, c, "utf-8");
					// 如果读取到的数据中包含"filename="""字符串时说明读取到了表单域描述信息
					if (msg.indexOf("filename=\"") != -1) {
						// 创建一个上传文件对象
						FormFile fileParameter = new FormFile();
						// 定义获取文件名的变量
						String fileName = "";
						// 获取上传的文件名
						fileName = msg
								.substring(msg.indexOf("filename=\"") + 10);
						fileName = fileName
								.substring(0, fileName.indexOf("\""));
						fileName = fileName.substring(fileName
								.lastIndexOf("\\") + 1);

						// 设置上传文件对象的文件名信息
						fileParameter.setFileName(fileName);
						// 获取表单域的名称
						String name = msg.substring(msg.indexOf("name=\"") + 6);
						name = name.substring(0, name.indexOf("\""));
						// 读取文件域的MIME信息行
						c = sis.readLine(buf, 0, buf.length);
						// 累加读取计数器
						readCounter += c;
						// 将MIME行数据转换为字符串
						String s = new String(buf, 0, c);
						// 获取文件的MIME类型
						String contextType = s.substring(s.indexOf(":") + 1)
								.trim();
						// 设置文件上传对象的mime类型信息
						fileParameter.setContextType(contextType);
						// 读取MIME信息后的一个空行
						c = sis.readLine(buf, 0, buf.length);
						// 累加计数器
						readCounter += c;
						// 获取当前WEB项目中的temp文件夹用于保存上传的临时文件
						String path = request.getSession().getServletContext()
								.getRealPath("/WEB-INF/temp");
						
						
						//如果临时文件夹不存在则创建一个新的临时文件夹
						File testPath=new File(path);
						if(!testPath.exists()){
							testPath.mkdir();
						}
						
						
						
						// 创建临时文件对象
						File tempFile = File.createTempFile("httpupload",
								".uploadtemp", new File(path));
						// 创建针对临时文件的输出流
						FileOutputStream fos = new FileOutputStream(tempFile);
						// 循环读取文件与表单中的数据
						while ((c = sis.readLine(buf, 0, buf.length)) != -1) {
							// 累加计数器
							readCounter += c;
							// 发布上传进度信息
							publishProgress(request, length, readCounter);
							// 将读取到的数据转换为字符串
							msg = new String(buf, 0, c);
							// 判定是否为表单域结束或整个输入流的结束，如果是，则跳出循环执行
							if (msg.trim().equals("--" + boundary)
									|| msg.trim()
											.equals("--" + boundary + "--")) {
								break;
							}
							// 如果不是表单域或整个请求的结束，将读取到的数据拷贝到临时文件中
							fos.write(buf, 0, c);
						}
						// 刷新输出流
						fos.flush();
						// 关闭文件输出流
						fos.close();
						// 设置上传文件的临时文件信息
						fileParameter.setTempFile(tempFile);
						// 将上传的文件加入到文件参数列表中
						addFileParameter(name, fileParameter);
						// 如果没有filename而只有name说明读取到普通的文本表单域了
					} else if (msg.indexOf("name=\"") != -1) {
						// 获取表单域的名称
						String name = msg.substring(msg.indexOf("name=\"") + 6);
						name = name.substring(0, name.indexOf("\""));
						// 读取表单域说明后的一个空行
						c = sis.readLine(buf, 0, buf.length);
						// 累加计数器
						readCounter += c;
						// 创建变量保存表单域数据
						String value = "";
						// 循环读取表单域的数据
						while ((c = sis.readLine(buf, 0, buf.length)) != -1) {
							// 累加读取计数器
							readCounter += c;
							// 将读取到的数据转换为字符串
							String s = new String(buf, 0, c, "utf-8");
							// 判定是否为表单域或整个请求的末尾，如果是则跳出循环执行
							if (s.trim().equals("--" + boundary)
									|| s.trim().equals("--" + boundary + "--")) {
								break;
							}
							// 将本次读取到的数据串联到内容变量中
							value += s;
						}
						// 获取结果字符串的byte数组形式
						byte[] valueByte = value.getBytes();
						// 忽略字符串末尾的2个字节的http结束控制字符
						addTxtParameter(name, new String(valueByte, 0,
								valueByte.length - 2));
						// 发布进度信息
						publishProgress(request, length, readCounter);
						// 如果遇到请求结尾，则跳出循环执行
					} else if (msg.trim().equals("--" + boundary + "--")) {
						break;
					}
				}
				// 读取结束后将进度信息发布为100%
				publishProgress(request, 100);
				
			}
			// 利用读取到的数据填充javaBean
			fillBean(bean);
		} catch (Exception e) {
			// 如果遇到异常则输出异常信息
			e.printStackTrace();
		}
		// 将填充后的javaBean对象返回
		return bean;
	}

	/**
	 * 填充JavaBean的方法
	 * 
	 * @param bean
	 *            需要填充的JavaBean对象
	 * */
	private void fillBean(Object bean) {

		try {
			// 利用内省获取Bean的描述信息
			BeanInfo info = Introspector.getBeanInfo(bean.getClass());
			// 获取Bean中所有属性的描述
			PropertyDescriptor[] pds = info.getPropertyDescriptors();
			// 获取存放文本表单域数据散列表的所有键
			Enumeration<String> txtKeys = txtParameters.keys();
			// 遍历所有的文本表单域
			while (txtKeys.hasMoreElements()) {
				// 获取一个特定的表单域名
				String key = txtKeys.nextElement();
				// 获取表单域名对应的值集合
				String[] values = txtParameters.get(key);
				// 遍历JavaBean的所有属性描述
				for (PropertyDescriptor pd : pds) {
					// 如果在Bean中找到和表单域名同名的属性，则执行填充操作
					if (pd.getName().equals(key)) {
						// 获取该属性的setter方法
						Method setMethod = pd.getWriteMethod();
						// 获取setter方法的第一个参数类型（setter方法只有一个参数）
						Class argType = setMethod.getParameterTypes()[0];
						// 如果Bean中定义的属性不为数组
						if (!argType.isArray()) {
							// 取出保存的第一个值
							String value = values[0];
							// 定义执行方法的参数对象
							Object objValue = value;
							// 获取数据转换器
							TypeConvertor convertor = beanUtil.getConvertors()
									.get(argType.getCanonicalName());
							// 如果转换器不会空，利用对应数据类型的转换器将字符串转化为目标类型
							if (convertor != null) {
								objValue = convertor.convertToObject(value);
							}
							// 利用反射调用setter方法
							setMethod.invoke(bean, new Object[] { objValue });
						} else {
							// 如果是数组，则创建和原始数组元素个数相同的参数数组
							Object[] objValues = new Object[values.length];
							// 获取数组中每个元素的类型
							Class elementType = argType.getComponentType();
							// 获取数组元素的类型转换器
							TypeConvertor convertor = beanUtil.getConvertors()
									.get(elementType.getCanonicalName());
							// 循环遍历数据数组
							for (int i = 0; i < objValues.length; i++) {
								// 如果转换器不为空，则将每一个数据转换为目标类型
								if (convertor != null) {
									objValues[i] = convertor
											.convertToObject(values[i]);
									// 如果转换器为空，则不执行转换操作
								} else {
									objValues[i] = values[i];
								}
							}
							// 执行setter方法
							setMethod.invoke(bean, new Object[] { objValues });
						}
					}
				}
			}
			// 获取所有文件表单域名称
			Enumeration<String> fileKeys = fileParameters.keys();
			// 遍历文件表单域名称
			while (fileKeys.hasMoreElements()) {
				// 获取单个文件表单域名称
				String key = fileKeys.nextElement();
				// 获取文件域表单名称对应的数据
				FormFile[] files = fileParameters.get(key);
				// 循环遍历bean的所有属性
				for (PropertyDescriptor pd : pds) {
					// 如果找到和文件域表单名称同名的属性则执行相应的setter方法
					if (pd.getName().equals(key)) {
						// 获取setter方法
						Method setMethod = pd.getWriteMethod();
						// 获取sertter方法的参数类型
						Class argType = setMethod.getParameterTypes()[0];
						// 如果setter方法的参数是数组，则直接利用原始数据数组作为参数调用setter方法
						if (argType.isArray()) {
							setMethod.invoke(bean, new Object[] { files });
							// 如果setter方法的参数不是数组，则利用原始数据数组的第一个元素作为参数调用setter方法
						} else {
							setMethod.invoke(bean, files[0]);
						}
					}
				}
			}

		} catch (Exception ex) {
			// 如果发生异常则输出异常信息
			ex.printStackTrace();
		}

	}

	/**
	 * 添加一个特定名字的文本参数
	 * 
	 * @param pname
	 *            参数名称
	 * @param pvalue
	 *            参数值
	 * */
	private void addTxtParameter(String pname, String pvalue) {

		// 判定是否存在同名的参数
		if (txtParameters.containsKey(pname)) {
			// 获取参数对应的原始数据数组
			String[] values = txtParameters.get(pname);
			// 扩展数据数组，将元素个数增加1
			String[] newValues = Arrays.copyOf(values, values.length + 1);
			// 将新数据存入新数组的最后一个元素
			newValues[values.length] = pvalue;
			// 将新数组作为参数对应的数据存入散列表
			txtParameters.put(pname, newValues);
		} else {
			// 如果原来不存在同名的参数，向散列表中存入新的数组
			txtParameters.put(pname, new String[] { pvalue });
		}

	}

	/**
	 * 添加一个特定名字的文件参数
	 * 
	 * @param pname
	 *            参数名称
	 * @param flie
	 *            参数值
	 * */
	private void addFileParameter(String pname, FormFile file) {
		// 判定是否存在同名的参数
		if (fileParameters.containsKey(pname)) {
			// 获取参数对应的原始数据数组
			FormFile[] values = fileParameters.get(pname);
			// 扩展数据数组，将元素个数增加1
			FormFile[] newValues = Arrays.copyOf(values, values.length + 1);
			// 将新数据存入新数组的最后一个元素
			newValues[values.length] = file;
			// 将新数组作为参数对应的数据存入散列表
			fileParameters.put(pname, newValues);
		} else {
			// 如果原来不存在同名的参数，向散列表中存入新的数组
			fileParameters.put(pname, new FormFile[] { file });
		}
	}

	/**
	 * 计算当前上传进度信息的方法
	 * 
	 * @param contentLength
	 *            请求体的长度
	 * @param readCounter
	 *            当前读取的字节数
	 * @return 返回计算得到的进度值
	 * */
	private Integer getProgress(int contentLength, int readCounter) {
		// 返回进度值，进度至为0-100，有两点需要注意：1、由于是整数除法，因此应该能先乘以100再除以总长度，2、乘以100以后可能会超出int值表示范围，因此计算时先将运算数转化为long型数据
		return new Integer((int) (((long) readCounter) * 100 / contentLength));
	}

	/**
	 * 向回话中发布进度信息的方法
	 * 
	 * @param request
	 *            http请求信息
	 * @param contentLength
	 *            请求体的长度
	 * @param readCounter
	 *            当前读取的字节数
	 * */
	private void publishProgress(HttpServletRequest request, int contentLength,
			int readCounter) {
		// 根据提供的http请求总长度和当前读取的字节数计算进度后存入会话对象中
		request.getSession(true).setAttribute(IUploadInfo.UPLOAD_PROGRESS,
				getProgress(contentLength, readCounter));
	}

	/**
	 * 向回话中发布进度信息的方法
	 * 
	 * @param request
	 *            http请求信息
	 * @param progress
	 *            需要发布的进度值，值为0-100
	 * */
	private void publishProgress(HttpServletRequest request, int progress) {
		// 在会话对象中保存进度值
		request.getSession(true).setAttribute(IUploadInfo.UPLOAD_PROGRESS,
				new Integer(progress));
	}

	/**
	 * 请求解析方法
	 * 
	 * @param request
	 *            http请求对象
	 * @param beanClass
	 *            需要利用http请求中的参数自动填充的javabean对应的Class对象,要求bean中的属性拥有对应的setter方法，
	 *            如果存在一个属性名对应多个属性值得情况，bean中应该定义数组
	 * @return 返回填充完毕的javaBean对象
	 * @see #parse(HttpServletRequest, Object)
	 * */
	public Object parse(HttpServletRequest request, Class beanClass) {
		try {
			// 构建Bean对象
			Object bean = beanClass.newInstance();
			// 调用本类的其他解析方法
			return parse(request, bean);
		} catch (Exception e) {
			// 如果存在异常则输出异常信息
			e.printStackTrace();
			// 存在异常的情况下直接返回null
			return null;
		}

	}

	/**
	 * 请求解析方法
	 * 
	 * @param request
	 *            http请求对象
	 * @param beanClassName
	 *            需要利用http请求中的参数自动填充的javabean对应的全限定类名,要求bean中的属性拥有对应的setter方法，
	 *            如果存在一个属性名对应多个属性值得情况，bean中应该定义数组
	 * @return 返回填充完毕的javaBean对象
	 * @see #parse(HttpServletRequest, Class)
	 * */
	public Object parse(HttpServletRequest request, String beanClassName) {
		try {
			// 加载给定的java类
			Class cls = Class.forName(beanClassName);
			// 调用本类的其他解析方法
			return parse(request, cls);
		} catch (Exception e) {
			// 如果存在异常则输出异常信息
			e.printStackTrace();
			// 存在异常的情况下直接返回null
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * 获取进度信息的方法
	 * 
	 * @param request
	 *            http请求对象
	 * @return 返回获取到的进度值，进度值为0-100
	 * */
	public static int getUploadProgress(HttpServletRequest request) {
		try {
			// 如果回话对象中存在进度信息，则将其转化为int值后返回
			return ((Integer) request.getSession(true).getAttribute(
					IUploadInfo.UPLOAD_PROGRESS)).intValue();
		} catch (Exception e) {
			// 如果回话对象中不存在进度信息，则返回0
			return 0;
		}
	}

}
