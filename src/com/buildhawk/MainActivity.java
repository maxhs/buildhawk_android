package com.buildhawk;

/*
 *  This file is used to get the user id and password and device token to login.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	Button buttonLogin;
	private String regIdString;
	SharedPreferences pref;
	EditText edittextEmail, edittextPass;
	ConnectionDetector connDect;
	Boolean isInternetPresentBoolean = false;
	RelativeLayout relativelayoutRoot;
//	ScrollView scrollview;
	ImageView imageview;
	SharedPreferences sharedpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		relativelayoutRoot = (RelativeLayout) findViewById(R.id.relativeLayoutRoot);
		new ASSL(this, relativelayoutRoot, 1134, 720, false);

		edittextEmail = (EditText) findViewById(R.id.edittextEmail);
		edittextPass = (EditText) findViewById(R.id.edittextPass);

		buttonLogin = (Button) findViewById(R.id.buttonLogin);
//		scrollview = (ScrollView) findViewById(R.id.scrollView1);
		imageview = (ImageView) findViewById(R.id.imageView1);

		edittextEmail.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		edittextPass.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));

		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresentBoolean = ConnectionDetector.isConnectingToInternet();
		sharedpref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		

		try {
			registerWithGCM();
		} catch (Exception e) {
			Log.e("error", "" + e.toString());
		}

		edittextPass.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// img.setVisibility(View.INVISIBLE);
//				scrollview.fullScroll(ScrollView.FOCUS_DOWN);

				return false;
			}
		});

		edittextEmail.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// img.setVisibility(View.INVISIBLE);
//				scrollview.fullScroll(ScrollView.FOCUS_DOWN);

				return false;
			}
		});

		buttonLogin.setOnClickListener(new OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				try {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(getCurrentFocus()

					.getWindowToken(), 0);

				} catch (Exception exception) {

					exception.printStackTrace();

				}
				if (isInternetPresentBoolean) {
					sessionData();

				} else {
					Toast.makeText(getApplicationContext(),
							"No internet connection.", Toast.LENGTH_SHORT);
				}

			}
		});
	}

	// ******* getting device token for push notification. **************//
	private void registerWithGCM() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		regIdString = GCMRegistrar.getRegistrationId(this);
		if (regIdString.equals("")) {
			GCMRegistrar.register(this, "149110570482"); // Note: get the sender 149110570482
			Log.v("reg id new ", "" + regIdString); // id from
			// configuration.
		} else {
			// Data.device_tok = regId;
			Log.v("Registration", "Already registered, regId: " + regIdString);
		}
	}

	// ************ API for Login. *************//

	public void sessionData() {

		Prefrences.showLoadingDialog(MainActivity.this, "Loading...");
		Log.v("Send...", "" + edittextEmail.getText().toString() + ", " + edittextPass.getText().toString() + ", "+ regIdString);
		RequestParams params = new RequestParams();

		params.put("email", edittextEmail.getText().toString());
		params.put("password", edittextPass.getText().toString());
		params.put("device_token", regIdString);
		Log.d("reg id HITTED ", "" + regIdString);
		params.put("device_type", "3");

		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		client.setTimeout(1000000);
		client.post(Prefrences.url + "/sessions", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);

						JSONObject res;
						try {
							Editor editor =  sharedpref.edit();
							editor.putString("userData", response);
							editor.putString("lat", ""+Prefrences.currentLatitude);
							editor.putString("longi", ""+Prefrences.currentLongitude);
							res = new JSONObject(response);
							JSONObject user = res.getJSONObject("user");
							Prefrences.userId = user.getString("id");
							Prefrences.firstName = user.getString("first_name");
							Prefrences.lastName = user.getString("last_name");
							Prefrences.fullName = user.getString("full_name");
							Prefrences.email = user.getString("email");
							Prefrences.phoneNumber = user
									.getString("formatted_phone");
							Prefrences.authToken = user
									.getString("authentication_token");
							
							editor.putString("authentication_token", Prefrences.authToken);
							editor.commit();
							Log.i("Response", "," + Prefrences.userId + ", "
									+ Prefrences.firstName + ", "
									+ Prefrences.lastName + ", "
									+ Prefrences.fullName + ", "
									+ Prefrences.email + ", "
									+ Prefrences.phoneNumber + ", "
									+ Prefrences.authToken);

							JSONArray coworkers = user
									.getJSONArray("coworkers");
							Log.v("coworkers", "," + coworkers.length());

							Prefrences.coworkrName = new String[coworkers
									.length()];
							Prefrences.coworkrEmail = new String[coworkers
									.length()];
							Prefrences.coworkrForPhone = new String[coworkers
									.length()];
							Prefrences.coworkrPhone = new String[coworkers
									.length()];
							Prefrences.coworkrId = new String[coworkers
									.length()];
							Prefrences.coworkrUrl = new String[coworkers
									.length()];

							for (int i = 0; i < coworkers.length(); i++) {
								JSONObject count = coworkers.getJSONObject(i);
								Prefrences.coworkrName[i] = count
										.getString("full_name");
								Log.d("jijijijii", "coworker"
										+ Prefrences.coworkrName[i].toString());
								Prefrences.coworkrEmail[i] = count
										.getString("email");
								Prefrences.coworkrForPhone[i] = count
										.getString("formatted_phone");
								Prefrences.coworkrPhone[i] = count
										.getString("phone");
								Prefrences.coworkrId[i] = count.getString("id");
								Prefrences.coworkrUrl[i] = count
										.getString("url_thumb");
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						Prefrences.dismissLoadingDialog();
						Prefrences.saveAccessToken(Prefrences.userId,
								edittextEmail.getText().toString(), edittextPass.getText().toString(),
								regIdString,getApplicationContext());

						startActivity(new Intent(MainActivity.this,
								Homepage.class));
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Toast.makeText(getApplicationContext(),
								"Not a valid user", Toast.LENGTH_LONG).show();
						Prefrences.dismissLoadingDialog();
					}
				});

	}

}
