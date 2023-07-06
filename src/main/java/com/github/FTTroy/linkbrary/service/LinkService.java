package com.github.FTTroy.linkbrary.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import com.github.FTTroy.linkbrary.model.Link;
import com.github.FTTroy.linkbrary.repository.LinkRepository;
import com.github.FTTroy.linkbrary.utilities.Constants;
import com.github.FTTroy.linkbrary.utilities.Utility;
import com.github.FTTroy.linkbrary.utilities.Validator;

@Service
public class LinkService {

	private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

	@Autowired
	public LinkRepository repository;

	public Link saveLink(Link link) {
		link.setName(link.getName().toUpperCase());

		Optional<Link> linkOpt = repository.findLinkByName(link.getName());
		if (linkOpt.isPresent()) {
			logger.error("There is already a link saved with this name: " + link.getName());
			return null;
		}
		link.setContent(Utility.adjustLink(link.getContent()));
		logger.info("saving link: " + link.toString());
		return repository.save(link);

	}

	public Link updateLink(Link link) {

		logger.info("update User with id: " + link.getId());
		Optional<Link> linkOpt = repository.findById(link.getId());
		if (linkOpt.isPresent()) {
			Link linkDb = linkOpt.get();
			BeanUtils.copyProperties(link, linkDb);
			return repository.save(linkDb);
		} else {
			if (linkOpt.get().getId() != null) {
				logger.info("error updating link with id: " + link.getId());
				return null;
			} else {
				logger.info("link with this id:" + link.getId() + " doesn't exist");
				return null;
			}
		}

	}

	public Link findLinkByName(String name) {
		logger.info("Finding link with name: " + name);
		Optional<Link> linkOpt = repository.findLinkByName(name);

		if (linkOpt.isPresent()) {
			Link linkDb = linkOpt.get();
			logger.info("Link found: \n" + linkDb.toString());
			return linkDb;
		} else {
			logger.info("Cannot find link with name: " + name);
			return null;
		}

	}

	public Link findLinkById(String id) {
		logger.info("Finding link with id: " + id);
		Optional<Link> linkOpt = repository.findById(id);
		if (linkOpt.isPresent()) {
			Link linkDb = linkOpt.get();
			logger.info("Link found: \n" + linkDb.toString());
			return linkDb;
		} else {
			logger.info("Cannot find link with id: " + id);
			return null;
		}
	}

	public List<Link> findAllLinks() {
		return repository.findAll().stream().filter(link -> link != null).collect(Collectors.toList());
	}

	public List<Link> findAllFavourites() {
		return repository.findAllFavourites();

	}

	public boolean deleteLink(String id) {
		Optional<Link> linkOpt = repository.findById(id);
		boolean isDeleted = false;
		if (linkOpt.isPresent()) {

			try {
				Link linkDb = linkOpt.get();
				repository.delete(linkDb);
				logger.info("Deleting Link with id: " + id);
				isDeleted = true;
				return isDeleted;
			} catch (Exception e) {
				logger.info("Something goes wrong while trying to delete link");
				return isDeleted;
			}

		} else {
			logger.info("There is no link with the given id: " + id);
			return isDeleted;
		}

	}

	@SuppressWarnings("resource")
	public boolean importLinks(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				logger.error("The file must not be EMPTY");
				throw new EmptyFileException();
			}

