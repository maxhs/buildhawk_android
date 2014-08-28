package com.buildhawk;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buildhawk.utils.Assignee;
import com.buildhawk.utils.CommentUser;
import com.buildhawk.utils.Comments;
import com.buildhawk.utils.Company;
import com.buildhawk.utils.PunchListsItem;
import com.buildhawk.utils.PunchListsPhotos;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

public class WorkListCompleteAdapter extends BaseAdapter {

//	public static ArrayList<PunchListsItem> punchListItem2 = new ArrayList<PunchListsItem>();
//	ArrayList<Assignee> assigne2;
//	public static ArrayList<Comments> commnt2;
//	ArrayList<CommentUser> cusr2;
//	public static ArrayList<PunchListsPhotos> punchlistphoto2;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	private LayoutInflater minflater;
	int len;
	 Activity activity;
	ArrayList<PunchListsItem> data = new ArrayList<PunchListsItem>();

	public WorkListCompleteAdapter(Activity activity,
			ArrayList<PunchListsItem> data) {
		super();
		Log.d("finally", "in adapter");
		minflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		len = data.size();
		this.data = data;
		this.activity = activity;
		connDect = new ConnectionDetector(activity);
		isInternetPresent = connDect.isConnectingToInternet();

	}

	@Override
	public int getCount() {

		return len;

	}

	@Override
	public Object getItem(int position) {

		return position;

	}

	@Override
	public long getItemId(int position) {

		return position;

	}

	public static class ViewHolder {

		public TextView textview;
		public ImageView right_img;
		public RelativeLayout RelativeLayout1;
		int position;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		Log.i("postion", "" + position);
		final ViewHolder holder;

		if (convertView == null) {

			vi = minflater.inflate(
					R.layout.work_list_complete_adapter_listitem, null);

			holder = new ViewHolder();

			holder.textview = (TextView) vi.findViewById(R.id.textviewHeaderProjSynopsis);
			holder.textview.setTypeface(Prefrences.helveticaNeuelt(activity));
			holder.right_img = (ImageView) vi.findViewById(R.id.imageView1);
			holder.RelativeLayout1 = (RelativeLayout) vi
					.findViewById(R.id.Rlayout_work_complete);
			holder.RelativeLayout1.setLayoutParams(new ListView.LayoutParams(
					ListView.LayoutParams.MATCH_PARENT, 200));
			ASSL.DoMagic(holder.RelativeLayout1);
			vi.setTag(holder);

		}

		else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.position = position;
		holder.textview.setTag(holder);
		holder.right_img.setTag(holder);
		
		holder.textview.setText(data.get(position).body.toString());
		
		Log.d("Size of ","Photos : "+data.get(position).photos.size());			
				if(data.get(position).photos.size()>0){//get(0).url200.equals("drawable")) {
					Picasso.with(activity)
					.load(data.get(position).photos.get(0).urlThumb)
					.placeholder(R.drawable.default_200)
					.into(holder.right_img);
		//	holder.image_count.setText(""
		//			+ reportdata_adp.get(position).photos.size());
		//	Log.d("if", "----if---");
		} else {
			holder.right_img.setImageResource(R.drawable.default_200);
		//	holder.image_count.setText(" ");
		//	Log.d("if", "----else---");
		
		}
		
		
		holder.RelativeLayout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(ConnectionDetector.isConnectingToInternet()){	
				Intent intent = new Intent(activity,WorkItemClick.class);				 				
				intent.putExtra("id",data.get(position).id.toString());
				activity.startActivity(intent);		
				activity.overridePendingTransition(
						R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			else{
				Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
			}
//				activity.finish();
				
//				if (isInternetPresent) {
//				
//				ViewHolder holder2 = (ViewHolder) v.getTag();
//				punchlst_item(data.get(position).id.toString(), data,
//						holder2.position);
//				Log.d("",
//						"posi tion --- " + holder2.position + ", "
//								+ data.get(position).id.toString());
//				}else{
//					DatabaseClass dbObject = new DatabaseClass(activity);
//					dbObject.open();
//					if(dbObject.exists_workitem(Prefrences.selectedProId, data.get(position).id.toString())){
//						String response = dbObject.get_workitem(Prefrences.selectedProId, data.get(position).id.toString());
//						dbObject.close();
//						fillServerData(response, data.get(position).id.toString());
//						
//					}else{
//						dbObject.close();
//						Toast.makeText(activity,"No internet connection.", Toast.LENGTH_SHORT).show();
//					}
//				}

			}
		});
		
