package com.buildhawk;



import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	

	public MyService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		Log.e("Service Created", "");

	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Perform your long running operations here.
		Log.e("Service Started", "efe");
		 //  Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();//toast
			projectDetail();
	}
	
	

	@Override
	public void onDestroy() {
		Log.e("Service Destroyed", "fefe");
	}
	
	

	
	
	// ************ API for Project Details. *************//

	public void projectDetail() {

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
						
						try {
							Prefrences.checklist_s = response;
							Prefrences.checklist_bool=true;
							stopSelf();
						} catch (Exception e) {
							Log.e("ServiceException", e.toString());
						}
						
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("Service request fail", arg0.toString());
						stopSelf();
					}
				});

	}


  }