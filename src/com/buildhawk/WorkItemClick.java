package com.buildhawk;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import rmn.androidscreenlibrary.ASSL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.buildhawk.R.color;
import com.buildhawk.R.drawable;
import com.buildhawk.utils.Assignee;
import com.buildhawk.utils.CommentUser;
import com.buildhawk.utils.Comments;
import com.buildhawk.utils.Company;
import com.buildhawk.utils.DialogBox;
import com.buildhawk.utils.PunchListsItem;
import com.buildhawk.utils.PunchListsPhotos;
import com.buildhawk.utils.Users;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

public class WorkItemClick extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	private static int TAKE_PICTURE = 1;
	private static int PICK_CONTACT = 1;
	private Handler mHandler = new Handler();
	RelativeLayout relLay;
	

	ImageView img_ladder_page2 = null;
//	ArrayList<PunchListsPhotos> photosarrayArrayList;
	ArrayList<String> arr = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<String> desc = new ArrayList<String>();

	ArrayList<PunchListsItem> array = new ArrayList<PunchListsItem>();
	ArrayList<String> loc = new ArrayList<String>();
	ArrayList<String> ass = new ArrayList<String>();
	public static ArrayList<String> locs;
	public static ArrayList<String> asss;
	String picturePath = "";
	private Uri imageUri;
	LinearLayout lin2;
	EditText txt_body;
	public static Button btnS_location, btnS_assigned;
//	String assignee_str,location_str;
	ListView commentslist;
	EditText txt_commentbody;
	Button btn_sendcomment;
	Button btn_markcomplete;
	Button btn_save;
	static Context con;
	static String id_punchlist_item;
//	int pos;
	static TextView reminder_txt;
	ScrollView scrollView;
	ArrayList<String> picarray = new ArrayList<String>();
//	ArrayList<Comments> comm = new ArrayList<Comments>();
	CustomDateTimePicker custom;
//	String assignee_id;
	
	ConnectionDetector connDect;
	
	
	public static ArrayList<PunchListsItem> punchListItem2 = new ArrayList<PunchListsItem>();
	ArrayList<Assignee> assigne2;
	public  ArrayList<Comments> commnt2;
	ArrayList<CommentUser> cusr2;
	public  ArrayList<PunchListsPhotos> punchlistphoto2;
	commentadapter comadap;
	LinearLayout commentLayout;
	

	SwipeDetector swipeDetecter = new SwipeDetector();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_item_click);

		relLay = (RelativeLayout) findViewById(R.id.rellayworkitem);
		new ASSL(this, relLay, 1134, 720, false);

		con = WorkItemClick.this;
		txt_body = (EditText) findViewById(R.id.txt_body);
		btnS_assigned = (Button) findViewById(R.id.assigned_btn);
		btnS_location = (Button) findViewById(R.id.location_btn);

		btn_markcomplete = (Button) findViewById(R.id.btnComplete);
		ImageView img_reminder = (ImageView) findViewById(R.id.reminder);
		ImageView img_camera = (ImageView) findViewById(R.id.camera);
		ImageView img_gallery = (ImageView) findViewById(R.id.photogallery);
		ImageView img_call = (ImageView) findViewById(R.id.call);
		Button btn_back = (Button) findViewById(R.id.relativeLayoutBack);
		btn_save = (Button) findViewById(R.id.save);
		ImageView img_email = (ImageView) findViewById(R.id.email);
		ImageView img_message = (ImageView) findViewById(R.id.message);
		lin2 = (LinearLayout) findViewById(R.id.linearlayoutLin2);
		txt_commentbody = (EditText) findViewById(R.id.commentBody);
		btn_sendcomment = (Button) findViewById(R.id.sendcomment);
		reminder_txt= (TextView) findViewById(R.id.txtReminder);
		commentLayout=(LinearLayout)findViewById(R.id.LinearLayout1);
		reminder_txt.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		
		btn_save.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		btn_back.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));

		txt_body.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		btnS_location.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		btnS_assigned.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));

		txt_commentbody.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		btn_sendcomment.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		btn_markcomplete.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		// photosarrayArrayList.clear();
