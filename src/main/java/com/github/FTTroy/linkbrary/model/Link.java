package com.github.FTTroy.linkbrary.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Link {
	
	@Id
	private String id;
	
	private String name;
	
	private String content;
	
	
	private String description;

	@Override
	public String toString() {
		return "id = " + id + "\n "
				+"name = " + name + "\n "
				+"content = " + content + "\n "
				+"description = " + description;
	}
	

}