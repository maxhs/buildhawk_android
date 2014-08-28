package com.buildhawk;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.json.JSONArray;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.Company;
import com.buildhawk.utils.ProjectPhotos;
import com.buildhawk.utils.Users;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
/*
 *  It filters the photos in the project and show them in a list.
 * 
 */
public class DocumentFragment extends Fragment {

	PullToRefreshListView pullToRefreshListView;
	ConnectionDetector connDect;
	Boolean isInternetPresentBoolean = false;
	Boolean pullBoolean = false;
	public static ArrayList<ProjectPhotos> photosListArrayList = new ArrayList<ProjectPhotos>();
	public static ArrayList<ProjectPhotos> photosList2ArrayList = new ArrayList<ProjectPhotos>();
	public static ArrayList<ProjectPhotos> photosList3ArrayList = new ArrayList<ProjectPhotos>();
	public static ArrayList<ProjectPhotos> photosList4ArrayList = new ArrayList<ProjectPhotos>();
	public static ArrayList<ProjectPhotos> photosList5ArrayList = new ArrayList<ProjectPhotos>();
	public static ArrayList<String> proDocImgArrayList = new ArrayList<String>();
	public static ArrayList<String> checklistImgArrayList = new ArrayList<String>();
	public static ArrayList<String> worklistImgArrayList = new ArrayList<String>();
	public static ArrayList<String> reportsImgArrayList = new ArrayList<String>();

	public ArrayList<String> allUsersArrayList;// = new ArrayList<String>();
	public ArrayList<String> docUsernameArrayList;
	public ArrayList<String> reportUsernameArrayList;// = new ArrayList<String>();
	public ArrayList<String> worklistUsernameArrayList;
	public ArrayList<String> checklistUsernameArrayList;

	public ArrayList<String> allDatesArrayList;// = new ArrayList<String>();
	public ArrayList<String> docDatesArrayList;// = new ArrayList<String>();
	public ArrayList<String> reportDatesArrayList;// = new ArrayList<String>();
	public ArrayList<String> worklistDatesArrayList;// = new ArrayList<String>();
	public ArrayList<String> checklistDatesArrayList;// = new ArrayList<String>();

	public ArrayList<String> allPhaseArrayList;// = new ArrayList<String>();
	public ArrayList<String> docPhaseArrayList;// = new ArrayList<String>();
	public ArrayList<String> reportPhaseArrayList;// = new ArrayList<String>();
	public ArrayList<String> worklistPhaseArrayList;// = new ArrayList<String>();
	public ArrayList<String> checklistPhaseArrayList;// = new ArrayList<String>();
	
