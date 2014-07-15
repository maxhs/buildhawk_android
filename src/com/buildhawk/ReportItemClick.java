package com.buildhawk;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import rmn.androidscreenlibrary.ASSL;
import android.R.array;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.CompanyList.adapter;
import com.buildhawk.utils.Author;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
//import com.horizontalscrollviewwithpageindicator.R;
//import com.horizontalscrollviewwithpageindicator.ViewPagerAdapter;

public class ReportItemClick extends Activity {

	String notesValue = "";
	 public static ArrayList<Report> reportdata_local;
	String types;
	//int pos;
	RelativeLayout ral;
	Button btn_back, btn_save;
	public static TextView tv_texttype, tv_textdate;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
	private static int PICK_CONTACT = 1;
	static String picturePath;
	ViewPager myPager;
	static Uri imageUri;
	
	public  ArrayList<ReportPersonnel> person;
	static String reportID;
	String reportType;
//	String companyID;
	
	public  ArrayList<ReportCompanies>companies;
//	public static ArrayList<ReportCompany>Rcompany;
	public  ArrayList<ReportCompanyUsers>coUsers;
	public  ArrayList<ReportCompanyUsers>coSubUsers;
	public  ArrayList<ReportCompanySubcontractors>coSubs;
	public ArrayList<SafetyTopics>safe;
	
	public ArrayList<ReportTopics>ReportTopics;
	
	public static ArrayList<String> personelnamesArray = new ArrayList<String>();
	public static ArrayList<String> personelIdArrayList = new ArrayList<String>();
	public static ArrayList<String> personelHoursArray = new ArrayList<String>();
	public static ArrayList<String> CompaniesArray = new ArrayList<String>();
	public static ArrayList<String> OnsiteArray = new ArrayList<String>();
	public static ArrayList<String> CompanyIdArrayList = new ArrayList<String>();
	public static ArrayList<String> SafetyIDArray = new ArrayList<String>();
	public static ArrayList<String> SafetyTitleArray = new ArrayList<String>();
	ViewPagerAdapter adapter;
	
	static Activity act;

	String winds, tempsHigh, precips, humiditys, tempsLow, icons, summarys,temps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_item_click);

		ral = (RelativeLayout) findViewById(R.id.flipper);
		new ASSL(this, ral, 1134, 720, false);
