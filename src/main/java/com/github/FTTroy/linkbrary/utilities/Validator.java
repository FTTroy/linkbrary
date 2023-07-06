package com.github.FTTroy.linkbrary.utilities;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import com.github.FTTroy.linkbrary.model.Link;

public class Validator {

//	private static final Logger logger = LoggerFactory.getLogger(Validator.class);

//	public static boolean isPossibleUrl(String content) {
//		return (content.matches(Constants.WWW_URL_REGEX) || content.matches(Constants.COM_URL_REGEX));
//	}

//	public static boolean isLink(String link) {
//		return link.substring(0, 8).equals(Constants.PREFIX);
//	}

	public static boolean isHeader(String content) {
		return content.equals(Constants.NAME) || content.equals(Constants.CONTENT);
	}

	public static boolean linkExist(Link link) {
		boolean isOk = false;
		if ((link.getName() != null && !link.getName().isEmpty() && !link.getName().isBlank())
				&& (link.getContent() != null && !link.getContent().isEmpty() && !link.getContent().isBlank())) {
			if (link.getContent().matches(Constants.COM_URL_REGEX)
					|| link.getContent().matches(Constants.WWW_URL_REGEX)) {
				isOk = true;
				return isOk;

			}

		}
		return isOk;
	}

	public static boolean checkLinkValidity(String content) {
		boolean isOk = false;
		if ((content.matches(Constants.WWW_URL_REGEX) || content.matches(Constants.COM_URL_REGEX))) {
			isOk = true;
			return isOk;
		} else if (content.substring(0, 8).equals(Constants.PREFIX)) {
			isOk = true;
			return isOk;
		} else {
			return isOk;
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
