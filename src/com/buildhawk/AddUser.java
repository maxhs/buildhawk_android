package com.buildhawk;
/*
 *  This file is used to add users and connect_users for the particular project. 
 * 
 */
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AddUser extends Activity {

	private static final int PICK_CONTACT = 0;
	String contactIDString;
	LinearLayout linearlayoutRoot;
	EditText edittextEmail,edittextFirstName,edittextLastName,edittextPhoneNumber;
	EditText  edittextCompanyName;
	TextView textviewCompany,textviewAdd;
	RelativeLayout relativelayoutBack, relativelayoutPullContacts;
	EmailFormatValidator check;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);

		linearlayoutRoot = (LinearLayout) findViewById(R.id.linearlayoutroot);
		new ASSL(this, linearlayoutRoot, 1134, 720, false);

		relativelayoutBack = (RelativeLayout) findViewById(R.id.relativelayoutBack);
		relativelayoutPullContacts = (RelativeLayout) findViewById(R.id.relativelayoutPullContacts);
		edittextEmail=(EditText)findViewById(R.id.edittextEmail);
		edittextFirstName=(EditText)findViewById(R.id.edittextFirstName);
		edittextLastName=(EditText)findViewById(R.id.edittextLastName);
		edittextPhoneNumber=(EditText)findViewById(R.id.edittextPhoneNumber);
		textviewCompany=(TextView)findViewById(R.id.textviewCompany);
		edittextCompanyName=(EditText)findViewById(R.id.edittextCompanyName);
		textviewAdd=(TextView)findViewById(R.id.textviewAdd);
		
		check= new EmailFormatValidator();
		textviewCompany.setText(Prefrences.selectedCompanyName);
		
		edittextCompanyName.setText(Prefrences.selectedCompanyName);
		
				textviewAdd.setOnClickListener(new OnClickListener() {
			
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
				
				if(edittextEmail.getText().toString().equals("") || edittextPhoneNumber.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "Fill the fields", Toast.LENGTH_LONG).show();
				
				}
				else
				{
					Boolean validate = check.validate(edittextEmail.getText().toString());

					if(validate)
					{											
//					new LongOperation().execute("");
					addUsers();
					}
					else{
						Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
						edittextEmail.setFocusable(true);
					}
				}
			
				
			}
		});
		
		relativelayoutPullContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edittextEmail.setText("");
				edittextFirstName.setText("");
				edittextLastName.setText("");
				edittextPhoneNumber.setText("");
				startActivityForResult(new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT);
			}
		});

		relativelayoutBack.setOnClickListener(new OnClickListener() {

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
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				finish();
			}
		});

	}
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			try {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(getCurrentFocus()

				.getWindowToken(), 0);

			} catch (Exception exception) {

				exception.printStackTrace();

			}
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			finish();
		}
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
		case (PICK_CONTACT):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData();
				Cursor c = getContentResolver().query(contactData, null, null,
						null, null);
				if (c.moveToFirst()) {
					@SuppressWarnings("unused")
					String nameString = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// TODO Whatever you want to do with the selected contact
					// name.
					retrieveContactName(contactData);
					retrieveContactNumber(contactData);
					retrieveNameEmailDetails();
				}
			}
			break;
		}
	}

