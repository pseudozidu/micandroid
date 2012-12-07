<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>Project</title>
<link href="/css/GDP-common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/js/placeholder.js"></script>
<script type="text/javascript" src="/js/project.js"></script>

</head>
<!--[if lte IE 8]>

<style>
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
.login_container .project_content .right_section .project_title .search_bg input {
	line-height:25px;
}
.login_container .contact_content .right_section .c_details .c_details_content li {
	margin-top:-4px;
}
</style>

<![endif]-->
<body>
  <div class="login_container">
    <div class="header login_header">
      <a href="/"><img src="/images/gdp_logo.png" alt="GDP_logo"></a>
      <span>Global Design &amp; Production</span>
      <ul>
        <li><a href="/logout">Sign Out</a></li>
        <li><a href="#">Manage Users</a></li>
        <li><a href="#">27 Days Remaining</a></li>
      </ul>
    </div>
    <div class="contact_content project_content">
      <div class="left_section">
        <ul>
          <li class="ie_f"><a href="/contact">Contacts</a></li>
          <li class="ie_s selected"><a href="/project">Projects</a></li>
          <li class="ie_t"><a href="/report">Reports</a></li>
        </ul>
      </div>
      <div class="right_section">
        <div class="project_title">
          <span>Projects</span>
          <div class="search_bg">
            <input type="text" placeholder="Search">
          </div>
          <div class="c_type_bg">
            <div class="c_type">
              <div class="cus_field">Active</div>
              <div class="plus_field"></div>
              <div class="sup_field selected_field">Closed</div>
              <div class="selected_bg"></div>
            </div>
          </div>
        </div>
        <div class="c_details">
          <div class="c_details_head">
            <span class="ed_area">End Date<strong></strong></span>
            <span class="sd_area">Start Date<strong></strong></span>
            <span class="cn_area">Customer Name</span>
            <span class="pn_area">PO Number<strong></strong></span>
            <span class="na_area">Name<strong></strong></span>
          </div>
          <ul class="c_details_content">
            <li class="li_defalut">
              <div class="info_area ed_info">9/11/2012</div>
              <div class="info_area sd_info">1/10/2012</div>
              <div class="info_area cn_info">Customer Name Ltd</div>
              <div class="info_area pn_info">3489572897</div>
              <div class="info_area na_info">Project Name</div>
            </li>
            <li class="li_defalut">
              <div class="info_area ed_info">9/11/2012</div>
              <div class="info_area sd_info">1/10/2012</div>
              <div class="info_area cn_info">Customer Name Ltd</div>
              <div class="info_area pn_info">3489572897</div>
              <div class="info_area na_info">Project Name</div>
            </li>
            <li class="li_defalut">
              <div class="info_area ed_info">9/11/2012</div>
              <div class="info_area sd_info">1/10/2012</div>
              <div class="info_area cn_info">Customer Name Ltd</div>
              <div class="info_area pn_info">3489572897</div>
              <div class="info_area na_info">Project Name</div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
