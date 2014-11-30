package com.buildhawk;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.Report;
import com.squareup.picasso.Picasso;

public class ReportListAdapter extends BaseAdapter {
	ArrayList<Report> reportdata_adpArrayList = new ArrayList<Report>();

	LayoutInflater inflator;
	Activity activity;
	// setValue set1;
	

	int index;

	public ReportListAdapter(Activity activity, ArrayList<Report> reportdataArrayList) {
		this.activity = activity;
		inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.reportdata_adpArrayList = reportdataArrayList;
		// set1= new setValue();

	}

	@Override
	public int getCount() {

		return reportdata_adpArrayList.size();
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/

	public static class ViewHolder {
		public TextView report_date, personnel_value, notesValue, image_count,
				reportType, personelOnsite, notes;
		public ImageView photo;
		LinearLayout root;
		public int pos;

	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			view = inflator.inflate(R.layout.report_listview_item, null);

			holder = new ViewHolder();
			holder.personelOnsite = (TextView) view
					.findViewById(R.id.personnel_onsite);
			holder.notes = (TextView) view.findViewById(R.id.edittextNotes);

			holder.root = (LinearLayout) view.findViewById(R.id.root);
			holder.report_date = (TextView) view
					.findViewById(R.id.daily_report_date);
			holder.reportType = (TextView) view.findViewById(R.id.daily_report);
			holder.personnel_value = (TextView) view
					.findViewById(R.id.personnel_value);
			holder.notesValue = (TextView) view.findViewById(R.id.notes_value);
			holder.image_count = (TextView) view
					.findViewById(R.id.report_photo_value);
			holder.photo = (ImageView) view.findViewById(R.id.report_photo);
			holder.notesValue.setTypeface(Prefrences.helveticaNeuelt(activity));
			holder.personnel_value.setTypeface(Prefrences
					.helveticaNeuelt(activity));
			holder.image_count
					.setTypeface(Prefrences.helveticaNeuelt(activity));
			holder.report_date
					.setTypeface(Prefrences.helveticaNeuelt(activity));
			holder.reportType.setTypeface(Prefrences.helveticaNeuelt(activity));
			holder.notes.setTypeface(Prefrences.helveticaNeuelt(activity));
			holder.personelOnsite.setTypeface(Prefrences
					.helveticaNeuelt(activity));
			holder.root.setLayoutParams(new ListView.LayoutParams(
					ListView.LayoutParams.MATCH_PARENT, 250));
			ASSL.DoMagic(holder.root);
			/************ Set holder with LayoutInflater ************/
			view.setTag(holder);

		}

		else {
			holder = (ViewHolder) view.getTag();
		}
		// Prefrences.posViewpager=position;
		holder.pos=position;
		
		holder.report_date.setTag(holder);
		holder.reportType.setTag(holder);
		holder.personnel_value.setTag(holder);
		holder.notesValue.setTag(holder);
		holder.image_count.setTag(holder);
		holder.photo.setTag(holder);
		holder.report_date.setText(reportdata_adpArrayList.get(position).created_date);

		Log.i("photo[report_id]", reportdata_adpArrayList.get(position).report_id.toString());

		holder.notesValue.setText(reportdata_adpArrayList.get(position).body);
		// try{
		if (reportdata_adpArrayList.get(position).report_type.equalsIgnoreCase("Daily")) {
			holder.reportType.setText(reportdata_adpArrayList.get(position).report_type
					+ " Report - ");
		} else if (reportdata_adpArrayList.get(position).report_type
				.equalsIgnoreCase("Safety")) {
			holder.reportType.setText(reportdata_adpArrayList.get(position).report_type
					+ " Report - ");
		} else if (reportdata_adpArrayList.get(position).report_type
				.equalsIgnoreCase("Weekly")) {
			holder.reportType.setText(reportdata_adpArrayList.get(position).report_type
					+ " Report - ");
		}

		int onsiteTotal = 0,onsiteTemp=0;
		int companiesPos=0;
		String onsitevalues="";
		
		onsiteTemp=reportdata_adpArrayList.get(position).personnel.size();
		companiesPos=reportdata_adpArrayList.get(position).companies.size();
		if(companiesPos>0)
		{
		for(int i=0;i<companiesPos;i++)
		{
		onsitevalues=reportdata_adpArrayList.get(position).companies.get(i).coCount;
		Log.d("onsitevalues","onsitevalues"+onsitevalues);
		if(onsitevalues.equals("null"))
		{
			
		}
		else{
		onsiteTotal= onsiteTotal + Integer.parseInt(onsitevalues);
		}
		Log.d("***********","**********"+onsitevalues);
		}
		}
		else{}
		onsiteTotal=onsiteTotal+onsiteTemp;
		
		
		if (onsiteTotal!=0) {
			// set1.setty(holder.personnel_value, position);
			Log.e("", "how many times" + position);
			holder.personnel_value.setText(" "+ onsiteTotal);
		} else
			holder.personnel_value.setText("0");
		
//		if (reportdata_adp.get(position).personnel != null) {
//			// set1.setty(holder.personnel_value, position);
//			Log.e("", "how many times" + position);
//			holder.personnel_value.setText(" "
//					+ reportdata_adp.get(position).personnel.size());
//		} else
//			holder.personnel_value.setText("0");
		
		// Log.d("","position"+position+"size"+reportdata.get(position).personnel.size());
		// }
		// catch(Exception e){}
		if (reportdata_adpArrayList.get(position).photos.size()!=0){//get(0).url200.equals("drawable")) {
			Picasso.with(activity)
					.load(reportdata_adpArrayList.get(position).photos.get(0).url200)
					.placeholder(R.drawable.default_200)
					.into(holder.photo);
			holder.image_count.setText(""
					+ reportdata_adpArrayList.get(position).photos.size());
			Log.d("if", "----if---");
		} else {
			holder.photo.setImageResource(R.drawable.default_200);
			holder.image_count.setText(" ");
			Log.d("if", "----else---");

		}
		
		
		holder.root.setTag(holder);
		holder.root.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ViewHolder viewholder = (ViewHolder) v.getTag();
			if(ConnectionDetector.isConnectingToInternet()){
				Log.e("CLICK", ",, "+Prefrences.reportlisttypes);
				Intent intent = new Intent(activity, ReportItemClick.class);
				intent.putExtra("pos", viewholder.pos);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			else{
				Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
			}
			}
		});
		//
		return view;
	}

	public class setValue {
		public setValue() {
		}

		void setty(final TextView txtview, int pos) {
			int num = reportdata_adpArrayList.get(pos).personnel.size();
			txtview.setText("" + num);

		}
	}

}
