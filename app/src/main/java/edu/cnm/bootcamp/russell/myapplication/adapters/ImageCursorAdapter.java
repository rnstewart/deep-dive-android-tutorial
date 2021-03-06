package edu.cnm.bootcamp.russell.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.util.LruCache;
import android.support.v4.widget.CursorAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private String mSubreddit;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    private DecimalFormat mScoreFormat = new DecimalFormat("#,###,###");
    private boolean mFlingMode = false;
    private LruCache<Integer, Bitmap> mMemoryCache;
    private int mImageWidth = -1;

    public ImageCursorAdapter(Context context, String subreddit) {
        super(context, DatabaseMethods.getImages(context, subreddit), FLAG_REGISTER_CONTENT_OBSERVER);
        mSubreddit = subreddit;
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mImageWidth = size.x;
    }

    protected void addBitmapToMemoryCache(int position, Bitmap bitmap) {
        if (bitmap != null && mMemoryCache != null && mMemoryCache.get(position) == null) {
            mMemoryCache.put(position, bitmap);
        }
    }

    protected Bitmap getCachedBitmap(int position) {
        Bitmap bitmap = null;
        if (mMemoryCache != null) {
            bitmap = mMemoryCache.get(position);
        }

        return bitmap;
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

        TextView txtScore = (TextView)view.findViewById(R.id.txtScore);
        txtScore.setText(mScoreFormat.format(image.getScore()));

        TextView txtDate = (TextView)view.findViewById(R.id.txtDate);
        Date date = new Date(image.getDatetime() * 1000);
        txtDate.setText(mDateFormat.format(date));

        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
        final int position = cursor.getPosition();
        Bitmap bitmap = getCachedBitmap(position);
        imageView.setImageBitmap(bitmap);
        if (bitmap == null && !mFlingMode) {
            Single.create(new Single.OnSubscribe<Bitmap>() {
                @Override
                public void call(SingleSubscriber<? super Bitmap> singleSubscriber) {
                    Bitmap bitmap = image.getDownloadedImage(mContext, mImageWidth);
                    singleSubscriber.onSuccess(bitmap);
                }
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                                   @Override
                                   public void call(Bitmap bitmap) {
                                       if (bitmap != null) {
                                           addBitmapToMemoryCache(position, bitmap);
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

    public void setSubreddit(String subreddit) {
        mSubreddit = subreddit;
        refresh();
    }

    public void refresh() {
        Cursor oldCursor = swapCursor(DatabaseMethods.getImages(mContext, mSubreddit));
        if (oldCursor != null) {
            oldCursor.close();
        }
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
        notifyDataSetChanged();
    }

    public void close() {
        Cursor oldCursor = swapCursor(null);
        if (oldCursor != null) {
            oldCursor.close();
        }
    }
}
