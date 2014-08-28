package com.buildhawk;

/*
 *  This file is used to create new report for the selected project.
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.CheckItemClick.ViewHolder;
import com.buildhawk.ReportItemClick.LongOperation;
import com.buildhawk.WorklistFragment.adapter;
import com.buildhawk.utils.PunchListsItem;
import com.buildhawk.utils.SafetyTopics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

public class ReportItemCreate extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
	private static int PICK_CONTACT = 1;
	public String reportIdString;
	String windsString, tempsHighString, precipsString, humiditysString, tempsLowString, iconsString, summarysString;
	TextView textviewWind, textviewTemp, textviewPrecip, textviewHumidity, textviewWeather;
	
	TextView textviewType, textviewDate;
	ImageView imageviewWeatherIcon;
	
	Button buttonType, buttonDate, buttonPersonnel, buttonSafetytopic;
	Button buttonSave, buttonBack;
	RelativeLayout relativelayoutRoot;
	int year, month, day;
	Dialog popup;
	Button buttonEmail, buttonText, buttonCall, buttonCancel;
	TextView textviewExpiryAlert;
	RelativeLayout relativelayoutListOutside;
	private Uri imageUri;
	LinearLayout linearlayoutLin2;
	String picturePathString = "";
	Boolean isInternetPresent = false;
	ConnectionDetector connDect;
	EditText edittextNotes;
	ListView safetyListView;
	
	public ArrayList<SafetyTopics>arraysafetyArrayList= new ArrayList<SafetyTopics>();
	public ArrayList<SafetyTopics>safetyArrayList;
	public static ArrayList<String> personelnamesArray = new ArrayList<String>();
	public static ArrayList<String> personelIdArrayList = new ArrayList<String>();
	public static ArrayList<String> personelHoursArray = new ArrayList<String>();
	public static ArrayList<String> CompaniesArray = new ArrayList<String>();
	public static ArrayList<String> OnsiteArray = new ArrayList<String>();
	public static ArrayList<String> CompanyIdArrayList = new ArrayList<String>();

	Dialog dialog;
	ListView personnelListView, companyListView;
	Activity activity;
	String currentDateandTimeString;
	LinearLayout linearlayoutPersonel, linearlayoutOnsite, linearlayoutSafetytopic,
			linearlayoutWeather;
	ScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_item_create);

		relativelayoutRoot = (RelativeLayout) findViewById(R.id.relativelayoutRootcreateitem);
		new ASSL(this, relativelayoutRoot, 1134, 720, false);
		scrollView = (ScrollView) findViewById(R.id.pullToRefreshScrollView);
		scrollView.smoothScrollTo(0, 0);
		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresent = connDect.isConnectingToInternet();
		activity = ReportItemCreate.this;
		linearlayoutPersonel = (LinearLayout) findViewById(R.id.linearlayoutPersonel);
		linearlayoutOnsite = (LinearLayout) findViewById(R.id.linearlayoutOnsite);
		linearlayoutSafetytopic = (LinearLayout) findViewById(R.id.linearlayoutSafetytopic);
		linearlayoutWeather = (LinearLayout) findViewById(R.id.linearlayoutWeather);
		buttonBack = (Button) findViewById(R.id.buttonBack);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		ImageView imageviewCamera = (ImageView) findViewById(R.id.imageviewCamera);
		ImageView imageviewGallery = (ImageView) findViewById(R.id.imageviewGallery);
		linearlayoutLin2 = (LinearLayout) findViewById(R.id.linearlayoutLin2);
		buttonDate = (Button) findViewById(R.id.buttonDate);
		buttonType = (Button) findViewById(R.id.buttonType);
		buttonPersonnel = (Button) findViewById(R.id.buttonPersonnel);
		buttonSafetytopic = (Button) findViewById(R.id.buttonSafetytopic);
		textviewWind = (TextView) findViewById(R.id.textviewWind);
		textviewTemp = (TextView) findViewById(R.id.textviewTemp);
		textviewPrecip = (TextView) findViewById(R.id.textviewPrecip);
		textviewHumidity = (TextView) findViewById(R.id.textviewHumidity);
		textviewWeather = (TextView) findViewById(R.id.textviewWeather);
		imageviewWeatherIcon = (ImageView) findViewById(R.id.imageviewWeatherIcon);
		edittextNotes = (EditText) findViewById(R.id.edittextNotes);
		personnelListView = (ListView) findViewById(R.id.personellist);
		safetyListView=(ListView)findViewById(R.id.safetylist);
		companyListView = (ListView) findViewById(R.id.companieslist);

		textviewDate = (TextView) findViewById(R.id.textviewDate);
		textviewType = (TextView) findViewById(R.id.textviewType);

		textviewWind.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		textviewTemp.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		textviewPrecip.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		textviewHumidity.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		textviewWeather.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));

		textviewType.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));
		textviewDate.setTypeface(Prefrences
				.helveticaNeuebd(getApplicationContext()));

		buttonType.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		buttonDate.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		buttonPersonnel.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		buttonSafetytopic.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		buttonSave.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		buttonBack.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));

		edittextNotes.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		
		CompaniesArray.clear();
		CompanyIdArrayList.clear();
		OnsiteArray.clear();
		personelHoursArray.clear();
		personelnamesArray.clear();
		personelIdArrayList.clear();
		arraysafetyArrayList.clear();
//		SafetyIDArray.clear();
//		SafetyTitleArray.clear();
		

		if (Prefrences.selecteddate == "") {
			Log.d("if if if ", "if if if ");
			if (Prefrences.reportTypeDialog == 1) {
				buttonType.setText("Daily");
				linearlayoutWeather.setVisibility(View.VISIBLE);
				linearlayoutSafetytopic.setVisibility(View.GONE);
				buttonType.setEnabled(true);

			} else if (Prefrences.reportTypeDialog == 2) {
				buttonType.setText("Safety");
				linearlayoutSafetytopic.setVisibility(View.VISIBLE);
				linearlayoutWeather.setVisibility(View.GONE);
				buttonType.setEnabled(true);
			} else if (Prefrences.reportTypeDialog == 3) {
				buttonType.setText("Weekly");
				linearlayoutWeather.setVisibility(View.GONE);
				linearlayoutSafetytopic.setVisibility(View.GONE);
				buttonType.setEnabled(true);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			currentDateandTimeString = sdf.format(new Date());

			textviewDate.setText(" - " + currentDateandTimeString);
			textviewType.setText(buttonType.getText().toString());
		} else {
			Log.d("else else", "else else else");
			buttonType.setText("Daily");
			linearlayoutWeather.setVisibility(View.VISIBLE);
			linearlayoutSafetytopic.setVisibility(View.GONE);
			buttonType.setEnabled(true);
			textviewDate.setText(" - " + Prefrences.selecteddate);
			textviewType.setText(buttonType.getText().toString());
			// date.setText(""+Prefrences.selecteddate);
			// date.setText("Hello...");
			// date.setBackgroundColor(Color.WHITE);
			// date.setTextColor(Color.BLACK);
			Log.d("date button", "date button" + buttonDate.getText().toString()
					+ ", prefrencedate" + Prefrences.selecteddate);
		}

		buttonDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});

		buttonType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				popup = new Dialog(ReportItemCreate.this,
						android.R.style.Theme_Translucent_NoTitleBar);
				// expiry_popup.setCancelable(false);

				popup.setContentView(R.layout.bridge_expiry_popup);
				buttonEmail = (Button) popup.findViewById(R.id.buttonEmail);
				buttonCall = (Button) popup.findViewById(R.id.buttonCall);
				buttonText = (Button) popup.findViewById(R.id.buttonText);
				buttonCancel = (Button) popup.findViewById(R.id.buttonCancel);
				textviewExpiryAlert = (TextView) popup.findViewById(R.id.textviewExpiryAlert);
				// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
				RelativeLayout relativelayoutExpiryMain = (RelativeLayout) popup
						.findViewById(R.id.relativelayoutExpiryMain);
				
//				buttonEmail = (Button) popup.findViewById(R.id.buttonEmail);
//				buttonCall = (Button) popup.findViewById(R.id.buttonCall);
//				buttonText = (Button) popup.findViewById(R.id.buttonText);
//				buttonCancel = (Button) popup.findViewById(R.id.buttonCancel);
//				textviewExpiryAlert = (TextView) popup.findViewById(R.id.textviewExpiryAlert);
				// expiry_main.setInAnimation(R.anim.slide_in_from_top);

				// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());
				buttonEmail.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonText.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonCall.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonCancel.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				textviewExpiryAlert.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				relativelayoutListOutside = (RelativeLayout) popup
						.findViewById(R.id.relativelayoutExpiryMain);
				new ASSL(ReportItemCreate.this, relativelayoutExpiryMain, 1134, 720, false);

				buttonEmail.setText("Daily");
				buttonCall.setText("Safety");
				buttonText.setText("Weekly");
				textviewExpiryAlert.setText("Reports");

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

				buttonEmail.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						buttonType.setText("Daily");
						textviewType.setText("Daily");
						linearlayoutWeather.setVisibility(View.VISIBLE);
						linearlayoutSafetytopic.setVisibility(View.GONE);
						popup.dismiss();
					}
				});

				buttonCall.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						buttonType.setText("Safety");
						textviewType.setText("Safety");
						linearlayoutSafetytopic.setVisibility(View.VISIBLE);
						linearlayoutWeather.setVisibility(View.GONE);
						popup.dismiss();
					}
				});

				buttonText.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						buttonType.setText("Weekly");
						textviewType.setText("Weekly");
						linearlayoutWeather.setVisibility(View.GONE);
						linearlayoutSafetytopic.setVisibility(View.GONE);
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

		buttonSafetytopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub addtopic ???????????

				safetyTopic();

				
			}
		});

		buttonPersonnel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				popup = new Dialog(ReportItemCreate.this,
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
				
//				buttonEmail = (Button) popup.findViewById(R.id.buttonEmail);
//				buttonCall = (Button) popup.findViewById(R.id.buttonCall);
//				buttonText = (Button) popup.findViewById(R.id.buttonText);
//				buttonCancel = (Button) popup.findViewById(R.id.buttonCancel);
//				textviewExpiryAlert = (TextView) popup.findViewById(R.id.textviewExpiryAlert);
				// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());
				buttonEmail.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonText.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonCall.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				buttonCancel.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				textviewExpiryAlert.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				relativelayoutListOutside = (RelativeLayout) popup
						.findViewById(R.id.relativelayoutExpiryMain);
				new ASSL(ReportItemCreate.this, relativelayoutExpiryMain, 1134, 720, false);

				buttonEmail.setVisibility(View.GONE);
				buttonCall.setText("Individuals");
				buttonText.setText("Companies");
				textviewExpiryAlert.setText("Reports Personnel");

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
						Prefrences.text=11;
						// type.setText("Safety");
						Intent intent = new Intent(ReportItemCreate.this,
								UsersList.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						popup.dismiss();
					}
				});

				buttonText.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// type.setText("Weekly");
						Prefrences.text=11;
						Intent intent = new Intent(ReportItemCreate.this,
								CompanyList.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
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

		imageviewGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});

		imageviewCamera.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
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
				picturePathString = photo.toString();
				imageUri = Uri.fromFile(photo);
				startActivityForResult(intent, TAKE_PICTURE);
			}
		});

		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Prefrences.selecteddate = "";
				Prefrences.reportTypeDialog = 0;
				personelHoursArray.clear();
				personelIdArrayList.clear();
				personelnamesArray.clear();
				CompaniesArray.clear();
				CompanyIdArrayList.clear();
				OnsiteArray.clear();
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(activity.getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

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
				if (isInternetPresent) {
					// Prefrences.stopingHit = 1;
					if (buttonSave.getText().equals("Create")) {
						createReportHit();
//						ReportFragment.fromCreateItem=true;
//						finish();

					} else {
						updateReportHit(activity);
					}
					// Toast.makeText(getApplicationContext(),
					// "Save is under construction", Toast.LENGTH_SHORT).show();
					// finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"No internet connection.", Toast.LENGTH_SHORT)
							.show();
				}
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(activity.getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}

			}
		});

		if (isInternetPresent) {
			weathertHit();

		} else {
			Toast.makeText(getApplicationContext(), "No internet connection.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (Prefrences.resumeflag == 1) {
			if (personelnamesArray.size() != 0) {
				linearlayoutPersonel.setVisibility(View.VISIBLE);
				personelListAdapterCreate personadapter = new personelListAdapterCreate();
				personnelListView.setAdapter(personadapter);
				setlist(personnelListView, personadapter, 100);
			} else {
				linearlayoutPersonel.setVisibility(View.GONE);
			}
			if (CompaniesArray.size() != 0) {
				linearlayoutOnsite.setVisibility(View.VISIBLE);
				companiesListAdapterCreate companyadapter = new companiesListAdapterCreate();
				companyListView.setAdapter(companyadapter);
				setlist(companyListView, companyadapter, 100);
			} else {
				linearlayoutOnsite.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Prefrences.selecteddate = "";
		Prefrences.reportTypeDialog = 0;
		personelHoursArray.clear();
		personelnamesArray.clear();
		CompaniesArray.clear();
		CompanyIdArrayList.clear();
		OnsiteArray.clear();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public class personelListAdapterCreate extends BaseAdapter {

		LayoutInflater inflator;

		public personelListAdapterCreate() {
			inflator = (LayoutInflater) ReportItemCreate.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d("paersonnel adapter", "In adapter");
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// Log.v("size of personel","size of personel"+reportdata.get(ret).personnel.size());
			// return Prefrences.personelName.length;
			return personelnamesArray.size();
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

		public class ViewHolder {
			public TextView personnelname, personnelhours;
			// int pos;
			ImageView Remove;
			RelativeLayout root, relatlay;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.personnel_list_item,
						null);

				holder = new ViewHolder();
				holder.root = (RelativeLayout) convertView
						.findViewById(R.id.root);
				holder.personnelname = (TextView) convertView
						.findViewById(R.id.personelname);
				holder.personnelhours = (TextView) convertView
						.findViewById(R.id.personelhours);
				holder.Remove = (ImageView) convertView
						.findViewById(R.id.remove);
				holder.relatlay = (RelativeLayout) convertView
						.findViewById(R.id.relatlay);
//				holder.Remove.setTypeface(Prefrences
//						.helveticaNeuelt(getApplicationContext()));
				holder.personnelhours.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				holder.personnelname.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				holder.root.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 100));
				ASSL.DoMagic(holder.root);
				/************ Set holder with LayoutInflater ************/
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}
			// holder.personnelhours.setTag(holder);
			// holder.personnelname.setTag(holder);
			// holder.Remove.setTag(holder);
			// holder.relatlay.setTag(holder);
			// Log.d("Loggy","Loggy"+reportdata.get(ret).personnel.get(position).users.get(0).uFullName.toString());

			// holder.personnelname.setText(Prefrences.personelName[position].toString());
			// holder.personnelhours.setText(Prefrences.personelHours[position].toString());
			if (!personelHoursArray.get(position).isEmpty()) {
				holder.personnelname.setText(personelnamesArray.get(position)
						.toString());
				holder.personnelhours.setText(personelHoursArray.get(position)
						.toString());
			} else {
				holder.personnelname.setText(personelnamesArray.get(position)
						.toString());
				holder.personnelhours.setText("");
			}
