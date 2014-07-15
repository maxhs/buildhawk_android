package com.buildhawk;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import com.buildhawk.utils.Users;

public class DialogBox2 extends Dialog {

	public Activity activity;
	LinearLayout linearLay;
	public Dialog dialog;
	public Button btnCancel, btnAddCLicked;
	TextView who;
	ArrayList<String> array = new ArrayList<String>();
	ArrayList<Users> usrs = new ArrayList<Users>();
	Dialog popup;
	InputMethodManager imm;
	Button submit, cancel;
	EditText hours, location;
	TextView expiryAlert;
	ListView dialoglist;

	public DialogBox2(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialogbox2);
		linearLay = (LinearLayout) findViewById(R.id.db);
		new ASSL(activity, linearLay, 1134, 720, false);

		dialoglist = (ListView) findViewById(R.id.dialoglist);
		who = (TextView) findViewById(R.id.txt_who);
		who.setTypeface(Prefrences.helveticaNeuelt(activity));
		if (Prefrences.text == 5 || Prefrences.text == 6) {
			array = WorkItemClick.locs;
		} else if (Prefrences.text == 4 || Prefrences.text == 7) {
			array = WorkItemClick.asss;
		}
		// else if(Prefrences.text==1 || Prefrences.text==2 ||
		// Prefrences.text==3)
		// usrs=Homepage.user2;

		// btn_users.setOnClickListener(this);
		// btn_sub.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.cancel);
		btnAddCLicked = (Button) findViewById(R.id.addClicked);
		btnAddCLicked.setTypeface(Prefrences.helveticaNeuelt(activity));
		btnCancel.setTypeface(Prefrences.helveticaNeuelt(activity));

		
		if(Prefrences.text==5)
		{
		btnAddCLicked.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				dismiss();
				popup = new Dialog(activity, android.R.style.Theme_Dialog);
				// expiry_popup.setCancelable(false);

				popup.setContentView(R.layout.dialogreportuser);
				// popup.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
				RelativeLayout expiryMain = (RelativeLayout) popup
						.findViewById(R.id.list_outside);
				// expiry_main.setInAnimation(R.anim.slide_in_from_top);
				submit = (Button) popup.findViewById(R.id.submit);
				hours = (EditText) popup.findViewById(R.id.hours);
				location = (EditText) popup.findViewById(R.id.location);
				hours.setVisibility(View.GONE);
				location.setVisibility(View.VISIBLE);

				InputMethodManager mgr = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				// only will trigger it if no physical keyboard is open
				mgr.showSoftInput(location, InputMethodManager.SHOW_IMPLICIT);
				// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
				// .toggleSoftInput(InputMethodManager.SHOW_FORCED,
				// InputMethodManager.HIDE_IMPLICIT_ONLY);
				cancel = (Button) popup.findViewById(R.id.cancel);
				submit.setTypeface(Prefrences.helveticaNeuelt(activity));
				cancel.setTypeface(Prefrences.helveticaNeuelt(activity));
				hours.setTypeface(Prefrences.helveticaNeuelt(activity));
				// expiry_alert.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
				// expiry_alert = (TextView)popup.findViewById(R.id.alert_text);
				// expiry_alert.setText("# of Hours ");
				if (Prefrences.text == 5) {
					popup.setTitle("Add Location ");
					location.setHint("Location");
				} else if (Prefrences.text == 4) {
					popup.setTitle("Add Assignee ");
					location.setHint("Assignee");
				}
				new ASSL(activity, expiryMain, 1134, 720, false);

				submit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (!location.getText().toString().equals("")) {
							if (Prefrences.text == 5) {
								
								String output = Character.toUpperCase(location.getText()
										.toString().charAt(0)) + location.getText()
										.toString().substring(1);
								Log.d("output","output"+output);
								WorkItemClick.locs.add(output);
								Prefrences.location_str=output;
								WorkItemClick.btnS_location.setText("Location: "+Prefrences.location_str);
							}

							else if (Prefrences.text == 4) {
								WorkItemClick.asss.add(Prefrences.assignee_str);//location.getText().toString());
								WorkItemClick.btnS_assigned.setText("Assignee: "+Prefrences.assignee_str);

							}

							imm = (InputMethodManager) activity
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(hours.getWindowToken(),
									0);
							// try {
							//
							// InputMethodManager imm = (InputMethodManager)
							// c.getSystemService(Context.INPUT_METHOD_SERVICE);
							//
							// imm.hideSoftInputFromWindow(getCurrentFocus()
							//
							// .getWindowToken(), 0);
							//
							// } catch (Exception exception) {
							//
							// exception.printStackTrace();
							//
							// }
							popup.dismiss();
						} else {
							Toast.makeText(activity, "Enter the value", 5000)
									.show();
						}

					}

				});

				cancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						imm = (InputMethodManager) activity
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(hours.getWindowToken(), 0);
						// try {
						//
						// InputMethodManager imm = (InputMethodManager)
						// c.getSystemService(Context.INPUT_METHOD_SERVICE);
						//
						// imm.hideSoftInputFromWindow(getCurrentFocus()
						//
						// .getWindowToken(), 0);
						//
						// } catch (Exception exception) {
						//
						// exception.printStackTrace();
						//
						// }
						popup.dismiss();

					}
				});
				popup.show();

				//
			}
		});
		
	}
	else if(Prefrences.text==4){
		btnAddCLicked.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(activity,AddUser.class);
				activity.startActivity(intent);
			}
		});
	}

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

				dismiss();
			}
		});

		if (Prefrences.text == 1) {
			who.setText("Who do you want to email?");
		} else if (Prefrences.text == 2) {
			who.setText("Who do you want to call?");
		} else if (Prefrences.text == 3) {
			who.setText("Who do you want to message?");
		} else if (Prefrences.text == 4) {
			who.setText("Assignee");
		} else if (Prefrences.text == 5) {
			who.setText("Location");
		}
		adapter adp = new adapter(activity, array);
		dialoglist.setAdapter(adp);
	}

	public class adapter extends BaseAdapter {

		ArrayList<String> array;
		// ArrayList<Users>usr;
		Context con;

		public adapter(Context con, ArrayList<String> array) {
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
			holder.txtview.setTypeface(Prefrences.helveticaNeuelt(con));
			holder.txtview.setText(array.get(position).toString());
			holder.txtview.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					// TODO Auto-generated method stub

					// Prefrences.selectedlocation =
					// array.get(position).uFullName.toString();
					// Toast.makeText(c, ""+Prefrences.selectedlocation,
					// Toast.LENGTH_SHORT).show();
					if (Prefrences.text == 5) {
						Prefrences.location_str = array.get(position).toString();
						WorkItemClick.btnS_location.setText("Location: " + Prefrences.location_str);
					} else if (Prefrences.text == 4) {
						
						Prefrences.assignee_str = array.get(position).toString();
						WorkItemClick.btnS_assigned.setText("Assignee: "+Prefrences.assignee_str);
					}

					dismiss();
				}

				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				//
				// }
			});
			return convertView;
		}

	}

	private static class viewholder {
		TextView txtview;
		LinearLayout linear;
	}
}

// @Override
// public void onClick(View v) {
// switch (v.getId()) {
// case R.id.btn_user:
// // array=ProjectsAdapter.
// // dismiss();
// Intent in = new Intent(c,UsersList.class);
// c.startActivity(in);
//
// break;
// case R.id.btn_sub:
// //dismiss();
// Intent i = new Intent(c,UsersList.class);
// c.startActivity(i);
// break;
// case R.id.cancel:
// dismiss();
// break;
// default:
// break;
// }
// dismiss();
// }
