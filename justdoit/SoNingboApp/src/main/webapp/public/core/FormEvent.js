﻿/*!
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
  
/**
 * 表单页面常用事件实现类。
 * 
 * @author TonyTan
 * @version 1.0, 2010-01-01
 */

Ext.ns('Jxstar');
Jxstar.FormEvent = function(define) {
	this.define = define;
	this.page = null;	//表单页面FormPanel
	this.form = null;	//表单对象BasicForm
	this.addEvents(
		/**
		* @param {Jxstar.FormEvent} this
		**/
		'beforecreate', 
		/**
		* @param {Jxstar.FormEvent} this
		**/
		'beforesave', 
		/**
		* @param {Jxstar.FormEvent} this
		* @param {JSON[]} data
		**/
		'aftersave', 
		/**
		* @param {Jxstar.FormEvent} this
		**/
		'beforedelete', 
		/**
		* @param {Jxstar.FormEvent} this
		* @param {JSON[]} data
		**/
		'afterdelete', 
		/**
		* @param {Jxstar.FormEvent} this
		**/
		'beforeaudit', 
		/**
		* @param {Jxstar.FormEvent} this
		* @param {JSON[]} data
		**/
		'afteraudit', 
		/**
		* @param {Jxstar.FormEvent} this
		**/
		'beforecopy', 
		/**
		* @param {Jxstar.FormEvent} this
		* @param {JSON[]} data
		**/
		'aftercopy',
		/**
		* @param {Jxstar.FormEvent} this
		* @param eventcode
		**/
		'beforecustom', 
		/**
		* @param {Jxstar.FormEvent} this
		* @param {JSON[]} data
		* @param eventcode
		**/
		'aftercustom'
	);

	Jxstar.FormEvent.superclass.constructor.call(this, define);
};

