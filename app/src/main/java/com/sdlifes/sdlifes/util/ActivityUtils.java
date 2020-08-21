package com.sdlifes.sdlifes.util;

import android.content.Context;
import android.content.Intent;

import com.sdlifes.sdlifes.activity.DetailsActivity;

/**
 * Created by gs on 2020/8/18.
 */
public class ActivityUtils {
    public static  void startDetailsActivity(Context context,String id,
                                             String title,String content,String focus){
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        intent.putExtra("focus",focus);
        context.startActivity(intent);
    }
}
