package com.buildhawk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.Categories;
import com.buildhawk.utils.CheckItems;
import com.buildhawk.utils.CheckList;
import com.buildhawk.utils.Company;
import com.buildhawk.utils.ProjectsFields;
import com.buildhawk.utils.SubCategories;
import com.buildhawk.utils.SynopsisCategories;
import com.buildhawk.utils.SynopsisProject;
import com.buildhawk.utils.SynopsisRecentDocuments;
import com.buildhawk.utils.SynopsisRecentlyCompleted;
import com.buildhawk.utils.SynopsisUpcomingItems;
import com.buildhawk.utils.Users;
import com.buildhawk.utils.subcontractors;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//****************LazyAdapterBrowse*********************

public class ProjectsAdapter extends BaseAdapter {

	private static Activity activity;
	private LayoutInflater inflater;
	private Filter filter;
	ViewHolder holder;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	public static ArrayList<Users> user2 = new ArrayList<Users>();
//	public static ArrayList<subcontractors> sub2 = new ArrayList<subcontractors>();
	public static ArrayList<String> usr = new ArrayList<String>();
	public static ArrayList<String> compny = new ArrayList<String>();
	public static ArrayList<String> compnyId = new ArrayList<String>();
	public static ArrayList<String> userid = new ArrayList<String>();
	// public static ArrayList<Categories> categList = new
	// ArrayList<Categories>();
	//
	// public static ArrayList<Categories> PcategList = new
	// ArrayList<Categories>();
	// public static ArrayList<Categories> activelist = new
	// ArrayList<Categories>();
	// public static ArrayList<Categories> completelist = new
	// ArrayList<Categories>();
	// public static ArrayList<CheckList> checkList = new
	// ArrayList<CheckList>();
	// public static ArrayList<CheckItems> checkall = new
	// ArrayList<CheckItems>();
	//
	// public static ArrayList<CheckItems> progressList2 = new
	// ArrayList<CheckItems>();
	//
	// // public static ArrayList<SynopsisProject>SynProject = new
	// ArrayList<SynopsisProject>();
	// // public static ArrayList<SynopsisCategories>SynCateg = new
	// ArrayList<SynopsisCategories>();
	// // public static ArrayList<SynopsisUpcomingItems>SynItems = new
	// ArrayList<SynopsisUpcomingItems>();
	// // public static ArrayList<SynopsisRecentDocuments>SynDoc = new
	// ArrayList<SynopsisRecentDocuments>();
	// // public static ArrayList<SynopsisRecentlyCompleted>Syncomp= new
	// ArrayList<SynopsisRecentlyCompleted>();
	// public static ArrayList<SubCategories> subCatList;
	// public static ArrayList<SubCategories> activeSubCatList;
	// public static ArrayList<SubCategories> completeSubCatList;
	// public static ArrayList<SubCategories> PsubCatList;
	// public static int k=0;

	public ArrayList<ProjectsFields> projectsListAll = new ArrayList<ProjectsFields>();

	public ProjectsAdapter(Activity act) {
		activity = act;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.v("in adapter", "in adapter");
		connDect = new ConnectionDetector(activity);
		isInternetPresent = connDect.isConnectingToInternet();

		projectsListAll.addAll(Homepage.projectsList);
	}

	public int getCount() {
		return Homepage.projectsList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return Homepage.projectsList.size();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null) {
			holder = new ViewHolder();
		}

		vi = inflater.inflate(R.layout.project_item, null);
		vi.setTag(holder);
		holder.linearLay = (LinearLayout) vi.findViewById(R.id.rv);
		holder.proName = (TextView) vi.findViewById(R.id.proj_name);
		holder.proAdd = (TextView) vi.findViewById(R.id.proj_add);
		holder.proPerc = (TextView) vi.findViewById(R.id.proj_perc);
		holder.selectedPro = (LinearLayout) vi.findViewById(R.id.selected_proj);

		holder.proName.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.proAdd.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.proPerc.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.proName.setText(Homepage.projectsList.get(position).name);
		holder.proAdd
				.setText(Homepage.projectsList.get(position).address.formattedAddress);
		holder.proPerc.setText(Homepage.projectsList.get(position).progress);
		holder.linearLay.setLayoutParams(new ListView.LayoutParams(720, 230));

		ASSL.DoMagic(holder.linearLay);

		holder.proPerc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// projectDetail();
				Prefrences.selectedProId = Homepage.projectsList.get(position).id;
				Prefrences.selectedProName = Homepage.projectsList
						.get(position).name;
				Prefrences.stopingHit = 1;
				
				Prefrences.companyId = Homepage.projectsList.get(position).users
						.get(position).company.coId;
				
				GetUsers();
				
