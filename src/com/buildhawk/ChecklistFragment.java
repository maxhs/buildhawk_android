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
/*
 *  Here we show the expandable list of checklist data.
 * 
 */
public class ChecklistFragment extends Fragment {

	public static ArrayList<Categories> categArrayList = new ArrayList<Categories>();

	public static ArrayList<Categories> pcategArrayList = new ArrayList<Categories>();
	public static ArrayList<Categories> activeArrayList = new ArrayList<Categories>();
	public static ArrayList<Categories> completeArrayList = new ArrayList<Categories>();
	public static ArrayList<CheckList> checkArrayList = new ArrayList<CheckList>();
	public static ArrayList<CheckItems> checkallArrayList = new ArrayList<CheckItems>();

	public static ArrayList<CheckItems> progressArrayList2 = new ArrayList<CheckItems>();

	public ArrayList<SubCategories> subCatArrayList;
	public static ArrayList<SubCategories> activeSubCatArrayList;
	public static ArrayList<SubCategories> completeSubCatArrayList;
	public static ArrayList<SubCategories> psubCatArrayList;

	ExpandableListView actualExpandableListview;
	public ArrayList<ProjectsFields> projectsAllArrayList = new ArrayList<ProjectsFields>();
	ConnectionDetector connDect;
	View footerView;
	Boolean pullBoolean = false;
	PullToRefreshExpandableListView pullToRefreshExpandableListView;
	ListView listviewSearch;
	PullToRefreshListView progressPullToRefreshListView;
	TextView textviewActive;
	TextView textviewAll;
	TextView textviewProgress;
	TextView textviewComplete;
	public static EditText edittextSearchcheck;
	public static RelativeLayout relativelayoutsearch;
	Button buttonCancelSearch;
	Button buttonSendcomment;
	RelativeLayout relativelayoutRoot;
	
	ParentLevel parentListAdapter;
	Boolean isInternetPresentBoolean = false;
	CheckAdapter checkadapter;
	Handler mHandler = new Handler();
	ProgressAdapter progressAdapter;
	// PullToRefreshListView list;

//	public static ArrayList<CommentsChecklistItem> comments = new ArrayList<CommentsChecklistItem>();
//	public static ArrayList<PhotosCheckListItem> photos = new ArrayList<PhotosCheckListItem>();
//	public static ArrayList<DataCheckListItems> checklistitemdata = new ArrayList<DataCheckListItems>();

	// private ArrayList<Categories> progressarray;
//	public static ArrayList<CheckItems> progressarr;
	static Context con;
	SharedPreferences sharedpref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.checklist, container, false);

		relativelayoutRoot = (RelativeLayout) root.findViewById(R.id.relativelayoutRootCkecklist);
		new ASSL(getActivity(), relativelayoutRoot, 1134, 720, false);
		Prefrences.clear = 1;
		con = getActivity();

		// list = (PullToRefreshListView) root.findViewById(R.id.pullList);

		relativelayoutsearch=(RelativeLayout)root.findViewById(R.id.relativelayoutsearch);
		textviewActive = (TextView) root.findViewById(R.id.textviewActive);
		textviewAll = (TextView) root.findViewById(R.id.textviewAll);
		textviewProgress = (TextView) root.findViewById(R.id.textviewProgress);
		textviewComplete = (TextView) root.findViewById(R.id.textviewComplete);
		edittextSearchcheck = (EditText) root.findViewById(R.id.edittextSearchcheck);
		buttonCancelSearch=(Button)root.findViewById(R.id.buttonCancelSearch);

		sharedpref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
		pullToRefreshExpandableListView = (PullToRefreshExpandableListView) root
				.findViewById(R.id.pullToRefreshExpandableListView);

		relativelayoutsearch.setVisibility(View.GONE);
		progressPullToRefreshListView = (PullToRefreshListView) root
				.findViewById(R.id.progressPullToRefreshListView);
		listviewSearch = (ListView) root.findViewById(R.id.listviewSearch);

		textviewActive.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		textviewAll.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		textviewComplete.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		textviewProgress.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		Prefrences.checklisttypes=1;
		// Log.v("count..",","+ProjectsAdapter.categList.size()+", "+ sAdapter.categList.get(0).subCat.size()+","+ProjectsAdapter.categList.get(0).subCat.get(0).checkItems.size());

		progressPullToRefreshListView.setVisibility(View.GONE);
		pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
		
//		if(Prefrences.checklist_s.equalsIgnoreCase("")){
//			Prefrences.checklist_bool=false;
//		}
		if(Prefrences.selectedProId.equalsIgnoreCase(Prefrences.LastSelectedProId))
		{
			if(!Prefrences.LastChecklist_s.equalsIgnoreCase(""))
//				Prefrences.document_bool = true;
//			else
			{
				Prefrences.checklist_s=Prefrences.LastChecklist_s;			
				Prefrences.checklist_bool = true;
			}
		}
		else
		{
			if (Prefrences.checklist_s.equalsIgnoreCase("")) {
				Prefrences.checklist_bool = false;
			}
		}
		
		actualExpandableListview = pullToRefreshExpandableListView.getRefreshableView();

		progressPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				pullBoolean = true;
//				pullToRefreshExpandableListView.onRefreshComplete();
//				progressPullToRefreshListView.onRefreshComplete();
				projectDetail(getActivity());
			}
		});

		
		pullToRefreshExpandableListView
				.setOnRefreshListener(new OnRefreshListener<ExpandableListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ExpandableListView> refreshView) {
						// TODO Auto-generated method stub
						pullBoolean = true;
//						pullToRefreshExpandableListView.onRefreshComplete();
//						progressPullToRefreshListView.onRefreshComplete();
						projectDetail(getActivity());
					}
				});

		buttonCancelSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					InputMethodManager imm = (InputMethodManager) con
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edittextSearchcheck.getWindowToken(),
							0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
