package com.github.FTTroy.linkbrary.utilities;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

import com.github.FTTroy.linkbrary.model.Link;

public class Validator {

//	private static final Logger logger = LoggerFactory.getLogger(Validator.class);

	public static boolean isPossibleUrl(String content) {
		return (content.matches(Constants.WWW_URL_REGEX) || content.matches(Constants.COM_URL_REGEX));
	}

	public static boolean isLink(String link) {
		return link.substring(0, 8).equals(Constants.PREFIX);
	}

	public static boolean isHeader(String content) {
		return content.equals(Constants.NAME) || content.equals(Constants.CONTENT);
	}

	public static boolean linkIsValid(Link link) {
		return (link.getName() != null && !link.getName().isEmpty() && !link.getName().isBlank())
				|| (link.getContent() != null && !link.getContent().isEmpty() && !link.getContent().isBlank());
	}

	public static boolean checkFileExtension(MultipartFile file) {
		return (file.getContentType().equals(Constants.XLSX_CONTENT_TYPE));

	}

	public static boolean checkFileFormat(Iterator<Row> rowIterator) {
		return (rowIterator.next().getCell(0).toString().equals(Constants.CONTENT)
				|| rowIterator.next().getCell(0).toString().equals(Constants.NAME));
	}
}// end class
