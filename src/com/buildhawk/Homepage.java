package com.buildhawk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.Address;
import com.buildhawk.utils.Company;
import com.buildhawk.utils.ProjectsFields;
import com.buildhawk.utils.Users;
import com.buildhawk.utils.subcontractors;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.squareup.picasso.Picasso;

public class Homepage extends SlidingFragmentActivity {
	Button sliding, logout, userName;
	ImageView userPic;
	Model model;
	ListView membersList, projectList, searchList;
	LazyAdapter memberAdap;
	ProjectsAdapter projectsAdap;
	RelativeLayout relLay;
	RelativeLayout relLay2, listOutside;
	String currentDateandTime;
	TextView currTime;
	Button email, text, call, cancel;
	TextView expiryAlert;
	Calendar cal;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	EditText listSearch;
	public static ArrayList<ProjectsFields> projectsList = new ArrayList<ProjectsFields>();
	public static ArrayList<Users> user2 = new ArrayList<Users>();
	public static ArrayList<subcontractors> sub2 = new ArrayList<subcontractors>();
	public static ArrayList<String> usr = new ArrayList<String>();
	public static ArrayList<String> compny = new ArrayList<String>();
	public static ArrayList<String> compnyId = new ArrayList<String>();
	public static ArrayList<String> userid = new ArrayList<String>();
	static Dialog popup;

