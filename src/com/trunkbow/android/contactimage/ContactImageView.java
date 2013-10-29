package com.trunkbow.android.contactimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactImageView extends ImageView {
    private static final int LOADING_THREADS = 4;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);

    private ContactImageTask currentTask;


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
        setImage(new ContactImage(contactId));
    }

    public void setImageContact(long contactId, final Integer fallbackResource) {
        setImage(new ContactImage(contactId), fallbackResource);
    }

    public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
    }

    // Helpers to set image by contact phone
    public void setImageContact(String phone) {
        setImage(new ContactImage(phone));
    }

    public void setImageContact(String phone, final Integer fallbackResource) {
        setImage(new ContactImage(phone), fallbackResource);
    }

    public void setImageContact(String phone, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ContactImage(phone), fallbackResource, fallbackResource);
    }
    // Set image using ContactImage object
    private void setImage(final ContactImage image) {
        setImage(image, null, null, null);
    }

    private void setImage(final ContactImage image, final ContactImageTask.OnCompleteListener completeListener) {
        setImage(image, null, null, completeListener);
    }

    private void setImage(final ContactImage image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource, null);
    }

    private void setImage(final ContactImage image, final Integer fallbackResource, ContactImageTask.OnCompleteListener completeListener) {
        setImage(image, fallbackResource, fallbackResource, completeListener);
    }

    private void setImage(final ContactImage image, final Integer fallbackResource, final Integer loadingResource) {
        setImage(image, fallbackResource, loadingResource, null);
    }

    private void setImage(final ContactImage image, final Integer fallbackResource, final Integer loadingResource, final ContactImageTask.OnCompleteListener completeListener) {
        // Set a loading resource
        if(loadingResource != null){
            setImageResource(loadingResource);
        }

        // Cancel any existing tasks for this image view
        if(currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }

        // Set up the new task
        currentTask = new ContactImageTask(getContext(), image);
        currentTask.setOnCompleteHandler(new ContactImageTask.OnCompleteHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if(bitmap != null) {
                    setImageBitmap(bitmap);
                } else { 
                    // Set fallback resource
                    if(fallbackResource != null) {
                        setImageResource(fallbackResource);
                    }
                }

                if(completeListener != null){
                    completeListener.onComplete();
                }
            }
        });

        // Run the task in a threadpool
        threadPool.execute(currentTask);
    }

    public static void cancelAllTasks() {
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    }
}