<%
/**
 * Light Portal
 *
 * Copyright (c) 2009, Light Portal, Inc or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Light Portal, Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
</head>
<body>
<fmt:bundle basename="resourceBundle">
<table border="0" cellpadding="0" cellspacing="0">
<tr>
<td class="portlet-table-td-left"><br/><br/><font size='3'><fmt:message key="portlet.info.header1"/></font></td>
</tr>
<tr>
<td class="portlet-table-td-left">
<p><br/><br/><font color='#ff6600'><b><fmt:message key="portlet.info.header2"/></b></font><br/></p>
<ul>
<li STYLE="padding:5px 15px 4px 6px; list-style-image:  url(light/images/recommended.gif)"><fmt:message key="portlet.info.list1"/></li>
<li STYLE="padding:5px 15px 4px 6px; list-style-image:  url(light/images/friend.gif)"><fmt:message key="portlet.info.list2"/></li>
<li STYLE="padding:5px 15px 4px 6px; list-style-image:  url(light/images/privacy.gif)"><fmt:message key="portlet.info.list3"/></li>
<li STYLE="padding:5px 15px 4px 6px; list-style-image:  url(light/images/group.gif)"><fmt:message key="portlet.info.list4"/></li>
</ul>
</td>
</tr>    
</table>
<span class="portlet-rss" style="text-align:right;">
<a href='javascript:void(0)' onclick="<portlet:renderURL  windowState='MAXIMIZED'/>" >more...</a>
</span>
</fmt:bundle>
</body>
</html>