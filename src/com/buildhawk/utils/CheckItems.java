package com.buildhawk.utils;

public class CheckItems {

	public String item_id;
	public String body;
	public String status;
	public String itemType;
	public String photosCount;
	public String commentsCount;
	public String cat_id;
	public String subCat_id;
	
	public CheckItems(String cat_id, String subCat_id, String item_id, String body, String status, String itemType, String photosCount, String commentsCount)
	{
		this.cat_id = cat_id;
		this.subCat_id = subCat_id;
		this.item_id = item_id;
		this.body = body;
		this.status = status;
		this.itemType = itemType;
		this.photosCount = photosCount;
		this.commentsCount = commentsCount;
	}
	
	public String getcat_id() {
		return cat_id;
	}
	
	public String getsubCat_id() {
		return subCat_id;
	}
	
	public String getitemid() {
		return item_id;
	}
	
	public String getstatus() {
		return status;
	}
}
