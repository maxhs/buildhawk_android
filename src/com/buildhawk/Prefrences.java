package com.buildhawk;

import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.buildhawk.utils.CommentsChecklistItem;
import com.buildhawk.utils.PhotosCheckListItem;

public class Prefrences {
	

	
	//    public static final String flurryKey = "DK448W5SYC23TFWQM8F8";
	private static ProgressDialog progressDial;
	public static String userId = "";
	public static String firstName = "";
	public static String lastName = "";
	public static String fullName = "";
	public static String userPic = "";
	public static String email = "";
	public static String phoneNumber = "";
	public static String authToken = "";
	public static Activity act;
	public static int clear = 0;
	public static SharedPreferences userDetail;
	public static Editor editor;
	public static String coworkrName[];
	public static String coworkrEmail[];
	public static String coworkrForPhone[];
	public static String coworkrPhone[];
	public static String coworkrId[];
	public static String coworkrUrl[];
	public static int text = 0;
	public static String projectId[];
	public static String projectName[];
	public static String projectActive[];
	public static String projectCore[];

	public static String projectAddStreet1[];
	public static String projectAddStreet2[];
	public static String projectAddCity[];
	public static String projectAddZip[];
	public static String projectAddCountry[];
	public static String projectAddPhone[];
	public static String projectAddForAdd[];
	// public static String projectAddLat[];
	// public static String projectAddLong[];
	public static boolean CheckItemClickFlag=false;

	public static String ContactMail;
	public static String ContactPhone;
	public static String ContactName;

	public static String projectComId[];
	public static String projectComName[];
	public static String selectedProId;
	public static String selectedProName;
	public static String selectedProAddress;
	public static String projectUsersId[];

	public static int deletePicFlag;
	public static String selectedlocation;
	public static int selectedPic;
	public static int selectedCheckitemSynopsis;
	public static int stopingHit;
	public static int well;
	public static int reportTypeDialog;
	public static int reportType;
	public static String lati, longi;
//	public static int posViewpager;

	public static String selecteddate = "";
	public static String personelName;
	public static String personelHours;
	public static int sizeofname = 0;
	public static int resumeflag;
	public static String currentLatitude="0", currentLongitude="0";
	public static String url = "https://www.buildhawk.com/api/v2";

	static String companyId;
	static String reportID;
	static int pageFlag = 1;
//	public static String assigneename="";
	public static String assigneeID="";

	
	public static int checklistHitStop=0;
	public static int checklisttypes=0;
	public static int reportlisttypes=0;
	public static int worklisttypes=0;
	
	public static String selected_location;
	
	public static ArrayList<PhotosCheckListItem> pho = new ArrayList<PhotosCheckListItem>();
	public static ArrayList<CommentsChecklistItem> comm = new ArrayList<CommentsChecklistItem>();

	static Typeface helveticaNeueLt;
	static Typeface helveticaNeueBd;
	
	public static boolean checklist_bool=false;
	public static boolean document_bool=false;
	public static boolean report_bool=false;
	public static boolean worklist_bool=false;
	
	public static String checklist_s="";
	public static String document_s="";
	public static String report_s="";
	public static String worklist_s="";
	public static int statusCompleted=0;
	public static String report_body_edited="";
	public static int ReportPosition;
	
	public static String removeCompanyID;
	public static String removeUserID;
	public static String removeReportID;
	public static String removeSafetyID;
	public static int removeFlag=0;					
	
	public static String assignee_str="";
	public static String location_str="";
//	public static int searchFlag=0;
	public static String selectedCompnyId="";
	public static String selectedCompanyName="";
	
	public static String notificationID="";
	public static String worklistNotification="";
	public static String checklistNotification="";
	public static String reportNotification="";
	public static String projectNotification="";
	public static String userNotification="";
	
	public static int comingFrom=0;
	
	public static String LastSelectedProId="";
	public static String LastDocument_s="";
	public static String LastReport_s = "";
	public static  String LastChecklist_s = "";
	public static  String LastWorklist_s = "";
	
	public static int PrefillFlag=0;
	
	