//		photosarrayArrayList = new ArrayList<PunchListsPhotos>();
		
		
//		if (Prefrences.text != 10) {
//			photosarrayArrayList
//					.addAll(punchlistphoto2);
//		}

		

		if (ProjectDetail.flag == 0) {
		connDect = new ConnectionDetector(con);
		commentLayout.setVisibility(View.VISIBLE);
		if (connDect.isConnectingToInternet()) {
			if (Prefrences.worklistNotification.equals("")) {
				Log.d("ID ", "ID------ " + Prefrences.worklistNotification);
				Bundle bundle = getIntent().getExtras();
				id_punchlist_item = bundle.getString("id");
				Log.d("ID ", "ID********* " + id_punchlist_item);
				punchlst_item(id_punchlist_item);
			} else {
				
				id_punchlist_item=Prefrences.worklistNotification;
				Log.d("ID ", "ID------- " + id_punchlist_item);
				
				punchlst_item(id_punchlist_item);
			}
		}else{
			DatabaseClass dbObject = new DatabaseClass(con);
			dbObject.open();
			if(dbObject.exists_workitem(Prefrences.selectedProId,id_punchlist_item.toString())){
				String response = dbObject.get_workitem(Prefrences.selectedProId, id_punchlist_item.toString());
				dbObject.close();
				fillServerData(response, id_punchlist_item.toString());
				
			}else{
				dbObject.close();
				Toast.makeText(con,"No internet connection.", Toast.LENGTH_SHORT).show();
			}
		}
		}
		else{
			loc.addAll(WorklistFragment.locs);
			ass.addAll(WorklistFragment.assignees);
			Log.d("loc", "sizeeee" + loc.size());
			Log.d("user", "sizeeee" + ass.size());
			LinkedHashSet<String> listToSet = new LinkedHashSet<String>(loc);
			LinkedHashSet<String> listToSet2 = new LinkedHashSet<String>(ass);
			locs = new ArrayList<String>(listToSet);
			asss = new ArrayList<String>(listToSet2);
			Log.d("locssss", "sizeeee" + locs.size());
			Log.d("usrs", "sizeeee" + asss.size());
			txt_body.setText("");
			// btn_location.setText("Location");
			btn_save.setText("Add");
			commentLayout.setVisibility(View.GONE);
		}
		
		scrollView = (ScrollView) findViewById(R.id.pullToRefreshScrollView);

		// scrollView.post(new Runnable() {
		// public void run() {
		// scrollView.fullScroll(ScrollView.FOCUS_UP);
		// }
		// });

		scrollView.scrollTo(0, 0);
		scrollView.scrollBy(0, 0);

		commentslist = (ListView) findViewById(R.id.commentslist);
		commentslist.setFocusable(false);
		
		
		custom = new CustomDateTimePicker(this,
	            new CustomDateTimePicker.ICustomDateTimeListener() {

			@Override
			public void onSet(Dialog dialog, Calendar calendarSelected,
					Date dateSelected, int year, String monthFullName,
					String monthShortName, int monthNumber, int date,
					String weekDayFullName, String weekDayShortName,
					int hour24, int hour12, int min, int sec,
					String AM_PM) {
				// TODO Auto-generated method stub

		                           reminder_txt.setText(monthShortName 
		                                    + " " + calendarSelected
                                            .get(Calendar.DAY_OF_MONTH) + " " + year
		                                    + " " + hour24 + ":" + min
		                                    + ":" + sec);
		                           String str = reminder_txt.getText().toString()+".000 UTC";
		                   	    SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
		                   	    Date date1 = null;
								try {
									date1 = df.parse(str);
								} catch (java.text.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		                   	    long epoch = date1.getTime();
		                   	    System.out.println(epoch); // 1055545912454
//		                           ReminderSet(epoch);
	                }

	                @Override
	                public void onCancel() {

	                }

	            });

	  /**
	    * Pass Directly current time format it will return AM and PM if you set
	    * false
	    */
	    custom.set24HourFormat(false);
	    /**
	    * Pass Directly current data and time to show when it pop up
	    */
		   custom.setDate(Calendar.getInstance());
		   
		   img_reminder.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Toast.makeText(getApplicationContext(), "Reminder Underconstruction...", Toast.LENGTH_LONG).show();
					
						
					custom.showDialog();
//					
				}
			});

		
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				if(ConnectionDetector.isConnectingToInternet()){
				if (btn_save.getText().toString().equalsIgnoreCase("Add")) {
					
					
					create_worklist(txt_body.getText().toString(),
							Prefrences.userId,Prefrences.location_str// btnS_location.getText().toString()
							, Prefrences.assignee_str//btnS_assigned.getText().toString()
									, Prefrences.selectedProId);
					

				} else {
					// new LongOperation1().execute("");
					updateWorkListHit(WorkItemClick.this);

				}
				}else{
					Toast.makeText(WorkItemClick.this,"No internet connection.", Toast.LENGTH_SHORT).show();
				}
				

			}
		});

		btn_markcomplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btn_markcomplete.getText().toString().equals("Mark Complete")) {
					btn_markcomplete.setText("Completed");
					btn_markcomplete.setBackgroundResource(drawable.completecolor);
					btn_markcomplete.setTextColor(Color.WHITE);
					Prefrences.statusCompleted = 1;
				} else {
					btn_markcomplete.setText("Mark Complete");
					btn_markcomplete.setBackgroundResource(drawable.markcomplete);
					btn_markcomplete.setTextColor(Color.BLACK);
					Prefrences.statusCompleted = 0;

				}

			}
		});

		// Log.d("--------","888888"+DocumentFragment.photosList.size());
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
			txt_commentbody.setBackground(null);
		}
		else {
			txt_commentbody.setBackgroundDrawable(null);
		}
	
		txt_commentbody.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				btn_sendcomment.setVisibility(View.GONE);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// progressList.setVisibility(View.VISIBLE);
				// Log.i("ARG", "-------" + arg0);
				if (arg0.toString().length() == 0) {
					btn_sendcomment.setVisibility(View.GONE);
				} else {
					btn_sendcomment.setVisibility(View.VISIBLE);
				}
				// ChecklistFragment.this.checkadapter.search2(arg0.toString());
			}
		});

		btn_sendcomment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				if(ConnectionDetector.isConnectingToInternet()){
				commentAdd(txt_commentbody.getText().toString());
				// commentslist.add
				commnt2.add(new Comments(id_punchlist_item, txt_commentbody.getText()
						.toString(), null, "just now"));
				Log.d("*****", "increased size" + commnt2.size());
				commentslist.setAdapter(comadap);
				comadap.notifyDataSetChanged();
				setlist(commentslist, comadap, 230);
				txt_commentbody.setText("");
				btn_sendcomment.setVisibility(View.GONE);
			}
			else{
				Toast.makeText(con, "No internet connection", Toast.LENGTH_LONG).show();
			}
				

			}
		});

		btnS_assigned.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				Log.d("", "before" + Prefrences.selectedlocation);

				Intent intent = new Intent(con,CompanyExpandable.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
//				Prefrences.text = 4;
//				Show_Dialog2(v);

				// btn_assigned.setText(""+Prefrences.selectedlocation);
				// Log.d("","after"+Prefrences.selectedlocation);
			}
		});

		btnS_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("location : ","Location : "+Prefrences.location_str);//btnS_location.getText().toString());
				Prefrences.text = 5;
				Show_Dialog2(v);
			}
		});

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Prefrences.assignee_str="";
				Prefrences.assigneeID="";
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				ProjectDetail.flag = 0;
				if(!Prefrences.worklistNotification.equalsIgnoreCase(""))
				{
					Prefrences.worklistNotification = "";
					Prefrences.comingFrom = 3;
					Intent intent = new Intent(WorkItemClick.this,
							ProjectDetail.class);
					startActivity(intent);
				}
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});

		img_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Prefrences.text = 1;

				Show_Dialog(v);
			}
		});

		img_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// sendEmail(getBaseContext(), new
				// String[]{"ravish@clicklabs.in"}, "Sending Email",
				// "Test Email", "I am body");
				Prefrences.text = 3;
				// Intent i = new Intent(WorkItemClick.this,UsersList.class);
				// startActivity(i);
				Show_Dialog(v);
			}
		});

		img_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Prefrences.text = 2;
				Show_Dialog(v);

				// Intent i = new Intent(WorkItemClick.this,UsersList.class);
				// startActivity(i);
			}
		});

		img_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});

		img_camera.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
				String date = sdf.format(Calendar.getInstance().getTime());
				Log.v("", "" + date);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				File photo = new File(
						Environment.getExternalStorageDirectory(), date
								+ ".jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				imageUri = Uri.fromFile(photo);
				picturePath = photo.toString();
				startActivityForResult(intent, TAKE_PICTURE);
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Prefrences.assignee_str="";
		Prefrences.assigneeID="";
		ProjectDetail.flag = 0;
		try {

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.hideSoftInputFromWindow(getCurrentFocus()

			.getWindowToken(), 0);

		} catch (Exception exception) {

			exception.printStackTrace();

		}
		if(!Prefrences.worklistNotification.equalsIgnoreCase(""))
		{
			Prefrences.worklistNotification = "";
			Prefrences.comingFrom = 3;
			Intent intent = new Intent(WorkItemClick.this,
					ProjectDetail.class);
			startActivity(intent);
		}
		
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if(Prefrences.stopingHit==1){
		//
		// setphotos();
		// }else
		// {

		// }
	}

	public void setphotos() {

		for (int i = 0; i < punchlistphoto2.size(); i++) {
			if (!punchlistphoto2.get(i).original.equalsIgnoreCase("")) {
				img_ladder_page2 = new ImageView(WorkItemClick.this);
				arr.add(punchlistphoto2.get(i).urlLarge);
				ids.add(punchlistphoto2.get(i).id);
				desc.add(punchlistphoto2.get(i).description);

				Log.d("array",
						"-------array-----"
								+ punchlistphoto2.get(i).urlLarge);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						(int) (200), (int) (200));
				lp.setMargins(10, 10, 10, 10);
				img_ladder_page2.setTag(i);
				img_ladder_page2.setLayoutParams(lp);
				// Picasso.with(WorkItemClick.this).load(arr.get(i).toString())
				// .into(ladder_page2);

				Picasso.with(WorkItemClick.this)
						.load(punchlistphoto2.get(i).urlSmall)

						.into(img_ladder_page2);

				// // ladder_page2.setImageBitmap(myBitmap);
				lin2.addView(img_ladder_page2);

				img_ladder_page2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Prefrences.selectedPic = (Integer) v.getTag();
						Log.i("Tag Value", "" + Prefrences.selectedPic);

						Intent intent = new Intent(con, SelectedImageView.class);
						intent.putExtra("array", arr);
						intent.putExtra("ids", ids);
						intent.putExtra("desc", desc);
						intent.putExtra("id", punchlistphoto2
								.get(Prefrences.selectedPic).id);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						// finish();
					}
				});
			}

		}
	}

	public void setlist(ListView lv, ListAdapter listAdapter, int size) {
		int maxHeight = 0;
		listAdapter = lv.getAdapter();

		// int totalHeight = listAdapter.getCount() * imageWidth;
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = null;
			listItem = listAdapter.getView(i, listItem, lv);
			if (listItem instanceof ViewGroup)
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();// (int) (size); //*
														// ASSL.Yscale());
		}
		maxHeight += totalHeight;
		ViewGroup.LayoutParams params = lv.getLayoutParams();

		params.height = totalHeight
				+ (lv.getDividerHeight() * (listAdapter.getCount() - 1));

		lv.setLayoutParams(params);
		lv.requestLayout();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Prefrences.stopingHit = 1;
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			// picarray
			ImageView ladder_page2 = null;
			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
			ladder_page2 = new ImageView(this);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					(int) (200), (int) (200));
			lp.setMargins(10, 10, 10, 10);
			ladder_page2.setLayoutParams(lp);
			// Picasso.with(CheckItemClick.this).load(picturePath).into(ladder_page2);
			// // ladder_page2.setImageBitmap(myBitmap);
			File file = new File(picturePath);
			Picasso.with(WorkItemClick.this).load(file)
			.resize((int) (200 * ASSL.Xscale()),
					(int) (200 * ASSL.Xscale()))
					.into(ladder_page2);
			lin2.addView(ladder_page2);
			
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 6;
//			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			

