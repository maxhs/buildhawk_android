package com.buildhawk.utils;

import java.util.ArrayList;

public class Categories {

	public String name; 
	public String completedDate;
	public String milestoneDate;
	public String progPerc;
	public ArrayList<SubCategories> subCat;
	
	public Categories(String name, String completeDate, String milstoneDate, String progPerc, ArrayList<SubCategories> subCat)
	{
		this.name = name;
		this.completedDate = completeDate;
		this.milestoneDate = milstoneDate;
		this.progPerc = progPerc;
		this.subCat = subCat;
	}
}
