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
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.buildhawk.R.drawable;
import com.buildhawk.utils.SynopsisRecentDocuments;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

public class SelectedImageView extends Activity {
	ViewFlipper viewFlipper;
	SwipeDetector swipe;
	RelativeLayout rlayout;
	Bitmap bitmap;
	// static int x;
	
	private ZoomView zoomView;
	ArrayList<String> workuser, checkuser, reportuser, docuser, allusers,
			docdate, workdate, checkdate, reportdate, alldates, docphase,
			checklistphase, worklistphase, reportphase, allphase;

	ArrayList<String> pic;
	ArrayList<String> ids;
	ArrayList<String> desc;
	ImageView img_deletepic;
//	ImageView photo;
	int position, finalposition;
	// int r;
	String finalId;
	String idGet, key;
	TextView tv_description;
	RelativeLayout back;
	TextView tv_num, tv_clickedOn;
	RelativeLayout relLay;
	// ArrayList<SynopsisRecentDocuments>pic =new
	// ArrayList<SynopsisRecentDocuments>();

	// ArrayList<String> pic = new ArrayList<String>();
	int len = 0;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity1);

		relLay = (RelativeLayout) findViewById(R.id.rl);
		new ASSL(this, relLay, 1134, 720, false);

		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		tv_description = (TextView) findViewById(R.id.description);
	//	photo=(ImageView)findViewById(R.id.imageView1);
		rlayout = (RelativeLayout) findViewById(R.id.rl);
		back = (RelativeLayout) findViewById(R.id.back);
		
		tv_num = (TextView) findViewById(R.id.num);
		
		tv_num.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		tv_clickedOn = (TextView) findViewById(R.id.clickedOn);
		tv_clickedOn.setTypeface(Prefrences.helveticaNeuebd(getApplicationContext()));
		swipe = new SwipeDetector();
		// pic.add(" http://upload.wikimedia.org/wikipedia/commons/a/ab/Caonima_word-150x150.jpg");
		// pic.add("http://www.biofortified.org/wp-content/uploads/2010/12/GE_wheat_400-400.jpg");
		// pic.add("http://wikieducator.org/images/thumb/5/5e/Sunset.pdf/400px-Sunset.pdf.png");
		// pic.add("http://www.visitbritainshop.com/ImageCache/Products/669.1.400.400.FFFFFF.0.jpeg");
		// pic.add("http://www.abilities365.com/uploads/sponsor/17/products/9765-Samba%20400_400x400.jpg");

		Bundle bundle = getIntent().getExtras();
		idGet = bundle.getString("id");
		key = bundle.getString("key");

		pic = (ArrayList<String>) getIntent().getSerializableExtra("array");
		ids = (ArrayList<String>) getIntent().getSerializableExtra("ids");
		desc = (ArrayList<String>) getIntent().getSerializableExtra("desc");
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
		Log.d("", "------778887----" + key + idGet);

		img_deletepic = (ImageView) findViewById(R.id.deletepic);
		position = Prefrences.selectedPic;
		finalposition = ids.size();
		Log.d("-----", "----" + position);

		// clickedOn.setText(key.toString());
		tv_num.setText((position + 1) + " of " + finalposition);
		if (Prefrences.pageFlag == 1) {
			if (!desc.get(position).toString().equals("null")) {
				tv_description.setText(desc.get(position).toString());
				Log.d("log for p", "log for p " + position
						+ desc.get(position).toString());
			} else {
				tv_description.setText("");
				Log.d("log for p", "log for else p ");
			}
			tv_description.setMovementMethod(new ScrollingMovementMethod());
		} else {
			Log.d("-----", "-------------");
		}

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
				if (Prefrences.well == 1) {

					Intent intent = new Intent(getApplicationContext(),
							ImageActivity.class);
					Log.d("", "------77777----" + key);
					intent.putExtra("key", key);

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

		img_deletepic.setOnClickListener(new OnClickListener() {

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
											position = position - 1;
											viewFlipper.removeViewAt(position);
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
											finalId = ids.get(position).toString();
											deletePhoto(finalId);
											Prefrences.stopingHit = 1;
											Log.d(" finnallu", "finally" + finalId);
										} else {
											finalId = ids.get(position).toString();

											if (key.equals("All")) {
												for (int l = 0; l < DocumentFragment.photosList.size(); l++) {
													if (DocumentFragment.photosList.get(l).id
															.equals(finalId)) {
														DocumentFragment.photosList.remove(l);
													}

												}
												alldates.clear();
												allphase.clear();
												allusers.clear();
												// DocumentFragment.photosList.remove(p);
												for (int k = 0; k < DocumentFragment.photosList.size(); k++) {
													if (DocumentFragment.photosList.get(k).userName
															.equals("null")) {
														allusers.add("kanika(null)");
													} else {
														allusers.add(DocumentFragment.photosList.get(k).userName);
													}

													if (DocumentFragment.photosList.get(k).phase
															.equals("null")) {
														allphase.add("kanika_phase(null)");
													} else {
														allphase.add(DocumentFragment.photosList.get(k).phase);
													}

													alldates.add(DocumentFragment.photosList.get(k).createdDate);
												}
											} else if (key.equals("Report")) {
												for (int l = 0; l < DocumentFragment.photosList5.size(); l++) {
													Log.d("",
															"000066669999"
																	+ finalId
																	+ ""
																	+ l
																	+ DocumentFragment.photosList5
																			.get(l).id.toString());
													if (DocumentFragment.photosList5.get(l).id
															.equals(finalId)) {
														DocumentFragment.photosList5.remove(l);
													}

												}

												reportdate.clear();
												reportphase.clear();
												reportuser.clear();
												// DocumentFragment.photosList5.remove(p);
												for (int k = 0; k < DocumentFragment.photosList5.size(); k++) {
													if (DocumentFragment.photosList5.get(k).userName
															.equals("null")) {
														reportuser.add("kanika(null)");
													} else {
														reportuser.add(DocumentFragment.photosList5
																.get(k).userName);
													}

													if (DocumentFragment.photosList5.get(k).phase
															.equals("null")) {
														reportphase.add("kanika_phase(null)");
													} else {
														reportphase.add(DocumentFragment.photosList5
																.get(k).phase);
													}

													reportdate.add(DocumentFragment.photosList5.get(k).createdDate);
												}
											} else if (key.equals("Worklist")) {
												for (int l = 0; l < DocumentFragment.photosList4.size(); l++) {
													Log.d("",
															"000066669999"
																	+ finalId
																	+ ""
																	+ l
																	+ DocumentFragment.photosList4
																			.get(l).id.toString());
													if (DocumentFragment.photosList4.get(l).id
															.equals(finalId)) {
														DocumentFragment.photosList4.remove(l);
													}

												}
												workdate.clear();
												workuser.clear();
												worklistphase.clear();
												// DocumentFragment.photosList4.remove(p);
												for (int k = 0; k < DocumentFragment.photosList4.size(); k++) {
													if (DocumentFragment.photosList4.get(k).userName
															.equals("null")) {
														workuser.add("kanika(null)");
													} else {
														workuser.add(DocumentFragment.photosList4
																.get(k).userName);
													}

													if (DocumentFragment.photosList4.get(k).phase
															.equals("null")) {
														worklistphase.add("kanika_phase(null)");
													} else {
														worklistphase.add(DocumentFragment.photosList4
																.get(k).phase);
													}

													workdate.add(DocumentFragment.photosList4.get(k).createdDate);
												}
											} else if (key.equals("Project Docs")) {
												for (int l = 0; l < DocumentFragment.photosList2.size(); l++) {
													Log.d("",
															"000066669999"
																	+ finalId
																	+ ""
																	+ l
																	+ DocumentFragment.photosList2
																			.get(l).id.toString());
													if (DocumentFragment.photosList2.get(l).id
															.equals(finalId)) {
														DocumentFragment.photosList2.remove(l);
													}

												}

												docdate.clear();
												docphase.clear();
												docuser.clear();
												// DocumentFragment.photosList2.remove(p);
												for (int k = 0; k < DocumentFragment.photosList2.size(); k++) {
													if (DocumentFragment.photosList2.get(k).userName
															.equals("null")) {
														docuser.add("kanika(null)");
													} else {
														docuser.add(DocumentFragment.photosList2.get(k).userName);
													}

													if (DocumentFragment.photosList2.get(k).phase
															.equals("null")) {
														docphase.add("kanika_phase(null)");
													} else {
														docphase.add(DocumentFragment.photosList2
																.get(k).phase);
													}

													docdate.add(DocumentFragment.photosList2.get(k).createdDate);
												}

											} else if (key.equals("Checklist")) {
												for (int l = 0; l < DocumentFragment.photosList3.size(); l++) {
													Log.d("",
															"000066669999"
																	+ finalId
																	+ ""
																	+ l
																	+ DocumentFragment.photosList3
																			.get(l).id.toString());
													if (DocumentFragment.photosList3.get(l).id
															.equals(finalId)) {
														DocumentFragment.photosList3.remove(l);
													}

												}
												checkuser.clear();
												checkdate.clear();
												checklistphase.clear();
												// DocumentFragment.photosList3.remove(p);
												for (int k = 0; k < DocumentFragment.photosList3.size(); k++) {
													if (DocumentFragment.photosList3.get(k).userName
															.equals("null")) {
														checkuser.add("kanika(null)");
													} else {
														checkuser.add(DocumentFragment.photosList3
																.get(k).userName);
													}

													if (DocumentFragment.photosList3.get(k).phase
															.equals("null")) {
														checklistphase.add("kanika_phase(null)");
													} else {
														checklistphase.add(DocumentFragment.photosList3
																.get(k).phase);
													}

													checkdate.add(DocumentFragment.photosList3.get(k).createdDate);
												}
												// doc_user.remove(p);
												// doc_phase.remove(p);
												// doc_date.remove(p);
											}
											deletePhoto(finalId);
											Prefrences.stopingHit = 1;
										}
										ids.remove(position);
										pic.remove(position);
										Log.d("------", "size of pics" + pic.size() + "size of ids"
												+ ids.size());
										len = pic.size(); // pic=array
										for (int i = 0; i < len; i++) { //
											// This will create dynamic image view and add them to
											// ViewFlipper
											// Log.i("In", "---------in for loop---------" + len);
											setFlipperImage(i);
											// Log.d("","length val= "+i);
										}

										Prefrences.deletePicFlag = 1;
										
										
										
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
			Log.d("", "------77777----" + key);
			intent.putExtra("key", key);

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

		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

	}

	private void setFlipperImage(final int res) {
		// Log.i("Set Flipper Called res == ", res + "");
//		ProgressBar prog = new ProgressBar(getApplicationContext());
		ImageView image = new ImageView(getApplicationContext());
		image.setId(res);
		// description.setId(res);
		Log.d("pic","pic"+pic.get(res).toString()+"size = "+pic.size());
		Picasso.with(SelectedImageView.this).load(pic.get(res).toString())//.placeholder(prog) //
				.into(image);

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
						} else {
							viewFlipper.showPrevious();
							if (Prefrences.pageFlag == 1) {
								if (!desc.get(position).toString()
										.equals("null")) {
									tv_description.setText(desc.get(position)
											.toString());
									Log.d("log for p", "log for p " + position
											+ desc.get(position).toString());
								} else {
									tv_description.setText("");
									Log.d("log for p", "log for else p ");
								}
								tv_description
										.setMovementMethod(new ScrollingMovementMethod());
							} else {
								Log.d("-----", "-------------");
							}
							tv_num.setText(position + 1 + " of " + finalposition);
						}
					} else {
						position = position + 1;
						Log.d("res ", "hota hai" + (position));
						viewFlipper.setInAnimation(SelectedImageView.this,
								R.anim.slide_in_right);
						viewFlipper.setOutAnimation(SelectedImageView.this,
								R.anim.slide_out_left);
						if (viewFlipper.getDisplayedChild() == len - 1) {
							position = finalposition;
							// so that it should not show first content view
						} else {
							viewFlipper.showNext();
							if (Prefrences.pageFlag == 1) {
								if (!desc.get(position).toString()
										.equals("null")) {
									tv_description.setText(desc.get(position)
											.toString());
									Log.d("log for p", "log for p " + position
											+ desc.get(position).toString());
								} else {
									tv_description.setText("");
									Log.d("log for p", "log for else p ");
								}
								tv_description
										.setMovementMethod(new ScrollingMovementMethod());
							} else {
								Log.d("-----", "-------------");
							}
							tv_num.setText(position + 1 + " of " + finalposition);
						}
					}
				}

			}
		});
//		zoomView.addView(prog);
		zoomView.addView(image);
		viewFlipper.setTag(res);
		viewFlipper.addView(zoomView);
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
						finalposition=finalposition-1;
						tv_num.setText((position + 1) + " of " + (finalposition));
						
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
