﻿/*!
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
  
/**
 * 表格页面常用事件实现类。
 * 
 * @author TonyTan
 * @version 1.0, 2010-01-01
 */

Ext.ns('Jxstar');
Jxstar.GridEvent = function(define) {
	this.define = define;
	this.grid = null;
	this.addEvents(
		/**
		* @param {Jxstar.GridEvent} this
		**/
		'beforecreate', 
		/**
		* @param {Jxstar.GridEvent} this
		**/
		'beforesave', 
		/**
		* @param {Jxstar.GridEvent} this
		* @param {JSON[]} data
		**/
		'aftersave', 
		/**
		* @param {Jxstar.GridEvent} this
		**/
		'beforedelete', 
		/**
		* @param {Jxstar.GridEvent} this
		* @param {JSON[]} data
		**/
		'afterdelete', 
		/**
		* @param {Jxstar.GridEvent} this
		**/
		'beforeaudit', 
		/**
		* @param {Jxstar.GridEvent} this
		* @param {JSON[]} data
		**/
		'afteraudit', 
		/**
		* @param {Jxstar.GridEvent} this
		**/
		'beforecopy', 
		/**
		* @param {Jxstar.GridEvent} this
		* @param {JSON[]} data
		**/
		'aftercopy',
		/**
		* @param {Jxstar.GridEvent} this
		* @param eventcode
		**/
		'beforecustom', 
		/**
		* @param {Jxstar.GridEvent} this
		* @param {JSON[]} data
		* @param eventcode
		**/
		'aftercustom'
	);

	Jxstar.GridEvent.superclass.constructor.call(this, define);
};

