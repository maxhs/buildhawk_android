package com.buildhawk;

import java.util.ArrayList;

import org.json.JSONObject;
import org.w3c.dom.Text;

import rmn.androidscreenlibrary.ASSL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.buildhawk.R.drawable;
import com.buildhawk.utils.SynopsisRecentDocuments;
import com.handmark.pulltorefresh.library.internal.FlipLoadingLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

public class SelectedImageView extends Activity {
	ViewFlipper viewFlipper;
	SwipeDetector swipe;
	
	Bitmap bitmap;
	// static int x;
	
	private ZoomView zoomView;
	ArrayList<String> workuser, checkuser, reportuser, docuser, allusers,
			docdate, workdate, checkdate, reportdate, alldates, docphase,
			checklistphase, worklistphase, reportphase, allphase;

	ArrayList<String> pic;
	ArrayList<String> ids;
	ArrayList<String> desc;
	ArrayList<String> type;
	ImageView imageviewDeletePic;
	Button button;
//	ImageView photo;
	int position, finalposition;
	// int r;
	String finalIdString;
	String idGetString, keyString;
//	TextView textviewDescription;
	RelativeLayout relativeLayoutBack;
	TextView textviewNum, textviewClickedOn;
	RelativeLayout relativelayoutRoot;
	// ArrayList<SynopsisRecentDocuments>pic =new
	// ArrayList<SynopsisRecentDocuments>();

	// ArrayList<String> pic = new ArrayList<String>();
	int len = 0;
	 WebView mWebView;
	 int flagwebview=0;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity1);

		relativelayoutRoot = (RelativeLayout) findViewById(R.id.relativelayoutRootSelected);
		new ASSL(this, relativelayoutRoot, 1134, 720, false);

		viewFlipper = (ViewFlipper) findViewById(R.id.relativelayoutReportClickRoot);
//		textviewDescription = (TextView) findViewById(R.id.textviewDescription);
	//	photo=(ImageView)findViewById(R.id.imageView1);
		
		relativeLayoutBack = (RelativeLayout) findViewById(R.id.relativeLayoutBack);
		
		textviewNum = (TextView) findViewById(R.id.textviewNum);
		mWebView=(WebView)findViewById(R.id.webView1);
		textviewNum.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		textviewClickedOn = (TextView) findViewById(R.id.textviewClickedOn);
		button=(Button)findViewById(R.id.button1);
		textviewClickedOn.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		swipe = new SwipeDetector();
		// pic.add(" http://upload.wikimedia.org/wikipedia/commons/a/ab/Caonima_word-150x150.jpg");
		// pic.add("http://www.biofortified.org/wp-content/uploads/2010/12/GE_wheat_400-400.jpg");
		// pic.add("http://wikieducator.org/images/thumb/5/5e/Sunset.pdf/400px-Sunset.pdf.png");
		// pic.add("http://www.visitbritainshop.com/ImageCache/Products/669.1.400.400.FFFFFF.0.jpeg");
		// pic.add("http://www.abilities365.com/uploads/sponsor/17/products/9765-Samba%20400_400x400.jpg");

		Bundle bundle = getIntent().getExtras();
		idGetString = bundle.getString("id");
		keyString = bundle.getString("key");

		pic = (ArrayList<String>) getIntent().getSerializableExtra("array");
		ids = (ArrayList<String>) getIntent().getSerializableExtra("ids");
		desc = (ArrayList<String>) getIntent().getSerializableExtra("desc");
		type = (ArrayList<String>) getIntent().getSerializableExtra("type");
		workuser = bundle.getStringArrayList("work");
		checkuser = bundle.getStringArrayList("check");
		reportuser = bundle.getStringArrayList("report");
		docuser = bundle.getStringArrayList("doc");
		allusers = bundle.getStringArrayList("allusers");

		docdate = bundle.getStringArrayList("doc_date");
		checkdate = bundle.getStringArrayList("check_date");
		workdate = bundle.getStringArrayList("work_date");
		reportdate = bundle.getStringArrayList("report_date");
		alldates = bundle.getStringArrayList("all_date");

		docphase = bundle.getStringArrayList("doc_phase");
		checklistphase = bundle.getStringArrayList("checklist_phase");
		worklistphase = bundle.getStringArrayList("worklist_phase");
		reportphase = bundle.getStringArrayList("report_phase");
		allphase = bundle.getStringArrayList("all_phase");
		Log.d("", "------778887----" + keyString + idGetString);

		imageviewDeletePic = (ImageView) findViewById(R.id.deletepic);
		position = Prefrences.selectedPic;
		finalposition = ids.size();
		Log.d("--finall---", "--posss--" + position+" ,  "+finalposition);

		// clickedOn.setText(key.toString());
		textviewNum.setText((position + 1) + " of " + finalposition);
		if (Prefrences.pageFlag == 1) {
//			if (!desc.get(position).toString().equals("null")) {
//				tv_description.setText(desc.get(position).toString());
//				Log.d("log for p", "log for p " + position
//						+ desc.get(position).toString());
//			} else {
//				tv_description.setText("");
//				Log.d("log for p", "log for else p ");
//			}
//			textviewDescription.setMovementMethod(new ScrollingMovementMethod());
		} else {
			Log.d("-----", "-------------");
		}
