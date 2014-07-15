package com.buildhawk;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.SynopsisCategories;
import com.buildhawk.utils.SynopsisProject;
import com.buildhawk.utils.SynopsisRecentDocuments;
import com.buildhawk.utils.SynopsisRecentlyCompleted;
import com.buildhawk.utils.SynopsisUpcomingItems;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

public class Synopsis extends Activity {

	ListView syncatlist, synupitemlist, syncomplist;
	Context con;
	TextView tv_addres;
	LinearLayout lin;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	ArrayList<SynopsisProject> SynProject = new ArrayList<SynopsisProject>();
	ArrayList<SynopsisCategories> SynCateg = new ArrayList<SynopsisCategories>();
	ArrayList<SynopsisUpcomingItems> SynItems = new ArrayList<SynopsisUpcomingItems>();
	ArrayList<SynopsisRecentDocuments> SynDoc = new ArrayList<SynopsisRecentDocuments>();
	ArrayList<SynopsisRecentlyCompleted> Syncomp = new ArrayList<SynopsisRecentlyCompleted>();

	ArrayList<String> arr = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	Boolean pull = false;
	// ArrayList<SynopsisCategories> synopcat;
	// ArrayList<SynopsisUpcomingItems> synoupitem;
	// ArrayList<SynopsisRecentDocuments> synopdoc;
	// ArrayList<SynopsisRecentlyCompleted> syncomp;
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	PointF startPoint = new PointF();
	PointF midPoint = new PointF();
	float oldDist = 1f;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;

