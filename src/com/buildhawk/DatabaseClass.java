package com.buildhawk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseClass {

		// TABLE Checklistitem
		public static final String ChecklistitemTable = "checklistitem_table";
		public static final String AUTO_ID = "_id";
		public static final String Project_id = "project_id";
		public static final String Item_id = "user_name";
		public static final String Response_Data = "data";
		
		//TABLE WorkListlistitem
		public static final String WorklistitemTable = "worklistitem_table";
		public static final String AUTO_IDwork = "_id";
		public static final String Project_idwork = "project_id";
		public static final String workItem_id = "user_name";
		public static final String Response_Work = "data";
		
	
		

		// ALTER TABLE tbl AUTO_INCREMENT = 1000;
		public static final String DATABASE_NAME = "database_table.db";
		public static final int DATABASE_VERSION = 2;

		private DbHelper ourhelper1;
		private final Context ourcontext;

		SQLiteDatabase db;

		private static class DbHelper extends SQLiteOpenHelper {

			public DbHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
				// TODO Auto-generated constructor stub
			}

			/**
			 * Create all tables
			 */

			@Override
			public void onCreate(SQLiteDatabase db) {
				// TODO Auto-generated method stub
				
				db.execSQL("CREATE TABLE " + ChecklistitemTable + "("
						+ AUTO_ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Project_id
						+ " TEXT NOT NULL," + Item_id
						+ " TEXT NOT NULL," + Response_Data
						+ " TEXT NOT NULL );");
				
				
				
				db.execSQL("CREATE TABLE " + WorklistitemTable + "("
						+ AUTO_IDwork
						+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Project_idwork
						+ " TEXT NOT NULL," + workItem_id
						+ " TEXT NOT NULL," + Response_Work
						+ " TEXT NOT NULL );");
				
			
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				// TODO Auto-generated method stub
				onCreate(db);
				
			}
		}
		
		// Constructor
		public DatabaseClass(Context c) {
			ourcontext = c;
		}

		// open database to perform operation
		public DatabaseClass open() {
			ourhelper1 = new DbHelper(ourcontext);
			db = ourhelper1.getWritableDatabase();
			return DatabaseClass.this;
		}

		// close the data base after performing any database operation
		public void close() {
			// TODO Auto-generated method stub

			ourhelper1.close();
		}
		
		
		
		
		
		
		
		
		// this method insert the entries into the User_company_table
		public long CreateChkItem(String project_id, String item_id, String userdata) {
			// TODO Auto-generated method stub
			return db.insert(ChecklistitemTable,null,createContentValues_User_company_table(project_id, item_id, userdata));
		}
	
		private ContentValues createContentValues_User_company_table(String project_id, String item_id, String userdata) {
			//Log.e("val to add", "sg: "+userdata);
			ContentValues cv = new ContentValues();
			cv.put(Project_id, project_id);
			cv.put(Item_id, item_id);
			cv.put(Response_Data, userdata);
			return cv;
		}
		
		
		// check if User_company_table row exist
		public boolean exists_checkitem(String pro_id, String item_id) {
			String[] columns = new String[] { Project_id, Item_id };
			Cursor c = db.query(ChecklistitemTable, columns, Project_id + "=?"+ "AND " + Item_id + "=?", new String[] { pro_id, item_id }, null, null, null);
			
			boolean exists = (c.getCount() > 0);
			c.close();
			return exists;
		}
		
		
		// update User_company_table
		public long update_checkitem(String pro_id, String item_id, String resp) {
			// TODO Auto-generated method stub
			
			ContentValues cv = new ContentValues();
			cv.put(Response_Data, resp);
			return db.update(ChecklistitemTable, cv, Project_id + "=?"+ "AND " + Item_id + "=?", new String[] { pro_id,item_id });
		}
		
		
		
		// get user_div data (offline mode)
		public String get_checkitem(String pro_id, String item_id) {
			// TODO Auto-generated method stub
			
			String[] columns = new String[] { Project_id, Item_id, Response_Data };

			Cursor c = db.query(false, ChecklistitemTable, columns, Project_id + "=?"+ "AND " + Item_id + "=?",
					new String[] { pro_id, item_id }, null, null, null, null);
			
			String result1 = "";
			
		
			
			int in1 = c.getColumnIndex(Response_Data);
			

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				result1 = result1 + c.getString(in1);
			}
			c.close();
			c.requery();
		
			
		//	Log.e("get item from db", ",, "+result1);
			
			return result1;
		}
		
		
		
		
		//????????????????????????
		
		
	
		
		
		
		// this method insert the entries into the User_company_table
		public long CreateEntry_workitem(String project_id, String item_id, String userdata) {
			// TODO Auto-generated method stub
			return db.insert(
					WorklistitemTable,
					null,
					createContentValues_workitem(project_id, item_id, userdata));
		}
	
		private ContentValues createContentValues_workitem(String project_id, String item_id, String userdata) {
	
			ContentValues cv = new ContentValues();
			cv.put(Project_idwork, project_id);
			cv.put(workItem_id, item_id);
			cv.put(Response_Work, userdata);
			return cv;
		}
		
		
	
		
		
		// check if User_company_table row exist
		public boolean exists_workitem(String pro_id, String item_id) {
			String[] columns = new String[] { Project_idwork, workItem_id };
			Cursor c = db.query(WorklistitemTable, columns, Project_idwork + "=?"+ "AND " + workItem_id + "=?", new String[] { pro_id, item_id }, null, null, null);
			
			boolean exists = (c.getCount() > 0);
			c.close();
			return exists;
		}
		
	
		
		// update User_company_table
		public long update_workitem(String pro_id, String item_id, String resp) {
			// TODO Auto-generated method stub
			ContentValues cv = new ContentValues();
			cv.put(Response_Work, resp);
			return db.update(WorklistitemTable, cv, Project_idwork + "=?"+ "AND " + workItem_id + "=?", new String[] { pro_id,item_id });
		}
		
		
	
		
		
		// get user_div data (offline mode)
		public String get_workitem(String pro_id, String item_id) {
			// TODO Auto-generated method stub
			
			String[] columns = new String[] { Project_idwork, workItem_id, Response_Work };

			Cursor c = db.query(false, WorklistitemTable, columns, Project_idwork + "=?"+ "AND " + workItem_id + "=?",
					new String[] { pro_id, item_id }, null, null, null, null);
			
			String result1 = "";
			
		
			
			int in1 = c.getColumnIndex(Response_Work);
			

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				result1 = result1 + c.getString(in1);
			}
			c.close();
			c.requery();
		
			
			//android.util.Log.v("get item from db", result1);
			
			return result1;
		}
		
}
