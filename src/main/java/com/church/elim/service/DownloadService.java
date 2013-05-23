package com.church.elim.service;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.church.elim.domain.Parishioner;
import com.church.elim.repository.ParishionerRepository;

/**
 * Service for processing Apache POI-based reports
 * 
 */
@Service("downloadService")
@Transactional
public class DownloadService {
	@Autowired
	public ParishionerRepository parishionerRepo;
	private static Logger logger = Logger.getLogger("service");
	/**
	 * Processes the download for Excel format.
	 * It does the following steps:
	 * <pre>1. Create new workbook
	 * 2. Create new worksheet
	 * 3. Define starting indices for rows and columns
	 * 4. Build layout 
	 * 5. Fill report
	 * 6. Set the HttpServletResponse properties
	 * 7. Write to the output stream
	 * </pre>
	 */
	public void downloadXLS(HttpServletResponse response, List dataSource) throws ClassNotFoundException {
		logger.debug("Downloading Excel report");

		// 1. Create new workbook
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 2. Create new worksheet
		HSSFSheet worksheet = workbook.createSheet("POI Worksheet");

		// 3. Define starting indices for rows and columns
		int startRowIndex = 0;
		int startColIndex = 0;

		// 4. Build layout 
		// Build title, date, and column headers
		Layouter.buildReport(worksheet, startRowIndex, startColIndex);

		// 5. Fill report
		FillManager.fillReport(worksheet, startRowIndex, startColIndex, dataSource);

		// 6. Set the response properties
		String fileName = "ParishionerReport.xls";
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		// Make sure to set the correct content type
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		//7. Write to the output stream
		Writer.write(response, worksheet);
	}

	/**
	 * Retrieves the datasource as as simple Java List.
	 */
	private List<Parishioner> getDatasource() {
		return parishionerRepo.findAll();
	}
}