package com.buildhawk;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
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

	ArrayList<String> workuser, checkuser, reportuser, docuser, allusers,
			docdate, workdate, checkdate, reportdate, alldates, docphase,
			checklistphase, worklistphase, reportphase, allphase;

	ArrayList<String> users = new ArrayList<String>();
	ArrayList<String> tempusers = new ArrayList<String>();
	ArrayList<String> tempdates = new ArrayList<String>();
	ArrayList<String> dates = new ArrayList<String>();

	ArrayList<String> tempphase = new ArrayList<String>();
	ArrayList<String> phase = new ArrayList<String>();
	ArrayList<String> arr = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<String> desc = new ArrayList<String>();
	Dialog dialog;
	int flag = 0;
	LinearLayout lay;
	LinearLayout mainLayout;
	ImageView imgView;
	TextView txtView, sort, projecttext;
	int count = 0;
	RelativeLayout relLay;
	RelativeLayout back;
	TextView tvKey;

	String key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		relLay = (RelativeLayout) findViewById(R.id.rellay);
		new ASSL(this, relLay, 1134, 720, false);
		// Bundle bundle=new Bundle();
		// bundle.getString("key",)
		sort = (TextView) findViewById(R.id.sort);
		// projecttext=(TextView)findViewById(R.id.project_txt);
		back = (RelativeLayout) findViewById(R.id.back);

		// projecttext.setText(Prefrences.selectedProName);
		Bundle bundle = getIntent().getExtras();
		key = bundle.getString("key");
		workuser = new ArrayList<String>();
		checkuser = new ArrayList<String>();
		reportuser = new ArrayList<String>();
		docuser = new ArrayList<String>();
		allusers = new ArrayList<String>();
		docdate = new ArrayList<String>();
		checkdate = new ArrayList<String>();
		workdate = new ArrayList<String>();
		reportdate = new ArrayList<String>();
		alldates = new ArrayList<String>();
		docphase = new ArrayList<String>();
		checklistphase = new ArrayList<String>();
		worklistphase = new ArrayList<String>();
		reportphase = new ArrayList<String>();
		allphase = new ArrayList<String>();

		workuser = bundle.getStringArrayList("work");
		checkuser = bundle.getStringArrayList("check");
		reportuser = bundle.getStringArrayList("report");
		docuser = bundle.getStringArrayList("doc");
		allusers = bundle.getStringArrayList("allusers");

		docdate = bundle.getStringArrayList("doc_date");
		checkdate = bundle.getStringArrayList("check_date");
		workdate = bundle.getStringArrayList("work_date");
		reportdate = bundle.getStringArrayList("report_date");
		alldates = bundle.getStringArrayList("all_date");

		docphase = bundle.getStringArrayList("doc_phase");
		checklistphase = bundle.getStringArrayList("checklist_phase");
		worklistphase = bundle.getStringArrayList("worklist_phase");
		reportphase = bundle.getStringArrayList("report_phase");
		allphase = bundle.getStringArrayList("all_phase");

		arraylist.clear();

		mainLayout = (LinearLayout) findViewById(R.id.view);
		new ASSL(this, mainLayout, 1134, 720, false);

		Prefrences.well = 1;

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				arraylist.clear();
				Prefrences.well = 0;
				// work_user, check_user, report_user, doc_user, all_users,
				// doc_date, work_date, check_date, report_date, all_dates,
				// doc_phase,
				// checklist_phase, worklist_phase, report_phase, all_phase;

				users.clear();
				tempusers.clear();

				tempdates.clear();
				dates.clear();
				allusers.clear();
				workuser.clear();
				checkuser.clear();
				reportuser.clear();

				docuser.clear();
				tempphase.clear();
				phase.clear();
				arr.clear();
				ids.clear();
				desc.clear();

				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		sort.setOnClickListener(new OnClickListener() {

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
				Button btnsort = (Button) dialog.findViewById(R.id.btn_sort);
				Button btnusers = (Button) dialog.findViewById(R.id.btn_user);
				RelativeLayout listoutside = (RelativeLayout) dialog
						.findViewById(R.id.list_outside);
				TextView who = (TextView) dialog.findViewById(R.id.txt_who);
				Button btnsub = (Button) dialog.findViewById(R.id.btn_sub);
				Button btncancel = (Button) dialog.findViewById(R.id.cancel);

				btncancel.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				btnsort.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				btnsub.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				btnusers.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				who.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				who.setGravity(Gravity.CENTER);
				who.setText("Sort");
				if (key.equals("All")) {
					btnsort.setVisibility(View.VISIBLE);
					btnsort.setText("By Taken/Uploaded");
					btnusers.setText("By Date");
					btnsub.setText("By Default");
				} else if (key.equals("Checklist")) {
					btnsort.setVisibility(View.VISIBLE);
					btnsort.setText("By Taken/Uploaded");
					btnusers.setText("By Date");
					btnsub.setText("By Default");
				} else if (key.equals("Worklist")) {
					btnsort.setVisibility(View.VISIBLE);
					btnsort.setText("By Taken/Uploaded");
					btnusers.setText("By Date");
					btnsub.setText("By Default");

				} else if (key.equals("Report")) {
					btnsort.setVisibility(View.GONE);
					// btn_sort.setText("By Taken/Uploaded");
					btnusers.setText("By Taken/Uploaded");
					btnsub.setText("By Default");
				} else if (key.equals("Project Docs")) {
					btnsort.setVisibility(View.GONE);
					// btn_sort.setText("By Taken/Uploaded");
					btnusers.setText("By Taken/Uploaded");
					btnsub.setText("By Default");
				}
				btnsort.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						byname(key);
						dialog.dismiss();
					}
				});

				btnusers.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (key.equals("Report")) {
							byname(key);
						} else if (key.equals("Project Docs")) {
							byname(key);
						} else {
							bydate(key);
						}
						dialog.dismiss();
					}
				});

				btnsub.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (key.equals("Checklist")) {
							byphase(key);
						} else if (key.equals("All")) {
							byall(key);
						} else if (key.equals("Report")) {
							bydate(key);
						} else if (key.equals("Worklist")) {
							byall(key);
						} else if (key.equals("Project Docs")) {
							byall(key);
						}
						dialog.dismiss();
					}
				});

				btncancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				listoutside.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});

		if (key.equals("Checklist")) {
			byphase(key);
		} else if (key.equals("All")) {
			byall(key);
		} else if (key.equals("Report")) {
			bydate(key);
		} else if (key.equals("Worklist")) {
			byall(key);
		} else if (key.equals("Project Docs")) {
			byall(key);
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

		users.clear();
		tempusers.clear();

		tempdates.clear();
		dates.clear();

		tempphase.clear();
		phase.clear();
		arr.clear();
		ids.clear();
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

	void byall(String str) {
		mainLayout.removeAllViews();
		arr.clear();
		ids.clear();
		desc.clear();
		arraylist.clear();
		if (str.equals("All")) {
			arraylist.addAll(DocumentFragment.photosList);
		} else if (str.equals("Report")) {
			arraylist.addAll(DocumentFragment.photosList5);
		} else if (str.equals("Worklist")) {
			arraylist.addAll(DocumentFragment.photosList4);
		} else if (str.equals("Project Docs")) {
			arraylist.addAll(DocumentFragment.photosList2);
		} else if (str.equals("Checklist")) {
			arraylist.addAll(DocumentFragment.photosList3);
		}

		if (arraylist.isEmpty()) {
			// Toast.makeText(getApplicationContext(), "null",
			// Toast.LENGTH_LONG).show();
		} else {
			int pos = 0;
			int rowValue = 0;
			lay = new LinearLayout(this);
			lay.setOrientation(LinearLayout.HORIZONTAL);
			mainLayout.addView(lay);
			do {
				arr.add(arraylist.get(pos).urlLarge);
				ids.add(arraylist.get(pos).id);
				desc.add(arraylist.get(pos).description);
				imgView = new ImageView(this);
				if (rowValue < 3) {
					rowValue++;
					// LayoutParams parms =

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);
					imgView.setTag(pos);
					imgView.setLayoutParams(lp);
					// Bitmap bitmap = BitmapFactory.decodeFile(Receiver1.list
					// .getAbsolutePath());

					Picasso.with(this)
							.load(arraylist.get(pos).url200.toString())
							.into(imgView);
					lay.addView(imgView);
				} else {
					rowValue = 0;
					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);
					imgView.setTag(pos);
					imgView.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(pos).url200.toString())
							.into(imgView);
					lay.addView(imgView);

					rowValue++;
				}
				imgView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Log.i("Tag Value", "" + Prefrences.selectedPic);
						Prefrences.selectedPic = (Integer) v.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);

						Intent intent = new Intent(ImageActivity.this,
								MainActivity1.class);
						intent.putExtra("array", arr);
						intent.putExtra("ids", ids);
						intent.putExtra("desc", desc);
						intent.putExtra("key", key);
						Log.d("", "----0000000----" + key);
						intent.putExtra("id",
								arraylist.get(Prefrences.selectedPic).id);

						intent.putStringArrayListExtra("doc", docuser);
						intent.putStringArrayListExtra("work", workuser);
						intent.putStringArrayListExtra("check", checkuser);
						intent.putStringArrayListExtra("report", reportuser);
						intent.putStringArrayListExtra("allusers", allusers);

						intent.putStringArrayListExtra("doc_date", docdate);
						intent.putStringArrayListExtra("work_date", workdate);
						intent.putStringArrayListExtra("check_date", checkdate);
						intent.putStringArrayListExtra("report_date",
								reportdate);
						intent.putStringArrayListExtra("all_date", alldates);

						intent.putStringArrayListExtra("doc_phase", docphase);
						intent.putStringArrayListExtra("worklist_phase",
								worklistphase);
						intent.putStringArrayListExtra("checklist_phase",
								checklistphase);
						intent.putStringArrayListExtra("report_phase",
								reportphase);
						intent.putStringArrayListExtra("all_phase", allphase);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				});

				pos++;
			} while (pos < arraylist.size());
		}
	}

	void byname(String s) {
		mainLayout.removeAllViews();
		arraylist.clear();
		users.clear();
		tempusers.clear();
		arr.clear();
		ids.clear();
		desc.clear();
		key = s;
		// Log.d("work size", "" + work_user.size());
		// Log.d("check size", "" + check_user.size());
		// Log.d("report size", "" + report_user.size());
		// Log.d("doc size", "" + doc_user.size());
		// Log.d("all users size", "" + all_users.size());

		if (s.equals("All")) {
			arraylist.addAll(DocumentFragment.photosList);
			users.addAll(allusers);
			tempusers.addAll(allusers);
		}
		if (s.equals("Project Docs")) {
			arraylist.addAll(DocumentFragment.photosList2);
			users.addAll(docuser);
			tempusers.addAll(docuser);
		} else if (s.equals("Checklist")) {
			arraylist.addAll(DocumentFragment.photosList3);

			users.addAll(checkuser);
			tempusers.addAll(checkuser);
		} else if (s.equals("Worklist")) {
			arraylist.addAll(DocumentFragment.photosList4);
			users.addAll(workuser);
			tempusers.addAll(workuser);
		} else if (s.equals("Report")) {
			arraylist.addAll(DocumentFragment.photosList5);
			users.addAll(reportuser);
			tempusers.addAll(reportuser);

		}

		Collections.sort(users);
		for (int i = 0; i < users.size(); i++) {
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
					txtView = new TextView(this);
					txtView.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					txtView.setText("" + users.get(pos));
					txtView.setTextSize(30);
					txtView.setTextColor(Color.WHITE);
					txtView.setPadding(10, 0, 0, 0);
					mainLayout.addView(txtView);

					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
					flag = 1;
					rowValue = 0;
				} else if (pos != 0
						&& !users.get(pos).equals(users.get(pos - 1))) {

					txtView = new TextView(this);
					txtView.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					txtView.setText("" + users.get(pos));
					txtView.setTextSize(30);
					txtView.setTextColor(Color.WHITE);
					txtView.setPadding(10, 0, 0, 0);
					mainLayout.addView(txtView);

					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
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
				imgView = new ImageView(this);
				int indexplus = 0;
				for (int j = 0; j < tempusers.size(); j++) {
					// Log.d("tempusers.get("+j+"):",tempusers.get(j));
					// Log.d("users.get("+i+"):",users.get(i));
					if (tempusers.get(j).equals(users.get(pos))) {
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
				arr.add(arraylist.get(index).urlLarge);
				ids.add(arraylist.get(index).id);
				desc.add(arraylist.get(index).description);
				if (rowValue < 3) {
					rowValue++;
					imgView.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);

					imgView.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.into(imgView);
					lay.addView(imgView);
					// Log.d("actual uri", arraylist.get(index).urlLarge);
				} else {
					rowValue = 0;
					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
					imgView.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);

					imgView.setLayoutParams(lp);

					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.into(imgView);
					lay.addView(imgView);
					// Log.d("actual uri", arraylist.get(index).urlLarge);
					rowValue++;
				}

				imgView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Log.i("Tag Value", "" + Prefrences.selectedPic);
						Prefrences.selectedPic = (Integer) v.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);

						Intent intent = new Intent(ImageActivity.this,
								MainActivity1.class);
						intent.putExtra("array", arr);
						intent.putExtra("ids", ids);
						intent.putExtra("desc", desc);
						intent.putExtra("key", key);
						Log.d("", "----0000000----" + key);
						intent.putExtra("id",
								arraylist.get(Prefrences.selectedPic).id);

						intent.putStringArrayListExtra("doc", docuser);
						intent.putStringArrayListExtra("work", workuser);
						intent.putStringArrayListExtra("check", checkuser);
						intent.putStringArrayListExtra("report", reportuser);
						intent.putStringArrayListExtra("allusers", allusers);

						intent.putStringArrayListExtra("doc_date", docdate);
						intent.putStringArrayListExtra("work_date", workdate);
						intent.putStringArrayListExtra("check_date", checkdate);
						intent.putStringArrayListExtra("report_date",
								reportdate);
						intent.putStringArrayListExtra("all_date", alldates);

						intent.putStringArrayListExtra("doc_phase", docphase);
						intent.putStringArrayListExtra("worklist_phase",
								worklistphase);
						intent.putStringArrayListExtra("checklist_phase",
								checklistphase);
						intent.putStringArrayListExtra("report_phase",
								reportphase);
						intent.putStringArrayListExtra("all_phase", allphase);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				});
				pos++;
			} while (pos < users.size());
		}
	}

	void bydate(String str) {
		mainLayout.removeAllViews();
		arraylist.clear();
		dates.clear();
		tempdates.clear();
		arr.clear();
		ids.clear();
		desc.clear();
		// Log.d("work date size", "" + work_date.size());
		// Log.d("check date size", "" + check_date.size());
		// Log.d("report date size", "" + report_date.size());
		// Log.d("doc date size", "" + doc_date.size());
		Log.d("all users date size", "" + alldates.size());
		if (str.equals("All")) {
			arraylist.addAll(DocumentFragment.photosList);
			dates.addAll(alldates);
			tempdates.addAll(alldates);
		}
		if (str.equals("Project Docs")) {
			arraylist.addAll(DocumentFragment.photosList2);
			dates.addAll(docdate);
			tempdates.addAll(docdate);
		} else if (str.equals("Checklist")) {
			arraylist.addAll(DocumentFragment.photosList3);
			dates.addAll(checkdate);
			tempdates.addAll(checkdate);
		} else if (str.equals("Worklist")) {
			arraylist.addAll(DocumentFragment.photosList4);
			dates.addAll(workdate);
			tempdates.addAll(workdate);
		} else if (str.equals("Report")) {
			arraylist.addAll(DocumentFragment.photosList5);
			dates.addAll(reportdate);
			tempdates.addAll(reportdate);

		}

		for (int i = 0; i < tempdates.size(); i++) {
			// Log.d("tempdates", tempdates.get(i));
			// Log.d("temp uri", arraylist.get(i).urlLarge);
		}

		Collections.sort(dates, Collections.reverseOrder());
		for (int i = 0; i < dates.size(); i++) {
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
					txtView = new TextView(this);
					txtView.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					txtView.setText("" + dates.get(pos));
					txtView.setTextSize(30);
					txtView.setTextColor(Color.WHITE);
					txtView.setPadding(10, 0, 0, 0);
					mainLayout.addView(txtView);

					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
					flag = 1;
					rowValue = 0;
				} else if (pos != 0
						&& !dates.get(pos).equals(dates.get(pos - 1))) {

					txtView = new TextView(this);
					txtView.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					txtView.setText("" + dates.get(pos));
					txtView.setTextSize(30);
					txtView.setTextColor(Color.WHITE);
					txtView.setPadding(10, 0, 0, 0);
					mainLayout.addView(txtView);

					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
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
				imgView = new ImageView(this);
				int indexplus = 0;
				for (int j = 0; j < tempdates.size(); j++) {
					// Log.d("tempdates.get("+j+"):",tempdates.get(j));
					// Log.d("dates.get("+i+"):",dates.get(i));
					if (tempdates.get(j).equals(dates.get(pos))) {
						indexplus++;
						if (count == indexplus) {
							index = j;
							// Log.d("index",""+index);
							break;
						}
					}
				}
				Log.d("", "-----1----" + index);
				Log.d("", "-----2----" + dates.size());
				Log.d("", "-----3----" + tempdates.size());
				arr.add(arraylist.get(index).urlLarge);
				ids.add(arraylist.get(index).id);
				desc.add(arraylist.get(index).description);

				if (rowValue < 3) {
					rowValue++;
					imgView.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);

					imgView.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.into(imgView);
					lay.addView(imgView);
					// Log.d("actual uri", arraylist.get(index).urlLarge);
				} else {
					rowValue = 0;
					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
					imgView.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 10);

					imgView.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.into(imgView);
					lay.addView(imgView);
					// Log.d("actual uri", arraylist.get(index).urlLarge);
					rowValue++;
				}
				imgView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Log.i("Tag Value", "" + Prefrences.selectedPic);
						Prefrences.selectedPic = (Integer) v.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);

						Intent intent = new Intent(ImageActivity.this,
								MainActivity1.class);
						intent.putExtra("array", arr);
						intent.putExtra("ids", ids);
						intent.putExtra("desc", desc);
						intent.putExtra("key", key);
						Log.d("", "----0000000----" + key);
						intent.putExtra("id",
								arraylist.get(Prefrences.selectedPic).id);

						intent.putStringArrayListExtra("doc", docuser);
						intent.putStringArrayListExtra("work", workuser);
						intent.putStringArrayListExtra("check", checkuser);
						intent.putStringArrayListExtra("report", reportuser);
						intent.putStringArrayListExtra("allusers", allusers);

						intent.putStringArrayListExtra("doc_date", docdate);
						intent.putStringArrayListExtra("work_date", workdate);
						intent.putStringArrayListExtra("check_date", checkdate);
						intent.putStringArrayListExtra("report_date",
								reportdate);
						intent.putStringArrayListExtra("all_date", alldates);

						intent.putStringArrayListExtra("doc_phase", docphase);
						intent.putStringArrayListExtra("worklist_phase",
								worklistphase);
						intent.putStringArrayListExtra("checklist_phase",
								checklistphase);
						intent.putStringArrayListExtra("report_phase",
								reportphase);
						intent.putStringArrayListExtra("all_phase", allphase);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				});
				pos++;
			} while (pos < dates.size());
		}
	}

	void byphase(String str) {
		mainLayout.removeAllViews();
		arraylist.clear();
		phase.clear();
		tempphase.clear();
		arr.clear();
		ids.clear();
		desc.clear();
		Log.d("work phase size", "" + worklistphase.size());
		Log.d("check phase size", "" + checklistphase.size());
		Log.d("report phase size", "" + reportphase.size());
		Log.d("doc phase size", "" + docphase.size());
		Log.d("all users phase size", "" + allphase.size());
		if (str.equals("All")) {
			arraylist.addAll(DocumentFragment.photosList);
			phase.addAll(allphase);
			tempphase.addAll(allphase);
		}
		if (str.equals("Project Docs")) {
			arraylist.addAll(DocumentFragment.photosList2);
			phase.addAll(docphase);
			tempphase.addAll(docphase);
		} else if (str.equals("Checklist")) {
			arraylist.addAll(DocumentFragment.photosList3);
			phase.addAll(checklistphase);
			tempphase.addAll(checklistphase);
		} else if (str.equals("Worklist")) {
			arraylist.addAll(DocumentFragment.photosList4);
			phase.addAll(worklistphase);
			tempphase.addAll(worklistphase);
		} else if (str.equals("Report")) {
			arraylist.addAll(DocumentFragment.photosList5);
			phase.addAll(reportphase);
			tempphase.addAll(reportphase);

		}

		for (int i = 0; i < tempphase.size(); i++) {
			// Log.d("tempphase", tempphase.get(i));
			// Log.d("temp uri", arraylist.get(i).urlLarge);
		}

		Collections.sort(phase);
		for (int i = 0; i < phase.size(); i++) {
			Log.d("phase", phase.get(i));
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
					txtView = new TextView(this);
					txtView.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					txtView.setText("" + phase.get(pos));
					txtView.setTextSize(30);
					txtView.setPadding(10, 0, 0, 0);
					txtView.setTextColor(Color.WHITE);
					mainLayout.addView(txtView);

					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
					flag = 1;
					rowValue = 0;
				} else if (pos != 0
						&& !phase.get(pos).equals(phase.get(pos - 1))) {

					txtView = new TextView(this);
					txtView.setTypeface(Prefrences
							.helveticaNeuelt(getApplicationContext()));
					txtView.setText("" + phase.get(pos));
					txtView.setTextSize(30);
					txtView.setPadding(10, 0, 0, 0);
					txtView.setTextColor(Color.WHITE);
					mainLayout.addView(txtView);

					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
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
				imgView = new ImageView(this);
				int indexplus = 0;
				for (int j = 0; j < tempphase.size(); j++) {
					Log.d("tempphase.get(" + j + "):", tempphase.get(j));
					Log.d("phase.get(" + pos + "):", phase.get(pos));
					if (tempphase.get(j).equals(phase.get(pos))) {
						indexplus++;
						if (count == indexplus) {
							index = j;
							Log.d("index", "" + index);
							break;
						}
					}
				}
				Log.d("", "-----1----" + index);
				Log.d("", "-----2----" + phase.size());
				Log.d("", "-----3----" + tempphase.size());
				arr.add(arraylist.get(index).urlLarge);
				ids.add(arraylist.get(index).id);
				desc.add(arraylist.get(index).description);
				if (rowValue < 3) {
					rowValue++;
					imgView.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 0);

					imgView.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.into(imgView);
					lay.addView(imgView);
					Log.d("actual uri", arraylist.get(index).urlLarge);
				} else {
					rowValue = 0;
					lay = new LinearLayout(this);
					lay.setOrientation(LinearLayout.HORIZONTAL);
					mainLayout.addView(lay);
					imgView.setTag(pos);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							(int) (220 * ASSL.Xscale()),
							(int) (220 * ASSL.Yscale()));
					lp.setMargins(15, 10, 0, 0);

					imgView.setLayoutParams(lp);
					Picasso.with(this)
							.load(arraylist.get(index).url200.toString())
							.into(imgView);
					lay.addView(imgView);
					Log.d("actual uri", arraylist.get(index).urlLarge);
					rowValue++;
				}
				imgView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Log.i("Tag Value", "" + Prefrences.selectedPic);
						Prefrences.selectedPic = (Integer) v.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);

						Intent intent = new Intent(ImageActivity.this,
								MainActivity1.class);
						intent.putExtra("array", arr);
						intent.putExtra("ids", ids);
						intent.putExtra("desc", desc);
						intent.putExtra("key", key);
						Log.d("", "----0000000----" + key);
						intent.putExtra("id",
								arraylist.get(Prefrences.selectedPic).id);

						intent.putStringArrayListExtra("doc", docuser);
						intent.putStringArrayListExtra("work", workuser);
						intent.putStringArrayListExtra("check", checkuser);
						intent.putStringArrayListExtra("report", reportuser);
						intent.putStringArrayListExtra("allusers", allusers);

						intent.putStringArrayListExtra("doc_date", docdate);
						intent.putStringArrayListExtra("work_date", workdate);
						intent.putStringArrayListExtra("check_date", checkdate);
						intent.putStringArrayListExtra("report_date",
								reportdate);
						intent.putStringArrayListExtra("all_date", alldates);

						intent.putStringArrayListExtra("doc_phase", docphase);
						intent.putStringArrayListExtra("worklist_phase",
								worklistphase);
						intent.putStringArrayListExtra("checklist_phase",
								checklistphase);
						intent.putStringArrayListExtra("report_phase",
								reportphase);
						intent.putStringArrayListExtra("all_phase", allphase);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}
				});
				pos++;
			} while (pos < phase.size());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

}