package com.buildhawk.utils;

import java.util.ArrayList;

public class SubCategories {

	public String name; 
	public String completedDate;
	public String milestoneDate;
	public String progPerc;
	public ArrayList<CheckItems> checkItems;
	
	public SubCategories(String name, String completeDate, String milstoneDate, String progPerc, ArrayList<CheckItems> checkItems)
	{
		this.name = name;
		this.completedDate = completeDate;
		this.milestoneDate = milstoneDate;
		this.progPerc = progPerc;
		this.checkItems = checkItems;
	}
}
