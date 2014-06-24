package com.buildhawk.utils;

public class Author {
	public String authorid;
	public String first_name;
	public String last_name;
	public String full_name;
	public String email;
	public String phone_number;

	public Author(String authorid, String first_name, String last_name,
			String full_name, String email, String phone_number) {
		this.first_name = first_name;
		this.authorid = authorid;
		this.last_name = last_name;
		this.full_name = full_name;
		this.email = email;
		this.phone_number = phone_number;
	}
}