package com.buildhawk;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.buildhawk.UsersList.adapter;

import com.buildhawk.utils.Users;

import rmn.androidscreenlibrary.ASSL;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyList extends Activity {

	ListView lstvw;
	ArrayList<String> array, array2, arrayID, arrayID2;
	TextView back;
	Context con;
	LinearLayout relLay;
	Dialog popup;
	Button submit, cancel;
	EditText hours, location;
	TextView expiryAlert;
	RelativeLayout listOutside;
	InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_list);
		relLay = (LinearLayout) findViewById(R.id.LinearLayoutuserlist);
		new ASSL(this, relLay, 1134, 720, false);

		con = CompanyList.this;
		back = (TextView) findViewById(R.id.back);

		lstvw = (ListView) findViewById(R.id.userslist);
		array2 = Homepage.compny;
		LinkedHashSet<String> listToSet = new LinkedHashSet<String>(array2);
		array = new ArrayList<String>(listToSet);

		arrayID2 = Homepage.compnyId;
		LinkedHashSet<String> listToSet2 = new LinkedHashSet<String>(arrayID2);
		arrayID = new ArrayList<String>(listToSet2);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		adapter adp = new adapter(con, array);
		lstvw.setAdapter(adp);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.company_list, menu);
		return true;
	}

	public class adapter extends BaseAdapter {

		ArrayList<String> array;
		Context con;

		public adapter(Context con, ArrayList<String> array) {
			// TODO Auto-generated constructor stub
			this.array = array;
			this.con = con;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.d("userlist", "Size=========" + array.size());
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
				convertView = inflater.inflate(R.layout.user_list_item, null);
				holder.linearLayout = (LinearLayout) convertView
						.findViewById(R.id.rv);
				holder.linearLayout.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.MATCH_PARENT));
				ASSL.DoMagic(holder.linearLayout);
				convertView.setTag(holder);
			} else {
				holder = (viewholder) convertView.getTag();
			}
			holder.txtView = (TextView) convertView.findViewById(R.id.array);
			holder.txtView.setTypeface(Prefrences.helveticaNeuelt(con));
			holder.txtView.setText(array.get(position).toString());
			holder.txtView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					// Intent intent = new Intent(Intent.ACTION_CALL,
					// Uri.parse(body.uPhone.toString()));
					// startActivity(intent);

					// Toast.makeText(getApplicationContext(),
					// ""+array.get(position).toString(),
					// Toast.LENGTH_SHORT).show();
					// Prefrences.personelName = new
					// String[Prefrences.sizeofname +1];
					// Prefrences.personelHours = new
					// String[Prefrences.sizeofname +1];
					// Prefrences.personelName[Prefrences.sizeofname]=body.uFullName.toString();
					// ReportItemCreate.CompaniesArray.add(array.get(position).toString());
					// Prefrences.personelName=array.get(position).toString();
					// Log.d("aaaja","aaaja"+Prefrences.personelName.toString());
					popup = new Dialog(CompanyList.this,
							android.R.style.Theme_Dialog);
					// expiry_popup.setCancelable(false);

					popup.setContentView(R.layout.dialogreportuser);
					// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
					RelativeLayout expiryMain = (RelativeLayout) popup
							.findViewById(R.id.list_outside);
					// expiry_main.setInAnimation(R.anim.slide_in_from_top);
					submit = (Button) popup.findViewById(R.id.submit);
					hours = (EditText) popup.findViewById(R.id.hours);
					hours.setHint("Counts");
					location = (EditText) popup.findViewById(R.id.location);
					location.setVisibility(View.GONE);
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
							.toggleSoftInput(InputMethodManager.SHOW_FORCED,
									InputMethodManager.HIDE_IMPLICIT_ONLY);
					cancel = (Button) popup.findViewById(R.id.cancel);
					// expiry_alert =
					// (TextView)popup.findViewById(R.id.alert_text);
					// expiry_alert.setText("# of Personnel ");
					popup.setTitle("# of counts");
					listOutside = (RelativeLayout) popup
							.findViewById(R.id.list_outside);
					new ASSL(CompanyList.this, expiryMain, 1134, 720, false);

					// list_outside.setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// if(popup.isShowing())
					// {
					// //
					// ReportItemCreate.CompaniesArray.remove(array.get(position).toString());
					// imm = (InputMethodManager)
					// getSystemService(Context.INPUT_METHOD_SERVICE);
					// imm.hideSoftInputFromWindow(hours.getWindowToken(), 0);
					// popup.dismiss();
					// //overridePendingTransition(R.anim.slide_in_bottom,
					// R.anim.slide_out_to_top);
					// }
					// }
					// });

					submit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Prefrences.reportTypeDialog = 1;
							Prefrences.resumeflag = 1;
							// Prefrences.personelHours[Prefrences.sizeofname]=
							// hours.getText().toString();
							ReportItemCreate.OnsiteArray.add(hours.getText()
									.toString());
							ReportItemCreate.CompaniesArray.add(array.get(
									position).toString());
							ReportItemCreate.CompanyIdArrayList.add(arrayID
									.get(position).toString());
							Prefrences.personelHours = hours.getText()
									.toString();
							// Prefrences.sizeofname++;
							Log.d("huhuh", "huhuh" + Prefrences.sizeofname);
							imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(hours.getWindowToken(),
									0);
							popup.dismiss();
							finish();
							overridePendingTransition(R.anim.slide_in_left,
									R.anim.slide_out_right);

						}
					});

					cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							popup.dismiss();
							// ReportItemCreate.CompaniesArray.remove(array.get(position).toString());
						}
					});
					popup.show();

				}
			});
			return convertView;
		}

	}

	private static class viewholder {
		TextView txtView;
		LinearLayout linearLayout;
	}

}
