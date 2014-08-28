package com.buildhawk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;

import com.buildhawk.R.color;
import com.buildhawk.utils.Company;
import com.buildhawk.utils.Users;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/*
 *  In this we are showing the expandable list of companies and under those companies the users related to particular company. 
 * 
 */
public class CompanyExpandable extends Activity {

	ExpandableListView expandableListView;
	ArrayList<Users>usersArrayList =new ArrayList<Users>();
	RelativeLayout relativelayoutRoot;
	RelativeLayout relativelayoutBack;
	public static ArrayList<Users> user2ArrayList = new ArrayList<Users>();
	public static ArrayList<Company>compny2ArrayList=new ArrayList<Company>();
//	public static ArrayList<subcontractors> sub2 = new ArrayList<subcontractors>();
	public static ArrayList<String> usrArrayList = new ArrayList<String>();
	public static ArrayList<String> compnyArrayList = new ArrayList<String>();
	public static ArrayList<String> compnyIdArrayList = new ArrayList<String>();
	public static ArrayList<String> useridArrayList = new ArrayList<String>();
	public static int size;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.companyexpandable);
		
		relativelayoutRoot = (RelativeLayout) findViewById(R.id.relativelayoutRootCompany);
		new ASSL(this, relativelayoutRoot, 1134, 720, false);
		
		relativelayoutBack=(RelativeLayout)findViewById(R.id.relativelayoutBack);
		expandableListView=(ExpandableListView)findViewById(R.id.expandableListView);
		GetUsers();
		
		relativelayoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		
		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Nothing here ever fires
                System.err.println("child clicked");
//                Toast.makeText(getApplicationContext(), "child clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
		expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				Prefrences.selectedCompnyId=compny2ArrayList.get(groupPosition).coId.toString();
				Prefrences.selectedCompanyName=compny2ArrayList.get(groupPosition).coName.toString();
				System.err.println(""+Prefrences.selectedCompnyId+","+Prefrences.selectedCompanyName);
//				Toast.makeText(getApplicationContext(), "---"+Prefrences.selectedCompnyId, Toast.LENGTH_SHORT).show();
				usersArrayList.clear();
				for(int i=0;i<user2ArrayList.size();i++)
				{
				if(user2ArrayList.get(i).company.coId.toString().equalsIgnoreCase(Prefrences.selectedCompnyId)){
//					tv.setText(""+ProjectsAdapter.user2.get(childPosition).uFullName);	
//					tv.setVisibility(View.VISIBLE);
					usersArrayList.add(user2ArrayList.get(i));
				}
				}
				return false;
			}
		});
		
		expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
		    int previousItem = -1;

		    @Override
		    public void onGroupExpand(int groupPosition) {
		        if(groupPosition != previousItem )
		        	expandableListView.collapseGroup(previousItem );
		        previousItem = groupPosition;
		    }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		GetUsers();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		finish();
	}

	public class ParentLevel extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			final TextView textView = new TextView(CompanyExpandable.this);
			
	        textView.setTextColor(Color.BLACK);
	        textView.setTextSize(20);
	        textView.setPadding(60, 5, 5, 5);
//	        textView.setBackgroundColor(Color.WHITE);
	        textView.setLayoutParams(new ListView.LayoutParams(
	                     LayoutParams.MATCH_PARENT, 150));
//	        textView.setPadding(20, 0, 0, 0);
	        ASSL.DoMagic(textView);
//	        textView.setTag();
	     
	        Log.d("-------","positionsss--- "+childPosition+","+groupPosition+","+usersArrayList.size());
			if(usersArrayList.size()==childPosition)	
			{
				textView.setHint("Add a contact");
				textView.setGravity(Gravity.CENTER);
				textView.setBackgroundResource(color.child_color);
				textView.setHintTextColor(Color.BLACK);
			}
			else {
				textView.setGravity(Gravity.CENTER_VERTICAL);
				 textView.setText("  "+usersArrayList.get(childPosition).uFullName);	
				 textView.setBackgroundResource(color.subchild_color);
			}
			
	        textView.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(textView.getText().toString().equalsIgnoreCase("")){
					Intent intent = new Intent(CompanyExpandable.this,AddUser.class);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					
					finish();
					}
					else{
						Prefrences.assignee_str=textView.getText().toString();
						Prefrences.assigneeID=usersArrayList.get(childPosition).uId.toString();
						WorkItemClick.btnS_assigned.setText(Prefrences.assignee_str);
						finish();
					}
				}
			});
	        return textView;

		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return usersArrayList.size()+1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return size;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			TextView textView = new TextView(CompanyExpandable.this);
	        textView.setText(""+compny2ArrayList.get(groupPosition).coName);
	        
