package com.buildhawk;

import java.util.ArrayList;

import rmn.androidscreenlibrary.ASSL;

import com.buildhawk.SelectedFolder.ViewHolder;
import com.buildhawk.SelectedFolder.adapter;
import com.buildhawk.utils.ProjectPhotos;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SelectedImage extends Activity {

	ListView listview;
	public static ArrayList<ProjectPhotos> arraylist;
	ArrayList<String> type;
	
	RelativeLayout relativelayoutBack;
	String keyString;
	Context con;
	ArrayList<String>foldersNameArrayList= new ArrayList<String>();
	LinearLayout linearlayoutroot;
	TextView textviewSelectedFolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_image);
		
		linearlayoutroot = (LinearLayout) findViewById(R.id.linearlayoutroot);
		new ASSL(this, linearlayoutroot, 1134, 720, false);
		
		con = SelectedImage.this;
//		relativelayoutBack = (TextView) findViewById(R.id.back);
		relativelayoutBack=(RelativeLayout)findViewById(R.id.relativelayoutBack);
		Bundle bundle = getIntent().getExtras();
		keyString = bundle.getString("key");

		listview = (ListView) findViewById(R.id.subslist);
		textviewSelectedFolder = (TextView)findViewById(R.id.textviewSelectedFolder);
		
		

//		relativelayoutBack.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		textviewSelectedFolder.setText(keyString);
		arraylist = new ArrayList<ProjectPhotos>();
		
		for(int i=0;i<DocumentFragment.photosList2ArrayList.size();i++)
		{
			if(DocumentFragment.photosList2ArrayList.get(i).folder.coName.toString().equalsIgnoreCase(keyString))
			{
				arraylist.add(DocumentFragment.photosList2ArrayList.get(i));
			}
		}

		Log.d("--------array-------","--------array---------"+arraylist.size());
		
//		for(int i=0; i<DocumentFragment.photosList2ArrayList.size();i++)
//		{
//			if(!DocumentFragment.photosList2ArrayList.get(i).folder.equals(null))
//				ProDocFold.add(DocumentFragment.photosList2ArrayList.get(i).folder.coName);
////		loc.addAll(WorklistFragment.locs);
//		}
//		LinkedHashSet<String> listToSet = new LinkedHashSet<String>(ProDocFold);
//		
//		ProDocFolders = new ArrayList<String>(listToSet);
		
		
		relativelayoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		adapter adp = new adapter(con, arraylist);
		listview.setAdapter(adp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selected_image, menu);
		return true;
	}
	
	public class adapter extends BaseAdapter {

		ArrayList<ProjectPhotos> arrayList;
		Context con;
		private LayoutInflater inflater;
		ViewHolder holder;

		public adapter(Context con, ArrayList<ProjectPhotos> arrayList) {
			// TODO Auto-generated constructor stub
			
			this.arrayList = arrayList;
			this.con = con;
			inflater = (LayoutInflater) con.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.d("sublist", "Size=========" + arrayList.size());
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View vi = convertView;

			if (convertView == null)
				holder = new ViewHolder();

			vi = inflater.inflate(R.layout.documentlist_item, null);
			vi.setTag(holder);
			holder.textviewName = (TextView) vi.findViewById(R.id.name);
			holder.imageview = (ImageView) vi.findViewById(R.id.image);
			holder.linearlayout = (LinearLayout) vi.findViewById(R.id.relLay);
			holder.textviewName.setTypeface(Prefrences.helveticaNeuelt(con));

			holder.linearlayout.setLayoutParams(new ListView.LayoutParams(720, 230));

			ASSL.DoMagic(holder.linearlayout);
			
			holder.textviewName.setText(arrayList.get(position).name.toString());
//			for(int i=0;i<DocumentFragment.photosList2ArrayList.size();i++)
//			{
//			if(DocumentFragment.ProDocFolders.get(position).toString().equalsIgnoreCase(DocumentFragment.photosList2ArrayList.get(i).folder.coName.toString()))
			Picasso.with(con).load(arrayList.get(position).url200.toString())
			.placeholder(R.drawable.default_200).into(holder.imageview);
//			}
			
			
			
			holder.linearlayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
//					Log.i("Tag Value", "" + Prefrences.selectedPic);
//					
//					Log.i("Tag Value", "" + Prefrences.selectedPic);

					Intent intent = new Intent(con, DocumentImageView.class);
					intent.putExtra("position",position);
					intent.putExtra("type", type);
//					intent.putExtra("ids", ids);
//					intent.putExtra("desc", desc);
//					intent.putExtra("id", photosArrayList
//							.get(Prefrences.selectedPic).id);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					// finish();
				}
			});
			
			
			
			return vi;
		}

	}

	class ViewHolder {

		private TextView textviewName;
		private ImageView imageview;
		LinearLayout linearlayout;

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