//			holder.Remove.setText("Remove");
			holder.Remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ViewHolder holder2 = (ViewHolder) v.getTag();
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
											if(personelHoursArray.size()==1)
												linearlayoutPersonel.setVisibility(View.GONE);
											personelHoursArray.remove(position);
											personelIdArrayList.remove(position);
											personelnamesArray.remove(position);
											personelListAdapterCreate.this.notifyDataSetChanged();

//											holder.relatlay.removeView(v);

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
					
					// holder.relatlay.setTag(holder);
				}
			});

			return convertView;
		}

	}

	public class companiesListAdapterCreate extends BaseAdapter {

		LayoutInflater inflator;

		public companiesListAdapterCreate() {
			inflator = (LayoutInflater) ReportItemCreate.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d("paersonnel adapter", "In adapter");
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// Log.v("size of personel","size of personel"+reportdata.get(ret).personnel.size());
			// return Prefrences.personelName.length;
			return CompaniesArray.size();
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

		public class ViewHolder {
			public TextView personnelname, personnelhours;
			ImageView Remove;
			RelativeLayout root, relatlay;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.personnel_list_item,
						null);

				holder = new ViewHolder();
				holder.root = (RelativeLayout) convertView
						.findViewById(R.id.root);
				holder.personnelname = (TextView) convertView
						.findViewById(R.id.personelname);
				holder.personnelhours = (TextView) convertView
						.findViewById(R.id.personelhours);
				holder.Remove = (ImageView) convertView
						.findViewById(R.id.remove);
				holder.relatlay = (RelativeLayout) convertView
						.findViewById(R.id.relatlay);

//				holder.Remove.setTypeface(Prefrences
//						.helveticaNeuelt(getApplicationContext()));
				holder.personnelhours.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				holder.personnelname.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				holder.root.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 100));
				ASSL.DoMagic(holder.root);
				/************ Set holder with LayoutInflater ************/
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.personnelhours.setTag(holder);
			holder.personnelname.setTag(holder);
			holder.Remove.setTag(holder);
			holder.relatlay.setTag(holder);
			
			if (!CompaniesArray.get(position).isEmpty()) {
				holder.personnelname.setText(CompaniesArray.get(position)
						.toString());
				holder.personnelhours.setText(OnsiteArray.get(position)
						.toString());
			} else {
				holder.personnelname.setText(CompaniesArray.get(position)
						.toString());
				holder.personnelhours.setText("");
			}

