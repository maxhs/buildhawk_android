package com.buildhawk.utils;

import java.util.ArrayList;

public class ReportPersonnelUser {
	
	public String uId; 
	public String uFirstName;
	public String uLastName;
	public String uFullName;
	public String uEmail;
	public String uPhone;
	public ArrayList<Company> company;
	
	public ReportPersonnelUser(String id, String firstName, String lastName, String fullName,
			String email, String phone, ArrayList<Company> company){
	this.uId = id;
	this.uFirstName = firstName;
	this.uLastName = lastName;
	this.uFullName = fullName;
	this.uEmail = email;
	this.uPhone = phone;
	this.company=company;
	}

}
