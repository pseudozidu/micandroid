/*
 * StringValidator.java 2010-10-15
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.util;

import java.util.regex.Pattern;


/**
 * 字符串格式效验类
 * 
 * @author TonyTan
 * @version 1.0, 2010-10-15
 */
public class StringValidator {
	
	//整数
	private static String _regInt = "^\\-?[0-9]+$";
	
	//浮点
	private static String _regDouble = "^\\-?[0-9]*\\.?[0-9]*$";

	//一般日期[2004-1-30]
	private static String _regDate = "^([0-9]{4}\\-[0,1]?[0-9]{1}\\-[0-3]?[0-9]{1})";

	//其它日期[1/30/2004]
	private static String _otherDate = "^([0,1]?[0-9]{1}\\/[0-3]?[0-9]{1}\\/[0-9]{4})";
	
	//数据类型
	public static String DOUBLE_TYPE = "double";
	public static String INTEGER_TYPE = "int";
	public static String DATE_TYPE = "date";
	public static String OTHER_DATE_TYPE = "other_date";

	/**
	 * 判断数据有效性
	 * 
	 * @param value -- 数据值
	 * @param type -- 数据类型，支持：int, double, date, other_date
	 * @return boolean
	 */
	public static boolean validValue(String value, String type) {
		boolean ret = false;

		try {
			if (value == null || value.length() == 0) return true;
			
			Pattern p = Pattern.compile(regex(type));
			ret = p.matcher(value).matches();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return ret;
	}
	
	/**
	 * 取匹配的正则表达式
	 * @param type -- 数据类型，支持：int, double, date, other_date
	 * @return
	 */
	private static String regex(String type) {
	    if (type.equalsIgnoreCase("int")) {
	        return _regInt;
	    } else if (type.equalsIgnoreCase("double")){
	        return _regDouble;
	    } else if (type.equalsIgnoreCase("date")){
            return _regDate;
        } else if (type.equalsIgnoreCase("other_date")){
            return _otherDate;
        }
	    return type;
	}
}