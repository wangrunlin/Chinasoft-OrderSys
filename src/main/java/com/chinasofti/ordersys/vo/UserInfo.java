/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.vo;

/**
 * <p>
 * Title:UserInfo
 * </p>
 * <p>
 * Description: 用户信息VO
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
public class UserInfo {
	/**
	 * 用户ID
	 * */
	private int userId;
	/**
	 * 用户账户
	 * */
	private String userAccount;
	/**
	 * 用户密码
	 * */
	private String userPass;
	/**
	 * 用户角色ID
	 * */
	private int roleId;
	/**
	 * 用户角色名
	 * */
	private String roleName;
	/**
	 * 用户是否被锁定的标识
	 * */
	private int locked;
	/**
	 * 用户头像路径
	 * */
	private String faceimg = "default.jpg";

	public String getFaceimg() {
		return faceimg;
	}

	public void setFaceimg(String faceimg) {
		this.faceimg = faceimg;
	}

	public int getLocked() {
		return locked;
	}

	public void setLocked(int locked) {

		this.locked = locked;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
