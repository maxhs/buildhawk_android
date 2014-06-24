package com.buildhawk;

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

public class DialogBoxSort extends Dialog implements
		android.view.View.OnClickListener {

	public Activity activity;

	LinearLayout linearLay;
	public Dialog dialog;
	Button btnSort, btnUsers, btnSub, btnCancel;
	TextView who;
	String key;

	public DialogBoxSort(Activity activity, String key) {

		super(activity);
		this.activity = activity;
		this.key = key;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialogbox_sort);
		linearLay = (LinearLayout) findViewById(R.id.db);
		new ASSL(activity, linearLay, 1134, 720, false);
		btnSort = (Button) findViewById(R.id.btn_sort);
		btnUsers = (Button) findViewById(R.id.btn_user);
		who = (TextView) findViewById(R.id.txt_who);
		btnSub = (Button) findViewById(R.id.btn_sub);
		btnCancel = (Button) findViewById(R.id.cancel);

		btnCancel.setTypeface(Prefrences.helveticaNeuelt(activity));
		btnSort.setTypeface(Prefrences.helveticaNeuelt(activity));
		btnSub.setTypeface(Prefrences.helveticaNeuelt(activity));
		btnUsers.setTypeface(Prefrences.helveticaNeuelt(activity));

		btnSort.setOnClickListener(this);
		btnUsers.setOnClickListener(this);
		btnSub.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		who.setText("Sort");
		if (key.equals("All")) {
			btnSort.setText("By Taken/Uploaded");
			btnUsers.setText("By Date");
			btnSub.setText("By Default");
		} else if (key.equals("Checklist")) {
			btnSort.setText("By Taken/Uploaded");
			btnUsers.setText("By Date");
			btnSub.setText("By Default");
		} else if (key.equals("Worklist")) {
			btnSort.setText("By Taken/Uploaded");
			btnUsers.setText("By Date");
			btnSub.setText("By Default");

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_user:
			if (key.equals("All")) {
				dismiss();
			}
			break;
		case R.id.btn_sub:
			dismiss();

			break;
		case R.id.btn_sort:
			dismiss();
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