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
	
	@Query("{name: /?0/}")
	public List<Link> findLikeLinksByName(String name);
	
	@Query("{content: /?0/}")
	public List<Link> findLikeLinksByContent(String content);
	
	@Query("{$and: [{name:/?0/}, {content:/?1/}]}")
	public List<Link> findLikeLinksByNameAndContent(String name, String content);

}