//				if(Prefrences.checklisttypes==1)
//				{
//				 listAdapter = new ParentLevel(con,
//						categList);
//				// checkListView.setAdapter(listAdapter);
//				actualListview.setAdapter(listAdapter);
//				checkadapter = new checkAdapter(con);
//				searchlist.setAdapter(checkadapter);
//				searchlist.setVisibility(View.GONE);
//				
//				tv_active.setBackgroundResource(R.color.white);
//				tv_progress.setBackgroundResource(R.color.white);
//				tv_all.setBackgroundResource(R.drawable.all_black_background);
//				tv_complete.setBackgroundResource(R.drawable.complete_white_background);
//
//				tv_active.setTextColor(Color.BLACK);
//				tv_complete.setTextColor(Color.BLACK);
//				tv_all.setTextColor(Color.WHITE);
//				tv_progress.setTextColor(Color.BLACK);
//				}
//				else if(Prefrences.checklisttypes==2)
//				{
//					 listAdapter = new ParentLevel(con, activelist);
//
//					actualListview.setAdapter(listAdapter);
//					expandlist.setVisibility(View.VISIBLE);
//					checkadapter = new checkAdapter(con);
//					searchlist.setAdapter(checkadapter);
//					searchlist.setVisibility(View.GONE);
//
//					progressList.setVisibility(View.GONE);
//					tv_active.setBackgroundResource(R.color.black);
//					tv_all.setBackgroundResource(R.drawable.all_white_background);
//					tv_progress.setBackgroundResource(R.color.white);
//					tv_complete.setBackgroundResource(R.drawable.complete_white_background);
//
//					tv_active.setTextColor(Color.WHITE);
//					tv_complete.setTextColor(Color.BLACK);
//					tv_all.setTextColor(Color.BLACK);
//					tv_progress.setTextColor(Color.BLACK);
//				}
//				else if(Prefrences.checklisttypes==3){
//					expandlist.setVisibility(View.GONE);
//					 adapter = new progressAdapter(con, progressList2);
//					progressList.setAdapter(adapter);
//					progressList.setVisibility(View.VISIBLE);
//					
//					checkadapter = new checkAdapter(con);
//					searchlist.setAdapter(checkadapter);
//					searchlist.setVisibility(View.GONE);
//					
//					tv_active.setBackgroundResource(R.color.white);
//					tv_complete.setBackgroundResource(R.drawable.complete_white_background);
//					tv_all.setBackgroundResource(R.drawable.all_white_background);
//					tv_progress.setBackgroundResource(R.color.black);
//
//					tv_active.setTextColor(Color.BLACK);
//					tv_complete.setTextColor(Color.BLACK);
//					tv_all.setTextColor(Color.BLACK);
//					tv_progress.setTextColor(Color.WHITE);
//				}
//				else if(Prefrences.checklisttypes==4){
//					 listAdapter = new ParentLevel(con, completelist);
//					actualListview.setAdapter(listAdapter);
//					expandlist.setVisibility(View.VISIBLE);
//					progressList.setVisibility(View.GONE);
//					
//					checkadapter = new checkAdapter(con);
//					searchlist.setAdapter(checkadapter);
//					searchlist.setVisibility(View.GONE);
//					
//					tv_progress.setBackgroundResource(R.color.white);
//					tv_active.setBackgroundResource(R.color.white);
//					tv_all.setBackgroundResource(R.drawable.all_white_background);
//					tv_complete.setBackgroundResource(R.drawable.complete_black_background);
//
//					tv_active.setTextColor(Color.BLACK);
//					tv_complete.setTextColor(Color.WHITE);
//					tv_all.setTextColor(Color.BLACK);
//					tv_progress.setTextColor(Color.BLACK);
//				}
				relativelayoutsearch.setVisibility(View.GONE);
				listviewSearch.setVisibility(View.GONE);
			}
		});
		
		ProjectDetail.imageviewSearchimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Search Image","Clicked");
				relativelayoutsearch.setVisibility(View.VISIBLE);
				edittextSearchcheck.setFocusable(true);
				InputMethodManager imm = (InputMethodManager) con
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittextSearchcheck.getWindowToken(),
						0);
			}
		});
		
		edittextSearchcheck.setOnEditorActionListener(new OnEditorActionListener() {
		  	@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {		  		
				// TODO Auto-generated method stub
		  		if (actionId == EditorInfo.IME_ACTION_DONE) {
//		                Toast.makeText(ACTIVITY_NAME.this, etSearchFriends.getText(),Toast.LENGTH_SHORT).show();
		  							
					 InputMethodManager imm = (InputMethodManager) con
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(edittextSearchcheck.getWindowToken(),
								0);
					
					 
		              return true;
		            }
		  		else{
				return false;
		  		}
			}
		});

		edittextSearchcheck.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence charSeq, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
