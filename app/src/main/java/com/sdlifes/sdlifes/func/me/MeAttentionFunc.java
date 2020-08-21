package com.sdlifes.sdlifes.func.me;

import android.app.Activity;
import android.content.Intent;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.AttentionActivity;

import www.xcd.com.mylibrary.func.BaseFunc;

/**
 * Created by gs on 2020/8/10.
 * 关注
 */
public class MeAttentionFunc extends BaseFunc {

    public MeAttentionFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.me_attention;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.me_attention;
    }

    @Override
    public void onclick() {
        getActivity().startActivity(new Intent(getActivity(), AttentionActivity.class));
    }
}