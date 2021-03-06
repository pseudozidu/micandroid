/*
 * ReportXlsUtil.java 2010-11-11
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.report.util;

import java.util.List;
import java.util.Map;

import org.jxstar.util.DateUtil;
import org.jxstar.util.FileUtil;
import org.jxstar.util.StringUtil;
import org.jxstar.util.config.SystemVar;

/**
 * 输出html报表的工具类。
 *
 * @author TonyTan
 * @version 1.0, 2010-11-11
 */
public class ReportHtmlUtil extends ReportUtil {
    
    /**
     * 返回javascript定义变量
     * @return String
     */
    public static String defineHead() {
        StringBuffer sbRet = new StringBuffer();
        //获取一个table对象
        sbRet.append("var tblobj = f_getTblObj();\r\n");

        //保存一个空的table模板
        sbRet.append("var tblValue = tblobj.innerHTML;\r\n");
        
        //定义一个新的table对象变量
        sbRet.append("var newTblObj = null;\r\n");

        //定义一个单元格位置变量
        sbRet.append("var posi = new Array();\r\n");
        
        //定义单元格内容变量
        sbRet.append("var cellValue = \"\";\r\n");
        return sbRet.toString();
    }
    
    /**
     * 填写报表头信息
     * @param lsHeadInfo -- 报表头信息
     * @return String
     */
    public static String fillHead(String jsTblObj, List<Map<String,String>> lsHeadInfo) {
        StringBuilder sbRet = new StringBuilder();
        if (lsHeadInfo == null || lsHeadInfo.isEmpty()) 
            return sbRet.toString();
        
        Map<String,String> mpHeadInfo = null;
        String strColName = null, strColValue = null, strColPostion = null;
        String strValue = "";
        int posi[] = null;

        for (int i = 0, n = lsHeadInfo.size(); i < n; i++) {
            mpHeadInfo = lsHeadInfo.get(i);
            if (mpHeadInfo.isEmpty()) continue;

            strColName = mpHeadInfo.get("display");
            strColValue = mpHeadInfo.get("col_code");
            strColPostion = mpHeadInfo.get("col_pos");

            posi = getPosition(strColPostion);
            if (posi.length != 2) {
                _log.showWarn(strColName + " ["+strColPostion+"] position is error!");
                continue;
            }

            //设置指定坐标位置
            sbRet.append("posi[0] = " + posi[0] + ";\r\n");
            sbRet.append("posi[1] = " + posi[1] + ";\r\n");
            
            if (strColValue.equalsIgnoreCase("{CURUSERNAME}")) {
            //当前用户
                if (_mpUser != null) strValue = _mpUser.get("user_name");
            } else if (strColValue.equalsIgnoreCase("{CURDATE}")) {
            //当前日期
                strValue = DateUtil.getToday();
            } else if (strColValue.equalsIgnoreCase("{CURDEPTNAME}")) {
            //当前部门
                if (_mpUser != null) strValue = _mpUser.get("dept_name");
                else strValue = "";
            } else {
            //设置cell的显示值
                strValue = strColValue;
                strValue = (strValue.equalsIgnoreCase("null"))?"":strValue;
            }

            //设置当前字段内容
            sbRet.append("cellValue = \"" + StringUtil.strForJson(strValue) + "\";\r\n");
            sbRet.append("f_setCellValueByPos(posi ,cellValue ,"+jsTblObj+");\r\n");
        }

        return sbRet.toString();
    }

