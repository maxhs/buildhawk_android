package com.buildhawk;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.buildhawk.UsersList.adapter;

import com.buildhawk.utils.ProjectPhotos;
import com.buildhawk.utils.Report;
import com.buildhawk.utils.ReportCompanies;
import com.buildhawk.utils.ReportCompany;
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

	ListView listview;
	ArrayList<String> array, array2, arrayID, arrayID2;
	TextView tv_back;
	Context con;
	LinearLayout relLay;
	Dialog popup;
	Button btn_submit, tv_cancel;
	EditText txt_hours, txt_location;
	TextView tv_expiryAlert;
	RelativeLayout listOutside;
	InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_list);
		relLay = (LinearLayout) findViewById(R.id.LinearLayoutuserlist);
		new ASSL(this, relLay, 1134, 720, false);

		con = CompanyList.this;
		tv_back = (TextView) findViewById(R.id.back);

		listview = (ListView) findViewById(R.id.userslist);
		array2 = ProjectsAdapter.compny;
		LinkedHashSet<String> listToSet = new LinkedHashSet<String>(array2);
		array = new ArrayList<String>(listToSet);

		arrayID2 = ProjectsAdapter.compnyId;
		LinkedHashSet<String> listToSet2 = new LinkedHashSet<String>(arrayID2);
		arrayID = new ArrayList<String>(listToSet2);

		tv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
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
		adapter adp = new adapter(con, array);
		listview.setAdapter(adp);
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
					if(Prefrences.text==11)
					{
					popup = new Dialog(CompanyList.this,
							android.R.style.Theme_Dialog);
					// expiry_popup.setCancelable(false);

					popup.setContentView(R.layout.dialogreportuser);
					// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
					RelativeLayout expiryMain = (RelativeLayout) popup
							.findViewById(R.id.list_outside);
					// expiry_main.setInAnimation(R.anim.slide_in_from_top);
					btn_submit = (Button) popup.findViewById(R.id.submit);
					txt_hours = (EditText) popup.findViewById(R.id.hours);
					txt_hours.setHint("Counts");
					txt_location = (EditText) popup.findViewById(R.id.location);
					txt_location.setVisibility(View.GONE);
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
							.toggleSoftInput(InputMethodManager.SHOW_FORCED,
									InputMethodManager.HIDE_IMPLICIT_ONLY);
					tv_cancel = (Button) popup.findViewById(R.id.cancel);
					
					popup.setTitle("# of counts");
					listOutside = (RelativeLayout) popup
							.findViewById(R.id.list_outside);
					new ASSL(CompanyList.this, expiryMain, 1134, 720, false);

					
					btn_submit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Prefrences.reportTypeDialog = 1;
							Prefrences.resumeflag = 1;
							// Prefrences.personelHours[Prefrences.sizeofname]=
							// hours.getText().toString();
							ReportItemCreate.OnsiteArray.add(txt_hours.getText()
									.toString());
							ReportItemCreate.CompaniesArray.add(array.get(
									position).toString());
							ReportItemCreate.CompanyIdArrayList.add(arrayID
									.get(position).toString());
//							Prefrences.personelHours = hours.getText()
//									.toString();
//							
//							 Prefrences.sizeofname++;
							Log.d("huhuh", "huhuh" + Prefrences.sizeofname);
							imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(txt_hours.getWindowToken(),0);
							popup.dismiss();
							finish();
							overridePendingTransition(R.anim.slide_in_left,
									R.anim.slide_out_right);

						}
					});

					tv_cancel.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							popup.dismiss();
							// ReportItemCreate.CompaniesArray.remove(array.get(position).toString());
						}
					});
					popup.show();
					}
					else if(Prefrences.text==12)
					{
						popup = new Dialog(CompanyList.this,
								android.R.style.Theme_Dialog);
						// expiry_popup.setCancelable(false);

						popup.setContentView(R.layout.dialogreportuser);
						// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
						RelativeLayout expiryMain = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						// expiry_main.setInAnimation(R.anim.slide_in_from_top);
						btn_submit = (Button) popup.findViewById(R.id.submit);
						txt_hours = (EditText) popup.findViewById(R.id.hours);
						txt_hours.setHint("Counts");
						txt_location = (EditText) popup.findViewById(R.id.location);
						txt_location.setVisibility(View.GONE);
						((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
								.toggleSoftInput(InputMethodManager.SHOW_FORCED,
										InputMethodManager.HIDE_IMPLICIT_ONLY);
						tv_cancel = (Button) popup.findViewById(R.id.cancel);
						
						popup.setTitle("# of counts");
						listOutside = (RelativeLayout) popup
								.findViewById(R.id.list_outside);
						new ASSL(CompanyList.this, expiryMain, 1134, 720, false);

						
						btn_submit.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								Prefrences.reportTypeDialog = 1;
								Prefrences.resumeflag = 1;
								// Prefrences.personelHours[Prefrences.sizeofname]=
								// hours.getText().toString();
								ReportItemClick.OnsiteArray.add(txt_hours.getText()
										.toString());
								ReportItemClick.CompaniesArray.add(array.get(
										position).toString());
								ReportItemClick.CompanyIdArrayList.add(arrayID
										.get(position).toString());
//								ReportFragment.reportdata.add(0, new Report("", "", "", "", "", "", "", "", "", "", "", "", "", "", null,null, null,
//												new ReportCompanies("", txt_hours.getText().toString(), 
//												new ReportCompany(arrayID.get(position).toString(),array.get(position).toString(),null,null)),null));
								Prefrences.stopingHit=1;
//								Prefrences.personelHours = hours.getText()
//										.toString();
//								// Prefrences.sizeofname++;
//								Log.d("huhuh", "huhuh" + Prefrences.sizeofname);
								imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(txt_hours.getWindowToken(),0);
								popup.dismiss();
								finish();
								overridePendingTransition(R.anim.slide_in_left,
										R.anim.slide_out_right);

							}
						});

						tv_cancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								popup.dismiss();
								// ReportItemCreate.CompaniesArray.remove(array.get(position).toString());
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
		TextView txtView;
		LinearLayout linearLayout;
	}

}
