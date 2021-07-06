/**
 *  Copyright 2014 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.web.upload;

/**
 * <p>
 * Title: SubmitInformation
 * </p>
 * <p>
 * Description: 用于测试文件上传的JavaBean类
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
public class SubmitInformation {
	/**
	 * 文字表单域ownerName对应的属性
	 * */
	private String ownerName;
	/**
	 * 文字表单域ownerAge对应的属性
	 * */
	private int ownerAge;
	/**
	 * 文件表单域uploadFile对应的属性
	 * */
	private FormFile uploadFile;

	/**
	 * 属性ownerName的getter方法
	 * 
	 * @return 返回ownerName属性的值
	 * */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * 属性ownerName的setter方法
	 * 
	 * @param ownerName
	 *            属性的新值
	 * */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * 属性ownerAgeName的getter方法
	 * 
	 * @return 返回ownerAge属性的值
	 * */
	public int getOwnerAge() {
		return ownerAge;
	}

	/**
	 * 属性ownerAge的setter方法
	 * 
	 * @param ownerAge
	 *            属性的新值
	 * */
	public void setOwnerAge(int ownerAge) {
		this.ownerAge = ownerAge;
	}

	/**
	 * 属性uploadFile的getter方法
	 * 
	 * @return 返回uploadFile属性的值
	 * */
	public FormFile getUploadFile() {
		return uploadFile;
	}

	/**
	 * 属性uploadFile的setter方法
	 * 
	 * @param uploadFile
	 *            属性的新值
	 * */
	public void setUploadFile(FormFile uploadFile) {
		this.uploadFile = uploadFile;
	}

}
