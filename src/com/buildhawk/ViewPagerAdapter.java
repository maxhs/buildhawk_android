package com.buildhawk;

/*
 *  This file is used to show all reports in a horizontal scrollable view . 
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.buildhawk.utils.ProjectPhotos;
import com.buildhawk.utils.Report;
import com.buildhawk.utils.ReportPersonnel;
import com.buildhawk.utils.ReportTopics;
import com.buildhawk.utils.SafetyTopics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

	// int pos;
	Activity activity;
	Dialog dialog;
	public ArrayList<SafetyTopics> safetyArray;

	public ArrayList<SafetyTopics> arraysafety = new ArrayList<SafetyTopics>();
	TopicListAdapter topicadapter;
	ArrayList<Report> reportdata,reportdatTemp;
	LayoutInflater inflater;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
//	private static int PICK_CONTACT = 1;
	ArrayList<String> arr = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<String> desc = new ArrayList<String>();
	Button buttonEmail, buttonText, buttonCall, buttonCancel;
	TextView textviewExpiryAlert;
	Dialog popup;
//	public companyListAdapter companyadapter;
	RelativeLayout list_outside;
	

	

	// int poss;

	public ViewPagerAdapter(Activity act, ArrayList<Report> reportdata) {
		this.reportdata = reportdata;
		this.reportdatTemp=reportdata;
		activity = act;

		// ReportItemClick.CompaniesArray = new ArrayList<String>();
		// ReportItemClick.CompanyIdArrayList = new ArrayList<String>();
		// ReportItemClick.OnsiteArray = new ArrayList<String>();
		// ReportItemClick.personelHoursArray = new ArrayList<String>();
		// ReportItemClick.personelIdArrayList = new ArrayList<String>();
		// ReportItemClick.personelnamesArray = new ArrayList<String>();
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return reportdata.size();
	}

	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public Object instantiateItem(View view, final int position) {

		TextView textviewWind, textviewTemp, textviewPrecip, textviewHumidity, textviewWeather;
		final ListView listviewPersonel;
		final ListView listviewCompany;
		final ListView listviewSafety;
		ImageView imageviewWeatherIcon;
		Button buttonType, buttonDate, buttonPersonnel, buttonSafety,buttonPrefill;
		LinearLayout linearlayoutPersonel, linearlayoutWeather;
		final LinearLayout linearlayoutOnsite;
		ScrollView scrollview;
		final EditText edittextNotes;
		final LinearLayout linearlayoutPersonelList;
		final LinearLayout linearlayoutLin2;
		LinearLayout linearlayoutSafetyTopic, linearlayoutReport;
		ImageView imageviewPhotogallery, imageviewCamera;

		final View rootView = (View) inflater.inflate(
				R.layout.fragment_screen_slide_page, null);

		linearlayoutReport = (LinearLayout) rootView.findViewById(R.id.reportLayout);
		linearlayoutSafetyTopic = (LinearLayout) rootView
				.findViewById(R.id.linearlayoutSafetytopic);
		imageviewWeatherIcon = (ImageView) rootView.findViewById(R.id.imageviewWeatherIcon);
		buttonPersonnel = (Button) rootView.findViewById(R.id.button3);
		buttonPrefill=(Button)rootView.findViewById(R.id.buttonPrefill);
		buttonSafety = (Button) rootView.findViewById(R.id.buttonSafetytopic);
		buttonDate = (Button) rootView.findViewById(R.id.button2);
		buttonType = (Button) rootView.findViewById(R.id.button1);
		textviewWind = (TextView) rootView.findViewById(R.id.textviewWind);
		textviewTemp = (TextView) rootView.findViewById(R.id.textviewTemp);
		textviewPrecip = (TextView) rootView.findViewById(R.id.textviewPrecip);
		textviewHumidity = (TextView) rootView.findViewById(R.id.textviewHumidity);
		textviewWeather = (TextView) rootView.findViewById(R.id.body);
		edittextNotes = (EditText) rootView.findViewById(R.id.edittextNotes);
		listviewPersonel = (ListView) rootView.findViewById(R.id.personellist);
		listviewCompany = (ListView) rootView.findViewById(R.id.companieslist);
		listviewSafety = (ListView) rootView.findViewById(R.id.safetylist);
		linearlayoutPersonelList = (LinearLayout) rootView
				.findViewById(R.id.linearlayoutPersonel);
		linearlayoutLin2 = (LinearLayout) rootView.findViewById(R.id.linearlayoutLin2);

		listviewPersonel.setFocusable(false);
		listviewCompany.setFocusable(false);
		listviewSafety.setFocusable(false);
		// view.setTag(position);
		// rootView.setTag(position);

		linearlayoutPersonel = (LinearLayout) rootView
				.findViewById(R.id.linearlayoutPersonel);
		linearlayoutOnsite = (LinearLayout) rootView.findViewById(R.id.linearlayoutOnsite);

		linearlayoutWeather = (LinearLayout) rootView
				.findViewById(R.id.linearlayoutWeather);

		textviewWind.setTypeface(Prefrences.helveticaNeuelt(activity));
		textviewTemp.setTypeface(Prefrences.helveticaNeuelt(activity));
		textviewPrecip.setTypeface(Prefrences.helveticaNeuelt(activity));
		textviewHumidity.setTypeface(Prefrences.helveticaNeuelt(activity));
		textviewWeather.setTypeface(Prefrences.helveticaNeuelt(activity));

		buttonType.setTypeface(Prefrences.helveticaNeuelt(activity));
		buttonDate.setTypeface(Prefrences.helveticaNeuelt(activity));

		edittextNotes.setTypeface(Prefrences.helveticaNeuelt(activity));
		edittextNotes.setFocusable(false);
		scrollview = (ScrollView) rootView.findViewById(R.id.pullToRefreshScrollView);
		scrollview.smoothScrollTo(0, 0);

		// reportLayout.setVisibility(View.GONE);

		if (reportdata.get(position).report_type.toString().equalsIgnoreCase(
				"Daily")) {
			// type.setText("Daily");
			linearlayoutWeather.setVisibility(View.VISIBLE);
			linearlayoutSafetyTopic.setVisibility(View.GONE);
			// type.setEnabled(true);

		} else if (reportdata.get(position).report_type.toString()
				.equalsIgnoreCase("Safety")) {
			// type.setText("Safety");
			linearlayoutSafetyTopic.setVisibility(View.VISIBLE);
			linearlayoutWeather.setVisibility(View.GONE);
			// type.setEnabled(true);
		} else if (reportdata.get(position).report_type.toString()
				.equalsIgnoreCase("Weekly")) {
			// type.setText("Weekly");
			linearlayoutWeather.setVisibility(View.GONE);
			linearlayoutSafetyTopic.setVisibility(View.GONE);
			// type.setEnabled(true);
		}
		edittextNotes.setFocusableInTouchMode(true);
		edittextNotes.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				Log.e("TEXTVAGASGV", edittextNotes.getText().toString());
				Prefrences.report_body_edited = edittextNotes.getText().toString();
				reportdata.get(position).body = Prefrences.report_body_edited;
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});

		// new ASSL(activity, scr1, 1134, 720, false);
		imageviewPhotogallery = (ImageView) rootView.findViewById(R.id.photogallery);
		imageviewCamera = (ImageView) rootView.findViewById(R.id.camera);
		imageviewPhotogallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				activity.startActivityForResult(intent, RESULT_LOAD_IMAGE);
				Log.i("BACk to adapter", "BACk to adapter");

			}
		});

		buttonSafety.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				safetyTopic(position);
			}
		});

		buttonPrefill.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Prefrences.PrefillFlag==1)
				{}
				else{
				if(position!=reportdata.size())
				{
					Prefrences.PrefillFlag=1;					
				Log.d("Prefill from last report","Companies size "+reportdata.get(position+1).companies.size());
				Log.d("Prefill from last report","Personnel size "+reportdata.get(position+1).personnel.size());
				
				ReportItemClick.CompaniesArrayList.clear();
				ReportItemClick.CompanyIdArrayList.clear();
				ReportItemClick.OnsiteArrayList.clear();
				ReportItemClick.personelHoursArrayList.clear();
				ReportItemClick.personelnamesArrayList.clear();
				ReportItemClick.personelIdArrayList.clear();
				ReportItemClick.SafetyIDArrayList.clear();
				ReportItemClick.SafetyTitleArrayList.clear();

				for (int i = 0; i < reportdata.get(position+1).personnel.size(); i++) {
					ReportItemClick.personelnamesArrayList.add(reportdata.get(position+1).personnel.get(i).users.get(0).uFullName
							.toString());
					ReportItemClick.personelIdArrayList.add(reportdata.get(position+1).personnel.get(i).users.get(0).uId
							.toString());
					ReportItemClick.personelHoursArrayList.add(reportdata.get(position+1).personnel.get(i).hours
							.toString());
				}

				for (int i = 0; i < reportdata.get(position+1).companies.size(); i++) {
					ReportItemClick.CompaniesArrayList.add(reportdata.get(position+1).companies.get(i).Rcompany.get(0).coName
							.toString());
					ReportItemClick.CompanyIdArrayList.add(reportdata.get(position+1).companies.get(i).Rcompany.get(0).coId
							.toString());
					ReportItemClick.OnsiteArrayList.add(reportdata.get(position+1).companies.get(i).coCount
							.toString());
				}

//				for (int i = 0; i < reportdata_local.get(myPager
//						.getCurrentItem()).topic.size(); i++) {
//					SafetyIDArray.add(reportdata_local.get(myPager
//							.getCurrentItem()).topic.get(i).safety.get(0).Id
//							.toString());
//					SafetyTitleArray.add(reportdata_local.get(myPager
//							.getCurrentItem()).topic.get(i).safety.get(0).Title
//							.toString());
//					// OnsiteArray.add(reportdata_local.get(myPager.getCurrentItem()).companies.get(i).coCount.toString());
//				}
//				Prefrences.stopingHit=1;
				
				reportdata.get(position).companies.clear();
				reportdata.get(position).personnel.clear();
				reportdata.get(position).companies=reportdata.get(position+1).companies;
				reportdata.get(position).personnel=reportdata.get(position+1).personnel;
				
				if (reportdata.get(position+1).personnel.size() == 0) {
					// if (ReportItemClick.personelnamesArray.size() == 0) {
					Log.d("loggy if", "if loggy" + position);
					linearlayoutPersonelList.setVisibility(View.GONE);

				} else {

					Log.d("loggy else", "else loggy" + position);
					linearlayoutPersonelList.setVisibility(View.VISIBLE);
					personelListAdapter personadapter = new personelListAdapter(
							reportdata.get(position+1).personnel, position+1);
					listviewPersonel.setAdapter(personadapter);
					setlist(listviewPersonel, personadapter, 100);
					// personadapter.notifyDataSetChanged();
				}
				
				if (reportdata.get(position+1).companies.size() == 0) {
					// if (ReportItemClick.CompaniesArray.size() == 0) {
					Log.d("loggy if", "if loggy" + position);
					linearlayoutOnsite.setVisibility(View.GONE);

				} else {

					Log.d("loggy else", "else loggy" + position);
					linearlayoutOnsite.setVisibility(View.VISIBLE);
					companyListAdapter companyadapter = new companyListAdapter(position+1);
					listviewCompany.setAdapter(companyadapter);
					// companyadapter.notifyDataSetChanged();
					setlist(listviewCompany, companyadapter, 100);
				}
//				if (reportdata.get(position-1).topic.size() == 0) {
//					// if (ReportItemClick.SafetyIDArray.size() == 0) {
//					Log.d("loggy if", "if loggy" + position);
//					safetylist.setVisibility(View.GONE);
//
//				} else {
//
//					Log.d("loggy else", "else loggy" + position);
//					safetylist.setVisibility(View.VISIBLE);
//					topicadapter = new TopicListAdapter(position-1);
//					safetylist.setAdapter(topicadapter);
//					// companyadapter.notifyDataSetChanged();
//					setlist(safetylist, topicadapter, 100);
//				}
				}
				}
			}
		});
		
		buttonPersonnel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub

				popup = new Dialog(activity,
						android.R.style.Theme_Translucent_NoTitleBar);
				// expiry_popup.setCancelable(false);

				popup.setContentView(R.layout.bridge_expiry_popup);
				// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
				RelativeLayout expiry_main = (RelativeLayout) popup
						.findViewById(R.id.relativelayoutExpiryMain);
				// expiry_main.setInAnimation(R.anim.slide_in_from_top);
				buttonEmail = (Button) popup.findViewById(R.id.buttonEmail);
				buttonCall = (Button) popup.findViewById(R.id.buttonCall);
				buttonText = (Button) popup.findViewById(R.id.buttonText);
				buttonCancel = (Button) popup.findViewById(R.id.buttonCancel);
				textviewExpiryAlert = (TextView) popup.findViewById(R.id.textviewExpiryAlert);
				// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());
				buttonEmail.setTypeface(Prefrences.helveticaNeuelt(activity));
				buttonText.setTypeface(Prefrences.helveticaNeuelt(activity));
				buttonCall.setTypeface(Prefrences.helveticaNeuelt(activity));
				buttonCancel.setTypeface(Prefrences.helveticaNeuelt(activity));

				textviewExpiryAlert.setTypeface(Prefrences
						.helveticaNeuelt(activity));

				list_outside = (RelativeLayout) popup
						.findViewById(R.id.relativelayoutExpiryMain);
				new ASSL(activity, expiry_main, 1134, 720, false);

				buttonEmail.setVisibility(View.GONE);
				buttonCall.setText("Individuals");
				buttonText.setText("Companies");
				textviewExpiryAlert.setText("Reports Personnel");

				list_outside.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (popup.isShowing()) {
							popup.dismiss();
							// overridePendingTransition(R.anim.slide_in_bottom,
							// R.anim.slide_out_to_top);
						}
					}
				});

				// Email.setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// // type.setText("Daily");
				// popup.dismiss();
				// }
				// });

				buttonCall.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// type.setText("Safety");
						Prefrences.text = 12;
						Intent intent = new Intent(activity, UsersList.class);
						activity.startActivityForResult(intent,
								ReportItemClick.PERSONNEL);
						activity.overridePendingTransition(
								R.anim.slide_in_right, R.anim.slide_out_left);
						popup.dismiss();
					}
				});

				buttonText.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// type.setText("Weekly");
						Prefrences.text = 12;
						Intent intent = new Intent(activity, CompanyList.class);
						activity.startActivityForResult(intent,
								ReportItemClick.COMPANY);
						activity.overridePendingTransition(
								R.anim.slide_in_right, R.anim.slide_out_left);
						popup.dismiss();
					}
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

		imageviewCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
				String date = sdf.format(Calendar.getInstance().getTime());
				Log.v("", "" + date);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				File photo = new File(
						Environment.getExternalStorageDirectory(), date
								+ ".jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				ReportItemClick.imageUri = Uri.fromFile(photo);
				ReportItemClick.picturePathString = photo.toString();
				activity.startActivityForResult(intent, TAKE_PICTURE);

			}
		});

		scrollview.setLayoutParams(new ListView.LayoutParams(
				ListView.LayoutParams.MATCH_PARENT,
				ListView.LayoutParams.MATCH_PARENT));
		ASSL.DoMagic(scrollview);

		if (reportdata.get(position).report_type.toString().equalsIgnoreCase(
				"safety")) {
			linearlayoutSafetyTopic.setVisibility(View.VISIBLE);
		} else {
			linearlayoutSafetyTopic.setVisibility(View.GONE);
		}

		// if(position==0){
		// ReportItemClick.textdate.setText(reportdata.get(position).created_date
		// .toString());
		// ReportItemClick.texttype.setText(reportdata.get(position).report_type
		// .toString() + " - ");
		// }else{
		// ReportItemClick.textdate.setText(reportdata.get(position-1).created_date
		// .toString());
		// ReportItemClick.texttype.setText(reportdata.get(position-1).report_type
		// .toString() + " - ");
		// }

		textviewWeather.setText("" + reportdata.get(position).weather.toString());
		buttonDate.setText("" + reportdata.get(position).created_date.toString());
		buttonType.setText("" + reportdata.get(position).report_type.toString());

		textviewWind.setText("" + reportdata.get(position).wind.toString());
		textviewTemp.setText("" + reportdata.get(position).temp.toString());
		textviewHumidity.setText("" + reportdata.get(position).humidity.toString());
		// String temp1[]=reportdata.get(position).precip.toString().split("%");
		// String s = String.format("%.2f", Float.parseFloat(temp1[0]));
		// precip.setText(s + "%");
		textviewPrecip.setText(reportdata.get(position).precip.toString());

		edittextNotes.setText("" + reportdata.get(position).body.toString());
		if (reportdata.get(position).weather_icon.toString().equalsIgnoreCase(
				"clear-day")
				|| reportdata.get(position).weather_icon.toString()
						.equalsIgnoreCase("clear-night")) {
			imageviewWeatherIcon.setBackgroundResource(R.drawable.sunny_temp);
		} else if (reportdata.get(position).weather_icon.toString()
				.equalsIgnoreCase("rain")
				|| reportdata.get(position).weather_icon.toString()
						.equalsIgnoreCase("sleet")) {
			imageviewWeatherIcon.setBackgroundResource(R.drawable.rainy_temp);
		} else if (reportdata.get(position).weather_icon.toString()
				.equalsIgnoreCase("partly-cloudy-day")
				|| reportdata.get(position).weather_icon.toString()
						.equalsIgnoreCase("partly-cloudy-night")) {
			imageviewWeatherIcon.setBackgroundResource(R.drawable.partly_temp);
		} else if (reportdata.get(position).weather_icon.toString()
				.equalsIgnoreCase("cloudy")) {
			imageviewWeatherIcon.setBackgroundResource(R.drawable.cloudy_temp);
		} else if (reportdata.get(position).weather_icon.toString()
				.equalsIgnoreCase("wind")
				|| reportdata.get(position).weather_icon.toString()
						.equalsIgnoreCase("fog")) {
			imageviewWeatherIcon.setBackgroundResource(R.drawable.wind_temp);
		} else {
			// Toast.makeText(activity, "No Image", Toast.LENGTH_SHORT).show();
			Log.d("hi no image ", "hiii no image");
		}
		// poss = position;
		// poss=position;
		arr.clear();
		ids.clear();
		desc.clear();
		linearlayoutLin2.setTag(position);

		int idx = -1;
		for (ProjectPhotos photo : reportdata.get(position).photos) {
			final ImageView ladder_page2 = new ImageView(activity);
			Log.d("----------", "position of array" + position);
			arr.add(photo.urlLarge);
			ids.add(photo.id);
			desc.add(photo.description);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					(int) (200), (int) (200));
			lp.setMargins(10, 10, 10, 10);

			ladder_page2.setTag(++idx);
			ladder_page2.setLayoutParams(lp);
			if (photo.isLocalPath) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 6;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap = BitmapFactory.decodeFile(photo.url200, options);
				ladder_page2.setImageBitmap(bitmap);

				// ladder_page2.setImageResource(R.drawable.ic_launcher);
				// File imgFile = new File(photo.url200);
				// if(imgFile.exists()){
				// Bitmap myBitmap =
				// BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				// //Drawable d = new BitmapDrawable(getResources(), myBitmap);
				// ladder_page2.setImageBitmap(myBitmap);
				// }
			} else {
				Picasso.with(activity).load(photo.url200.toString())
						.into(ladder_page2);
			}
			linearlayoutLin2.addView(ladder_page2);

			Log.d("size of ", " array  :  " + arr.size());

			ladder_page2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (reportdata.get((Integer) linearlayoutLin2.getTag()).photos.size() > 0) {
						Prefrences.selectedPic = (Integer) v.getTag();
						// poss= (Integer) rootView.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);
						arr.clear();
						ids.clear();
						desc.clear();
						for (int i = 0; i < reportdata.get((Integer) linearlayoutLin2
								.getTag()).photos.size(); i++) {
							arr.add(reportdata.get((Integer) linearlayoutLin2.getTag()).photos
									.get(i).urlLarge);
							ids.add(reportdata.get((Integer) linearlayoutLin2.getTag()).photos
									.get(i).id);
							desc.add(reportdata.get((Integer) linearlayoutLin2.getTag()).photos
									.get(i).description);
						}
						Intent in = new Intent(activity,
								SelectedImageView.class);
						in.putExtra("array", arr);
						in.putExtra("ids", ids);
						in.putExtra("desc", desc);
						// Log.d("----","logg value for viewpager"+(Integer)ladder_page2.getTag()+"array ="+arr.size()+"Prefrences.posViewpager"+Prefrences.posViewpager);
						// in.putExtra("id",
						// reportdata.get(poss).photos.get(Prefrences.selectedPic).id);
						in.putExtra("id",
								reportdata.get((Integer) linearlayoutLin2.getTag()).photos
										.get(Prefrences.selectedPic).id);
						activity.startActivity(in);
						activity.overridePendingTransition(
								R.anim.slide_in_right, R.anim.slide_out_left);
					}
				}
			});

		}

		if (reportdata.get(position).personnel.size() == 0) {
			// if (ReportItemClick.personelnamesArray.size() == 0) {
			Log.d("loggy if", "if loggy" + position);
			linearlayoutPersonelList.setVisibility(View.GONE);

		} else {

			Log.d("loggy else", "else loggy" + position);
			linearlayoutPersonelList.setVisibility(View.VISIBLE);
			personelListAdapter personadapter = new personelListAdapter(
					reportdata.get(position).personnel, position);
			listviewPersonel.setAdapter(personadapter);
			setlist(listviewPersonel, personadapter, 100);
			// personadapter.notifyDataSetChanged();
		}
		if (reportdata.get(position).companies.size() == 0) {
			// if (ReportItemClick.CompaniesArray.size() == 0) {
			Log.d("loggy if", "if loggy" + position);
			linearlayoutOnsite.setVisibility(View.GONE);

		} else {

			Log.d("loggy else", "else loggy" + position);
			linearlayoutOnsite.setVisibility(View.VISIBLE);
			companyListAdapter companyadapter = new companyListAdapter(position);
			listviewCompany.setAdapter(companyadapter);
			// companyadapter.notifyDataSetChanged();
			setlist(listviewCompany, companyadapter, 100);
		}
		if (reportdata.get(position).topic.size() == 0) {
			// if (ReportItemClick.SafetyIDArray.size() == 0) {
			Log.d("loggy if", "if loggy" + position);
			listviewSafety.setVisibility(View.GONE);

		} else {

			Log.d("loggy else", "else loggy" + position);
			listviewSafety.setVisibility(View.VISIBLE);
			topicadapter = new TopicListAdapter(position);
			listviewSafety.setAdapter(topicadapter);
			// companyadapter.notifyDataSetChanged();
			setlist(listviewSafety, topicadapter, 100);
		}

		// textdate.setText(" - "+reportdata.get(position).created_date.toString());
		// texttype.setText(""+reportdata.get(position).report_type.toString());
		((ViewPager) view).addView(rootView, 0);
		// notifyDataSetChanged();
		// notifyDataSetChanged();
		return rootView;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	public class personelListAdapter extends BaseAdapter {

		LayoutInflater inflator;
		int positionPager;

		// List<ReportPersonnel> dataList;

		public personelListAdapter(ArrayList<ReportPersonnel> personnel,
				int position) {
			inflator = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d("paersonnel adapter", "In adapter");
			// dataList = personnel;
			this.positionPager = position;

		}

		public personelListAdapter(int position) {
			inflator = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.positionPager = position;
			// ReportItemClick.personelnamesArray.clear();
			// ReportItemClick.personelIdArrayList.clear();
			// ReportItemClick.personelHoursArray.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.v("size of personel",
					"size of personel"
							+ reportdata.get(positionPager).personnel.size());
			// Log.v("size of personel",
			// "size of personel"
			// + ReportItemClick.personelnamesArray.size());
			return reportdata.get(positionPager).personnel.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public class ViewHolder3 {
			public TextView personnelname, personnelhours;
			RelativeLayout root;
			ImageView Remove;
			int position;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder3 holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.personnel_list_item,
						null);

				holder = new ViewHolder3();
				holder.root = (RelativeLayout) convertView
						.findViewById(R.id.root);
				holder.personnelname = (TextView) convertView
						.findViewById(R.id.personelname);
				holder.personnelhours = (TextView) convertView
						.findViewById(R.id.personelhours);
				holder.Remove = (ImageView) convertView
						.findViewById(R.id.remove);
				holder.personnelhours.setTypeface(Prefrences
						.helveticaNeuelt(activity));
				holder.personnelname.setTypeface(Prefrences
						.helveticaNeuelt(activity));
				// holder.Remove.setTypeface(Prefrences.helveticaNeuelt(activity));
				holder.root.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 100));
				ASSL.DoMagic(holder.root);
				/************ Set holder with LayoutInflater ************/
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder3) convertView.getTag();
			}
			holder.position = position;
			holder.personnelhours.setTag(holder);
			holder.personnelname.setTag(holder);
			holder.Remove.setTag(holder);
			holder.root.setTag(holder);
			// final RelativeLayout.LayoutParams layoutParams2 = new
			// RelativeLayout.LayoutParams(
			// ((int) (720.0f * ASSL.Xscale())),
			// ((int) (100.0f * ASSL.Yscale())));
			//
			// layoutParams2.setMargins(0, 0, 0, 0);

			holder.root.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					final ViewHolder3 holder3 = (ViewHolder3) v.getTag();
					Prefrences.removeFlag = 1;
					Log.d("Remove : ", "clicked : ");
					// Prefrences.removeCompanyID =
					if (reportdata.get(positionPager).personnel
							.get(holder3.position).id.toString().equals("")) {

					} else {
						reportdata.get(positionPager).personnel
						//
								.get(holder3.position).users.get(0).company
								.get(0).coId.toString();
						Prefrences.removeUserID = reportdata.get(positionPager).personnel
								.get(holder3.position).users.get(0).uId
								.toString();
						Prefrences.removeReportID = reportdata
								.get(positionPager).report_id.toString();
						Log.d("remooooovveeddd", "----" + holder3.position
								+ ", " + Prefrences.removeCompanyID + ", "
								+ Prefrences.removeUserID + ","
								+ Prefrences.removeReportID);
					}

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							activity);

					// set title
					alertDialogBuilder.setTitle("Please confirm");

					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Are you sure you want to remove this personnel?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int idOnclick) {
											// if this button is clicked,
											// close
											// current activity

											// holder.root.setLayoutParams(layoutParams2);
											if (reportdata.get(positionPager).personnel
													.get(holder3.position).id
													.toString().equals("")) {

											} else
												ReportItemClick.removeUsers();

											holder.root.removeView(v);
											ReportItemClick.personelnamesArrayList
													.remove(holder3.position);
											ReportItemClick.personelHoursArrayList
													.remove(holder3.position);
											ReportItemClick.personelIdArrayList
													.remove(holder3.position);
											reportdata.get(positionPager).personnel.remove(reportdata
													.get(positionPager).personnel
													.get(holder3.position));

											ViewPagerAdapter.this
													.notifyDataSetChanged();

										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int idOnclick) {
											// if this button is clicked,
											// just close
											// the dialog box and do nothing
											Log.d("Dialog", "No");
											dialog.cancel();
										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
					// ReportItemClick.removeUsers();
				}
			});

			holder.personnelname
					.setText(reportdata.get(positionPager).personnel
							.get(position).users.get(0).uFullName.toString());
			// holder.personnelname
			// .setText(ReportItemClick.personelnamesArray.get(position).toString());
			holder.personnelhours
					.setText(reportdata.get(positionPager).personnel
							.get(position).hours.toString());
			// holder.personnelhours
			// .setText(ReportItemClick.personelHoursArray.get(position).toString());
			// ReportItemClick.personelnamesArray.add(reportdata.get(positionPager).personnel
			// .get(position).users.get(0).uFullName
			// .toString());
			// ReportItemClick.personelIdArrayList.add(reportdata.get(positionPager).personnel
			// .get(position).users.get(0).uId
			// .toString());
			// ReportItemClick.personelHoursArray.add(reportdata.get(positionPager).personnel
			// .get(position).hours.toString());
			// holder.personnelname
			// .setText(ReportItemClick.personelnamesArray.get(position).toString());
			// holder.personnelhours
			// .setText(ReportItemClick.personelHoursArray.get(position).toString());

			return convertView;
		}

	}

	public class companyListAdapter extends BaseAdapter {

		LayoutInflater inflator;
		int positionPager;

		public companyListAdapter() {
			inflator = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d("company adapter", "In adapter");
		}

		public companyListAdapter(int position) {
			inflator = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.positionPager = position;
			// ReportItemClick.OnsiteArray.clear();
			// ReportItemClick.CompaniesArray.clear();
			// ReportItemClick.CompanyIdArrayList.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.v("size of personel",
					"size of personel"
							+ reportdata.get(positionPager).companies.size());
			// Log.v("size of company",
			// "size of company"
			// + ReportItemClick.CompaniesArray.size());
			return reportdata.get(positionPager).companies.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public class ViewHolder1 {
			public TextView personnelname, personnelhours;
			RelativeLayout root;
			ImageView Remove;
			int position;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder1 holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.personnel_list_item,
						null);

				holder = new ViewHolder1();
				holder.root = (RelativeLayout) convertView
						.findViewById(R.id.root);
				holder.personnelname = (TextView) convertView
						.findViewById(R.id.personelname);
				holder.personnelhours = (TextView) convertView
						.findViewById(R.id.personelhours);
				holder.Remove = (ImageView) convertView
						.findViewById(R.id.remove);
				holder.personnelhours.setTypeface(Prefrences
						.helveticaNeuelt(activity));
				holder.personnelname.setTypeface(Prefrences
						.helveticaNeuelt(activity));
				// holder.Remove.setTypeface(Prefrences.helveticaNeuelt(activity));
				holder.root.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 100));
				ASSL.DoMagic(holder.root);
				/************ Set holder with LayoutInflater ************/
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder1) convertView.getTag();
			}
			holder.personnelhours.setTag(holder);
			holder.personnelname.setTag(holder);
			holder.Remove.setTag(holder);
			holder.root.setTag(holder);
			holder.position = position;

			// Prefrences.companyID=reportdata.get(positionPager).companies
			// .get(position).coId.toString();
			holder.personnelname
					.setText(reportdata.get(positionPager).companies
							.get(position).Rcompany.get(0).coName.toString());
			// holder.personnelname
			// .setText(ReportItemClick.CompaniesArray.get(position).toString());
			// ReportItemClick.CompaniesArray.add(reportdata.get(positionPager).companies
			// .get(position).Rcompany.get(0).coName
			// .toString());
			// ReportItemClick.CompanyIdArrayList.add(reportdata.get(positionPager).companies
			// .get(position).Rcompany.get(0).coId
			// .toString());
			// ReportItemClick.OnsiteArray.add(reportdata.get(positionPager).companies
			// .get(position).coCount.toString());
			holder.personnelhours
					.setText(reportdata.get(positionPager).companies
							.get(position).coCount.toString());
			// holder.personnelhours
			// .setText(ReportItemClick.OnsiteArray.get(position).toString());

			holder.root.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					final ViewHolder1 holder3 = (ViewHolder1) v.getTag();
					Prefrences.removeFlag = 2;
					Log.d("Remove : ", "clicked : ");

					if (reportdata.get(positionPager).companies
							.get(holder3.position).coId.toString().equals("")) {

					} else {
						Prefrences.removeCompanyID = reportdata
								.get(positionPager).companies
								.get(holder3.position).Rcompany.get(0).coId
								.toString();
						// Prefrences.removeUserID =
						// reportdata.get(positionPager).companies
						// .get(holder3.position).Rcompany.get(0).coId.toString();
						Prefrences.removeReportID = reportdata
								.get(positionPager).report_id.toString();
					}
					Log.d("remooooovveeddd", "----" + holder3.position + ", "
							+ Prefrences.removeCompanyID + ", "
							+ Prefrences.removeUserID + ","
							+ Prefrences.removeReportID);

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							activity);

					// set title
					alertDialogBuilder.setTitle("Please confirm");

					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Are you sure you want to remove this company?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int idOnclick) {
											// if this button is clicked,
											// close
											// current activity

											// holder.root.setLayoutParams(layoutParams2);
											if (reportdata.get(positionPager).companies
													.get(holder3.position).coId
													.toString().equals("")) {

											} else
												ReportItemClick.removeUsers();
											holder.root.removeView(v);
											ReportItemClick.CompaniesArrayList
													.remove(holder3.position);
											ReportItemClick.CompanyIdArrayList
													.remove(holder3.position);
											ReportItemClick.OnsiteArrayList
													.remove(holder3.position);
											reportdata.get(positionPager).companies.remove(reportdata
													.get(positionPager).companies
													.get(holder3.position));
											// notifyDataSetChanged();

											ViewPagerAdapter.this
													.notifyDataSetChanged();

										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int idOnclick) {
											// if this button is clicked,
											// just close
											// the dialog box and do nothing
											Log.d("Dialog", "No");
											dialog.cancel();
										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
					// ReportItemClick.removeUsers();
				}
			});

			return convertView;
		}

	}

	public class TopicListAdapter extends BaseAdapter {

		LayoutInflater inflator;
		int positionPager;

		public TopicListAdapter() {
			inflator = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d("safety adapter", "In adapter");
		}

		public TopicListAdapter(int position) {
			inflator = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.positionPager = position;
			// ReportItemClick.OnsiteArray.clear();
			// ReportItemClick.CompaniesArray.clear();
			// ReportItemClick.CompanyIdArrayList.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.v("size of personel",
					"size of personel"
							+ reportdata.get(positionPager).topic.size());
			// Log.v("size of safety",
			// "size of safety"
			// + ReportItemClick.SafetyIDArray.size());
			return reportdata.get(positionPager).topic.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public class ViewHolder2 {
			public TextView personnelname;
			ImageView info, Remove;
			RelativeLayout root;
			int position;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder2 holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.safetylistitem, null);

				holder = new ViewHolder2();
				holder.root = (RelativeLayout) convertView
						.findViewById(R.id.root);
				holder.personnelname = (TextView) convertView
						.findViewById(R.id.personelname);
				holder.info = (ImageView) convertView.findViewById(R.id.info);
				// holder.personnelhours = (TextView) convertView
				// .findViewById(R.id.personelhours);
				holder.Remove = (ImageView) convertView
						.findViewById(R.id.remove);
				// holder.personnelhours.setTypeface(Prefrences
				// .helveticaNeuelt(activity));
				holder.personnelname.setTypeface(Prefrences
						.helveticaNeuelt(activity));
				// holder.Remove.setTypeface(Prefrences.helveticaNeuelt(activity));
				holder.root.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 100));
				ASSL.DoMagic(holder.root);
				/************ Set holder with LayoutInflater ************/
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder2) convertView.getTag();
			}
			// holder.personnelhours.setTag(holder);
			holder.personnelname.setTag(holder);
			holder.Remove.setTag(holder);
			holder.root.setTag(holder);
			holder.position = position;
			// Prefrences.companyID=reportdata.get(positionPager).topic
			// .get(position).id.toString();

			holder.personnelname.setText(reportdata.get(positionPager).topic
					.get(position).safety.get(0).Title.toString());
			// holder.personnelname
			// .setText(ReportItemClick.SafetyTitleArray.get(position).toString());
			// ReportItemClick.CompaniesArray.add(reportdata.get(positionPager).companies
			// .get(position).Rcompany.get(0).coName
			// .toString());
			// ReportItemClick.CompanyIdArrayList.add(reportdata.get(positionPager).companies
			// .get(position).Rcompany.get(0).coId
			// .toString());
			// ReportItemClick.OnsiteArray.add(reportdata.get(positionPager).companies
			// .get(position).coCount.toString());
			// holder.personnelhours
			// .setText(ReportItemClick.SafetyIDArray.get(position).toString());
			// holder.personnelhours
			// .setText(reportdata.get(positionPager).topic
			// .get(position).safety.get(0).Id
			// .toString());

			// holder.root.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(final View v) {
			// // TODO Auto-generated method stub
			// final ViewHolder2 holder3 = (ViewHolder2) v.getTag();
			// Prefrences.removeFlag=3;
			// Log.d("Remove : ","clicked : ");
			// Prefrences.removeSafetyID = reportdata.get(positionPager).topic
			// .get(holder3.position).safety.get(0).Id.toString();
			// //// Prefrences.removeUserID =
			// reportdata.get(positionPager).companies
			// //// .get(holder3.position).Rcompany.get(0).coId.toString();
			// // Prefrences.removeReportID =
			// reportdata.get(positionPager).report_id.toString();
			// Log.d("remooooovveeddd","----"+holder3.position+", "+Prefrences.removeCompanyID+", "+Prefrences.removeUserID+","+Prefrences.removeReportID);
			//
			// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			// activity);
			//
			// // set title
			// alertDialogBuilder.setTitle("Please confirm");
			//
			// // set dialog message
			// alertDialogBuilder
			// .setMessage(
			// "Are you sure you want to remove this safety topic?")
			// .setCancelable(false)
			// .setPositiveButton("Yes",
			// new DialogInterface.OnClickListener() {
			// public void onClick(
			// DialogInterface dialog,
			// int idOnclick) {
			// // if this button is clicked,
			// // close
			// // current activity
			//
			//
			// // holder.root.setLayoutParams(layoutParams2);
			// ReportItemClick.removeUsers();
			// holder.root.removeView(v);
			// ReportItemClick.SafetyIDArray.remove(holder3.position);
			// ReportItemClick.SafetyTitleArray.remove(holder3.position);
			// // ReportItemClick.OnsiteArray.remove(holder3.position);
			// notifyDataSetChanged();
			// // setlist(safetylist, topicadapter, 100);
			// }
			// })
			// .setNegativeButton("No",
			// new DialogInterface.OnClickListener() {
			// public void onClick(
			// DialogInterface dialog,
			// int idOnclick) {
			// // if this button is clicked,
			// // just close
			// // the dialog box and do nothing
			// Log.d("Dialog", "No");
			// dialog.cancel();
			// }
			// });
			//
			// // create alert dialog
			// AlertDialog alertDialog = alertDialogBuilder.create();
			//
			// // show it
			// alertDialog.show();
			// // ReportItemClick.removeUsers();
			// }
			// });

			return convertView;
		}

	}

	public void setlist(ListView lv, ListAdapter listAdapter, int size) {

		listAdapter = lv.getAdapter();

		// int totalHeight = listAdapter.getCount() * imageWidth;
		int totalHeight = 0;
		View listItem = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			listItem = listAdapter.getView(i, listItem, lv);
			if (listItem instanceof ViewGroup)
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			listItem.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += (int) (size * ASSL.Yscale());

			// totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = lv.getLayoutParams();

		params.height = totalHeight
				+ (lv.getDividerHeight() * (listAdapter.getCount() - 1));

		lv.setLayoutParams(params);
		lv.requestLayout();
	}

	public void safetyTopic(final int position) {

		Prefrences.showLoadingDialog(activity, "Loading...");

		RequestParams params = new RequestParams();

		params.put("user_id", Prefrences.userId);

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(100000);
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		// client.get()
		client.get(activity, Prefrences.url + "/reports/options", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
							safetyArray = new ArrayList<SafetyTopics>();
							JSONArray current = res
									.getJSONArray("possible_topics");
							// //
							// winds,temps,precips,humiditys,bodys,icons,summarys
							String id, title, info;
							for (int i = 0; i < current.length(); i++) {

								JSONObject obj = current.getJSONObject(i);

								id = obj.getString("id");
								title = obj.getString("title");
								info = obj.getString("info");

								safetyArray.add(new SafetyTopics(id, title,
										info));

							}

							Log.d("Safety", "size = " + safetyArray.size());
							dialog = new Dialog(
									activity,
									android.R.style.Theme_Translucent_NoTitleBar);
							// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.dialogbox_list);
							ArrayList<SafetyTopics> array = new ArrayList<SafetyTopics>();

							Window window = dialog.getWindow();
							WindowManager.LayoutParams wlp = window
									.getAttributes();

							wlp.gravity = Gravity.CENTER;
							wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
							// wlp.width &=
							// ~WindowManager.LayoutParams.MATCH_PARENT;
							window.setAttributes(wlp);
							array = safetyArray;
							// dialog.setTitle("Location");
							TextView txtwho = (TextView) dialog
									.findViewById(R.id.txt_who);
							txtwho.setText("Location");
							LinearLayout linearLay = (LinearLayout) dialog
									.findViewById(R.id.db);
							RelativeLayout list_outside = (RelativeLayout) dialog
									.findViewById(R.id.list_outside);

							// db.setBackgroundColor(drawable.transparent_image);
							new ASSL(activity, linearLay, 1134, 720, false);
							Button cancel = (Button) dialog
									.findViewById(R.id.cancel);

							ListView dialoglist = (ListView) dialog
									.findViewById(R.id.dialoglist);

							// Button btn_cancel = (Button)dialog.
							// findViewById(R.id.cancel);

							adapter1 adp = new adapter1(activity, array,
									position);
							dialoglist.setAdapter(adp);

							cancel.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

									dialog.dismiss();
								}
							});

							list_outside
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub

											dialog.dismiss();
										}
									});

							dialog.show();

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

	public class adapter1 extends BaseAdapter {

		ArrayList<SafetyTopics> array;
		// ArrayList<Users>usr;
		Context con;
		int pagerPosition;

		public adapter1(Context con, ArrayList<SafetyTopics> array,
				int pagerPosition) {
			// TODO Auto-generated constructor stub
			this.array = array;
			this.con = con;
			this.pagerPosition = pagerPosition;
			// this.usr=usrs;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// Log.d("userlist","Size========="+array.size());
			// if()
			return array.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return array.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			viewholder holder;
			// final Users body = (Users) this.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new viewholder();
				// LayoutInflater inflater =
				// ((Activity)con).getLayoutInflater();
				LayoutInflater inflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.dialog2list_item, null);
				holder.linear = (LinearLayout) convertView
						.findViewById(R.id.dialoglinear);
				holder.linear.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 80));
				ASSL.DoMagic(holder.linear);
				convertView.setTag(holder);
			} else {
				holder = (viewholder) convertView.getTag();
			}

			holder.txtview = (TextView) convertView.findViewById(R.id.array);
			holder.txtview.setTypeface(Prefrences.helveticaNeuelt(activity));
			holder.txtview.setText(array.get(position).Title.toString());

			holder.txtview.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub

					
					String safetyTopicId ="";
					for (int i = 0; i < reportdata.get(pagerPosition).topic.size(); i++) 
					{
						if (array.get(position).Id.toString().equals(reportdata.get(pagerPosition).topic.get(i).safety.get(0).Id.toString()))
						{
							Log.d("Safety ID","Safety ID"+array.get(position).Id.toString());
							safetyTopicId = array.get(position).Id.toString();
						}
					}
					if (safetyTopicId.toString().equals("")) 
					{
						arraysafety.add(array.get(position));
						ArrayList<SafetyTopics> safetyTopics = new ArrayList<SafetyTopics>();
						safetyTopics.add(new SafetyTopics(
								array.get(position).Id.toString(), array
										.get(position).Title.toString(), ""));
						reportdata.get(pagerPosition).topic
								.add(new ReportTopics("", "", safetyTopics));
						ViewPagerAdapter.this.notifyDataSetChanged();

						// Prefrences.stopingHit=1;
						ReportItemClick.SafetyTitleArrayList.add(array
								.get(position).Title.toString());
						ReportItemClick.SafetyIDArrayList.add(array.get(position).Id
								.toString());
//						safetylist.setVisibility(View.VISIBLE);
					}
					// topicadapter = new TopicListAdapter();
					// safetyadapter adp = new safetyadapter();
					// safetyListView.setAdapter(adp);
					// setlist(safetyListView, adp, 100);
					// Prefrences.stopingHit=1;
					// notifyDataSetChanged();
					// topicadapter = new TopicListAdapter();
					// safetylist.setAdapter(topicadapter);
					// setlist(safetylist, topicadapter, 100);
					dialog.dismiss();

				}

			});
			return convertView;
		}

	}

	private static class viewholder {
		TextView txtview;
		LinearLayout linear;
	}

	// public class ViewHolder {
	// public TextView text;
	// public CheckBox checkbox;
	// public ImageView imageview;
	// public ViewHolder(View v) {
	// this.text = (TextView)v.findViewById(R.id.text1);
	// this.checkbox = (CheckBox)v.findViewById(R.id.cbx1);
	// this.imageview = (ImageView)v.findViewById(R.id.image1);
	// }
	//
	// }

}
