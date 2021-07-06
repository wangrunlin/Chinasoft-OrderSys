/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.upload;

/**
 * <p>
 * Title: IUploadInfo
 * </p>
 * <p>
 * Description: 保存发布或获取进度信息时使用的内建字符串
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
public interface IUploadInfo {

	/**
	 * 表示当前设置或获取的为实际进度信息的session键
	 * */
	public String UPLOAD_PROGRESS = "ICSS_UTIL_WEB_UPLOAD_PROGRESS";
	/**
	 * 表示当前状态为上传中的session值
	 * */
	public String UPLOAD_STATE_UPLOADING = "ICSS_UTIL_WEB_UPLOAD_UPLOADING";
	/**
	 * 表示当前状态为正在将磁盘缓存内容保存到实际目标文件的session值
	 * */
	public String UPLOAD_STATE_SAVE = "ICSS_UTIL_WEB_UPLOAD_SAVE";
	/**
	 * 表示当前设置或获取的为状态信息的session键
	 * */
	public String UPLOAD_STATE = "ICSS_UTIL_WEB_UPLOAD_STATE";

	/**
	 * 表示当前设置或获取的为正在上传的文件名信息的session值
	 * */
	public String UPLOAD_UPLOADING_FILENAME = "ICSS_UTIL_WEB_UPLOAD_UPLOADING_FILENAME";
	/**
	 * 表示当前设置或获取的为正在保存的文件名信息的session值
	 * */
	public String UPLOAD_SAVING_FILENAME = "ICSS_UTIL_WEB_UPLOAD_SAVING_FILENAME";

}