		//****************************** API change ****************************
//		if (data.get(position).photos.size() == 0) {
//			holder.right_img.setImageResource(R.drawable.default_200);
//		} else {
//			Picasso.with(activity)
//					.load(data.get(position).photos.get(0).urlThumb.toString())
//					.placeholder(R.drawable.default_200)
//					.into(holder.right_img);
//		}
		
		
		
//	}
//		else if(Prefrences.worklisttypes==1){
//
//			for(int i =0;i<data.size();i++)
//			{
//				if(!data.get(position).completed.toString().equalsIgnoreCase("true"))
//				{
////					!punchListItem.get(i).completed
////					.equals("true")
//					holder.textview.setText(data.get(position).body.toString());
//					holder.RelativeLayout1.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							ViewHolder holder2 = (ViewHolder) v.getTag();
//							punchlst_item(data.get(position).id.toString(), data,
//									holder2.position);
//							Log.d("",
//									"posi tion --- " + holder2.position + ", "
//											+ data.get(position).id.toString());
//							
//						}
//					});
//					if (data.get(position).photos.size() == 0) {
//						holder.right_img.setImageResource(R.drawable.default_200);
//					} else {
//						Picasso.with(activity)
//								.load(data.get(position).photos.get(0).urlThumb.toString())
//								.placeholder(R.drawable.default_200)
//								.into(holder.right_img);
//					}
//				}
//			}
//			
//			
//		}
		return vi;

	}

	// ************ API for Get Worklist Item . *************//

//	public void punchlst_item(final String punchlist_id,
//			final ArrayList<PunchListsItem> data, final int position) {
//
//		Prefrences.showLoadingDialog(activity, "Loading...");
//		AsyncHttpClient client = new AsyncHttpClient();
//
//		client.addHeader("Content-type", "application/json");
//		client.addHeader("Accept", "application/json");
//		client.get(Prefrences.url + "/worklist_items/" + punchlist_id,
//				new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onSuccess(String response) {
//						DatabaseClass dbObject = new DatabaseClass(activity);
//						dbObject.open();
//						if(dbObject.exists_workitem(Prefrences.selectedProId, punchlist_id)){
//							dbObject.update_workitem(Prefrences.selectedProId, punchlist_id, response);
//						}else{
//							dbObject.CreateEntry_workitem(Prefrences.selectedProId, punchlist_id, response);
//						}
//						dbObject.close();
//						fillServerData(response,punchlist_id);
//						Prefrences.dismissLoadingDialog();
//
//					}
//
//					@Override
//					public void onFailure(Throwable arg0) {
//
//						Log.e("request fail", arg0.toString());
//						Toast.makeText(activity, "Server Issue",
//								Toast.LENGTH_LONG).show();
//
//						Prefrences.dismissLoadingDialog();
//
//					}
//
//				});
//
//	}
	
