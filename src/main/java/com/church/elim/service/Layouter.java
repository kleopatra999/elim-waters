package com.church.elim.service;

import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
 
/**
 * Builds the report layout, the template, the design, the pattern or whatever synonym you may want to call it.
 * 
 * @author Krams at {@link http://krams915@blogspot.com}
 */
@SuppressWarnings("deprecation")
public class Layouter {
 
 private static Logger logger = Logger.getLogger("service");
  
 /**
  * Builds the report layout. 
  * <p>
  * This doesn't have any data yet. This is your template.
  */
 public static void buildReport(HSSFSheet worksheet, int startRowIndex, int startColIndex) {
  // Set column widths
  worksheet.setColumnWidth(0, 5000);
  worksheet.setColumnWidth(1, 5000);
  worksheet.setColumnWidth(2, 5000);
  worksheet.setColumnWidth(3, 5000);
  worksheet.setColumnWidth(4, 5000);
  worksheet.setColumnWidth(5, 5000);
   
  // Build the title and date headers
  buildTitle(worksheet, startRowIndex, startColIndex);
  // Build the column headers
  buildHeaders(worksheet, startRowIndex, startColIndex);
 }
  
 /**
  * Builds the report title and the date header
  * 
  * @param worksheet
  * @param startRowIndex starting row offset
  * @param startColIndex starting column offset
  */
 public static void buildTitle(HSSFSheet worksheet, int startRowIndex, int startColIndex) {
  // Create font style for the report title
  Font fontTitle = worksheet.getWorkbook().createFont();
  fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
  fontTitle.setFontHeight((short) 280);
   
        // Create cell style for the report title
        HSSFCellStyle cellStyleTitle = worksheet.getWorkbook().createCellStyle();
        cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyleTitle.setWrapText(true);
        cellStyleTitle.setFont(fontTitle);
   
        // Create report title
  HSSFRow rowTitle = worksheet.createRow((short) startRowIndex);
  rowTitle.setHeight((short) 500);
  HSSFCell cellTitle = rowTitle.createCell(startColIndex);
  cellTitle.setCellValue("Parishioners Report");
  cellTitle.setCellStyle(cellStyleTitle);
   
  // Create merged region for the report title
  worksheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
   
  // Create date header
  HSSFRow dateTitle = worksheet.createRow((short) startRowIndex +1);
  HSSFCell cellDate = dateTitle.createCell(startColIndex);
  cellDate.setCellValue("This report was generated at " + new Date());
 }
  
 /**
  * Builds the column headers
  * 
  * @param worksheet
  * @param startRowIndex starting row offset
  * @param startColIndex starting column offset
  */
 public static void buildHeaders(HSSFSheet worksheet, int startRowIndex, int startColIndex) {
  // Create font style for the headers
  Font font = worksheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
 
        // Create cell style for the headers
  HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
  headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
  headerCellStyle.setFillPattern(CellStyle.FINE_DOTS);
  headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
  headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
  headerCellStyle.setWrapText(true);
  headerCellStyle.setFont(font);
  headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
   
  // Create the column headers
  HSSFRow row = worksheet.createRow((short) startRowIndex +2);
  row.setHeight((short) 500);
   
  
  HSSFCell cell= row.createCell(startColIndex+0);
  cell.setCellValue("Address");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+1);
  cell.setCellValue("MarriageDate");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+2);
  cell.setCellValue("MaritalStatus");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+3);
  cell.setCellValue("SpouseReligion");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+4);
  cell.setCellValue("id");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+5);
  cell.setCellValue("FirstName");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+6);
  cell.setCellValue("LastName");
  cell.setCellStyle(headerCellStyle);

  /*cell= row.createCell(startColIndex+7);
  cell.setCellValue("Cnp");
  cell.setCellStyle(headerCellStyle);*/

  cell= row.createCell(startColIndex+8);
  cell.setCellValue("Email");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+9);
  cell.setCellValue("Workplace");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+10);
  cell.setCellValue("Studies");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+11);
  cell.setCellValue("Image");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+12);
  cell.setCellValue("Company");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+13);
  cell.setCellValue("Job");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+14);
  cell.setCellValue("Phone");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+15);
  cell.setCellValue("Id");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+16);
  cell.setCellValue("BaptismDate");
  cell.setCellStyle(headerCellStyle);

  cell= row.createCell(startColIndex+17);
  cell.setCellValue("HolySpiritBaptism");
  cell.setCellStyle(headerCellStyle);
 }
}