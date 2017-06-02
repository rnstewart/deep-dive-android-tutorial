package edu.cnm.bootcamp.russell.myapplication.objects;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import edu.cnm.bootcamp.russell.myapplication.database.DatabaseHelper;
import edu.cnm.bootcamp.russell.myapplication.datatables.TableImages;
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

    @SerializedName("section")
    String section;

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

    public Image(Cursor cursor) {
        if (cursor != null) {
            id = cursor.getString(cursor.getColumnIndex(TableImages.COL_ID));
            title = cursor.getString(cursor.getColumnIndex(TableImages.COL_TITLE));
            description = cursor.getString(cursor.getColumnIndex(TableImages.COL_DESCRIPTION));
            link = cursor.getString(cursor.getColumnIndex(TableImages.COL_LINK));
            datetime = cursor.getLong(cursor.getColumnIndex(TableImages.COL_DATETIME));
            views = cursor.getInt(cursor.getColumnIndex(TableImages.COL_VIEWS));
            score = cursor.getInt(cursor.getColumnIndex(TableImages.COL_SCORE));
            cover = cursor.getString(cursor.getColumnIndex(TableImages.COL_COVER));
            in_gallery = (cursor.getInt(cursor.getColumnIndex(TableImages.COL_IN_GALLERY)) == 1);
        }
    }

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
                    boolean success = FilesystemMethods.downloadFile(activity, getImageURL());
                    singleSubscriber.onSuccess(success);
                }
            })
                    .subscribeOn(Schedulers.io())
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
    }

    public Bitmap getDownloadedImage(Context context, int width) {
        return FilesystemMethods.getDownloadedImage(context, getImageURL(), width);
    }

    public void saveToDB(Context context) {
        SQLiteDatabase db = DatabaseHelper.getDatabase(context);
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(TableImages.COL_ID, id);
            values.put(TableImages.COL_TITLE, title);
            values.put(TableImages.COL_DESCRIPTION, description);
            values.put(TableImages.COL_LINK, link);
            values.put(TableImages.COL_DATETIME, datetime);
            values.put(TableImages.COL_VIEWS, views);
            values.put(TableImages.COL_SCORE, score);
            values.put(TableImages.COL_COVER, cover);
            values.put(TableImages.COL_SECTION, section);
            values.put(TableImages.COL_IN_GALLERY, in_gallery ? 1 : 0);

            String selection = TableImages.COL_ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(id)};
            Cursor c = db.query(
                    TableImages.NAME,
                    new String[]{TableImages.COL_ID},
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            boolean exists = false;
            if (c != null) {
                exists = (c.getCount() > 0);
                c.close();
            }
            if (exists) {
                db.update(TableImages.NAME, values, selection, selectionArgs);
            }
            else {
                db.insert(TableImages.NAME, null, values);
            }
        }
    }
}
