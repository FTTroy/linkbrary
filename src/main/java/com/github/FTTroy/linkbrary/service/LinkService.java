package com.github.FTTroy.linkbrary.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.github.FTTroy.linkbrary.model.Link;
import com.github.FTTroy.linkbrary.repository.LinkRepository;

@Service
public class LinkService {

	private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

	@Autowired
	public LinkRepository repository;

	public Link saveLink(Link link) {
		Optional<Link> linkOpt = repository.findLinkByName(link.getName());
		if (linkOpt.isPresent()) {
			logger.info("There is already a link saved with this name: " + link.getName());
			return null;
		}
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
			if (linkOpt.get().getId() != null)
				logger.info("error updating link with id: " + link.getId());
			else
				logger.info("link with this id:" + link.getId() + " doesn't exist");
			return null;
		}
	}

	public Link findLinkByName(String name) {
		logger.info("Finding link with name: " + name);
		try {
			Optional<Link> linkOpt = repository.findLinkByName(name);

			if (linkOpt.isPresent()) {
				Link linkDb = linkOpt.get();
				logger.info("Link found: \n" + linkDb.toString());
				return linkDb;
			} else {
				logger.info("Cannot find link with name: " + name);
				return null;
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			logger.info("There is more than 1 link with this name: " + name);
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

}// end class
