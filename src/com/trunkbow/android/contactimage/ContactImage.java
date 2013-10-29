package com.trunkbow.android.contactimage;

import java.io.InputStream;

import android.content.ContentUris;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

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

		if (phone != null) {
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phone));
			// PhoneLookup._ID 相当于联系人ID
			Cursor c = context.getContentResolver().query(uri,
					new String[] { PhoneLookup._ID }, null, null, null);
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

		return bitmap;
	}
}