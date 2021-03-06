<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>爱学乐园</title>
<link rel="stylesheet" href="css/style.css" type="text/css" media="all" />
<link rel="stylesheet" href="css/impromptu.css" type="text/css" media="all" />
<!--[if IE 6]>
			<link rel="stylesheet" href="css/ie6-style.css" type="text/css" media="all" />
		<![endif]-->
<script src="js/jquery-1.4.2.js" type="text/javascript"></script>
<script src="js/login_regist.js" type="text/javascript"></script>
<script src="js/jquery-impromptu.3.1.js" type="text/javascript"></script>
</head>
<body>
m<!-- Page -->
<div id="page" class="shell">
  <!-- Header -->
  <div id="header">
    <!-- Top Navigation -->
    <div id="top-nav">
      <ul>
        <li class="home"><a href="index.jsp">home</a></li>
        <c:if test="${empty sessionScope.userInfo}"><li><a><font color="gray">游客你好，欢迎您注册为本网站的会员</font></a></li></c:if>
        <c:if test="${!empty sessionScope.userInfo}"><li><a><font color="gray">${sessionScope.userInfo.user_name},欢迎您的到来</font></a></li></c:if>
 
      </ul>
    </div>
    <!-- / Top Navigation -->
    <div class="cl">&nbsp;</div>
    <!-- Logo -->
    <div id="logo">
      <h1><a href="#">爱学<span>乐园</span></a></h1>
      <p class="description">your study zone</p>
    </div>
    <!-- / Logo -->
    <!-- Main Navigation -->
    <div id="main-nav">
      <div class="bg-right">
        <div class="bg-left">
             <c:if test="${empty sessionScope.userInfo}">
         <ul>
            <li><a href="index.jsp">主页</a></li>
            <!-- ，视频信息修改，上传视频，申请付款，充值 -->
            <li><a class="button button-left login">登陆</a></li>
            <li><a class="button button-right regist">注册</a></li>
            <li><a href="myteam.jsp">关于我们</a></li>
          </ul>
        </c:if>
        <c:if test="${!empty sessionScope.userInfo}">
          <ul>
            <li><a href="index.jsp">主页</a></li>
            <!-- ，视频信息修改，上传视频，申请付款，充值 -->
            <li><a href="userPay.jsp">充值</a></li>
            <li><a href="payUser.jsp">申请提现</a></li>
            <li><a href="videoUpload.jsp">上传视频</a></li>
            <li><a href="showUserDetailServlet.do">个人资料修改</a></li>
            <li><a class="button button-left login" href="exitSystem.do">退出</a></li>
            <li><a href="myteam.jsp">关于我们</a></li>
          </ul>
          </c:if>
        </div>
      </div>
    </div>
    <!-- / Main Navigation -->
    <div class="cl">&nbsp;</div>
    <!-- Sort Navigation -->
    <div id="sort-nav">
      <div class="bg-right">
        <div class="bg-left">
          <div class="cl">&nbsp;</div>
      <ul>
            <li class="first active first-active"><a href="#">热门视频分类</a><span class="sep">&nbsp;</span></li>
            <c:forEach items="${videoType.list}" var="videoType" begin="0" end="5">
             <li><a href="/team1_videoplay_v2.0/videoListServlet.do?videoTypeID=${videoType.type_id}&page=1">${videoType.type_name}</a><span class="sep">&nbsp;</span></li>
            </c:forEach>
           
          </ul>
          <div class="cl">&nbsp;</div>
        </div>
      </div>
    </div>
    <!-- / Sort Navigation -->
  </div>
  <!-- / Header -->
  <!-- Main -->
  <div id="main">
    <div id="main-bot">
      <div class="cl">&nbsp;</div>
      <!-- Content -->
      <div id="content">
        <!-- 第三个开始 -->
        <div class="block">
          <div class="block-bot">
            <div class="head">
              <div class="head-cnt">
                <h3>密码找回<br></h3>
                <div class="cl">&nbsp;</div>
              </div>
            </div>
            <div class="row-articles articles">
              <div class="cl">&nbsp;</div>
             
              <div class="article">
              <form action="getPasswordServlet.do" method="post">  
              		<br/> 
              		 您的邮箱地址： <input type='text' name='email'/>
                	<br/> 
                	<br/> 
                	选&nbsp;&nbsp;择&nbsp;&nbsp;问&nbsp;&nbsp;题 :&nbsp;&nbsp; 
                	<select name='question'>
					<option value='您的母亲名字'>您的母亲名字</option>
					<option value='您的父亲名字'>您的父亲名字</option>
					<option value='您的中学名字'>您的中学名字</option>
					</select>
                	<br/>
                	<br/>
                		您&nbsp;&nbsp;的&nbsp;&nbsp;答&nbsp;&nbsp;案：<input type='text' name='answer'/>
                	<br/><br/>
                	<input type="submit" value="&nbsp;找回&nbsp;"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                	<input type="button" value="&nbsp;返回到上一页&nbsp;" onclick="javascript:history.go(-1)"/>
                </form>
              </div>
              <div class="cl">&nbsp;</div>
            </div>
          </div>
        </div>
        <!-- 第三个结束-->
      </div>
      <!-- / Content -->
      <!--  -->
       <div id="sidebar">
        <!-- Search -->
        <div id="search" class="block">
          <div class="block-bot">
            <div class="block-cnt">
              <form action="searchServlet.do?page=1" method="post">
                <div class="cl">&nbsp;</div>
                <div class="fieldplace">
                  <input type="text" class="field" name="search" value="Search" title="Search" />
                </div>
                <input type="submit" class="button" value="GO" />
                <div class="cl">&nbsp;</div>
              </form>
            </div>
          </div>
        </div>
        <!-- / Search -->
        <!-- 登陆前显示-->
        <c:if test="${empty sessionScope.userInfo}">
         <div id="sign" class="block">
          <div class="block-bot">
            <div class="block-cnt">
              <div class="cl">&nbsp;</div>
              <a class="button button-left login">登&nbsp;&nbsp;陆</a> <a class="button button-right regist">注&nbsp;&nbsp;册</a>
              <div class="cl">&nbsp;</div>
              <p class="center"><a href="getPassword.jsp">忘记密码?</a>&nbsp;&nbsp;<a href="#">联系我们?</a></p>
            </div>
          </div>
        </div>
        <!-- / 登陆前显示 -->
        </c:if>
       <c:if test="${!empty sessionScope.userInfo}">
       <!-- 登陆后显示-->
        <div id="sign" class="block">
          <div class="block-bot">
            <div class="block-cnt">
              <div class="cl">&nbsp;</div>
              <a href="videoUpload.jsp" class="button button-left">上 &nbsp;传</a> <a href="myWalletServlet.do" class="button button-right">我的钱包</a>
              <div class="cl">&nbsp;</div>
              <p class="center"><a href="#">帮助?</a>&nbsp;&nbsp;<a href="#">联系我们?</a></p>
            </div>
          </div>
        </div>
        <!-- / 登陆后显示 -->
       </c:if>
        
        <div class="block">
          <div class="block-bot">
            <div class="head">
              <div class="head-cnt">
                <h3>热播视频Top 6</h3>
              </div>
            </div>
            <div class="image-articles articles">
              <div class="cl">&nbsp;</div>
              <!-- 循环开始 -->
              <c:forEach items="${hotVideo}" var="video" begin="0" end="5">
              <div class="article">
                <div class="cl">&nbsp;</div>
                <div class="image"> <a href="videoDetailServlet.do?videoID=${video.video_id}"><img src="${video.video_pictureUrl}" alt="点击播放" /></a> </div>
                <div class="cnt">
                  <h4><a href="videoDetailServlet.do?videoID=${video.video_id}">${video.video_title}</a></h4>
                  <p>${video.video_desc}</p>
                </div>
                <div class="cl">&nbsp;</div>
              </div>
              </c:forEach> 
              <!-- 借宿 -->
              
              <div class="cl">&nbsp;</div>
              <a href="#" class="view-all">view all</a>
              <div class="cl">&nbsp;</div>
            </div>
          </div>
        </div>
        <div class="block">
          <div class="block-bot">
            <div class="head">
              <div class="head-cnt">
                <h3>最新上传Top 6</h3>
              </div>
            </div>
            <div class="image-articles articles">
              <div class="cl">&nbsp;</div>
              
           <c:forEach items="${newVideo}" var="video" begin="0" end="5"> 
              <div class="article">
                <div class="cl">&nbsp;</div>
                <div class="image"> <a href="videoDetailServlet.do?videoID=${video.video_id}"><img src="${video.video_pictureUrl}" alt="点击播放" /></a> </div>
                <div class="cnt">
                  <h4><a href="videoDetailServlet.do?videoID=${video.video_id}">${video.video_title}</a></h4>
                  <p>${video.video_desc}</p>
                </div>
                <div class="cl">&nbsp;</div>
              </div>
            </c:forEach>    
              
              <div class="cl">&nbsp;</div>
              <a href="#" class="view-all">view all</a>
              <div class="cl">&nbsp;</div>
            </div>
          </div>
        </div>
        <div class="block">
          <div class="block-bot">
            <div class="head">
              <div class="head-cnt">
                <h3>广告招商<br></h3>
              </div>
            </div>
            <div class="text-articles articles">
              
              <div class="article">
                <p>诚招各路英杰加入本网站</p>
              </div>
              <div class="cl">&nbsp;</div>
              <a href="#" class="view-all">view all</a>
              <div class="cl">&nbsp;</div>
            </div>
          </div>
        </div>
      </div>
      <!-- / Sidebar -->
      <div class="cl">&nbsp;</div>
      <!-- Footer -->
      <div id="footer">
        <div class="navs">
          <div class="navs-bot">
            <div class="cl">&nbsp;</div>
            <ul>
              <li>@copy right 本网站权利私有</li>
            </ul>
            <ul>
            	<li>东华理工大学授权，team one项目组完成，感谢东华理工大学，感谢厦门万策的吴老师的指导</li>
            </ul>
            <div class="cl">&nbsp;</div>
          </div>
        </div>
        <p class="copy">&copy;Design by <a href="myteam.jsp">Ecit team one</a></p>
      </div>
      <!-- / Footer -->
    </div>
  </div>
  <!-- / Main -->
</div>
<!-- / Page -->
</body>
</html>
