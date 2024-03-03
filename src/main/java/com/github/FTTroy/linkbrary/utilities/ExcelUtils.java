package com.github.FTTroy.linkbrary.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ExcelUtils {

    public static CellStyle boldStyle(CellStyle style) { // setto i bordi delle celle
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }

    public static void createHeader(Sheet sheet) {
        int columnCount = 0;
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(columnCount);
        headerCell.setCellValue(Constants.NAME);
        headerCell = headerRow.createCell(columnCount + 1);
        headerCell.setCellValue(Constants.CONTENT);
    }

    public static Hyperlink createHyperLink(Workbook wb, String content) {
        CreationHelper createHelper = wb.getCreationHelper();
        XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL); // creo l'hyperlink
        link.setAddress(content); // setto l'indirizzo di destinazione
        return link;
    }

    public static boolean isValidCell(Cell cell) {
        return (cell != null && !StringUtils.isEmpty(cell.getStringCellValue()));
    }

    public static Sheet createSheet (Workbook wb,String sheetName){
        Sheet sheet = wb.createSheet(sheetName);
        ExcelUtils.createHeader(sheet);
        return sheet;
    }

    public static HttpHeaders setHeaders(String filename, int contentLength){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // setto il content type
        headers.setContentLength(contentLength); // setto la lunghezza del contenuto del file in byte
        headers.setContentDispositionFormData("attachment", filename);
        return headers;
    }

    public static byte[] getExcelBytes(Workbook wb,ByteArrayOutputStream outputStream) throws IOException {
            wb.write(outputStream);
            wb.close();
            return outputStream.toByteArray();
    }
}
