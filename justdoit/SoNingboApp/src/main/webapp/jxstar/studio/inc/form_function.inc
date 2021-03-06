	config.initpage = function(formNode){
		var event = formNode.event;
		event.on('beforecreate', function(event) {
			var page = event.form;
			var funTree = Ext.getCmp('tree_sys_fun_base');
			var moduleId = funTree.selModel.selNode.id;
			//JxHint.alert(funTree.selModel.selNode.id);
			page.findField('fun_base__module_id').setValue(moduleId);
		});
		
		//如果FROM_SQL值为空，在修改table_name时自动添加SQL
		var form = formNode.page.getForm();
		var tblField = form.findField('fun_base__table_name');
		tblField.on('change', function(field){
			var fs = form.findField('fun_base__from_sql').getValue();
			if (fs.length == 0) {
				form.set('fun_base__from_sql', 'from ' + tblField.getValue());
			}
		});
	};