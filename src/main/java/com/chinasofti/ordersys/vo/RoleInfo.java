package com.chinasofti.ordersys.vo;

import lombok.Data;

/**
 * 角色信息
 *
 * @author leo
 * @date 2021/06/28 10:31
 **/
@Data
public class RoleInfo {

    /**
     * 角色编号 #
     * 自动增长
     */
    private int id;

    /**
     * 角色名称
     */
    private String roleName;

}
