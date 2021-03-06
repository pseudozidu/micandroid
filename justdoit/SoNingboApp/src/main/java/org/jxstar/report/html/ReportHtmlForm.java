/*
 * ReportXlsForm.java 2010-11-11
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.report.html;

import java.util.Map;

import org.jxstar.report.ReportException;
import org.jxstar.report.util.ReportHtmlUtil;

/**
 * 输出Html表单报表。
 *
 * @author TonyTan
 * @version 1.0, 2010-11-12
 */
public class ReportHtmlForm extends ReportHtml {
    
    public Object output() throws ReportException {
        _log.showDebug("html form report output ...");
        //取报表所属功能ID，用于报表图片输出时取功能表名、主键信息用，取功能审批信息用
        String funId = _mpReptInfo.get("fun_id");
        //报表区域ID
        String areaId = _mpMainArea.get("area_id");
        
        StringBuffer sbRet = new StringBuffer();
        //获取javascript变量声明
        sbRet.append(ReportHtmlUtil.defineHead());

        Map<String, String> mpValue = null;
        for (int i = 0 ,n = (_lsMainRecord.size() >= _imaxPage)?_imaxPage:_lsMainRecord.size() ;i < n ;i++) {
            mpValue = _lsMainRecord.get(i);

            if (i == 0) {
                sbRet.append(ReportHtmlUtil.fillForm(mpValue, _lsMainCol, "tblobj", i + 1, _lsMainRecord.size()));
                sbRet.append(ReportHtmlUtil.fillCheckInfo(funId, areaId, "tblobj", mpValue));
                sbRet.append(ReportHtmlUtil.fillHead("tblobj" ,_lsHeadInfo));
            } else {
                //插入新table
                sbRet.append("newTblObj = f_insertTable(tblValue ,tblobj);\r\n");

                sbRet.append(ReportHtmlUtil.fillForm(mpValue, _lsMainCol, "newTblObj", i + 1, _lsMainRecord.size()));
                sbRet.append(ReportHtmlUtil.fillCheckInfo(funId, areaId, "newTblObj", mpValue));
                sbRet.append(ReportHtmlUtil.fillHead("tblobj" ,_lsHeadInfo));
            }
        }
        
        return sbRet.toString();
    }

}
