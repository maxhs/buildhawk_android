package com.buildhawk;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import rmn.androidscreenlibrary.ASSL;

import com.buildhawk.DocumentFragment.LazyAdapter.ViewHolder;
import com.buildhawk.SubsList.adapter;

import com.buildhawk.utils.ProjectPhotos;
import com.buildhawk.utils.Users;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.Photos;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedFolder extends Activity {


//	LinearLayout relLay;
//	Dialog popup;
//	Button btn_Submit, btn_Cancel;
//	EditText txt_hours, txt_location;
//	TextView tv_expiry_alert;
//	RelativeLayout list_outside;
//	InputMethodManager imm;
	ListView listview;
	ArrayList<ProjectPhotos> arraylist;
	RelativeLayout relativelayoutBack;
	LinearLayout linearlayoutroot;
	Context con;
	ArrayList<String>foldersNameArraylist= new ArrayList<String>();
//	ArrayList<String>ProDocFold;
//	ArrayList<String>ProDocFolders;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selected_folder);
		
		linearlayoutroot = (LinearLayout) findViewById(R.id.linearlayoutroot);
		new ASSL(this, linearlayoutroot, 1134, 720, false);
		
		con = SelectedFolder.this;
//		relativelayoutBack = (TextView) findViewById(R.id.back);
		relativelayoutBack=(RelativeLayout)findViewById(R.id.relativelayoutBack);

		listview = (ListView) findViewById(R.id.subslist);

//		relativelayoutBack.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));

		arraylist = DocumentFragment.photosList2ArrayList;
		
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
		getMenuInflater().inflate(R.menu.selected_folder, menu);
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
			return DocumentFragment.ProDocFolders.size();
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
			
			
			int size=0;
			for(int i=0;i<DocumentFragment.photosList2ArrayList.size();i++)
			{
				if(DocumentFragment.photosList2ArrayList.get(i).folder.coName.toString().equalsIgnoreCase(DocumentFragment.ProDocFolders.get(position).toString()))
				{
					size++;
				}
			}
			if(size>1)
				holder.textviewName.setText(DocumentFragment.ProDocFolders.get(position).toString()+" - "+size +" items");
			else
				holder.textviewName.setText(DocumentFragment.ProDocFolders.get(position).toString()+" - "+size +" item");
			
			for(int i=0;i<DocumentFragment.photosList2ArrayList.size();i++)
			{
			if(DocumentFragment.ProDocFolders.get(position).toString().equalsIgnoreCase(DocumentFragment.photosList2ArrayList.get(i).folder.coName.toString()))
			Picasso.with(con).load(DocumentFragment.photosList2ArrayList.get(i).url200.toString())
			.placeholder(R.drawable.default_200).into(holder.imageview);
			}
			
			holder.linearlayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				Intent intent = new Intent(con,SelectedImage.class);			
				intent.putExtra("key", DocumentFragment.ProDocFolders.get(position).toString());
				
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
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
