/*
 * FileUtil.java 2009-10-25
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件操作工具类，包括文件的打开、创建、删除、修改等操作。
 *
 * @author TonyTan
 * @version 1.0, 2009-10-25
 */
public class FileUtil {

	/**
	 * 判断该文件是否存在，文件完整路径加文件名
	 * @param fileName
	 * @return
	 */
	public static boolean exists(String fileName) {
		if (fileName == null || fileName.length() == 0)
			return false;
		
		//路径中的\转换为/
		fileName = fileName.replace('\\', '/');	
		//创建或修改文件
		File file = new File(fileName);
		
		return file.exists();
	}
	
	/**
	 * 把文件内容保存到指定的文件中，如果指定的文件已存在，则先删除这个文件，
	 * 如果没有则创建一个新文件，文件内容采用UTF-8编码方式保存。
	 * 如果指定的文件路径不存在，则先创建文件路径，文件路径从根目录开始创建。
	 * 
	 * @param fileName -- 文件路径
	 * @param content -- 文件内容
	 * @return
	 */
	public static boolean saveFileUtf8(String fileName, String content) {
		if (fileName == null || fileName.length() == 0)
			return false;
		if (content == null) return false;
		
		//路径中的\转换为/
		fileName = fileName.replace('\\', '/');
		//处理文件路径
		createPath(fileName.substring(0, fileName.lastIndexOf('/')));
		
		File file = null;
		FileOutputStream out = null;
		try {			
			//创建或修改文件
			file = new File(fileName);
			
			if (file.exists()) {
				file.delete();
			} else {
				file.createNewFile();
			}
			
			out = new FileOutputStream(file);
			//添加三个字节标识为UTF-8格式
			out.write(new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF});
			out.write(content.getBytes("utf-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		return true;
	}
	
	/**
	 * 从指定的文件中读取文件内容，并把文件内容作为字符串返回，
	 * 如果采用byte[]方式读取文件内容，会造成字符错位，出现乱码，
	 * 如果采用char[]方式读取文件就不会有这种问题了。
	 * 
	 * @param fileName -- 文件名
	 * @return
	 */
	public static String read(String fileName) {
		StringBuilder sb = new StringBuilder();
		FileReader in = null;
		int cnt = 0;
		try {
			in = new FileReader(new File(fileName));
			
			char[] b = new char[1024];
			while ((cnt = in.read(b)) > 0) {
				sb.append(new String(b, 0, cnt));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 把文件内容转换为字节数组输出。
	 * @param fileName -- 文件名
	 * @return
	 */
	public static byte[] fileToBytes(String fileName) {
		FileInputStream ins = null;
		ByteArrayOutputStream bos = null;
		try {
			//创建文件读入流
			ins = new FileInputStream(new File(fileName));
			//创建目标输出流
			bos = new ByteArrayOutputStream();
			
			//取流中的数据
			int len = 0;
			byte[] buf = new byte[256];
			while ((len = ins.read(buf, 0, 256)) > -1) {
				bos.write(buf, 0, len);
			}
			
			//目标流转为字节数组返回到前台
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ins != null) {ins.close(); ins = null;}
				if (bos != null) {bos.close(); bos = null;}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * 检查指定的文件路径，如果文件路径不存在，则创建新的路径，
	 * 文件路径从根目录开始创建。
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean createPath(String filePath) {
		if (filePath == null || filePath.length() == 0) 
			return false;
		
		//路径中的\转换为/
		filePath = filePath.replace('\\', '/');
		//处理文件路径
		String[] paths = filePath.split("/");
		
		//处理文件名中没有的路径
		StringBuilder sbpath = new StringBuilder();
		for (int i = 0, n = paths.length; i < n; i++) {
			sbpath.append(paths[i]);
			//检查文件路径如果没有则创建
			File ftmp = new File(sbpath.toString());
			if (!ftmp.exists()) {
				ftmp.mkdir();
			}
			
			sbpath.append("/");
		}
		
		return true;
	}
	
	/**
	 * 获取有效文件，如果文件存在，则在文件名后添加(n)序号。
	 * @param file -- 原文件
	 * @return
	 */
	public static File getValidFile(File file) {
		if (file == null) return file;
		//取文件名
		String orgName = file.getName();
		String[] orgNames = orgName.split("\\.");
		
		//取文件路径
		String filePath = file.getParent();
		filePath = filePath.replace('\\', '/') + "/";
				
		int i = 1;
		while(file.exists()) {
			i++;
			String fileName = filePath + orgNames[0]+"("+i+")";
			if (orgNames.length > 1) {
				fileName += orgName.substring(orgName.indexOf("."));
			}
			
			file = new File(fileName);
		}
		
		return file; 
	}
	
	/**
	 * 获取有效文件，如果文件存在，则在文件名后添加(n)序号。
	 * @param fileName -- 原文件名
	 * @return
	 */
	public static File getValidFile(String fileName) {
		File file = new File(fileName);
		return getValidFile(file);
	}
	
	/**
	 * 取路径中的文件名
	 * @param path -- 文件路径，含文件名
	 * @return
	 */
	public static String getFileName(String path) {
		if (path == null || path.length() == 0) return "";
		
		path = path.replaceAll("\\\\", "/");
		int last = path.lastIndexOf("/");
		
		if (last >= 0) {
			return path.substring(last+1);
		} else {
			return path;
		}
	}
	
	/**
	 * 取当前类加载路径
	 * @return
	 */
	public static String getClassPath() {
	    return Thread.currentThread().getContextClassLoader()
        .getResource("").getPath(); 
	}
}
