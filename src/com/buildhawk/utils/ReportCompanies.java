package com.buildhawk.utils;

import java.util.ArrayList;

public class ReportCompanies {

	public String coId; 
	public ArrayList<ReportCompany>Rcompany;
	public String coCount;
	
	public ReportCompanies(String id, String count, ArrayList<ReportCompany>Rcompany){
		this.coId = id;
		this.coCount = count;
		this.Rcompany=Rcompany;
	}
	
}
