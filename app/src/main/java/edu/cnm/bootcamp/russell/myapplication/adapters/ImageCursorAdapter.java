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
        Image image = new Image(cursor);
        TextView txtImageTitle = (TextView)view.findViewById(R.id.txtImageTitle);
        txtImageTitle.setText(image.getTitle());

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        if (mFlingMode) {
            imageView.setImageBitmap(null);
        }
        else {
            Bitmap bitmap = image.getDownloadedImage(mContext);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
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
    }

    public void setFlingMode(boolean flingMode) {
        mFlingMode = flingMode;
        notifyDataSetChanged();
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
