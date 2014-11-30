package com.buildhawk;

/*
 *  This file shows all worklist of selected project.
 */

import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.R.drawable;
import com.buildhawk.utils.Assignee;
import com.buildhawk.utils.CommentUser;
import com.buildhawk.utils.Comments;
import com.buildhawk.utils.Company;
import com.buildhawk.utils.Personnel;
import com.buildhawk.utils.PunchList;
import com.buildhawk.utils.PunchListsItem;
import com.buildhawk.utils.PunchListsPhotos;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WorklistFragment extends Fragment {
	TextView tv_active, tv_assignee, tv_location, tv_completed;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	String totalCount;
	PullToRefreshListView listview;
	Dialog dialog;
	ArrayList<String> loc = new ArrayList<String>();
	ArrayList<String> ass = new ArrayList<String>();
	public  ArrayList<String> locss;
	public  ArrayList<String> asss;
//	String selected_location;

	public static ArrayList<String> locs ;
	ArrayList<PunchList> punchList = new ArrayList<PunchList>();
	public static ArrayList<PunchListsItem> punchListItem = new ArrayList<PunchListsItem>();
	ArrayList<Personnel> personnel = new ArrayList<Personnel>();
	public static ArrayList<Assignee> assigne;
	ArrayList<Comments> commnt;
	ArrayList<CommentUser> cusr;
	ArrayList<PunchListsPhotos> punchlistphoto;
	public static ArrayList<PunchListsItem> worklist_complted;
	ArrayList<PunchListsItem> slectedLocs;
	public static ArrayList<PunchListsItem> worklist_Active;
	public static ArrayList<String> assignees;
	Boolean pull = false;
	RelativeLayout relLay;
	static Context con;
	WorkListCompleteAdapter workComAdp;
	SharedPreferences sharedpref;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.worklist, container, false);

		relLay = (RelativeLayout) root.findViewById(R.id.rellay);
		new ASSL(getActivity(), relLay, 1134, 720, false);

		con = getActivity();
		sharedpref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
		listview = (PullToRefreshListView) root.findViewById(R.id.workList);
		tv_active = (TextView) root.findViewById(R.id.active);
		tv_location = (TextView) root.findViewById(R.id.location);
		tv_assignee = (TextView) root.findViewById(R.id.assignee);
		tv_completed = (TextView) root.findViewById(R.id.completed);

		tv_active.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		tv_assignee.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		tv_location.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		tv_completed.setTypeface(Prefrences.helveticaNeuelt(getActivity()));

		connDect = new ConnectionDetector(getActivity());
		isInternetPresent = connDect.isConnectingToInternet();
		
		
