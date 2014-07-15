package com.buildhawk;

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
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import android.widget.Toast;

import com.buildhawk.CheckItemClick.ViewHolder;
import com.buildhawk.ReportItemCreate.adapter1;
import com.buildhawk.ReportItemCreate.safetyadapter;
import com.buildhawk.utils.DialogBox;
import com.buildhawk.utils.Report;
import com.buildhawk.utils.SafetyTopics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

	// int pos;
	Activity activity;
	Dialog dialog;
	public ArrayList<SafetyTopics>safetyArray;
	ListView safetyListView;
	public ArrayList<SafetyTopics>arraysafety= new ArrayList<SafetyTopics>();
	
	ArrayList<Report> reportdata;
	LayoutInflater inflater;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
	private static int PICK_CONTACT = 1;
	ArrayList<String> arr = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<String> desc = new ArrayList<String>();
	Button btn_Email, btn_Text, btn_Call, btn_Cancel;
	TextView tv_expiry_alert;
	Dialog popup;
	companyListAdapter companyadapter;
	RelativeLayout list_outside;
//	int poss;
	
	
	
	public ViewPagerAdapter(Activity act, ArrayList<Report> reportdata) {
		this.reportdata = reportdata;
		activity = act;
//		ReportItemClick.CompaniesArray = new ArrayList<String>();
//		ReportItemClick.CompanyIdArrayList = new ArrayList<String>();
//		ReportItemClick.OnsiteArray = new ArrayList<String>();
//		ReportItemClick.personelHoursArray = new ArrayList<String>();
//		ReportItemClick.personelIdArrayList = new ArrayList<String>();
//		ReportItemClick.personelnamesArray = new ArrayList<String>();
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	

	public int getCount() {
		return reportdata.size();
	}

	
	public Object instantiateItem(View view, int position) {
		
		TextView wind, temp, precip, humidity, weather;
		ListView personellist,companylist,safetylist;
		ImageView weatherIcon;
		Button type, date,personnel,btn_safety;
		LinearLayout personellayout, onsitelayout, weatherlayout;
		ScrollView scr1;
		final EditText notes;
		LinearLayout personellistlayout;
		final LinearLayout lin2;
		LinearLayout safetytopiclayout,reportLayout;
		ImageView photogallery, camera;
		
		final View rootView = (View) inflater.inflate(
				R.layout.fragment_screen_slide_page, null);
		
		
		reportLayout=(LinearLayout)rootView.findViewById(R.id.reportLayout);
		safetytopiclayout = (LinearLayout) rootView.findViewById(R.id.safetyTopicslayout);
		weatherIcon = (ImageView) rootView.findViewById(R.id.imgview);
		personnel = (Button) rootView.findViewById(R.id.button3);
		btn_safety=(Button)rootView.findViewById(R.id.addtopics);
		date = (Button) rootView.findViewById(R.id.button2);
		type = (Button) rootView.findViewById(R.id.button1);
		wind = (TextView) rootView.findViewById(R.id.wind);
		temp = (TextView) rootView.findViewById(R.id.temp);
		precip = (TextView) rootView.findViewById(R.id.precip);
		humidity = (TextView) rootView.findViewById(R.id.humidity);
		weather = (TextView) rootView.findViewById(R.id.body);
		notes = (EditText) rootView.findViewById(R.id.notes);
		personellist = (ListView) rootView.findViewById(R.id.personellist);
		companylist = (ListView) rootView.findViewById(R.id.companieslist);
		safetylist=(ListView)rootView.findViewById(R.id.safetylist);
		personellistlayout = (LinearLayout) rootView.findViewById(R.id.personnelLayout);
		lin2 = (LinearLayout) rootView.findViewById(R.id.scrolllayout2);
		
		personellayout = (LinearLayout)rootView.findViewById(R.id.personnelLayout);
		onsitelayout = (LinearLayout)rootView. findViewById(R.id.onsite);
	
		weatherlayout = (LinearLayout) rootView.findViewById(R.id.weatherlayout);
		
		
		wind.setTypeface(Prefrences.helveticaNeuelt(activity));
		temp.setTypeface(Prefrences.helveticaNeuelt(activity));
		precip.setTypeface(Prefrences.helveticaNeuelt(activity));
		humidity.setTypeface(Prefrences.helveticaNeuelt(activity));
		weather.setTypeface(Prefrences.helveticaNeuelt(activity));

		type.setTypeface(Prefrences.helveticaNeuelt(activity));
		date.setTypeface(Prefrences.helveticaNeuelt(activity));

		notes.setTypeface(Prefrences.helveticaNeuelt(activity));
		
//		reportLayout.setVisibility(View.GONE);
		
		if (reportdata.get(position).report_type.toString().equalsIgnoreCase("Daily")) {
//			type.setText("Daily");
			weatherlayout.setVisibility(View.VISIBLE);
			safetytopiclayout.setVisibility(View.GONE);
//			type.setEnabled(true);

		} else if (reportdata.get(position).report_type.toString().equalsIgnoreCase("Safety")) {
//			type.setText("Safety");
			safetytopiclayout.setVisibility(View.VISIBLE);
			weatherlayout.setVisibility(View.GONE);
//			type.setEnabled(true);
		} else if (reportdata.get(position).report_type.toString().equalsIgnoreCase("Weekly")) {
//			type.setText("Weekly");
			weatherlayout.setVisibility(View.GONE);
			safetytopiclayout.setVisibility(View.GONE);
//			type.setEnabled(true);
		}
		
		notes.addTextChangedListener(new TextWatcher() {
			 
			   public void afterTextChanged(Editable s) {
				   Log.e("TEXTVAGASGV", notes.getText().toString());
				   Prefrences.report_body_edited = notes.getText().toString();
			   }
			 
			   public void beforeTextChanged(CharSequence s, int start, 
			     int count, int after) {
			   }
			 
			   public void onTextChanged(CharSequence s, int start, 
			     int before, int count) {
			  
			   }
			  });

		scr1 = (ScrollView) rootView.findViewById(R.id.scrollView1);
		// new ASSL(activity, scr1, 1134, 720, false);
		photogallery = (ImageView) rootView.findViewById(R.id.photogallery);
		camera = (ImageView) rootView.findViewById(R.id.camera);
		photogallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				activity.startActivityForResult(intent, RESULT_LOAD_IMAGE);
				Log.i("BACk to adapter", "BACk to adapter");

			}
		});

		btn_safety.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			safetyTopic();
				
			}
		});
		
		personnel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				
				popup = new Dialog(activity,
						android.R.style.Theme_Translucent_NoTitleBar);
				// expiry_popup.setCancelable(false);

				popup.setContentView(R.layout.bridge_expiry_popup);
				// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
				RelativeLayout expiry_main = (RelativeLayout) popup
						.findViewById(R.id.list_outside);
				// expiry_main.setInAnimation(R.anim.slide_in_from_top);
				btn_Email = (Button) popup.findViewById(R.id.Email);
				btn_Call = (Button) popup.findViewById(R.id.Call);
				btn_Text = (Button) popup.findViewById(R.id.Text);
				btn_Cancel = (Button) popup.findViewById(R.id.Cancel);
				tv_expiry_alert = (TextView) popup.findViewById(R.id.alert_text);
				// expiry_alert.setText("Contact "+Prefrences.ContactName.toString());
				btn_Email.setTypeface(Prefrences.helveticaNeuelt(activity));
				btn_Text.setTypeface(Prefrences.helveticaNeuelt(activity));
				btn_Call.setTypeface(Prefrences.helveticaNeuelt(activity));
				btn_Cancel.setTypeface(Prefrences.helveticaNeuelt(activity));

				tv_expiry_alert.setTypeface(Prefrences.helveticaNeuelt(activity));

				list_outside = (RelativeLayout) popup.findViewById(R.id.list_outside);
				new ASSL(activity, expiry_main, 1134, 720, false);

				btn_Email.setVisibility(View.GONE);
				btn_Call.setText("Individuals");
				btn_Text.setText("Companies");
				tv_expiry_alert.setText("Reports Personnel");

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

				btn_Call.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// type.setText("Safety");
						Prefrences.text=12;
						Intent intent = new Intent(activity,
								UsersList.class);
						activity.startActivity(intent);
						activity.overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						popup.dismiss();
					}
				});

				btn_Text.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// type.setText("Weekly");
						Prefrences.text=12;
						Intent intent = new Intent(activity,
								CompanyList.class);
						activity.startActivity(intent);
						activity.overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						popup.dismiss();
					}
				});

				btn_Cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						popup.dismiss();
					}
				});
				popup.show();

			}
		});
		
		
		
		camera.setOnClickListener(new OnClickListener() {

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
				ReportItemClick.picturePath = photo.toString();
				activity.startActivityForResult(intent, TAKE_PICTURE);

			}
		});		
		
		scr1.setLayoutParams(new ListView.LayoutParams(
				ListView.LayoutParams.MATCH_PARENT,
				ListView.LayoutParams.MATCH_PARENT));
		ASSL.DoMagic(scr1);

		if (reportdata.get(position).report_type.toString().equalsIgnoreCase("safety")) {
			safetytopiclayout.setVisibility(View.VISIBLE);
		} else {
			safetytopiclayout.setVisibility(View.GONE);
		}

