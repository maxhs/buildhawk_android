package com.buildhawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	TextView proName, checklisttxt, documenttxt, reporttxt, worklisttxt;
	RelativeLayout checklist, document, report, worklist, back;
	ImageView checklistimg, documentimg, reportimg, worklistimg;
	static Activity activity;
	public static Fragment fragment;
	RelativeLayout relLay;
	// TextView back_txt;
	ImageView backimg;
	ImageView addWorkitem;
	Button selectdate;
	public static int flag = 0;
	Dialog popup;
	Button email, text, call, cancel;
	TextView expiryAlert;
	RelativeLayout listoutside;
	int year, day, month;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		relLay = (RelativeLayout) findViewById(R.id.rellay);
		new ASSL(this, relLay, 1134, 720, false);
		Prefrences.pageFlag = 1;
		back = (RelativeLayout) findViewById(R.id.back);
		proName = (TextView) findViewById(R.id.proName);
		checklist = (RelativeLayout) findViewById(R.id.checklist);
		document = (RelativeLayout) findViewById(R.id.documents);
		report = (RelativeLayout) findViewById(R.id.reports);
		worklist = (RelativeLayout) findViewById(R.id.worklist);
		activity = ProjectDetail.this;
		checklisttxt = (TextView) findViewById(R.id.checklist_txt);
		documenttxt = (TextView) findViewById(R.id.documents_txt);
		reporttxt = (TextView) findViewById(R.id.reports_txt);
		worklisttxt = (TextView) findViewById(R.id.worklist_txt);
		backimg = (ImageView) findViewById(R.id.back_img);
		// back_txt=(TextView)findViewById(R.id.project_txt);

		checklistimg = (ImageView) findViewById(R.id.checklist_img);
		documentimg = (ImageView) findViewById(R.id.documents_img);
		reportimg = (ImageView) findViewById(R.id.reports_img);
		worklistimg = (ImageView) findViewById(R.id.worklist_img);

		addWorkitem = (ImageView) findViewById(R.id.addWorkitem);
		selectdate = (Button) findViewById(R.id.selectdate);
		selectdate.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		fragment = new ChecklistFragment();

		getFragmentManager().beginTransaction()
				.replace(R.id.fragment, fragment).commit();

		proName.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		checklisttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		documenttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		reporttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		worklisttxt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		proName.setText(Prefrences.selectedProName);

		selectdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// back_img.setBackgroundResource(drawable.back_btn_onclick);
				// back_txt.setTextColor(Color.BLACK);

				ChecklistFragment.listDataHeader.clear();
				ChecklistFragment.completeHeader.clear();
				ChecklistFragment.activeHeader.clear();
				ChecklistFragment.progressarr.clear();
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		checklist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				addWorkitem.setVisibility(View.GONE);
				selectdate.setVisibility(View.GONE);

				checklist.setBackgroundColor(Color.WHITE);
				document.setBackgroundColor(Color.BLACK);
				report.setBackgroundColor(Color.BLACK);
				worklist.setBackgroundColor(Color.BLACK);

				checklisttxt.setTextColor(Color.BLACK);
				documenttxt.setTextColor(Color.WHITE);
				reporttxt.setTextColor(Color.WHITE);
				worklisttxt.setTextColor(Color.WHITE);

				checklistimg.setImageResource(R.drawable.btmcheck_off);
				documentimg.setImageResource(R.drawable.btmdocs_on);
				reportimg.setImageResource(R.drawable.btmreports_on);
				worklistimg.setImageResource(R.drawable.btmworklist_on);

				document.setEnabled(true);
				report.setEnabled(true);
				worklist.setEnabled(true);

				fragment = new ChecklistFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
			}
		});

		document.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Prefrences.stopingHit = 1;
				addWorkitem.setVisibility(View.GONE);
				selectdate.setVisibility(View.GONE);
				checklist.setBackgroundColor(Color.BLACK);
				document.setBackgroundColor(Color.WHITE);
				report.setBackgroundColor(Color.BLACK);
				worklist.setBackgroundColor(Color.BLACK);

				checklisttxt.setTextColor(Color.WHITE);
				documenttxt.setTextColor(Color.BLACK);
				reporttxt.setTextColor(Color.WHITE);
				worklisttxt.setTextColor(Color.WHITE);

				checklistimg.setImageResource(R.drawable.btmcheck_on);
				documentimg.setImageResource(R.drawable.btmdocs_off);
				reportimg.setImageResource(R.drawable.btmreports_on);
				worklistimg.setImageResource(R.drawable.btmworklist_on);

				document.setEnabled(false);
				report.setEnabled(true);
				worklist.setEnabled(true);
				fragment = new DocumentFragment();
				getFragmentManager().beginTransaction()
						.replace(R.id.fragment, fragment).commit();
			}
		});

		report.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// addWorkitem.setVisibility(View.GONE);
				Prefrences.stopingHit = 1;

				checklist.setBackgroundColor(Color.BLACK);
				document.setBackgroundColor(Color.BLACK);
				report.setBackgroundColor(Color.WHITE);
				worklist.setBackgroundColor(Color.BLACK);

				checklisttxt.setTextColor(Color.WHITE);
				documenttxt.setTextColor(Color.WHITE);
				reporttxt.setTextColor(Color.BLACK);
				worklisttxt.setTextColor(Color.WHITE);

				checklistimg.setImageResource(R.drawable.btmcheck_on);
				documentimg.setImageResource(R.drawable.btmdocs_on);
				reportimg.setImageResource(R.drawable.btmreports_off);
				worklistimg.setImageResource(R.drawable.btmworklist_on);

				document.setEnabled(true);
				report.setEnabled(false);
				worklist.setEnabled(true);

				addWorkitem.setVisibility(View.VISIBLE);
				selectdate.setVisibility(View.VISIBLE);

				addWorkitem.setOnClickListener(new OnClickListener() {

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
						email = (Button) popup.findViewById(R.id.Email);
						call = (Button) popup.findViewById(R.id.Call);
						text = (Button) popup.findViewById(R.id.Text);
						cancel = (Button) popup.findViewById(R.id.Cancel);
						expiryAlert = (TextView) popup
								.findViewById(R.id.alert_text);

						email.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						text.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						call.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						cancel.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));

						expiryAlert.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());

						listoutside = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						new ASSL(ProjectDetail.this, expirymain, 1134, 720,
								false);

						email.setText("Daily");
						call.setText("Safety");
						text.setText("Weekly");
						expiryAlert.setText("Reports");

						listoutside.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (popup.isShowing()) {
									popup.dismiss();
									// overridePendingTransition(R.anim.slide_in_bottom,
									// R.anim.slide_out_to_top);
								}
							}
						});

						email.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								DateFormat dateFormat = new SimpleDateFormat(
										"yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();

								// Prefrences.saveDate(dateFormat.format(date).toString(),
								// getApplicationContext());
								if (!dateFormat
										.format(date)
										.toString()
										.equalsIgnoreCase(
												Prefrences
														.getDateDaily(getApplicationContext()))) {
									Prefrences.reportTypeDialog = 1;
									popup.dismiss();
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

						call.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								DateFormat dateFormat = new SimpleDateFormat(
										"yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();

								// Prefrences.saveDate(dateFormat.format(date).toString(),
								// getApplicationContext());
//								if (!dateFormat
//										.format(date)
//										.toString()
//										.equalsIgnoreCase(
//												Prefrences
//														.getDateSafety(getApplicationContext()))) {
									Prefrences.reportTypeDialog = 2;
									Intent intent = new Intent(activity,
											ReportItemCreate.class);
									startActivity(intent);
									overridePendingTransition(
											R.anim.slide_in_right,
											R.anim.slide_out_left);
									popup.dismiss();
//								} else {
//									Toast.makeText(
//											activity,
//											"Sorry, you can create only one Safety report in a day",
//											50000).show();
//								}
							}
						});

						text.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								DateFormat dateFormat = new SimpleDateFormat(
										"yyyy/MM/dd");
								// get current date time with Date()
								Date date = new Date();

								// Prefrences.saveDate(dateFormat.format(date).toString(),
								// getApplicationContext());
								// if(!dateFormat.format(date).toString().equalsIgnoreCase(Prefrences.getDate(getApplicationContext())))
								// {
								Prefrences.reportTypeDialog = 3;
								Intent in = new Intent(activity,
										ReportItemCreate.class);
								startActivity(in);
								overridePendingTransition(
										R.anim.slide_in_right,
										R.anim.slide_out_left);
								popup.dismiss();
								// }
								// else
								// {
								// Toast.makeText(activity,
								// "Sorry, you can create only one Weekly report in a day",
								// 50000).show();
								// }
							}
						});

						cancel.setOnClickListener(new OnClickListener() {
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

		worklist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				checklist.setBackgroundColor(Color.BLACK);
				document.setBackgroundColor(Color.BLACK);
				report.setBackgroundColor(Color.BLACK);
				worklist.setBackgroundColor(Color.WHITE);

				checklisttxt.setTextColor(Color.WHITE);
				documenttxt.setTextColor(Color.WHITE);
				reporttxt.setTextColor(Color.WHITE);
				worklisttxt.setTextColor(Color.BLACK);

				checklistimg.setImageResource(R.drawable.btmcheck_on);
				documentimg.setImageResource(R.drawable.btmdocs_on);
				reportimg.setImageResource(R.drawable.btmreports_on);
				worklistimg.setImageResource(R.drawable.btmworklist_off);

				document.setEnabled(true);
				report.setEnabled(true);
				worklist.setEnabled(false);

				addWorkitem.setVisibility(View.VISIBLE);
				selectdate.setVisibility(View.GONE);

				addWorkitem.setOnClickListener(new OnClickListener() {

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

				if (which == DialogInterface.BUTTON_POSITIVE) {
					DatePicker datePicker = datepiker.getDatePicker();
					dateListener.onDateSet(datePicker, datePicker.getYear(),
							datePicker.getMonth(), datePicker.getDayOfMonth());

					// dateSet = true;
					Intent intent = new Intent(ProjectDetail.this,
							ReportItemCreate.class);
					startActivity(intent);
					dialog.dismiss();

					dialog.cancel();

				}

			}

		});

		return datepiker;

	}

}