    /**
     * 填写form格式的html报表
     * @param mpData -- 记录值
     * @param lsField -- 字段信息
     * @param jsTblObj -- 当前javascript中使用的table对象
     * @param curPage -- 当前页号，用于输出页码信息
     * @param sumPage -- 总页数，用于输出页码信息
     * @return String
     */
    public static String fillForm(Map<String,String> mpData,
            List<Map<String,String>> lsField,
            String jsTblObj, int curPage, int sumPage) {
        StringBuffer sbRet = new StringBuffer();
        if (lsField == null || mpData == null) {
            return sbRet.toString();
        }
        if (lsField.isEmpty() || mpData.isEmpty()) {
            _log.showDebug("data is empty or field is null!");
            return sbRet.toString();
        }
        
        //单元格的值
        String strValue = "";
        for (int i = 0, n = lsField.size(); i < n; i++ ) {
            Map<String,String> mpField = lsField.get(i);
            
            String strStyle = mpField.get("format");                //字段格式
            String isOutZero = mpField.get("is_outzero");           //0是否输出
            if (isOutZero == null) isOutZero = "1";
            String strColName = mpField.get("display");             //字段名称
            String strColCode = (mpField.get("col_code")).toLowerCase();        //字段编码
            String strColTag = mpField.get("combo_code");           //标签
            
            int[] posi = getPosition(mpField.get("col_pos"));
            if (posi.length != 2) {
                _log.showWarn(strColName + " ["+posi+"] position is error!");
                continue;
            }

            //设置指定坐标位置
            sbRet.append("posi[0] = " + posi[0] + ";\r\n");
            sbRet.append("posi[1] = " + posi[1] + ";\r\n");

            if (strColCode.equalsIgnoreCase("{CURUSERNAME}")) {
            //当前用户
                if (_mpUser != null) strValue = _mpUser.get("user_name");
            } else if (strColCode.equalsIgnoreCase("{CURDATE}")) {
            //当前日期
                strValue = DateUtil.getToday();
            } else if (strColCode.equalsIgnoreCase("{CURDEPTNAME}")) {
            //当前部门
                if (_mpUser != null) strValue = _mpUser.get("dept_name");
                else strValue = "";
            } else if (strColCode.equalsIgnoreCase("{CURPAGENUM}")) {
            //当前所在页数
                strValue = Integer.toString(curPage);
            } else if (strColCode.equalsIgnoreCase("{CURSUMPAGE}")) {
            //当前共页数
                strValue = Integer.toString(sumPage);
            } else {
            //设置cell的显示值，如果是图片字段，则不用处理
                if (!strStyle.equals("image")) {
                    strValue = mpData.get(strColCode);
                    strValue = (strValue != null)?strValue:"";
                    strValue = (strValue.equalsIgnoreCase("null"))?"":strValue;
                    
                    //取选项显示值
                    strValue = getComboTitle(strValue, strColTag);
                    //转换数据格式
                    strValue = convertValue(strValue, strStyle);
                    if (isOutZero.equals("0")) strValue = getZeroOut(strValue, strStyle);
                }
            }
            
            //设置当前字段内容
            sbRet.append("cellValue = \"" + StringUtil.strForJson(strValue) + "\";\r\n");
            sbRet.append("f_setCellValueByPos(posi ,cellValue ,"+jsTblObj+");\r\n");
        }

        return sbRet.toString();
    }