				activity.startActivity(new Intent(activity, Synopsis.class));
				activity.overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			}
		});

		holder.selectedPro.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Prefrences.pageFlag = 1;

				Prefrences.selectedProId = Homepage.projectsList.get(position).id;
				Prefrences.selectedProName = Homepage.projectsList
						.get(position).name;
				GetUsers();
				
					Prefrences.stopingHit = 1;
					Prefrences.companyId = Homepage.projectsList.get(position).users
							.get(position).company.coId;
					Log.i("Prefrences.companyId", Prefrences.companyId);
					activity.startActivity(new Intent(activity,
							ProjectDetail.class));
					activity.overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
			}
		});
		
		holder.selectedPro.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Prefrences.selectedProId = Homepage.projectsList.get(position).id;

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						activity);

				// set title
				//alertDialogBuilder.setTitle("Archive Project");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to Archive this Project?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int id) {
										// if this button is clicked,
										// close
										// current activity

										archive(Prefrences.selectedProId);
										//notifyDataSetChanged();

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int id) {
										// if this button is clicked,
										// just close
										// the dialog box and do nothing
										Log.d("Dialog", "No");
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				return false;
			}
		});

		return vi;

	}

	public void search(String text) {

		text = text.toLowerCase();
		Homepage.projectsList.clear();
		if (text.length() == 0) {
			Homepage.projectsList.addAll(projectsListAll);
		} else {
			for (ProjectsFields projectsFields : projectsListAll) {
				if (projectsFields.name.toLowerCase().contains(text)) {
					Homepage.projectsList.add(projectsFields);
				}
			}
		}
		notifyDataSetChanged();

	}

	class ViewHolder {

		private TextView proName, proAdd, proPerc;
		LinearLayout linearLay;
		LinearLayout selectedPro;

	}

	public void archive(String projectId) {

		 Prefrences.showLoadingDialog(activity, "Loading...");
		//JSONObject json = new JSONObject();
		RequestParams params = new RequestParams();
		params.put("user_id", Prefrences.userId);

		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.post(Prefrences.url + "/projects/" +projectId+"/archive" ,params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
						} catch (Exception e) {
							e.printStackTrace();
						}
						// WorkListCompleteAdapter.punchlst_item(id_punchlist_item,
						// WorkListCompleteAdapter.data, pos);
						 Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Toast.makeText(activity, "Server Issue",
								Toast.LENGTH_LONG).show();

						Log.e("request fail", arg0.toString());
						 Prefrences.dismissLoadingDialog();
					}
				});

	}
	
	public void GetUsers() {

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
						user2.clear();
						usr.clear();
						compny.clear();
						compnyId.clear();
						userid.clear();
						
							JSONArray users = project.getJSONArray("users");

							ArrayList<Users> user = new ArrayList<Users>();
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
								usr.add(uCount.getString("full_name"));

								usrid.add(user.get(j).uId.toString());
								
								Log.d("USERS", "USERS... "
										+ user.get(j).uId+"names "+user.get(j).uFullName);

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
							user2.addAll(user);
							compny.addAll(cmpny);
							compnyId.addAll(cmpnyid);
							userid.addAll(usrid);
							
							Log.d("usssrrsrsrsrsr",
									"sizeeee" + compny.size());

							Log.d("usssrrsrsrsrsr",
									"sizeeee" + compnyId.size());
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						
					}
				});

	}
	
}	
	
	// ************ API for Synopsis Details. *************//

	// public void Synopsis(){
	// Prefrences.showLoadingDialog(activity, "Loading...");
	//
	// RequestParams params = new RequestParams();
	//
	// params.put("id", Prefrences.selectedProId);
	//
	//
	// AsyncHttpClient client = new AsyncHttpClient();
	//
	// client.addHeader("Content-type", "application/json");
	// client.addHeader("Accept", "application/json");
	// client.setTimeout(100000);
	// client.get(Prefrences.url+"/projects/dash", params,
	// new AsyncHttpResponseHandler(){
	// @Override
	// public void onSuccess(String response) {
	// Log.i("request succesfull", "response = " + response);
	//
	// JSONObject res = null;
	// try {
	// res=new JSONObject(response);
	// SynCateg.clear();
	// Syncomp.clear();
	// SynDoc.clear();
	// SynItems.clear();
	// SynProject.clear();
	// JSONObject project = res.getJSONObject("project");
	// Log.v("Synopsis value",project.toString());
	//
	// JSONArray Syncategories = project.getJSONArray("categories");
	// JSONArray Syncompl = project.getJSONArray("recently_completed");
	// JSONArray Syndocs = project.getJSONArray("recent_documents");
	// JSONArray Synitems= project.getJSONArray("upcoming_items");
	// for(int i = 0; i < Syncategories.length(); i++)
	// {
	//
	// JSONObject count = Syncategories.getJSONObject(i);
	//
	// SynCateg.add(new SynopsisCategories(count.getString("name"),
	// count.getString("item_count"), count.getString("completed_count")
	// , count.getString("progress_count"), count.getString("order_index")));
	//
	// }
	// for(int j=0;j<Syncompl.length();j++)
	// {
	// JSONObject count2= Syncompl.getJSONObject(j);
	// Syncomp.add(new SynopsisRecentlyCompleted(count2.getString("id"),
	// count2.getString("body"), count2.getString("status"),
	// count2.getString("item_type"), count2.getString("photos_count"),
	// count2.getString("comments_count")));
	//
	// Log.d("Syncomp","Syncomp"+Syncomp.get(j).itemType);
	// }
	// for(int k=0;k<Syndocs.length();k++)
	// {
	// JSONObject count = Syndocs.getJSONObject(k);
	// SynDoc.add(new SynopsisRecentDocuments(count.getString("id"),
	// count.getString("url_large"), count.getString("original"),
	// count.getString("url_small"),count.getString("url_thumb"),
	// count.getString("image_file_size"),
	// count.getString("image_content_type"),count.getString("source"),count.getString("phase"),
	// count.getString("created_at"),
	// count.getString("user_name"), count.getString("name"),
	// count.getString("created_date")));
	//
	// }
	// for(int l=0;l<Synitems.length();l++)
	// {
	// JSONObject count = Synitems.getJSONObject(l);
	// SynItems.add(new SynopsisUpcomingItems(count.getString("id"),
	// count.getString("body"),count.getString("critical_date"),
	// count.getString("completed_date"), count.getString("status"),
	// count.getString("item_type"),
	// count.getString("photos_count"), count.getString("comments_count")));
	//
	// Log.d("SynItems","SynItems"+SynItems.get(l).itemType);
	//
	// }
	//
	// SynProject.add(new
	// SynopsisProject(project.getString("progress"),SynItems,Syncomp,SynDoc,SynCateg
	// ));
	// for(int j=0;j<SynProject.size();j++){
	// Log.d("","cate"+SynProject.get(j).categories.size());
	// Log.d("","comp"+SynProject.get(j).recentlyCompleted.size());
	// Log.d("","items"+SynProject.get(j).upcomingItems.size());
	// Log.d("","docs"+SynProject.get(j).recentDocuments.size());
	// }
	//
	//
	//
	// activity.startActivity(new Intent(activity, Synopsis.class));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Prefrences.dismissLoadingDialog();
	// }
	//
	// @Override
	// public void onFailure(Throwable arg0) {
	// Log.e("request fail", arg0.toString());
	// Prefrences.dismissLoadingDialog();
	// }
	// });
	// }

	//
	//
	//
	// // ************ API for Project Details. *************//
	//
	// public static void projectDetail(final Activity activity) {
	//
	// Prefrences.showLoadingDialog(activity, "Loading...");
	//
	// RequestParams params = new RequestParams();
	//
	// params.put("user_id", Prefrences.userId);
	//
	//
	// AsyncHttpClient client = new AsyncHttpClient();
	//
	// client.addHeader("Content-type", "application/json");
	// client.addHeader("Accept", "application/json");
	// client.setTimeout(100000);
	// client.get(Prefrences.url+"/checklists/"+Prefrences.selectedProId,
	// params,
	// new AsyncHttpResponseHandler() {
	//
	// @Override
	// public void onSuccess(String response) {
	// Log.i("request succesfull", "response = " + response);
	// checkall.clear();
	// JSONObject res = null;
	// try {
	// res=new JSONObject(response);
	//
	// JSONObject checklist = res.getJSONObject("checklist");
	// Log.v("checklist value",checklist.toString());
	//
	// JSONArray categories = checklist.getJSONArray("phases");
	// categList.clear();
	//
	// PcategList.clear();// = new ArrayList<Categories>();
	// activelist.clear();// = new ArrayList<Categories>();
	// completelist.clear();// = new ArrayList<Categories>();
	// checkList.clear();// = new ArrayList<CheckList>();
	// checkall.clear();// = new ArrayList<CheckItems>();
	//
	// progressList2.clear();// = new ArrayList<CheckItems>();
	//
	// for(int i = 0; i < categories.length(); i++)
	//
	// {
	//
	// JSONObject count = categories.getJSONObject(i);
	//
	// JSONArray subCategories = count.getJSONArray("categories");
	//
	// subCatList = new ArrayList<SubCategories>();
	// PsubCatList = new ArrayList<SubCategories>();
	// activeSubCatList = new ArrayList<SubCategories>();
	// completeSubCatList = new ArrayList<SubCategories>();
	//
	// for(int j = 0; j < subCategories.length(); j++)
	// {
	// JSONObject uCount = subCategories.getJSONObject(j);
	//
	// JSONArray checkItem = uCount.getJSONArray("checklist_items");
	//
	// ArrayList<CheckItems> checkItemList = new ArrayList<CheckItems>();
	// ArrayList<CheckItems> activeCheckItemList = new ArrayList<CheckItems>();
	// ArrayList<CheckItems> progressList = new ArrayList<CheckItems>();
	// ArrayList<CheckItems> completeCheckItemList = new
	// ArrayList<CheckItems>();
	// for(int m = 0; m < checkItem.length(); m++)
	// {
	// JSONObject cCount = checkItem.getJSONObject(m);
	//
	// checkItemList.add(new CheckItems(cCount.getString("id"),
	// cCount.getString("body"), cCount.getString("status")
	// , cCount.getString("item_type"), cCount.getString("photos_count"),
	// cCount.getString("comments_count")));
	// if(!checkItemList.get(m).status.equals("Completed") &&
	// !checkItemList.get(m).status.equals("Not Applicable"))
	// {
	// activeCheckItemList.add(new CheckItems(cCount.getString("id"),
	// cCount.getString("body"), cCount.getString("status")
	// , cCount.getString("item_type"), cCount.getString("photos_count"),
	// cCount.getString("comments_count")));
	// }
	//
	// if(checkItemList.get(m).status.equals("Completed"))
	// {
	// completeCheckItemList.add(new CheckItems(cCount.getString("id"),
	// cCount.getString("body"), cCount.getString("status")
	// , cCount.getString("item_type"), cCount.getString("photos_count"),
	// cCount.getString("comments_count")));
	// }
	//
	// if(checkItemList.get(m).status.equals("In-Progress"))
	// {
	//
	// progressList.add(new CheckItems(cCount.getString("id"),
	// cCount.getString("body"), cCount.getString("status")
	// , cCount.getString("item_type"), cCount.getString("photos_count"),
	// cCount.getString("comments_count")));
	// //progressList2.addAll(progressList);
	// //Log.d("","Size="+progressList2.size()+"Body="+progressList2.get(m).body);
	// }
	//
	// }
	// checkall.addAll(checkItemList);
	// //Log.d("checkall","size of checkall"+checkall.size());
	// progressList2.addAll(progressList);
	// if(activeCheckItemList.size()>0)
	// {
	// activeSubCatList.add(new SubCategories(uCount.getString("name"),
	// uCount.getString("completed_date"), uCount.getString("milestone_date")
	// , uCount.getString("progress_percentage"), activeCheckItemList));
	// }
	//
	// if(completeCheckItemList.size()>0)
	// {
	// completeSubCatList.add(new SubCategories(uCount.getString("name"),
	// uCount.getString("completed_date"), uCount.getString("milestone_date")
	// , uCount.getString("progress_percentage"), completeCheckItemList));
	// }
	//
	// subCatList.add(new SubCategories(uCount.getString("name"),
	// uCount.getString("completed_date"), uCount.getString("milestone_date")
	// , uCount.getString("progress_percentage"), checkItemList));
	// PsubCatList.add(new SubCategories(uCount.getString("name"),
	// uCount.getString("completed_date"), uCount.getString("milestone_date")
	// , uCount.getString("progress_percentage"),progressList));
	// }
	// Log.d("checkall","size of checkall"+checkall.size());
	// categList.add(new Categories(count.getString("name"),
	// count.getString("completed_date"), count.getString("milestone_date")
	// , count.getString("progress_percentage"), subCatList));
	// PcategList.add(new Categories(count.getString("name"),
	// count.getString("completed_date"), count.getString("milestone_date")
	// , count.getString("progress_percentage"), PsubCatList));
	//
	// if(activeSubCatList.size()>0)
	// {
	// activelist.add(new Categories(count.getString("name"),
	// count.getString("completed_date"), count.getString("milestone_date")
	// , count.getString("progress_percentage"), activeSubCatList));
	//
	// }
	//
	// if(completeSubCatList.size()>0)
	// {
	// completelist.add(new Categories(count.getString("name"),
	// count.getString("completed_date"), count.getString("milestone_date")
	// , count.getString("progress_percentage"), completeSubCatList));
	// //Log.d("ActiveList",""+activelist.size());
	// }
	//
	// }
	//
	//
	//
	//
	// checkList.add(new CheckList(checklist.getString("id"),
	// checklist.getString("name"), categList));
	//
	// Log.d("project adapter ","Progress List size = "+progressList2.size());
	// // for(int i=0;i<progressList2.size();i++)
	// Log.d("project adapter ","body"+progressList2.size());
	//
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Prefrences.dismissLoadingDialog();
	// }
	//
	// @Override
	// public void onFailure(Throwable arg0) {
	// Log.e("request fail", arg0.toString());
	// Prefrences.dismissLoadingDialog();
	// }
	// });
	//
	// }

