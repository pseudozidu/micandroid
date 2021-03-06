/*
 * EndNode.java 2011-1-28
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.wf.define;

import org.jxstar.util.resource.JsMessage;
import org.jxstar.wf.WfException;
import org.jxstar.wf.engine.ProcessContext;

/**
 * 结束节点，调用过程实例的完成方法。
 *
 * @author TonyTan
 * @version 1.0, 2011-1-28
 */
public class EndNode extends Node {
	/**
	 * 流程执行完成。
	 * @param context -- 过程上下文对象
	 * @throws WfException
	 */
	public void execute(ProcessContext context) throws WfException {
		//"执行节点【结束】"
		_log.showDebug(JsMessage.getValue("node.donode"), getNodeTitle());
		
		context.getProcessInstance().complete(context);
	}
}
