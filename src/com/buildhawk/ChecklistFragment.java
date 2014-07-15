package com.buildhawk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.buildhawk.WorkListCompleteAdapter.ViewHolder;
import com.buildhawk.utils.Categories;
import com.buildhawk.utils.CheckItems;
import com.buildhawk.utils.CheckList;
import com.buildhawk.utils.CommentsChecklistItem;
import com.buildhawk.utils.CommentsUserChecklistItem;
import com.buildhawk.utils.DataCheckListItems;
import com.buildhawk.utils.PhotosCheckListItem;
import com.buildhawk.utils.ProjectsFields;
import com.buildhawk.utils.SubCategories;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ChecklistFragment extends Fragment {

	public static ArrayList<Categories> categList = new ArrayList<Categories>();

	public static ArrayList<Categories> pcategList = new ArrayList<Categories>();
	public static ArrayList<Categories> activelist = new ArrayList<Categories>();
	public static ArrayList<Categories> completelist = new ArrayList<Categories>();
	public static ArrayList<CheckList> checkList = new ArrayList<CheckList>();
	public static ArrayList<CheckItems> checkall = new ArrayList<CheckItems>();

	public static ArrayList<CheckItems> progressList2 = new ArrayList<CheckItems>();

	public ArrayList<SubCategories> subCatList;
	public static ArrayList<SubCategories> activeSubCatList;
	public static ArrayList<SubCategories> completeSubCatList;
	public static ArrayList<SubCategories> psubCatList;

	ExpandableListView actualListview;
	public ArrayList<ProjectsFields> projectsListAll = new ArrayList<ProjectsFields>();
	ConnectionDetector connDect;
	View footerView;
	Boolean pull = false;
	PullToRefreshExpandableListView expandlist;
	ListView searchlist;
	PullToRefreshListView progressList;
	TextView tv_active;
	TextView tv_all;
	TextView tv_progress;
	TextView tv_complete;
	EditText txt_searchcheck;
	public static RelativeLayout search_rel;
	Button btn_cancel;
	Button btn_sendcomment;
	RelativeLayout relLay;
	
	ParentLevel listAdapter;
	Boolean isInternetPresent = false;
	checkAdapter checkadapter;
	Handler mHandler = new Handler();
	progressAdapter adapter;
	// PullToRefreshListView list;

	public static ArrayList<CommentsChecklistItem> comments = new ArrayList<CommentsChecklistItem>();
	public static ArrayList<PhotosCheckListItem> photos = new ArrayList<PhotosCheckListItem>();
	public static ArrayList<DataCheckListItems> checklistitemdata = new ArrayList<DataCheckListItems>();

	// private ArrayList<Categories> progressarray;
//	public static ArrayList<CheckItems> progressarr;
	static Context con;
	SharedPreferences sharedpref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.checklist, container, false);

		relLay = (RelativeLayout) root.findViewById(R.id.rellay);
		new ASSL(getActivity(), relLay, 1134, 720, false);
		Prefrences.clear = 1;
		con = getActivity();

		// list = (PullToRefreshListView) root.findViewById(R.id.pullList);

		search_rel=(RelativeLayout)root.findViewById(R.id.search_rel);
		tv_active = (TextView) root.findViewById(R.id.active);
		tv_all = (TextView) root.findViewById(R.id.all);
		tv_progress = (TextView) root.findViewById(R.id.progress);
		tv_complete = (TextView) root.findViewById(R.id.completed);
		txt_searchcheck = (EditText) root.findViewById(R.id.checksearch);
		btn_cancel=(Button)root.findViewById(R.id.cancelSearch);

		sharedpref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
		expandlist = (PullToRefreshExpandableListView) root
				.findViewById(R.id.expandList);

		search_rel.setVisibility(View.GONE);
		progressList = (PullToRefreshListView) root
				.findViewById(R.id.progressList);
		searchlist = (ListView) root.findViewById(R.id.checksearchlist);

		tv_active.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		tv_all.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		tv_complete.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		tv_progress.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		Prefrences.checklisttypes=1;
		// Log.v("count..",","+ProjectsAdapter.categList.size()+", "+ sAdapter.categList.get(0).subCat.size()+","+ProjectsAdapter.categList.get(0).subCat.get(0).checkItems.size());

		progressList.setVisibility(View.GONE);
		expandlist.setVisibility(View.VISIBLE);
		
		if(Prefrences.checklist_s.equalsIgnoreCase("")){
			Prefrences.checklist_bool=false;
		}

		actualListview = expandlist.getRefreshableView();

		progressList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				pull = true;
				projectDetail(getActivity());
			}
		});

		
		expandlist
				.setOnRefreshListener(new OnRefreshListener<ExpandableListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ExpandableListView> refreshView) {
						// TODO Auto-generated method stub
						pull = true;
						projectDetail(getActivity());
					}
				});

		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search_rel.setVisibility(View.GONE);
				searchlist.setVisibility(View.GONE);
			}
		});
		
		txt_searchcheck.setOnEditorActionListener(new OnEditorActionListener() {
		  	@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {		  		
				// TODO Auto-generated method stub
		  		if (actionId == EditorInfo.IME_ACTION_DONE) {
//		                Toast.makeText(ACTIVITY_NAME.this, etSearchFriends.getText(),Toast.LENGTH_SHORT).show();
		  							
					 InputMethodManager imm = (InputMethodManager) con
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(txt_searchcheck.getWindowToken(),
								0);
					
					 
		              return true;
		            }
		  		else{
				return false;
		  		}
			}
		});

		txt_searchcheck.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence charSeq, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// progressList.setVisibility(View.VISIBLE);
				Log.i("ARG", "-------" + arg0);
				ChecklistFragment.this.checkadapter.search2(arg0.toString());
			}
		});

		tv_active.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.checklisttypes=2;
				 listAdapter = new ParentLevel(con, activelist);

				actualListview.setAdapter(listAdapter);
				expandlist.setVisibility(View.VISIBLE);

				progressList.setVisibility(View.GONE);
				tv_active.setBackgroundResource(R.color.black);
				tv_all.setBackgroundResource(R.drawable.all_white_background);
				tv_progress.setBackgroundResource(R.color.white);
				tv_complete.setBackgroundResource(R.drawable.complete_white_background);

				tv_active.setTextColor(Color.WHITE);
				tv_complete.setTextColor(Color.BLACK);
				tv_all.setTextColor(Color.BLACK);
				tv_progress.setTextColor(Color.BLACK);

			}
		});
		tv_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Prefrences.checklisttypes=1;
				 listAdapter = new ParentLevel(con, categList);
				actualListview.setAdapter(listAdapter);
				expandlist.setVisibility(View.VISIBLE);
				progressList.setVisibility(View.GONE);
				tv_active.setBackgroundResource(R.color.white);
				tv_progress.setBackgroundResource(R.color.white);
				tv_all.setBackgroundResource(R.drawable.all_black_background);
				tv_complete.setBackgroundResource(R.drawable.complete_white_background);

				tv_active.setTextColor(Color.BLACK);
				tv_complete.setTextColor(Color.BLACK);
				tv_all.setTextColor(Color.WHITE);
				tv_progress.setTextColor(Color.BLACK);
			}
		});

		tv_progress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Prefrences.checklisttypes=3;
				expandlist.setVisibility(View.GONE);
				 adapter = new progressAdapter(con, progressList2);
				progressList.setAdapter(adapter);
				progressList.setVisibility(View.VISIBLE);
				tv_active.setBackgroundResource(R.color.white);
				tv_complete.setBackgroundResource(R.drawable.complete_white_background);
				tv_all.setBackgroundResource(R.drawable.all_white_background);
				tv_progress.setBackgroundResource(R.color.black);

				tv_active.setTextColor(Color.BLACK);
				tv_complete.setTextColor(Color.BLACK);
				tv_all.setTextColor(Color.BLACK);
				tv_progress.setTextColor(Color.WHITE);
			}
		});

		tv_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Prefrences.checklisttypes=4;
				listAdapter = new ParentLevel(con, completelist);
				actualListview.setAdapter(listAdapter);
				expandlist.setVisibility(View.VISIBLE);
				progressList.setVisibility(View.GONE);
				tv_progress.setBackgroundResource(R.color.white);
				tv_active.setBackgroundResource(R.color.white);
				tv_all.setBackgroundResource(R.drawable.all_white_background);
				tv_complete.setBackgroundResource(R.drawable.complete_black_background);

				tv_active.setTextColor(Color.BLACK);
				tv_complete.setTextColor(Color.WHITE);
				tv_all.setTextColor(Color.BLACK);
				tv_progress.setTextColor(Color.BLACK);
			}
		});

		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.e("resumed", "msg");
		super.onResume();