(function(){

Ext.extend(Jxstar.GridEvent, Ext.util.Observable, {
	/**
	* public 销毁事件对象
	**/
	myDestroy : function() {
		this.define = null;		delete this.define;
		this.grid = null;		delete this.grid;
	},

	/**
	* public
	* 设置事件对象操作的表格
	**/
	setPage : function(page) {
		this.grid = page;
	},
	
	/**
	* public
	* 设置数据权限类别值
	**/
	setType: function() {
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selectone(records)) return;
		
		//可以针对用户设置通用数据权限，也可以针对用户的单个功能设置数据权限
		var datatable = 'sys_user_data';
		
		//取用户数据权限表格
		var udgrid = Ext.getCmp('node_'+datatable+'_subeditgrid');
		if (udgrid == null) {
			datatable = 'sys_user_datax';
			udgrid = Ext.getCmp('node_'+datatable+'_subeditgrid');
			if (udgrid == null) {
				JxHint.alert(jx.event.noqxbg);	//没有找到用户数据权限设置表格！
				return;
			}
		}
		
		var udrecord = udgrid.getSelectionModel().getSelections()[0];
		var funfield = udrecord.get('sys_datatype__fun_field');
		var funvfield = udrecord.get('sys_datatype__fun_vfield');
		
		var value = records[0].get(funfield.replace('.', '__'));
		var display = records[0].get(funvfield.replace('.', '__'));
		udrecord.set(datatable+'__dtype_data', value);
		udrecord.set(datatable+'__display', display);
		
		//找到对话框，并关闭
		var parent = this.grid.ownerCt;
		while(parent != null && !parent.isXType('window')) {
			parent = parent.ownerCt;
		}
		if (parent != null) parent.close();
	},
	
	/**
	* public
	* 自定义通用事件
	**/
	customEvent : function(eventCode) {	
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selected(records)) return;
		
		if (this.fireEvent('beforecustom', this, eventCode) == false) return;
		
		var self = this;
		var hdcall = function() {
			//取选择记录的主键值
			var params = 'funid='+ self.define.nodeid;
			for (var i = 0; i < records.length; i++) {
				params += '&keyid=' + records[i].get(self.define.pkcol);
			}

			//设置请求的参数
			params += '&pagetype=grid&eventcode='+eventCode;

			//执行处理的内容
			var endcall = function(data) {
				//重新加载数据
				self.grid.getStore().reload();

				self.fireEvent('aftercustom', self, data, eventCode);
			};

			//发送请求
			Request.postRequest(params, endcall);
		};
		//确定执行当前操作吗？
		Ext.Msg.confirm(jx.base.hint, jx.base.doyes, function(btn) {
			if (btn == 'yes') hdcall();
		});
	},

	/**
	* public
	* 显示表单，表格记录双击时执行
	**/
	showForm : function() {
		var store = this.grid.getStore();
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selectone(records)) return;

		//显示表单数据
		Jxstar.showForm({define:this.define, grid:this.grid, record:records[0], store:store});
	},

	/**
	* public
	* 显示子表单
	**/
	showSubForm : function() {
		var self = this;
		var url = self.define.formpage;
		if (url == null || url.length == 0) return;
		
		var store = this.grid.getStore();
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selectone(records)) return;

		//显示表单数据
		Jxstar.openSubForm({
			filename: url,
			title: self.define.nodetitle, 
			grid: self.grid,
			pagetype: 'subform',
			parentNodeId: self.grid.gridNode.parentNodeId,
			record: records[0], store: store
		});
	},

	/**
	* public
	* 新增事件
	**/
	create : function() {
		var self = this;
		var store = this.grid.getStore();

		//显示表单数据
		Jxstar.showForm({define:self.define, grid:self.grid, record:null, store:store});
	},

	/**
	* public
	* 子表新增事件
	**/
	subcreate : function() {
		var self = this;
		var store = this.grid.getStore();

		//显示表单数据
		Jxstar.openSubForm({
			filename: self.define.formpage,
			title: self.define.nodetitle, 
			grid: self.grid,
			pagetype: 'subform',
			parentNodeId: self.grid.gridNode.parentNodeId,
			record: null, store: store
		});
	},

	/**
	* private
	* 提交时：检查是否存在已复核的记录；取消时：检查是否存在未复核记录
	**/
	checkAudit: function(auditval) {
		if (auditval == null) auditval = '1';

		var records = this.grid.getSelectionModel().getSelections();
		for (var i = 0; i < records.length; i++) {
			var state = records[i].get(this.define.auditcol);
			if (state == null) return false;
			
			if (auditval == '0') {
				if (state != '1'){
					JxHint.alert(jx.event.selaudit0);		//选择的记录中存在未复核的记录，不能操作！
					return true;
				}
			} else {
				if (state != '0' && state != '6'){
					JxHint.alert(jx.event.selaudit1);	//选择的记录中存在已复核的记录，不能操作！
					return true;
				}
			}
		}

		return false;
	},

	/**
	* public
	* 删除事件
	**/
	del : function() {
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selected(records)) return;

		if (this.checkAudit()) return;
		if (this.fireEvent('beforedelete', this) == false) return;

		var self = this;
		var hdcall = function() {
			//取选择记录的主键值
			var params = 'funid='+ self.define.nodeid;
			for (var i = 0; i < records.length; i++) {
				params += '&keyid=' + records[i].get(self.define.pkcol);
			}

			//设置请求的参数
			params += '&pagetype=grid&eventcode=delete';

			//删除后要处理的内容
			var endcall = function(data) {
				/*for (var i = 0; i < records.length; i++) {
					self.grid.getStore().remove(records[i]);
				}*/
				//重新加载数据
				self.grid.getStore().reload();

				self.fireEvent('afterdelete', self, data);
			};

			//发送请求
			Request.postRequest(params, endcall);
		};
		//确定删除选择的记录吗？
		Ext.Msg.confirm(jx.base.hint, jx.event.delyes, function(btn) {
			if (btn == 'yes') hdcall();
		});
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
		var keyids = [];
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selected(records)) return;

		//取复核值
		if (auditval == null) auditval = '1';

		if (this.checkAudit(auditval)) return;
		if (this.fireEvent('beforeaudit', this) == false) return;

		var self = this;
		var define = this.define;
		var hdcall = function() {
			//取选择记录的主键值
			var params = 'funid='+ self.define.nodeid;
			for (var i = 0; i < records.length; i++) {
				params += '&keyid=' + records[i].get(self.define.pkcol);
			}

			//复核事件代号
			var eventcode = (auditval == '0') ? 'unaudit' : 'audit';

			//设置请求的参数
			params += '&pagetype=grid&eventcode='+ eventcode +'&auditvalue='+auditval.toString();
			
			//提交后要处理的内容
			var endcall = function(data) {
				/*for (var i = 0; i < records.length; i++) {
					records[i].set(define.auditcol, auditval);
					records[i].commit();
				}*/
				self.fireEvent('afteraudit', self, data);
				
				//重新加载数据
				self.grid.getStore().reload();
			};

			//发送请求
			Request.postRequest(params, endcall);
		};

		var shint = jx.event.audityes;	//确定复核选择的记录吗？
 		if (auditval == '0') {
			shint = jx.event.auditno;	//确定反复核选择的记录吗？
 		};
		Ext.Msg.confirm(jx.base.hint, shint, function(btn) {
			if (btn == 'yes') hdcall();
		});
	},

	/**
	* public
	* GRID编辑新增事件
	**/
	editCreate : function() {
		//新建一个初始化的记录对象
		var record = this.createRecord();
		var store = this.grid.getStore();

		//添加记录
		this.grid.stopEditing();
		store.insert(0, record);
		this.grid.startEditing(0, 0);

		if (this.fireEvent('beforecreate', this) == false) return;
	},

	/**
	* private
	* 新增一个数据记录对象。
	**/
	createRecord : function() {
		//列模型
		var cm = this.grid.getColumnModel();
		//数据存储
		var store = this.grid.getStore();
		var record = new (store.reader.recordType)({});
		var cols = record.fields.keys;

		//字段定义开始位置
		var ipos = 1;
		if (cm.config[0].id == 'numberer') {
			if (cm.config[1].id == 'checker') ipos = 2;
		} else {
			if (cm.config[0].id != 'checker') ipos = 0;
		}

		//给每个字段给缺省值
		for (var i = 0; i < cols.length; i++){
			var defaultval = cm.config[i+ipos].defaultval;
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
	* GRID编辑删除事件
	**/
	editDelete : function() {
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selected(records)) return;

		if (this.checkAudit()) return;
		if (this.fireEvent('beforedelete', this) == false) return;

		var self = this;
		var pkcol = self.define.pkcol;
		var store = self.grid.getStore();
		var hdcall = function() {
			//取选择记录的主键值
			var keys = '';
			for (var i = 0; i < records.length; i++) {
				//如果是一条空记录，直接删除
				var k = records[i].get(pkcol);
				if (k == null || k.length == 0) {
					store.remove(records[i]);
				} else {
					keys += '&keyid=' + records[i].get(pkcol);
				}
			}
			//如果是空记录，不需要复核后台
			if (keys.length == 0) return true;

			//设置请求的参数
			var params = 'funid='+ self.define.nodeid + keys;
			params += '&pagetype=editgrid&eventcode=delete_eg';

			//删除后要处理的内容
			var endcall = function(data) {
				/*for (var i = 0; i < records.length; i++) {
					self.grid.getStore().remove(records[i]);
				}*/
				self.fireEvent('afterdelete', self, data);
				
				//重新加载数据
				self.grid.getStore().reload();
			};

			//发送请求
			Request.postRequest(params, endcall);
		};
		//确定删除选择的记录吗？
		Ext.Msg.confirm(jx.base.hint, jx.event.delyes, function(btn) {
			if (btn == 'yes') hdcall();
		});
	},

	/**
	* public
	* GRID编辑复制事件
	**/
	editCopy : function() {
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selected(records)) return;

		if (this.fireEvent('beforecopy', this) == false) return;

		//取选择记录的主键值
		var keys = '';
		var pkcol = this.define.pkcol;
		for (var i = 0; i < records.length; i++) {
			keys += '&keyid=' + records[i].get(pkcol);
		}

		var self = this;
		var hdcall = function(text) {
			//取选择记录的主键值
			var params = 'funid='+ self.define.nodeid;
			params += keys + '&copynum='+text;
			
			//添加树型参数
			if (self.grid.treeParam) {
				var parentId = self.grid.treeParam.parentId;
				var levelCol = self.grid.treeParam.levelCol;
				params += '&parentId=' + parentId + '&levelCol=' + levelCol;
			}
			
			//设置请求的参数
			params += '&pagetype=grid&eventcode=copy_eg';

			//复制后刷新数据
			var endcall = function(data) {
				self.fireEvent('aftercopy', self, data);
				
				self.grid.getStore().reload();
			};

			//发送请求
			Request.postRequest(params, endcall);
		};
		//请输入复制记录条数
		Ext.MessageBox.prompt(jx.base.hint, jx.event.copynum, function(btn, text) {
			if (btn != 'ok') return;
			if (text.length == 0 || isNaN(text)) {
				JxHint.alert(jx.event.nocopynum);
				return;
			}
			
			if (text < 1) text = '1';
			hdcall(text);
		},null,null,'1');
	},

	/**
	* public
	* GRID编辑保存事件
	**/
	editSave : function() {
		var cm = this.grid.getColumnModel();
		var store = this.grid.getStore();
		var mrow = store.getModifiedRecords();
		if (mrow.length == 0) {
			//JxHint.alert(jx.event.nomodify);	//没有修改记录，不需要保存！
			return;
		}
		if (this.fireEvent('beforesave', this) == false) return;

		//取选择记录的主键值
		var keys = '';
		var pkcol = this.define.pkcol;

		var self = this;
		for (var i = 0, n = mrow.length; i < n; i++) {
			var record = mrow[i];
			var fields = record.fields.keys;
			//取选择记录的主键值
			keys += '&keyid=' + record.get(pkcol);

			for (var j = 0; j < fields.length; j++) {
				var name = fields[j];
				var value = record.data[name];
				if (value == null) value = '';

				var colIndex = cm.findColumnIndex(name);
				var rowIndex = store.indexOfId(record.id);
				var editor = cm.getCellEditor(colIndex, rowIndex);
				//隐藏字段为空
				if (editor == null) continue;

				var field = editor.field;
				if (field != null && !field.validateValue(value)) {
					JxHint.alert(jx.event.datavalid);	//请确保输入的数据正确完整。
					self.grid.startEditing(rowIndex, colIndex);
					return;
				}
			}
		}
		
		var params = 'funid='+ this.define.nodeid;
		//添加外键值
		var fkn = this.grid.fkName;
		if (fkn && fkn.length > 0) {
			var fkv = this.grid.fkValue ? this.grid.fkValue : '';
			if (fkv.length == 0) {
				JxHint.alert(jx.event.nofkv);	//当前记录没有外键值，不能保存！
				return;
			}
			params += '&fkValue=' + fkv;
		}
		//添加树型参数
		if (this.grid.treeParam) {
			var parentId = this.grid.treeParam.parentId;
			var levelCol = this.grid.treeParam.levelCol;
			params += '&parentId=' + parentId + '&levelCol=' + levelCol;
		}

		//设置请求的参数
		params += keys + '&pagetype=editgrid&eventcode=save_eg';
		Ext.each(mrow, function(item) {
			params += '&' + Ext.urlEncode(item.data);
		});

		//保存后要处理的内容
		var self = this;
		var endcall = function(data) {
			/*if (data) {
				//把新增的记录ID写到界面上
				Ext.each(data, function(item) {
					//JxHint.alert(item + ':' + item.index + ':' + item.keyid);
					//保存时没有值
					if (item.keyid != null) {
						var pkcol = self.define.pkcol;
						var record = mrow[item.index];
						record.set(pkcol, item.keyid);
					}
				});
			}*/
			//复核数据修改
			store.commitChanges();
			
			self.fireEvent('aftersave', self, data);

			//重新加载数据
			self.grid.getStore().reload();
		};

		//发送请求
		Request.postRequest(params, endcall);
	},


	/**
	* public
	* 打开数据导入窗口
	**/
	dataImport : function() {
		var self = this;
		//取外键值
		var fkValue = this.grid.fkValue; 
		
		//取路由定义信息，格式：{srcNodeId:"sys_event",whereSql:"fun_id='sysevent'",whereType:"",whereValue:""}
		var route = RuleData[this.define.nodeid][0];
		var srcNodeId = route.srcNodeId;
		var layout = route.layout;
		var whereSql = route.whereSql||'';
		var whereType = route.whereType||'';
		var whereValue = route.whereValue||'';
		
		//解析过滤语句中的外键值，在导入明细记录时有用，注意{FKEYID}必须是第一参数；
		if (whereSql.indexOf('{FKEYID}') >= 0) {
			whereSql = whereSql.replace('\{FKEYID\}', '?');
			whereType = whereType.length == 0 ? 'string' : 'string;'+whereType;
			whereValue = whereValue.length == 0 ? fkValue : fkValue+';'+whereValue;
		}
		
		//扩展过滤语句与参数
		if (typeof this.dataImportParam == 'function') {
			var options = this.dataImportParam();
			if (!options) return;
			
			if (options.whereSql != null && options.whereSql.length > 0) {
				if (whereSql.length > 0) {
					whereSql += ' and (' + options.whereSql + ')';
				} else {
					whereSql = options.whereSql;
				}
			}
			
			if (options.whereType != null && options.whereType.length > 0) {
				if (whereType.length > 0) whereType += ';';
				whereType += options.whereType;
			}
			
			if (options.whereValue != null && options.whereValue.length > 0) {
				if (whereValue.length > 0) whereValue += ';';
				whereValue += options.whereValue;
			}
		}
		//JxHint.alert(whereSql);
		//加载数据
		var hdcall = function(grid) {
			//显示数据
			JxUtil.delay(500, function(){
				//处理树形页面的情况
				if (!grid.isXType('grid')) {
					grid = grid.getComponent(1).getComponent(0);
				}

				//设置外键值与目标功能ID
				grid.destParentId = fkValue;
				grid.destNodeId = self.define.nodeid;
				grid.destGrid = self.grid;
				//删除GRID的自定义参数
				grid.on('beforedestroy', function(gp){
					gp.destParentId = null;		delete gp.destParentId;
					gp.destNodeId = null;		delete gp.destNodeId;
					gp.destGrid = null;			delete gp.destGrid;
					gp = null;
					return true;
				});

				Jxstar.loadData(grid, {where_sql:whereSql, where_value:whereValue, where_type:whereType});
			});
		};

		//显示数据窗口
		var srcDefine = Jxstar.findNode(srcNodeId);
		//显示导入布局
		if (layout == null || layout.length == 0) {
			layout = srcDefine.gridpage;
		}
		Jxstar.showData({
			pagetype: 'import',
			filename: layout,
			nodedefine: srcDefine,
			title: jx.base.imp+'--'+srcDefine.nodetitle, 
			callback: hdcall
		});
	},

	/**
	* public
	* 执行数据导入事件
	**/
	imp : function() {
		var self = this;
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selected(records)) return;

		//取选择记录的主键值
		var keys = '';
		var pkcol = this.define.pkcol;
		for (var i = 0; i < records.length; i++) {
			keys += '&keyid=' + records[i].get(pkcol);
		}

		//目标功能外键值
		var parentId = this.grid.destParentId;
		//目标功能ID
		var destFunId = this.grid.destNodeId;
		//设置请求的参数
		var params = 'funid='+ this.define.nodeid + '&destfunid=' + destFunId + keys;
		params += '&parentId='+ parentId +'&pagetype=import&eventcode=import';
		
		//导入后刷新数据
		var endcall = function(data) {
			self.grid.getStore().reload();

			var destGrid = self.grid.destGrid;
			if (destGrid != null) {
				destGrid.getStore().reload();
			}
		};

		//发送请求
		Request.postRequest(params, endcall);
	},
	
	/**
	* public
	* 清除选择记录，创建一条空记录执行双击事件，
	**/
	clearRecord: function() {
		this.grid.getSelectionModel().clearSelections();
		
		this.grid.fireEvent('rowclick', this.grid);
		this.grid.fireEvent('rowdblclick', this.grid);
	},
	
	/**
	* public
	* 选择记录，执行双击事件
	**/
	selRecord: function() {
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selectone(records)) return;
		
		this.grid.fireEvent('rowclick', this.grid);
		this.grid.fireEvent('rowdblclick', this.grid);
	},
	
	/**
	* public
	* 直接添加附件，需要指定附件所属的字段名
	**/
	addAttach: function(attachField) {
		//需要传递到后台的参数值
		var dataId, dataFunId, dataField;
		
		//取当前功能ID
		var nodeid = this.define.nodeid;
		//如果是图文附件功能的新增按钮
		if (nodeid == 'sys_attach') {
			//取来源数据记录ID
			dataId = this.grid.attachDataId || '';
			//取来源数据功能ID
			dataFunId = this.grid.attachFunId || '';
			//取来源数据字段名称，如图片字段
			dataField = attachField || '';
		} else {
			var records = this.grid.getSelectionModel().getSelections();
			if (!JxUtil.selectone(records)) return;
			var pkcol = this.define.pkcol;
			
			dataId = records[0].get(pkcol);
			dataFunId = nodeid;
			dataField = attachField || '';
		}
		if (dataId.length == 0) {
			JxHint.alert(jx.event.noup);	//没有选择上传附件的记录
			return;
		}
		if (dataFunId.length == 0) {
			JxHint.alert(jx.event.noupfun);	//无法识别上传附件的功能！
			return;
		}
		//-----------------传递参数的判断-------------------
	
		var queryForm = new Ext.form.FormPanel({
				layout:'form', 
				labelAlign:'right',
				labelWidth:80,
				border:false, 
				baseCls:'x-plain',
				autoHeight: true,
				bodyStyle: 'padding: 20px 10px 0 10px;',
				defaults: {
					anchor: '95%',
					allowBlank: false,
					msgTarget: 'side'
				},
				items: [
				{
					xtype: 'fileuploadfield',
					fieldLabel: jx.event.selfile,	//选择文件
					name: 'attach_path',
					labelSeparator:'*', 
					buttonText: '',
					buttonCfg: {
						iconCls: 'upload_icon'
					},
					listeners:{
						fileselected: function(f, path) {
							var len = path.length;
							if (len > 0) {
								var pos = path.lastIndexOf('\\');
								if (pos >= 0) {
									path = path.substr(pos+1, len);
								}
							}
							queryForm.getForm().findField('attach_name').setValue(path);
						}
					}
				},{
					xtype: 'textfield',
					fieldLabel: jx.event.upname,	//附件名称
					name: 'attach_name',
					labelSeparator:'*', maxLength:50
				}
				]
			});

		//创建对话框
		var self = this;
		var win = new Ext.Window({
			title:jx.event.uptitle,	//上传附件
			layout:'fit',
			width:400,
			height:160,
			resizable: false,
			modal: true,
			closeAction:'close',
			items:[queryForm],

			buttons: [{
				text:jx.base.ok,	//确定
				handler:function(){
					var form = queryForm.getForm();
					if (!form.isValid()) return;
					
					//上传参数
					var params = 'funid=sys_attach&pagetype=editgrid&eventcode=create';
						params += '&attach_field='+ dataField +'&dataid='+ dataId +'&datafunid='+ dataFunId;
					
					//上传成功后关闭窗口并刷新数据
					var hdCall = function() {
						win.close();
						if (nodeid == 'sys_attach') {
							self.grid.getStore().reload();
						}
					};
					//上传附件
					Request.fileRequest(form, params, hdCall);
				}
			},{
				text:jx.base.cancel,	//取消
				handler:function(){win.close();}
			}]
		});
		win.show();
	}, 

	/**
	* public
	* 管理图文资料
	**/
	upload : function() {
		var records = this.grid.getSelectionModel().getSelections();
		if (!JxUtil.selectone(records)) return;
		
		var self = this;
		var pkcol = this.define.pkcol;
		var nodeid = this.define.nodeid;
		var keyid = records[0].get(pkcol);
		var audit = records[0].get(this.define.auditcol);
		
		var tablename = this.define.tablename;
		
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
	* 另存结果集为xls文件
	**/
	exptxt : function() {
		JxExport.showWindow(this.grid.gridNode);
	},

	/**
	* public
	* 打印当前记录
	**/
	print : function() {
		JxPrint.showWindow(this.grid.gridNode);
	},
	
	/**
	* public 
	* 批量审批同意选择的记录。
	**/
	agree: function() {
		var self = this;
		var records = self.grid.getSelectionModel().getSelections();
		if (!JxUtil.selected(records)) return;

		var hdcall = function() {
			var params = 'funid=wf_assign&pagetype=chkgrid&eventcode=execheck&check_funid='+ self.define.nodeid;
			for (var i = 0; i < records.length; i++) {
				params += '&keyid=' + records[i].get(self.define.pkcol);
			}
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
		var records = self.grid.getSelectionModel().getSelections();
		if (!JxUtil.selectone(records)) return;
		
		var funId =  self.define.nodeid;
		var dataId = records[0].get(self.define.pkcol);
		
		var appData = {funId:funId, dataId:dataId};
		JxUtil.showCheckWindow(appData, fileName);
	}

});

})();
