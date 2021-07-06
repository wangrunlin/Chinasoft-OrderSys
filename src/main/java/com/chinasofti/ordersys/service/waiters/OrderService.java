/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.waiters;

import java.util.ArrayList;
import java.util.Date;

import com.chinasofti.ordersys.vo.Cart;
import com.chinasofti.ordersys.vo.OrderInfo;
import com.chinasofti.util.jdbc.template.JDBCTemplateWithDS;

/**
 * <p>
 * Title: OrderService
 * </p>
 * <p>
 * Description: 订单管理服务对象
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
public class OrderService {

	/**
	 * 增加订单的放发
	 * 
	 * @param waiterId
	 *            订单点餐的服务员id
	 * @param tableId
	 *            订单对应的桌号
	 * @return 添加成功的订单对应的主键值(Long型)
	 * */
	public Object addOrder(int waiterId, int tableId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 获取添加订单时的时间
		Date now = new Date();
		// 执行订单插入操作并获取本次插入操作成功后自动获取的主键值
		Object[] key = helper
				.preparedInsertForGeneratedKeys(
						"insert into orderinfo(orderBeginDate,waiterId,tableId) values(?,?,?)",
						new Object[] { now, new Integer(waiterId),
								new Integer(tableId) });
		// 由于订单表只有单列主键，因此将第一个生成的主键值返回
		return key[0];
	}

	/**
	 * 添加订单菜品详细信息的方法
	 * 
	 * @param unit
	 *            订单菜品详情
	 * @param key
	 *            对应的订单Id
	 * */
	public void addOrderDishesMap(Cart.CartUnit unit, Object key) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 执行订单菜品详情插入操作
		helper.executePreparedUpdate(
				"insert into orderdishes(orderReference,dishes,num) values(?,?,?)",
				new Object[] { key, new Integer(unit.getDishesId()),
						new Integer(unit.getNum()) });

	}

	/**
	 * 以分页方式获取不同支付状态订单信息的方法
	 * 
	 * @param page
	 *            需要获取的页码数
	 * @param pageSize
	 *            每页显示的条目数
	 * @param state
	 *            需要查询的支付状态信息
	 * @return 查询结果列表
	 * */
	public ArrayList<OrderInfo> getNeedPayOrdersByPage(int page, int pageSize,
			int state) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// ArrayList<OrderInfo> list = helper
		// .preparedForPageList(
		// "select orderId,orderBeginDate,orderEndDate,waiterId,orderState,dishes,num from orderinfo,orderdishes where orderinfo.orderId=orderdishes.orderReference and orderinfo.orderState=0",
		// new Object[] {}, page, pageSize, OrderInfo.class);

		// 进行查询操作
		ArrayList<OrderInfo> list = helper
				.preparedForPageList(
						"select * from orderinfo,userInfo where orderState=? and userInfo.userId=orderinfo.waiterId",
						new Object[] { new Integer(state) }, page, pageSize,
						OrderInfo.class);
		// 返回查询的结果
		return list;

	}

	/**
	 * 获取特定支付状态订单的总页数
	 * 
	 * @param pageSize
	 *            每页显示的条目数
	 * @param state
	 *            订单支付状态
	 * @return 总页数
	 * */
	public int getMaxPage(int pageSize, int state) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 查询符合条件的总条目数
		Long rows = (Long) helper.preparedQueryForObject(
				"select count(*) from orderinfo where orderState=?",
				new Object[] { new Integer(state) });
		// 计算总页数并返回
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * 获取不同支付状态订单信息的方法
	 * 
	 * @param state
	 *            需要查询的支付状态信息
	 * @return 订单信息集合
	 */
	public ArrayList<OrderInfo> getNeedPayOrders(int state) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 执行查询
		ArrayList<OrderInfo> list = helper
				.preparedQueryForList(
						"select * from orderinfo,userInfo where orderState=? and userInfo.userId=orderinfo.waiterId",
						new Object[] { new Integer(state) }, OrderInfo.class);
		// 返回查询结果
		return list;

	}

	/**
	 * 请求支付订单的方法
	 * 
	 * @param orderId
	 *            请求支付订单的订单号
	 */
	public void requestPay(Integer orderId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 获取结单时间
		Date now = new Date();
		// 将该订单的状态设置为准备结账
		helper.executePreparedUpdate(
				"update orderinfo set orderState=1,orderEndDate=? where orderId=?",
				new Object[] { now, new Integer(orderId) });

	}

	/**
	 * 根据订单号获取订单详情的方法
	 * 
	 * @param orderId
	 *            需要获取详情的订单号
	 * @return 查询到的订单详细信息
	 * */
	public OrderInfo getOrderById(Integer orderId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 执行查询并返回结果
		return helper
				.preparedQueryForList(
						"select * from orderinfo,userinfo where orderId=? and orderinfo.waiterId=userinfo.userId",
						new Object[] { orderId }, OrderInfo.class).get(0);

	}

	/**
	 * 获取单一订单的总价
	 * 
	 * @param 要获取总价的订单号
	 * @return 查询到的总价
	 * */
	public float getSumPriceByOrderId(Integer orderId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 查询总价
		Double sum = (Double) helper
				.preparedQueryForObject(
						"SELECT SUM(d.dishesPrice*od.num) FROM orderinfo a,dishesinfo d,orderdishes od WHERE a.orderId=od.orderReference AND od.dishes=d.dishesId AND a.orderId=?",
						new Object[] { orderId });
		// 返回总价
		return sum.floatValue();
	}

	/**
	 * 根据订单号获取订单详情
	 * 
	 * @param 要获取详情的订单号
	 * @return 订单详情列表
	 * */
	public ArrayList<OrderInfo> getOrderDetailById(Integer orderId) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 查询并返回订单详情列表
		return helper
				.preparedQueryForList(
						"SELECT * FROM orderinfo o,userinfo u,orderdishes od,dishesinfo d WHERE orderId=? AND o.waiterId=u.userId AND od.orderReference=o.orderId AND d.dishesId=od.dishes",
						new Object[] { orderId }, OrderInfo.class);

	}

	/**
	 * 修改订单支付状态的方法
	 * 
	 * @param orderId
	 *            要修改状态的订单号
	 * @param state
	 *            目标状态值
	 * */
	public void changeState(Integer orderId, int state) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 执行订单状态更新操作
		helper.executePreparedUpdate(
				"update orderinfo set orderState=? where orderId=?",
				new Object[] { new Integer(state), orderId });

	}

	/**
	 * 根据结单时间段查询订单信息的方法
	 * 
	 * @param beginDate
	 *            查询的开始时间
	 * @param endDate
	 *            查询的结束时间
	 * @return 结单时间在开始时间和结束时间之间的所有订单列表
	 * */
	public ArrayList<OrderInfo> getOrderInfoBetweenDate(Date beginDate,
			Date endDate) {
		// 获取带有连接池的数据库模版操作工具对象
		JDBCTemplateWithDS helper = JDBCTemplateWithDS.getJDBCHelper();
		// 根据结单时间段查询订单信息
		ArrayList<OrderInfo> list = helper
				.preparedQueryForList(
						"select * from orderinfo,userInfo where orderState=2 and userInfo.userId=orderinfo.waiterId and orderinfo.orderEndDate between ? and ?",
						new Object[] { beginDate, endDate }, OrderInfo.class);
		// 返回结果
		return list;
	}

}
