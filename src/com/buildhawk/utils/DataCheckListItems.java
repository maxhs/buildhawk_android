package com.buildhawk.utils;

import java.util.ArrayList;

public class DataCheckListItems {
	
	public String id;
	public String body;
	public String status;
	public String itemType;
	public String photosCount;
	public String commentsCount;
	public ArrayList<PhotosCheckListItem>photo;
	public ArrayList<CommentsChecklistItem>comments;
	public String category_name;
	public String project_id;
	
	public DataCheckListItems(String id, String body, String status, String itemType, String photosCount, 
			String commentsCount,ArrayList<PhotosCheckListItem>photo, ArrayList<CommentsChecklistItem>comments, 
			String category_name, String project_id ) 
	{
		this.id = id;
		this.body = body;
		this.status = status;
		this.itemType = itemType;
		this.photosCount = photosCount;
		this.commentsCount = commentsCount;
		this.photo=photo;
		this.comments=comments;
		this.category_name=category_name;
		this.project_id=project_id;
	}
	
	

	public String getid() {
		return id;
}
}
