package com.buildhawk;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ProjectLoadService extends Service {
	SharedPreferences sharedpref;

	public ProjectLoadService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		Log.e("Service Created", "");
		sharedpref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Perform your long running operations here.
		Log.e("Service Started", "efe");
		 //  Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();//toast
		projectData();
	}
	
	

	@Override
	public void onDestroy() {
		Log.e("Service Destroyed", "fefe");
	}
	
	

	
	
		public void projectData() {

		
			RequestParams params = new RequestParams();

			params.put("user_id", Prefrences.userId);

			
			AsyncHttpClient client = new AsyncHttpClient();

			client.addHeader("Content-type", "application/json");
			client.addHeader("Accept", "application/json");

			client.get(Prefrences.url + "/projects", params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(String response) {
							Log.i("request succesfull", "response = " + response);
							
							Editor editor =  sharedpref.edit();
							editor.putString("userData", response);
							editor.putString("lat", ""+Prefrences.currentLatitude);
							editor.putString("longi", ""+Prefrences.currentLongitude);
							editor.commit();
							stopSelf();
						}

						@Override
						public void onFailure(Throwable arg0) {
							Log.e("request fail", arg0.toString());
							Prefrences.dismissLoadingDialog();
						}
					});

		}
		


  }
