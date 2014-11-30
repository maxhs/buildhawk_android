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
/*
 *  Dialog Box which shows the list of locations with add button.
 * 
 */
public class DialogBox2 extends Dialog {

	public Activity activity;
	LinearLayout linearlayoutRoot;
	public Dialog dialog;
	public Button buttonCancelClicked, buttonAddCLicked,buttonRemoveCLicked;
	TextView textviewWho;
	ArrayList<String> arrayArrayList = new ArrayList<String>();
	ArrayList<Users> usrsArrayList = new ArrayList<Users>();
	Dialog popup;
	InputMethodManager imm;
	Button buttonSubmit, buttonCancel;
	EditText edittextHours, edittextLocation;
	TextView textviewExpiryAlert;
	ListView listviewDialoglist;

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
		linearlayoutRoot = (LinearLayout) findViewById(R.id.linearlayoutRoot);
		new ASSL(activity, linearlayoutRoot, 1134, 720, false);

		listviewDialoglist = (ListView) findViewById(R.id.listviewDialoglist);
		textviewWho = (TextView) findViewById(R.id.textviewWho);
		textviewWho.setTypeface(Prefrences.helveticaNeuelt(activity));
		if (Prefrences.text == 5 || Prefrences.text == 6) {
			arrayArrayList = WorkItemClick.locs;
		} else if (Prefrences.text == 4 || Prefrences.text == 7) {
			arrayArrayList = WorkItemClick.asss;
		}
		// else if(Prefrences.text==1 || Prefrences.text==2 ||
		// Prefrences.text==3)
		// usrs=Homepage.user2;

		// btn_users.setOnClickListener(this);
		// btn_sub.setOnClickListener(this);
		buttonCancelClicked = (Button) findViewById(R.id.buttonCancelClicked);
		buttonAddCLicked = (Button) findViewById(R.id.buttonAddCLicked);
		buttonAddCLicked.setTypeface(Prefrences.helveticaNeuelt(activity));
		buttonRemoveCLicked = (Button) findViewById(R.id.buttonRemoveCLicked);
		buttonRemoveCLicked.setTypeface(Prefrences.helveticaNeuelt(activity));
		buttonCancelClicked.setTypeface(Prefrences.helveticaNeuelt(activity));

		
		if(Prefrences.location_str.equalsIgnoreCase("") || Prefrences.location_str.equalsIgnoreCase("Selected location"))
		{
			buttonRemoveCLicked.setVisibility(View.GONE);
		}
		else
		{
			buttonRemoveCLicked.setVisibility(View.VISIBLE);
		}
		
		if(Prefrences.text==5)
		{
		buttonAddCLicked.setOnClickListener(new View.OnClickListener() {

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
				buttonSubmit = (Button) popup.findViewById(R.id.buttonSubmit);
				edittextHours = (EditText) popup.findViewById(R.id.edittextHours);
				edittextLocation = (EditText) popup.findViewById(R.id.edittextLocation);
				edittextHours.setVisibility(View.GONE);
				edittextLocation.setVisibility(View.VISIBLE);

				InputMethodManager mgr = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				// only will trigger it if no physical keyboard is open
				mgr.showSoftInput(edittextLocation, InputMethodManager.SHOW_IMPLICIT);
				// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
				// .toggleSoftInput(InputMethodManager.SHOW_FORCED,
				// InputMethodManager.HIDE_IMPLICIT_ONLY);
				buttonCancel = (Button) popup.findViewById(R.id.cancel);
				buttonSubmit.setTypeface(Prefrences.helveticaNeuelt(activity));
				buttonCancel.setTypeface(Prefrences.helveticaNeuelt(activity));
				edittextHours.setTypeface(Prefrences.helveticaNeuelt(activity));
				// expiry_alert.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
				// expiry_alert = (TextView)popup.findViewById(R.id.alert_text);
				// expiry_alert.setText("# of Hours ");
				if (Prefrences.text == 5) {
					popup.setTitle("Add Location ");
					edittextLocation.setHint("Location");
				} else if (Prefrences.text == 4) {
					popup.setTitle("Add Assignee ");
					edittextLocation.setHint("Assignee");
				}
				new ASSL(activity, expiryMain, 1134, 720, false);

				buttonSubmit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (!edittextLocation.getText().toString().equals("")) {
							if (Prefrences.text == 5) {
								
								String output = Character.toUpperCase(edittextLocation.getText()
										.toString().charAt(0)) + edittextLocation.getText()
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
							imm.hideSoftInputFromWindow(edittextHours.getWindowToken(),
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

				
				
				buttonCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						imm = (InputMethodManager) activity
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edittextHours.getWindowToken(), 0);
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
		buttonAddCLicked.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(activity,CompanyExpandable.class);
				activity.startActivity(intent);
				dismiss();
			}
		});
	}

		buttonRemoveCLicked.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Prefrences.location_str="";
				WorkItemClick.btnS_location.setText("Selected location"+Prefrences.location_str);
				dismiss();
				
			}
		});
		
		buttonCancelClicked.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

				dismiss();
			}
		});

		if (Prefrences.text == 1) {
			textviewWho.setText("Who do you want to email?");
		} else if (Prefrences.text == 2) {
			textviewWho.setText("Who do you want to call?");
		} else if (Prefrences.text == 3) {
			textviewWho.setText("Who do you want to message?");
		} else if (Prefrences.text == 4) {
			textviewWho.setText("Assignee");
		} else if (Prefrences.text == 5) {
			textviewWho.setText("Location");
		}
		adapter adp = new adapter(activity, arrayArrayList);
		listviewDialoglist.setAdapter(adp);
	}

	public class adapter extends BaseAdapter {

		ArrayList<String> array2ArrayList;
		// ArrayList<Users>usr;
		Context con;

		public adapter(Context con, ArrayList<String> arrayArrayList) {
			// TODO Auto-generated constructor stub
			this.array2ArrayList = arrayArrayList;
			this.con = con;
			// this.usr=usrs;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// Log.d("userlist","Size========="+array.size());
			// if()
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
				holder.linearlayout = (LinearLayout) convertView
						.findViewById(R.id.dialoglinear);
				holder.linearlayout.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 80));
				ASSL.DoMagic(holder.linearlayout);
				convertView.setTag(holder);
			} else {
				holder = (viewholder) convertView.getTag();
			}
			holder.textview = (TextView) convertView.findViewById(R.id.array);
			holder.textview.setTypeface(Prefrences.helveticaNeuelt(con));
			holder.textview.setText(array2ArrayList.get(position).toString());
			holder.textview.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					// TODO Auto-generated method stub

					// Prefrences.selectedlocation =
					// array.get(position).uFullName.toString();
					// Toast.makeText(c, ""+Prefrences.selectedlocation,
					// Toast.LENGTH_SHORT).show();
					if (Prefrences.text == 5) {
						Prefrences.location_str = array2ArrayList.get(position).toString();
						WorkItemClick.btnS_location.setText("Location: " + Prefrences.location_str);
					} else if (Prefrences.text == 4) {
						
						Prefrences.assignee_str = array2ArrayList.get(position).toString();
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
		TextView textview;
		LinearLayout linearlayout;
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
