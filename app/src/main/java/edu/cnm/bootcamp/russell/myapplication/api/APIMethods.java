package edu.cnm.bootcamp.russell.myapplication.api;

import android.app.Activity;

import edu.cnm.bootcamp.russell.myapplication.database.DatabaseMethods;
import edu.cnm.bootcamp.russell.myapplication.objects.GalleryResponse;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by russell on 5/29/17.
 */

public class APIMethods {
    public static void getSubredditGallery(final Activity activity, String subreddit, final Runnable callback) {
        API.subredditGallery(subreddit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<GalleryResponse>(){

                    @Override
                    public void onSuccess(GalleryResponse value) {
                        DatabaseMethods.saveImages(activity, value.getData(), callback);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }
}