//		if(Prefrences.searchFlag==1)		
//			tv_searchcheck.setVisibility(View.VISIBLE);
		
		connDect = new ConnectionDetector(getActivity());
		isInternetPresent = connDect.isConnectingToInternet();
		if(Prefrences.CheckItemClickFlag==true){
			Prefrences.CheckItemClickFlag =false;
			try{
				listAdapter.notifyDataSetChanged();
			}catch(Exception e){
				
			}
			try{
			adapter.notifyDataSetChanged();
		}catch(Exception e){
			
		}
		}
		

		if (Prefrences.stopingHit == 1) {
			Prefrences.stopingHit = 0;
			if(Prefrences.checklist_bool==false){
				if (isInternetPresent) {
					projectDetail(getActivity());
				} else {
					String response = sharedpref.getString("checklistfragment", "");
					if(response.equalsIgnoreCase("")){
						Toast.makeText(getActivity(),"No internet connection.", Toast.LENGTH_SHORT).show();
					}else{
						fillServerData(response);
					}
				}
				
			}else
			{
				JSONObject res=null;
				try {
					res = new JSONObject(Prefrences.checklist_s);
				
				JSONObject checklist = res.getJSONObject("checklist");
				Log.v("checklist value", checklist.toString());

				JSONArray categories = checklist
						.getJSONArray("phases");
				
				
				categList.clear();

				
				pcategList.clear();// = new ArrayList<Categories>();
				activelist.clear();// = new ArrayList<Categories>();
				completelist.clear();// = new
										// ArrayList<Categories>();
				checkList.clear();// = new ArrayList<CheckList>();
				checkall.clear();// = new ArrayList<CheckItems>();

				progressList2.clear();// = new
										// ArrayList<CheckItems>();

				for (int i = 0; i < categories.length(); i++)

				{

					JSONObject count = categories.getJSONObject(i);

					JSONArray subCategories = count
							.getJSONArray("categories");

					subCatList = new ArrayList<SubCategories>();

					psubCatList = new ArrayList<SubCategories>();
					activeSubCatList = new ArrayList<SubCategories>();
					completeSubCatList = new ArrayList<SubCategories>();

					for (int j = 0; j < subCategories.length(); j++) {
						JSONObject uCount = subCategories
								.getJSONObject(j);

						JSONArray checkItem = uCount
								.getJSONArray("checklist_items");

						ArrayList<CheckItems> checkItemList = new ArrayList<CheckItems>();

						ArrayList<CheckItems> activeCheckItemList = new ArrayList<CheckItems>();
						ArrayList<CheckItems> completeCheckItemList = new ArrayList<CheckItems>();
						ArrayList<CheckItems> progressList = new ArrayList<CheckItems>();

						for (int m = 0; m < checkItem.length(); m++) {
							JSONObject cCount = checkItem
									.getJSONObject(m);
							String state;
							try{
							 state = cCount.getString("state");
							 if(state.equalsIgnoreCase(null)){
								 state = "";
							 }
							}catch(NullPointerException e){
								state = "";
							}

							checkItemList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
									cCount.getString("id"),
									cCount.getString("body"),
									state,
									cCount.getString("item_type"),
									cCount.getString("photos_count"),
									cCount.getString("comments_count")));

							if (!checkItemList.get(m).status
									.equals("1")
									&& !checkItemList.get(m).status
											.equals("-1")) {
								activeCheckItemList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
										cCount.getString("id"),
										cCount.getString("body"),
										state,
										cCount.getString("item_type"),
										cCount.getString("photos_count"),
										cCount.getString("comments_count")));
							}

							if (checkItemList.get(m).status
									.equals("1")) {
								completeCheckItemList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
										cCount.getString("id"),
										cCount.getString("body"),
										state,
										cCount.getString("item_type"),
										cCount.getString("photos_count"),
										cCount.getString("comments_count")));
							}

							if (checkItemList.get(m).status
									.equals("0")) {

								progressList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
										cCount.getString("id"),
										cCount.getString("body"),
										state,
										cCount.getString("item_type"),
										cCount.getString("photos_count"),
										cCount.getString("comments_count")));
								// progressList2.addAll(progressList);
								// Log.d("","Size="+progressList2.size()+"Body="+progressList2.get(m).body);
							}

						}
						checkall.addAll(checkItemList);
						progressList2.addAll(progressList);

						if (activeCheckItemList.size() > 0) {
							activeSubCatList.add(new SubCategories(uCount.getString("id"),
									uCount.getString("name"),
									uCount.getString("completed_date"),
									uCount.getString("milestone_date"),
									uCount.getString("progress_percentage"),
									activeCheckItemList));
						}

						if (completeCheckItemList.size() > 0) {
							completeSubCatList.add(new SubCategories(uCount.getString("id"),
									uCount.getString("name"),
									uCount.getString("completed_date"),
									uCount.getString("milestone_date"),
									uCount.getString("progress_percentage"),
									completeCheckItemList));
						}

						subCatList.add(new SubCategories(uCount.getString("id"),
								uCount.getString("name"),
								uCount.getString("completed_date"),
								uCount.getString("milestone_date"),
								uCount.getString("progress_percentage"),
								checkItemList));

						psubCatList.add(new SubCategories(uCount.getString("id"),
								uCount.getString("name"),
								uCount.getString("completed_date"),
								uCount.getString("milestone_date"),
								uCount.getString("progress_percentage"),
								progressList));
					}
					Log.d("checkall",
							"size of checkall" + checkall.size());
					categList.add(new Categories(count.getString("id"),
							count.getString("name"), count
							.getString("completed_date"), count
							.getString("milestone_date"), count
							.getString("progress_percentage"),
							subCatList));

					pcategList.add(new Categories(count.getString("id"),count
							.getString("name"), count
							.getString("completed_date"), count
							.getString("milestone_date"), count
							.getString("progress_percentage"),
							psubCatList));

					if (activeSubCatList.size() > 0) {
						activelist.add(new Categories(count.getString("id"),count
								.getString("name"), count
								.getString("completed_date"), count
								.getString("milestone_date"), count
								.getString("progress_percentage"),
								activeSubCatList));

					}

					if (completeSubCatList.size() > 0) {
						completelist.add(new Categories(count.getString("id"),count
								.getString("name"), count
								.getString("completed_date"), count
								.getString("milestone_date"), count
								.getString("progress_percentage"),
								completeSubCatList));
						// Log.d("ActiveList",""+activelist.size());
					}

				}

				checkList.add(new CheckList(checklist
						.getString("id"), checklist
						.getString("name"), categList));
				
				

				

				Log.d("project adapter ", "Progress List size = "
						+ progressList2.size());
				// for(int i=0;i<progressList2.size();i++)
				Log.d("project adapter ",
						"body" + progressList2.size());
				
				if(Prefrences.checklisttypes==1)
				{
				 listAdapter = new ParentLevel(con,
						categList);
				// checkListView.setAdapter(listAdapter);
				actualListview.setAdapter(listAdapter);
				checkadapter = new checkAdapter(con);
				searchlist.setAdapter(checkadapter);
				searchlist.setVisibility(View.GONE);
				
				tv_active.setBackgroundResource(R.color.white);
				tv_progress.setBackgroundResource(R.color.white);
				tv_all.setBackgroundResource(R.drawable.all_black_background);
				tv_complete.setBackgroundResource(R.drawable.complete_white_background);

				tv_active.setTextColor(Color.BLACK);
				tv_complete.setTextColor(Color.BLACK);
				tv_all.setTextColor(Color.WHITE);
				tv_progress.setTextColor(Color.BLACK);
				}
				else if(Prefrences.checklisttypes==2)
				{
					 listAdapter = new ParentLevel(con, activelist);

					actualListview.setAdapter(listAdapter);
					expandlist.setVisibility(View.VISIBLE);
					checkadapter = new checkAdapter(con);
					searchlist.setAdapter(checkadapter);
					searchlist.setVisibility(View.GONE);

					progressList.setVisibility(View.GONE);
					tv_active.setBackgroundResource(R.color.black);
					tv_all.setBackgroundResource(R.drawable.all_white_background);
					tv_progress.setBackgroundResource(R.color.white);
					tv_complete.setBackgroundResource(R.drawable.complete_white_background);

					tv_active.setTextColor(Color.WHITE);
					tv_complete.setTextColor(Color.BLACK);
					tv_all.setTextColor(Color.BLACK);
					tv_progress.setTextColor(Color.BLACK);
				}
				else if(Prefrences.checklisttypes==3){
					expandlist.setVisibility(View.GONE);
					 adapter = new progressAdapter(con, progressList2);
					progressList.setAdapter(adapter);
					progressList.setVisibility(View.VISIBLE);
					
					checkadapter = new checkAdapter(con);
					searchlist.setAdapter(checkadapter);
					searchlist.setVisibility(View.GONE);
					
					tv_active.setBackgroundResource(R.color.white);
					tv_complete.setBackgroundResource(R.drawable.complete_white_background);
					tv_all.setBackgroundResource(R.drawable.all_white_background);
					tv_progress.setBackgroundResource(R.color.black);

					tv_active.setTextColor(Color.BLACK);
					tv_complete.setTextColor(Color.BLACK);
					tv_all.setTextColor(Color.BLACK);
					tv_progress.setTextColor(Color.WHITE);
				}
				else if(Prefrences.checklisttypes==4){
					 listAdapter = new ParentLevel(con, completelist);
					actualListview.setAdapter(listAdapter);
					expandlist.setVisibility(View.VISIBLE);
					progressList.setVisibility(View.GONE);
					
					checkadapter = new checkAdapter(con);
					searchlist.setAdapter(checkadapter);
					searchlist.setVisibility(View.GONE);
					
					tv_progress.setBackgroundResource(R.color.white);
					tv_active.setBackgroundResource(R.color.white);
					tv_all.setBackgroundResource(R.drawable.all_white_background);
					tv_complete.setBackgroundResource(R.drawable.complete_black_background);

					tv_active.setTextColor(Color.BLACK);
					tv_complete.setTextColor(Color.WHITE);
					tv_all.setTextColor(Color.BLACK);
					tv_progress.setTextColor(Color.BLACK);
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
//		else
//		{
//			ParentLevel listAdapter = new ParentLevel(con, categList);
//			actualListview.setAdapter(listAdapter);
//			expandlist.setVisibility(View.VISIBLE);
//			progressList.setVisibility(View.GONE);
//		}
	}

	public void onBackPressed() {

		activelist.clear();
		completelist.clear();
		progressList2.clear();
		categList.clear();
	}

	// ****************************** Expandable List Adapters for Shows All
	// Click List ************************************************************//

	public class ParentLevel extends BaseExpandableListAdapter {
		Context con;
		ArrayList<Categories> groupitems;

		public ParentLevel(Context con, ArrayList<Categories> groupitems) {
			this.con = con;
			this.groupitems = groupitems;

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return groupitems.get(groupPosition).subCat.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			SubCategories subcat = (SubCategories) getChild(groupPosition,
					childPosition);

			CustExpListview secondLevelexplv = new CustExpListview(
					getActivity());
			secondLevelexplv.setAdapter(new SecondLevelAdapter(con, groupitems,
					subcat));
			secondLevelexplv.setGroupIndicator(null);
			return secondLevelexplv;

		}

		@Override
		public int getChildrenCount(int groupPosition) {

			return groupitems.get(groupPosition).subCat.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// return this.groupitems.get(groupPosition);
			return groupitems.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return groupitems.size();

		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder;
			Categories cat = (Categories) getGroup(groupPosition);
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater infalInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater
						.inflate(R.layout.explist_item, null);
				holder.txtview = (TextView) convertView
						.findViewById(R.id.tv101);
				holder.subcatogries = (TextView) convertView
						.findViewById(R.id.subcat);
				holder.divider = (ImageView) convertView
						.findViewById(R.id.divider);
				holder.relParent = (RelativeLayout) convertView
						.findViewById(R.id.root);

				holder.relParent.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.WRAP_CONTENT));

				ASSL.DoMagic(holder.relParent);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.subcatogries.setTypeface(Prefrences
					.helveticaNeuelt(getActivity()));
			holder.txtview.setTypeface(Prefrences
					.helveticaNeuelt(getActivity()));
			holder.subcatogries.setText("Subcategories " + cat.subCat.size());
			holder.txtview.setText(cat.name.toString());
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	static class ViewHolder {
		TextView txtview;
		TextView subcatogries;
		RelativeLayout relParent;
		ImageView divider;
	}

	static class ViewHolder2 {
		TextView tv2;
		TextView items;
		RelativeLayout childRel;
		ImageView divider;
	}

	static class ViewHolder3 {
		TextView tv3;
		ImageView imgLeft;
		ImageView imgRight;
		LinearLayout linearlay;
		RelativeLayout rel;
		ImageView divider;

	}

	// Custom Expandable List
	public class CustExpListview extends ExpandableListView {

		int intGroupPosition, intChildPosition, intGroupid;

		public CustExpListview(Context context) {
			super(context);
		}

		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(
					LayoutParams.MATCH_PARENT, MeasureSpec.AT_MOST);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(
					LayoutParams.MATCH_PARENT, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public class SecondLevelAdapter extends BaseExpandableListAdapter {

		Context con;
		ArrayList<Categories> groupitems;
		private SubCategories subcat;

		public SecondLevelAdapter(Context con,
				ArrayList<Categories> groupitems, SubCategories subcat) {
			this.con = con;
			this.groupitems = groupitems;
			this.subcat = subcat;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return subcat.checkItems.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ViewHolder3 holder3;
			final CheckItems check = (CheckItems) getChild(groupPosition,
					childPosition);
			if (convertView == null) {
				holder3 = new ViewHolder3();
				LayoutInflater infalInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.exp_list_3level,
						null);
				holder3.tv3 = (TextView) convertView.findViewById(R.id.tv103);

				holder3.imgLeft = (ImageView) convertView
						.findViewById(R.id.img_left);

				holder3.imgRight = (ImageView) convertView
						.findViewById(R.id.img_right);
				holder3.linearlay = (LinearLayout) convertView
						.findViewById(R.id.linear1);
				holder3.divider = (ImageView) convertView
						.findViewById(R.id.divider);
				holder3.rel = (RelativeLayout) convertView
						.findViewById(R.id.relay);
				holder3.tv3.setTypeface(Prefrences
						.helveticaNeuelt(getActivity()));

				holder3.rel.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.WRAP_CONTENT));

				ASSL.DoMagic(holder3.rel);
				convertView.setTag(holder3);
			} else {
				holder3 = (ViewHolder3) convertView.getTag();
			}

			holder3.tv3.setText(check.body.toString());
			// holder3.rl.setLayoutParams(new ListView.LayoutParams(
			// android.view.ViewGroup.LayoutParams.MATCH_PARENT,
			// android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			//
			// ASSL.DoMagic(holder3.rl);

			if (check.itemType.equals("Doc")) {
				holder3.imgLeft
						.setBackgroundResource(R.drawable.document_outline);
			} else if (check.itemType.equals("Com")) {
				holder3.imgLeft
						.setBackgroundResource(R.drawable.mobile_outline);
			} else if (check.itemType.equals("S&C")) {
				holder3.imgLeft
						.setBackgroundResource(R.drawable.hands_outline_white);
			}

			if (check.status.equalsIgnoreCase("1")) {
				holder3.imgRight.setBackgroundResource(R.drawable.check);
				// holder3.rl.setAlpha(0.5f);
				holder3.tv3.setAlpha(0.5f);
				if (check.itemType.equalsIgnoreCase("Doc")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.document_outline);
				} else if (check.itemType.equalsIgnoreCase("Com")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.mobile_outline);
				} else if (check.itemType.equalsIgnoreCase("S&C")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.hands_outline_white);
				}

			} else if (check.status.equalsIgnoreCase("0")) {
				holder3.tv3.setTextColor(Color.WHITE);
				holder3.tv3.setAlpha(1);
				// img_right.setBackgroundResource(R.drawable.back_onclick);
				if (check.itemType.equalsIgnoreCase("Doc")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.document_outline);
				} else if (check.itemType.equalsIgnoreCase("Com")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.mobile_outline);
				} else if (check.itemType.equalsIgnoreCase("S&C")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.hands_outline_white);
				}
			} else if (check.status.equalsIgnoreCase("-1")) {
				holder3.tv3.setAlpha(0.5f);
				holder3.tv3.setPaintFlags(holder3.tv3.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
				if (check.itemType.equalsIgnoreCase("Doc")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.document_outline);
				} else if (check.itemType.equalsIgnoreCase("Com")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.mobile_outline);
				} else if (check.itemType.equalsIgnoreCase("S&C")) {
					holder3.imgLeft
							.setBackgroundResource(R.drawable.hands_outline_white);
				}
				// img_right.setBackgroundResource(R.drawable.backoff);
			}

			holder3.rel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					if (isInternetPresent) {

					String idcheck, status;
					idcheck = check.item_id.toString();
					status = check.status.toString();
					
					// Log.e("","874378684376348257"+id);
					showComments(idcheck, status, check);

					// Intent in = new Intent(getActivity(),
					// CheckItemClick.class);
					// in.putExtra("body", check.body.toString());
					// in.putExtra("id",check.id.toString());
					// //in.putParcelableArrayListExtra("key", (ArrayList<?
					// extends Parcelable>) comments);
					// //in.putExtra("hello",comments);
					// in.putExtra("status",check.status.toString());
					// in.putExtra("itemtype", check.itemType.toString());
					// //Log.d("111111","********----------"+childPosition+groupPosition);
					// startActivity(in);
					}else{
						Log.e("tag", ""+Prefrences.selectedProId+" val "+ check.item_id.toString());

						DatabaseClass dbObject = new DatabaseClass(getActivity());
						dbObject.open();
						if(dbObject.exists_checkitem(Prefrences.selectedProId, check.item_id.toString())){
							String response = dbObject.get_checkitem(Prefrences.selectedProId, check.item_id.toString());
							dbObject.close();
							fillServerDataItem(response, check);
							
						}else{
							dbObject.close();
							Toast.makeText(getActivity(),"No internet connection.", Toast.LENGTH_SHORT).show();
						}
					
				}
				}
			});

			int photocount, chatcount;
			if (check.commentsCount.toString().equals("null")
					|| check.photosCount.toString().equals("null")) {
				chatcount = 0;
				photocount = 0;
			} else {
				chatcount = Integer.parseInt(check.commentsCount.toString());
				photocount = Integer.parseInt(check.photosCount.toString());
			}

			if(check.status.toString().equalsIgnoreCase("1"))
			{
				holder3.imgRight.setBackgroundResource(R.drawable.check);
			}
			else if (photocount > 0) {
				holder3.imgRight.setBackgroundResource(R.drawable.camera_icon);
			} else if (chatcount > 0) {
				holder3.imgRight.setBackgroundResource(R.drawable.chat_icon);
			}
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// Log.v("child position",""+groupitems.get(i).subCat.get(groupPosition).checkItems.size());
			return subcat.checkItems.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return subcat;
		}

		@Override
		public int getGroupCount() {
			return 1;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			ViewHolder2 holder2;
			SubCategories subcat = (SubCategories) getGroup(groupPosition);
			if (convertView == null) {
				holder2 = new ViewHolder2();
				LayoutInflater infalInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.exp_child, null);
				holder2.tv2 = (TextView) convertView.findViewById(R.id.tv102);
				holder2.items = (TextView) convertView.findViewById(R.id.items);
				holder2.divider = (ImageView) convertView
						.findViewById(R.id.divider);
				holder2.childRel = (RelativeLayout) convertView
						.findViewById(R.id.root);

				// (new ListView.LayoutParams(
				// ListView.LayoutParams.MATCH_PARENT, 200))
				holder2.tv2.setTypeface(Prefrences
						.helveticaNeuelt(getActivity()));
				holder2.items.setTypeface(Prefrences
						.helveticaNeuelt(getActivity()));
				holder2.childRel.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.WRAP_CONTENT));

				ASSL.DoMagic(holder2.childRel);
				convertView.setTag(holder2);
			} else {
				holder2 = (ViewHolder2) convertView.getTag();
			}

			// Log.v("groupview of 2level=",""+groupitems.get(i).subCat.get(groupPosition).name.toString());
			holder2.tv2.setText(subcat.name.toString());

			int num = subcat.checkItems.size();
			holder2.items.setText("items : " + num);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}

	// //************************************************* Progress LIST adapter
	// **********************************************//

	public class progressAdapter extends BaseAdapter {
		Context con;
		ArrayList<CheckItems> progressarray;

		public progressAdapter(Context con, ArrayList<CheckItems> progressarr) {
			this.progressarray = progressarr;
			this.con = con;
		}

		@Override
		public int getCount() {
			return progressarray.size();
		}

		@Override
		public Object getItem(int position) {
			return progressarray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Pviewholder holder;
			final CheckItems body = (CheckItems) this.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new Pviewholder();
				LayoutInflater infalInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(
						R.layout.progress_list_item, null);
				convertView.setTag(holder);
			} else {
				holder = (Pviewholder) convertView.getTag();
			}
			holder.pBody = (TextView) convertView
					.findViewById(R.id.progress_body);
			holder.leftimg = (ImageView) convertView
					.findViewById(R.id.img_left);
			holder.pBody.setText("" + body.body.toString());
			holder.pBody.setTypeface(Prefrences.helveticaNeuelt(getActivity()));

			if (body.itemType.equals("Doc")) {
				holder.leftimg.setBackgroundResource(R.drawable.document);
			} else if (body.itemType.equals("Com")) {
				holder.leftimg.setBackgroundResource(R.drawable.mobile);
			} else if (body.itemType.equals("S&C")) {
				holder.leftimg
						.setBackgroundResource(R.drawable.hands_outline_black);
			}

			holder.pBody.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					String idcheck, status;
					idcheck = body.item_id.toString();
					status = body.status.toString();

					showComments(idcheck, status, body);
					// Intent in = new Intent(getActivity(),
					// CheckItemClick.class);
					// in.putExtra("body", body.body.toString());
					// in.putExtra("id",body.id.toString());
					// //in.putParcelableArrayListExtra("key", (ArrayList<?
					// extends Parcelable>) comments);
					// //in.putExtra("hello",comments);
					// in.putExtra("status",body.status.toString());
					// in.putExtra("itemtype", body.itemType.toString());
					// //Log.d("111111","********----------"+childPosition+groupPosition);
					// startActivity(in);

				}
			});
			return convertView;
		}

	}

	private static class Pviewholder {
		TextView pBody;
		ImageView leftimg;

	}

	// ///////////////////////////////////////////////////////// Search
	// Checkitems Adapter ?////////////////////////////////
	public class checkAdapter extends BaseAdapter {
		Context con;
		// ArrayList<CheckItems> progressarray;
		public ArrayList<CheckItems> checkall2 = new ArrayList<CheckItems>();

		public checkAdapter(Context con) {
			// this.progressarray = progressarr;
			this.con = con;
			checkall2.clear();
			checkall2.addAll(checkall);
		}

		@Override
		public int getCount() {
			return checkall.size();// progressarray.size();
		}

		@Override
		public Object getItem(int position) {
			return checkall.get(position);// progressarray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Pviewholder holder;
			final CheckItems body = (CheckItems) this.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new Pviewholder();
				LayoutInflater infalInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(
						R.layout.progress_list_item, null);
				convertView.setTag(holder);
			} else {
				holder = (Pviewholder) convertView.getTag();
			}
			holder.pBody = (TextView) convertView
					.findViewById(R.id.progress_body);
			holder.leftimg = (ImageView) convertView
					.findViewById(R.id.img_left);
			holder.pBody.setText("" + body.body.toString());

			if (body.itemType.equals("Doc")) {
				holder.leftimg.setBackgroundResource(R.drawable.document);
			} else if (body.itemType.equals("Com")) {
				holder.leftimg.setBackgroundResource(R.drawable.mobile);
			} else if (body.itemType.equals("S&C")) {
				holder.leftimg
						.setBackgroundResource(R.drawable.hands_outline_black);
			}

			holder.pBody.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getActivity(),
							CheckItemClick.class);
					
					Log.e("val.......",body.getitemid().toString()+" "+ body.getcat_id().toString()+" "+body.getsubCat_id().toString()+" "+ body.item_id);
					intent.putExtra("body", body.body.toString());
					intent.putExtra("itemtype", body.itemType.toString());
					intent.putExtra("status", body.status.toString());
					intent.putExtra("item_id", body.getitemid().toString());
					intent.putExtra("cat_id", body.getcat_id().toString());
					intent.putExtra("subcat_id", body.getsubCat_id().toString());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			});
			return convertView;
		}

		public void search2(String text) {
			text = text.toLowerCase();

			checkall.clear();
			if (text.length() == 0) {
				searchlist.setVisibility(View.GONE);
				progressList.setVisibility(View.GONE);
				expandlist.setVisibility(View.VISIBLE);
				Log.i("Search ", text);
				checkall.addAll(checkall2);
			} else {
				// Log.i("ELSE  ", checkall2.get(1).body.toString());
				searchlist.setVisibility(View.VISIBLE);
				progressList.setVisibility(View.GONE);
				expandlist.setVisibility(View.GONE);
				for (CheckItems chekitem : checkall2) {
					// Log.i("Data-----", checkall2.toString());
					if (chekitem.body.toLowerCase().contains(text)) {
						checkall.add(chekitem);
					}
				}
			}
			checkadapter.notifyDataSetChanged();
		}
	}

	public void showComments(final String idCheck, String status,
			final CheckItems check) {

		Prefrences.showLoadingDialog(con, "Loading...");

		RequestParams params = new RequestParams();

		params.put("id", idCheck);
//		params.put("status", status);
		// Log.e("json:", json1.toString());
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(1000000);
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.get(Prefrences.url + "/checklist_items/" + idCheck,// +
																	// Prefrences.selectedProId,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						DatabaseClass dbObject = new DatabaseClass(getActivity());
						dbObject.open();
						Log.e("CHKITEMResponse", "msg: "+response);
						if(dbObject.exists_checkitem(Prefrences.selectedProId, idCheck)){
							dbObject.update_checkitem(Prefrences.selectedProId, idCheck, response);
						}else{
							dbObject.CreateChkItem(Prefrences.selectedProId, idCheck, response);
						}
						dbObject.close();
						fillServerDataItem(response, check);
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());

						Prefrences.dismissLoadingDialog();
					}
				});
		// Log.d("", "lalalala"+CheckItemClick.comm.size());

	}

	// ************ API for Project Details. *************//

	public void projectDetail(final Activity activity) {

		Prefrences.showLoadingDialog(activity, "Loading...");

		RequestParams params = new RequestParams();

		params.put("user_id", Prefrences.userId);

		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		client.setTimeout(1000000);
		client.get(Prefrences.url + "/checklists/" + Prefrences.selectedProId,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);
						fillServerData(response);
						Editor editor = sharedpref.edit();
						editor.putString("checklistfragment", response);
						editor.commit();
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Prefrences.dismissLoadingDialog();
						if (pull == true) {
							pull = false;
							expandlist.onRefreshComplete();
							progressList.onRefreshComplete();
						}
					}
				});

	}
	
	public void fillServerData(String response){
		checkall.clear();
		JSONObject res = null;
		try {
			res = new JSONObject(response);
			
			
			Prefrences.checklist_s = response;
			Prefrences.checklist_bool=true;

			JSONObject checklist = res.getJSONObject("checklist");
			Log.v("checklist value", checklist.toString());

			JSONArray categories = checklist.getJSONArray("phases");   //Phase contains categories array and phase info like name id etc.
			
			
			categList.clear();
			pcategList.clear();
			activelist.clear();
			completelist.clear();
			checkList.clear();
			checkall.clear();

			progressList2.clear();// = new
									// ArrayList<CheckItems>();

			for (int i = 0; i < categories.length(); i++)

			{

				JSONObject count = categories.getJSONObject(i);

				JSONArray subCategories = count.getJSONArray("categories");

				subCatList = new ArrayList<SubCategories>();

				psubCatList = new ArrayList<SubCategories>();
				activeSubCatList = new ArrayList<SubCategories>();
				completeSubCatList = new ArrayList<SubCategories>();

				for (int j = 0; j < subCategories.length(); j++) {
					JSONObject uCount = subCategories
							.getJSONObject(j);

					JSONArray checkItem = uCount.getJSONArray("checklist_items");

					ArrayList<CheckItems> checkItemList = new ArrayList<CheckItems>();

					ArrayList<CheckItems> activeCheckItemList = new ArrayList<CheckItems>();
					ArrayList<CheckItems> completeCheckItemList = new ArrayList<CheckItems>();
					ArrayList<CheckItems> progressList = new ArrayList<CheckItems>();

					for (int m = 0; m < checkItem.length(); m++) {
						JSONObject cCount = checkItem
								.getJSONObject(m);
						
						String state;
						try{
						 state = cCount.getString("state");
						 if(state.equalsIgnoreCase(null)){
							 state = "";
						 }
						}catch(NullPointerException e){
							state = "";
						}
						
						
						
						
						//Log.e("ids.....", count.getString("id")+" "+ uCount.getString("id")+" "+cCount.getString("id"));
						checkItemList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
								cCount.getString("id"),
								cCount.getString("body"),
								state,
								cCount.getString("item_type"),
								cCount.getString("photos_count"),
								cCount.getString("comments_count")));

						if (!checkItemList.get(m).status
								.equals("1")
								&& !checkItemList.get(m).status
										.equals("-1")) {
							activeCheckItemList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
									cCount.getString("id"),
									cCount.getString("body"),
									state,
									cCount.getString("item_type"),
									cCount.getString("photos_count"),
									cCount.getString("comments_count")));
						}

						if (checkItemList.get(m).status
								.equals("1")) {
							completeCheckItemList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
									cCount.getString("id"),
									cCount.getString("body"),
									state,
									cCount.getString("item_type"),
									cCount.getString("photos_count"),
									cCount.getString("comments_count")));
						}

						if (checkItemList.get(m).status
								.equals("0")) {

							progressList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
									cCount.getString("id"),
									cCount.getString("body"),
									state,
									cCount.getString("item_type"),
									cCount.getString("photos_count"),
									cCount.getString("comments_count")));
							
						}

					}
					checkall.addAll(checkItemList);
					progressList2.addAll(progressList);

					if (activeCheckItemList.size() > 0) {
						activeSubCatList.add(new SubCategories(uCount.getString("id"),
								uCount.getString("name"),
								uCount.getString("completed_date"),
								uCount.getString("milestone_date"),
								uCount.getString("progress_percentage"),
								activeCheckItemList));
					}

					if (completeCheckItemList.size() > 0) {
						completeSubCatList.add(new SubCategories(uCount.getString("id"),
								uCount.getString("name"),
								uCount.getString("completed_date"),
								uCount.getString("milestone_date"),
								uCount.getString("progress_percentage"),
								completeCheckItemList));
					}

					subCatList.add(new SubCategories(uCount.getString("id"),
							uCount.getString("name"),
							uCount.getString("completed_date"),
							uCount.getString("milestone_date"),
							uCount.getString("progress_percentage"),
							checkItemList));

					psubCatList.add(new SubCategories(uCount.getString("id"),
							uCount.getString("name"),
							uCount.getString("completed_date"),
							uCount.getString("milestone_date"),
							uCount.getString("progress_percentage"),
							progressList));
				}
				Log.d("checkall",
						"size of checkall" + checkall.size());
				categList.add(new Categories(count.getString("id"),count
						.getString("name"), count
						.getString("completed_date"), count
						.getString("milestone_date"), count
						.getString("progress_percentage"),
						subCatList));

				pcategList.add(new Categories(count.getString("id"),count
						.getString("name"), count
						.getString("completed_date"), count
						.getString("milestone_date"), count
						.getString("progress_percentage"),
						psubCatList));

				if (activeSubCatList.size() > 0) {
					activelist.add(new Categories(count.getString("id"),count
							.getString("name"), count
							.getString("completed_date"), count
							.getString("milestone_date"), count
							.getString("progress_percentage"),
							activeSubCatList));

				}

				if (completeSubCatList.size() > 0) {
					completelist.add(new Categories(count.getString("id"),count
							.getString("name"), count
							.getString("completed_date"), count
							.getString("milestone_date"), count
							.getString("progress_percentage"),
							completeSubCatList));
					
				}

			}

			checkList.add(new CheckList(checklist
					.getString("id"), checklist
					.getString("name"), categList));
			
			

			

			Log.d("project adapter ", "Progress List size = "
					+ progressList2.size());
			// for(int i=0;i<progressList2.size();i++)
			Log.d("project adapter ",
					"body" + progressList2.size());
			
			if(Prefrences.checklisttypes==1)
			{
			 listAdapter = new ParentLevel(con,
					categList);
			// checkListView.setAdapter(listAdapter);
			actualListview.setAdapter(listAdapter);
			checkadapter = new checkAdapter(con);
			searchlist.setAdapter(checkadapter);
			searchlist.setVisibility(View.GONE);
			
			tv_active.setBackgroundResource(R.color.white);
			tv_progress.setBackgroundResource(R.color.white);
			tv_all.setBackgroundResource(R.drawable.all_black_background);
			tv_complete.setBackgroundResource(R.drawable.complete_white_background);

			tv_active.setTextColor(Color.BLACK);
			tv_complete.setTextColor(Color.BLACK);
			tv_all.setTextColor(Color.WHITE);
			tv_progress.setTextColor(Color.BLACK);
			}
			else if(Prefrences.checklisttypes==2)
			{
				 listAdapter = new ParentLevel(con, activelist);

				actualListview.setAdapter(listAdapter);
				expandlist.setVisibility(View.VISIBLE);
				checkadapter = new checkAdapter(con);
				searchlist.setAdapter(checkadapter);
				searchlist.setVisibility(View.GONE);

				progressList.setVisibility(View.GONE);
				tv_active.setBackgroundResource(R.color.black);
				tv_all.setBackgroundResource(R.drawable.all_white_background);
				tv_progress.setBackgroundResource(R.color.white);
				tv_complete.setBackgroundResource(R.drawable.complete_white_background);

				tv_active.setTextColor(Color.WHITE);
				tv_complete.setTextColor(Color.BLACK);
				tv_all.setTextColor(Color.BLACK);
				tv_progress.setTextColor(Color.BLACK);
			}
			else if(Prefrences.checklisttypes==3){
				expandlist.setVisibility(View.GONE);
				 adapter = new progressAdapter(con, progressList2);
				progressList.setAdapter(adapter);
				progressList.setVisibility(View.VISIBLE);
				
				checkadapter = new checkAdapter(con);
				searchlist.setAdapter(checkadapter);
				searchlist.setVisibility(View.GONE);
				
				tv_active.setBackgroundResource(R.color.white);
				tv_complete.setBackgroundResource(R.drawable.complete_white_background);
				tv_all.setBackgroundResource(R.drawable.all_white_background);
				tv_progress.setBackgroundResource(R.color.black);

				tv_active.setTextColor(Color.BLACK);
				tv_complete.setTextColor(Color.BLACK);
				tv_all.setTextColor(Color.BLACK);
				tv_progress.setTextColor(Color.WHITE);
			}
			else if(Prefrences.checklisttypes==4){
				 listAdapter = new ParentLevel(con, completelist);
				actualListview.setAdapter(listAdapter);
				expandlist.setVisibility(View.VISIBLE);
				progressList.setVisibility(View.GONE);
				
				checkadapter = new checkAdapter(con);
				searchlist.setAdapter(checkadapter);
				searchlist.setVisibility(View.GONE);
				
				tv_progress.setBackgroundResource(R.color.white);
				tv_active.setBackgroundResource(R.color.white);
				tv_all.setBackgroundResource(R.drawable.all_white_background);
				tv_complete.setBackgroundResource(R.drawable.complete_black_background);

				tv_active.setTextColor(Color.BLACK);
				tv_complete.setTextColor(Color.WHITE);
				tv_all.setTextColor(Color.BLACK);
				tv_progress.setTextColor(Color.BLACK);
			}
			// final ListView lv = list.getRefreshableView();
			// lv.addFooterView(footerView);
			// // list.setAdapter(adpter);
			//
			// list.setAdapter(checkadapter);
			// list.setVisibility(View.VISIBLE);
			if (pull == true) {
				pull = false;
				
//				expandlist.setVisibility(View.VISIBLE);
//				progressList.setVisibility(View.GONE);
//				active.setBackgroundResource(R.color.white);
//				progress.setBackgroundResource(R.color.white);
//				all.setBackgroundResource(R.drawable.all_black_background);
//				complete.setBackgroundResource(R.drawable.complete_white_background);
//
//				active.setTextColor(Color.BLACK);
//				complete.setTextColor(Color.BLACK);
//				all.setTextColor(Color.WHITE);
//				progress.setTextColor(Color.BLACK);
				expandlist.onRefreshComplete();
				progressList.onRefreshComplete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fillServerDataItem(String response, CheckItems check){
		JSONObject res = null;

		try {
			Log.e("response", "msg: "+response);
			checklistitemdata.clear();
			comments.clear();
			photos.clear();
			res = new JSONObject(response);
			Log.v("response ", "" + res.toString(2));
			JSONObject punchlists = res
					.getJSONObject("checklist_item");// chexklist

			JSONArray phot = punchlists.getJSONArray("photos");// categ

			JSONArray personel = punchlists
					.getJSONArray("comments");

			for (int j = 0; j < phot.length(); j++) {
				JSONObject ccount = phot.getJSONObject(j);

				photos.add(new PhotosCheckListItem(ccount
						.getString("id"), ccount
						.getString("url_large"), ccount
						.getString("original"), ccount
						.getString("url_small"), ccount
						.getString("url_thumb"), ccount
						.getString("image_file_size"), ccount
						.getString("image_content_type"),
						ccount.getString("source"), ccount
								.getString("phase"), ccount
								.getString("created_at"),
						ccount.getString("user_name"), ccount
								.getString("name"), ccount
								.getString("description"),
						ccount.getString("created_date")));

			}
			// Prefrences.pho.addAll(photos);
			// Log.v("%%%%%%%%%","photooooooossss sssiizzee "+CheckItemClick.pho.size());
			// comments = new
			// ArrayList<CommentsChecklistItem>();

			for (int m = 0; m < personel.length(); m++) {
				JSONObject count = personel.getJSONObject(m);

				JSONObject cmpny = count.getJSONObject("user");

				ArrayList<CommentsUserChecklistItem> cuser = new ArrayList<CommentsUserChecklistItem>();
				cuser.add(new CommentsUserChecklistItem(cmpny
						.getString("first_name"), cmpny
						.getString("full_name"), cmpny
						.getString("email"), cmpny
						.getString("formatted_phone"), cmpny
						.getString("id")));

				comments.add(new CommentsChecklistItem(count
						.getString("id"), count
						.getString("body"), cuser, count
						.getString("created_at")));

			}
			// Prefrences.comm.addAll(comments);
			// for(int k=0;k<comments.size();k++)
			// {
			// CheckItemClick.comm.add(k,comments.get(k));
			// }

			// Log.v("%%%%%%%%%","photooooooossss sssiizzee "+CheckItemClick.comm.size());
			Log.d("comments", "Size" + comments.size());
			// Log.d("","commentbody "+comments.get(0).body.toString());
//			"id": 38246,
//			"body": "Construction docs/direction should have: Full Set of Working Drawings - Permit Application",
//			"status": "Not Applicable",
//			"item_type": "S&C",
//			"photos_count": 1,
//			"comments_count": 2,
//			"activities": [],
//			"reminders": [],
//			"project_id": 37
			
			
			checklistitemdata.add(new DataCheckListItems(
					punchlists.getString("id"), punchlists
							.getString("body"), punchlists
							.getString("state"), punchlists
							.getString("item_type"), punchlists
							.getString("photos_count"),
					punchlists.getString("comments_count"),
					photos, comments, 
					punchlists
							.getString("phase_name"),
					punchlists.getString("project_id")));

			// Log.d("checklistfragment ",
			// "lalalala"+CheckItemClick.comm.size());

			Intent intent = new Intent(con,
					CheckItemClick.class);
			
			intent.putExtra("body", punchlists.getString("body"));
			intent.putExtra("itemtype", punchlists.getString("item_type"));
			intent.putExtra("status", punchlists.getString("state"));
			intent.putExtra("item_id", punchlists.getString("id"));
			intent.putExtra("cat_id", check.getcat_id().toString());
			intent.putExtra("subcat_id", check.getsubCat_id().toString());
			// Log.d("111111","********----------"+childPosition+groupPosition);
			con.startActivity(intent);
			((Activity) con).overridePendingTransition(
					R.anim.slide_in_right,
					R.anim.slide_out_left);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
