package com.github.FTTroy.linkbrary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.FTTroy.linkbrary.model.Link;
import com.github.FTTroy.linkbrary.service.LinkService;

@RestController
@RequestMapping("/link-controller")
public class LinkController {

	//sprivate static final Logger logger = LoggerFactory.getLogger(LinkController.class);

	@Autowired
	private LinkService service;

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

	@PostMapping("/save-link")
	public Link saveLink(@RequestBody Link link) {
		//Link savedLink = service.saveLink(link);
		//logger.info("Information Saved:\n " + savedLink.toString());
		return service.saveLink(link);
	}

	@PutMapping("/update-link")
	public Link updateLink(@RequestBody Link link) {
		return service.saveLink(link);
	}

	@DeleteMapping
	public boolean deleteLink(@RequestParam String id) {
		return service.deleteLink(id);
	}

}// end class
