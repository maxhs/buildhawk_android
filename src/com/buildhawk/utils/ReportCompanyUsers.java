package com.buildhawk.utils;

public class ReportCompanyUsers {

	public String uId; 
	public String uFirstName;
	public String uLastName;
	public String uFullName;
	public String uEmail;
	public String uPhone;

	
	public ReportCompanyUsers(String id, String firstName, String lastName, String fullName,
			String email, String phone){
		
		this.uId = id;
		this.uFirstName = firstName;
		this.uLastName = lastName;
		this.uFullName = fullName;
		this.uEmail = email;
		this.uPhone = phone;
		
	}
	
}
