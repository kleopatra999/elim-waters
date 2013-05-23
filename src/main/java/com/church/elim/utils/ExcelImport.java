package com.church.elim.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**import org.json.simple.JSONArray;
import org.json.simple.JSONObject;**/

public class ExcelImport {
	@SuppressWarnings("deprecation")
	public void importExcel(String filePath) throws IOException{
		FileInputStream fileInputStream = new FileInputStream(filePath);

		HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
		HSSFSheet sheet = workbook.getSheetAt(0);

		int rows = sheet.getPhysicalNumberOfRows() + 5;
		System.out.println(workbook.getSheetName(0) + "\" has " + rows + " row(s).");
		for (int r = 0; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			// Skip Header Row
			if (r == 0) {
				continue;
			}


			int cells = row.getPhysicalNumberOfCells();
			System.out.println("\nROW " + row.getRowNum() + " has " + cells + " cell(s).");
			for (int c = 0; c < cells; c++) {
				HSSFCell cell = row.getCell(c);
				String value = null,printValue = null;

				System.out.println("Column=" + cell.getColumnIndex() + " CELL Type=" + cell.getCellType());

				switch (cell.getCellType()) {

				case HSSFCell.CELL_TYPE_FORMULA:
					printValue = "FORMULA value=" + cell.getCellFormula();
					value = cell.getCellFormula();
					break;

				case HSSFCell.CELL_TYPE_NUMERIC:
					printValue = "NUMERIC value=" + cell.getNumericCellValue();
					value = String.valueOf(cell.getNumericCellValue());
					break;

				case HSSFCell.CELL_TYPE_STRING:
					printValue = "STRING value=" + cell.getStringCellValue();
					value = cell.getStringCellValue();
					break;

				case HSSFCell.CELL_TYPE_BLANK:
					printValue = "STRING value=" + cell.getStringCellValue();
					value = cell.getStringCellValue();
					break;   
				default:
				}
			}
		}
	}
	public static String getCellValue(XSSFCell cell){
		String value=null;
		if(cell!=null){

			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_FORMULA:
				value = cell.getStringCellValue();//cell.getCellFormula();
				break;

			case XSSFCell.CELL_TYPE_NUMERIC:
				value = String.valueOf(cell.getNumericCellValue());
				break;

			case XSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;

			case XSSFCell.CELL_TYPE_BLANK:
				value = cell.getStringCellValue();
				break;   
			default:
			}
		}
		return value;
	}

	public static String readParishionerFromExcelFile(InputStream is) throws IOException{
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);

		// Start constructing JSON.
		JSONObject json = new JSONObject();

		// Iterate through the rows.
		JSONArray rows = new JSONArray();
		Iterator<Row> rowsIT = sheet.rowIterator();
		Row header = rowsIT.next();
		
		while (  rowsIT.hasNext())
		{
			Row row = rowsIT.next();
			JSONObject jRow = new JSONObject();

			// Iterate through the cells.
			JSONArray cells = new JSONArray();
			for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
			{
				Cell cell = cellsIT.next();
				cells.add( getCellValue(cell));
			}
			jRow.put( "cell", cells );
			rows.add( jRow );
		}

		// Create the JSON.
		json.put( "rows", rows );

		// Get the JSON text.
		return json.toString();
	}
	public static String getCellValue(Cell cell){
		String value=null;
		if(cell!=null){

			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_FORMULA:
				value = cell.getStringCellValue();//cell.getCellFormula();
				break;

			case Cell.CELL_TYPE_NUMERIC:
				value = String.valueOf(cell.getNumericCellValue());
				break;

			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;

			case Cell.CELL_TYPE_BLANK:
				value = cell.getStringCellValue();
				break;   
			default:
			}
		}
		return value;
	}
}