package com.trunkbow.android.contactimage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class CacheContainer {

	private static CacheContainer cacheContainer;
	private MemoryCache memoryCache;
	private final int LOADING_THREADS = 6;
	private ExecutorService threadPool;

	public static CacheContainer getIntance() {

		Log.d("CacheContainer", "CacheContainer=null:"
				+ (cacheContainer == null));
		if (cacheContainer == null) {
			synchronized (CacheContainer.class) {
				if (cacheContainer == null)
					cacheContainer = new CacheContainer();
			}
		}
		return cacheContainer;
	}

	private CacheContainer() {
		memoryCache = new MemoryCache();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}

	private Bitmap get(String id) {
		return memoryCache.get(id);
	}

	private void put(String id, Bitmap bitmap) {
		memoryCache.put(id, bitmap);
	}

	private ContactImageTask currentTask;

	public void setImage(final ContactImageView contactImageView, final String key,
			Context context, final ContactImage image,
			final Integer fallbackResource, final Integer loadingResource,
			final ContactImageTask.OnCompleteListener completeListener) {
		// Set a loading resource

		Bitmap bitmap = get(key);
		if (bitmap != null) {
			contactImageView.setImageBitmap(bitmap);
		} else {
			if (loadingResource != null) {
				contactImageView.setImageResource(loadingResource);
			}
			// Cancel any existing tasks for this image view
			if (currentTask != null) {
				// currentTask.cancel();
				currentTask = null;
			}
			Log.d("CacheContainer", "CacheContainer=currentTask = null:"
					+ (currentTask == null));
			// Set up the new task
			currentTask = new ContactImageTask(context, image);
			currentTask
					.setOnCompleteHandler(new ContactImageTask.OnCompleteHandler() {
						@Override
						public void onComplete(Bitmap bitmap) {

							Log.d("CacheContainer", "CacheContaine_onComplete:"
									+ (bitmap != null));

							if (bitmap != null) {
								contactImageView.setImageBitmap(bitmap);
								put(key, bitmap);
							} else {
								// Set fallback resource
								if (fallbackResource != null) {
									contactImageView
											.setImageResource(fallbackResource);
								}
								
								if(contactImageView.getDrawingCache()!=null){
									put(key, contactImageView.getDrawingCache());
								}
							}
							if (completeListener != null) {
								completeListener.onComplete();
							}
						}
					});

			// Run the task in a threadpool
			threadPool.execute(currentTask);
		}

	}

	public void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}
}
