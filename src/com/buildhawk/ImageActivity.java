package com.buildhawk;

/*
 *  This file shows the gallery  and sorting of all photos.
 */

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.ProjectPhotos;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

public class ImageActivity extends Activity {

	public static ArrayList<ProjectPhotos> arraylist = new ArrayList<ProjectPhotos>();

	ArrayList<String> workuserArrayList, checkuserArrayList, reportuserArrayList, docuserArrayList, allusersArrayList,
			docdateArrayList, workdateArrayList, checkdateArrayList, reportdateArrayList, alldatesArrayList, docphaseArrayList,
			checklistphaseArrayList, worklistphaseArrayList, reportphaseArrayList, allphaseArrayList;

	ArrayList<String> usersArrayList = new ArrayList<String>();
	ArrayList<String> tempusersArrayList = new ArrayList<String>();
	ArrayList<String> tempdatesArrayList = new ArrayList<String>();
	ArrayList<String> datesArrayList = new ArrayList<String>();

	ArrayList<String> tempphaseArrayList = new ArrayList<String>();
	ArrayList<String> phaseArrayList = new ArrayList<String>();
	ArrayList<String> arrArrayList = new ArrayList<String>();
	ArrayList<String> idsArrayList = new ArrayList<String>();
	ArrayList<String> descArrayList = new ArrayList<String>();
	ArrayList<String> contenttype = new ArrayList<String>();
	
	
	Dialog dialog;
	int flag = 0;
	LinearLayout linearlayout;
	LinearLayout linearlayoutMain;
	ImageView imageview;
	TextView textview, textviewSort, textviewProject;
	int count = 0;
	RelativeLayout relativelayoutRyt;
	RelativeLayout relativelayoutBack;
	TextView textviewKey;
	ConnectionDetector connDect;
	Boolean isInternetPresentBoolean = false;
	String keyString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		relativelayoutRyt = (RelativeLayout) findViewById(R.id.relativelayoutRyt);
		new ASSL(this, relativelayoutRyt, 1134, 720, false);
		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresentBoolean = connDect.isConnectingToInternet();
		// Bundle bundle=new Bundle();
		// bundle.getString("key",)
		textviewSort = (TextView) findViewById(R.id.textviewSort);
		textviewSort.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		// projecttext=(TextView)findViewById(R.id.project_txt);
		relativelayoutBack = (RelativeLayout) findViewById(R.id.relativelayoutBack);

		// projecttext.setText(Prefrences.selectedProName);
		Bundle bundle = getIntent().getExtras();
		keyString = bundle.getString("key");
		workuserArrayList = new ArrayList<String>();
		checkuserArrayList = new ArrayList<String>();
		reportuserArrayList = new ArrayList<String>();
		docuserArrayList = new ArrayList<String>();
		allusersArrayList = new ArrayList<String>();
		docdateArrayList = new ArrayList<String>();
		checkdateArrayList = new ArrayList<String>();
		workdateArrayList = new ArrayList<String>();
		reportdateArrayList = new ArrayList<String>();
		alldatesArrayList = new ArrayList<String>();
		docphaseArrayList = new ArrayList<String>();
		checklistphaseArrayList = new ArrayList<String>();
		worklistphaseArrayList = new ArrayList<String>();
		reportphaseArrayList = new ArrayList<String>();
		allphaseArrayList = new ArrayList<String>();

		workuserArrayList = bundle.getStringArrayList("work");
		checkuserArrayList = bundle.getStringArrayList("check");
		reportuserArrayList = bundle.getStringArrayList("report");
		docuserArrayList = bundle.getStringArrayList("doc");
		allusersArrayList = bundle.getStringArrayList("allusers");

		docdateArrayList = bundle.getStringArrayList("doc_date");
		checkdateArrayList = bundle.getStringArrayList("check_date");
		workdateArrayList = bundle.getStringArrayList("work_date");
		reportdateArrayList = bundle.getStringArrayList("report_date");
		alldatesArrayList = bundle.getStringArrayList("all_date");

		docphaseArrayList = bundle.getStringArrayList("doc_phase");
		checklistphaseArrayList = bundle.getStringArrayList("checklist_phase");
		worklistphaseArrayList = bundle.getStringArrayList("worklist_phase");
		reportphaseArrayList = bundle.getStringArrayList("report_phase");
		allphaseArrayList = bundle.getStringArrayList("all_phase");

		arraylist.clear();

