package com.sdlifes.sdlifes.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sdlifes.sdlifes.activity.DetailsActivity;
import com.sdlifes.sdlifes.activity.ImageCheckActivity;
import com.sdlifes.sdlifes.activity.WebViewActivity;
import com.sdlifes.sdlifes.activity.WebViewVideoActivity;

import java.util.ArrayList;

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

    /**
     *
     * @param context
     * @param url
     * @param AdId 广告id
     */
    public static void startWebViewActivity(Context context,String url, String AdId){
        startWebViewActivity(context,url,AdId,false);
    }
    public static void startWebViewActivity(Context context,String url,
                                             String adId,boolean isAgreement){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("Url", url);
        intent.putExtra("AdId",adId);
        intent.putExtra("isAgreement",isAgreement);
        context.startActivity(intent);
    }

    /**
     *
     * @param context
     * @param url 视频跳转URL
     * @param adId
     * @param ostate 是否跳转 0 不跳转 1跳转
     * @param ourl 详情跳转URL
     */
    public static void startWebViewVideoActivity(Context context,String url,String title,
                                            String adId,int ostate,String ourl
            ,String pic){
        Log.e("TAG_视频","Url="+url);
        Log.e("TAG_视频详情","ourl="+ourl);
        Intent intent = new Intent(context, WebViewVideoActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("Url", url);
        intent.putExtra("AdId",adId);
        intent.putExtra("ostate",ostate);
        intent.putExtra("ourl",ourl);
        intent.putExtra("pic",pic);
        context.startActivity(intent);
    }

    public static void startImageCheckActivity(Context mContext, int position, ArrayList<String> list){
            Intent intent = new Intent(mContext, ImageCheckActivity.class);
        intent.putExtra("position", position);
        intent.putStringArrayListExtra("listjson", list);
        mContext.startActivity(intent);
    }
}
