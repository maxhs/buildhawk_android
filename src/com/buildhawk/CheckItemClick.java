package com.buildhawk;
/*
 *  In this it shows the comments, photos, status and description of particular checklist item.
 * 
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
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
import android.app.Fragment;
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
import android.provider.MediaStore;
import android.provider.CalendarContract.Reminders;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import com.buildhawk.R.drawable;
import com.buildhawk.utils.Categories;
import com.buildhawk.utils.CheckItems;
import com.buildhawk.utils.CommentsChecklistItem;
import com.buildhawk.utils.CommentsUserChecklistItem;
import com.buildhawk.utils.DataCheckListItems;
import com.buildhawk.utils.DialogBox;
import com.buildhawk.utils.PhotosCheckListItem;
import com.buildhawk.utils.SubCategories;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

public class CheckItemClick extends Activity {

//	public static Fragment fragment;
	private int resultLoadImage = 1;
	private int takePicture = 1;
	private Uri imageUri;
	RelativeLayout relativeLayoutRoot;
	TextView textviewBody, textviewType;
	EditText edittextCommentbody;
	Button buttonSendcomment;
	CustomDateTimePicker custom;
	// public static ArrayList<PhotosCheckListItem>pho=new
	// ArrayList<PhotosCheckListItem>();
	
	public  ArrayList<CommentsChecklistItem> commentsArrayList = new ArrayList<CommentsChecklistItem>();
	public  ArrayList<PhotosCheckListItem> photosArrayList = new ArrayList<PhotosCheckListItem>();
	public  ArrayList<DataCheckListItems> checkListItemDataArrayList = new ArrayList<DataCheckListItems>();
	commentadapter comadap;
	
//	ArrayList<CommentsChecklistItem> comm = new ArrayList<CommentsChecklistItem>();
	Button buttonComplete, buttonProgress, buttonNotapp, buttonSave;
	int completed=0;
	String picturePathString;
	LinearLayout linearlayoutScrollable;
	int temp, temp2;
//	LinearLayout lin3;
	SwipeDetector swipeDetector;
	static String idCheckitemString;
	String statusCheckitemString="", catidString,subcatidString;
	ListView listviewComments;
	static Context con;
	static TextView textviewReminder;
	SwipeDetector swipeDetecter = new SwipeDetector();
	ScrollView scrollView;
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	ConnectionDetector connDect;
	Boolean isInternetPresentBoolean = false;

	// final Context context = this;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_item_click);

		relativeLayoutRoot = (RelativeLayout) findViewById(R.id.relativeLayoutRoot);
		new ASSL(this, relativeLayoutRoot, 1134, 720, false);
		con = CheckItemClick.this;
		connDect = new ConnectionDetector(con);
//		isInternetPresent = connDect.isConnectingToInternet();
		
		commentsArrayList.clear();
//		comments.addAll(comments);
		listviewComments = (ListView) findViewById(R.id.listviewComments);

		
		
		ImageView imageviewCamera = (ImageView) findViewById(R.id.imageviewCamera);
		ImageView imageviewReminder = (ImageView) findViewById(R.id.imageviewReminder);
		ImageView imageviewGallery = (ImageView) findViewById(R.id.imageviewGallery);
		ImageView imageviewCall = (ImageView) findViewById(R.id.imageviewCall);
		Button buttonBack = (Button) findViewById(R.id.buttonBack);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		ImageView imageviewEmail = (ImageView) findViewById(R.id.imageviewEmail);
		ImageView imageviewMessage = (ImageView) findViewById(R.id.imageviewMessage);
		edittextCommentbody = (EditText) findViewById(R.id.edittextCommentbody);
		buttonSendcomment = (Button) findViewById(R.id.buttonSendcomment);
		buttonComplete = (Button) findViewById(R.id.buttonComplete);
		buttonProgress = (Button) findViewById(R.id.buttonProgress);
		buttonNotapp = (Button) findViewById(R.id.buttonNotapp);
		textviewBody = (TextView) findViewById(R.id.textviewBody);
		textviewType = (TextView) findViewById(R.id.textviewType);
		textviewReminder= (TextView) findViewById(R.id.textviewReminder);

		textviewReminder.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		buttonComplete.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		textviewBody.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		textviewType.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		buttonProgress.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		buttonNotapp.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		edittextCommentbody.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		buttonSendcomment.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		buttonBack.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		buttonSave.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		scrollView = (ScrollView) findViewById(R.id.scrollView1_checkitem);
		scrollView.smoothScrollTo(0, 0);
		
		listviewComments.setFocusable(false);
		if (ConnectionDetector.isConnectingToInternet()) {
			if (Prefrences.checklistNotification.equals("")) {
				Bundle bundle = getIntent().getExtras();
				idCheckitemString = bundle.getString("id");
				catidString = bundle.getString("cat_id");
				subcatidString = bundle.getString("subcat_id");
				Log.d("ID ", "ID********* " + idCheckitemString);
				showComments(idCheckitemString);
			} else {
				Prefrences.comingFrom=4;
				idCheckitemString=Prefrences.checklistNotification;
				Log.d("ID ", "ID------- " + idCheckitemString);
				Prefrences.checklistNotification="";
				showComments(idCheckitemString);
			}
		} else {

			DatabaseClass dbObject = new DatabaseClass(con);
			dbObject.open();
			if (dbObject.exists_checkitem(Prefrences.selectedProId,
					idCheckitemString.toString())) {
				String response = dbObject.get_checkitem(
						Prefrences.selectedProId, idCheckitemString.toString());
				dbObject.close();
				 fillServerDataItem(response);

			} else {
				dbObject.close();
				Toast.makeText(getApplicationContext(), "No internet connection.",
						Toast.LENGTH_SHORT).show();
			}
		}
		
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
			        	

		                           textviewReminder.setText(monthShortName 
		                                    + " " + calendarSelected
                                            .get(Calendar.DAY_OF_MONTH) + " " + year
		                                    + " " + hour24 + ":" + min
		                                    + ":" + sec);
		                           String str = textviewReminder.getText().toString()+".454 UTC";
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
		                           reminderSet(epoch);
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


		
	

		

		

		buttonComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(statusCheckitemString.equalsIgnoreCase("1"))
				{
					completed = 0;
					statusCheckitemString = "";
					buttonComplete.setBackgroundResource(drawable.transparentclick);
					buttonComplete.setTextColor(Color.BLACK);
					buttonProgress.setBackgroundResource(drawable.transparentclick);
					buttonProgress.setTextColor(Color.BLACK);
					buttonNotapp.setBackgroundResource(drawable.transparentclick);
					buttonNotapp.setTextColor(Color.BLACK);
				}
				else
				{
				completed = 1;
				statusCheckitemString = "1";
				buttonComplete.setBackgroundColor(Color.BLACK);
				buttonComplete.setTextColor(Color.WHITE);
				buttonProgress.setBackgroundResource(drawable.transparentclick);
				buttonProgress.setTextColor(Color.BLACK);
				buttonNotapp.setBackgroundResource(drawable.transparentclick);
				buttonNotapp.setTextColor(Color.BLACK);
				}
			}
		});

		buttonProgress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(statusCheckitemString.equalsIgnoreCase("0"))
				{
					statusCheckitemString = "";
					buttonComplete.setBackgroundResource(drawable.transparentclick);
					buttonComplete.setTextColor(Color.BLACK);
					buttonProgress.setBackgroundResource(drawable.transparentclick);
					buttonProgress.setTextColor(Color.BLACK);
					buttonNotapp.setBackgroundResource(drawable.transparentclick);
					buttonNotapp.setTextColor(Color.BLACK);
				}
				else
				{
				statusCheckitemString = "0";
				buttonComplete.setBackgroundResource(drawable.transparentclick);
				buttonComplete.setTextColor(Color.BLACK);
				buttonProgress.setBackgroundColor(Color.BLACK);
				buttonProgress.setTextColor(Color.WHITE);
				buttonNotapp.setBackgroundResource(drawable.transparentclick);
				buttonNotapp.setTextColor(Color.BLACK);
				
				}
				completed = 0;
			}
		});

		buttonNotapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(statusCheckitemString.equalsIgnoreCase("-1"))
				{
					statusCheckitemString = "";
					buttonComplete.setBackgroundResource(drawable.transparentclick);
					buttonComplete.setTextColor(Color.BLACK);
					buttonProgress.setBackgroundResource(drawable.transparentclick);
					buttonProgress.setTextColor(Color.BLACK);
					buttonNotapp.setBackgroundResource(drawable.transparentclick);
					buttonNotapp.setTextColor(Color.BLACK);
				}
				else
				{
				statusCheckitemString = "-1";
				buttonComplete.setBackgroundResource(drawable.transparentclick);
				buttonComplete.setTextColor(Color.BLACK);
				buttonProgress.setBackgroundResource(drawable.transparentclick);
				buttonProgress.setTextColor(Color.BLACK);
				buttonNotapp.setBackgroundColor(Color.BLACK);
				buttonNotapp.setTextColor(Color.WHITE);
				}
				completed = 0;
			}
		});

		
		buttonSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				if(ConnectionDetector.isConnectingToInternet()){
					
				
				updateItem();
				}
				else
				{
					Toast.makeText(CheckItemClick.this,"No internet connection.", Toast.LENGTH_SHORT).show();
				}

			}
		});
		
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
			edittextCommentbody.setBackground(null);
		}
		else {
			edittextCommentbody.setBackgroundDrawable(null);
		}

		
		
		edittextCommentbody.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence charsequence, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				buttonSendcomment.setVisibility(View.GONE);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// progressList.setVisibility(View.VISIBLE);
				// Log.i("ARG", "-------" + arg0);
				if (arg0.toString().length() == 0) {
					buttonSendcomment.setVisibility(View.GONE);
				} else {
					buttonSendcomment.setVisibility(View.VISIBLE);
				}
				// ChecklistFragment.this.checkadapter.search2(arg0.toString());
			}
		});

		buttonSendcomment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
			if(ConnectionDetector.isConnectingToInternet()){
				commentAdd(edittextCommentbody.getText().toString());
				// commentslist.add
				commentsArrayList.add(new CommentsChecklistItem(idCheckitemString, edittextCommentbody
						.getText().toString(), null, "just now"));
				Log.d("*****", "increased size" + commentsArrayList.size());
				listviewComments.setAdapter(comadap);
				comadap.notifyDataSetChanged();
				setlist(listviewComments, comadap, 230);
				edittextCommentbody.setText("");
				buttonSendcomment.setVisibility(View.GONE);
			}else{
				Toast.makeText(CheckItemClick.this,"No internet connection.", Toast.LENGTH_SHORT).show();
			}
			}
		});

		

		// Log.d("checkitemclick","hullulullululu");
		// Log.d("","++++++++++++"+ChecklistFragment.comments.size);
		// lin3=(LinearLayout)findViewById(R.id.lin3);
		// EditText edt1 =null;
		// for(int j=0;j<ChecklistFragment.comments.size();j++)
		// {
		// edt1=new EditText(CheckItemClick.this);
		// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT,(int) (200));
		// lp.setMargins(10, 10, 10, 10);
		//
		// edt1.setLayoutParams(lp);
		// edt1.setText(""+ChecklistFragment.comments.get(j).body.toString());
		// edt1.setEnabled(false);
		// edt1.setBackgroundColor(Color.WHITE);
		// lin3.addView(edt1);
		// }

		buttonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if(Prefrences.selectedCheckitemSynopsis != 1 || Prefrences.comingFrom==4)
				{
				Intent intent = new Intent(CheckItemClick.this,ProjectDetail.class);
				startActivity(intent);
				}
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
			}
		});

		imageviewEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Prefrences.text = 1;
				showDialog(view);
			}
		});

		imageviewMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.text = 3;
				showDialog(view);
			}
		});

		imageviewCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.text = 2;
				showDialog(view);

			}
		});

		imageviewGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(intent, resultLoadImage);
			}
		});

		imageviewCamera.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
				String dateString = sdf.format(Calendar.getInstance().getTime());
				Log.v("", "" + dateString);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				File photo = new File(
						Environment.getExternalStorageDirectory(), dateString
								+ ".jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				imageUri = Uri.fromFile(photo);
				picturePathString = photo.toString();
				startActivityForResult(intent, takePicture);

			}
		});
		
	
		
		imageviewReminder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				Toast.makeText(getApplicationContext(), "Reminder Underconstruction...", Toast.LENGTH_LONG).show();
				
					
				custom.showDialog();
//				
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(Prefrences.selectedCheckitemSynopsis != 1 || Prefrences.comingFrom==4)
		{
		Intent intent = new Intent(CheckItemClick.this,ProjectDetail.class);
		startActivity(intent);
		}
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		try {

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.hideSoftInputFromWindow(getCurrentFocus()

			.getWindowToken(), 0);

		} catch (Exception exception) {

			exception.printStackTrace();

		}
	}

	public static void main(String[] args) throws Exception
	{
	    String str = textviewReminder.getText().toString()+".454 UTC";
	    SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
	    Date date = df.parse(str);
	    long epoch = date.getTime();
	    System.out.println(epoch); // 1055545912454
        reminderSet(epoch);

	}
	
	public void setlist(ListView listview, ListAdapter listAdapter, int size) {
		int maxHeight = 0;
		listAdapter = listview.getAdapter();

		// int totalHeight = listAdapter.getCount() * imageWidth;
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = null;
			listItem = listAdapter.getView(i, listItem, listview);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();// (int) (size); //*
														// ASSL.Yscale());
		}
		maxHeight += totalHeight;
		ViewGroup.LayoutParams params = listview.getLayoutParams();

		params.height = totalHeight
				+ (listview.getDividerHeight() * (listAdapter.getCount() - 1));

		listview.setLayoutParams(params);
		listview.requestLayout();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == resultLoadImage && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePathString = cursor.getString(columnIndex);
			cursor.close();

			ImageView ladderPageImageView = null;
			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
			ladderPageImageView = new ImageView(CheckItemClick.this);

			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					(int) (200), (int) (200));
			layoutParam.setMargins(10, 10, 10, 10);
			ladderPageImageView.setLayoutParams(layoutParam);
			File file = new File(picturePathString);
			Picasso.with(CheckItemClick.this).load(file)
					.placeholder(R.drawable.ic_launcher)
					.resize((int) (200 * ASSL.Xscale()),
					(int) (200 * ASSL.Xscale()))
					.into(ladderPageImageView);
			// ladder_page2.setImageBitmap(myBitmap);
			linearlayoutScrollable.addView(ladderPageImageView);
			// }

			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
//			ladderPageImageView.setImageBitmap(BitmapFactory.decodeFile(picturePathString));
			new LongOperation().execute("");
			// upload();
			// new Updating().execute("");
			// post_punchlst_items(picturePath);
			// commentAdd();

		}

		else if (requestCode == takePicture && resultCode == Activity.RESULT_OK) {
			Uri selectedImage = imageUri;
			
			
			picturePathString = ""+getRealPathFromURI(imageUri);
			getContentResolver().notifyChange(selectedImage, null);
		
			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			ImageView ladderPageImageView = null;
			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
			ladderPageImageView = new ImageView(CheckItemClick.this);

			ContentResolver contentResolver = getContentResolver();
			Bitmap bitmap;
			try {
				bitmap = android.provider.MediaStore.Images.Media.getBitmap(
						contentResolver, selectedImage);
				new LongOperation().execute("");
				LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
						(int) (200), (int) (200));
				layoutParam.setMargins(10, 10, 10, 10);
				ladderPageImageView.setLayoutParams(layoutParam);
				File file = new File(picturePathString);

				Picasso.with(CheckItemClick.this)
						.load(file)
						.resize((int) (200 * ASSL.Xscale()),
								(int) (200 * ASSL.Xscale())).centerCrop()
						.into(ladderPageImageView);
				ladderPageImageView.setImageBitmap(bitmap);
				linearlayoutScrollable.addView(ladderPageImageView);

				// imageView.setImageBitmap(bitmap);
				// Toast.makeText(this, selectedImage.toString(),
				// Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
						.show();
				Log.e("Camera", e.toString());
			}
		}
	}

	public void showDialog(View view) {

	
		DialogBox cdd = new DialogBox(CheckItemClick.this);
		cdd.requestWindowFeature(android.R.style.Theme_Translucent_NoTitleBar);
		// Window window = cdd.getWindow();
		// WindowManager.LayoutParams wlp = window.getAttributes();
		// cdd.getWindow().setWindowAnimations(R.anim.slide_in_from_bottom);
		// wlp.gravity = Gravity.BOTTOM;
		// wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		// wlp.width &= ~WindowManager.LayoutParams.MATCH_PARENT;
		// window.setAttributes(wlp);
		cdd.show();

	}
	
	// ************ API for Add Comments. *************//

	public void commentAdd(String body) {
		Prefrences.showLoadingDialog(CheckItemClick.this, "Loading...");
		JSONObject json = new JSONObject();
		try {
			json.put("checklist_item_id", idCheckitemString);
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

	

	// ************ API hit for "Post A Worklist Photo." *************//

	public void postPunchlstItems(String selectedPath) {

		/*
		 * Required Parameters “photo” : { “id” : {punchlist_item.id}, “source”
		 * : “Worklist”, “user_id” : {current_user.id}, “project_id” :
		 * {project.id}, “company_id” : {company.id}, “image” : your img_data }
		 */
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();

		HttpPost localHttpPost = new HttpPost(Prefrences.url
				+ "/checklist_items/photo");
		byte[] data = bos.toByteArray();
		// ByteArrayBody bab = new ByteArrayBody(data, selectedPath);

		File file = new File(selectedPath);
		try {

			JSONObject jsonobj = new JSONObject();
			jsonobj.put("punchlist_item_id", "10");
			jsonobj.put("source", "Worklist");
			jsonobj.put("user_id", "46");
			jsonobj.put("project_id", "37");
			jsonobj.put("company_id", "1");

			Prefrences.showLoadingDialog(CheckItemClick.this, "Loading...");

			MultipartEntity entity = new MultipartEntity();
			ContentBody imageFile = new FileBody(file, "image/jpeg");
			entity.addPart("image", imageFile);

			// MultipartEntity entity = new MultipartEntity(
			// HttpMultipartMode.BROWSER_COMPATIBLE);
			ContentBody str = new StringBody("application/json");
			// File file = new File(selectedPath);
			// FileBody bin = new FileBody(file);
			// entity.addPart("image",bin);
			// Log.d("image uri", "" + selectedImageUri);
			// File file = new File(selectedImageUri.getPath());
			// FileBody bin = new FileBody(file);
			// Log.d("bin file",""+bin);
			// entity.addPart("uploaded", bab);

			// entity.addPart("image", bab);
			entity.addPart(jsonobj.toString(), str);

			// localHttpPost.setEntity(entity);
			// new myAsync(localDefaultHttpClient, localHttpPost, entity)
			// .execute();
			//
			AsyncHttpClient client = new AsyncHttpClient();
			client.addHeader("ENCTYPE", "multipart/form-data");
			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");
			client.post(CheckItemClick.this, Prefrences.url
					+ "/punchlist_items/photo/", entity, "image/jpeg",
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(String response) {
							JSONObject res = null;
							try {
								res = new JSONObject(response);
								Log.d("punchlist_items response", "" + res);
								Prefrences.dismissLoadingDialog();
							} catch (Exception e) {
							}
						}

						@Override
						public void onFailure(Throwable arg0) {

							Log.e("request fail", arg0.toString());
							//
							Prefrences.dismissLoadingDialog();
							//
						}

					});
			Prefrences.dismissLoadingDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class commentadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentsArrayList.size();
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
				holder.textviewbody = (TextView) convertView.findViewById(R.id.comBody);
				holder.textviewUser = (TextView) convertView.findViewById(R.id.comUser);
				holder.textviewDate = (TextView) convertView.findViewById(R.id.comDate);
				// holder.delete=(Button)convertView.findViewById(R.id.DeleteSwipe);
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

			holder.textviewbody.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.textviewDate.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.textviewUser.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.textviewbody.setText("" + commentsArrayList.get(position).body.toString());
			if (commentsArrayList.get(position).cuser == null) {
				holder.textviewUser.setText("");
			} else {
				holder.textviewUser.setText(""
						+ commentsArrayList.get(position).cuser.get(0).fullName.toString());
			}

			if (commentsArrayList.get(position).created_at.toString().equals("just now")) {
				holder.textviewDate.setText("just now");
			} else {
				String date = utcToLocal(commentsArrayList.get(position).created_at.toString());
				Log.d("date==", "" + date);
				holder.textviewDate.setText("" + date);
			}
			// holder.linear2.setLayoutParams(new
			// ListView.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			//
			// ASSL.DoMagic(holder.linear2);

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
				public boolean onLongClick(final View view) {
					// TODO Auto-generated method stub

					// holder.delete.setVisibility(View.VISIBLE);
					ViewHolder holder2 = (ViewHolder) view.getTag();

					if (commentsArrayList.get(holder2.position).cuser.get(0).id.toString()
							.equalsIgnoreCase(Prefrences.userId.toString())) {
						final String ids = commentsArrayList.get(holder2.position).id
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
													int idOnclick) {
												// if this button is clicked,
												// close
												// current activity
												Log.d("Dialog", "Delete");
												deleteComments(ids);
												commentsArrayList.remove(position);
												Log.d("", "" + position);
												Log.d("", "" + commentsArrayList.size());
												holder.relativelayoutComment
														.setLayoutParams(layoutParams2);
												holder.relativelayoutComment.removeView(view);
												notifyDataSetChanged();

												// CheckItemClick.this.finish();
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int idOnclick) {
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
			// }
			// });

			// holder.delete.setTag(holder);
			notifyDataSetChanged();
			return convertView;

		}

	}

	public static class ViewHolder {

		public TextView textviewbody, textviewDate, textviewUser;
		// Button delete;
		// public ImageView right_img;
		public RelativeLayout relativelayoutComment;
		LinearLayout linearlayoutRoot;
		int position;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_item_click, menu);
		return true;
	}

	// ************ API for Delete comment. *************//
	public void deleteComments(String idComment) {

		Prefrences.showLoadingDialog(CheckItemClick.this, "Loading...");
		JSONObject json = new JSONObject();
		RequestParams params = new RequestParams();
		params.put("id", idComment);

		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.delete(this, Prefrences.url + "/comments/" + idComment,
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
						// ChecklistFragment.showcomments(id_checkitem,
						// status_checkitem, check)
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Prefrences.dismissLoadingDialog();
					}
				});

	}

	public class Updating extends AsyncTask<String, Integer, String>

	{

		String responseStr = null;

		HttpResponse response = null;

		HttpPost httpPost;

		private StringEntity strinEntity;
		HttpEntity resEntity;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			Log.e("Response ", "response : "
					+ response.getStatusLine().getStatusCode());

		}

		@Override
		protected String doInBackground(String... arg0) {

			HttpClient httpClient = new DefaultHttpClient();

			httpPost = new HttpPost(Prefrences.url + "/punchlist_items/photo");

			File file = new File(picturePathString);

			try {

				JSONObject dish = new JSONObject();

				// dish.put("punchlist_item_id", "10");

				dish.put("source", "Worklist");

				dish.put("user_id", "46");

				dish.put("project_id", "37");
				dish.put("company_id", "1");
				// dish.put("image","iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAMAAADyHTlpAAAAPFBMVEUAAAA/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz8/Pz/FnN5NAAAAE3RSTlMAH8UnkQ2iFc0FU9xFYLfscDqB+4/DKwAAAXVJREFUOMudlOuuhCAMhCmXAqJcnPd/1+OyahbUw2bnB4Hmo6ETWvGrPC0cUCX1ZP0jaOYIILHeFLFJq3vYFwnEyR5HlR3AdAPbFdDUxkoEFtOTFOHUNcGcoG0bUgGruJNlcMNSwCIepKHNx1XZkJ0Yqz9rX7H+Z7bEfBYKKf4TIdndeQnVluJc61BG3g2BFj3aGRRQAz6CRujyLpvAYoQaOF9vTENUMKiudoCe+UISY1RV58E9aqQ0fahS0OfZvqWcU/v2aASfZINGVyUdsC1vHSUH16AhyB6VDyjR5QHHmz3iC41flGVrQokvzCrI9ZfTGM0o9WPlMepg3l9hiCrw3mdlgJ6IQuzRQF3H7G3hNabOrA5lzF2bnZom3xzB/mMvnlXwYadfoR9JSpi7yeTvSZXQPsfqfhCeb6tkP17zJTEx0nyJ+jkgtXNXaYDpdpRmALwo41+9U7ID3GweRp5dJDalEPASt2BP07SydC7qXLbsv+oP6UIUDt4ImFYAAAAASUVORK5CYII=");

				JSONObject json = new JSONObject();

				// json.put("auth_token", Homepage.Token);

				json.put("photo", dish);
				json.put("punchlist_item_id", "10");
				// json.put("id", dish_id);

				strinEntity = new StringEntity(json.toString());

				httpPost.setHeader("Content-Type", "application/json");

				httpPost.setHeader("Accept", "application/json");

			} catch (JSONException e3) {

				//

				e3.printStackTrace();

			} catch (UnsupportedEncodingException e) {

				//

				e.printStackTrace();

			}

			httpPost.setEntity(strinEntity);

			try {

				response = httpClient.execute(httpPost);

				Log.e("execute the httppost", "execute the httppost" + response);

			} catch (ClientProtocolException e) {

				e.printStackTrace();

				Log.e("error in execute ", "," + e.toString());

			} catch (IOException e) {

				e.printStackTrace();

				Log.e("error in execute ", "," + e.toString());

			} catch (Exception e) {

				Log.v("error in jkghk ", e.toString());

			}

			try {

				resEntity = response.getEntity();

				// is = resEntity.getContent();

				responseStr = EntityUtils.toString(resEntity);

				Log.e("in resEntity", "in resEntity..." + responseStr);

			} catch (ParseException e1) {

				Log.e("1", "1");

				e1.printStackTrace();

			} catch (IOException e1) {

				Log.e("1", "2");

				e1.printStackTrace();

			} catch (Exception e) {

				Log.e("1", "3" + e);

			}

			return responseStr;

		}

	}

	// Update Checklist Item
	// PUT /checklist_items/{checklist_item_id}
	//
	// Sample Request
	// {
	// “checklist_item” : {
	// “id” : 122380,
	// “status” : “Completed”,
	// “completed” : 1,
	// “completed_by_user_id” : 2
	// }
	// }

	// ************ API for Update checklist item. *************//
	void updateItem() {

		Prefrences.showLoadingDialog(CheckItemClick.this, "Loading...");
//		JSONObject json = new JSONObject();
		 RequestParams params = new RequestParams();

	
		 		params.put("id", idCheckitemString);
		 		params.put("user_id", Prefrences.userId);
		 		params.put("checklist_item[state]", statusCheckitemString);
		 		if(statusCheckitemString.equalsIgnoreCase("1"))
		 			params.put("checklist_item[completed_by_user_id]", Prefrences.userId);
		 				
		
				
//			 params.put("checklist_item[state]", "0");
//				Log.e("State", "---      0");
//			}else if(statusCheckitem.equalsIgnoreCase("Completed")){
//				params.put("checklist_item[state]", "1");
//				Log.e("State", "---      1");
//			}else if(statusCheckitem.equalsIgnoreCase("-1")){
//				params.put("checklist_item[state]", "-1");
//				Log.e("State", "---     -1");
//			}else{
//				params.put("checklist_item[state]", "nil");
//				Log.e("State", "---      null");
//			}
//		 params.put("checklist_item[completed_by_user_id]", Prefrences.userId);
//		try {
//			json.put("id", idCheckitem);
//			json.put("user_id", Prefrences.userId);
//			if(completed==1)
//			json.put("completed_by_user_id", Prefrences.userId);
//			//json.put("status", statusCheckitem);
//			if(statusCheckitem.equalsIgnoreCase("In-Progress")){
//				
//				json.put("checklist_item[state]", "0");
//				Log.e("State", "---      0");
//			}else if(statusCheckitem.equalsIgnoreCase("Completed")){
//				json.put("checklist_item[state]", "1");
//				Log.e("State", "---      1");
//			}else if(statusCheckitem.equalsIgnoreCase("Not Applicable")){
//				json.put("checklist_item[state]", "-1");
//				Log.e("State", "---     -1");
//			}else{
//				json.put("checklist_item[state]", null);
//				Log.e("State", "---      null");
//			}
//			
//			
//			
//				json.put("completed", completed);
//			Log.v("value chk ---- ",Prefrences.userId+" "+statusCheckitem+" "+idCheckitem);
//
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
//		ByteArrayEntity entity = null;
//		try {
////			entity = new ByteArrayEntity(json.toString().getBytes("UTF-8"));
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
		client.put(this, Prefrences.url + "/checklist_items/" + idCheckitemString,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						call_service();
					
						try {
							
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
							
							for(int i=0;i<ChecklistFragment.categArrayList.size();i++){
								if(ChecklistFragment.categArrayList.get(i).getid().equalsIgnoreCase(catidString)){
									ArrayList<SubCategories> list_val = ChecklistFragment.categArrayList.get(i).getsubcat_array();
									for(int j=0;j<list_val.size();j++){
										if(list_val.get(j).getsub_id().equalsIgnoreCase(subcatidString)){
											 ArrayList<CheckItems> checklist =  list_val.get(j).getcheckItems();
											 for(int k=0;k<checklist.size();k++){
												 Log.e("k val123", k+".. ");
												 if(checklist.get(k).getitemid().equalsIgnoreCase(idCheckitemString)){
												
													 JSONObject cCount = res.getJSONObject("checklist_item");
													 Log.e("check123", k+" "+cCount.getString("body"));
													 Log.e("check123", k+" "+cCount.getString("state"));
													 
													 ChecklistFragment.categArrayList.get(i).getsubcat_array().get(j).getcheckItems().set(k, new CheckItems(catidString, subcatidString,
													idCheckitemString,
													cCount.getString("body"),
													cCount.getString("state"),
													cCount.getString("item_type"),
													cCount.getString("photos_count"),
													cCount.getString("comments_count")));
													 
													break; 
													 
												 }
												 
											 }
											
										}
									}
									
								}
								
							}
							
							
							
							
							ChecklistFragment.activeArrayList.clear();
							ChecklistFragment.completeArrayList.clear();
							ChecklistFragment.pcategArrayList.clear();
							
							ChecklistFragment.progressArrayList2.clear();
							ChecklistFragment.checkallArrayList.clear();
							
							for(int i=0;i<ChecklistFragment.categArrayList.size();i++){
								
								ChecklistFragment.psubCatArrayList = new ArrayList<SubCategories>();
								ChecklistFragment.activeSubCatArrayList = new ArrayList<SubCategories>();
								ChecklistFragment.completeSubCatArrayList = new ArrayList<SubCategories>();
								
									ArrayList<SubCategories> list_val = ChecklistFragment.categArrayList.get(i).getsubcat_array();
									
									for(int j=0;j<list_val.size();j++){
										
										ArrayList<CheckItems> activeCheckItemList = new ArrayList<CheckItems>();
										ArrayList<CheckItems> completeCheckItemList = new ArrayList<CheckItems>();
										ArrayList<CheckItems> progressList = new ArrayList<CheckItems>();
										
										ArrayList<CheckItems> checklist =  list_val.get(j).getcheckItems();
											 
											 for(int k=0;k<checklist.size();k++){
													if (!checklist.get(k).status
															.equals("1")
															&& !checklist.get(k).status
																	.equals("-1")) {
																activeCheckItemList.add(checklist.get(k));
													}

													if (checklist.get(k).status
															.equals("1")) {
														Log.e("complete", ",,"+ k);
														completeCheckItemList.add(checklist.get(k));
													}

													if (checklist.get(k).status.equals("0")) {
														progressList.add(checklist.get(k));
														Log.e("Progress",",,"+ k);
													//	progressList.add(ChecklistFragment.categList.get(i).getsubcat_array().get(j).getcheckItems().get(k));
														
													}
												 
											 }
											 
											ChecklistFragment.checkallArrayList.addAll(checklist);
											 ChecklistFragment.progressArrayList2.addAll(progressList);
											 
												if (activeCheckItemList.size() > 0) {
												
												ChecklistFragment.activeSubCatArrayList.add(new SubCategories( list_val.get(j).getsub_id(),
														list_val.get(j).name,
														list_val.get(j).completedDate,
														list_val.get(j).milestoneDate,
														list_val.get(j).progPerc,
														activeCheckItemList));
											}
											 
											 

												if (completeCheckItemList.size() > 0) {
												
													ChecklistFragment.completeSubCatArrayList.add(new SubCategories( list_val.get(j).getsub_id(),
															list_val.get(j).name,
															list_val.get(j).completedDate,
															list_val.get(j).milestoneDate,
															list_val.get(j).progPerc,
															 completeCheckItemList));

												
												if (progressList.size() > 0) {

													ChecklistFragment.psubCatArrayList.add(new SubCategories( list_val.get(j).getsub_id(),
															list_val.get(j).name,
															list_val.get(j).completedDate,
															list_val.get(j).milestoneDate,
															list_val.get(j).progPerc,
														 progressList));
												}
												}
											
										}
									
									
									
									
									if (ChecklistFragment.psubCatArrayList.size() > 0) {
									ChecklistFragment.pcategArrayList.add(new Categories(ChecklistFragment.categArrayList.get(i).id,ChecklistFragment.categArrayList.get(i).name,
											ChecklistFragment.categArrayList.get(i).completedDate, ChecklistFragment.categArrayList.get(i).milestoneDate, ChecklistFragment.categArrayList.get(i).progPerc,
											ChecklistFragment.psubCatArrayList));
									}

									if (ChecklistFragment.activeSubCatArrayList.size() > 0) {
										ChecklistFragment.activeArrayList.add(new Categories(ChecklistFragment.categArrayList.get(i).id,ChecklistFragment.categArrayList.get(i).name,
												ChecklistFragment.categArrayList.get(i).completedDate, ChecklistFragment.categArrayList.get(i).milestoneDate, ChecklistFragment.categArrayList.get(i).progPerc,
												ChecklistFragment.activeSubCatArrayList));

									}

									if (ChecklistFragment.completeSubCatArrayList.size() > 0) {
										ChecklistFragment.completeArrayList.add(new Categories(ChecklistFragment.categArrayList.get(i).id,ChecklistFragment.categArrayList.get(i).name,
												ChecklistFragment.categArrayList.get(i).completedDate, ChecklistFragment.categArrayList.get(i).milestoneDate, ChecklistFragment.categArrayList.get(i).progPerc,
												ChecklistFragment.completeSubCatArrayList));
									}
								}
									
								
								
							
							

							
							
							
							Prefrences.CheckItemClickFlag =true;
							finish();
							
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

	// @Override
	// public void onBackPressed() {
	//
	// finish();
	// Intent in = null;
	// // if(Prefrences.selectedCheckitem == 0 ||
	// Prefrences.selectedCheckitem==1 )
	// // {
	// // in = new Intent(getApplicationContext(),Synopsis.class);
	// // startActivity(in);
	// // }
	// if(Prefrences.selectedCheckitem == 2 || Prefrences.selectedCheckitem == 3
	// || Prefrences.selectedCheckitem == 4 || Prefrences.selectedCheckitem == 5
	// || Prefrences.selectedCheckitem == 6)
	// {
	// in = new Intent(getApplicationContext(),ProjectDetail.class);
	// startActivity(in);
	// }
	//
	//
	//
	//
	// }

	private class LongOperation extends AsyncTask<String, Void, String> {
		
		boolean error;

		@Override
		protected String doInBackground(String... params) {

			try {
//				if(picturePathString.isEmpty()){
				postPicture();
//				}
//				else{
					
//				}

			} catch (ParseException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}
//			catch (IOException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			}
//			catch (XmlPullParserException e) {
//
//				// TODO Auto-generated catch block
//
//				e.printStackTrace();
//
//			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				error = true;
//				Toast.makeText(CheckItemClick.this, "Failed to load", Toast.LENGTH_SHORT).show();
//				Toast.makeText(getApplicationContext(), "Not a valid Photo", Toast.LENGTH_SHORT).show();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			Prefrences.dismissLoadingDialog();
			if(error)
			{
				AlertMessage();
			}
		}

		@Override
		protected void onPreExecute() {
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

	// ************ API for Post photo. *************//
	public void postPicture() throws ParseException, IOException,

	XmlPullParserException {

		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Prefrences.url

		+ "/checklist_items/photo/");

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

		
			
		Log.v("picturePath", picturePathString);

		File file = new File(picturePathString);

		FileBody cbFile = new FileBody(file, "image/jpg");

		cbFile.getMediaType();

		mpEntity.addPart("photo[checklist_item_id]",
				new StringBody(idCheckitemString));

		mpEntity.addPart("photo[mobile]", new StringBody("1"));

		mpEntity.addPart("photo[company_id]", new StringBody(
				Prefrences.companyId));

		mpEntity.addPart("photo[source]", new StringBody("Checklist"));

		mpEntity.addPart("photo[user_id]", new StringBody(Prefrences.userId));

		mpEntity.addPart("photo[project_id]", new StringBody(
				Prefrences.selectedProId));

		mpEntity.addPart("photo[name]", new StringBody("android"));

		// mpEntity.addPart("photo[description]", new StringBody("android2"));

		mpEntity.addPart("photo[image]", cbFile);

		Log.i("photo[checklist_item_id]", idCheckitemString);
		Log.i("photo[mobile]", "1");
		Log.i("photo[company_id]", "8");
		Log.i("photo[source]", "Checklist");
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
	
	// ************ API for setting Reminder. *************//
	
	public static void reminderSet(long epoch) {
		Prefrences.showLoadingDialog(con, "Loading...");
//		JSONObject json = new JSONObject();
		RequestParams params = new RequestParams();
		
		params.put("reminder[checklist_item_id]",idCheckitemString );
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
	
	public void call_service(){
		Log.e("Inside service call", "fgbvtxsh");
		startService(new Intent(this, MyService.class));
	}

	public static String utcToLocal(String utcTime) {

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy, hh:mm a");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	try {
	Date myDate = simpleDateFormat.parse(utcTime);
	String localDateString = sdf.format(myDate);
	Log.d("date ","hiii date "+localDateString.toString());
	return localDateString;
	} catch (Exception e1) {
	Log.e("e1","="+e1);
	return utcTime;
	}
	}
	
	// ************ API for selected checklist item. *************//
	
	public void showComments(final String idCheck) {

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
						DatabaseClass dbObject = new DatabaseClass(con);
						dbObject.open();
						Log.e("CHKITEMResponse", "msg: "+response);
						if(dbObject.exists_checkitem(Prefrences.selectedProId, idCheck)){
							dbObject.update_checkitem(Prefrences.selectedProId, idCheck, response);
						}else{
							dbObject.CreateChkItem(Prefrences.selectedProId, idCheck, response);
						}
						dbObject.close();
						fillServerDataItem(response);
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
	
	
	public void fillServerDataItem(String response){
		JSONObject res = null;

		try {
			Log.e("response", "msg: "+response);
			checkListItemDataArrayList.clear();
			commentsArrayList.clear();
			photosArrayList.clear();
			res = new JSONObject(response);
			Log.v("response ", "" + res.toString(2));
			JSONObject punchlists = res
					.getJSONObject("checklist_item");// chexklist

			JSONArray phot = punchlists.getJSONArray("photos");// categ

			JSONArray personel = punchlists
					.getJSONArray("comments");

			for (int j = 0; j < phot.length(); j++) {
				JSONObject ccount = phot.getJSONObject(j);

				photosArrayList.add(new PhotosCheckListItem(ccount
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

				commentsArrayList.add(new CommentsChecklistItem(count
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
			Log.d("comments", "Size" + commentsArrayList.size());
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
			
			
			checkListItemDataArrayList.add(new DataCheckListItems(
					punchlists.getString("id"), punchlists
							.getString("body"), punchlists
							.getString("state"), punchlists
							.getString("item_type"), punchlists
							.getString("photos_count"),
					punchlists.getString("comments_count"),
					photosArrayList, commentsArrayList, 
					punchlists
							.getString("phase_name"),
					punchlists.getString("project_id")));

			// Log.d("checklistfragment ",
			// "lalalala"+CheckItemClick.comm.size());

//			Intent intent = new Intent(con,
//					CheckItemClick.class);
			
			textviewBody.setText(punchlists.getString("body"));
			textviewType.setText(punchlists.getString("item_type"));
	
//				idCheckitem = bundle.getString("item_id");
				statusCheckitemString = punchlists.getString("state");
				
				if (statusCheckitemString.equalsIgnoreCase("1")) {
					completed=1;
					buttonComplete.setBackgroundColor(Color.BLACK);
					buttonComplete.setTextColor(Color.WHITE);
					buttonProgress.setBackgroundResource(drawable.transparentclick);
					buttonProgress.setTextColor(Color.BLACK);
					buttonNotapp.setBackgroundResource(drawable.transparentclick);
					buttonNotapp.setTextColor(Color.BLACK);

				} else if (statusCheckitemString.equalsIgnoreCase("0")) {
					buttonComplete.setBackgroundResource(drawable.transparentclick);
					buttonComplete.setTextColor(Color.BLACK);
					buttonProgress.setBackgroundColor(Color.BLACK);
					buttonProgress.setTextColor(Color.WHITE);
					buttonNotapp.setBackgroundResource(drawable.transparentclick);
					buttonNotapp.setTextColor(Color.BLACK);

				} else if (statusCheckitemString.equalsIgnoreCase("-1")) {
					buttonComplete.setBackgroundResource(drawable.transparentclick);
					buttonComplete.setTextColor(Color.BLACK);
					buttonProgress.setBackgroundResource(drawable.transparentclick);
					buttonProgress.setTextColor(Color.BLACK);
					buttonNotapp.setBackgroundColor(Color.BLACK);
					buttonNotapp.setTextColor(Color.WHITE);
				} else {
					Log.d("***********&&&&&&&&&&&&&","--------------############   "+statusCheckitemString);
					statusCheckitemString = "";
					buttonComplete.setBackgroundResource(drawable.transparentclick);
					buttonComplete.setTextColor(Color.BLACK);
					buttonProgress.setBackgroundResource(drawable.transparentclick);
					buttonProgress.setTextColor(Color.BLACK);
					buttonNotapp.setBackgroundResource(drawable.transparentclick);
					buttonNotapp.setTextColor(Color.BLACK);
				}
				   comadap = new commentadapter();
					listviewComments.setAdapter(comadap);

					setlist(listviewComments, comadap, 230);
					
					final ArrayList<String> arr = new ArrayList<String>();
					final ArrayList<String> ids = new ArrayList<String>();
					final ArrayList<String> desc = new ArrayList<String>();
					// Log.d("",""+comm.get(0).body.toString());
					linearlayoutScrollable = (LinearLayout) findViewById(R.id.linearlayoutLin2);

					ImageView ladderPage2 = null;
					// Log.d("--------","888888"+DocumentFragment.photosList.size());
					for (int i = 0; i < photosArrayList.size(); i++) {
						ladderPage2 = new ImageView(CheckItemClick.this);
						arr.add(photosArrayList.get(i).urlLarge);
						ids.add(photosArrayList.get(i).id);
						desc.add(photosArrayList.get(i).description);
						LinearLayout.LayoutParams layouParam = new LinearLayout.LayoutParams(
								(int) (200), (int) (200));
						layouParam.setMargins(10, 10, 10, 10);
						ladderPage2.setTag(i);
						ladderPage2.setLayoutParams(layouParam);
						// Picasso.with(con).load(arr.get(i).toString()).into(ladder_page2);

						Picasso.with(CheckItemClick.this)
								.load(arr.get(i).toString())
								.resize((int) (200 * ASSL.Xscale()),
										(int) (200 * ASSL.Xscale())).centerCrop()
								.into(ladderPage2);
						// // ladder_page2.setImageBitmap(myBitmap);
						linearlayoutScrollable.addView(ladderPage2);

						ladderPage2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								Log.i("Tag Value", "" + Prefrences.selectedPic);
								Prefrences.selectedPic = (Integer) view.getTag();
								Log.i("Tag Value", "" + Prefrences.selectedPic);

								Intent intent = new Intent(con, SelectedImageView.class);
								intent.putExtra("array", arr);
								intent.putExtra("ids", ids);
								intent.putExtra("desc", desc);
								intent.putExtra("id", photosArrayList
										.get(Prefrences.selectedPic).id);
								startActivity(intent);
								overridePendingTransition(R.anim.slide_in_right,
										R.anim.slide_out_left);
								// finish();
							}
						});

					}
//				catid = check.getcat_id().toString();
//				subcatid = check.getsubCat_id().toString();
				
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
