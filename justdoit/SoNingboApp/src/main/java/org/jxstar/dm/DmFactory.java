/*
 * DmFactory.java 2010-12-21
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.dm;


import org.jxstar.dao.util.DBTypeUtil;
import org.jxstar.dm.ddl.MysqlDdlTable;
import org.jxstar.dm.ddl.OracleDdlTable;
import org.jxstar.dm.reverse.OracleMetaData;

/**
 * 取数据DDL语句执行对象。
 *
 * @author TonyTan
 * @version 1.0, 2010-12-21
 */
public class DmFactory {

	/**
	 * 取数据源对于的数据库的解析对象。
	 * @param dsName -- 数据源名，如果为空，则取缺省数据源
	 * @return
	 */
	public static DdlTable getDdlTable(String dsName) {
		String dbType = DBTypeUtil.getDbmsType(dsName);
		
		if (dbType.equals("oracle")) {
			return new OracleDdlTable();
		} else if (dbType.equals("mysql")) {
			return new MysqlDdlTable();
		} else if (dbType.equals("sqlserver")) {
			return null;
		};
		
		return null;
	}
	
	/**
	 * 取数据源对于的数据库元数据获取对象。
	 * @param dsName -- 数据源名，如果为空，则取缺省数据源
	 * @return
	 */
	public static MetaData getMetaData(String dsName) {
		String dbType = DBTypeUtil.getDbmsType(dsName);
		
		if (dbType.equals("oracle")) {
			return new OracleMetaData();
		} else if (dbType.equals("mysql")) {
			return null;
		} else if (dbType.equals("sqlserver")) {
			return null;
		};
		
		return null;
	}
}
