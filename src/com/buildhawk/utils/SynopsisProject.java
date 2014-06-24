package com.buildhawk.utils;

import java.util.ArrayList;

public class SynopsisProject {

	public String progress; 
	public ArrayList<SynopsisUpcomingItems> upcomingItems;
	public ArrayList<SynopsisRecentlyCompleted> recentlyCompleted;
	public ArrayList<SynopsisRecentDocuments> recentDocuments;
	public ArrayList<SynopsisCategories> categories;
	
	
	public SynopsisProject(String progress, ArrayList<SynopsisUpcomingItems> upcomingItems, ArrayList<SynopsisRecentlyCompleted> recentlyCompleted ,
			ArrayList<SynopsisRecentDocuments> recentDocuments, ArrayList<SynopsisCategories> categories) 
	{
		this.progress = progress;
		this.upcomingItems = upcomingItems;
		this.recentlyCompleted = recentlyCompleted;
		this.recentDocuments = recentDocuments;
		this.categories = categories;
		
	}
}
