	config.eventcfg = {

		createFun: function(){
			var self = this;
			var hintcall = function(btn, text) {
				if (btn != 'ok') return;

				var hdcall = function() {
					self.grid.getStore().reload();
				};
				
				var params = 'funid=sys_fun_base&cfunid='+ text +'&pagetype=editgrid&eventcode=create';
				//添加树型参数
				if (self.grid.treeParam) {
					var parentId = self.grid.treeParam.parentId;
					params += '&cmodid=' + parentId;
				}
				
				//发送请求
				Request.postRequest(params, hdcall);
			};

			//'请输入新功能ID'
			Ext.MessageBox.prompt(jx.base.hint, jx.fun.newid, hintcall);
		},				copyFun: function(){			var records = this.grid.getSelectionModel().getSelections();			if (!JxUtil.selectone(records)) return;			var selfunid = records[0].get('fun_base__fun_id');					var self = this;			var hintcall = function(btn, text) {				if (btn != 'ok') return;				var hdcall = function() {					self.grid.getStore().reload();				};								var params = 'funid=sys_fun_base&oldfunid='+ selfunid +'&newfunid='+ text +'&pagetype=editgrid&eventcode=copy';								//发送请求				Request.postRequest(params, hdcall);			};			//'请输入新功能ID'			Ext.MessageBox.prompt(jx.base.hint, jx.fun.newid, hintcall);		},
			
		createNode: function(){
			//取选择记录的主键值
			var params = 'funid='+ this.define.nodeid;
			
			//设置请求的参数
			params += '&pagetype=grid&eventcode=createNode&projectpath=' + Jxstar.session['project_path'];

			//生成文件后自动加载该文件
			var hdcall = function() {
				Request.loadJS('/public/data/NodeDefine.js');
			};

			//发送请求
			Request.postRequest(params, hdcall);
		},

		createRule: function(){
			//取选择记录的主键值
			var params = 'funid='+ this.define.nodeid;
			
			//设置请求的参数
			params += '&pagetype=grid&eventcode=createRule&projectpath=' + Jxstar.session['project_path'];

			//生成文件后自动加载该文件
			var hdcall = function() {
				Request.loadJS('/public/data/RuleData.js');
			};

			//发送请求
			Request.postRequest(params, hdcall);
		},
		
		createTree: function(){
			//取选择记录的主键值
			var params = 'funid='+ this.define.nodeid;
			
			//设置请求的参数
			params += '&pagetype=grid&eventcode=createTree&projectpath=' + Jxstar.session['project_path'];

			//生成文件后自动加载该文件
			var hdcall = function() {
				Request.loadJS('/public/data/TreeData.js');
			};

			//发送请求
			Request.postRequest(params, hdcall);
		},

		setFunext: function(){			var records = this.grid.getSelectionModel().getSelections();			if (!JxUtil.selectone(records)) return;			var selfunid = records[0].get('fun_base__fun_id');						//加载Form数据			var hdcall = function(page) {				//设置外键键				page.getForm().fkName = 'fun_ext__fun_id';				page.getForm().fkValue = selfunid;								//加载显示数据				var options = {					where_sql: 'fun_ext.fun_id = ?',					where_type: 'string',					where_value: selfunid,					callback: function(data) {						//如果没有数据则执行新增						if (data.length == 0) {							page.formNode.event.create();						} else {							var r = page.formNode.event.newRecord(data[0]);														page.getForm().myRecord = r;							page.getForm().loadRecord(r);						}					}				};				Jxstar.queryData('fun_ext', options);			};						//显示数据			var define = Jxstar.findNode('fun_ext');			Jxstar.showData({				filename: define.formpage,				title: define.nodetitle,				width: 500,				height: 200,				callback: hdcall			});		},		setFunTree: function(){			var records = this.grid.getSelectionModel().getSelections();			if (!JxUtil.selectone(records)) return;			//过滤条件			var where_sql = 'fun_tree.fun_id = ?';			var where_type = 'string';			var where_value = records[0].get('fun_base__fun_id');						//加载数据			var hdcall = function(layout) {				//显示数据				JxUtil.delay(500, function(){					var grid = layout.getComponent(0).getComponent(0);									//设置外键值					grid.fkValue = where_value;					Jxstar.loadData(grid, {where_sql:where_sql, where_value:where_value, where_type:where_type});				});			};			//显示数据			var define = Jxstar.findNode('fun_tree');			Jxstar.showData({				filename: define.layout,				title: define.nodetitle,				pagetype: 'subgrid',				nodedefine: define,				callback: hdcall			});		},				setFunRoute: function(){			var records = this.grid.getSelectionModel().getSelections();			if (!JxUtil.selectone(records)) return;			//过滤条件			var where_sql = 'fun_rule_route.fun_id = ?';			var where_type = 'string';			var where_value = records[0].get('fun_base__fun_id');						//加载数据			var hdcall = function(layout) {				//显示数据				JxUtil.delay(500, function(){					var grid = layout.getComponent(0).getComponent(0);									//设置外键值					grid.fkValue = where_value;					Jxstar.loadData(grid, {where_sql:where_sql, where_value:where_value, where_type:where_type});				});			};			//显示数据			var define = Jxstar.findNode('rule_route');			Jxstar.showData({				filename: define.layout,				title: define.nodetitle,				pagetype: 'subgrid',				nodedefine: define,				callback: hdcall			});		},				setFunSql: function(){			var records = this.grid.getSelectionModel().getSelections();			if (!JxUtil.selectone(records)) return;			//过滤条件			var where_sql = 'fun_rule_sql.src_funid = ? and fun_rule_sql.route_id = ?';			var where_type = 'string;string';			var where_value = records[0].get('fun_base__fun_id') + ';noroute';						//加载数据			var hdcall = function(layout) {				//显示数据				JxUtil.delay(500, function(){					var grid = layout.getComponent(0).getComponent(0);									//清除外键设置，在form的initpage方法中处理来源功能ID为外键值					grid.fkValue = 'noroute';					Jxstar.loadData(grid, {where_sql:where_sql, where_value:where_value, where_type:where_type});				});			};			//显示数据			var define = Jxstar.findNode('rule_sqlm');			Jxstar.showData({				filename: define.layout,				title: define.nodetitle,				pagetype: 'subgrid',				nodedefine: define,				callback: hdcall			});		},				setCodeRule: function(){			var records = this.grid.getSelectionModel().getSelections();			if (!JxUtil.selectone(records)) return;			var selfunid = records[0].get('fun_base__fun_id');						//加载Form数据			var hdcall = function(page) {				//设置外键键				page.getForm().fkName = 'sys_coderule__fun_id';				page.getForm().fkValue = selfunid;								//加载显示数据				var options = {					where_sql: 'sys_coderule.fun_id = ?',					where_type: 'string',					where_value: selfunid,					callback: function(data) {						//如果没有数据则执行新增						if (data.length == 0) {							page.formNode.event.create();						} else {							var r = page.formNode.event.newRecord(data[0]);														page.getForm().myRecord = r;							page.getForm().loadRecord(r);						}					}				};				Jxstar.queryData('sys_coderule', options);			};						//显示数据			var define = Jxstar.findNode('sys_coderule');			Jxstar.showData({				filename: define.formpage,				title: define.nodetitle,				width: 650,				height: 250,				callback: hdcall			});		},				createReport: function(reportType) {			var records = this.grid.getSelectionModel().getSelections();			if (!JxUtil.selected(records)) return;			for (var i = 0, n = records.length; i < n; i++) {				var funid = records[i].get('fun_base__fun_id');				var params = 'funid=sys_fun_base&selfunid='+ funid +'&pagetype=grid&eventcode=createreport&reportType=' + reportType;				//发送请求				Request.postRequest(params, null);			}		}	};
	
	config.initpage = function(gridNode){		var event = gridNode.event;		event.on('beforecreate', function(event) {			var page = event.grid;			var funTree = Ext.getCmp('tree_sys_fun_base');			var moduleId = funTree.selModel.selNode.id;			page.getStore().getAt(0).set('fun_base__module_id', moduleId);		});		var grid = gridNode.page;		grid.on('rowdblclick', function(g, n, e) {			event.showForm();		});	};	/**	config.toolext = function(node, tbar, extItems){		var event = node.event;				var i = extItems.length;		if (i > 0) {			extItems[i++] = '-';		}		extItems[i++] = {				text:jx.fun.newgrid,	//'生成表格报表'				handler:function() {event.createReport('grid');}			};		extItems[i++] = {				text:jx.fun.newform,	//'生成表单报表'				handler:function() {event.createReport('form');}			};		extItems[i++] = {				text:jx.fun.newmain,	//'生成主从报表'				handler:function() {event.createReport('formgrid');}			};	};**/