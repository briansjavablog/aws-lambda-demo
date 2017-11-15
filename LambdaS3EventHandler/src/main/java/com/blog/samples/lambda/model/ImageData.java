package com.blog.samples.lambda.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ImageData {

	private String bucket;
	private String name;
	private String contentType; 
    private String s3Uri;
    private Long sizeBytes;
    private String lastModified;
	
    public ImageData(String bucket, String name, String contentType, String s3Uri, Long sizeBytes, String lastModified) {
		this.bucket = bucket;
    	this.name = name;
    	this.contentType = contentType;
		this.s3Uri = s3Uri;
		this.sizeBytes = sizeBytes;
		this.lastModified = lastModified;
	}
    
}
