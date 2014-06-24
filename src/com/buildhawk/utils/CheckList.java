package com.buildhawk.utils;

import java.util.ArrayList;

public class CheckList {

	public String id;
	public String name;
	public ArrayList<Categories> cate;
	
	public CheckList(String id, String name, ArrayList<Categories> cate)
	{
		this.id = id;
		this.name = name;
		this.cate = cate;
	}
}
