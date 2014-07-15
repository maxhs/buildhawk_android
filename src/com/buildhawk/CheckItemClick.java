package com.buildhawk;

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
import com.buildhawk.utils.DataCheckListItems;
import com.buildhawk.utils.DialogBox;
import com.buildhawk.utils.SubCategories;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

public class CheckItemClick extends Activity {

	public static Fragment fragment;
	private int resultLoadImage = 1;
	private int takePicture = 1;
	private Uri imageUri;
	RelativeLayout relLay;
	TextView tv_txtBody, tv_txtType;
	EditText edt_txt_commentbody;
	Button btn_sendcomment;
	CustomDateTimePicker custom;
	// public static ArrayList<PhotosCheckListItem>pho=new
	// ArrayList<PhotosCheckListItem>();
	ArrayList<CommentsChecklistItem> comm = new ArrayList<CommentsChecklistItem>();
	Button btn_complete, btn_progress, btn_notapp, btn_save;
	int completed=0;
	String picturePath;
	LinearLayout lin2;
	int temp, temp2;
	LinearLayout lin3;
	SwipeDetector swipeDetector;
	static String idCheckitem;
	String statusCheckitem, catid,subcatid;
	ListView commentslist;
	static Context con;
	static TextView txt_reminder;
	SwipeDetector swipeDetecter = new SwipeDetector();
	ScrollView scrollView;
	ByteArrayOutputStream bos = new ByteArrayOutputStream();

	// final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_item_click);

		relLay = (RelativeLayout) findViewById(R.id.rellay);
		new ASSL(this, relLay, 1134, 720, false);

		
		con = CheckItemClick.this;
		comm.clear();
		comm.addAll(ChecklistFragment.comments);
		commentslist = (ListView) findViewById(R.id.commentslist);

		ImageView img_camera = (ImageView) findViewById(R.id.camera);
		ImageView img_reminder = (ImageView) findViewById(R.id.reminder);
		ImageView img_gallery = (ImageView) findViewById(R.id.photogallery);
		ImageView img_call = (ImageView) findViewById(R.id.call);
		Button btn_back = (Button) findViewById(R.id.back);
		btn_save = (Button) findViewById(R.id.save);
		ImageView img_email = (ImageView) findViewById(R.id.email);
		ImageView img_message = (ImageView) findViewById(R.id.message);
		edt_txt_commentbody = (EditText) findViewById(R.id.commentBody);
		btn_sendcomment = (Button) findViewById(R.id.sendcomment);
		btn_complete = (Button) findViewById(R.id.complete_btn);
		btn_progress = (Button) findViewById(R.id.progress_btn);
		btn_notapp = (Button) findViewById(R.id.notApplicable_btn);
		tv_txtBody = (TextView) findViewById(R.id.txt_body);
		tv_txtType = (TextView) findViewById(R.id.txt_type);
		txt_reminder= (TextView) findViewById(R.id.txtReminder);

		txt_reminder.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		btn_complete.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		tv_txtBody.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		tv_txtType.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		btn_progress.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		btn_notapp.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		edt_txt_commentbody.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		btn_sendcomment.setTypeface(Prefrences
				.helveticaNeuelt(getApplicationContext()));
		btn_back.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		btn_save.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		scrollView = (ScrollView) findViewById(R.id.scrollView1_checkitem);
		scrollView.smoothScrollTo(0, 0);
		
		
		
		
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
			        	

		                           txt_reminder.setText(monthShortName 
		                                    + " " + calendarSelected
                                            .get(Calendar.DAY_OF_MONTH) + " " + year
		                                    + " " + hour24 + ":" + min
		                                    + ":" + sec);
		                           String str = txt_reminder.getText().toString()+".454 UTC";
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


		Bundle bundle = getIntent().getExtras();
		tv_txtBody.setText(bundle.getString("body"));

		tv_txtType.setText(bundle.getString("itemtype"));

		final commentadapter comadap = new commentadapter();
		commentslist.setAdapter(comadap);

