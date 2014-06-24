package com.buildhawk.utils;

public class SynopsisUpcomingItems {

	public String id; 
	public String body;
	public String criticalDate;
	public String completedDate;
	public String status;
	public String itemType;
	public String photosCount;
	public String commentsCount;
	
	public SynopsisUpcomingItems(String id,String body, String criticalDate,String completedDate, String status, String itemType, String photosCount, String commentsCount)
	{
		this.id = id;
		this.body = body;
		this.criticalDate = criticalDate;
		this.completedDate = completedDate;
		this.status = status;
		this.itemType = itemType;
		this.photosCount = photosCount;
		this.commentsCount = commentsCount;
	}
}