//			ladder_page2.setImageBitmap(BitmapFactory.decodeFile(picturePath,options));
			if (ProjectDetail.flag == 0) {
				new LongOperation().execute("");
			}
		}

		else if (requestCode == TAKE_PICTURE
				&& resultCode == Activity.RESULT_OK) {					
						
			Uri selectedImage = imageUri;
			

			picturePath = getRealPathFromURI(imageUri);
			getContentResolver().notifyChange(selectedImage, null);
			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			ImageView ladder_page2 = null;

			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
			ladder_page2 = new ImageView(WorkItemClick.this);
			// picturePath = selectedImage.toString();
			ContentResolver cr = getContentResolver();
			Bitmap bitmap;
//			picturePath=selectedImage.toString();
			try {
				bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr,
						selectedImage);
				if (ProjectDetail.flag == 0)
				{
					new LongOperation().execute("");
				}		
//				else
//					picturePath=selectedImage.toString();
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						(int) (200), (int) (200));
				lp.setMargins(10, 10, 10, 10);
				ladder_page2.setLayoutParams(lp);
				
				File file = new File(picturePath);
				Picasso.with(WorkItemClick.this).load(file)
						.placeholder(R.drawable.ic_launcher)
						.resize((int) (200 * ASSL.Xscale()),
								(int) (200 * ASSL.Xscale()))
						.centerCrop().into(ladder_page2);
				// Picasso.with(CheckItemClick.this).load(picturePath).into(ladder_page2);
				ladder_page2.setImageBitmap(bitmap);
