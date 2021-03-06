/*
 * ReportXlsUtil.java 2010-11-11
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.report.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import org.jxstar.service.BoException;
import org.jxstar.service.define.FunDefineDao;
import org.jxstar.service.studio.AttachBO;
import org.jxstar.util.DateUtil;
import org.jxstar.util.StringUtil;
import org.jxstar.util.factory.FactoryUtil;
import org.jxstar.util.resource.JsMessage;

/**
 * 输出excel报表的工具类。
 *
 * @author TonyTan
 * @version 1.0, 2010-11-11
 */
public class ReportXlsUtil extends ReportUtil {
	/**
	 * 填充表头信息
	 * @param sheet -- 报表对象
	 * @param lsHeadInfo -- 头部定义信息
	 * @return
	 */
	public static HSSFSheet fillHead(HSSFSheet sheet, List<Map<String,String>> lsHeadInfo) {
		if (lsHeadInfo == null || lsHeadInfo.isEmpty()) 
			return sheet;

		Map<String,String> mpHeadInfo = null;
		String strColName = null, strColValue = null, strColPostion = null;

		HSSFRow row = null;
		HSSFCell cell = null;
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

			//获取指定的单元格
			row = sheet.getRow(posi[0]);
			if (row == null) row = sheet.createRow(posi[0]);
			cell = row.getCell(posi[1]);
			if (cell == null) cell = row.createCell(posi[1]);
			
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

			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(strValue.trim());
		}
		
