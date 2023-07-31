package com.github.FTTroy.linkbrary.utilities;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import com.github.FTTroy.linkbrary.model.Link;

public class Validator {

	public static boolean isHeader(String content) {
		return content.equals(Constants.NAME) || content.equals(Constants.CONTENT);
	}

	public static boolean checkName(String name) {
		return (name != null && !name.isEmpty() && !name.isBlank());
	}

	public static boolean checkContent(String content) {
		return (content != null && !content.isEmpty() && !content.isBlank());
	}

	public static boolean linkExist(Link link) {
		if ((checkName(link.getName()) && checkContent(link.getContent()))) {
			if (link.getContent().matches(Constants.COM_URL_REGEX) || link.getContent().matches(Constants.WWW_URL_REGEX)
					|| link.getContent().contains(Constants.PREFIX)) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkLinkValidity(String content) {
		try {
			if ((content.matches(Constants.WWW_URL_REGEX) || content.matches(Constants.COM_URL_REGEX))) {
				return true;
			} else if (content.length() > 8 && content.substring(0, 8).equals(Constants.PREFIX)) {
				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			Utility.fileLogger(Utility.fileCreator(Constants.LOG_FILE_PATH), e.getMessage());
			return false;
		}

	}

	public static boolean checkFileExtension(MultipartFile file) {
		return (file.getContentType().equals(Constants.XLSX_CONTENT_TYPE));

	}

	public static boolean checkFileFormat(Sheet sheet) {
		return (sheet.getRow(0).getCell(0).toString().equals(Constants.NAME)
				&& sheet.getRow(0).getCell(1).toString().equals(Constants.CONTENT));
	}
}// end class
