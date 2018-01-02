package com.blog.samples.lambda.model;

import lombok.ToString;

@ToString
public class ImageData {

	private String bucket;
	private String name;
	private String contentType;
	private String s3Uri;
	private Long sizeBytes;
	private String lastModified;

	public String getBucket() {
		return bucket;
	}

	public ImageData setBucket(String bucket) {
		this.bucket = bucket;
		return this;
	}

	public String getName() {
		return name;
	}

	public ImageData setName(String name) {
		this.name = name;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public ImageData setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public String getS3Uri() {
		return s3Uri;
	}

	public ImageData setS3Uri(String s3Uri) {
		this.s3Uri = s3Uri;
		return this;
	}

	public Long getSizeBytes() {
		return sizeBytes;
	}

	public ImageData setSizeBytes(Long sizeBytes) {
		this.sizeBytes = sizeBytes;
		return this;
	}

	public String getLastModified() {
		return lastModified;
	}

	public ImageData setLastModified(String lastModified) {
		this.lastModified = lastModified;
		return this;
	}
}
