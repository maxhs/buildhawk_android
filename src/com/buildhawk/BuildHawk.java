package com.buildhawk;

/*
 *  Its a SplashScreen and in background it is fetching location and device token.
 * 
 */
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gcm.GCMRegistrar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class BuildHawk extends Activity {
	Handler handler;
	public static String regIdString, emailString, passwordString;
	RelativeLayout relativeLayoutRoot;
	ConnectionDetector connDect;
	Boolean isInternetPresent = false;
	// LocationManager locationManager = null;
	// Boolean network_enabled, attempt = false;
	ProgressDialog progressdialog;
	Timer timer;
	int counter = 0;
	double longi = 0, lati = 0;
	SharedPreferences sharedpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.splash);
		relativeLayoutRoot = (RelativeLayout) findViewById(R.id.relativeLayoutRoot);
		new ASSL(this, relativeLayoutRoot, 1134, 720, false);
		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresent = ConnectionDetector.isConnectingToInternet();
		Log.e("BuildHawk", "msg");
		
		
		sharedpref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		
	
		
		try {
			if (isInternetPresent) {
				registerWithGCM();
			} else {
				String id_val = sharedpref.getString("regId", "");
				if(id_val.equalsIgnoreCase("")){
					Log.e("noid", "noid");
					Toast.makeText(getApplicationContext(),"No internet connection1.", Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			Log.e("error", "" + e.toString());
		}

		emailString = Prefrences.getCredential(getApplicationContext())[0];
		passwordString = Prefrences.getCredential(getApplicationContext())[1];
		regIdString = Prefrences.getCredential(getApplicationContext())[2];
		
		Log.v("saved...", "" + emailString + ", " + passwordString + ", "+ regIdString);

		if (Prefrences.getAccessToken(getApplicationContext()).isEmpty()) {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
//					GPSTracker gps = new GPSTracker(BuildHawk.this);
					// To check whether current location is got through network
					// or wifi
//					if (gps.isLocationEnabled(BuildHawk.this)) {
//						gps.start(BuildHawk.this);
//						Prefrences.currentLatitude = gps.getLatitude();
//						Prefrences.currentLongitude = gps.getLongitude();
//						Log.e("lat", ",RVB, " + gps.getLatitude());
						
						startActivity(new Intent(BuildHawk.this,
								MainActivity.class));
						finish();
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						// findLoc = new FinderClass();
//					}
//					// Ask user to turn on gps
//					else {
//						buildAlertMessageNoGps();
//					}
					// finish();
					// startActivity(new Intent(BuildHawk.this,
					// MainActivity.class));
				}
			}, 1000);
		} else {
		
			if (isInternetPresent) {
//				GPSTracker gps = new GPSTracker(BuildHawk.this);
				// To check whether current location is got through network or
				// wifi
//				if (gps.isLocationEnabled(BuildHawk.this)) {
//					gps.start(BuildHawk.this);
////					Prefrences.currentLatitude = gps.getLatitude();
////					Prefrences.currentLongitude = gps.getLongitude();
//					Log.e("lat", ",, " + gps.getLatitude());
//					// findLoc = new FinderClass();
//				}
//				// Ask user to turn on gps
//				else {
//					buildAlertMessageNoGps();
//				}
				
					sessionData();
				
			} else {
				String response = sharedpref.getString("userData", "");
				Log.e("noid", ".. "+response);
				if(response.equalsIgnoreCase("")){
					Log.e("noid", ".. "+response);
					Toast.makeText(getApplicationContext(),	"No internet connection.", Toast.LENGTH_SHORT).show();
				}else{
//					Prefrences.currentLatitude = Double.parseDouble(sharedpref.getString("lat", "0.0"));
//					Prefrences.currentLongitude = Double.parseDouble(sharedpref.getString("longi", "0.0"));
					fillServerData(response);
				}
			}

		}
		
	}
	
//	 @Override
//	    protected void onStart() {
//
//	        super.onStart();
//	        FlurryAgent.onStartSession(BuildHawk.this, Prefrences.flurryKey); 
//	        FlurryAgent.onEvent("Splash screen started");
//	    }
//
//	    @Override
//	    protected void onStop() {
//	        super.onStop();
//	        FlurryAgent.onEndSession(this);
//	        FlurryAgent.endTimedEvent("Article_Read");
//	    }
	
	

	// ******* getting device token for push notification. **************//
	private void registerWithGCM() {
		//GCMRegistrar.checkDevice(this);
		//GCMRegistrar.checkManifest(this);
		regIdString = GCMRegistrar.getRegistrationId(this);
		if (regIdString.equals("")) {
			GCMRegistrar.register(this, "149110570482"); // Note: get the sender 149110570482   //981259510888
			Log.v("reg id1 ", "" + regIdString); // id from 
			// configuration.
			Editor editor =  sharedpref.edit();
			editor.putString("New regId", regIdString);
			editor.commit();
			
		} else {
			// Data.device_tok = regId;
			Log.v("reg id2 ", "" + regIdString); 
			Editor editor =  sharedpref.edit();
			editor.putString("regId", regIdString);
			editor.commit();
			Log.v("Registration", "Already registered, regId: " + regIdString);
		}
	}

	// ************ API for Login. *************//

	public void sessionData() {

		RequestParams params = new RequestParams();

		params.put("email", emailString);
		params.put("password", passwordString); // password);
		params.put("device_token", regIdString);
		Log.d("reg id HITTTED ", "" + regIdString);
		params.put("device_type", "3");
		
		Editor editor =  sharedpref.edit();
		editor.putString("regId", regIdString);
		editor.commit();
		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");
		client.setTimeout(100000);
		client.post(Prefrences.url + "/sessions", params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						Log.i("request succesfull", "response = " + response);
						fillServerData(response);
						
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						finish();
					}
				});

	}

	/**
	 * 
	 * buildAlertMessageNoGps() is used ask the user to turn on gps
	 */

//	private void buildAlertMessageNoGps() {
//
//		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//		builder.setTitle("Location services disabled")
//
//				.setMessage(
//
//				"BuildHawk needs access to your location, Please turn on your location access.")
//
//				.setCancelable(false)
//
//				.setPositiveButton("Settings",
//
//				new DialogInterface.OnClickListener() {
//
//					public void onClick(final DialogInterface dialog,
//
//					int intValue) {
//
//						startActivity(new Intent(
//
//								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//
//						overridePendingTransition(R.anim.slide_in_left,
//
//						R.anim.slide_out_left);
//
//						finish();
//
//						// setting_page = true;
//
//					}
//
//				})
//
//				.setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//					public void onClick(final DialogInterface dialog,
//
//					int intValue) {
//
//						dialog.cancel();
//
//						finish();
//
//					}
//
//				});
//
//		final AlertDialog alert = builder.create();
//
//		alert.show();
//
//	}
	
	public void fillServerData(String response){
		JSONObject res;
		try {
			res = new JSONObject(response);
			Editor editor =  sharedpref.edit();
			editor.putString("userData", response);
			editor.putString("lat", ""+Prefrences.currentLatitude);
			editor.putString("longi", ""+Prefrences.currentLongitude);
			
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
			Prefrences.userPic = user.getString("url_small");
			Log.i("Response", "," + Prefrences.userId + ", "
					+ Prefrences.firstName + ", "
					+ Prefrences.lastName + ", "
					+ Prefrences.fullName + ", "
					+ Prefrences.email + ", "
					+ Prefrences.phoneNumber + ", "
					+ Prefrences.authToken + ", "
					+ Prefrences.userPic);

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
			
			
			startActivity(new Intent(BuildHawk.this, Homepage.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
}
