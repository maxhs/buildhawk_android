package com.buildhawk.utils;

import java.util.ArrayList;

public class Categories {

	public String id; 
	public String name; 
	public String completedDate;
	public String milestoneDate;
	public String progPerc;
	public ArrayList<SubCategories> subCat;
	
	public Categories(String id, String name, String completeDate, String milstoneDate, String progPerc, ArrayList<SubCategories> subCat)
	{
		
		this.id = id;
		this.name = name;
		this.completedDate = completeDate;
		this.milestoneDate = milstoneDate;
		this.progPerc = progPerc;
		this.subCat = subCat;
	}
	
	
	public String getid(){
		return id;
	}
	
	public ArrayList<SubCategories> getsubcat_array(){
		return subCat;
	}
}