//		button.setVisibility(View.GONE);
		 mWebView.setVisibility(View.GONE);
		
//		 if(type.get(position).toString().equalsIgnoreCase("application/pdf"))
//	        {
//				button.setVisibility(View.VISIBLE);
//				
//	        }
//			else
//			{
//				button.setVisibility(View.GONE);
//			}
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flagwebview==0)
				{
				 if(type.get(viewFlipper.getDisplayedChild()).toString().equalsIgnoreCase("application/pdf"))
		         {
					 flagwebview=1;
			     mWebView.setVisibility(View.VISIBLE);
		         mWebView.getSettings().setJavaScriptEnabled(true);
		         mWebView.getSettings().setPluginsEnabled(true);		        
		         mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+""+desc.get(viewFlipper.getDisplayedChild()).toString());
//		         setContentView(mWebView);
		         }
				}
				else{
					flagwebview=0;
					mWebView.setVisibility(View.GONE);
				}
				
			}
		});
		
		relativeLayoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
				if (Prefrences.well == 1) {

					Intent intent = new Intent(getApplicationContext(),
							ImageActivity.class);
					Log.d("", "------77777----" + keyString);
					intent.putExtra("key", keyString);

					intent.putStringArrayListExtra("doc", docuser);
					intent.putStringArrayListExtra("work", workuser);
					intent.putStringArrayListExtra("check", checkuser);
					intent.putStringArrayListExtra("report", reportuser);
					intent.putStringArrayListExtra("allusers", allusers);

					intent.putStringArrayListExtra("doc_date", docdate);
					intent.putStringArrayListExtra("work_date", workdate);
					intent.putStringArrayListExtra("check_date", checkdate);
					intent.putStringArrayListExtra("report_date", reportdate);
					intent.putStringArrayListExtra("all_date", alldates);

					intent.putStringArrayListExtra("doc_phase", docphase);
					intent.putStringArrayListExtra("worklist_phase",
							worklistphase);
					intent.putStringArrayListExtra("checklist_phase",
							checklistphase);
					intent.putStringArrayListExtra("report_phase", reportphase);
					intent.putStringArrayListExtra("all_phase", allphase);
					startActivity(intent);
				}

				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		imageviewDeletePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.d("******---","--****"+len);
				
				
				
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						SelectedImageView.this);

				// set title
				alertDialogBuilder.setTitle("Confirmation Needed:");

				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to delete this document?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int id) {
										// if this button is clicked,
										// close
										// current activity
										//archive(Prefrences.selectedProId);
										

										if(ConnectionDetector.isConnectingToInternet())
										{
											Log.d("possssiiiiitiooonnn","possssiiiiitiooonnn"+position);
											Log.d("viewFlipper.getDisplayedChild()","viewFlipper.getDisplayedChild()"+viewFlipper.getDisplayedChild());
											position=viewFlipper.getDisplayedChild();
											if ((position == 0 && len == 0) || (position == -1 && len == 0)
													|| (position == 1 && len == 0)) {
												position = 0;
												viewFlipper.removeViewAt(position);

												// String final_id=ids.get(p).toString();
												// //delete_photo(final_id);
												// Log.d(" finnallu","finally"+final_id);

											} else if (position == 0 || position == -1) {
												position = 0;
												viewFlipper.removeViewAt(position);
												// viewFlipper.showNext();

											} else if (position == len)// || p==len+1)
											{
												viewFlipper.removeViewAt(position);
												position = position - 1;
											
												// viewFlipper.showPrevious();
											} else if (len == 0) {
												position = len;
												viewFlipper.removeViewAt(position);
												// viewFlipper.showPrevious();
											} else {
												viewFlipper.removeViewAt(position);
											}
											// if(p==len==1)
											Log.d("", "array of ids == " + ids);

											if (Prefrences.well != 1) {
												finalIdString = ids.get(position).toString();
												deletePhoto(finalIdString);
												Prefrences.stopingHit = 1;
												Log.d(" finnallu", "finally" + finalIdString);
											} else {
												
												finalIdString = ids.get(position).toString();

												if (keyString.equals("All")) {
													for (int l = 0; l < DocumentFragment.photosListArrayList.size(); l++) {
														if (DocumentFragment.photosListArrayList.get(l).id
																.equals(finalIdString)) {
															DocumentFragment.photosListArrayList.remove(l);
														}

													}
													alldates.clear();
													allphase.clear();
													allusers.clear();
													// DocumentFragment.photosList.remove(p);
													for (int k = 0; k < DocumentFragment.photosListArrayList.size(); k++) {
														if (DocumentFragment.photosListArrayList.get(k).userName
																.equals("null")) {
															allusers.add("others(null)");
														} else {
															allusers.add(DocumentFragment.photosListArrayList.get(k).userName);
														}

														if (DocumentFragment.photosListArrayList.get(k).phase
																.equals("null")) {
															allphase.add("others_phase(null)");
														} else {
															allphase.add(DocumentFragment.photosListArrayList.get(k).phase);
														}

														alldates.add(DocumentFragment.photosListArrayList.get(k).createdDate);
													}
												} else if (keyString.equals("Report Pictures")) {
													for (int l = 0; l < DocumentFragment.photosList5ArrayList.size(); l++) {
														Log.d("",
																"000066669999"
																		+ finalIdString
																		+ ""
																		+ l
																		+ DocumentFragment.photosList5ArrayList
																				.get(l).id.toString());
														if (DocumentFragment.photosList5ArrayList.get(l).id
																.equals(finalIdString)) {
															DocumentFragment.photosList5ArrayList.remove(l);
														}

													}

													reportdate.clear();
													reportphase.clear();
													reportuser.clear();
													// DocumentFragment.photosList5.remove(p);
													for (int k = 0; k < DocumentFragment.photosList5ArrayList.size(); k++) {
														if (DocumentFragment.photosList5ArrayList.get(k).userName
																.equals("null")) {
															reportuser.add("others(null)");
														} else {
															reportuser.add(DocumentFragment.photosList5ArrayList
																	.get(k).userName);
														}

														if (DocumentFragment.photosList5ArrayList.get(k).phase
																.equals("null")) {
															reportphase.add("others_phase(null)");
														} else {
															reportphase.add(DocumentFragment.photosList5ArrayList
																	.get(k).phase);
														}

														reportdate.add(DocumentFragment.photosList5ArrayList.get(k).createdDate);
													}
												} else if (keyString.equals("Task Pictures")) {
													for (int l = 0; l < DocumentFragment.photosList4ArrayList.size(); l++) {
														Log.d("",
																"000066669999"
																		+ finalIdString
																		+ ""
																		+ l
																		+ DocumentFragment.photosList4ArrayList
																				.get(l).id.toString());
														if (DocumentFragment.photosList4ArrayList.get(l).id
																.equals(finalIdString)) {
															DocumentFragment.photosList4ArrayList.remove(l);
														}

													}
													workdate.clear();
													workuser.clear();
													worklistphase.clear();
													// DocumentFragment.photosList4.remove(p);
													for (int k = 0; k < DocumentFragment.photosList4ArrayList.size(); k++) {
														if (DocumentFragment.photosList4ArrayList.get(k).userName
																.equals("null")) {
															workuser.add("others(null)");
														} else {
															workuser.add(DocumentFragment.photosList4ArrayList
																	.get(k).userName);
														}

														if (DocumentFragment.photosList4ArrayList.get(k).phase
																.equals("null")) {
															worklistphase.add("others_phase(null)");
														} else {
															worklistphase.add(DocumentFragment.photosList4ArrayList
																	.get(k).phase);
														}

														workdate.add(DocumentFragment.photosList4ArrayList.get(k).createdDate);
													}
												} else if (keyString.equals("Project Docs")) {
													for (int l = 0; l < DocumentFragment.photosList2ArrayList.size(); l++) {
														Log.d("",
																"000066669999"
																		+ finalIdString
																		+ ""
																		+ l
																		+ DocumentFragment.photosList2ArrayList
																				.get(l).id.toString());
														if (DocumentFragment.photosList2ArrayList.get(l).id
																.equals(finalIdString)) {
															DocumentFragment.photosList2ArrayList.remove(l);
														}

													}

													docdate.clear();
													docphase.clear();
													docuser.clear();
													// DocumentFragment.photosList2.remove(p);
													for (int k = 0; k < DocumentFragment.photosList2ArrayList.size(); k++) {
														if (DocumentFragment.photosList2ArrayList.get(k).userName
																.equals("null")) {
															docuser.add("others(null)");
														} else {
															docuser.add(DocumentFragment.photosList2ArrayList.get(k).userName);
														}

														if (DocumentFragment.photosList2ArrayList.get(k).phase
																.equals("null")) {
															docphase.add("others_phase(null)");
														} else {
															docphase.add(DocumentFragment.photosList2ArrayList
																	.get(k).phase);
														}

														docdate.add(DocumentFragment.photosList2ArrayList.get(k).createdDate);
													}

												} else if (keyString.equals("Checklist Pictures")) {
													for (int l = 0; l < DocumentFragment.photosList3ArrayList.size(); l++) {
														Log.d("",
																"000066669999"
																		+ finalIdString
																		+ ""
																		+ l
																		+ DocumentFragment.photosList3ArrayList
																				.get(l).id.toString());
														if (DocumentFragment.photosList3ArrayList.get(l).id
																.equals(finalIdString)) {
															DocumentFragment.photosList3ArrayList.remove(l);
														}

													}
													checkuser.clear();
													checkdate.clear();
													checklistphase.clear();
													// DocumentFragment.photosList3.remove(p);
													for (int k = 0; k < DocumentFragment.photosList3ArrayList.size(); k++) {
														if (DocumentFragment.photosList3ArrayList.get(k).userName
																.equals("null")) {
															checkuser.add("others(null)");
														} else {
															checkuser.add(DocumentFragment.photosList3ArrayList
																	.get(k).userName);
														}

														if (DocumentFragment.photosList3ArrayList.get(k).phase
																.equals("null")) {
															checklistphase.add("others_phase(null)");
														} else {
															checklistphase.add(DocumentFragment.photosList3ArrayList
																	.get(k).phase);
														}

														checkdate.add(DocumentFragment.photosList3ArrayList.get(k).createdDate);
													}
													// doc_user.remove(p);
													// doc_phase.remove(p);
													// doc_date.remove(p);
												}
												deletePhoto(finalIdString);
												
											}
//										viewFlipper.removeAllViews();
											ids.remove(position);
											pic.remove(position);
											desc.remove(position);
											type.remove(position);
											Log.d("------", "size of pics" + pic.size() + "size of ids"
													+ ids.size());
											len = pic.size(); // pic=array
//											for (int i = 0; i < len; i++) { //
//												// This will create dynamic image view and add them to
//												// ViewFlipper
//												// Log.i("In", "---------in for loop---------" + len);
//												setFlipperImage(i);
//												// Log.d("","length val= "+i);
//											}

											Prefrences.deletePicFlag = 1;
										}
										else
										{
											Toast.makeText(getApplicationContext(), "No internet access", Toast.LENGTH_SHORT).show();
										}
										
										
										
										
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int id) {
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
		});
		
		
		// pic =
		// (ArrayList<SynopsisRecentDocuments>)getIntent().getSerializableExtra("QuestionListExtra");
		// ArrayList<Parcelable> bundle=
		// getIntent().getParcelableArrayListExtra("array");

		len = pic.size(); // pic=array
		for (int i = 0; i < len; i++) { //
			// This will create dynamic image view and add them to ViewFlipper
			// Log.i("In", "---------in for loop---------" + len);
			setFlipperImage(i);
			// Log.d("","length val= "+i);
		}
		Log.i("VAl", "" + Prefrences.selectedPic); //
		viewFlipper.setDisplayedChild(Prefrences.selectedPic); //
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (finalposition == 0)
			finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		
		if (Prefrences.well == 1) {

			Intent intent = new Intent(getApplicationContext(),
					ImageActivity.class);
			Log.d("", "------77777----" + keyString);
			intent.putExtra("key", keyString);

			intent.putStringArrayListExtra("doc", docuser);
			intent.putStringArrayListExtra("work", workuser);
			intent.putStringArrayListExtra("check", checkuser);
			intent.putStringArrayListExtra("report", reportuser);
			intent.putStringArrayListExtra("allusers", allusers);

			intent.putStringArrayListExtra("doc_date", docdate);
			intent.putStringArrayListExtra("work_date", workdate);
			intent.putStringArrayListExtra("check_date", checkdate);
			intent.putStringArrayListExtra("report_date", reportdate);
			intent.putStringArrayListExtra("all_date", alldates);

			intent.putStringArrayListExtra("doc_phase", docphase);
			intent.putStringArrayListExtra("worklist_phase", worklistphase);
			intent.putStringArrayListExtra("checklist_phase", checklistphase);
			intent.putStringArrayListExtra("report_phase", reportphase);
			intent.putStringArrayListExtra("all_phase", allphase);
			startActivity(intent);
		}
		else
		{

		}

		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

	}

	private void setFlipperImage(final int res) {
		// Log.i("Set Flipper Called res == ", res + "");
//		ProgressBar prog = new ProgressBar(getApplicationContext());
		ImageView image = new ImageView(getApplicationContext());
		image.setId(res);
		// description.setId(res);
		Log.d("pic","pic"+pic.get(res).toString()+"size = "+pic.size());
		

		
//		 WebView mWebView=new WebView(getApplicationContext());
//         mWebView.getSettings().setJavaScriptEnabled(true);
//         mWebView.getSettings().setPluginsEnabled(true);
//         mWebView.setId(res);
//         if(type.get(res).toString().equalsIgnoreCase("application/pdf"))
//         {
//        	 mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+""+desc.get(res).toString());
//         setContentView(mWebView);
//         }
//         else
//         {
         Picasso.with(SelectedImageView.this).load(pic.get(res).toString()).placeholder(drawable.processing_image) //
			.into(image);
//         }
         
         
		
		
		// Log.d("log for res", "log for res" + res + desc.get(res).toString());
		// viewFlipper.addView(image);
		zoomView = new ZoomView(this);
		zoomView.setOnTouchListener(swipe);
		zoomView.setId(res);
		zoomView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				position = viewFlipper.getDisplayedChild();
				// int p =(Integer) viewFlipper.getTag();
				if (swipe.swipeDetected()) {
					if (swipe.getAction() == SwipeDetector.Action.LR) {
						position = position - 1;
						Log.d("res ", "hota hai" + (position));

						viewFlipper.setInAnimation(SelectedImageView.this,
								R.anim.slide_in_left);
						viewFlipper.setOutAnimation(SelectedImageView.this,
								R.anim.slide_out_right);
						if (viewFlipper.getDisplayedChild() == 0) {
							// so that it should not show fourth content view
							position = 1;
						}
						else if(viewFlipper.getDisplayedChild() == finalposition){
							position=finalposition;
						}
						else {
							viewFlipper.showPrevious();
//							if (Prefrences.pageFlag == 1) {
//								if (!desc.get(position).toString()
//										.equals("null")) {
//									textviewDescription.setText(desc.get(position)
//											.toString());
//									Log.d("log for p", "log for p " + position
//											+ desc.get(position).toString());
//								} else {
//									textviewDescription.setText("");
//									Log.d("log for p", "log for else p ");
//								}
//								textviewDescription
//										.setMovementMethod(new ScrollingMovementMethod());
//							} else {
//								Log.d("-----", "-------------");
//							}
							textviewNum.setText(position + 1 + " of " + finalposition);
						}
					} else {
						position = position + 1;
						Log.d("res ", "hota hai" + (position));
						viewFlipper.setInAnimation(SelectedImageView.this,
								R.anim.slide_in_right);
						viewFlipper.setOutAnimation(SelectedImageView.this,
								R.anim.slide_out_left);
						if (viewFlipper.getDisplayedChild() == len - 1) {
							position = finalposition-1;
							// so that it should not show first content view
						} else {
							viewFlipper.showNext();
//							if (Prefrences.pageFlag == 1) {
//								if (!desc.get(position).toString()
//										.equals("null")) {
//									textviewDescription.setText(desc.get(position)
//											.toString());
//									Log.d("log for p", "log for p " + position
//											+ desc.get(position).toString());
//								} else {
//									textviewDescription.setText("");
//									Log.d("log for p", "log for else p ");
//								}
//								textviewDescription
//										.setMovementMethod(new ScrollingMovementMethod());
//							} else {
//								Log.d("-----", "-------------");
//							}
							textviewNum.setText(position + 1 + " of " + finalposition);
						}
					}
				}

			}
		});
