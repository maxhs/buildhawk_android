package com.buildhawk.utils;

import java.util.ArrayList;

public class ReportPersonnel {
	public String id;
	public ArrayList<ReportPersonnelUser>users; 	
	public String hours;
//	public ArrayList<subcontractors>psubs;
//	public String count;

	public ReportPersonnel(String id, ArrayList<ReportPersonnelUser>users, String hours){ //,ArrayList<subcontractors>psubs, String count) {
	
		this.id=id;
		this.users=users;
		this.hours=hours;
//		this.psubs=psubs;
//		this.count=count;
	}
}