	public static ArrayList<String> ProDocFold;
	public static ArrayList<String> ProDocFolders;
	String totalCountString;
	RelativeLayout relativelayoutRoot;
	Activity activity;
	SharedPreferences sharedpref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.documents, container, false);

		relativelayoutRoot = (RelativeLayout) root.findViewById(R.id.relativelayoutRoot);
		new ASSL(getActivity(), relativelayoutRoot, 1134, 720, false);
		pullToRefreshListView = (PullToRefreshListView) root.findViewById(R.id.pullToRefreshListView);
		// act=ProjectDetail.this;
		sharedpref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for
																		// private
																		// mode
		if(Prefrences.selectedProId.equalsIgnoreCase(Prefrences.LastSelectedProId))
		{
			if(!Prefrences.LastDocument_s.equalsIgnoreCase(""))
//				Prefrences.document_bool = true;
//			else
			{
				Prefrences.document_s=Prefrences.LastDocument_s;			
				Prefrences.document_bool = true;
			}
		}
		else
		{
			if (Prefrences.document_s.equalsIgnoreCase("")) {
				Prefrences.document_bool = false;
			}
		}
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub

				if (isInternetPresentBoolean) {
					pullBoolean = true;
//					pullToRefreshListView.onRefreshComplete();
					projectPhotos();
				} else {
					String response,projectId;
					response = sharedpref.getString("documentlistfragment", "");
//					projectId = sharedpref.getString("projectId", "");
					if (response.equalsIgnoreCase("") ) {//&& projectId.equalsIgnoreCase("")) {
						Toast.makeText(getActivity(),
								"No internet connection.", Toast.LENGTH_SHORT)
								.show();
					} else {
						fillServerData(response);
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
		connDect = new ConnectionDetector(getActivity());
		isInternetPresentBoolean = connDect.isConnectingToInternet();
		if (Prefrences.stopingHit == 1) {
			Prefrences.stopingHit = 0;

			if (Prefrences.document_bool == false ) {
				
				if (isInternetPresentBoolean) {

					projectPhotos();
				} else {
					String response,projectId;
					response = sharedpref.getString("documentlistfragment", "");
//					projectId = sharedpref.getString("projectId", "");
					if (response.equalsIgnoreCase("") ) {//&& projectId.equalsIgnoreCase("")) {
						Toast.makeText(getActivity(),
								"No internet connection.", Toast.LENGTH_SHORT)
								.show();
					}
					else {
						fillServerData(response);
					}
				}

			} else {

				JSONObject res = null;
				try {

					res = new JSONObject(Prefrences.document_s);

					photosListArrayList.clear();
					photosList2ArrayList.clear();
					photosList3ArrayList.clear();
					photosList4ArrayList.clear();
					photosList5ArrayList.clear();
					proDocImgArrayList.clear();
					checklistImgArrayList.clear();
					worklistImgArrayList.clear();
					reportsImgArrayList.clear();
					allUsersArrayList = new ArrayList<String>();
					reportUsernameArrayList = new ArrayList<String>();
					worklistUsernameArrayList = new ArrayList<String>();
					checklistUsernameArrayList = new ArrayList<String>();
					docUsernameArrayList = new ArrayList<String>();
					allDatesArrayList = new ArrayList<String>();
					docDatesArrayList = new ArrayList<String>();
					reportDatesArrayList = new ArrayList<String>();
					worklistDatesArrayList = new ArrayList<String>();
					checklistDatesArrayList = new ArrayList<String>();
					allPhaseArrayList = new ArrayList<String>();
					docPhaseArrayList = new ArrayList<String>();
					reportPhaseArrayList = new ArrayList<String>();
					worklistPhaseArrayList = new ArrayList<String>();
					checklistPhaseArrayList = new ArrayList<String>();

					Log.v("response ", "" + res.toString(2));
					JSONArray photos = res.getJSONArray("photos");

					for (int i = 0; i < photos.length(); i++) {
						JSONObject count = photos.getJSONObject(i);
						if(count.has("folder"))
						{

							JSONObject uCompany = count
									.getJSONObject("folder");
							
//							userArrayList.add(new Users(
//									uCount.getString("id"),
//									uCount.getString("first_name"),
//									uCount.getString("last_name"),
//									uCount.getString("full_name"),
//									uCount.getString("email"),
//									uCount.getString("formatted_phone"),
////									uCount.getString("authentication_token"),
//									uCount.getString("url_thumb"),
//									uCount.getString("url_small"),
//									new Company(uCompany
//											.getString("id"), uCompany
//											.getString("name"))));

							photosListArrayList.add(new ProjectPhotos(count.getString("id"), count
									.getString("url_large"), count.getString("original"),
									count.getString("url_small"), count
											.getString("url_thumb"), count
											.getString("image_file_size"), count
											.getString("image_content_type"), count
											.getString("source"), count.getString("phase"),
									count.getString("created_at"), count
											.getString("user_name"), count
											.getString("name"), count
											.getString("description"), count
											.getString("date_string"),new Company(uCompany
													.getString("id"), uCompany
													.getString("name"))));

							Log.d("----description----",
									"----description----" + count.getString("description")
											+ " name" + count.getString("name"));
							if (count.getString("user_name").equals("null")) {
								allUsersArrayList.add("others(null)");
							} else {
								allUsersArrayList.add(count.getString("user_name"));
							}

							if (count.getString("phase").equals("null")) {
								allPhaseArrayList.add("others_phase(null)");
							} else {
								allPhaseArrayList.add(count.getString("phase"));
							}

							allDatesArrayList.add(count.getString("date_string"));
							

							if (photosListArrayList.get(i).source.equalsIgnoreCase("Documents")) {
								proDocImgArrayList.add(count.getString("source"));
								photosList2ArrayList.add(new ProjectPhotos(count.getString("id"),
										count.getString("url_large"), count
												.getString("original"), count
												.getString("url_small"), count
												.getString("url_thumb"), count
												.getString("image_file_size"), count
												.getString("image_content_type"), count
												.getString("source"), count
												.getString("phase"), count
												.getString("created_at"), count
												.getString("user_name"), count
												.getString("name"), count
												.getString("description"), count
												.getString("date_string"),new Company(uCompany
														.getString("id"), uCompany
														.getString("name"))
												));
								if (count.getString("user_name").equals("null")) {
									docUsernameArrayList.add("others(null)");
								} else {
									docUsernameArrayList.add(count.getString("user_name"));
								}
								if (count.getString("phase").equals("null")) {
									docPhaseArrayList.add("others_phase(null)");
								} else {
									docPhaseArrayList.add(count.getString("phase"));
								}

								docDatesArrayList.add(count.getString("date_string"));
								
								ProDocFold.add(photosListArrayList.get(i).folder.coName.toString());
//								loc.addAll(WorklistFragment.locs);
								
								
//							
								
								
							} else if (photosListArrayList.get(i).source
									.equalsIgnoreCase("Checklist")) {

								checklistImgArrayList.add(count.getString("source"));
								photosList3ArrayList.add(new ProjectPhotos(count.getString("id"),
										count.getString("url_large"), count
												.getString("original"), count
												.getString("url_small"), count
												.getString("url_thumb"), count
												.getString("image_file_size"), count
												.getString("image_content_type"), count
												.getString("source"), count
												.getString("phase"), count
												.getString("created_at"), count
												.getString("user_name"), count
												.getString("name"), count
												.getString("description"), count
												.getString("date_string"),new Company(uCompany
														.getString("id"), uCompany
														.getString("name"))));
								if (count.getString("user_name").equals("null")) {
									checklistUsernameArrayList.add("others(null)");
								} else {
									checklistUsernameArrayList.add(count.getString("user_name"));
								}

								if (count.getString("phase").equals("null")) {
									checklistPhaseArrayList.add("others_phase(null)");
								} else {
									checklistPhaseArrayList.add(count.getString("phase"));
								}

								checklistDatesArrayList.add(count.getString("date_string"));
							} else if (photosListArrayList.get(i).source
									.equalsIgnoreCase("Worklist")) {
								worklistImgArrayList.add(count.getString("source"));
								photosList4ArrayList.add(new ProjectPhotos(count.getString("id"),
										count.getString("url_large"), count
												.getString("original"), count
												.getString("url_small"), count
												.getString("url_thumb"), count
												.getString("image_file_size"), count
												.getString("image_content_type"), count
												.getString("source"), count
												.getString("phase"), count
												.getString("created_at"), count
												.getString("user_name"), count
												.getString("name"), count
												.getString("description"), count
												.getString("date_string"),new Company(uCompany
														.getString("id"), uCompany
														.getString("name"))));
								if (count.getString("user_name").equals("null")) {
									worklistUsernameArrayList.add("others(null)");
								} else {
									worklistUsernameArrayList.add(count.getString("user_name"));
								}

								if (count.getString("phase").equals("null")) {
									worklistPhaseArrayList.add("others_phase(null)");
								} else {
									worklistPhaseArrayList.add(count.getString("phase"));
								}

								worklistDatesArrayList.add(count.getString("date_string"));
							} else if (photosListArrayList.get(i).source.equalsIgnoreCase("Reports")) {
								reportsImgArrayList.add(count.getString("source"));
								photosList5ArrayList.add(new ProjectPhotos(count.getString("id"),
										count.getString("url_large"), count
												.getString("original"), count
												.getString("url_small"), count
												.getString("url_thumb"), count
												.getString("image_file_size"), count
												.getString("image_content_type"), count
												.getString("source"), count
												.getString("phase"), count
												.getString("created_at"), count
												.getString("user_name"), count
												.getString("name"), count
												.getString("description"), count
												.getString("date_string"),new Company(uCompany
														.getString("id"), uCompany
														.getString("name"))));
								if (count.getString("user_name").equals("null")) {
									reportUsernameArrayList.add("others(null)");
								} else {
									reportUsernameArrayList.add(count.getString("user_name"));
								}

								if (count.getString("phase").equals("null")) {
									reportPhaseArrayList.add("others_phase(null)");
								} else {
									reportPhaseArrayList.add(count.getString("phase"));
								}

								reportDatesArrayList.add(count.getString("date_string"));
							}
						
							
						}	
						else
						{
							photosListArrayList.add(new ProjectPhotos(count.getString("id"), count
									.getString("url_large"), count.getString("original"),
									count.getString("url_small"), count
											.getString("url_thumb"), count
											.getString("image_file_size"), count
											.getString("image_content_type"), count
											.getString("source"), count.getString("phase"),
									count.getString("created_at"), count
											.getString("user_name"), count
											.getString("name"), count
											.getString("description"), count
											.getString("date_string"),new Company("","")));

							Log.d("----description----",
									"----description----" + count.getString("description")
											+ " name" + count.getString("name"));
							if (count.getString("user_name").equals("null")) {
								allUsersArrayList.add("others(null)");
							} else {
								allUsersArrayList.add(count.getString("user_name"));
							}

							if (count.getString("phase").equals("null")) {
								allPhaseArrayList.add("others_phase(null)");
							} else {
								allPhaseArrayList.add(count.getString("phase"));
							}

							allDatesArrayList.add(count.getString("date_string"));
							

							if (photosListArrayList.get(i).source.equalsIgnoreCase("Documents")) {
								proDocImgArrayList.add(count.getString("source"));
								photosList2ArrayList.add(new ProjectPhotos(count.getString("id"),
										count.getString("url_large"), count
												.getString("original"), count
												.getString("url_small"), count
												.getString("url_thumb"), count
												.getString("image_file_size"), count
												.getString("image_content_type"), count
												.getString("source"), count
												.getString("phase"), count
												.getString("created_at"), count
												.getString("user_name"), count
												.getString("name"), count
												.getString("description"), count
												.getString("date_string"),new Company("","")
												));
								if (count.getString("user_name").equals("null")) {
									docUsernameArrayList.add("others(null)");
								} else {
									docUsernameArrayList.add(count.getString("user_name"));
								}
								if (count.getString("phase").equals("null")) {
									docPhaseArrayList.add("others_phase(null)");
								} else {
									docPhaseArrayList.add(count.getString("phase"));
								}

								docDatesArrayList.add(count.getString("date_string"));
								
//								ProDocFold.add("","");
////								loc.addAll(WorklistFragment.locs);
//								
//								LinkedHashSet<String> listToSet = new LinkedHashSet<String>(
//										ProDocFold);
//								
//								ProDocFolders = new ArrayList<String>(listToSet);
								
								
								
								
								
							} else if (photosListArrayList.get(i).source
									.equalsIgnoreCase("Checklist")) {

								checklistImgArrayList.add(count.getString("source"));
								photosList3ArrayList.add(new ProjectPhotos(count.getString("id"),
										count.getString("url_large"), count
												.getString("original"), count
												.getString("url_small"), count
												.getString("url_thumb"), count
												.getString("image_file_size"), count
												.getString("image_content_type"), count
												.getString("source"), count
												.getString("phase"), count
												.getString("created_at"), count
												.getString("user_name"), count
												.getString("name"), count
												.getString("description"), count
												.getString("date_string"),new Company("","")));
								if (count.getString("user_name").equals("null")) {
									checklistUsernameArrayList.add("others(null)");
								} else {
									checklistUsernameArrayList.add(count.getString("user_name"));
								}

								if (count.getString("phase").equals("null")) {
									checklistPhaseArrayList.add("others_phase(null)");
								} else {
									checklistPhaseArrayList.add(count.getString("phase"));
								}

								checklistDatesArrayList.add(count.getString("date_string"));
							} else if (photosListArrayList.get(i).source
									.equalsIgnoreCase("Worklist")) {
								worklistImgArrayList.add(count.getString("source"));
								photosList4ArrayList.add(new ProjectPhotos(count.getString("id"),
										count.getString("url_large"), count
												.getString("original"), count
												.getString("url_small"), count
												.getString("url_thumb"), count
												.getString("image_file_size"), count
												.getString("image_content_type"), count
												.getString("source"), count
												.getString("phase"), count
												.getString("created_at"), count
												.getString("user_name"), count
												.getString("name"), count
												.getString("description"), count
												.getString("date_string"),new Company("","")));
								if (count.getString("user_name").equals("null")) {
									worklistUsernameArrayList.add("others(null)");
								} else {
									worklistUsernameArrayList.add(count.getString("user_name"));
								}

								if (count.getString("phase").equals("null")) {
									worklistPhaseArrayList.add("others_phase(null)");
								} else {
									worklistPhaseArrayList.add(count.getString("phase"));
								}

								worklistDatesArrayList.add(count.getString("date_string"));
							} else if (photosListArrayList.get(i).source.equalsIgnoreCase("Reports")) {
								reportsImgArrayList.add(count.getString("source"));
								photosList5ArrayList.add(new ProjectPhotos(count.getString("id"),
										count.getString("url_large"), count
												.getString("original"), count
												.getString("url_small"), count
												.getString("url_thumb"), count
												.getString("image_file_size"), count
												.getString("image_content_type"), count
												.getString("source"), count
												.getString("phase"), count
												.getString("created_at"), count
												.getString("user_name"), count
												.getString("name"), count
												.getString("description"), count
												.getString("date_string"),new Company("","")));
								if (count.getString("user_name").equals("null")) {
									reportUsernameArrayList.add("others(null)");
								} else {
									reportUsernameArrayList.add(count.getString("user_name"));
								}

								if (count.getString("phase").equals("null")) {
									reportPhaseArrayList.add("others_phase(null)");
								} else {
									reportPhaseArrayList.add(count.getString("phase"));
								}

								reportDatesArrayList.add(count.getString("date_string"));
							}
						}
					}
					// for(int i=0;i<doc_username.size();i++)
					// {Log.i("doc",""+doc_username.get(i));}
					//
					// for(int i=0;i<checklist_username.size();i++)
					// {Log.i("checklist",""+checklist_username.get(i));}
					//
					// for(int i=0;i<report_username.size();i++)
					// {Log.i("report",""+report_username.get(i));}
					//
					// for(int i=0;i<worklist_username.size();i++)
					// {Log.i("worklist",""+worklist_username.get(i));}

					totalCountString = Integer.toString(photosListArrayList.size());
					Log.i("value..", "" + photosListArrayList.size());
					pullToRefreshListView.setAdapter(new LazyAdapter());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	// ************ API for Project Details. *************//

	public void projectPhotos() {
		if(pullBoolean!=true)
		Prefrences.showLoadingDialog(getActivity(), "Loading...");

		RequestParams params = new RequestParams();

		params.put("id", Prefrences.selectedProId);

		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.get(Prefrences.url + "/photos/" + Prefrences.selectedProId,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {

						Editor editor = sharedpref.edit();
						editor.putString("documentlistfragment", response);
//						editor.putString("projectId", Prefrences.selectedProId);
						editor.commit();
						fillServerData(response);
						if(pullBoolean!=true)
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Toast.makeText(getActivity(), "Server Issue",
								Toast.LENGTH_LONG).show();
						if(pullBoolean!=true)
						Prefrences.dismissLoadingDialog();
						if (pullBoolean == true) {
							pullBoolean = false;
							pullToRefreshListView.onRefreshComplete();
						}
					}
				});

	}

	public class LazyAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		ViewHolder holder;
		String proDocImages[] = new String[] { "All", "Project Docs",
				"Checklist", "Worklist", "Report" };
		ArrayList<String> proDocSize = new ArrayList<String>();

		public LazyAdapter() {
			inflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			Log.v("in adapter", "in adapter");
			proDocSize.add(totalCountString);
//			if (proDocImg.size() > 0)
				proDocSize.add(Integer.toString(proDocImgArrayList.size()));
//			if (checklistImg.size() > 0)
				proDocSize.add(Integer.toString(checklistImgArrayList.size()));
//			if (worklistImg.size() > 0)
				proDocSize.add(Integer.toString(worklistImgArrayList.size()));
//			if (reportsImg.size() > 0)
				proDocSize.add(Integer.toString(reportsImgArrayList.size()));

		}

		public int getCount() {
			// Toast.makeText(activity, ""+proDocSize.size(),
			// Toast.LENGTH_LONG).show();
			return 5;//proDocSize.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 5;//proDocSize.size();
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;

			if (convertView == null)
				holder = new ViewHolder();

			vi = inflater.inflate(R.layout.documentlist_item, null);
			vi.setTag(holder);
			holder.textviewName = (TextView) vi.findViewById(R.id.name);
			holder.imageview = (ImageView) vi.findViewById(R.id.image);
			holder.linearlayout = (LinearLayout) vi.findViewById(R.id.relLay);
			holder.textviewName.setTypeface(Prefrences.helveticaNeuelt(getActivity()));

			holder.linearlayout.setLayoutParams(new ListView.LayoutParams(720, 230));

			ASSL.DoMagic(holder.linearlayout);
			if (proDocSize.get(position).equals("0")) {
				holder.textviewName.setText(proDocImages[position] + " - No items");
			} else {
				holder.textviewName.setText(proDocImages[position] + " - "
						+ proDocSize.get(position) + " items");
				// Log.d("",""+)
			}
			// holder.image.setBackgroundResource(R.drawable.default_200);
			// if(data.get(position).photos.size()==0)
			// {
			// holder.right_img.setImageResource(R.drawable.default_200);
			// }
			// else
			// {
			// Picasso.with(activity).load(data.get(position).photos.get(0).urlThumb.toString()).into(holder.right_img);
			// }

			if(photosListArrayList.size()>0)
			{
				if(position>=photosListArrayList.size())
				{
					holder.imageview.setImageResource(R.drawable.default_200);
				}
				else
				{
			if (photosListArrayList.get(position).urlLarge.equals("null")) {
				holder.imageview.setImageResource(R.drawable.default_200);
			} else {
				
				if (position == 0) {
					if(photosListArrayList.size()>0)
					Picasso.with(ProjectDetail.activity)
							.load(photosListArrayList.get(0).url200.toString())
							.placeholder(R.drawable.default_200)
							.into(holder.imageview);
					else
						holder.imageview.setImageResource(R.drawable.default_200);
						
				} else if (position == 1) {
//					Log.v("error", photosList2ArrayList + "hellooo");
					if (photosList2ArrayList.size() != 0)
						Picasso.with(ProjectDetail.activity)
								.load(photosList2ArrayList.get(0).url200.toString())
								.placeholder(R.drawable.default_200)
								.into(holder.imageview);
					else
						holder.imageview.setImageResource(R.drawable.default_200);
				} else if (position == 2) {
					if (photosList3ArrayList.size() > 0)
						Picasso.with(ProjectDetail.activity)
								.load(photosList3ArrayList.get(0).url200.toString())
								.placeholder(R.drawable.default_200)
								.into(holder.imageview);
					else
						holder.imageview.setImageResource(R.drawable.default_200);
				} else if (position == 3) {
					if (photosList4ArrayList.size() > 0)
						Picasso.with(ProjectDetail.activity)
								.load(photosList4ArrayList.get(0).url200
										.toString())
								.placeholder(R.drawable.default_200)
								.into(holder.imageview);
					else
						holder.imageview.setImageResource(R.drawable.default_200);
				} else if (position == 4) {
					if (photosList5ArrayList.size() > 0)
						Picasso.with(ProjectDetail.activity)
								.load(photosList5ArrayList.get(0).url200
										.toString())
								.placeholder(R.drawable.default_200)
								.into(holder.imageview);
					else
						holder.imageview.setImageResource(R.drawable.default_200);
				}
				}
			}
			}
			else
			{
				holder.imageview.setImageResource(R.drawable.default_200);
			}
//			if(photosList.get(position).urlLarge.equals("null"))
//			{
//				holder.image.setImageResource(R.drawable.default_200);
//			}
//			else
//			{
//				Picasso.with(ProjectDetail.activity).load(photosList.get(position).url200.toString()).into(holder.image);
//			}

			
			
			holder.linearlayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (proDocSize.get(position).length() > 0) {
						int count_checker = 0;
						if (proDocImages[position].equals("All")) {
							count_checker = photosListArrayList.size();
						}
						if (proDocImages[position].equals("Project Docs")) {
							count_checker = photosList2ArrayList.size();

						} else if (proDocImages[position].equals("Checklist")) {
							count_checker = photosList3ArrayList.size();

						} else if (proDocImages[position].equals("Worklist")) {
							count_checker = photosList4ArrayList.size();

						} else if (proDocImages[position].equals("Report")) {
							count_checker = photosList5ArrayList.size();

						}

						if (proDocImages[position].equals("Project Docs")) {
							Intent intent = new Intent(getActivity(),SelectedFolder.class);
							startActivity(intent);
							getActivity().overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
						}
						else {
							
						
						if (count_checker > 0) {

							Intent intent = new Intent(getActivity(),
									ImageActivity.class);
							intent.putExtra("key", proDocImages[position]);
							intent.putStringArrayListExtra("doc", docUsernameArrayList);
							intent.putStringArrayListExtra("work",
									worklistUsernameArrayList);
							intent.putStringArrayListExtra("check",
									checklistUsernameArrayList);
							intent.putStringArrayListExtra("report",
									reportUsernameArrayList);
							intent.putStringArrayListExtra("allusers", allUsersArrayList);

							intent.putStringArrayListExtra("doc_date", docDatesArrayList);
							intent.putStringArrayListExtra("work_date",
									worklistDatesArrayList);
							intent.putStringArrayListExtra("check_date",
									checklistDatesArrayList);
							intent.putStringArrayListExtra("report_date",
									reportDatesArrayList);
							intent.putStringArrayListExtra("all_date", allDatesArrayList);

							intent.putStringArrayListExtra("doc_phase",
									docPhaseArrayList);
							intent.putStringArrayListExtra("worklist_phase",
									worklistPhaseArrayList);
							intent.putStringArrayListExtra("checklist_phase",
									checklistPhaseArrayList);
							intent.putStringArrayListExtra("report_phase",
									reportPhaseArrayList);
							intent.putStringArrayListExtra("all_phase",
									allPhaseArrayList);

							startActivity(intent);
							getActivity().overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);
						} else {
							Toast.makeText(getActivity(),
									"No Data",
									Toast.LENGTH_SHORT).show();
						}
						}
						
						
						
						// openImgAct();
					}
				}
			});

			return vi;

		}

		// public void openImgAct()
		// {
		//
		//
		// }

		class ViewHolder {

			private TextView textviewName;
			private ImageView imageview;
			LinearLayout linearlayout;

		}
	}

	public void fillServerData(String response) {
		JSONObject res = null;
		try {

			Prefrences.document_bool = true;
			Prefrences.document_s = response;
			res = new JSONObject(response);

			photosListArrayList.clear();
			photosList2ArrayList.clear();
			photosList3ArrayList.clear();
			photosList4ArrayList.clear();
			photosList5ArrayList.clear();
			proDocImgArrayList.clear();
			checklistImgArrayList.clear();
			worklistImgArrayList.clear();
			reportsImgArrayList.clear();
			allUsersArrayList = new ArrayList<String>();
			reportUsernameArrayList = new ArrayList<String>();
			worklistUsernameArrayList = new ArrayList<String>();
			checklistUsernameArrayList = new ArrayList<String>();
			docUsernameArrayList = new ArrayList<String>();
			allDatesArrayList = new ArrayList<String>();
			docDatesArrayList = new ArrayList<String>();
			reportDatesArrayList = new ArrayList<String>();
			worklistDatesArrayList = new ArrayList<String>();
			checklistDatesArrayList = new ArrayList<String>();
			allPhaseArrayList = new ArrayList<String>();
			docPhaseArrayList = new ArrayList<String>();
			reportPhaseArrayList = new ArrayList<String>();
			worklistPhaseArrayList = new ArrayList<String>();
			checklistPhaseArrayList = new ArrayList<String>();
			ProDocFold = new ArrayList<String>();
			ProDocFolders = new ArrayList<String>();

			Log.v("response ", "" + res.toString(2));
			JSONArray photos = res.getJSONArray("photos");

			for (int i = 0; i < photos.length(); i++) {
				JSONObject count = photos.getJSONObject(i);
				if(count.has("folder"))
				{

					JSONObject uCompany = count
							.getJSONObject("folder");
					
//					userArrayList.add(new Users(
//							uCount.getString("id"),
//							uCount.getString("first_name"),
//							uCount.getString("last_name"),
//							uCount.getString("full_name"),
//							uCount.getString("email"),
//							uCount.getString("formatted_phone"),
////							uCount.getString("authentication_token"),
//							uCount.getString("url_thumb"),
//							uCount.getString("url_small"),
//							new Company(uCompany
//									.getString("id"), uCompany
//									.getString("name"))));

					photosListArrayList.add(new ProjectPhotos(count.getString("id"), count
							.getString("url_large"), count.getString("original"),
							count.getString("url_small"), count
									.getString("url_thumb"), count
									.getString("image_file_size"), count
									.getString("image_content_type"), count
									.getString("source"), count.getString("phase"),
							count.getString("created_at"), count
									.getString("user_name"), count
									.getString("name"), count
									.getString("description"), count
									.getString("date_string"),new Company(uCompany
											.getString("id"), uCompany
											.getString("name"))));

					Log.d("----description----",
							"----description----" + count.getString("description")
									+ " name" + count.getString("name"));
					if (count.getString("user_name").equals("null")) {
						allUsersArrayList.add("others(null)");
					} else {
						allUsersArrayList.add(count.getString("user_name"));
					}

					if (count.getString("phase").equals("null")) {
						allPhaseArrayList.add("others_phase(null)");
					} else {
						allPhaseArrayList.add(count.getString("phase"));
					}

					allDatesArrayList.add(count.getString("date_string"));
					

					if (photosListArrayList.get(i).source.equalsIgnoreCase("Documents")) {
						proDocImgArrayList.add(count.getString("source"));
						photosList2ArrayList.add(new ProjectPhotos(count.getString("id"),
								count.getString("url_large"), count
										.getString("original"), count
										.getString("url_small"), count
										.getString("url_thumb"), count
										.getString("image_file_size"), count
										.getString("image_content_type"), count
										.getString("source"), count
										.getString("phase"), count
										.getString("created_at"), count
										.getString("user_name"), count
										.getString("name"), count
										.getString("description"), count
										.getString("date_string"),new Company(uCompany
												.getString("id"), uCompany
												.getString("name"))
										));
						if (count.getString("user_name").equals("null")) {
							docUsernameArrayList.add("others(null)");
						} else {
							docUsernameArrayList.add(count.getString("user_name"));
						}
						if (count.getString("phase").equals("null")) {
							docPhaseArrayList.add("others_phase(null)");
						} else {
							docPhaseArrayList.add(count.getString("phase"));
						}

						docDatesArrayList.add(count.getString("date_string"));
						
						ProDocFold.add(photosListArrayList.get(i).folder.coName.toString());
//						loc.addAll(WorklistFragment.locs);
						
						
//					
						
						
					} else if (photosListArrayList.get(i).source
							.equalsIgnoreCase("Checklist")) {

						checklistImgArrayList.add(count.getString("source"));
						photosList3ArrayList.add(new ProjectPhotos(count.getString("id"),
								count.getString("url_large"), count
										.getString("original"), count
										.getString("url_small"), count
										.getString("url_thumb"), count
										.getString("image_file_size"), count
										.getString("image_content_type"), count
										.getString("source"), count
										.getString("phase"), count
										.getString("created_at"), count
										.getString("user_name"), count
										.getString("name"), count
										.getString("description"), count
										.getString("date_string"),new Company(uCompany
												.getString("id"), uCompany
												.getString("name"))));
						if (count.getString("user_name").equals("null")) {
							checklistUsernameArrayList.add("others(null)");
						} else {
							checklistUsernameArrayList.add(count.getString("user_name"));
						}

						if (count.getString("phase").equals("null")) {
							checklistPhaseArrayList.add("others_phase(null)");
						} else {
							checklistPhaseArrayList.add(count.getString("phase"));
						}

						checklistDatesArrayList.add(count.getString("date_string"));
					} else if (photosListArrayList.get(i).source
							.equalsIgnoreCase("Worklist")) {
						worklistImgArrayList.add(count.getString("source"));
						photosList4ArrayList.add(new ProjectPhotos(count.getString("id"),
								count.getString("url_large"), count
										.getString("original"), count
										.getString("url_small"), count
										.getString("url_thumb"), count
										.getString("image_file_size"), count
										.getString("image_content_type"), count
										.getString("source"), count
										.getString("phase"), count
										.getString("created_at"), count
										.getString("user_name"), count
										.getString("name"), count
										.getString("description"), count
										.getString("date_string"),new Company(uCompany
												.getString("id"), uCompany
												.getString("name"))));
						if (count.getString("user_name").equals("null")) {
							worklistUsernameArrayList.add("others(null)");
						} else {
							worklistUsernameArrayList.add(count.getString("user_name"));
						}

						if (count.getString("phase").equals("null")) {
							worklistPhaseArrayList.add("others_phase(null)");
						} else {
							worklistPhaseArrayList.add(count.getString("phase"));
						}

						worklistDatesArrayList.add(count.getString("date_string"));
					} else if (photosListArrayList.get(i).source.equalsIgnoreCase("Reports")) {
						reportsImgArrayList.add(count.getString("source"));
						photosList5ArrayList.add(new ProjectPhotos(count.getString("id"),
								count.getString("url_large"), count
										.getString("original"), count
										.getString("url_small"), count
										.getString("url_thumb"), count
										.getString("image_file_size"), count
										.getString("image_content_type"), count
										.getString("source"), count
										.getString("phase"), count
										.getString("created_at"), count
										.getString("user_name"), count
										.getString("name"), count
										.getString("description"), count
										.getString("date_string"),new Company(uCompany
												.getString("id"), uCompany
												.getString("name"))));
						if (count.getString("user_name").equals("null")) {
							reportUsernameArrayList.add("others(null)");
						} else {
							reportUsernameArrayList.add(count.getString("user_name"));
						}

						if (count.getString("phase").equals("null")) {
							reportPhaseArrayList.add("others_phase(null)");
						} else {
							reportPhaseArrayList.add(count.getString("phase"));
						}

						reportDatesArrayList.add(count.getString("date_string"));
					}
				
					
				}	
				else
				{
					photosListArrayList.add(new ProjectPhotos(count.getString("id"), count
							.getString("url_large"), count.getString("original"),
							count.getString("url_small"), count
									.getString("url_thumb"), count
									.getString("image_file_size"), count
									.getString("image_content_type"), count
									.getString("source"), count.getString("phase"),
							count.getString("created_at"), count
									.getString("user_name"), count
									.getString("name"), count
									.getString("description"), count
									.getString("date_string"),new Company("","")));

					Log.d("----description----",
							"----description----" + count.getString("description")
									+ " name" + count.getString("name"));
					if (count.getString("user_name").equals("null")) {
						allUsersArrayList.add("others(null)");
					} else {
						allUsersArrayList.add(count.getString("user_name"));
					}

					if (count.getString("phase").equals("null")) {
						allPhaseArrayList.add("others_phase(null)");
					} else {
						allPhaseArrayList.add(count.getString("phase"));
					}

					allDatesArrayList.add(count.getString("date_string"));
					

					if (photosListArrayList.get(i).source.equalsIgnoreCase("Documents")) {
						proDocImgArrayList.add(count.getString("source"));
						photosList2ArrayList.add(new ProjectPhotos(count.getString("id"),
								count.getString("url_large"), count
										.getString("original"), count
										.getString("url_small"), count
										.getString("url_thumb"), count
										.getString("image_file_size"), count
										.getString("image_content_type"), count
										.getString("source"), count
										.getString("phase"), count
										.getString("created_at"), count
										.getString("user_name"), count
										.getString("name"), count
										.getString("description"), count
										.getString("date_string"),new Company("","")
										));
						if (count.getString("user_name").equals("null")) {
							docUsernameArrayList.add("others(null)");
						} else {
							docUsernameArrayList.add(count.getString("user_name"));
						}
						if (count.getString("phase").equals("null")) {
							docPhaseArrayList.add("others_phase(null)");
						} else {
							docPhaseArrayList.add(count.getString("phase"));
						}

						docDatesArrayList.add(count.getString("date_string"));
						
//						ProDocFold.add("","");
////						loc.addAll(WorklistFragment.locs);
//						
//						LinkedHashSet<String> listToSet = new LinkedHashSet<String>(
//								ProDocFold);
//						
//						ProDocFolders = new ArrayList<String>(listToSet);
						
						
						
						
						
					} else if (photosListArrayList.get(i).source
							.equalsIgnoreCase("Checklist")) {

						checklistImgArrayList.add(count.getString("source"));
						photosList3ArrayList.add(new ProjectPhotos(count.getString("id"),
								count.getString("url_large"), count
										.getString("original"), count
										.getString("url_small"), count
										.getString("url_thumb"), count
										.getString("image_file_size"), count
										.getString("image_content_type"), count
										.getString("source"), count
										.getString("phase"), count
										.getString("created_at"), count
										.getString("user_name"), count
										.getString("name"), count
										.getString("description"), count
										.getString("date_string"),new Company("","")));
						if (count.getString("user_name").equals("null")) {
							checklistUsernameArrayList.add("others(null)");
						} else {
							checklistUsernameArrayList.add(count.getString("user_name"));
						}

						if (count.getString("phase").equals("null")) {
							checklistPhaseArrayList.add("others_phase(null)");
						} else {
							checklistPhaseArrayList.add(count.getString("phase"));
						}

						checklistDatesArrayList.add(count.getString("date_string"));
					} else if (photosListArrayList.get(i).source
							.equalsIgnoreCase("Worklist")) {
						worklistImgArrayList.add(count.getString("source"));
						photosList4ArrayList.add(new ProjectPhotos(count.getString("id"),
								count.getString("url_large"), count
										.getString("original"), count
										.getString("url_small"), count
										.getString("url_thumb"), count
										.getString("image_file_size"), count
										.getString("image_content_type"), count
										.getString("source"), count
										.getString("phase"), count
										.getString("created_at"), count
										.getString("user_name"), count
										.getString("name"), count
										.getString("description"), count
										.getString("date_string"),new Company("","")));
						if (count.getString("user_name").equals("null")) {
							worklistUsernameArrayList.add("others(null)");
						} else {
							worklistUsernameArrayList.add(count.getString("user_name"));
						}

						if (count.getString("phase").equals("null")) {
							worklistPhaseArrayList.add("others_phase(null)");
						} else {
							worklistPhaseArrayList.add(count.getString("phase"));
						}

						worklistDatesArrayList.add(count.getString("date_string"));
					} else if (photosListArrayList.get(i).source.equalsIgnoreCase("Reports")) {
						reportsImgArrayList.add(count.getString("source"));
						photosList5ArrayList.add(new ProjectPhotos(count.getString("id"),
								count.getString("url_large"), count
										.getString("original"), count
										.getString("url_small"), count
										.getString("url_thumb"), count
										.getString("image_file_size"), count
										.getString("image_content_type"), count
										.getString("source"), count
										.getString("phase"), count
										.getString("created_at"), count
										.getString("user_name"), count
										.getString("name"), count
										.getString("description"), count
										.getString("date_string"),new Company("","")));
						if (count.getString("user_name").equals("null")) {
							reportUsernameArrayList.add("others(null)");
						} else {
							reportUsernameArrayList.add(count.getString("user_name"));
						}

						if (count.getString("phase").equals("null")) {
							reportPhaseArrayList.add("others_phase(null)");
						} else {
							reportPhaseArrayList.add(count.getString("phase"));
						}

						reportDatesArrayList.add(count.getString("date_string"));
					}
				}
			}
			LinkedHashSet<String> listToSet = new LinkedHashSet<String>(
					ProDocFold);
			
			ProDocFolders = new ArrayList<String>(listToSet);
			
			Log.d("ProDocFolders", "----ProDocFolders-----"+ProDocFolders.size());
			
			// for(int i=0;i<doc_username.size();i++)
			// {Log.i("doc",""+doc_username.get(i));}
			//
			// for(int i=0;i<checklist_username.size();i++)
			// {Log.i("checklist",""+checklist_username.get(i));}
			//
			// for(int i=0;i<report_username.size();i++)
			// {Log.i("report",""+report_username.get(i));}
			//
			// for(int i=0;i<worklist_username.size();i++)
			// {Log.i("worklist",""+worklist_username.get(i));}

			totalCountString = Integer.toString(photosListArrayList.size());
			Log.i("value..", "" + photosListArrayList.size());
			pullToRefreshListView.setAdapter(new LazyAdapter());

			if (pullBoolean == true) {
				pullBoolean = false;
				pullToRefreshListView.onRefreshComplete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