//		if(Prefrences.worklist_s.equalsIgnoreCase("")){
//			Prefrences.worklist_bool=false;
//		}
		if(Prefrences.selectedProId.equalsIgnoreCase(Prefrences.LastSelectedProId))
		{
			if(!Prefrences.LastWorklist_s.equalsIgnoreCase(""))
//				Prefrences.document_bool = true;
//			else
			{
				Prefrences.worklist_s=Prefrences.LastWorklist_s;			
				Prefrences.worklist_bool = true;
			}
			else{
				if (Prefrences.worklist_s.equalsIgnoreCase("")) {
					Prefrences.worklist_bool = false;
				}
			}
		}
		else
		{
			if (Prefrences.worklist_s.equalsIgnoreCase("")) {
				Prefrences.worklist_bool = false;
			}
		}

		listview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub

				if (isInternetPresent) {
					pull = true;
//					listview.onRefreshComplete();
					punchlst();
				} else {
					Toast.makeText(getActivity(), "No internet connection.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		tv_active.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Prefrences.worklisttypes==1)
				{
					Prefrences.worklisttypes=0;
					
					tv_active.setBackgroundResource(R.drawable.all_white_background);
					tv_completed.setBackgroundResource(R.drawable.complete_white_background);
					tv_assignee.setBackgroundResource(R.color.white);
					tv_location.setBackgroundResource(R.color.white);

					tv_completed.setTextColor(Color.BLACK);
					tv_active.setTextColor(Color.BLACK);
					tv_assignee.setTextColor(Color.BLACK);
					tv_location.setTextColor(Color.BLACK);
					
					workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, punchListItem);
					listview.setAdapter(workComAdp);
				}else
				{
					Prefrences.worklisttypes=1;
					tv_active.setBackgroundResource(R.drawable.all_black_background);
					tv_completed
							.setBackgroundResource(R.drawable.complete_white_background);
					tv_assignee.setBackgroundResource(R.color.white);
					tv_location.setBackgroundResource(R.color.white);

					tv_completed.setTextColor(Color.BLACK);
					tv_active.setTextColor(Color.WHITE);
					tv_assignee.setTextColor(Color.BLACK);
					tv_location.setTextColor(Color.BLACK);

					listview.setVisibility(View.VISIBLE);
					if (Prefrences.text == 10) {
						Toast.makeText(getActivity(), "Server Issue",
								Toast.LENGTH_SHORT).show();
					} else {
						Log.i("WORK LIST SIZE", "" + worklist_Active.size());
						workComAdp = new WorkListCompleteAdapter(
								ProjectDetail.activity, worklist_Active);
						listview.setAdapter(workComAdp);
					}
				}
			}
		});

		tv_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Prefrences.worklisttypes==2)
				{
					listview.setVisibility(View.VISIBLE);
					Prefrences.worklisttypes=0;
					tv_active.setBackgroundResource(R.drawable.all_white_background);
					tv_completed.setBackgroundResource(R.drawable.complete_white_background);
					tv_assignee.setBackgroundResource(R.color.white);
					tv_location.setBackgroundResource(R.color.white);

					tv_completed.setTextColor(Color.BLACK);
					tv_active.setTextColor(Color.BLACK);
					tv_assignee.setTextColor(Color.BLACK);
					tv_location.setTextColor(Color.BLACK);
					
					workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, punchListItem);
					listview.setAdapter(workComAdp);
				}else
				{
				Prefrences.worklisttypes=2;
				tv_active.setBackgroundResource(R.drawable.all_white_background);
				tv_completed
						.setBackgroundResource(R.drawable.complete_white_background);
				tv_assignee.setBackgroundResource(R.color.white);
				tv_location.setBackgroundResource(R.color.black);

				tv_completed.setTextColor(Color.BLACK);
				tv_active.setTextColor(Color.BLACK);
				tv_assignee.setTextColor(Color.BLACK);
				tv_location.setTextColor(Color.WHITE);

				listview.setVisibility(View.GONE);

				// Show_Dialog2(v);
				if (Prefrences.text == 10) {
					Toast.makeText(getActivity(), "Server Issue",
							Toast.LENGTH_SHORT).show();
				} else {
					Prefrences.text = 6;
					dialog = new Dialog(getActivity(),
							android.R.style.Theme_Translucent_NoTitleBar);
					// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialgllist_safety);
					ArrayList<String> array = new ArrayList<String>();

					Window window = dialog.getWindow();
					WindowManager.LayoutParams wlp = window.getAttributes();

					wlp.gravity = Gravity.CENTER;
					wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
					// wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
					window.setAttributes(wlp);
					array = locss;
					// dialog.setTitle("Location");
					TextView txtwho = (TextView) dialog
							.findViewById(R.id.txt_who);
					txtwho.setText("Location");
					LinearLayout linearLay = (LinearLayout) dialog
							.findViewById(R.id.db);
					RelativeLayout list_outside = (RelativeLayout) dialog
							.findViewById(R.id.list_outside);

					// db.setBackgroundColor(drawable.transparent_image);
					new ASSL(getActivity(), linearLay, 1134, 720, false);
					Button cancel = (Button) dialog.findViewById(R.id.cancel);

					ListView dialoglist = (ListView) dialog
							.findViewById(R.id.dialoglist);
					// Button btn_cancel = (Button)dialog.
					// findViewById(R.id.cancel);

					adapter adp = new adapter(getActivity(), array);
					dialoglist.setAdapter(adp);
					cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});

					list_outside.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});

					dialog.show();
				}
				// create_worklist(body, user_id, location, user_assignee,
				// project_id);
				}
			}
		});

		tv_assignee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Prefrences.worklisttypes==3)
				{
					listview.setVisibility(View.VISIBLE);
					Prefrences.worklisttypes=0;
					tv_active.setBackgroundResource(R.drawable.all_white_background);
					tv_completed.setBackgroundResource(R.drawable.complete_white_background);
					tv_assignee.setBackgroundResource(R.color.white);
					tv_location.setBackgroundResource(R.color.white);

					tv_completed.setTextColor(Color.BLACK);
					tv_active.setTextColor(Color.BLACK);
					tv_assignee.setTextColor(Color.BLACK);
					tv_location.setTextColor(Color.BLACK);
					
					workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, punchListItem);
					listview.setAdapter(workComAdp);
				}else
				{
				Prefrences.worklisttypes=3;
				tv_active.setBackgroundResource(R.drawable.all_white_background);
				tv_completed
						.setBackgroundResource(R.drawable.complete_white_background);
				tv_assignee.setBackgroundResource(R.color.black);
				tv_location.setBackgroundResource(R.color.white);

				tv_completed.setTextColor(Color.BLACK);
				tv_active.setTextColor(Color.BLACK);
				tv_assignee.setTextColor(Color.WHITE);
				tv_location.setTextColor(Color.BLACK);

				listview.setVisibility(View.GONE);
//				if (Prefrences.text == 10) {
//					Toast.makeText(getActivity(), "Server Issue",
//							Toast.LENGTH_SHORT).show();
//				} else {
					Prefrences.text = 7;
					// Show_Dialog2(v);

					dialog = new Dialog(getActivity(),
							android.R.style.Theme_Translucent_NoTitleBar);
					// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialgllist_safety);
					ArrayList<String> array = new ArrayList<String>();

					Window window = dialog.getWindow();
					WindowManager.LayoutParams wlp = window.getAttributes();

					wlp.gravity = Gravity.CENTER;
					wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
					// wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
					window.setAttributes(wlp);
					array = asss;
					// dialog.setTitle("Location");
					RelativeLayout list_outside = (RelativeLayout) dialog
							.findViewById(R.id.list_outside);
					LinearLayout linearLay = (LinearLayout) dialog
							.findViewById(R.id.db);
					// db.setBackgroundColor(drawable.transparent_image);
					new ASSL(getActivity(), linearLay, 1134, 720, false);
					Button cancel = (Button) dialog.findViewById(R.id.cancel);
					TextView txtwho = (TextView) dialog
							.findViewById(R.id.txt_who);
					txtwho.setText("Assignee");
					ListView dialoglist = (ListView) dialog
							.findViewById(R.id.dialoglist);
					// Button btn_cancel = (Button)dialog.
					// findViewById(R.id.cancel);
                  if(array.size()>0){
					adapter adp = new adapter(getActivity(), array);
					dialoglist.setAdapter(adp);
                  }
					cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});

					list_outside.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});

					dialog.show();
