package com.github.FTTroy.linkbrary.repository;

import java.util.List;
import java.util.Optional;

import com.github.FTTroy.linkbrary.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
	public Optional<Link> findLinkByName(String name);
	public List<Link> findLinkByIsFavouriteTrue();
}
