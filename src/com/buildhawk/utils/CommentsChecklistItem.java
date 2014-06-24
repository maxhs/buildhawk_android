package com.buildhawk.utils;

import java.util.ArrayList;

public class CommentsChecklistItem {

	
	public String id;
	public String body;
	public String created_at;
	public ArrayList<CommentsUserChecklistItem> cuser;

	public CommentsChecklistItem(String id, String body, ArrayList<CommentsUserChecklistItem> cuser,
			String created_at) {
		this.id = id;
		this.body = body;
		this.created_at = created_at;
		this.cuser = cuser;
	}
	
}