//		if(position==0){
//		ReportItemClick.textdate.setText(reportdata.get(position).created_date
//				.toString());
//		ReportItemClick.texttype.setText(reportdata.get(position).report_type
//				.toString() + " - ");
//		}else{
//			ReportItemClick.textdate.setText(reportdata.get(position-1).created_date
//					.toString());
//			ReportItemClick.texttype.setText(reportdata.get(position-1).report_type
//					.toString() + " - ");
//		}
		
		weather.setText("" + reportdata.get(position).weather.toString());
		date.setText("" + reportdata.get(position).created_date.toString());
		type.setText("" + reportdata.get(position).report_type.toString());

		wind.setText("" + reportdata.get(position).wind.toString());
		temp.setText("" + reportdata.get(position).temp.toString());
		humidity.setText("" + reportdata.get(position).humidity.toString());
		// String temp1[]=reportdata.get(position).precip.toString().split("%");
		// String s = String.format("%.2f", Float.parseFloat(temp1[0]));
		// precip.setText(s + "%");
		precip.setText(reportdata.get(position).precip.toString());

		notes.setText("" + reportdata.get(position).body.toString());
		if (reportdata.get(position).weather_icon.toString().equalsIgnoreCase(
				"clear-day")
				|| reportdata.get(position).weather_icon.toString()
						.equalsIgnoreCase("clear-night")) {
			weatherIcon.setBackgroundResource(R.drawable.sunny_temp);
		} else if (reportdata.get(position).weather_icon.toString()
				.equalsIgnoreCase("rain")
				|| reportdata.get(position).weather_icon.toString()
						.equalsIgnoreCase("sleet")) {
			weatherIcon.setBackgroundResource(R.drawable.rainy_temp);
		} else if (reportdata.get(position).weather_icon.toString()
				.equalsIgnoreCase("partly-cloudy-day")
				|| reportdata.get(position).weather_icon.toString()
						.equalsIgnoreCase("partly-cloudy-night")) {
			weatherIcon.setBackgroundResource(R.drawable.partly_temp);
		} else if (reportdata.get(position).weather_icon.toString()
				.equalsIgnoreCase("cloudy")) {
			weatherIcon.setBackgroundResource(R.drawable.cloudy_temp);
		} else if (reportdata.get(position).weather_icon.toString()
				.equalsIgnoreCase("wind")
				|| reportdata.get(position).weather_icon.toString()
						.equalsIgnoreCase("fog")) {
			weatherIcon.setBackgroundResource(R.drawable.wind_temp);
		} else {
			Toast.makeText(activity, "No Image", Toast.LENGTH_SHORT).show();
			Log.d("hi no image ", "hiii no image");
		}