		setlist(commentslist, comadap, 230);

		
			idCheckitem = bundle.getString("item_id");
			statusCheckitem = bundle.getString("status");
			catid = bundle.getString("cat_id");
			subcatid = bundle.getString("subcat_id");		

		if (statusCheckitem.equalsIgnoreCase("1")) {
			completed=1;
			btn_complete.setBackgroundColor(Color.BLACK);
			btn_complete.setTextColor(Color.WHITE);
			btn_progress.setBackgroundResource(drawable.transparentclick);
			btn_progress.setTextColor(Color.BLACK);
			btn_notapp.setBackgroundResource(drawable.transparentclick);
			btn_notapp.setTextColor(Color.BLACK);

		} else if (statusCheckitem.equalsIgnoreCase("0")) {
			btn_complete.setBackgroundResource(drawable.transparentclick);
			btn_complete.setTextColor(Color.BLACK);
			btn_progress.setBackgroundColor(Color.BLACK);
			btn_progress.setTextColor(Color.WHITE);
			btn_notapp.setBackgroundResource(drawable.transparentclick);
			btn_notapp.setTextColor(Color.BLACK);

		} else if (statusCheckitem.equalsIgnoreCase("-1")) {
			btn_complete.setBackgroundResource(drawable.transparentclick);
			btn_complete.setTextColor(Color.BLACK);
			btn_progress.setBackgroundResource(drawable.transparentclick);
			btn_progress.setTextColor(Color.BLACK);
			btn_notapp.setBackgroundColor(Color.BLACK);
			btn_notapp.setTextColor(Color.WHITE);
		} else {
			statusCheckitem = "";
			btn_complete.setBackgroundResource(drawable.transparentclick);
			btn_complete.setTextColor(Color.BLACK);
			btn_progress.setBackgroundResource(drawable.transparentclick);
			btn_progress.setTextColor(Color.BLACK);
			btn_notapp.setBackgroundResource(drawable.transparentclick);
			btn_notapp.setTextColor(Color.BLACK);
		}

