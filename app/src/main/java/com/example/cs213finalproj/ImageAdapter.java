package com.example.cs213finalproj;

import android.content.Context;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Uri> uriList;

    public ImageAdapter(Context c, ArrayList<Uri> uriList) {
        mContext = c;
        this.uriList = uriList;
    }

    public int getCount() {
        return uriList.size();
    }
    public Object getItem(int position) {
        return null;
    }
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }else{
            imageView = (ImageView) convertView;
        }

        imageView.setImageURI(uriList.get(position));
        return imageView;
    }
}
