package com.buildhawk;

import java.util.ArrayList;

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

import com.buildhawk.utils.ProjectPhotos;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

public class DocumentFragment extends Fragment {

	PullToRefreshListView docListView;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	Boolean pull = false;
	public static ArrayList<ProjectPhotos> photosList = new ArrayList<ProjectPhotos>();
	public static ArrayList<ProjectPhotos> photosList2 = new ArrayList<ProjectPhotos>();
	public static ArrayList<ProjectPhotos> photosList3 = new ArrayList<ProjectPhotos>();
	public static ArrayList<ProjectPhotos> photosList4 = new ArrayList<ProjectPhotos>();
	public static ArrayList<ProjectPhotos> photosList5 = new ArrayList<ProjectPhotos>();
	public static ArrayList<String> proDocImg = new ArrayList<String>();
	public static ArrayList<String> checklistImg = new ArrayList<String>();
	public static ArrayList<String> worklistImg = new ArrayList<String>();
	public static ArrayList<String> reportsImg = new ArrayList<String>();

	public ArrayList<String> allUsers;// = new ArrayList<String>();
	public ArrayList<String> docUsername;
	public ArrayList<String> reportUsername;// = new ArrayList<String>();
	public ArrayList<String> worklistUsername;
	public ArrayList<String> checklistUsername;

	public ArrayList<String> allDates;// = new ArrayList<String>();
	public ArrayList<String> docDates;// = new ArrayList<String>();
	public ArrayList<String> reportDates;// = new ArrayList<String>();
	public ArrayList<String> worklistDates;// = new ArrayList<String>();
	public ArrayList<String> checklistDates;// = new ArrayList<String>();

