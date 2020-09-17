package com.sdlifes.sdlifes.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.ImageCheckActivity;
import com.sdlifes.sdlifes.util.ImageUtils;

import java.util.List;

/**
 * Created by Duke on 2015/8/24.
 */
public class ImageCheckAdapter extends PagerAdapter{

    Context context;
    List<String> list;

    public ImageCheckAdapter(Context context, List<String> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.imagecheck_pager_layout, null);
//        final PhotoView photoView = new PhotoView(container.getContext());

        final ImageView photoView = (ImageView) view.findViewById(R.id.photo_view);
        final ImageView userphoto_view = (ImageView) view.findViewById(R.id.userphoto_view);

        final String url = list.get(position);
//        Log.e("TAG_图片",isUser+"=====imageInfo="+imageInfo.toString());
        boolean ifShowDelInfo = false;

            if (TextUtils.isEmpty(url)) {
                userphoto_view.setImageResource(R.mipmap.icon_default_image);
            } else {
                ImageUtils.setImage(userphoto_view,url);
                userphoto_view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((ImageCheckActivity)context).showDialog(url);
                        return false;
                    }
                });
            }
            userphoto_view.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.GONE);
            container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