//		poss = position;
		// poss=position;
		 arr.clear();
		 ids.clear();
		 desc.clear();
		 lin2.setTag(position);
		
		for (int i = 0; i < reportdata.get(position).photos.size(); i++) {
			final ImageView ladder_page2 = new ImageView(activity);
			Log.d("----------", "position of array" + position);
			 arr.add(reportdata.get(position).photos.get(i).urlLarge);
			 ids.add(reportdata.get(position).photos.get(i).id);
			 desc.add(reportdata.get(position).photos.get(i).description);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					(int) (200), (int) (200));
			lp.setMargins(10, 10, 10, 10);
			
			 ladder_page2.setTag(i);
			ladder_page2.setLayoutParams(lp);
			Picasso.with(activity).load(reportdata.get(position).photos.get(i).url200.toString()).into(ladder_page2);
			
			lin2.addView(ladder_page2);
			
			Log.d("size of "," array  :  "+arr.size());
			
			 ladder_page2.setOnClickListener(new OnClickListener() {
					
				 @Override
				 public void onClick(View v) {
				 // TODO Auto-generated method stub
					 if(reportdata.get((Integer)lin2.getTag()).photos.size()>0)
					 {
					 Prefrences.selectedPic = (Integer) v.getTag();
					 //poss= (Integer) rootView.getTag();
					 Log.i("Tag Value", "" + Prefrences.selectedPic);
					 arr.clear();
					 ids.clear();
					 desc.clear();
					 for(int
					 i=0;i<reportdata.get((Integer)lin2.getTag()).photos.size();i++)
					 {
						 arr.add(reportdata.get((Integer)lin2.getTag()).photos.get(i).urlLarge);
						 ids.add(reportdata.get((Integer)lin2.getTag()).photos.get(i).id);
						 desc.add(reportdata.get((Integer)lin2.getTag()).photos.get(i).description);
					 }
					 Intent in = new Intent(activity, SelectedImageView.class);
					 in.putExtra("array", arr);
					 in.putExtra("ids", ids);
					 in.putExtra("desc", desc);
					// Log.d("----","logg value for viewpager"+(Integer)ladder_page2.getTag()+"array ="+arr.size()+"Prefrences.posViewpager"+Prefrences.posViewpager);
//					 in.putExtra("id", reportdata.get(poss).photos.get(Prefrences.selectedPic).id);
					 in.putExtra("id", reportdata.get((Integer)lin2.getTag()).photos.get(Prefrences.selectedPic).id);
					 activity.startActivity(in);
					 activity.overridePendingTransition(R.anim.slide_in_right,
					 R.anim.slide_out_left);
					 }
				 }
				 });
			
			 
		}
		
		