	public ArrayList<String> allPhase;// = new ArrayList<String>();
	public ArrayList<String> docPhase;// = new ArrayList<String>();
	public ArrayList<String> reportPhase;// = new ArrayList<String>();
	public ArrayList<String> worklistPhase;// = new ArrayList<String>();
	public ArrayList<String> checklistPhase;// = new ArrayList<String>();
	String totalCount;
	RelativeLayout relLay;
	Activity activity;
	SharedPreferences sharedpref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.documents, container, false);

		relLay = (RelativeLayout) root.findViewById(R.id.rellay);
		new ASSL(getActivity(), relLay, 1134, 720, false);
		docListView = (PullToRefreshListView) root.findViewById(R.id.docList);
		// act=ProjectDetail.this;
		sharedpref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
		
		if(Prefrences.document_s.equalsIgnoreCase("")){
			Prefrences.document_bool=false;
		}
		
		docListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub

				if (isInternetPresent) {
					pull = true;
					projectPhotos();
				} else {
				String response;
					response = sharedpref.getString("documentlistfragment", "");
					if(response.equalsIgnoreCase("")){
						Toast.makeText(getActivity(), "No internet connection.",Toast.LENGTH_SHORT).show();
					}else{
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
		isInternetPresent = connDect.isConnectingToInternet();
		if (Prefrences.stopingHit == 1) {
			Prefrences.stopingHit = 0;
			
			if(Prefrences.document_bool==false){
			if (isInternetPresent) {
				projectPhotos();
			} else {
				String response = sharedpref.getString("documentlistfragment", "");
				if(response.equalsIgnoreCase("")){
					Toast.makeText(getActivity(),"No internet connection.", Toast.LENGTH_SHORT).show();
				}else{
					fillServerData(response);
				}
			}
			}else{
				
				JSONObject res = null;
				try {
					
					
					res = new JSONObject(Prefrences.document_s);
					
					photosList.clear();
					photosList2.clear();
					photosList3.clear();
					photosList4.clear();
					photosList5.clear();
					proDocImg.clear();
					checklistImg.clear();
					worklistImg.clear();
					reportsImg.clear();
					allUsers = new ArrayList<String>();
					reportUsername = new ArrayList<String>();
					worklistUsername = new ArrayList<String>();
					checklistUsername = new ArrayList<String>();
					docUsername = new ArrayList<String>();
					allDates = new ArrayList<String>();
					docDates = new ArrayList<String>();
					reportDates = new ArrayList<String>();
					worklistDates = new ArrayList<String>();
					checklistDates = new ArrayList<String>();
					allPhase = new ArrayList<String>();
					docPhase = new ArrayList<String>();
					reportPhase = new ArrayList<String>();
					worklistPhase = new ArrayList<String>();
					checklistPhase = new ArrayList<String>();

					Log.v("response ", "" + res.toString(2));
					JSONArray photos = res.getJSONArray("photos");

					for (int i = 0; i < photos.length(); i++) {
						JSONObject count = photos.getJSONObject(i);

						photosList.add(new ProjectPhotos(count
								.getString("id"), count
								.getString("url_large"), count
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
								.getString("date_string")));

						Log.d("----description----",
								"----description----"
										+ count.getString("description")
										+ " name"
										+ count.getString("name"));
						if (count.getString("user_name").equals("null")) {
							allUsers.add("kanika(null)");
						} else {
							allUsers.add(count.getString("user_name"));
						}

						if (count.getString("phase").equals("null")) {
							allPhase.add("kanika_phase(null)");
						} else {
							allPhase.add(count.getString("phase"));
						}

						allDates.add(count.getString("date_string"));

						if (photosList.get(i).source
								.equalsIgnoreCase("Documents")) {
							proDocImg.add(count.getString("source"));
							photosList2.add(new ProjectPhotos(
									count.getString("id"),
									count.getString("url_large"),
									count.getString("original"),
									count.getString("url_small"),
									count.getString("url_thumb"),
									count.getString("image_file_size"),
									count.getString("image_content_type"),
									count.getString("source"), count
											.getString("phase"), count
											.getString("created_at"),
									count.getString("user_name"), count
											.getString("name"), count
											.getString("description"),
									count.getString("date_string")));
							if (count.getString("user_name").equals(
									"null")) {
								docUsername.add("kanika(null)");
							} else {
								docUsername.add(count
										.getString("user_name"));
							}
							if (count.getString("phase").equals("null")) {
								docPhase.add("kanika_phase(null)");
							} else {
								docPhase.add(count.getString("phase"));
							}

							docDates.add(count
									.getString("date_string"));
						} else if (photosList.get(i).source
								.equalsIgnoreCase("Checklist")) {

							checklistImg.add(count.getString("source"));
							photosList3.add(new ProjectPhotos(
									count.getString("id"),
									count.getString("url_large"),
									count.getString("original"),
									count.getString("url_small"),
									count.getString("url_thumb"),
									count.getString("image_file_size"),
									count.getString("image_content_type"),
									count.getString("source"), count
											.getString("phase"), count
											.getString("created_at"),
									count.getString("user_name"), count
											.getString("name"), count
											.getString("description"),
									count.getString("date_string")));
							if (count.getString("user_name").equals(
									"null")) {
								checklistUsername.add("kanika(null)");
							} else {
								checklistUsername.add(count
										.getString("user_name"));
							}

							if (count.getString("phase").equals("null")) {
								checklistPhase
										.add("kanika_phase(null)");
							} else {
								checklistPhase.add(count
										.getString("phase"));
							}

							checklistDates.add(count
									.getString("date_string"));
						} else if (photosList.get(i).source
								.equalsIgnoreCase("Worklist")) {
							worklistImg.add(count.getString("source"));
							photosList4.add(new ProjectPhotos(
									count.getString("id"),
									count.getString("url_large"),
									count.getString("original"),
									count.getString("url_small"),
									count.getString("url_thumb"),
									count.getString("image_file_size"),
									count.getString("image_content_type"),
									count.getString("source"), count
											.getString("phase"), count
											.getString("created_at"),
									count.getString("user_name"), count
											.getString("name"), count
											.getString("description"),
									count.getString("date_string")));
							if (count.getString("user_name").equals(
									"null")) {
								worklistUsername.add("kanika(null)");
							} else {
								worklistUsername.add(count
										.getString("user_name"));
							}

							if (count.getString("phase").equals("null")) {
								worklistPhase.add("kanika_phase(null)");
							} else {
								worklistPhase.add(count
										.getString("phase"));
							}

							worklistDates.add(count
									.getString("date_string"));
						} else if (photosList.get(i).source
								.equalsIgnoreCase("Reports")) {
							reportsImg.add(count.getString("source"));
							photosList5.add(new ProjectPhotos(
									count.getString("id"),
									count.getString("url_large"),
									count.getString("original"),
									count.getString("url_small"),
									count.getString("url_thumb"),
									count.getString("image_file_size"),
									count.getString("image_content_type"),
									count.getString("source"), count
											.getString("phase"), count
											.getString("created_at"),
									count.getString("user_name"), count
											.getString("name"), count
											.getString("description"),
									count.getString("date_string")));
							if (count.getString("user_name").equals(
									"null")) {
								reportUsername.add("kanika(null)");
							} else {
								reportUsername.add(count
										.getString("user_name"));
							}

							if (count.getString("phase").equals("null")) {
								reportPhase.add("kanika_phase(null)");
							} else {
								reportPhase.add(count
										.getString("phase"));
							}

							reportDates.add(count
									.getString("date_string"));
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

					totalCount = Integer.toString(photosList.size());
					Log.i("value..", "" + photosList.size());
					docListView.setAdapter(new LazyAdapter());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	// ************ API for Project Details. *************//

	public void projectPhotos() {

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
						editor.commit();
						fillServerData(response);
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Toast.makeText(getActivity(), "Server Issue",
								Toast.LENGTH_LONG).show();
						Prefrences.dismissLoadingDialog();
						if (pull == true) {
							pull = false;
							docListView.onRefreshComplete();
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
			proDocSize.add(totalCount);
			proDocSize.add(Integer.toString(proDocImg.size()));
			proDocSize.add(Integer.toString(checklistImg.size()));
			proDocSize.add(Integer.toString(worklistImg.size()));
			proDocSize.add(Integer.toString(reportsImg.size()));

		}

		public int getCount() {
			return 5;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 5;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View vi = convertView;

			if (convertView == null)
				holder = new ViewHolder();

			vi = inflater.inflate(R.layout.documentlist_item, null);
			vi.setTag(holder);
			holder.name = (TextView) vi.findViewById(R.id.name);
			holder.image = (ImageView) vi.findViewById(R.id.image);
			holder.relLay = (LinearLayout) vi.findViewById(R.id.relLay);
			holder.name.setTypeface(Prefrences.helveticaNeuelt(getActivity()));

			holder.relLay.setLayoutParams(new ListView.LayoutParams(720, 230));

			ASSL.DoMagic(holder.relLay);
			if (proDocSize.get(position).equals("0")) {
				holder.name.setText(proDocImages[position] + " - No items");
			} else {
				holder.name.setText(proDocImages[position] + " - "
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

			if (photosList.get(position).urlLarge.equals("null")) {
				holder.image.setImageResource(R.drawable.default_200);
			} else {
				Picasso.with(ProjectDetail.activity)
						.load(photosList.get(position).url200.toString())
						.placeholder(R.drawable.default_200)
						.into(holder.image);
			}

			holder.relLay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (proDocSize.get(position).length() > 0) {
						int count_checker=0;
						if (proDocImages[position].equals("All")) {
							count_checker = photosList.size();
						}
						if (proDocImages[position].equals("Project Docs")) {
							count_checker = photosList2.size();
							
						} else if (proDocImages[position].equals("Checklist")) {
							count_checker = photosList3.size();
							
						} else if (proDocImages[position].equals("Worklist")) {
							count_checker = photosList4.size();

						} else if (proDocImages[position].equals("Report")) {
							count_checker = photosList5.size();

						}
						
						
						if(count_checker>0){
						
								Intent intent = new Intent(getActivity(),
										ImageActivity.class);
								intent.putExtra("key", proDocImages[position]);
								intent.putStringArrayListExtra("doc", docUsername);
								intent.putStringArrayListExtra("work", worklistUsername);
								intent.putStringArrayListExtra("check",
										checklistUsername);
								intent.putStringArrayListExtra("report", reportUsername);
								intent.putStringArrayListExtra("allusers", allUsers);
		
								intent.putStringArrayListExtra("doc_date", docDates);
								intent.putStringArrayListExtra("work_date",
										worklistDates);
								intent.putStringArrayListExtra("check_date",
										checklistDates);
								intent.putStringArrayListExtra("report_date",
										reportDates);
								intent.putStringArrayListExtra("all_date", allDates);
		
								intent.putStringArrayListExtra("doc_phase", docPhase);
								intent.putStringArrayListExtra("worklist_phase",
										worklistPhase);
								intent.putStringArrayListExtra("checklist_phase",
										checklistPhase);
								intent.putStringArrayListExtra("report_phase",
										reportPhase);
								intent.putStringArrayListExtra("all_phase", allPhase);
		
								startActivity(intent);
								getActivity().overridePendingTransition(
										R.anim.slide_in_right, R.anim.slide_out_left);
						}else{
							Toast.makeText(getActivity(),"No internet connection.", Toast.LENGTH_SHORT).show();
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

			private TextView name;
			private ImageView image;
			LinearLayout relLay;

		}
	}
	
	
	public void fillServerData(String response){
		JSONObject res = null;
		try {
			
			Prefrences.document_bool=true;
			Prefrences.document_s = response;
			res = new JSONObject(response);
			
			photosList.clear();
			photosList2.clear();
			photosList3.clear();
			photosList4.clear();
			photosList5.clear();
			proDocImg.clear();
			checklistImg.clear();
			worklistImg.clear();
			reportsImg.clear();
			allUsers = new ArrayList<String>();
			reportUsername = new ArrayList<String>();
			worklistUsername = new ArrayList<String>();
			checklistUsername = new ArrayList<String>();
			docUsername = new ArrayList<String>();
			allDates = new ArrayList<String>();
			docDates = new ArrayList<String>();
			reportDates = new ArrayList<String>();
			worklistDates = new ArrayList<String>();
			checklistDates = new ArrayList<String>();
			allPhase = new ArrayList<String>();
			docPhase = new ArrayList<String>();
			reportPhase = new ArrayList<String>();
			worklistPhase = new ArrayList<String>();
			checklistPhase = new ArrayList<String>();

			Log.v("response ", "" + res.toString(2));
			JSONArray photos = res.getJSONArray("photos");

			for (int i = 0; i < photos.length(); i++) {
				JSONObject count = photos.getJSONObject(i);

				photosList.add(new ProjectPhotos(count
						.getString("id"), count
						.getString("url_large"), count
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
						.getString("date_string")));

				Log.d("----description----",
						"----description----"
								+ count.getString("description")
								+ " name"
								+ count.getString("name"));
				if (count.getString("user_name").equals("null")) {
					allUsers.add("kanika(null)");
				} else {
					allUsers.add(count.getString("user_name"));
				}

				if (count.getString("phase").equals("null")) {
					allPhase.add("kanika_phase(null)");
				} else {
					allPhase.add(count.getString("phase"));
				}

				allDates.add(count.getString("date_string"));

				if (photosList.get(i).source
						.equalsIgnoreCase("Documents")) {
					proDocImg.add(count.getString("source"));
					photosList2.add(new ProjectPhotos(
							count.getString("id"),
							count.getString("url_large"),
							count.getString("original"),
							count.getString("url_small"),
							count.getString("url_thumb"),
							count.getString("image_file_size"),
							count.getString("image_content_type"),
							count.getString("source"), count
									.getString("phase"), count
									.getString("created_at"),
							count.getString("user_name"), count
									.getString("name"), count
									.getString("description"),
							count.getString("date_string")));
					if (count.getString("user_name").equals(
							"null")) {
						docUsername.add("kanika(null)");
					} else {
						docUsername.add(count
								.getString("user_name"));
					}
					if (count.getString("phase").equals("null")) {
						docPhase.add("kanika_phase(null)");
					} else {
						docPhase.add(count.getString("phase"));
					}

					docDates.add(count
							.getString("date_string"));
				} else if (photosList.get(i).source
						.equalsIgnoreCase("Checklist")) {

					checklistImg.add(count.getString("source"));
					photosList3.add(new ProjectPhotos(
							count.getString("id"),
							count.getString("url_large"),
							count.getString("original"),
							count.getString("url_small"),
							count.getString("url_thumb"),
							count.getString("image_file_size"),
							count.getString("image_content_type"),
							count.getString("source"), count
									.getString("phase"), count
									.getString("created_at"),
							count.getString("user_name"), count
									.getString("name"), count
									.getString("description"),
							count.getString("date_string")));
					if (count.getString("user_name").equals(
							"null")) {
						checklistUsername.add("kanika(null)");
					} else {
						checklistUsername.add(count
								.getString("user_name"));
					}

					if (count.getString("phase").equals("null")) {
						checklistPhase
								.add("kanika_phase(null)");
					} else {
						checklistPhase.add(count
								.getString("phase"));
					}

					checklistDates.add(count
							.getString("date_string"));
				} else if (photosList.get(i).source
						.equalsIgnoreCase("Worklist")) {
					worklistImg.add(count.getString("source"));
					photosList4.add(new ProjectPhotos(
							count.getString("id"),
							count.getString("url_large"),
							count.getString("original"),
							count.getString("url_small"),
							count.getString("url_thumb"),
							count.getString("image_file_size"),
							count.getString("image_content_type"),
							count.getString("source"), count
									.getString("phase"), count
									.getString("created_at"),
							count.getString("user_name"), count
									.getString("name"), count
									.getString("description"),
							count.getString("date_string")));
					if (count.getString("user_name").equals(
							"null")) {
						worklistUsername.add("kanika(null)");
					} else {
						worklistUsername.add(count
								.getString("user_name"));
					}

					if (count.getString("phase").equals("null")) {
						worklistPhase.add("kanika_phase(null)");
					} else {
						worklistPhase.add(count
								.getString("phase"));
					}

					worklistDates.add(count
							.getString("date_string"));
				} else if (photosList.get(i).source
						.equalsIgnoreCase("Reports")) {
					reportsImg.add(count.getString("source"));
					photosList5.add(new ProjectPhotos(
							count.getString("id"),
							count.getString("url_large"),
							count.getString("original"),
							count.getString("url_small"),
							count.getString("url_thumb"),
							count.getString("image_file_size"),
							count.getString("image_content_type"),
							count.getString("source"), count
									.getString("phase"), count
									.getString("created_at"),
							count.getString("user_name"), count
									.getString("name"), count
									.getString("description"),
							count.getString("date_string")));
					if (count.getString("user_name").equals(
							"null")) {
						reportUsername.add("kanika(null)");
					} else {
						reportUsername.add(count
								.getString("user_name"));
					}

					if (count.getString("phase").equals("null")) {
						reportPhase.add("kanika_phase(null)");
					} else {
						reportPhase.add(count
								.getString("phase"));
					}

					reportDates.add(count
							.getString("date_string"));
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

			totalCount = Integer.toString(photosList.size());
			Log.i("value..", "" + photosList.size());
			docListView.setAdapter(new LazyAdapter());

			if (pull == true) {
				pull = false;
				docListView.onRefreshComplete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
