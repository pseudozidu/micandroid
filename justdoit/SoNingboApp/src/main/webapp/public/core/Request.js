﻿/*!
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
 
/**
 * 请求对象：负责动态异步加载js文件；异步执行后台请求；下载文件等。
 * 同步请求可以采用JxUtil.loadJS()，或直接采用XmlRequest对象。
 * 
 * @author TonyTan
 * @version 1.0, 2010-01-01
 */
Request = {};

(function(){
	if (Ext.LoadMask) {
		Ext.LoadMask.prototype.msg = jx.req.exeing;	//'正在执行...'
	}

	Ext.apply(Request, {
		/**
		 * 创建Request对象，下面提供的方法全部是静态方法。
		 **/
		loadXML: function(data) {
			if (data == null || data.length == 0) return;
			
			var xdoc = null;
			if (typeof(DOMParser) == 'undefined'){
				xdoc = new ActiveXObject("Microsoft.XMLDOM");
				xdoc.async = 'false';
				xdoc.loadXML(data);
			}else{
				var parser = new DOMParser();
				xdoc = parser.parseFromString(data, "application/xml");
			}

			return xdoc;
		},

		/**
		 * 异步加载script文件
		 **/
		loadJS: function(url, hdCall) {
			if (url == null || url.length == 0) {
				JxHint.alert('Request.loadJS'+jx.req.nopath);	//'访问的路径为空！'
				return;
			}
			//第1一个字符应该是/
			if (url.charAt(0) != '/') url = '/' + url;
			
			Ext.Ajax.request({
				method: 'GET',
				url: Jxstar.path + url,
				success: function(response) {
					//需要执行JS脚本，不然不能调用JS中的对象
					var f = eval(response.responseText);

					//执行外部的回调函数
					if (hdCall != null) hdCall(f);
					f = null;
					//清除原函数
					Jxstar.currentPage = null;
				},
				failure: function(response) {
					JxUtil.errorResponse(response);
				}
				//设置js请求支持缓存
				//disableCaching: false
			});

		},

		/**
		 * 异步加载jsp文件
		 **/
		loadJsp: function(url) {
			if (url == null || url.length == 0) {
				JxHint.alert('Request.loadJsp'+jx.req.nopath);	//'访问的路径为空！'
				return;
			}
			Ext.get('main_body').load({
				method: 'GET',
				url:url, 
				scripts:true
			});
		},

		/**
		* 异步发送按钮事件请求
		**/
		postRequest: function(params, hdCall, options) {
			options = options || {};
			options.action = 'event';
			this.asynchRequest(params, hdCall, options);
		},

		/**
		* 异步发送查询事件请求
		**/
		dataRequest: function(params, hdCall, options) {
			options = options || {};
			options.action = 'query';
			this.asynchRequest(params, hdCall, options);
		},

		/**
		* 异步发送事件请求
		* params 是post参数，hdCall 是执行成功的回调函数
		* options 扩展选项参数有：
		* wait 是否显示执行进度条，值有：true|false
		* type 是响应的数据类型有：xml|json，默认为json
		* action 有event与query
		* query_type 查询类型，缺省为0，0为普通查询、1为高级查询
		* has_page 是否加分页处理，缺省为0，0为不加分页查询、1为加分页查询
		* 
		* result的参数有：
		* success -- 执行是否成功
		* message -- 提示信息
		* data -- 响应的数据
		**/
		asynchRequest: function(params, hdCall, options) {
			SessionTimer.resetTimer();
			
			var myMask = null;
			var useWait = options.wait || false;
			var dataType = options.type || 'json';
			var action = options.action;
			var sync = options.sync || false;
			
			//添加用户名判断
			params += '&user_id=' + Jxstar.session['user_id'];
			//添加返回数据类型
			params += '&dataType=' + dataType;
			
			if (action == 'query') {
				//添加查询类型参数
				params += '&query_type=' + (options.query_type||'0');
				//添加是否加分页处理参数
				params += '&has_page=' + (options.has_page||'0');
			}

			//是否命令事件，只有命令事件才提示进度条
			useWait = useWait || (action == 'event');
			if (useWait) {
				myMask = new Ext.LoadMask(Ext.getBody());
				myMask.show();
			}
			Ext.Ajax.request({
				method: 'POST',
				url: Jxstar.path + '/commonAction.do',
				success: function(response) {
					var result = {};
					if (dataType == 'xml') {
						//把xml响应对象解析为JSON对象
						var xdoc = response.responseXML;
						var query = Ext.DomQuery;
						result.data = query.selectValue('data', xdoc);
						result.success = query.selectValue('success', xdoc);
						result.message = query.selectValue('message', xdoc);
					} else {
						//把响应字符串解析为JSON对象
						result = Ext.decode(response.responseText);
					}
					if (result.success == true || result.success == 'true') {
						var msg = result.message;
						if (action == 'event') {
							if (msg.length == 0) msg = jx.req.success;	//'执行成功！'
							JxHint.hint(msg);
						}

						//成功执行外部的回调函数
						if (hdCall != null) hdCall(result.data);
					} else {
						var msg = result.message;
						if (msg.length == 0) msg = jx.req.faild;		//'执行失败！'
						JxHint.hint(msg);
					}
					
					if (useWait && myMask) {myMask.hide(); myMask = null;}
				},
				failure: function(response) {
					if (useWait && myMask) {myMask.hide(); myMask = null;}
					JxUtil.errorResponse(response);
				},
				params: params
			});
		},
		
		/**
		* 异步发送输出文件请求，响应网页更新到HTML对象中。
		* targetId -- 文件显示的控件ID
		* params -- 读取文件的参数
		* hdCall -- 回调函数，参数有：el, success, response, options
		**/
		fileUpdate: function(targetId, params, hdCall) {
			SessionTimer.resetTimer();
			
			params = params||{};
			//添加用户名判断
			params += '&user_id=' + Jxstar.session['user_id'];
			//添加数据类型
			params += '&dataType=byte';
		
			Ext.get(targetId).load({
				method: 'POST',
				url:Jxstar.path + '/fileAction.do',
				scripts:true, 
				nocache:true,//不缓存文件
				callback:function(el, success, response, options) {
					if (success == true) {
						if (hdCall != null) hdCall(response.responseText);
					} else {
						JxUtil.errorResponse(response);
					}
				},
				params:params
			});
		},
		
		/**
		* 异步发送下载文件的请求。
		* params -- 读取文件的参数
		**/
		fileDown: function(params) {
			SessionTimer.resetTimer();
			
			params = params||{};
			//添加用户名判断
			params += '&user_id=' + Jxstar.session['user_id'];
			//添加数据类型
			params += '&dataType=byte';
			
			//输出iframe下载文件
			var href = Jxstar.path + '/fileAction.do?' + params;
			var iframe = document.getElementById('frmhidden');
			iframe.src = href;
		},
		
		/**
		* 异步发送上传文件的请求
		* form -- 表单对象BasicForm
		* params -- 读取文件的参数
		* hdCall -- 回调函数，参数有：result.data
		**/
		fileRequest: function(form, params, hdCall) {
			SessionTimer.resetTimer();
			
			params = params||{};
			//添加用户名判断
			params += '&user_id=' + Jxstar.session['user_id'];
			//设置上传附件的参数
			form.fileUpload = true;
			form.method = 'POST';
			//发送后台请求
			form.submit({
				url: Jxstar.path + '/fileAction.do',
				waitTitle: jx.req.wait,	//'请等待'
				waitMsg: jx.req.uping,	//'正在上传附件...'
				success: function(form, action) {
					var result = Ext.decode(action.response.responseText);
					if (result.success) {
						var msg = result.message;
						if (msg.length == 0) {
							msg = jx.req.success;	//'执行成功'
						}
						JxHint.hint(msg);
						//成功执行外部的回调函数
						if (hdCall != null) hdCall(result.data);
					} else {
						JxUtil.errorResponse(action.response);
					}
				},
				failure: function(form, action) {
					JxUtil.errorResponse(action.response);
				},
				params:params
			});
		},
		
		/**
		* 表格中的数据导出到xls文件中
		* grid -- 数据表格
		* fileName -- excel文件名
		**/
		exportCSV: function(grid, fileName) {
			var vExportContent = JxUtil.gridToCSV(grid, true);
			var fd = Ext.get('frmDummy');
			if (!fd) {
				fd = Ext.DomHelper.append(Ext.getBody(), {
						tag:'form', 
						method:'post', 
						id:'frmDummy', 
						name:'frmDummy',
						action:Jxstar.path+'/public/core/exportfile.jsp', 
						target:'_blank',
						cls:'x-hidden',
						cn:[
							{tag:'input',name:'fileName',id:'fileName',type:'hidden'},
							{tag:'input',name:'exportContent',id:'exportContent',type:'hidden'}]
					}, true);
			}
			fd.child('#fileName').set({value:fileName});
			fd.child('#exportContent').set({value:vExportContent});
			fd.dom.submit();
		}

	});
})();