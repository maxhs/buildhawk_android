package com.buildhawk;

/*
 *  This file is used to display the home screen of an app in this it display list of projects and users in left side. 
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class Homepage extends SlidingFragmentActivity {
	Button buttonSliding, buttonLogout, buttonUserName;
	ImageView imageviewUserPic;
	Model model;
	ListView listviewMembers, listviewSearch;
	PullToRefreshListView pullToRefreshListView;
	Boolean pullBoolean = false;
	LazyAdapter memberAdap;
	ProjectsAdapter projectsAdap;
	RelativeLayout relativelayoutRoot;
	RelativeLayout relativelayoutRootFrame, relativelayoutListOutside;
	String currentDateandTimeString;
	TextView textviewCurrTime;
	Button buttonEmail, buttonText, buttonCall, buttonCancel;
	TextView textviewExpiryAlert;
	Calendar cal;
	ConnectionDetector connDect;
	Boolean isInternetPresentBoolean = false;
	EditText edittextListSearch;
	public static ArrayList<ProjectsFields> projectsArrayList = new ArrayList<ProjectsFields>();
	public  ArrayList<Users> user2ArrayList = new ArrayList<Users>();
	public  ArrayList<subcontractors> sub2ArrayList = new ArrayList<subcontractors>();
	public  ArrayList<String> usrArrayList = new ArrayList<String>();
	public  ArrayList<String> compnyArrayList = new ArrayList<String>();
	public  ArrayList<String> compnyIdArrayList = new ArrayList<String>();
	public  ArrayList<String> useridArrayList = new ArrayList<String>();
	static Dialog popup;
	SharedPreferences sharedpref;

	@SuppressLint({ "SimpleDateFormat", "ShowToast" })
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);

		setBehindContentView(R.layout.frame);
		relativelayoutRoot = (RelativeLayout) findViewById(R.id.relativelayoutRootHome);
		new ASSL(this, relativelayoutRoot, 1134, 720, false);

		relativelayoutRootFrame = (RelativeLayout) findViewById(R.id.relativelayoutRootFrame);
		new ASSL(this, relativelayoutRootFrame, 1134, 720, false);
		buttonSliding = (Button) findViewById(R.id.buttonSliding);
		buttonUserName = (Button) findViewById(R.id.buttonUserName);
		imageviewUserPic = (ImageView) findViewById(R.id.imageviewUserPic);
		listviewMembers = (ListView) findViewById(R.id.listviewMembers);
		buttonLogout = (Button) findViewById(R.id.buttonLogout);
		textviewCurrTime = (TextView) findViewById(R.id.textviewCurrTime);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
		listviewSearch = (ListView) findViewById(R.id.listviewSearch);
		edittextListSearch = (EditText) findViewById(R.id.edittextListSearch);

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

		buttonUserName.setText(Prefrences.fullName);
		buttonUserName.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));

		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresentBoolean = connDect.isConnectingToInternet();
//		try {
//
//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//			imm.hideSoftInputFromWindow(getCurrentFocus()
//
//			.getWindowToken(), 0);
//
//		} catch (Exception exception) {
//
//			exception.printStackTrace();
//
//		}

		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				pullBoolean = true;
//				pullToRefreshListView.onRefreshComplete();
				projectData();
			}
		});
		
		memberAdap = new LazyAdapter(Homepage.this);
		listviewMembers.setAdapter(memberAdap);

		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		currentDateandTimeString = sdf.format(new Date());
		textviewCurrTime.setText(currentDateandTimeString);
		textviewCurrTime.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));
		sharedpref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		// popup start
		popup = new Dialog(Homepage.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		// expiry_popup.setCancelable(false);

		popup.setContentView(R.layout.bridge_expiry_popup);
		// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
		RelativeLayout relativelayoutExpiryMain = (RelativeLayout) popup
				.findViewById(R.id.relativelayoutExpiryMain);
		// expiry_main.setInAnimation(R.anim.slide_in_from_top);
		buttonEmail = (Button) popup.findViewById(R.id.buttonEmail);
		buttonCall = (Button) popup.findViewById(R.id.buttonCall);
		buttonText = (Button) popup.findViewById(R.id.buttonText);
		buttonCancel = (Button) popup.findViewById(R.id.buttonCancel);
		textviewExpiryAlert = (TextView) popup.findViewById(R.id.textviewExpiryAlert);
		// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());

		relativelayoutListOutside = (RelativeLayout) popup.findViewById(R.id.relativelayoutExpiryMain);
		new ASSL(Homepage.this, relativelayoutExpiryMain, 1134, 720, false);

		relativelayoutListOutside.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popup.isShowing()) {
					popup.dismiss();
					// overridePendingTransition(R.anim.slide_in_bottom,
					// R.anim.slide_out_to_top);
				}
			}
		});

		buttonCall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
				if(Prefrences.ContactPhone.equals(""))
				{
					AlertMessage();
				}
				else{
				Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
				phoneCallIntent.setData(Uri.parse("tel:"
						+ Prefrences.ContactPhone.toString()));
				startActivity(phoneCallIntent);
				}
			}
		});

		buttonEmail.setOnClickListener(new OnClickListener() {
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

		buttonText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
				if(Prefrences.ContactPhone.equals(""))
				{
					AlertMessage();
				}
				else{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts(
						"sms", Prefrences.ContactPhone.toString(), null)));
				}

			}
		});

		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});
		String response = sharedpref.getString("projectData", "");
		if (isInternetPresentBoolean) {
			
			if(response.equalsIgnoreCase("")){
				projectData();
			}else{
				startservice();
				fillServerData(response);
			}
			// updateReportHit(Homepage.this);
		} else {
			
			if(response.equalsIgnoreCase("")){
			Toast.makeText(getApplicationContext(),
					"No internet connection.", Toast.LENGTH_SHORT).show();
			}else{
				fillServerData(response);
			}
		}

		edittextListSearch.addTextChangedListener(new TextWatcher() {

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

		buttonSliding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});

		buttonLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Prefrences.saveAccessToken(
//						"454876423564",
						"",
						"","","",

						getApplicationContext());
				// Prefrences.authToken="";
				// Prefrences.email="";
				
				 Homepage.this.deleteDatabase("database_table.db");
				 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
				 Editor editor = pref.edit();
				 editor.clear();
				 editor.commit();
//				 FlurryAgent.onEvent("Logout button clicked");
				
				startActivity(new Intent(Homepage.this, MainActivity.class));
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				finish();
			}
		});
	}

//	 @Override
//	    protected void onStart() {
//
//	        super.onStart();
//	        FlurryAgent.onStartSession(Homepage.this, Prefrences.flurryKey); 
//	        FlurryAgent.onEvent("Dash board screen");
//	    }
//
//	    @Override
//	    protected void onStop() {
//	        super.onStop();
//	        FlurryAgent.onEndSession(this);
//	        FlurryAgent.endTimedEvent("Article_Read");
//	    }
	// “punchlist_item” : {
	// “id” : "208",
	// “body” : “Worklist item”,
	// “location” : “Basemenet”,
	// “user_id” : "2",
	// “status” : "1",
	// “completed_by_user_id” : "2",
	// “user_assignee” : "3"
	// }

//	void updateReportHit(Activity con) {
//
//		Prefrences.showLoadingDialog(con, "Loading...");
//		JSONObject jsonobj = new JSONObject();
//
//		// JSONArray jo1 = new JSONArray();
//
//		try {
//			// // “punchlist_item” : {
//			// “id” : "208",
//			// “body” : “Worklist item”,
//			// “location” : “Basemenet”,
//			// “user_id” : "2",
//			// “status” : "1",
//			// “completed_by_user_id” : "2",
//			// “user_assignee” : "3"
//			//
//			jsonobj.put("id", "208");
//			jsonobj.put("body", "worklist itemssss");
//			jsonobj.put("location", "kitchen");
//			jsonobj.put("user_id", "46");
//			jsonobj.put("status", "1");
//			jsonobj.put("completed_by_user_id", "46");
//			jsonobj.put("user_assignee", "1");
//
//			JSONObject finalJson = new JSONObject();
//			finalJson.put("punchlist_item", jsonobj.toString());
//
//			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
//					.getBytes("UTF-8"));
//
//			AsyncHttpClient client = new AsyncHttpClient();
//			client.addHeader("Content-type", "application/json");
//			client.addHeader("Accept", "application/json");
//
//			client.put(con, Prefrences.url + "/punchlist_items/208", entity,
//					"application/json", new AsyncHttpResponseHandler() {
//						@Override
//						public void onSuccess(String response) {
//							JSONObject res = null;
//							try {
//								res = new JSONObject(response);
//								Log.v("response ---- ",
//										"---*****----" + res.toString(2));
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							Prefrences.dismissLoadingDialog();
//						}
//
//						@Override
//						public void onFailure(Throwable arg0) {
//							Log.e("request fail", arg0.toString());
//							Prefrences.dismissLoadingDialog();
//						}
//					});
//
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}

	// ************ API for Projects. *************//

	public void projectData() {

		if(pullBoolean!=true)
		Prefrences.showLoadingDialog(Homepage.this, "Loading...");

		RequestParams params = new RequestParams();

		params.put("user_id", Prefrences.userId);

		user2ArrayList.clear();
		sub2ArrayList.clear();

		AsyncHttpClient client = new AsyncHttpClient();

		client.setTimeout(100000);
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.get(Prefrences.url + "/projects", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);
						
						fillServerData(response);
						if(pullBoolean!=true)
						Prefrences.dismissLoadingDialog();
						
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						if(pullBoolean!=true)
						Prefrences.dismissLoadingDialog();
						if (pullBoolean == true) {
							pullBoolean = false;
							pullToRefreshListView.onRefreshComplete();
						}
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Homepage.this);
				 
							// set title
							alertDialogBuilder.setTitle("Server timeout.");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Something went wrong while downloading your project information")
								.setCancelable(false)
								.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// if this button is clicked, close
										/// current activity
										dialog.cancel();
										projectData();
									}
								  });
