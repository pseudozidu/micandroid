/*
 * ExportXlsBO.java 2010-12-14
 * 
 * Copyright 2008-2011 the original author or authors.
 * Licensed under the Apache License, Version 2.0
 */
package org.jxstar.report.studio;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import org.jxstar.control.action.RequestContext;
import org.jxstar.dao.DaoParam;
import org.jxstar.service.BoException;
import org.jxstar.service.BusinessObject;
import org.jxstar.service.define.ColumnDefine;
import org.jxstar.service.define.FunDefineDao;
import org.jxstar.service.util.WhereUtil;
import org.jxstar.util.StringUtil;
import org.jxstar.util.factory.FactoryUtil;
import org.jxstar.util.resource.JsMessage;

/**
 * 导出数据表的文件到xls文件中。
 *
 * @author TonyTan
 * @version 1.0, 2010-12-14
 */
public class ExportXlsBO extends BusinessObject {
	private static final long serialVersionUID = 1L;

	public String exportXls(RequestContext request) {
		//取页面参数
		String funid = request.getRequestValue("query_funid");
		String where_sql = request.getRequestValue("where_sql");
		String where_value = request.getRequestValue("where_value");
		String where_type = request.getRequestValue("where_type");
		//String orderclause = request.getParameter("orderclause");
		String user_id = request.getRequestValue("user_id");
		String selfield = request.getRequestValue("selfield");
		//String zerotonull = request.getParameter("zerotonull");
		_log.showDebug("==========exp file param funid=" + funid + ";where_sql=" + where_sql+";where_value="+where_value+";where_type="+where_type);
		//取功能定义对象
		Map<String,String> mpDefine = FunDefineDao.queryFun(funid);
		
		//取功能定义where
		try {
			where_sql = WhereUtil.queryWhere(funid, user_id, where_sql, "0");
		} catch (BoException e) {
			_log.showError(e);
			setMessage(e.getMessage());
		}
		_log.showDebug("==========exp file where sql=" + where_sql);
		
		//构建查询SQL
		String sql = "select " + selfield + ' ' + mpDefine.get("from_sql");
		if (where_sql != null && where_sql.length() > 0) {
			sql += " where " + where_sql;
		}
		_log.showDebug("==========exp file query sql=" + sql);
		//取查询数据
		DaoParam param = _dao.createParam(sql);
		if (where_type != null && where_type.length() > 0) {
			param.setType(where_type);
			param.setValue(where_value);
		}
		List<Map<String,String>> lsData = _dao.query(param);
		_log.showDebug("===========exp file query data size=" + lsData.size());
		
		//取下拉控件值，控件代号为key
		Map<String,Map<String,String>> mpCombo = queryComboValue(funid); 
		
		//创建excel对象
		HSSFWorkbook wb = new HSSFWorkbook();
		//新建一个sheet
		HSSFSheet sheet = wb.createSheet();
		
		//设置sheet页名称
		String title = mpDefine.get("fun_name");
		wb.setSheetName(0, title);
		
		//把字段字符串转换为数组
		String[] fields = selfield.split(",");
		
		//建标题区域
		List<Map<String,String>> lsCol = queryCol(funid, fields);
		sheet = createTitleArea(title, lsCol, sheet);
		
		//创建表格内容样式，左对齐
		HSSFCellStyle cellStyle = createCellStyle(wb);
		
		for (int i = 0, n = lsData.size(); i < n; i++) {
			Map<String,String> mpData = lsData.get(i);
			
			if (i > 100000) {//"导出数据行数超出了最大行数：{0}！"
				_log.showWarn(JsMessage.getValue("exportxlsbo.maxnum", 100000));
				break;
			}
			
			HSSFRow hfRow = sheet.createRow(i+3);
			HSSFCell sfCell = hfRow.createCell(0);
			
			for (int j = 0, m = lsCol.size(); j < m; j++) {
				Map<String,String> mpcol = lsCol.get(j);
				String colcode = StringUtil.getNoTableCol(mpcol.get("col_code"));
				//String datatype = mpcol.get("data_type");
				String ctltype = mpcol.get("col_control");
				String ctlname = mpcol.get("control_name");
				
				String colvalue = mpData.get(colcode);
				if (colvalue == null) colvalue = "";
				
				if (ctltype.equals("combo") && colvalue.length() > 0 && ctlname.length() > 0) {
					Map<String,String> mpcombo = mpCombo.get(ctlname);
					if (mpcombo != null && !mpcombo.isEmpty()) {
						colvalue = mpcombo.get(colvalue);
					}
				}

				sfCell = hfRow.createCell(j+1);
				sfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				sfCell.setCellValue(colvalue);
				sfCell.setCellStyle(cellStyle);
			}
		}
		
		//设置响应头信息
		request.setRequestValue("ContentType", "application/vnd.ms-excel");
		request.setRequestValue("Attachment", title+".xls");
		//返回xls文件对象
		request.setReturnObject(wb);
		
		_log.showDebug("---------file output end!");
		
		return _returnSuccess;
	}
	