//		zoomView.addView(prog);
//		if(type.get(res).toString().equalsIgnoreCase("application/pdf"))
//		{
//			zoomView.removeView(image);
//			zoomView.addView(mWebView);
//			viewFlipper.setTag(res);
////			viewFlipper.addView(zoomView);
//		}
//		else
//		{
//			zoomView.removeView(mWebView);
			zoomView.addView(image);
			viewFlipper.setTag(res);
			viewFlipper.addView(zoomView);
//		}
		
	}

	// ************ API hit for "DELETE Photo/Document" *************//

	public void deletePhoto(String photoId) {

		/*
		 * Required Parameters ÒidÓ : {photo.id}
		 */
		Prefrences.showLoadingDialog(SelectedImageView.this, "Loading...");
		AsyncHttpClient client = new AsyncHttpClient();

		client.addHeader("Content-type", "application/json");
		client.addHeader("Accept", "application/json");

		client.delete(SelectedImageView.this,
				Prefrences.url + "/photos/" + photoId,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String response) {
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("response ", "" + res.toString(2));
							String success = res.getString("success");
							Log.d("success", "" + success);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(finalposition==1)
							finish();
						if(position==finalposition-1)
						{
							finalposition=finalposition-1;
							textviewNum.setText((position) + " of " + (finalposition));
						}
						else{
							finalposition=finalposition-1;
							textviewNum.setText((position + 1) + " of " + (finalposition));
						}
						Log.d("pos","pos"+position);
						Prefrences.document_bool = false;
						Prefrences.stopingHit=1;
						Prefrences.dismissLoadingDialog();
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.e("request fail", arg0.toString());
						Prefrences.dismissLoadingDialog();
					}
				});
	}

}

