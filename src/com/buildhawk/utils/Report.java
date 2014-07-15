package com.buildhawk.utils;

import java.util.ArrayList;

public class Report {
	public String report_id;
	public String epoch_time;
	public String created_at;
	public String updated_at;
	public String created_date;
	public String title;
	public String report_type;
	public String body;
	public String weather;
	public String weather_icon;
	public String precip;
	public String temp;
	public String wind; 
	public String humidity;
	public ArrayList<Author> author;
//	public ArrayList<String> possible_types;
//	public ArrayList<Comments> comments;
	public ArrayList<ProjectPhotos> photos;
	public ArrayList<ReportPersonnel>personnel;
	public ArrayList<ReportCompanies>companies;
	public ArrayList<ReportTopics>topic;

	public Report(String report_id, String epoch_time, String created_at,
			String updated_at, String created_date, String title,
			String report_type, String body, String weather, String weather_icon,
			String precip, String temp, String wind, String humidity,
			ArrayList<Author> author,ArrayList<ProjectPhotos> photos,ArrayList<ReportPersonnel>personnel,
			ArrayList<ReportCompanies>companies,ArrayList<ReportTopics>topic) {

		this.report_id = report_id;
		this.epoch_time = epoch_time;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.created_date = created_date;
		this.title = title;
		this.report_type = report_type;
		this.body=body;
		this.weather = weather;
		this.weather_icon = weather_icon;
		this.precip = precip;
		this.temp = temp;
		this.wind = wind;
		this.humidity = humidity;
		this.author = author;
		this.photos=photos;
		this.personnel=personnel;
		this.companies=companies;
		this.topic=topic;

	}
}
