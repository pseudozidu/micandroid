<%@ page import="java.util.*,org.light.portal.util.*,org.json.*" %>

<ul id="tabList">
	<%
		List<LabelBean> list = (List<LabelBean>)request.getAttribute(Constants._PORTAL_INIT_LIST);
		if(list != null){
			for(LabelBean bean : list){	
				if(bean.getId() == "tabsJSON"){
					JSONArray jarray =  JSONUtil.parseArray(bean.getDesc());
					if(jarray != null){
						for(int i=0;i<jarray.length();i++){
							JSONObject json = jarray.getJSONObject(i);
							if(i==0){
	%>
							<div id='panel_<%= json.get("id") %>' style="display:block;" class="Panel" color="" panelurl="">
								<jsp:include page='<%= org.light.portal.util.PropUtil.getString("default.pannelInner.ipad.page") %>' />								
							</div>
	<%
							}else{
	%>
							<div id='panel_<%= json.get("id") %>' style="display:none" class="Panel" color="" panelurl=""></div>											
	<%
							}
						}
					}
	
				}
			}
		}
	%>
</ul>