//		if (reportdata.get(position).personnel.size() == 0) {
			if (ReportItemClick.personelnamesArray.size() == 0) {
			Log.d("loggy if", "if loggy" + position);
			personellistlayout.setVisibility(View.GONE);

		} else {
			
			Log.d("loggy else", "else loggy" + position);
			personellistlayout.setVisibility(View.VISIBLE);
			personelListAdapter personadapter = new personelListAdapter(
					position);
			personellist.setAdapter(personadapter);
			setlist(personellist, personadapter, 100);
			//personadapter.notifyDataSetChanged();
		}
//		if (reportdata.get(position).companies.size() == 0) {
		if (ReportItemClick.CompaniesArray.size() == 0) {
			Log.d("loggy if", "if loggy" + position);
			onsitelayout.setVisibility(View.GONE);

		} else {
			
			Log.d("loggy else", "else loggy" + position);
			onsitelayout.setVisibility(View.VISIBLE);
			 companyadapter = new companyListAdapter(
					position);
			companylist.setAdapter(companyadapter);
			//companyadapter.notifyDataSetChanged();
			setlist(companylist, companyadapter, 100);
		}
//		if (reportdata.get(position).topic.size() == 0) {
			if (ReportItemClick.SafetyIDArray.size() == 0) {
			Log.d("loggy if", "if loggy" + position);
			safetylist.setVisibility(View.GONE);

		} else {
			
			Log.d("loggy else", "else loggy" + position);
			safetylist.setVisibility(View.VISIBLE);
			TopicListAdapter topicadapter = new TopicListAdapter(
					position);
			safetylist.setAdapter(topicadapter);
			//companyadapter.notifyDataSetChanged();
			setlist(safetylist, topicadapter, 100);
		}

		// textdate.setText(" - "+reportdata.get(position).created_date.toString());
		// texttype.setText(""+reportdata.get(position).report_type.toString());
		((ViewPager) view).addView(rootView, 0);
