package com.example.cs213finalproj;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecyclerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Uri> uriArrayList;
    private int selectedPosition = -1;

    public RecyclerAdapter(Context c, ArrayList<Uri> uriArrayList) {
        mContext = c;
        this.uriArrayList = uriArrayList;
    }

    public int getCount() {
        return uriArrayList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.getLayoutParams().height = 300;
            imageView.getLayoutParams().width = 250;
            imageView.setPadding(5, 5, 5, 5);
        }else{
            imageView = (ImageView) convertView;
        }

        imageView.setImageURI(uriArrayList.get(position));

        if (position == selectedPosition) {
            imageView.setBackgroundColor(Color.BLACK);
        } else {
            imageView.setBackgroundColor(Color.TRANSPARENT);
        }

        return imageView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
}
