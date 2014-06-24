package com.buildhawk.utils;

import java.util.ArrayList;

public class SynopsisCategories {

	public String name; 
	public String itemCount;
	public String completedCount;
	public String progressCount;
	public String orderIndex;
	
	public SynopsisCategories(String name,String itemCount, String completedCount,String progressCount, String orderIndex)
	{
		this.name = name;
		this.itemCount = itemCount;
		this.completedCount = completedCount;
		this.progressCount = progressCount;
		this.orderIndex = orderIndex;
	}
}
