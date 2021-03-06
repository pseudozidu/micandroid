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

import java.io.IOException;

import javax.portlet.PortletURL;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Function: Supporting class for the <CODE>actionURL</CODE> and <CODE>renderURL</CODE> tag.
 * Creates a url that points to the current Portlet and triggers an action request
 * with the supplied parameters.
 *
 * @author Jianmin Liu
 **/
public abstract class BasicURLTag extends TagSupport
{

    public static class TEI extends TagExtraInfo
    {
        public VariableInfo[] getVariableInfo(TagData tagData)
        {
            VariableInfo vi[] = null;
            String var = tagData.getAttributeString("var");
            if (var != null)
            {
                vi = new VariableInfo[1];
                vi[0] = new VariableInfo(var, "java.lang.String", true, VariableInfo.AT_END);
            }
            return vi;
        }
    }

    protected String portletMode;
    protected String secure;
    protected Boolean secureBoolean;
    protected String windowState;
    protected PortletURL url;
    protected String var;

    /**
     * Processes the <CODE>actionURL</CODE> or <CODE>renderURL</CODE> tag.
     * @return int
     */
    public abstract int doStartTag() throws JspException;

    /**
     *
     * @return int
     */
    public int doEndTag() throws JspException {
        if (var == null)
        {
            try
            {
                JspWriter writer = pageContext.getOut();
                writer.print(url); 
                //writer.flush();
            }
            catch (IOException ioe)
            {
                throw new JspException("actionURL/renderURL Tag Exception: cannot write to the output writer.");
            }
        } else {
                pageContext.setAttribute (var, url.toString(), PageContext.PAGE_SCOPE);
        }
        return EVAL_PAGE;
    }

    /**
     * Returns the portletMode.
     * @return String
     */
    public String getPortletMode()
    {
        return portletMode;
    }

    /**
     * @return secure as String
     */
    public String getSecure()
    {
        return secure;
    }

    /**
     * @return secure as Boolean
     */
    public boolean getSecureBoolean()
    {
        return this.secureBoolean.booleanValue();
    }

    /**
     * Returns the windowState.
     * @return String
     */
    public String getWindowState()
    {
        return windowState;
    }

    /**
     * @return PortletURL
     */
    public PortletURL getUrl()
    {
        return url;
    }

    /**
     * Returns the var.
     * @return String
     */
    public String getVar()
    {
        return var;
    }

    /**
     * Sets the portletMode.
     * @param portletMode The portletMode to set
     */
    public void setPortletMode(String portletMode)
    {
        this.portletMode = portletMode;
    }

    /**
     * Sets secure to boolean value of the string
     * @param secure
     */
    public void setSecure(String secure)
    {
        this.secure = secure;
        this.secureBoolean = new Boolean(secure);
    }

    /**
     * Sets the windowState.
     * @param windowState The windowState to set
     */
    public void setWindowState(String windowState)
    {
        this.windowState = windowState;
    }

    /**
     * Sets the url.
     * @param url The url to set
     */
    public void setUrl(PortletURL url)
    {
        this.url = url;
    }

    /**
     * Sets the var.
     * @param var The var to set
     */
    public void setVar(String var)
    {
        this.var = var;
    }
}
