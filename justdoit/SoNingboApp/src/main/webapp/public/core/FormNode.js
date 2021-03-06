﻿/*!
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
 
/**
 * 表单页面的基类对象。
 * 
 * @author TonyTan
 * @version 1.0, 2010-01-01
 */

Ext.ns('Jxstar');
Jxstar.FormNode = function(config){
	//节点配置信息
	this.config = config;
	//表格定义信息
	this.param = config.param;

	//功能定义ID
	this.nodeId = this.param.funid;
	//功能页面类型
	this.pageType = 'form';
	//功能对象ID
	this.id = 'node_' + this.nodeId + '_' + this.pageType;

	//功能定义对象
	this.define = Jxstar.findNode(this.nodeId);
	//事件定义对象
	this.event = new Jxstar.FormEvent(this.define);

	//功能页面对象
	this.page = null;
	
	//父功能ID，用于控制子功能的操作权限
	this.parentNodeId = '';

	//是否设计状态 0 运行状态 1 设计状态
	this.state = '0';
	
	//当前功能权限信息，与读取按钮信息同时读出来
	this.right = {edit:'1', print:'1', audit:'1', other:'1'};
	
	//处理字段多语言文字
	if (this.state == '0') {
		JxLang.formField(this.nodeId, this.param.items);
	}
};

