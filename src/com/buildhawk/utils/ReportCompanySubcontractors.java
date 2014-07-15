package com.buildhawk.utils;

import java.util.ArrayList;

public class ReportCompanySubcontractors {

	public String coId; 
	public String coName;
	ArrayList<ReportCompanyUsers>subUsers;
	public String userCount;
	
	public ReportCompanySubcontractors(String id, String name, ArrayList<ReportCompanyUsers>subUsers, String userCount){
		this.coId = id;
		this.coName = name;
		this.subUsers=subUsers;
		this.userCount=userCount;
	}
	
}
