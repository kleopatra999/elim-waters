package com.church.elim.web.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;
 
public class ExcelReport extends AbstractExcelView{
 
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
 
		Map<String,String> revenueData = (Map<String,String>) model.get("revenueData");
		//save a wordsheet
		HSSFSheet sheet = workbook.createSheet("Parishioners");
 
		HSSFRow header = sheet.createRow(0);
		header.createCell(0).setCellValue("Month");
		header.createCell(1).setCellValue("Revenue");
 
		int rowNum = 1;
		for (Map.Entry<String, String> entry : revenueData.entrySet()) {
			//save the row data
			HSSFRow row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(entry.getKey());
			row.createCell(1).setCellValue(entry.getValue());
                }
	}
}