//	public void fillServerData(String response, String item_id){
//		JSONObject res = null;
//		try {
//			
//			
//			res = new JSONObject(response);
//			Log.v("response ", "" + res.toString(2));
//			punchListItem2.clear();
//			JSONObject count = null;
//
//			// if (res.has("punchlist_item")) {
//			// count = res.getJSONObject("punchlist_item");//
//			// chexklist
//			//
//			// } else {
//			count = res.getJSONObject("worklist_item");
//
//			// }
//
//			if (!count.isNull("assignee")) {
//				JSONObject asigne = count
//						.getJSONObject("assignee");// subcateg
//				Log.i("asigneees", "" + asigne);
//
//				assigne2 = new ArrayList<Assignee>();
//
//				JSONObject company = asigne
//						.getJSONObject("company");// checkitem
//
//				ArrayList<Company> compny = new ArrayList<Company>();
//
//				compny.add(new Company(company.getString("id"),
//						company.getString("name")));
//				assigne2.add(new Assignee(asigne
//						.getString("id"), asigne
//
//				.getString("first_name"), asigne
//
//				.getString("last_name"), asigne
//
//				.getString("full_name"), asigne
//
////				.getString("admin"), asigne
//
////				.getString("company_admin"), asigne
//
////				.getString("uber_admin"), asigne
//
//				.getString("email"), asigne
//
//				.getString("formatted_phone"), asigne
//
////				.getString("authentication_token"),
//
////				asigne
//				.getString("url_thumb"),
//
//				asigne.getString("url_small"),
//
//				compny));
//
//			}
//
//			punchlistphoto2 = new ArrayList<PunchListsPhotos>();
//
//			// commnt2 = new ArrayList<Comments>();
//			// commnt2.clear();
//			// punchlistphoto2.clear();
//			// assigne2.clear();
//
//			JSONArray phot = count.getJSONArray("photos");
//
//			for (int j = 0; j < phot.length(); j++) {
//
//				JSONObject ccount = phot.getJSONObject(j);
//
//				// Log.d("photos data","---------------");
//				//
//				// Log.d("id", ccount.getString("id"));
//				//
//				// Log.d("url_large",
//				// ccount.getString("url_large"));
//				//
//				// Log.d("original",
//				// ccount.getString("original"));
//				//
//				// Log.d("url_small",
//				// ccount.getString("url_small"));
//				//
//				// Log.d("url_thumb",
//				// ccount.getString("url_thumb"));
//				//
//				// Log.d("image_file_size",
//				// ccount.getString("image_file_size"));
//				//
//				// Log.d("image_content_type",
//				// ccount.getString("image_content_type"));
//				//
//				// Log.d("source", ccount.getString("source"));
//				//
//				// Log.d("phase", ccount.getString("phase"));
//				//
//				// Log.d("created_at",
//				// ccount.getString("created_at"));
//				//
//				// Log.d("user_name",
//				// ccount.getString("user_name"));
//				//
//				// Log.d("created_date",
//				// ccount.getString("created_date"));
//				//
//				// Log.d("assignee",
//				// ccount.getString("assignee"));
//
//				punchlistphoto2.add(new PunchListsPhotos(
//
//				ccount.getString("id"),
//
//				ccount.getString("url_large"),
//
//				ccount.getString("original"),
//
//				ccount.getString("url_small"),
//
//				ccount.getString("url_thumb"),
//
//				ccount.getString("image_file_size"),
//
//				ccount.getString("image_content_type"),
//
//				ccount.getString("source"), ccount
//
//				.getString("phase"), ccount
//
//				.getString("created_at"),
//
//				ccount.getString("user_name"),
//
//				ccount.getString("name"), ccount
//
//				.getString("description"), ccount
//
//				.getString("created_date"),
//				// ccount.getString("assignee"),
//						ccount.getString("epoch_time")));
//
//				Log.i("photos", "" + phot);
//
//			}
//
//			JSONArray comm = count.getJSONArray("comments");
//
//			commnt2 = new ArrayList<Comments>();
//
//			for (int k = 0; k < comm.length(); k++) {
//
//				JSONObject ccount = comm.getJSONObject(k);
//
//				JSONObject cuser = ccount
//
//				.getJSONObject("user");
//
//				Log.i("comment user", "" + cuser);
//
//				cusr2 = new ArrayList<CommentUser>();
//
//				JSONObject company = cuser
//
//				.getJSONObject("company");
//
//				ArrayList<Company> compny = new ArrayList<Company>();
//
//				compny.add(new Company(company
//
//				.getString("id"), company
//
//				.getString("name")));
//
//				// Log.i("ddddddd", "" + company);
//
//				cusr2.add(new CommentUser(cuser
//
//				.getString("id"), cuser
//
//				.getString("first_name"), cuser
//
//				.getString("last_name"), cuser
//
//				.getString("full_name"), cuser
//
////				.getString("admin"), cuser
////
////				.getString("company_admin"), cuser
////
////				.getString("uber_admin"), cuser
//
//				.getString("email"), cuser
//
//				.getString("formatted_phone"), cuser
//
////				.getString("authentication_token"),
////
////				cuser
//				.getString("url_thumb"), cuser
//
//				.getString("url_small"),
//
//				compny));
//
//				commnt2.add(new Comments(ccount
//
//				.getString("id"), ccount
//
//				.getString("body"), cusr2, ccount
//
//				.getString("created_at")));
//
//				Log.d("comments in loop ",
//						"size==" + commnt2.size());
//
//			}
//
//			punchListItem2.add(new PunchListsItem(count
//
//			.getString("id"), count
//
//			.getString("body"), assigne2, count
//
//			// .getString("sub_assignee"), count
//
//					.getString("location"), count
//
//			.getString("completed_at"), count
//
//			.getString("completed"),
//
//			punchlistphoto2, count.getString("created_at"),
//
//			commnt2, count.getString("epoch_time")));
//
//			// Log.d("comment end ","size=="+commnt2.size());
//
//			// if (punchListItem.get(i).completed
//
//			// .equals("true")) {
//
//			// worklist_complted.add(new PunchListsItem(
//
//			// count.getString("id"), count
//
//			// .getString("body"),
//
//			// assigne, count
//
//			// .getString("sub_assignee"),
//
//			// count.getString("location"), count
//
//			// .getString("completed_at"),
//
//			// count.getString("completed"),
//
//			// punchlistphoto, count
//
//			// .getString("created_at"),
//
//			// commnt,count.getString("epoch_time")));
//
//			// Log.i("true",
//
//			// "--------Completed-------------"
//
//			// + punchListItem.get(i).body
//
//			// .toString());
//
//			// } else if (!punchListItem.get(i).completed
//
//			// .equals("true")) {
//
//			// worklist_Active.add(new PunchListsItem(
//
//			// count.getString("id"), count
//
//			// .getString("body"),
//
//			// assigne, count
//
//			// .getString("sub_assignee"),
//
//			// count.getString("location"), count
//
//			// .getString("completed_at"),
//
//			// count.getString("completed"),
//
//			// punchlistphoto, count
//
//			// .getString("created_at"),
//
//			// commnt,count.getString("epoch_time")));
//
//			//
//
//			// }
//
//			// Log.d("punchlist_item",
//			//
//			// "Size" + punchListItem.size());
//			//
//			//
//			// Log.d("punchlist_item", "Size" +
//			// personnel.size());
//			//
//			//
//			//
//			// punchList.add(new PunchList(punchListItem,
//			//
//			// personnel));
//			//
//			// Log.d("punchlist", "--------" + personnel);
//
//			// totalCount = Integer.toString(punchList.size());
//
//			// Log.i("value..",""+punchList.size());
//
//			Intent intent = new Intent(activity,
//					WorkItemClick.class);
//			// startActivity(new Intent(getActivity(),
//			// ImageActivity.class));
//			// in.putExtra("assigne",data.get(position).assignee.get(0).fullName.toString());
//
//			// in.putParcelableArrayListExtra("array",(ArrayList<?
//			// extends Parcelable>) data );
//			// in.putExtra("pos", holder2.p);
//			intent.putExtra("id",
//					count.getString("id"));
//			intent.putExtra("location",
//					count.getString("location"));
//			intent.putExtra("body",
//					count.getString("body"));
//			intent.putExtra("completed",
//					count.getString("completed"));
//		
//				
//			try{
//			intent.putExtra("assignee",count.getJSONObject("assignee").getString("full_name"));
//			}catch(JSONException e){
//				intent.putExtra("assignee",	"");
//			}
//			
//			activity.startActivity(intent);
//			activity.overridePendingTransition(
//					R.anim.slide_in_right,
//					R.anim.slide_out_left);
//			
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//	}

}