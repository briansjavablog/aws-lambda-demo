package com.blog.samples.lambda.model;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ImageData {

	private String name;
	private String contentType; 
    private String s3Uri;
    private Long sizeBytes;
    private Date lastModified;
	
    public ImageData(String name, String contentType, String s3Uri, Long sizeBytes, Date lastModified) {
		this.name = name;
    	this.contentType = contentType;
		this.s3Uri = s3Uri;
		this.sizeBytes = sizeBytes;
		this.lastModified = lastModified;
	}
    
}
