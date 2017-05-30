package edu.cnm.bootcamp.russell.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.cnm.bootcamp.russell.myapplication.R;
import edu.cnm.bootcamp.russell.myapplication.database.DatabaseMethods;
import edu.cnm.bootcamp.russell.myapplication.objects.Image;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by russell on 5/29/17.
 */

public class ImageCursorAdapter extends CursorAdapter {
    private boolean mFlingMode = false;

    public ImageCursorAdapter(Context context) {
        super(context, DatabaseMethods.getImages(context), FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final Image image = new Image(cursor);
        TextView txtImageTitle = (TextView)view.findViewById(R.id.txtImageTitle);
        txtImageTitle.setText(image.getTitle());

        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
        if (mFlingMode) {
            imageView.setImageBitmap(null);
        }
        else {
            Single.create(new Single.OnSubscribe<Bitmap>() {
                @Override
                public void call(SingleSubscriber<? super Bitmap> singleSubscriber) {
                    Bitmap bitmap = image.getDownloadedImage(mContext);
                    singleSubscriber.onSuccess(bitmap);
                }
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                                   @Override
                                   public void call(Bitmap bitmap) {
                                       if (bitmap != null) {
                                           if (imageView != null) {
                                               imageView.setImageBitmap(bitmap);
                                           }
                                       } else {
                                           if (mContext instanceof Activity) {
                                               image.downloadImage((Activity) mContext, new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       notifyDataSetChanged();
                                                   }
                                               });
                                           }
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

    public void setFlingMode(boolean flingMode) {
        if (mFlingMode != flingMode) {
            mFlingMode = flingMode;
            notifyDataSetChanged();
        }
    }

    public void refresh() {
        Cursor oldCursor = swapCursor(DatabaseMethods.getImages(mContext));
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    public void close() {
        Cursor oldCursor = swapCursor(null);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }
}
