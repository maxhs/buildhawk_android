package com.buildhawk;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import rmn.androidscreenlibrary.ASSL;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AddUser extends Activity {

	private static final int PICK_CONTACT = 0;
	String contactID;
	LinearLayout linear;
	RelativeLayout back, pullcontacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);

		linear = (LinearLayout) findViewById(R.id.rl);
		new ASSL(this, linear, 1134, 720, false);

		back = (RelativeLayout) findViewById(R.id.back);
		pullcontacts = (RelativeLayout) findViewById(R.id.pullcontacts);

		pullcontacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT);
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

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
					String name = c
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

	private Bitmap retrieveContactPhoto() {

		Bitmap contactImage = null;

		InputStream inputStream = ContactsContract.Contacts
				.openContactPhotoInputStream(getContentResolver(), ContentUris
						.withAppendedId(ContactsContract.Contacts.CONTENT_URI,

						new Long(contactID)));

//		if (inputStream == null) {
//
//			contactImage = BitmapFactory.decodeResource(getResources(),
//					R.drawable.user);
//
//			ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//			contactImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//
//			byte[] imageInByte = stream.toByteArray();
//
//			Data.imageOfContact = imageInByte;
//
//			Log.e("Images+", "" + imageInByte);
//
//			Data.boolContactImage = 1;
//
//		} else {
//
//			contactImage = BitmapFactory.decodeStream(inputStream);
//
//			ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//			contactImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//
//			Log.e("LOG_Entry4", "LOG_Entry4");
//
//			byte[] imageInByte = stream.toByteArray();
//
//			Log.e("LOG_Entry5", "LOG_Entry5");
//
//			Data.imageOfContact = imageInByte;
//
//			Log.e("Images+", "" + imageInByte);
//
//			Data.boolContactImage = 0;
//
//		}

		assert inputStream != null;

		return contactImage;

	}

	public String retrieveNameEmailDetails() {

		String email = "";

		Cursor emailCur = getContentResolver().query(

		ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,

		ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",

		new String[] { contactID }, null);

		while (emailCur.moveToNext()) {

			email = emailCur
					.getString(emailCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

		}
		Log.d("email: ", "===>" + email);

		emailCur.close();

		return email;

	}

	private String retrieveContactNumber(Uri uriContact) {

		Cursor cursorPhone;

		String contactNo = "";

		Cursor cursorID = getContentResolver().query(uriContact,

		new String[] { ContactsContract.Contacts._ID }, null, null,

		null);

		if (cursorID.moveToFirst()) {

			contactID = cursorID.getString(cursorID
					.getColumnIndex(ContactsContract.Contacts._ID));

		}

		cursorID.close();

		cursorPhone = getContentResolver().query(

		ContactsContract.CommonDataKinds.Phone.CONTENT_URI,

		new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },

		ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "

		+ ContactsContract.CommonDataKinds.Phone.TYPE + " = "

		+ ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

		new String[] { contactID }, null);

		if (cursorPhone.moveToFirst()) {

			contactNo = cursorPhone

			.getString(cursorPhone

			.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

		}

		Log.d("contactno: ", "===>" + contactNo);
		cursorPhone.close();
		

		return contactNo;

	}

	private String retrieveContactName(Uri uriContact) {

		Cursor cursor = getContentResolver().query(uriContact, null, null,

		null, null);

		String name = "";

		if (cursor.moveToFirst()) {

			name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

			Log.d("name ", "===>" + name);

		}

		cursor.close();

		return name;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}

}
