package com.chinasofti.ordersys.vo;

import lombok.Data;

/**
 * 用户信息
 *
 * @author leo
 * @date 2021/06/28 10:35
 **/
@Data
public class UserInfo {

    /**
     * 用户编号 #
     * 自动增长
     */
    private int id;

    /**
     * 用户登陆时使用的帐号名称
     */
    private String username;

    /**
     * 用户登陆时使用的密码
     */
    private String pwd;

    /**
     * 用户的角色 ID $
     * role_info.id
     */
    private int role_id;

    /**
     * 用户是否被锁定
     * 0-未锁定
     * 1-锁定
     */
    private int locked;

    /**
     * 用户头像图片名
     */
    private String faceimg = "default.jpg";

}