//				ladder_page2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
				lin2.addView(ladder_page2);
				
			} catch (Exception e) {
				Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
						.show();
				Log.e("Camera", e.toString());
			}
		}
	}

	public void Show_Dialog(View v) {
		DialogBox cdd = new DialogBox(this);
		// cdd.requestWindowFeature(android.R.style.Theme_Translucent_NoTitleBar);
		// Window window = cdd.getWindow();
		// WindowManager.LayoutParams wlp = window.getAttributes();
		//
		// //wlp.gravity = Gravity.BOTTOM;
		// wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		// wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
		// window.setAttributes(wlp);
		cdd.show();
	}

	public void Show_Dialog2(View v) {
		DialogBox2 cdd = new DialogBox2(this);
		// cdd.requestWindowFeature(android.R.style.Theme_Translucent_NoTitleBar);
		// Window window = cdd.getWindow();
		// WindowManager.LayoutParams wlp = window.getAttributes();

		// wlp.gravity = Gravity.BOTTOM;
		// wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		// wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
		// window.setAttributes(wlp);
		cdd.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.work_item_click, menu);
		return true;
	}

	
	// ************ API for Add comments. *************//
	
	public void commentAdd(String body) {

		Prefrences.showLoadingDialog(this, "Loading...");
		JSONObject json = new JSONObject();
		// String body="hiii";
		try {
			json.put("worklist_item_id", id_punchlist_item);
			json.put("user_id", Prefrences.userId);
			json.put("body", body);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		ByteArrayEntity entity = null;
		try {
			entity = new ByteArrayEntity(json.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		client.post(this, Prefrences.url + "/comments/", entity,
				"application/json", new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
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
	}

	public class commentadapter extends BaseAdapter {

		public commentadapter() {

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// Log.d("comment adapter"," id = "+WorkListCompleteAdapter.data.get(pos).id);
			// Log.d("comment adapter"," size = "+WorkListCompleteAdapter.data.get(pos).comments.size());

			return commnt2.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			final ViewHolder holder;
			// final CheckItems body = (CheckItems) this.getItem(position);
			// Log.d("adp","body="+body+"item pos"+this.getItem(position));

			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater infalInflater = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.commentslistitem,
						null);
				holder.body = (TextView) convertView.findViewById(R.id.comBody);
				holder.user = (TextView) convertView.findViewById(R.id.comUser);
				holder.date = (TextView) convertView.findViewById(R.id.comDate);
				// holder.delete=(Button)convertView.findViewById(R.id.DeleteSwipe);
				holder.body.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				holder.date.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				// holder.delete.setTypeface(Prefrences.HelveticaNeueLt(getApplicationContext()));
				holder.user.setTypeface(Prefrences
						.helveticaNeuelt(getApplicationContext()));
				holder.relativelayoutComment = (RelativeLayout) convertView
						.findViewById(R.id.relativelayoutComment);
				holder.linearlayoutRoot = (LinearLayout) convertView
						.findViewById(R.id.linearlayoutRoot);
				holder.linearlayoutRoot.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 200));
				ASSL.DoMagic(holder.linearlayoutRoot);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.relativelayoutComment.setTag(holder);
			holder.position = position;

			holder.body.setText("" + commnt2.get(position).body.toString());

			if (commnt2.get(position).cuser == null) {
				holder.user.setText("");
			} else {
				holder.user.setText(""
						+ commnt2.get(position).cuser.get(0).fullName.toString());
			}
			if (commnt2.get(position).created_at.toString().equals("just now")) {
				holder.date.setText("just now");
			} else {
				
//				String date = comm.get(position).created_at.toString()
//						.substring(0, 10);
//				String time = comm.get(position).created_at.toString()
//						.substring(11, 19);
				String date = utcToLocal(commnt2.get(position).created_at.toString());
//				Log.d("date==", "" + date);
//				Log.d("time==", "" + time);
				holder.date.setText(date);
			}

			final LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
					((int) (720.0f * ASSL.Xscale())),
					((int) (200.0f * ASSL.Yscale())));

			layoutParams1.setMargins(-(int) (200.0f * ASSL.Yscale()), 0, 0, 0);
			final LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
					((int) (720.0f * ASSL.Xscale())),
					((int) (200.0f * ASSL.Yscale())));

			layoutParams2.setMargins(0, 0, 0, 0);

			holder.relativelayoutComment.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(final View v) {
					// TODO Auto-generated method stub

					// holder.delete.setVisibility(View.VISIBLE);
					ViewHolder holder2 = (ViewHolder) v.getTag();
					if (commnt2.get(holder2.position).cuser.get(0).id.toString()
							.equalsIgnoreCase(Prefrences.userId.toString())) {
						final String ids = commnt2.get(holder2.position).id
								.toString();
						Log.d("", "id===" + ids);
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								con);

						// set title
						alertDialogBuilder.setTitle("Please confirm");

						// set dialog message
						alertDialogBuilder
								.setMessage(
										"Are you sure you want to delete this comment?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// close
												// current activity
												Log.d("Dialog", "Delete");
												deletecomments(ids);
												commnt2.remove(position);
												Log.d("", "" + position);
												Log.d("", "" + commnt2.size());
												holder.relativelayoutComment
														.setLayoutParams(layoutParams2);
												holder.relativelayoutComment.removeView(v);
												notifyDataSetChanged();

											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// just close
												// the dialog box and do nothing
												Log.d("Dialog", "No");
												dialog.cancel();
											}
										});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();
					}
					return true;
				}
			});
			// holder.lin4.setOnTouchListener(swipeDetecter);
			// holder.lin4.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// ViewHolder holder1=(ViewHolder)v.getTag();
			//
			// // TODO Auto-generated method stub
			// if (swipeDetecter.swipeDetected()) {
			// if (swipeDetecter.getAction() == swipeDetecter
			// .getAction().LR) {
			// //holder1.delete.setVisibility(View.VISIBLE);
			// holder.lin4.setLayoutParams(layoutParams2);
			// Log.d("Swipe detected LR",
			// "" + swipeDetecter.swipeDetected());
			// }
			// if (swipeDetecter.getAction() == swipeDetecter
			// .getAction().RL) {
			// //holder1.delete.setVisibility(View.GONE);
			// holder.lin4.setLayoutParams(layoutParams1);
			// Log.d("Swipe detected RL",
			// "" + swipeDetecter.swipeDetected());
			// }
			// }
			// }
			// });

			// holder.delete.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(final View v) {
			// // TODO Auto-generated method stub
			//
			//
			// }
			// });

			// holder.delete.setTag(holder);
			return convertView;

		}

	}

	public static class ViewHolder {

		public TextView body, date, user;
		// Button delete;
		// public ImageView right_img;
		public RelativeLayout relativelayoutComment;
		LinearLayout linearlayoutRoot;
		int position;
	}
	
	// ************ API for Delete Comments. *************//

	public void deletecomments(String id) {

		// Prefrences.showLoadingDialog(WorkItemClick.this, "Loading...");
		JSONObject json = new JSONObject();
		RequestParams params = new RequestParams();
		params.put("id", id);

		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.delete(this, Prefrences.url + "/comments/" + id,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
						} catch (Exception e) {
							e.printStackTrace();
						}
						// WorkListCompleteAdapter.punchlst_item(id_punchlist_item,
						// WorkListCompleteAdapter.data, pos);
						// Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Toast.makeText(getApplicationContext(), "Server Issue",
								Toast.LENGTH_LONG).show();

						Log.e("request fail", arg0.toString());
						// Prefrences.dismissLoadingDialog();
					}
				});

	}

	// ************ API for Create Worklist. *************//
	
	public void create_worklist(String body, String user_id, String location,
			String user_assignee, String project_id) {
		/*
		 * Required Parameters Òpunchlist_itemÓ : { ÒbodyÓ : ÒRemove moldÓ,
		 * Òuser_idÓ : 2, ÒlocationÓ : ÒBasementÓ, Òuser_assigneeÓ : ÒMax
		 * Haines-StilesÓ }, Òproject_idÓ : {project.id}
		 */
		Prefrences.showLoadingDialog(con, "Loading...");
		JSONObject jsonobj = new JSONObject();
		JSONObject jomain = new JSONObject();
		try {
			jsonobj.put("body", body);
			String assigneeId = "";
			jsonobj.put("user_id", user_id);
			jsonobj.put("location", location);
			for (int j = 0; j < ProjectsAdapter.user2ArrayList.size(); j++) {
				if (ProjectsAdapter.user2ArrayList.get(j).uFullName.toString()
						.equalsIgnoreCase(Prefrences.assignee_str.toString())) {
					assigneeId = ProjectsAdapter.user2ArrayList.get(j).uId.toString();
				}
				
			}
			if(assigneeId.equals(""))
				jsonobj.put("connect_assignee_id", Prefrences.assigneeID);
			else
				jsonobj.put("assignee_id",assigneeId);
			Log.d("assignee", "Assignee Id" + Prefrences.assigneeID
					+ "Prefrences.assigneename.toString()"
					+ Prefrences.assignee_str.toString());
			

			jomain.put("worklist_item", jsonobj);
			jomain.put("project_id", project_id);
			ByteArrayEntity entity = new ByteArrayEntity(jomain.toString()
					.getBytes("UTF-8"));

			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			client.post(con, Prefrences.url + "/worklist_items/", entity,
					"application/json", new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {

							JSONObject res = null;

							/*
							 * {"punchlist_item":{"photos":[],"id":153,"body":
							 * "Remove mold","epoch_time":1399707530,"
							 * completed_at
							 * ":null,"location":"Basement","created_at
							 * ":"2014-05
							 * -10T00:38:50.939-07:00","assignee":null,"
							 * comments":[],
							 * "completed":false,"sub_assignee":null}}
							 */
							try {
								res = new JSONObject(response);
//								Prefrences.dismissLoadingDialog();
								Log.d("create worklist response",
										res.toString(2));
								JSONObject punchlist_item_obj = null;

								// if (res.has("punchlist_item")) {
								// punchlist_item_obj =
								// res.getJSONObject("punchlist_item");//
								// //checklist
								//
								// } else {
								punchlist_item_obj = res
										.getJSONObject("worklist_item");

								// }

								String id = punchlist_item_obj.getString("id");
								Log.i("punchlist_item_obj", id);
								id_punchlist_item = id;

								if (!picturePath.equals("")) {
//								if(ProjectDetail.flag==1){
									Log.d("if","if"+picturePath);
									new LongOperation().execute("");
								} else {
									Log.d("else","else");
									Prefrences.dismissLoadingDialog();
								}
								btn_save.setText("Save");
								// String body = punchlist_item_obj
								// .getString("body");
								// String epoch_time = punchlist_item_obj
								// .getString("epoch_time");
								// String completed_at = punchlist_item_obj
								// .getString("completed_at");
								// String location = punchlist_item_obj
								// .getString("location");
								// String created_at = punchlist_item_obj
								// .getString("created_at");
								// String assignee = punchlist_item_obj
								// .getString("assignee");
								// String completed = punchlist_item_obj
								// .getString("completed");
								// String sub_assignee = punchlist_item_obj
								// .getString("sub_assignee");

								// Log.d("id", "" + id);
								// Log.d("body", "" + body);
								// Log.d("epoch_time", "" + epoch_time);
								// Log.d("completed_at", "" + completed_at);
								// Log.d("location", "" + location);
								// Log.d("created_at", "" + created_at);
								// Log.d("assignee", "" + assignee);
								// Log.d("completed", "" + completed);
								// Log.d("sub_assignee", "" + sub_assignee);
								//
								// JSONArray photos = punchlist_item_obj
								// .getJSONArray("photos");
								// JSONArray comments = punchlist_item_obj
								// .getJSONArray("comments");
								// for (int j = 0; j < photos.length(); j++) {
								// JSONObject photosobj = photos
								// .getJSONObject(j);
								// String photoid = photosobj.getString("id");
								// String url_large = photosobj
								// .getString("url_large");
								// String original = photosobj
								// .getString("original");
								// String url200 = photosobj
								// .getString("url200");
								// String url100 = photosobj
								// .getString("url100");
								// String photoepoch_time = photosobj
								// .getString("epoch_time");
								// String url_small = photosobj
								// .getString("url_small");
								// String url_thumb = photosobj
								// .getString("url_thumb");
								// String image_file_size = photosobj
								// .getString("image_file_size");
								// String image_content_type = photosobj
								// .getString("image_content_type");
								// String source = photosobj
								// .getString("source");
								// String phase = photosobj.getString("phase");
								// String photocreated_at = photosobj
								// .getString("created_at");
								// String user_name = photosobj
								// .getString("user_name");
								// String name = photosobj.getString("name");
								// String created_date = photosobj
								// .getString("created_date");
								// String photoassignee = photosobj
								// .getString("assignee");
								// Log.d("photosdata ",
								// "----------------photosdata start----------------------");
								// Log.d("photoid", ":" + photoid);
								// Log.d("url_large", ":" + url_large);
								// Log.d("original", ":" + original);
								// Log.d("url200", ":" + url200);
								// Log.d("url100", ":" + url100);
								// Log.d("photoepoch_time", ":"
								// + photoepoch_time);
								// Log.d("url_small", ":" + url_small);
								// Log.d("url_thumb", ":" + url_thumb);
								// Log.d("image_file_size", ":"
								// + image_file_size);
								// Log.d("image_content_type", ":"
								// + image_content_type);
								// Log.d("source", ":" + source);
								// Log.d("phase", ":" + phase);
								// Log.d("photocreated_at", ":"
								// + photocreated_at);
								// Log.d("user_name", ":" + user_name);
								// Log.d("name", ":" + name);
								// Log.d("created_date", ":" + created_date);
								// Log.d("photoassignee", ":" + photoassignee);
								// Log.d("photosdata ",
								// "--------------photosdata end----------------------");
								// }
								// for (int j = 0; j < comments.length(); j++) {
								// JSONObject commentsobj = comments
								// .getJSONObject(j);
								// String commentsid = commentsobj
								// .getString("id");
								// String commentsbody = commentsobj
								// .getString("body");
								// String comments_created_at = commentsobj
								// .getString("created_at");
								// Log.d("comments data ",
								// "---------------comments data start-------------------");
								// Log.d("commentsid", ":" + commentsid);
								// Log.d("commentsbody", ":" + commentsbody);
								// Log.d("comments_created_at", ":"
								// + comments_created_at);
								//
								// JSONObject userobj = commentsobj
								// .getJSONObject("user");
								// String userid = userobj.getString("id");
								// String first_name = userobj
								// .getString("first_name");
								// String last_name = userobj
								// .getString("last_name");
								// String full_name = userobj
								// .getString("full_name");
								// String admin = userobj.getString("admin");
								// String company_admin = userobj
								// .getString("company_admin");
								// String uber_admin = userobj
								// .getString("uber_admin");
								// String email = userobj.getString("email");
								// String phone_number = userobj
								// .getString("phone_number");
								// String authentication_token = userobj
								// .getString("authentication_token");
								// String url200 = userobj.getString("url200");
								// String url_thumb = userobj
								// .getString("url_thumb");
								// String url_small = userobj
								// .getString("url_small");
								// Log.d("user data in comments",
								// "----------user data in comments start-------");
								// Log.d("userid", ":" + userid);
								// Log.d("first_name", ":" + first_name);
								// Log.d("last_name", ":" + last_name);
								// Log.d("full_name", ":" + full_name);
								// Log.d("admin", ":" + admin);
								// Log.d("company_admin", ":" + company_admin);
								// Log.d("uber_admin", ":" + uber_admin);
								// Log.d("email", ":" + email);
								// Log.d("phone_number", ":" + phone_number);
								// Log.d("authentication_token", ":"
								// + authentication_token);
								// Log.d("url200", ":" + url200);
								// Log.d("url_thumb", ":" + url_thumb);
								// Log.d("url_small", ":" + url_small);
								// Log.d("userdata in comments ",
								// "-------------user data in comments end-------------------------");
								// Log.d("comments data ",
								// "--------------comments data end-------------------------");
								//
								// }
								ProjectDetail.flag = 0;
								Prefrences.worklist_bool=false;
								finish();
								
							}

							catch (Exception e) {
								Prefrences.dismissLoadingDialog();
								e.printStackTrace();

							}
						}

						@Override
						public void onFailure(Throwable arg0) {
							Log.e("request fail", arg0.toString());
							Toast.makeText(getApplicationContext(),
									"Server Issue", Toast.LENGTH_LONG).show();

							Prefrences.dismissLoadingDialog();
						}

					});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// ************ API for update worklist. *************//
	
	void updateWorkListHit(Activity con) {

		Prefrences.showLoadingDialog(con, "Loading...");
		JSONObject jsonobj = new JSONObject();
//		assignee_id = "";
		RequestParams params = new RequestParams();
		Log.i("id", id_punchlist_item);
		Log.i("body", txt_body.getText().toString());
		Log.i("location",Prefrences.location_str); //btnS_location.getText().toString());
		Log.i("user_id", Prefrences.userId);
		Log.i("status", "" + Prefrences.statusCompleted);
		Log.i("completed_by_user_id", Prefrences.userId);
		Log.i("user_assignee", "" + Prefrences.assignee_str);//btnS_assigned.getText().toString());

		params.put("worklist_item[mobile]", "1");
		params.put("worklist_item[body]", txt_body.getText().toString());
		
		params.put("worklist_item[location]", Prefrences.location_str);//btnS_location.getText().toString());
		params.put("worklist_item[user_id]", Prefrences.userId);
		params.put("worklist_item[completed]", ""+Prefrences.statusCompleted); //0 incomplete, 1 complete.
		params.put("worklist_item[completed_by_user_id]", Prefrences.userId);
		
		String assignneID="";
		for (int i = 0; i < ProjectsAdapter.user2ArrayList.size(); i++) {
			Log.d("", "user2.get(i).uFullName.toString()==== " + ProjectsAdapter.user2ArrayList.get(i).uFullName.toString());
			if (ProjectsAdapter.user2ArrayList.get(i).uId.toString().equalsIgnoreCase(Prefrences.assigneeID)) {
				assignneID = ProjectsAdapter.user2ArrayList.get(i).uId.toString();				
			}			
		}
		if(assignneID.equals(""))
			params.put("worklist_item[connect_assignee_id]", Prefrences.assigneeID);
		else
			params.put("worklist_item[assignee_id]", assignneID);
		
		Log.d("-----status-----","----status-----"+Prefrences.statusCompleted);
		Log.d("-----assignee_str-----","----assignee_str-----"+Prefrences.assignee_str);
		Log.d("-----assigneeID-----","----assigneeID-----"+Prefrences.assigneeID);
		Log.d("-----location_str-----","----location_str-----"+Prefrences.statusCompleted);
		Log.d("", "assignee id ==== " + assignneID);
		
		if(Prefrences.worklisttypes==0 || Prefrences.worklisttypes==2 || Prefrences.worklisttypes==3)
		{
		for(int i=0; i < WorklistFragment.punchListItem.size();i++){
			if(WorklistFragment.punchListItem.get(i).id.toString().equals(id_punchlist_item)){
				
				Log.d("hellloooooo","yoooo");
				WorklistFragment.punchListItem.get(i).body=txt_body.getText().toString();
				WorklistFragment.punchListItem.get(i).location=Prefrences.location_str;//btnS_location.getText().toString();
				WorklistFragment.punchListItem.get(i).assignee.get(0).fullName=Prefrences.assignee_str;//btnS_assigned.getText().toString();
				WorklistFragment.punchListItem.get(i).assignee.get(0).id=Prefrences.assigneeID.toString();
				if(Prefrences.statusCompleted==1)
				{
					WorklistFragment.punchListItem.get(i).completed="true";
					WorklistFragment.worklist_complted.add(0, new PunchListsItem(id_punchlist_item, txt_body.getText().toString(),WorklistFragment.assigne ,
							Prefrences.location_str// btnS_location.getText().toString()
							, "",
							"true", punchlistphoto2, "", commnt2, ""));
					for(int j=0;j<WorklistFragment.worklist_Active.size();j++)
					{
						if(WorklistFragment.worklist_Active.get(j).id.toString().equals(id_punchlist_item))
							WorklistFragment.worklist_Active.remove(j);
					}
				}
				else if(Prefrences.statusCompleted==0)
				{
				WorklistFragment.punchListItem.get(i).completed="false";
				WorklistFragment.worklist_Active.add(0, new PunchListsItem(id_punchlist_item, txt_body.getText().toString(),WorklistFragment.assigne ,
						Prefrences.location_str//btnS_location.getText().toString()
						,"","false", punchlistphoto2, "", commnt2, ""));
				
				for(int j=0;j<WorklistFragment.worklist_complted.size();j++)
				{
					if(WorklistFragment.worklist_complted.get(j).id.toString().equals(id_punchlist_item))
						WorklistFragment.worklist_complted.remove(j);
				}
			}
				}
				
		}
		}
		else if (Prefrences.worklisttypes == 1) {
			for (int i = 0; i < WorklistFragment.worklist_Active.size(); i++) {
				if (WorklistFragment.worklist_Active.get(i).id.toString()
						.equals(id_punchlist_item)) {

					Log.d("hellloooooo", "yoooo");
					WorklistFragment.worklist_Active.get(i).body = txt_body
							.getText().toString();
					WorklistFragment.worklist_Active.get(i).location = Prefrences.location_str;// btnS_location.getText().toString();
					WorklistFragment.worklist_Active.get(i).assignee.get(0).fullName = Prefrences.assignee_str;// btnS_assigned.getText().toString();
					WorklistFragment.worklist_Active.get(i).assignee.get(0).id = Prefrences.assigneeID
							.toString();
					if (Prefrences.statusCompleted == 1) {
						WorklistFragment.worklist_Active.get(i).completed = "true";

						WorklistFragment.worklist_complted.add(0,
								new PunchListsItem(id_punchlist_item,
										txt_body.getText().toString(),
										WorklistFragment.assigne,
										Prefrences.assignee_str// btnS_location.getText().toString()
										, "", "true", punchlistphoto2, "",
										commnt2, ""));
						WorklistFragment.worklist_Active.remove(i);

					} else
						WorklistFragment.worklist_Active.get(i).completed = "false";

				}

			}
		}
		else if(Prefrences.worklisttypes==4){
		for(int i=0; i < WorklistFragment.worklist_complted.size();i++){
			if(WorklistFragment.worklist_complted.get(i).id.toString().equals(id_punchlist_item)){
				Log.d("hellloooooo","yoooo");
				WorklistFragment.worklist_complted.get(i).body=txt_body.getText().toString();
				WorklistFragment.worklist_complted.get(i).location=Prefrences.location_str;//btnS_location.getText().toString();
				WorklistFragment.worklist_complted.get(i).assignee.get(0).fullName=Prefrences.assignee_str;//btnS_assigned.getText().toString();
				WorklistFragment.worklist_complted.get(i).assignee.get(0).id=Prefrences.assigneeID.toString();
				if(Prefrences.statusCompleted==1){
					WorklistFragment.worklist_complted.get(i).completed="true";
					
				}
				else{
					WorklistFragment.worklist_complted.get(i).completed="false";
					WorklistFragment.worklist_Active.add(0, new PunchListsItem(id_punchlist_item, txt_body.getText().toString(),
							WorklistFragment.assigne , 
							Prefrences.location_str//btnS_location.getText().toString()
							, "",	"false", punchlistphoto2, "", commnt2, ""));
					
					WorklistFragment.worklist_complted.remove(i);
				}
			}
				
		}
		}
		
		

		// params.put();

		try {

//			jsonobj.put("worklist_item[mobile]", 1);// id_punchlist_item);
//
//			jsonobj.put("worklist_item[body]", txt_body.getText().toString()
//					+ "Eshant");
//			jsonobj.put("worklist_item[location]", Prefrences.location_str);//btnS_location.getText().toString());
//			jsonobj.put("worklist_item[user_id]", Prefrences.userId);
//			jsonobj.put("worklist_item[status]", "" + Prefrences.statusCompleted);
//			jsonobj.put("worklist_item[completed_by_user_id]",
//					Prefrences.userId);
//			for (int i = 0; i < ProjectsAdapter.user2.size(); i++) {
//				if (ProjectsAdapter.user2.get(i).uFullName.toString()
//						.equalsIgnoreCase(Prefrences.assignee_str//btnS_assigned.getText().toString()
//								)) {
//					Prefrences.assigneeID = ProjectsAdapter.user2.get(i).uId.toString();
//				}
//				else{
//					Prefrences.assigneeID=Prefrences.assigneeID;
//				}
//			}
//			Log.d("", "assignee id ==== " + Prefrences.assigneeID);
//			jsonobj.put("worklist_item[assignee_id]", Prefrences.assigneeID);
//
//			JSONObject finalJson = new JSONObject();
//			finalJson.put("report", jsonobj);
//
//			ByteArrayEntity entity = new ByteArrayEntity(finalJson.toString()
//					.getBytes("UTF-8"));

			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			client.put(con, Prefrences.url + "/worklist_items/"
					+ id_punchlist_item, params, // "application/json",
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							JSONObject res = null;
							try {
								res = new JSONObject(response);
								startservice();
								Log.v("response ---- ",
										"---*****----" + res.toString(2));
							} catch (Exception e) {
								e.printStackTrace();
							}
							// WorkListCompleteAdapter.punchlst_item(id_punchlist_item,
							// WorkListCompleteAdapter.data, pos);
							Prefrences.assigneeID="";
							Prefrences.assignee_str="";
							Prefrences.worklist_bool=false;
							Prefrences.dismissLoadingDialog();
							finish();
						}

						@Override
						public void onFailure(Throwable arg0) {
							Log.e("request fail", arg0.toString());
							Toast.makeText(getApplicationContext(),
									"Server Issue", Toast.LENGTH_LONG).show();

							Prefrences.dismissLoadingDialog();
						}
					});

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			finish();
		}
	}

	private class LongOperation extends AsyncTask<String, Void, String> {
boolean error;
		@Override
		protected String doInBackground(String... params) {

			try {

				postPicture();

			} catch (Exception e) {

				// TODO Auto-generated catch block
				error=true;
				e.printStackTrace();

			}
//			catch (IOException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			} catch (XmlPullParserException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			Prefrences.dismissLoadingDialog();
			if(error)
			{
				AlertMessage();
			}
//			finish();
		}

		@Override
		protected void onPreExecute() {
			 if(ProjectDetail.flag==0)
			Prefrences.showLoadingDialog(con, "Uploading...");

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}
	private void AlertMessage() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Failed")

		.setMessage(

		"Fail to load Image")

		.setCancelable(false)

		.setPositiveButton("OK",

		new DialogInterface.OnClickListener() {

			public void onClick(final DialogInterface dialog,

			int intValue) {

				finish();

				// setting_page = true;

			}

		});

		final AlertDialog alert = builder.create();

		alert.show();

	}
	
	// ************ API for Post picture. *************//
	
	public void postPicture() throws ParseException, IOException,

	XmlPullParserException {

		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Prefrences.url

		+ "/worklist_items/photo/");

		// MultipartEntity mpEntity = new MultipartEntity();

		String BOUNDARY = "--buildhawk--";

		MultipartEntity mpEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY,
				Charset.defaultCharset());

		httppost.addHeader("Accept-Encoding", "gzip, deflate");

		// httppost.setHeader("Accept", "image/jpg");

		httppost.setHeader("Accept", "application/json");

		httppost.setHeader("Content-Type", "multipart/form-data; boundary="

		+ BOUNDARY);

		Log.v("picturePath", picturePath);

		File file = new File(picturePath);

		FileBody cbFile = new FileBody(file, "image/jpg");

		cbFile.getMediaType();

		mpEntity.addPart("photo[worklist_item_id]", new StringBody(
				id_punchlist_item));

		mpEntity.addPart("photo[mobile]", new StringBody("1"));

		mpEntity.addPart("photo[company_id]", new StringBody(
				Prefrences.companyId));

		mpEntity.addPart("photo[source]", new StringBody("Worklist"));

		mpEntity.addPart("photo[user_id]", new StringBody(Prefrences.userId));

		mpEntity.addPart("photo[project_id]", new StringBody(
				Prefrences.selectedProId));

		mpEntity.addPart("photo[name]", new StringBody("android"));

		mpEntity.addPart("photo[image]", cbFile);

		Log.i("photo[worklist_item_id]", id_punchlist_item);
		Log.i("photo[mobile]", "1");
		Log.i("photo[company_id]", "8");
		Log.i("photo[source]", "Worklist");
		Log.i("photo[user_id]", Prefrences.userId);
		Log.i("photo[project_id]", Prefrences.selectedProId);
		Log.i("photo[name]", "android");
		Log.i("photo[image]", "" + cbFile);

		httppost.setEntity(mpEntity);

		HttpResponse response = httpclient.execute(httppost);

		Log.v("response", response.getStatusLine().toString() + "");

		Log.v("res", "," + response);

		HttpEntity entity = response.getEntity();

		String responseString = EntityUtils.toString(entity, "UTF-8");

		Log.v("res", "," + responseString);

		System.out.println(responseString);

	}