		linearlayoutMain = (LinearLayout) findViewById(R.id.linearlayoutMain);
		new ASSL(this, linearlayoutMain, 1134, 720, false);

		Prefrences.well = 1;

		relativelayoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				arraylist.clear();
				Prefrences.well = 0;
				// work_user, check_user, report_user, doc_user, all_users,
				// doc_date, work_date, check_date, report_date, all_dates,
				// doc_phase,
				// checklist_phase, worklist_phase, report_phase, all_phase;

				usersArrayList.clear();
				tempusersArrayList.clear();

				tempdatesArrayList.clear();
				datesArrayList.clear();
				allusersArrayList.clear();
				workuserArrayList.clear();
				checkuserArrayList.clear();
				reportuserArrayList.clear();

				docuserArrayList.clear();
				tempphaseArrayList.clear();
				phaseArrayList.clear();
				arrArrayList.clear();
				idsArrayList.clear();
				descArrayList.clear();
				contenttype.clear();

				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		textviewSort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(ImageActivity.this,
						android.R.style.Theme_Translucent_NoTitleBar);
				// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialogbox_sort);
				// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				FrameLayout fm = (FrameLayout) dialog.findViewById(R.id.rv);
				new ASSL(ImageActivity.this, fm, 1134, 720, false);

				// Window window = dialog.getWindow();
				// WindowManager.LayoutParams wlp = window.getAttributes();
				// wlp.gravity = Gravity.BOTTOM;
				// wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
				// // wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
				// window.setAttributes(wlp);

