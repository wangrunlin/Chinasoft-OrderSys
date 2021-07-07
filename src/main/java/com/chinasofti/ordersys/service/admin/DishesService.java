/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.admin;

import java.util.ArrayList;

import com.chinasofti.ordersys.vo.DishesInfo;
import com.chinasofti.util.jdbc.template.JDBCTemplateWithDS;

/**
 * <p>
 * Title: DishesService
 * </p>
 * <p>
 * Description: 菜品管理服务对象
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
public class DishesService {
	/**
	 * 分页获取菜品数据的方法
	 * 
	 * @param page
	 *            要获取数据的页号
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前页的菜品数据列表
	 * */
	public ArrayList<DishesInfo> getDishesInfoByPage(int page, int pageSize) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 通过查询语句获取对应页的数据
		ArrayList<DishesInfo> list = helper.preparedForPageList(
				"select * from dishesinfo order by recommend desc,dishesId",
				new Object[] {}, page, pageSize, DishesInfo.class);
		// 返回结果
		return list;

	}

	/**
	 * 获取菜品信息的最大页数
	 * 
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前数据库中数据的最大页数
	 * */
	public int getMaxPage(int pageSize) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 获取最大页数信息
		Long rows = (Long) helper.preparedQueryForObject(
				"select count(*) from dishesinfo", new Object[] {});
		// 返回最大页数
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * 根据菜品ID值删除菜品信息的方法
	 * 
	 * @param dishesId
	 *            要删除的菜品Id
	 * */
	public void deleteDishesById(Integer dishesId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 执行删除操作
		helper.executePreparedUpdate("delete from dishesinfo where dishesId=?",
				new Object[] { dishesId });
	}

	/**
	 * 添加菜品的方法
	 * 
	 * @param info
	 *            需要添加的菜品信息
	 * */
	public void addDishes(DishesInfo info) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 执行添加操作
		helper.executePreparedUpdate(
				"insert into dishesinfo(dishesName,dishesDiscript,dishesTxt,dishesImg,recommend,dishesPrice) values(?,?,?,?,?,?)",
				new Object[] { info.getDishesName(), info.getDishesDiscript(),
						info.getDishesTxt(), info.getDishesImg(),
						new Integer(info.getRecommend()),
						new Float(info.getDishesPrice()) });

	}

	/**
	 * 根据dishesId获取菜品详细信息的方法
	 * 
	 * @param dishesId
	 *            要获取信息的特定菜品Id
	 * @return 返回该id的菜品详细信息
	 * */
	public DishesInfo getDishesById(Integer dishesId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 查询给定id的菜品详细信息
		ArrayList<DishesInfo> list = helper.preparedQueryForList(
				"select * from dishesinfo where dishesId=?",
				new Object[] { dishesId }, DishesInfo.class);
		// 返回查询结果
		return list.get(0);
	}

	/**
	 * 修改菜品信息的方法
	 * 
	 * @param Info
	 *            要修改的菜品信息，其中dishesId为修改依据，其余信息为修改的目标值
	 * */
	public void modifyDishes(DishesInfo info) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 执行修改操作
		helper.executePreparedUpdate(
				"update dishesinfo set dishesName=?,dishesDiscript=?,dishesTxt=?,dishesImg=?,recommend=?,dishesPrice=? where dishesId=?",
				new Object[] { info.getDishesName(), info.getDishesDiscript(),
						info.getDishesTxt(), info.getDishesImg(),
						new Integer(info.getRecommend()),
						new Float(info.getDishesPrice()),
						new Integer(info.getDishesId()) });

	}

	/**
	 * 获取头4条推荐菜品的信息
	 * 
	 * @return 头4条推荐菜品列表
	 * */
	public ArrayList<DishesInfo> getTop4RecommendDishes() {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 查询头4条推荐的菜品信息
		ArrayList<DishesInfo> list = helper.preparedForPageList(
				"select * from dishesinfo where recommend=1 order by dishesId",
				new Object[] {}, 1, 4, DishesInfo.class);

		// ArrayList<DishesInfo> list = helper
		// .preparedQueryForList(
		// "select * from dishesinfo where recommend=1 order by dishesId limit 0,4",
		// new Object[] {}, DishesInfo.class);
		// 返回结果
		return list;

	}

}
