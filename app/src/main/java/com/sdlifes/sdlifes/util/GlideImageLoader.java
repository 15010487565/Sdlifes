package com.sdlifes.sdlifes.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.sdlifes.sdlifes.activity.WebViewActivity;
import com.sdlifes.sdlifes.model.UserAdModel;
import com.to.aboomy.banner.HolderCreator;


public class GlideImageLoader implements HolderCreator {
    @Override
    public View createView(final Context context, final int index, Object o) {
        UserAdModel.DataBean dataBean = (UserAdModel.DataBean) o;

        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageUtils.setImage(iv,dataBean.getPic());
//        Glide.with(iv).load().into(iv);
        //内部实现点击事件
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("Url",dataBean.getUrl());
                intent.putExtra("AdId",String.valueOf(dataBean.getId()));
                context.startActivity(intent);
            }
        });
        return iv;
    }
}