package com.sdlifes.sdlifes.adapter;


import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.SearchModel;
import com.sdlifes.sdlifes.util.ImageUtils;

import java.util.List;

/**
 * Created by gs on 21/11/2018.
 */

public class SearchAdapter extends BaseQuickAdapter<SearchModel.DataBean, BaseViewHolder> {

    public SearchAdapter(int layoutResId, @Nullable List<SearchModel.DataBean> data) {
        super(layoutResId,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, SearchModel.DataBean dataBean) {

        helper.setText(R.id.tv_title,dataBean.getTitle());

        ImageView imageView = helper.getView(R.id.iv_search);
        List<String> picList = dataBean.getPic();
        String pic = picList.get(0);
        if (TextUtils.isEmpty(pic)){
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.VISIBLE);
            ImageUtils.setImage(imageView, pic,0,R.mipmap.album_photo_default);
        }
        String content = dataBean.getContent();
        helper.setText(R.id.tv_search,Html.fromHtml(content));

    }
}

