package com.buildhawk;

import java.util.ArrayList;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.Author;
import com.buildhawk.utils.CommentUser;
import com.buildhawk.utils.Comments;
import com.buildhawk.utils.Company;
import com.buildhawk.utils.Personnel;
import com.buildhawk.utils.ProjectPhotos;
import com.buildhawk.utils.Report;
import com.buildhawk.utils.ReportCompanies;
import com.buildhawk.utils.ReportCompany;
import com.buildhawk.utils.ReportCompanySubcontractors;
import com.buildhawk.utils.ReportCompanyUsers;
import com.buildhawk.utils.ReportPersonnel;
import com.buildhawk.utils.ReportPersonnelUser;
import com.buildhawk.utils.ReportTopics;
import com.buildhawk.utils.SafetyTopics;
import com.buildhawk.utils.subcontractors;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ReportFragment extends Fragment {
	static ArrayList<Report> reportdata;
	static ArrayList<Author> author;
//	static ArrayList<String> possibleTypesArray;
	static ArrayList<Report> reportdataDaily;
	static ArrayList<Report> reportdataSafety;
	static ArrayList<Report> reportdataWeekly;
	public static boolean fromReportItem=false;
	public ArrayList<Comments> commnt;
	public ArrayList<CommentUser> cusr;
	public  ArrayList<ProjectPhotos> photo;
	public  ArrayList<ReportPersonnelUser> personnelUser;
	public  ArrayList<subcontractors> psub;
	public  ArrayList<ReportPersonnel> person;
	
	public  ArrayList<ReportCompanies>companies;
//	public static ArrayList<ReportCompany>Rcompany;
	public  ArrayList<ReportCompanyUsers>coUsers;
	public  ArrayList<ReportCompanyUsers>coSubUsers;
	public  ArrayList<ReportCompanySubcontractors>coSubs;
	public ArrayList<SafetyTopics>safe;
	
	public ArrayList<ReportTopics>ReportTopics;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	String type;
	int j;
	TextView tv_daily;
	TextView tv_weekly;
	TextView tv_safety;
	PullToRefreshListView reportlist;
	ReportListAdapter reportAdapter;
	LinearLayout linlay;
	Boolean pull = false;
	SharedPreferences sharedpref;

	// ArrayList<String> photo_url;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.report, container, false);
		linlay = (LinearLayout) root.findViewById(R.id.linearlay);
		new ASSL(getActivity(), linlay, 1134, 720, false);
		sharedpref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
		tv_daily = (TextView) root.findViewById(R.id.daily);
		tv_safety = (TextView) root.findViewById(R.id.safety);
		tv_weekly = (TextView) root.findViewById(R.id.weekly);

		tv_daily.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		tv_safety.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		tv_weekly.setTypeface(Prefrences.helveticaNeuelt(getActivity()));

		reportlist = (PullToRefreshListView) root.findViewById(R.id.reportlist);
		// registerForContextMenu(reportlist);
		// get_project_reports(Prefrences.selectedProId);

		if(Prefrences.report_s.equalsIgnoreCase("")){
			Prefrences.report_bool=false;
		}
		
		reportlist.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// if (isInternetPresent) {
				pull = true;
				registerForContextMenu(reportlist);
				getProjectReports(Prefrences.selectedProId);
				// } else {
				// Toast.makeText(getActivity(),
				// "No internet connection.", Toast.LENGTH_SHORT).show();
				// }
			}
		});

		tv_daily.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Prefrences.reportlisttypes==1)
					{
					Prefrences.reportType = 0;
						Prefrences.reportlisttypes=0;
						reportAdapter =null;
						reportAdapter = new ReportListAdapter(
								getActivity(), reportdata);
						reportlist.setAdapter(reportAdapter);
						tv_daily.setBackgroundResource(R.drawable.all_white_background);
						tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
						tv_safety.setBackgroundResource(R.color.white);

						tv_safety.setTextColor(Color.BLACK);
						tv_weekly.setTextColor(Color.BLACK);
						tv_daily.setTextColor(Color.BLACK);
						
					}else
				{
				Prefrences.reportlisttypes=1;
				tv_daily.setBackgroundResource(R.drawable.all_black_background);
				tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
				tv_safety.setBackgroundResource(R.color.white);

				tv_safety.setTextColor(Color.BLACK);
				tv_weekly.setTextColor(Color.BLACK);
				tv_daily.setTextColor(Color.WHITE);

				if (reportdataDaily.size() == 0) {
					Toast.makeText(getActivity(), "No data", 5000).show();
					reportlist.setVisibility(View.INVISIBLE);
				} else {
					Prefrences.reportType = 1;
					reportAdapter =null;
					reportAdapter = new ReportListAdapter(getActivity(),
							reportdataDaily);
					reportlist.setVisibility(View.VISIBLE);
					reportlist.setAdapter(reportAdapter);
					// reportAdapter.notify();
				}
			}
		}
	});
		tv_weekly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Prefrences.reportlisttypes==3){
					Prefrences.reportType = 0;
					Prefrences.reportlisttypes=0;
					reportAdapter =null;
					reportAdapter = new ReportListAdapter(
							getActivity(), reportdata);
					reportlist.setAdapter(reportAdapter);
					tv_daily.setBackgroundResource(R.drawable.all_white_background);
					tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
					tv_safety.setBackgroundResource(R.color.white);

					tv_safety.setTextColor(Color.BLACK);
					tv_weekly.setTextColor(Color.BLACK);
					tv_daily.setTextColor(Color.BLACK);
					
				}else{
				Prefrences.reportlisttypes=3;
				tv_daily.setBackgroundResource(R.drawable.all_white_background);
				tv_weekly.setBackgroundResource(R.drawable.complete_black_background);
				tv_safety.setBackgroundResource(R.color.white);

				tv_safety.setTextColor(Color.BLACK);
				tv_weekly.setTextColor(Color.WHITE);
				tv_daily.setTextColor(Color.BLACK);

				if (reportdataWeekly.isEmpty()) {
					Toast.makeText(getActivity(), "No data", 5000).show();
					reportlist.setVisibility(View.INVISIBLE);
				} else {
					Prefrences.reportType = 3;
					reportAdapter =null;
					reportAdapter = new ReportListAdapter(getActivity(),
							reportdataWeekly);
					reportlist.setVisibility(View.VISIBLE);
					reportlist.setAdapter(reportAdapter);
					// reportAdapter.notify();
				}
				}
			}
		});
		tv_safety.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Prefrences.reportlisttypes==2){
					Prefrences.reportType = 0;
					Prefrences.reportlisttypes=0;
					reportAdapter =null;
					reportAdapter = new ReportListAdapter(
							getActivity(), reportdata);
					reportlist.setAdapter(reportAdapter);
					tv_daily.setBackgroundResource(R.drawable.all_white_background);
					tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
					tv_safety.setBackgroundResource(R.color.white);

					tv_safety.setTextColor(Color.BLACK);
					tv_weekly.setTextColor(Color.BLACK);
					tv_daily.setTextColor(Color.BLACK);
					
				}else{
				Prefrences.reportlisttypes=2;
				
				tv_daily.setBackgroundResource(R.drawable.all_white_background);
				tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
				tv_safety.setBackgroundResource(R.color.black);

				tv_safety.setTextColor(Color.WHITE);
				tv_weekly.setTextColor(Color.BLACK);
				tv_daily.setTextColor(Color.BLACK);

				if (reportdataSafety.size() == 0) {
					Toast.makeText(getActivity(), "No data", 5000).show();
					reportlist.setVisibility(View.INVISIBLE);

				} else {
					Prefrences.reportType = 2;
					reportAdapter =null;
					reportAdapter = new ReportListAdapter(getActivity(),
							reportdataSafety);
					reportlist.setVisibility(View.VISIBLE);
					reportlist.setAdapter(reportAdapter);
					// reportAdapter.notify();
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
		
		if(fromReportItem==true){
			fromReportItem=false;
			if(Prefrences.reportlisttypes==0)
			{
			reportAdapter = new ReportListAdapter(
					getActivity(), reportdata);
			reportlist.setAdapter(reportAdapter);
			tv_daily.setBackgroundResource(R.drawable.all_white_background);
			tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			tv_safety.setBackgroundResource(R.color.white);

			tv_safety.setTextColor(Color.BLACK);
			tv_weekly.setTextColor(Color.BLACK);
			tv_daily.setTextColor(Color.BLACK);
			}
			else if(Prefrences.reportlisttypes==1)
			{
			reportAdapter = new ReportListAdapter(
					getActivity(), reportdataDaily);
			reportlist.setAdapter(reportAdapter);
			tv_daily.setBackgroundResource(R.drawable.all_black_background);
			tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			tv_safety.setBackgroundResource(R.color.white);

			tv_safety.setTextColor(Color.BLACK);
			tv_weekly.setTextColor(Color.BLACK);
			tv_daily.setTextColor(Color.WHITE);
			}
			else if(Prefrences.reportlisttypes==2)
			{
			reportAdapter = new ReportListAdapter(
					getActivity(), reportdataSafety);
			reportlist.setAdapter(reportAdapter);
			tv_daily.setBackgroundResource(R.drawable.all_white_background);
			tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			tv_safety.setBackgroundResource(R.color.black);

			tv_safety.setTextColor(Color.WHITE);
			tv_weekly.setTextColor(Color.BLACK);
			tv_daily.setTextColor(Color.BLACK);
			}
			else if(Prefrences.reportlisttypes==3)
			{
			reportAdapter = new ReportListAdapter(
					getActivity(), reportdataWeekly);
			reportlist.setAdapter(reportAdapter);
			tv_daily.setBackgroundResource(R.drawable.all_white_background);
			tv_weekly.setBackgroundResource(R.drawable.complete_black_background);
			tv_safety.setBackgroundResource(R.color.white);

			tv_safety.setTextColor(Color.BLACK);
			tv_weekly.setTextColor(Color.WHITE);
			tv_daily.setTextColor(Color.BLACK);
			}
		}else{
		if (Prefrences.stopingHit == 1) {
			Prefrences.stopingHit = 0;
			if(Prefrences.report_bool==false){
			if (isInternetPresent) {
				
				getProjectReports(Prefrences.selectedProId);
			} else {
				String response = sharedpref.getString("reportlistfragment", "");
				if(response.equalsIgnoreCase("")){
					Toast.makeText(getActivity(),"No internet connection.", Toast.LENGTH_SHORT).show();
				}else{
					registerForContextMenu(reportlist);
					fillServerData(response);
				}
			}
			}else
			{
				JSONObject res = null;
				try {
					
					res = new JSONObject(Prefrences.report_s);
					Log.v("response ", "" + res.toString(2));
					JSONArray reportArrray = res
							.getJSONArray("reports");
					reportdata = new ArrayList<Report>();
					reportdataDaily = new ArrayList<Report>();
					reportdataSafety = new ArrayList<Report>();
					reportdataWeekly = new ArrayList<Report>();
					// photo_url=new ArrayList<String>();
					Log.d("report Array length",
							"" + reportArrray.length());
					for (int i = 0; i < reportArrray.length(); i++) {

						person = new ArrayList<ReportPersonnel>();
						companies= new ArrayList<ReportCompanies>();
						ReportTopics = new ArrayList<ReportTopics>();
//						Rcompany = new ArrayList<ReportCompany>();
						coUsers = new ArrayList<ReportCompanyUsers>();
						
						coSubs = new ArrayList<ReportCompanySubcontractors>();

					//	possibleTypesArray = new ArrayList<String>();
						JSONObject reportobj = reportArrray
								.getJSONObject(i);
						String reportId = reportobj.getString("id");
						Log.d("report_id", "-----------***" + reportId);
						if (reportId.equals("null")) {
							reportId = "";
						}
						String epochTime = reportobj
								.getString("epoch_time");
						if (epochTime.equals("null")) {
							epochTime = "";
						}
						String createdAt = reportobj
								.getString("created_at");
						if (createdAt.equals("null")) {
							createdAt = "";
						}
						String updatedat = reportobj
								.getString("updated_at");
						if (updatedat.equals("null")) {
							updatedat = "";
						}
						String createdDate = reportobj
								.getString("date_string");
						if (createdDate.equals("null")) {
							createdDate = "";
						}
						String title = reportobj.getString("title");
						if (title.equals("null")) {
							title = "";
						}
						String reportType = reportobj
								.getString("report_type");
						String body;
						if (reportobj.has("body")) {
							body = reportobj.getString("body");
							// It exists, do your stuff
						} else {
							body = "N/A";
							// It doesn't exist, do nothing
						}

						if (reportType.equals("null")) {
							reportType = "";
						}
						String weather = reportobj.getString("weather");
						if (weather.equals("null")) {
							weather = "";
						}
						String weathericon = reportobj
								.getString("weather_icon");
						if (weathericon.equals("null")) {
							weathericon = "";
						}
						String precip = reportobj.getString("precip");
						if (precip.equals("null")) {
							precip = "";
						}
						String temp = reportobj.getString("temp");
						if (temp.equals("null")) {
							temp = "";
						}
						String wind = reportobj.getString("wind");
						if (wind.equals("null")) {
							wind = "";
						}
						String humidity = reportobj
								.getString("humidity");
						if (humidity.equals("null")) {
							humidity = "";
						}

						

						/*------- author object-------*/
						if (!reportobj.isNull("author")) {
							author = new ArrayList<Author>();
							JSONObject authorobj = reportobj
									.getJSONObject("author");
							String authorid = authorobj.getString("id");
							if (authorid.equals("null")) {
								authorid = "";
							}
							String firstname = authorobj
									.getString("first_name");
							if (firstname.equals("null")) {
								firstname = "";
							}
							String lastname = authorobj
									.getString("last_name");
							if (lastname.equals("null")) {
								lastname = "";
							}
							String fullname = authorobj
									.getString("full_name");
							if (fullname.equals("null")) {
								fullname = "";
							}
							String email = authorobj.getString("email");
							if (email.equals("null")) {
								email = "";
							}
							String phonenumber = authorobj
									.getString("phone");
							if (phonenumber.equals("null")) {
								phonenumber = "";
							}
							
							author.add(new Author(authorid, firstname,
									lastname, fullname, email,
									phonenumber));
						} else {
							author = new ArrayList<Author>();
							author.add(new Author("", "", "", "", "",
									""));
						}
						/*------- report_fields array-------*/
						JSONArray report_fields = reportobj
								.getJSONArray("report_fields");
						for (int j = 0; j < report_fields.length(); j++) {
						}

						
						JSONArray photos = reportobj
								.getJSONArray("photos");
						if (photos.length() == 0) {
							photo = new ArrayList<ProjectPhotos>();
							Log.d("if", "----photo if null--");
							photo.add(new ProjectPhotos("", "", "",
									"drawable", "", "", "", "", "", "",
									"", "", "", ""));
							// photo_url.add("http://3.bp.blogspot.com/-1EULR14bUFc/T8srSY2KZqI/AAAAAAAAEOk/vTI6mnpZ43g/s1600/nature-wallpaper-15.jpg");
						} else {
							photo = new ArrayList<ProjectPhotos>();
							for (int k = 0; k < photos.length(); k++) {

								JSONObject photosobj = photos
										.getJSONObject(k);
								String photoid = photosobj
										.getString("id");
								String url_large = photosobj
										.getString("url_large");
								String original = photosobj
										.getString("original");
								String photo_url200 = photosobj
										.getString("url_small");
								String url100 = photosobj
										.getString("url_thumb");
								String photo_epoch_time = photosobj
										.getString("epoch_time");
								String photo_url_small = photosobj
										.getString("url_small");
								String photo_url_thumb = photosobj
										.getString("url_thumb");
								String image_file_size = photosobj
										.getString("image_file_size");
								String image_content_type = photosobj
										.getString("image_content_type");

								String source = photosobj
										.getString("source");
								String phase = photosobj
										.getString("phase");
								String photo_created_at = photosobj
										.getString("created_at");
								String user_name = photosobj
										.getString("user_name");
								String name = photosobj
										.getString("name");

								String desc = photosobj
										.getString("description");
								String photo_created_date = photosobj
										.getString("date_string");

								// Log.d("photosdata ",
								// "------------------photosdata start----------------------");
								// Log.d("photoid", ":" + photoid);
								// Log.d("url_large", ":" + url_large);
								// Log.d("original", ":" + original);
								// Log.d("url200", ":" + photo_url200);
								// Log.d("url100", ":" + url100);
								// Log.d("epoch_time", ":" +
								// photo_epoch_time);
								// Log.d("url_small", ":" +
								// photo_url_small);
								// Log.d("url_thumb", ":" +
								// photo_url_thumb);
								// Log.d("image_file_size", ":"
								// + image_file_size);
								// Log.d("image_content_type", ":"
								// + image_content_type);
								// Log.d("source", ":" + source);
								// Log.d("phase", ":" + phase);
								// Log.d("created_at", ":" +
								// photo_created_at);
								// Log.d("user_name", ":" + user_name);
								// Log.d("name", ":" + name);
								// Log.d("created_date", ":"
								// + photo_created_date);
								//
								// Log.d("photosdata ",
								// "-----------------photosdata end----------------------");
								// photo_url.add(photo_url200);
								Log.d("----photo url---", ""
										+ photo_url200);
								photo.add(new ProjectPhotos(photoid,
										url_large, original,
										photo_url200, url100,
										image_file_size,
										image_content_type, source,
										phase, photo_created_at,
										user_name, name, desc,
										photo_created_date));
							}
						}

						/*------- personnel array-------*/

						JSONArray personnel = reportobj
								.getJSONArray("personnel");
						if (personnel.length() == 0) {
							// person.add(new ReportPersonnel("",null,
							// ""));
						} else {
							Log.e("", "how many times" + i);

							for (j = 0; j < personnel.length(); j++) {

								JSONObject count = personnel
										.getJSONObject(j);
								Log.d("", "count : " + count);
								if (count.isNull("user")) {
									

								} else {

									JSONObject puser = count
											.getJSONObject("user");

									Log.d("", "puser : " + puser);
									JSONObject company = puser
											.getJSONObject("company");// checkitem

									ArrayList<Company> compny = new ArrayList<Company>();
									compny.add(new Company(company
											.getString("id"), company
											.getString("name")));
									personnelUser = new ArrayList<ReportPersonnelUser>();

									personnelUser
											.add(new ReportPersonnelUser(
													puser.getString("id"),
													puser.getString("first_name"),
													puser.getString("last_name"),
													puser.getString("full_name"),
													puser.getString("email"),
													puser.getString("phone"),compny));

									
									person.add(new ReportPersonnel(
											count.getString("id"),
											personnelUser,
											count.getString("hours")
											));
								}
							}
						}
							Log.d("", "person size=" + person.size());
							JSONArray reportCompany = reportobj
									.getJSONArray("report_companies");
							if (reportCompany.length() == 0) {
								// person.add(new ReportPersonnel("",null,
								// ""));
							} else {
								Log.e("report companies", "times = " + i);

								for (j = 0; j < reportCompany.length(); j++) {
									JSONObject count = reportCompany
											.getJSONObject(j);
									Log.e("report companies", "times j = " + j);
									JSONObject company = count.
											getJSONObject("company");
									
//									JSONArray cousers = company.getJSONArray("users");
//									if(cousers.length()!=0)
//									{
//										for(int k=0;k<cousers.length();k++)
//										{
//											Log.e("report companies", "times k = " + k);
//											JSONObject ccount = cousers
//													.getJSONObject(k);
//											coUsers.add(new ReportCompanyUsers(ccount.getString("id"), ccount.getString("first_name"), 
//													ccount.getString("last_name"), ccount.getString("full_name"), ccount.getString("email"), 
//													ccount.getString("phone")));
//										}
//									}
//									JSONArray cosubs = company.getJSONArray("subcontractors");
//									coSubUsers = new ArrayList<ReportCompanyUsers>();
//									if(cosubs.length()!=0)
//									{
//										for(int k=0;k<cosubs.length();k++)
//										{
//											Log.e("report companies", "times k2 = " + k);
//											JSONObject ccount = cosubs
//													.getJSONObject(k);
//											
//											JSONArray couser = ccount.getJSONArray("users");
//											if(couser.length()!=0)
//											{
//											for(int m=0;m<couser.length();m++)
//											{
//												Log.e("report companies", "times m = " + k);
//												JSONObject cccount = couser.getJSONObject(m);
//												
//												coSubUsers.add(new ReportCompanyUsers(cccount.getString("id"), cccount.getString("first_name"), 
//													cccount.getString("last_name"), cccount.getString("full_name"), cccount.getString("email"), 
//													cccount.getString("phone")));
//												
//											}
//											coSubs.add(new ReportCompanySubcontractors(ccount.getString("id"), ccount.getString("name"),
//													coSubUsers, ccount.getString("users_count")));
//											}
//										}
//									}
									ArrayList<ReportCompany>Rcompany = new ArrayList<ReportCompany>();
									Rcompany.add(new ReportCompany(company.getString("id"), company.getString("name"), coUsers, coSubs));
									
									companies.add(new ReportCompanies(count.getString("id"), count.getString("count"), Rcompany));
								
						
							}
						}
//							ReportTopics = new ArrayList<ReportTopics>();
							JSONArray report_topics = reportobj
									.getJSONArray("report_topics");
							for (j = 0; j < report_topics.length(); j++) {
								JSONObject obj = report_topics.getJSONObject(j);
								safe = new ArrayList<SafetyTopics>();
								JSONObject safety_topic = obj.getJSONObject("safety_topic");
								
								safe.add(new SafetyTopics(safety_topic.getString("id"),
								safety_topic.getString("title"),
								safety_topic.getString("info")));
								
								ReportTopics.add(new ReportTopics(obj.getString("id"), obj.getString("report_id"), safe));
								
							}
							Log.d("Safe ","Size= "+ReportTopics.size());
							
//						companies.get(i).
						//Log.d("","sizecouser= "+coUsers.size()+"cosubs"+coSubs.size()+"cosubuser"+coSubUsers.size()+"company"+companies.size());
						/*------- possible_types array-------*/
						JSONArray possible_types = reportobj
								.getJSONArray("possible_types");

						for (int j = 0; j < possible_types.length(); j++) {

							type = possible_types.getString(j);
							// Log.d("possible type", "" + type);
						//	possibleTypesArray.add(type);

						}
						if (reportType.equals("Daily")) {
							reportdataDaily.add((new Report(reportId,
									epochTime, createdAt, updatedat,
									createdDate, title, reportType,
									body, weather, weathericon, precip,
									temp, wind, humidity, author,
									photo, person, companies,ReportTopics)));
						} else if (reportType.equals("Safety")) {
							reportdataSafety.add((new Report(reportId,
									epochTime, createdAt, updatedat,
									createdDate, title, reportType,
									body, weather, weathericon, precip,
									temp, wind, humidity, author,
									photo, person, companies,ReportTopics)));
						} else if (reportType.equals("Weekly")) {
							reportdataWeekly.add((new Report(reportId,
									epochTime, createdAt, updatedat,
									createdDate, title, reportType,
									body, weather, weathericon, precip,
									temp, wind, humidity, author,
									photo, person, companies,ReportTopics)));
						}
						reportdata.add(new Report(reportId, epochTime,
								createdAt, updatedat, createdDate,
								title, reportType, body, weather,
								weathericon, precip, temp, wind,
								humidity, author, photo, person, companies,ReportTopics));
						
//						Log.d("",""+reportdata.get(i).author);
						/*------- comments array-------*/
						JSONArray comments = reportobj
								.getJSONArray("comments");
						commnt = new ArrayList<Comments>();

						/*------- report_users array-------*/
						JSONArray report_users = reportobj
								.getJSONArray("report_users");
						for (j = 0; j < report_users.length(); j++) {
						}
						/*------- safety_topics array-------*/
						
						/*------- report_subs array-------*/
						JSONArray report_subs = reportobj
								.getJSONArray("report_subs");
						for (j = 0; j < report_subs.length(); j++) {
						}

					}
					
					if(Prefrences.reportlisttypes==0)
					{
					reportAdapter = new ReportListAdapter(
							getActivity(), reportdata);
					reportlist.setAdapter(reportAdapter);
					tv_daily.setBackgroundResource(R.drawable.all_white_background);
					tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
					tv_safety.setBackgroundResource(R.color.white);

					tv_safety.setTextColor(Color.BLACK);
					tv_weekly.setTextColor(Color.BLACK);
					tv_daily.setTextColor(Color.BLACK);
					}
					else if(Prefrences.reportlisttypes==1)
					{
					reportAdapter = new ReportListAdapter(
							getActivity(), reportdataDaily);
					reportlist.setAdapter(reportAdapter);
					tv_daily.setBackgroundResource(R.drawable.all_black_background);
					tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
					tv_safety.setBackgroundResource(R.color.white);

					tv_safety.setTextColor(Color.BLACK);
					tv_weekly.setTextColor(Color.BLACK);
					tv_daily.setTextColor(Color.WHITE);
					}
					else if(Prefrences.reportlisttypes==2)
					{
					reportAdapter = new ReportListAdapter(
							getActivity(), reportdataSafety);
					reportlist.setAdapter(reportAdapter);
					tv_daily.setBackgroundResource(R.drawable.all_white_background);
					tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
					tv_safety.setBackgroundResource(R.color.black);

					tv_safety.setTextColor(Color.WHITE);
					tv_weekly.setTextColor(Color.BLACK);
					tv_daily.setTextColor(Color.BLACK);
					}
					else if(Prefrences.reportlisttypes==3)
					{
					reportAdapter = new ReportListAdapter(
							getActivity(), reportdataWeekly);
					reportlist.setAdapter(reportAdapter);
					tv_daily.setBackgroundResource(R.drawable.all_white_background);
					tv_weekly.setBackgroundResource(R.drawable.complete_black_background);
					tv_safety.setBackgroundResource(R.color.white);

					tv_safety.setTextColor(Color.BLACK);
					tv_weekly.setTextColor(Color.WHITE);
					tv_daily.setTextColor(Color.BLACK);
					}
					
//					reportAdapter.notifyDataSetChanged();
					
					// Log.d("photourl size",""+photo_url.size());
					for (int i = 0; i < reportdataDaily.size(); i++) {
						// Log.d("photo url",""+reportdata_daily.get(i).photos.get(0).url200);
						Log.d("photo url", "" + i + ":"
								+ reportdataDaily.get(i).photos.size());
					}
					if (pull == true) {

						pull = false;
//						daily.setBackgroundResource(R.drawable.all_white_background);
//						weekly.setBackgroundResource(R.drawable.complete_white_background);
//						safety.setBackgroundResource(R.color.white);
//
//						safety.setTextColor(Color.BLACK);
//						weekly.setTextColor(Color.BLACK);
//						daily.setTextColor(Color.BLACK);
						reportlist.onRefreshComplete();
					}
					 reportAdapter.notify();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} 
		}
	}

	// ************ API for GEt Project reports. *************//
	public void getProjectReports(final String projectId) {
		/*
		 * Required Parameters Òpunchlist_itemÓ : { ÒbodyÓ : ÒRemove moldÓ,
		 * Òuser_idÓ : 2, ÒlocationÓ : ÒBasementÓ, Òuser_assigneeÓ : ÒMax
		 * Haines-StilesÓ }, Òproject_idÓ : {project.id}
		 */

		Prefrences.showLoadingDialog(getActivity(), "Loading...");
		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		
		client.get(getActivity(), Prefrences.url + "/reports/" + projectId,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						
						Editor editor = sharedpref.edit();
						editor.putString("reportlistfragment", response);
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
							reportlist.onRefreshComplete();
						}
					}
				});
	}
	
	public void fillServerData(String response){
		JSONObject res = null;
		try {
			
			res = new JSONObject(response);
			Prefrences.report_bool=true;
			Prefrences.report_s = response;
			Log.v("response ", "" + res.toString(2));
			
			JSONArray reportArrray = res.getJSONArray("reports");
			reportdata = new ArrayList<Report>();
			reportdataDaily = new ArrayList<Report>();
			reportdataSafety = new ArrayList<Report>();
			reportdataWeekly = new ArrayList<Report>();

			Log.d("report Array length","" + reportArrray.length());
			
			for (int i = 0; i < reportArrray.length(); i++) {
				companies= new ArrayList<ReportCompanies>();
				ReportTopics = new ArrayList<ReportTopics>();
				person= new ArrayList<ReportPersonnel>();

				coUsers = new ArrayList<ReportCompanyUsers>();
				
				coSubs = new ArrayList<ReportCompanySubcontractors>();

		
				
				JSONObject reportobj = reportArrray.getJSONObject(i);
				
				String reportId = reportobj.getString("id");
				Log.d("report_id", "-----------***" + reportId);
				try{
				if (reportId.equals("null")) {
					reportId = "";
				}
				}catch(Exception e){
					reportId = "";
				}
				
				
				String epochTime = reportobj.getString("epoch_time");
				if (epochTime.equals("null")) {
					epochTime = "";
				}
				String createdAt = reportobj
						.getString("created_at");
				if (createdAt.equals("null")) {
					createdAt = "";
				}
				String updatedat = reportobj
						.getString("updated_at");
				if (updatedat.equals("null")) {
					updatedat = "";
				}
				String createdDate = reportobj
						.getString("date_string");
				if (createdDate.equals("null")) {
					createdDate = "";
				}
				String title = reportobj.getString("title");
				if (title.equals("null")) {
					title = "";
				}
				String reportType = reportobj
						.getString("report_type");
				String body;
				if (reportobj.has("body")) {
					body = reportobj.getString("body");
					// It exists, do your stuff
				} else {
					body = "N/A";
					// It doesn't exist, do nothing
				}

				if (reportType.equals("null")) {
					reportType = "";
				}
				String weather = reportobj.getString("weather");
				if (weather.equals("null")) {
					weather = "";
				}
				String weathericon = reportobj
						.getString("weather_icon");
				if (weathericon.equals("null")) {
					weathericon = "";
				}
				String precip = reportobj.getString("precip");
				if (precip.equals("null") ||precip.equals("") ) {
					precip = "";
				}
			    else{
			    	precip=precip.substring(0, precip.length()-1);
			    	Log.d("waaaooooo","waaaaaooo "+precip.toString());

			    	precip=String.format("%.1f", Float.parseFloat(precip))+"%";
			    	}
				String temp = reportobj.getString("temp");
				if (temp.equals("null")) {
					temp = "";
				}
				String wind = reportobj.getString("wind");
				if (wind.equals("null")) {
					wind = "";
				}
				String humidity = reportobj
						.getString("humidity");
				if (humidity.equals("null") ||humidity.equals("")) {
					humidity = "";
				}
				else{
			    	humidity=humidity.substring(0, humidity.length()-1);
			    	Log.d("waaaooooo","waaaaaooo "+humidity.toString());

			    	humidity=String.format("%.1f", Float.parseFloat(humidity))+"%";
			    	}

				
				/*------- author object-------*/
				if (!reportobj.isNull("author")) {
					author = new ArrayList<Author>();
					
					JSONObject authorobj = reportobj.getJSONObject("author");
					
					String authorid = authorobj.getString("id");
					if (authorid.equals("null")) {
						authorid = "";
					}
					String firstname = authorobj.getString("first_name");
					if (firstname.equals("null")) {
						firstname = "";
					}
					String lastname = authorobj.getString("last_name");
					if (lastname.equals("null")) {
						lastname = "";
					}
					String fullname = authorobj.getString("full_name");
					if (fullname.equals("null")) {
						fullname = "";
					}
					String email = authorobj.getString("email");
					if (email.equals("null")) {
						email = "";
					}
					String phonenumber = authorobj
							.getString("phone");
					if (phonenumber.equals("null")) {
						phonenumber = "";
					}
					// Log.d("authorobj",
					// "-------------authorobj data start---------");
					// Log.d("authorid", "" + authorid);
					// Log.d("first_name", "" + first_name);
					// Log.d("last_name", "" + last_name);
					// Log.d("full_name", "" + full_name);
					// Log.d("email", "" + email);
					//
					// Log.d("phone_number", "" + phone_number);
					// Log.d("authorobj",
					// "-------------authorobj data end---------");
					author.add(new Author(authorid, firstname,
							lastname, fullname, email,
							phonenumber));
				} else {
					author = new ArrayList<Author>();
					author.add(new Author("", "", "", "", "",
							""));
				}
				/*------- report_fields array-------*/
				JSONArray report_fields = reportobj
						.getJSONArray("report_fields");
				for (int j = 0; j < report_fields.length(); j++) {
				}

				// if (reportobj.isNull("photos")) {
				// photo = new ArrayList<ProjectPhotos>();
				// Log.d("if","----photo if--");
				// photo_url.add("http://3.bp.blogspot.com/-1EULR14bUFc/T8srSY2KZqI/AAAAAAAAEOk/vTI6mnpZ43g/s1600/nature-wallpaper-15.jpg");
				// Log.d("----photo url---",""+"fake");
				// photo.add(new ProjectPhotos("", "", "",
				// "http://3.bp.blogspot.com/-1EULR14bUFc/T8srSY2KZqI/AAAAAAAAEOk/vTI6mnpZ43g/s1600/nature-wallpaper-15.jpg",
				// "", "", "", "", "", "", "", "", ""));
				// }
				// else if (!reportobj.isNull("photos")) {
				// Log.d("else","----photo else--");
				//
				JSONArray photos = reportobj
						.getJSONArray("photos");
				if (photos.length() == 0) {
					photo = new ArrayList<ProjectPhotos>();
					Log.d("if", "----photo if null--");
					photo.add(new ProjectPhotos("", "", "",
							"drawable", "", "", "", "", "", "",
							"", "", "", ""));
					// photo_url.add("http://3.bp.blogspot.com/-1EULR14bUFc/T8srSY2KZqI/AAAAAAAAEOk/vTI6mnpZ43g/s1600/nature-wallpaper-15.jpg");
				} else {
					photo = new ArrayList<ProjectPhotos>();
					for (int k = 0; k < photos.length(); k++) {

						JSONObject photosobj = photos
								.getJSONObject(k);
						String photoid = photosobj
								.getString("id");
						String url_large = photosobj
								.getString("url_large");
						String original = photosobj
								.getString("original");
						String photo_url200 = photosobj
								.getString("url_small");
						String url100 = photosobj
								.getString("url_thumb");
						String photo_epoch_time = photosobj
								.getString("epoch_time");
						String photo_url_small = photosobj
								.getString("url_small");
						String photo_url_thumb = photosobj
								.getString("url_thumb");
						String image_file_size = photosobj
								.getString("image_file_size");
						String image_content_type = photosobj
								.getString("image_content_type");

						String source = photosobj
								.getString("source");
						String phase = photosobj
								.getString("phase");
						String photo_created_at = photosobj
								.getString("created_at");
						String user_name = photosobj
								.getString("user_name");
						String name = photosobj
								.getString("name");

						String desc = photosobj
								.getString("description");
						String photo_created_date = photosobj
								.getString("date_string");

						// Log.d("photosdata ",
						// "------------------photosdata start----------------------");
						// Log.d("photoid", ":" + photoid);
						// Log.d("url_large", ":" + url_large);
						// Log.d("original", ":" + original);
						// Log.d("url200", ":" + photo_url200);
						// Log.d("url100", ":" + url100);
						// Log.d("epoch_time", ":" +
						// photo_epoch_time);
						// Log.d("url_small", ":" +
						// photo_url_small);
						// Log.d("url_thumb", ":" +
						// photo_url_thumb);
						// Log.d("image_file_size", ":"
						// + image_file_size);
						// Log.d("image_content_type", ":"
						// + image_content_type);
						// Log.d("source", ":" + source);
						// Log.d("phase", ":" + phase);
						// Log.d("created_at", ":" +
						// photo_created_at);
						// Log.d("user_name", ":" + user_name);
						// Log.d("name", ":" + name);
						// Log.d("created_date", ":"
						// + photo_created_date);
						//
						// Log.d("photosdata ",
						// "-----------------photosdata end----------------------");
						// photo_url.add(photo_url200);
						Log.d("----photo url---", ""
								+ photo_url200);
						photo.add(new ProjectPhotos(photoid,
								url_large, original,
								photo_url200, url100,
								image_file_size,
								image_content_type, source,
								phase, photo_created_at,
								user_name, name, desc,
								photo_created_date));
					}
				}

				/*------- personnel array-------*/
//
//				JSONArray personnel = reportobj
//						.getJSONArray("personnel");
//				if (personnel.length() == 0) {
//					// person.add(new ReportPersonnel("",null,
//					// ""));
//				} else {
//					Log.e("", "how many times" + i);
//
//					for (j = 0; j < personnel.length(); j++) {
//
//						JSONObject count = personnel
//								.getJSONObject(j);
//						Log.d("", "count : " + count);
//						if (count.isNull("user")) {
//							
////							JSONObject psubs = count
////									.getJSONObject("sub");
////
////							Log.d("", "psubs : " + psubs);
////							psub = new ArrayList<subcontractors>();
////
////							psub.add(new subcontractors(psubs
////									.getString("id"), psubs
////									.getString("name"), psubs
////									.getString("email"), psubs
////									.getString("phone"), psubs
////									.getString("count")));
////							person.add(new ReportPersonnel(
////									count.getString("id"),
////									null, null, psub, count
////											.getString("count")));
//						} else {
//
//							JSONObject puser = count
//									.getJSONObject("user");
//
//							Log.d("", "puser : " + puser);
//							personnelUser = new ArrayList<ReportPersonnelUser>();
//
//							personnelUser
//									.add(new ReportPersonnelUser(
//											puser.getString("id"),
//											puser.getString("first_name"),
//											puser.getString("last_name"),
//											puser.getString("full_name"),
//											puser.getString("email"),
//											puser.getString("phone")));
//
//							// if(count.isNull("hours"))
//							// {
//							// person.add(new
//							// ReportPersonnel(count.getString("id"),
//							// psub,count.getString("count")));
//							// }
//							// else
//							person.add(new ReportPersonnel(
//									count.getString("id"),
//									personnelUser,
//									count.getString("hours")
//									));
//						}
//					}
//				}
//					Log.d("", "person size=" + person.size());
					JSONArray reportCompany = reportobj
							.getJSONArray("report_companies");
					if (reportCompany.length() == 0) {
						// person.add(new ReportPersonnel("",null,
						// ""));
					} else {
						Log.e("report companies", "times = " + i);

						for (j = 0; j < reportCompany.length(); j++) {
							JSONObject count = reportCompany
									.getJSONObject(j);
							Log.e("report companies", "times j = " + j);
							JSONObject company = count.
									getJSONObject("company");
							
//							JSONArray cousers = company.getJSONArray("users");
//							if(cousers.length()!=0)
//							{
//								for(int k=0;k<cousers.length();k++)
//								{
//									Log.e("report companies", "times k = " + k);
//									JSONObject ccount = cousers
//											.getJSONObject(k);
//									coUsers.add(new ReportCompanyUsers(ccount.getString("id"), ccount.getString("first_name"), 
//											ccount.getString("last_name"), ccount.getString("full_name"), ccount.getString("email"), 
//											ccount.getString("phone")));
//								}
//							}
//							JSONArray cosubs = company.getJSONArray("subcontractors");
							coSubUsers = new ArrayList<ReportCompanyUsers>();
//							if(cosubs.length()!=0)
//							{
//								for(int k=0;k<cosubs.length();k++)
//								{
//									Log.e("report companies", "times k2 = " + k);
//									JSONObject ccount = cosubs
//											.getJSONObject(k);
//									
//									JSONArray couser = ccount.getJSONArray("users");
//									if(couser.length()!=0)
//									{
//									for(int m=0;m<couser.length();m++)
//									{
//										Log.e("report companies", "times m = " + m);
//										JSONObject cccount = couser.getJSONObject(m);
//										
//										coSubUsers.add(new ReportCompanyUsers(cccount.getString("id"), cccount.getString("first_name"), 
//											cccount.getString("last_name"), cccount.getString("full_name"), cccount.getString("email"), 
//											cccount.getString("phone")));
//										
//									}
//									coSubs.add(new ReportCompanySubcontractors(ccount.getString("id"), ccount.getString("name"),
//											coSubUsers, ccount.getString("users_count")));
//									}
//								}
//							}
							ArrayList<ReportCompany>Rcompany = new ArrayList<ReportCompany>();
							Rcompany.add(new ReportCompany(company.getString("id"), company.getString("name"), coUsers, coSubs));
							
							companies.add(new ReportCompanies(count.getString("id"), count.getString("count"), Rcompany));
						
				
					}
				}
					
					/*------- report_users array-------*/
					JSONArray report_users = reportobj
							.getJSONArray("report_users");
					
					if (report_users.length() == 0) {
						// person.add(new ReportPersonnel("",null,
						// ""));
					} else {
						Log.e("", "how many times" + i);

						for (j = 0; j < report_users.length(); j++) {

							JSONObject count = report_users
									.getJSONObject(j);
							Log.d("", "count : " + count);
							if (count.isNull("user")) {
								
//								JSONObject psubs = count
//										.getJSONObject("sub");
	//
//								Log.d("", "psubs : " + psubs);
//								psub = new ArrayList<subcontractors>();
	//
//								psub.add(new subcontractors(psubs
//										.getString("id"), psubs
//										.getString("name"), psubs
//										.getString("email"), psubs
//										.getString("phone"), psubs
//										.getString("count")));
//								person.add(new ReportPersonnel(
//										count.getString("id"),
//										null, null, psub, count
//												.getString("count")));
							} else {

								JSONObject puser = count
										.getJSONObject("user");
								
								JSONObject company = puser
										.getJSONObject("company");// checkitem

								ArrayList<Company> compny = new ArrayList<Company>();
								compny.add(new Company(company
										.getString("id"), company
										.getString("name")));

								Log.d("", "puser : " + puser);
								personnelUser = new ArrayList<ReportPersonnelUser>();

								personnelUser
										.add(new ReportPersonnelUser(
												puser.getString("id"),
												puser.getString("first_name"),
												puser.getString("last_name"),
												puser.getString("full_name"),
												puser.getString("email"),
												puser.getString("phone"),compny));

								// if(count.isNull("hours"))
								// {
								// person.add(new
								// ReportPersonnel(count.getString("id"),
								// psub,count.getString("count")));
								// }
								// else
								person.add(new ReportPersonnel(
										count.getString("id"),
										personnelUser,
										count.getString("hours")
										));
							}
						}
					}
					for (j = 0; j < report_users.length(); j++) {
						
						
						
					}
					
					/*------- safety_topics array-------*/
					
//					ReportTopics = new ArrayList<ReportTopics>();
					JSONArray report_topics = reportobj
							.getJSONArray("report_topics");
					for (j = 0; j < report_topics.length(); j++) {
						JSONObject obj = report_topics.getJSONObject(j);
						safe = new ArrayList<SafetyTopics>();
						JSONObject safety_topic = obj.getJSONObject("safety_topic");
						
						safe.add(new SafetyTopics(safety_topic.getString("id"),
						safety_topic.getString("title"),
						safety_topic.getString("info")));
						
						ReportTopics.add(new ReportTopics(obj.getString("id"), obj.getString("report_id"), safe));
						
					}
					Log.d("Safe ","Size= "+ReportTopics.size());
					
//				companies.get(i).
				//Log.d("","sizecouser= "+coUsers.size()+"cosubs"+coSubs.size()+"cosubuser"+coSubUsers.size()+"company"+companies.size());
				/*------- possible_types array-------*/
				JSONArray possible_types = reportobj
						.getJSONArray("possible_types");

				for (int j = 0; j < possible_types.length(); j++) {

					type = possible_types.getString(j);
					// Log.d("possible type", "" + type);
				//	possibleTypesArray.add(type);

				}
				if (reportType.equals("Daily")) {
					reportdataDaily.add((new Report(reportId,
							epochTime, createdAt, updatedat,
							createdDate, title, reportType,
							body, weather, weathericon, precip,
							temp, wind, humidity, author,
							photo, person, companies,ReportTopics)));
				} else if (reportType.equals("Safety")) {
					reportdataSafety.add((new Report(reportId,
							epochTime, createdAt, updatedat,
							createdDate, title, reportType,
							body, weather, weathericon, precip,
							temp, wind, humidity, author,
							photo, person, companies,ReportTopics)));
				} else if (reportType.equals("Weekly")) {
					reportdataWeekly.add((new Report(reportId,
							epochTime, createdAt, updatedat,
							createdDate, title, reportType,
							body, weather, weathericon, precip,
							temp, wind, humidity, author,
							photo, person, companies,ReportTopics)));
				}
				reportdata.add(new Report(reportId, epochTime,
						createdAt, updatedat, createdDate,
						title, reportType, body, weather,
						weathericon, precip, temp, wind,
						humidity, author, photo, person, companies,ReportTopics));
				
//				Log.d("",""+reportdata.get(i).author);
				/*------- comments array-------*/
				JSONArray comments = reportobj
						.getJSONArray("comments");
				commnt = new ArrayList<Comments>();
				// for (int j = 0; j < comments.length(); j++) {
				//
				//
				// JSONObject commentsobj = comments
				// .getJSONObject(j);
				// String commentsid = commentsobj
				// .getString("id");
				// String commentsbody = commentsobj
				// .getString("body");
				// String comments_created_at = commentsobj
				// .getString("created_at");
				// // Log.d("comments data ",
				// //
				// "-----------------comments data start-----------------------------");
				// // Log.d("commentsid", ":" + commentsid);
				// // Log.d("commentsbody", ":" + commentsbody);
				// // Log.d("comments_created_at", ":"
				// // + comments_created_at);
				//
				// JSONObject userobj = commentsobj
				// .getJSONObject("user");
				// cusr = new ArrayList<CommentUser>();
				// String userid = userobj.getString("id");
				// String comment_first_name = userobj
				// .getString("first_name");
				// String comment_last_name = userobj
				// .getString("last_name");
				// String comment_full_name = userobj
				// .getString("full_name");
				// String admin = userobj.getString("admin");
				// String company_admin = userobj
				// .getString("company_admin");
				// String uber_admin = userobj
				// .getString("uber_admin");
				// String comment_email = userobj
				// .getString("email");
				// String comment_phone_number = userobj
				// .getString("phone_number");
				// String authentication_token = userobj
				// .getString("authentication_token");
				// String url200 = userobj.getString("url200");
				// String url_thumb = userobj
				// .getString("url_thumb");
				// String url_small = userobj
				// .getString("url_small");
				// // Log.d("user data in comments",
				// //
				// "------------user data in comments start-------");
				// // Log.d("userid", ":" + userid);
				// // Log.d("first_name", ":"
				// // + comment_first_name);
				// // Log.d("last_name", ":" +
				// comment_last_name);
				// // Log.d("full_name", ":" +
				// comment_full_name);
				// // Log.d("admin", ":" + admin);
				// // Log.d("company_admin", ":" +
				// company_admin);
				// // Log.d("uber_admin", ":" + uber_admin);
				// // Log.d("email", ":" + comment_email);
				// // Log.d("phone_number", ":"
				// // + comment_phone_number);
				// // Log.d("authentication_token", ":"
				// // + authentication_token);
				// // Log.d("url200", ":" + url200);
				// // Log.d("url_thumb", ":" + url_thumb);
				// // Log.d("url_small", ":" + url_small);
				// // Log.d("userdata in comments ",
				// //
				// "-------------------userdata in comments end-------------------------");
				// // Log.d("comments data ",
				// //
				// "-------------------comments data end-------------------------");
				// // cusr.add(new CommentUser(userid,
				// // comment_first_name, comment_last_name,
				// // comment_full_name, admin, company_admin,
				// // uber_admin, comment_email,
				// // comment_phone_number,
				// // authentication_token, url_thumb,
				// // url_small, company) ;
				// /*------- photos array-------*/
				// }

				
				
				/*------- report_subs array-------*/
				JSONArray report_subs = reportobj
						.getJSONArray("report_subs");
				for (j = 0; j < report_subs.length(); j++) {
				}

				// JSONArray comm =
				// count.getJSONArray("comments");
				//
				// for (int k = 0; k < comm.length(); k++) {
				//
				// JSONObject ccount = comm.getJSONObject(k);
				//
				// JSONObject cuser = ccount
				// .getJSONObject("user");
				// Log.i("comment user", "" + cuser);
				//
				// commnt = new ArrayList<Comments>();
				// cusr = new ArrayList<CommentUser>();
				// JSONObject company = cuser
				// .getJSONObject("company");
				//
				// ArrayList<Company> compny = new
				// ArrayList<Company>();
				// compny.add(new Company(company
				// .getString("id"), company
				// .getString("name")));
				// // Log.i("ddddddd", "" + company);
				// cusr.add(new CommentUser(cuser
				// .getString("id"), cuser

			}
			if(Prefrences.reportlisttypes==0)
			{
			reportAdapter = new ReportListAdapter(
					getActivity(), reportdata);
			reportlist.setAdapter(reportAdapter);
			tv_daily.setBackgroundResource(R.drawable.all_white_background);
			tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			tv_safety.setBackgroundResource(R.color.white);

			tv_safety.setTextColor(Color.BLACK);
			tv_weekly.setTextColor(Color.BLACK);
			tv_daily.setTextColor(Color.BLACK);
			}
			else if(Prefrences.reportlisttypes==1)
			{
			reportAdapter = new ReportListAdapter(
					getActivity(), reportdataDaily);
			reportlist.setAdapter(reportAdapter);
			tv_daily.setBackgroundResource(R.drawable.all_black_background);
			tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			tv_safety.setBackgroundResource(R.color.white);

			tv_safety.setTextColor(Color.BLACK);
			tv_weekly.setTextColor(Color.BLACK);
			tv_daily.setTextColor(Color.WHITE);
			}
			else if(Prefrences.reportlisttypes==2)
			{
			reportAdapter = new ReportListAdapter(
					getActivity(), reportdataSafety);
			reportlist.setAdapter(reportAdapter);
			tv_daily.setBackgroundResource(R.drawable.all_white_background);
			tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			tv_safety.setBackgroundResource(R.color.black);

			tv_safety.setTextColor(Color.WHITE);
			tv_weekly.setTextColor(Color.BLACK);
			tv_daily.setTextColor(Color.BLACK);
			}
			else if(Prefrences.reportlisttypes==3)
			{
			reportAdapter = new ReportListAdapter(
					getActivity(), reportdataWeekly);
			reportlist.setAdapter(reportAdapter);
			tv_daily.setBackgroundResource(R.drawable.all_white_background);
			tv_weekly.setBackgroundResource(R.drawable.complete_black_background);
			tv_safety.setBackgroundResource(R.color.white);

			tv_safety.setTextColor(Color.BLACK);
			tv_weekly.setTextColor(Color.WHITE);
			tv_daily.setTextColor(Color.BLACK);
			}
			
//			reportAdapter.notifyDataSetChanged();
			
			// Log.d("photourl size",""+photo_url.size());
			for (int i = 0; i < reportdataDaily.size(); i++) {
				// Log.d("photo url",""+reportdata_daily.get(i).photos.get(0).url200);
				Log.d("photo url", "" + i + ":"
						+ reportdataDaily.get(i).photos.size());
			}
			if (pull == true) {

				pull = false;
//				daily.setBackgroundResource(R.drawable.all_white_background);
//				weekly.setBackgroundResource(R.drawable.complete_white_background);
//				safety.setBackgroundResource(R.color.white);
//
//				safety.setTextColor(Color.BLACK);
//				weekly.setTextColor(Color.BLACK);
//				daily.setTextColor(Color.BLACK);
				reportlist.onRefreshComplete();
			}
			 reportAdapter.notify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
