package com.buildhawk;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

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
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import rmn.androidscreenlibrary.ASSL;
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

import com.buildhawk.utils.Report;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
//import com.horizontalscrollviewwithpageindicator.R;
//import com.horizontalscrollviewwithpageindicator.ViewPagerAdapter;

public class ReportItemClick extends Activity {

	static ArrayList<Report> reportdata = new ArrayList<Report>();
	static String types;
	int pos;
	RelativeLayout ral;
	Button back, save;
	public static TextView texttype, textdate;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
	private static int PICK_CONTACT = 1;
	static String picturePath;
	ViewPager myPager;
	static Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_item_click);

		ral = (RelativeLayout) findViewById(R.id.flipper);
		new ASSL(this, ral, 1134, 720, false);

		back = (Button) findViewById(R.id.back);
		save = (Button) findViewById(R.id.save);
		texttype = (TextView) findViewById(R.id.texttype);
		textdate = (TextView) findViewById(R.id.textdate);
		back.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		save.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		textdate.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		texttype.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		Bundle bundle = getIntent().getExtras();
		pos = bundle.getInt("pos");

		if (Prefrences.reportType == 1) {
			types = "Daily";
			reportdata.addAll(ReportFragment.reportdataDaily);
		} else if (Prefrences.reportType == 2) {
			reportdata.addAll(ReportFragment.reportdataSafety);
			types = "Safety";
		} else if (Prefrences.reportType == 3) {
			reportdata.addAll(ReportFragment.reportdataWeekly);
			types = "Weekly";
		} else {
			reportdata.addAll(ReportFragment.reportdata);
		}
		ViewPagerAdapter adapter = new ViewPagerAdapter(this, reportdata);
		myPager = (ViewPager) findViewById(R.id.pager);
		myPager.setAdapter(adapter);
		myPager.setCurrentItem(pos);
		Prefrences.posViewpager = pos;
		// pos=Prefrences.posViewpager;
		myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				// pos=arg0;
				// Prefrences.posViewpager=arg0;
				Log.d("arg0", "arg0 " + arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		back.setOnClickListener(new OnClickListener() {

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
				reportdata.clear();
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// sspg.updateReportHit(con);

				// adapter.
				// myPager.get
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				updateReportHit(ReportItemClick.this);
				AlertMessage();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
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
		reportdata.clear();
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
			// ViewGroup group = (ViewGroup) myPager.getChildAt(myPager
			// .getCurrentItem() + 1);
			// LinearLayout lin2 = (LinearLayout)
			// group.findViewById(R.id.scrolllayout2);
			// ImageView ladder_page2 = new ImageView(ReportItemClick.this);
			// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			// (int) (200), (int) (200));
			// lp.setMargins(10, 10, 10, 10);
			// ladder_page2.setLayoutParams(lp);
			// File file = new File(picturePath);
			// Picasso.with(ReportItemClick.this)
			// .load(file).placeholder(R.drawable.ic_launcher).into(ladder_page2);
			// // // ladder_page2.setImageBitmap(myBitmap);
			// lin2.addView(ladder_page2);
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
		Log.i("photo[report_id]",
				reportdata.get(myPager.getCurrentItem()).report_id.toString());
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
						reportdata.get(myPager.getCurrentItem()).report_id
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
		JSONObject jsonobj = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		Prefrences.stopingHit = 1;
		try {
			for (int i = 0; i < ReportItemCreate.personelnamesArray.size(); i++) {
				JSONObject jo3 = new JSONObject();
				Log.i("name ", ""
						+ ReportItemCreate.personelnamesArray.get(i).toString());
				jo3.put("user_id", ReportItemCreate.personelIdArrayList.get(i)
						.toString());
				jo3.put("full_name", ReportItemCreate.personelnamesArray.get(i)
						.toString());
				jo3.put("hours", ReportItemCreate.personelHoursArray.get(i)
						.toString());
				jsonArray.put(jo3);
			}

			String notesValue = "";
			ViewGroup group = (ViewGroup) myPager.getChildAt(myPager
					.getCurrentItem() + 1);
			if (group != null) {
				EditText text = (EditText) group.findViewById(R.id.notes);
				notesValue = text.getText().toString();
			}
			Log.i("NOTES", notesValue);
			// Log.d("jo3", "jo3" + jo3.length());
			// try {
			jsonobj.put("author_id", Prefrences.userId);

			jsonobj.put("created_date",
					reportdata.get(myPager.getCurrentItem()).created_date
							.toString());
			jsonobj.put("humidity",
					reportdata.get(myPager.getCurrentItem()).humidity
							.toString());
			jsonobj.put("precip",
					reportdata.get(myPager.getCurrentItem()).precip.toString());
			jsonobj.put("project_id", Prefrences.selectedProId);
			jsonobj.put("report_type",
					reportdata.get(myPager.getCurrentItem()).report_type
							.toString());
			jsonobj.put("temp",
					reportdata.get(myPager.getCurrentItem()).temp.toString());
			jsonobj.put("weather",
					reportdata.get(myPager.getCurrentItem()).weather.toString());
			jsonobj.put("weather_icon",
					reportdata.get(myPager.getCurrentItem()).weather_icon
							.toString());
			jsonobj.put("wind",
					reportdata.get(myPager.getCurrentItem()).wind.toString());
			jsonobj.put("body", notesValue);
			jsonobj.put("report_users", jsonArray);

			JSONObject finalJson = new JSONObject();
			finalJson.put("report", jsonobj);

			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
					.getBytes("UTF-8"));

			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			client.put(
					con,
					Prefrences.url
							+ "/reports/"
							+ reportdata.get(myPager.getCurrentItem()).report_id
									.toString(), entity, "application/json",
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
							Log.e("request fail", arg0.toString());
							Prefrences.dismissLoadingDialog();
						}
					});

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//
	// // public class DetailOnPageChangeListener extends
	// // ViewPager.SimpleOnPageChangeListener {
	// //
	// // private int currentPage;
	// //
	// // @Override
	// // public void onPageSelected(int position) {
	// //
	// // currentPage=ret;
	// // Log.d("","--position--"+ret);
	// // }
	// //
	// // public final int getCurrentPage() {
	// // Log.d("","--currentpage--"+ret);
	// // return currentPage;
	// // }
	// // }
}
