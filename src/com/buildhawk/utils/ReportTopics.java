package com.buildhawk.utils;

import java.util.ArrayList;

public class ReportTopics {

	public String id; 
	public String reportId;
	public ArrayList<SafetyTopics>safety;
	
	public ReportTopics(String id, String reportId, ArrayList<SafetyTopics>safety){
		this.id = id;
		this.reportId = reportId;
		this.safety=safety;
	}
	
}