				// dialog.setTitle("Sort");
				Button buttonSort = (Button) dialog.findViewById(R.id.buttonSort);
				Button buttonUsers = (Button) dialog.findViewById(R.id.buttonUsers);
				RelativeLayout relativelayoutListoutside = (RelativeLayout) dialog
						.findViewById(R.id.relativelayoutListoutside);
				TextView textviewWho = (TextView) dialog.findViewById(R.id.textviewWho);
				Button buttonSub = (Button) dialog.findViewById(R.id.buttonSub);
				Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);

				buttonCancel.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonSort.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonSub.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonUsers.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				textviewWho.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				textviewWho.setGravity(Gravity.CENTER);
				textviewWho.setText("Sort");
				if (keyString.equals("All")) {
					buttonSort.setVisibility(View.VISIBLE);
					buttonSort.setText("By Taken/Uploaded");
					buttonUsers.setText("By Date");
					buttonSub.setText("By Default");
				} else if (keyString.equals("Checklist Pictures")) {
					buttonSort.setVisibility(View.VISIBLE);
					buttonSort.setText("By Taken/Uploaded");
					buttonUsers.setText("By Date");
					buttonSub.setText("By Default");
				} else if (keyString.equals("Task Pictures")) {
					buttonSort.setVisibility(View.VISIBLE);
					buttonSort.setText("By Taken/Uploaded");
					buttonUsers.setText("By Date");
					buttonSub.setText("By Default");

				} else if (keyString.equals("Report Pictures")) {
					buttonSort.setVisibility(View.GONE);
					// btn_sort.setText("By Taken/Uploaded");
					buttonUsers.setText("By Taken/Uploaded");
					buttonSub.setText("By Default");
				} else if (keyString.equals("Project Docs")) {
					buttonSort.setVisibility(View.GONE);
					// btn_sort.setText("By Taken/Uploaded");
					buttonUsers.setText("By Taken/Uploaded");
					buttonSub.setText("By Default");
				}
				buttonSort.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						byName(keyString);
						dialog.dismiss();
					}
				});

				buttonUsers.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (keyString.equals("Report Pictures")) {
							byName(keyString);
						} else if (keyString.equals("Project Docs")) {
							byName(keyString);
						} else {
							byDate(keyString);
						}
						dialog.dismiss();
					}
				});

				buttonSub.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (keyString.equals("Checklist Pictures")) {
							byPhase(keyString);
						} else if (keyString.equals("All")) {
							byAll(keyString);
						} else if (keyString.equals("Report Pictures")) {
							byDate(keyString);
						} else if (keyString.equals("Task Pictures")) {
							byAll(keyString);
						} else if (keyString.equals("Project Docs")) {
							byAll(keyString);
						}
						dialog.dismiss();
					}
				});

				buttonCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				relativelayoutListoutside.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});

		if (keyString.equals("Checklist Pictures")) {
			byPhase(keyString);
		} else if (keyString.equals("All")) {
			byAll(keyString);
		} else if (keyString.equals("Report Pictures")) {
			byDate(keyString);
		} else if (keyString.equals("Task Pictures")) {
			byAll(keyString);
		} else if (keyString.equals("Project Docs")) {
			byAll(keyString);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		arraylist.clear();
		Prefrences.well = 0;

		// work_user, check_user, report_user, doc_user, all_users,
		// doc_date, work_date, check_date, report_date, all_dates, doc_phase,
		// checklist_phase, worklist_phase, report_phase, all_phase;

		usersArrayList.clear();
		tempusersArrayList.clear();

		tempdatesArrayList.clear();
		datesArrayList.clear();

		tempphaseArrayList.clear();
		phaseArrayList.clear();
		arrArrayList.clear();
		idsArrayList.clear();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void Show_Dialog(View v, String key) {
		DialogBoxSort cdd = new DialogBoxSort(ImageActivity.this, key);
		Window window = cdd.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();

		wlp.gravity = Gravity.BOTTOM;
		wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		// wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(wlp);
		cdd.show();
	}

	void byAll(String str) {
		linearlayoutMain.removeAllViews();
		arrArrayList.clear();
		idsArrayList.clear();
		descArrayList.clear();
		contenttype.clear();
		arraylist.clear();
		if (str.equals("All")) {
			arraylist.addAll(DocumentFragment.photosListArrayList);
		} else if (str.equals("Report Pictures")) {
			arraylist.addAll(DocumentFragment.photosList5ArrayList);
		} else if (str.equals("Task Pictures")) {
			arraylist.addAll(DocumentFragment.photosList4ArrayList);
		} else if (str.equals("Project Docs")) {
			arraylist.addAll(DocumentFragment.photosList2ArrayList);
		} else if (str.equals("Checklist Pictures")) {
			arraylist.addAll(DocumentFragment.photosList3ArrayList);
		}

		if (arraylist.isEmpty()) {
			// Toast.makeText(getApplicationContext(), "null",
			// Toast.LENGTH_LONG).show();
		} else {
			int pos = 0;
			int rowValue = 0;
			linearlayout = new LinearLayout(this);
			linearlayout.setOrientation(LinearLayout.HORIZONTAL);
			linearlayoutMain.addView(linearlayout);
			do {
				arrArrayList.add(arraylist.get(pos).urlLarge);
				idsArrayList.add(arraylist.get(pos).id);
				descArrayList.add(arraylist.get(pos).original);
				contenttype.add(arraylist.get(pos).imageContentType);
				imageview = new ImageView(this);
				if (rowValue < 3) {
					rowValue++;
					// LayoutParams parms =

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);
					imageview.setTag(pos);
					imageview.setLayoutParams(lp);
					// Bitmap bitmap = BitmapFactory.decodeFile(Receiver1.list
					// .getAbsolutePath());

					Picasso.with(this)
							.load(arraylist.get(pos).url200.toString())
							.placeholder(R.drawable.default_200)
							.resize((int) (200 * ASSL.Xscale()),(int) (200 * ASSL.Yscale()))
							.into(imageview);
					linearlayout.addView(imageview);
				} else {
					rowValue = 0;
					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);
					imageview.setTag(pos);
					imageview.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(pos).url200.toString())
							.placeholder(R.drawable.default_200)
							.resize((int) (200 * ASSL.Xscale()),(int) (200 * ASSL.Yscale()))
							.into(imageview);
					linearlayout.addView(imageview);

					rowValue++;
				}
				imageview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					if (connDect.isConnectingToInternet()) {
								
							
							Log.i("Tag Value", "" + Prefrences.selectedPic);
							Prefrences.selectedPic = (Integer) v.getTag();
							Log.i("Tag Value", "" + Prefrences.selectedPic);
							
//							Intent intent = new Intent();
//				            intent.setAction(Intent.ACTION_VIEW);
//				            String str="https://dw9f6h00eoolt.cloudfront.net/photo_image_147_original.pdf?1387432463";
//				            Uri uri = Uri.parse(str);
//				            intent.setDataAndType(uri, "application/pdf");
//				            startActivity(intent);
	
//				            WebView mWebView=new WebView(ImageActivity.this);
//				            mWebView.getSettings().setJavaScriptEnabled(true);
//				            mWebView.getSettings().setPluginsEnabled(true);
//				            mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+"https://dw9f6h00eoolt.cloudfront.net/photo_image_147_original.pdf?1387432463");
//				            setContentView(mWebView);
				            
				            
							Intent intent = new Intent(ImageActivity.this,
									SelectedImageView.class);
							intent.putExtra("array", arrArrayList);
							intent.putExtra("ids", idsArrayList);
							intent.putExtra("desc", descArrayList);
							intent.putExtra("type", contenttype);
							intent.putExtra("key", keyString);
							Log.d("", "----0000000----" + keyString);
							intent.putExtra("id",arraylist.get(Prefrences.selectedPic).id);
							intent.putStringArrayListExtra("doc", docuserArrayList);
							intent.putStringArrayListExtra("work", workuserArrayList);
							intent.putStringArrayListExtra("check", checkuserArrayList);
							intent.putStringArrayListExtra("report", reportuserArrayList);
							intent.putStringArrayListExtra("allusers", allusersArrayList);
							intent.putStringArrayListExtra("doc_date", docdateArrayList);
							intent.putStringArrayListExtra("work_date", workdateArrayList);
							intent.putStringArrayListExtra("check_date", checkdateArrayList);
							intent.putStringArrayListExtra("report_date",reportdateArrayList);
							intent.putStringArrayListExtra("all_date", alldatesArrayList);
							intent.putStringArrayListExtra("doc_phase", docphaseArrayList);
							intent.putStringArrayListExtra("worklist_phase",worklistphaseArrayList);
							intent.putStringArrayListExtra("checklist_phase",checklistphaseArrayList);
							intent.putStringArrayListExtra("report_phase",reportphaseArrayList);
							intent.putStringArrayListExtra("all_phase", allphaseArrayList);
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
					} else {
						Toast.makeText(getApplicationContext(),"No internet connection.", Toast.LENGTH_SHORT).show();
								
					}
					}
				});

				pos++;
			} while (pos < arraylist.size());
		}
	}

	void byName(String s) {
		linearlayoutMain.removeAllViews();
		arraylist.clear();
		usersArrayList.clear();
		tempusersArrayList.clear();
		arrArrayList.clear();
		idsArrayList.clear();
		descArrayList.clear();
		contenttype.clear();
		keyString = s;
		// Log.d("work size", "" + work_user.size());
		// Log.d("check size", "" + check_user.size());
		// Log.d("report size", "" + report_user.size());
		// Log.d("doc size", "" + doc_user.size());
		// Log.d("all users size", "" + all_users.size());

		if (s.equals("All")) {
			arraylist.addAll(DocumentFragment.photosListArrayList);
			usersArrayList.addAll(allusersArrayList);
			tempusersArrayList.addAll(allusersArrayList);
		}
		if (s.equals("Project Docs")) {
			arraylist.addAll(DocumentFragment.photosList2ArrayList);
			usersArrayList.addAll(docuserArrayList);
			tempusersArrayList.addAll(docuserArrayList);
		} else if (s.equals("Checklist Pictures")) {
			arraylist.addAll(DocumentFragment.photosList3ArrayList);

			usersArrayList.addAll(checkuserArrayList);
			tempusersArrayList.addAll(checkuserArrayList);
		} else if (s.equals("Task Pictures")) {
			arraylist.addAll(DocumentFragment.photosList4ArrayList);
			usersArrayList.addAll(workuserArrayList);
			tempusersArrayList.addAll(workuserArrayList);
		} else if (s.equals("Report Pictures")) {
			arraylist.addAll(DocumentFragment.photosList5ArrayList);
			usersArrayList.addAll(reportuserArrayList);
			tempusersArrayList.addAll(reportuserArrayList);

		}

		Collections.sort(usersArrayList);
		for (int i = 0; i < usersArrayList.size(); i++) {
			// Log.d("users", users.get(i));
		}
		if (arraylist.isEmpty()) {
			// Toast.makeText(getApplicationContext(), "null",
			// Toast.LENGTH_LONG).show();
		} else {
			int pos = 0;
			int rowValue = 0;
			int index = 0;// from where image is taken actually
			do {
				flag = 0;
				if (pos == 0) {
					textview = new TextView(this);
					textview.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					textview.setText("" + usersArrayList.get(pos));
					textview.setTextSize(30);
					textview.setTextColor(Color.WHITE);
					textview.setPadding(10, 0, 0, 0);
					linearlayoutMain.addView(textview);

					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					flag = 1;
					rowValue = 0;
				} else if (pos != 0
						&& !usersArrayList.get(pos).equals(usersArrayList.get(pos - 1))) {

					textview = new TextView(this);
					textview.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					textview.setText("" + usersArrayList.get(pos));
					textview.setTextSize(30);
					textview.setTextColor(Color.WHITE);
					textview.setPadding(10, 0, 0, 0);
					linearlayoutMain.addView(textview);

					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					flag = 1;
					rowValue = 0;
				}
				if (flag == 1) // to get exact index, as if user has more than
								// one
								// images
				{
					count = 1;
				}
				if (flag == 0) {
					count++;
				}
				Log.d("count", "" + count);
				imageview = new ImageView(this);
				int indexplus = 0;
				for (int j = 0; j < tempusersArrayList.size(); j++) {
					// Log.d("tempusers.get("+j+"):",tempusers.get(j));
					// Log.d("users.get("+i+"):",users.get(i));
					if (tempusersArrayList.get(j).equals(usersArrayList.get(pos))) {
						indexplus++;
						if (count == indexplus) {
							index = j;
							// Log.d("index",""+index);
							break;
						}
					}
				}
				// Log.d("", "------1---"+users);
				// Log.d("", "------2----"+tempusers);
				// Log.d("", "------3------"+index);
				arrArrayList.add(arraylist.get(index).urlLarge);
				idsArrayList.add(arraylist.get(index).id);
				descArrayList.add(arraylist.get(index).original);
				contenttype.add(arraylist.get(pos).imageContentType);
				if (rowValue < 3) {
					rowValue++;
					imageview.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);

					imageview.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.placeholder(R.drawable.default_200)
							.resize((int) (200 * ASSL.Xscale()),(int) (200 * ASSL.Yscale()))
							.into(imageview);
					linearlayout.addView(imageview);
					// Log.d("actual uri", arraylist.get(index).urlLarge);
				} else {
					rowValue = 0;
					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					imageview.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);

					imageview.setLayoutParams(lp);

					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.placeholder(R.drawable.default_200)
							.resize((int) (200 * ASSL.Xscale()),(int) (200 * ASSL.Yscale()))
							.into(imageview);
					linearlayout.addView(imageview);
					// Log.d("actual uri", arraylist.get(index).urlLarge);
					rowValue++;
				}

				imageview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Log.i("Tag Value", "" + Prefrences.selectedPic);
						Prefrences.selectedPic = (Integer) v.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);

						Intent intent = new Intent(ImageActivity.this,
								SelectedImageView.class);
						intent.putExtra("array", arrArrayList);
						intent.putExtra("ids", idsArrayList);
						intent.putExtra("desc", descArrayList);
						intent.putExtra("type", contenttype);
						intent.putExtra("key", keyString);
						Log.d("", "----0000000----" + keyString);
						intent.putExtra("id",
								arraylist.get(Prefrences.selectedPic).id);

						intent.putStringArrayListExtra("doc", docuserArrayList);
						intent.putStringArrayListExtra("work", workuserArrayList);
						intent.putStringArrayListExtra("check", checkuserArrayList);
						intent.putStringArrayListExtra("report", reportuserArrayList);
						intent.putStringArrayListExtra("allusers", allusersArrayList);

						intent.putStringArrayListExtra("doc_date", docdateArrayList);
						intent.putStringArrayListExtra("work_date", workdateArrayList);
						intent.putStringArrayListExtra("check_date", checkdateArrayList);
						intent.putStringArrayListExtra("report_date",
								reportdateArrayList);
						intent.putStringArrayListExtra("all_date", alldatesArrayList);

						intent.putStringArrayListExtra("doc_phase", docphaseArrayList);
						intent.putStringArrayListExtra("worklist_phase",
								worklistphaseArrayList);
						intent.putStringArrayListExtra("checklist_phase",
								checklistphaseArrayList);
						intent.putStringArrayListExtra("report_phase",
								reportphaseArrayList);
						intent.putStringArrayListExtra("all_phase", allphaseArrayList);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				});
				pos++;
			} while (pos < usersArrayList.size());
		}
	}

	void byDate(String str) {
		linearlayoutMain.removeAllViews();
		arraylist.clear();
		datesArrayList.clear();
		tempdatesArrayList.clear();
		arrArrayList.clear();
		idsArrayList.clear();
		descArrayList.clear();
		contenttype.clear();
		// Log.d("work date size", "" + work_date.size());
		// Log.d("check date size", "" + check_date.size());
		// Log.d("report date size", "" + report_date.size());
		// Log.d("doc date size", "" + doc_date.size());
		Log.d("all users date size", "" + alldatesArrayList.size());
		if (str.equals("All")) {
			arraylist.addAll(DocumentFragment.photosListArrayList);
			datesArrayList.addAll(alldatesArrayList);
			tempdatesArrayList.addAll(alldatesArrayList);
		}
		if (str.equals("Project Docs")) {
			arraylist.addAll(DocumentFragment.photosList2ArrayList);
			datesArrayList.addAll(docdateArrayList);
			tempdatesArrayList.addAll(docdateArrayList);
		} else if (str.equals("Checklist Pictures")) {
			arraylist.addAll(DocumentFragment.photosList3ArrayList);
			datesArrayList.addAll(checkdateArrayList);
			tempdatesArrayList.addAll(checkdateArrayList);
		} else if (str.equals("Task Pictures")) {
			arraylist.addAll(DocumentFragment.photosList4ArrayList);
			datesArrayList.addAll(workdateArrayList);
			tempdatesArrayList.addAll(workdateArrayList);
		} else if (str.equals("Report Pictures")) {
			arraylist.addAll(DocumentFragment.photosList5ArrayList);
			datesArrayList.addAll(reportdateArrayList);
			tempdatesArrayList.addAll(reportdateArrayList);

		}

		for (int i = 0; i < tempdatesArrayList.size(); i++) {
			// Log.d("tempdates", tempdates.get(i));
			// Log.d("temp uri", arraylist.get(i).urlLarge);
		}

		Collections.sort(datesArrayList, Collections.reverseOrder());
		for (int i = 0; i < datesArrayList.size(); i++) {
			// Log.d("dates", dates.get(i));
		}
		if (arraylist.isEmpty()) {
			// Toast.makeText(getApplicationContext(), "null",
			// Toast.LENGTH_LONG).show();
		} else {
			int pos = 0;
			int rowValue = 0;
			int index = 0;// from where image is taken actually
			do {
				flag = 0;
				if (pos == 0) {
					textview = new TextView(this);
					textview.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					textview.setText("" + datesArrayList.get(pos));
					textview.setTextSize(30);
					textview.setTextColor(Color.WHITE);
					textview.setPadding(10, 0, 0, 0);
					linearlayoutMain.addView(textview);

					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					flag = 1;
					rowValue = 0;
				} else if (pos != 0
						&& !datesArrayList.get(pos).equals(datesArrayList.get(pos - 1))) {

					textview = new TextView(this);
					textview.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					textview.setText("" + datesArrayList.get(pos));
					textview.setTextSize(30);
					textview.setTextColor(Color.WHITE);
					textview.setPadding(10, 0, 0, 0);
					linearlayoutMain.addView(textview);

					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					flag = 1;
					rowValue = 0;
				}
				if (flag == 1) // to get exact index, as if user has more than
								// one
								// images
				{
					count = 1;
				}
				if (flag == 0) {
					count++;
				}
				Log.d("count", "" + count);
				imageview = new ImageView(this);
				int indexplus = 0;
				for (int j = 0; j < tempdatesArrayList.size(); j++) {
					// Log.d("tempdates.get("+j+"):",tempdates.get(j));
					// Log.d("dates.get("+i+"):",dates.get(i));
					if (tempdatesArrayList.get(j).equals(datesArrayList.get(pos))) {
						indexplus++;
						if (count == indexplus) {
							index = j;
							// Log.d("index",""+index);
							break;
						}
					}
				}
				Log.d("", "-----1----" + index);
				Log.d("", "-----2----" + datesArrayList.size());
				Log.d("", "-----3----" + tempdatesArrayList.size());
				arrArrayList.add(arraylist.get(index).urlLarge);
				idsArrayList.add(arraylist.get(index).id);
				descArrayList.add(arraylist.get(index).original);
				contenttype.add(arraylist.get(pos).imageContentType);

				if (rowValue < 3) {
					rowValue++;
					imageview.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);

					imageview.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.placeholder(R.drawable.default_200)
							.into(imageview);
					linearlayout.addView(imageview);
					// Log.d("actual uri", arraylist.get(index).urlLarge);
				} else {
					rowValue = 0;
					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					imageview.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);

					imageview.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.placeholder(R.drawable.default_200)
							.resize((int) (200 * ASSL.Xscale()),(int) (200 * ASSL.Yscale()))
							.into(imageview);
					linearlayout.addView(imageview);
					// Log.d("actual uri", arraylist.get(index).urlLarge);
					rowValue++;
				}
				imageview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Log.i("Tag Value", "" + Prefrences.selectedPic);
						Prefrences.selectedPic = (Integer) v.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);

						Intent intent = new Intent(ImageActivity.this,
								SelectedImageView.class);
						intent.putExtra("array", arrArrayList);
						intent.putExtra("ids", idsArrayList);
						intent.putExtra("desc", descArrayList);
						intent.putExtra("type", contenttype);
						intent.putExtra("key", keyString);
						Log.d("", "----0000000----" + keyString);
						intent.putExtra("id",
								arraylist.get(Prefrences.selectedPic).id);

						intent.putStringArrayListExtra("doc", docuserArrayList);
						intent.putStringArrayListExtra("work", workuserArrayList);
						intent.putStringArrayListExtra("check", checkuserArrayList);
						intent.putStringArrayListExtra("report", reportuserArrayList);
						intent.putStringArrayListExtra("allusers", allusersArrayList);

						intent.putStringArrayListExtra("doc_date", docdateArrayList);
						intent.putStringArrayListExtra("work_date", workdateArrayList);
						intent.putStringArrayListExtra("check_date", checkdateArrayList);
						intent.putStringArrayListExtra("report_date",
								reportdateArrayList);
						intent.putStringArrayListExtra("all_date", alldatesArrayList);

						intent.putStringArrayListExtra("doc_phase", docphaseArrayList);
						intent.putStringArrayListExtra("worklist_phase",
								worklistphaseArrayList);
						intent.putStringArrayListExtra("checklist_phase",
								checklistphaseArrayList);
						intent.putStringArrayListExtra("report_phase",
								reportphaseArrayList);
						intent.putStringArrayListExtra("all_phase", allphaseArrayList);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				});
				pos++;
			} while (pos < datesArrayList.size());
		}
	}

	void byPhase(String str) {
		linearlayoutMain.removeAllViews();
		arraylist.clear();
		phaseArrayList.clear();
		tempphaseArrayList.clear();
		arrArrayList.clear();
		idsArrayList.clear();
		descArrayList.clear();
		contenttype.clear();
		Log.d("work phase size", "" + worklistphaseArrayList.size());
		Log.d("check phase size", "" + checklistphaseArrayList.size());
		Log.d("report phase size", "" + reportphaseArrayList.size());
		Log.d("doc phase size", "" + docphaseArrayList.size());
		Log.d("all users phase size", "" + allphaseArrayList.size());
		if (str.equals("All")) {
			arraylist.addAll(DocumentFragment.photosListArrayList);
			phaseArrayList.addAll(allphaseArrayList);
			tempphaseArrayList.addAll(allphaseArrayList);
		}
		if (str.equals("Project Docs")) {
			arraylist.addAll(DocumentFragment.photosList2ArrayList);
			phaseArrayList.addAll(docphaseArrayList);
			tempphaseArrayList.addAll(docphaseArrayList);
		} else if (str.equals("Checklist Pictures")) {
			arraylist.addAll(DocumentFragment.photosList3ArrayList);
			phaseArrayList.addAll(checklistphaseArrayList);
			tempphaseArrayList.addAll(checklistphaseArrayList);
		} else if (str.equals("Task Pictures")) {
			arraylist.addAll(DocumentFragment.photosList4ArrayList);
			phaseArrayList.addAll(worklistphaseArrayList);
			tempphaseArrayList.addAll(worklistphaseArrayList);
		} else if (str.equals("Report Pictures")) {
			arraylist.addAll(DocumentFragment.photosList5ArrayList);
			phaseArrayList.addAll(reportphaseArrayList);
			tempphaseArrayList.addAll(reportphaseArrayList);

		}

		for (int i = 0; i < tempphaseArrayList.size(); i++) {
			// Log.d("tempphase", tempphase.get(i));
			// Log.d("temp uri", arraylist.get(i).urlLarge);
		}

		Collections.sort(phaseArrayList);
		for (int i = 0; i < phaseArrayList.size(); i++) {
			Log.d("phase", phaseArrayList.get(i));
		}
		if (arraylist.isEmpty()) {
			Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG)
					.show();
		} else {
			int pos = 0;
			int rowValue = 0;
			int index = 0;// from where image is taken actually
			do {
				flag = 0;
				if (pos == 0) {
					textview = new TextView(this);
					textview.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					textview.setText("" + phaseArrayList.get(pos));
					textview.setTextSize(30);
					textview.setPadding(10, 0, 0, 0);
					textview.setTextColor(Color.WHITE);
					linearlayoutMain.addView(textview);

					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					flag = 1;
					rowValue = 0;
				} else if (pos != 0
						&& !phaseArrayList.get(pos).equals(phaseArrayList.get(pos - 1))) {

					textview = new TextView(this);
					textview.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					textview.setText("" + phaseArrayList.get(pos));
					textview.setTextSize(30);
					textview.setPadding(10, 0, 0, 0);
					textview.setTextColor(Color.WHITE);
					linearlayoutMain.addView(textview);

					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					flag = 1;
					rowValue = 0;
				}
				if (flag == 1) // to get exact index, as if user has more than
								// one
								// images
				{
					count = 1;
				}
				if (flag == 0) {
					count++;
				}
				Log.d("count", "" + count);
				imageview = new ImageView(this);
				int indexplus = 0;
				for (int j = 0; j < tempphaseArrayList.size(); j++) {
					Log.d("tempphase.get(" + j + "):", tempphaseArrayList.get(j));
					Log.d("phase.get(" + pos + "):", phaseArrayList.get(pos));
					if (tempphaseArrayList.get(j).equals(phaseArrayList.get(pos))) {
						indexplus++;
						if (count == indexplus) {
							index = j;
							Log.d("index", "" + index);
							break;
						}
					}
				}
				Log.d("", "-----1----" + index);
				Log.d("", "-----2----" + phaseArrayList.size());
				Log.d("", "-----3----" + tempphaseArrayList.size());
				arrArrayList.add(arraylist.get(index).urlLarge);
				idsArrayList.add(arraylist.get(index).id);
				descArrayList.add(arraylist.get(index).original);
				contenttype.add(arraylist.get(pos).imageContentType);
				if (rowValue < 3) {
					rowValue++;
					imageview.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 0);

					imageview.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.placeholder(R.drawable.default_200)
							.resize((int) (200 * ASSL.Xscale()),(int) (200 * ASSL.Yscale()))
							.into(imageview);
					linearlayout.addView(imageview);
					Log.d("actual uri", arraylist.get(index).urlLarge);
				} else {
					rowValue = 0;
					linearlayout = new LinearLayout(this);
					linearlayout.setOrientation(LinearLayout.HORIZONTAL);
					linearlayoutMain.addView(linearlayout);
					imageview.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 0);

					imageview.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.placeholder(R.drawable.default_200)
							.resize((int) (200 * ASSL.Xscale()),(int) (200 * ASSL.Yscale()))
							.into(imageview);
					linearlayout.addView(imageview);
					Log.d("actual uri", arraylist.get(index).urlLarge);
					rowValue++;
				}
				imageview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (connDect.isConnectingToInternet()) {
							
						
						Log.i("Tag Value", "" + Prefrences.selectedPic);
						Prefrences.selectedPic = (Integer) v.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);

						Intent intent = new Intent(ImageActivity.this,
								SelectedImageView.class);
						intent.putExtra("array", arrArrayList);
						intent.putExtra("ids", idsArrayList);
						intent.putExtra("desc", descArrayList);
						intent.putExtra("type", contenttype);
						intent.putExtra("key", keyString);
						Log.d("", "----0000000----" + keyString);
						intent.putExtra("id",
								arraylist.get(Prefrences.selectedPic).id);

						intent.putStringArrayListExtra("doc", docuserArrayList);
						intent.putStringArrayListExtra("work", workuserArrayList);
						intent.putStringArrayListExtra("check", checkuserArrayList);
						intent.putStringArrayListExtra("report", reportuserArrayList);
						intent.putStringArrayListExtra("allusers", allusersArrayList);

						intent.putStringArrayListExtra("doc_date", docdateArrayList);
						intent.putStringArrayListExtra("work_date", workdateArrayList);
						intent.putStringArrayListExtra("check_date", checkdateArrayList);
						intent.putStringArrayListExtra("report_date",
								reportdateArrayList);
						intent.putStringArrayListExtra("all_date", alldatesArrayList);

						intent.putStringArrayListExtra("doc_phase", docphaseArrayList);
						intent.putStringArrayListExtra("worklist_phase",
								worklistphaseArrayList);
						intent.putStringArrayListExtra("checklist_phase",
								checklistphaseArrayList);
						intent.putStringArrayListExtra("report_phase",
								reportphaseArrayList);
						intent.putStringArrayListExtra("all_phase", allphaseArrayList);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						} else {
								Toast.makeText(getApplicationContext(),"No internet connection.", Toast.LENGTH_SHORT).show();
							}
					}
				});
				pos++;
			} while (pos < phaseArrayList.size());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

}