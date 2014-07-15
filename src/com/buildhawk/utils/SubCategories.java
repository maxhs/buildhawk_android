package com.buildhawk.utils;

import java.util.ArrayList;

public class SubCategories {

	public String sub_id; 
	public String name; 
	public String completedDate;
	public String milestoneDate;
	public String progPerc;
	public ArrayList<CheckItems> checkItems;
	
	public SubCategories(String sub_id, String name, String completeDate, String milstoneDate, String progPerc, ArrayList<CheckItems> checkItems)
	{
		this.sub_id = sub_id;
		this.name = name;
		this.completedDate = completeDate;
		this.milestoneDate = milstoneDate;
		this.progPerc = progPerc;
		this.checkItems = checkItems;
	}
	
	public String getsub_id(){
		return sub_id;
	}
	
	public  ArrayList<CheckItems> getcheckItems(){
		return checkItems;
	}
}
