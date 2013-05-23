package com.church.elim.utils;

import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class Excel {
	public static HSSFCell createCell(HSSFRow row, String value){
		HSSFCell cell = row.createCell(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		return cell;
	}
	
	public static HSSFCell createCell(HSSFRow row, Date value){
		HSSFCell cell = row.createCell(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellValue(value);
		return cell;
	}
	
	public static HSSFCell createCell(HSSFRow row, Integer value){
		HSSFCell cell = row.createCell(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		return cell;
	}


}
