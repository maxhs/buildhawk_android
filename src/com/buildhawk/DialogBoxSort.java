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
/*
 *  Dialog box with 3 types of sorting.
 * 
 */
public class DialogBoxSort extends Dialog implements
		android.view.View.OnClickListener {

	public Activity activity;

	LinearLayout linearlayoutRoot;
	public Dialog dialog;
	Button buttonSort, buttonUsers, buttonSub, buttonCancel;
	TextView textviewWho;
	String keyString;

	public DialogBoxSort(Activity activity, String keyString) {

		super(activity);
		this.activity = activity;
		this.keyString = keyString;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialogbox_sort);
		linearlayoutRoot = (LinearLayout) findViewById(R.id.linearlayoutRoot);
		new ASSL(activity, linearlayoutRoot, 1134, 720, false);
		buttonSort = (Button) findViewById(R.id.buttonSort);
		buttonUsers = (Button) findViewById(R.id.buttonUsers);
		textviewWho = (TextView) findViewById(R.id.textviewWho);
		buttonSub = (Button) findViewById(R.id.buttonSub);
		buttonCancel = (Button) findViewById(R.id.buttonCancel);

		buttonCancel.setTypeface(Prefrences.helveticaNeuelt(activity));
		buttonSort.setTypeface(Prefrences.helveticaNeuelt(activity));
		buttonSub.setTypeface(Prefrences.helveticaNeuelt(activity));
		buttonUsers.setTypeface(Prefrences.helveticaNeuelt(activity));

		buttonSort.setOnClickListener(this);
		buttonUsers.setOnClickListener(this);
		buttonSub.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);
		textviewWho.setText("Sort");
		if (keyString.equals("All")) {
			buttonSort.setText("By Taken/Uploaded");
			buttonUsers.setText("By Date");
			buttonSub.setText("By Default");
		} else if (keyString.equals("Checklist")) {
			buttonSort.setText("By Taken/Uploaded");
			buttonUsers.setText("By Date");
			buttonSub.setText("By Default");
		} else if (keyString.equals("Worklist")) {
			buttonSort.setText("By Taken/Uploaded");
			buttonUsers.setText("By Date");
			buttonSub.setText("By Default");

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonUsers:
			if (keyString.equals("All")) {
				dismiss();
			}
			break;
		case R.id.buttonSub:
			dismiss();

			break;
		case R.id.buttonSort:
			dismiss();
			break;
		case R.id.buttonCancel:
			dismiss();
			break;

		default:
			break;
		}
		dismiss();
	}
}