//	        Log.d("Compny id","Compny id "+Prefrences.companyId);
	        
	        textView.setTextColor(Color.BLACK);
	        textView.setTextSize(20);
	        textView.setBackgroundColor(Color.WHITE);
	        textView.setPadding(20, 7, 7, 7);
	        textView.setLayoutParams(new ListView.LayoutParams(
                    LayoutParams.MATCH_PARENT, 150));
	        textView.setGravity(Gravity.CENTER_VERTICAL);

	        ASSL.DoMagic(textView);
	        
	        return textView;

			
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}
	
	// ************ API for Get all users for a particular project *************//

	public void GetUsers() {
		Prefrences.showLoadingDialog(CompanyExpandable.this, "Loading...");	
		RequestParams params = new RequestParams();

		params.put("id", Prefrences.selectedProId);
		
//		Editor editor =  sharedpref.edit();
//		editor.putString("regId", regId);
//		editor.commit();
		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		client.setTimeout(100000);
		client.get(Prefrences.url + "/projects/"+Prefrences.selectedProId, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);
//						fillServerData(response);
						JSONObject res;
						try {
							res = new JSONObject(response);
							Log.d("RESPONSE","RESPONSE"+res.toString(2));
																		
								JSONObject project = res.getJSONObject("project");
						user2ArrayList.clear();
						compny2ArrayList.clear();
						usrArrayList.clear();
						compnyArrayList.clear();
						compnyIdArrayList.clear();
						useridArrayList.clear();
						
//						id": 94,
//			            "first_name": "Max",
//			            "last_name": "Orca",
//			            "full_name": "Max Orca",
//			            "phone": "9732079450",
//			            "email": null,
//			            "company": {
//			                "id": 8,
//			                "name": "Test"
//			            }
						
							JSONArray users = project.getJSONArray("users");

							ArrayList<Users> user = new ArrayList<Users>();
							ArrayList<Users> conn_user = new ArrayList<Users>();
							ArrayList<String> cmpny = new ArrayList<String>();
							ArrayList<String> cmpnyid = new ArrayList<String>();
							ArrayList<String> usrid = new ArrayList<String>();
							
							ArrayList<Company> CompaniesArray = new ArrayList<Company>();

							for (int j = 0; j < users.length(); j++) {
								JSONObject uCount = users.getJSONObject(j);
								JSONObject uCompany = uCount
										.getJSONObject("company");

								user.add(new Users(
										uCount.getString("id"),
										uCount.getString("first_name"),
										uCount.getString("last_name"),
										uCount.getString("full_name"),
										uCount.getString("email"),
										uCount.getString("formatted_phone"),
//										uCount.getString("authentication_token"),
										uCount.getString("url_thumb"),
										uCount.getString("url_small"),
										new Company(uCompany
												.getString("id"), uCompany
												.getString("name"))));
								// if(!uCount.getString("full_name").equals("null"))
								usrArrayList.add(uCount.getString("full_name"));

								usrid.add(user.get(j).uId.toString());
								
								Log.d("USERS", "USERS... "
										+ user.get(j).uId+"names "+user.get(j).uFullName);

							}
							
							JSONArray connectUsers = project.getJSONArray("connect_users");
							for (int j = 0; j < connectUsers.length(); j++) {
								JSONObject uCount = connectUsers.getJSONObject(j);
								JSONObject CCompany = uCount
										.getJSONObject("company");

								conn_user.add(new Users(
										uCount.getString("id"),
										uCount.getString("first_name"),
										uCount.getString("last_name"),
										uCount.getString("full_name"),
										uCount.getString("email"),
										uCount.getString("phone"),
//										uCount.getString("authentication_token"),
										"","",
										new Company(CCompany
												.getString("id"), CCompany
												.getString("name"))));
								// if(!uCount.getString("full_name").equals("null"))
//								usr.add(uCount.getString("full_name"));
//
//								usrid.add(user.get(j).uId.toString());
								
								Log.d("USERS", "USERS... "
										+ conn_user.get(j).uId+"names "+conn_user.get(j).uFullName);

							}
							
							JSONArray company = project
									.getJSONArray("companies");
							for(int j=0;j<company.length();j++)
							{
								JSONObject uCount = company.getJSONObject(j);
								
								CompaniesArray.add(new Company(uCount.getString("id"), uCount.getString("name")));
								
								cmpny.add(CompaniesArray.get(j).coName.toString());
								cmpnyid.add(CompaniesArray.get(j).coId.toString());
								Log.d("companieeeesss", "companiess... "
										+ CompaniesArray.get(j).coId);
							}
							user2ArrayList.addAll(user);
							user2ArrayList.addAll(conn_user);
							compny2ArrayList.addAll(CompaniesArray);
							compnyArrayList.addAll(cmpny);
							compnyIdArrayList.addAll(cmpnyid);
							useridArrayList.addAll(usrid);
							
							Log.d("usssrrsrsrsrsr",
									"sizeeee" + compnyArrayList.size());

							Log.d("usssrrsrsrsrsr",
									"sizeeee" + compnyIdArrayList.size());
							
							Log.d("compny2",
									"compny2" + compnyIdArrayList.size());
							
							expandableListView.setAdapter(new ParentLevel());
							size=compny2ArrayList.size();
//							Intent in = new Intent(activity,CompanyExpandable.class);
//							activity.startActivity(in);
							Prefrences.dismissLoadingDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Prefrences.dismissLoadingDialog();
					}
				});

	}

	
}