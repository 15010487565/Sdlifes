package com.sdlifes.sdlifes.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.UserAdModel;
import com.to.aboomy.banner.HolderCreator;


public class GlideImageLoader implements HolderCreator {
    @Override
    public View createView(final Context context, final int index, Object o) {
        UserAdModel.DataBean dataBean = (UserAdModel.DataBean) o;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_banner_image, null);
        TextView tv = view.findViewById(R.id.tv_item_banner);
        tv.setText(dataBean.getTitle());
        ImageView iv = view.findViewById(R.id.iv_item_banner);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageUtils.setImage(iv,dataBean.getPic());
//        Glide.with(iv).load().into(iv);
        //内部实现点击事件
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startWebViewActivity(context, dataBean.getUrl(),String.valueOf(dataBean.getId()));

//                Intent intent = new Intent(context, WebViewActivity.class);
//                intent.putExtra("Url",dataBean.getUrl());
//                intent.putExtra("AdId",String.valueOf(dataBean.getId()));
//                context.startActivity(intent);
            }
        });
        return view;
    }
}
