package com.github.FTTroy.linkbrary.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {
	
	private static final  Logger logger = LoggerFactory.getLogger(Utility.class);


	public static String adjustLink(String content) {

		if (!content.substring(0, 8).equals(Constants.PREFIX)) {
			content = Constants.PREFIX + content;
		}
		return content;
	}

	public static File fileCreator(String fileName) {
		File file = new File (fileName);
		try {
			if(file.createNewFile()) {
			  logger.info("created file with name: "+ file.getName());
			}else {
				logger.info("file with name: "+file.getName()+" already exist!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
	}
	
	public static void LoggingInFile(File file, String fileContent) {
		
	      try {
			FileWriter writer = new FileWriter(file.getAbsoluteFile(), true);
			writer.write(fileContent);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	// ------------------------------EXCEL UTILS-----------------------------------------------

	public static CellStyle boldStyle(CellStyle style) { // setto i bordi delle celle
		style.setBorderTop(BorderStyle.MEDIUM);
		style.setBorderBottom(BorderStyle.MEDIUM);
		style.setBorderLeft(BorderStyle.MEDIUM);
		style.setBorderRight(BorderStyle.MEDIUM);

		return style;
	}

	public static boolean createHeader(Sheet sheet) {
		int columnCount = 0;
		Row headerRow = sheet.createRow(0);

		Cell headerCell = headerRow.createCell(columnCount);
		headerCell.setCellValue("NAME");

		headerCell = headerRow.createCell(columnCount + 1);
		headerCell.setCellValue("CONTENT");

		return true;
	}

	public static Hyperlink createHyperLink(Workbook wb, String content) {

		@SuppressWarnings("resource")
		CreationHelper createHelper = wb.getCreationHelper();
		XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL); // creo l'hyperlink
		link.setAddress(content); // setto l'indirizzo di destinazione
		return link;

	}

	public static boolean isValidCell(Cell cell) {
		return (cell != null || cell.getStringCellValue().equals("") || cell.getStringCellValue().equals(" "));
	}

}
