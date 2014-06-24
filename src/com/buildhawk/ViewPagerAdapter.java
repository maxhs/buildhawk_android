package com.buildhawk;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import rmn.androidscreenlibrary.ASSL;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import com.buildhawk.utils.Report;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

	// int pos;
	Activity activity;
	static ArrayList<Report> reportdata;
	LayoutInflater inflater;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
	private static int PICK_CONTACT = 1;
	ArrayList<String> arr = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<String> desc = new ArrayList<String>();
	int poss;

	public ViewPagerAdapter(Activity act, ArrayList<Report> reportdata) {
		this.reportdata = reportdata;
		activity = act;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return reportdata.size();
	}

	public Object instantiateItem(View view, int position) {
		// ImageView view = new ImageView(activity);
		// view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.FILL_PARENT));
		// view.setScaleType(ScaleType.FIT_XY);
		// //view.setBackgroundResource(reportdata[position]);
		// ((ViewPager) collection).addView(view, 0);
		TextView wind, temp, precip, humidity, weather;
		ListView personellist;
		ImageView weatherIcon;
		Button type, date;
		ScrollView scr1;
		EditText notes;
		LinearLayout personellistlayout, lin2;
		LinearLayout safetytopiclayout;
		ImageView photogallery, camera;
		// pos=position;
		final View rootView = (View) inflater.inflate(
				R.layout.fragment_screen_slide_page, null);
		// String message = getArguments().getString("s");
		// rootView.setTag(position);
		// scr1=(ScrollView)rootView.findViewById(R.id.scrollView1);
		// new ASSL(activity, scr1, 1134, 720, false);
		safetytopiclayout = (LinearLayout) rootView
				.findViewById(R.id.safetyTopicslayout);
		weatherIcon = (ImageView) rootView.findViewById(R.id.imgview);
		date = (Button) rootView.findViewById(R.id.button2);
		type = (Button) rootView.findViewById(R.id.button1);
		wind = (TextView) rootView.findViewById(R.id.wind);
		temp = (TextView) rootView.findViewById(R.id.temp);
		precip = (TextView) rootView.findViewById(R.id.precip);
		humidity = (TextView) rootView.findViewById(R.id.humidity);
		weather = (TextView) rootView.findViewById(R.id.body);
		notes = (EditText) rootView.findViewById(R.id.notes);
		personellist = (ListView) rootView.findViewById(R.id.personellist);
		personellistlayout = (LinearLayout) rootView
				.findViewById(R.id.personnelLayout);
		lin2 = (LinearLayout) rootView.findViewById(R.id.scrolllayout2);
		wind.setTypeface(Prefrences.helveticaNeuelt(activity));
		temp.setTypeface(Prefrences.helveticaNeuelt(activity));
		precip.setTypeface(Prefrences.helveticaNeuelt(activity));
		humidity.setTypeface(Prefrences.helveticaNeuelt(activity));
		weather.setTypeface(Prefrences.helveticaNeuelt(activity));

		type.setTypeface(Prefrences.helveticaNeuelt(activity));
		date.setTypeface(Prefrences.helveticaNeuelt(activity));

		notes.setTypeface(Prefrences.helveticaNeuelt(activity));

		scr1 = (ScrollView) rootView.findViewById(R.id.scrollView1);
		// new ASSL(activity, scr1, 1134, 720, false);
		photogallery = (ImageView) rootView.findViewById(R.id.photogallery);
		camera = (ImageView) rootView.findViewById(R.id.camera);
		photogallery.setOnClickListener(new OnClickListener() {

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

		if (reportdata.get(position).report_type.toString().equalsIgnoreCase(
				"safety")) {
			safetytopiclayout.setVisibility(View.VISIBLE);
		} else {
			safetytopiclayout.setVisibility(View.GONE);
		}

		ReportItemClick.textdate.setText(reportdata.get(position).created_date
				.toString());
		ReportItemClick.texttype.setText(reportdata.get(position).report_type
				.toString() + " - ");
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
		poss = position;
		// poss=position;
		 arr.clear();
		 ids.clear();
		 desc.clear();
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
			Picasso.with(activity)
					.load(reportdata.get(position).photos.get(i).url200
							.toString()).into(ladder_page2);
			// // ladder_page2.setImageBitmap(myBitmap);
			lin2.addView(ladder_page2);

//			ladder_page2.setTag(position);
			// ladder_page2.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// Prefrences.selectedPic = (Integer) v.getTag();
			// //poss= (Integer) rootView.getTag();
			// Log.i("Tag Value", "" + Prefrences.selectedPic);
			// arr.clear();
			// ids.clear();
			// desc.clear();
			// for(int
			// i=0;i<reportdata.get((Integer)ladder_page2.getTag()).photos.size();i++)
			// {
			// arr.add(reportdata.get((Integer)ladder_page2.getTag()).photos.get(i).urlLarge);
			// ids.add(reportdata.get((Integer)ladder_page2.getTag()).photos.get(i).id);
			// desc.add(reportdata.get((Integer)ladder_page2.getTag()).photos.get(i).description);
			// }
			// Intent in = new Intent(activity, MainActivity1.class);
			// in.putExtra("array", arr);
			// in.putExtra("ids", ids);
			// in.putExtra("desc", desc);
			// Log.d("----","logg value for viewpager"+(Integer)ladder_page2.getTag()+"array ="+arr.size()+"Prefrences.posViewpager"+Prefrences.posViewpager);
			// //in.putExtra("id",
			// reportdata.get(poss).photos.get(Prefrences.selectedPic).id);
			// in.putExtra("id",
			// reportdata.get((Integer)ladder_page2.getTag()).photos.get(Prefrences.selectedPic).id);
			// activity.startActivity(in);
			// activity.overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_left);
			// }
			// });
		}

		if (reportdata.get(position).personnel.size() == 0) {
			Log.d("loggy if", "if loggy" + position);
			personellistlayout.setVisibility(View.GONE);

		} else {
			Log.d("loggy else", "else loggy" + position);
			personellistlayout.setVisibility(View.VISIBLE);
			personelListAdapter personadapter = new personelListAdapter(
					position);
			personellist.setAdapter(personadapter);
			setlist(personellist, personadapter, 100);
		}

		// textdate.setText(" - "+reportdata.get(position).created_date.toString());
		// texttype.setText(""+reportdata.get(position).report_type.toString());
		((ViewPager) view).addView(rootView, 0);
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
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.v("size of personel",
					"size of personel"
							+ reportdata.get(positionPager).personnel.size());
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

		public class ViewHolder {
			public TextView personnelname, personnelhours, Remove;

			RelativeLayout root;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
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
				holder.personnelhours.setTypeface(Prefrences
						.helveticaNeuelt(activity));
				holder.personnelname.setTypeface(Prefrences
						.helveticaNeuelt(activity));
				holder.Remove.setTypeface(Prefrences.helveticaNeuelt(activity));
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
			// Log.d("Loggy","Loggy"+reportdata.get(ret).personnel.get(position).users.get(0).uFullName.toString());
			if (reportdata.get(positionPager).personnel.get(position).users != null) {
				holder.personnelname
						.setText(reportdata.get(positionPager).personnel
								.get(position).users.get(0).uFullName
								.toString());
				holder.personnelhours
						.setText(reportdata.get(positionPager).personnel
								.get(position).hours.toString());
			} else {
				holder.personnelname
						.setText(reportdata.get(positionPager).personnel
								.get(position).psubs.get(0).sName.toString());
				holder.personnelhours
						.setText(reportdata.get(positionPager).personnel
								.get(position).count.toString());
			}
			return convertView;
		}

	}

	public void setlist(ListView lv, ListAdapter listAdapter, int size) {
		int maxHeight = 0;
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
		maxHeight += totalHeight;
		ViewGroup.LayoutParams params = lv.getLayoutParams();

		params.height = totalHeight
				+ (lv.getDividerHeight() * (listAdapter.getCount() - 1));

		lv.setLayoutParams(params);
		lv.requestLayout();
	}

}