//								.setNegativeButton("No thanks",new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,int id) {
//										// if this button is clicked, just close
//										// the dialog box and do nothing
//										dialog.cancel();
//										finish();
//									}
//								});
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
								alertDialog.show();
							}
					
				});

	}
	
	
	
	public void fillServerData(String response){
		JSONObject res = null;
		try {
			res = new JSONObject(response);
			Editor editor =  sharedpref.edit();
			editor.putString("projectData", response);
			editor.commit();
			editor.putString("lat", ""+Prefrences.currentLatitude);
			editor.putString("longi", ""+Prefrences.currentLongitude);
			JSONArray projects = res.getJSONArray("projects");
			projectsArrayList.clear();

			for (int i = 0; i < projects.length(); i++) {

				JSONObject count = projects.getJSONObject(i);

				JSONObject address = count
						.getJSONObject("address");

				

				JSONArray users = count.getJSONArray("users");

//				JSONArray subs = count
//						.getJSONArray("company_subs");

				ArrayList<Users> userArrayList = new ArrayList<Users>();
//				ArrayList<subcontractors> sub = new ArrayList<subcontractors>();

				ArrayList<String> cmpnyArrayList = new ArrayList<String>();
				ArrayList<String> cmpnyidArrayList = new ArrayList<String>();
				ArrayList<String> usridArrayList = new ArrayList<String>();

//				 for(int k=0;k<subs.length();k++)
//				 {
//				 JSONObject kCount = subs.getJSONObject(k);
//				
//				 sub.add(new
//				 subcontractors(kCount.getString("id"),
//				 kCount.getString("name"),
//				 kCount.getString("email"),
//				 kCount.getString("phone"),
//				 kCount.getString("count")));
//				
//				 }
//				 sub2.addAll(sub);

				for (int j = 0; j < users.length(); j++) {
					JSONObject uCount = users.getJSONObject(j);
					JSONObject uCompany = uCount
							.getJSONObject("company");

					userArrayList.add(new Users(
							uCount.getString("id"),
							uCount.getString("first_name"),
							uCount.getString("last_name"),
							uCount.getString("full_name"),
							uCount.getString("email"),
							uCount.getString("formatted_phone"),
//							uCount.getString("authentication_token"),
							uCount.getString("url_thumb"),
							uCount.getString("url_small"),
							new Company(uCompany
									.getString("id"), uCompany
									.getString("name"))));
					// if(!uCount.getString("full_name").equals("null"))
					usrArrayList.add(uCount.getString("full_name"));
					cmpnyArrayList.add(userArrayList.get(j).company.coName
							.toString());
					cmpnyidArrayList.add(userArrayList.get(j).company.coId
							.toString());
					usridArrayList.add(userArrayList.get(j).uId.toString());
					Log.d("companieeeesss", "companiess... "
							+ userArrayList.get(j).company.coId);

				}
				user2ArrayList.addAll(userArrayList);
				compnyArrayList.addAll(cmpnyArrayList);
				compnyIdArrayList.addAll(cmpnyidArrayList);
				useridArrayList.addAll(usridArrayList);

				Log.d("usssrrsrsrsrsr",
						"sizeeee" + compnyArrayList.size());

				Log.d("usssrrsrsrsrsr",
						"sizeeee" + compnyIdArrayList.size());
				if(!count.isNull("company"))
				{
				JSONObject company = count
						.getJSONObject("company");
				
				projectsArrayList.add(new ProjectsFields(count
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
								.getString("longitude")), userArrayList,
						new Company(company.getString("id"),
								company.getString("name"))));
				// compny.add(company.getInt("name"));
				}
				else{
					projectsArrayList.add(new ProjectsFields(count
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
									.getString("longitude")), userArrayList,
							new Company("","")));
				}
			}

			for (int i = 0; i < projectsArrayList.size(); i++) {
				Log.v("name = ", projectsArrayList.get(i).name);
				Log.v("id = ", projectsArrayList.get(i).id);

				for (int k = 0; k < projectsArrayList.get(i).users
						.size(); k++) {
					Log.v("username = ",
							projectsArrayList.get(i).users.get(k).uId
									+ ", "
									+ projectsArrayList.get(i).users
											.get(k).uFirstName);

				}
			}

			projectsAdap = new ProjectsAdapter(Homepage.this);
			pullToRefreshListView.setAdapter(projectsAdap);
			if (pullBoolean == true)
			{
				pullBoolean = false;
			}
			pullToRefreshListView.onRefreshComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void startservice(){
		Log.d("in service","in service");
		startService(new Intent(this, ProjectLoadService.class));
	}
	private void AlertMessage() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Sorry!")

		.setMessage(

		"The recipient doesn't have a phone number")

		.setCancelable(false)

		.setPositiveButton("OK",

		new DialogInterface.OnClickListener() {

			public void onClick(final DialogInterface dialog,

			int intValue) {

//				finish();

				// setting_page = true;

			}

		});

		final AlertDialog alert = builder.create();

		alert.show();

	}
}
