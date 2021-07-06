/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Title: FormFile
 * </p>
 * <p>
 * Description: 用于在Servlet文件上传过程中临时保存文件信息的工具类
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
public class FormFile {

	/**
	 * 上传文件的文件名
	 * */
	private String fileName;
	/**
	 * 上传文件的MIME类型
	 * */
	private String contextType;
	/**
	 * 缓存的临时文件，该文件末尾包含2个字节的HTTP结尾控制字符，在实际拷贝到目标路径时需要跳过这两个字节
	 * */
	private File tempFile;

	/**
	 * 获取当前上传文件的文件名的方法
	 * 
	 * @return 返回该对象对应的上传文件的文件名
	 * */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 设置上传 文件文件名的方法,该方法仅供上传工具内部调用
	 * 
	 * @param fileName
	 *            在Http请求体中获取的文件名
	 * */
	void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取当前文件MIME类型的方法
	 * 
	 * @return 返回该对象对应文件的MIME类型的字符串描述
	 * */
	public String getContextType() {
		return contextType;
	}

	/**
	 * 设置上传 文件文件MIME类型的方法,该方法仅供上传工具内部调用
	 * 
	 * @param contextType
	 *            从Http请求体中获取的文件MIME类型
	 * */
	void setContextType(String contextType) {
		this.contextType = contextType;
	}

	/**
	 * 获取上传文件磁盘缓存临时文件的方法
	 * 
	 * @return 返回上传内容保存在磁盘上的临时文件
	 * */
	File getTempFile() {
		return tempFile;
	}

	/**
	 * 设置上传 文件临时缓存的方法,该方法仅供上传工具内部调用
	 * 
	 * @param tempFile
	 *            从Http请求体中获取的文件内容
	 * */
	void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	/**
	 * 将上传的文件保存到实际路径的方法
	 * 
	 * @param request
	 *            当前的请求对象，用于在session中添加状态信息,如果不需要状态信息，可以直接传递null
	 * @param path
	 *            文件最终存储的实际路径(包含文件名)
	 * */
	public void saveToFileSystem(HttpServletRequest request, String path) {
		try {
			// 判定request对象是否为空
			if (request != null) {
				// 获取session回话对象
				HttpSession session = request.getSession(true);
				// 将session中的状态信息设定为“正在保存”状态
				session.setAttribute(IUploadInfo.UPLOAD_STATE,
						IUploadInfo.UPLOAD_STATE_SAVE);
				// 将session中正在保存的文件名属性设置为当前文件的文件名
				session.setAttribute(IUploadInfo.UPLOAD_SAVING_FILENAME,
						fileName);
			}
			// 利用给出的路径创建目标文件对象
			File distFile = new File(path);
			// 获取临时文件的输入流
			FileInputStream fis = new FileInputStream(tempFile);
			// 获取目标文件的输出流
			FileOutputStream fos = new FileOutputStream(distFile);
			// 获取文件输入管道
			FileChannel inChannel = fis.getChannel();
			// 获取文件输出管道
			FileChannel outChannel = fos.getChannel();
			// 将输入管道中的数据（原临时文件中的数据）传输到输出管道（目标文件）中,由于临时文件中的内容最末尾为2个字节的HTTP的结束控制字符，因此拷贝时要将最后两个字符忽略
			inChannel.transferTo(0, inChannel.size() - 2, outChannel);
			// 关闭输入管道
			inChannel.close();
			// 关闭输出管道
			outChannel.close();
			// 关闭临时文件的输入流
			fis.close();
			// 关闭目标文件的输出流
			fos.close();
			// 删除原有临时文件
			tempFile.delete();

		} catch (Exception ex) {
			// 如果遇到异常则输出异常信息
			ex.printStackTrace();
		}
	}

}
