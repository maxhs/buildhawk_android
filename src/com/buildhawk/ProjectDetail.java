package com.buildhawk;

/*
 *  This file is used to show the details of each project which has been selected.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.buildhawk.utils.Report;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class ProjectDetail extends Activity {
	TextView textviewProName, textviewChecklisttxt, textviewDocumenttxt, textviewReporttxt,
			textviewWorklisttxt;
	RelativeLayout relativelayoutChecklist, relativelayoutDocument, relativelayoutReport, relativelayoutWorklist,
			relativelayoutBack;
	ImageView imageviewChecklistimg, imageviewDocumentimg, imageviewReportimg, imageviewWorklistimg;
	static ImageView imageviewSearchimg;
	static Activity activity;
	public static Fragment fragment;
	RelativeLayout relativelayoutRoot;
	// TextView back_txt;
	public static ArrayList<Report> dateReports;
	ImageView imageviewBackimg;
	ImageView imageviewAddWorkitem;
	Button buttonSelectdate;
	public static int flag = 0; // 1 : Create Worklist 0 : Existing Worklist
	Dialog popup;
	Button buttonEmail, buttonText, buttonCall, buttonCancel;
	TextView tv_expiryAlert;
	RelativeLayout relativelayoutListoutside;
	int year, day, month;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		relativelayoutRoot = (RelativeLayout) findViewById(R.id.relativelayoutRootDetails);
		new ASSL(this, relativelayoutRoot, 1134, 720, false);
		Prefrences.pageFlag = 1;
		relativelayoutBack = (RelativeLayout) findViewById(R.id.relativelayoutBack);
		textviewProName = (TextView) findViewById(R.id.textviewProName);
		relativelayoutChecklist = (RelativeLayout) findViewById(R.id.relativelayoutChecklist);
		relativelayoutDocument = (RelativeLayout) findViewById(R.id.relativelayoutDocument);
		relativelayoutReport = (RelativeLayout) findViewById(R.id.relativelayoutReport);
		relativelayoutWorklist = (RelativeLayout) findViewById(R.id.relativelayoutWorklist);
		activity = ProjectDetail.this;
		textviewChecklisttxt = (TextView) findViewById(R.id.textviewChecklisttxt);
		textviewDocumenttxt = (TextView) findViewById(R.id.textviewDocumenttxt);
		textviewReporttxt = (TextView) findViewById(R.id.textviewReporttxt);
		textviewWorklisttxt = (TextView) findViewById(R.id.textviewWorklisttxt);
		imageviewBackimg = (ImageView) findViewById(R.id.imageviewBackimg);
		imageviewSearchimg = (ImageView) findViewById(R.id.imageviewSearchimg);
		// back_txt=(TextView)findViewById(R.id.project_txt);

		imageviewChecklistimg = (ImageView) findViewById(R.id.imageviewChecklistimg);
		imageviewDocumentimg = (ImageView) findViewById(R.id.imageviewDocumentimg);
		imageviewReportimg = (ImageView) findViewById(R.id.imageviewReportimg);
		imageviewWorklistimg = (ImageView) findViewById(R.id.imageviewWorklistimg);

		imageviewAddWorkitem = (ImageView) findViewById(R.id.imageviewAddWorkitem);
		buttonSelectdate = (Button) findViewById(R.id.buttonSelectdate);
		buttonSelectdate.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		if (Prefrences.comingFrom == 1) {
			Prefrences.comingFrom = 5;
			imageviewAddWorkitem.setVisibility(View.GONE);
			imageviewSearchimg.setVisibility(View.GONE);
			buttonSelectdate.setVisibility(View.GONE);
			relativelayoutChecklist.setBackgroundColor(Color.BLACK);
			relativelayoutDocument.setBackgroundColor(Color.WHITE);
			relativelayoutReport.setBackgroundColor(Color.BLACK);
			relativelayoutWorklist.setBackgroundColor(Color.BLACK);

			textviewChecklisttxt.setTextColor(Color.WHITE);
			textviewDocumenttxt.setTextColor(Color.BLACK);
			textviewReporttxt.setTextColor(Color.WHITE);
			textviewWorklisttxt.setTextColor(Color.WHITE);

			imageviewChecklistimg.setImageResource(R.drawable.btmcheck_on);
			imageviewDocumentimg.setImageResource(R.drawable.btmdocs_off);
			imageviewReportimg.setImageResource(R.drawable.btmreports_on);
			imageviewWorklistimg.setImageResource(R.drawable.btmworklist_on);

			relativelayoutDocument.setEnabled(false);
			relativelayoutReport.setEnabled(true);
			relativelayoutWorklist.setEnabled(true);
			relativelayoutChecklist.setEnabled(true);

			fragment = new DocumentFragment();
		} else if (Prefrences.comingFrom == 2) {
			Prefrences.comingFrom = 5;
			relativelayoutChecklist.setBackgroundColor(Color.BLACK);
			relativelayoutDocument.setBackgroundColor(Color.BLACK);
			relativelayoutReport.setBackgroundColor(Color.WHITE);
			relativelayoutWorklist.setBackgroundColor(Color.BLACK);

			textviewChecklisttxt.setTextColor(Color.WHITE);
			textviewDocumenttxt.setTextColor(Color.WHITE);
			textviewReporttxt.setTextColor(Color.BLACK);
			textviewWorklisttxt.setTextColor(Color.WHITE);

			imageviewChecklistimg.setImageResource(R.drawable.btmcheck_on);
			imageviewDocumentimg.setImageResource(R.drawable.btmdocs_on);
			imageviewReportimg.setImageResource(R.drawable.btmreports_off);
			imageviewWorklistimg.setImageResource(R.drawable.btmworklist_on);

			relativelayoutDocument.setEnabled(true);
			relativelayoutReport.setEnabled(false);
			relativelayoutWorklist.setEnabled(true);
			relativelayoutChecklist.setEnabled(true);

			imageviewAddWorkitem.setVisibility(View.VISIBLE);
			buttonSelectdate.setVisibility(View.VISIBLE);
			imageviewSearchimg.setVisibility(View.GONE);
			fragment = new ReportFragment();
		} else if (Prefrences.comingFrom == 3) {
			Prefrences.comingFrom = 5;
			relativelayoutChecklist.setBackgroundColor(Color.BLACK);
			relativelayoutDocument.setBackgroundColor(Color.BLACK);
			relativelayoutReport.setBackgroundColor(Color.BLACK);
			relativelayoutWorklist.setBackgroundColor(Color.WHITE);

			textviewChecklisttxt.setTextColor(Color.WHITE);
			textviewDocumenttxt.setTextColor(Color.WHITE);
			textviewReporttxt.setTextColor(Color.WHITE);
			textviewWorklisttxt.setTextColor(Color.BLACK);

			imageviewChecklistimg.setImageResource(R.drawable.btmcheck_on);
			imageviewDocumentimg.setImageResource(R.drawable.btmdocs_on);
			imageviewReportimg.setImageResource(R.drawable.btmreports_on);
			imageviewWorklistimg.setImageResource(R.drawable.btmworklist_off);

			relativelayoutDocument.setEnabled(true);
			relativelayoutReport.setEnabled(true);
			relativelayoutWorklist.setEnabled(false);
			relativelayoutChecklist.setEnabled(true);

			imageviewSearchimg.setVisibility(View.GONE);
			imageviewAddWorkitem.setVisibility(View.VISIBLE);
			buttonSelectdate.setVisibility(View.GONE);
			fragment = new WorklistFragment();
		} else if (Prefrences.comingFrom == 4) {
			Prefrences.comingFrom = 5;
			fragment = new ChecklistFragment();
		} else
			fragment = new ChecklistFragment();

		getFragmentManager().beginTransaction()
				.replace(R.id.fragment, fragment).commit();

		textviewProName.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));
		textviewChecklisttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		textviewDocumenttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		textviewReporttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		textviewWorklisttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		textviewProName.setText(Prefrences.selectedProName);

		buttonSelectdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});

		relativelayoutBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// back_img.setBackgroundResource(drawable.back_btn_onclick);
				// back_txt.setTextColor(Color.BLACK);
				
				if (Prefrences.comingFrom == 5) {
					Prefrences.comingFrom = 0;
					sessionData();
				} else {
					Prefrences.LastDocument_s=Prefrences.document_s;
					Prefrences.LastReport_s = Prefrences.report_s;
					Prefrences.LastWorklist_s = Prefrences.worklist_s;
					Prefrences.LastChecklist_s = Prefrences.checklist_s;
					Prefrences.LastSelectedProId=Prefrences.selectedProId;
					Prefrences.document_s="";
					Prefrences.report_s="";
					Prefrences.worklist_s="";
					Prefrences.checklist_s="";
					finish();
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
				}
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
			}
		});

		// searchimg.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Log.d("Search Image","Clicked");
		// ChecklistFragment.search_rel.setVisibility(View.VISIBLE);
		// ChecklistFragment.txt_searchcheck.setFocusable(true);
		// //
		// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
		// // .toggleSoftInput(InputMethodManager.SHOW_FORCED,
		// // InputMethodManager.HIDE_IMPLICIT_ONLY);
		// }
		// });

		relativelayoutChecklist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Prefrences.stopingHit = 1;
				imageviewAddWorkitem.setVisibility(View.GONE);
				buttonSelectdate.setVisibility(View.GONE);
				imageviewSearchimg.setVisibility(View.VISIBLE);

				relativelayoutChecklist.setBackgroundColor(Color.WHITE);
				relativelayoutDocument.setBackgroundColor(Color.BLACK);
				relativelayoutReport.setBackgroundColor(Color.BLACK);
				relativelayoutWorklist.setBackgroundColor(Color.BLACK);

				textviewChecklisttxt.setTextColor(Color.BLACK);
				textviewDocumenttxt.setTextColor(Color.WHITE);
				textviewReporttxt.setTextColor(Color.WHITE);
				textviewWorklisttxt.setTextColor(Color.WHITE);

				imageviewChecklistimg.setImageResource(R.drawable.btmcheck_off);
				imageviewDocumentimg.setImageResource(R.drawable.btmdocs_on);
				imageviewReportimg.setImageResource(R.drawable.btmreports_on);
				imageviewWorklistimg.setImageResource(R.drawable.btmworklist_on);

				relativelayoutDocument.setEnabled(true);
				relativelayoutReport.setEnabled(true);
				relativelayoutWorklist.setEnabled(true);
				relativelayoutChecklist.setEnabled(false);
				if(ConnectionDetector.isConnectingToInternet()){
				fragment = new ChecklistFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
			}
			else{
				Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
			}
			}
		});

		relativelayoutDocument.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Prefrences.stopingHit = 1;
				imageviewAddWorkitem.setVisibility(View.GONE);
				imageviewSearchimg.setVisibility(View.GONE);
				buttonSelectdate.setVisibility(View.GONE);
				relativelayoutChecklist.setBackgroundColor(Color.BLACK);
				relativelayoutDocument.setBackgroundColor(Color.WHITE);
				relativelayoutReport.setBackgroundColor(Color.BLACK);
				relativelayoutWorklist.setBackgroundColor(Color.BLACK);

				textviewChecklisttxt.setTextColor(Color.WHITE);
				textviewDocumenttxt.setTextColor(Color.BLACK);
				textviewReporttxt.setTextColor(Color.WHITE);
				textviewWorklisttxt.setTextColor(Color.WHITE);

				imageviewChecklistimg.setImageResource(R.drawable.btmcheck_on);
				imageviewDocumentimg.setImageResource(R.drawable.btmdocs_off);
				imageviewReportimg.setImageResource(R.drawable.btmreports_on);
				imageviewWorklistimg.setImageResource(R.drawable.btmworklist_on);

				relativelayoutDocument.setEnabled(false);
				relativelayoutReport.setEnabled(true);
				relativelayoutWorklist.setEnabled(true);
				relativelayoutChecklist.setEnabled(true);
				
				if(ConnectionDetector.isConnectingToInternet()){
				fragment = new DocumentFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
				}
				else{
					Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
				}
			}
		});

		relativelayoutReport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// addWorkitem.setVisibility(View.GONE);
				Prefrences.stopingHit = 1;

				relativelayoutChecklist.setBackgroundColor(Color.BLACK);
				relativelayoutDocument.setBackgroundColor(Color.BLACK);
				relativelayoutReport.setBackgroundColor(Color.WHITE);
				relativelayoutWorklist.setBackgroundColor(Color.BLACK);

				textviewChecklisttxt.setTextColor(Color.WHITE);
				textviewDocumenttxt.setTextColor(Color.WHITE);
				textviewReporttxt.setTextColor(Color.BLACK);
				textviewWorklisttxt.setTextColor(Color.WHITE);

				imageviewChecklistimg.setImageResource(R.drawable.btmcheck_on);
				imageviewDocumentimg.setImageResource(R.drawable.btmdocs_on);
				imageviewReportimg.setImageResource(R.drawable.btmreports_off);
				imageviewWorklistimg.setImageResource(R.drawable.btmworklist_on);

				relativelayoutDocument.setEnabled(true);
				relativelayoutReport.setEnabled(false);
				relativelayoutWorklist.setEnabled(true);
				relativelayoutChecklist.setEnabled(true);

				imageviewAddWorkitem.setVisibility(View.VISIBLE);
				buttonSelectdate.setVisibility(View.VISIBLE);
				imageviewSearchimg.setVisibility(View.GONE);
				imageviewAddWorkitem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Flag=2;

						popup = new Dialog(ProjectDetail.this,
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
						
						tv_expiryAlert = (TextView) popup
								.findViewById(R.id.textviewExpiryAlert);
						
//						 = (Button) popup.findViewById(R.id.buttonEmail);
//						buttonCall = (Button) popup.findViewById(R.id.buttonCall);
//						buttonText = (Button) popup.findViewById(R.id.buttonText);
//						buttonCancel = (Button) popup.findViewById(R.id.buttonCancel);
//						textviewExpiryAlert = (TextView) popup.findViewById(R.id.textviewExpiryAlert);

						buttonEmail.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						buttonText.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						buttonCall.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						buttonCancel.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));

						tv_expiryAlert.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());

						relativelayoutListoutside = (RelativeLayout) popup
								.findViewById(R.id.relativelayoutExpiryMain);
						new ASSL(ProjectDetail.this, relativelayoutExpiryMain, 1134, 720,
								false);

						buttonEmail.setText("Daily");
						buttonCall.setText("Safety");
						buttonText.setText("Weekly");
						tv_expiryAlert.setText("Reports");

						relativelayoutListoutside
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										if (popup.isShowing()) {
											popup.dismiss();
											// overridePendingTransition(R.anim.slide_in_bottom,
											// R.anim.slide_out_to_top);
										}
									}
								});

						buttonEmail.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								DateFormat dateFormat = new SimpleDateFormat(
										"yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();
								Log.e("Today date",
										", "
												+ dateFormat.format(date)
														.toString());

								Log.e("Saved date",
										", "
												+ Prefrences
														.getDateDaily(getApplicationContext()));
								Log.e("Saved Type",
										", "
												+ Prefrences
														.getTypeDaily(getApplicationContext()));

//								if (dateFormat
//										.format(date)
//										.toString()
//										.equalsIgnoreCase(
//												Prefrences
//														.getDateDaily(getApplicationContext()))
//										&& Prefrences
//												.getProjectId(
//														getApplicationContext())
//												.equalsIgnoreCase(
//														Prefrences.selectedProId
//																.toString())) {
//
//									Toast.makeText(
//											activity,
//											"Sorry, you can create only one Daily report in a day",
//											50000).show();
//									
//								} else {
									

									// Prefrences.saveDateDaily(dateFormat.format(date).toString(),
									// getApplicationContext(),email.getText().toString());
									if(ConnectionDetector.isConnectingToInternet()){
										Prefrences.reportTypeDialog = 1;
									Intent intent = new Intent(activity,
											ReportItemCreate.class);
									startActivity(intent);
									overridePendingTransition(
											R.anim.slide_in_right,
											R.anim.slide_out_left);
								}
									else{
									Toast.makeText(activity,"No internet connection.", Toast.LENGTH_SHORT).show();
									}
									popup.dismiss();
								}

//							}
						});

						buttonCall.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								DateFormat dateFormat = new SimpleDateFormat(
										"yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();
								Log.e("Today date",
										", "
												+ dateFormat.format(date)
														.toString());

								Log.e("Saved date",
										", "
												+ Prefrences
														.getDateSafety(getApplicationContext()));
								Log.e("Saved Type",
										", "
												+ Prefrences
														.getTypeSafety(getApplicationContext()));

//								if (dateFormat
//										.format(date)
//										.toString()
//										.equalsIgnoreCase(
//												Prefrences
//														.getDateSafety(getApplicationContext()))
//								
//									&& 	Prefrences
//												.getProjectId(
//														getApplicationContext())
//												.equalsIgnoreCase(
//														Prefrences.selectedProId
//																.toString()))
//										 {
//									Toast.makeText(
//											activity,
//											"Sorry, you can create only one Safety report in a day",
//											50000).show();
//									
//										 } else {
											 if(ConnectionDetector.isConnectingToInternet()){
													Prefrences.reportTypeDialog = 2;
													Intent intent = new Intent(activity,
															ReportItemCreate.class);
													startActivity(intent);
													overridePendingTransition(
															R.anim.slide_in_right,
															R.anim.slide_out_left);
												}
												else{
												Toast.makeText(activity,"No internet connection.", Toast.LENGTH_SHORT).show();
												}
													// Prefrences.saveDateSafety(dateFormat.format(date).toString(),
													// getApplicationContext(),call.getText().toString());
													popup.dismiss();
								}
//							}
						});

						buttonText.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								DateFormat dateFormat = new SimpleDateFormat(
										"yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();
								Log.e("Today date",
										", "
												+ dateFormat.format(date)
														.toString());

								Log.e("Saved date",
										", "
												+ Prefrences
														.getDateWeekly(getApplicationContext()));
								Log.e("Saved Type",
										", "
												+ Prefrences
														.getTypeWeekly(getApplicationContext()));

//								if (dateFormat
//										.format(date)
//										.toString()
//										.equalsIgnoreCase(
//												Prefrences
//														.getDateWeekly(getApplicationContext()))
//										&& Prefrences
//												.getProjectId(
//														getApplicationContext())
//												.equalsIgnoreCase(
//														Prefrences.selectedProId
//																.toString())) {
//									
//									Toast.makeText(
//											activity,
//											"Sorry, you can create only one Weekly report in a day",
//											50000).show();
//								
//								} else {
									if(ConnectionDetector.isConnectingToInternet()){	
										Prefrences.reportTypeDialog = 3;
										Intent in = new Intent(activity,
												ReportItemCreate.class);
										startActivity(in);
										overridePendingTransition(
												R.anim.slide_in_right,
												R.anim.slide_out_left);
									}
									else{
									Toast.makeText(activity,"No internet connection.", Toast.LENGTH_SHORT).show();
									}
										// Prefrences.saveDateWeekly(dateFormat.format(date).toString(),
										// getApplicationContext(),text.getText().toString());
										popup.dismiss();
								}
//							}
						});

						buttonCancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								popup.dismiss();
							}
						});
						popup.show();

					}
				});
				if (ConnectionDetector.isConnectingToInternet()) {

					fragment = new ReportFragment();
					getFragmentManager().beginTransaction()
							.replace(R.id.fragment, fragment).commit();
				} else {
					Toast.makeText(activity, "No internet connection",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		relativelayoutWorklist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				relativelayoutChecklist.setBackgroundColor(Color.BLACK);
				relativelayoutDocument.setBackgroundColor(Color.BLACK);
				relativelayoutReport.setBackgroundColor(Color.BLACK);
				relativelayoutWorklist.setBackgroundColor(Color.WHITE);

				textviewChecklisttxt.setTextColor(Color.WHITE);
				textviewDocumenttxt.setTextColor(Color.WHITE);
				textviewReporttxt.setTextColor(Color.WHITE);
				textviewWorklisttxt.setTextColor(Color.BLACK);

				imageviewChecklistimg.setImageResource(R.drawable.btmcheck_on);
				imageviewDocumentimg.setImageResource(R.drawable.btmdocs_on);
				imageviewReportimg.setImageResource(R.drawable.btmreports_on);
				imageviewWorklistimg.setImageResource(R.drawable.btmworklist_off);

				relativelayoutDocument.setEnabled(true);
				relativelayoutReport.setEnabled(true);
				relativelayoutWorklist.setEnabled(false);
				relativelayoutChecklist.setEnabled(true);

				imageviewSearchimg.setVisibility(View.GONE);
				imageviewAddWorkitem.setVisibility(View.VISIBLE);
				buttonSelectdate.setVisibility(View.GONE);

				imageviewAddWorkitem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						flag = 1;
						Prefrences.text = 10;
						Intent intent = new Intent(activity,
								WorkItemClick.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);

					}
				});
				if(ConnectionDetector.isConnectingToInternet()){
				fragment = new WorklistFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
			}
			else{
				Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
			}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("Destroying", "Destroying");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		Log.d("Stoping", "Stoping");
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Activity.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if (Prefrences.comingFrom == 5) {
			Prefrences.comingFrom = 0;
			sessionData();
		} else {
			Prefrences.LastDocument_s=Prefrences.document_s;
			Prefrences.LastReport_s = Prefrences.report_s;
			Prefrences.LastWorklist_s = Prefrences.worklist_s;
			Prefrences.LastChecklist_s = Prefrences.checklist_s;
			Prefrences.LastSelectedProId=Prefrences.selectedProId;
			Prefrences.document_s="";
			Prefrences.report_s="";
			Prefrences.worklist_s="";
			Prefrences.checklist_s="";
			finish();
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
		try {

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.hideSoftInputFromWindow(getCurrentFocus()

			.getWindowToken(), 0);

		} catch (Exception exception) {

			exception.printStackTrace();

		}
		// super.onBackPressed();
	}

	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		private String new_month, new_day;

		@Override
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			year = years;
			month = monthOfYear + 1;

			day = dayOfMonth;
			if (month < 10) {
				new_month = "0" + month;
			} else {
				new_month = "" + month;
			}
			if (day < 10) {
				new_day = "0" + day;
			} else {
				new_day = "" + day;
			}
			String final_date = new_month + "/" + new_day + "/" + year;
			Log.i("date", "" + final_date);
			Prefrences.selecteddate = final_date;

			// date.setText(final_date);
		}
	};

	// protected Dialog onCreateDialog() {
	//
	// Log.v("year month day",""+year+" "+month+" "+day);
	// return new DatePickerDialog(this, dateListener, year, month, day);
	//
	// }
	protected Dialog onCreateDialog(int dialogId) {

		Calendar cal = Calendar.getInstance();

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);
		final DatePickerDialog datepiker = new DatePickerDialog(this,
				dateListener, year, month, day);

		Log.e("dateDialog new", "==");

		datepiker.setTitle("Select Date:");

		datepiker.setCanceledOnTouchOutside(false);

		datepiker.setCancelable(false);
		// datepiker.getDatePicker().setMinDate(cal.getTimeInMillis());
		datepiker.getDatePicker().setCalendarViewShown(false);

		datepiker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",

		new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (which == DialogInterface.BUTTON_NEGATIVE) {

					// dateSet = false;
					dialog.dismiss();

					dialog.cancel();

				}

			}

		});

		datepiker.setButton(DialogInterface.BUTTON_POSITIVE, "OK",

		new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int flag = 0;
				if (which == DialogInterface.BUTTON_POSITIVE) {
					DatePicker datePicker = datepiker.getDatePicker();
					dateListener.onDateSet(datePicker, datePicker.getYear(),
							datePicker.getMonth(), datePicker.getDayOfMonth());
					dateReports = new ArrayList<Report>();
					for (int i = 0; i < ReportFragment.reportdataArrayList.size(); i++) {
						Log.d("hellllooooo",
								"helllloo"
										+ Prefrences.selecteddate.toString()
										+ "yoyoyoyo"
										+ ReportFragment.reportdataArrayList.get(i).created_date
												.toString());

						if (ReportFragment.reportdataArrayList.get(i).created_date
								.toString().equalsIgnoreCase(
										Prefrences.selecteddate.toString())) {

							flag = 1;
							// Toast.makeText(getApplicationContext(),
							// "Hellloooo"+Prefrences.selecteddate.toString(),
							// Toast.LENGTH_LONG).show();
							Log.d("hellllooooo", "helllloo"
									+ Prefrences.selecteddate.toString());
							dateReports.add(ReportFragment.reportdataArrayList.get(i));
						}
					}
					// dateSet = true;
					if (flag == 1) {
						Prefrences.reportType = 4;
						Intent intent = new Intent(activity,
								ReportItemClick.class);

						intent.putExtra("pos", 0);
						activity.startActivity(intent);
					} else {
						if(ConnectionDetector.isConnectingToInternet()){
						Intent intent = new Intent(ProjectDetail.this,
								ReportItemCreate.class);
						startActivity(intent);
						}
						else{
							Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
						}
					}
					dialog.dismiss();

					dialog.cancel();

				}

			}

		});

		return datepiker;

	}

	// ************ API for Login. *************//

	public void sessionData() {

		Prefrences.showLoadingDialog(ProjectDetail.this, "Loading...");
		final SharedPreferences sharedpref;
		sharedpref = getApplicationContext().getSharedPreferences("MyPref", 0);
		String emailString, passwordString, regIdString;
		emailString = Prefrences.getCredential(getApplicationContext())[0];
		passwordString = Prefrences.getCredential(getApplicationContext())[1];
		regIdString = Prefrences.getCredential(getApplicationContext())[2];

		RequestParams params = new RequestParams();

		params.put("email", emailString.toString());
		params.put("password", passwordString.toString());
		params.put("device_token", regIdString.toString());
		Log.d("reg id ", "" + regIdString.toString());
		params.put("device_type", "3");

		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		client.setTimeout(1000000);
		client.post(Prefrences.url + "/sessions", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);

						JSONObject res;
						try {
							Editor editor = sharedpref.edit();
							editor.putString("userData", response);
							editor.putString("lat", ""
									+ Prefrences.currentLatitude);
							editor.putString("longi", ""
									+ Prefrences.currentLongitude);
							res = new JSONObject(response);
							JSONObject user = res.getJSONObject("user");
							Prefrences.userId = user.getString("id");
							Prefrences.firstName = user.getString("first_name");
							Prefrences.lastName = user.getString("last_name");
							Prefrences.fullName = user.getString("full_name");
							Prefrences.email = user.getString("email");
							Prefrences.phoneNumber = user
									.getString("formatted_phone");
							Prefrences.authToken = user
									.getString("authentication_token");

							editor.putString("authentication_token",
									Prefrences.authToken);
							editor.commit();
							Log.i("Response", "," + Prefrences.userId + ", "
									+ Prefrences.firstName + ", "
									+ Prefrences.lastName + ", "
									+ Prefrences.fullName + ", "
									+ Prefrences.email + ", "
									+ Prefrences.phoneNumber + ", "
									+ Prefrences.authToken);

							JSONArray coworkers = user
									.getJSONArray("coworkers");
							Log.v("coworkers", "," + coworkers.length());

							Prefrences.coworkrName = new String[coworkers
									.length()];
							Prefrences.coworkrEmail = new String[coworkers
									.length()];
							Prefrences.coworkrForPhone = new String[coworkers
									.length()];
							Prefrences.coworkrPhone = new String[coworkers
									.length()];
							Prefrences.coworkrId = new String[coworkers
									.length()];
							Prefrences.coworkrUrl = new String[coworkers
									.length()];

							for (int i = 0; i < coworkers.length(); i++) {
								JSONObject count = coworkers.getJSONObject(i);
								Prefrences.coworkrName[i] = count
										.getString("full_name");
								Log.d("jijijijii", "coworker"
										+ Prefrences.coworkrName[i].toString());
								Prefrences.coworkrEmail[i] = count
										.getString("email");
								Prefrences.coworkrForPhone[i] = count
										.getString("formatted_phone");
								Prefrences.coworkrPhone[i] = count
										.getString("phone");
								Prefrences.coworkrId[i] = count.getString("id");
								Prefrences.coworkrUrl[i] = count
										.getString("url_thumb");
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

						// Prefrences.saveAccessToken(Prefrences.userId,
						// txt_email.getText().toString(),
						// txt_pass.getText().toString(),
						// regId,getApplicationContext());

						startActivity(new Intent(ProjectDetail.this,
								Homepage.class));

						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);

						finish();
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Toast.makeText(getApplicationContext(), "Server Issue",
								Toast.LENGTH_LONG).show();
						Prefrences.dismissLoadingDialog();
					}
				});

	}

}
