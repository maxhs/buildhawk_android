package com.buildhawk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	Button login;
	private String regId;
	SharedPreferences pref;
	EditText email, pass;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	RelativeLayout relLay;
	ScrollView scroll;
	ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		relLay = (RelativeLayout) findViewById(R.id.rellay);
		new ASSL(this, relLay, 1134, 720, false);

		email = (EditText) findViewById(R.id.email);
		pass = (EditText) findViewById(R.id.password);

		login = (Button) findViewById(R.id.login);
		scroll = (ScrollView) findViewById(R.id.scrollView1);
		img = (ImageView) findViewById(R.id.imageView1);

		email.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));
		pass.setTypeface(Prefrences.helveticaNeuelt(getApplicationContext()));

		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresent = connDect.isConnectingToInternet();

		try {
			registerWithGCM();
		} catch (Exception e) {
			Log.e("error", "" + e.toString());
		}

		pass.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// img.setVisibility(View.INVISIBLE);
				scroll.fullScroll(ScrollView.FOCUS_DOWN);

				return false;
			}
		});

		email.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// img.setVisibility(View.INVISIBLE);
				scroll.fullScroll(ScrollView.FOCUS_DOWN);

				return false;
			}
		});

		login.setOnClickListener(new OnClickListener() {
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
				if (isInternetPresent) {
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
		regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, "454876423564"); // Note: get the sender
			Log.v("reg id ", "" + regId); // id from
			// configuration.
		} else {
			// Data.device_tok = regId;
			Log.v("Registration", "Already registered, regId: " + regId);
		}
	}

	// ************ API for Login. *************//

	public void sessionData() {

		Prefrences.showLoadingDialog(MainActivity.this, "Loading...");

		RequestParams params = new RequestParams();

		params.put("email", email.getText().toString());
		params.put("password", pass.getText().toString());
		params.put("device_token", regId);

		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		client.setTimeout(100000);
		client.post(Prefrences.url + "/sessions", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);

						JSONObject res;
						try {
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
								"test@ristrettolabs.com", "password",
								getApplicationContext());

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