//			holder.Remove.setText("Remove");
			holder.Remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ViewHolder holder2 = (ViewHolder) v.getTag();
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
											if(CompaniesArray.size()==1)
												linearlayoutOnsite.setVisibility(View.GONE);
											CompaniesArray.remove(position);
											OnsiteArray.remove(position);
											CompanyIdArrayList.remove(position);
											companiesListAdapterCreate.this.notifyDataSetChanged();

//											holder.relatlay.removeView(v);

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
					
					// holder.relatlay.setTag(holder);
				}
			});

			return convertView;
		}

	}

	public void setlist(ListView listview, ListAdapter listAdapter, int size) {
		int maxHeight = 0;
		listAdapter = listview.getAdapter();

		// int totalHeight = listAdapter.getCount() * imageWidth;
		int totalHeight = 0;
		View listItem = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {

			listItem = listAdapter.getView(i, listItem, listview);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += (int) (size * ASSL.Yscale());

			// totalHeight += listItem.getMeasuredHeight();
		}
		maxHeight += totalHeight;
		ViewGroup.LayoutParams params = listview.getLayoutParams();

		params.height = totalHeight
				+ (listview.getDividerHeight() * (listAdapter.getCount() - 1));

		listview.setLayoutParams(params);
		listview.requestLayout();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

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

			ImageView ladder_page2 = null;
			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
			ladder_page2 = new ImageView(this);

			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					(int) (200), (int) (200));
			layoutParam.setMargins(10, 10, 10, 10);
			ladder_page2.setLayoutParams(layoutParam);
			File file = new File(picturePathString);
			 Picasso.with(ReportItemCreate.this).load(file)
			 .resize((int) (200 * ASSL.Xscale()),
					 (int) (200 * ASSL.Xscale()))
			 .into(ladder_page2);
			// // ladder_page2.setImageBitmap(myBitmap);
			linearlayoutLin2.addView(ladder_page2);
			// }

			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
