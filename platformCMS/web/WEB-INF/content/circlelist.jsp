<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
		<%@ include file="/common/meta.jsp"%>
		<title>圈网_${showCategory.name}</title>
		<link type="text/css" rel="stylesheet" href="css/newslist.css" />
		<script type="text/javascript" src="js/jquery.js"></script>
		<script type="text/javascript" src="js/util.js"></script>
		<script type="text/javascript" src="js/newslist.js"></script>
		<!--[if lte IE 6]>
		<link rel="stylesheet" type="text/css" href="css/navHankIE6.css" />
		<![endif]-->
		<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/navHankIE7.css" />
		<![endif]-->
	</head>
	<body>
	<div class="container">
		<%@ include file="/common/top.jsp"%>

		<div id="content">
			<div id="topBanner">
				<s:if test="adsArray.length>0">
					<s:if test="adsArray[0].type.name==1">
						<s:property value="adsArray[0].script" escapeJavaScript="false"/>
					</s:if><s:else>
						<a href="<s:property value='adsArray[0].url'/>" target="_blank">
						<img src="${ctx }<s:property value='adsArray[0].image'/>" height="100px" width="960px" alt="<s:property value='adsArray[0].description'/>" />
						</a>
					</s:else>
				</s:if>
			</div>
			
			<div id="postion">
				<p>
					<span>您的位置:首页&gt;<a href="${ctx }/circle">圈网</a>&gt;${showCategory.name}</span>
				</p>
			</div>
			<div id="contentLeft">
				<div id="totop">
					<a href="#top" rel="">
						<img src="images/toTop.gif" alt="返回顶部" />
					</a>
				</div>
				
				<div id="bannertop">
				   <s:if test="adsArray.length>0">
					<s:if test="adsArray[3].type.name==1">
						<s:property value="adsArray[3].script" escapeJavaScript="false"/>
					</s:if><s:else>
						<a href="<s:property value='adsArray[3].url'/>" target="_blank">
						<img src="${ctx }<s:property value='adsArray[3].image'/>" height="150px" width="600px" alt="<s:property value='adsArray[3].description'/>" />
						</a>
					</s:else>
				</s:if>
				</div>
				<div id="tabshow">
					<h3>${showCategory.nameEn}_${showCategory.name}</h3>
					<ul>
						 <li><span><a href="${ctx}/circlelist.action?showCategory.id=${showCategory.id}" class="tablink" >列表显示</a></span></li>
                         <li><span><a href="${ctx}/circlepiclist.action?showCategory.id=${showCategory.id}" class="tablink" >图标显示</a></span></li>
					</ul>
				</div>
				
				
				
					<div id="tablist">
					<s:iterator value="page.result">
					<div class="news">
						<div class="newsleft">
							<a href="${ctx}/circlecontent.action?article.id=${id}">
							<s:if test="imageUri!=null && imageUri != ''"><img src="${ctx}${imageUri}" alt="" />
							</s:if><s:else>
								<img src="images/newsimages.gif" alt="" />	
							</s:else>
							</a>					
						</div>
						
						<div class="newsright">
							<h3><a href="${ctx}/circlecontent.action?article.id=${id}">${title }</a></h3>
							<h4><s:date name="createDate" format="yyyy-MM-dd"/>│  阅读_${readCount}  │  评论_<s:property value="comments.size"/>  │  投票_${voteCount }</h4>
							<p>
								<ouun:substring length="50" value="${content}"/>
							</p>
						</div>
					</div>
					</s:iterator>
					
					</div>
					<div style="padding-left:96px">
						第${page.pageNo}页, 共${page.totalPages}页 
						<s:if test="page.hasPre">
							<a href="circlelist.action?page.pageNo=${page.prePage}&page.orderBy=${page.orderBy}&page.order=${page.order}&showCategory.id=${showCategory.id}">上一页</a>
						</s:if>
						<ouun:pageNum totalPages="${page.totalPages}" pageNo="${page.pageNo}" url="circlelist.action?&page.orderBy=${page.orderBy}&page.order=${page.order}&showCategory.id=${showCategory.id}&page.pageNo=" />
						<s:if test="page.hasNext">
							<a href="circlelist.action?page.pageNo=${page.nextPage}&page.orderBy=${page.orderBy}&page.order=${page.order}&showCategory.id=${showCategory.id}">下一页</a>
						</s:if>
					</div>
								
				<%@ include file="/common/searcharticle.jsp"%>
				
				<div id="bannerThree">
				   <s:if test="adsArray.length>0">
					<s:if test="adsArray[4].type.name==1">
						<s:property value="adsArray[4].script" escapeJavaScript="false"/>
					</s:if><s:else>
						<a href="<s:property value='adsArray[4].url'/>" target="_blank">
						<img src="${ctx }<s:property value='adsArray[4].image'/>" height="100px" width="600px" alt="<s:property value='adsArray[4].description'/>" />
						</a>
					</s:else>
				</s:if>
				</div>
				<div id="longdetails">
                	<div class="longdetails_left">
						<img src="images/DetailsImage.gif" alt=""/>
					</div>
					<div id="longdetails_right">
                    	<div>
                        	<ul>
                                <li class="titleLi">频道编辑:Snow<a href="#"><span>我要当编辑</span></a></li>
                                <li>责任内容:全站更新</li>
                                <li>个性签名:春暖花开</li>
                            </ul>
                        </div>
                        <div>
                        	 <p>
                                声明网站所转载的文章，书籍等内容纯属作者个人观点，并不代表本站观点。
                                欢迎转载，敬请书名转载设计圈网 ooowo.coe
                            </p>
                        </div>
					</div>	
                </div>
				
			</div>
			<div id="rightContent">
				<div class="rightContent_top_ad_345-240">
					<s:if test="adsArray.length>0">
						<s:if test="adsArray[1].type.name==1">
							<s:property value="adsArray[1].script" escapeJavaScript="false"/>
						</s:if><s:else>
							<a href="<s:property value='adsArray[1].url'/>" target="_blank">
							<img src="${ctx }<s:property value='adsArray[1].image'/>" height="240px" width="345px" alt="<s:property value='adsArray[1].description'/>" />
							</a>
						</s:else>
					</s:if>
                </div>
                
				<div class="webspecialimgs">
					<h3><span>WEB Special_圈网精选</span></h3>
					<s:iterator value="circleRecommend">
                                    <a href="${ctx}/circlecontent.action?article.id=${id}">
										<s:if test="imageUri!=null">
										<img height="46px" width="46px" src="${ctx }${imageUri}" alt="${title }" />
										</s:if><s:else>
										<img src="images/renwu.gif" height="46px" width="46px"  alt="${title }" />
										</s:else>
									</a>
                    </s:iterator>
				</div>
                
                <div class="hotCommodity">
					<h3>FOCUS_焦点人物</h3>
					<p>
						<s:if test="adsArray.length>7">
										<s:if test="adsArray[7].type.name==1">
											<s:property value="adsArray[7].script" escapeJavaScript="false"/>
										</s:if><s:else>
										<a href="<s:property value='adsArray[7].url'/>" target="_blank">
											<img src="${ctx }<s:property value='adsArray[7].image'/>" height="204px" width="204px" alt="<s:property value='adsArray[7].description'/>" />
											</a>
										</s:else>
					   </s:if>
					</p>
				</div>
				
                <div class="hotCommodity">
					<h3>NEW Topic_最新专题</h3>
					<p>
						<s:if test="adsArray.length>8">
										<s:if test="adsArray[8].type.name==1">
											<s:property value="adsArray[8].script" escapeJavaScript="false"/>
										</s:if><s:else>
										<a href="<s:property value='adsArray[8].url'/>" target="_blank">
											<img src="${ctx }<s:property value='adsArray[8].image'/>" height="204px" width="204px" alt="<s:property value='adsArray[8].description'/>" />
											</a>
										</s:else>
					   </s:if>
					</p>
				</div>
				
				
				<div class="hotCommodity">
					<h3>NEW Topic_最新下载</h3>
					<p>
						<s:iterator value="downloadResourceTop">
						<a href="${ctx}/downloadcontent.action?resource.id=${id}&&type.id=${type.id}">
						<s:if test="uri!=null && uri!=''">
                             <img src="${ctx}${uri}"  height="204px" width="204px" alt="${name }" title="${name }"/>
                                 </s:if><s:else>
                             <img src="images/hotcommodity1.gif"  width="204" height="204" alt="${name }"/></s:else>
		                </a>
                       </s:iterator>
					</p>
				</div>
                
				<div class="hotCommodity">
					<h3>Recommend book 本周推荐书品</h3>
					<p>
						<s:if test="adsArray.length>9">
										<s:if test="adsArray[9].type.name==1">
											<s:property value="adsArray[9].script" escapeJavaScript="false"/>
										</s:if><s:else>
										<a href="<s:property value='adsArray[9].url'/>" target="_blank">
											<img src="${ctx }<s:property value='adsArray[9].image'/>" height="204px" width="204px" alt="<s:property value='adsArray[9].description'/>" />
											</a>
										</s:else>
					   </s:if>
					</p>
				</div>
				
				
				<div class="hotCommodity">
					<h3>Recommend book 热销商品</h3>
					<p>
						<s:if test="adsArray.length>10">
										<s:if test="adsArray[10].type.name==1">
											<s:property value="adsArray[10].script" escapeJavaScript="false"/>
										</s:if><s:else>
										<a href="<s:property value='adsArray[10].url'/>" target="_blank">
											<img src="${ctx }<s:property value='adsArray[10.image'/>" height="204px" width="204px" alt="<s:property value='adsArray[10].description'/>" />
											</a>
										</s:else>
					   </s:if>
					</p>
				</div>
				
				<div class="topTags">
					<a href="#"><span>创意</span></a>
					<a href="#"><span>coool</span></a>
					<a href="#"><span>家居</span></a>
					<a href="#"><span>有趣</span></a>
					<a href="#"><span>风格</span></a>
					<a href="#"><span>酷玩</span></a>
					<a href="#"><span>科技</span></a>
					<a href="#"><span>文化</span></a>
					<a href="#"><span>手机</span></a>
					<a href="#"><span>头枕</span></a>
					<a href="#"><span>性感</span></a>
					<a href="#"><span>视频</span></a>
					<a href="#"><span>BEDU</span></a>
					<a href="#"><span>遥控狙击枪</span></a>
					<a href="#"><span>科技数码</span></a>
					<a href="#"><span>电击枪</span></a>
					<a href="#"><span>有趣的家具</span></a>
					<a href="#"><span>鼻钉</span></a>
					<a href="#"><span>鼻环</span></a>
					<a href="#"><span>超酷饰品</span></a>
					<a href="#"><span>神奇茶几</span></a>
					<a href="#"><span>机器猫的竹蜻蜓</span></a>
					<a href="#"><span>数字抽屉</span></a>
					<a href="#"><span>温暖</span></a>
					<a href="#"><span>有趣橱柜</span></a>
				</div>
                
                <div class="advtype3">
					<s:if test="adsArray.length>0">
					<s:if test="adsArray[2].type.name==1">
						<s:property value="adsArray[2].script" escapeJavaScript="false"/>
					</s:if><s:else>
						<a href="<s:property value='adsArray[2].url'/>" target="_blank">
						<img src="${ctx }<s:property value='adsArray[2].image'/>" height="600px" width="160px" alt="<s:property value='adsArray[2].description'/>" />
						</a>
					</s:else>
				</s:if>
				</div>
				<div class="clean"></div>
			</div>
        <div class="clean"></div>
        <div id="bannerFour">
           <s:if test="adsArray.length>0">
					<s:if test="adsArray[5].type.name==1">
						<s:property value="adsArray[5].script" escapeJavaScript="false"/>
					</s:if><s:else>
						<a href="<s:property value='adsArray[5].url'/>" target="_blank">
						<img src="${ctx }<s:property value='adsArray[5].image'/>" height="100px" width="960px" alt="<s:property value='adsArray[5].description'/>" />
						</a>
					</s:else>
			</s:if>
        </div>
			<%@ include file="/common/foot.jsp"%>
			</div>
		</div>
	</body>
</html>