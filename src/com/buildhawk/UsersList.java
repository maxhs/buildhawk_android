package com.buildhawk;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;

//import com.buildhawk.ReportItemClick.ScreenSlidePageFragment.personelListAdapter;
import com.buildhawk.ReportItemCreate.personelListAdapterCreate;
import com.buildhawk.utils.Report;
import com.buildhawk.utils.ReportTopics;
import com.buildhawk.utils.SafetyTopics;
import com.buildhawk.utils.Users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UsersList extends Activity {
	ListView listviewUsers;
	ArrayList<Users> arrayArrayList;
	InputMethodManager imm;
	TextView textviewBack;
	Context con;
	LinearLayout linearlayoutRoot;
	Dialog popup;
	Button buttonSubmit, buttonCancel;
	EditText edittextHours, edittextLocation;
	TextView textviewExpiryAlert;
	RelativeLayout relativelayoutListOutside;
//	ArrayList<String>personelnamesArray= new ArrayList<String>();
//	ArrayList<String>personelIdArrayList= new ArrayList<String>();
//	ArrayList<String>personelHoursArray= new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users_list);
		linearlayoutRoot = (LinearLayout) findViewById(R.id.linearlayoutRoot);
		new ASSL(this, linearlayoutRoot, 1134, 720, false);

		con = UsersList.this;
		textviewBack = (TextView) findViewById(R.id.textviewBack);

		listviewUsers = (ListView) findViewById(R.id.listviewUsers);

		textviewBack.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));

		arrayArrayList = ProjectsAdapter.user2ArrayList;
		textviewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		adapter adp = new adapter(con, arrayArrayList);
		listviewUsers.setAdapter(adp);
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
		getMenuInflater().inflate(R.menu.users_list, menu);
		return true;
	}

	public class adapter extends BaseAdapter {

		ArrayList<Users> array2ArrayList;
		Context con;

		public adapter(Context con, ArrayList<Users> arrayArrayList) {
			// TODO Auto-generated constructor stub
			this.array2ArrayList = arrayArrayList;
			this.con = con;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.d("userlist", "Size=========" + array2ArrayList.size());
			return array2ArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return array2ArrayList.get(position);
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
			final Users body = (Users) this.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new viewholder();
				// LayoutInflater inflater =
				// ((Activity)con).getLayoutInflater();
				LayoutInflater inflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.user_list_item, null);
				holder.linearlayout = (LinearLayout) convertView
						.findViewById(R.id.rv);
				holder.linearlayout.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.MATCH_PARENT));
				ASSL.DoMagic(holder.linearlayout);
				convertView.setTag(holder);
			} else {
				holder = (viewholder) convertView.getTag();
			}
			holder.textview = (TextView) convertView.findViewById(R.id.array);
			holder.textview.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
			holder.textview.setText(body.uFullName.toString());
			holder.textview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Intent intent = new Intent(Intent.ACTION_CALL,
					// Uri.parse(body.uPhone.toString()));
					// startActivity(intent);
					
					if (Prefrences.text == 1) {
//						Prefrences.text = 0;
						Log.v("", "" + body.uEmail.toString());
						// Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
						// Uri.fromParts(
						// "mailto",body.uEmail.toString(), null));
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						emailIntent.putExtra(Intent.EXTRA_EMAIL,
								new String[] { body.uEmail.toString() });
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
//						Prefrences.text = 0;
						Log.v("", "" + body.uPhone.toString());
						if(body.uPhone.toString().equals("null"))
						{
							AlertMessage();
						}
						else{
						Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
						phoneCallIntent.setData(Uri.parse("tel:"
								+ body.uPhone.toString()));
						startActivity(phoneCallIntent);
						}
					} else if (Prefrences.text == 3) {
//						Prefrences.text = 0;
						if(body.uPhone.toString().equals("null"))
						{
							AlertMessage();
						}
						else{
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.fromParts("sms", body.uPhone.toString(), null)));
						}
					} else if(Prefrences.text == 11){
//						Prefrences.text = 0;
						Prefrences.personelName = body.uFullName.toString();

						popup = new Dialog(UsersList.this,
								android.R.style.Theme_Dialog);
						// expiry_popup.setCancelable(false);

						popup.setContentView(R.layout.dialogreportuser);
						// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
						RelativeLayout expiry_main = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						// expiry_main.setInAnimation(R.anim.slide_in_from_top);
						buttonSubmit = (Button) popup.findViewById(R.id.buttonSubmit);
						edittextHours = (EditText) popup.findViewById(R.id.edittextHours);
						edittextLocation = (EditText) popup.findViewById(R.id.edittextLocation);
						edittextHours.setVisibility(View.VISIBLE);
						edittextLocation.setVisibility(View.GONE);
						edittextHours.setHint("Hours");
						((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
								.toggleSoftInput(
										InputMethodManager.SHOW_FORCED,
										InputMethodManager.HIDE_IMPLICIT_ONLY);
						buttonCancel = (Button) popup.findViewById(R.id.cancel);
						buttonSubmit.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						buttonCancel.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						edittextHours.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						// expiry_alert.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
						// expiry_alert =
						// (TextView)popup.findViewById(R.id.alert_text);
						// expiry_alert.setText("# of Hours ");
						popup.setTitle("# of hours");
						relativelayoutListOutside = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						new ASSL(UsersList.this, expiry_main, 1134, 720, false);

						expiry_main.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
							popup.dismiss();
							imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edittextHours.getWindowToken(), 0);
							}
						});

						buttonSubmit.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Prefrences.reportTypeDialog = 1;
								Prefrences.resumeflag = 1;
								// Prefrences.personelHours[Prefrences.sizeofname]=
								// hours.getText().toString();
								ReportItemCreate.personelnamesArray
										.add(body.uFullName.toString());
								ReportItemCreate.personelIdArrayList
										.add(body.uId.toString());
								Log.d("aaaja", "aaaja"
										+ ReportItemCreate.personelIdArrayList);
								Log.d("aaaja", "aaaja"
										+ ReportItemCreate.personelnamesArray);
								ReportItemCreate.personelHoursArray.add(edittextHours
										.getText().toString());
								
								imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										edittextHours.getWindowToken(), 0);
								popup.dismiss();
								finish();
								overridePendingTransition(R.anim.slide_in_left,
										R.anim.slide_out_right);

							}
						});

						buttonCancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										edittextHours.getWindowToken(), 0);
								popup.dismiss();
								
							}
						});
						popup.show();

					}
					else if(Prefrences.text == 12){
//						Prefrences.text = 0;
						Prefrences.personelName = body.uFullName.toString();

						popup = new Dialog(UsersList.this,
								android.R.style.Theme_Dialog);
						// expiry_popup.setCancelable(false);

						popup.setContentView(R.layout.dialogreportuser);
						// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
						RelativeLayout expiry_main = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						// expiry_main.setInAnimation(R.anim.slide_in_from_top);
						buttonSubmit = (Button) popup.findViewById(R.id.buttonSubmit);
						edittextHours = (EditText) popup.findViewById(R.id.edittextHours);
						edittextLocation = (EditText) popup.findViewById(R.id.edittextLocation);
						edittextHours.setVisibility(View.VISIBLE);
						edittextLocation.setVisibility(View.GONE);
						edittextHours.setHint("Hours");
						((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
								.toggleSoftInput(
										InputMethodManager.SHOW_FORCED,
										InputMethodManager.HIDE_IMPLICIT_ONLY);
						buttonCancel = (Button) popup.findViewById(R.id.cancel);
						buttonSubmit.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						buttonCancel.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						edittextHours.setTypeface(Prefrences
								.helveticaNeuelt(getApplicationContext()));
						// expiry_alert.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
						// expiry_alert =
						// (TextView)popup.findViewById(R.id.alert_text);
						// expiry_alert.setText("# of Hours ");
						popup.setTitle("# of hours");
						relativelayoutListOutside = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						new ASSL(UsersList.this, expiry_main, 1134, 720, false);

						expiry_main.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
							popup.dismiss();
							imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									edittextHours.getWindowToken(), 0);
							}
						});

						buttonSubmit.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								
								
								String safetyTopicId ="";
								for (int i = 0; i < ReportItemClick.personelIdArrayList.size(); i++) 
								{
									if (ReportItemClick.personelIdArrayList.get(i).toString().equals(body.uId.toString()))										
									{
										Log.d("Safety ID","Safety ID"+body.uId.toString().toString());
										safetyTopicId = body.uId.toString().toString();
									}
								}
								if (safetyTopicId.toString().equals("")) 
								{
									Prefrences.reportTypeDialog = 1;
									Prefrences.resumeflag = 1;
									// Prefrences.personelHours[Prefrences.sizeofname]=
									// hours.getText().toString();
//									personelnamesArray
//											.add(body.uFullName.toString());
//									personelIdArrayList
//											.add(body.uId.toString());
//									Log.d("aaaja", "aaaja"
//											+ personelIdArrayList);
//									Log.d("aaaja", "aaaja"
//											+ personelnamesArray);
//									personelHoursArray.add(hours
//											.getText().toString());
//									Prefrences.stopingHit=1;
									ReportItemClick.personelnamesArrayList
									.add(body.uFullName.toString());
									ReportItemClick.personelIdArrayList
									.add(body.uId.toString());
									ReportItemClick.personelHoursArrayList.add(edittextHours
											.getText().toString());
									
									imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(
											edittextHours.getWindowToken(), 0);
									popup.dismiss();
									
									Intent intent = new Intent(UsersList.this,
											ReportItemClick.class);
									intent.putExtra("id", body.uId.toString());
									intent.putExtra("name", body.uFullName.toString());
									intent.putExtra("hours", edittextHours.getText()
											.toString());
									setResult(RESULT_OK, intent);
								}
								
								
								imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										edittextHours.getWindowToken(), 0);
								
								
								
								finish();
								overridePendingTransition(R.anim.slide_in_left,
										R.anim.slide_out_right);

							}
						});

						buttonCancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										edittextHours.getWindowToken(), 0);
								popup.dismiss();
								
							}
						});
						popup.show();

					
						
					}
					
				}
			});
			return convertView;
		}

	}

	private static class viewholder {
		TextView textview;
		LinearLayout linearlayout;
	}
	private void AlertMessage() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Sorry!")

		.setMessage(

		"The recipient doesn't have a phone number")

		.setCancelable(false)

		.setPositiveButton("OK",

		new DialogInterface.OnClickListener() {

			public void onClick(final DialogInterface dialog,

			int intValue) {

//				finish();

				// setting_page = true;

			}

		});

		final AlertDialog alert = builder.create();

		alert.show();

	}
}
