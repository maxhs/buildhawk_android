package com.buildhawk.utils;
public class Address{
		
		public String street1; 
		public String street2;
		public String city;
		public String zip;
		public String country;
		public String phone;
		public String formattedAddress;
		public String latitude;
		public String longitude;
		
		public Address(String street1, String street2, String city, String zip, String country,
				String phone,String forAddress, String lat, String longi)
		{
			this.street1 = street1;
			this.street2 = street2;
			this.city = city;
			this.zip = zip;
			this.country = country;
			this.phone = phone;
			this.formattedAddress = forAddress;
			this.latitude = lat;
			this.longitude = longi;
		}
	}