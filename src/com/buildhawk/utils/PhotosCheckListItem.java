package com.buildhawk.utils;

public class PhotosCheckListItem {

	public String id;
	public String urlLarge;
	public String original;
	public String urlSmall;
	public String urlThumb;
	public String imageFileSize;
	public String imageContentType;
	public String source;
	public String phase;
//	public String createdAt;
	public String userName;
	public String name;
	public String description;
	public String createdDate;

	
	public PhotosCheckListItem(String id, String urlLarge, String original, String urlSmall, String urlThumb, String imageFileSize, String imageContentType
			, String source, String phase, 
//			String createdAt, 
			String userName, String name,String description, String createdDate){
		this.id = id;
		this.urlLarge = urlLarge;
		this.original = original;
		this.urlSmall = urlSmall;
		this.urlThumb = urlThumb;
		this.imageFileSize = imageFileSize;
		this.imageContentType = imageContentType;
		this.source = source;
		this.phase = phase;
//		this.createdAt = createdAt;
		this.userName = userName;
		this.name = name;
		this.description=description;
		this.createdDate = createdDate;
		
	}
	
}