	@SuppressLint({ "SimpleDateFormat", "ShowToast" })
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);

		setBehindContentView(R.layout.frame);
		relLay = (RelativeLayout) findViewById(R.id.rellay);
		new ASSL(this, relLay, 1134, 720, false);

		relLay2 = (RelativeLayout) findViewById(R.id.rellay1);
		new ASSL(this, relLay2, 1134, 720, false);
		sliding = (Button) findViewById(R.id.sliding);
		userName = (Button) findViewById(R.id.username);
		userPic = (ImageView) findViewById(R.id.user_pic);
		membersList = (ListView) findViewById(R.id.list);
		logout = (Button) findViewById(R.id.logout);
		currTime = (TextView) findViewById(R.id.currTime);
		projectList = (ListView) findViewById(R.id.projectList);
		searchList = (ListView) findViewById(R.id.lvsearch);
		listSearch = (EditText) findViewById(R.id.search);

		this.setSlidingActionBarEnabled(false);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);

		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setFadeEnabled(true);
		getSlidingMenu().setBehindOffsetRes(R.dimen.actionbar_home_width);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSlidingMenu().setBehindWidth((int) (580));
		getSlidingMenu().setBehindScrollScale(0.45f);
		// Log.d("aaaaa","aaaaaa"+Prefrences.coworkrUrl[0]);
		// if(!Prefrences.userPic.equals("null"))
		// Picasso.with(getApplicationContext()).load(Prefrences.userPic).into(user_pic);

		userName.setText(Prefrences.fullName);
		userName.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresent = connDect.isConnectingToInternet();

		memberAdap = new LazyAdapter(Homepage.this);
		membersList.setAdapter(memberAdap);

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		currentDateandTime = sdf.format(new Date());
		currTime.setText(currentDateandTime);
		currTime.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		// popup start
		popup = new Dialog(Homepage.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		// expiry_popup.setCancelable(false);

		popup.setContentView(R.layout.bridge_expiry_popup);
		// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
		RelativeLayout expiryMain = (RelativeLayout) popup
				.findViewById(R.id.list_outside);
		// expiry_main.setInAnimation(R.anim.slide_in_from_top);
		email = (Button) popup.findViewById(R.id.Email);
		call = (Button) popup.findViewById(R.id.Call);
		text = (Button) popup.findViewById(R.id.Text);
		cancel = (Button) popup.findViewById(R.id.Cancel);
		expiryAlert = (TextView) popup.findViewById(R.id.alert_text);
		// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());

		listOutside = (RelativeLayout) popup.findViewById(R.id.list_outside);
		new ASSL(Homepage.this, expiryMain, 1134, 720, false);

		listOutside.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popup.isShowing()) {
					popup.dismiss();
					// overridePendingTransition(R.anim.slide_in_bottom,
					// R.anim.slide_out_to_top);
				}
			}
		});

		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
				Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
				phoneCallIntent.setData(Uri.parse("tel:"
						+ Prefrences.ContactPhone.toString()));
				startActivity(phoneCallIntent);
			}
		});

		email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { Prefrences.ContactMail.toString() });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
				emailIntent.setType("plain/text");
				try {
					popup.dismiss();
					startActivity(Intent.createChooser(emailIntent,
							"Send mail..."));

					Log.i("Finished sending email...", "");
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getApplicationContext(),
							"There is no email client installed.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(
						"sms", Prefrences.ContactPhone.toString(), null)));

			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});

		if (isInternetPresent) {
			projectData();
			// updateReportHit(Homepage.this);
		} else {
			Toast.makeText(getApplicationContext(), "No internet connection.",
					Toast.LENGTH_SHORT);
		}

		listSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				Homepage.this.projectsAdap.search(arg0.toString());
			}
		});

		sliding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});

		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Prefrences.saveAccessToken("454876423564",
						"test@ristrettolabs.com", "password",
						getApplicationContext());
				// Prefrences.authToken="";
				// Prefrences.email="";

				finish();
				startActivity(new Intent(Homepage.this, MainActivity.class));
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
	}

	// “punchlist_item” : {
	// “id” : "208",
	// “body” : “Worklist item”,
	// “location” : “Basemenet”,
	// “user_id” : "2",
	// “status” : "1",
	// “completed_by_user_id” : "2",
	// “user_assignee” : "3"
	// }

	void updateReportHit(Activity con) {

		Prefrences.showLoadingDialog(con, "Loading...");
		JSONObject jsonobj = new JSONObject();

		// JSONArray jo1 = new JSONArray();

		try {
			// // “punchlist_item” : {
			// “id” : "208",
			// “body” : “Worklist item”,
			// “location” : “Basemenet”,
			// “user_id” : "2",
			// “status” : "1",
			// “completed_by_user_id” : "2",
			// “user_assignee” : "3"
			//
			jsonobj.put("id", "208");
			jsonobj.put("body", "worklist itemssss");
			jsonobj.put("location", "kitchen");
			jsonobj.put("user_id", "46");
			jsonobj.put("status", "1");
			jsonobj.put("completed_by_user_id", "46");
			jsonobj.put("user_assignee", "1");

			JSONObject finalJson = new JSONObject();
			finalJson.put("punchlist_item", jsonobj.toString());

			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
					.getBytes("UTF-8"));

			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			client.put(con, Prefrences.url + "/punchlist_items/208", entity,
					"application/json", new AsyncHttpResponseHandler() {
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
							Prefrences.dismissLoadingDialog();
						}

						@Override
						public void onFailure(Throwable arg0) {
							Log.e("request fail", arg0.toString());
							Prefrences.dismissLoadingDialog();
						}
					});

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// ************ API for Projects. *************//

	public void projectData() {

		Prefrences.showLoadingDialog(Homepage.this, "Loading...");

		RequestParams params = new RequestParams();

		params.put("user_id", Prefrences.userId);

		user2.clear();
		sub2.clear();

		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.get(Prefrences.url + "/projects", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);

						JSONObject res = null;
						try {
							res = new JSONObject(response);

							JSONArray projects = res.getJSONArray("projects");
							projectsList.clear();

							for (int i = 0; i < projects.length(); i++) {

								JSONObject count = projects.getJSONObject(i);

								JSONObject address = count
										.getJSONObject("address");

								JSONObject company = count
										.getJSONObject("company");

								JSONArray users = count.getJSONArray("users");

								JSONArray subs = count
										.getJSONArray("company_subs");

								ArrayList<Users> user = new ArrayList<Users>();
								ArrayList<subcontractors> sub = new ArrayList<subcontractors>();

								ArrayList<String> cmpny = new ArrayList<String>();
								ArrayList<String> cmpnyid = new ArrayList<String>();
								ArrayList<String> usrid = new ArrayList<String>();

								// for(int k=0;k<subs.length();k++)
								// {
								// JSONObject kCount = subs.getJSONObject(k);
								//
								// sub.add(new
								// subcontractors(kCount.getString("id"),
								// kCount.getString("name"),
								// kCount.getString("email"),
								// kCount.getString("phone"),
								// kCount.getString("count")));
								//
								// }
								// sub2.addAll(sub);

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
											uCount.getString("authentication_token"),
											uCount.getString("url_thumb"),
											uCount.getString("url_small"),
											new Company(uCompany
													.getString("id"), uCompany
													.getString("name"))));
									// if(!uCount.getString("full_name").equals("null"))
									usr.add(uCount.getString("full_name"));
									cmpny.add(user.get(j).company.coName
											.toString());
									cmpnyid.add(user.get(j).company.coId
											.toString());
									usrid.add(user.get(j).uId.toString());
									Log.d("companieeeesss", "companiess... "
											+ user.get(j).company.coName);

								}
								user2.addAll(user);
								compny.addAll(cmpny);
								compnyId.addAll(cmpnyid);
								userid.addAll(usrid);

								Log.d("usssrrsrsrsrsr",
										"sizeeee" + compny.size());

								Log.d("usssrrsrsrsrsr",
										"sizeeee" + compnyId.size());

								projectsList.add(new ProjectsFields(count
										.getString("id"), count
										.getString("name"), count
										.getString("active"), count
										.getString("core"), count
										.getString("progress"), new Address(
										address.getString("street1"), address
												.getString("street2"), address
												.getString("city"), address
												.getString("zip"), address
												.getString("country"), address
												.getString("phone"),
										address.getString("formatted_address"),
										address.getString("latitude"), address
												.getString("longitude")), user,
										new Company(company.getString("id"),
												company.getString("name"))));
								// compny.add(company.getInt("name"));

							}

							for (int i = 0; i < projectsList.size(); i++) {
								Log.v("name = ", projectsList.get(i).name);
								Log.v("id = ", projectsList.get(i).id);

								for (int k = 0; k < projectsList.get(i).users
										.size(); k++) {
									Log.v("username = ",
											projectsList.get(i).users.get(k).uId
													+ ", "
													+ projectsList.get(i).users
															.get(k).uFirstName);

								}
							}

							projectsAdap = new ProjectsAdapter(Homepage.this);
							projectList.setAdapter(projectsAdap);

						} catch (Exception e) {
							e.printStackTrace();
						}
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Prefrences.dismissLoadingDialog();
					}
				});

	}

}