act=ReportItemClick.this;
		btn_back = (Button) findViewById(R.id.back);
		btn_save = (Button) findViewById(R.id.save);
		tv_texttype = (TextView) findViewById(R.id.texttype);
		tv_textdate = (TextView) findViewById(R.id.textdate);
		btn_back.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		btn_save.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		tv_textdate.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));
		tv_texttype.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));
		Bundle bundle = getIntent().getExtras();
		Prefrences.ReportPosition = bundle.getInt("pos");
		weathertHit();
		reportdata_local = new ArrayList<Report>();
		if (Prefrences.reportType == 1) {
			types = "Daily";
			reportdata_local.addAll(ReportFragment.reportdataDaily);
		} else if (Prefrences.reportType == 2) {
			reportdata_local.addAll(ReportFragment.reportdataSafety);
			types = "Safety";
		} else if (Prefrences.reportType == 3) {
			reportdata_local.addAll(ReportFragment.reportdataWeekly);
			types = "Weekly";
		} else if(Prefrences.reportType == 4) 
		{
			reportdata_local.addAll(ProjectDetail.dateReports);
		}
		else if(Prefrences.reportType == 0){
			reportdata_local.addAll(ReportFragment.reportdata);
		}
		
		
		CompaniesArray.clear();
		CompanyIdArrayList.clear();
		OnsiteArray.clear();
		personelHoursArray.clear();
		personelnamesArray.clear();
		personelIdArrayList.clear();
		SafetyIDArray.clear();
		SafetyTitleArray.clear();
		
		 adapter = new ViewPagerAdapter(ReportItemClick.this, reportdata_local);
		myPager = (ViewPager) findViewById(R.id.pager);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(Prefrences.ReportPosition);
		
		
		tv_texttype.setText(reportdata_local.get(myPager.getCurrentItem()).report_type.toString() + " - ");
		tv_textdate.setText(reportdata_local.get(myPager.getCurrentItem()).created_date.toString());
		reportID=reportdata_local.get(myPager.getCurrentItem()).report_id.toString();
		reportType=reportdata_local.get(myPager.getCurrentItem()).report_type.toString();
		Log.v("clicked REPORT ID", "------- "+reportID);
		Log.v("clicked REPORT Type", "------- "+reportType);
		for(int i=0;i<reportdata_local.get(myPager.getCurrentItem()).personnel.size();i++)
		{
			personelnamesArray.add(reportdata_local.get(myPager.getCurrentItem()).personnel.get(i).users.get(0).uFullName.toString());
			personelIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).personnel.get(i).users.get(0).uId.toString());
			personelHoursArray.add(reportdata_local.get(myPager.getCurrentItem()).personnel.get(i).hours.toString());
		}

		for(int i=0;i<reportdata_local.get(myPager.getCurrentItem()).companies.size();i++)
		{
			CompaniesArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
			CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
			OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
		}
		
		for(int i=0;i<reportdata_local.get(myPager.getCurrentItem()).topic.size();i++)
		{
			SafetyIDArray.add(reportdata_local.get(myPager.getCurrentItem()).topic.get(i).safety.get(0).Id.toString());
			SafetyTitleArray.add(reportdata_local.get(myPager.getCurrentItem()).topic.get(i).safety.get(0).Title.toString());
//			OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
		}
		
		
		
		for(int i=0;i<reportdata_local.get(myPager.getCurrentItem()).companies.size();i++)
		{
			Log.d("naem","name"+CompaniesArray.get(i).toString());//add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
			Log.d("id","id"+CompanyIdArrayList.get(i).toString());//CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
			Log.d("count","count"+OnsiteArray.get(i).toString());//OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
		}
		
		Log.v("BBB-", ".."+reportID);
		myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			int oldPos = myPager.getCurrentItem();
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				// pos=arg0;
				// Prefrences.posViewpager=arg0;
				Log.d("arg0", "arg0 " + arg0);
				tv_texttype.setText(reportdata_local.get(myPager.getCurrentItem()).report_type.toString() + " - ");
				tv_textdate.setText(reportdata_local.get(myPager.getCurrentItem()).created_date.toString());
				reportID=reportdata_local.get(myPager.getCurrentItem()).report_id.toString();
//				companyID=reportdata_local.get(myPager.getCurrentItem()).companies.get(0).coId.toString();
				reportType=reportdata_local.get(myPager.getCurrentItem()).report_type.toString();
				Log.v("REPORT ID", "------- "+reportID);
				Log.v("REPORT Type", "------- "+reportType);
				
//				CompaniesArray.clear();
//				CompanyIdArrayList.clear();
//				OnsiteArray.clear();
//				personelHoursArray.clear();
//				personelnamesArray.clear();
//				personelIdArrayList.clear();
//				SafetyIDArray.clear();
//				SafetyTitleArray.clear();
//				
//				
//					for(int i=0;i<reportdata_local.get(myPager.getCurrentItem()+1).personnel.size();i++)
//					{
//						personelnamesArray.add(reportdata_local.get(myPager.getCurrentItem()+1).personnel.get(i).users.get(0).uFullName.toString());
//						personelIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()+1).personnel.get(i).users.get(0).uId.toString());
//						personelHoursArray.add(reportdata_local.get(myPager.getCurrentItem()+1).personnel.get(i).hours.toString());
//					}
//
//					for(int i=0;i<reportdata_local.get(myPager.getCurrentItem()+1).companies.size();i++)
//					{
//						CompaniesArray.add(reportdata_local.get(myPager.getCurrentItem()+1).companies.get(i).Rcompany.get(0).coName.toString());
//						CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()+1).companies.get(i).Rcompany.get(0).coId.toString());
//						OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()+1).companies.get(i).coCount.toString());
//					}
//					
//					for(int i=0;i<reportdata_local.get(myPager.getCurrentItem()+1).topic.size();i++)
//					{
//						SafetyIDArray.add(reportdata_local.get(myPager.getCurrentItem()+1).topic.get(i).safety.get(0).Id.toString());
//						SafetyTitleArray.add(reportdata_local.get(myPager.getCurrentItem()+1).topic.get(i).safety.get(0).Title.toString());
////						OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
//					}

	           
				
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
		
