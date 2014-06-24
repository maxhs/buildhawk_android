package com.buildhawk;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.Context;
import android.test.PerformanceTestCase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//****************LazyAdapterBrowse*********************

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private LayoutInflater inflater;
	ViewHolder holder;

	public LazyAdapter(Activity act) {
		activity = act;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.v("in adapter", "in adapter");
	}

	public int getCount() {
		Log.v("Count", "COunt" + Prefrences.coworkrName.length);
		return Prefrences.coworkrName.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return Prefrences.coworkrName.length;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null) {
			holder = new ViewHolder();
		}

		vi = inflater.inflate(R.layout.list_item, null);
		vi.setTag(holder);
		holder.coName = (TextView) vi.findViewById(R.id.co_name);
		holder.coPhone = (TextView) vi.findViewById(R.id.co_phone);
		holder.coEmail = (TextView) vi.findViewById(R.id.co_email);
		holder.relativeLay = (RelativeLayout) vi.findViewById(R.id.rv);
		holder.coName.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.coPhone.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.coEmail.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.coName.setText(Prefrences.coworkrName[position]);
		holder.coPhone.setText(Prefrences.coworkrForPhone[position]);
		holder.coEmail.setText(Prefrences.coworkrEmail[position]);
		holder.relativeLay.setLayoutParams(new ListView.LayoutParams(
				ListView.LayoutParams.MATCH_PARENT, 200));

		ASSL.DoMagic(holder.relativeLay);

		holder.relativeLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Prefrences.ContactName = Prefrences.coworkrName[position];
				Prefrences.ContactPhone = Prefrences.coworkrForPhone[position];
				Prefrences.ContactMail = Prefrences.coworkrEmail[position];
				Homepage.popup.show();
				// activity.overridePendingTransition(R.anim.slide_in_bottom,
				// R.anim.slide_out_to_top);
				// Toast.makeText(activity, ""+Prefrences.coworkrName[position],
				// 500).show();
			}
		});

		return vi;

	}

	class ViewHolder {

		private TextView coName, coPhone, coEmail;
		RelativeLayout relativeLay;

	}

}