	/**
	 * 设置输出文件的头部
	 * @param title -- 表格标题
	 * @param lsCol -- 表头字段列表
	 * @param sheet -- 表格
	 * @return
	 */
	private HSSFSheet createTitleArea(String title, 
			List<Map<String,String>> lsCol, HSSFSheet sheet) {
		HSSFCell sfCell = null;
		int rsCnt = lsCol.size() + 1;

		//第一行，空行
		HSSFRow hfRow = sheet.createRow(0);
		hfRow.setHeightInPoints(10);
		for (int i = 0, n = rsCnt; i < n; i++) {
			sfCell = hfRow.createCell(i);
			sheet.setColumnWidth(i, 4000);
		}
		sfCell = hfRow.getCell(0);
		sheet.setColumnWidth(0, 448); //设置第一列的宽度

		//第二行，设置标题栏
		hfRow = sheet.createRow(1);
		hfRow.setHeightInPoints(25);
		for (int i = 0, n = rsCnt; i < n; i++) {
			sfCell = hfRow.createCell(i);
		}
		//合并单元格，取中间的6格合并
		int posi = rsCnt / 2;
		int fromCell = (((posi - 2) < 0) ? 0 : (posi - 2));
		int toCell = (((rsCnt - posi) < 0) ? 0 : (rsCnt - posi + 2));
		
		CellRangeAddress range = new CellRangeAddress(1, 1, fromCell, toCell);
		sheet.addMergedRegion(range);
		
		HSSFWorkbook wb = sheet.getWorkbook();
		
		//设置标题单元格样式和内容
		sfCell = hfRow.getCell(fromCell);
		sfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
		sfCell.setCellValue(title);
		sfCell.setCellStyle(createTitleStyle(wb));

		//第三行，设置列表头
		hfRow = sheet.createRow(2);
		for (int i = 0, n = rsCnt; i < n; i++) {
			sfCell = hfRow.createCell(i);

			if (i != 0) {
				String colname = lsCol.get(i - 1).get("col_name");

				sfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
				sfCell.setCellValue(colname);
				sfCell.setCellStyle(createHeadStyle(wb));
			}
		}
		
		return sheet;
	}
	
	/**
	 * 创建表格标题样式
	 * @param wb -- 表单对象
	 * @return
	 */
	private HSSFCellStyle createTitleStyle(HSSFWorkbook wb) {
		//创建标题字体
		HSSFFont cellFont = wb.createFont();
		cellFont.setFontName("楷体");
		cellFont.setFontHeightInPoints((short) 16);
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		//创建标题样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(cellFont);
		
		return cellStyle;
	}
	
	/**
	 * 创建表头标题样式
	 * @param wb -- 表单对象
	 * @return
	 */
	private HSSFCellStyle createHeadStyle(HSSFWorkbook wb) {
		//创建标题字体
		HSSFFont cellFont = wb.createFont();
		cellFont.setFontName("宋体");
		cellFont.setFontHeightInPoints((short) 9);
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		//创建标题样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(cellFont);
		
		//设置边框
		cellStyle.setBorderBottom((short)1);
		cellStyle.setBorderLeft((short)1);
		cellStyle.setBorderRight((short)1);
		cellStyle.setBorderTop((short)1);
		
		return cellStyle;
	}
	
	/**
	 * 创建表格内容样式
	 * @param wb -- 表单对象
	 * @return
	 */
	private HSSFCellStyle createCellStyle(HSSFWorkbook wb) {
		//创建标题字体
		HSSFFont cellFont = wb.createFont();
		cellFont.setFontName("宋体");
		cellFont.setFontHeightInPoints((short) 9);

		//创建标题样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(cellFont);
		
		//设置边框
		cellStyle.setBorderBottom((short)1);
		cellStyle.setBorderLeft((short)1);
		cellStyle.setBorderRight((short)1);
		cellStyle.setBorderTop((short)1);
		
		return cellStyle;
	}
	
	/**
	 * 取字段列名
	 * @param funid -- 功能ID
	 * @param fields --前台选择的字段
	 * @return
	 */
	private List<Map<String, String>> queryCol(String funid, String[] fields) {
		List<Map<String, String>> lsname = FactoryUtil.newList();
		
		ColumnDefine colDefine = FunDefineDao.queryColDefine(funid);
		for (int i = 0, n = fields.length; i < n; i++) {
			lsname.add(colDefine.getColumnData(fields[i]));
		}
		
		return lsname;
	}

	/**
	 * 查询下拉选项控件的值
	 * @param funid
	 * @return
	 */
	private Map<String,Map<String,String>> queryComboValue(String funid) {
		Map<String,Map<String,String>> mpCombo = FactoryUtil.newMap();
		
		String sqlcol = "select control_name from fun_col where col_control = 'combo' and fun_id = ?";
		String sqlctl = "select display_data, value_data from funall_control where control_type = 'combo' and control_code = ?";
		
		//当前功能的下拉控件
		DaoParam param = _dao.createParam(sqlcol);
		param.addStringValue(funid);
		List<Map<String,String>> lscol = _dao.query(param);
		if (lscol.isEmpty()) return mpCombo;
		
		for (int i = 0; i < lscol.size(); i++) {
			Map<String,String> mpcol = lscol.get(i);
			String code = mpcol.get("control_name");
			
			//取下拉控件的选项值
			param = _dao.createParam(sqlctl);
			param.addStringValue(code);
			List<Map<String,String>> lsval = _dao.query(param);
			
			//把一个控件的选项值存到一个map中
			Map<String,String> mpvals = FactoryUtil.newMap();
			for (int j = 0; j < lsval.size(); j++) {
				Map<String,String> mpval = lsval.get(j);
				String text = mpval.get("display_data");
				String value = mpval.get("value_data");
				
				mpvals.put(value, text);
			}
			
			mpCombo.put(code, mpvals);
		}
		
		return mpCombo;
	}
}