//		myPager.notify();
//		Prefrences.posViewpager = pos;
		// pos=Prefrences.posViewpager;
		

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ProjectDetail.Flag=0;
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				reportdata_local.clear();
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		btn_save.setOnClickListener(new OnClickListener() {

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
				if(ConnectionDetector.isConnectingToInternet()){
				updateReportHit(ReportItemClick.this);
				}else{
					Toast.makeText(ReportItemClick.this,"No internet connection.", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		adapter.notifyDataSetChanged();
		if(Prefrences.stopingHit==1){
			Prefrences.stopingHit=0;
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(Prefrences.ReportPosition);
		
//		ViewPagerAdapter.companyadapter.notifyDataSetChanged();
		if(CompaniesArray.size()>0)
		{
		for(int i=0;i<CompaniesArray.size();i++)
		{
			Log.d("Company","name"+CompaniesArray.get(i).toString());//add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
			Log.d("Company","id"+CompanyIdArrayList.get(i).toString());//CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
			Log.d("Company","count"+OnsiteArray.get(i).toString());//OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
		}
		}
		if(personelnamesArray.size()>0)
		{
		for(int i=0;i<personelIdArrayList.size();i++)
		{
			Log.d("personnel","name"+personelnamesArray.get(i).toString());//add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
			Log.d("personnel","id"+personelIdArrayList.get(i).toString());//CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
			Log.d("personnel","hours"+personelHoursArray.get(i).toString());//OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
		}
		}
		if(SafetyIDArray.size()>0)
		{
		for(int i=0;i<SafetyIDArray.size();i++)
		{
			Log.d("safety","title"+SafetyTitleArray.get(i).toString());//add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coName.toString());
			Log.d("safety","id"+SafetyIDArray.get(i).toString());//CompanyIdArrayList.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).Rcompany.get(0).coId.toString());
//			Log.d("count","count"+OnsiteArray.get(i).toString());//OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
		}
		}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
//		 try {
//		
//		 InputMethodManager imm = (InputMethodManager)
//		 getSystemService(Context.INPUT_METHOD_SERVICE);
//		
//		 imm.hideSoftInputFromWindow(getCurrentFocus()
//		
//		 .getWindowToken(), 0);
//		
//		 } catch (Exception exception) {
//		
//		 exception.printStackTrace();
//		
//		 }
		reportdata_local.clear();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Prefrences.stopingHit = 1;
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
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
		}
	}

	class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			try {

				postPicture();

			} catch (ParseException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			} catch (IOException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			} catch (XmlPullParserException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			Prefrences.dismissLoadingDialog();
//			 ViewGroup group = (ViewGroup) myPager.getChildAt(myPager.getCurrentItem());
//			 LinearLayout lin2 = (LinearLayout)
//			 group.findViewById(R.id.scrolllayout2);
//			 ImageView ladder_page2 = new ImageView(ReportItemClick.this);
//			 LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//			 (int) (200), (int) (200));
//			 lp.setMargins(10, 10, 10, 10);
//			 ladder_page2.setLayoutParams(lp);
//			 File file = new File(picturePath);
//			 Picasso.with(ReportItemClick.this)
//			 .load(file).placeholder(R.drawable.ic_launcher).into(ladder_page2);
//			 // // ladder_page2.setImageBitmap(myBitmap);
//			 lin2.addView(ladder_page2);
		}

		@Override
		protected void onPreExecute() {
			Prefrences.showLoadingDialog(ReportItemClick.this, "Uploading...");
			ViewGroup group = (ViewGroup) myPager.getChildAt(myPager
					.getCurrentItem());
			LinearLayout lin2 = (LinearLayout) group
					.findViewById(R.id.scrolllayout2);
			ImageView ladder_page2 = new ImageView(ReportItemClick.this);
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					(int) (200), (int) (200));
			layoutParam.setMargins(10, 10, 10, 10);
			ladder_page2.setLayoutParams(layoutParam);
			File file = new File(picturePath);
			Picasso.with(ReportItemClick.this).load(file)
					.placeholder(R.drawable.ic_launcher).into(ladder_page2);
			// // ladder_page2.setImageBitmap(myBitmap);
			lin2.addView(ladder_page2);
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}

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

		Log.v("picturePath", picturePath);

		File file = new File(picturePath);

		FileBody cbFile = new FileBody(file, "image/jpg");

		cbFile.getMediaType();
		Log.i("photo[report_id]",reportID);
//				reportdata_local.get(myPager.getCurrentItem()).report_id.toString());
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
						reportdata_local.get(myPager.getCurrentItem()).report_id
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

	void updateReportHit(Activity con) {

		Prefrences.showLoadingDialog(con, "Loading...");
//		JSONObject jsonobj = new JSONObject();

		RequestParams params = new RequestParams();
		Log.e("myPager.getCurrentItem()", ",, "+myPager.getCurrentItem());

		
			notesValue = Prefrences.report_body_edited;
			Prefrences.report_body_edited = "";
		
			
			
		Log.i("NOTES", notesValue);
		Log.i("Group ","group  "+ myPager.getChildAt(myPager
				.getCurrentItem() + 1));
		Log.i("report id ","report id  "+ reportID);
		params.put("user_id", Prefrences.userId);
		params.put("report[body]", notesValue);
		params.put("report[report_type]", reportType);
		params.put("report[project_id]", Prefrences.selectedProId);
		params.put("report[date_string]", tv_textdate.getText().toString());
//		params.put("report[wind]", "");
//		params.put("report[weather]", "");
//		params.put("report[precip]", "");
//		params.put("report[weather_icon]", "");
//		JSONArray array = new JSONArray();
//		try{
//		
//		for (int i = 0; i < CompaniesArray.size(); i++) {
//			JSONObject jo3 = new JSONObject();
//			Log.i("name ", "" + CompaniesArray.get(i).toString());
//			jo3.put("user_id", CompanyIdArrayList.get(i).toString());
//			jo3.put("full_name", CompaniesArray.get(i).toString());
//			jo3.put("hours", OnsiteArray.get(i).toString());
//			array.put(jo3);
//		}
//		}catch(Exception e)
//		{
//			
//		}
		
//		params.put("report[report_users]", 
//		params.put(arg0, CompaniesArray)
//		params.put("report[report_companies]", CompaniesArray);
//		params.put("report[safety_topics]", "");

		JSONObject jsonobj = new JSONObject();
		JSONObject finalJson = new JSONObject();
		JSONArray companyArray = new JSONArray();
		JSONArray personnelArray = new JSONArray();
		JSONArray safetyArray = new JSONArray();
		try {
			jsonobj.put("body", notesValue );
			jsonobj.put("report_type", reportType);
			jsonobj.put("project_id", Prefrences.selectedProId);
			jsonobj.put("date_string", tv_textdate.getText().toString());
			jsonobj.put("wind", winds);
			jsonobj.put("weather", summarys);
			jsonobj.put("precip", precips);
			jsonobj.put("weather_icon", icons);
			

			Log.d("CompanyArray","Size of CompanyArray"+CompaniesArray.size());
			
			for (int i = 0; i < CompaniesArray.size(); i++) {
				JSONObject jo3 = new JSONObject();
				Log.i("name ", "" + CompaniesArray.get(i).toString());
				jo3.put("name", CompaniesArray.get(i).toString());
				jo3.put("id", CompanyIdArrayList.get(i).toString());
				jo3.put("count", OnsiteArray.get(i).toString());
				companyArray.put(jo3);
			}
			
			jsonobj.put("report_companies", companyArray);
			
			for (int i = 0; i < personelnamesArray.size(); i++) {
				JSONObject jo3 = new JSONObject();
				Log.i("name ", "" + personelnamesArray.get(i).toString());
				jo3.put("full_name", personelnamesArray.get(i).toString());
				jo3.put("id", personelIdArrayList.get(i).toString());			
				jo3.put("hours", personelHoursArray.get(i).toString());
				personnelArray.put(jo3);
			}
			
			jsonobj.put("report_users", personnelArray);
			
			for (int i = 0; i < SafetyIDArray.size(); i++) {
				JSONObject jo3 = new JSONObject();
				Log.i("ID ", "" + SafetyIDArray.get(i).toString());
				jo3.put("title", SafetyTitleArray.get(i).toString());
				jo3.put("id", SafetyIDArray.get(i).toString());

//				jo3.put("hours", personelHoursArray.get(i).toString());
				safetyArray.put(jo3);
			}
			jsonobj.put("safety_topics", safetyArray);
			
			finalJson.put("user_id", 46);
			finalJson.put("report", jsonobj);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Prefrences.stopingHit = 1;
		try {
			
			
			Log.v("final json = ", finalJson.toString(2));
						
			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
					.getBytes("UTF-8"));


			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");
			
			String report_id_val="";
									
				report_id_val = reportdata_local.get(myPager.getCurrentItem()).report_id
				.toString();
			
			
			
			
			Log.v("AAAAA-", ".."+reportID);
			
			Log.v("AAAAA-", ".."+ reportdata_local.get(myPager.getCurrentItem()).body
					.toString());
			Log.v("AAAAA-", ".."+ reportdata_local.get(myPager.getCurrentItem()).created_date
					.toString());

			client.put(
					con,
					Prefrences.url
							+ "/reports/"
							+ reportID, entity, "application/json", 
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							JSONObject res = null;
							ArrayList<Author> author;
							try {
								res = new JSONObject(response);
								Log.v("response ---- ",
										"---*****----" + res.toString(2));
								

								person = new ArrayList<ReportPersonnel>();
								companies= new ArrayList<ReportCompanies>();
								ReportTopics = new ArrayList<ReportTopics>();
//								Rcompany = new ArrayList<ReportCompany>();
								coUsers = new ArrayList<ReportCompanyUsers>();
								
								coSubs = new ArrayList<ReportCompanySubcontractors>();
								
								JSONObject reportobj = res.getJSONObject("report");
								
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

								
								JSONArray photos = reportobj
										.getJSONArray("photos");
								ArrayList<ProjectPhotos> photo;
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
//									Log.d("", "person size=" + person.size());
									JSONArray reportCompany = reportobj
											.getJSONArray("report_companies");
									if (reportCompany.length() == 0) {
										// person.add(new ReportPersonnel("",null,
										// ""));
									} else {
										

										for (int j = 0; j < reportCompany.length(); j++) {
											JSONObject count = reportCompany
													.getJSONObject(j);
											Log.e("report companies", "times j = " + j);
											JSONObject company = count.
													getJSONObject("company");
											
//											JSONArray cousers = company.getJSONArray("users");
//											if(cousers.length()!=0)
//											{
//												for(int k=0;k<cousers.length();k++)
//												{
//													Log.e("report companies", "times k = " + k);
//													JSONObject ccount = cousers
//															.getJSONObject(k);
//													coUsers.add(new ReportCompanyUsers(ccount.getString("id"), ccount.getString("first_name"), 
//															ccount.getString("last_name"), ccount.getString("full_name"), ccount.getString("email"), 
//															ccount.getString("phone")));
//												}
//											}
//											JSONArray cosubs = company.getJSONArray("subcontractors");
//											coSubUsers = new ArrayList<ReportCompanyUsers>();
//											if(cosubs.length()!=0)
//											{
//												for(int k=0;k<cosubs.length();k++)
//												{
//													Log.e("report companies", "times k2 = " + k);
//													JSONObject ccount = cosubs
//															.getJSONObject(k);
//													
//													JSONArray couser = ccount.getJSONArray("users");
//													if(couser.length()!=0)
//													{
//													for(int m=0;m<couser.length();m++)
//													{
//														Log.e("report companies", "times m = " + m);
//														JSONObject cccount = couser.getJSONObject(m);
//														
//														coSubUsers.add(new ReportCompanyUsers(cccount.getString("id"), cccount.getString("first_name"), 
//															cccount.getString("last_name"), cccount.getString("full_name"), cccount.getString("email"), 
//															cccount.getString("phone")));
//														
//													}
//													coSubs.add(new ReportCompanySubcontractors(ccount.getString("id"), ccount.getString("name"),
//															coSubUsers, ccount.getString("users_count")));
//													}
//												}
//											}
											ArrayList<ReportCompany>Rcompany = new ArrayList<ReportCompany>();
											Rcompany.add(new ReportCompany(company.getString("id"), company.getString("name"), coUsers, coSubs));
											
											companies.add(new ReportCompanies(count.getString("id"), count.getString("count"), Rcompany));
										
								
									}
								}
//									ReportTopics = new ArrayList<ReportTopics>();
									JSONArray report_topics = reportobj	.getJSONArray("report_topics");
									for (int j = 0; j < report_topics.length(); j++) {
										JSONObject obj = report_topics.getJSONObject(j);
										safe = new ArrayList<SafetyTopics>();
										JSONObject safety_topic = obj.getJSONObject("safety_topic");
										
										safe.add(new SafetyTopics(safety_topic.getString("id"),
										safety_topic.getString("title"),
										safety_topic.getString("info")));
										
										ReportTopics.add(new ReportTopics(obj.getString("id"), obj.getString("report_id"), safe));
										
									}
								
									
//								companies.get(i).
								//Log.d("","sizecouser= "+coUsers.size()+"cosubs"+coSubs.size()+"cosubuser"+coSubUsers.size()+"company"+companies.size());
								/*------- possible_types array-------*/

								if (Prefrences.reportlisttypes==1) {
									ReportFragment.reportdataDaily.set(myPager.getCurrentItem(),(new Report(reportId,
											epochTime, createdAt, updatedat,
											createdDate, title, reportType,
											body, weather, weathericon, precip,
											temp, wind, humidity, author,
											photo, person, companies,ReportTopics)));
								} else if (Prefrences.reportlisttypes==2) {
									ReportFragment.reportdataSafety.set(myPager.getCurrentItem(),(new Report(reportId,
											epochTime, createdAt, updatedat,
											createdDate, title, reportType,
											body, weather, weathericon, precip,
											temp, wind, humidity, author,
											photo, person, companies,ReportTopics)));
								} else if (Prefrences.reportlisttypes==3) {
									ReportFragment.reportdataWeekly.set(myPager.getCurrentItem(),(new Report(reportId,
											epochTime, createdAt, updatedat,
											createdDate, title, reportType,
											body, weather, weathericon, precip,
											temp, wind, humidity, author,
											photo, person, companies,ReportTopics)));
								}else{
									ReportFragment.reportdata.set(myPager.getCurrentItem(),(new Report(reportId,
											epochTime, createdAt, updatedat,
											createdDate, title, reportType,
											body, weather, weathericon, precip,
											temp, wind, humidity, author,
											photo, person, companies,ReportTopics)));
								}
								
								ReportFragment.fromReportItem=true;
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
							winds = current.getString("windSpeed");
							tempsHigh = current.getString("temperature");
							tempsLow = current.getString("apparentTemperature");

							precips = current.getString("precipProbability");
							double pre = Double.parseDouble(precips);
							
							pre = pre * 100;
							precips = Double.toString(pre);
//							precips= String.format("%.1f", precips);
							// precips=precips.spl

							humiditys = current.getString("humidity");
							double hum = Double.parseDouble(humiditys);
							hum = hum * 100;
							humiditys = Double.toString(hum);
//							humiditys= String.format("%.1f", humiditys);
							// bodys=current.getString("windSpeed");
							
							icons = current.getString("icon");
							summarys = current.getString("summary");
							// winds=current.getString("windSpeed");

							Log.d("responseeeee", "responseeeee" + winds
									+ tempsHigh + precips + humiditys
									+ tempsLow + icons + summarys);
//							if (Prefrences.selecteddate != "") {
//								date.setText(Prefrences.selecteddate);
//							} else {
//								date.setText(currentDateandTime);
//							}
							winds=winds+"mph";
							temps=(tempsLow + (char) 0x00B0 + " / "
									+ tempsHigh + (char) 0x00B0);
							precips = String.format("%.2f",
									Float.parseFloat(precips)+"%");
//							precip.setText(str + "%");
							humiditys= String.format("%.2f",
									Float.parseFloat(humiditys)+"%");
//							humidity.setText(str2 + "%");
//							weather.setText(summarys);
//							if (icons.toString().equalsIgnoreCase("clear-day")
//									|| icons.toString().equalsIgnoreCase(
//											"clear-night")) {
//								weatherIcon
//										.setBackgroundResource(R.drawable.sunny_temp);
//							} else if (icons.toString()
//									.equalsIgnoreCase("rain")
//									|| icons.toString().equalsIgnoreCase(
//											"sleet")) {
//								weatherIcon
//										.setBackgroundResource(R.drawable.rainy_temp);
//							} else if (icons.toString().equalsIgnoreCase(
//									"partly-cloudy-day")
//									|| icons.toString().equalsIgnoreCase(
//											"partly-cloudy-night")) {
//								weatherIcon
//										.setBackgroundResource(R.drawable.partly_temp);
//							} else if (icons.toString().equalsIgnoreCase(
//									"cloudy")) {
//								weatherIcon
//										.setBackgroundResource(R.drawable.cloudy_temp);
//							} else if (icons.toString()
//									.equalsIgnoreCase("wind")
//									|| icons.toString().equalsIgnoreCase("fog")) {
//								weatherIcon
//										.setBackgroundResource(R.drawable.wind_temp);
//							} else {
//								Log.d("hi no image ", "hiii no image");
//							}

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
	
//	// ************ API hit for "DELETE Photo/Document" *************//
//
//		public static void removeUsers() {
//
//			/*
//			 * Required Parameters “id” : {photo.id}
//			 */
//			
//			Prefrences.showLoadingDialog(act, "Loading...");
//			RequestParams params= new RequestParams();
//			
//			params.put("report_id", reportID);
//			params.put("user_id", Prefrences.removeUserID);
//			params.put("company_id", Prefrences.removeCompanyID);
//			
//			AsyncHttpClient client = new AsyncHttpClient();
//
//			client.addHeader("Content-type", "application/json");
//			client.addHeader("Accept", "application/json");
//
//			client.delete(act,
//					Prefrences.url + "/reports/remove_personnel" ,params,
//					new AsyncHttpResponseHandler() {
//
//						@Override
//						public void onSuccess(String response) {
//							JSONObject res = null;
//							try {
//								res = new JSONObject(response);
//								Log.v("response ", "" + res.toString(2));
//								String success = res.getString("success");
//								Log.d("success", "" + success);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							
//							
//							Prefrences.dismissLoadingDialog();
//						}
//
//						@Override
//						public void onFailure(Throwable arg0) {
//							Log.e("request fail", arg0.toString());
//							Prefrences.dismissLoadingDialog();
//						}
//					});
//		}
}