//			ladder_page2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			// upload();
			// commentAdd();

		}

		else if (requestCode == TAKE_PICTURE
				&& resultCode == Activity.RESULT_OK) {
			Uri selectedImage = imageUri;
			getContentResolver().notifyChange(selectedImage, null);
			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			ImageView ladder_page2 = null;

			ladder_page2 = new ImageView(this);

			ContentResolver cr = getContentResolver();
			Bitmap bitmap;
			try {
				bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
						selectedImage);

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						(int) (200), (int) (200));
				lp.setMargins(10, 10, 10, 10);
				ladder_page2.setLayoutParams(lp);
//				picturePath=selectedImage.toString();
				File file = new File(picturePathString);

				Picasso.with(ReportItemCreate.this)
						.load(file)
						.resize((int) (200 * ASSL.Xscale()),
								(int) (200 * ASSL.Xscale())).centerCrop()
						.into(ladder_page2);
				ladder_page2.setImageBitmap(bitmap);
				linearlayoutLin2.addView(ladder_page2);

				ladder_page2.setImageBitmap(bitmap);

			} catch (Exception e) {
				Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
						.show();
				Log.e("Camera", e.toString());
			}
		}
	}

	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		private String new_month, new_day;

		@Override
		public void onDateSet(DatePicker view, int yr, int monthOfYear,
				int dayOfMonth) {
			year = yr;
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

			buttonDate.setText(final_date);
		}
	};

	// protected Dialog onCreateDialog() {
	//
	// Log.v("year month day",""+year+" "+month+" "+day);
	// return new DatePickerDialog(this, dateListener, year, month, day);
	//
	// }
	protected Dialog onCreateDialog(int id) {

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

					dialog.dismiss();

					dialog.cancel();

				}

			}

		});

		return datepiker;

	}

	
	// ************ API for Create Report. *************//
	
	void createReportHit() {

		Prefrences.showLoadingDialog(ReportItemCreate.this, "Loading...");
		JSONObject jsonobj = new JSONObject();

		// JSONObject jo2 =new JSONObject();
		JSONArray jsonPersonnel1 = new JSONArray();
		JSONArray jsonCompany = new JSONArray();
		JSONArray jsonSafety = new JSONArray();
		// Òreport_companiesÓ : [
		// {
		// ÒnameÓ : ÒXYZ LocksmithÓ,
		// Òcompany_idÓ : Ò12Ó,
		// ÒcountÓ : 2
		// },
		// more report_companies

		// jo1= jo.getJSONArray(currentDateandTime) ;
		try {
			for (int i = 0; i < personelnamesArray.size(); i++) {
				JSONObject jo3 = new JSONObject();
				Log.i("name ", "" + personelnamesArray.get(i).toString());
				jo3.put("user_id", personelIdArrayList.get(i).toString());
				jo3.put("full_name", personelnamesArray.get(i).toString());
				jo3.put("hours", personelHoursArray.get(i).toString());
				jsonPersonnel1.put(jo3);
			}

			for (int j = 0; j < CompaniesArray.size(); j++) {
				JSONObject jo4 = new JSONObject();
				Log.i("id ", "" + CompanyIdArrayList.get(j).toString());
				jo4.put("company_id", CompanyIdArrayList.get(j).toString());
				jo4.put("name", CompaniesArray.get(j).toString());
				jo4.put("count", OnsiteArray.get(j).toString());
				jsonCompany.put(jo4);
			}

			for (int j = 0; j < arraysafetyArrayList.size(); j++) {
				JSONObject jo5 = new JSONObject();
//				Log.i("id ", "" + arraysafety.get(j).toString());
				jo5.put("id", arraysafetyArrayList.get(j).Id.toString());
				jo5.put("title", arraysafetyArrayList.get(j).Title.toString());
				jo5.put("info", arraysafetyArrayList.get(j).Info.toString());
				jsonSafety.put(jo5);
			}
			
			 Log.d("arraysafety", "size" + arraysafetyArrayList.size());
			// try {
			jsonobj.put("author_id", Prefrences.userId);

			jsonobj.put("created_date", buttonDate.getText().toString());
			jsonobj.put("humidity", textviewHumidity.getText().toString());
			jsonobj.put("precip", textviewPrecip.getText().toString());
			jsonobj.put("project_id", Prefrences.selectedProId);
			jsonobj.put("report_type", buttonType.getText().toString());
			jsonobj.put("temp", textviewTemp.getText().toString());
			jsonobj.put("weather", textviewWeather.getText().toString());
			jsonobj.put("weather_icon", iconsString);
			jsonobj.put("wind", textviewWind.getText().toString());
			jsonobj.put("body", edittextNotes.getText().toString());
			jsonobj.put("report_users", jsonPersonnel1);
			jsonobj.put("report_companies", jsonCompany);
			jsonobj.put("safety_topics", jsonSafety );
			jsonobj.put("mobile", "1");

			JSONObject finalJson = new JSONObject();
			finalJson.put("report", jsonobj);
			// jo.put("", Prefrences.personelHours.toString());
			// Gson gson = new Gson(); String out = gson.toJson(array);
			// params.put("descr_array", out );

			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
					.getBytes("UTF-8"));

			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			client.post(getApplicationContext(), Prefrences.url + "/reports/",
					entity, "application/json", new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							JSONObject res = null;
							try {
								res = new JSONObject(response);
								Log.v("response ---- ",
										"---*****----" + res.toString(2));
//								JSONObject duplicate = res.getJSONObject("duplicate");
//
//								duplicate.getString("")
								
//								if(res.toString().equalsIgnoreCase("duplicate": "06\/25\/2014")))
//								{
//									Toast.makeText(getApplicationContext(), "Duplication",  Toast.LENGTH_LONG).show();
//								}
								if(res.has("duplicate"))
								{
									Prefrences.dismissLoadingDialog();
									Log.d("duplicate","duplicate");
									Toast.makeText(
											activity,
											"Sorry, you can create only one Daily report in a day",
											50000).show();
									finish();
								}
								else
								{
								 JSONObject report =
								 res.getJSONObject("report");
								
								 String reportId = report.getString("id");
								
								 reportId = report.getString("id");
								 Prefrences.reportID = "" + reportId;
								 Log.d("reportID",
								 "reportID" + reportId.toString());
								if (!picturePathString.equals("")) {
									Log.d("if Picture ","if Picture ");
									new LongOperation().execute("");
								} else {
									Prefrences.dismissLoadingDialog();
									Log.d("else Picture ","else Picture ");
									AlertMessage();
								}
								if (buttonSave.getText().equals("Create")) {
									buttonSave.setText("Save");
									DateFormat dateFormat = new SimpleDateFormat(
											"yyyy/MM/dd");
									// get current date time with Date()
									Date date = new Date();
									if (buttonType.getText().toString()
											.equalsIgnoreCase("Daily")) {
										Prefrences.saveDateDaily(dateFormat
												.format(date).toString(),
												getApplicationContext(),buttonType.getText().toString(),Prefrences.selectedProId);
									}
									 else if (buttonType.getText().toString()
											.equalsIgnoreCase("Safety")) {
										Prefrences.saveDateSafety(dateFormat
												.format(date).toString(),
												getApplicationContext(),buttonType.getText().toString(),Prefrences.selectedProId);
									}
									 else if (buttonType.getText().toString()
												.equalsIgnoreCase("Weekly")) {
											Prefrences.saveDateWeekly(dateFormat
													.format(date).toString(),
													getApplicationContext(),buttonType.getText().toString(),Prefrences.selectedProId);
										}
								}
								ReportFragment.fromCreateItem=true;
								Prefrences.stopingHit=1;
								Prefrences.report_bool=false;
//								if(Prefrences.reportTypeDialog==1)
//									Prefrences.reportlisttypes=1;
//								else if(Prefrences.reportTypeDialog==2)
//									Prefrences.reportlisttypes=2;
//								else if(Prefrences.reportTypeDialog==3)
//									Prefrences.reportlisttypes=3;
//								finish();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							// WorkListCompleteAdapter.punchlst_item(id_punchlist_item,
							// WorkListCompleteAdapter.data, pos);

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

	
	// ************ API for Weather. *************//
	void weathertHit() {
		// lati 30.7187527 longi 76.8102047

		// Prefrences.showLoadingDialog(WorkItemClick.this, "Loading...");

		Prefrences.showLoadingDialog(ReportItemCreate.this, "Loading...");

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

							JSONObject daily = res.getJSONObject("daily");
							
							
							JSONArray data = daily.getJSONArray("data");
							
//							for (int i = 0; i < data.length(); i++) {

								
								JSONObject count = data.getJSONObject(1);
							
							// winds,temps,precips,humiditys,bodys,icons,summarys
							windsString = count.getString("windSpeed");
							tempsHighString = count.getString("temperatureMax");
							tempsLowString = count.getString("temperatureMin");

							precipsString = count.getString("precipProbability");
							double pre = Double.parseDouble(precipsString);
							
							pre = pre * 100;
							precipsString = Double.toString(pre);
//							precips= String.format("%.1f", precips);
							// precips=precips.spl

							humiditysString = count.getString("humidity");
							double hum = Double.parseDouble(humiditysString);
							hum = hum * 100;
							humiditysString = Double.toString(hum);
//							humiditys= String.format("%.1f", humiditys);
							// bodys=current.getString("windSpeed");
							
							iconsString = count.getString("icon");
							summarysString = count.getString("summary");
							// winds=current.getString("windSpeed");

							Log.d("responseeeee", "responseeeee" + windsString
									+ tempsHighString + precipsString + humiditysString
									+ tempsLowString + iconsString + summarysString);
							if (Prefrences.selecteddate != "") {
								buttonDate.setText(Prefrences.selecteddate);
							} else {
								buttonDate.setText(currentDateandTimeString);
							}
							textviewWind.setText(windsString + "mph");
							textviewTemp.setText(tempsLowString + (char) 0x00B0 + " / "
									+ tempsHighString + (char) 0x00B0);
							String str = String.format("%.2f",
									Float.parseFloat(precipsString));
							textviewPrecip.setText(str + "%");
							String str2 = String.format("%.2f",
									Float.parseFloat(humiditysString));
							textviewHumidity.setText(str2 + "%");
							textviewWeather.setText(summarysString);
							if (iconsString.toString().equalsIgnoreCase("clear-day")
									|| iconsString.toString().equalsIgnoreCase(
											"clear-night")) {
								imageviewWeatherIcon
										.setBackgroundResource(R.drawable.sunny_temp);
							} else if (iconsString.toString()
									.equalsIgnoreCase("rain")
									|| iconsString.toString().equalsIgnoreCase(
											"sleet")) {
								imageviewWeatherIcon
										.setBackgroundResource(R.drawable.rainy_temp);
							} else if (iconsString.toString().equalsIgnoreCase(
									"partly-cloudy-day")
									|| iconsString.toString().equalsIgnoreCase(
											"partly-cloudy-night")) {
								imageviewWeatherIcon
										.setBackgroundResource(R.drawable.partly_temp);
							} else if (iconsString.toString().equalsIgnoreCase(
									"cloudy")) {
								imageviewWeatherIcon
										.setBackgroundResource(R.drawable.cloudy_temp);
							} else if (iconsString.toString()
									.equalsIgnoreCase("wind")
									|| iconsString.toString().equalsIgnoreCase("fog")) {
								imageviewWeatherIcon
										.setBackgroundResource(R.drawable.wind_temp);
							} else {
								Log.d("hi no image ", "hiii no image");
							}

								
//						}
							
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

	// ************ API for Update report. *************//
	void updateReportHit(Activity con) {

		Prefrences.showLoadingDialog(con, "Loading...");
		JSONObject jsonobj = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		try {
			for (int i = 0; i < personelnamesArray.size(); i++) {
				JSONObject jo3 = new JSONObject();
				Log.i("name ", "" + personelnamesArray.get(i).toString());
				jo3.put("user_id", personelIdArrayList.get(i).toString());
				jo3.put("full_name", personelnamesArray.get(i).toString());
				jo3.put("hours", personelHoursArray.get(i).toString());
				jsonArray.put(jo3);
			}

			// Log.d("jo3", "jo3" + jo3.length());
			// try {
			jsonobj.put("author_id", Prefrences.userId);

			jsonobj.put("created_date", buttonDate.getText().toString());
			jsonobj.put("humidity", textviewHumidity.getText().toString());
			jsonobj.put("precip", textviewPrecip.getText().toString());
			jsonobj.put("project_id", Prefrences.selectedProId);
			jsonobj.put("report_type", buttonType.getText().toString());
			jsonobj.put("temp", textviewTemp.getText().toString());
			jsonobj.put("weather", textviewWeather.getText().toString());
			jsonobj.put("weather_icon", iconsString);
			jsonobj.put("wind", textviewWind.getText().toString());
			jsonobj.put("body", edittextNotes.getText().toString());
			jsonobj.put("report_users", jsonArray);

			JSONObject finalJson = new JSONObject();
			finalJson.put("report", jsonobj);

			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
					.getBytes("UTF-8"));

			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			client.put(con, Prefrences.url + "/reports/" + reportIdString, entity,
					"application/json", new AsyncHttpResponseHandler() {
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

	class LongOperation extends AsyncTask<String, Void, String> {
boolean error;
		@Override
		protected String doInBackground(String... params) {

			try {
				Log.d("Post Picture ","Post Picture ");
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
			AlertMessage();
			if(error)
			{
				AlertMessage2();
			}
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}

	
	// ************ API for Post photo. *************//
	
	
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
		Log.i("photo[report_id]", "" + Prefrences.reportID);
		Log.i("photo[mobile]", "1");
		Log.i("photo[company_id]", Prefrences.companyId);
		Log.i("photo[source]", "Reports");
		Log.i("photo[user_id]", Prefrences.userId);
		Log.i("photo[project_id]", Prefrences.selectedProId);
		Log.i("photo[name]", "android");
		Log.i("photo[image]", "" + cbFile);

		mpEntity.addPart("photo[report_id]",
				new StringBody(Prefrences.reportID));

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
		Prefrences.dismissLoadingDialog();

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

				finish();

				// setting_page = true;

			}

		});

		final AlertDialog alert = builder.create();

		alert.show();

	}
	
	// ************ API for Safety Topics. *************//
	
	public void safetyTopic(){
		

			Prefrences.showLoadingDialog(ReportItemCreate.this, "Loading...");


			RequestParams params = new RequestParams();
			
			params.put("user_id", Prefrences.userId);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(100000);
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");
			// client.get()
			client.get(getApplicationContext(),
					Prefrences.url+"/reports/options",params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							JSONObject res = null;
							try {
								res = new JSONObject(response);
								Log.v("response ---- ",
										"---*****----" + res.toString(2));
								safetyArrayList=new ArrayList<SafetyTopics>();
								JSONArray current = res.getJSONArray("possible_topics");
//								// winds,temps,precips,humiditys,bodys,icons,summarys
								String id,title,info;
								for(int i=0;i<current.length();i++)
								{
								
									JSONObject obj = current
											.getJSONObject(i);
								
								id = obj.getString("id");
								title = obj.getString("title");
								info = obj.getString("info");
								
								safetyArrayList.add(new SafetyTopics(id, title, info));
	
								}

								Log.d("Safety", "size = "+safetyArrayList.size());
								dialog = new Dialog(activity,
										android.R.style.Theme_Translucent_NoTitleBar);
								// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialogbox_list);
								ArrayList<SafetyTopics> array = new ArrayList<SafetyTopics>();

								Window window = dialog.getWindow();
								WindowManager.LayoutParams wlp = window.getAttributes();

								wlp.gravity = Gravity.CENTER;
								wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
								// wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
								window.setAttributes(wlp);
								array = safetyArrayList;
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
								Button cancel = (Button) dialog.findViewById(R.id.cancel);

								ListView dialoglist = (ListView) dialog
										.findViewById(R.id.dialoglist);
								// Button btn_cancel = (Button)dialog.
								// findViewById(R.id.cancel);

								adapter1 adp = new adapter1(activity, array);
								dialoglist.setAdapter(adp);
								cancel.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub

										dialog.dismiss();
									}
								});

								list_outside.setOnClickListener(new OnClickListener() {

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

		public adapter1(Context con, ArrayList<SafetyTopics> array) {
			// TODO Auto-generated constructor stub
			this.array = array;
			this.con = con;
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
			holder.txtview.setTypeface(Prefrences
					.helveticaNeuelt(activity));
			holder.txtview.setText(array.get(position).Title.toString());

			holder.txtview.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					String safetyTopicTitle="";
					for(int i=0;i<arraysafetyArrayList.size();i++)
					{
						if(array.get(position).Title.toString().equals(arraysafetyArrayList.get(i).Title.toString()))
						{
							safetyTopicTitle=array.get(position).Title.toString();
						}
					}
					if(safetyTopicTitle.equals(""))
					{
					arraysafetyArrayList.add(array.get(position));
					Prefrences.selected_location = array.get(position).Title.toString();
					safetyadapter adp = new safetyadapter();
					safetyListView.setAdapter(adp);
					setlist(safetyListView, adp, 100);
					}
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
	
	public class safetyadapter extends BaseAdapter {

		LayoutInflater inflator;

		public safetyadapter() {
			inflator = (LayoutInflater) ReportItemCreate.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d("paersonnel adapter", "In adapter");
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// Log.v("size of personel","size of personel"+reportdata.get(ret).personnel.size());
			// return Prefrences.personelName.length;
			return arraysafetyArrayList.size();
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

		public class ViewHolder {
			public TextView personnelname;
			ImageView info, Remove;
			// int pos;
			RelativeLayout root, relatlay;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.safetylistitem,
						null);

				holder = new ViewHolder();
				holder.root = (RelativeLayout) convertView
						.findViewById(R.id.root);
				holder.info=(ImageView)convertView.findViewById(R.id.info);
				holder.personnelname = (TextView) convertView
						.findViewById(R.id.personelname);
//				holder.personnelhours = (TextView) convertView
//						.findViewById(R.id.personelhours);
				holder.Remove = (ImageView) convertView
						.findViewById(R.id.remove);
				holder.relatlay = (RelativeLayout) convertView
						.findViewById(R.id.relatlay);
//				holder.Remove.setTypeface(Prefrences
//						.helveticaNeuelt(getApplicationContext()));
//				holder.personnelhours.setTypeface(Prefrences
//						.helveticaNeuelt(getApplicationContext()));
				holder.personnelname.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				holder.root.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 100));
				ASSL.DoMagic(holder.root);
				/************ Set holder with LayoutInflater ************/
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}
			// holder.personnelhours.setTag(holder);
			// holder.personnelname.setTag(holder);
			// holder.Remove.setTag(holder);
			// holder.relatlay.setTag(holder);
			// Log.d("Loggy","Loggy"+reportdata.get(ret).personnel.get(position).users.get(0).uFullName.toString());

			// holder.personnelname.setText(Prefrences.personelName[position].toString());
			// holder.personnelhours.setText(Prefrences.personelHours[position].toString());
			safetyListView.setVisibility(View.VISIBLE);

				holder.personnelname.setText(arraysafetyArrayList.get(position).Title
						.toString());
//				holder.personnelhours.setText(arraysafety.get(position).Id
//						.toString());
			
//			holder.Remove.setText("Remove");
			holder.Remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ViewHolder holder2 = (ViewHolder) v.getTag();
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
											if(arraysafetyArrayList.size()==1)
												safetyListView.setVisibility(View.GONE);
											
											arraysafetyArrayList.remove(position);
//											personelIdArrayList.remove(position);
//											personelnamesArray.remove(position);
											safetyadapter.this.notifyDataSetChanged();

//											holder.relatlay.removeView(v);

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
					
					// holder.relatlay.setTag(holder);
				}
			});
			notifyDataSetChanged();
			return convertView;
		}

	}
}
