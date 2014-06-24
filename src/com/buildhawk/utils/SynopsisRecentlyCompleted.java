package com.buildhawk.utils;

public class SynopsisRecentlyCompleted {

	public String id; 
	public String body;
	public String status;
	public String itemType;
	public String photosCount;
	public String commentsCount;
	
	public SynopsisRecentlyCompleted(String id,String body, String status, String itemType, String photosCount, String commentsCount)
	{
		this.id = id;
		this.body = body;		
		this.status = status;
		this.itemType = itemType;
		this.photosCount = photosCount;
		this.commentsCount = commentsCount;
	}
}
