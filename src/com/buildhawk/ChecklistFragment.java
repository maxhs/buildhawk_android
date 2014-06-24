package com.buildhawk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ChecklistFragment extends Fragment {

	public ArrayList<Categories> categList = new ArrayList<Categories>();

	public ArrayList<Categories> pcategList = new ArrayList<Categories>();
	public ArrayList<Categories> activelist = new ArrayList<Categories>();
	public ArrayList<Categories> completelist = new ArrayList<Categories>();
	public ArrayList<CheckList> checkList = new ArrayList<CheckList>();
	public ArrayList<CheckItems> checkall = new ArrayList<CheckItems>();

	public ArrayList<CheckItems> progressList2 = new ArrayList<CheckItems>();

	public ArrayList<SubCategories> subCatList;
	public ArrayList<SubCategories> activeSubCatList;
	public ArrayList<SubCategories> completeSubCatList;
	public ArrayList<SubCategories> psubCatList;

	ExpandableListView actualListview;
	public ArrayList<ProjectsFields> projectsListAll = new ArrayList<ProjectsFields>();

	View footerView;
	Boolean pull = false;
	PullToRefreshExpandableListView expandlist;
	ListView searchlist;
	PullToRefreshListView progressList;
	TextView active;
	TextView all;
	TextView progress;
	TextView complete;
	TextView searchcheck;
	Button sendcomment;
	RelativeLayout relLay;
	static ArrayList<Categories> listDataHeader;
	static ArrayList<Categories> activeHeader;
	static ArrayList<Categories> completeHeader;
	checkAdapter checkadapter;
	Handler mHandler = new Handler();
	// PullToRefreshListView list;

	public static ArrayList<CommentsChecklistItem> comments = new ArrayList<CommentsChecklistItem>();
	public static ArrayList<PhotosCheckListItem> photos = new ArrayList<PhotosCheckListItem>();
	public static ArrayList<DataCheckListItems> checklistitemdata = new ArrayList<DataCheckListItems>();

	// private ArrayList<Categories> progressarray;
	public static ArrayList<CheckItems> progressarr;
	static Context con;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.checklist, container, false);

		relLay = (RelativeLayout) root.findViewById(R.id.rellay);
		new ASSL(getActivity(), relLay, 1134, 720, false);
		Prefrences.clear = 1;
		con = getActivity();

		// list = (PullToRefreshListView) root.findViewById(R.id.pullList);

		active = (TextView) root.findViewById(R.id.active);
		all = (TextView) root.findViewById(R.id.all);
		progress = (TextView) root.findViewById(R.id.progress);
		complete = (TextView) root.findViewById(R.id.completed);
		searchcheck = (TextView) root.findViewById(R.id.checksearch);

		expandlist = (PullToRefreshExpandableListView) root
				.findViewById(R.id.expandList);

		progressList = (PullToRefreshListView) root
				.findViewById(R.id.progressList);
		searchlist = (ListView) root.findViewById(R.id.checksearchlist);

		active.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		all.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		complete.setTypeface(Prefrences.helveticaNeuelt(getActivity()));
		progress.setTypeface(Prefrences.helveticaNeuelt(getActivity()));

		// Log.v("count..",","+ProjectsAdapter.categList.size()+", "+ProjectsAdapter.categList.get(0).subCat.size()+","+ProjectsAdapter.categList.get(0).subCat.get(0).checkItems.size());

		progressList.setVisibility(View.GONE);
		expandlist.setVisibility(View.VISIBLE);

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

		// listDataHeader.clear();
		// activeHeader.clear();
		// completeHeader.clear();

		// listDataHeader = categList;
		// activeHeader = activelist;
		// completeHeader = completelist;
		// progressarr = progressList2;

		// progressarray=ProjectsAdapter.PcategList;

		// footerView =
		// ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.last_item,
		// null, false);
		//
		// LinearLayout lay = (LinearLayout)
		// footerView.findViewById(R.id.footerview);
		// lay.setLayoutParams(new ListView.LayoutParams(720, 156));
		// ASSL.DoMagic(lay);

		// ParentLevel listAdapter = new ParentLevel(con, listDataHeader);
		// // checkListView.setAdapter(listAdapter);
		// expandlist.setAdapter(listAdapter);
		// checkadapter = new checkAdapter(con);
		// searchlist.setAdapter(checkadapter);
		// searchlist.setVisibility(View.GONE);

		// // adpter = new CollectionAdapter();
		// final ListView lv = list.getRefreshableView();
		// lv.addFooterView(footerView);
		// // list.setAdapter(adpter);
		// list.setAdapter(checkadapter);
		// list.setVisibility(View.VISIBLE);
		//
		// //*******************************
		//

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

		// expandlist.setOnGroupClickListener(new OnGroupClickListener() {
		//
		// @Override
		// public boolean onGroupClick(ExpandableListView parent, View v,
		// int groupPosition, long id) {
		// // Toast.makeText(getApplicationContext(),
		// // "Group Clicked " + listDataHeader.get(groupPosition),
		// // Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// });
		//
		// // Listview Group expanded listener
		// expandlist.setOnGroupExpandListener(new OnGroupExpandListener() {
		//
		// @Override
		// public void onGroupExpand(int groupPosition) {
		// Toast.makeText(getActivity(),
		// listDataHeader.get(groupPosition) + " Expanded",
		// Toast.LENGTH_SHORT).show();
		// }
		// });
		//
		// // Listview Group collasped listener
		// expandlist.setOnGroupCollapseListener(new OnGroupCollapseListener() {
		//
		// @Override
		// public void onGroupCollapse(int groupPosition) {
		// Toast.makeText(getActivity(),
		// listDataHeader.get(groupPosition) + " Collapsed",
		// Toast.LENGTH_SHORT).show();
		//
		// }
		// });
		//
		// // Listview on child click listener
		// expandlist.setOnChildClickListener(new OnChildClickListener() {
		//
		// @Override
		// public boolean onChildClick(ExpandableListView parent, View v,
		// int groupPosition, int childPosition, long id) {
		// // TODO Auto-generated method stub
		// Toast.makeText(
		// getActivity(),
		// listDataHeader.get(groupPosition)
		// + " : "
		// + listDataHeader.get(
		// listDataHeader.get(groupPosition)).get(
		// childPosition), Toast.LENGTH_SHORT)
		// .show();
		// return false;
		// }
		// });

		Prefrences.selectedCheckitem = 2;

		searchcheck.addTextChangedListener(new TextWatcher() {

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
				Prefrences.selectedCheckitem = 6;
				ChecklistFragment.this.checkadapter.search2(arg0.toString());
			}
		});

		active.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.selectedCheckitem = 3;

				ParentLevel listAdapter = new ParentLevel(con, activeHeader);

				actualListview.setAdapter(listAdapter);
				expandlist.setVisibility(View.VISIBLE);

				progressList.setVisibility(View.GONE);
				active.setBackgroundResource(R.color.black);
				all.setBackgroundResource(R.drawable.all_white_background);
				progress.setBackgroundResource(R.color.white);
				complete.setBackgroundResource(R.drawable.complete_white_background);

				active.setTextColor(Color.WHITE);
				complete.setTextColor(Color.BLACK);
				all.setTextColor(Color.BLACK);
				progress.setTextColor(Color.BLACK);

			}
		});
		all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Prefrences.selectedCheckitem = 2;

				ParentLevel listAdapter = new ParentLevel(con, listDataHeader);
				actualListview.setAdapter(listAdapter);
				expandlist.setVisibility(View.VISIBLE);
				progressList.setVisibility(View.GONE);
				active.setBackgroundResource(R.color.white);
				progress.setBackgroundResource(R.color.white);
				all.setBackgroundResource(R.drawable.all_black_background);
				complete.setBackgroundResource(R.drawable.complete_white_background);

				active.setTextColor(Color.BLACK);
				complete.setTextColor(Color.BLACK);
				all.setTextColor(Color.WHITE);
				progress.setTextColor(Color.BLACK);
			}
		});

		progress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.selectedCheckitem = 5;
				expandlist.setVisibility(View.GONE);
				progressAdapter adapter = new progressAdapter(con, progressarr);
				progressList.setAdapter(adapter);
				progressList.setVisibility(View.VISIBLE);
				active.setBackgroundResource(R.color.white);
				complete.setBackgroundResource(R.drawable.complete_white_background);
				all.setBackgroundResource(R.drawable.all_white_background);
				progress.setBackgroundResource(R.color.black);

				active.setTextColor(Color.BLACK);
				complete.setTextColor(Color.BLACK);
				all.setTextColor(Color.BLACK);
				progress.setTextColor(Color.WHITE);
			}
		});

		complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.selectedCheckitem = 4;
				ParentLevel listAdapter = new ParentLevel(con, completeHeader);
				actualListview.setAdapter(listAdapter);
				expandlist.setVisibility(View.VISIBLE);
				progressList.setVisibility(View.GONE);
				progress.setBackgroundResource(R.color.white);
				active.setBackgroundResource(R.color.white);
				all.setBackgroundResource(R.drawable.all_white_background);
				complete.setBackgroundResource(R.drawable.complete_black_background);

				active.setTextColor(Color.BLACK);
				complete.setTextColor(Color.WHITE);
				all.setTextColor(Color.BLACK);
				progress.setTextColor(Color.BLACK);
			}
		});

		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Prefrences.stopingHit == 1) {

			Prefrences.stopingHit = 0;
			projectDetail(getActivity());

		}
	}

	public void onBackPressed() {

		activeHeader.clear();
		completeHeader.clear();
		progressarr.clear();
		listDataHeader.clear();
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

			if (check.status.equalsIgnoreCase("Completed")) {
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

			} else if (check.status.equalsIgnoreCase("In-Progress")) {
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
			} else if (check.status.equalsIgnoreCase("Not Applicable")) {
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

					String idcheck, status;
					idcheck = check.id.toString();
					status = check.status.toString();
					// Log.e("","874378684376348257"+id);
					showcomments(idcheck, status, check);

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

			if (photocount > 0) {
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
					idcheck = body.id.toString();
					status = body.status.toString();

					showcomments(idcheck, status, body);
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
					intent.putExtra("body", body.body.toString());
					intent.putExtra("itemtype", body.itemType.toString());
					intent.putExtra("status", body.status.toString());
					intent.putExtra("id", body.id.toString());
					startActivity(intent);
					getActivity().overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
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

	public static void showcomments(String idCheck, String status,
			final CheckItems check) {

		Prefrences.showLoadingDialog(con, "Loading...");

		RequestParams params = new RequestParams();

		params.put("id", idCheck);
		params.put("status", status);
		// Log.e("json:", json1.toString());
		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.get(Prefrences.url + "/checklist_items/" + idCheck,// +
																	// Prefrences.selectedProId,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {

						JSONObject res = null;

						try {

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
										.getString("phone"), cmpny
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

							checklistitemdata.add(new DataCheckListItems(
									punchlists.getString("id"), punchlists
											.getString("body"), punchlists
											.getString("status"), punchlists
											.getString("item_type"), punchlists
											.getString("photos_count"),
									punchlists.getString("comments_count"),
									photos, comments, punchlists
											.getString("phase_name"),
									punchlists.getString("project_id")));

							// Log.d("checklistfragment ",
							// "lalalala"+CheckItemClick.comm.size());

							Intent intent = new Intent(con,
									CheckItemClick.class);
							intent.putExtra("body", check.body.toString());
							intent.putExtra("id", check.id.toString());
							// in.putParcelableArrayListExtra("key",
							// (ArrayList<? extends Parcelable>) comments);
							// in.putExtra("hello",comments);
							intent.putExtra("status", check.status.toString());
							intent.putExtra("itemtype",
									check.itemType.toString());
							// Log.d("111111","********----------"+childPosition+groupPosition);
							con.startActivity(intent);
							((Activity) con).overridePendingTransition(
									R.anim.slide_in_right,
									R.anim.slide_out_left);

						} catch (Exception e) {
							e.printStackTrace();
						}
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
		client.setTimeout(100000);
		client.get(Prefrences.url + "/checklists/" + Prefrences.selectedProId,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);
						checkall.clear();
						JSONObject res = null;
						try {
							res = new JSONObject(response);

							JSONObject checklist = res
									.getJSONObject("checklist");
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

										checkItemList.add(new CheckItems(
												cCount.getString("id"),
												cCount.getString("body"),
												cCount.getString("status"),
												cCount.getString("item_type"),
												cCount.getString("photos_count"),
												cCount.getString("comments_count")));

										if (!checkItemList.get(m).status
												.equals("Completed")
												&& !checkItemList.get(m).status
														.equals("Not Applicable")) {
											activeCheckItemList.add(new CheckItems(
													cCount.getString("id"),
													cCount.getString("body"),
													cCount.getString("status"),
													cCount.getString("item_type"),
													cCount.getString("photos_count"),
													cCount.getString("comments_count")));
										}

										if (checkItemList.get(m).status
												.equals("Completed")) {
											completeCheckItemList.add(new CheckItems(
													cCount.getString("id"),
													cCount.getString("body"),
													cCount.getString("status"),
													cCount.getString("item_type"),
													cCount.getString("photos_count"),
													cCount.getString("comments_count")));
										}

										if (checkItemList.get(m).status
												.equals("In-Progress")) {

											progressList.add(new CheckItems(
													cCount.getString("id"),
													cCount.getString("body"),
													cCount.getString("status"),
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
										activeSubCatList.add(new SubCategories(
												uCount.getString("name"),
												uCount.getString("completed_date"),
												uCount.getString("milestone_date"),
												uCount.getString("progress_percentage"),
												activeCheckItemList));
									}

									if (completeCheckItemList.size() > 0) {
										completeSubCatList.add(new SubCategories(
												uCount.getString("name"),
												uCount.getString("completed_date"),
												uCount.getString("milestone_date"),
												uCount.getString("progress_percentage"),
												completeCheckItemList));
									}

									subCatList.add(new SubCategories(
											uCount.getString("name"),
											uCount.getString("completed_date"),
											uCount.getString("milestone_date"),
											uCount.getString("progress_percentage"),
											checkItemList));

									psubCatList.add(new SubCategories(
											uCount.getString("name"),
											uCount.getString("completed_date"),
											uCount.getString("milestone_date"),
											uCount.getString("progress_percentage"),
											progressList));
								}
								Log.d("checkall",
										"size of checkall" + checkall.size());
								categList.add(new Categories(count
										.getString("name"), count
										.getString("completed_date"), count
										.getString("milestone_date"), count
										.getString("progress_percentage"),
										subCatList));

								pcategList.add(new Categories(count
										.getString("name"), count
										.getString("completed_date"), count
										.getString("milestone_date"), count
										.getString("progress_percentage"),
										psubCatList));

								if (activeSubCatList.size() > 0) {
									activelist.add(new Categories(count
											.getString("name"), count
											.getString("completed_date"), count
											.getString("milestone_date"), count
											.getString("progress_percentage"),
											activeSubCatList));

								}

								if (completeSubCatList.size() > 0) {
									completelist.add(new Categories(count
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

							// for(int a=0; a < checkall.size(); a++)
							// {
							// if(!checkall.get(a).status.equals("Completed") &&
							// !checkall.get(a).status.equals("Not Applicable"))
							// {
							// activeCheckItemList.addAll(checkall);
							// }
							// else
							// if(checkall.get(a).status.equals("Completed"))
							// {
							// completeCheckItemList.addAll(checkall);
							// }
							// else
							// if(checkall.get(a).status.equals("In-Progress"))
							// {
							// progressList.addAll(checkall);
							// }
							//
							// }
							// progressList2.addAll(progressList);
							//
							// for(int b=0;b<subCatList.size();b++)
							// {
							// if(activeCheckItemList.size()>0)
							// {
							// activeSubCatList.add(new
							// SubCategories(subCatList.get(b).name,
							// subCatList.get(b).completedDate,
							// subCatList.get(b).milestoneDate
							// , subCatList.get(b).progPerc,
							// activeCheckItemList));
							// }
							// if(completeCheckItemList.size()>0)
							// {
							// completeSubCatList.add(new
							// SubCategories(subCatList.get(b).name,
							// subCatList.get(b).completedDate,
							// subCatList.get(b).milestoneDate
							// , subCatList.get(b).progPerc,
							// completeCheckItemList));
							// }
							// PsubCatList.add(new
							// SubCategories(subCatList.get(b).name,
							// subCatList.get(b).completedDate,
							// subCatList.get(b).milestoneDate
							// , subCatList.get(b).progPerc, progressList));
							//
							// }
							//
							// for(int c=0;c<categList.size();c++)
							// {
							// if(activeSubCatList.size()>0)
							// {
							// activelist.add(new
							// Categories(categList.get(c).name,
							// categList.get(c).completedDate,
							// categList.get(c).milestoneDate
							// , categList.get(c).progPerc, activeSubCatList));
							//
							// }
							// if(completeSubCatList.size()>0)
							// {
							// completelist.add(new
							// Categories(categList.get(c).name,
							// categList.get(c).completedDate,
							// categList.get(c).milestoneDate
							// , categList.get(c).progPerc,
							// completeSubCatList));
							// //Log.d("ActiveList",""+activelist.size());
							// }
							// PcategList.add(new
							// Categories(categList.get(c).name,
							// categList.get(c).completedDate,
							// categList.get(c).milestoneDate
							// , categList.get(c).progPerc, PsubCatList));
							// }

							Log.d("project adapter ", "Progress List size = "
									+ progressList2.size());
							// for(int i=0;i<progressList2.size();i++)
							Log.d("project adapter ",
									"body" + progressList2.size());
							// activity.startActivity(new Intent(activity,
							// ProjectDetail.class));
							// activity.overridePendingTransition(R.anim.slide_in_right,
							// R.anim.slide_out_left);

							listDataHeader = categList;
							activeHeader = activelist;
							completeHeader = completelist;
							progressarr = progressList2;

							ParentLevel listAdapter = new ParentLevel(con,
									listDataHeader);
							// checkListView.setAdapter(listAdapter);
							actualListview.setAdapter(listAdapter);
							checkadapter = new checkAdapter(con);
							searchlist.setAdapter(checkadapter);
							searchlist.setVisibility(View.GONE);

							// final ListView lv = list.getRefreshableView();
							// lv.addFooterView(footerView);
							// // list.setAdapter(adpter);
							//
							// list.setAdapter(checkadapter);
							// list.setVisibility(View.VISIBLE);
							if (pull == true) {
								pull = false;
								expandlist.setVisibility(View.VISIBLE);
								progressList.setVisibility(View.GONE);
								active.setBackgroundResource(R.color.white);
								progress.setBackgroundResource(R.color.white);
								all.setBackgroundResource(R.drawable.all_black_background);
								complete.setBackgroundResource(R.drawable.complete_white_background);

								active.setTextColor(Color.BLACK);
								complete.setTextColor(Color.BLACK);
								all.setTextColor(Color.WHITE);
								progress.setTextColor(Color.BLACK);
								expandlist.onRefreshComplete();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Prefrences.dismissLoadingDialog();
						if (pull == true) {
							pull = false;
							expandlist.onRefreshComplete();
						}
					}
				});

	}

}
