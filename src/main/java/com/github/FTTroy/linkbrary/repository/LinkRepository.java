package com.github.FTTroy.linkbrary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.github.FTTroy.linkbrary.model.Link;


public interface LinkRepository extends MongoRepository<Link, String>{
	
	@Query("{name : ?0}")
	public Optional<Link> findLinkByName(String name);
	
	@Query("{isFavourite : true}")
	public List<Link> findAllFavourites();

}