Jxstar.FormNode.prototype = {
	/**
	 * 显示功能页面，外部调用主要方法。
	 * pageType -- 页面类型，根据需要在显示时确定页面用途类型
	 * parentNodeId -- 父功能ID
	 **/
	showPage: function(pageType, parentNodeId){
		//如果外部给页面类型，则取外部的值
		if (pageType != null) this.pageType = pageType;
		//设置父功能ID
		if (parentNodeId != null) this.parentNodeId = parentNodeId;
		//创建页面
		if (!this.createPage()) return;
		//扩展自定义事件信息
		this.extEvent();
		//创建工具栏
		this.createTool();
		//扩展页面信息
		this.initPage();

		//返回页面对象
		return this.page;
	},

	/**
	 * 创建功能内容对象，用于扩展。
	 **/
	createPage: function(){
		var self = this, fm = self.param;
		if (fm == null || fm.items == null || fm.items.length == 0) {
			//'创建页面的字段参数为空！'
			JxHint.alert(jx.node.nofields);
			return false;
		}

		
		var tcfg = {deferHeight:true, items:[{text:' '}]};
		//处理：IE下工具栏的高度为29px，FF下工具栏的高度为27px
		if (Ext.isIE) tcfg.style = 'padding:1px;';
		//创建工具栏
		var tbar = new Ext.Toolbar(tcfg);

		var form = new Ext.form.FormPanel({
			id: self.id,
			labelAlign: fm.labelAlign || 'right',
			labelWidth: 100,
			border: false,
			autoScroll: true,
			frame: true,
			tbar: tbar,
			items: fm.items
		});

		//取消表单的边框，原来padding值为6px
		form.on('afterrender', function(fm){
			fm.getEl().select('.x-panel-mc').setStyle('padding', '0');
			fm.getEl().select('.x-panel-ml').setStyle('padding-left', '1px');
			fm.getEl().select('.x-panel-mr').setStyle('padding', '0');
			//如果记录表单的宽度超过1000，则设置为1000
			var fw = fm.getWidth();
			if (fw > 1000) {
				fm.getComponent(0).setWidth(1000);
			}
			//给所有字段添加change事件，处理字段修改标记。trigger控件的事件在select值后触发的form.set方法中处理。
			fm.getForm().fieldItems().each(function(f){
				var name = f.name;
				if (name != null && name.length > 0) {
					f.on('change', JxUtil.changeValue);
					//添加CTRL+F1事件查看字段帮助说明
					f.on('specialkey', JxUtil.specialKey);
				}
			});
		});
		//添加表单控件注销事件
		form.on('beforedestroy', function(fm){
			var my = fm.getForm();
			my.myGrid = null;		delete my.myGrid;
			my.myRecord = null;		delete my.myRecord;
			my.myStore = null;		delete my.myStore;
			my.fkName = null;		delete my.fkName;
			my.fkValue = null;		delete my.fkValue;
			my.srcEvent = null;		delete my.srcEvent;
			
			fm.formNode.destroy();
			fm.formNode = null;		delete fm.formNode;
			fm = null;
			
			return true;
		});
		
		//临时处理办法，将来采用全局对象来管理程序对象
		form.formNode = self;
		//设置事件对象的页面
		self.event.setPage(form);
		//设置页面对象
		self.page = form;

		return true;
	},

	/**
	 * 创建工具栏对象。
	 **/
	createTool: function(){
		var params = 'funid=queryevent&eventcode=query_loadtb&selpfunid='+this.parentNodeId;
		if (this.state == '1') {
			params += '&selfunid=sys_fun_base&selpagetype=desform';
		} else {
			params += '&selfunid='+this.nodeId+'&selpagetype='+this.pageType;
		}

		var self = this;
		var hdCall = function(json) {
			//如果打开功能立即关闭，会存在page为null
			if (self.page == null || json == null) return;
			//取按钮数组
			var items = json.buttons;
			//取权限信息
			var right = json.right;
			if (right) self.right = right;
			
			var ei = self.event;
			var tbar = self.page.getTopToolbar();
			
			//标示按钮段号，如果在不同的段位加分隔栏
			var dnum = 0;
			//保存扩展按钮
			var extItems = [];
			
			//给按钮对象分配事件
			for (var i = 0, n = items.length; i < n; i++){
				//处理按钮多语言文字
				JxLang.eventLang(self.nodeId, items[i]);
				
				//按钮显示类型[tool|menu]
				var showType = items[i].showType;
				
				if (items[i].method.length > 0) {
					var h = ei[items[i].method];
					var a = items[i].args;
					//自定义方法用事件代码作为参数
					if (items[i].method == 'customEvent') {
						a = items[i].eventCode
					}
					
					items[i].scope = ei;
					if (a != null && a.length > 0) {
						items[i].handler = h.createDelegate(ei, [a]);
					} else {
						items[i].handler = h;
					}
				}

				//如果没有定义事件的样式，则取缺省样式
				var hasCss = Ext.util.CSS.getRule('.'+items[i].iconCls);
				if (hasCss == null && showType != 'menu') {
					items[i].iconCls = 'eb_empty';
				}

				//添加分隔栏
				var idx = items[i].eventIndex;
				if (idx == null || idx.length == 0) idx = '0';
				idx = parseInt(parseInt(idx)/100);
				if (idx > dnum) {
					if (i > 0) tbar.add('-');
					dnum = idx;
				}
				
				//如果是显示到菜单，则添加扩展栏中
				if (showType == 'menu') {
					extItems[extItems.length] = items[i];
				} else {
					//添加按钮
					tbar.add(items[i]);	
				}
			}
			//添加扩展工具栏
			var fn = self.config.toolext;
			if (fn && typeof fn == 'function') {
				fn(self, tbar, extItems);
			}
			
			//添加扩展按钮
			if (extItems.length > 0) {
				var extMenu = new Ext.menu.Menu({items: extItems});
				tbar.add({
					text: jx.node.extmenu,//'扩展操作…'
					iconCls: 'eb_menu',
					menu: extMenu
				});
			}

			if (tbar.items != null && tbar.items.getCount() == 1) {
				tbar.hide();
			} else {
				tbar.doLayout();
			}
		};

		Request.dataRequest(params, hdCall);
	},

	/**
	 * 扩展自定义事件到this.event中。
	 **/
	extEvent: function(){
		var scope = this;
		var cfg = this.config.eventcfg;
		if (cfg) {
			Ext.apply(this.event, cfg);
		}
	},
		
	/**
	 * 页面加载完后执行的方法，参数有表格对象与功能定义对象。
	 **/
	initPage: function(){
		var self = this;
		//自定义扩展
		var fn = self.config.initpage;
		if (typeof fn == 'function') {
			fn(self);
		}
	},

	/**
	 * 设置设计状态 0 运行状态 1 设计状态。
	 **/
	setState: function(state) {
		this.state = state;
	},
	
	/**
	 * 销毁form页面对象
	 **/
	destroy: function() {
		this.config = null;				delete this.config;
		this.param = null;				delete this.param;
		this.nodeId = null;				delete this.nodeId;
		this.pageType = null;			delete this.pageType;
		this.id = null;					delete this.id;
		this.define = null;				delete this.define;
		this.event.myDestroy();
		this.event = null;				delete this.event;
		this.page = null;				delete this.page;
		this.parentNodeId = null;		delete this.parentNodeId;
		this.state = null;				delete this.state;
		this.right = null;				delete this.right;
	}
};
