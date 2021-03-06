﻿Jxstar.currentPage = function() {
	var config = {param:{},initpage:function(page, define){},eventcfg:{}};

	var cols = [
	{col:{header:'*显示值', width:132, sortable:true, editable:true, hcss:'color:#0000ff;',
		editor:new Ext.form.TextField({
			maxLength:50, allowBlank:false
		})}, field:{name:'rpt_head__col_code',type:'string'}},
	{col:{header:'*字段标签', width:122, sortable:true, editable:true, hcss:'color:#0000ff;',
		editor:new Ext.form.TextField({
			maxLength:50, allowBlank:false
		})}, field:{name:'rpt_head__display',type:'string'}},
	{col:{header:'显示位置', width:100, sortable:true, editable:true, hcss:'color:#3039b4;',
		editor:new Ext.form.TextField({
			maxLength:20
		})}, field:{name:'rpt_head__col_pos',type:'string'}},
	{col:{header:'表头ID', width:100, sortable:true, hidden:true}, field:{name:'rpt_head__head_id',type:'string'}},
	{col:{header:'报表ID', width:100, sortable:true, hidden:true}, field:{name:'rpt_head__report_id',type:'string'}}
	];
	
	config.param = {
		cols: cols,
		sorts: null,
		hasQuery: '0',
		isedit: '0',
		isshow: '0',
		funid: 'rpt_head'
	};
	
	config.initpage = function(gridNode){
		var grid = gridNode.page;
		grid.on('rowclick', function(g, rowindex, e) {
			var seldiv = Ext.get('sel_rpttddiv');
			if (seldiv != null) {
				var record = g.getStore().getAt(rowindex);
				
				seldiv.oldRecord = seldiv.curRecord;
				seldiv.curRecord = record;
				seldiv.curTable = 'rpt_head';
			}
		});
	};
		
	return new Jxstar.GridNode(config);
}