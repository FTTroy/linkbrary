package com.github.FTTroy.linkbrary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.FTTroy.linkbrary.model.Link;
import com.github.FTTroy.linkbrary.service.LinkService;

@RestController
@RequestMapping("/link-controller")
@CrossOrigin
public class LinkController {

	// sprivate static final Logger logger =
	// LoggerFactory.getLogger(LinkController.class);

	@Autowired
	private LinkService service;

	@GetMapping(value = "/export-link")
//	@Produces("application/excel")
	public ResponseEntity<byte[]> exportLink() {
		return service.exportLinks();
	}

	@GetMapping("/find-all-links")
	public List<Link> findAllLinks() {
		return service.findAllLinks();
	}

	@GetMapping("/find-link-by-id")
	public Link findLinkById(@RequestParam String id) {
		return service.findLinkById(id);
	}

	@GetMapping("/find-link-by-name")
	public Link findLinkByName(@RequestParam String name) {
		return service.findLinkByName(name);
	}

	@GetMapping("/find-all-favourites")
	public List<Link> findAllFavourites() {
		return service.findAllFavourites();
	}

	@PostMapping("/save-link")
	public Link saveLink(@RequestBody Link link) {
		return service.saveLink(link);
	}
	
	@PostMapping(value="/import-links", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //prende in input un MultiPartFile
	public boolean importLinks (@RequestPart("file") MultipartFile file) {
		return service.importLinks(file);
	}

	@PutMapping("/update-link")
	public Link updateLink(@RequestBody Link link) {
		return service.updateLink(link);
	}

	@DeleteMapping
	public boolean deleteLink(@RequestParam String id) {
		return service.deleteLink(id);
	}

}// end class