	public static Typeface helveticaNeuelt(Context appContext) { // accessing
																	// fonts
																	// functions
		if (helveticaNeueLt == null) {
			helveticaNeueLt = Typeface.createFromAsset(appContext.getAssets(),
					"HelveticaNeueLt.ttf");
		}
		return helveticaNeueLt;
	}

	public static Typeface helveticaNeuebd(Context appContext) { // accessing
																	// fonts
																	// functions
		if (helveticaNeueBd == null) {
			helveticaNeueBd = Typeface.createFromAsset(appContext.getAssets(),
					"HelveticaNeueBd.ttf");
		}
		return helveticaNeueBd;
	}

	public static void saveDateDaily(String token, Context context, String type, String projectId) {
		SharedPreferences prefDaily = PreferenceManager
				.getDefaultSharedPreferences(context);
	//	type="Daily";
		Editor editor = prefDaily.edit();
		editor.putString("accessDateDaily", token);
		editor.putString("typeDaily", type);
		editor.putString("projectId", projectId);
		editor.commit();
	}

	public static String getDateDaily(Context context) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("accessDateDaily", "");

	}
	public static String getTypeDaily(Context context) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("typeDaily", "");

	}
	public static String getProjectId(Context context) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("projectId", "");

	}

	public static void saveDateSafety(String token, Context context, String type, String projectId) {
		SharedPreferences prefSafety = PreferenceManager
				.getDefaultSharedPreferences(context);
//		type="Safety";
		Editor editor = prefSafety.edit();
		editor.putString("accessDateSafety", token);
		editor.putString("typeSafety", type);
		editor.putString("projectId", projectId);
		editor.commit();
	}

	public static String getDateSafety(Context context) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("accessDateSafety", "");

	}

	public static String getTypeSafety(Context context) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("typeSafety", "");

	}
	
	public static void saveDateWeekly(String token, Context context, String type, String projectId) {
		SharedPreferences prefSafety = PreferenceManager
				.getDefaultSharedPreferences(context);
//		type="Safety";
		Editor editor = prefSafety.edit();
		editor.putString("accessDateWeekly", token);
		editor.putString("typeWeekly", type);
		editor.putString("projectId", projectId);
		editor.commit();
	}

	public static String getDateWeekly(Context context) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("accessDateWeekly", "");

	}

	public static String getTypeWeekly(Context context) {

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("typeSafetyWeekly", "");

	}
	
	public static void saveAccessToken(String userID, String email,
			String password, String regId, Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.putString("userId", userID);
		editor.putString("email", email);
		editor.putString("password", password);
		editor.putString("regId", regId);
		editor.commit();
	}

	public static String getAccessToken(Context context) {
		if (Prefrences.userId.isEmpty()) {
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(context);
			return pref.getString("userId", "");
		}
		return Prefrences.userId;
	}

	public static String[] getCredential(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return new String[] { pref.getString("email", ""),
				pref.getString("password", ""),
				pref.getString("regId", "")};
	}

	/**
	 * Displays custom loading dialog
	 * 
	 * @param contxt
	 *            application context
	 * @param msg
	 *            string message to show in dialog
	 */

	public static void showLoadingDialog(Context contxt, String msg) {
		progressDial = new ProgressDialog(contxt,
				android.R.style.Theme_Translucent_NoTitleBar);
		// pd_st.getWindow().getAttributes().windowAnimations =
		// R.style.Animations_LoadingDialogFade;
		progressDial.show();
		WindowManager.LayoutParams layoutParams = progressDial.getWindow()
				.getAttributes();
		layoutParams.dimAmount = 0.6f;
		progressDial.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		progressDial.setCancelable(false);
		progressDial.setContentView(R.layout.loading_box);
		FrameLayout framLay = (FrameLayout) progressDial.findViewById(R.id.rv);
		// new ASSL((Activity)c, rv, 1134, 720, false);
		TextView dialogTxt = (TextView) progressDial.findViewById(R.id.tv101);
		dialogTxt.setText(msg);
	}

	/**
	 * 
	 * Dismisses above loading dialog
	 */

	public static void dismissLoadingDialog() {
		if (progressDial != null) {
			progressDial.dismiss();
			progressDial = null;
		}

	}

}
