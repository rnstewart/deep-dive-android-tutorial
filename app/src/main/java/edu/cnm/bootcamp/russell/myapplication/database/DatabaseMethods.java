package edu.cnm.bootcamp.russell.myapplication.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import edu.cnm.bootcamp.russell.myapplication.datatables.TableImages;
import edu.cnm.bootcamp.russell.myapplication.objects.Image;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by russell on 5/29/17.
 */

public class DatabaseMethods {
    public static void saveImages(final Activity activity, final List<Image> images, final Runnable callback) {
        Single.create(new Single.OnSubscribe<Boolean>() {
            @Override
            public void call(SingleSubscriber<? super Boolean> singleSubscriber) {
                if (images != null) {
                    for (Image image : images) {
                        image.saveToDB(activity);
                    }
                }
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                               @Override
                               public void call(Boolean success) {
                                   if (callback != null && success) {
                                       activity.runOnUiThread(callback);
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
    }

    public static Cursor getImages(Context context) {
        Cursor c = null;
        SQLiteDatabase db = DatabaseHelper.getDatabase(context);
        if (db != null) {
            c = db.query(
                    TableImages.NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        return c;
    }
}
