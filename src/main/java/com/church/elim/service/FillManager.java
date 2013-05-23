package com.church.elim.service;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;

import com.church.elim.domain.Parishioner;


public class FillManager {

	/**
	 * Fills the report with content
	 * 
	 * @param worksheet
	 * @param startRowIndex starting row offset
	 * @param startColIndex starting column offset
	 * @param datasource the data source
	 */
	public static void fillReport(HSSFSheet worksheet, int startRowIndex, int startColIndex, List<Parishioner> datasource) {
		// Row offset
		startRowIndex += 2;
		CreationHelper createHelper = worksheet.getWorkbook().getCreationHelper();
		// Create cell style for the body
		HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();
		bodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		bodyCellStyle.setWrapText(true);
		
		HSSFCellStyle dataCellStyle = worksheet.getWorkbook().createCellStyle();
		dataCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		dataCellStyle.setDataFormat(
			        createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

		// Create body
		for (int i=startRowIndex; i+startRowIndex-2< datasource.size()+2; i++) {
			// Create a new row
			HSSFRow row = worksheet.createRow((short) i+1);

			// Retrieve the id value
			HSSFCell cell=null;
			String value0 = datasource.get(i-2).getAddress();
			if (value0 != null) {
			cell= row.createCell(startColIndex+0);
			cell.setCellValue(value0);cell.setCellStyle(bodyCellStyle);
			}


			Date value1 = datasource.get(i-2).getMarriageDate();
			if (value1 != null) {
			cell= row.createCell(startColIndex+1);
			cell.setCellValue(value1);cell.setCellStyle(dataCellStyle);
			}


			String value2 = datasource.get(i-2).getMaritalStatus();
			if (value2 != null) {
			cell= row.createCell(startColIndex+2);
			cell.setCellValue(value2);cell.setCellStyle(bodyCellStyle);
			}


			String value3 = datasource.get(i-2).getSpouseReligion();
			if (value3 != null) {
			cell= row.createCell(startColIndex+3);
			cell.setCellValue(value3);cell.setCellStyle(bodyCellStyle);
			}


			Long value4 = datasource.get(i-2).getId();
			if (value4 != null) {
			cell= row.createCell(startColIndex+4);
			cell.setCellValue(value4);cell.setCellStyle(bodyCellStyle);
			}


			String value5 = datasource.get(i-2).getFirstName();
			if (value5 != null) {
			cell= row.createCell(startColIndex+5);
			cell.setCellValue(value5);cell.setCellStyle(bodyCellStyle);
			}


			String value6 = datasource.get(i-2).getLastName();
			if (value6 != null) {
			cell= row.createCell(startColIndex+6);
			cell.setCellValue(value6);cell.setCellStyle(bodyCellStyle);
			}


			/*String value7 = datasource.get(i-2).getCnp();
			if (value7 != null) {
			cell= row.createCell(startColIndex+7);
			cell.setCellValue(value7);cell.setCellStyle(bodyCellStyle);
			}*/


			String value8 = datasource.get(i-2).getEmail();
			if (value8 != null) {
			cell= row.createCell(startColIndex+8);
			cell.setCellValue(value8);cell.setCellStyle(bodyCellStyle);
			}


			String value9 = datasource.get(i-2).getWorkplace();
			if (value9 != null) {
			cell= row.createCell(startColIndex+9);
			cell.setCellValue(value9);cell.setCellStyle(bodyCellStyle);
			}


			String value10 = datasource.get(i-2).getStudies();
			if (value10 != null) {
			cell= row.createCell(startColIndex+10);
			cell.setCellValue(value10);cell.setCellStyle(bodyCellStyle);
			}


			String value11 = datasource.get(i-2).getImage();
			if (value11 != null) {
			cell= row.createCell(startColIndex+11);
			cell.setCellValue(value11);cell.setCellStyle(bodyCellStyle);
			}


			String value12 = datasource.get(i-2).getCompany();
			if (value12 != null) {
			cell= row.createCell(startColIndex+12);
			cell.setCellValue(value12);cell.setCellStyle(bodyCellStyle);
			}


			String value13 = datasource.get(i-2).getJob();
			if (value13 != null) {
			cell= row.createCell(startColIndex+13);
			cell.setCellValue(value13);cell.setCellStyle(bodyCellStyle);
			}


			String value14 = datasource.get(i-2).getPhone();
			if (value14 != null) {
			cell= row.createCell(startColIndex+14);
			cell.setCellValue(value14);cell.setCellStyle(bodyCellStyle);
			}


			Long value15 = datasource.get(i-2).getId();
			if (value15 != null) {
			cell= row.createCell(startColIndex+15);
			cell.setCellValue(value15);cell.setCellStyle(bodyCellStyle);
			}


			Date value16 = datasource.get(i-2).getBaptismDate();
			if (value16 != null) {
			cell= row.createCell(startColIndex+16);
			cell.setCellValue(value16);cell.setCellStyle(dataCellStyle);
			}


			Date value17 = datasource.get(i-2).getHolySpiritBaptism();
			if (value17 != null) {
			cell= row.createCell(startColIndex+17);
			cell.setCellValue(value17);cell.setCellStyle(dataCellStyle);
			}
		}
	}

	private void setCellValue(HSSFCell cell, Object value){
	}
}