//	private class LongOperation1 extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			try {
//
//				postPicture1();
//
//			} catch (ParseException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			} catch (IOException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			} catch (XmlPullParserException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			}
//
//			return null;
//
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//
//		}
//
//		@Override
//		protected void onPreExecute() {
//
//		}
//
//		@Override
//		protected void onProgressUpdate(Void... values) {
//
//		}
//
//	}
//
	// jo.put("worklist_item[body]", body.getText().toString() + "Eshant");
	// jo.put("worklist_item[location]", btnS_location.getText().toString());
	// jo.put("worklist_item[user_id]", Prefrences.userId);
	// jo.put("worklist_item[status]", "" + status);
	// jo.put("worklist_item[completed_by_user_id]", Prefrences.userId);
	// jo.put("worklist_item[assignee_id]", "2");
//
//	public void postPicture1() throws ParseException, IOException,
//
//	XmlPullParserException {
//
//		HttpClient httpclient = new DefaultHttpClient();
//
//		HttpPost httppost = new HttpPost(Prefrences.url + "/worklist_items/"
//				+ id_punchlist_item);
//
//		// MultipartEntity mpEntity = new MultipartEntity();
//
//		String BOUNDARY = "--buildhawk--";
//
//		MultipartEntity mpEntity = new MultipartEntity(
//				HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY,
//				Charset.defaultCharset());
//
//		httppost.addHeader("Accept-Encoding", "gzip, deflate");
//
//		// httppost.setHeader("Accept", "image/jpg");
//
//		httppost.setHeader("Accept", "application/json");
//
//		httppost.setHeader("Content-Type", "multipart/form-data; boundary="
//
//		+ BOUNDARY);
//
//		// Log.v("picturePath", picturePath);
//
//		// File file = new File(picturePath);
//
//		// FileBody cbFile = new FileBody(file, "image/jpg");
//
//		// cbFile.getMediaType();
//
//		mpEntity.addPart("worklist_item[user_id]", new StringBody(
//				Prefrences.userId));
//
//		mpEntity.addPart("worklist_item[mobile]", new StringBody("1"));
//
//		mpEntity.addPart("worklist_item[body]", new StringBody(txt_body.getText()
//				.toString() + "Eshant"));
//
//		mpEntity.addPart("worklist_item[location]", new StringBody(Prefrences.location_str
////				btnS_location.getText().toString()
//				));
//
//		mpEntity.addPart("worklist_item[status]", new StringBody("" + Prefrences.statusCompleted));
//
//		mpEntity.addPart("worklist_item[completed_by_user_id]", new StringBody(
//				Prefrences.userId));
//
//		mpEntity.addPart("worklist_item[assignee_id]", new StringBody("2"));
//
//		// mpEntity.addPart("photo[image]", cbFile);
//
//		// Log.i("photo[worklist_item_id]", id_punchlist_item);
//		// Log.i("photo[mobile]", "1");
//		// Log.i("photo[company_id]", "8");
//		// Log.i("photo[source]", "Worklist");
//		// Log.i("photo[user_id]", Prefrences.userId);
//		// Log.i("photo[project_id]", Prefrences.selectedProId);
//		// Log.i("photo[name]", "android");
//		// Log.i("photo[image]", "" + cbFile);
//
//		httppost.setEntity(mpEntity);
//
//		HttpResponse response = httpclient.execute(httppost);
//
//		Log.v("response", response.getStatusLine().toString() + "");
//
//		Log.v("res", "," + response);
//
//		HttpEntity entity = response.getEntity();
//
//		String responseString = EntityUtils.toString(entity, "UTF-8");
//
//		Log.v("res", "," + responseString);
//
//		System.out.println(responseString);
//
//	}
	
	
	// ************ API for setting Reminder. *************//
	
	public static void ReminderSet(long epoch) {
		Prefrences.showLoadingDialog(con, "Loading...");
//		JSONObject json = new JSONObject();
		RequestParams params = new RequestParams();
		
		params.put("reminder[worklist_item_id]",id_punchlist_item );
		params.put("reminder[user_id]", Prefrences.userId);
		params.put("reminder[project_id]",Prefrences.selectedProId );
		params.put("date",""+epoch);

		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		
		client.post(con, Prefrences.url + "/reminders", params,
				 new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
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
	}
	
	public void startservice(){
		Log.d("in service","in service");
		startService(new Intent(this, MyService.class));
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String utcToLocal(String utcTime) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy, hh:mm a");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			Date myDate = simpleDateFormat.parse(utcTime);
			String localDate = sdf.format(myDate);
			Log.d("date ", "hiii date " + localDate.toString());
			return localDate;
		} catch (Exception e1) {
			Log.e("e1", "=" + e1);
			return utcTime;
		}
	}
	
	
	// ************ API for Get Worklist Item . *************//

	public void punchlst_item(final String punchlist_id) {

		Prefrences.showLoadingDialog(con, "Loading...");
		AsyncHttpClient client = new AsyncHttpClient();

		Log.d("----------------","---------------"+Prefrences.url + "/worklist_items/" + punchlist_id);
		
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		client.get(Prefrences.url + "/worklist_items/" + punchlist_id,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						DatabaseClass dbObject = new DatabaseClass(con);
						dbObject.open();
						if(dbObject.exists_workitem(Prefrences.selectedProId, punchlist_id)){
							dbObject.update_workitem(Prefrences.selectedProId, punchlist_id, response);
						}else{
							dbObject.CreateEntry_workitem(Prefrences.selectedProId, punchlist_id, response);
						}
						dbObject.close();
						fillServerData(response,punchlist_id);
						Prefrences.dismissLoadingDialog();

					}

					@Override
					public void onFailure(Throwable arg0) {

						Log.e("request fail", arg0.toString());
						Toast.makeText(con, "Server Issue",
								Toast.LENGTH_LONG).show();

						Prefrences.dismissLoadingDialog();

					}

				});

	}
	
	public void fillServerData(String response, String item_id){
		JSONObject res = null;
		try {
			
			
			res = new JSONObject(response);
			Log.v("response ", "" + res.toString(2));
			punchListItem2.clear();
			JSONObject count = null;

			// if (res.has("punchlist_item")) {
			// count = res.getJSONObject("punchlist_item");//
			// chexklist
			//
			// } else {
			count = res.getJSONObject("worklist_item");

			// }

			if (!count.isNull("assignee")) {
				JSONObject asigne = count
						.getJSONObject("assignee");// subcateg
				Log.i("asigneees", "" + asigne);

				assigne2 = new ArrayList<Assignee>();

				JSONObject company = asigne
						.getJSONObject("company");// checkitem

				ArrayList<Company> compny = new ArrayList<Company>();

				compny.add(new Company(company.getString("id"),
						company.getString("name")));
				assigne2.add(new Assignee(asigne
						.getString("id"), asigne

				.getString("first_name"), asigne

				.getString("last_name"), asigne

				.getString("full_name"), asigne

//				.getString("admin"), asigne

//				.getString("company_admin"), asigne

//				.getString("uber_admin"), asigne

				.getString("email"), asigne

				.getString("formatted_phone"), asigne

//				.getString("authentication_token"),

//				asigne
				.getString("url_thumb"),

				asigne.getString("url_small"),

				compny));

			}
			
			if (!count.isNull("connect_assignee")) {
				JSONObject connect_asigne = count
						.getJSONObject("connect_assignee");// subcateg
				Log.i("asigneees", "" + connect_asigne);

				assigne2 = new ArrayList<Assignee>();

				JSONObject company = connect_asigne
						.getJSONObject("company");// checkitem

				ArrayList<Company> compny = new ArrayList<Company>();

				compny.add(new Company(company.getString("id"),
						company.getString("name")));
				assigne2.add(new Assignee(connect_asigne
						.getString("id"), connect_asigne

				.getString("first_name"), connect_asigne

				.getString("last_name"), connect_asigne

				.getString("full_name"), connect_asigne

//				.getString("admin"), asigne

//				.getString("company_admin"), asigne

//				.getString("uber_admin"), asigne

				.getString("email"), connect_asigne

				.getString("phone"),"","",
//				connect_asigne.getString("url_thumb"),

//				.getString("authentication_token"),

//				asigne
				

				//connect_asigne.getString("url_small"),

				compny));

			}

			punchlistphoto2 = new ArrayList<PunchListsPhotos>();

			// commnt2 = new ArrayList<Comments>();
			// commnt2.clear();
			// punchlistphoto2.clear();
			// assigne2.clear();

			JSONArray phot = count.getJSONArray("photos");

			for (int j = 0; j < phot.length(); j++) {

				JSONObject ccount = phot.getJSONObject(j);

				// Log.d("photos data","---------------");
				//
				// Log.d("id", ccount.getString("id"));
				//
				// Log.d("url_large",
				// ccount.getString("url_large"));
				//
				// Log.d("original",
				// ccount.getString("original"));
				//
				// Log.d("url_small",
				// ccount.getString("url_small"));
				//
				// Log.d("url_thumb",
				// ccount.getString("url_thumb"));
				//
				// Log.d("image_file_size",
				// ccount.getString("image_file_size"));
				//
				// Log.d("image_content_type",
				// ccount.getString("image_content_type"));
				//
				// Log.d("source", ccount.getString("source"));
				//
				// Log.d("phase", ccount.getString("phase"));
				//
				// Log.d("created_at",
				// ccount.getString("created_at"));
				//
				// Log.d("user_name",
				// ccount.getString("user_name"));
				//
				// Log.d("created_date",
				// ccount.getString("created_date"));
				//
				// Log.d("assignee",
				// ccount.getString("assignee"));

				punchlistphoto2.add(new PunchListsPhotos(

				ccount.getString("id"),

				ccount.getString("url_large"),

				ccount.getString("original"),

				ccount.getString("url_small"),

				ccount.getString("url_thumb"),

				ccount.getString("image_file_size"),

				ccount.getString("image_content_type"),

				ccount.getString("source"), ccount

				.getString("phase"), ccount

				.getString("created_at"),

				ccount.getString("user_name"),

				ccount.getString("name"), ccount

				.getString("description"), ccount

				.getString("created_date"),
				// ccount.getString("assignee"),
						ccount.getString("epoch_time")));

				Log.i("photos", "" + phot);

			}

			JSONArray comm = count.getJSONArray("comments");

			commnt2 = new ArrayList<Comments>();

			for (int k = 0; k < comm.length(); k++) {

				JSONObject ccount = comm.getJSONObject(k);

				JSONObject cuser = ccount

				.getJSONObject("user");

				Log.i("comment user", "" + cuser);

				cusr2 = new ArrayList<CommentUser>();

				JSONObject company = cuser

				.getJSONObject("company");

				ArrayList<Company> compny = new ArrayList<Company>();

				compny.add(new Company(company

				.getString("id"), company

				.getString("name")));

				// Log.i("ddddddd", "" + company);

				cusr2.add(new CommentUser(cuser

				.getString("id"), cuser

				.getString("first_name"), cuser

				.getString("last_name"), cuser

				.getString("full_name"), cuser

//				.getString("admin"), cuser
//
//				.getString("company_admin"), cuser
//
//				.getString("uber_admin"), cuser

				.getString("email"), cuser

				.getString("formatted_phone"), cuser

//				.getString("authentication_token"),
//
//				cuser
				.getString("url_thumb"), cuser

				.getString("url_small"),

				compny));

				commnt2.add(new Comments(ccount

				.getString("id"), ccount

				.getString("body"), cusr2, ccount

				.getString("created_at")));

				Log.d("comments in loop ",
						"size==" + commnt2.size());

			}

			punchListItem2.add(new PunchListsItem(count

			.getString("id"), count

			.getString("body"), assigne2, count

			// .getString("sub_assignee"), count

					.getString("location"), count

			.getString("completed_at"), count

			.getString("completed"),

			punchlistphoto2, count.getString("created_at"),

			commnt2, count.getString("epoch_time")));

			// Log.d("comment end ","size=="+commnt2.size());

			// if (punchListItem.get(i).completed

			// .equals("true")) {

			// worklist_complted.add(new PunchListsItem(

			// count.getString("id"), count

			// .getString("body"),

			// assigne, count

			// .getString("sub_assignee"),

			// count.getString("location"), count

			// .getString("completed_at"),

			// count.getString("completed"),

			// punchlistphoto, count

			// .getString("created_at"),

			// commnt,count.getString("epoch_time")));

			// Log.i("true",

			// "--------Completed-------------"

			// + punchListItem.get(i).body

			// .toString());

			// } else if (!punchListItem.get(i).completed

			// .equals("true")) {

			// worklist_Active.add(new PunchListsItem(

			// count.getString("id"), count

			// .getString("body"),

			// assigne, count

			// .getString("sub_assignee"),

			// count.getString("location"), count

			// .getString("completed_at"),

			// count.getString("completed"),

			// punchlistphoto, count

			// .getString("created_at"),

			// commnt,count.getString("epoch_time")));

			//

			// }

			// Log.d("punchlist_item",
			//
			// "Size" + punchListItem.size());
			//
			//
			// Log.d("punchlist_item", "Size" +
			// personnel.size());
			//
			//
			//
			// punchList.add(new PunchList(punchListItem,
			//
			// personnel));
			//
			// Log.d("punchlist", "--------" + personnel);

			// totalCount = Integer.toString(punchList.size());

			// Log.i("value..",""+punchList.size());

//			Intent intent = new Intent(con,
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
//			con.startActivity(intent);
			
			setphotos();
			
			
				btn_save.setText("Save");
				if(count.getString("completed").equals("true")){
					btn_markcomplete.setText("Completed");
					btn_markcomplete.setBackgroundResource(drawable.completecolor);
					btn_markcomplete.setTextColor(Color.WHITE);
					Prefrences.statusCompleted = 1;
				}
				else {
					btn_markcomplete.setText("Mark Complete");
					btn_markcomplete.setBackgroundResource(drawable.markcomplete);
					btn_markcomplete.setTextColor(Color.BLACK);
					Prefrences.statusCompleted = 0;

				}
				Log.d("worklist", "id of worklist " + id_punchlist_item);
//				pos = bundle.getInt("pos");
				if (!count.getString("location").equals("null")) {
					Prefrences.location_str=count.getString("location");
					btnS_location.setText("Location: " + Prefrences.location_str);
				}
				if (!count.isNull("assignee")){
				if (!count.getJSONObject("assignee").getString("full_name").equals("null")) {
					Prefrences.assignee_str = count.getJSONObject("assignee").getString("full_name");
					btnS_assigned.setText("Assigned: " + Prefrences.assignee_str);
					Prefrences.assigneeID = count.getJSONObject("assignee").getString("id");
				}
				}
				if (!count.isNull("connect_assignee")){
					if (!count.getJSONObject("connect_assignee").getString("full_name").equals("null")) {
						Prefrences.assignee_str = count.getJSONObject("connect_assignee").getString("full_name");
						btnS_assigned.setText("Assigned: " + Prefrences.assignee_str);
						Prefrences.assigneeID = count.getJSONObject("connect_assignee").getString("id");
					}
					}

				// btn_assigned.setText("Assigned:"+bundle.getString("assigne"));
				txt_body.setText(count.getString("body"));
				Log.d("-----status-----","----status-----"+Prefrences.statusCompleted);
				Log.d("-----assignee_str-----","----assignee_str-----"+Prefrences.assignee_str);
				Log.d("-----assigneeID-----","----assigneeID-----"+Prefrences.assigneeID);
				Log.d("-----location_str-----","----location_str-----"+Prefrences.statusCompleted);
				comadap = new commentadapter();
				commentslist.setAdapter(comadap);
				setlist(commentslist, comadap, 230);
				loc.addAll(WorklistFragment.locs);
				ass.addAll(WorklistFragment.assignees);
				Log.d("loc", "sizeeee" + loc.size());
				Log.d("user", "sizeeee" + ass.size());
				LinkedHashSet<String> listToSet = new LinkedHashSet<String>(loc);
				LinkedHashSet<String> listToSet2 = new LinkedHashSet<String>(ass);
				locs = new ArrayList<String>(listToSet);
				asss = new ArrayList<String>(listToSet2);
				Log.d("locssss", "sizeeee" + locs.size());
				Log.d("usrs", "sizeeee" + asss.size());

				// save.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// create_worklist(body.getText().toString(),
				// Prefrences.userId, btnS_location.getText()
				// .toString(), btnS_assigned.getText()
				// .toString(), Prefrences.selectedProId);
				// }
				// });

			

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
	
	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}
	
	
}