		return sheet;
	}
	
	/**
	 * 填充数据到表格对象。
	 * @param sheet -- 表格对象
	 * @param lsData -- 输出数据记录
	 * @param lsField -- 输出字段明细
	 * @param pageSize -- 每页显示行数
	 * @param pos -- 开始输出行数
	 * @param curPage -- 当前是第几页
	 * @param sumPage -- 总页数
	 * @return
	 */
	public static HSSFSheet fillGrid(HSSFSheet sheet, 
			List<Map<String,String>> lsData,
			List<Map<String,String>> lsField,
			int pageSize, int pos, int curPage, int sumPage) {
		if (lsField == null || lsData == null) {
			_log.showWarn("data is null or field is null!");
			return sheet;
		}
		
		if (lsField.isEmpty() || lsData.isEmpty()) {
			_log.showDebug("data is empty or field is null!");
			return sheet;
		}
		
		HSSFRow row = null;					//excel的行对象
		HSSFCell cell = null;				//excel的列对象
		String strValue = null;				//每个格的信息内容
		Map<String,String> mpData = null;	//每条记录数据
		Map<String,String> mpField = null;	//每条个字段的信息
		
		//HSSFCellStyle style = null;			//excel单元格风格对象
		
		int posi = (pageSize > 0 && pos >= 0)?pos:0;
		int cnt = (pageSize <= 0)?lsData.size():pageSize + posi;
		int[] posis = new int[2];
		int index = 0, rowIndex = 0;
		int currRow = 0;
		int cntCol = 1; //合计列的位置
		String strStyle = null, strColName = null, strColCode = null, strColTag = null;
		
		//用于每页小计
		List<Map<String,String>> lsStatCol = getStatField(lsField);
		String isOutZero = "0";
		Map<String,String> mpStat = null, mpStatValue = FactoryUtil.newMap();
		String strCol = null;
		BigDecimal bdStat = null;
		boolean isStatCol = false;
		//用于每页小计
		
		for (rowIndex = posi, index = 0; rowIndex < cnt; rowIndex++, index++) {
			if (lsData.size() <= rowIndex) break;					//如果rowIndex大于记录数
			mpData = lsData.get(rowIndex);
			
			for (int i = 0, n = lsField.size(); i < n; i++ ) {
				mpField = lsField.get(i);
				isOutZero = mpField.get("is_outzero");
				if (isOutZero == null) isOutZero = "1";
				strStyle = mpField.get("format");					//字段格式
				
				strColName = mpField.get("display");				//字段名称
				strColCode = mpField.get("col_code").toLowerCase();	//字段编码
				strColTag = mpField.get("combo_code");				//标签
				
				posis = getPosition(mpField.get("col_pos"));
				if (posis.length != 2) {
					_log.showWarn(strColName + " ["+mpField.get("col_pos")+"] position is error!");
					continue;
				}
				//_log.showDebug("col_code=" + strColCode + " col_pos=" + posis);
				
				row = sheet.getRow(posis[0] + index);
				currRow = posis[0] + index;
				if (row == null) row = sheet.createRow(posis[0] + index);
				
				cell = row.getCell(posis[1]);
				if (cell == null) cell = row.createCell(posis[1]);
				
				if (strColCode.equalsIgnoreCase("{CURUSERNAME}")) {
				//当前用户
					if (_mpUser != null) strValue = _mpUser.get("user_name");
					else strValue = "";
				} else if (strColCode.equalsIgnoreCase("{CURDATE}")) {
				//当前日期
					strValue = DateUtil.getToday();
				} else if (strColCode.equalsIgnoreCase("{CURDEPTNAME}")) {
				//当前部门
					if (_mpUser != null) strValue = _mpUser.get("dept_name");
					else strValue = "";
				} else if (strColCode.equalsIgnoreCase("{NUMBER}")) {
				//输出序号
					strValue = Integer.toString(rowIndex+1);
					cntCol = (short)posis[1];
				} else if (strColCode.equalsIgnoreCase("{CURPAGENUM}")) {
				//当前所在页数
					strValue = Integer.toString(curPage);
				} else if (strColCode.equalsIgnoreCase("{CURSUMPAGE}")) {
				//当前共页数
					strValue = Integer.toString(sumPage);
				} else {
				//设置cell的显示值
					strValue = mpData.get(strColCode);
					//如果已经设置了0值不输出，并且当前值就是0，则输出空字符串
					strValue = (strValue != null)?strValue:"";
					strValue = (strValue.equalsIgnoreCase("null"))?"":strValue;
				
					//真实值与显示值
					strValue = getComboTitle(strValue, strColTag);
					//转换数据格式
					strValue = convertValue(strValue, strStyle);
					if (isOutZero.equals("0")) strValue = getZeroOut(strValue, strStyle);
				}
				cell.setCellValue(strValue.trim());
				
				if (!lsStatCol.isEmpty()) {
					for (int iStat = 0, statNum = lsStatCol.size(); iStat < statNum; iStat++) {
						mpStat = lsStatCol.get(iStat);
						//_log.showDebug("mpStat = " + mpStat.toString());
					
						if (mpStat.isEmpty()) continue;
						strCol = ( mpStat.get("col_code")).toLowerCase();
						//_log.showDebug("strCol = " + strCol + " | strValue = " + strValue);						
						if (strColCode.equalsIgnoreCase(strCol)) {
							isStatCol = true;
					
							if ( mpStatValue.get(strCol) == null) bdStat = new BigDecimal("0");
							else bdStat = new BigDecimal( mpStatValue.get(strCol));
					
							if (strValue.length() == 0) strValue = "0";
							//_log.showDebug("bdStat.add(new BigDecimal(strValue)) = " + bdStat.add(new BigDecimal(strValue)));
					
							mpStatValue.put(strCol, bdStat.add(new BigDecimal(strValue)).toString());
						}
					}
				}
			}
		}
		
		//填写每页小计
		if (isStatCol == true) {
			currRow ++;
			row = sheet.getRow(currRow);
			if (row == null) row = sheet.createRow(currRow);
			cell = row.getCell(cntCol);
			if (cell == null) cell = row.createCell(cntCol);
			cell.setCellValue("小计");
			for (int i = 0, colNum = lsStatCol.size(); i < colNum; i ++){
				mpField = lsStatCol.get(i);
				posis = getPosition(mpField.get("col_pos"));
				
				//if (row == null) row = sheet.createRow(posi + cnt);
				
				cell = row.getCell(posis[1]);
				if (cell == null) cell = row.createCell(posis[1]);
				
				strColCode = (mpField.get("col_code")).toLowerCase();
				strValue = mpStatValue.get(strColCode);
				strStyle = mpField.get("format");
				strValue = convertValue(strValue, strStyle);
				cell.setCellValue(strValue.trim());
			}
		}
		return sheet;
	}
	
	/**
	 * 填充数据到表单对象。
	 * @param funId -- 报表所属功能ID
	 * @param sheet -- 表格对象
	 * @param mpData -- 表单数据
	 * @param lsField -- 字段信息
	 * @param curPage -- 当前页数
	 * @param sumPage -- 总页数
	 * @return
	 */
	public static HSSFSheet fillForm(String funId,
			HSSFSheet sheet,
		  	Map<String,String> mpData,
		  	List<Map<String,String>> lsField,
			int curPage, int sumPage) {
		if (lsField == null || mpData == null) {
			return sheet;
		}
		if (lsField.isEmpty() || mpData.isEmpty()) {
			_log.showDebug("data is empty or field is null!");
			return sheet;
		}
		
		//单元格的值
		String strValue = "";
		for (int i = 0, n = lsField.size(); i < n; i++ ) {
			Map<String,String> mpField = lsField.get(i);
			
			String strStyle = mpField.get("format");				//字段格式
			String isOutZero = mpField.get("is_outzero");			//0是否输出
			if (isOutZero == null) isOutZero = "1";
			String strColName = mpField.get("display");				//字段名称
			String strColCode = (mpField.get("col_code")).toLowerCase();		//字段编码
			String strColTag = mpField.get("combo_code");			//标签
			
			int[] posi = getPosition(mpField.get("col_pos"));
			if (posi.length != 2) {
				_log.showWarn(strColName + " ["+mpField.get("col_pos")+"] position is error!");
				continue;
			}
			
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
			
			//获取指定的单元格
			HSSFRow row = sheet.getRow(posi[0]);
			if (row == null) row = sheet.createRow(posi[0]);
			HSSFCell cell = row.getCell(posi[1]);
			if (cell == null) cell = row.createCell(posi[1]);
			//填充单元格内容
			if (!strStyle.equals("image")) {
				cell.setCellValue(strValue.trim());
			} else {
				//如果填充图片不成功能，则填写文字
				if (!printCellImage(funId, strColCode, mpData, cell)) {
					cell.setCellValue(JsMessage.getValue("report.xlsutil.noimage"));
				}
			}
		}
		
		return sheet;
	}
	
    /**
     * 先找该报表是否有审批信息报表输出定义，取定义信息；
     * 然后根据数据ID、过程ID、节点ID找审批信息，如果有则输出；
     * @param funId -- 功能ID
     * @param areaId -- 报表区域ID
     * @param sheet -- 报表表单
     * @param mpData -- 记录值
     * @return
     */
    public static HSSFSheet fillCheckInfo(String funId, String areaId, HSSFSheet sheet, Map<String,String> mpData) {
        //取审批信息报表输出定义
        List<Map<String,String>> lsField = ReportDao.getAreaWfCol(areaId);
        if (lsField == null || lsField.isEmpty()) {
            _log.showDebug("--------outCheckInfo(): not find report detail wfcol.");
            return sheet;
        }
        
        //取记录主键值
        String dataId = getKeyValue(funId, mpData);
        
        //保存已取审批信息的节点ID
        String preNodeId = "";
        //保存取出的审批信息
        Map<String,String> mpCheck = null;
        for (int i = 0, n = lsField.size(); i < n; i++) {
            Map<String,String> mpField = lsField.get(i);
            
            String format = mpField.get("format");
            String nodeId = mpField.get("node_id");
            String colCode = mpField.get("col_code");
            String colPos = mpField.get("col_pos");
            String processId = mpField.get("process_id");
            
            //如果是同一个节点，则不用重新去审批信息，否则需要取审批信息，
            //因为一个节点有三个字段的信息要输出到报表中：check_user, check_date, check_desc
            if (!preNodeId.equals(nodeId)) {
                mpCheck = ReportDao.getCheckInfo(funId, dataId, processId, nodeId);
            }
            
            if (mpCheck == null || mpCheck.isEmpty()) continue;
            
            String strValue = mpCheck.get(colCode);
            strValue = convertValue(strValue, format);
            _log.showDebug("................checkfield={0}; checkvalue={1}; reportpos={2}; format={3}", colCode, strValue, colPos, format);
            
            int[] posi = getPosition(mpField.get("col_pos"));
            if (posi.length != 2) {
                _log.showWarn(colCode + " ["+posi+"] position is error!");
                continue;
            }

            //获取指定的单元格
            HSSFRow row = sheet.getRow(posi[0]);
            if (row == null) row = sheet.createRow(posi[0]);
            HSSFCell cell = row.getCell(posi[1]);
            if (cell == null) cell = row.createCell(posi[1]);
            //填充单元格内容
            cell.setCellValue(strValue.trim());
        }
        
        return sheet;
    }
	
	/**
	 * 添加临时表格内容
	 * @param mainSheet -- 原表单内容
	 * @param subSheet -- 临时表单内容
	 * @return
	 */
	public static HSSFSheet appendSheet(HSSFSheet mainSheet, HSSFSheet subSheet) {
		if (mainSheet == null || subSheet == null) return null;
		//判断报表是否允许输出
		if (!isAllowOut(mainSheet)) return mainSheet;
		//原报表的最后一行
		int endRowNum = mainSheet.getPhysicalNumberOfRows();

		HSSFRow sourow = null, descrow = null;
		HSSFCell sourcell = null, descell = null, orgcell = null;
		int i = 0, offsetcnt = 0;
		
		//复制表格中的图片
		copySheetImage(mainSheet.getWorkbook(), subSheet.getWorkbook());

		//设置以合并的单元格
		CellRangeAddress range = null;
		int mergedNum = subSheet.getNumMergedRegions();
		for (i = 0; i < mergedNum; i++) {
			range = subSheet.getMergedRegion(i);
			range.setFirstRow(range.getFirstRow() + endRowNum);
			range.setLastRow(range.getLastRow() + endRowNum);
			mainSheet.addMergedRegion(range);
		}
		range = null;
		//int k = 0;

		//设置相关参数
		mainSheet.setAlternativeExpression(subSheet.getAlternateExpression());
		mainSheet.setAlternativeFormula(subSheet.getAlternateFormula());
		mainSheet.setAutobreaks(subSheet.getAutobreaks());
		mainSheet.setDialog(subSheet.getDialog());
		mainSheet.setDisplayGuts(subSheet.getDisplayGuts());
		mainSheet.setFitToPage(subSheet.getFitToPage());

		for (java.util.Iterator<Row> iterow = subSheet.rowIterator(); iterow.hasNext();) {
			sourow = (HSSFRow) iterow.next();
			offsetcnt = sourow.getRowNum() + endRowNum;
			descrow = mainSheet.createRow(offsetcnt);
			descrow.setHeight(sourow.getHeight());
			descrow.setHeightInPoints(sourow.getHeightInPoints());

			java.util.Iterator<Cell> iter = sourow.cellIterator();
			while(iter.hasNext()) {
				sourcell = (HSSFCell)iter.next();
				int column = sourcell.getColumnIndex();
				descell = descrow.createCell(column);
				
				//取模板中的单元格，与来源表单位置相同
				int row = sourcell.getRowIndex();
				orgcell = mainSheet.getRow(row).getCell(column);
				if (orgcell != null) {
					//取模板中的类型赋值
					descell.setCellType(orgcell.getCellType());
					//取模板中的样式赋值
					descell.setCellStyle(orgcell.getCellStyle());
				} else {
					_log.showWarn("module xls [{0}, {1}] cell is null!", row, column);
				}
				
				if(sourcell.getCellType() == HSSFCell.CELL_TYPE_STRING)
					descell.setCellValue( sourcell.getStringCellValue() );
				else if(sourcell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
					descell.setCellValue( sourcell.getNumericCellValue() );
				else if(sourcell.getCellType() == HSSFCell.CELL_TYPE_BLANK);
			}
			sourow = null;
			sourcell = null;
			descrow = null;
			orgcell = null;
		}

		return mainSheet;
	}
	
	/**
	 * 复制来源表格中第1个SHEET中的图片到目标表格中的第1个SHEET中。
	 * 
	 * @param destBook -- 目标表格
	 * @param srcBook -- 来源表格
	 */
	private static void copySheetImage(HSSFWorkbook destBook,  HSSFWorkbook srcBook) {
		//来源表单
		HSSFSheet srcSheet = srcBook.getSheetAt(0);
		//目标表单
		HSSFSheet destSheet = destBook.getSheetAt(0);
		
		//需要偏移的行数
		int endRowNum = destSheet.getPhysicalNumberOfRows();

		//取来源表单中的图片对象
		List<HSSFPicture> lsSrcPicture = getAllPicture(srcSheet);
		_log.showDebug("----------source picture size:" + lsSrcPicture.size());
		
		//取所有子图形数据，如果是主从报表且明细数据占多页时，则会报空指针错误
		List<HSSFPictureData> lsPicData = null;
		try {
			lsPicData = srcBook.getAllPictures();
		} catch(Exception e) {
			//e.printStackTrace();
			_log.showWarn("由于表单明细有多页，造成临时表的图片数据取不到，只能采用原表第1个图替代！");
			
			//原表中也没有图片，则不处理图片复制了
			lsPicData = destBook.getAllPictures();
			if (lsPicData == null || lsPicData.isEmpty()) return;
			
			//只取原表中第1个图片信息
			List<HSSFPictureData> destData = FactoryUtil.newList();
			for (int i = 0, n = lsSrcPicture.size(); i < n; i++) {
				destData.add(lsPicData.get(0));
			}
			lsPicData = destData;
		}
		if (lsPicData == null || lsPicData.isEmpty()) return;
		_log.showDebug("----------source data size:" + lsPicData.size());
		
		//data数量可能大于图片数量
		if (lsSrcPicture.size() > lsPicData.size()) {
			_log.showWarn("图片数量与数据数量不符！");
			return;
		}
		
		//取图片管理器
		HSSFPatriarch destDraw = destSheet.getDrawingPatriarch();
		if (destDraw == null) {
			destDraw = destSheet.createDrawingPatriarch();
		}
		
		//取原目标表单中的图片对象
		List<HSSFPicture> lsDestPicture = getAllPicture(destSheet);
		int index = lsDestPicture.size();
		
		for(int i = 0, n = lsSrcPicture.size(); i < n; i++) {
			//取图片对象
			HSSFPicture picture = lsSrcPicture.get(i);
			//根据图片序号取图片数据
			HSSFPictureData picdata = lsPicData.get(i);
			//取图片字节信息
			byte[] datas = picdata.getData();

			//取图片位置信息
			HSSFClientAnchor anchor = (HSSFClientAnchor) picture.getAnchor();
			
			//添加行偏移值
			anchor.setRow1(anchor.getRow1() + endRowNum);
			anchor.setRow2(anchor.getRow2() + endRowNum);
			
			//插入新图片，返回的新图片序号无效
			destBook.addPicture(datas, picdata.getFormat());
			//上面代码中新建图片的序号没有考虑原有图片数量，所以取原图片数量+1作为新图片的序号
			index++;
			_log.showDebug("---------copy new image index="+index);			
			
			destDraw.createPicture(anchor, index);
		}
	}
	
	/**
	 * 输出图片到指定的单元格。
	 * @param funId -- 功能ID
	 * @param field -- 图片字段
	 * @param mpData -- 当前数据记录，取记录主键值
	 * @param cell -- 输出图片的指定单元格
	 * 
	 */
	private static boolean printCellImage(String funId, String fieldName, 
							Map<String,String> mpData, HSSFCell cell) {
		//取功能基础信息
		Map<String,String> mpFun = FunDefineDao.queryFun(funId);
		if (mpFun == null || mpFun.isEmpty()) {
			_log.showDebug("--------printCellImage(): not find function is null!!");
			return false;
		}
		//取功能表名
		String tableName = mpFun.get("table_name");
		//取主键，不带表名
		String pkcol = mpFun.get("pk_col");
		pkcol = StringUtil.getNoTableCol(pkcol);
		//取主键值
		String dataId = mpData.get(pkcol);
		if (dataId == null || dataId.length() == 0) {
			_log.showDebug("--------printCellImage(): not find data id!!");
			return false;
		}
		
		AttachBO attachBO = new AttachBO();
		//取图文附件记录
		Map<String, String> mpAttach = null;
		try {
			mpAttach = attachBO.queryAttach(dataId, tableName, fieldName);
		} catch (BoException e1) {
			e1.printStackTrace();
		}
		if (mpAttach == null || mpAttach.isEmpty()) {
			_log.showDebug("--------printCellImage(): not attach dataId=" + dataId + "; tableName=" + tableName + ";fieldName=" + fieldName);
			return false;
		}
		
		//取图文附件字节信息
		byte[] bytes = null;
		try {
			bytes = attachBO.queryAttachContent(mpAttach);
		} catch (BoException e1) {
			e1.printStackTrace();
		}
		if (bytes == null || bytes.length == 0) {
			_log.showDebug("--------printCellImage(): not find attach image file!!");
			return false;
		}
		
		//输出图片到指定位置
		addImageToSheet(cell, bytes);
		
		return true;
	}
	
	/**
	 * 输出图片到指定的单元格，参考POI例子中的ReportImageUtil类。
	 * @param cell -- 单元格
	 * @param bytes -- 图片内容
	 */
	public static void addImageToSheet(HSSFCell cell, byte[] bytes) {
		if (cell == null) {
			_log.showError("-----insertImageToSheet: cell is null!");
			return;
		}
		if (bytes == null || bytes.length == 0) {
			_log.showError("-----insertImageToSheet: bytes is null!");
			return;
		}
		
		//取所在表单对象
		HSSFSheet sheet = cell.getSheet();
		
		//取图片输出行与列
		int firstRow = cell.getRowIndex();
		int lastRow = cell.getRowIndex();
		int firstCol = cell.getColumnIndex();
		int lastCol = cell.getColumnIndex();
		//取单元格所在的区域
		CellRangeAddress range = getMergedRegion(cell);
		if (range != null) {
			firstRow = range.getFirstRow();
			lastRow = range.getLastRow();
			firstCol = range.getFirstColumn();
			lastCol = range.getLastColumn();
		}
		_log.showDebug("---------image cells=["+firstRow+","+firstCol+","+lastRow+","+lastCol+"]");
		//图片输出要比单元格的高与宽偏5个值，保留单元的边框，宽度1023表示填充满，高度255表示填充满
		HSSFClientAnchor anchor = new HSSFClientAnchor(5, 5, 1023, 255,   
									(short)firstCol, firstRow, (short)lastCol, lastRow);
		anchor.setAnchorType(HSSFClientAnchor.MOVE_AND_RESIZE);   
		
		//取图片管理器，如果没有则创建
		HSSFPatriarch draw = sheet.getDrawingPatriarch();
		if (draw == null) {
			draw = sheet.createDrawingPatriarch();
		}
		
		//插入新图片，返回的新图片序号无效
        sheet.getWorkbook().addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
		//上面代码中新建图片的序号没有考虑原有图片数量，所以取原图片数量+1作为新图片的序号
		List<HSSFPicture> lsPicture = getAllPicture(sheet);
		int index = lsPicture.size() + 1;
		_log.showDebug("---------new image index="+index);
        
        draw.createPicture(anchor, index);
	}
	
	/**
	 * 取表单中的所有图片元素。
	 * @param sheet -- 表单对象
	 * @return
	 */
	private static List<HSSFPicture> getAllPicture(HSSFSheet sheet) {
		List<HSSFPicture> lsPicture = FactoryUtil.newList();
		
		HSSFPatriarch draw = sheet.getDrawingPatriarch();
		if (draw == null) return lsPicture;
		
		//取所有子图形位置
		List<HSSFShape> lsShape = draw.getChildren();
		if (lsShape == null || lsShape.isEmpty()) return lsPicture;
		
		for (int i = 0, n = lsShape.size(); i < n; i++) {
			HSSFShape shape = lsShape.get(i);
			if (shape instanceof HSSFPicture) {
				lsPicture.add((HSSFPicture) shape);
			}
		}
		return lsPicture;
	} 
	
	/**
	 * 取单元格所在的合并区域，如果返回空，则说明没有在合并区域
	 * @param cell -- 指定的单元格
	 * @return
	 */
	private static CellRangeAddress getMergedRegion(HSSFCell cell) {
		HSSFSheet sheet = cell.getSheet();
		
		CellRangeAddress range = null;
		int mergedNum = sheet.getNumMergedRegions();
		for (int i = 0; i < mergedNum; i++) {
			range = sheet.getMergedRegion(i);
			if (range.getFirstColumn() == cell.getColumnIndex() &&
					range.getFirstRow() == cell.getRowIndex()) {
				return range;
			}
		}
		return null;
	}
	
	/**
	 * 读取xls文件，构建HSSFWorkbook对象。
	 * @param fileName
	 * @return
	 */
	public static HSSFWorkbook readWorkBook(String fileName) {
		POIFSFileSystem fs = null;
		FileInputStream fis = null;
		HSSFWorkbook wb = null;
		if (fileName == null || fileName.length() == 0) return wb;

		try {
			fis = new FileInputStream(fileName);
			fs = new POIFSFileSystem(fis);
			fis.close();

			wb = new HSSFWorkbook(fs);
		} catch (FileNotFoundException e) {
			_log.showWarn("not find excel file: " + fileName);
			fs = null;
			return wb;
		} catch (IOException e) {
			_log.showWarn(e.getMessage());
			fs = null;
			return wb;
		}
		fis = null;

		return wb;
	}
	
	/**
	 * 判断报表是否可以输出
	 * 
	 * @param sheet -- 报表对象
	 * @return
	 */
	public static boolean isAllowOut(HSSFSheet sheet) {
		boolean ret = true;
		
		if (sheet.getPhysicalNumberOfRows() > 50000) {
			_log.showWarn("报表输出行数超出了最大行数：50000！");
			ret = false;
		}

		return ret;
	}

	/**
	 * 计算报表输出页数
	 * 
	 * @param nums -- 总记录行数
	 * @param pageSize -- 每页行数
	 * @return
	 */
	public static int calPageNum(int nums, int pageSize) {
		int ret = 0;
		if (pageSize == 0) pageSize = 1;
		int mod = nums % pageSize;

		ret = nums / pageSize;
		if (mod != 0 || nums == 0) {
			ret++;
		}

		return ret;
	}
	
	/**
	 * 取统计字段
	 * @param lsField -- 所有字段信息
	 * @return
	 */
	private static List<Map<String,String>> getStatField(List<Map<String,String>> lsField) {
		List<Map<String,String>> lsRet = FactoryUtil.newList();
		if (lsField == null) return lsRet;

		Map<String,String> mpField = null;
		String isstat = null;
		for (int i = 0, n = lsField.size(); i < n; i++) {
			mpField = lsField.get(i);

			isstat = mpField.get("is_stat");
			if (isstat.trim().equals("1")) lsRet.add(mpField);
		}

		return lsRet;
	}
}