	TextView tv_proName;
	int mode = NONE;
	Activity act;
	HorizontalScrollView hScrollView;
	RelativeLayout back;
	LinearLayout relLay;
	PullToRefreshScrollView scrollView;
	Button btn_Goto;
	TextView tv_headerProjSynopsis, tv_headerprogress, tv_headerRecentDocs,
			tv_headerUpcomingItems, tv_headerRecentItems;
	ImageView img_ladder_page2;
	 SharedPreferences sharedpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synopsis);
		// Prefrences.dismissLoadingDialog();
		relLay = (LinearLayout) findViewById(R.id.LinearLayoutSynopsis);
		new ASSL(this, relLay, 1134, 720, false);
		sharedpref = Synopsis.this.getSharedPreferences("MyPref", 0); // 0 - for private mode
		
		
		act = this;
		back = (RelativeLayout) findViewById(R.id.back);
		con = Synopsis.this;
		hScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		syncatlist = (ListView) findViewById(R.id.syncatlist);
		synupitemlist = (ListView) findViewById(R.id.synupitems);
		syncomplist = (ListView) findViewById(R.id.syncomplist);
		// synopcat = ProjectsAdapter.SynCateg;
		// synoupitem = ProjectsAdapter.SynItems;
		// synopdoc=ProjectsAdapter.SynDoc;
		// syncomp = ProjectsAdapter.Syncomp;
		tv_addres = (TextView) findViewById(R.id.addres);
		tv_proName = (TextView) findViewById(R.id.proName);
		tv_proName.setText(Prefrences.selectedProName);
		btn_Goto = (Button) findViewById(R.id.GotoProject);

		tv_headerProjSynopsis = (TextView) findViewById(R.id.textView1);
		tv_headerprogress = (TextView) findViewById(R.id.textView2);
		tv_headerRecentDocs = (TextView) findViewById(R.id.textView3);
		tv_headerUpcomingItems = (TextView) findViewById(R.id.textView4);
		tv_headerRecentItems = (TextView) findViewById(R.id.textView5);
		tv_headerProjSynopsis.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		tv_headerprogress.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		tv_headerRecentDocs.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		tv_headerUpcomingItems.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		tv_headerRecentItems.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		tv_addres.setText(Homepage.projectsList.get(0).address.formattedAddress); // get(position)

		tv_addres.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		tv_proName.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		btn_Goto.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));

		scrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView1);

		// scrollView.post(new Runnable() {
		// public void run() {
		// scrollView.fullScroll(ScrollView.FOCUS_UP);
		// }
		// });

		scrollView.scrollTo(0, 0);
		scrollView.scrollBy(0, 0);

		lin = (LinearLayout) findViewById(R.id.scrolllayout);
		img_ladder_page2 = new ImageView(Synopsis.this);

		scrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				pull = true;
				synopsisHit();

			}

		});

		// list.setOnRefreshListener(new OnRefreshListener2<ListView>() {
		//
		// public void onPullDownToRefresh(PullToRefreshBase<ListView>
		// refreshView) {
		//
		// projectDetail(getActivity());
		// }
		//
		// @Override
		// public void onPullUpToRefresh(
		// PullToRefreshBase<ListView> refreshView) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// });

		btn_Goto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Prefrences.stopingHit=1;
				startActivity(new Intent(con, ProjectDetail.class));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				
				// ProjectsAdapter.projectDetail(act);
			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				// ChecklistFragment.listDataHeader.clear();
				// ChecklistFragment.completeHeader.clear();
				// ChecklistFragment.activeHeader.clear();
				// ChecklistFragment.progressarr.clear();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Prefrences.pageFlag = 0;
		connDect = new ConnectionDetector(Synopsis.this);
		isInternetPresent = connDect.isConnectingToInternet();
		if (Prefrences.stopingHit == 1) {
			Prefrences.stopingHit = 0;
			if (isInternetPresent) {
				synopsisHit();
			}else{
				String response;
				response = sharedpref.getString("projectSynopsis", "");
				if(response.equalsIgnoreCase("")){
					Toast.makeText(Synopsis.this,"No internet connection.", Toast.LENGTH_SHORT).show();
				}else{
					fillServerData(response);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void setlist(ListView lv, ListAdapter listAdapter, int size) {
		int maxHeight = 0;
		listAdapter = lv.getAdapter();

		// int totalHeight = listAdapter.getCount() * imageWidth;
		int totalHeight = 0;
		View listItem = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {

			listItem = listAdapter.getView(i, listItem, lv);
			if (listItem instanceof ViewGroup)
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			listItem.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += (int) (size * ASSL.Yscale());

			// totalHeight += listItem.getMeasuredHeight();
		}
		maxHeight += totalHeight;
		ViewGroup.LayoutParams params = lv.getLayoutParams();

		params.height = totalHeight
				+ (lv.getDividerHeight() * (listAdapter.getCount() - 1));

		lv.setLayoutParams(params);
		lv.requestLayout();

		// try {
		// int totalHeight
		// ListAdapter listAdapterTheir = syncomplist
		// .getAdapter();
		//
		// for (int i = 0; i < listAdapterTheir
		// .getCount(); i++) {
		// View listItem = listAdapterTheir
		// .getView(i, null,
		// syncomplist);
		// //listItem.measure(0, 0);
		// listItem.measure(
		// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		// totalHeight += listItem.getMeasuredHeight();
		//
		// ViewGroup.LayoutParams params = syncomplist
		// .getLayoutParams();
		// params.height = totalHeight + (syncomplist.getDividerHeight() *
		// (syncomp.size()));
		// // params.height = 214 *
		// // details_comment_adapter.getCount();
		// syncomplist.setLayoutParams(params);
		// syncomplist.requestLayout();
		//
		// }
		//
		// } catch (Exception e) {
		// Log.e("error in expand ", e.toString());
		// }

	}

	public class adapter extends BaseAdapter {

		ArrayList<SynopsisCategories> synopcat;
		Context con;

		public adapter(Context con, ArrayList<SynopsisCategories> synopcat) {
			// TODO Auto-generated constructor stub
			this.con = con;
			this.synopcat = synopcat;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return synopcat.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return synopcat.get(position);
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
			final SynopsisCategories sycat = (SynopsisCategories) this
					.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new viewholder();
				LayoutInflater inflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.synocateglisti_tem,
						null);
				convertView.setTag(holder);
			} else {
				holder = (viewholder) convertView.getTag();
			}
			holder.prog_bar = (ProgressBar) convertView
					.findViewById(R.id.progressBar1);
			holder.tv1 = (TextView) convertView.findViewById(R.id.syncategname);
			holder.tv2 = (TextView) convertView.findViewById(R.id.percent);
			holder.tv1.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.tv2.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.llayout = (LinearLayout) convertView
					.findViewById(R.id.llayout);
			holder.tv1.setText(sycat.name.toString());
			Float per = 0.00f, float1 = 0.00f, float2 = 0.00f;
			float1 = Float.parseFloat(sycat.itemCount.toString());
			float2 = Float.parseFloat(sycat.progressCount.toString());
			per = (float2 / float1 * 100);
			String str = String.format("%.1f", per);
			int percentage_val = Math.round(per);
			for (int i = 0; i < percentage_val; i++) {
				holder.prog_bar.setProgress(i);
			}
			holder.tv2.setText(str + "%");// sycat.progressCount.toString());
			holder.llayout.setLayoutParams(new ListView.LayoutParams(720, 100));

			ASSL.DoMagic(holder.llayout);
			holder.llayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Prefrences.showLoadingDialog(con, "Loading...");
					Prefrences.stopingHit=1;
					startActivity(new Intent(con, ProjectDetail.class));
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					// ProjectsAdapter.projectDetail(act);
					// finish();

				}
			});
			return convertView;
		}

	}

	private static class viewholder {
		TextView tv1, tv2;
		ProgressBar prog_bar;
		LinearLayout llayout;
	}

	public class adapter2 extends BaseAdapter {

		ArrayList<SynopsisUpcomingItems> synopitem;
		Context con;

		public adapter2(Context con, ArrayList<SynopsisUpcomingItems> synopitem) {
			// TODO Auto-generated constructor stub
			this.con = con;
			this.synopitem = synopitem;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return synopitem.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return synopitem.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			viewholder2 holder;
			final SynopsisUpcomingItems syitem = (SynopsisUpcomingItems) this
					.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new viewholder2();
				LayoutInflater inflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.synodoclist_item, null);

				convertView.setTag(holder);
			} else {
				holder = (viewholder2) convertView.getTag();
			}

			holder.tv1 = (TextView) convertView
					.findViewById(R.id.synupitemsbody);
			holder.tv1.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			// holder.tv2=(TextView)convertView.findViewById(R.id.percent);
			holder.llayout = (LinearLayout) convertView
					.findViewById(R.id.layoutt);
			holder.tv1.setText(syitem.body.toString());

			holder.img = (ImageView) convertView.findViewById(R.id.img);

			if (syitem.itemType.toString().equals("Doc")) {
				holder.img.setBackgroundResource(R.drawable.document);
			} else if (syitem.itemType.toString().equals("Com")) {
				holder.img.setBackgroundResource(R.drawable.mobile);
			} else if (syitem.itemType.toString().equals("S&C")) {
				holder.img
						.setBackgroundResource(R.drawable.hands_outline_black);
			}

			holder.llayout.setLayoutParams(new ListView.LayoutParams(720, 100));// ListView.LayoutParams.WRAP_CONTENT));

			ASSL.DoMagic(holder.llayout);

			holder.llayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Prefrences.selectedCheckitem = 0;
					Intent intent = new Intent(con, CheckItemClick.class);
					intent.putExtra("body", syitem.body.toString());
					intent.putExtra("itemtype", syitem.itemType.toString());
					intent.putExtra("status", syitem.status.toString());
					intent.putExtra("id", syitem.id.toString());
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			});

			// Float per=0.00f,f1=0.00f,f2=0.00f;
			// f1 = Float.parseFloat(sycat.itemCount.toString());
			// f2= Float.parseFloat(sycat.progressCount.toString());
			// per=f2/f1*100;
			// holder.tv2.setText(per+"%");//sycat.progressCount.toString());
			return convertView;
		}

	}

	private static class viewholder2 {
		TextView tv1;
		ImageView img;
		LinearLayout llayout;
	}

	public class adapter3 extends BaseAdapter {

		ArrayList<SynopsisRecentlyCompleted> synopcomp;
		Context con;

		public adapter3(Context con,
				ArrayList<SynopsisRecentlyCompleted> synopcomp) {
			// TODO Auto-generated constructor stub
			this.con = con;
			this.synopcomp = synopcomp;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return synopcomp.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return synopcomp.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			viewholder2 holder;
			final SynopsisRecentlyCompleted sycomp = (SynopsisRecentlyCompleted) this
					.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new viewholder2();
				LayoutInflater inflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.synodoclist_item, null);
				convertView.setTag(holder);
			} else {
				holder = (viewholder2) convertView.getTag();
			}

			holder.tv1 = (TextView) convertView
					.findViewById(R.id.synupitemsbody);
			holder.tv1.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.llayout = (LinearLayout) convertView
					.findViewById(R.id.layoutt);
			holder.tv1.setText(sycomp.body.toString());

			// Log.d("0-----","******* "+holder.tv1.getLineSpacingMultiplier());

			holder.img = (ImageView) convertView.findViewById(R.id.img);

			if (sycomp.itemType.toString().equals("Doc")) {
				holder.img.setBackgroundResource(R.drawable.document);
			} else if (sycomp.itemType.toString().equals("Com")) {
				holder.img.setBackgroundResource(R.drawable.mobile);
			} else if (sycomp.itemType.toString().equals("S&C")) {
				holder.img
						.setBackgroundResource(R.drawable.hands_outline_black);
			}

			holder.llayout.setLayoutParams(new ListView.LayoutParams(720, 100));// ListView.LayoutParams.WRAP_CONTENT));

			ASSL.DoMagic(holder.llayout);
			holder.llayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Prefrences.selectedCheckitem = 1;
					Intent intent = new Intent(con, CheckItemClick.class);
					intent.putExtra("body", sycomp.body.toString());
					intent.putExtra("itemtype", sycomp.itemType.toString());
					intent.putExtra("status", sycomp.status.toString());
					intent.putExtra("id", sycomp.id.toString());
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			});

			return convertView;
		}

	}

	public void synopsisHit() {
		Prefrences.showLoadingDialog(Synopsis.this, "Loading...");

		RequestParams params = new RequestParams();

		params.put("id", Prefrences.selectedProId);

		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		client.setTimeout(100000);
		client.get(Prefrences.url + "/projects/dash", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);

						fillServerData(response);
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Toast.makeText(getApplicationContext(), "Server Issue",
								Toast.LENGTH_LONG).show();

						Prefrences.dismissLoadingDialog();
						if (pull == true) {
							pull = false;
							scrollView.onRefreshComplete();
						}
					}
				});
	}
	
	public void fillServerData(String response){
		JSONObject res = null;
		Editor editor = sharedpref.edit();
		editor.putString("projectSynopsis", response);
		editor.commit();
		try {
			res = new JSONObject(response);
			SynCateg.clear();
			Syncomp.clear();
			SynDoc.clear();
			SynItems.clear();
			SynProject.clear();
			JSONObject project = res.getJSONObject("project");
			Log.v("Synopsis value", project.toString());

			JSONArray Syncategories = project
					.getJSONArray("categories");
			JSONArray Syncompl = project
					.getJSONArray("recently_completed");
			JSONArray Syndocs = project
					.getJSONArray("recent_documents");
			JSONArray Synitems = project
					.getJSONArray("upcoming_items");
			for (int i = 0; i < Syncategories.length(); i++) {

				JSONObject count = Syncategories
						.getJSONObject(i);

				SynCateg.add(new SynopsisCategories(count
						.getString("name"), count
						.getString("item_count"), count
						.getString("completed_count"), count
						.getString("progress_count"), count
						.getString("order_index")));

			}
			for (int j = 0; j < Syncompl.length(); j++) {
				JSONObject count2 = Syncompl.getJSONObject(j);
				Syncomp.add(new SynopsisRecentlyCompleted(
						count2.getString("id"), count2
								.getString("body"), count2
								.getString("status"), count2
								.getString("item_type"), count2
								.getString("photos_count"),
						count2.getString("comments_count")));

				Log.d("Syncomp", "Syncomp"
						+ Syncomp.get(j).itemType);
			}
			for (int k = 0; k < Syndocs.length(); k++) {
				JSONObject count = Syndocs.getJSONObject(k);
				SynDoc.add(new SynopsisRecentDocuments(count
						.getString("id"), count
						.getString("url_large"), count
						.getString("original"), count
						.getString("url_small"), count
						.getString("url_thumb"), count
						.getString("image_file_size"), count
						.getString("image_content_type"), count
						.getString("source"), count
						.getString("phase"), count
						.getString("created_at"), count
						.getString("user_name"), count
						.getString("name"), count
						.getString("created_date")));

			}
			for (int l = 0; l < Synitems.length(); l++) {
				JSONObject count = Synitems.getJSONObject(l);
				SynItems.add(new SynopsisUpcomingItems(count
						.getString("id"), count
						.getString("body"), count
						.getString("critical_date"), count
						.getString("completed_date"), count
						.getString("status"), count
						.getString("item_type"), count
						.getString("photos_count"), count
						.getString("comments_count")));

				Log.d("SynItems", "SynItems"
						+ SynItems.get(l).itemType);

			}

			SynProject.add(new SynopsisProject(project
					.getString("progress"), SynItems, Syncomp,
					SynDoc, SynCateg));
			for (int j = 0; j < SynProject.size(); j++) {
				Log.d("",
						"cate"
								+ SynProject.get(j).categories
										.size());
				Log.d("",
						"comp"
								+ SynProject.get(j).recentlyCompleted
										.size());
				Log.d("",
						"items"
								+ SynProject.get(j).upcomingItems
										.size());
				Log.d("",
						"docs"
								+ SynProject.get(j).recentDocuments
										.size());
			}

			adapter adp = new adapter(con, SynCateg);
			syncatlist.setAdapter(adp);

			adapter2 adp2 = new adapter2(con, SynItems);
			synupitemlist.setAdapter(adp2);

			adapter3 adp3 = new adapter3(con, Syncomp);
			syncomplist.setAdapter(adp3);
			setlist(syncatlist, adp, 100);
			setlist(synupitemlist, adp2, 100);
			setlist(syncomplist, adp3, 100);

			arr.clear();
			ids.clear();
			lin.removeAllViews();
			lin = (LinearLayout) findViewById(R.id.scrolllayout);
			for (int i = 0; i < SynDoc.size(); i++) {

				arr.add(SynDoc.get(i).urlLarge);
				ids.add(SynDoc.get(i).id);
				img_ladder_page2 = new ImageView(Synopsis.this);

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						(int) (200), (int) (200));
				lp.setMargins(10, 10, 10, 10);

				img_ladder_page2.setTag(i);
				img_ladder_page2.setLayoutParams(lp);
				// Picasso.with(con).load(arr.get(i).toString()).into(ladder_page2);

				// File file = new File(SynDoc.get(i).urlSmall);
				Picasso.with(con)
						.load(SynDoc.get(i).urlSmall.toString())
						.resize((int) (200 * ASSL.Xscale()),
								(int) (200 * ASSL.Yscale()))
						.into(img_ladder_page2);
				// ladder_page2.setImageBitmap(myBitmap);
				lin.addView(img_ladder_page2);
				img_ladder_page2
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Prefrences.selectedPic = (Integer) v
										.getTag();
								Log.i("Tag Value",
										""
												+ Prefrences.selectedPic);

								Intent intent = new Intent(
										Synopsis.this,
										SelectedImageView.class);
								intent.putExtra("array", arr);
								intent.putExtra("ids", ids);
								intent.putExtra(
										"id",
										SynDoc.get(Prefrences.selectedPic).id);
								startActivity(intent);
								overridePendingTransition(
										R.anim.slide_in_right,
										R.anim.slide_out_left);
								// finish();
							}
						});

			}

			if (pull == true) {
				pull = false;
				scrollView.onRefreshComplete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
