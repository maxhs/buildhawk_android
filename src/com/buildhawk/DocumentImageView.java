package com.buildhawk;

/*
 *  This file is used to show project Docs images.
 */
		
import rmn.androidscreenlibrary.ASSL;

import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DocumentImageView extends Activity {
	ImageView imageviewSelected;
	private ZoomView zoomView;
	TextView textviewSelectedImage;
	RelativeLayout relativeLayoutMain, relativelayoutBack, relativelayoutrootDocImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_image_view);
		
		relativelayoutrootDocImage = (RelativeLayout) findViewById(R.id.relativelayoutrootDocImage);
		new ASSL(this, relativelayoutrootDocImage, 1134, 720, false);
		
		int position;
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		relativeLayoutMain = (RelativeLayout) findViewById(R.id.RelLayout);
		textviewSelectedImage=(TextView)findViewById(R.id.textviewSelectedImage);
		zoomView = new ZoomView(this);
		Log.d("kjhudjklkhdkijlh",
				"kjhwkhkjgkgjklwhl"
						+ SelectedImage.arraylist.get(position).urlLarge.toString()
						+ "helll " + position);
		imageviewSelected = new ImageView(this);
		imageviewSelected.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));//.LayoutParams(LayoutParams.FILL_PARENT, 100))
//		imageviewSelected.setLayoutParams(LayoutParams.(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//		imageviewSelected.getLayoutParams().width = 720;

		Picasso.with(DocumentImageView.this)
				.load(SelectedImage.arraylist.get(position).urlLarge.toString())
				.placeholder(R.drawable.processing_image).into(imageviewSelected);
		
		textviewSelectedImage.setText(SelectedImage.arraylist.get(position).name);
		
		relativeLayoutMain.addView(zoomView);
		
		zoomView.addView(imageviewSelected);
		relativelayoutBack=(RelativeLayout)findViewById(R.id.relativelayoutBack);
		relativelayoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.document_image_view, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_right);
	}

}
