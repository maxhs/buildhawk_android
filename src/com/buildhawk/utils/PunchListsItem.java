package com.buildhawk.utils;

import java.util.ArrayList;

public class PunchListsItem {
	
public String id;
public String body;
public ArrayList<Assignee> assignee;
//public String subAssignee;
public String location;
public String completedAt;
public String completed;
public ArrayList<PunchListsPhotos> photos;
public String createdAt;
public ArrayList<Comments> comments;
public String epoch_time;

public PunchListsItem(String id, String body, ArrayList<Assignee> assignee//, String subAssignee
		, String location, String completedAt,String completed , ArrayList<PunchListsPhotos> photos, String createdAt, ArrayList<Comments> comments, String epoch_time){
	this.id=id;
	this.body=body;
	this.assignee=assignee;
	//this.subAssignee=subAssignee;
	this.location=location;
	this.completedAt=completedAt;
	this.completed=completed;
	this.photos=photos;
	this.createdAt=createdAt;
	this.comments=comments;
	this.epoch_time=epoch_time;
	
	}
}
