package com.buildhawk;

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
import android.view.LayoutInflater;
import android.view.View;
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

import com.buildhawk.ReportItemClick.LongOperation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

public class ReportItemCreate extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
	private static int PICK_CONTACT = 1;
	public String reportId;
	String winds, tempsHigh, precips, humiditys, tempsLow, icons, summarys;
	TextView wind, temp, precip, humidity, weather;
	TextView texttype, textdate;
	ImageView weatherIcon;
	Button type, date, personnel, safetytopic;
	Button save, back;
	RelativeLayout ral;
	int year, month, day;
	Dialog popup;
	Button Email, Text, Call, Cancel;
	TextView expiry_alert;
	RelativeLayout list_outside;
	private Uri imageUri;
	LinearLayout lin2;
	String picturePath = "";
	Boolean isInternetPresent = false;
	ConnectionDetector connDect;
	EditText notes;
	public static ArrayList<String> personelnamesArray = new ArrayList<String>();
	public static ArrayList<String> personelIdArrayList = new ArrayList<String>();
	public static ArrayList<String> personelHoursArray = new ArrayList<String>();
	public static ArrayList<String> CompaniesArray = new ArrayList<String>();
	public static ArrayList<String> OnsiteArray = new ArrayList<String>();
	public static ArrayList<String> CompanyIdArrayList = new ArrayList<String>();

	ListView personnellist, companylist;
	Activity activity;
	String currentDateandTime;
	LinearLayout personellayout, onsitelayout, safetytopiclayout,
			weatherlayout;
	ScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_item_create);

		ral = (RelativeLayout) findViewById(R.id.relaycreateitem);
		new ASSL(this, ral, 1134, 720, false);
		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		scrollView.smoothScrollTo(0, 0);
		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresent = connDect.isConnectingToInternet();
		activity = ReportItemCreate.this;
		personellayout = (LinearLayout) findViewById(R.id.personnelLayout);
		onsitelayout = (LinearLayout) findViewById(R.id.onsite);
		safetytopiclayout = (LinearLayout) findViewById(R.id.safetyTopicslayout);
		weatherlayout = (LinearLayout) findViewById(R.id.weatherlayout);
		back = (Button) findViewById(R.id.back);
		save = (Button) findViewById(R.id.save);
		ImageView camera = (ImageView) findViewById(R.id.camera);
		ImageView gallery = (ImageView) findViewById(R.id.photogallery);
		lin2 = (LinearLayout) findViewById(R.id.scrolllayout2);
		date = (Button) findViewById(R.id.button2);
		type = (Button) findViewById(R.id.button1);
		personnel = (Button) findViewById(R.id.button3);
		safetytopic = (Button) findViewById(R.id.addtopics);
		wind = (TextView) findViewById(R.id.wind);
		temp = (TextView) findViewById(R.id.temp);
		precip = (TextView) findViewById(R.id.precip);
		humidity = (TextView) findViewById(R.id.humidity);
		weather = (TextView) findViewById(R.id.body);
		weatherIcon = (ImageView) findViewById(R.id.imgview);
		notes = (EditText) findViewById(R.id.notes);
		personnellist = (ListView) findViewById(R.id.personellist);
		companylist = (ListView) findViewById(R.id.companieslist);

		textdate = (TextView) findViewById(R.id.textdate);
		texttype = (TextView) findViewById(R.id.texttype);

		wind.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		temp.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		precip.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		humidity.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		weather.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));

		texttype.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		textdate.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		type.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		date.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		personnel.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		safetytopic.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		save.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		back.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));

		notes.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));

		if (Prefrences.selecteddate == "") {
			Log.d("if if if ", "if if if ");
			if (Prefrences.reportTypeDialog == 1) {
				type.setText("Daily");
				weatherlayout.setVisibility(View.VISIBLE);
				safetytopiclayout.setVisibility(View.GONE);
				type.setEnabled(true);

			} else if (Prefrences.reportTypeDialog == 2) {
				type.setText("Safety");
				safetytopiclayout.setVisibility(View.VISIBLE);
				weatherlayout.setVisibility(View.GONE);
				type.setEnabled(true);
			} else if (Prefrences.reportTypeDialog == 3) {
				type.setText("Weekly");
				weatherlayout.setVisibility(View.GONE);
				safetytopiclayout.setVisibility(View.GONE);
				type.setEnabled(true);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			currentDateandTime = sdf.format(new Date());

			textdate.setText(" - " + currentDateandTime);
			texttype.setText(type.getText().toString());
		} else {
			Log.d("else else", "else else else");
			type.setText("Daily");
			weatherlayout.setVisibility(View.VISIBLE);
			safetytopiclayout.setVisibility(View.GONE);
			type.setEnabled(false);
			textdate.setText(" - " + Prefrences.selecteddate);
			texttype.setText(type.getText().toString());
			// date.setText(""+Prefrences.selecteddate);
			// date.setText("Hello...");
			// date.setBackgroundColor(Color.WHITE);
			// date.setTextColor(Color.BLACK);
			Log.d("date button", "date button" + date.getText().toString()
					+ ", prefrencedate" + Prefrences.selecteddate);
		}

		date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});

		type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				popup = new Dialog(ReportItemCreate.this,
						android.R.style.Theme_Translucent_NoTitleBar);
				// expiry_popup.setCancelable(false);

				popup.setContentView(R.layout.bridge_expiry_popup);
				Email = (Button) popup.findViewById(R.id.Email);
				Call = (Button) popup.findViewById(R.id.Call);
				Text = (Button) popup.findViewById(R.id.Text);
				Cancel = (Button) popup.findViewById(R.id.Cancel);
				expiry_alert = (TextView) popup.findViewById(R.id.alert_text);
				// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
				RelativeLayout expiry_main = (RelativeLayout) popup
						.findViewById(R.id.list_outside);
				// expiry_main.setInAnimation(R.anim.slide_in_from_top);

				// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());
				Email.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				Text.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				Call.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				Cancel.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				expiry_alert.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				list_outside = (RelativeLayout) popup
						.findViewById(R.id.list_outside);
				new ASSL(ReportItemCreate.this, expiry_main, 1134, 720, false);

				Email.setText("Daily");
				Call.setText("Safety");
				Text.setText("Weekly");
				expiry_alert.setText("Reports");

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

				Email.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						type.setText("Daily");
						texttype.setText("Daily");
						weatherlayout.setVisibility(View.VISIBLE);
						safetytopiclayout.setVisibility(View.GONE);
						popup.dismiss();
					}
				});

				Call.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						type.setText("Safety");
						texttype.setText("Safety");
						safetytopiclayout.setVisibility(View.VISIBLE);
						weatherlayout.setVisibility(View.GONE);
						popup.dismiss();
					}
				});

				Text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						type.setText("Weekly");
						texttype.setText("Weekly");
						weatherlayout.setVisibility(View.GONE);
						safetytopiclayout.setVisibility(View.GONE);
						popup.dismiss();
					}
				});

				Cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						popup.dismiss();
					}
				});
				popup.show();

			}
		});

		safetytopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub addtopic ???????????

			}
		});

		personnel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				popup = new Dialog(ReportItemCreate.this,
						android.R.style.Theme_Translucent_NoTitleBar);
				// expiry_popup.setCancelable(false);

				popup.setContentView(R.layout.bridge_expiry_popup);
				// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
				RelativeLayout expiry_main = (RelativeLayout) popup
						.findViewById(R.id.list_outside);
				// expiry_main.setInAnimation(R.anim.slide_in_from_top);
				Email = (Button) popup.findViewById(R.id.Email);
				Call = (Button) popup.findViewById(R.id.Call);
				Text = (Button) popup.findViewById(R.id.Text);
				Cancel = (Button) popup.findViewById(R.id.Cancel);
				expiry_alert = (TextView) popup.findViewById(R.id.alert_text);
				// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());
				Email.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				Text.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				Call.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				Cancel.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				expiry_alert.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));

				list_outside = (RelativeLayout) popup
						.findViewById(R.id.list_outside);
				new ASSL(ReportItemCreate.this, expiry_main, 1134, 720, false);

				Email.setVisibility(View.GONE);
				Call.setText("Individuals");
				Text.setText("Companies");
				expiry_alert.setText("Reports Personnel");

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

				Call.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// type.setText("Safety");
						Intent intent = new Intent(ReportItemCreate.this,
								UsersList.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						popup.dismiss();
					}
				});

				Text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// type.setText("Weekly");
						Intent intent = new Intent(ReportItemCreate.this,
								CompanyList.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						popup.dismiss();
					}
				});

				Cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						popup.dismiss();
					}
				});
				popup.show();

			}
		});

		gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});

		camera.setOnClickListener(new OnClickListener() {

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
				picturePath = photo.toString();
				imageUri = Uri.fromFile(photo);
				startActivityForResult(intent, TAKE_PICTURE);
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Prefrences.selecteddate = "";
				Prefrences.reportTypeDialog = 0;
				personelHoursArray.clear();
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
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isInternetPresent) {
					// Prefrences.stopingHit = 1;
					if (save.getText().equals("Create")) {
						createReportHit();

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
				personellayout.setVisibility(View.VISIBLE);
				personelListAdapterCreate personadapter = new personelListAdapterCreate();
				personnellist.setAdapter(personadapter);
				setlist(personnellist, personadapter, 100);
			} else {
				personellayout.setVisibility(View.GONE);
			}
			if (CompaniesArray.size() != 0) {
				onsitelayout.setVisibility(View.VISIBLE);
				companiesListAdapterCreate companyadapter = new companiesListAdapterCreate();
				companylist.setAdapter(companyadapter);
				setlist(companylist, companyadapter, 100);
			} else {
				onsitelayout.setVisibility(View.GONE);
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
			public TextView personnelname, personnelhours, Remove;
			// int pos;
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
				holder.Remove = (TextView) convertView
						.findViewById(R.id.remove);
				holder.relatlay = (RelativeLayout) convertView
						.findViewById(R.id.relatlay);
				holder.Remove.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
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
			holder.Remove.setText("Remove");
			holder.Remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					personelHoursArray.remove(position);
					personelnamesArray.remove(position);
					personelIdArrayList.remove(position);
					holder.relatlay.removeViewAt(position);
					notifyDataSetChanged();
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
			public TextView personnelname, personnelhours, Remove;

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
				holder.Remove = (TextView) convertView
						.findViewById(R.id.remove);
				holder.relatlay = (RelativeLayout) convertView
						.findViewById(R.id.relatlay);

				holder.Remove.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
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
			// Log.d("Loggy","Loggy"+reportdata.get(ret).personnel.get(position).users.get(0).uFullName.toString());

			// holder.personnelname.setText(Prefrences.personelName[position].toString());
			// holder.personnelhours.setText(Prefrences.personelHours[position].toString());
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

			holder.Remove.setText("Remove");
			holder.Remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					CompaniesArray.remove(position);
					OnsiteArray.remove(position);
					CompanyIdArrayList.remove(position);
					holder.relatlay.removeView(v);
					// holder.relatlay.setTag(holder);
					notifyDataSetChanged();
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
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			ImageView ladder_page2 = null;
			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
			ladder_page2 = new ImageView(this);

			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					(int) (200), (int) (200));
			layoutParam.setMargins(10, 10, 10, 10);
			ladder_page2.setLayoutParams(layoutParam);
			// Picasso.with(CheckItemClick.this).load(picturePath).into(ladder_page2);
			// // ladder_page2.setImageBitmap(myBitmap);
			lin2.addView(ladder_page2);
			// }

			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			ladder_page2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			// upload();
			// commentAdd();

		}

		else if (requestCode == TAKE_PICTURE
				&& resultCode == Activity.RESULT_OK) {
			Uri selectedImage = imageUri;
			getContentResolver().notifyChange(selectedImage, null);
			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			ImageView ladder_page2 = null;
			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
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
				File file = new File(picturePath);

				Picasso.with(ReportItemCreate.this)
						.load(file)
						.resize((int) (200 * ASSL.Xscale()),
								(int) (200 * ASSL.Xscale())).centerCrop()
						.into(ladder_page2);
				ladder_page2.setImageBitmap(bitmap);
				lin2.addView(ladder_page2);

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

			date.setText(final_date);
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

	void createReportHit() {

		Prefrences.showLoadingDialog(ReportItemCreate.this, "Loading...");
		JSONObject jsonobj = new JSONObject();

		// JSONObject jo2 =new JSONObject();
		JSONArray jsonPersonnel1 = new JSONArray();
		JSONArray jsonCompany = new JSONArray();
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

			// Log.d("jo3", "jo3" + jo3.length());
			// try {
			jsonobj.put("author_id", Prefrences.userId);

			jsonobj.put("created_date", date.getText().toString());
			jsonobj.put("humidity", humidity.getText().toString());
			jsonobj.put("precip", precip.getText().toString());
			jsonobj.put("project_id", Prefrences.selectedProId);
			jsonobj.put("report_type", type.getText().toString());
			jsonobj.put("temp", temp.getText().toString());
			jsonobj.put("weather", weather.getText().toString());
			jsonobj.put("weather_icon", icons);
			jsonobj.put("wind", wind.getText().toString());
			jsonobj.put("body", notes.getText().toString());
			jsonobj.put("report_users", jsonPersonnel1);
			jsonobj.put("report_companies", jsonCompany);
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

								 JSONObject report =
								 res.getJSONObject("report");
								
								 String reportId = report.getString("id");
								
								 reportId = report.getString("id");
								 Prefrences.reportID = "" + reportId;
								 Log.d("reportID",
								 "reportID" + reportId.toString());
								if (!picturePath.equals("")) {
									new LongOperation().execute("");
								} else {
									Prefrences.dismissLoadingDialog();
									AlertMessage();
								}
								if (save.getText().equals("Create")) {
									save.setText("Save");
									DateFormat dateFormat = new SimpleDateFormat(
											"yyyy/MM/dd");
									// get current date time with Date()
									Date date = new Date();
									if (type.getText().toString()
											.equalsIgnoreCase("Daily")) {
										Prefrences.saveDateDaily(dateFormat
												.format(date).toString(),
												getApplicationContext());
									} else if (type.getText().toString()
											.equalsIgnoreCase("Safety")) {
										Prefrences.saveDateSafety(dateFormat
												.format(date).toString(),
												getApplicationContext());
									}
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

							JSONObject current = res.getJSONObject("currently");
							// winds,temps,precips,humiditys,bodys,icons,summarys
							winds = current.getString("windSpeed");
							tempsHigh = current.getString("temperature");
							tempsLow = current.getString("apparentTemperature");

							precips = current.getString("precipProbability");
							double pre = Double.parseDouble(precips);
							pre = pre * 100;
							precips = Double.toString(pre);
							// precips=precips.spl

							humiditys = current.getString("humidity");
							double hum = Double.parseDouble(humiditys);
							hum = hum * 100;
							humiditys = Double.toString(hum);
							// bodys=current.getString("windSpeed");

							icons = current.getString("icon");
							summarys = current.getString("summary");
							// winds=current.getString("windSpeed");

							Log.d("responseeeee", "responseeeee" + winds
									+ tempsHigh + precips + humiditys
									+ tempsLow + icons + summarys);
							if (Prefrences.selecteddate != "") {
								date.setText(Prefrences.selecteddate);
							} else {
								date.setText(currentDateandTime);
							}
							wind.setText(winds + "mph");
							temp.setText(tempsLow + (char) 0x00B0 + " / "
									+ tempsHigh + (char) 0x00B0);
							String s = String.format("%.2f",
									Float.parseFloat(precips));
							precip.setText(s + "%");
							humidity.setText(humiditys + "%");
							weather.setText(summarys);
							if (icons.toString().equalsIgnoreCase("clear-day")
									|| icons.toString().equalsIgnoreCase(
											"clear-night")) {
								weatherIcon
										.setBackgroundResource(R.drawable.sunny_temp);
							} else if (icons.toString()
									.equalsIgnoreCase("rain")
									|| icons.toString().equalsIgnoreCase(
											"sleet")) {
								weatherIcon
										.setBackgroundResource(R.drawable.rainy_temp);
							} else if (icons.toString().equalsIgnoreCase(
									"partly-cloudy-day")
									|| icons.toString().equalsIgnoreCase(
											"partly-cloudy-night")) {
								weatherIcon
										.setBackgroundResource(R.drawable.partly_temp);
							} else if (icons.toString().equalsIgnoreCase(
									"cloudy")) {
								weatherIcon
										.setBackgroundResource(R.drawable.cloudy_temp);
							} else if (icons.toString()
									.equalsIgnoreCase("wind")
									|| icons.toString().equalsIgnoreCase("fog")) {
								weatherIcon
										.setBackgroundResource(R.drawable.wind_temp);
							} else {
								Log.d("hi no image ", "hiii no image");
							}

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

			jsonobj.put("created_date", date.getText().toString());
			jsonobj.put("humidity", humidity.getText().toString());
			jsonobj.put("precip", precip.getText().toString());
			jsonobj.put("project_id", Prefrences.selectedProId);
			jsonobj.put("report_type", type.getText().toString());
			jsonobj.put("temp", temp.getText().toString());
			jsonobj.put("weather", weather.getText().toString());
			jsonobj.put("weather_icon", icons);
			jsonobj.put("wind", wind.getText().toString());
			jsonobj.put("body", notes.getText().toString());
			jsonobj.put("report_users", jsonArray);

			JSONObject finalJson = new JSONObject();
			finalJson.put("report", jsonobj);

			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
					.getBytes("UTF-8"));

			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			client.put(con, Prefrences.url + "/reports/" + reportId, entity,
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
			AlertMessage();
		}

		@Override
		protected void onPreExecute() {

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

}