//				 InputMethodManager imm = (InputMethodManager) con
//							.getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(txt_searchcheck.getWindowToken(),
//							0);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// progressList.setVisibility(View.VISIBLE);
				Log.i("ARG", "-------" + arg0);
				ChecklistFragment.this.checkadapter.search2(arg0.toString());
			}
		});

		textviewActive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.checklisttypes=2;
				 parentListAdapter = new ParentLevel(con, activeArrayList);

				actualExpandableListview.setAdapter(parentListAdapter);
				pullToRefreshExpandableListView.setVisibility(View.VISIBLE);

				progressPullToRefreshListView.setVisibility(View.GONE);
				textviewActive.setBackgroundResource(R.color.black);
				textviewAll.setBackgroundResource(R.drawable.all_white_background);
				textviewProgress.setBackgroundResource(R.color.white);
				textviewComplete.setBackgroundResource(R.drawable.complete_white_background);

				textviewActive.setTextColor(Color.WHITE);
				textviewComplete.setTextColor(Color.BLACK);
				textviewAll.setTextColor(Color.BLACK);
				textviewProgress.setTextColor(Color.BLACK);

			}
		});
		textviewAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Prefrences.checklisttypes=1;
				 parentListAdapter = new ParentLevel(con, categArrayList);
				actualExpandableListview.setAdapter(parentListAdapter);
				pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
				progressPullToRefreshListView.setVisibility(View.GONE);
				textviewActive.setBackgroundResource(R.color.white);
				textviewProgress.setBackgroundResource(R.color.white);
				textviewAll.setBackgroundResource(R.drawable.all_black_background);
				textviewComplete.setBackgroundResource(R.drawable.complete_white_background);

				textviewActive.setTextColor(Color.BLACK);
				textviewComplete.setTextColor(Color.BLACK);
				textviewAll.setTextColor(Color.WHITE);
				textviewProgress.setTextColor(Color.BLACK);
			}
		});

		textviewProgress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Prefrences.checklisttypes=3;
				pullToRefreshExpandableListView.setVisibility(View.GONE);
				 progressAdapter = new ProgressAdapter(con, progressArrayList2);
				progressPullToRefreshListView.setAdapter(progressAdapter);
				progressPullToRefreshListView.setVisibility(View.VISIBLE);
				textviewActive.setBackgroundResource(R.color.white);
				textviewComplete.setBackgroundResource(R.drawable.complete_white_background);
				textviewAll.setBackgroundResource(R.drawable.all_white_background);
				textviewProgress.setBackgroundResource(R.color.black);

				textviewActive.setTextColor(Color.BLACK);
				textviewComplete.setTextColor(Color.BLACK);
				textviewAll.setTextColor(Color.BLACK);
				textviewProgress.setTextColor(Color.WHITE);
			}
		});

		textviewComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Prefrences.checklisttypes=4;
				parentListAdapter = new ParentLevel(con, completeArrayList);
				actualExpandableListview.setAdapter(parentListAdapter);
				pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
				progressPullToRefreshListView.setVisibility(View.GONE);
				textviewProgress.setBackgroundResource(R.color.white);
				textviewActive.setBackgroundResource(R.color.white);
				textviewAll.setBackgroundResource(R.drawable.all_white_background);
				textviewComplete.setBackgroundResource(R.drawable.complete_black_background);

				textviewActive.setTextColor(Color.BLACK);
				textviewComplete.setTextColor(Color.WHITE);
				textviewAll.setTextColor(Color.BLACK);
				textviewProgress.setTextColor(Color.BLACK);
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
		isInternetPresentBoolean = connDect.isConnectingToInternet();
		if(Prefrences.CheckItemClickFlag==true){
			Prefrences.CheckItemClickFlag =false;
			try{
//				actualListview.setAdapter(listAdapter);
				parentListAdapter.notifyDataSetChanged();
			}catch(Exception e){
				
			}
			try{
			progressAdapter.notifyDataSetChanged();
//				progressList.setAdapter(adapter);
		}catch(Exception e){
			
		}
		}
		
		Log.d("-------ifff------","--------ifff------"+Prefrences.stopingHit);
		if (Prefrences.stopingHit == 1 || !Prefrences.projectNotification.equals("")) {
			Log.d("-------ifff------","--------ifff------"+Prefrences.projectNotification);
			if(!Prefrences.projectNotification.equals(""))
			{
			Prefrences.selectedProId=Prefrences.projectNotification;
			Prefrences.projectNotification="";
			}
			Prefrences.stopingHit = 0;
			if(Prefrences.checklist_bool==false){
				if (isInternetPresentBoolean) {
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
				
				
				categArrayList.clear();

				
				pcategArrayList.clear();// = new ArrayList<Categories>();
				activeArrayList.clear();// = new ArrayList<Categories>();
				completeArrayList.clear();// = new
										// ArrayList<Categories>();
				checkArrayList.clear();// = new ArrayList<CheckList>();
				checkallArrayList.clear();// = new ArrayList<CheckItems>();

				progressArrayList2.clear();// = new
										// ArrayList<CheckItems>();

				for (int i = 0; i < categories.length(); i++)

				{

					JSONObject count = categories.getJSONObject(i);

					JSONArray subCategories = count
							.getJSONArray("categories");

					subCatArrayList = new ArrayList<SubCategories>();

					psubCatArrayList = new ArrayList<SubCategories>();
					activeSubCatArrayList = new ArrayList<SubCategories>();
					completeSubCatArrayList = new ArrayList<SubCategories>();

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
							String stateString;
							try{
							 stateString = cCount.getString("state");
							 if(stateString.equalsIgnoreCase(null)){
								 stateString = "";
							 }
							}catch(NullPointerException e){
								stateString = "";
							}

							checkItemList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
									cCount.getString("id"),
									cCount.getString("body"),
									stateString,
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
										stateString,
										cCount.getString("item_type"),
										cCount.getString("photos_count"),
										cCount.getString("comments_count")));
							}

							if (checkItemList.get(m).status
									.equals("1")) {
								completeCheckItemList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
										cCount.getString("id"),
										cCount.getString("body"),
										stateString,
										cCount.getString("item_type"),
										cCount.getString("photos_count"),
										cCount.getString("comments_count")));
							}

							if (checkItemList.get(m).status
									.equals("0")) {

								progressList.add(new CheckItems(count.getString("id"), uCount.getString("id"),
										cCount.getString("id"),
										cCount.getString("body"),
										stateString,
										cCount.getString("item_type"),
										cCount.getString("photos_count"),
										cCount.getString("comments_count")));
								// progressList2.addAll(progressList);
								// Log.d("","Size="+progressList2.size()+"Body="+progressList2.get(m).body);
							}

						}
						checkallArrayList.addAll(checkItemList);
						progressArrayList2.addAll(progressList);

						if (activeCheckItemList.size() > 0) {
							activeSubCatArrayList.add(new SubCategories(uCount.getString("id"),
									uCount.getString("name"),
									uCount.getString("completed_date"),
									uCount.getString("milestone_date"),
									uCount.getString("progress_percentage"),
									activeCheckItemList));
						}

						if (completeCheckItemList.size() > 0) {
							completeSubCatArrayList.add(new SubCategories(uCount.getString("id"),
									uCount.getString("name"),
									uCount.getString("completed_date"),
									uCount.getString("milestone_date"),
									uCount.getString("progress_percentage"),
									completeCheckItemList));
						}

						subCatArrayList.add(new SubCategories(uCount.getString("id"),
								uCount.getString("name"),
								uCount.getString("completed_date"),
								uCount.getString("milestone_date"),
								uCount.getString("progress_percentage"),
								checkItemList));

						psubCatArrayList.add(new SubCategories(uCount.getString("id"),
								uCount.getString("name"),
								uCount.getString("completed_date"),
								uCount.getString("milestone_date"),
								uCount.getString("progress_percentage"),
								progressList));
					}
					Log.d("checkall",
							"size of checkall" + checkallArrayList.size());
					categArrayList.add(new Categories(count.getString("id"),
							count.getString("name"), count
							.getString("completed_date"), count
							.getString("milestone_date"), count
							.getString("progress_percentage"),
							subCatArrayList));

					pcategArrayList.add(new Categories(count.getString("id"),count
							.getString("name"), count
							.getString("completed_date"), count
							.getString("milestone_date"), count
							.getString("progress_percentage"),
							psubCatArrayList));

					if (activeSubCatArrayList.size() > 0) {
						activeArrayList.add(new Categories(count.getString("id"),count
								.getString("name"), count
								.getString("completed_date"), count
								.getString("milestone_date"), count
								.getString("progress_percentage"),
								activeSubCatArrayList));

					}

					if (completeSubCatArrayList.size() > 0) {
						completeArrayList.add(new Categories(count.getString("id"),count
								.getString("name"), count
								.getString("completed_date"), count
								.getString("milestone_date"), count
								.getString("progress_percentage"),
								completeSubCatArrayList));
						// Log.d("ActiveList",""+activelist.size());
					}

				}

				checkArrayList.add(new CheckList(checklist
						.getString("id"), checklist
						.getString("name"), categArrayList));
				
				

				

				Log.d("project adapter ", "Progress List size = "
						+ progressArrayList2.size());
				// for(int i=0;i<progressList2.size();i++)
				Log.d("project adapter ",
						"body" + progressArrayList2.size());
				
				if(Prefrences.checklisttypes==1)
				{
				 parentListAdapter = new ParentLevel(con,
						categArrayList);
				// checkListView.setAdapter(listAdapter);
				actualExpandableListview.setAdapter(parentListAdapter);
				checkadapter = new CheckAdapter(con);
				listviewSearch.setAdapter(checkadapter);
				listviewSearch.setVisibility(View.GONE);
				
				textviewActive.setBackgroundResource(R.color.white);
				textviewProgress.setBackgroundResource(R.color.white);
				textviewAll.setBackgroundResource(R.drawable.all_black_background);
				textviewComplete.setBackgroundResource(R.drawable.complete_white_background);

				textviewActive.setTextColor(Color.BLACK);
				textviewComplete.setTextColor(Color.BLACK);
				textviewAll.setTextColor(Color.WHITE);
				textviewProgress.setTextColor(Color.BLACK);
				}
				else if(Prefrences.checklisttypes==2)
				{
					 parentListAdapter = new ParentLevel(con, activeArrayList);

					actualExpandableListview.setAdapter(parentListAdapter);
					pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
					checkadapter = new CheckAdapter(con);
					listviewSearch.setAdapter(checkadapter);
					listviewSearch.setVisibility(View.GONE);

					progressPullToRefreshListView.setVisibility(View.GONE);
					textviewActive.setBackgroundResource(R.color.black);
					textviewAll.setBackgroundResource(R.drawable.all_white_background);
					textviewProgress.setBackgroundResource(R.color.white);
					textviewComplete.setBackgroundResource(R.drawable.complete_white_background);

					textviewActive.setTextColor(Color.WHITE);
					textviewComplete.setTextColor(Color.BLACK);
					textviewAll.setTextColor(Color.BLACK);
					textviewProgress.setTextColor(Color.BLACK);
				}
				else if(Prefrences.checklisttypes==3){
					pullToRefreshExpandableListView.setVisibility(View.GONE);
					 progressAdapter = new ProgressAdapter(con, progressArrayList2);
					progressPullToRefreshListView.setAdapter(progressAdapter);
					progressPullToRefreshListView.setVisibility(View.VISIBLE);
					
					checkadapter = new CheckAdapter(con);
					listviewSearch.setAdapter(checkadapter);
					listviewSearch.setVisibility(View.GONE);
					
					textviewActive.setBackgroundResource(R.color.white);
					textviewComplete.setBackgroundResource(R.drawable.complete_white_background);
					textviewAll.setBackgroundResource(R.drawable.all_white_background);
					textviewProgress.setBackgroundResource(R.color.black);

					textviewActive.setTextColor(Color.BLACK);
					textviewComplete.setTextColor(Color.BLACK);
					textviewAll.setTextColor(Color.BLACK);
					textviewProgress.setTextColor(Color.WHITE);
				}
				else if(Prefrences.checklisttypes==4){
					 parentListAdapter = new ParentLevel(con, completeArrayList);
					actualExpandableListview.setAdapter(parentListAdapter);
					pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
					progressPullToRefreshListView.setVisibility(View.GONE);
					
					checkadapter = new CheckAdapter(con);
					listviewSearch.setAdapter(checkadapter);
					listviewSearch.setVisibility(View.GONE);
					
					textviewProgress.setBackgroundResource(R.color.white);
					textviewActive.setBackgroundResource(R.color.white);
					textviewAll.setBackgroundResource(R.drawable.all_white_background);
					textviewComplete.setBackgroundResource(R.drawable.complete_black_background);

					textviewActive.setTextColor(Color.BLACK);
					textviewComplete.setTextColor(Color.WHITE);
					textviewAll.setTextColor(Color.BLACK);
					textviewProgress.setTextColor(Color.BLACK);
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		else
		{
			Log.d("-----else-------","------else------");
		}
//		{
//			ParentLevel listAdapter = new ParentLevel(con, categList);
//			actualListview.setAdapter(listAdapter);
//			expandlist.setVisibility(View.VISIBLE);
//			progressList.setVisibility(View.GONE);
//		}
	}

	public void onBackPressed() {

		activeArrayList.clear();
		completeArrayList.clear();
		progressArrayList2.clear();
		categArrayList.clear();
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
					getActivity(),(groupitems.get(groupPosition).subCat.get(childPosition).checkItems.size()+1));
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
				holder.textviewCategoryName = (TextView) convertView
						.findViewById(R.id.textviewCategoryName);
				holder.textviewSubcatCount = (TextView) convertView
						.findViewById(R.id.textviewSubcatCount);
