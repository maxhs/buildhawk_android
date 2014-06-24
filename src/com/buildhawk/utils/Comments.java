package com.buildhawk.utils;

import java.util.ArrayList;

public class Comments {
	public String id;
	public String body;
	public String created_at;
	public ArrayList<CommentUser> cuser;

	public Comments(String id, String body, ArrayList<CommentUser> cuser,
			String created_at) {
		this.id = id;
		this.body = body;
		this.created_at = created_at;
		this.cuser = cuser;
	}

}
