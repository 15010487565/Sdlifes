package com.sdlifes.sdlifes.dialog;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdlifes.sdlifes.R;

import java.util.List;

/**
 *
 * Created by Duke on 2016/3/10.
 */
public class UserInfoBottomOptionAdapter extends BaseQuickAdapter<String, BaseViewHolder>
         {

    public UserInfoBottomOptionAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String bean) {

        helper.setText(R.id.text,bean);

    }
}
