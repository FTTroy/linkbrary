package com.github.FTTroy.linkbrary.utilities;

import java.time.LocalDate;

public class Constants {
	public static final String LOG_FILE_NAME = "\\linkbrary"+ LocalDate.now() +".log";
	public static final String PREFIX = "https://";
	public static final String WWW_URL_REGEX = "^www\\..*\\.com$";
	public static final String COM_URL_REGEX = ".*\\.com$";
	public static final String CONTENT = "CONTENT";
	public static final String NAME = "NAME";
	public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
}
