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
		holder.textviewCoName = (TextView) vi.findViewById(R.id.textviewCoName);
		holder.textviewCoPhone = (TextView) vi.findViewById(R.id.textviewCoPhone);
		holder.textviewCoEmail = (TextView) vi.findViewById(R.id.textviewCoEmail);
		holder.relativelayout = (RelativeLayout) vi.findViewById(R.id.relativelayout);
		holder.textviewCoName.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.textviewCoPhone.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.textviewCoEmail.setTypeface(Prefrences.helveticaNeuelt(activity));
		holder.textviewCoName.setText(Prefrences.coworkrName[position]);
		if(Prefrences.coworkrForPhone[position].equals("null"))
		{
			holder.textviewCoPhone.setText("");
		}
		else
		{
			holder.textviewCoPhone.setText(Prefrences.coworkrForPhone[position]);
		}
				
		holder.textviewCoEmail.setText(Prefrences.coworkrEmail[position]);
		holder.relativelayout.setLayoutParams(new ListView.LayoutParams(
				ListView.LayoutParams.MATCH_PARENT, 200));

		ASSL.DoMagic(holder.relativelayout);

		holder.relativelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Prefrences.ContactName = Prefrences.coworkrName[position];
				if(Prefrences.coworkrForPhone[position].equals("null"))
				{
					Prefrences.ContactPhone = "";
				}
				else
				{
				Prefrences.ContactPhone = Prefrences.coworkrForPhone[position];
				}
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

		private TextView textviewCoName, textviewCoPhone, textviewCoEmail;
		RelativeLayout relativelayout;

	}

}
