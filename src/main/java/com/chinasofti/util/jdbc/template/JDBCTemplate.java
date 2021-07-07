/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.jdbc.template;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static java.sql.Types.*;

import com.chinasofti.util.bean.BeanUtil;
import com.chinasofti.util.jdbc.template.specialsqloperation.MySQLSpecialOperation;
import com.chinasofti.util.jdbc.template.specialsqloperation.OracleSpecialOperation;

import com.chinasofti.util.jdbc.template.specialsqloperation.SpecialSQLOperation;

/**
 * <p>
 * Title: JDBCTemplate
 * </p>
 * <p>
 * Description: JDBC模板模式封装主类
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
public class JDBCTemplate {

	/**
	 * 存放不同数据库产品的特殊处理对象映射集合，本案例中映射键为JDBC驱动名，特殊处理主要针对数据库查询分页
	 * */
	private static Hashtable<String, Class<? extends SpecialSQLOperation>> driverDBMSMapping = new Hashtable<String, Class<? extends SpecialSQLOperation>>();

	/**
	 * 向工具中添加特定数据库产品对应特殊处理对象的方法
	 * 
	 * @param driverName
	 *            数据库JDBC驱动类名
	 * @param operationClass
	 *            对应的特殊处理类
	 * */
	public static void addDriverDBMSMapping(String driverName,
			Class<? extends SpecialSQLOperation> operationClass) {

		driverDBMSMapping.put(driverName, operationClass);
	}

	/**
	 * 创建利用反射和内省自动填充实体Bean的工具
	 * */
	private BeanUtil beanUtil = new BeanUtil();

	/**
	 * 用于保存JDBC连接时注册的驱动名
	 * */
	private String driverName;

	/**
	 * 用于保存JDBC连接时使用的连接字符串
	 * */
	private String connectionString;

	/**
	 * 用于保存连接数据库时进行验证的数据库管理系统登陆用户名
	 * */
	private String dbmsUserName;

	/**
	 * 用于保存连接数据库时进行验证的数据库管理系统登陆密码
	 * */
	private String dbmsPassword;

	/**
	 * 设置JDBC驱动名称
	 * 
	 * @param driverName
	 *            提供给连接工具的JDBC驱动名
	 * */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * 设置JDBC连接字符串
	 * 
	 * @param connectionString
	 *            提供给连接工具的数据库连接字符串
	 * */
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	/**
	 * 设置数据库登陆用户名
	 * 
	 * @param dbmsUserName
	 *            数据库登陆名
	 * */
	public void setDbmsUserName(String dbmsUserName) {
		this.dbmsUserName = dbmsUserName;
	}

	/**
	 * 设置数据库登陆密码
	 * 
	 * @param dbmsPassword
	 *            数据库登陆密码
	 * */
	public void setDbmsPassword(String dbmsPassword) {
		this.dbmsPassword = dbmsPassword;
	}

	/**
	 * 获取数据库连接的方法
	 * 
	 * @return 返回针对特定数据库所创建的数据库连接对象，如果JDBC属性错误不能创建连接，则返回null
	 * */
	public Connection getConnection() {

		try {
			// 将驱动类加载进当前的类加载器，实现注册驱动的步骤
			// FIXME: 2021/7/6 delete this
			Class.forName("com.mysql.cj.jdbc.Driver");
//			Class.forName(driverName);
			// 根据提供的连接字符串与数据库登陆账号创建JDBC数据库连接
			Connection con = DriverManager.getConnection(connectionString,
					dbmsUserName, dbmsPassword);

			// 返回连接对象
			return con;
		} catch (Exception e) {
			// 输出异常信息
			e.printStackTrace();
			// 如果在创建连接的过程中发生异常，如账户错误，则返回nullsss
			return null;
		}

	}

	/**
	 * 构造方法，根据提供的参数创建模板模式数据库操作对象
	 * 
	 * @param driverName
	 *            JDBC驱动名称
	 * @param connectionString
	 *            JDBC连接字符串
	 * @param dbmsUserName
	 *            数据库用户名
	 * @param dbmsPassword
	 *            数据库登陆密码
	 * */
	JDBCTemplate(String driverName, String connectionString,
			String dbmsUserName, String dbmsPassword) {
		super();
		// 初始化驱动名
		this.driverName = driverName;
		// 初始化连接字符串
		this.connectionString = connectionString;
		// 初始化用户名
		this.dbmsUserName = dbmsUserName;
		// 初始化密码
		this.dbmsPassword = dbmsPassword;
		// 添加MYSQL的特殊操作对象
		addDriverDBMSMapping("com.mysql.jdbc.Driver",
				MySQLSpecialOperation.class);
		// 添加Oracle的特殊操作对象
		addDriverDBMSMapping("oracle.jdbc.dirver.OracleDriver",
				OracleSpecialOperation.class);

	}

	/**
	 * 利用普通语句对象执行SQL语句的方法
	 * 
	 * @param callback
	 *            JDBC回调对象
	 * @return JDBC执行返回结果
	 * */
	public Object execute(JDBCCallback callback) {
		// 定义返回值对象
		Object result = null;
		Connection con = null;
		Statement statement = null;
		try {
			// 获取数据库连接
			con = getConnection();
			// 获取非预编译的语句对象
			statement = con.createStatement();
			// 执行回调方法
			result = callback.doWithStatement(statement);

		} catch (Exception e) {
			// 如果有异常则输出异常信息
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			// 关闭语句对象释放资源
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 关闭连接对象释放资源
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// 返回最终结果
		return result;
	}

	/**
	 * 利用预编译语句对象执行SQL语句的方法
	 * 
	 * @param callback
	 *            JDBC回调对象
	 * @return JDBC执行返回结果
	 * */
	public Object execute(String preparedSQL, JDBCCallback callback) {
		// 定义返回值对象
		Object result = null;
		Connection con = null;
		PreparedStatement statement = null;
		try {
			// 获取数据库连接
			con = getConnection();
			// 利用提供的参数创建预编译语句对象

			statement = con.prepareStatement(preparedSQL,
					Statement.RETURN_GENERATED_KEYS);

			// 利用回调对象执行SQL语句并获取返回值
			result = callback.doWithStatement(statement);
			// 关闭语句对象
			statement.close();
			// 关闭连接对象
			con.close();
		} catch (Exception e) {

			// 如果有异常则输出异常信息
			e.printStackTrace();
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		// 返回执行后的结果
		return result;
	}

	/**
	 * 利用普通语句对象执行单次数据更新操作的方法
	 * 
	 * @param sql
	 *            需要执行的数据更新SQL语句
	 * @return 返回数据更新操作涉及的数据行数，如果执行过程发生异常，则返回-1
	 * */
	public int executeUpdate(final String sql) {

		// 利用匿名的内部类创建执行数据更新操作的回调对象
		JDBCCallback updateCallback = new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				// 定义返回值，如果执行过程中出现异常，则返回-1
				int result = -1;
				try {
					// 执行传递过来的数据更新SQL操作
					result = statement.executeUpdate(sql);
				} catch (Exception e) {
					// 如果有异常，则输出异常信息
					e.printStackTrace();
				}
				// 将返回值包装成对象后返回
				return new Integer(result);
			}
		};
		// 执行参数提供的SQL语句，进行数据更新并返回更新涉及的行数
		return ((Integer) execute(updateCallback)).intValue();
	}

	/**
	 * 利用预编译语句对象执行单次数据更新操作的方法
	 * 
	 * @param preparedSQL
	 *            需要执行的数据更新的预编译SQL语句
	 * @param data
	 *            存放更新数据的参数数据
	 * @return 返回数据更新操作涉及的数据行数，如果执行过程发生异常，则返回-1
	 * */
	public int executePreparedUpdate(String preparedSQL, final Object[] data) {
		// 创建执行更新操作的回调对象
		JDBCCallback updateCallback = new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				// TODO Auto-generated method stub
				try {
					// 获取预编译语句对象
					PreparedStatement pstatement = (PreparedStatement) statement;
					// 遍历参数值
					for (int i = 0; i < data.length; i++) {
						// 为特定的占位符设置对应的参数值
						pstatement.setObject(i + 1, data[i]);
					}
					// 利用预编译语句对象执行数据更新的SQL语句并获得返回值
					int result = pstatement.executeUpdate();
					// 将返回值包装为对象后返回
					return new Integer(result);

				} catch (Exception e) {
					// 如果遇到异常，则输出异常信息
					e.printStackTrace();
					// 遇到异常，返回-1作为错误码
					return new Integer(-1);
					// TODO: handle exception
				}

			}
		};
		return ((Integer) execute(preparedSQL, updateCallback)).intValue();
	}

	/**
	 * 利用普通语句对象执行批量更新操作的方法
	 * 
	 * @param 存放需要批量更新操作的SQL语句的数组
	 * @return 返回每条更新语句涉及的数据条目数
	 * */
	public int[] executeBatch(final String[] batchSQLs) {
		// 创建执行批处理的回调对象
		JDBCCallback batchCallback = new JDBCCallback() {
			// 实现执行批处理的回调方法
			@Override
			public Object doWithStatement(Statement statement) {

				try {
					for (String sql : batchSQLs) {
						// 数组中的每一个字符串当成需要执行的一个SQL语句添加到批处理中
						statement.addBatch(sql);
					}
					// 执行数据批处理更新，并返回结果
					return statement.executeBatch();
				} catch (Exception e) {
					// 如果遇到异常，则输出异常信息
					e.printStackTrace();
					// 在异常情况下，返回nulls
					return null;
				}
			}
		};

		// 执行回调并返回最终的结果
		return (int[]) execute(batchCallback);

	}

	/**
	 * 利用预编译语句对象执行批处理数据更新的方法
	 * 
	 * @param preparedSQL
	 *            创建预编译语句对象的SQL语句
	 * @param data
	 *            保存每一次更新操作的参数数据列表，列表的每个元素都是Object数组，该数组的元素保存预编译SQL语句中占位符的实际数据
	 * @param batchSize
	 *            每次执行更新操作的数量
	 * */
	public void executePreparedBatch(String preparedSQL,
			final List<Object[]> data, final int batchSize) {

		// 创建执行批处理的回调对象
		JDBCCallback batchCallback = new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				try {
					// 获取预编译语句对象
					PreparedStatement pstatement = (PreparedStatement) statement;
					// 遍历存放所有数据的列表
					for (int i = 0; i < data.size(); i++) {
						// 获取列表中的一个元素，每个元素都是一个Object[]数组，该数组中存放了单次更新操作涉及的数据
						Object[] updateData = data.get(i);
						// 遍历数据数组
						for (int index = 0; index < updateData.length; index++) {
							// 按照顺序对占位符填充实际数据
							pstatement.setObject(index + 1, updateData[index]);
						}
						// 将单次更新操作加入批处理序列
						pstatement.addBatch();
						// 判定是否到达规定的单次批处理最大数量
						if (i % batchSize == 0) {
							// 执行批处理
							pstatement.executeBatch();
							// 清空历史批处理任务
							pstatement.clearBatch();
						}

					}
					// 执行剩余的批处理任务
					return pstatement.executeBatch();
				} catch (Exception e) {
					// 如果执行过程中发生异常，则输出异常信息
					e.printStackTrace();
					// 在遇到异常的情况下返回null
					return null;
				}

			}

		};

		execute(preparedSQL, batchCallback);
	}

	/**
	 * 利用预编译语句对象及非预编译语句对象执行混合批处理数据更新的方法
	 * 
	 * @param preparedSQL
	 *            创建预编译语句对象的SQL语句
	 * @param data
	 *            保存每一次更新操作的参数数据列表，列表的每个元素都是Object数组，该数组的元素保存预编译SQL语句中占位符的实际数据
	 * @param batchSize
	 *            每次执行更新操作的数量
	 * @param batchSQLs
	 *            需要批处理执行的非预编译SQL语句数组
	 * */
	public void executeMixedBatch(String preparedSQL,
			final List<Object[]> data, final int batchSize,
			final String[] batchSQLs) {

		JDBCCallback batchCallback = new JDBCCallback() {

			@Override
			public Object doWithStatement(Statement statement) {
				// TODO Auto-generated method stub
				try {
					// 获取预编译语句对象
					PreparedStatement pstatement = (PreparedStatement) statement;
					// 遍历存放所有数据的列表
					for (int i = 0; i < data.size(); i++) {
						// 获取列表中的一个元素，每个元素都是一个Object[]数组，该数组中存放了单次更新操作涉及的数据
						Object[] updateData = data.get(i);
						// 遍历数据数组
						for (int index = 0; index < updateData.length; index++) {
							// 按照顺序对占位符填充实际数据
							pstatement.setObject(index + 1, updateData[index]);
						}
						// 将单次更新操作加入批处理序列
						pstatement.addBatch();
						// 判定是否到达规定的单次批处理最大数量
						if (i % batchSize == 0) {
							// 执行批处理
							pstatement.executeBatch();
							// 清空历史批处理任务
							pstatement.clearBatch();
						}
					}
					// 遍历非预编译的批处理SQL语句
					for (String sql : batchSQLs) {
						// 将每一条SQL语句加入批处理序列
						pstatement.addBatch(sql);
					}
					// 执行剩余的批处理任务
					pstatement.executeBatch();
					// 本功能无需返回值
					return null;
				} catch (Exception e) {
					// 如果遇到异常情况则输出异常信息
					e.printStackTrace();
					// 本功能无需返回值
					return null;
				}
			}
		};
		// 执行批处理
		execute(preparedSQL, batchCallback);
	}

	/**
	 * 利用普通语句对象执行SQL查询的方法
	 * 
	 * @param sql
	 *            执行查询的SQL语句
	 * @param persistenceClass
	 *            存放查询结果的实体类信息
	 * @return 返回执行查询后的结果集
	 * */
	public <T> ArrayList<T> queryForList(final String sql,
			final Class<T> persistenceClass) {
		// 创建执行查询的回调对象
		JDBCCallback queryCallback = new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				// TODO Auto-generated method stub
				try {
					// 执行SQL查询
					ResultSet rs = statement.executeQuery(sql);
					// 将结果集中的数据自动填充到集合类型中
					ArrayList<T> resultList = resultSet2List(rs,
							persistenceClass);
					// 关闭结果集，释放资源
					rs.close();
					// 返回结果集合
					return resultList;
				} catch (Exception e) {
					// 如果在执行过程中出现异常，则输出异常信息
					e.printStackTrace();
					// 在有异常的情况下，返回null
					return null;
				}
			}
		};
		// 执行查询回调并最终返回结果
		return (ArrayList<T>) execute(queryCallback);

	}

	/**
	 * 利用预编译语句对象执行SQL查询的方法
	 * 
	 * @param preparedSQL
	 *            执行查询的预编译SQL语句
	 * @param args
	 *            执行查询的条件值集合
	 * @param persistenceClass
	 *            存放查询结果的实体类信息
	 * @return 返回执行查询后的结果集
	 * */
	public <T> ArrayList<T> preparedQueryForList(final String preparedSQL,
			final Object[] args, final Class<T> persistenceClass) {

		// 创建执行查询的回调对象
		JDBCCallback queryCallback = new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {

				try {
					// 获取预编译语句对象
					PreparedStatement pstatement = (PreparedStatement) statement;
					// 遍历查询条件值数组
					for (int i = 0; i < args.length; i++) {
						// 为相应的占位符设置对应的值
						pstatement.setObject(i + 1, args[i]);
					}
					// 执行查询操作
					ResultSet rs = pstatement.executeQuery();
					// 将结果集中的数据自动填充到集合类型中
					ArrayList<T> resultList = resultSet2List(rs,
							persistenceClass);
					// 关闭结果集，释放资源
					rs.close();
					// 返回结果集合
					return resultList;

				} catch (Exception e) {
					// 如果在执行过程中出现异常，则输出异常信息
					e.printStackTrace();
					// 在有异常的情况下，返回null
					return null;
				}

			}
		};
		// 执行查询回调并最终返回结果
		return (ArrayList<T>) execute(preparedSQL, queryCallback);

	}

	/**
	 * 查询单个数据并将结果转换为Object对象的方法
	 * 
	 * @param sql
	 *            执行查询的SQL语句
	 * @return 返回的查询结果
	 * */
	public Object queryForObject(final String sql) {
		// 创建查询回调，执行查询并返回结果
		Object returnObj = execute(new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				// TODO Auto-generated method stub
				// 尝试查询结果
				try {
					// 获取查询结果集
					ResultSet rs = statement.executeQuery(sql);
					// 移动游标
					rs.next();
					// 获取查询到的唯一数据
					Object obj = rs.getObject(1);
					// 关闭结果集
					rs.close();
					// 返回结果
					return obj;
					// 捕获异常
				} catch (Exception ex) {
					// 输出异常信息
					ex.printStackTrace();
					// 返回空结果
					return null;
				}
			}
		});
		// 返回获取到的结果对象
		return returnObj;

	}

	/**
	 * 查询多个数据并将结果转换为对象数组的方法
	 * 
	 * @param sql
	 *            执行查询的SQL语句
	 * @return 返回的查询结果
	 * */
	public Object[] queryForObjectArray(final String sql) {
		// 创建查询回调，执行查询并返回结果
		Object[] array = (Object[]) execute(new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				// TODO Auto-generated method stub
				// 尝试查询结果
				try {
					// 获取查询结果集
					ResultSet rs = statement.executeQuery(sql);
					// ResultSetMetaData metaData = rs.getMetaData();
					// int columnCount = metaData.getColumnCount();
					// 将结果集的单条记录转换为对象数组
					Object[] resultArray = singleLineResut2ObjectArray(rs);
					// rs.next();
					// for (int i = 1; i <= columnCount; i++) {
					// resultArray[i - 1] = rs.getObject(i);
					// }
					// 关闭结果集
					rs.close();
					// 返回结果数组
					return resultArray;
					// 捕获异常
				} catch (Exception ex) {
					// 输出异常信息
					ex.printStackTrace();
					// 返回空数组
					return null;
				}
			}
		});
		// 返回结果数组
		return array;
	}

	/**
	 * 利用预编译语句对象查询多个数据并将结果转换为对象数组的方法
	 * 
	 * @param sql
	 *            执行查询的SQL语句
	 * @param args
	 *            预编译SQL语句参数
	 * @return 返回的查询结果数组
	 * */
	public Object[] preparedQueryForObjectArray(String preparedSQL,
			final Object[] args) {
		// 创建查询回调，执行查询并返回结果
		Object[] array = (Object[]) execute(preparedSQL, new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				// TODO Auto-generated method stub
				// 将语句对象造型为预编译语句对象
				PreparedStatement stmt = (PreparedStatement) statement;
				// 尝试查询结果
				try {
					// 按照顺序对占位符参数进行设置
					for (int i = 0; i < args.length; i++) {
						// 为相应的占位符设置对应的值
						stmt.setObject(i + 1, args[i]);
					}
					// 获取查询结果集
					ResultSet rs = stmt.executeQuery();
					// ResultSetMetaData metaData = rs.getMetaData();
					// int columnCount = metaData.getColumnCount();
					// 将结果集的单条记录转换为对象数组
					Object[] resultArray = singleLineResut2ObjectArray(rs);
					// rs.next();
					// for (int i = 1; i <= columnCount; i++) {
					// resultArray[i - 1] = rs.getObject(i);
					// }
					// 关闭结果集
					rs.close();
					// 返回结果数组
					return resultArray;
					// 捕获异常
				} catch (Exception ex) {
					// 输出异常信息
					ex.printStackTrace();
					// 返回空数组
					return null;
				}
			}
		});
		// 返回结果数组
		return array;
	}

	/**
	 * 执行插入操作并返回插入时生成的主键值列表的方法
	 * 
	 * @param insertSQL
	 *            执行插入操作的SQL语句
	 * @return 本次插入操作执行后自动生成的主键值集合
	 * */
	public Object[] insertForGeneratedKeys(final String insertSQL) {
		// 创建查询回调，执行查询并返回结果
		Object[] generatedKeys = (Object[]) execute(new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				// TODO Auto-generated method stub
				// 尝试执行插入操作
				try {
					// 执行插入操作并要求返回自动生成的主键值列表
					statement.executeUpdate(insertSQL,
							Statement.RETURN_GENERATED_KEYS);
					// 获取主键值结果集
					ResultSet rs = statement.getGeneratedKeys();
					// 将主键值结果集转换为Object数组
					Object[] keys = singleLineResut2ObjectArray(rs);
					// 关闭结果集
					rs.close();
					// 返回主键值数组
					return keys;
					// 捕获异常
				} catch (Exception ex) {
					// 输出异常信息
					ex.printStackTrace();
					// 返回null数组
					return null;
				}
			}
		});
		// 返回主键值数组
		return generatedKeys;
	}

	/**
	 * 利用预编译语句对象执行插入操作并返回插入时生成的主键值列表的方法
	 * 
	 * @param sql
	 *            执行插入操作的SQL语句
	 * @param args
	 *            预编译SQL语句参数
	 * @return 本次插入操作执行后自动生成的主键值集合
	 * */
	public Object[] preparedInsertForGeneratedKeys(String preparedInsertSQL,
			final Object[] args) {
		// 创建查询回调，执行查询并返回结果
		Object[] generatedKeys = (Object[]) execute(preparedInsertSQL,
				new JDBCCallback() {
					// 实现回调方法
					@Override
					public Object doWithStatement(Statement statement) {
						// TODO Auto-generated method stub
						// 尝试执行插入操作
						try {
							// 将语句对象造型为预编译语句对象
							PreparedStatement stmt = (PreparedStatement) statement;
							// 按照顺序对占位符参数进行设置
							for (int i = 0; i < args.length; i++) {
								// 为特定的占位符设置对应的参数值
								stmt.setObject(i + 1, args[i]);
							}
							// 执行插入操作
							stmt.executeUpdate();
							// 获取主键值结果集
							ResultSet rs = stmt.getGeneratedKeys();
							// 将主键值结果集转换为Object数组
							Object[] keys = singleLineResut2ObjectArray(rs);
							// 关闭结果集
							rs.close();
							// 返回主键数组
							return keys;
							// 捕获异常
						} catch (Exception e) {
							// 输出异常信息
							e.printStackTrace();
							// 返回null数组
							return null;
							// TODO: handle exception
						}

					}
				});
		// 返回主键值数组
		return generatedKeys;
	}

	/**
	 * 将只有一行的结果集数据转换为对象数组的方法
	 * 
	 * @param rs
	 *            要转换的结果集
	 * @return 返回转换后的结果数组
	 * */
	private Object[] singleLineResut2ObjectArray(ResultSet rs) {
		// 尝试进行转换操作
		try {
			// 获取结果集元数据
			ResultSetMetaData metaData = rs.getMetaData();
			// 获取结果及列数
			int columnNum = metaData.getColumnCount();
			// 创建结果数组
			Object[] array = new Object[columnNum];
			// 移动游标
			rs.next();
			// 遍历结果集的每一列
			for (int i = 0; i < columnNum; i++) {
				// 取出结果集中对应列的数据并保存在数组响应的位置上
				array[i] = rs.getObject(i + 1);
			}
			// 返回结果数组
			return array;
			// 捕获异常
		} catch (Exception e) {
			// 输出异常信息
			e.printStackTrace();
			// 返回null数组
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * 利用预编译语句对象查询单个数据并将结果转换为Object对象的方法
	 * 
	 * @param preparedSQL
	 *            执行查询的SQL语句
	 * @param args
	 *            预编译SQL语句参数
	 * @return 返回的查询结果
	 * */
	public Object preparedQueryForObject(String preparedSQL, final Object[] args) {
		// 创建查询回调，执行查询并返回结果
		Object returnObj = execute(preparedSQL, new JDBCCallback() {
			// 实现回调方法
			@Override
			public Object doWithStatement(Statement statement) {
				// 将语句对象造型为预编译语句对象
				PreparedStatement stmt = (PreparedStatement) statement;
				// 尝试执行查询操作
				try {
					// 按照顺序对占位符参数进行设置
					for (int i = 0; i < args.length; i++) {
						// 为相应的占位符设置对应的值
						stmt.setObject(i + 1, args[i]);
					}
					// 获取查询结果集
					ResultSet rs = stmt.executeQuery();
					// 移动结果集游标
					rs.next();
					// 获取结果集中的单一结果数据
					Object obj = rs.getObject(1);
					// 关闭结果集
					rs.close();
					// 返回结果对象
					return obj;
					// 捕获异常
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// 输出异常信息
					e.printStackTrace();
					// 返回空对象
					return null;
				}
				// TODO Auto-generated method stub

			}
		});
		// 返回结果对象
		return returnObj;

	}

	/**
	 * 利用预编译语句对象实现数据分页显示查询的方法
	 * 
	 * @param preparedSQL
	 *            查询语句
	 * @param args
	 *            预编译查询语句参数数据
	 * @param page
	 *            要查询的页码
	 * @param pageSize
	 *            每页显示的条目数
	 * @param persistenceClass
	 *            查询结果对应的实体类
	 * @return 返回查询结果
	 * */
	public <T> ArrayList<T> preparedForPageList(String preparedSQL,
			Object[] args, int page, int pageSize, Class<T> persistenceClass) {
		// 获取分页数据并返回
		return preparedForOffsetList(preparedSQL, args, (page - 1) * pageSize,
				pageSize, persistenceClass);
	}

	/**
	 * 利用预编译语句对象实现数据offset查询的方法
	 * 
	 * @param preparedSQL
	 *            查询语句
	 * @param args
	 *            预编译查询语句参数数据
	 * @param page
	 *            要查询的页码
	 * @param offset
	 *            偏移量
	 * @param rowNum
	 *            要获取的条目数
	 * @return 返回查询结果
	 * */
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> preparedForOffsetList(String preparedSQL,
			final Object[] args, final int offset, final int rowNum,
			final Class<T> persistenceClass) {
		// 创建结果集合
		ArrayList<T> list = new ArrayList<T>();
		// 获取不同数据库的特殊TOPN操作数据库
		String modifySQL = getSpecialTopNSQL(preparedSQL);

		// System.out.println(modifySQL);
		// 如果查询语句没有变化，说明没有找到对应的特殊操作对象
		if (modifySQL == preparedSQL) {

			// System.out.println("Common WAY");
			// 执行查询语句并获取结果集合
			list = (ArrayList<T>) execute(preparedSQL, new JDBCCallback() {
				// 实现回调
				@Override
				public Object doWithStatement(Statement statement) {
					// TODO Auto-generated method stub
					// 对语句对象进行造型
					PreparedStatement stmt = (PreparedStatement) statement;
					// 尝试查询
					try {
						// 遍历参数数组
						for (int i = 0; i < args.length; i++) {
							// 为相应的占位符设置对应的值
							stmt.setObject(i + 1, args[i]);
						}
						ResultSet rs = stmt.executeQuery();
						// 将结果集中的数据自动填充到集合类型中
						ArrayList<T> resultList = resultSet2List(rs, offset,
								rowNum, persistenceClass);
						// 关闭结果集，释放资源
						rs.close();
						// 返回结果集合
						return resultList;
						// 捕获异常
					} catch (Exception e) {
						// 输出异常信息
						e.printStackTrace();
						// 返回空对象
						return null;

						// TODO: handle exception
					}

				}
			});
			// 如果查找到了对应的特殊操作对象并获取到特殊的TOPN操作SQL语句
		} else {

			// System.out.println("Special WAY");
			// 执行查询语句并获取结果集合
			list = (ArrayList<T>) execute(modifySQL, new JDBCCallback() {
				// 实现回调
				@Override
				public Object doWithStatement(Statement statement) {
					// 对语句对象造型
					PreparedStatement stmt = (PreparedStatement) statement;
					// 尝试获取数据
					try {
						// 设置分页查询参数
						driverDBMSMapping
								.get(driverName)
								.newInstance()
								.setTopNQueryParameter(stmt, args, offset,
										rowNum);
						// 执行查询操作
						ResultSet rs = stmt.executeQuery();
						// 获取结果集合
						ArrayList<T> resultList = resultSet2List(rs,
								persistenceClass);
						// 关闭结果集
						rs.close();
						// 返回结果集合
						return resultList;
						// 捕获异常
					} catch (Exception e) {
						// 输出异常信息
						e.printStackTrace();
						// 返回空对象
						return null;
						// TODO: handle exception
					}

				}
			});

		}
		// 返回结果数组
		return list;
	}

	/**
	 * 获取特殊数据库TOPN操作SQL语句的方法
	 * 
	 * @param sql
	 *            原始SQL语句
	 * @return 获取到的特殊操作语句
	 * */
	public String getSpecialTopNSQL(String sql) {
		// 尝试获取特殊语句
		try {
			// 如果检索到特殊操作对象，则返回新的特殊操作语句，否则返回原始查询语句
			sql = driverDBMSMapping.containsKey(driverName) ? driverDBMSMapping
					.get(driverName).newInstance().getTopNSQL(sql, true) : sql;
			// 捕获异常
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// 输出异常信息
			e.printStackTrace();
		}
		// 返回结果
		return sql;
	}

	/**
	 * 获取驱动名的方法
	 * 
	 * @return 数据库驱动名
	 * */
	public String getDriverName() {
		// 返回驱动名
		return driverName;
	}

	/**
	 * 将JDBC结果集转化为实体对象集合的方法
	 * 
	 * @param rs
	 *            从数据库中查询得到的结果集对象
	 * @param persistenceClass
	 *            对应的实体类信息
	 * @return 最终转化的结果集合
	 * */
	private <T> ArrayList<T> resultSet2List(ResultSet rs,
			Class<T> persistenceClass) {
		// 创建结果集合
		ArrayList<T> resultList = new ArrayList<T>();
		try {
			// 获取结果集的元数据，用以获取结果集中的总列数，每个列的列名等
			ResultSetMetaData rsMetaData = rs.getMetaData();
			// 得到本结果集中的总列数
			int columnCount = rsMetaData.getColumnCount();
			// 循环遍历结果集
			while (rs.next()) {
				// 创建单一的实体对象
				T entityObject = persistenceClass.newInstance();
				// 循环遍历本行的所有单元格
				for (int cindex = 1; cindex <= columnCount; cindex++) {
					// 获取到当前单元的对应的列名
					String cname = rsMetaData.getColumnName(cindex);
					// 判定单元格中数据的JDBC类型
					switch (rsMetaData.getColumnType(cindex)) {
					// 处理BLOB类型
					case BLOB:
						// 获取BLOB类型数据
						Blob blob = rs.getBlob(cindex);
						// 填充实体对象中的相关属性
						beanUtil.setBeanProperty(entityObject, cname, blob,
								true);
						break;
					// 处理CLOB类型
					case CLOB:
						// 获取CLOB类型数据
						Clob clob = rs.getClob(cindex);
						// 填充实体对象中的相关属性
						beanUtil.setBeanProperty(entityObject, cname, clob,
								true);
						break;
					// 处理日期时间类型
					case TIME:
					case TIMESTAMP:
					case DATE:
						// 获取日期数据
						// System.out.println(rs.getTimestamp(cindex));
						Timestamp time = rs.getTimestamp(cindex);
						if (time != null) {
							// long timestamp=rs.getTimestamp(cindex).getTime();
							Date date = new Date(time.getTime());
							// 填充实体对象中的相关属性
							beanUtil.setBeanProperty(entityObject, cname, date,
									true);
						}
						break;
					// 处理其他日常数据
					default:
						// 以字符串的形式获取数据值
						String value = rs.getString(cindex);
						// 利用转换器进行格式转换后填充相关的属性
						beanUtil.setBeanProperty(entityObject, cname, value,
								true);
						break;

					}

				}
				// 将本次创建的实体对象加入结果集合
				resultList.add(entityObject);

			}
		} catch (Exception e) {
			// 如果执行的过程中遇到异常，则输出异常信息
			e.printStackTrace();
			// 在有异常的情况下，返回null
			return null;
		}
		// 返回最终的结果集合
		return resultList;
	}

	private <T> ArrayList<T> resultSet2List(ResultSet rs, int offset,
			int rowNum, Class<T> persistenceClass) {
		// 创建结果集合
		ArrayList<T> resultList = new ArrayList<T>();
		try {
			// 获取结果集的元数据，用以获取结果集中的总列数，每个列的列名等
			ResultSetMetaData rsMetaData = rs.getMetaData();
			// 得到本结果集中的总列数
			int columnCount = rsMetaData.getColumnCount();

			for (int i = 0; i < offset; i++) {
				rs.next();
			}

			// 循环遍历结果集
			while (rs.next() && rowNum > 0) {
				// 创建单一的实体对象
				T entityObject = persistenceClass.newInstance();
				// 循环遍历本行的所有单元格
				for (int cindex = 1; cindex <= columnCount; cindex++) {
					// 获取到当前单元的对应的列名
					String cname = rsMetaData.getColumnName(cindex);
					// 判定单元格中数据的JDBC类型
					switch (rsMetaData.getColumnType(cindex)) {
					// 处理BLOB类型
					case BLOB:
						// 获取BLOB类型数据
						Blob blob = rs.getBlob(cindex);
						// 填充实体对象中的相关属性
						beanUtil.setBeanProperty(entityObject, cname, blob,
								true);
						break;
					// 处理CLOB类型
					case CLOB:
						// 获取CLOB类型数据
						Clob clob = rs.getClob(cindex);
						// 填充实体对象中的相关属性
						beanUtil.setBeanProperty(entityObject, cname, clob,
								true);
						break;
					// 处理日期时间类型
					case TIME:
					case TIMESTAMP:
					case DATE:
						// 获取日期数据
						Date date = rs.getDate(cindex);
						// 填充实体对象中的相关属性
						beanUtil.setBeanProperty(entityObject, cname, date,
								true);
						break;
					// 处理其他日常数据
					default:
						// 以字符串的形式获取数据值
						String value = rs.getString(cindex);
						// 利用转换器进行格式转换后填充相关的属性
						beanUtil.setBeanProperty(entityObject, cname, value,
								true);
						break;

					}

				}
				// 将本次创建的实体对象加入结果集合
				resultList.add(entityObject);
				rowNum--;

			}
		} catch (Exception e) {
			// 如果执行的过程中遇到异常，则输出异常信息
			e.printStackTrace();
			// 在有异常的情况下，返回null
			return null;
		}
		// 返回最终的结果集合
		return resultList;
	}

}
