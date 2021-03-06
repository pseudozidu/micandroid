<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>Manage user</title>
<link href="/css/GDP-common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/js/placeholder.js"></script>
<script type="text/javascript" src="/js/verification.js"></script>
</head>
<!--[if lte IE 8]>

<style>
.mask_area {
filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=0,StartColorStr='#bf000000',EndColorStr='#bf000000');
}
.login_header ul {
	margin-top:-56px;
}
.login_header ul li a {
	width:auto;
	height:auto;
}
.login_container .contact_content .left_section ul .ie_f {
	background:url(../images/contacts_icon.jpg) no-repeat 0 7px;
}
.login_container .contact_content .left_section ul .ie_s {
    background:url(../images/projects_icon.jpg) no-repeat 0 7px;
}
.login_container .contact_content .left_section ul .ie_t {
    background:url(../images/reports_icon.jpg) no-repeat 0 5px;
}
.login_container .manage_content .right_section .manage_title .search_bg input {
	line-height:25px;
}
.login_container .contact_content .right_section .c_details .c_details_content li {
	margin-top:-4px;
}
</style>

<![endif]-->
<body>
<div class="mask_area"></div>
<div class="add_cus_bg">
  <div class="add_cus_content" style="height:350px;">
    <div class="add_manage_title dividing_line"><span>Add a new user</span></div>
    <div class="border_row dividing_line"><input type="text" placeholder="Name" name="name"></div>
    <div class="border_row dividing_line"><input type="email" placeholder="Email Address" name="uemail"></div>
    <div class="border_row dividing_line"><input type="password" placeholder="Password" name="password"></div>
    <input type="hidden" name="companyId" value="${currentCompany}"/>
    <input type="hidden" name="planId" value="${planId}"/>
    <input type="hidden" name="expiredDate" value="${xeroUser.expiredDateTime}"/>
    <input type="hidden" name="_current_userId"/>
    <input type="hidden" name="_current_login_userId" value="${xeroUser.id}"/>
    <div class="cancel_button"><span>CANCEL</span></div>
    <div class="del_button"><span>DELETE</span></div>
    <div class="add_button" style="right:329px;"><span>ADD USER</span></div>
    <span class="check_name">Name should not be null</span>
    <span class="check_email">Email is not correct</span>
    <span class="check_pawo">Password should be 6 ～ 12</span>
  </div>
</div>
  <div class="login_container">
    <c:import url="/header-common" />
    <div class="contact_content manage_content">
      <%@ include file="/common/leftMenu.jsp"%>
      
      <div class="right_section">
        <div class="manage_title">
          <span>Manage Users</span>
          <div class="search_bg">
            <input type="text" placeholder="Search" id="search">
          </div>
          
         <c:if test="${planId == 3 || (planId == 2 && allowsRegisteredUsers < 5)}">
          	 <a href="javascript:void(0)" class="add_manage_btn" id="add_manage_btn"></a>
          </c:if>
          
          <c:if test="${planId == 1 || (planId == 2 && allowsRegisteredUsers == 5)}">
          	 <a href="/payment" class="add_manage_btn"></a>
          </c:if>
          
        </div>
        <div class="c_details">
          <div class="c_details_head">
            <span class="dad_area">Date Added<strong id="dad_area"></strong></span>
            <span class="lse_area">Last Seen<strong id="lse_area"></strong></span>
            <span class="ema_area">Email<strong id="ema_area"></strong></span>
            <span class="nam_area">Name<strong id="nam_area"></strong></span>
          </div>
          <ul class="c_details_content">
             
          </ul>
        </div>
      </div>
    </div>
  </div>
  <input type="hidden" value="user" id="type_page"/>
</body>
<script type="text/javascript" src="/js/orderBy-com.js"></script>
<script type="text/javascript" src="/js/manager-user.js"></script>
</html>