//		notifyDataSetChanged();
		
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

		public personelListAdapter() {
			inflator = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d("paersonnel adapter", "In adapter");
		}

		public personelListAdapter(int position) {
			inflator = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.positionPager = position;
//			ReportItemClick.personelnamesArray.clear();
//			ReportItemClick.personelIdArrayList.clear();
//			ReportItemClick.personelHoursArray.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.v("size of personel",
					"size of personel"
							+ reportdata.get(positionPager).personnel.size());
			Log.v("size of personel",
					"size of personel"
							+ ReportItemClick.personelnamesArray.size());
			return ReportItemClick.personelnamesArray.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
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
//				holder.Remove.setTypeface(Prefrences.helveticaNeuelt(activity));
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
//			final RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
//					((int) (720.0f * ASSL.Xscale())),
//					((int) (100.0f * ASSL.Yscale())));
//
//			layoutParams2.setMargins(0, 0, 0, 0);
		
			holder.root.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					final ViewHolder3 holder3 = (ViewHolder3) v.getTag();
					Log.d("Remove : ","clicked : ");	
					Prefrences.removeCompanyID = reportdata.get(positionPager).personnel
							.get(holder3.position).users.get(0).company.get(0).coId.toString();
					Prefrences.removeUserID = reportdata.get(positionPager).personnel
							.get(holder3.position).users.get(0).uId.toString();
					Prefrences.removeReportID = reportdata.get(positionPager).report_id.toString();
					Log.d("remooooovveeddd","----"+holder3.position+", "+Prefrences.removeCompanyID+", "+Prefrences.removeUserID+","+Prefrences.removeReportID);
				
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
										
										
//										holder.root.setLayoutParams(layoutParams2);
										holder.root.removeView(v);
										ReportItemClick.personelnamesArray.remove(holder3.position);
										ReportItemClick.personelHoursArray.remove(holder3.position);
										ReportItemClick.personelIdArrayList.remove(holder3.position);
										notifyDataSetChanged();
									
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
//				ReportItemClick.removeUsers();
				}
			});
			
//				holder.personnelname
//						.setText(reportdata.get(positionPager).personnel
//								.get(position).users.get(0).uFullName
//								.toString());
				holder.personnelname
				.setText(ReportItemClick.personelnamesArray.get(position).toString());
//				holder.personnelhours
//						.setText(reportdata.get(positionPager).personnel
//								.get(position).hours.toString());
				holder.personnelhours
				.setText(ReportItemClick.personelHoursArray.get(position).toString());
//				ReportItemClick.personelnamesArray.add(reportdata.get(positionPager).personnel
//						.get(position).users.get(0).uFullName
//						.toString());
//		ReportItemClick.personelIdArrayList.add(reportdata.get(positionPager).personnel
//				.get(position).users.get(0).uId
//				.toString());
//		ReportItemClick.personelHoursArray.add(reportdata.get(positionPager).personnel
//				.get(position).hours.toString());
//		holder.personnelname
//		.setText(ReportItemClick.personelnamesArray.get(position).toString());
//		holder.personnelhours
//		.setText(ReportItemClick.personelHoursArray.get(position).toString());
				
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
//			ReportItemClick.OnsiteArray.clear();
//			ReportItemClick.CompaniesArray.clear();
//			ReportItemClick.CompanyIdArrayList.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			Log.v("size of personel",
//					"size of personel"
//							+ reportdata.get(positionPager).companies.size());
			Log.v("size of company",
					"size of company"
							+ ReportItemClick.CompaniesArray.size());
			return ReportItemClick.CompaniesArray.size();
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
			ImageView  Remove;

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
//				holder.Remove.setTypeface(Prefrences.helveticaNeuelt(activity));
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
			
//			Prefrences.companyID=reportdata.get(positionPager).companies
//					.get(position).coId.toString();
//				holder.personnelname
//						.setText(reportdata.get(positionPager).companies
//								.get(position).Rcompany.get(0).coName
//								.toString());
				holder.personnelname
				.setText(ReportItemClick.CompaniesArray.get(position).toString());
