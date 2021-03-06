<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	request.setCharacterEncoding("UTF-8");
	String htmlData = request.getParameter("detail") != null ? request
			.getParameter("detail") : "";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>增加新商品</title>
		<style type="text/css">
#triggers img {
	cursor: pointer;
	margin: 0 5px;
	background-color: #fff;
	border: 1px solid #ccc;
	padding: 2px;
	width: 200px;
	height: 150px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
}
</style>
	</head>

	<body>
		<!-- header 包含头部导航-->
		<%@include file="/jshop/admin/header.jsp"%>
		<!-- end header -->
		<!-- content -->
		<div id="content">
			<!-- end content / left -->
			<%@include file="/jshop/admin/left.jsp"%>
			<!-- end content / left -->
			<!-- content / right -->
			<div id="right">
				<!-- table -->
				<div class="box">
					<!-- box / title -->
					<div class="title">
						<h5>
							增加新商品
						</h5>
					</div>
					<div class="form">
						<div class="fields">
							<div class="field field-first">
								<div class="label">
									<label for="select">
										选择商品所属分类:
									</label>
								</div>
								<div class="select">
									<select id="navid" name="navid">
										<option value="0">
											--请选择--
										</option>
									</select>
									<select id="ltypeid" name="ltypeid">
										<option value="0">
											--请选择--
										</option>
									</select>
									<select id="stypeid" name="stypeid">
										<option value="0">
											--请选择--
										</option>
									</select>
								</div>
							</div>
							<div class="field">
								<div class="label">
									<label for="input-medium">
										商品名称:
									</label>
								</div>
								<div class="input">
									<input id="goodsname" name="goodsname" type="text" class="medium"></input>
								</div>

							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										商品编码:
									</label>
								</div>
								<div class="input">
									<input id="usersetnum" name="usersetnum" type="text" class="small" reg="^\d{4,20}$" tip="编号要是4位-20位数字之间"></input>
								</div>

							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										货号:
									</label>
								</div>
								<div class="input">
									<input id="productSn" name="productSn" type="text" class="small"></input>
								</div>

							</div>
							<div class="field">
								<div class="label">
									<label for="select">
										商品品牌:
									</label>
								</div>
								<div class="select">
									<select id="brandname" name="brandname">
										<option value="0">
											--请选择--
										</option>
									</select>
								</div>
							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										销售价:
									</label>
								</div>
								<div class="input">
									<input id="saleprice" name="saleprice" value="0" type="text" class="small"></input>
								</div>

							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										会员价:
									</label>
								</div>
								<div class="input">
									<input type="text" id="memberprice" name="memberprice" value="0" class="small"></input>
								</div>

							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										市场价:
									</label>
								</div>
								<div class="input">
									<input type="text" id="price" name="price" size="20" value="0" class="small"></input>
								</div>

							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										成本价:
									</label>
								</div>
								<div class="input">
									<input type="text" id="cost" name="cost" value="0" class="small"></input>
								</div>

							</div>
							<div class="field">
								<div class="label">
									<label for="select">
										重量:
									</label>
								</div>
								<div class="select">
									<input type="text" id="weight" name="weight" class="small"></input>
									<select id="weightselect">
										<option value="0">
											--请选择--
										</option>
									</select>
								</div>
							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										初始库存:
									</label>
								</div>
								<div class="input">
									<input type="text" id="quantity" name="quantity" value="0" class="small" />
								</div>
							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										预警库存:
									</label>
								</div>
								<div class="input">
									<input type="text" id="freezeStore" name="freezeStore" value="10" class="small" />

								</div>
							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										货位:
									</label>
								</div>
								<div class="input">
									<input type="text" id="placeStore" name="placeStore" value="" class="small" />
								</div>
							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										商品积分:
									</label>
								</div>
								<div class="input">
									<input type="text" id="points" name="point" value="0" class="small"></input>
								</div>
							</div>
							<div class="field">
								<div class="label label-radio">
									<label>
										是否新品:
									</label>
								</div>
								<div class="radios">
									<div class="radio">
										<input type="radio" id="isNew" name="isNew" value="1" />
										<label for="radio-1">
											新品
										</label>
									</div>
									<div class="radio">
										<input type="radio" id="isNew" name="isNew" value="0" checked />
										<label for="radio-2">
											非新品
										</label>
									</div>
								</div>
							</div>
							<div class="field">
								<div class="label label-radio">
									<label>
										是否推荐:
									</label>
								</div>
								<div class="radios">
									<div class="radio">
										<input type="radio" id="recommended" name="recommended" value="1" />
										<label for="radio-1">
											推荐
										</label>
									</div>
									<div class="radio">
										<input type="radio" id="recommended" name="recommended" value="0" checked />
										<label for="radio-2">
											不推荐
										</label>
									</div>
								</div>
							</div>
							<div class="field">
								<div class="label label-radio">
									<label>
										是否热销:
									</label>
								</div>
								<div class="radios">
									<div class="radio">
										<input type="radio" id="hotsale" name="hotsale" value="1" />
										<label for="radio-1">
											热销
										</label>
									</div>
									<div class="radio">
										<input type="radio" id="hotsale" name="hotsale" value="0" checked />
										<label for="radio-2">
											不热销
										</label>
									</div>
								</div>
							</div>
							<div class="field">
								<div class="label label-radio">
									<label>
										是否特价:
									</label>
								</div>
								<div class="radios">
									<div class="radio">
										<input type="radio" id="bargainprice" name="bargainprice" value="1" />
										<label for="radio-1">
											特价
										</label>
									</div>
									<div class="radio">
										<input type="radio" id="bargainprice" name="bargainprice" value="0" checked />
										<label for="radio-2">
											不特价
										</label>
									</div>
								</div>
							</div>
							<div class="field">
								<div class="label label-radio">
									<label>
										是否上架:
									</label>
								</div>
								<div class="radios">
									<div class="radio">
										<input type="radio" id="salestate" name="salestate" value="1" />
										<label for="radio-1">
											上架
										</label>
									</div>
									<div class="radio">
										<input type="radio" id="salestate" name="salestate" value="0" checked />
										<label for="radio-2">
											下架
										</label>
									</div>
								</div>
							</div>
							<div class="field">
								<div class="label label-radio">
									<label>
										是否同步到移动平台:
									</label>
								</div>
								<div class="radios">
									<div class="radio">
										<input type="radio" id="ismobileplatformgoods" name="ismobileplatformgoods" value="1" />
										<label for="radio-1">
											同步
										</label>
									</div>
									<div class="radio">
										<input type="radio" id="ismobileplatformgoods" name="ismobileplatformgoods" value="0" checked />
										<label for="radio-2">
											不同步
										</label>
									</div>
								</div>
							</div>
							<div class="field">
								<div class="label">
									<label for="select">
										搜索关键字:
									</label>
								</div>
								<div class="select">
									<select id="keywordname" name="keywordname">
										<option value="0">
											--请选择--
										</option>
									</select>
								</div>
							</div>
							<div class="field">
								<div class="label">
									<label for="input-small">
										页面关键字:
									</label>
								</div>
								<div class="input">
									<input type="text" id="metaKeywords" name="metaKeywords" value="" class="small" />

								</div>
							</div>
							<div class="field">
								<div class="label label-textarea">
									<label for="textarea">
										页面描述:
									</label>
								</div>
								<div class="textarea textarea-editor">
									<textarea id="metaDescription" name="metaDescription" cols="50" rows="12"></textarea>
								</div>
							</div>
							<div class="title">
								<h5>
									商品描述
								</h5>
							</div>
							<div class="field">
								<div class="label label-textarea">
									<label for="textarea">
										商品描述:
									</label>
								</div>
								<div style="margin: 0 0 0 200px;">
									<textarea id="detail" name="detail" cols="50" rows="12" style="width: 100%; height: 600px; visibility: hidden;"><%=htmlspecialchars(htmlData)%></textarea>
								</div>
							</div>
							<div class="title">
								<h5>
									商品主图片
								</h5>
							</div>


							<div class="field">
								<div class="label">
									<label for="file">
										主图片:
									</label>
								</div>
								<div class="input input-file">
									<button id="delpc" name="delpc">
										删除图片
									</button>
									<div id="file-uploader-demo1">
										<noscript>
											<p>
												Please enable JavaScript to use file uploader.
											</p>
											<!-- or put a simple form for upload here -->
										</noscript>
									</div>
									<!-- trigger elements -->
									<div id="triggers">
									</div>
								</div>
							</div>
							<div class="title">
								<h5>
									商品规格
								</h5>
							</div>
							<div class="field">
								<div class="label">
									<label for="select">
										是否开启商品规格功能:
									</label>
								</div>
								<div class="select">
									<select id="isSpecificationsOpen" name="isSpecificationsOpen">
										<option value="0">
											--请选择--
										</option>
										<option value="1">
											开启
										</option>
										<option value="2">
											关闭
										</option>
									</select>
								</div>
							</div>
							<div class="field">
								<div class="label label-checkbox">
									<label>

									</label>
								</div>
								<div class="checkboxes">
									<div id="Specificationslistarea">

									</div>
									<div id="addproductarea">
										<input type="button" id="addproduct" name="addproduct" value="增加货品" />
									</div>
								</div>
							</div>

							<div class="title">
								<h5>
									货品区域
								</h5>
							</div>
							<div class="table">
								<div style="overflow-y: scroll; width: 100%; height: 100%">
									<table id="addproductlistarea">
										<thead>
											<tr>
												<th class="center">
													货号
												</th>
												<th id="tempkey"></th>
												<th>
													销售价
												</th>
												<th>
													会员价
												</th>
												<th>
													市场价
												</th>
												<th>
													成本价
												</th>
												<th>
													重量
												</th>
												<th>
													库存
												</th>
												<th>
													预警库存
												</th>
												<th>
													货位
												</th>
												<th>
													仓库位置
												</th>
												<th>
													默认
												</th>
												<th>
													上架
												</th>
												<th>
													删除
												</th>
											</tr>
										</thead>
										<tbody>

										</tbody>
									</table>
								</div>
							</div>
							<div class="title">
								<h5>
									商品属性和参数
								</h5>
							</div>
							<div class="field">
								<div class="label">
									<label for="select">
										请选择商品类型:
									</label>
								</div>
								<div class="select">
									<select id="goodsTypeId" name="goodsTypeId">

									</select>
								</div>

							</div>
							<div class="field">
								<div class="label">
									<label for="select">
									</label>
								</div>
								<div id="gat" class="select">

								</div>
							</div>



							<div id="gatattr">

							</div>

						</div>


					</div>

					<div class="form">
						<div class="fields">
							<div class="field field-first">
								<div class="input">
									<div class="button highlight">
										<input type="button" id="submit" name="submit" value="完成并增加商品"></input>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>



			</div>
			<!-- end table -->



		</div>
		<!-- end content / right -->
		</div>
		<!-- end content -->
		<!-- footer -->
		<%@include file="/jshop/admin/footer.jsp"%>
		<!-- end footert -->
		<script type="text/javascript" src="<%=basePath%>jshop/admin/js/addgoodsjs.js"></script>
		
		<script type="text/javascript">
 		 function createUploader(){ 
             var uploader = new qq.FileUploader({
                 element: document.getElementById('file-uploader-demo1'),
                 action: '<%=basePath%>ajaxFileUploads.action;jsessionid=<%=session.getId()%>',
                 debug: true,
                 minSizeLimit:1024,
                 sizeLimit: 1073741824,
                 allowedExtensions: ['jpeg','jpg','gif','png'],
                 onComplete: function(id, fileName, responseJSON){
                	var pcpath="<%=basePath%>"+responseJSON.success;
  					var htm="<img id='"+id+"' src='"+pcpath+"' rel='#"+fileName+"'/>";
  					var checkpc="<input id='"+id+"' name='pcpath' type='checkbox' value='"+pcpath+"' checked='true'/> "
  					$("#triggers").append(htm).append(checkpc);
                 },
               
             });           
         }
 		window.onload = createUploader; 
 	</script>

		<script type="text/javascript">
 		KE.show({
			id : 'detail',
		    imageUploadJson : '<%=basePath%>jshop/admin/js/jsp/upload_json.jsp',
			fileManagerJson : '<%=basePath%>jshop/admin/js/jsp/file_manager_json.jsp',
			allowFileManager : true,
			afterCreate : function(id) {
				KE.event.ctrl(document, 13, function() {
					KE.util.setData(id);
				});
			}
		});
 		
 	</script>
	</body>

</html>
<%!private String htmlspecialchars(String str) {
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}%>