package com.buildhawk.utils;
public class Users{
		
		public String uId; 
		public String uFirstName;
		public String uLastName;
		public String uFullName;
		public String uEmail;
		public String uPhone;
//		public String uToken;
		public String uUrl100;
		public String uUrl200;
		public Company company;
		
		public Users(String id, String firstName, String lastName, String fullName,	String email, String phone, //String token,
				String url100, String url200, Company company){
			
			this.uId = id;
			this.uFirstName = firstName;
			this.uLastName = lastName;
			this.uFullName = fullName;
			this.uEmail = email;
			this.uPhone = phone;
//			this.uToken = token;
			this.uUrl100 = url100;
			this.uUrl200 = url200;
			this.company = company;
		}
		
		
	}