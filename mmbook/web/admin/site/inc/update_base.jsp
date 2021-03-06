<%@ page contentType="text/html; charset=UTF-8"%>
<%
System.out.println(request.getParameter("pageTitle"));
 %>
<script type="text/javascript"> 
var comboBoxCheckTree_keyWords = new Ext.ux.ComboBoxCheckTree({
		fieldLabel: '内容关键字',
		width : 300,
		height : 150,
		hiddenName: 'siteContent.keywordsValue',
		tree : {
			xtype:'treepanel',
			height:100,
			checkModel: 'cascade',    //多选: 'multiple'(默认)单选: 'single'级联多选: 'cascade'(同时选父和子);'parentCascade'(选父);'childCascade'(选子)
			onlyLeafCheckable: false, 
			animate: true,             //设置为true以启用展开 收缩时的动画效果
			rootVisible: true,
			autoScroll:true,  
			loader: new Ext.tree.TreeLoader({dataUrl:'base/BaseKeywords/getTreeCombox.do?contentid='+id  
	          ,baseAttrs: { uiProvider: Ext.ux.TreeCheckNodeUI } }),
			root : new Ext.tree.AsyncTreeNode({id:'1',text:'关键字列表'})
			},
		selectValueModel:'all' //all 多选, folder选父节点  leaf选子节点
});

var field_base_sitecontent = new Ext.form.FieldSet({
    id : 'field_update_sitecontent',
	title : '内容基础信息',
	autoHeight : true,
	layout: 'fit',
    layout:'column',
    items:[{
                     columnWidth:.5,
                     layout: 'form',
                     defaults : {
		   			 	anchor : '88%'
			         },
			         items: [
			                  
		                       {
		                    	xtype:'textfield',
		                        fieldLabel: '标题',
		                        name:'siteContent.title'
		                        }
	                       
	                        ,{
		                    	xtype:'textfield',
		                        fieldLabel: '作者',
		                        name:'siteContent.author'
		                        }
	                         
	                        ,{
		                    	xtype:'numberfield',
		                        fieldLabel: '排序值',
		                        name:'siteContent.sortValue'
		                        }
	                           ,
	                            {
								xtype : 'combo',
								fieldLabel : '显示时间段',
								emptyText : '请选择',
								triggerAction : 'all',
								hiddenName :'siteContent.viewClass',
                                store : new Ext.data.SimpleStore({ 
									fields : ['val','tex'],
									data : [['1','任何人'],['2','注册用户'],['3','管理员'],['4','收费方式1'],['4','收费方式2']]
                                 }),
								displayField : 'tex',
								valueField : 'val',
								editable : false,
								mode : 'local'
	                        }
	                      ,{
					        xtype: 'treeField',
					        hiddenName: 'treeField',
					        fieldLabel: '网站内容分类',
					        emptyText:'选择内容分类',
					        listWidth:200,
					        listHeight:200,
					        readOnly:false,
					        displayField : 'text',
							valueField : 'id',
					        dataUrl:'manage/SiteContentSort/getTreeCombox.do?contentid='+id
			               }
	                        , 
		                        {
								xtype : 'combo',
								fieldLabel : '显示时间段',
								emptyText : '请选择',
								triggerAction : 'all',
								hiddenName :'siteContent.showTime',
                                store : new Ext.data.SimpleStore({ 
									fields : ['val','tex'],
									data : [['1','任何时间'],['2','0-8点'],['3','8-15点'],['4','15-24点']]
                                 }),
								displayField : 'tex',
								valueField : 'val',
								editable : false,
								mode : 'local'
	                        },{
								xtype : 'combo',
								fieldLabel : '评论状态',
								emptyText : '请选择',
								triggerAction : 'all',
								hiddenName :'siteContent.commentStat',
                                store : new Ext.data.SimpleStore({ 
									fields : ['val','tex'],
									data : [['1','开启 '],['2','关闭']]
                                 }),
								displayField : 'tex',
								valueField : 'val',
								editable : false,
								mode : 'local'
	                        }
	                    ]
            	},{
	                columnWidth:.5,
	                layout: 'form',
	                defaults : {
						anchor : '88%'
					},
                	items: [
                	      {
		                    	xtype:'textfield',
		                        fieldLabel: '内容URL',
		                        name:'siteContent.conentUrl'
		                        } 
	                        ,{
	                    	xtype:'textfield',
	                        fieldLabel: '全标题',
	                        name:'siteContent.titleFull'
	                        }
	                        ,{
	                    	xtype:'textfield',
	                        fieldLabel: '来源',
	                        name:'siteContent.sources'
	                        } 
	                        ,{
	                    	xtype:'textfield',
	                        fieldLabel: '缩略图URL',
	                        name:'siteContent.previewsImgUrl'
	                        } 
	                        ,{
		                    	xtype:'textarea',
		                        fieldLabel: '内容简介',
		                        name:'siteContent.synopsis'
		                   },{
				        	xtype:'hidden',
					        fieldLabel:'网站内容分类ID',
					        name:'siteContent.sortId'
			             },{
				        	xtype:'hidden',
					        fieldLabel:'上传缩略图标记',
					        name:'previewsUpload'
			             }
		                 
						,{
				        	xtype:'hidden',
					        fieldLabel:'内容ID',
					        name:'siteContent.id'
			             },{
				        	xtype:'hidden',
					        fieldLabel:'内容ID',
					        name:'contentId'
			             }



                    ]
	 }]
});


</script>