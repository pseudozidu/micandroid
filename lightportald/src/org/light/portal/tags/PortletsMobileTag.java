 /*
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

package org.light.portal.tags;

import static org.light.portal.util.Constants._PORTLET_MINIMIZED;
import static org.light.portal.util.Constants._PORTLET_MODE_HEADER;
import static org.light.portal.util.Constants._PORTLET_RENDER_ID_PREFIX;
import static org.light.portal.util.Constants._PORTLET_SYNC_LOAD;

import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.light.portal.logger.Logger;
import org.light.portal.logger.LoggerFactory;
import org.light.portal.model.PortalTab;
import org.light.portal.model.PortletObject;
import org.light.portal.model.User;
import org.light.portal.portlet.core.impl.PortletWindow;
import org.light.portal.portlet.factory.PortletContainerFactory;

/**
 * 
 * @author Jianmin Liu
 **/
public class PortletsMobileTag extends BaseTag {
	private Logger logger = LoggerFactory.getLogger(this.getClass()); 
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();		
		HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
		try{
			User user = this.getUser(request);
	    	if(user != null){
	    		List<PortalTab> tabs = this.getPortalService(request).getUserPersonalPortalTab(user.getUserId(),user.getOrgId());
		    	if(tabs != null && tabs.size() > 0){
		    		for(PortalTab tab : tabs){
	    				List<PortletObject> portlets = this.getPortalService(request).getPortletsByTab(tab.getId());               
	    		    	for(PortletObject po : portlets){
	    		    		if(po.isSupportMobile() && po.getType() >= _PORTLET_SYNC_LOAD){
	    		    			try{		    			    		
		    			    		request.setAttribute("portletId",String.valueOf(po.getId()));
						    		request.setAttribute("portletObject",po);
					        		request.setAttribute("responseId",_PORTLET_RENDER_ID_PREFIX+po.getId());
					        		PortletMode mode = PortletMode.VIEW;	        		
					        		WindowState state = WindowState.NORMAL;
				        		    if(po.getWindowStatus() == 1) state = WindowState.MINIMIZED;   
				        		    if(po.getWindowStatus() == 2) state = WindowState.MAXIMIZED; 
				        		    if(po.getMode() == 1) mode = PortletMode.EDIT;        		    
				        		    if(po.getMode() == 2) mode = PortletMode.HELP;    
					        		String suffix = ".view";
					        		if(po.getType() > _PORTLET_SYNC_LOAD) suffix = ".data";
					        		if(WindowState.MAXIMIZED.equals(state))	suffix=".max"+suffix;
					        		String portletName = po.getPath();
					        		if(portletName.startsWith("/")) portletName = portletName.substring(1);
					        		if(portletName.indexOf(".") > 0) portletName = portletName.substring(0,portletName.indexOf("."));
					        		PortletWindow portletWindow = new PortletWindow(portletName, state, mode, po);
						    		if(po.getWindowStatus() != _PORTLET_MINIMIZED){
						        		response.getWriter().print("<textarea id='portlet"+po.getId()+suffix+"' style='display:none;' rows='0' cols='0'>"); 
							    		PortletContainerFactory.getPortletContainer().renderPortlet(portletWindow, request, response);
							    		response.getWriter().println("</textarea>"); 
						    		}
						    		suffix = ".title";
						    		portletWindow.setPortletMode(new PortletMode(_PORTLET_MODE_HEADER));
						    		response.getWriter().print("<textarea id='portlet"+po.getId()+suffix+"' style='display:none;' rows='0' cols='0'>"); 
						    		PortletContainerFactory.getPortletContainer().renderPortlet(portletWindow, request, response);
						    		response.getWriter().println("</textarea>"); 
	    		    			}catch(Exception e){
	    		    				//ignore individual portlet rendering exception
	    		    				logger.error(e);
	    		    			}
	    		    		}
	    		     	}
		    		}
		    	}
	    	}
		}catch(Exception e){
	    	logger.error(e);
	    }
		
        return EVAL_PAGE;
    }
}