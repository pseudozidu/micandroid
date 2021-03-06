/*
 * TransactionManager.java 2008-3-30
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.dao.transaction;

/**
 * 事务管理器，负责创建事务对象，管理的事务开始、结束。
 * 
 * @author TonyTan
 * @version 1.0, 2008-3-30
 */
public interface TransactionManager {

	/**
	 * 获取事务对象。
	 * @return
	 */
	public TransactionObject getTransactionObject();
	
	/**
	 * 开始一个新的事务。
	 */
	public void startTran();
	
	/**
	 * 提交当前事务。
	 */
	public void commitTran() throws TransactionException;
	
	/**
	 * 回滚当前事务。
	 */
	public void rollbackTran() throws TransactionException;
	
	/**
	 * 获取当前事务ID。
	 * @return Object
	 */
	public Object getCurrentTransactionID();
}