(function(){

Ext.extend(Jxstar.FormEvent, Ext.util.Observable, {
	/**
	* public 销毁事件对象
	**/
	myDestroy : function() {
		this.define = null;		delete this.define;
		this.page = null;		delete this.page;
		this.form = null;		delete this.form;
	},

	/**
	* public
	* 设置事件对象操作的表单
	**/
	setPage : function(page) {
		this.page = page;
		this.form = page.getForm();
	},
	
	/**
	* public
	* 自定义通用事件
	**/
	customEvent : function(eventCode) {
		var keyid = this.getPkField().getValue();
		if (keyid == null || keyid.length == 0) {
			JxHint.alert(jx.event.nosave);	//当前记录没有保存，不能操作！
			return;
		}
	
		if (this.fireEvent('beforecustom', this, eventCode) == false) return;
	
		var self = this;
		var hdcall = function() {
			//回调函数
			var endcall = function(data) {
				self.fireEvent('aftercustom', self, data, eventCode);
			};
		
			//设置请求的参数
			var params = 'funid='+ self.define.nodeid +'&keyid=' + keyid;
			params += '&pagetype=form&eventcode=' + eventCode;

			//发送请求
			Request.postRequest(params, endcall);
		};
		//'提示', '确定执行当前操作吗？'
		Ext.Msg.confirm(jx.base.hint, jx.base.doyes, function(btn) {
			if (btn == "yes") hdcall();
		});
	},
	
	/**
	* public
	* 表单初始化事件，显示每条记录都会执行
	**/
	initForm: function() {
		var self = this;
		var record = self.form.myRecord;
		if (record) {
			//记录状态
			var state = record.get(self.define.auditcol);
			//没有编辑权限
			var noedit = (self.page.formNode.right.edit == '0');
			//已复核记录设置表单为只读，编辑按钮不能使用
			if (noedit || state != null) {
				//设置只读值
				var readOnly = noedit || (state != '0' && state != '6');
				JxUtil.readOnlyForm(self.form, readOnly);
				var tbar = self.page.getTopToolbar();
				JxUtil.disableButton(tbar, readOnly);
			}
		}
		//清除脏标记，设置最新的原始值
		JxUtil.clearDirty(self.form);
		
		return self.initOther();
	},
	
	/**
	* public
	* 表单初始化事件，用于外部扩展
	**/
	initOther : function() {},

	/**
	* public
	* 新增事件
	**/
	create : function() {
		var self = this;
		if (self.form.myRecord && self.form.isDirty()) {
			if (confirm(jx.event.saveyes)) {	//记录已被修改，是否需要先保存？
				self.save();return;
			}
		}
		//创建数据对象
		var record = self.createRecord();
		self.form.myRecord = record;
		self.form.loadRecord(record);

		if (self.fireEvent('beforecreate', self) == false) return;
	},

	/**
	* private
	* 新增一个数据记录对象。
	**/
	createRecord : function() {
		//表单模型
		var fm = this.form;
		
		var record = null;
		
		//功能存储对象，从grid对象中传递过来的
		var store = fm.myStore;
		if (store == null) {
			record = this.newEmptyRecord();
		} else {
			record = new (store.reader.recordType)({});
		}
		
		var cols = record.fields.keys;
		//给每个字段给缺省值
		for (var i = 0; i < cols.length; i++){
			var field = fm.findField(cols[i]);
			if (field == null) continue;
			
			var defaultval = field.defaultval;
			if (typeof defaultval == 'string' && defaultval.indexOf('fun_') == 0) {
				var val = eval('JxDefault.' + defaultval.split('fun_')[1]);
				record.set(cols[i], val);
			} else if (typeof defaultval == 'undefined') {
				record.set(cols[i], '');
			} else {
				record.set(cols[i], defaultval);
			}
		}

		return record;
	},
	
	/**
	* public
	* 根据现有数据值创建一个数据记录对象，暂时应用于单form页面中，如：功能扩展页、字段扩展页。
	**/
	newRecord : function(data) {
		//创建空记录对象
		var record = this.newEmptyRecord();
		
		//添加记录值
		var cols = record.fields.keys;
		Ext.each(cols, function(f){
			record.set(f, data[f]);
		});

		return record;
	},
	
	/**
	* private
	* 创建一个空的数据记录对象。
	**/
	newEmptyRecord: function() {
		var fields = [];
		var fm = this.form;
		
		fm.fieldItems().each(function(f){
			if (f.isFormField) {
				var type = f.getXType();
				if (type == 'datefield') {
					type = 'date';
				} else if (type == 'numberfield') {
					type = 'float';
				} else {
					type = 'string';
				}
				
				var o = {name: f.getName(), type:type};
				fields[fields.length] = o;
			}
		});
		
		return new (Ext.data.Record.create(fields))();
	},

	/**
	* public
	* 删除事件
	**/
	del : function() {
		var keyid = this.getPkField().getValue();
		if (keyid == null || keyid.length == 0) {
			JxHint.alert(jx.event.nokey);	//当前记录没有主键值，不能操作！
			return;
		}
	
		if (this.checkAudit()) return;
		if (this.fireEvent('beforedelete', this) == false) return;

		//删除提交
		var self = this;
		var hdcall = function() {
			//设置请求的参数
			var params = 'funid='+ self.define.nodeid +'&keyid=' + keyid;
			params += '&pagetype=form&eventcode=delete';

			//删除后要处理的内容
			var endcall = function(data) {
				//清除脏标记，设置最新的原始值
				JxUtil.clearDirty(self.form);
				//删除表格中的数据
				var record = self.form.myRecord;
				if (record != null) {
					var store = self.form.myStore;
					if (store != null) store.remove(record);
				}
				//删除记录后新增一条记录
				self.create();
				
				self.fireEvent('afterdelete', self, data);
			};

			//发送请求
			Request.postRequest(params, endcall);
		};
		//'确定删除当前记录吗？'
		Ext.Msg.confirm(jx.base.hint, jx.event.delyes, function(btn) {
			if (btn == "yes") hdcall();
		});
	},

	/**
	* public
	* 保存事件
	**/
	save : function() {
		var self = this;
		//数据校验
		if (!this.form.isValid()) {
			JxHint.alert(jx.event.datavalid);	//'请确保输入的数据正确完整！'
			return;
		}

		if (this.checkAudit()) return;
		if (this.fireEvent('beforesave', this) == false) return;
		
		//取主键值，如果有主键值是保存，否则为新增
		var eventcode = 'save';
		var keyid = this.getPkField().getValue();
		if (keyid.length == 0) {
			eventcode = 'create';
		}
		if (eventcode == 'save' && !this.form.isDirty()) {
			//JxHint.alert(jx.event.nomodify);	//'记录没有被修改，不需要保存！'
			self.fireEvent('aftersave', self, {});
			return;
		}
		
		//设置请求的参数
		var params = 'funid='+ this.define.nodeid + '&keyid=' + keyid;
			params += JxUtil.getFormValues(this.form, false);
			params += '&pagetype=form&eventcode=' + eventcode;
		//取所有修改了值的字段，保存时用
		if (eventcode !== 'create') {
			params += '&dirtyfields=' + JxUtil.getDirtyFields(this.form);
		}

		//添加外键值
		var fkv = this.form.fkValue ? this.form.fkValue : '';
		params += '&fkValue=' + fkv;
		
		//保存后处理的内容
		var endcall = function(data) {		
			//取其对应的数据对象，在Jxstar.openSubForm方法中赋值的;
			var record = self.form.myRecord;		
			//如果是新增记录，则反馈后台新增的主键值与编码
			//如果是保存，可能存在后台构建的值需要反馈
			for(var key in data) {
				var val = data[key];
				if (key == 'keyid')	{
					self.getPkField().osetValue(val);
				} else {
					key = key.replace('.', '__');
					self.form.findField(key).osetValue(val);
				}
			}
			
			if (eventcode == 'create') {
				//外键值必须赋值，否则有问题
				var fkn = self.form.fkName;
				if (fkv.length > 0 && fkn && fkn.length > 0) {
					self.form.findField(fkn).osetValue(fkv);
				}
			}

			//更新表格中的数据
			if (record != null) {
				self.form.updateRecord(record);
				record.commit();
				
				//如果是新增记录，则在表格中增加一条记录
				if (eventcode == 'create') {
					var store = self.form.myStore;
					if (store != null) store.insert(0, record);
					
					//移动表格中的数据
					var grid = self.form.myGrid;
					if (grid != null) {
						grid.getSelectionModel().selectFirstRow();
					}
				}
			}
			self.fireEvent('aftersave', self, data);
			//清除脏标记，设置最新原始值
			JxUtil.clearDirty(self.form);
		};
		//发送请求
		Request.postRequest(params, endcall);
	},

	/**
	* private
	* 提交时：检查是否存在已复核的记录；取消时：检查是否存在未复核记录
	**/
	checkAudit: function(auditval) {
		if (auditval == null) auditval = '1';

		var record = this.form.myRecord;
		var state = record.get(this.define.auditcol);
		if (state == null) return false;
		
		if (auditval == '0') {
			if (state != '1'){
				JxHint.alert(jx.event.curaudit0);		//'当前记录未复核，不能操作！'
				return true;
			}
		} else {
			if (state != '0' && state != '6'){
				JxHint.alert(jx.event.curaudit1);	//'当前记录已复核，不能操作！'
				return true;
			}
		}

		return false;
	},

	/**
	* public
	* 提交事件
	**/
	audit : function() {
		this.baseAudit('1');
	},

	/**
	* public
	* 取消提交事件
	**/
	unaudit : function() {
		this.baseAudit('0');
	},

	/**
	* private
	* 基础提交事件
	**/
	baseAudit : function(auditval) {
		var keyid = this.getPkField().getValue();
		if (keyid == null || keyid.length == 0) {
			JxHint.alert(jx.event.nosave);		//'当前记录没有保存，不能操作！'
			return;
		}
		
		var self = this;
		var record = self.form.myRecord;
		if (self.form.isDirty()) {
			if (confirm(jx.event.saveyes)) {		//'记录已被修改，是否需要先保存？'
				self.save();return;
			}
		}

		//取复核值
		if (auditval == null) auditval = '1';

		if (this.checkAudit(auditval)) return;
		if (this.fireEvent('beforeaudit', this) == false) return;

		//复核记录
		var hdcall = function() {
			//取选择记录的主键值
			var params = 'funid='+ self.define.nodeid +'&keyid='+ keyid;

			//复核事件代号
			var eventcode = (auditval == '0') ? 'unaudit' : 'audit';

			//设置请求的参数
			params += '&pagetype=form&eventcode='+ eventcode +'&auditvalue='+auditval.toString();
			
			//提交后要处理的内容
			var endcall = function(data) {
				var auditcol = self.define.auditcol;
				self.form.findField(auditcol).osetValue(auditval);
				record.set(auditcol, auditval);
				record.commit();

				self.fireEvent('afteraudit', self, data);
				//清除脏标记，设置最新的原始值
				JxUtil.clearDirty(self.form);
			};

			//发送请求
			Request.postRequest(params, endcall);
		};

		var shint = jx.event.audityes;		//确定复核当前记录吗？
 		if (auditval == '0') {
			shint = jx.event.auditno;		//确定反复核当前记录吗？
 		};
		Ext.Msg.confirm(jx.base.hint, shint, function(btn) {
			if (btn == "yes") hdcall();
		});
	},

	/**
	* private
	* 取当前主键值
	**/
	getPkField : function() {
		return this.form.findField(this.define.pkcol);
	},

	/**
	* public
	* 上一条
	**/
	preRecord : function() {
		var self = this;
		var record = this.form.myRecord;
		if (record != null) {
			if (self.form.isDirty()) {
				if (confirm(jx.event.saveyes)) {		//'记录已被修改，是否需要先保存？'
					self.save();return;
				}
			}

			var store = this.form.myStore;
			if (store != null) {
				var index = store.indexOf(record);
				if (index >= 1) {
					var cunidx = index-1;
					record = store.getAt(cunidx);
					this.form.myRecord = record;
					this.form.loadRecord(record);

					//移动表格中的数据
					var grid = this.form.myGrid;
					if (grid) {
						grid.getSelectionModel().selectRow(cunidx);
						grid.fireEvent('rowclick', grid, cunidx);
					}
					
					//表单初始化
					self.initForm();
					return true;
				} else {
					JxHint.alert(jx.event.firstrec);	//已经是第一条记录了！
				}
			}
		}
		return false;
	},

	/**
	* public
	* 下一条
	**/
	nextRecord : function() {
		var self = this;
		var record = self.form.myRecord;
		if (record != null) {
			if (self.form.isDirty()) {
				if (confirm(jx.event.saveyes)) {		//'记录已被修改，是否需要先保存？'
					self.save();return;
				}
			}

			var store = this.form.myStore;
			if (store != null) {
				var index = store.indexOf(record);
				if (index < store.getCount()-1) {
					var cunidx = index+1;
					record = store.getAt(cunidx);

					//刷新表单数据
					this.form.myRecord = record;
					this.form.loadRecord(record);

					//移动表格中的数据
					var grid = this.form.myGrid;
					if (grid) {
						grid.getSelectionModel().selectRow(cunidx);
						grid.fireEvent('rowclick', grid, cunidx);
					}
					
					//表单初始化
					self.initForm();
					return true;
				} else {
					JxHint.alert(jx.event.endrec);	//已经是最后一条记录了！
				}
			}
		}
		return false;
	},

	/**
	* public
	* 管理图文资料
	**/
	upload : function() {
		var keyid = this.getPkField().getValue();
		if (keyid == null || keyid.length == 0) {
			JxHint.alert(jx.event.nokey);	//当前记录没有主键值，不能操作！
			return;
		}
		
		var self = this;
		var pkcol = this.define.pkcol;
		var nodeid = this.define.nodeid;
		var tablename = this.define.tablename;
		var audit = this.form.get(this.define.auditcol);
		
		//过滤条件
		var where_sql = 'sys_attach.data_id = ? and sys_attach.table_name = ?';
		var where_type = 'string;string';
		var where_value = keyid+';'+tablename;
		
		//加载数据
		var hdcall = function(grid) {
			//显示数据
			JxUtil.delay(500, function(){
				//设置目标功能信息
				grid.attachDataId = keyid;
				grid.attachFunId = nodeid;
				grid.attachAudit = audit || '0';
				//删除GRID的自定义参数
				grid.on('beforedestroy', function(gp){
					gp.attachDataId = null;		delete gp.attachDataId;
					gp.attachFunId = null;		delete gp.attachFunId;
					gp.attachAudit = null;		delete gp.attachAudit;
					gp = null;
					return true;
				});
				Jxstar.loadData(grid, {where_sql:where_sql, where_value:where_value, where_type:where_type});
			});
		};

		var srcDefine = Jxstar.findNode('sys_attach');
		//显示数据
		Jxstar.showData({
			filename: srcDefine.gridpage,
			title: srcDefine.nodetitle, 
			pagetype: 'editgrid',
			nodedefine: srcDefine,
			callback: hdcall
		});
	},

	/**
	* public
	* 打印当前记录
	**/
	print : function() {
		JxPrint.showWindow(this.page.formNode);
	},
	
	/**
	* public 
	* 审批同意选择的记录。
	**/
	agree: function() {
		var self = this;
		var keyid = self.getPkField().getValue();
		if (keyid == null || keyid.length == 0) {
			JxHint.alert(jx.event.nokey);	//'当前记录没有主键值，不能操作！'
			return;
		}

		var hdcall = function() {
			var params = 'funid=wf_assign&pagetype=chkgrid&eventcode=execheck&check_funid='+ self.define.nodeid;
			params += '&keyid=' + keyid;
			//缺省审批同意
			var checkType = 'Y', checkDesc = jx.event.agree;	//同意
			params += '&check_type='+ checkType +'&check_desc='+ checkDesc;
			
			Request.postRequest(params, null);
		};
		//确定审批同意选择的记录吗？
		Ext.Msg.confirm(jx.base.hint, jx.event.agreeyes, function(btn) {
			if (btn == 'yes') hdcall();
		});
	},
	
	/**
	* public 
	* 弹出完成分配任务界面。
	**/
	check: function() {
		this.baseWf('check_work');
	},
	
	/**
	* public 
	* 弹出查看分配任务表格界面。
	**/
	showassign: function() {
		this.baseWf('check_assign');
	},
	
	/**
	* public 
	* 弹出查看流程执行历史表格界面。
	**/
	showhis: function() {
		this.baseWf('check_his');
	},
	
	/**
	* public 
	* 弹出查看流程图界面。
	**/
	showmap: function() {
		this.baseWf('check_map');
	},
	
	/**
	* private 
	* 查看流程信息的基础函数。
	**/
	baseWf: function(fileName) {
		var self = this;
		var keyid = self.getPkField().getValue();
		if (keyid == null || keyid.length == 0) {
			JxHint.alert(jx.event.nokey);	//'当前记录没有主键值，不能操作！'
			return;
		}
		
		var funId =  self.define.nodeid;
		var appData = {funId:funId, dataId:keyid};
		JxUtil.showCheckWindow(appData, fileName);
	}
});

})();