		btn_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				completed = 1;
				statusCheckitem = "1";
				btn_complete.setBackgroundColor(Color.BLACK);
				btn_complete.setTextColor(Color.WHITE);
				btn_progress.setBackgroundResource(drawable.transparentclick);
				btn_progress.setTextColor(Color.BLACK);
				btn_notapp.setBackgroundResource(drawable.transparentclick);
				btn_notapp.setTextColor(Color.BLACK);
			}
		});

		btn_progress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				statusCheckitem = "0";
				btn_complete.setBackgroundResource(drawable.transparentclick);
				btn_complete.setTextColor(Color.BLACK);
				btn_progress.setBackgroundColor(Color.BLACK);
				btn_progress.setTextColor(Color.WHITE);
				btn_notapp.setBackgroundResource(drawable.transparentclick);
				btn_notapp.setTextColor(Color.BLACK);
				completed = 0;

			}
		});

		btn_notapp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				statusCheckitem = "-1";
				btn_complete.setBackgroundResource(drawable.transparentclick);
				btn_complete.setTextColor(Color.BLACK);
				btn_progress.setBackgroundResource(drawable.transparentclick);
				btn_progress.setTextColor(Color.BLACK);
				btn_notapp.setBackgroundColor(Color.BLACK);
				btn_notapp.setTextColor(Color.WHITE);
				completed = 0;
			}
		});

		
		btn_save.setOnClickListener(new OnClickListener() {

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
				updateItem();


			}
		});
		
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
			edt_txt_commentbody.setBackground(null);
		}
		else {
			edt_txt_commentbody.setBackgroundDrawable(null);
		}

		
		
		edt_txt_commentbody.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence charsequence, int arg1,
					int arg2, int arg3) {

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
				commentAdd(edt_txt_commentbody.getText().toString());
				// commentslist.add
				comm.add(new CommentsChecklistItem(idCheckitem, edt_txt_commentbody
						.getText().toString(), null, "just now"));
				Log.d("*****", "increased size" + comm.size());
				commentslist.setAdapter(comadap);
				comadap.notifyDataSetChanged();
				setlist(commentslist, comadap, 230);
				edt_txt_commentbody.setText("");
				btn_sendcomment.setVisibility(View.GONE);
			}else{
				Toast.makeText(CheckItemClick.this,"No internet connection.", Toast.LENGTH_SHORT).show();
			}
			}
		});

		final ArrayList<String> arr = new ArrayList<String>();
		final ArrayList<String> ids = new ArrayList<String>();
		final ArrayList<String> desc = new ArrayList<String>();
		// Log.d("",""+comm.get(0).body.toString());
		lin2 = (LinearLayout) findViewById(R.id.scrolllayout2);

		ImageView ladderPage2 = null;
		// Log.d("--------","888888"+DocumentFragment.photosList.size());
		for (int i = 0; i < ChecklistFragment.photos.size(); i++) {
			ladderPage2 = new ImageView(CheckItemClick.this);
			arr.add(ChecklistFragment.photos.get(i).urlLarge);
			ids.add(ChecklistFragment.photos.get(i).id);
			desc.add(ChecklistFragment.photos.get(i).description);
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
			lin2.addView(ladderPage2);

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
					intent.putExtra("id", ChecklistFragment.photos
							.get(Prefrences.selectedPic).id);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					// finish();
				}
			});

		}

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

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
			}
		});

		img_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Prefrences.text = 1;
				showDialog(view);
			}
		});

		img_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.text = 3;
				showDialog(view);
			}
		});

		img_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Prefrences.text = 2;
				showDialog(view);

			}
		});

		img_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(intent, resultLoadImage);
			}
		});

		img_camera.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View view) {
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
				startActivityForResult(intent, takePicture);

			}
		});
		
	
		
		img_reminder.setOnClickListener(new OnClickListener() {
			
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
	    String str = txt_reminder.getText().toString()+".454 UTC";
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
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			ImageView ladderPage2 = null;
			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
			ladderPage2 = new ImageView(CheckItemClick.this);

			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
					(int) (200), (int) (200));
			layoutParam.setMargins(10, 10, 10, 10);
			ladderPage2.setLayoutParams(layoutParam);

			Picasso.with(CheckItemClick.this).load(picturePath)
					.placeholder(R.drawable.ic_launcher).into(ladderPage2);
			// ladder_page2.setImageBitmap(myBitmap);
			lin2.addView(ladderPage2);
			// }

			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			ladderPage2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			new LongOperation().execute("");
			// upload();
			// new Updating().execute("");
			// post_punchlst_items(picturePath);
			// commentAdd();

		}

		else if (requestCode == takePicture && resultCode == Activity.RESULT_OK) {
			Uri selectedImage = imageUri;
			getContentResolver().notifyChange(selectedImage, null);
			// ImageView imageView = (ImageView) findViewById(R.id.imgView);
			ImageView ladderPage2 = null;
			// Log.d("--------","888888"+DocumentFragment.photosList.size());
			// for (int i = 0; i < DocumentFragment.photosList.size(); i++) {
			ladderPage2 = new ImageView(CheckItemClick.this);

			ContentResolver contentResolver = getContentResolver();
			Bitmap bitmap;
			try {
				bitmap = android.provider.MediaStore.Images.Media.getBitmap(
						contentResolver, selectedImage);
				new LongOperation().execute("");
				LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
						(int) (200), (int) (200));
				layoutParam.setMargins(10, 10, 10, 10);
				ladderPage2.setLayoutParams(layoutParam);
				File file = new File(picturePath);

				Picasso.with(CheckItemClick.this)
						.load(file)
						.resize((int) (200 * ASSL.Xscale()),
								(int) (200 * ASSL.Xscale())).centerCrop()
						.into(ladderPage2);
				ladderPage2.setImageBitmap(bitmap);
				lin2.addView(ladderPage2);

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

	public void commentAdd(String body) {
		Prefrences.showLoadingDialog(CheckItemClick.this, "Loading...");
		JSONObject json = new JSONObject();
		try {
			json.put("checklist_item_id", idCheckitem);
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
			return comm.size();
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
				holder.lin4 = (RelativeLayout) convertView
						.findViewById(R.id.lin4);
				holder.linear2 = (LinearLayout) convertView
						.findViewById(R.id.linear2);

				holder.linear2.setLayoutParams(new ListView.LayoutParams(
						ListView.LayoutParams.MATCH_PARENT, 200));
				ASSL.DoMagic(holder.linear2);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.lin4.setTag(holder);
			holder.position = position;

			holder.body.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.date.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.user.setTypeface(Prefrences
					.helveticaNeuelt(getApplicationContext()));
			holder.body.setText("" + comm.get(position).body.toString());
			if (comm.get(position).cuser == null) {
				holder.user.setText("");
			} else {
				holder.user.setText(""
						+ comm.get(position).cuser.get(0).fullName.toString());
			}

			if (comm.get(position).created_at.toString().equals("just now")) {
				holder.date.setText("just now");
			} else {
				String date = utcToLocal(comm.get(position).created_at.toString());
				Log.d("date==", "" + date);
				holder.date.setText("" + date);
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
			

			holder.lin4.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(final View view) {
					// TODO Auto-generated method stub

					// holder.delete.setVisibility(View.VISIBLE);
					ViewHolder holder2 = (ViewHolder) view.getTag();

					if (comm.get(holder2.position).cuser.get(0).id.toString()
							.equalsIgnoreCase(Prefrences.userId.toString())) {
						final String ids = comm.get(holder2.position).id
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
												comm.remove(position);
												Log.d("", "" + position);
												Log.d("", "" + comm.size());
												holder.lin4
														.setLayoutParams(layoutParams2);
												holder.lin4.removeView(view);
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

		public TextView body, date, user;
		// Button delete;
		// public ImageView right_img;
		public RelativeLayout lin4;
		LinearLayout linear2;
		int position;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.check_item_click, menu);
		return true;
	}

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

			File file = new File(picturePath);

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

	void updateItem() {

		Prefrences.showLoadingDialog(CheckItemClick.this, "Loading...");
//		JSONObject json = new JSONObject();
		 RequestParams params = new RequestParams();

	
		 		params.put("id", idCheckitem);
		 		params.put("user_id", Prefrences.userId);
		 		params.put("checklist_item[state]", statusCheckitem);
		 		if(statusCheckitem.equalsIgnoreCase("1"))
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
		client.put(this, Prefrences.url + "/checklist_items/" + idCheckitem,
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						call_service();
					
						try {
							
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
							
							for(int i=0;i<ChecklistFragment.categList.size();i++){
								if(ChecklistFragment.categList.get(i).getid().equalsIgnoreCase(catid)){
									ArrayList<SubCategories> list_val = ChecklistFragment.categList.get(i).getsubcat_array();
									for(int j=0;j<list_val.size();j++){
										if(list_val.get(j).getsub_id().equalsIgnoreCase(subcatid)){
											 ArrayList<CheckItems> checklist =  list_val.get(j).getcheckItems();
											 for(int k=0;k<checklist.size();k++){
												 Log.e("k val123", k+".. ");
												 if(checklist.get(k).getitemid().equalsIgnoreCase(idCheckitem)){
												
													 JSONObject cCount = res.getJSONObject("checklist_item");
													 Log.e("check123", k+" "+cCount.getString("body"));
													 
													 ChecklistFragment.categList.get(i).getsubcat_array().get(j).getcheckItems().set(k, new CheckItems(catid, subcatid,
													idCheckitem,
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
							
							
							
							
							ChecklistFragment.activelist.clear();
							ChecklistFragment.completelist.clear();
							ChecklistFragment.pcategList.clear();
							
							ChecklistFragment.progressList2.clear();
							ChecklistFragment.checkall.clear();
							
							for(int i=0;i<ChecklistFragment.categList.size();i++){
								
								ChecklistFragment.psubCatList = new ArrayList<SubCategories>();
								ChecklistFragment.activeSubCatList = new ArrayList<SubCategories>();
								ChecklistFragment.completeSubCatList = new ArrayList<SubCategories>();
								
									ArrayList<SubCategories> list_val = ChecklistFragment.categList.get(i).getsubcat_array();
									
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
											 
											ChecklistFragment.checkall.addAll(checklist);
											 ChecklistFragment.progressList2.addAll(progressList);
											 
												if (activeCheckItemList.size() > 0) {
												
												ChecklistFragment.activeSubCatList.add(new SubCategories( list_val.get(j).getsub_id(),
														list_val.get(j).name,
														list_val.get(j).completedDate,
														list_val.get(j).milestoneDate,
														list_val.get(j).progPerc,
														activeCheckItemList));
											}
											 
											 

												if (completeCheckItemList.size() > 0) {
												
													ChecklistFragment.completeSubCatList.add(new SubCategories( list_val.get(j).getsub_id(),
															list_val.get(j).name,
															list_val.get(j).completedDate,
															list_val.get(j).milestoneDate,
															list_val.get(j).progPerc,
															 completeCheckItemList));

												
												if (progressList.size() > 0) {

													ChecklistFragment.psubCatList.add(new SubCategories( list_val.get(j).getsub_id(),
															list_val.get(j).name,
															list_val.get(j).completedDate,
															list_val.get(j).milestoneDate,
															list_val.get(j).progPerc,
														 progressList));
												}
												}
											
										}
									
									
									
									
									if (ChecklistFragment.psubCatList.size() > 0) {
									ChecklistFragment.pcategList.add(new Categories(ChecklistFragment.categList.get(i).id,ChecklistFragment.categList.get(i).name,
											ChecklistFragment.categList.get(i).completedDate, ChecklistFragment.categList.get(i).milestoneDate, ChecklistFragment.categList.get(i).progPerc,
											ChecklistFragment.psubCatList));
									}

									if (ChecklistFragment.activeSubCatList.size() > 0) {
										ChecklistFragment.activelist.add(new Categories(ChecklistFragment.categList.get(i).id,ChecklistFragment.categList.get(i).name,
												ChecklistFragment.categList.get(i).completedDate, ChecklistFragment.categList.get(i).milestoneDate, ChecklistFragment.categList.get(i).progPerc,
												ChecklistFragment.activeSubCatList));

									}

									if (ChecklistFragment.completeSubCatList.size() > 0) {
										ChecklistFragment.completelist.add(new Categories(ChecklistFragment.categList.get(i).id,ChecklistFragment.categList.get(i).name,
												ChecklistFragment.categList.get(i).completedDate, ChecklistFragment.categList.get(i).milestoneDate, ChecklistFragment.categList.get(i).progPerc,
												ChecklistFragment.completeSubCatList));
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

		@Override
		protected String doInBackground(String... params) {

			try {

				postPicture();

			} catch (ParseException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			} catch (IOException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			} catch (XmlPullParserException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			Prefrences.dismissLoadingDialog();
		}

		@Override
		protected void onPreExecute() {
			Prefrences.showLoadingDialog(con, "Uploading...");
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}

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

		Log.v("picturePath", picturePath);

		File file = new File(picturePath);

		FileBody cbFile = new FileBody(file, "image/jpg");

		cbFile.getMediaType();

		mpEntity.addPart("photo[checklist_item_id]",
				new StringBody(idCheckitem));

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

		Log.i("photo[checklist_item_id]", idCheckitem);
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
	
	public static void reminderSet(long epoch) {
		Prefrences.showLoadingDialog(con, "Loading...");
//		JSONObject json = new JSONObject();
		RequestParams params = new RequestParams();
		
		params.put("reminder[checklist_item_id]",idCheckitem );
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

	SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yy, hh:mm a");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	try {
	Date myDate = simpleDateFormat.parse(utcTime);
	String localDate = sdf.format(myDate);
	Log.d("date ","hiii date "+localDate.toString());
	return localDate;
	} catch (Exception e1) {
	Log.e("e1","="+e1);
	return utcTime;
	}
	}
}
