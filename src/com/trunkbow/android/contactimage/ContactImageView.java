package com.trunkbow.android.contactimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactImageView extends ImageView {

	private String key;

    public ContactImageView(Context context) {
        super(context);
    }

    public ContactImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Helpers to set image by contact address book id
    public void setImageContact(long contactId) {
    	key=String.valueOf(contactId);
        setImage(new ContactImage(contactId));
    }

    public void setImageContact(long contactId, final Integer fallbackResource) {
    	key=String.valueOf(contactId);
    	setImage(new ContactImage(contactId), fallbackResource);
    }

    public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
    	key=String.valueOf(contactId);
    	setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
    }

    // Helpers to set image by contact phone
    public void setImageContact(String phone) {
    	key=String.valueOf(phone);
        setImage(new ContactImage(phone));
    }

    public void setImageContact(String phone, final Integer fallbackResource) {
    	key=String.valueOf(phone);
    	setImage(new ContactImage(phone), fallbackResource);
    }

    public void setImageContact(String phone, final Integer fallbackResource, final Integer loadingResource) {
    	key=String.valueOf(phone);
    	setImage(new ContactImage(phone), fallbackResource, fallbackResource);
    }
    // Set image using ContactImage object
    private void setImage(final ContactImage image) {
        setImage(image, null, null, null);
    }

//    private void setImage(final ContactImage image, final ContactImageTask.OnCompleteListener completeListener) {
//        setImage(image, null, null, completeListener);
//    }

    private void setImage(final ContactImage image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource, null);
    }

    public void setImage(final ContactImage image, final Integer fallbackResource, ContactImageTask.OnCompleteListener completeListener) {
        setImage(image, fallbackResource, fallbackResource, completeListener);
    }

    private void setImage(final ContactImage image, final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, fallbackResource, loadingResource, null);
    }
    
    
    private void setImage(final ContactImage image, final Integer fallbackResource, final Integer loadingResource, final ContactImageTask.OnCompleteListener completeListener) {
    	 CacheContainer cacheContainer=CacheContainer.getIntance();
    	 cacheContainer.setImage(ContactImageView.this, key, getContext(), image, fallbackResource, loadingResource, completeListener);
    }

    
}