//				holder.imageviewDivider = (ImageView) convertView
//						.findViewById(R.id.divider);
				holder.relativelayoutParent = (LinearLayout) convertView
						.findViewById(R.id.relativelayoutParent);

				holder.relativelayoutParent.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT,
						ListView.LayoutParams.WRAP_CONTENT));

				ASSL.DoMagic(holder.relativelayoutParent);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textviewSubcatCount.setTypeface(Prefrences
					.helveticaNeuelt(getActivity()));
			holder.textviewCategoryName.setTypeface(Prefrences
					.helveticaNeuelt(getActivity()));
			holder.textviewSubcatCount.setText("Subcategories " + cat.subCat.size());
			holder.textviewCategoryName.setText(cat.name.toString());
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
		TextView textviewCategoryName;
		TextView textviewSubcatCount;
		LinearLayout relativelayoutParent;
//		ImageView imageviewDivider;
	}

	static class ViewHolder2 {
		TextView textviewSubcatName;
		TextView textviewItemsCount;
		LinearLayout relativelayoutRootChild;
		ImageView divider;
	}

	static class ViewHolder3 {
		TextView textviewCheckItem;
		ImageView imageviewLeft;
		ImageView imageviewRight;
		LinearLayout linearlayout;
		LinearLayout relativelayoutRootSubchild;
		ImageView divider;
		int childpos,grouppos;

	}

	// Custom Expandable List
	public class CustExpListview extends ExpandableListView {

		int intGroupPosition, intChildPosition, intGroupid;

		public CustExpListview(Context context,int childPosition) {
			super(context);
			intChildPosition=childPosition;
		}

		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(
					LayoutParams.MATCH_PARENT,MeasureSpec.AT_MOST);
			
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(
					((int) (300.0f * ASSL.Yscale()))*(intChildPosition),MeasureSpec.AT_MOST);
//			int what = 300*intChildPosition;
//			Log.v("what","what"+what);
			Log.e("", "width="+widthMeasureSpec+"height="+heightMeasureSpec+"intChildPosition"+intChildPosition);
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
		public View getChildView(int groupPosition,
				int childPosition, boolean isLastChild, View convertView,
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
				holder3.textviewCheckItem = (TextView) convertView.findViewById(R.id.textviewCheckItem);

				holder3.imageviewLeft = (ImageView) convertView
						.findViewById(R.id.img_left);

				holder3.imageviewRight = (ImageView) convertView
						.findViewById(R.id.img_right);
				holder3.linearlayout = (LinearLayout) convertView
						.findViewById(R.id.linearlayout);
				holder3.divider = (ImageView) convertView
						.findViewById(R.id.divider);
				holder3.relativelayoutRootSubchild = (LinearLayout) convertView
						.findViewById(R.id.relativelayoutRootSubchild);
				holder3.textviewCheckItem.setTypeface(Prefrences
						.helveticaNeuelt(getActivity()));

				holder3.relativelayoutRootSubchild.setLayoutParams(new ListView.LayoutParams(720,300));

				ASSL.DoMagic(holder3.relativelayoutRootSubchild);
				convertView.setTag(holder3);
			} else {
				holder3 = (ViewHolder3) convertView.getTag();
			}
				
//			holder3.childpos=childPosition;
//			holder3.grouppos=groupPosition;
//
//			holder3.rel.setTag(holder3);
			
				
			holder3.textviewCheckItem.setText(check.body.toString());
			// holder3.rl.setLayoutParams(new ListView.LayoutParams(
			// android.view.ViewGroup.LayoutParams.MATCH_PARENT,
			// android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			//
			// ASSL.DoMagic(holder3.rl);

			if (check.itemType.equals("Doc")) {
				holder3.imageviewLeft
						.setBackgroundResource(R.drawable.document_outline);
			} else if (check.itemType.equals("Com")) {
				holder3.imageviewLeft
						.setBackgroundResource(R.drawable.mobile_outline);
			} else if (check.itemType.equals("S&C")) {
				holder3.imageviewLeft
						.setBackgroundResource(R.drawable.hands_outline_white);
			}

			if (check.status.equals("1")) {
				holder3.imageviewRight.setBackgroundResource(R.drawable.check);
				// holder3.rl.setAlpha(0.5f);
				holder3.textviewCheckItem.setAlpha(0.5f);
				if (check.itemType.equalsIgnoreCase("Doc")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.document_outline);
				} else if (check.itemType.equalsIgnoreCase("Com")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.mobile_outline);
				} else if (check.itemType.equalsIgnoreCase("S&C")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.hands_outline_white);
				}
				holder3.textviewCheckItem.setPaintFlags(holder3.textviewCheckItem.getPaintFlags()
						& ~Paint.STRIKE_THRU_TEXT_FLAG);
			} else if (check.status.equals("0")) {
				holder3.textviewCheckItem.setTextColor(Color.WHITE);
				holder3.textviewCheckItem.setAlpha(1);
				// img_right.setBackgroundResource(R.drawable.back_onclick);
				if (check.itemType.equalsIgnoreCase("Doc")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.document_outline);
				} else if (check.itemType.equalsIgnoreCase("Com")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.mobile_outline);
				} else if (check.itemType.equalsIgnoreCase("S&C")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.hands_outline_white);
				}
				holder3.textviewCheckItem.setPaintFlags(holder3.textviewCheckItem.getPaintFlags()
						& ~Paint.STRIKE_THRU_TEXT_FLAG);
			} else if (check.status.equals("-1")) {
				holder3.textviewCheckItem.setAlpha(0.5f);
				holder3.textviewCheckItem.setPaintFlags(holder3.textviewCheckItem.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
				if (check.itemType.equalsIgnoreCase("Doc")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.document_outline);
				} else if (check.itemType.equalsIgnoreCase("Com")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.mobile_outline);
				} else if (check.itemType.equalsIgnoreCase("S&C")) {
					holder3.imageviewLeft
							.setBackgroundResource(R.drawable.hands_outline_white);
				}
				// img_right.setBackgroundResource(R.drawable.backoff);
			}else{
				holder3.textviewCheckItem.setPaintFlags(holder3.textviewCheckItem.getPaintFlags()
						& ~Paint.STRIKE_THRU_TEXT_FLAG);
				holder3.textviewCheckItem.setAlpha(1);
				holder3.textviewCheckItem.setTextColor(Color.WHITE);
			}
			
			
			holder3.relativelayoutRootSubchild.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
