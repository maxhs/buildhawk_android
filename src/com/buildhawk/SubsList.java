package com.buildhawk;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;

import com.buildhawk.utils.Users;
import com.buildhawk.utils.subcontractors;

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

public class SubsList extends Activity {

	ListView lstvw;
	ArrayList<subcontractors> array;
	InputMethodManager imm;
	TextView back;
	Context con;
	LinearLayout relLay;
	Dialog popup;
	Button Submit, Cancel;
	EditText hours, location;
	TextView expiry_alert;
	RelativeLayout list_outside;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subs_list);
		relLay = (LinearLayout) findViewById(R.id.LinearLayoutuserlist);
		new ASSL(this, relLay, 1134, 720, false);

		con = SubsList.this;
		back = (TextView) findViewById(R.id.back);

		lstvw = (ListView) findViewById(R.id.subslist);

		back.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));

		array = Homepage.sub2;
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
		getMenuInflater().inflate(R.menu.subs_list, menu);
		return true;
	}

	public class adapter extends BaseAdapter {

		ArrayList<subcontractors> array;
		Context con;

		public adapter(Context con, ArrayList<subcontractors> array) {
			// TODO Auto-generated constructor stub
			this.array = array;
			this.con = con;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.d("sublist", "Size=========" + array.size());
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			viewholder holder;
			final subcontractors body = (subcontractors) this.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new viewholder();
				// LayoutInflater inflater =
				// ((Activity)con).getLayoutInflater();
				LayoutInflater inflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.user_list_item, null);
				holder.linearLay = (LinearLayout) convertView
						.findViewById(R.id.rv);
				holder.linearLay.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.MATCH_PARENT));
				ASSL.DoMagic(holder.linearLay);
				convertView.setTag(holder);
			} else {
				holder = (viewholder) convertView.getTag();
			}
			holder.txtview = (TextView) convertView.findViewById(R.id.array);
			holder.txtview.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.txtview.setText(body.sName.toString());
			holder.txtview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Intent intent = new Intent(Intent.ACTION_CALL,
					// Uri.parse(body.uPhone.toString()));
					// startActivity(intent);

					if (Prefrences.text == 1) {
						Prefrences.text = 0;
						Log.v("", "" + body.sEmail.toString());
						// Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
						// Uri.fromParts(
						// "mailto",body.uEmail.toString(), null));
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						emailIntent.putExtra(Intent.EXTRA_EMAIL,
								new String[] { body.sEmail.toString() });
						emailIntent.putExtra(
								android.content.Intent.EXTRA_SUBJECT, "");
						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
								"");
						emailIntent.setType("plain/text");
						try {
							startActivity(Intent.createChooser(emailIntent,
									"Send mail..."));
							finish();
							Log.i("Finished sending email...", "");
						} catch (android.content.ActivityNotFoundException ex) {
							Toast.makeText(getApplicationContext(),
									"There is no email client installed.",
									Toast.LENGTH_SHORT).show();
						}
					} else if (Prefrences.text == 2) {
						Prefrences.text = 0;
						Log.v("", "" + body.sPhone.toString());
						Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
						phoneCallIntent.setData(Uri.parse("tel:"
								+ body.sPhone.toString()));
						startActivity(phoneCallIntent);
					} else if (Prefrences.text == 3) {
						Prefrences.text = 0;
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.fromParts("sms", body.sPhone.toString(), null)));
					}
					// else
					// {
					// // Toast.makeText(getApplicationContext(),
					// ""+body.uFullName.toString(), Toast.LENGTH_SHORT).show();
					// // Prefrences.personelName = new
					// String[Prefrences.sizeofname +1];
					// // Prefrences.personelHours = new
					// String[Prefrences.sizeofname +1];
					// //
					// Prefrences.personelName[Prefrences.sizeofname]=body.uFullName.toString();
					// Prefrences.personelName=body.uFullName.toString();
					//
					// popup = new Dialog(UsersList.this,
					// android.R.style.Theme_Dialog);
					// //expiry_popup.setCancelable(false);
					//
					// popup.setContentView(R.layout.dialogreportuser);
					// //
					// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
					// RelativeLayout expiry_main = (RelativeLayout) popup
					// .findViewById(R.id.list_outside);
					// //expiry_main.setInAnimation(R.anim.slide_in_from_top);
					// Submit=(Button)popup.findViewById(R.id.submit);
					// hours=(EditText)popup.findViewById(R.id.hours);
					// location=(EditText)popup.findViewById(R.id.location);
					// hours.setVisibility(View.VISIBLE);
					// location.setVisibility(View.GONE);
					// hours.setHint("Hours");
					// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
					// .toggleSoftInput(InputMethodManager.SHOW_FORCED,
					// InputMethodManager.HIDE_IMPLICIT_ONLY);
					// Cancel=(Button)popup.findViewById(R.id.cancel);
					// Submit.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
					// Cancel.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
					// hours.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
					// //expiry_alert.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
					// // expiry_alert =
					// (TextView)popup.findViewById(R.id.alert_text);
					// // expiry_alert.setText("# of Hours ");
					// popup.setTitle("# of hours");
					// list_outside = (RelativeLayout)
					// popup.findViewById(R.id.list_outside);
					// new ASSL(UsersList.this, expiry_main, 1134, 720, false);
					//
					//
					// // list_outside.setOnClickListener(new OnClickListener()
					// {
					// // @Override
					// // public void onClick(View v) {
					// // if(popup.isShowing())
					// // {
					// //
					// ReportItemCreate.personelnamesArray.remove(body.uFullName.toString());
					// //
					// ReportItemCreate.personelIdArrayList.remove(body.uId.toString());
					// //
					// Log.d("aaaja","aaaja"+ReportItemCreate.personelIdArrayList);
					// //
					// Log.d("aaaja","aaaja"+ReportItemCreate.personelnamesArray);
					// // popup.dismiss();
					// // //overridePendingTransition(R.anim.slide_in_bottom,
					// R.anim.slide_out_to_top);
					// // }
					// // }
					// // });
					//
					// Submit.setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// Prefrences.reportTypeDialog=1;
					// Prefrences.resumeflag=1;
					// // Prefrences.personelHours[Prefrences.sizeofname]=
					// hours.getText().toString();
					// ReportItemCreate.personelnamesArray.add(body.uFullName.toString());
					// ReportItemCreate.personelIdArrayList.add(body.uId.toString());
					// Log.d("aaaja","aaaja"+ReportItemCreate.personelIdArrayList);
					// Log.d("aaaja","aaaja"+ReportItemCreate.personelnamesArray);
					// ReportItemCreate.personelHoursArray.add(hours.getText().toString());
					// // Prefrences.personelHours = hours.getText().toString();
					// // Prefrences.sizeofname++;
					// // Log.d("huhuh","huhuh"+Prefrences.sizeofname);
					// imm = (InputMethodManager)
					// getSystemService(Context.INPUT_METHOD_SERVICE);
					// imm.hideSoftInputFromWindow(hours.getWindowToken(), 0);
					// popup.dismiss();
					// finish();
					// overridePendingTransition(R.anim.slide_in_left,
					// R.anim.slide_out_right);
					//
					// }
					// });
					//
					//
					// Cancel.setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					// imm = (InputMethodManager)
					// getSystemService(Context.INPUT_METHOD_SERVICE);
					// imm.hideSoftInputFromWindow(hours.getWindowToken(), 0);
					// popup.dismiss();
					// //
					// ReportItemCreate.personelnamesArray.remove(body.uFullName.toString());
					// //
					// ReportItemCreate.personelIdArrayList.remove(body.uId.toString());
					// //
					// Log.d("aaaja","aaaja"+ReportItemCreate.personelIdArrayList);
					// //
					// Log.d("aaaja","aaaja"+ReportItemCreate.personelnamesArray);
					// }
					// });
					// popup.show();
					//
					//
					//
					// }
					// WorkItemClick.btnS_assigned.setText(""+body.uFullName.toString());

					//
					// startActivity(emailIntent);
					//
					// Intent mail= new Intent(UsersList.this,SendEmail.class);
					// mail.putExtra("emailId",body.uEmail);
					// startActivity(mail);
				}
			});
			return convertView;
		}

	}

	private static class viewholder {
		TextView txtview;
		LinearLayout linearLay;
	}
}
