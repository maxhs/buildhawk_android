package com.buildhawk.utils;

import java.util.ArrayList;

public class ProjectsFields {

	public String id; 
	public String name;
	public String active;
	public String core;
	public String progress;
	public Address address;
	public ArrayList<Users> users;
	public Company company;
	
	public ProjectsFields(String id, String name, String active, String core, String progress, Address address, ArrayList<Users> users, Company company)
	{
		this.id = id;
		this.name = name;
		this.active = active;
		this.core = core;
		this.progress = progress;
		this.address = address;
		this.users = users;
		this.company=company;
	}
	
	
	
	
	
	
}
