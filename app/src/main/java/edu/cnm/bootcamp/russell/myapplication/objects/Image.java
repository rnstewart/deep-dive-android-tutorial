package edu.cnm.bootcamp.russell.myapplication.objects;

import android.app.Activity;

import com.google.gson.annotations.SerializedName;

import edu.cnm.bootcamp.russell.myapplication.utils.FilesystemMethods;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by russell on 5/27/17.
 */

public class Image {
    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("link")
    String link;

    @SerializedName("datetime")
    long datetime;

    @SerializedName("views")
    int views;

    @SerializedName("score")
    int score;

    @SerializedName("cover")
    String cover;

    @SerializedName("in_gallery")
    boolean in_gallery;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public long getDatetime() {
        return datetime;
    }

    public int getViews() {
        return views;
    }

    public int getScore() {
        return score;
    }

    public String getImageURL() {
        String url = null;
        if (in_gallery) {
            if (cover != null && cover.length() > 0) {
                url = "http://i.imgur.com/" + cover + ".jpg";
            }
        }
        else {
            url = link;
        }
        return url;
    }

    public void downloadImage(final Activity activity, final Runnable callback) {
        if (activity != null) {
            Single.create(new Single.OnSubscribe<Boolean>() {
                @Override
                public void call(SingleSubscriber<? super Boolean> singleSubscriber) {
                    FilesystemMethods.downloadFile(activity, getImageURL());
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Boolean>() {
                                   @Override
                                   public void call(Boolean success) {
                                       if (callback != null) {
                                           activity.runOnUiThread(callback);
                                       }
                                   }
                               },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {

                                }
                            });
        }
    }
}
