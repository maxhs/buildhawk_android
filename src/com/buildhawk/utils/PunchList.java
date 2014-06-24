package com.buildhawk.utils;

import java.util.ArrayList;

public class PunchList {
	
	public ArrayList<PunchListsItem> punchlistsItem;
	public ArrayList<Personnel>personnel;
	
	public PunchList( ArrayList<PunchListsItem> punchlistsItem, ArrayList<Personnel>personnel)
	{
		this.punchlistsItem=punchlistsItem;
		this.personnel=personnel;
	}

}