//				}
				}
			}
		});

		tv_completed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Prefrences.worklisttypes==4)
				{
					Prefrences.worklisttypes=0;
					tv_active.setBackgroundResource(R.drawable.all_white_background);
					tv_completed.setBackgroundResource(R.drawable.complete_white_background);
					tv_assignee.setBackgroundResource(R.color.white);
					tv_location.setBackgroundResource(R.color.white);

					tv_completed.setTextColor(Color.BLACK);
					tv_active.setTextColor(Color.BLACK);
					tv_assignee.setTextColor(Color.BLACK);
					tv_location.setTextColor(Color.BLACK);
					
					workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, punchListItem);
					listview.setAdapter(workComAdp);
				}else
				{
				Prefrences.worklisttypes=4;
				tv_active.setBackgroundResource(R.drawable.all_white_background);
				tv_location.setBackgroundResource(R.color.white);
				tv_completed
						.setBackgroundResource(R.drawable.complete_black_background);
				tv_assignee.setBackgroundResource(R.color.white);

				tv_completed.setTextColor(Color.WHITE);
				tv_active.setTextColor(Color.BLACK);
				tv_assignee.setTextColor(Color.BLACK);
				tv_location.setTextColor(Color.BLACK);

				listview.setVisibility(View.VISIBLE);
				if (Prefrences.text == 10) {
					Toast.makeText(getActivity(), "Server Issue",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.i("WORK LIST SIZE", "" + worklist_complted.size());

					WorkListCompleteAdapter workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, worklist_complted);
					listview.setAdapter(workComAdp);
				}
				}
			}
		});

		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Prefrences.worklist_bool==false){
			if (isInternetPresent) {
				punchlst();

			} else {
				String response = sharedpref.getString("worklistfragment", "");
				if(response.equalsIgnoreCase("")){
					Toast.makeText(getActivity(),"No internet connection.", Toast.LENGTH_SHORT).show();
				}else{
					fillServerData(response);
				}
			}
			}else{
		
				punchListItem.clear();
				JSONObject res = null;
				try {
					res = new JSONObject(Prefrences.worklist_s);
					Log.v("response ", "" + res.toString(2));
					JSONObject punchlists = null;
					JSONArray punchlistitem = null;

					// if (res.has("punchlist")) {
					// punchlists = res.getJSONObject("punchlist");//
					// chexklist
					// punchlistitem = punchlists
					// .getJSONArray("punchlist_items");
					// } else {
					punchlists = res.getJSONObject("punchlist");
					punchlistitem = punchlists
							.getJSONArray("worklist_items");
					// }

					JSONArray personel = punchlists
							.getJSONArray("personnel");
					worklist_complted = new ArrayList<PunchListsItem>();
					worklist_Active = new ArrayList<PunchListsItem>();
					slectedLocs = new ArrayList<PunchListsItem>();
					ArrayList<String> sub = new ArrayList<String>();
					assignees = new ArrayList<String>();
					locs = new ArrayList<String>();
					for (int i = 0; i < punchlistitem.length(); i++) {
						Log.d("", "i=====" + i);
						JSONObject count = punchlistitem
								.getJSONObject(i);
						if (!count.isNull("sub_assignee")) {
							JSONObject subasigne = count
									.getJSONObject("sub_assignee");
							sub.add((String) subasigne.get("name"));
							assignees.add(subasigne.getString("name"));
						}
						assigne = new ArrayList<Assignee>();
						if (!count.isNull("assignee")) {
							JSONObject asigne = count
									.getJSONObject("assignee");// subcateg
//							Log.i("asigneees", "" + asigne);

							// commnt = new ArrayList<Comments>();
							JSONObject company = asigne
									.getJSONObject("company");// checkitem

							ArrayList<Company> compny = new ArrayList<Company>();
							compny.add(new Company(company
									.getString("id"), company
									.getString("name")));
							assigne.add(new Assignee(asigne
									.getString("id"), asigne
									.getString("first_name"), asigne
									.getString("last_name"), asigne
									.getString("full_name"), asigne
//									.getString("admin"), asigne
//									.getString("company_admin"), asigne
//									.getString("uber_admin"), asigne
									.getString("email"), asigne
									.getString("phone"), asigne
//									.getString("authentication_token"),
//									asigne
									.getString("url_thumb"),
									asigne.getString("url_small"),
									compny));
							assignees.add(asigne.getString("full_name"));
						} else {

							assigne.add(new Assignee( "",
									 "", "", "", "", "", "", "",
									null));
						}

						JSONArray phot = count.getJSONArray("photos");
						punchlistphoto = new ArrayList<PunchListsPhotos>();
						for (int j = 0; j < phot.length(); j++) {
							JSONObject ccount = phot.getJSONObject(j);

							Log.d("", "j=====" + j);
							punchlistphoto.add(new PunchListsPhotos(
									ccount.getString("id"),
									ccount.getString("url_large"),
									ccount.getString("original"),
									ccount.getString("epoch_time"),
									ccount.getString("url_small"),
									ccount.getString("url_thumb"),
									ccount.getString("image_file_size"),
									ccount.getString("image_content_type"),
									ccount.getString("source"), ccount
											.getString("phase"), 
//											ccount
//											.getString("created_at"),
									ccount.getString("user_name"),
									ccount.getString("name"), ccount
											.getString("description"),
									ccount.getString("created_date")));
							// ccount.getString("assignee")

							Log.i("photos", "" + phot);
						}
//						JSONArray comm = count.getJSONArray("comments");

//						for (int k = 0; k < comm.length(); k++) {
	//
//							JSONObject ccount = comm.getJSONObject(k);
	//
//							JSONObject cuser = ccount
//									.getJSONObject("user");
//							Log.i("comment user", "" + cuser);
	//
//							commnt = new ArrayList<Comments>();
//							cusr = new ArrayList<CommentUser>();
//							JSONObject company = cuser
//									.getJSONObject("company");
	//
//							ArrayList<Company> compny = new ArrayList<Company>();
//							compny.add(new Company(company
//									.getString("id"), company
//									.getString("name")));
//							// Log.i("ddddddd", "" + company);
//							cusr.add(new CommentUser(cuser
//									.getString("id"), cuser
//									.getString("first_name"), cuser
//									.getString("last_name"), cuser
//									.getString("full_name"), cuser
////									.getString("admin"), cuser
////									.getString("company_admin"), cuser
////									.getString("uber_admin"), cuser
//									.getString("email"), cuser
//									.getString("phone"), cuser
////									.getString("authentication_token"),
////									cuser
//									.getString("url_thumb"), cuser
//											.getString("url_small"),
//									compny));
	//
//							commnt.add(new Comments(ccount
//									.getString("id"), ccount
//									.getString("body"), cusr, ccount
//									.getString("created_at")));
//							Log.d("comments", "size==" + commnt.size());
//						}
						punchListItem.add(new PunchListsItem(count
								.getString("id"), count
								.getString("body"), assigne, count
						// .getString("sub_assignee"), count
								.getString("location"), count
								.getString("completed_at"), count
								.getString("completed"),
								punchlistphoto, count
										.getString("created_at"),
								commnt, count.getString("epoch_time")));

						if (!count.getString("location").equals("null") && !count.getString("location").equals("")) {
							locs.add(count.getString("location"));
						}
						// if(!count.getString("sub_assignee").equals("null"))
						// assignees.add(
						// count.getString("sub_assignee"));

						Log.d("asssigneeeeeee",
								"sizee" + assignees.size());

						if (punchListItem.get(i).completed
								.equals("true")) {
							worklist_complted.add(new PunchListsItem(
									count.getString("id"), count
											.getString("body"),
									assigne, count
									// .getString("sub_assignee"), count
											.getString("location"),
									count.getString("completed_at"),
									count.getString("completed"),
									punchlistphoto, count
											.getString("created_at"),
									commnt, count
											.getString("epoch_time")));
							Log.i("true",
									"--------Completed-------------"
											+ punchListItem.get(i).body
													.toString());
						} else if (!punchListItem.get(i).completed
								.equals("true")) {
							worklist_Active.add(new PunchListsItem(
									count.getString("id"), count
											.getString("body"),
									assigne, count
									// .getString("sub_assignee"), count
											.getString("location"),
									count.getString("completed_at"),
									count.getString("completed"),
									punchlistphoto, count
											.getString("created_at"),
									commnt, count
											.getString("epoch_time")));
							Log.i("true",
									"--------Active-------------"
											+ punchListItem.get(i).body
													.toString());
						}

					}
					Log.d("loc", "sizeeee" + locs.size());
					Log.d("punchlist_item",
							"Size" + punchListItem.size());
					ArrayList<Company> compny = null;
					for (int m = 0; m < personel.length(); m++) {
						JSONObject count = personel.getJSONObject(m);
						if (!count.isNull("company")) {
							JSONObject cmpny = count
									.getJSONObject("company");

							compny = new ArrayList<Company>();
							compny.add(new Company(cmpny
									.getString("id"), cmpny
									.getString("name")));

							personnel.add(new Personnel(count
									.getString("id"), count
									.getString("first_name"), count
									.getString("last_name"), count
									.getString("full_name"), count
//									.getString("admin"), count
//									.getString("company_admin"), count
//									.getString("uber_admin"), count
									.getString("email"), count
									.getString("phone"), count
//									.getString("authentication_token"),
//									count
									.getString("url_thumb"), count
											.getString("url_small"),
									compny));
						} else {
							personnel.add(new Personnel(count
									.getString("id"), "", "", "", 
									count.getString("email"), count
											.getString("phone"),
									 "", "", null));
						}

					}
					Log.d("punchlist_item", "Size" + personnel.size());

					punchList.add(new PunchList(punchListItem,
							personnel));
					Log.d("punchlist", "--------" + personnel);
					// totalCount = Integer.toString(punchList.size());
					// Log.i("value..",""+punchList.size());
					
					if(Prefrences.worklisttypes==0)
					{
					workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, punchListItem);
					listview.setAdapter(workComAdp);
					}
					else if(Prefrences.worklisttypes==1)
					{
					workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, worklist_Active);
					listview.setAdapter(workComAdp);
					}
					else if(Prefrences.worklisttypes==4)
					{
					workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, worklist_complted);
					listview.setAdapter(workComAdp);
					}
					else if(Prefrences.worklisttypes==2)
					{
						for (int i = 0; i < punchListItem.size(); i++) {
							if (punchListItem.get(i).location.toString()
									.equalsIgnoreCase(
											Prefrences.selected_location.toString())) {
								Log.d("$$$$$$",
										"selsected lovcsa ="
												+ Prefrences.selected_location
												+ ",array"
												+ punchListItem.get(i).location
														.toString());
								slectedLocs.add(punchListItem.get(i));
							}
						workComAdp = new WorkListCompleteAdapter(
								ProjectDetail.activity, slectedLocs);
						listview.setAdapter(workComAdp);
					}
					}
						else if(Prefrences.worklisttypes==3){
							for (int i = 0; i < punchListItem.size(); i++) {
								if (!punchListItem.get(i).assignee.equals("null")) {
									Log.d("ans---",
											"aaagya----"
													+ punchListItem.get(i).id
															.toString());
									if (punchListItem.get(i).assignee.get(0).fullName
											.toString().equalsIgnoreCase(
													Prefrences.selected_location.toString()))// ||
																					// punchListItem.get(i).subAssignee.toString().equals(selected_location.toString())
																					// )
									{
										Log.d("$$$$$$",
												"selsected lovcsa ="
														+ Prefrences.selected_location
														+ ",array"
														+ punchListItem.get(i).assignee
																.get(0).fullName
																.toString());
										slectedLocs.add(punchListItem.get(i));
									}
									workComAdp = new WorkListCompleteAdapter(
											ProjectDetail.activity, slectedLocs);
									listview.setAdapter(workComAdp);
								}
							}
						}
					workComAdp.notifyDataSetChanged();

					loc.addAll(locs);
					ass.addAll(assignees);
					Log.d("loc", "sizeeee" + loc.size());
					Log.d("user", "sizeeee" + ass.size());
					LinkedHashSet<String> listToSet = new LinkedHashSet<String>(
							loc);
					LinkedHashSet<String> listToSet2 = new LinkedHashSet<String>(
							ass);
					locss = new ArrayList<String>(listToSet);
					asss = new ArrayList<String>(listToSet2);
					Log.d("locssss", "sizeeee" + locss.size());
					Log.d("usrs", "sizeeee" + asss.size());

					if (pull == true) {
						pull = false;
						if(Prefrences.worklisttypes==0)
						{
						tv_active.setBackgroundResource(R.drawable.all_white_background);
						tv_completed.setBackgroundResource(R.drawable.complete_white_background);
						tv_assignee.setBackgroundResource(R.color.white);
						tv_location.setBackgroundResource(R.color.white);

						tv_completed.setTextColor(Color.BLACK);
						tv_active.setTextColor(Color.BLACK);
						tv_assignee.setTextColor(Color.BLACK);
						tv_location.setTextColor(Color.BLACK);
						listview.onRefreshComplete();
						}
						else if(Prefrences.worklisttypes==1)
						{
						tv_active.setBackgroundResource(R.drawable.all_black_background);
						tv_completed.setBackgroundResource(R.drawable.complete_white_background);
						tv_assignee.setBackgroundResource(R.color.white);
						tv_location.setBackgroundResource(R.color.white);

						tv_completed.setTextColor(Color.BLACK);
						tv_active.setTextColor(Color.WHITE);
						tv_assignee.setTextColor(Color.BLACK);
						tv_location.setTextColor(Color.BLACK);
						listview.onRefreshComplete();
						}
						else if(Prefrences.worklisttypes==2)
						{
							tv_active.setBackgroundResource(R.drawable.all_white_background);
							tv_completed
									.setBackgroundResource(R.drawable.complete_white_background);
							tv_assignee.setBackgroundResource(R.color.white);
							tv_location.setBackgroundResource(R.color.black);

							tv_completed.setTextColor(Color.BLACK);
							tv_active.setTextColor(Color.BLACK);
							tv_assignee.setTextColor(Color.BLACK);
							tv_location.setTextColor(Color.WHITE);
						listview.onRefreshComplete();
						}
						else if(Prefrences.worklisttypes==3)
						{
							tv_active.setBackgroundResource(R.drawable.all_white_background);
							tv_completed
									.setBackgroundResource(R.drawable.complete_white_background);
							tv_assignee.setBackgroundResource(R.color.black);
							tv_location.setBackgroundResource(R.color.white);

							tv_completed.setTextColor(Color.BLACK);
							tv_active.setTextColor(Color.BLACK);
							tv_assignee.setTextColor(Color.WHITE);
							tv_location.setTextColor(Color.BLACK);
						listview.onRefreshComplete();
						}
						else if(Prefrences.worklisttypes==4)
						{
							tv_active.setBackgroundResource(R.drawable.all_white_background);
							tv_location.setBackgroundResource(R.color.white);
							tv_completed
									.setBackgroundResource(R.drawable.complete_black_background);
							tv_assignee.setBackgroundResource(R.color.white);

							tv_completed.setTextColor(Color.WHITE);
							tv_active.setTextColor(Color.BLACK);
							tv_assignee.setTextColor(Color.BLACK);
							tv_location.setTextColor(Color.BLACK);
						listview.onRefreshComplete();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
		
			}
		Prefrences.selected_location="";
		if(Prefrences.worklisttypes==0)
		{
		tv_active.setBackgroundResource(R.drawable.all_white_background);
		tv_completed.setBackgroundResource(R.drawable.complete_white_background);
		tv_assignee.setBackgroundResource(R.color.white);
		tv_location.setBackgroundResource(R.color.white);

		tv_completed.setTextColor(Color.BLACK);
		tv_active.setTextColor(Color.BLACK);
		tv_assignee.setTextColor(Color.BLACK);
		tv_location.setTextColor(Color.BLACK);
		listview.onRefreshComplete();
		}
		else if(Prefrences.worklisttypes==1)
		{
		tv_active.setBackgroundResource(R.drawable.all_black_background);
		tv_completed.setBackgroundResource(R.drawable.complete_white_background);
		tv_assignee.setBackgroundResource(R.color.white);
		tv_location.setBackgroundResource(R.color.white);

		tv_completed.setTextColor(Color.BLACK);
		tv_active.setTextColor(Color.WHITE);
		tv_assignee.setTextColor(Color.BLACK);
		tv_location.setTextColor(Color.BLACK);
		listview.onRefreshComplete();
		}
		else if(Prefrences.worklisttypes==2)
		{
			tv_active.setBackgroundResource(R.drawable.all_white_background);
			tv_completed
					.setBackgroundResource(R.drawable.complete_white_background);
			tv_assignee.setBackgroundResource(R.color.white);
			tv_location.setBackgroundResource(R.color.black);

			tv_completed.setTextColor(Color.BLACK);
			tv_active.setTextColor(Color.BLACK);
			tv_assignee.setTextColor(Color.BLACK);
			tv_location.setTextColor(Color.WHITE);
		listview.onRefreshComplete();
		}
		else if(Prefrences.worklisttypes==3)
		{
			tv_active.setBackgroundResource(R.drawable.all_white_background);
			tv_completed
					.setBackgroundResource(R.drawable.complete_white_background);
			tv_assignee.setBackgroundResource(R.color.black);
			tv_location.setBackgroundResource(R.color.white);

			tv_completed.setTextColor(Color.BLACK);
			tv_active.setTextColor(Color.BLACK);
			tv_assignee.setTextColor(Color.WHITE);
			tv_location.setTextColor(Color.BLACK);
		listview.onRefreshComplete();
		}
		else if(Prefrences.worklisttypes==4)
		{
			tv_active.setBackgroundResource(R.drawable.all_white_background);
			tv_location.setBackgroundResource(R.color.white);
			tv_completed
					.setBackgroundResource(R.drawable.complete_black_background);
			tv_assignee.setBackgroundResource(R.color.white);

			tv_completed.setTextColor(Color.WHITE);
			tv_active.setTextColor(Color.BLACK);
			tv_assignee.setTextColor(Color.BLACK);
			tv_location.setTextColor(Color.BLACK);
		listview.onRefreshComplete();
		}
		
		if(Prefrences.worklisttypes==0)
		{
		workComAdp = new WorkListCompleteAdapter(
				ProjectDetail.activity, punchListItem);
		listview.setAdapter(workComAdp);
		workComAdp.notifyDataSetChanged();
		}
		else if(Prefrences.worklisttypes==1)
		{
		workComAdp = new WorkListCompleteAdapter(
				ProjectDetail.activity, worklist_Active);
		listview.setAdapter(workComAdp);
		workComAdp.notifyDataSetChanged();
		}
		else if(Prefrences.worklisttypes==4)
		{
		workComAdp = new WorkListCompleteAdapter(
				ProjectDetail.activity, worklist_complted);
		listview.setAdapter(workComAdp);
		workComAdp.notifyDataSetChanged();
		}
		else if(Prefrences.worklisttypes==2)
		{
			slectedLocs = new ArrayList<PunchListsItem>();
			for (int i = 0; i < punchListItem.size(); i++) {
				if (punchListItem.get(i).location.toString()
						.equalsIgnoreCase(
								Prefrences.selected_location.toString())) {
					Log.d("$$$$$$",
							"selsected lovcsa ="
									+ Prefrences.selected_location
									+ ",array"
									+ punchListItem.get(i).location
											.toString());
					slectedLocs.add(punchListItem.get(i));
				}
			workComAdp = new WorkListCompleteAdapter(
					ProjectDetail.activity, slectedLocs);
			listview.setAdapter(workComAdp);
			workComAdp.notifyDataSetChanged();
		}
		}
			else if(Prefrences.worklisttypes==3){
				slectedLocs = new ArrayList<PunchListsItem>();
				for (int i = 0; i < punchListItem.size(); i++) {
					if (!punchListItem.get(i).assignee.equals("null")) {
						Log.d("ans---",
								"aaagya----"
										+ punchListItem.get(i).id
												.toString());
						if (punchListItem.get(i).assignee.get(0).fullName
								.toString().equalsIgnoreCase(
										Prefrences.selected_location.toString()))// ||
																		// punchListItem.get(i).subAssignee.toString().equals(selected_location.toString())
																		// )
						{
							Log.d("$$$$$$",
									"selsected lovcsa ="
											+ Prefrences.selected_location
											+ ",array"
											+ punchListItem.get(i).assignee
													.get(0).fullName
													.toString());
							slectedLocs.add(punchListItem.get(i));
						}
						workComAdp = new WorkListCompleteAdapter(
								ProjectDetail.activity, slectedLocs);
						listview.setAdapter(workComAdp);
					}
					
				}
			}
		
	}
	public class adapter extends BaseAdapter {

		ArrayList<String> array;
		// ArrayList<Users>usr;
		Context con;

		public adapter(Context con, ArrayList<String> array) {
			// TODO Auto-generated constructor stub
			this.array = array;
			this.con = con;
			// this.usr=usrs;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// Log.d("userlist","Size========="+array.size());
			// if()
			return array.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return array.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			viewholder holder;
			// final Users body = (Users) this.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new viewholder();
				// LayoutInflater inflater =
				// ((Activity)con).getLayoutInflater();
				LayoutInflater inflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.dialog2list_item, null);
				holder.linear = (LinearLayout) convertView
						.findViewById(R.id.dialoglinear);
				holder.linear.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 80));
				ASSL.DoMagic(holder.linear);
				convertView.setTag(holder);
			} else {
				holder = (viewholder) convertView.getTag();
			}

			holder.txtview = (TextView) convertView.findViewById(R.id.array);
			holder.txtview.setTypeface(Prefrences
					.helveticaNeuelt(getActivity()));
			holder.txtview.setText(array.get(position).toString());

			holder.txtview.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub

					// Prefrences.selectedlocation =
					// array.get(position).uFullName.toString();
					// Toast.makeText(c, ""+Prefrences.selectedlocation,
					// Toast.LENGTH_SHORT).show();
					// if(Prefrences.text==6)
					// WorkItemClick.btnS_location.setText(""+array.get(position).toString());
					// else if(Prefrences.text==4)
					// WorkItemClick.btnS_assigned.setText(""+array.get(position).toString());
					Prefrences.selected_location = array.get(position).toString();
					slectedLocs = new ArrayList<PunchListsItem>();
					if (Prefrences.text == 6) {
						for (int i = 0; i < punchListItem.size(); i++) {
							if (punchListItem.get(i).location.toString()
									.equalsIgnoreCase(
											Prefrences.selected_location.toString())) {
								Log.d("$$$$$$",
										"selsected lovcsa ="
												+ Prefrences.selected_location
												+ ",array"
												+ punchListItem.get(i).location
														.toString());
								slectedLocs.add(punchListItem.get(i));
							}
						}
					} else if (Prefrences.text == 7) {
						for (int i = 0; i < punchListItem.size(); i++) {
							if (!punchListItem.get(i).assignee.equals("null")) {
								Log.d("ans---",
										"aaagya----"
												+ punchListItem.get(i).id
														.toString());
								if (punchListItem.get(i).assignee.get(0).fullName
										.toString().equalsIgnoreCase(
												Prefrences.selected_location.toString()))// ||
																				// punchListItem.get(i).subAssignee.toString().equals(selected_location.toString())
																				// )
								{
									Log.d("$$$$$$",
											"selsected lovcsa ="
													+ Prefrences.selected_location
													+ ",array"
													+ punchListItem.get(i).assignee
															.get(0).fullName
															.toString());
									slectedLocs.add(punchListItem.get(i));
								}
							}
						}
					}
					listview.setVisibility(View.VISIBLE);
					workComAdp = new WorkListCompleteAdapter(
							ProjectDetail.activity, slectedLocs);
					listview.setAdapter(workComAdp);

					dialog.dismiss();
				}

				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				//
				// }
			});
			return convertView;
		}

	}

	private static class viewholder {
		TextView txtview;
		LinearLayout linear;
	}

	public void Show_Dialog2(View v) {
		DialogBox2 cdd = new DialogBox2(getActivity());
		Window window = cdd.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.BOTTOM;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(wlp);
		cdd.show();
	}

	// ************ API for Punch list Details. *************//

	public void punchlst() {

		if(pull!=true)
		Prefrences.showLoadingDialog(getActivity(), "Loading...");

		RequestParams params = new RequestParams();

		params.put("id", Prefrences.selectedProId);

		Log.d("----------------","---------------"+Prefrences.url + "/worklists/" + Prefrences.selectedProId);
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(100000);
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.get(Prefrences.url + "/worklists/" + Prefrences.selectedProId,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						
						Editor editor = sharedpref.edit();
						editor.putString("worklistfragment", response);
						editor.commit();
						fillServerData(response);
						if(pull!=true)
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Toast.makeText(getActivity(), "Server Issue",
								Toast.LENGTH_LONG).show();

						Prefrences.text = 10;
						if(pull!=true)
						Prefrences.dismissLoadingDialog();
						if (pull == true) {
							pull = false;
							
							listview.onRefreshComplete();
						}

					}
				});

	}
	
	public void fillServerData(String response){
		
		punchListItem.clear();
		JSONObject res = null;
		try {
			res = new JSONObject(response);
			Prefrences.worklist_bool=true;
			Prefrences.worklist_s=response;
			Log.v("response ", "" + res.toString(2));
			JSONObject punchlists = null;
			JSONArray punchlistitem = null;

			// if (res.has("punchlist")) {
			// punchlists = res.getJSONObject("punchlist");//
			// chexklist
			// punchlistitem = punchlists
			// .getJSONArray("punchlist_items");
			// } else {
			punchlists = res.getJSONObject("punchlist");
			punchlistitem = punchlists
					.getJSONArray("worklist_items");
			// }

			JSONArray personel = punchlists
					.getJSONArray("personnel");
			worklist_complted = new ArrayList<PunchListsItem>();
			worklist_Active = new ArrayList<PunchListsItem>();
			slectedLocs = new ArrayList<PunchListsItem>();
			ArrayList<String> sub = new ArrayList<String>();
			assignees = new ArrayList<String>();
			locs = new ArrayList<String>();
			for (int i = 0; i < punchlistitem.length(); i++) {
				Log.d("", "i=====" + i);
				JSONObject count = punchlistitem
						.getJSONObject(i);
				if (!count.isNull("sub_assignee")) {
					JSONObject subasigne = count
							.getJSONObject("sub_assignee");
					sub.add((String) subasigne.get("name"));
					assignees.add(subasigne.getString("name"));
				}
				assigne = new ArrayList<Assignee>();
				if (!count.isNull("assignee")) {
					JSONObject asigne = count
							.getJSONObject("assignee");// subcateg
//					Log.i("asigneees", "" + asigne);

					// commnt = new ArrayList<Comments>();
					JSONObject company = asigne
							.getJSONObject("company");// checkitem

					ArrayList<Company> compny = new ArrayList<Company>();
					compny.add(new Company(company
							.getString("id"), company
							.getString("name")));
					assigne.add(new Assignee(asigne
							.getString("id"), asigne
							.getString("first_name"), asigne
							.getString("last_name"), asigne
							.getString("full_name"), asigne
//							.getString("admin"), asigne
//							.getString("company_admin"), asigne
//							.getString("uber_admin"), asigne
							.getString("email"), asigne
							.getString("phone"), asigne
//							.getString("authentication_token"),
//							asigne
							
							.getString("url_thumb"),
							asigne.getString("url_small"),
							compny));
					assignees.add(asigne.getString("full_name"));
				} else {

					assigne.add(new Assignee("", "", "", "", "", "", "", "",null));
				}
				//****************************** API change ****************************

				JSONArray phot = count.getJSONArray("photos");
				punchlistphoto = new ArrayList<PunchListsPhotos>();
				for (int j = 0; j < phot.length(); j++) {
					JSONObject ccount = phot.getJSONObject(j);

					Log.d("", "j=====" + j);
					punchlistphoto.add(new PunchListsPhotos(
							ccount.getString("id"),
							ccount.getString("url_large"),
							ccount.getString("original"),
							ccount.getString("epoch_time"),
							ccount.getString("url_small"),
							ccount.getString("url_thumb"),
							ccount.getString("image_file_size"),
							ccount.getString("image_content_type"),
							ccount.getString("source"), ccount
									.getString("phase"),
//									ccount
//									.getString("created_at"),
							ccount.getString("user_name"),
							ccount.getString("name"), ccount
									.getString("description"),
							ccount.getString("created_date")));
					// ccount.getString("assignee")

					Log.i("photos", "" + phot);
				}
//				JSONArray comm = count.getJSONArray("comments");
//
//				for (int k = 0; k < comm.length(); k++) {
//
//					JSONObject ccount = comm.getJSONObject(k);
//
//					JSONObject cuser = ccount
//							.getJSONObject("user");
//					Log.i("comment user", "" + cuser);
//
//					commnt = new ArrayList<Comments>();
//					cusr = new ArrayList<CommentUser>();
//					JSONObject company = cuser
//							.getJSONObject("company");
//
//					ArrayList<Company> compny = new ArrayList<Company>();
//					compny.add(new Company(company
//							.getString("id"), company
//							.getString("name")));
//					// Log.i("ddddddd", "" + company);
//					cusr.add(new CommentUser(cuser
//							.getString("id"), cuser
//							.getString("first_name"), cuser
//							.getString("last_name"), cuser
//							.getString("full_name"), cuser
////							.getString("admin"), cuser
////							.getString("company_admin"), cuser
////							.getString("uber_admin"), cuser
//							.getString("email"), cuser
//							.getString("phone"), cuser
////							.getString("authentication_token"),
////							cuser
//							.getString("url_thumb"), cuser
//									.getString("url_small"),
//							compny));
//
//					commnt.add(new Comments(ccount
//							.getString("id"), ccount
//							.getString("body"), cusr, ccount
//							.getString("created_at")));
//					Log.d("comments", "size==" + commnt.size());
//				}
				punchListItem.add(new PunchListsItem(count
						.getString("id"), count
						.getString("body"), assigne, count
				// .getString("sub_assignee"), count
						.getString("location"), count
						.getString("completed_at"), count
						.getString("completed"),
						punchlistphoto, count
								.getString("created_at"),
						commnt, count.getString("epoch_time")));

				if (!count.getString("location").equals("null") && !count.getString("location").equals("")) {
					locs.add(count.getString("location"));
				}
				// if(!count.getString("sub_assignee").equals("null"))
				// assignees.add(
				// count.getString("sub_assignee"));

				Log.d("asssigneeeeeee",
						"sizee" + assignees.size());

				if (punchListItem.get(i).completed
						.equals("true")) {
					worklist_complted.add(new PunchListsItem(
							count.getString("id"), count
									.getString("body"),
							assigne, count
							// .getString("sub_assignee"), count
									.getString("location"),
							count.getString("completed_at"),
							count.getString("completed"),
							punchlistphoto, count
									.getString("created_at"),
							commnt, count
									.getString("epoch_time")));
					Log.i("true",
							"--------Completed-------------"
									+ punchListItem.get(i).body
											.toString());
				} else if (!punchListItem.get(i).completed
						.equals("true")) {
					worklist_Active.add(new PunchListsItem(
							count.getString("id"), count
									.getString("body"),
							assigne, count
							// .getString("sub_assignee"), count
									.getString("location"),
							count.getString("completed_at"),
							count.getString("completed"),
							punchlistphoto, count
									.getString("created_at"),
							commnt, count
									.getString("epoch_time")));
					Log.i("true",
							"--------Active-------------"
									+ punchListItem.get(i).body
											.toString());
				}

			}
			Log.d("loc", "sizeeee" + locs.size());
			Log.d("punchlist_item",
					"Size" + punchListItem.size());
			ArrayList<Company> compny = null;
			for (int m = 0; m < personel.length(); m++) {
				JSONObject count = personel.getJSONObject(m);
				if (!count.isNull("company")) {
					JSONObject cmpny = count
							.getJSONObject("company");

					compny = new ArrayList<Company>();
					compny.add(new Company(cmpny
							.getString("id"), cmpny
							.getString("name")));

					personnel.add(new Personnel(count
							.getString("id"), count
							.getString("first_name"), count
							.getString("last_name"), count
							.getString("full_name"), count
//							.getString("admin"), count
//							.getString("company_admin"), count
//							.getString("uber_admin"), count
							.getString("email"), count
							.getString("phone"), count
//							.getString("authentication_token"),
//							count
							.getString("url_thumb"), count
									.getString("url_small"),
							compny));
				} else {
					personnel.add(new Personnel(count
							.getString("id"), "", "", "", count.getString("email"), count
									.getString("phone"), 
							 "", "", null));
				}

			}
			Log.d("punchlist_item", "Size" + personnel.size());

			punchList.add(new PunchList(punchListItem,
					personnel));
			Log.d("punchlist", "--------" + personnel);
			// totalCount = Integer.toString(punchList.size());
			// Log.i("value..",""+punchList.size());
			if(Prefrences.worklisttypes==0)
			{
			workComAdp = new WorkListCompleteAdapter(
					ProjectDetail.activity, punchListItem);
			listview.setAdapter(workComAdp);
			}
			else if(Prefrences.worklisttypes==1)
			{
			workComAdp = new WorkListCompleteAdapter(
					ProjectDetail.activity, worklist_Active);
			listview.setAdapter(workComAdp);
			}
			else if(Prefrences.worklisttypes==4)
			{
			workComAdp = new WorkListCompleteAdapter(
					ProjectDetail.activity, worklist_complted);
			listview.setAdapter(workComAdp);
			}
			else if(Prefrences.worklisttypes==2)
			{
				for (int i = 0; i < punchListItem.size(); i++) {
					if (punchListItem.get(i).location.toString()
							.equalsIgnoreCase(
									Prefrences.selected_location.toString())) {
						Log.d("$$$$$$",
								"selsected lovcsa ="
										+ Prefrences.selected_location
										+ ",array"
										+ punchListItem.get(i).location
												.toString());
						slectedLocs.add(punchListItem.get(i));
					}
				workComAdp = new WorkListCompleteAdapter(
						ProjectDetail.activity, slectedLocs);
				listview.setAdapter(workComAdp);
			}
			}
				else if(Prefrences.worklisttypes==3){
					for (int i = 0; i < punchListItem.size(); i++) {
						if (!punchListItem.get(i).assignee.equals("null")) {
							Log.d("ans---",
									"aaagya----"
											+ punchListItem.get(i).id
													.toString());
							if (punchListItem.get(i).assignee.get(0).fullName
									.toString().equalsIgnoreCase(
											Prefrences.selected_location.toString()))// ||
																			// punchListItem.get(i).subAssignee.toString().equals(selected_location.toString())
																			// )
							{
								Log.d("$$$$$$",
										"selsected lovcsa ="
												+ Prefrences.selected_location
												+ ",array"
												+ punchListItem.get(i).assignee
														.get(0).fullName
														.toString());
								slectedLocs.add(punchListItem.get(i));
							}
							workComAdp = new WorkListCompleteAdapter(
									ProjectDetail.activity, slectedLocs);
							listview.setAdapter(workComAdp);
						}
						
					}
				}

			loc.addAll(locs);
			ass.addAll(assignees);
			Log.d("loc", "sizeeee" + loc.size());
			Log.d("user", "sizeeee" + ass.size());
			LinkedHashSet<String> listToSet = new LinkedHashSet<String>(
					loc);
			LinkedHashSet<String> listToSet2 = new LinkedHashSet<String>(
					ass);
			locss = new ArrayList<String>(listToSet);
			asss = new ArrayList<String>(listToSet2);
			Log.d("locssss", "sizeeee" + locss.size());
			Log.d("usrs", "sizeeee" + asss.size());

			if (pull == true) {
				pull = false;
				if(Prefrences.worklisttypes==0)
				{
				tv_active.setBackgroundResource(R.drawable.all_white_background);
				tv_completed.setBackgroundResource(R.drawable.complete_white_background);
				tv_assignee.setBackgroundResource(R.color.white);
				tv_location.setBackgroundResource(R.color.white);

				tv_completed.setTextColor(Color.BLACK);
				tv_active.setTextColor(Color.BLACK);
				tv_assignee.setTextColor(Color.BLACK);
				tv_location.setTextColor(Color.BLACK);
				listview.onRefreshComplete();
				}
				else if(Prefrences.worklisttypes==1)
				{
				tv_active.setBackgroundResource(R.drawable.all_black_background);
				tv_completed.setBackgroundResource(R.drawable.complete_white_background);
				tv_assignee.setBackgroundResource(R.color.white);
				tv_location.setBackgroundResource(R.color.white);

				tv_completed.setTextColor(Color.BLACK);
				tv_active.setTextColor(Color.WHITE);
				tv_assignee.setTextColor(Color.BLACK);
				tv_location.setTextColor(Color.BLACK);
				listview.onRefreshComplete();
				}
				else if(Prefrences.worklisttypes==2)
				{
					tv_active.setBackgroundResource(R.drawable.all_white_background);
					tv_completed
							.setBackgroundResource(R.drawable.complete_white_background);
					tv_assignee.setBackgroundResource(R.color.white);
					tv_location.setBackgroundResource(R.color.black);

					tv_completed.setTextColor(Color.BLACK);
					tv_active.setTextColor(Color.BLACK);
					tv_assignee.setTextColor(Color.BLACK);
					tv_location.setTextColor(Color.WHITE);
				listview.onRefreshComplete();
				}
				else if(Prefrences.worklisttypes==3)
				{
					tv_active.setBackgroundResource(R.drawable.all_white_background);
					tv_completed
							.setBackgroundResource(R.drawable.complete_white_background);
					tv_assignee.setBackgroundResource(R.color.black);
					tv_location.setBackgroundResource(R.color.white);

					tv_completed.setTextColor(Color.BLACK);
					tv_active.setTextColor(Color.BLACK);
					tv_assignee.setTextColor(Color.WHITE);
					tv_location.setTextColor(Color.BLACK);
				listview.onRefreshComplete();
				}
				else if(Prefrences.worklisttypes==4)
				{
					tv_active.setBackgroundResource(R.drawable.all_white_background);
					tv_location.setBackgroundResource(R.color.white);
					tv_completed
							.setBackgroundResource(R.drawable.complete_black_background);
					tv_assignee.setBackgroundResource(R.color.white);

					tv_completed.setTextColor(Color.WHITE);
					tv_active.setTextColor(Color.BLACK);
					tv_assignee.setTextColor(Color.BLACK);
					tv_location.setTextColor(Color.BLACK);
				listview.onRefreshComplete();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}