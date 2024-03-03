package com.github.FTTroy.linkbrary.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.FTTroy.linkbrary.utilities.ExcelUtils;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import com.github.FTTroy.linkbrary.model.Link;
import com.github.FTTroy.linkbrary.repository.LinkRepository;
import com.github.FTTroy.linkbrary.utilities.Utility;
import com.github.FTTroy.linkbrary.utilities.Validator;

@Service
public class LinkService {

    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

    @Autowired
    public LinkRepository repository;


    public Link saveLink(Link link) {
        link.setName(link.getName().toUpperCase());
        link.setContent(Utility.setHttpPrefix(link.getContent()));
        return repository.save(link);
    }

    public Link updateLink(Link link) {
        Link linkDb = repository.findById(link.getId()).orElseThrow(RuntimeException::new);
        BeanUtils.copyProperties(link, linkDb);
        return repository.save(linkDb);
    }


    public Link findLinkByName(String name) {
        return repository.findLinkByName(name).isPresent() ? repository.findLinkByName(name).get() : null;
    }

    public Link findLinkById(Long id) {
        return repository.findById(id).isPresent() ? repository.findById(id).get() : null;
    }

    public List<Link> findAllLinks() {
        return repository.findAll();
    }

    public List<Link> findAllFavourites() {
        return repository.findLinkByIsFavouriteTrue();
    }

    public void deleteLink(Long id) {
        repository.findById(id).ifPresent(x -> repository.deleteById(x.getId()));
    }

    public ResponseEntity<?> importLinks(MultipartFile file) throws IOException {
        ArrayList<Link> invalidLinks = new ArrayList<>();
        try {
            if (file.isEmpty()) {
                logger.error("file is empty");
                throw new EmptyFileException();
            }
            if (!Validator.checkFileExtension(file.getContentType())) {
                logger.error("file must be an .xlsx");
                throw new UnsupportedMediaTypeStatusException("file must be an .xlsx");
            }
        } catch (UnsupportedMediaTypeStatusException e) {
            Utility.fileLogger(e.getMessage());
        } catch (EmptyFileException e) {
            Utility.fileLogger(e.getMessage());
        }

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream()); // crea il workbook sulla base del file
            Sheet sheet = workbook.getSheetAt(0); // prende la prima sheet

            Iterator<Row> rowIterator = sheet.iterator(); // creo iteratore per le righe

            if (!Validator.checkFileTemplate(sheet)) {
                logger.error("INVALID FILE FORMAT");
                throw new Exception("invalid file format");
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
                    if (Validator.isValidLinkToImport(cell)) {
                        link.setContent(cell.getStringCellValue());
                    } else {
                        link.setName(cell.getStringCellValue());
                    }
                }
                if (Validator.checkStringValidity(link.getName(), link.getContent())
                        && Validator.checkLinkValidity(link.getContent())) {
                    saveLink(link);
                } else {
                    invalidLinks.add(link);
                }
            }
        } catch (Exception e) {
            Utility.fileLogger(e.getMessage());
        }
        return invalidLinks.isEmpty() ? ResponseEntity.ok().build() : ResponseEntity.ok(invalidLinks);
    }

    public ResponseEntity<byte[]> exportLinks() throws IOException {
        byte[] excelBytes = new byte[0];
        int rowCount = 1;
        int columnCount = 0;
        List<Link> linkList = repository.findAll();
        Map<String, String> linkMap = new HashMap<>();

        // creazione del file
        Workbook wb = new XSSFWorkbook();
        // creazione del foglio di lavoro
        Sheet sheet = ExcelUtils.createSheet(wb, "links");
        // creo lo stile delle celle
        CellStyle style = wb.createCellStyle();
        // creo l'header

        // itero la lista mettendo i valori nella mappa
        linkList.forEach(link -> linkMap.put(link.getName(), link.getContent()));

        // itero la mappa facendo stampare chiave e valore nell'excel
        for (Entry<String, String> entry : linkMap.entrySet()) {

            Row row = sheet.createRow(rowCount++); // creo la riga incrementando l'indice
            Cell cell = row.createCell(columnCount);// creo la cella incrementando l'indice
            cell.setCellStyle(ExcelUtils.boldStyle(style));
            cell.setCellValue(entry.getKey());

            cell = row.createCell(columnCount + 1);

            cell.setCellValue(entry.getValue());
            cell.setHyperlink(ExcelUtils.createHyperLink(wb, entry.getValue())); // set il valore della cella come un HyperLink
            cell.setCellStyle(ExcelUtils.boldStyle(style));// setto lo stile
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1); // le celle si adattano alla lunghezza del testo

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            excelBytes = ExcelUtils.getExcelBytes(wb, outputStream);
        } catch (IOException e) {
            Utility.fileLogger(e.getMessage());
        }

        HttpHeaders headers = ExcelUtils.setHeaders("linkbrary.xlsx", excelBytes.length);
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

    }

    public ResponseEntity<byte[]> getTemplate() throws IOException {
        byte[] excelBytes = new byte[0];
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = ExcelUtils.createSheet(wb, "links");
        CellStyle style = wb.createCellStyle();
        ExcelUtils.createHeader(sheet);
        sheet.getRow(0).getCell(0).setCellStyle(ExcelUtils.boldStyle(style));
        sheet.getRow(0).getCell(1).setCellStyle(ExcelUtils.boldStyle(style));
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1); // le celle si adattano alla lunghezza del testo

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            excelBytes = ExcelUtils.getExcelBytes(wb, outputStream);
        } catch (IOException e) {
            Utility.fileLogger(e.getMessage());
        }
        HttpHeaders headers = ExcelUtils.setHeaders("linkbrary_template.xlsx", excelBytes.length);
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}

