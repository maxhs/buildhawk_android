package com.buildhawk.utils;

import java.util.ArrayList;

public class Assignee {

	public String id;
	public String firstName; 
	public String lastName;
	public String fullName;
	public String admin;
	public String companyAdmin;
	public String userAdmin;
	public String email;
	public String phoneNumber;
	public String authenticationToken;	
	public String urlThumb;
	public String urlSmall;	
	public ArrayList<Company> company;
	
	public Assignee(String id, String firstName, String lastName, String fullName, String admin, String companyAdmin, 
			String userAdmin, String email, String phoneNumber, String authenticationToken, String urlThumb, String urlSmall, ArrayList<Company> company)
	{
		this.id = id;
		this.firstName=firstName;
		this.lastName=lastName;
		this.fullName=fullName;
		this.admin=admin;
		this.companyAdmin=companyAdmin;
		this.userAdmin=userAdmin;
		this.email=email;
		this.phoneNumber=phoneNumber;
		this.authenticationToken=authenticationToken;
		this.urlThumb=urlThumb;
		this.urlSmall=urlSmall;
		this.company=company;
	}
}
