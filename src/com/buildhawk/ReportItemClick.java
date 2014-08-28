package com.buildhawk;

/*
 * 	This file is used to show the clicked Report and we can edit the report.
 * 
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.Author;
import com.buildhawk.utils.CommentUser;
import com.buildhawk.utils.Comments;
import com.buildhawk.utils.Company;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
//import com.horizontalscrollviewwithpageindicator.R;
//import com.horizontalscrollviewwithpageindicator.ViewPagerAdapter;

public class ReportItemClick extends Activity {

	String notesValueString = "";
	public ArrayList<Report> reportDataLocalArrayList;
	String typesString;
	// int pos;
	RelativeLayout relativelayoutRoot;
	Button buttonBack, buttonSave;
	public static TextView textviewType, textviewDate;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
	private static int PICK_CONTACT = 1;
	static String picturePathString;
	public ViewPager myPager;
	static Uri imageUri;

	public static final int COMPANY = 100;
	public static final int PERSONNEL = 200;

	public ArrayList<ReportPersonnel> personArrayList;
	static String reportIDString;
	String reportTypeString;
	// String companyID;

	public ArrayList<ReportCompanies> companiesArrayList;
	// public static ArrayList<ReportCompany>Rcompany;
	public ArrayList<ReportCompanyUsers> coUsersArrayList;
	public ArrayList<ReportCompanyUsers> coSubUsersArrayList;
	public ArrayList<ReportCompanySubcontractors> coSubsArrayList;
	public ArrayList<SafetyTopics> safeArrayList;

	public ArrayList<ReportTopics> reportTopicsArrayList;

	public static ArrayList<String> personelnamesArrayList = new ArrayList<String>();
	public static ArrayList<String> personelIdArrayList = new ArrayList<String>();
	public static ArrayList<String> personelHoursArrayList = new ArrayList<String>();
	public static ArrayList<String> CompaniesArrayList = new ArrayList<String>();
	public static ArrayList<String> OnsiteArrayList = new ArrayList<String>();
	public static ArrayList<String> CompanyIdArrayList = new ArrayList<String>();
	public static ArrayList<String> SafetyIDArrayList = new ArrayList<String>();
	public static ArrayList<String> SafetyTitleArrayList = new ArrayList<String>();
	ViewPagerAdapter adapter;

	static Activity act;

	ArrayList<Report> reportdataArrayList;
	ArrayList<Author> authorArrayList;
	ArrayList<Comments> commntArrayList;
	ArrayList<CommentUser> cusrArrayList;
	ArrayList<ProjectPhotos> photoArrayList;
	ArrayList<ReportPersonnelUser> personnelUserArrayList;
	ArrayList<subcontractors> psubArrayList;
	int j;

	String windsString, tempsHighString, precipsString, humiditysString, tempsLowString, iconsString, summarysString,
			tempsString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_item_click);

		relativelayoutRoot = (RelativeLayout) findViewById(R.id.relativelayoutReportClickRoot);
		new ASSL(this, relativelayoutRoot, 1134, 720, false);
		act = ReportItemClick.this;
		buttonBack = (Button) findViewById(R.id.buttonBack);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		textviewType = (TextView) findViewById(R.id.textviewType);
		textviewDate = (TextView) findViewById(R.id.textviewDate);
		buttonBack.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));
		buttonSave.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));
		textviewDate.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));
		textviewType.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));

		CompaniesArrayList.clear();
		CompanyIdArrayList.clear();
		OnsiteArrayList.clear();
		personelHoursArrayList.clear();
		personelnamesArrayList.clear();
		personelIdArrayList.clear();
		SafetyIDArrayList.clear();
		SafetyTitleArrayList.clear();

		// weathertHit();
		reportDataLocalArrayList = new ArrayList<Report>();
		myPager = (ViewPager) findViewById(R.id.pager);
		if (Prefrences.reportNotification.equalsIgnoreCase("")) {
			Bundle bundle = getIntent().getExtras();
			Prefrences.ReportPosition = bundle.getInt("pos");

			if (Prefrences.reportType == 1) {
				typesString = "Daily";
				reportDataLocalArrayList.addAll(ReportFragment.reportdataDailyArrayList);
			} else if (Prefrences.reportType == 2) {
				reportDataLocalArrayList.addAll(ReportFragment.reportdataSafetyArrayList);
				typesString = "Safety";
			} else if (Prefrences.reportType == 3) {
				reportDataLocalArrayList.addAll(ReportFragment.reportdataWeeklyArrayList);
				typesString = "Weekly";
			} else if (Prefrences.reportType == 4) {
				reportDataLocalArrayList.addAll(ProjectDetail.dateReports);
			} else if (Prefrences.reportType == 0) {
				reportDataLocalArrayList.addAll(ReportFragment.reportdataArrayList);
			}

			adapter = new ViewPagerAdapter(ReportItemClick.this,
					reportDataLocalArrayList);
			myPager.setOffscreenPageLimit(0);
			myPager.setAdapter(adapter);

			myPager.setCurrentItem(Prefrences.ReportPosition);

			textviewType
					.setText(reportDataLocalArrayList.get(myPager.getCurrentItem()).report_type
							.toString() + " - ");
			textviewDate
					.setText(reportDataLocalArrayList.get(myPager.getCurrentItem()).created_date
							.toString());
			reportIDString = reportDataLocalArrayList.get(myPager.getCurrentItem()).report_id
					.toString();
			reportTypeString = reportDataLocalArrayList.get(myPager.getCurrentItem()).report_type
					.toString();
			Log.v("clicked REPORT ID", "------- " + reportIDString);
			Log.v("clicked REPORT Type", "------- " + reportTypeString);
			for (int i = 0; i < reportDataLocalArrayList.get(myPager.getCurrentItem()).personnel
					.size(); i++) {
				personelnamesArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).personnel
								.get(i).users.get(0).uFullName.toString());
				personelIdArrayList.add(reportDataLocalArrayList.get(myPager
						.getCurrentItem()).personnel.get(i).users.get(0).uId
						.toString());
				personelHoursArrayList.add(reportDataLocalArrayList.get(myPager
						.getCurrentItem()).personnel.get(i).hours.toString());
			}

			for (int i = 0; i < reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
					.size(); i++) {
				CompaniesArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
								.get(i).Rcompany.get(0).coName.toString());
				CompanyIdArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
								.get(i).Rcompany.get(0).coId.toString());
				OnsiteArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
								.get(i).coCount.toString());
			}

			for (int i = 0; i < reportDataLocalArrayList.get(myPager.getCurrentItem()).topic
					.size(); i++) {
				SafetyIDArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).topic
								.get(i).safety.get(0).Id.toString());
				SafetyTitleArrayList.add(reportDataLocalArrayList.get(myPager
						.getCurrentItem()).topic.get(i).safety.get(0).Title
						.toString());
				// OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
			}

			for (int i = 0; i < reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
					.size(); i++) {
				Log.d("naem", "name" + CompaniesArrayList.get(i).toString());// add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
				Log.d("id", "id" + CompanyIdArrayList.get(i).toString());// CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
				Log.d("count", "count" + OnsiteArrayList.get(i).toString());// OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
			}

			Log.v("BBB-", ".." + reportIDString);
		} else {
			getProjectReports(Prefrences.selectedProId);
		}

		myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			// int oldPos = myPager.getCurrentItem();
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				// pos=arg0;
				// Prefrences.posViewpager=arg0;
				Log.d("arg0", "arg0 " + arg0);
				textviewType.setText(reportDataLocalArrayList.get(myPager
						.getCurrentItem()).report_type.toString() + " - ");
				textviewDate.setText(reportDataLocalArrayList.get(myPager
						.getCurrentItem()).created_date.toString());
				reportIDString = reportDataLocalArrayList.get(myPager.getCurrentItem()).report_id
						.toString();
				// companyID=reportdata_local.get(myPager.getCurrentItem()).companies.get(0).coId.toString();
				reportTypeString = reportDataLocalArrayList.get(myPager.getCurrentItem()).report_type
						.toString();
				Log.v("REPORT ID", "------- " + reportIDString);
				Log.v("REPORT Type", "------- " + reportTypeString);

				CompaniesArrayList.clear();
				CompanyIdArrayList.clear();
				OnsiteArrayList.clear();
				personelHoursArrayList.clear();
				personelnamesArrayList.clear();
				personelIdArrayList.clear();
				SafetyIDArrayList.clear();
				SafetyTitleArrayList.clear();

				for (int i = 0; i < reportDataLocalArrayList.get(myPager
						.getCurrentItem()).personnel.size(); i++) {
					personelnamesArrayList.add(reportDataLocalArrayList.get(myPager
							.getCurrentItem()).personnel.get(i).users.get(0).uFullName
							.toString());
					personelIdArrayList.add(reportDataLocalArrayList.get(myPager
							.getCurrentItem()).personnel.get(i).users.get(0).uId
							.toString());
					personelHoursArrayList.add(reportDataLocalArrayList.get(myPager
							.getCurrentItem()).personnel.get(i).hours
							.toString());
				}

				for (int i = 0; i < reportDataLocalArrayList.get(myPager
						.getCurrentItem()).companies.size(); i++) {
					CompaniesArrayList.add(reportDataLocalArrayList.get(myPager
							.getCurrentItem()).companies.get(i).Rcompany.get(0).coName
							.toString());
					CompanyIdArrayList.add(reportDataLocalArrayList.get(myPager
							.getCurrentItem()).companies.get(i).Rcompany.get(0).coId
							.toString());
					OnsiteArrayList.add(reportDataLocalArrayList.get(myPager
							.getCurrentItem()).companies.get(i).coCount
							.toString());
				}

				for (int i = 0; i < reportDataLocalArrayList.get(myPager
						.getCurrentItem()).topic.size(); i++) {
					SafetyIDArrayList.add(reportDataLocalArrayList.get(myPager
							.getCurrentItem()).topic.get(i).safety.get(0).Id
							.toString());
					SafetyTitleArrayList.add(reportDataLocalArrayList.get(myPager
							.getCurrentItem()).topic.get(i).safety.get(0).Title
							.toString());
					// OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
				}

				//
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		// myPager.notify();
		// Prefrences.posViewpager = pos;
		// pos=Prefrences.posViewpager;

		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ProjectDetail.Flag=0;
//				adapter.reportdata.get(myPager.getCurrentItem()).companies = adapter.reportdatTemp.get(myPager.getCurrentItem()).companies;
//				adapter.reportdata.get(myPager.getCurrentItem()).personnel = adapter.reportdatTemp.get(myPager.getCurrentItem()).personnel;
//				adapter.reportdata.get(myPager.getCurrentItem()).topic = adapter.reportdatTemp.get(myPager.getCurrentItem()).topic;
//				adapter.reportdata.get(myPager.getCurrentItem()).body = adapter.reportdatTemp.get(myPager.getCurrentItem()).body;
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				Prefrences.PrefillFlag=0;
			
				// ReportFragment.reportdata=reportdata;
				reportDataLocalArrayList.clear();
				if (!Prefrences.reportNotification.equalsIgnoreCase("")) {
					Prefrences.reportNotification = "";
					Prefrences.comingFrom = 2;
					Intent intent = new Intent(ReportItemClick.this,
							ProjectDetail.class);
					startActivity(intent);
				}
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		buttonSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// sspg.updateReportHit(con);

				// adapter.

				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				if (ConnectionDetector.isConnectingToInternet()) {
					updateReportHit(ReportItemClick.this);
				} else {
					Toast.makeText(ReportItemClick.this,
							"No internet connection.", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
	}

	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (Prefrences.stopingHit == 1) {
			Prefrences.stopingHit = 0;
//			// adapter.notifyDataSetChanged();
//			// myPager.setAdapter(adapter);
//			// myPager.setCurrentItem(Prefrences.ReportPosition);
//
//			// ViewPagerAdapter.companyadapter.notifyDataSetChanged();
//			if (CompaniesArray.size() > 0) {
//				for (int i = 0; i < CompaniesArray.size(); i++) {
//					Log.d("Company", "name" + CompaniesArray.get(i).toString());// add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
//					Log.d("Company", "id"
//							+ CompanyIdArrayList.get(i).toString());// CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
//					Log.d("Company", "count" + OnsiteArray.get(i).toString());// OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
//				}
//			}
//			if (personelnamesArray.size() > 0) {
//				for (int i = 0; i < personelIdArrayList.size(); i++) {
//					Log.d("personnel", "name"
//							+ personelnamesArray.get(i).toString());// add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
//					Log.d("personnel", "id"
//							+ personelIdArrayList.get(i).toString());// CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
//					Log.d("personnel", "hours"
//							+ personelHoursArray.get(i).toString());// OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
//				}
//			}
//			if (SafetyIDArray.size() > 0) {
//				for (int i = 0; i < SafetyIDArray.size(); i++) {
//					Log.d("safety", "title"
//							+ SafetyTitleArray.get(i).toString());// add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
//					Log.d("safety", "id" + SafetyIDArray.get(i).toString());// CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
//					// Log.d("count","count"+OnsiteArray.get(i).toString());//OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
//				}
//			}
			
			getProjectReports(Prefrences.selectedProId);
			
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Prefrences.PrefillFlag=0;
//		adapter.reportdata = adapter.reportdatTemp;
//		adapter.reportdata.get(myPager.getCurrentItem()).companies.clear();
//		adapter.reportdata.get(myPager.getCurrentItem()).companies.addAll(adapter.reportdatTemp.get(myPager.getCurrentItem()).companies);
//		adapter.reportdata.get(myPager.getCurrentItem()).companies = adapter.reportdatTemp.get(myPager.getCurrentItem()).companies;
//		adapter.reportdata.get(myPager.getCurrentItem()).personnel = adapter.reportdatTemp.get(myPager.getCurrentItem()).personnel;
//		adapter.reportdata.get(myPager.getCurrentItem()).topic = adapter.reportdatTemp.get(myPager.getCurrentItem()).topic;
//		adapter.reportdata.get(myPager.getCurrentItem()).body = adapter.reportdatTemp.get(myPager.getCurrentItem()).body;
		// try {
		//
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		//
		// imm.hideSoftInputFromWindow(getCurrentFocus()
		//
		// .getWindowToken(), 0);
		//
		// } catch (Exception exception) {
		//
		// exception.printStackTrace();
		//
		// }
		// ReportFragment.reportdata=reportdata;
		// adapter.reportdata.get(myPager.getCurrentItem()).companies
		// .remove(adapter.reportdata.get(myPager.getCurrentItem()).companies.size()-1);

		reportDataLocalArrayList.clear();
		if (!Prefrences.reportNotification.equalsIgnoreCase("")) {
			Prefrences.stopingHit = 1;
			Prefrences.reportNotification = "";
			Prefrences.comingFrom = 2;
			Intent intent = new Intent(ReportItemClick.this,
					ProjectDetail.class);
			startActivity(intent);
		}
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Prefrences.stopingHit = 1;
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePathString = cursor.getString(columnIndex);
			cursor.close();
			new LongOperation().execute("");
			// picarray

			// rt
			// }

			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			// WorkListCompleteAdapter.punchlistphoto2.add(new
			// PunchListsPhotos("", picturePath, picturePath, "", picturePath,
			// picturePath, "", "", "", "", "", "", "", ""));
			// photosarrayArrayList.clear();
			// arr.add(WorkListCompleteAdapter.punchlistphoto2.get(i).urlSmall);
			// ids.add(WorkListCompleteAdapter.punchlistphoto2.get(i).id);
			// upload();

			// commentAdd();

		}

		else if (requestCode == TAKE_PICTURE
				&& resultCode == Activity.RESULT_OK) {

			Uri selectedImage = imageUri;

			picturePathString = getRealPathFromURI(imageUri);
			getContentResolver().notifyChange(selectedImage, null);

			// ImageView imageView = (ImageView) findViewById(R.id.imgView);

			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {

			new LongOperation().execute("");
			ContentResolver cr = getContentResolver();

			// WorkListCompleteAdapter.punchlistphoto2.add(new
			// PunchListsPhotos("", selectedImage.toString(),
			// selectedImage.toString(), "", selectedImage.toString(),
			// selectedImage.toString(), "", "", "", "", "", "", "", ""));
			// photosarrayArrayList.clear();
			// imageView.setImageBitmap(bitmap);
		} else {

			switch (requestCode) {
			case COMPANY:
				if (resultCode == RESULT_OK) {
					String id = data.getStringExtra("id");
					String name = data.getStringExtra("name");
					String hours = data.getStringExtra("hours");

					ArrayList<ReportCompany> reportList = new ArrayList<ReportCompany>();
					reportList.add(new ReportCompany(id, name,
							new ArrayList<ReportCompanyUsers>(),
							new ArrayList<ReportCompanySubcontractors>()));
					adapter.reportdata.get(myPager.getCurrentItem()).companies
							.add(new ReportCompanies("", hours, reportList));
					adapter.notifyDataSetChanged();
				}
				break;

			case PERSONNEL:
				if (resultCode == RESULT_OK) {
					String id = data.getStringExtra("id");
					String name = data.getStringExtra("name");
					String hours = data.getStringExtra("hours");

					ArrayList<ReportPersonnelUser> reportList = new ArrayList<ReportPersonnelUser>();
					reportList.add(new ReportPersonnelUser(id, name));
					adapter.reportdata.get(myPager.getCurrentItem()).personnel
							.add(new ReportPersonnel("", reportList, hours));
					adapter.notifyDataSetChanged();
				}
				break;
			}

		}

	}

	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	class LongOperation extends AsyncTask<String, Void, String> {

		boolean error;
		@Override
		protected String doInBackground(String... params) {

			try {

				postPicture();

			} catch (Exception e) {

				// TODO Auto-generated catch block
				error=true;
				e.printStackTrace();

			}
//			catch (IOException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			} catch (XmlPullParserException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			Prefrences.dismissLoadingDialog();
			// ViewGroup group = (ViewGroup) myPager.getChildAt(myPager
			// .getCurrentItem());
			// LinearLayout lin2 = (LinearLayout) group
			// .findViewById(R.id.scrolllayout2);
			// ImageView ladder_page2 = new ImageView(ReportItemClick.this);
			// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			// (int) (200), (int) (200));
			// lp.setMargins(10, 10, 10, 10);
			// ladder_page2.setLayoutParams(lp);
			// File file = new File(picturePath);
			// Picasso.with(ReportItemClick.this).load(file)
			// .placeholder(R.drawable.ic_launcher).into(ladder_page2);
			// // // ladder_page2.setImageBitmap(myBitmap);
			// lin2.addView(ladder_page2);
			adapter.reportdata.get(myPager.getCurrentItem()).photos
					.add(new ProjectPhotos(picturePathString, picturePathString, true));
			adapter.notifyDataSetChanged();
			if(error)
			{
				AlertMessage2();
			}
		}

		@Override
		protected void onPreExecute() {
			// ViewGroup group = (ViewGroup) myPager.getChildAt(myPager
			// .getCurrentItem() - 1);
			// LinearLayout lin2 = (LinearLayout)
			// group.findViewById(R.id.scrolllayout2);
			// ImageView ladder_page2 = new ImageView(ReportItemClick.this);
			// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			// (int) (200), (int) (200));
			// lp.setMargins(10, 10, 10, 10);
			// ladder_page2.setLayoutParams(lp);
			// File file = new File(picturePath);
			// Picasso.with(ReportItemClick.this).load(file)
			// .placeholder(R.drawable.ic_launcher).into(ladder_page2);
			// // // ladder_page2.setImageBitmap(myBitmap);
			// lin2.addView(ladder_page2);

			Prefrences.showLoadingDialog(ReportItemClick.this, "Uploading...");
			// View group = myPager.findViewWithTag(myPager.getCurren
			// Prefrences.stopingHit = 0;
			// ViewGroup group = (ViewGroup) myPager.getChildAt(myPager
			// .getCurrentItem());
			//
			// LinearLayout lin2 = (LinearLayout) group
			// .findViewById(R.id.scrolllayout2);
			//
			// final ImageView ladder_page2 = new
			// ImageView(ReportItemClick.this);
			//
			// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			// (int) (200), (int) (200));
			// lp.setMargins(10, 10, 10, 10);
			// ladder_page2.setLayoutParams(lp);
			//
			// File file = new File(picturePath);
			//
			// Bitmap myBitmap =
			// BitmapFactory.decodeFile(file.getAbsolutePath());
			// ladder_page2.setImageBitmap(myBitmap);
			//
			// lin2.addView(ladder_page2);
			// adapter.notifyDataSetChanged();

			// Picasso.with(ReportItemClick.this).load(file)
			// .placeholder(R.drawable.ic_launcher).into(ladder_page2);

			// Log.e("---------","-------------"+myPager.getCurrentItem()+"-----------"+myPager.getch));
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}
	private void AlertMessage2() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Failed")

		.setMessage(

		"Fail to load Image")

		.setCancelable(false)

		.setPositiveButton("OK",

		new DialogInterface.OnClickListener() {

			public void onClick(final DialogInterface dialog,

			int intValue) {

				finish();

				// setting_page = true;

			}

		});

		final AlertDialog alert = builder.create();

		alert.show();

	}
	
	// ************ API for Post pictures. *************//
	
	public void postPicture() throws ParseException, IOException,

	XmlPullParserException {

		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Prefrences.url

		+ "/reports/photo/");

		// MultipartEntity mpEntity = new MultipartEntity();

		String BOUNDARY = "--buildhawk--";

		MultipartEntity mpEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY,
				Charset.defaultCharset());

		httppost.addHeader("Accept-Encoding", "gzip, deflate");

		// httppost.setHeader("Accept", "image/jpg");

		httppost.setHeader("Accept", "application/json");

		httppost.setHeader("Content-Type", "multipart/form-data; boundary="

		+ BOUNDARY);

		Log.v("picturePath", picturePathString);

		File file = new File(picturePathString);

		FileBody cbFile = new FileBody(file, "image/jpg");

		cbFile.getMediaType();
		Log.i("photo[report_id]", reportIDString);
		// reportdata_local.get(myPager.getCurrentItem()).report_id.toString());
		Log.i("photo[mobile]", "1");
		Log.i("photo[company_id]", Prefrences.companyId);
		Log.i("photo[source]", "Reports");
		Log.i("photo[user_id]", Prefrences.userId);
		Log.i("photo[project_id]", Prefrences.selectedProId);
		Log.i("photo[name]", "android");
		Log.i("photo[image]", "" + cbFile);

		mpEntity.addPart(
				"photo[report_id]",
				new StringBody(
						reportDataLocalArrayList.get(myPager.getCurrentItem()).report_id
								.toString()));

		mpEntity.addPart("photo[mobile]", new StringBody("1"));

		mpEntity.addPart("photo[company_id]", new StringBody(
				Prefrences.companyId));

		mpEntity.addPart("photo[source]", new StringBody("Reports"));

		mpEntity.addPart("photo[user_id]", new StringBody(Prefrences.userId));

		mpEntity.addPart("photo[project_id]", new StringBody(
				Prefrences.selectedProId));

		mpEntity.addPart("photo[name]", new StringBody("android"));

		mpEntity.addPart("photo[image]", cbFile);

		httppost.setEntity(mpEntity);

		HttpResponse response = httpclient.execute(httppost);

		Log.v("response", response.getStatusLine().toString() + "");

		Log.v("res", "," + response);

		HttpEntity entity = response.getEntity();

		String responseString = EntityUtils.toString(entity, "UTF-8");

		Log.v("res", "," + responseString);

		System.out.println(responseString);

	}

	private void AlertMessage() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Success")

		.setMessage(

		"Report added")

		.setCancelable(false)

		.setPositiveButton("OK",

		new DialogInterface.OnClickListener() {

			public void onClick(final DialogInterface dialog,

			int intValue) {

				// setting_page = true;

			}

		});

		final AlertDialog alert = builder.create();

		alert.show();

	}

	
	// ************ API for Update report. *************//
	
	void updateReportHit(Activity con) {

		Prefrences.showLoadingDialog(con, "Loading...");
		// JSONObject jsonobj = new JSONObject();

		RequestParams params = new RequestParams();
		Log.e("myPager.getCurrentItem()", ",, " + myPager.getCurrentItem());

		notesValueString = Prefrences.report_body_edited;
		Prefrences.report_body_edited = "";

		Log.i("NOTES", notesValueString);
		Log.i("Group ",
				"group  " + myPager.getChildAt(myPager.getCurrentItem() + 1));
		Log.i("report id ", "report id  " + reportIDString);

		params.put("user_id", Prefrences.userId);
		params.put("report[body]", notesValueString);
		params.put("report[report_type]", reportTypeString);
		params.put("report[project_id]", Prefrences.selectedProId);
		params.put("report[date_string]", textviewDate.getText().toString());
		// params.put("report[wind]", "");
		// params.put("report[weather]", "");
		// params.put("report[precip]", "");
		// params.put("report[weather_icon]", "");
		// JSONArray array = new JSONArray();
		// try{
		//
		// for (int i = 0; i < CompaniesArray.size(); i++) {
		// JSONObject jo3 = new JSONObject();
		// Log.i("name ", "" + CompaniesArray.get(i).toString());
		// jo3.put("user_id", CompanyIdArrayList.get(i).toString());
		// jo3.put("full_name", CompaniesArray.get(i).toString());
		// jo3.put("hours", OnsiteArray.get(i).toString());
		// array.put(jo3);
		// }
		// }catch(Exception e)
		// {
		//
		// }

		// params.put("report[report_users]",
		// params.put(arg0, CompaniesArray)
		// params.put("report[report_companies]", CompaniesArray);
		// params.put("report[safety_topics]", "");

		JSONObject jsonobj = new JSONObject();
		JSONObject finalJson = new JSONObject();
		JSONArray companyArray = new JSONArray();
		JSONArray personnelArray = new JSONArray();
		JSONArray safetyArray = new JSONArray();
		try {
			jsonobj.put("body", notesValueString);
			jsonobj.put("report_type", reportTypeString);
			jsonobj.put("project_id", Prefrences.selectedProId);
			jsonobj.put("date_string", textviewDate.getText().toString());
			jsonobj.put("wind", windsString);
			jsonobj.put("weather", summarysString);
			jsonobj.put("precip", precipsString);
			jsonobj.put("weather_icon", iconsString);

			Log.d("CompanyArray",
					"Size of CompanyArray" + CompaniesArrayList.size());

//			if(CompaniesArray.size()==0)
//			{
//				companyArray = new JSONArray();
//			}
//			else
//			{
				for (int i = 0; i < CompaniesArrayList.size(); i++) {
					JSONObject jo3 = new JSONObject();
					Log.i("name ", "" + CompaniesArrayList.get(i).toString());
					jo3.put("name", CompaniesArrayList.get(i).toString());
					jo3.put("id", CompanyIdArrayList.get(i).toString());
					jo3.put("count", OnsiteArrayList.get(i).toString());
					companyArray.put(jo3);
				}
//			}

			jsonobj.put("report_companies", companyArray);
			
//			if(personelnamesArray.size()==0)
//			{
//				personnelArray = new JSONArray();
//			}
//			else
//			{

				for (int i = 0; i < personelnamesArrayList.size(); i++) {
					JSONObject jo3 = new JSONObject();
					Log.i("name ", "" + personelnamesArrayList.get(i).toString());
					jo3.put("full_name", personelnamesArrayList.get(i).toString());
					jo3.put("id", personelIdArrayList.get(i).toString());
					jo3.put("hours", personelHoursArrayList.get(i).toString());
					personnelArray.put(jo3);
				}
//			}

			jsonobj.put("report_users", personnelArray);

//			if(SafetyIDArray.size()==0)
//			{
//				safetyArray = new JSONArray();
//			}
//			else
//			{
				for (int i = 0; i < SafetyIDArrayList.size(); i++) {
					JSONObject jo3 = new JSONObject();
					Log.i("ID ", "" + SafetyIDArrayList.get(i).toString());
					jo3.put("title", SafetyTitleArrayList.get(i).toString());
					jo3.put("id", SafetyIDArrayList.get(i).toString());
	
					// jo3.put("hours", personelHoursArray.get(i).toString());
					safetyArray.put(jo3);
				}
//			}
			jsonobj.put("safety_topics", safetyArray);

			finalJson.put("user_id", 46);
			finalJson.put("report", jsonobj);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
//		Prefrences.stopingHit = 1;
		try {

			Log.v("final json = ", finalJson.toString(2));

			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
					.getBytes("UTF-8"));

			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			String report_id_val = "";

			report_id_val = reportDataLocalArrayList.get(myPager.getCurrentItem()).report_id
					.toString();

			Log.v("AAAAA-", ".." + reportIDString);

			Log.v("AAAAA-",
					".."
							+ reportDataLocalArrayList.get(myPager.getCurrentItem()).body
									.toString());
			Log.v("AAAAA-",
					".."
							+ reportDataLocalArrayList.get(myPager.getCurrentItem()).created_date
									.toString());

			client.put(con, Prefrences.url + "/reports/" + reportIDString, entity,
					"application/json", new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							JSONObject res = null;
							ArrayList<Author> author;
							try {
								res = new JSONObject(response);
								Log.v("response ---- ",
										"---*****----" + res.toString(2));

								personArrayList = new ArrayList<ReportPersonnel>();
								companiesArrayList = new ArrayList<ReportCompanies>();
								reportTopicsArrayList = new ArrayList<ReportTopics>();
								// Rcompany = new ArrayList<ReportCompany>();
								coUsersArrayList = new ArrayList<ReportCompanyUsers>();

								coSubsArrayList = new ArrayList<ReportCompanySubcontractors>();

								JSONObject reportobj = res
										.getJSONObject("report");

								String reportId = reportobj.getString("id");
								Log.d("report_id", "-----------***" + reportId);
								try {
									if (reportId.equals("null")) {
										reportId = "";
									}
								} catch (Exception e) {
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

								JSONArray photos = reportobj
										.getJSONArray("photos");
								ArrayList<ProjectPhotos> photo;
								if (photos.length() == 0) {
									photo = new ArrayList<ProjectPhotos>();
//									Log.d("if", "----photo if null--");
//									photo.add(new ProjectPhotos("", "", "",
//											"drawable", "", "", "", "", "", "",
//											"", "", "", "",null));
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

										Log.d("----photo url---", ""
												+ photo_url200);
										photo.add(new ProjectPhotos(photoid,
												url_large, original,
												photo_url200, url100,
												image_file_size,
												image_content_type, source,
												phase, photo_created_at,
												user_name, name, desc,
												photo_created_date,null));
									}
								}

								/*------- personnel array-------*/

								JSONArray personnel = reportobj
										.getJSONArray("personnel");
								if (personnel.length() == 0) {
									// person.add(new ReportPersonnel("",null,
									// ""));
								} else {

									for (int j = 0; j < personnel.length(); j++) {

										JSONObject count = personnel
												.getJSONObject(j);
										Log.d("", "count : " + count);
										if (count.isNull("user")) {

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
											ArrayList<ReportPersonnelUser> personnelUser = new ArrayList<ReportPersonnelUser>();

											personnelUser
													.add(new ReportPersonnelUser(
															puser.getString("id"),
															puser.getString("first_name"),
															puser.getString("last_name"),
															puser.getString("full_name"),
															puser.getString("email"),
															puser.getString("phone"),
															compny));

											// if(count.isNull("hours"))
											// {
											// person.add(new
											// ReportPersonnel(count.getString("id"),
											// psub,count.getString("count")));
											// }
											// else
											personArrayList.add(new ReportPersonnel(
													count.getString("id"),
													personnelUser, count
															.getString("hours")));
										}
									}
								}
								// Log.d("", "person size=" + person.size());
								JSONArray reportCompany = reportobj
										.getJSONArray("report_companies");
								if (reportCompany.length() == 0) {
									// person.add(new ReportPersonnel("",null,
									// ""));
								} else {

									for (int j = 0; j < reportCompany.length(); j++) {
										JSONObject count = reportCompany
												.getJSONObject(j);
										Log.e("report companies", "times j = "
												+ j);
										JSONObject company = count
												.getJSONObject("company");

										// JSONArray cousers =
										// company.getJSONArray("users");
										// if(cousers.length()!=0)
										// {
										// for(int k=0;k<cousers.length();k++)
										// {
										// Log.e("report companies",
										// "times k = " + k);
										// JSONObject ccount = cousers
										// .getJSONObject(k);
										// coUsers.add(new
										// ReportCompanyUsers(ccount.getString("id"),
										// ccount.getString("first_name"),
										// ccount.getString("last_name"),
										// ccount.getString("full_name"),
										// ccount.getString("email"),
										// ccount.getString("phone")));
										// }
										// }
										// JSONArray cosubs =
										// company.getJSONArray("subcontractors");
										// coSubUsers = new
										// ArrayList<ReportCompanyUsers>();
										// if(cosubs.length()!=0)
										// {
										// for(int k=0;k<cosubs.length();k++)
										// {
										// Log.e("report companies",
										// "times k2 = " + k);
										// JSONObject ccount = cosubs
										// .getJSONObject(k);
										//
										// JSONArray couser =
										// ccount.getJSONArray("users");
										// if(couser.length()!=0)
										// {
										// for(int m=0;m<couser.length();m++)
										// {
										// Log.e("report companies",
										// "times m = " + m);
										// JSONObject cccount =
										// couser.getJSONObject(m);
										//
										// coSubUsers.add(new
										// ReportCompanyUsers(cccount.getString("id"),
										// cccount.getString("first_name"),
										// cccount.getString("last_name"),
										// cccount.getString("full_name"),
										// cccount.getString("email"),
										// cccount.getString("phone")));
										//
										// }
										// coSubs.add(new
										// ReportCompanySubcontractors(ccount.getString("id"),
										// ccount.getString("name"),
										// coSubUsers,
										// ccount.getString("users_count")));
										// }
										// }
										// }
										ArrayList<ReportCompany> Rcompany = new ArrayList<ReportCompany>();
										Rcompany.add(new ReportCompany(company
												.getString("id"), company
												.getString("name"), coUsersArrayList,
												coSubsArrayList));

										companiesArrayList.add(new ReportCompanies(count
												.getString("id"), count
												.getString("count"), Rcompany));

									}
								}
								// ReportTopics = new ArrayList<ReportTopics>();
								JSONArray report_topics = reportobj
										.getJSONArray("report_topics");
								for (int j = 0; j < report_topics.length(); j++) {
									JSONObject obj = report_topics
											.getJSONObject(j);
									safeArrayList = new ArrayList<SafetyTopics>();
									JSONObject safety_topic = obj
											.getJSONObject("safety_topic");

									safeArrayList.add(new SafetyTopics(safety_topic
											.getString("id"), safety_topic
											.getString("title"), safety_topic
											.getString("info")));

									reportTopicsArrayList.add(new ReportTopics(obj
											.getString("id"), obj
											.getString("report_id"), safeArrayList));

								}

								// companies.get(i).
								// Log.d("","sizecouser= "+coUsers.size()+"cosubs"+coSubs.size()+"cosubuser"+coSubUsers.size()+"company"+companies.size());
								/*------- possible_types array-------*/

								if (Prefrences.reportlisttypes == 1) {
									ReportFragment.reportdataDailyArrayList.set(myPager
											.getCurrentItem(), (new Report(
											reportId, epochTime, createdAt,
											updatedat, createdDate, title,
											reportType, body, weather,
											weathericon, precip, temp, wind,
											humidity, author, photo, personArrayList,
											companiesArrayList, reportTopicsArrayList)));
								} else if (Prefrences.reportlisttypes == 2) {
									ReportFragment.reportdataSafetyArrayList.set(myPager
											.getCurrentItem(), (new Report(
											reportId, epochTime, createdAt,
											updatedat, createdDate, title,
											reportType, body, weather,
											weathericon, precip, temp, wind,
											humidity, author, photo, personArrayList,
											companiesArrayList, reportTopicsArrayList)));
								} else if (Prefrences.reportlisttypes == 3) {
									ReportFragment.reportdataWeeklyArrayList.set(myPager
											.getCurrentItem(), (new Report(
											reportId, epochTime, createdAt,
											updatedat, createdDate, title,
											reportType, body, weather,
											weathericon, precip, temp, wind,
											humidity, author, photo, personArrayList,
											companiesArrayList, reportTopicsArrayList)));
								} else {
									ReportFragment.reportdataArrayList.set(myPager
											.getCurrentItem(), (new Report(
											reportId, epochTime, createdAt,
											updatedat, createdDate, title,
											reportType, body, weather,
											weathericon, precip, temp, wind,
											humidity, author, photo, personArrayList,
											companiesArrayList, reportTopicsArrayList)));
								}
								if(Prefrences.PrefillFlag==1)
								{
									Prefrences.stopingHit = 1;
									Prefrences.PrefillFlag=0;
									Prefrences.report_bool=false;
								}
								else
								{
									ReportFragment.fromReportItem = true;
								}
								finish();

							} catch (Exception e) {
								e.printStackTrace();
							}
							// WorkListCompleteAdapter.punchlst_item(id_punchlist_item,
							// WorkListCompleteAdapter.data, pos);
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

	// ************ API for weather. *************//
	
	void weathertHit() {
		// lati 30.7187527 longi 76.8102047

		// Prefrences.showLoadingDialog(WorkItemClick.this, "Loading...");

		Prefrences.showLoadingDialog(ReportItemClick.this, "Loading...");

		// String lati = "30.7187527";
		// String longi = "76.8102047";

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(100000);
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		// client.get()
		client.get(getApplicationContext(),
				"https://api.forecast.io/forecast/32a0ebe578f183fac27d67bb57f230b5/"
						+ Prefrences.currentLatitude + ","
						+ Prefrences.currentLongitude,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));

							JSONObject current = res.getJSONObject("currently");
							// winds,temps,precips,humiditys,bodys,icons,summarys
							windsString = current.getString("windSpeed");
							tempsHighString = current.getString("temperature");
							tempsLowString = current.getString("apparentTemperature");

							precipsString = current.getString("precipProbability");
							double pre = Double.parseDouble(precipsString);

							pre = pre * 100;
							precipsString = Double.toString(pre);
							// precips= String.format("%.1f", precips);
							// precips=precips.spl

							humiditysString = current.getString("humidity");
							double hum = Double.parseDouble(humiditysString);
							hum = hum * 100;
							humiditysString = Double.toString(hum);
							// humiditys= String.format("%.1f", humiditys);
							// bodys=current.getString("windSpeed");

							iconsString = current.getString("icon");
							summarysString = current.getString("summary");
							// winds=current.getString("windSpeed");

							Log.d("responseeeee", "responseeeee" + windsString
									+ tempsHighString + precipsString + humiditysString
									+ tempsLowString + iconsString + summarysString);
							// if (Prefrences.selecteddate != "") {
							// date.setText(Prefrences.selecteddate);
							// } else {
							// date.setText(currentDateandTime);
							// }
							windsString = windsString + "mph";
							tempsString = (tempsLowString + (char) 0x00B0 + " / "
									+ tempsHighString + (char) 0x00B0);
							precipsString = String.format("%.2f",
									Float.parseFloat(precipsString) + "%");
							// precip.setText(str + "%");
							humiditysString = String.format("%.2f",
									Float.parseFloat(humiditysString) + "%");
							// humidity.setText(str2 + "%");
							// weather.setText(summarys);
							// if
							// (icons.toString().equalsIgnoreCase("clear-day")
							// || icons.toString().equalsIgnoreCase(
							// "clear-night")) {
							// weatherIcon
							// .setBackgroundResource(R.drawable.sunny_temp);
							// } else if (icons.toString()
							// .equalsIgnoreCase("rain")
							// || icons.toString().equalsIgnoreCase(
							// "sleet")) {
							// weatherIcon
							// .setBackgroundResource(R.drawable.rainy_temp);
							// } else if (icons.toString().equalsIgnoreCase(
							// "partly-cloudy-day")
							// || icons.toString().equalsIgnoreCase(
							// "partly-cloudy-night")) {
							// weatherIcon
							// .setBackgroundResource(R.drawable.partly_temp);
							// } else if (icons.toString().equalsIgnoreCase(
							// "cloudy")) {
							// weatherIcon
							// .setBackgroundResource(R.drawable.cloudy_temp);
							// } else if (icons.toString()
							// .equalsIgnoreCase("wind")
							// || icons.toString().equalsIgnoreCase("fog")) {
							// weatherIcon
							// .setBackgroundResource(R.drawable.wind_temp);
							// } else {
							// Log.d("hi no image ", "hiii no image");
							// }

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

	// ************ API hit for "Remove users/personnel" *************//

	public static void removeUsers() {

		/*
		 * Required Parameters “id” : {photo.id}
		 */

		Prefrences.showLoadingDialog(act, "Loading...");
		// RequestParams params= new RequestParams();
		//
		// params.put("report_id", reportID);
		//
		// params.put("user_id", Prefrences.removeUserID);
		// params.put("company_id", Prefrences.removeCompanyID);
		Log.d("", "" + reportIDString + Prefrences.removeCompanyID
				+ Prefrences.removeUserID);
		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		// client.delete(arg0, arg1, arg2)
		if (Prefrences.removeFlag == 1) {
			client.delete(act, Prefrences.url + "/reports/remove_personnel/?"
					+ "report_id=" + Prefrences.removeReportID + "&user_id="
					+ Prefrences.removeUserID// +"&company_id="+Prefrences.removeCompanyID
			, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(String response) {
					JSONObject res = null;
					try {
						res = new JSONObject(response);
						Log.v("response ", "" + res.toString(2));
						String success = res.getString("success");
						Log.d("success", "" + success);
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
		} else if (Prefrences.removeFlag == 2) {
			client.delete(act, Prefrences.url + "/reports/remove_personnel/?"
					+ "report_id=" + Prefrences.removeReportID +
					// "&user_id=" +Prefrences.removeUserID
					"&company_id=" + Prefrences.removeCompanyID,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(String response) {
							JSONObject res = null;
							try {
								res = new JSONObject(response);
								Log.v("response ", "" + res.toString(2));
								String success = res.getString("success");
								Log.d("success", "" + success);
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
		// else if(Prefrences.removeFlag==3)
		// {
		// client.delete(act,
		// Prefrences.url +
		// "/reports/remove_personnel/?"+"report_id="+Prefrences.removeReportID
		// +
		// //"&user_id=" +Prefrences.removeUserID
		// "&safety_id="+Prefrences.removeSafetyID
		// ,new AsyncHttpResponseHandler() {
		//
		// @Override
		// public void onSuccess(String response) {
		// JSONObject res = null;
		// try {
		// res = new JSONObject(response);
		// Log.v("response ", "" + res.toString(2));
		// String success = res.getString("success");
		// Log.d("success", "" + success);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		//
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
	}

	// ************ API for GEt Project reports. *************//
	public void getProjectReports(final String projectId) {
		/*
		 * Required Parameters “punchlist_item” : { “body” : “Remove mold”,
		 * “user_id” : 2, “location” : “Basement”, “user_assignee” : “Max
		 * Haines-Stiles” }, “project_id” : {project.id}
		 */

		Prefrences.showLoadingDialog(ReportItemClick.this, "Loading...");
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(100000);
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.get(ReportItemClick.this, Prefrences.url + "/reports/"
				+ projectId, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {
				SharedPreferences sharedpref;
				sharedpref = ReportItemClick.this.getSharedPreferences(
						"MyPref", 0);
				Editor editor = sharedpref.edit();
				editor.putString("reportlistfragment", response);
				editor.commit();
				fillServerData(response);
				Prefrences.dismissLoadingDialog();

			}

			@Override
			public void onFailure(Throwable arg0) {
				Log.e("request fail", arg0.toString());
				Toast.makeText(ReportItemClick.this, "Server Issue",
						Toast.LENGTH_LONG).show();

				Prefrences.dismissLoadingDialog();
				// if (pull == true) {
				// pull = false;
				// reportlist.onRefreshComplete();
				// }
			}
		});
	}

	public void fillServerData(String response) {
		JSONObject res = null;
		try {

			res = new JSONObject(response);
			Prefrences.report_bool = true;
			Prefrences.report_s = response;
			Log.v("response ", "" + res.toString(2));

			JSONArray reportArrray = res.getJSONArray("reports");
			reportdataArrayList = new ArrayList<Report>();
			// reportdataDaily = new ArrayList<Report>();
			// reportdataSafety = new ArrayList<Report>();
			// reportdataWeekly = new ArrayList<Report>();

			Log.d("report Array length", "" + reportArrray.length());

			for (int i = 0; i < reportArrray.length(); i++) {
				companiesArrayList = new ArrayList<ReportCompanies>();
				reportTopicsArrayList = new ArrayList<ReportTopics>();
				personArrayList = new ArrayList<ReportPersonnel>();

				coUsersArrayList = new ArrayList<ReportCompanyUsers>();

				coSubsArrayList = new ArrayList<ReportCompanySubcontractors>();

				JSONObject reportobj = reportArrray.getJSONObject(i);

				String reportId = reportobj.getString("id");
				Log.d("report_id", "-----------***" + reportId);
				try {
					if (reportId.equals("null")) {
						reportId = "";
					}
				} catch (Exception e) {
					reportId = "";
				}

				String epochTime = reportobj.getString("epoch_time");
				if (epochTime.equals("null")) {
					epochTime = "";
				}
				String createdAt = reportobj.getString("created_at");
				if (createdAt.equals("null")) {
					createdAt = "";
				}
				String updatedat = reportobj.getString("updated_at");
				if (updatedat.equals("null")) {
					updatedat = "";
				}
				String createdDate = reportobj.getString("date_string");
				if (createdDate.equals("null")) {
					createdDate = "";
				}
				String title = reportobj.getString("title");
				if (title.equals("null")) {
					title = "";
				}
				String reportType = reportobj.getString("report_type");
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
				String weathericon = reportobj.getString("weather_icon");
				if (weathericon.equals("null")) {
					weathericon = "";
				}
				String precip = reportobj.getString("precip");
				if (precip.equals("null") || precip.equals("")) {
					precip = "";
				} else {
					precip = precip.substring(0, precip.length() - 1);
					Log.d("waaaooooo", "waaaaaooo " + precip.toString());

					precip = String.format("%.1f", Float.parseFloat(precip))
							+ "%";
				}
				String temp = reportobj.getString("temp");
				if (temp.equals("null")) {
					temp = "";
				}
				String wind = reportobj.getString("wind");
				if (wind.equals("null")) {
					wind = "";
				}
				String humidity = reportobj.getString("humidity");
				if (humidity.equals("null") || humidity.equals("")) {
					humidity = "";
				} else {
					humidity = humidity.substring(0, humidity.length() - 1);
					Log.d("waaaooooo", "waaaaaooo " + humidity.toString());

					humidity = String
							.format("%.1f", Float.parseFloat(humidity)) + "%";
				}

				/*------- author object-------*/
				if (!reportobj.isNull("author")) {
					authorArrayList = new ArrayList<Author>();

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
					String phonenumber = authorobj.getString("phone");
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
					authorArrayList.add(new Author(authorid, firstname, lastname,
							fullname, email, phonenumber));
				} else {
					authorArrayList = new ArrayList<Author>();
					authorArrayList.add(new Author("", "", "", "", "", ""));
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
				JSONArray photos = reportobj.getJSONArray("photos");
				if (photos.length() == 0) {
					photoArrayList = new ArrayList<ProjectPhotos>();
					// Log.d("if", "----photo if null--");
					// photo.add(new ProjectPhotos("", "", "",
					// "drawable", "", "", "", "", "", "",
					// "", "", "", ""));
					// photo_url.add("http://3.bp.blogspot.com/-1EULR14bUFc/T8srSY2KZqI/AAAAAAAAEOk/vTI6mnpZ43g/s1600/nature-wallpaper-15.jpg");
				} else {
					photoArrayList = new ArrayList<ProjectPhotos>();
					for (int k = 0; k < photos.length(); k++) {

						JSONObject photosobj = photos.getJSONObject(k);
						String photoid = photosobj.getString("id");
						String url_large = photosobj.getString("url_large");
						String original = photosobj.getString("original");
						String photo_url200 = photosobj.getString("url_small");
						String url100 = photosobj.getString("url_thumb");
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

						String source = photosobj.getString("source");
						String phase = photosobj.getString("phase");
						String photo_created_at = photosobj
								.getString("created_at");
						String user_name = photosobj.getString("user_name");
						String name = photosobj.getString("name");

						String desc = photosobj.getString("description");
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
						Log.d("----photo url---", "" + photo_url200);
						photoArrayList.add(new ProjectPhotos(photoid, url_large,
								original, photo_url200, url100,
								image_file_size, image_content_type, source,
								phase, photo_created_at, user_name, name, desc,
								photo_created_date,null));
					}
				}

				/*------- personnel array-------*/
				//
				// JSONArray personnel = reportobj
				// .getJSONArray("personnel");
				// if (personnel.length() == 0) {
				// // person.add(new ReportPersonnel("",null,
				// // ""));
				// } else {
				// Log.e("", "how many times" + i);
				//
				// for (j = 0; j < personnel.length(); j++) {
				//
				// JSONObject count = personnel
				// .getJSONObject(j);
				// Log.d("", "count : " + count);
				// if (count.isNull("user")) {
				//
				// // JSONObject psubs = count
				// // .getJSONObject("sub");
				// //
				// // Log.d("", "psubs : " + psubs);
				// // psub = new ArrayList<subcontractors>();
				// //
				// // psub.add(new subcontractors(psubs
				// // .getString("id"), psubs
				// // .getString("name"), psubs
				// // .getString("email"), psubs
				// // .getString("phone"), psubs
				// // .getString("count")));
				// // person.add(new ReportPersonnel(
				// // count.getString("id"),
				// // null, null, psub, count
				// // .getString("count")));
				// } else {
				//
				// JSONObject puser = count
				// .getJSONObject("user");
				//
				// Log.d("", "puser : " + puser);
				// personnelUser = new ArrayList<ReportPersonnelUser>();
				//
				// personnelUser
				// .add(new ReportPersonnelUser(
				// puser.getString("id"),
				// puser.getString("first_name"),
				// puser.getString("last_name"),
				// puser.getString("full_name"),
				// puser.getString("email"),
				// puser.getString("phone")));
				//
				// // if(count.isNull("hours"))
				// // {
				// // person.add(new
				// // ReportPersonnel(count.getString("id"),
				// // psub,count.getString("count")));
				// // }
				// // else
				// person.add(new ReportPersonnel(
				// count.getString("id"),
				// personnelUser,
				// count.getString("hours")
				// ));
				// }
				// }
				// }
				// Log.d("", "person size=" + person.size());
				JSONArray reportCompany = reportobj
						.getJSONArray("report_companies");
				if (reportCompany.length() == 0) {
					// person.add(new ReportPersonnel("",null,
					// ""));
				} else {
					Log.e("report companies", "times = " + i);

					for (j = 0; j < reportCompany.length(); j++) {
						JSONObject count = reportCompany.getJSONObject(j);
						Log.e("report companies", "times j = " + j);
						ArrayList<ReportCompany> Rcompany = new ArrayList<ReportCompany>();
						if (count.isNull("company")) {
							Rcompany.add(new ReportCompany("", "", coUsersArrayList,
									coSubsArrayList));
						} else {
							JSONObject company = count.getJSONObject("company");

							// JSONArray cousers =
							// company.getJSONArray("users");
							// if(cousers.length()!=0)
							// {
							// for(int k=0;k<cousers.length();k++)
							// {
							// Log.e("report companies", "times k = " + k);
							// JSONObject ccount = cousers
							// .getJSONObject(k);
							// coUsers.add(new
							// ReportCompanyUsers(ccount.getString("id"),
							// ccount.getString("first_name"),
							// ccount.getString("last_name"),
							// ccount.getString("full_name"),
							// ccount.getString("email"),
							// ccount.getString("phone")));
							// }
							// }
							// JSONArray cosubs =
							// company.getJSONArray("subcontractors");
							coSubUsersArrayList = new ArrayList<ReportCompanyUsers>();
							// if(cosubs.length()!=0)
							// {
							// for(int k=0;k<cosubs.length();k++)
							// {
							// Log.e("report companies", "times k2 = " + k);
							// JSONObject ccount = cosubs
							// .getJSONObject(k);
							//
							// JSONArray couser = ccount.getJSONArray("users");
							// if(couser.length()!=0)
							// {
							// for(int m=0;m<couser.length();m++)
							// {
							// Log.e("report companies", "times m = " + m);
							// JSONObject cccount = couser.getJSONObject(m);
							//
							// coSubUsers.add(new
							// ReportCompanyUsers(cccount.getString("id"),
							// cccount.getString("first_name"),
							// cccount.getString("last_name"),
							// cccount.getString("full_name"),
							// cccount.getString("email"),
							// cccount.getString("phone")));
							//
							// }
							// coSubs.add(new
							// ReportCompanySubcontractors(ccount.getString("id"),
							// ccount.getString("name"),
							// coSubUsers, ccount.getString("users_count")));
							// }
							// }
							// }

							Rcompany.add(new ReportCompany(company
									.getString("id"),
									company.getString("name"), coUsersArrayList, coSubsArrayList));
						}
						companiesArrayList.add(new ReportCompanies(
								count.getString("id"),
								count.getString("count"), Rcompany));

					}
				}

				/*------- report_users array-------*/
				JSONArray report_users = reportobj.getJSONArray("report_users");

				if (report_users.length() == 0) {
					// person.add(new ReportPersonnel("",null,
					// ""));
				} else {
					Log.e("", "how many times" + i);

					for (j = 0; j < report_users.length(); j++) {

						JSONObject count = report_users.getJSONObject(j);
						Log.d("", "count : " + count);
						if (count.isNull("user")) {

							// JSONObject psubs = count
							// .getJSONObject("sub");
							//
							// Log.d("", "psubs : " + psubs);
							// psub = new ArrayList<subcontractors>();
							//
							// psub.add(new subcontractors(psubs
							// .getString("id"), psubs
							// .getString("name"), psubs
							// .getString("email"), psubs
							// .getString("phone"), psubs
							// .getString("count")));
							// person.add(new ReportPersonnel(
							// count.getString("id"),
							// null, null, psub, count
							// .getString("count")));
						} else {

							JSONObject puser = count.getJSONObject("user");

							JSONObject company = puser.getJSONObject("company");// checkitem

							ArrayList<Company> compny = new ArrayList<Company>();
							compny.add(new Company(company.getString("id"),
									company.getString("name")));

							Log.d("", "puser : " + puser);
							personnelUserArrayList = new ArrayList<ReportPersonnelUser>();

							personnelUserArrayList.add(new ReportPersonnelUser(puser
									.getString("id"), puser
									.getString("first_name"), puser
									.getString("last_name"), puser
									.getString("full_name"), puser
									.getString("email"), puser
									.getString("phone"), compny));

							// if(count.isNull("hours"))
							// {
							// person.add(new
							// ReportPersonnel(count.getString("id"),
							// psub,count.getString("count")));
							// }
							// else
							personArrayList.add(new ReportPersonnel(count
									.getString("id"), personnelUserArrayList, count
									.getString("hours")));
						}
					}
				}
				for (j = 0; j < report_users.length(); j++) {

				}

				/*------- safety_topics array-------*/

				// ReportTopics = new ArrayList<ReportTopics>();
				JSONArray report_topics = reportobj
						.getJSONArray("report_topics");
				for (j = 0; j < report_topics.length(); j++) {
					JSONObject obj = report_topics.getJSONObject(j);
					safeArrayList = new ArrayList<SafetyTopics>();
					JSONObject safety_topic = obj.getJSONObject("safety_topic");

					safeArrayList.add(new SafetyTopics(safety_topic.getString("id"),
							safety_topic.getString("title"), safety_topic
									.getString("info")));

					reportTopicsArrayList.add(new ReportTopics(obj.getString("id"), obj
							.getString("report_id"), safeArrayList));

				}
				Log.d("Safe ", "Size= " + reportTopicsArrayList.size());

				// companies.get(i).
				// Log.d("","sizecouser= "+coUsers.size()+"cosubs"+coSubs.size()+"cosubuser"+coSubUsers.size()+"company"+companies.size());
				/*------- possible_types array-------*/
				JSONArray possible_types = reportobj
						.getJSONArray("possible_types");
				String type;
				for (int j = 0; j < possible_types.length(); j++) {

					type = possible_types.getString(j);
					// Log.d("possible type", "" + type);
					// possibleTypesArray.add(type);

				}
				// if (reportType.equals("Daily")) {
				// reportdataDaily.add((new Report(reportId,
				// epochTime, createdAt, updatedat,
				// createdDate, title, reportType,
				// body, weather, weathericon, precip,
				// temp, wind, humidity, author,
				// photo, person, companies,ReportTopics)));
				// } else if (reportType.equals("Safety")) {
				// reportdataSafety.add((new Report(reportId,
				// epochTime, createdAt, updatedat,
				// createdDate, title, reportType,
				// body, weather, weathericon, precip,
				// temp, wind, humidity, author,
				// photo, person, companies,ReportTopics)));
				// } else if (reportType.equals("Weekly")) {
				// reportdataWeekly.add((new Report(reportId,
				// epochTime, createdAt, updatedat,
				// createdDate, title, reportType,
				// body, weather, weathericon, precip,
				// temp, wind, humidity, author,
				// photo, person, companies,ReportTopics)));
				// }
				reportdataArrayList.add(new Report(reportId, epochTime, createdAt,
						updatedat, createdDate, title, reportType, body,
						weather, weathericon, precip, temp, wind, humidity,
						authorArrayList, photoArrayList, personArrayList, companiesArrayList, reportTopicsArrayList));

				// Log.d("",""+reportdata.get(i).author);
				/*------- comments array-------*/
				JSONArray comments = reportobj.getJSONArray("comments");
				commntArrayList = new ArrayList<Comments>();
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
				JSONArray report_subs = reportobj.getJSONArray("report_subs");
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

			reportDataLocalArrayList.addAll(reportdataArrayList);

			for (int i = 0; i < reportDataLocalArrayList.size(); i++) {
				if (reportDataLocalArrayList.get(i).report_id.toString()
						.equalsIgnoreCase(Prefrences.reportNotification)) {
					Prefrences.ReportPosition = i;
				}
			}

			adapter = new ViewPagerAdapter(ReportItemClick.this,
					reportDataLocalArrayList);
			myPager = (ViewPager) findViewById(R.id.pager);
			myPager.setAdapter(adapter);
			myPager.setCurrentItem(Prefrences.ReportPosition);

			textviewType
					.setText(reportDataLocalArrayList.get(myPager.getCurrentItem()).report_type
							.toString() + " - ");
			textviewDate
					.setText(reportDataLocalArrayList.get(myPager.getCurrentItem()).created_date
							.toString());
			reportIDString = reportDataLocalArrayList.get(myPager.getCurrentItem()).report_id
					.toString();
			reportTypeString = reportDataLocalArrayList.get(myPager.getCurrentItem()).report_type
					.toString();
			Log.v("clicked REPORT ID", "------- " + reportIDString);
			Log.v("clicked REPORT Type", "------- " + reportTypeString);
			for (int i = 0; i < reportDataLocalArrayList.get(myPager.getCurrentItem()).personnel
					.size(); i++) {
				personelnamesArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).personnel
								.get(i).users.get(0).uFullName.toString());
				personelIdArrayList.add(reportDataLocalArrayList.get(myPager
						.getCurrentItem()).personnel.get(i).users.get(0).uId
						.toString());
				personelHoursArrayList.add(reportDataLocalArrayList.get(myPager
						.getCurrentItem()).personnel.get(i).hours.toString());
			}

			for (int i = 0; i < reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
					.size(); i++) {
				CompaniesArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
								.get(i).Rcompany.get(0).coName.toString());
				CompanyIdArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
								.get(i).Rcompany.get(0).coId.toString());
				OnsiteArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
								.get(i).coCount.toString());
			}

			for (int i = 0; i < reportDataLocalArrayList.get(myPager.getCurrentItem()).topic
					.size(); i++) {
				SafetyIDArrayList
						.add(reportDataLocalArrayList.get(myPager.getCurrentItem()).topic
								.get(i).safety.get(0).Id.toString());
				SafetyTitleArrayList.add(reportDataLocalArrayList.get(myPager
						.getCurrentItem()).topic.get(i).safety.get(0).Title
						.toString());
				// OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
			}

			for (int i = 0; i < reportDataLocalArrayList.get(myPager.getCurrentItem()).companies
					.size(); i++) {
				Log.d("naem", "name" + CompaniesArrayList.get(i).toString());// add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
				Log.d("id", "id" + CompanyIdArrayList.get(i).toString());// CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
				Log.d("count", "count" + OnsiteArrayList.get(i).toString());// OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
			}

			Log.v("BBB-", ".." + reportIDString);

			// if(Prefrences.reportlisttypes==0)
			// {
			// reportAdapter = new ReportListAdapter(
			// getActivity(), reportdata);
			// reportlist.setAdapter(reportAdapter);
			// tv_daily.setBackgroundResource(R.drawable.all_white_background);
			// tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			// tv_safety.setBackgroundResource(R.color.white);
			//
			// tv_safety.setTextColor(Color.BLACK);
			// tv_weekly.setTextColor(Color.BLACK);
			// tv_daily.setTextColor(Color.BLACK);
			// }
			// else if(Prefrences.reportlisttypes==1)
			// {
			// reportAdapter = new ReportListAdapter(
			// getActivity(), reportdataDaily);
			// reportlist.setAdapter(reportAdapter);
			// tv_daily.setBackgroundResource(R.drawable.all_black_background);
			// tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			// tv_safety.setBackgroundResource(R.color.white);
			//
			// tv_safety.setTextColor(Color.BLACK);
			// tv_weekly.setTextColor(Color.BLACK);
			// tv_daily.setTextColor(Color.WHITE);
			// }
			// else if(Prefrences.reportlisttypes==2)
			// {
			// reportAdapter = new ReportListAdapter(
			// getActivity(), reportdataSafety);
			// reportlist.setAdapter(reportAdapter);
			// tv_daily.setBackgroundResource(R.drawable.all_white_background);
			// tv_weekly.setBackgroundResource(R.drawable.complete_white_background);
			// tv_safety.setBackgroundResource(R.color.black);
			//
			// tv_safety.setTextColor(Color.WHITE);
			// tv_weekly.setTextColor(Color.BLACK);
			// tv_daily.setTextColor(Color.BLACK);
			// }
			// else if(Prefrences.reportlisttypes==3)
			// {
			// reportAdapter = new ReportListAdapter(
			// getActivity(), reportdataWeekly);
			// reportlist.setAdapter(reportAdapter);
			// tv_daily.setBackgroundResource(R.drawable.all_white_background);
			// tv_weekly.setBackgroundResource(R.drawable.complete_black_background);
			// tv_safety.setBackgroundResource(R.color.white);
			//
			// tv_safety.setTextColor(Color.BLACK);
			// tv_weekly.setTextColor(Color.WHITE);
			// tv_daily.setTextColor(Color.BLACK);
			// }
			//
			// // reportAdapter.notifyDataSetChanged();
			//
			// // Log.d("photourl size",""+photo_url.size());
			// for (int i = 0; i < reportdataDaily.size(); i++) {
			// //
			// Log.d("photo url",""+reportdata_daily.get(i).photos.get(0).url200);
			// Log.d("photo url", "" + i + ":"
			// + reportdataDaily.get(i).photos.size());
			// }
			// if (pull == true) {
			//
			// pull = false;
			// // daily.setBackgroundResource(R.drawable.all_white_background);
			// //
			// weekly.setBackgroundResource(R.drawable.complete_white_background);
			// // safety.setBackgroundResource(R.color.white);
			// //
			// // safety.setTextColor(Color.BLACK);
			// // weekly.setTextColor(Color.BLACK);
			// // daily.setTextColor(Color.BLACK);
			// reportlist.onRefreshComplete();
			// }
			// reportAdapter.notify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
