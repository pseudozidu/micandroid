﻿Jxstar.currentPage = function() {
	var config = {param:{},initpage:function(page, define){},eventcfg:{}};

	var cols = [
	{col:{header:'功能名称', width:159, sortable:true}, field:{name:'fun_base__fun_name',type:'string'}},
	{col:{header:'设置ID', width:100, sortable:true, hidden:true}, field:{name:'sys_user_funx__user_funx_id',type:'string'}},
	{col:{header:'用户ID', width:100, sortable:true, hidden:true}, field:{name:'sys_user_funx__user_id',type:'string'}},
	{col:{header:'功能ID', width:100, sortable:true, hidden:true}, field:{name:'sys_user_funx__fun_id',type:'string'}}
	];
	
	config.param = {
		cols: cols,
		sorts: null,
		hasQuery: '0',
		isedit: '0',
		isshow: '0',
		funid: 'sys_user_funx'
	};
	
	config.eventcfg = {		
		dataImportParam: function() {
			var userId = this.grid.fkValue;			var options = {				whereSql: 'fun_id not in (select fun_id from sys_user_funx where user_id = ?)',				whereValue: userId,				whereType: 'string'			};			return options;		},				setData: function(){			var records = this.grid.getSelectionModel().getSelections();			if (!JxUtil.selectone(records)) return;			var pkcol = this.define.pkcol;			var selfunid = records[0].get(pkcol);						//过滤条件			var where_sql = 'sys_user_datax.user_funx_id = ?';			var where_type = 'string';			var where_value = selfunid;						//加载数据			var hdcall = function(grid) {				//显示数据				JxUtil.delay(500, function(){					//设置外键值					grid.fkValue = where_value;					Jxstar.loadData(grid, {where_sql:where_sql, where_value:where_value, where_type:where_type});				});			};			var srcDefine = Jxstar.findNode('sys_user_datax');			//显示数据			Jxstar.showData({				filename: srcDefine.gridpage,				title: srcDefine.nodetitle, 				pagetype: 'subgrid',				nodedefine: srcDefine,				callback: hdcall			});		}		
	};
		
	return new Jxstar.GridNode(config);
}