    /**
     * 先找该报表是否有审批信息报表输出定义，取定义信息；
     * 然后根据数据ID、过程ID、节点ID找审批信息，如果有则输出；
     * @param funId -- 功能ID
     * @param areaId -- 报表区域ID
     * @param jsTblObj -- 当前javascript中使用的table对象
     * @param mpData -- 记录值
     * @return
     */
    public static String fillCheckInfo(String funId, String areaId, String jsTblObj, Map<String,String> mpData) {
        //取审批信息报表输出定义
        List<Map<String,String>> lsField = ReportDao.getAreaWfCol(areaId);
        if (lsField == null || lsField.isEmpty()) {
            _log.showDebug("--------outCheckInfo(): not find report detail wfcol.");
            return "";
        }
        
        //取记录主键值
        String dataId = getKeyValue(funId, mpData);
        
        StringBuilder sbRet = new StringBuilder(); 
        //保存已取审批信息的节点ID
        String preNodeId = "";
        Map<String,String> mpSign = null;
        //保存取出的审批信息
        Map<String,String> mpCheck = null;
        for (int i = 0, n = lsField.size(); i < n; i++) {
            Map<String,String> mpField = lsField.get(i);
            
            String strStyle = mpField.get("format");
            String nodeId = mpField.get("node_id");
            String colCode = mpField.get("col_code");
            String colPos = mpField.get("col_pos");
            String viewPos = mpField.get("view_pos");
            String processId = mpField.get("process_id");
            
            //如果是同一个节点，则不用重新去审批信息，否则需要取审批信息，
            //因为一个节点有三个字段的信息要输出到报表中：check_userid, check_user, check_date, check_desc
            if (!preNodeId.equals(nodeId)) {
            	mpSign = ReportDao.getNodeAttr(processId, nodeId);
                mpCheck = ReportDao.getCheckInfo(funId, dataId, processId, nodeId);
            }
            
            if (mpCheck == null || mpCheck.isEmpty()) continue;
            
            int[] posi = getPosition(colPos);
            if (posi.length != 2) {
                _log.showWarn(colCode + " ["+posi+"] position is error!");
                continue;
            }

            //设置指定坐标位置
            sbRet.append("posi[0] = " + posi[0] + ";\r\n");
            sbRet.append("posi[1] = " + posi[1] + ";\r\n");
            
            String valHtml = "";
            String scriptFun = "f_setCellValueByPos(";
            String userId = mpCheck.get("check_userid");
            //如果显示部门印章，如果没有设置印章，则直接退出
            if (colCode.equals("check_sign")) {
            	valHtml = signPicHtml(userId, colCode, mpSign);
            	if (valHtml.length() == 0) continue;
            	
            	int[] vpos = getPosition(viewPos);
            	if (vpos.length != 2) {
            		scriptFun = "f_setCellPic(180, 180, 0, 0, ";
            	} else {
            		scriptFun = "f_setCellPic(180, 180, "+ vpos[0] +", "+ vpos[1] +", ";
            	}
            } else if (colCode.equals("check_user")) {
            	String userCode = ReportDao.getUserCode(userId);
            	valHtml = signPicHtml(userCode, colCode, mpSign);
            	if (valHtml.length() == 0) {
            		valHtml = mpCheck.get(colCode);
            	} else {
            		int[] vpos = getPosition(viewPos);
                	if (vpos.length != 2) {
                		scriptFun = "f_setCellPic(150, 75, 0, 0, ";
                	} else {
                		scriptFun = "f_setCellPic(150, 75, "+ vpos[0] +", "+ vpos[1] +", ";
                	}
            	}
            } else {
            	valHtml = mpCheck.get(colCode);
            	valHtml = convertValue(valHtml, strStyle);
            	valHtml = StringUtil.strForJson(valHtml);
            }
            _log.showDebug("................checkfield={0}; checkvalue={1}; reportpos={2}", colCode, valHtml, colPos);
            
            //设置当前字段内容
            sbRet.append("cellValue = \"" + valHtml + "\";\r\n");
            sbRet.append(scriptFun + "posi ,cellValue ,"+jsTblObj+");\r\n");
        }
        
        return sbRet.toString();
    }
    
    /**
     * 取是否有印章文件
     * @param userId -- 用户ID
     * @param colCode -- 报表字段名
     * @param mpSign -- 是否显示印章的设置信息
     * @return
     */
    private static String signPicHtml(String userId, String colCode, Map<String,String> mpSign) {
    	if (userId == null || userId.length() == 0 || 
    			mpSign == null || mpSign.isEmpty() ||
    			colCode == null || colCode.length() == 0) return "";
    	
    	if (colCode.equals("check_user")) {
    		//流程节点是否显示个人签名
    		String use = mpSign.get("user_sign");
    		if (use.equals("1")) {
    			return getUserSign(userId);
    		}
    	} else if (colCode.equals("check_sign")) {
    		//流程节点是否显示部门印章
    		String use = mpSign.get("dept_sign");
    		if (use.equals("1")) {
    			return getDeptSign(userId);
    		}
    	}
    	
    	return "";
    }
    
    /**
     * 取当前用户的个人签名的html
     * @param userId
     * @return
     */
    private static String getUserSign(String userId) {
    	String fileName = SystemVar.REALPATH + "/report/sign/user/" + userId + ".gif";
    	if (FileUtil.exists(fileName)) {
    		String img = "../../sign/user/" + userId + ".gif";
    		return "<img src='"+ img +"' width='150' />";
    	}
    	
    	return "";
    }
    
    /**
     * 取当前用户所在部门的印章的html
     * @param userId
     * @return
     */
    private static String getDeptSign(String userId) {
    	String deptId = ReportDao.getDeptId(userId);
    	String fileName = SystemVar.REALPATH + "/report/sign/dept/" + deptId + ".gif";
    	if (FileUtil.exists(fileName)) {
    		String img = "../../sign/dept/" + deptId + ".gif";
    		return "<img src='"+ img +"' />";
    	}
    	
    	return "";
    }
}
