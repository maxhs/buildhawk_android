package com.buildhawk.utils;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buildhawk.Prefrences;
import com.buildhawk.R;
import com.buildhawk.SubsList;
import com.buildhawk.UsersList;

	public class DialogBox extends Dialog implements
	android.view.View.OnClickListener {
	
		public Activity c;
		LinearLayout db;
		public Dialog d;
		public Button btn_users, btn_sub,btn_cancel;
		TextView who;

		public DialogBox(Activity a) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dialog_box);	
		db=(LinearLayout)findViewById(R.id.db);		
		new ASSL(c, db, 1134, 720, false);
		
		btn_users = (Button) findViewById(R.id.btn_user);
		who=(TextView)findViewById(R.id.txt_who);
		btn_sub = (Button) findViewById(R.id.btn_sub);
		btn_cancel = (Button) findViewById(R.id.cancel);
		btn_users.setOnClickListener(this);
		btn_sub.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
			if(Prefrences.text==1)
			{
			who.setText("Who do you want to email?");
			}
			else if(Prefrences.text==2)
			{
				who.setText("Who do you want to call?");
			}
			else if(Prefrences.text==3)
			{
				who.setText("Who do you want to message?");
			}
	}
		
		@Override
		public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_user:
//			array=ProjectsAdapter.
		 // dismiss();
			Intent in = new Intent(c,UsersList.class);
			c.startActivity(in);
			c.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			
		  break;
		case R.id.btn_sub:
		  //dismiss();
			Intent i = new Intent(c,SubsList.class);
			c.startActivity(i);
			c.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		  break;
		case R.id.cancel:
			dismiss();
			break;
		default:
		  break;
		}
		dismiss();
		}
		}