//					ViewHolder3 holder3 =(ViewHolder3) view.getTag();
					
					if (isInternetPresentBoolean) {
						 String idcheck,cat_id,subcat_id;
						
						idcheck = check.item_id.toString();
						subcat_id=check.getsubCat_id().toString();
						cat_id=check.getcat_id().toString();

					Prefrences.selectedCheckitemSynopsis = 1;
					 Intent intent = new Intent(getActivity(),CheckItemClick.class);
					 intent.putExtra("id",idcheck);
					 intent.putExtra("cat_id", cat_id);
					 intent.putExtra("subcat_id",subcat_id );
					 startActivity(intent);
					 
					 ((Activity) con).overridePendingTransition(
								R.anim.slide_in_right,
								R.anim.slide_out_left);
//					 ((Activity) con).finish();
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
						Toast.makeText(con, "No internet connection", Toast.LENGTH_LONG).show();
				}
				}
			});

			int photocount, chatcount;
			if (check.commentsCount.toString().equals("null")
					|| check.photosCount.toString().equals("null"))
			{
				chatcount = 0;
				photocount = 0;
			}
			else 
			{
				chatcount = Integer.parseInt(check.commentsCount.toString());
				photocount = Integer.parseInt(check.photosCount.toString());
			}

			if(check.status.toString().equalsIgnoreCase("1"))
			{
				holder3.imageviewRight.setBackgroundResource(R.drawable.check);
			}
			else if (photocount > 0) 
			{
				holder3.imageviewRight.setBackgroundResource(R.drawable.camera_icon);
			}
			else if (chatcount > 0) 
			{
				holder3.imageviewRight.setBackgroundResource(R.drawable.chat_icon);
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
				holder2.textviewSubcatName = (TextView) convertView.findViewById(R.id.textviewSubcatName);
				holder2.textviewItemsCount = (TextView) convertView.findViewById(R.id.textviewItemsCount);
				holder2.divider = (ImageView) convertView
						.findViewById(R.id.divider);
				holder2.relativelayoutRootChild = (LinearLayout) convertView
						.findViewById(R.id.relativelayoutRootChild);

				// (new ListView.LayoutParams(
				// ListView.LayoutParams.MATCH_PARENT, 200))
				holder2.textviewSubcatName.setTypeface(Prefrences
						.helveticaNeuelt(getActivity()));
				holder2.textviewItemsCount.setTypeface(Prefrences
						.helveticaNeuelt(getActivity()));
				holder2.relativelayoutRootChild.setLayoutParams(new ListView.LayoutParams(
						720,
						ListView.LayoutParams.WRAP_CONTENT));

				ASSL.DoMagic(holder2.relativelayoutRootChild);
				convertView.setTag(holder2);
			} else {
				holder2 = (ViewHolder2) convertView.getTag();
			}

			// Log.v("groupview of 2level=",""+groupitems.get(i).subCat.get(groupPosition).name.toString());
			holder2.textviewSubcatName.setText(subcat.name.toString());

			int num = subcat.checkItems.size();
			holder2.textviewItemsCount.setText("items : " + num);
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

	public class ProgressAdapter extends BaseAdapter {
		Context con;
		ArrayList<CheckItems> progressarrayArrayList;

		public ProgressAdapter(Context con, ArrayList<CheckItems> progressarrArrayList) {
			this.progressarrayArrayList = progressarrArrayList;
			this.con = con;
		}

		@Override
		public int getCount() {
			return progressarrayArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return progressarrayArrayList.get(position);
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
			holder.textviewBody = (TextView) convertView
					.findViewById(R.id.progress_body);
			holder.imageviewLeftimg = (ImageView) convertView
					.findViewById(R.id.img_left);
			holder.textviewBody.setText("" + body.body.toString());
			holder.textviewBody.setTypeface(Prefrences.helveticaNeuelt(getActivity()));

			if (body.itemType.equals("Doc")) {
				holder.imageviewLeftimg.setBackgroundResource(R.drawable.document);
			} else if (body.itemType.equals("Com")) {
				holder.imageviewLeftimg.setBackgroundResource(R.drawable.mobile);
			} else if (body.itemType.equals("S&C")) {
				holder.imageviewLeftimg
						.setBackgroundResource(R.drawable.hands_outline_black);
			}

			holder.textviewBody.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					String idcheck, status;
					idcheck = body.item_id.toString();
					status = body.status.toString();
					if(ConnectionDetector.isConnectingToInternet()){
//					showComments(idcheck, status, body);
					 Intent intent = new Intent(getActivity(),CheckItemClick.class);
					 intent.putExtra("id",idcheck);
					 intent.putExtra("cat_id", body.getcat_id().toString());
					 intent.putExtra("subcat_id", body.getsubCat_id().toString());
					 startActivity(intent);
					}else{
//					Log.e("tag", ""+Prefrences.selectedProId+" val "+ check.item_id.toString());
					Toast.makeText(con, "No internet connection", Toast.LENGTH_LONG).show();
			}
					 
					 
//					 in.putExtra("body", body.body.toString());
					 
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
		TextView textviewBody;
		ImageView imageviewLeftimg;

	}

	// ///////////////////////////////////////////////////////// Search
	// Checkitems Adapter ?////////////////////////////////
	public class CheckAdapter extends BaseAdapter {
		Context con;
		// ArrayList<CheckItems> progressarray;
		public ArrayList<CheckItems> checkall2ArrayList = new ArrayList<CheckItems>();

		public CheckAdapter(Context con) {
			// this.progressarray = progressarr;
			this.con = con;
			checkall2ArrayList.clear();
			checkall2ArrayList.addAll(checkallArrayList);
		}

		@Override
		public int getCount() {
			return checkallArrayList.size();// progressarray.size();
		}

		@Override
		public Object getItem(int position) {
			return checkallArrayList.get(position);// progressarray.get(position);
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
			holder.textviewBody = (TextView) convertView
					.findViewById(R.id.progress_body);
			holder.imageviewLeftimg = (ImageView) convertView
					.findViewById(R.id.img_left);
			holder.textviewBody.setText("" + body.body.toString());

			if (body.itemType.equals("Doc")) {
				holder.imageviewLeftimg.setBackgroundResource(R.drawable.document);
			} else if (body.itemType.equals("Com")) {
				holder.imageviewLeftimg.setBackgroundResource(R.drawable.mobile);
			} else if (body.itemType.equals("S&C")) {
				holder.imageviewLeftimg
						.setBackgroundResource(R.drawable.hands_outline_black);
			}

			holder.textviewBody.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if(ConnectionDetector.isConnectingToInternet()){
					Intent intent = new Intent(getActivity(),
							CheckItemClick.class);
					
					Log.e("val.......",body.getitemid().toString()+" "+ body.getcat_id().toString()+" "+body.getsubCat_id().toString()+" "+ body.item_id);
//					intent.putExtra("body", body.body.toString());
//					intent.putExtra("itemtype", body.itemType.toString());
//					intent.putExtra("status", body.status.toString());
					intent.putExtra("id", body.getitemid().toString());
//					intent.putExtra("cat_id", body.getcat_id().toString());
//					intent.putExtra("subcat_id", body.getsubCat_id().toString());
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}else{
//					Log.e("tag", ""+Prefrences.selectedProId+" val "+ check.item_id.toString());
					Toast.makeText(con, "No internet connection", Toast.LENGTH_LONG).show();
			}
				}
			});
			return convertView;
		}

		public void search2(String text) {
			text = text.toLowerCase();

			checkallArrayList.clear();
			if (text.length() == 0) {
				listviewSearch.setVisibility(View.GONE);
				progressPullToRefreshListView.setVisibility(View.GONE);
				pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
				Log.i("Search ", text);
				checkallArrayList.addAll(checkall2ArrayList);
			} else {
				// Log.i("ELSE  ", checkall2.get(1).body.toString());
				listviewSearch.setVisibility(View.VISIBLE);
				progressPullToRefreshListView.setVisibility(View.GONE);
				pullToRefreshExpandableListView.setVisibility(View.GONE);
				for (CheckItems chekitem : checkall2ArrayList) {
					// Log.i("Data-----", checkall2.toString());
					if (chekitem.body.toLowerCase().contains(text)) {
						checkallArrayList.add(chekitem);
					}
				}
			}
			checkadapter.notifyDataSetChanged();
		}
	}

