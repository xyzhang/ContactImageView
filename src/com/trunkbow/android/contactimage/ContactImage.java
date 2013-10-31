package com.trunkbow.android.contactimage;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class ContactImage {
	private long contactId;

	public ContactImage(long contactId) {
		this.contactId = contactId;
	}

	private String phone;

	public ContactImage(String phone) {
		this.phone = phone;
	}

	public Bitmap getBitmap(Context context) {
		Bitmap bitmap = null;

		Log.d("ContactImage", "ContactImage_phone:"+phone);
		if (phone != null) {
			Uri uri = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI,
					Uri.encode(phone));
			// PhoneLookup._ID
			Cursor c = context.getContentResolver().query(uri,
					new String[] { Phone.CONTACT_ID }, null, null, null);
			if (c.moveToFirst()) {
				contactId = c.getLong(0);
			}
			c.close();
		}

		ContentResolver contentResolver = context.getContentResolver();

		try {
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI, contactId);
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(contentResolver, uri);
			if (input != null) {
				bitmap = BitmapFactory.decodeStream(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("ContactImage", "ContactImage_bitmap:"+(bitmap==null));
		return bitmap;
	}
}