package com.buildhawk;

/*
 *  Push notification.
 * 
 */
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	static Intent notificationIntent;
	static String notificationMessage;

	String className;

	static Context context;

	protected void onError(Context arg0, String arg1) {
		Log.e("Registration", "Got an error1!");
		Log.e("Registration", arg1.toString());
	}

	protected boolean onRecoverableError(Context context, String errorId) {
		Log.d("onRecoverableError", errorId);
		return false;
	}

	protected void onMessage(Context context, Intent arg1) {
		try {
			Log.e("Recieved a message...", "," + arg1.getExtras());
			GCMIntentService.context = context;
			Log.i("values",
					"worklist id" + arg1.getStringExtra("worklist_item_id")
							+ ": checklist id"
							+ arg1.getStringExtra("checklist_item_id")
							+ "report id" + arg1.getStringExtra("report_id")
							+ "photo id: " + arg1.getStringExtra("photo_id")
							+ "message id:" + arg1.getStringExtra("message_id")
							+ "project id: "
							+ arg1.getStringExtra("project_id")
							+ arg1.getStringExtra("user_id"));
			Log.v("unread.", "," + arg1.getStringExtra("unread_messages"));
			notificationMessage = arg1.getStringExtra("unread_messages") + ": "
					+ arg1.getStringExtra("message");

			if (arg1.getStringExtra("project_id") != null) {
				Log.v("project_id", "project_id");
				Prefrences.projectNotification = arg1
						.getStringExtra("project_id");
				className = "com.buildhawk.ProjectDetail";
				Prefrences.selectedProId = Prefrences.projectNotification;
				Prefrences.notificationID = Prefrences.projectNotification;
			}
			
			Log.v("projectid", "projectid");
			if (arg1.getStringExtra("checklist_item_id") != null) {
				Log.v("checklist_item_id", "checklist_item_id");
				Prefrences.checklistNotification = arg1
						.getStringExtra("checklist_item_id");
				className = "com.buildhawk.CheckItemClick";
				Prefrences.notificationID = Prefrences.checklistNotification;

			} else if (arg1.getStringExtra("worklist_item_id") != null) {
				Log.v("worklist_item_id", "worklist_item_id");
				Prefrences.worklistNotification = arg1
						.getStringExtra("worklist_item_id");
				className = "com.buildhawk.WorkItemClick";
				Prefrences.notificationID = Prefrences.worklistNotification;

			} else if (arg1.getStringExtra("report_id") != null) {
				Log.v("report_id", "report_id");
				Prefrences.reportNotification = arg1
						.getStringExtra("report_id");
				className = "com.buildhawk.ReportItemClick";
				Prefrences.notificationID = Prefrences.reportNotification;
			} else {
				
				Log.v("else", "else");
				className = "com.buildhawk.BuildHawk";

			}
			Log.v("9876789", "9876789");

			notificationManager(context, arg1.getExtras().getString("message"));

			//
			Log.e("no exception", ",no exception");

		} catch (Exception e) {
			Log.e("Recieved exception message arg1...", "," + arg1);
			Log.e("exception", "," + e);
		}

	}

	@SuppressWarnings("deprecation")
	private void notificationManager(Context context, String message) {

		long when = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Log.v("message", "," + message);

		// notificationIntent = new Intent(context, BuildHawk.class);
		// Log.v("notification_message", "," + notificationMessage);

		Notification notification = new Notification(R.drawable.default_200,
				notificationMessage, when);
		String title = "Build Hawk";

		// // set intent so it does not start a new activity
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//
		// PendingIntent intent = PendingIntent.getActivity(context, 0,
		// notificationIntent, 0);
		//
		// notification.setLatestEventInfo(context, title, notificationMessage,
		// intent);
		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// notificationManager.notify(0, notification);

		// if(!Prefrences.notificationID.equalsIgnoreCase(""))
		// {
		// Log.d("notification id :",","+Prefrences.notificationID);

		try {
			notificationIntent = new Intent(this, Class.forName(className));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// notificationIntent = new Intent(context, Profile.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, title, notificationMessage,
				intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}

	// }

	protected void onRegistered(Context arg0, String arg1) {
		Log.e("Registration", "!");
		Log.e("Registration", arg1.toString());
		// Data.deviceToken = arg1.toString();
	}

	protected void onUnregistered(Context arg0, String arg1) {
		Log.e("Registration", "Got an error4!");
		Log.e("Registration", arg1.toString());
	}

}