//	public void showComments(final String idCheck, String status,
//			final CheckItems check) {
//
//		Prefrences.showLoadingDialog(con, "Loading...");
//
//		RequestParams params = new RequestParams();
//
//		params.put("id", idCheck);
////		params.put("status", status);
//		// Log.e("json:", json1.toString());
//		AsyncHttpClient client = new AsyncHttpClient();
//		client.setTimeout(1000000);
//		client.addHeader("Content-type", "application/json");
//		client.addHeader("Accept", "application/json");
//
//		client.get(Prefrences.url + "/checklist_items/" + idCheck,// +
//																	// Prefrences.selectedProId,
//				params, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onSuccess(String response) {
//						DatabaseClass dbObject = new DatabaseClass(getActivity());
//						dbObject.open();
//						Log.e("CHKITEMResponse", "msg: "+response);
//						if(dbObject.exists_checkitem(Prefrences.selectedProId, idCheck)){
//							dbObject.update_checkitem(Prefrences.selectedProId, idCheck, response);
//						}else{
//							dbObject.CreateChkItem(Prefrences.selectedProId, idCheck, response);
//						}
//						dbObject.close();
//						fillServerDataItem(response, check);
//						Prefrences.dismissLoadingDialog();
//					}
//
//					@Override
//					public void onFailure(Throwable arg0) {
//						Log.e("request fail", arg0.toString());
//
//						Prefrences.dismissLoadingDialog();
//					}
//				});
//		// Log.d("", "lalalala"+CheckItemClick.comm.size());
//
//	}


	// ************ API for Project Details. *************//

	public void projectDetail(final Activity activity) {
		if(pullBoolean!=true)
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
						if(pullBoolean!=true)
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						if(pullBoolean!=true)
						Prefrences.dismissLoadingDialog();
						if (pullBoolean == true) {
							pullBoolean = false;
							pullToRefreshExpandableListView.onRefreshComplete();
							progressPullToRefreshListView.onRefreshComplete();
						}
					}
				});

	}
	
	public void fillServerData(String response){
		checkallArrayList.clear();
		JSONObject res = null;
		try {
			res = new JSONObject(response);
			
			
			Prefrences.checklist_s = response;
			Prefrences.checklist_bool=true;

			JSONObject checklist = res.getJSONObject("checklist");
			Log.v("checklist value", checklist.toString());

			JSONArray categories = checklist.getJSONArray("phases");   //Phase contains categories array and phase info like name id etc.
			
			
			categArrayList.clear();
			pcategArrayList.clear();
			activeArrayList.clear();
			completeArrayList.clear();
			checkArrayList.clear();
			checkallArrayList.clear();

			progressArrayList2.clear();// = new
									// ArrayList<CheckItems>();

			for (int i = 0; i < categories.length(); i++)

			{

				JSONObject count = categories.getJSONObject(i);

				JSONArray subCategories = count.getJSONArray("categories");

				subCatArrayList = new ArrayList<SubCategories>();

				psubCatArrayList = new ArrayList<SubCategories>();
				activeSubCatArrayList = new ArrayList<SubCategories>();
				completeSubCatArrayList = new ArrayList<SubCategories>();

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
					checkallArrayList.addAll(checkItemList);
					progressArrayList2.addAll(progressList);

					if (activeCheckItemList.size() > 0) {
						activeSubCatArrayList.add(new SubCategories(uCount.getString("id"),
								uCount.getString("name"),
								uCount.getString("completed_date"),
								uCount.getString("milestone_date"),
								uCount.getString("progress_percentage"),
								activeCheckItemList));
					}

					if (completeCheckItemList.size() > 0) {
						completeSubCatArrayList.add(new SubCategories(uCount.getString("id"),
								uCount.getString("name"),
								uCount.getString("completed_date"),
								uCount.getString("milestone_date"),
								uCount.getString("progress_percentage"),
								completeCheckItemList));
					}

					subCatArrayList.add(new SubCategories(uCount.getString("id"),
							uCount.getString("name"),
							uCount.getString("completed_date"),
							uCount.getString("milestone_date"),
							uCount.getString("progress_percentage"),
							checkItemList));

					psubCatArrayList.add(new SubCategories(uCount.getString("id"),
							uCount.getString("name"),
							uCount.getString("completed_date"),
							uCount.getString("milestone_date"),
							uCount.getString("progress_percentage"),
							progressList));
				}
				Log.d("checkall",
						"size of checkall" + checkallArrayList.size());
				categArrayList.add(new Categories(count.getString("id"),count
						.getString("name"), count
						.getString("completed_date"), count
						.getString("milestone_date"), count
						.getString("progress_percentage"),
						subCatArrayList));

				pcategArrayList.add(new Categories(count.getString("id"),count
						.getString("name"), count
						.getString("completed_date"), count
						.getString("milestone_date"), count
						.getString("progress_percentage"),
						psubCatArrayList));

				if (activeSubCatArrayList.size() > 0) {
					activeArrayList.add(new Categories(count.getString("id"),count
							.getString("name"), count
							.getString("completed_date"), count
							.getString("milestone_date"), count
							.getString("progress_percentage"),
							activeSubCatArrayList));

				}

				if (completeSubCatArrayList.size() > 0) {
					completeArrayList.add(new Categories(count.getString("id"),count
							.getString("name"), count
							.getString("completed_date"), count
							.getString("milestone_date"), count
							.getString("progress_percentage"),
							completeSubCatArrayList));
					
				}

			}

			checkArrayList.add(new CheckList(checklist
					.getString("id"), checklist
					.getString("name"), categArrayList));
			
			

			

			Log.d("project adapter ", "Progress List size = "
					+ progressArrayList2.size());
			// for(int i=0;i<progressList2.size();i++)
			Log.d("project adapter ",
					"body" + progressArrayList2.size());
			
			if(Prefrences.checklisttypes==1)
			{
			 parentListAdapter = new ParentLevel(con,
					categArrayList);
			// checkListView.setAdapter(listAdapter);
			actualExpandableListview.setAdapter(parentListAdapter);
			checkadapter = new CheckAdapter(con);
			listviewSearch.setAdapter(checkadapter);
			listviewSearch.setVisibility(View.GONE);
			
			textviewActive.setBackgroundResource(R.color.white);
			textviewProgress.setBackgroundResource(R.color.white);
			textviewAll.setBackgroundResource(R.drawable.all_black_background);
			textviewComplete.setBackgroundResource(R.drawable.complete_white_background);

			textviewActive.setTextColor(Color.BLACK);
			textviewComplete.setTextColor(Color.BLACK);
			textviewAll.setTextColor(Color.WHITE);
			textviewProgress.setTextColor(Color.BLACK);
			}
			else if(Prefrences.checklisttypes==2)
			{
				 parentListAdapter = new ParentLevel(con, activeArrayList);

				actualExpandableListview.setAdapter(parentListAdapter);
				pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
				checkadapter = new CheckAdapter(con);
				listviewSearch.setAdapter(checkadapter);
				listviewSearch.setVisibility(View.GONE);

				progressPullToRefreshListView.setVisibility(View.GONE);
				textviewActive.setBackgroundResource(R.color.black);
				textviewAll.setBackgroundResource(R.drawable.all_white_background);
				textviewProgress.setBackgroundResource(R.color.white);
				textviewComplete.setBackgroundResource(R.drawable.complete_white_background);

				textviewActive.setTextColor(Color.WHITE);
				textviewComplete.setTextColor(Color.BLACK);
				textviewAll.setTextColor(Color.BLACK);
				textviewProgress.setTextColor(Color.BLACK);
			}
			else if(Prefrences.checklisttypes==3){
				pullToRefreshExpandableListView.setVisibility(View.GONE);
				 progressAdapter = new ProgressAdapter(con, progressArrayList2);
				progressPullToRefreshListView.setAdapter(progressAdapter);
				progressPullToRefreshListView.setVisibility(View.VISIBLE);
				
				checkadapter = new CheckAdapter(con);
				listviewSearch.setAdapter(checkadapter);
				listviewSearch.setVisibility(View.GONE);
				
				textviewActive.setBackgroundResource(R.color.white);
				textviewComplete.setBackgroundResource(R.drawable.complete_white_background);
				textviewAll.setBackgroundResource(R.drawable.all_white_background);
				textviewProgress.setBackgroundResource(R.color.black);

				textviewActive.setTextColor(Color.BLACK);
				textviewComplete.setTextColor(Color.BLACK);
				textviewAll.setTextColor(Color.BLACK);
				textviewProgress.setTextColor(Color.WHITE);
			}
			else if(Prefrences.checklisttypes==4){
				 parentListAdapter = new ParentLevel(con, completeArrayList);
				actualExpandableListview.setAdapter(parentListAdapter);
				pullToRefreshExpandableListView.setVisibility(View.VISIBLE);
				progressPullToRefreshListView.setVisibility(View.GONE);
				
				checkadapter = new CheckAdapter(con);
				listviewSearch.setAdapter(checkadapter);
				listviewSearch.setVisibility(View.GONE);
				
				textviewProgress.setBackgroundResource(R.color.white);
				textviewActive.setBackgroundResource(R.color.white);
				textviewAll.setBackgroundResource(R.drawable.all_white_background);
				textviewComplete.setBackgroundResource(R.drawable.complete_black_background);

				textviewActive.setTextColor(Color.BLACK);
				textviewComplete.setTextColor(Color.WHITE);
				textviewAll.setTextColor(Color.BLACK);
				textviewProgress.setTextColor(Color.BLACK);
			}
			// final ListView lv = list.getRefreshableView();
			// lv.addFooterView(footerView);
			// // list.setAdapter(adpter);
			//
			// list.setAdapter(checkadapter);
			// list.setVisibility(View.VISIBLE);
			if (pullBoolean == true) {
				pullBoolean = false;
				
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
				pullToRefreshExpandableListView.onRefreshComplete();
				progressPullToRefreshListView.onRefreshComplete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void fillServerDataItem(String response, CheckItems check){
//		JSONObject res = null;
//
//		try {
//			Log.e("response", "msg: "+response);
//			checklistitemdata.clear();
//			comments.clear();
//			photos.clear();
//			res = new JSONObject(response);
//			Log.v("response ", "" + res.toString(2));
//			JSONObject punchlists = res
//					.getJSONObject("checklist_item");// chexklist
//
//			JSONArray phot = punchlists.getJSONArray("photos");// categ
//
//			JSONArray personel = punchlists
//					.getJSONArray("comments");
//
//			for (int j = 0; j < phot.length(); j++) {
//				JSONObject ccount = phot.getJSONObject(j);
//
//				photos.add(new PhotosCheckListItem(ccount
//						.getString("id"), ccount
//						.getString("url_large"), ccount
//						.getString("original"), ccount
//						.getString("url_small"), ccount
//						.getString("url_thumb"), ccount
//						.getString("image_file_size"), ccount
//						.getString("image_content_type"),
//						ccount.getString("source"), ccount
//								.getString("phase"), ccount
//								.getString("created_at"),
//						ccount.getString("user_name"), ccount
//								.getString("name"), ccount
//								.getString("description"),
//						ccount.getString("created_date")));
//
//			}
//			// Prefrences.pho.addAll(photos);
//			// Log.v("%%%%%%%%%","photooooooossss sssiizzee "+CheckItemClick.pho.size());
//			// comments = new
//			// ArrayList<CommentsChecklistItem>();
//
//			for (int m = 0; m < personel.length(); m++) {
//				JSONObject count = personel.getJSONObject(m);
//
//				JSONObject cmpny = count.getJSONObject("user");
//
//				ArrayList<CommentsUserChecklistItem> cuser = new ArrayList<CommentsUserChecklistItem>();
//				cuser.add(new CommentsUserChecklistItem(cmpny
//						.getString("first_name"), cmpny
//						.getString("full_name"), cmpny
//						.getString("email"), cmpny
//						.getString("formatted_phone"), cmpny
//						.getString("id")));
//
//				comments.add(new CommentsChecklistItem(count
//						.getString("id"), count
//						.getString("body"), cuser, count
//						.getString("created_at")));
//
//			}
//			// Prefrences.comm.addAll(comments);
//			// for(int k=0;k<comments.size();k++)
//			// {
//			// CheckItemClick.comm.add(k,comments.get(k));
//			// }
//
//			// Log.v("%%%%%%%%%","photooooooossss sssiizzee "+CheckItemClick.comm.size());
//			Log.d("comments", "Size" + comments.size());
//			// Log.d("","commentbody "+comments.get(0).body.toString());
////			"id": 38246,
////			"body": "Construction docs/direction should have: Full Set of Working Drawings - Permit Application",
////			"status": "Not Applicable",
////			"item_type": "S&C",
////			"photos_count": 1,
////			"comments_count": 2,
////			"activities": [],
////			"reminders": [],
////			"project_id": 37
//			
//			
//			checklistitemdata.add(new DataCheckListItems(
//					punchlists.getString("id"), punchlists
//							.getString("body"), punchlists
//							.getString("state"), punchlists
//							.getString("item_type"), punchlists
//							.getString("photos_count"),
//					punchlists.getString("comments_count"),
//					photos, comments, 
//					punchlists
//							.getString("phase_name"),
//					punchlists.getString("project_id")));
//
//			// Log.d("checklistfragment ",
//			// "lalalala"+CheckItemClick.comm.size());
//
//			Intent intent = new Intent(con,
//					CheckItemClick.class);
//			
//			intent.putExtra("body", punchlists.getString("body"));
//			intent.putExtra("itemtype", punchlists.getString("item_type"));
//			intent.putExtra("status", punchlists.getString("state"));
//			intent.putExtra("item_id", punchlists.getString("id"));
//			intent.putExtra("cat_id", check.getcat_id().toString());
//			intent.putExtra("subcat_id", check.getsubCat_id().toString());
//			// Log.d("111111","********----------"+childPosition+groupPosition);
//			con.startActivity(intent);
//			((Activity) con).overridePendingTransition(
//					R.anim.slide_in_right,
//					R.anim.slide_out_left);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
