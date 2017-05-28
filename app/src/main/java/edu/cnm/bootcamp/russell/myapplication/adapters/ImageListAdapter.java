package edu.cnm.bootcamp.russell.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.cnm.bootcamp.russell.myapplication.R;
import edu.cnm.bootcamp.russell.myapplication.objects.Image;

/**
 * Created by russell on 5/27/17.
 */

public class ImageListAdapter extends BaseAdapter {
    private List<Image> mImages;
    private Context mContext;

    public ImageListAdapter(Context context, List<Image> data) {
        mContext = context;
        mImages = data;
    }

    @Override
    public int getCount() {
        return (mImages != null) ? mImages.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (mImages != null && position < mImages.size()) ? mImages.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_list_item, parent, false);
        }

        Image image = (Image)getItem(position);
        TextView txtImageTitle = (TextView)convertView.findViewById(R.id.txtImageTitle);
        txtImageTitle.setText(image.getTitle());

        return convertView;
    }
}
