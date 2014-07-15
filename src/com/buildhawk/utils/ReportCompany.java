package com.buildhawk.utils;

import java.util.ArrayList;

public class ReportCompany {

	public String coId; 
	public String coName;
	ArrayList<ReportCompanyUsers>coUsers;
	ArrayList<ReportCompanySubcontractors>coSub;
	
	public ReportCompany(String id, String name, ArrayList<ReportCompanyUsers>coUsers, ArrayList<ReportCompanySubcontractors>coSub){
		this.coId = id;
		this.coName = name;
		this.coUsers=coUsers;
		this.coSub=coSub;
	}
	
}
