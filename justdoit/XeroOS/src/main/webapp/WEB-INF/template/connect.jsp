<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8">
<title>Connect</title>
<link href="/css/GDP-common.css" rel="stylesheet" type="text/css">
</head>
<!--[if lte IE 8]>

<style>

</style>

<![endif]-->
<body>
  <div class="container">
    <div class="header">
      <a href="/"><img src="/images/gdp_logo.png" alt="GDP_logo"></a>
      <span>Global Design &amp; Production</span>
    </div>
    <div class="connect_content">
      <div class="left_section">
        <h1>Lets connect up your Xero account</h1>
        <h2>Import all your business contacts from xero.com</h2>
        <p>Xero is great for acocunting and as your already using it you can bring all your contacts into GDP!</p>
        <p>Changing contact details in Xero will automatically change your contacts in GDP, and changing contacts GDP will change your contacts in Xero.com, keeping eveything nicely synchronised.</p>
        <p>After selecting "Connect" we are going to leave GDP and we are going to take you to the Xero login page, you will need login with your Xero email and password, the authorise GDP to connect to Xero.</p>
        <p>After you complete these two steps you will magically be returned to GDP and all your Xero contacts will be available for you to start linking to projects as either suppliers or customers.</p>
        <p>It wont take long, go ahead and click Connect!</p>
        <div class="button_field">
          <div class="skip_button"><a href="/contact">SKIP THIS</a></div>
          <div class="connect_button" id="connect_button"><a href="/oauth/xero">CONNECT</a></div>
        </div>
      </div>
      <div class="right_section">
        <img src="/images/xero_advert.png" alt="x_adv">
      </div>
    </div>
  </div>
</body>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script>
	$(function(){
		$("#connect_button").click(function(){
			$(".connect_button").addClass("loading_btn");
		});
	});
</script>
</html>
