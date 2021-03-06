/*
 * DaoUtil.java 2009-5-28
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.jxstar.dao.pool.PooledConnection;
import org.jxstar.dao.transaction.TransactionException;
import org.jxstar.dao.transaction.TransactionObject;
import org.jxstar.util.StringUtil;
import org.jxstar.util.config.SystemVar;
import org.jxstar.util.factory.FactoryUtil;
import org.jxstar.util.log.Log;

/**
 * 数据访问工具对象: 处理ResultSet 到 List 的转换； 处理ResultSet 到 Map 的转换；
 * 
 * @author TonyTan
 * @version 1.0, 2009-5-28
 */
public class DaoUtil {
	private static Log _log = Log.getInstance();
	
	/**
	 * 获取结果集中的字段名。
	 * 
	 * @param rs --
	 *            结果集
	 * @return List
	 * @throws SQLException
	 */
	public static List<String> getRsToColName(ResultSet rs) throws SQLException {
		if (rs == null) {
			throw new SQLException(
					"getRsToColName(): ResultSet param is null! ");
		}

		List<String> lsRet = FactoryUtil.newList();

		// 取得结果集的单元信息
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();

			for (int i = 1; i <= columnNum; i++) {
				lsRet.add(rsmd.getColumnName(i).toLowerCase());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("getRsToColName(): " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("getRsToColName(): " + e.toString());
		}

		return lsRet;
	}

	/**
	 * ResultSet 转换为 List。
	 * 
	 * @param rs --
	 *            结果集
	 * @return List
	 * @throws SQLException
	 */
	public static List<Map<String,String>> getRsToList(ResultSet rs) throws SQLException {
		if (rs == null) {
			throw new SQLException("getRsToList(): ResultSet param is null! ");
		}

		List<Map<String,String>> lsRet = FactoryUtil.newList();
		Map<String,String> map = null;

		try {
			// 取得结果集的单元信息
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();

			// 列数为0,返回空的ArrayList
			String strVal = new String();
			String strCol = new String();
			while (rs.next()) {
				map = FactoryUtil.newMap();
				for (int i = 1; i <= columnNum; i++) {
					strCol = rsmd.getColumnName(i).toLowerCase();
					strVal = rs.getString(strCol);
					
					//如果是日期类型的字段，转换为日期对象
					if (rsmd.getColumnType(i) == java.sql.Types.DATE || 
							rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
						if (strVal == null) strVal = "";
						
						if (strVal.length() > 0) {
							String strTmp[] = strVal.split("\\.");
							if (strTmp.length == 2) {
								strVal = strTmp[0];
							}
						}
					} else if (rsmd.getColumnType(i) == java.sql.Types.CHAR || 
							rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
						if (strVal == null) strVal = "";
					}
					map.put(strCol, strVal);
				}
				lsRet.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("getRsToList(): " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("getRsToList(): " + e.toString());
		}

		return lsRet;
	}

	/**
	 * 给查询结果集添加表名。
	 * 
	 * @param mp --	 结果集
	 * @param tblName -- 表名
	 * @return Map<String,String>
	 */
	public static Map<String,String> mapAddTable(Map<String,String> mp, String tblName) {
		Map<String,String> mpRet = FactoryUtil.newMap();
		
		if (mp == null || mp.isEmpty()) return mpRet;
		
		if (tblName == null || tblName.length() == 0) return mpRet;
		
		Iterator<String> itr = mp.keySet().iterator();
		while(itr.hasNext()) {
			String key = itr.next();
			String value = mp.get(key);
			mpRet.put(tblName+"."+key, value);
		}

		return mpRet;
	}

	/**
	 * 给预编译语句赋值。
	 * 
	 * @param astrVal --
	 *            参数值
	 * @param astrType --
	 *            参数类型
	 * @param aps --
	 *            预编译语句
	 * @return PreparedStatement
	 */
	public static PreparedStatement setPreStmParams(List<String> lsValue, 
			List<String> lsType, PreparedStatement aps) throws SQLException {
		if (lsValue == null) {
			throw new SQLException("setPreStmParams(): astrVal param is null! ");
		}
		if (lsType == null) {
			throw new SQLException(
					"setPreStmParams(): astrType param is null! ");
		}
		if (aps == null) {
			throw new SQLException(
					"setPreStmParams(): PreparedStatement param is null! ");
		}

		try {
			String strval = "";
			String strtype = "";

			int icount = lsValue.size();
			if (icount > lsType.size())
				icount = lsType.size();

			for (int i = 1; i <= icount; i++) {
				strval = lsValue.get(i - 1);
				strtype = lsType.get(i - 1);

				if (strtype.equalsIgnoreCase("string")) {
					if ((null != strval) && (strval.length() > 0)) {
						aps.setString(i, strval);
					} else {
						aps.setNull(i, java.sql.Types.VARCHAR);
					}
				} else if (strtype.equalsIgnoreCase("date")) {
					if ((null != strval)
							&& (!strval.trim().equalsIgnoreCase("null"))
							&& (strval.trim().length() > 0)) {
						if (strval.length() <= 10) {
							aps.setDate(i, Date.valueOf(strval));
						} else {
							aps.setTimestamp(i, Timestamp.valueOf(strval));
						}
					} else {
						aps.setNull(i, java.sql.Types.DATE);
					}
				} else if ((strtype.equalsIgnoreCase("double"))
						|| (strtype.equalsIgnoreCase("float"))) {
					if ((null != strval) && (strval.length() > 0)) {
						aps.setDouble(i, Double.parseDouble(strval));
					} else {
						aps.setNull(i, java.sql.Types.DOUBLE);
					}
				} else if (strtype.equalsIgnoreCase("int")) {
					if ((null != strval) && (strval.length() > 0)) {
						aps.setInt(i, Integer.parseInt(strval));
					} else {
						aps.setNull(i, java.sql.Types.INTEGER);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("setPreStmParams(): " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("setPreStmParams(): " + e.toString());
		}

		return aps;
	}	
	
	/**
	 * ResultSet 转换为 JSON。
	 * 
	 * @param rs --
	 *            结果集
	 * @return String
	 * @throws SQLException
	 */
	public static String getRsToJson(ResultSet rs, String[] astrCol) 
		throws SQLException {
		if (rs == null) {
			throw new SQLException("getRsToJson(): ResultSet param is null! ");
		}
		if (astrCol == null) {
			throw new SQLException("getRsToJson(): astrCol param is null! ");
		}
		if (astrCol.length == 0) return "";

		//取字段的数据类型值 
		ResultSetMetaData rsmd = rs.getMetaData();
		//构建json对象
		StringBuilder sbJson = new StringBuilder();
		try {
			String strVal = null;
			while (rs.next()) {
				sbJson.append("{");
				//组织一行数据
				for (int i = 1, n = astrCol.length; i <= n; i++) {
					strVal = rs.getString(i);
					if (strVal == null) strVal = "";
					
					//如果是日期类型的字段，转换为日期对象
					if (rsmd.getColumnType(i) == java.sql.Types.DATE || 
							rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
						if (strVal.length() == 0) {
							strVal = "''";
						} else {
							//秒后可能会存在.0的字符
							String strTmp[] = strVal.split("\\.");
							if (strTmp.length == 2) {
								strVal = strTmp[0];
							}
							
							//约定日期格式：yyyy-mm-dd 时间戳格式：yyyy-mm-dd hh:mm:ss
							String[] dt = strVal.split(" ");
							String[] ds = dt[0].split("-");
							
							//月份值要减1
							int y = Integer.parseInt(ds[0]);
							int m = Integer.parseInt(ds[1])-1;
							int d = Integer.parseInt(ds[2]);
							
							if (dt.length > 1) {
							//TIMESTAMP类型
								String[] ts = dt[1].split(":");
								int hh = Integer.parseInt(ts[0]);
								int mm = Integer.parseInt(ts[1]);
								int ss = Integer.parseInt(ts[2]);
								
								strVal = "(new Date("+y+", "+m+", "+d+", "+hh+", "+mm+", "+ss+"))";
							} else {
							//DATE类型
								strVal = "(new Date("+y+", "+m+", "+d+"))";
							}
						}
						
						sbJson.append("'"+astrCol[i-1]+"'").append(":").append(strVal).append(",");
					} else {
						strVal = StringUtil.strForJson(strVal);
	
						sbJson.append("'"+astrCol[i-1]+"'").append(":'").append(strVal).append("',");
					}
				}

				sbJson.append("},\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("getRsToList(): " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("getRsToList(): " + e.toString());
		}
		
		String retJson = sbJson.toString();
		if (retJson.length() == 0) {
			return "";
		}
		//取掉每行数据最后的,号
		retJson = retJson.replaceAll(",},", "},");
		retJson = retJson.substring(0, retJson.length()-2);
		
		return retJson;
	}
	
	/**
	 * 取字段长度
	 * @param tableName -- 表名
	 * @param dsName -- 数据源名
	 * @return
	 */
	public static Map<String,String> fieldLength(String tableName, String dsName) {
		Map<String,String> mpField = FactoryUtil.newMap();
		PooledConnection pool = PooledConnection.getInstance();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = pool.getConnection(dsName);
			DatabaseMetaData dmd = conn.getMetaData();
			rs = dmd.getColumns(null, null, tableName.toUpperCase(), null);
			while(rs.next()) {
				String COLUMN_NAME = rs.getString("COLUMN_NAME").toLowerCase();
				//String TYPE_NAME = rs.getString("TYPE_NAME");
				String COLUMN_SIZE = rs.getString("COLUMN_SIZE");
				
				mpField.put(COLUMN_NAME, COLUMN_SIZE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return mpField;
		} finally {
			try {
				if (rs != null) rs.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return mpField;
	}
	
	/**
	 * 打印查询时间，并打印超长时间的SQL。
	 * 
	 * @param long			开始时间
	 * @param String		执行的SQL
	 */
	public static void showUpdateTime(long startTime, String sql) {
		long curQueryTime = System.currentTimeMillis() - startTime;
		//是否显示所有查询时间
		String isShow = SystemVar.getValue("db.show.querytime");
		if (isShow.equals("1")) {
			_log.showDebug("update time = " + curQueryTime);
		}
		
		//查询超时提醒时间
		String maxTime = SystemVar.getValue("db.max.querytime", "5000");
		if (curQueryTime > Integer.parseInt(maxTime)) {
			StringBuilder sbError = new StringBuilder();
			sbError.append("update time = " + curQueryTime + "; update time out sql = " + sql);
			_log.showWarn(sbError.toString());
		}
	}
	
	/**
	 * 打印查询时间，并打印超长时间的SQL。
	 * 
	 * @param long			开始时间
	 * @param String		执行的SQL
	 */
	public static void showQueryTime(long startTime, String sql) {
		long curQueryTime = System.currentTimeMillis() - startTime;
		//是否显示所有查询时间
		String isShow = SystemVar.getValue("db.show.querytime", "0");
		if (isShow.equals("1")) {
			_log.showDebug("query time = " + curQueryTime);
		}
		
		//查询超时提醒时间
		String maxTime = SystemVar.getValue("db.max.querytime", "5000");
		if (curQueryTime > Integer.parseInt(maxTime)) {
			StringBuilder sbError = new StringBuilder();
			sbError.append("query time = " + curQueryTime + "; query time out sql = " + sql);
			_log.showWarn(sbError.toString());
		}
	}	
	
	/**
	 * 显示异常信息。
	 * 
	 * @param Exception		异常对象
	 * @param String		执行的SQL
	 */
	public static void showException(Exception e, String sql) {
		StringBuilder sbError = new StringBuilder();
		sbError.append("SQLException: Excute SQL = " + sql);
		_log.showWarn(sbError.toString());
		_log.showError(e);
	}
	
	/**
	 * 关闭事务对象。
	 * 
	 * @param tranObj - 事务对象
	 */
	public static void closeTranObj(TransactionObject tranObj) {
		try {
			if (tranObj != null) {
				tranObj.rollback();
			}
		} catch (TransactionException e) {
			tranObj = null;
			_log.showError(e);
		}
	}	
}