//	private Bitmap retrieveContactPhoto() {
//
//		Bitmap contactImage = null;
//
//		InputStream inputStream = ContactsContract.Contacts
//				.openContactPhotoInputStream(getContentResolver(), ContentUris
//						.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
//
//						new Long(contactIDString)));
//
////		if (inputStream == null) {
////
////			contactImage = BitmapFactory.decodeResource(getResources(),
////					R.drawable.user);
////
////			ByteArrayOutputStream stream = new ByteArrayOutputStream();
////
////			contactImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
////
////			byte[] imageInByte = stream.toByteArray();
////
////			Data.imageOfContact = imageInByte;
////
////			Log.e("Images+", "" + imageInByte);
////
////			Data.boolContactImage = 1;
////
////		} else {
////
////			contactImage = BitmapFactory.decodeStream(inputStream);
////
////			ByteArrayOutputStream stream = new ByteArrayOutputStream();
////
////			contactImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
////
////			Log.e("LOG_Entry4", "LOG_Entry4");
////
////			byte[] imageInByte = stream.toByteArray();
////
////			Log.e("LOG_Entry5", "LOG_Entry5");
////
////			Data.imageOfContact = imageInByte;
////
////			Log.e("Images+", "" + imageInByte);
////
////			Data.boolContactImage = 0;
////
////		}
//
//		assert inputStream != null;
//
//		return contactImage;
//
//	}

	public String retrieveNameEmailDetails() {

		String emailString = "";

		Cursor emailCur = getContentResolver().query(

		ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,

		ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",

		new String[] { contactIDString }, null);

		while (emailCur.moveToNext()) {

			emailString = emailCur
					.getString(emailCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

		}
		Log.d("email: ", "===>" + emailString);
		edittextEmail.setText(emailString);
		
		emailCur.close();

		return emailString;

	}

	private String retrieveContactNumber(Uri uriContact) {

		Cursor cursorPhone;

		String contactNoString = "";

		Cursor cursorID = getContentResolver().query(uriContact,

		new String[] { ContactsContract.Contacts._ID }, null, null,

		null);

		if (cursorID.moveToFirst()) {

			contactIDString = cursorID.getString(cursorID
					.getColumnIndex(ContactsContract.Contacts._ID));

		}

		cursorID.close();

		cursorPhone = getContentResolver().query(

		ContactsContract.CommonDataKinds.Phone.CONTENT_URI,

		new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },

		ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "

		+ ContactsContract.CommonDataKinds.Phone.TYPE + " = "

		+ ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

		new String[] { contactIDString }, null);

		if (cursorPhone.moveToFirst()) {

			contactNoString = cursorPhone

			.getString(cursorPhone

			.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

		}

		Log.d("contactno: ", "===>" + contactNoString);
		edittextPhoneNumber.setText(contactNoString);
		cursorPhone.close();
		

		return contactNoString;

	}

	private String retrieveContactName(Uri uriContact) {

		Cursor cursor = getContentResolver().query(uriContact, null, null,

		null, null);

		String nameString = "", firstnameString="";

		if (cursor.moveToFirst()) {

			nameString = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			
			firstnameString= cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE));
			
			Log.d("name ", "===>" + nameString+","+firstnameString+",");
			
			if(firstnameString.contains(", "))
			{
			String[] s=firstnameString.split(", ");			
			Log.d("firstname ", "===>" +s[0]+","+s[1]);
			
			edittextFirstName.setText(s[1]);
			edittextLastName.setText(s[0]);
			}
			else{
				edittextFirstName.setText(nameString);
			}

		}

		cursor.close();

		return nameString;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}
	
	public void addUsers() {

		 Prefrences.showLoadingDialog(AddUser.this, "Loading...");		
		 
		RequestParams params = new RequestParams();
		Log.d("email", "email"+edittextEmail.getText().toString()+","+edittextPhoneNumber.getText().toString()+","+edittextCompanyName.getText().toString());
		params.put("id", Prefrences.selectedProId);
		params.put("user_id", Prefrences.userId);
		
		params.put("user[email]",edittextEmail.getText().toString());
		params.put("user[phone]", edittextPhoneNumber.getText().toString());
		params.put("user[first_name]",edittextFirstName.getText().toString());
		params.put("user[last_name]", edittextLastName.getText().toString());
//		params.put("user[company_id]", txt_companyName.getText().toString());
		params.put("user[company_name]", edittextCompanyName.getText().toString());

						
			
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		
		Log.v("URL","URL"+Prefrences.url +"/projects/"+Prefrences.selectedProId+"/add_user");
		Log.d("params","params"+params.toString());
		
		client.post(Prefrences.url +"/projects/"+Prefrences.selectedProId+"/add_user" ,params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ---- ",
									"---*****----" + res.toString(2));
							
							 JSONObject connectUser =
									 res.getJSONObject("user");
									
							 		String emailaddress = connectUser.getString("email");
									 String fullname = connectUser.getString("full_name");
									 
									 String id = connectUser.getString("id");
									 if(fullname.equalsIgnoreCase("")||fullname.equalsIgnoreCase(" "))
										{
											fullname=emailaddress;
										}
							Prefrences.assigneeID=id;
							Prefrences.assignee_str=fullname;
							Log.d("id","fullname"+id+","+fullname);
							
							WorkItemClick.btnS_assigned.setText(Prefrences.assignee_str);
							overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
							finish();
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						// WorkListCompleteAdapter.punchlst_item(id_punchlist_item,
						// WorkListCompleteAdapter.data, pos);
						
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Toast.makeText(AddUser.this, "Server Issue",
								Toast.LENGTH_LONG).show();

						Log.e("request fail", arg0.toString());
						 Prefrences.dismissLoadingDialog();
					}
				});	
	}

	class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			try {

				adduserss();

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
			 Prefrences.showLoadingDialog(AddUser.this, "Loading...");
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

	}
	
	public void adduserss() throws ParseException, IOException,

	XmlPullParserException {


		HttpClient httpclient = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(Prefrences.url

				+ "projects/"+Prefrences.selectedProId+"/add_user");

		// MultipartEntity mpEntity = new MultipartEntity();

		String BOUNDARY = "--buildhawk--";

		MultipartEntity mpEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY,
				Charset.defaultCharset());

