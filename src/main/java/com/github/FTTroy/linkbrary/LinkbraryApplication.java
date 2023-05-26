package com.github.FTTroy.linkbrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class LinkbraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkbraryApplication.class, args);
	}

}
