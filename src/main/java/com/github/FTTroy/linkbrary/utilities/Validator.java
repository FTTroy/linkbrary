package com.github.FTTroy.linkbrary.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Arrays;

public class Validator {

    /**
     * verify if the given string is a valid link that match the regular expressions
     *
     * @author Francesco Troiano
     */
    public static boolean checkLinkValidity(String content) {
        return content != null && (content.matches(Constants.WWW_URL_REGEX)
                || content.matches(Constants.COM_URL_REGEX)
                || content.startsWith(Constants.PREFIX));
    }

    /**
     * verify if the file extension is valid.
     *
     * @author Francesco Troiano
     */
    public static boolean checkFileExtension(String contentType) {
        return Constants.XLSX_CONTENT_TYPE.equals(contentType);
    }

    /**
     * verify if the file template is valid.
     *
     * @author Francesco Troiano
     */
    public static boolean checkFileTemplate(Sheet sheet) {
        return (sheet.getRow(0).getCell(0).toString().equals(Constants.NAME)
                && sheet.getRow(0).getCell(1).toString().equals(Constants.CONTENT));
    }


    public static boolean isValidLinkToImport(Cell cell) {
        return (ExcelUtils.isValidCell(cell))
                && (cell.getStringCellValue().length() > 8)
                && (Validator.checkLinkValidity(cell.getStringCellValue()));
    }

    /**
     * verify if the given varargs are null, blank or empty.
     *
     * @author Francesco Troiano
     */
    public static boolean checkStringValidity(String... args) {
        return Arrays.stream(args).allMatch(StringUtils::isNotBlank);
    }
}