//		httppost.addHeader("Accept-Encoding", "gzip, deflate");

		// httppost.setHeader("Accept", "image/jpg");

		httppost.setHeader("Accept", "application/json");

//		httppost.setHeader("Content-Type", "multipart/form-data; boundary="
//
//		+ BOUNDARY);
//
//		Log.v("picturePath", picturePath);
//
//		File file = new File(picturePath);
//
//		FileBody cbFile = new FileBody(file, "image/jpg");
//
//		cbFile.getMediaType();
//		Log.i("photo[report_id]",reportID);
////				reportdata_local.get(myPager.getCurrentItem()).report_id.toString());
//		Log.i("photo[mobile]", "1");
//		Log.i("photo[company_id]", Prefrences.companyId);
//		Log.i("photo[source]", "Reports");
//		Log.i("photo[user_id]", Prefrences.userId);
//		Log.i("photo[project_id]", Prefrences.selectedProId);
//		Log.i("photo[name]", "android");
//		Log.i("photo[image]", "" + cbFile);
//
//		mpEntity.addPart(
//				"photo[report_id]",
//				new StringBody(
//						reportdata_local.get(myPager.getCurrentItem()).report_id
//								.toString()));
//
//		mpEntity.addPart("photo[mobile]", new StringBody("1"));
//
//		mpEntity.addPart("photo[company_id]", new StringBody(
//				Prefrences.companyId));
//
//		mpEntity.addPart("photo[source]", new StringBody("Reports"));

		mpEntity.addPart("user_id", new StringBody(Prefrences.userId));
		mpEntity.addPart("id", new StringBody(Prefrences.selectedProId));
		mpEntity.addPart("user[email]",new StringBody(edittextEmail.getText().toString()));
		
		mpEntity.addPart("user[phone]", new StringBody(edittextPhoneNumber.getText().toString()));
		mpEntity.addPart("user[company_name]", new StringBody(edittextCompanyName.getText().toString()));
//
//		mpEntity.addPart("photo[project_id]", new StringBody(
//				Prefrences.selectedProId));
//
//		mpEntity.addPart("photo[name]", new StringBody("android"));
//
//		mpEntity.addPart("photo[image]", cbFile);

		httppost.setEntity(mpEntity);

		HttpResponse response = httpclient.execute(httppost);

		Log.v("response", response.getStatusLine().toString() + "");

		Log.v("res", "," + response);

		HttpEntity entity = response.getEntity();

		String responseString = EntityUtils.toString(entity, "UTF-8");

		Log.v("res", "," + responseString);

		System.out.println(responseString);

	
	}
	
}
