package com.buildhawk.utils;

public class ProjectPhotos {
	
	public String id;
	public String urlLarge;
	public String original;
	public String url200;
	public String url100;
	public String imageFileSize;
	public String imageContentType;
	public String source;
	public String phase;
	public String createdAt;
	public String userName;
	public String name;
	public String description;
	public String createdDate;
	
	public ProjectPhotos(String id, String urlLarge, String original, String url200, String url100, String imageSize, String imageType
			, String source, String phase, String createdAt, String uName, String name, String description, String createdDate){
		this.id = id;
		this.urlLarge = urlLarge;
		this.original = original;
		this.url200 = url200;
		this.url100 = url100;
		this.imageFileSize = imageSize;
		this.imageContentType = imageType;
		this.source = source;
		this.phase = phase;
		this.createdAt = createdAt;
		this.userName = uName;
		this.name = name;
		this.description=description;
		this.createdDate = createdDate;
	}

}
