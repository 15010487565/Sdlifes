package com.sdlifes.sdlifes.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdlifes.sdlifes.R;

import java.util.List;

/**
 * Created by gs on 21/11/2018.
 */

public class SearchLikeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchLikeAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String dataBean) {

        helper.setText(R.id.tv_SearchLike,dataBean);

    }
}