			if (!Validator.checkFileExtension(file)) {
				logger.error("error in the file extension!");
				throw new UnsupportedMediaTypeStatusException("The file must be a .xlsx");
			}
		} catch (UnsupportedMediaTypeStatusException e) {
			Utility.fileLogger(Utility.fileCreator(Constants.LOG_FILE_PATH), e.getMessage());
			e.printStackTrace();
		} catch (EmptyFileException e) {
			Utility.fileLogger(Utility.fileCreator(Constants.LOG_FILE_PATH), e.getMessage());
			e.printStackTrace();
		}

		try {
			ArrayList<Link> linkKO = new ArrayList<Link>();
			Workbook workbook = WorkbookFactory.create(file.getInputStream()); // crea il workbook sulla base del file
			Sheet sheet = workbook.getSheetAt(0); // prende la prima sheet

			Iterator<Row> rowIterator = sheet.iterator(); // creo iteratore per le righe

			if (!Validator.checkFileFormat(sheet)) {
				logger.error("INVALID FILE FORMAT");

				throw new Exception("INVALID FILE FORMAT!");
			}

			if (rowIterator.hasNext()) {
				rowIterator.next();
			}
			while (rowIterator.hasNext()) {
				Link link = new Link();

				Row row = rowIterator.next(); // assegno alla row il prossimo valore dell'iteratore
				Iterator<Cell> cellIterator = row.cellIterator(); // creo iteratore delle celle

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next(); // assegno alla cella il prossimo valore dell'iteratore
					if (Utility.isValidCell(cell)) { // verifico se le celle non sono vuote, con spazio vuoto o null
						if (cell.getStringCellValue().length() > 8) {
							if (Validator.checkLinkValidity(cell.getStringCellValue())) {
								link.setContent(cell.getStringCellValue());
							} else {
								if(link.getName() != null) {
								link.setName(cell.getStringCellValue()); //TO DO snellire la logica di validazione
								}else {
									link.setContent(cell.getStringCellValue());
								}
							}

						} else {
							link.setName(cell.getStringCellValue());
						}

					}else {
						logger.info("The Cell must not be NULL, BLANK OR EMPTY");
					}
				} // end 2nd while
				link.setFavourite(false);
				logger.info("saving link with content: " + link.getContent());

				if (Validator.linkExist(link))
					saveLink(link);
				else
					linkKO.add(link);
					logger.error("not a valid link " + link.toString());

			} // end 1st while
			logger.info("Discarded links: \n");
			for (Link l : linkKO)
				logger.info(l.toString());
		} catch (Exception e) {
			Utility.fileLogger(Utility.fileCreator(Constants.LOG_FILE_PATH), e.getMessage());
			e.printStackTrace();

		}

		return true;
	}

	public ResponseEntity<byte[]> exportLinks() {
		int rowCount = 1;
		int columnCount = 0;
		List<Link> linkList = repository.findAll();
		Map<String, String> linkMap = new HashMap<>();

		// creazione del file
		Workbook wb = new XSSFWorkbook();
		// creazione del foglio di lavoro
		Sheet sheet = wb.createSheet("Links");

		// creo lo stile delle celle
		CellStyle style = wb.createCellStyle();

		// creo l'header
		Utility.createHeader(sheet);

		// itero la lista mettendo i valori nella mappa
		for (Link l : linkList) {
			linkMap.put(l.getName(), l.getContent());
		}

		// itero la mappa facendo stampare chiave e valore nell'excel
		for (Entry<String, String> entry : linkMap.entrySet()) {

			Row row = sheet.createRow(rowCount++); // creo la riga incrementando l'indice
			Cell cell = row.createCell(columnCount);// creo la cella incrementando l'indice
			cell.setCellStyle(Utility.boldStyle(style));
			cell.setCellValue(entry.getKey()); // stampo le chiavi

			cell = row.createCell(columnCount + 1);

			cell.setCellValue(entry.getValue());
			cell.setHyperlink(Utility.createHyperLink(wb, entry.getValue())); // set il valore della cella come un
																				// HyperLink
			cell.setCellStyle(Utility.boldStyle(style));// setto lo stile
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1); // le celle si adattano alla lunghezza del testo

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			wb.write(outputStream);
			wb.close();
		} catch (IOException e) {
			Utility.fileLogger(Utility.fileCreator(Constants.LOG_FILE_PATH), e.getMessage());
			e.printStackTrace();
		} // end catch

		byte[] excelBytes = outputStream.toByteArray();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // setto il content type
		headers.setContentLength(excelBytes.length); // setto la lunghezza del contenuto del file in byte
		headers.setContentDispositionFormData("attachment", "linkbrary.xlsx");

		return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
	}

}// end class
