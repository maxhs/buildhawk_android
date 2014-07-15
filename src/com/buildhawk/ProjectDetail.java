package com.buildhawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.buildhawk.utils.Report;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetail extends Activity {
	TextView tv_proName, tv_checklisttxt, tv_documenttxt, tv_reporttxt, tv_worklisttxt;
	RelativeLayout rel_checklist, rel_document, rel_report, rel_worklist, rel_back;
	ImageView checklistimg, documentimg, reportimg, worklistimg;
	ImageView searchimg;
	static Activity activity;
	public static Fragment fragment;
	RelativeLayout relLay;
	// TextView back_txt;
	public static ArrayList<Report>dateReports;
	ImageView backimg;
	ImageView img_addWorkitem;
	Button btn_selectdate;
	public static int flag = 0;
	Dialog popup;
	Button btn_email, btn_text, btn_call, btn_cancel;
	TextView tv_expiryAlert;
	RelativeLayout rel_listoutside;
	int year, day, month;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		relLay = (RelativeLayout) findViewById(R.id.rellay);
		new ASSL(this, relLay, 1134, 720, false);
		Prefrences.pageFlag = 1;
		rel_back = (RelativeLayout) findViewById(R.id.back);
		tv_proName = (TextView) findViewById(R.id.proName);
		rel_checklist = (RelativeLayout) findViewById(R.id.checklist);
		rel_document = (RelativeLayout) findViewById(R.id.documents);
		rel_report = (RelativeLayout) findViewById(R.id.reports);
		rel_worklist = (RelativeLayout) findViewById(R.id.worklist);
		activity = ProjectDetail.this;
		tv_checklisttxt = (TextView) findViewById(R.id.checklist_txt);
		tv_documenttxt = (TextView) findViewById(R.id.documents_txt);
		tv_reporttxt = (TextView) findViewById(R.id.reports_txt);
		tv_worklisttxt = (TextView) findViewById(R.id.worklist_txt);
		backimg = (ImageView) findViewById(R.id.back_img);
		searchimg = (ImageView)findViewById(R.id.searchimg);
		// back_txt=(TextView)findViewById(R.id.project_txt);

		checklistimg = (ImageView) findViewById(R.id.checklist_img);
		documentimg = (ImageView) findViewById(R.id.documents_img);
		reportimg = (ImageView) findViewById(R.id.reports_img);
		worklistimg = (ImageView) findViewById(R.id.worklist_img);

		img_addWorkitem = (ImageView) findViewById(R.id.addWorkitem);
		btn_selectdate = (Button) findViewById(R.id.selectdate);
		btn_selectdate.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		fragment = new ChecklistFragment();

		getFragmentManager().beginTransaction()
				.replace(R.id.fragment, fragment).commit();

		tv_proName.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		tv_checklisttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		tv_documenttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		tv_reporttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		tv_worklisttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		tv_proName.setText(Prefrences.selectedProName);

		btn_selectdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});

		rel_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// back_img.setBackgroundResource(drawable.back_btn_onclick);
				// back_txt.setTextColor(Color.BLACK);

				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		searchimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Search Image","Clicked");
				ChecklistFragment.search_rel.setVisibility(View.VISIBLE);
				
			}
		});
		
		rel_checklist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Prefrences.stopingHit=1;
				img_addWorkitem.setVisibility(View.GONE);
				btn_selectdate.setVisibility(View.GONE);
				searchimg.setVisibility(View.VISIBLE);

				rel_checklist.setBackgroundColor(Color.WHITE);
				rel_document.setBackgroundColor(Color.BLACK);
				rel_report.setBackgroundColor(Color.BLACK);
				rel_worklist.setBackgroundColor(Color.BLACK);

				tv_checklisttxt.setTextColor(Color.BLACK);
				tv_documenttxt.setTextColor(Color.WHITE);
				tv_reporttxt.setTextColor(Color.WHITE);
				tv_worklisttxt.setTextColor(Color.WHITE);

				checklistimg.setImageResource(R.drawable.btmcheck_off);
				documentimg.setImageResource(R.drawable.btmdocs_on);
				reportimg.setImageResource(R.drawable.btmreports_on);
				worklistimg.setImageResource(R.drawable.btmworklist_on);

				rel_document.setEnabled(true);
				rel_report.setEnabled(true);
				rel_worklist.setEnabled(true);
				rel_checklist.setEnabled(false);

				fragment = new ChecklistFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
			}
		});

		rel_document.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Prefrences.stopingHit = 1;
				img_addWorkitem.setVisibility(View.GONE);
				searchimg.setVisibility(View.GONE);
				btn_selectdate.setVisibility(View.GONE);
				rel_checklist.setBackgroundColor(Color.BLACK);
				rel_document.setBackgroundColor(Color.WHITE);
				rel_report.setBackgroundColor(Color.BLACK);
				rel_worklist.setBackgroundColor(Color.BLACK);

				tv_checklisttxt.setTextColor(Color.WHITE);
				tv_documenttxt.setTextColor(Color.BLACK);
				tv_reporttxt.setTextColor(Color.WHITE);
				tv_worklisttxt.setTextColor(Color.WHITE);

				checklistimg.setImageResource(R.drawable.btmcheck_on);
				documentimg.setImageResource(R.drawable.btmdocs_off);
				reportimg.setImageResource(R.drawable.btmreports_on);
				worklistimg.setImageResource(R.drawable.btmworklist_on);

				rel_document.setEnabled(false);
				rel_report.setEnabled(true);
				rel_worklist.setEnabled(true);
				rel_checklist.setEnabled(true);
				fragment = new DocumentFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
			}
		});

		rel_report.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// addWorkitem.setVisibility(View.GONE);
				Prefrences.stopingHit = 1;

				rel_checklist.setBackgroundColor(Color.BLACK);
				rel_document.setBackgroundColor(Color.BLACK);
				rel_report.setBackgroundColor(Color.WHITE);
				rel_worklist.setBackgroundColor(Color.BLACK);

				tv_checklisttxt.setTextColor(Color.WHITE);
				tv_documenttxt.setTextColor(Color.WHITE);
				tv_reporttxt.setTextColor(Color.BLACK);
				tv_worklisttxt.setTextColor(Color.WHITE);

				checklistimg.setImageResource(R.drawable.btmcheck_on);
				documentimg.setImageResource(R.drawable.btmdocs_on);
				reportimg.setImageResource(R.drawable.btmreports_off);
				worklistimg.setImageResource(R.drawable.btmworklist_on);

				rel_document.setEnabled(true);
				rel_report.setEnabled(false);
				rel_worklist.setEnabled(true);
				rel_checklist.setEnabled(true);

				img_addWorkitem.setVisibility(View.VISIBLE);
				btn_selectdate.setVisibility(View.VISIBLE);
				searchimg.setVisibility(View.GONE);
				img_addWorkitem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Flag=2;

						popup = new Dialog(ProjectDetail.this,
								android.R.style.Theme_Translucent_NoTitleBar);
						// expiry_popup.setCancelable(false);

						popup.setContentView(R.layout.bridge_expiry_popup);
						// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
						RelativeLayout expirymain = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						// expiry_main.setInAnimation(R.anim.slide_in_from_top);
						btn_email = (Button) popup.findViewById(R.id.Email);
						btn_call = (Button) popup.findViewById(R.id.Call);
						btn_text = (Button) popup.findViewById(R.id.Text);
						btn_cancel = (Button) popup.findViewById(R.id.Cancel);
						tv_expiryAlert = (TextView) popup
								.findViewById(R.id.alert_text);

						btn_email.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						btn_text.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						btn_call.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						btn_cancel.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));

						tv_expiryAlert.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());

						rel_listoutside = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						new ASSL(ProjectDetail.this, expirymain, 1134, 720,
								false);

						btn_email.setText("Daily");
						btn_call.setText("Safety");
						btn_text.setText("Weekly");
						tv_expiryAlert.setText("Reports");

						rel_listoutside.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (popup.isShowing()) {
									popup.dismiss();
									// overridePendingTransition(R.anim.slide_in_bottom,
									// R.anim.slide_out_to_top);
								}
							}
						});

						btn_email.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();
								Log.e("Today date", ", "+dateFormat
										.format(date)
										.toString());
								
								Log.e("Saved date", ", "+Prefrences
										.getDateDaily(getApplicationContext()));
								Log.e("Saved Type", ", "+Prefrences
										.getTypeDaily(getApplicationContext()));

								if (!dateFormat
										.format(date)
										.toString()
										.equalsIgnoreCase(
												Prefrences
														.getDateDaily(getApplicationContext())) && !btn_email.getText()
														.toString()
														.equalsIgnoreCase(
																Prefrences
																		.getTypeDaily(getApplicationContext()))) {
									
									Prefrences.reportTypeDialog = 1;
									popup.dismiss();

//									 Prefrences.saveDateDaily(dateFormat.format(date).toString(),
//									 getApplicationContext(),email.getText().toString());
									 
									 
									Intent intent = new Intent(activity,
											ReportItemCreate.class);
									startActivity(intent);
									overridePendingTransition(
											R.anim.slide_in_right,
											R.anim.slide_out_left);
									
								} else {
									Toast.makeText(
											activity,
											"Sorry, you can create only one Daily report in a day",
											50000).show();
								}

							}
						});

						btn_call.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();
								Log.e("Today date", ", "+dateFormat
										.format(date)
										.toString());
								
								Log.e("Saved date", ", "+Prefrences
										.getDateSafety(getApplicationContext()));
								Log.e("Saved Type", ", "+Prefrences
										.getTypeSafety(getApplicationContext()));

								if (!dateFormat
										.format(date)
										.toString()
										.equalsIgnoreCase(
												Prefrences
														.getDateSafety(getApplicationContext())) && !btn_email.getText()
														.toString()
														.equalsIgnoreCase(
																Prefrences
																		.getTypeSafety(getApplicationContext()))) {
									Prefrences.reportTypeDialog = 2;
									Intent intent = new Intent(activity,
											ReportItemCreate.class);
									startActivity(intent);
									overridePendingTransition(
											R.anim.slide_in_right,
											R.anim.slide_out_left);
//									Prefrences.saveDateSafety(dateFormat.format(date).toString(),
//											 getApplicationContext(),call.getText().toString());
									popup.dismiss();
								} else {
									Toast.makeText(
											activity,
											"Sorry, you can create only one Safety report in a day",
											50000).show();
								}
							}
						});

						btn_text.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();
								Log.e("Today date", ", "+dateFormat
										.format(date)
										.toString());
								
								Log.e("Saved date", ", "+Prefrences
										.getDateWeekly(getApplicationContext()));
								Log.e("Saved Type", ", "+Prefrences
										.getTypeWeekly(getApplicationContext()));

								if (!dateFormat
										.format(date)
										.toString()
										.equalsIgnoreCase(
												Prefrences
														.getDateWeekly(getApplicationContext())) && !btn_text.getText()
														.toString()
														.equalsIgnoreCase(
																Prefrences
																		.getTypeWeekly(getApplicationContext()))) {
								Prefrences.reportTypeDialog = 3;
								Intent in = new Intent(activity,
										ReportItemCreate.class);
								startActivity(in);
								overridePendingTransition(
										R.anim.slide_in_right,
										R.anim.slide_out_left);
//								Prefrences.saveDateWeekly(dateFormat.format(date).toString(),
//										 getApplicationContext(),text.getText().toString());
								popup.dismiss();
								 }
								 else
								 {
								 Toast.makeText(activity,
								 "Sorry, you can create only one Weekly report in a day",
								 50000).show();
								 }
							}
						});

						btn_cancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								popup.dismiss();
							}
						});
						popup.show();

					}
				});

				fragment = new ReportFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
			}
		});

		rel_worklist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				rel_checklist.setBackgroundColor(Color.BLACK);
				rel_document.setBackgroundColor(Color.BLACK);
				rel_report.setBackgroundColor(Color.BLACK);
				rel_worklist.setBackgroundColor(Color.WHITE);

				tv_checklisttxt.setTextColor(Color.WHITE);
				tv_documenttxt.setTextColor(Color.WHITE);
				tv_reporttxt.setTextColor(Color.WHITE);
				tv_worklisttxt.setTextColor(Color.BLACK);

				checklistimg.setImageResource(R.drawable.btmcheck_on);
				documentimg.setImageResource(R.drawable.btmdocs_on);
				reportimg.setImageResource(R.drawable.btmreports_on);
				worklistimg.setImageResource(R.drawable.btmworklist_off);

				rel_document.setEnabled(true);
				rel_report.setEnabled(true);
				rel_worklist.setEnabled(false);
				rel_checklist.setEnabled(true);

				searchimg.setVisibility(View.GONE);
				img_addWorkitem.setVisibility(View.VISIBLE);
				btn_selectdate.setVisibility(View.GONE);

				img_addWorkitem.setOnClickListener(new OnClickListener() {

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

				fragment = new WorklistFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
int flag=0;
				if (which == DialogInterface.BUTTON_POSITIVE) {
					DatePicker datePicker = datepiker.getDatePicker();
					dateListener.onDateSet(datePicker, datePicker.getYear(),
							datePicker.getMonth(), datePicker.getDayOfMonth());
					dateReports= new ArrayList<Report>();
					for(int i =0; i<ReportFragment.reportdata.size();i++)
					{
						Log.d("hellllooooo", "helllloo"+Prefrences.selecteddate.toString()+"yoyoyoyo"+ReportFragment.reportdata.get(i).created_date.toString());

						if(ReportFragment.reportdata.get(i).created_date.toString().equalsIgnoreCase(Prefrences.selecteddate.toString()))
						{
							
							flag=1;
//							Toast.makeText(getApplicationContext(), "Hellloooo"+Prefrences.selecteddate.toString(), Toast.LENGTH_LONG).show();
							Log.d("hellllooooo", "helllloo"+Prefrences.selecteddate.toString());
							dateReports.add(ReportFragment.reportdata.get(i));
						}
					}
					// dateSet = true;
					if(flag==1)
					{
						Prefrences.reportType=4;
						Intent intent = new Intent(activity, ReportItemClick.class);

						intent.putExtra("pos", 0);
						activity.startActivity(intent);
					}
					else{
					Intent intent = new Intent(ProjectDetail.this,
							ReportItemCreate.class);
					startActivity(intent);
					}
					dialog.dismiss();

					dialog.cancel();

				}

			}

		});

		return datepiker;

	}

}