//				ReportItemClick.CompaniesArray.add(reportdata.get(positionPager).companies
//								.get(position).Rcompany.get(0).coName
//								.toString());
//				ReportItemClick.CompanyIdArrayList.add(reportdata.get(positionPager).companies
//								.get(position).Rcompany.get(0).coId
//								.toString());
//				ReportItemClick.OnsiteArray.add(reportdata.get(positionPager).companies
//						.get(position).coCount.toString());
//				holder.personnelhours
//						.setText(reportdata.get(positionPager).companies
//								.get(position).coCount.toString());
				holder.personnelhours
				.setText(ReportItemClick.OnsiteArray.get(position).toString());

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
//			ReportItemClick.OnsiteArray.clear();
//			ReportItemClick.CompaniesArray.clear();
//			ReportItemClick.CompanyIdArrayList.clear();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			Log.v("size of personel",
//					"size of personel"
//							+ reportdata.get(positionPager).topic.size());
			Log.v("size of safety",
					"size of safety"
							+ ReportItemClick.SafetyIDArray.size());
			return ReportItemClick.SafetyIDArray.size();
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

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder2 holder;
			if (convertView == null) {
				convertView = inflator.inflate(R.layout.safetylistitem,
						null);

				holder = new ViewHolder2();
				holder.root = (RelativeLayout) convertView
						.findViewById(R.id.root);
				holder.personnelname = (TextView) convertView
						.findViewById(R.id.personelname);
				holder.info=(ImageView)convertView.findViewById(R.id.info);
//				holder.personnelhours = (TextView) convertView
//						.findViewById(R.id.personelhours);
				holder.Remove = (ImageView) convertView
						.findViewById(R.id.remove);
//				holder.personnelhours.setTypeface(Prefrences
//						.helveticaNeuelt(activity));
				holder.personnelname.setTypeface(Prefrences
						.helveticaNeuelt(activity));
//				holder.Remove.setTypeface(Prefrences.helveticaNeuelt(activity));
				holder.root.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 100));
				ASSL.DoMagic(holder.root);
				/************ Set holder with LayoutInflater ************/
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder2) convertView.getTag();
			}
//			holder.personnelhours.setTag(holder);
			holder.personnelname.setTag(holder);
			holder.Remove.setTag(holder);
//			Prefrences.companyID=reportdata.get(positionPager).topic
//					.get(position).id.toString();
			
//				holder.personnelname
//						.setText(reportdata.get(positionPager).topic
//								.get(position).safety.get(0).Title
//								.toString());
				holder.personnelname
				.setText(ReportItemClick.SafetyTitleArray.get(position).toString());
//				ReportItemClick.CompaniesArray.add(reportdata.get(positionPager).companies
//								.get(position).Rcompany.get(0).coName
//								.toString());
//				ReportItemClick.CompanyIdArrayList.add(reportdata.get(positionPager).companies
//								.get(position).Rcompany.get(0).coId
//								.toString());
//				ReportItemClick.OnsiteArray.add(reportdata.get(positionPager).companies
//						.get(position).coCount.toString());
//				holder.personnelhours
//				.setText(ReportItemClick.SafetyIDArray.get(position).toString());
//				holder.personnelhours
//						.setText(reportdata.get(positionPager).topic
//								.get(position).safety.get(0).Id
//								.toString());

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

		params.height = totalHeight	+ (lv.getDividerHeight() * (listAdapter.getCount() - 1));

		lv.setLayoutParams(params);
		lv.requestLayout();
	}
	
	public void safetyTopic(){
		

		Prefrences.showLoadingDialog(activity, "Loading...");


		RequestParams params = new RequestParams();
		
		params.put("user_id", Prefrences.userId);

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(100000);
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		// client.get()
		client.get(activity,
				Prefrences.url+"/reports/options",params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
							safetyArray=new ArrayList<SafetyTopics>();
							JSONArray current = res.getJSONArray("possible_topics");
//							// winds,temps,precips,humiditys,bodys,icons,summarys
							String id,title,info;
							for(int i=0;i<current.length();i++)
							{
							
								JSONObject obj = current
										.getJSONObject(i);
							
							id = obj.getString("id");
							title = obj.getString("title");
							info = obj.getString("info");
							
							safetyArray.add(new SafetyTopics(id, title, info));

							}

							Log.d("Safety", "size = "+safetyArray.size());
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
					
					arraysafety.add(array.get(position));
					Prefrences.stopingHit=1;
					ReportItemClick.SafetyTitleArray.add(array.get(position).Title.toString());
					ReportItemClick.SafetyIDArray.add(array.get(position).Id.toString());
//					safetyadapter adp = new safetyadapter();
//					safetyListView.setAdapter(adp);
//					setlist(safetyListView, adp, 100);
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
	
}
