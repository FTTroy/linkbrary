package com.github.FTTroy.linkbrary.utilities;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Utility {
	public static final String prefix = "https://";

	public static String adjustLink(String content) {

		if (!content.substring(0, 8).equals(prefix)) {
			content = prefix + content;
		}
		return content;
	}
	
	//EXCEL UTILS
	
	public static CellStyle boldStyle(Workbook wb, CellStyle style) { //setto i bordi delle celle
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
	
	public static org.apache.poi.ss.usermodel.Hyperlink createHyperLink(String content) {
		
		 @SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook();
	     CreationHelper createHelper = wb.getCreationHelper();
		 XSSFHyperlink link = (XSSFHyperlink)createHelper.createHyperlink(HyperlinkType.URL); //creo l'hyperlink
	     link.setAddress(content); //setto l'indirizzo di destinazione
	     return link;
	     
	}
}

